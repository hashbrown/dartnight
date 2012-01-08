package com.myinterwebspot.app.dartnight.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.myinterwebspot.app.dartnight.db.DBHelper;
import com.myinterwebspot.app.dartnight.db.GameTable;
import com.myinterwebspot.app.dartnight.model.GameState;

public class GameProvider extends ContentProvider {

	/**
	 * A UriMatcher instance
	 */
	private static final UriMatcher sUriMatcher;
	/**
	 * maps columns of contract api to actual db implementation
	 */
	private static final HashMap<String, String> sProjectionMap;

	/*
	 * Constants used by the Uri matcher to choose an action based on the pattern
	 * of the incoming URI
	 */
	// The incoming URI matches the Games URI pattern
	private static final int GAMES = 1;

	// The incoming URI matches the Game ID URI pattern
	private static final int GAME_ID = 2;

	private DBHelper mDBHelper;

	static {
		/*
		 * Creates and initializes the URI matcher
		 */
		// Create a new instance
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		// Add a pattern that routes URIs terminated with "games" to a GAMES operation
		sUriMatcher.addURI(DataContract.Games.CONTENT_URI.getAuthority(), DataContract.Games.CONTENT_URI.getPath(), GAMES);

		// Add a pattern that routes URIs terminated with "notes" plus an integer
		// to a note ID operation
		sUriMatcher.addURI(DataContract.Games.CONTENT_URI.getAuthority(), DataContract.Games.CONTENT_URI.getPath() + "/#", GAME_ID);


		/*
		 * Creates and initializes a projection map that returns all columns
		 */

		// Creates a new projection map instance. The map returns a column name
		// given a string. The two are usually equal.
		sProjectionMap = new HashMap<String, String>();
		sProjectionMap.put(DataContract.Games._ID, GameTable._ID);
		sProjectionMap.put(DataContract.Games.NAME, GameTable.NAME);
		sProjectionMap.put(DataContract.Games.PARENT, GameTable.PARENT);
		sProjectionMap.put(DataContract.Games.STATE, GameTable.STATE);
		sProjectionMap.put(DataContract.Games.CREATED_DATE, GameTable.CREATED_DATE);
		sProjectionMap.put(DataContract.Games.MODIFIED_DATE, GameTable.MODIFIED_DATE);
	}

	@Override
	public boolean onCreate() {
		mDBHelper = new DBHelper(getContext());
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Opens the database object in "write" mode.
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		int count;
		String finalWhere;

		// Does the update based on the incoming URI pattern
		switch (sUriMatcher.match(uri)) {

		// If the incoming URI matches the general games pattern, does the update based on
		// the incoming data.
		case GAMES:

			// Does the update and returns the number of rows updated.
			count = db.delete(GameTable.TABLE_NAME, selection, selectionArgs);
			break;

			// If the incoming URI matches a single game ID, does the update based on the incoming
			// data, but modifies the where clause to restrict it to the particular game ID.
		case GAME_ID:
			// From the incoming URI, get the note ID
			String noteId = uri.getPathSegments().get(1);

			finalWhere = GameTable._ID + " = " + noteId;

			// If there were additional selection criteria, append them to the final WHERE
			// clause
			if (selection !=null) {
				finalWhere = finalWhere + " AND " + selection;
			}


			// Does the update and returns the number of rows updated.
			count = db.delete(GameTable.TABLE_NAME, finalWhere, selectionArgs);
			break;
			// If the incoming pattern is invalid, throws an exception.
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		/*Gets a handle to the content resolver object for the current context, and notifies it
		 * that the incoming URI changed. The object passes this along to the resolver framework,
		 * and observers that have registered themselves for the provider are notified.
		 */
		getContext().getContentResolver().notifyChange(uri, null);

		// Returns the number of rows updated.
		return count;
	}


	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// Validates the incoming URI. Only the full provider URI is allowed for inserts.
		if (sUriMatcher.match(uri) != GAMES) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// A map to hold the new record's values.
		ContentValues values;

		// If the incoming values map is not null, uses it for the new values.
		if (initialValues != null) {
			values = new ContentValues(initialValues);

		} else {
			// Otherwise, create a new value map
			values = new ContentValues();
		}

		if(!values.containsKey(GameTable.STATE)){
			values.put(GameTable.STATE,GameState.NEW.toString());
		}

		values.put(GameTable.CREATED_DATE, Long.valueOf(System.currentTimeMillis()));
		values.put(GameTable.MODIFIED_DATE, Long.valueOf(System.currentTimeMillis()));

		Log.d("DBHelper", "Inserting new Game");
		long rowId = mDBHelper.getWritableDatabase().insert(GameTable.TABLE_NAME, null, values);
		// If the insert succeeded, the row ID exists.
		if (rowId > 0) {
			// Creates a URI with the note ID pattern and the new row ID appended to it.
			Uri noteUri = ContentUris.withAppendedId(DataContract.Games.CONTENT_ID_URI_BASE, rowId);

			// Notifies observers registered against this provider that the data changed.
			getContext().getContentResolver().notifyChange(noteUri, null);
			Log.d("DBHelper","Inserted Game[" + rowId + "]");
			return noteUri;
		}

		// If the insert didn't succeed, then the rowID is <= 0. Throws an exception.
		throw new SQLException("Failed to insert row into " + uri);


	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// Constructs a new query builder and sets its table name
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(GameTable.TABLE_NAME);

		switch (sUriMatcher.match(uri)) {

		// If the pattern is for all games, returns the general content type.
		case GAMES:
			qb.setProjectionMap(sProjectionMap);
			break;

			// If the pattern is for a specific game, returns the game ID content type.
		case GAME_ID:
			qb.setProjectionMap(sProjectionMap);
			qb.appendWhere(GameTable._ID + uri.getPathSegments().get(1));
			break;

			// If the URI pattern doesn't match any permitted patterns, throws an exception.
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		String orderBy;
		// If no sort order is specified, uses the default
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = GameTable.DEFAULT_SORT_ORDER;
		} else {
			// otherwise, uses the incoming sort order
			orderBy = sortOrder;
		}

		Cursor c = qb.query(mDBHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, orderBy);

		// Tells the Cursor what URI to watch, so it knows when its source data changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;

	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// Opens the database object in "write" mode.
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		int count;
		String finalWhere;

		// Does the update based on the incoming URI pattern
		switch (sUriMatcher.match(uri)) {

		// If the incoming URI matches the general games pattern, does the update based on
		// the incoming data.
		case GAMES:

			// Does the update and returns the number of rows updated.
			count = db.update(GameTable.TABLE_NAME, values, selection, selectionArgs);
			break;

			// If the incoming URI matches a single game ID, does the update based on the incoming
			// data, but modifies the where clause to restrict it to the particular game ID.
		case GAME_ID:
			// From the incoming URI, get the note ID
			String noteId = uri.getPathSegments().get(1);

			finalWhere = GameTable._ID + " = " + noteId;

			// If there were additional selection criteria, append them to the final WHERE
			// clause
			if (selection !=null) {
				finalWhere = finalWhere + " AND " + selection;
			}


			// Does the update and returns the number of rows updated.
			count = db.update(GameTable.TABLE_NAME, values, finalWhere, selectionArgs);
			break;
			// If the incoming pattern is invalid, throws an exception.
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		/*Gets a handle to the content resolver object for the current context, and notifies it
		 * that the incoming URI changed. The object passes this along to the resolver framework,
		 * and observers that have registered themselves for the provider are notified.
		 */
		getContext().getContentResolver().notifyChange(uri, null);

		// Returns the number of rows updated.
		return count;
	}

	@Override
	public String getType(Uri uri) {

		/**
		 * Chooses the MIME type based on the incoming URI pattern
		 */
		switch (sUriMatcher.match(uri)) {

		// If the pattern is for notes or live folders, returns the general content type.
		case GAMES:
			return GameTable.CONTENT_TYPE;

			// If the pattern is for note IDs, returns the note ID content type.
		case GAME_ID:
			return GameTable.CONTENT_ITEM_TYPE;

			// If the URI pattern doesn't match any permitted patterns, throws an exception.
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}
}
