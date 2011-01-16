/**
 * 
 */
package com.myinterwebspot.app.dartnight.db;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public final class PlayerTable implements BaseColumns {

	
	// This class cannot be instantiated
	private PlayerTable() {}

	public static final String TABLE_NAME = "Player";


	/**
	 * The default sort order for this table
	 */
	public static final String DEFAULT_SORT_ORDER = "first_name asc";

	/**
	 * The player first name
	 * <P>Type: TEXT</P>
	 */
	public static final String FIRST_NAME = "first_name";

	/**
	 * The player last name
	 * <P>Type: TEXT</P>
	 */
	public static final String LAST_NAME = "last_name";

	/**
	 * The player nick name
	 * <P>Type: TEXT</P>
	 */
	public static final String SHORT_NAME = "short_name";

	/**
	 * The timestamp for when the player was created
	 * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
	 */
	public static final String CREATED_DATE = "creation_date";

	/**
	 * The timestamp for when the player was last modified
	 * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
	 */
	public static final String MODIFIED_DATE = "modification_date";

	public static void create(SQLiteDatabase db){
		Log.i("DBHelper", "CREATING PLAYER TABLE");
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ PlayerTable._ID + " INTEGER PRIMARY KEY, "
				+ PlayerTable.FIRST_NAME + " TEXT, "
				+ PlayerTable.LAST_NAME + " TEXT, "
				+ PlayerTable.SHORT_NAME + " TEXT, "
				+ PlayerTable.CREATED_DATE + " INTEGER, "
				+ PlayerTable.MODIFIED_DATE + " INTEGER);");
	}

	public static void drop(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}
}