package com.myinterwebspot.app.dartnight.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TeamGameStatsTable{
	
	// This class cannot be instantiated
	private TeamGameStatsTable() {}

	public static final String TABLE_NAME = "TeamGameStats";

	public static final String GAME_ID = "game_id";
	public static final String TEAM_ID = "team_id";
	public static final String SCORE   = "score";
	public static final String WINNER  = "winner_flag";

	public static void create(SQLiteDatabase db){
		Log.i("DBHelper", "CREATING TEAMSTATS TABLE");
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ TeamGameStatsTable.GAME_ID + " INTEGER, "
				+ TeamGameStatsTable.TEAM_ID + " INTEGER, "
				+ TeamGameStatsTable.SCORE + " REAL, "
				+ TeamGameStatsTable.WINNER + " INTEGER );");
	}

	public static void drop(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}

}