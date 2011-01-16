/**
 * 
 */
package com.myinterwebspot.app.dartnight.db;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public final class GameTable implements BaseColumns {

	
	// This class cannot be instantiated
	private GameTable() {}

	public static final String TABLE_NAME = "Game";


	/**
	 * The default sort order for this table
	 */
	public static final String DEFAULT_SORT_ORDER = "modification_date desc";

	/**
	 * The game name
	 * <P>Type: TEXT</P>
	 */
	public static final String NAME = "name";
	
	/**
	 * The game state
	 * NEW, IN_PROGRESS, COMPLETE
	 * <P>Type: TEXT</P>
	 */
	public static final String STATE = "state";

	
	/**
	 * The timestamp for when the game was created
	 * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
	 */
	public static final String CREATED_DATE = "creation_date";

	/**
	 * The timestamp for when the game was last modified
	 * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
	 */
	public static final String MODIFIED_DATE = "modification_date";

	public static void create(SQLiteDatabase db){
		Log.i("DBHelper", "CREATING GAME TABLE");
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ GameTable._ID + " INTEGER PRIMARY KEY, "
				+ GameTable.NAME + " TEXT, "
				+ GameTable.STATE + " TEXT, "
				+ GameTable.CREATED_DATE + " INTEGER, "
				+ GameTable.MODIFIED_DATE + " INTEGER);");
	}

	public static void drop(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}
}