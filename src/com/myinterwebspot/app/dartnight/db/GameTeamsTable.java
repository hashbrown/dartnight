package com.myinterwebspot.app.dartnight.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GameTeamsTable{
	
	// This class cannot be instantiated
	private GameTeamsTable() {}

	public static final String TABLE_NAME = "GameTeams";

	public static final String GAME_ID = "game_id";

	public static final String TEAM_ID = "team_id";

	public static void create(SQLiteDatabase db){
		Log.i("DBHelper", "CREATING GAMETEAMS TABLE");
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ GameTeamsTable.GAME_ID + " INTEGER, "
				+ GameTeamsTable.TEAM_ID + " INTEGER);");
	}

	public static void drop(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}

}
