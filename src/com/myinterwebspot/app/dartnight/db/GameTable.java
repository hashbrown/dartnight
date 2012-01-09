/**
 * 
 */
package com.myinterwebspot.app.dartnight.db;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public final class GameTable implements BaseColumns {
	
	/**
     * The MIME type of {@link #CONTENT_URI}.
     */
    public static final String CONTENT_TYPE
            = "vnd.android.cursor.dir/vnd.myinterwebspot.dartnight.game";

    /**
     * The MIME type of a {@link #CONTENT_URI} sub-directory of a single row.
     */
    public static final String CONTENT_ITEM_TYPE
            = "vnd.android.cursor.item/vnd.myinterwebspot.dartnight.game";

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
	 * Indicates that this was a rematch of the parent game
	 * <P>Type: INTEGER</P>
	 */
	public static final String PARENT = "parent_game";

	
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
				+ GameTable.PARENT + " INTEGER, "
				+ GameTable.CREATED_DATE + " INTEGER, "
				+ GameTable.MODIFIED_DATE + " INTEGER);");
	}

	public static void drop(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}
}