package com.myinterwebspot.app.dartnight.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TeamStatsTable{
	
	// This class cannot be instantiated
	private TeamStatsTable() {}

	public static final String TABLE_NAME = "TeamStats";

	public static final String GAME_ID = "game_id";
	public static final String TEAM_ID = "team_id";
	public static final String MPR 	   = "mpr";
	public static final String WINNER  = "winner_flag";

	public static void create(SQLiteDatabase db){
		Log.i("DBHelper", "CREATING TEAMSTATS TABLE");
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ TeamStatsTable.GAME_ID + " INTEGER, "
				+ TeamStatsTable.TEAM_ID + " INTEGER, "
				+ TeamStatsTable.MPR + " REAL, "
				+ TeamStatsTable.WINNER + " INTEGER );");
	}

	public static void drop(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}

}