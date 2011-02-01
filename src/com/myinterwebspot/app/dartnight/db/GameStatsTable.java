package com.myinterwebspot.app.dartnight.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GameStatsTable{
	
	// This class cannot be instantiated
	private GameStatsTable() {}

	public static final String TABLE_NAME = "GameStats";

	public static final String GAME_ID = "game_id";
	public static final String CONTESTANT_ID = "contestant_id";
	public static final String CONTESTANT_TYPE = "type";
	public static final String SCORE   = "score";
	public static final String WINNER  = "winner_flag";

	public static void create(SQLiteDatabase db){
		Log.i("DBHelper", "CREATING TEAMSTATS TABLE");
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ GameStatsTable.GAME_ID + " INTEGER, "
				+ GameStatsTable.CONTESTANT_ID + " INTEGER, "
				+ GameStatsTable.CONTESTANT_TYPE + " STRING, "
				+ GameStatsTable.SCORE + " REAL, "
				+ GameStatsTable.WINNER + " INTEGER );");
	}

	public static void drop(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}

}