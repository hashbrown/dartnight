/**
 * 
 */
package com.myinterwebspot.app.dartnight.db;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * @author hashbrown
 *
 */
public class TeamTable implements BaseColumns {
	
	// This class cannot be instantiated
	private TeamTable() {}

	public static final String TABLE_NAME = "Team";


	/**
	 * The default sort order for this table
	 */
	public static final String DEFAULT_SORT_ORDER = "";

	/**
	 * The player first name
	 * <P>Type: TEXT</P>
	 */
	public static final String NAME = "name";

	
	/**
	 * The timestamp for when the team was created
	 * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
	 */
	public static final String CREATED_DATE = "creation_date";

	/**
	 * The timestamp for when the team was last modified
	 * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
	 */
	public static final String MODIFIED_DATE = "modification_date";

	public static void create(SQLiteDatabase db){
		Log.i("DBHelper", "CREATING TEAM TABLE");
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ TeamTable._ID + " INTEGER PRIMARY KEY, "
				+ TeamTable.NAME + " TEXT, "
				+ TeamTable.CREATED_DATE + " INTEGER, "
				+ TeamTable.MODIFIED_DATE + " INTEGER);");
	}

	public static void drop(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}

}
