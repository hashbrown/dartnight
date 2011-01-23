package com.myinterwebspot.app.dartnight.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PlayerGameStatsTable{
	
	// This class cannot be instantiated
	private PlayerGameStatsTable() {}

	public static final String TABLE_NAME = "PlayerGameStats";

	public static final String PLAYER_ID = "player_id";
	public static final String GAME_ID   = "game_id";
	public static final String SCORE     = "score";
	public static final String WINNER    = "winner_flag";

	public static void create(SQLiteDatabase db){
		Log.i("DBHelper", "CREATING TEAMSTATS TABLE");
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ PlayerGameStatsTable.PLAYER_ID + " INTEGER, "
				+ PlayerGameStatsTable.GAME_ID + " INTEGER, "
				+ PlayerGameStatsTable.SCORE + " REAL, "
				+ PlayerGameStatsTable.WINNER + " INTEGER );");
	}

	public static void drop(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}

}