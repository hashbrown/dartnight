package com.myinterwebspot.app.dartnight.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LeaderboardTable{
	
	// This class cannot be instantiated
	private LeaderboardTable() {}

	public static final String TABLE_NAME = "Leaderboard";

	public static final String LEADER_ID   = "leader_id";
	public static final String LEADER_TYPE = "leader_type";
	public static final String STAT_TYPE   = "stat_type";
	public static final String RANK        = "rank";
	
	/**
	 * The default sort order for this table
	 */
	public static final String DEFAULT_SORT_ORDER = "rank asc";
	
	public static void create(SQLiteDatabase db){
		Log.i("DBHelper", "CREATING LEADERBOARD TABLE");
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ LeaderboardTable.LEADER_ID + " INTEGER, "
				+ LeaderboardTable.LEADER_TYPE + " STRING, "
				+ LeaderboardTable.STAT_TYPE + " STRING, "
				+ LeaderboardTable.RANK + " INTEGER );");
	}

	public static void drop(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}

}