package com.myinterwebspot.app.dartnight.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PlayerStatsTable{
	
	// This class cannot be instantiated
	private PlayerStatsTable() {}

	public static final String TABLE_NAME = "PlayerStats";

	public static final String PLAYER_ID = "player_id";
	public static final String GAME_ID = "game_id";
	public static final String MPR 	   = "mpr";
	public static final String WINNER  = "winner_flag";

	public static void create(SQLiteDatabase db){
		Log.i("DBHelper", "CREATING TEAMSTATS TABLE");
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ PlayerStatsTable.PLAYER_ID + " INTEGER, "
				+ PlayerStatsTable.GAME_ID + " INTEGER, "
				+ PlayerStatsTable.MPR + " REAL, "
				+ PlayerStatsTable.WINNER + " INTEGER );");
	}

	public static void drop(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}

}