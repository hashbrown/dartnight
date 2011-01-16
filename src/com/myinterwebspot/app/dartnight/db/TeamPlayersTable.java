package com.myinterwebspot.app.dartnight.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TeamPlayersTable{
	
	// This class cannot be instantiated
	private TeamPlayersTable() {}

	public static final String TABLE_NAME = "TeamPlayers";

	public static final String TEAM_ID = "team_id";

	public static final String PLAYER_ID = "player_id";

	public static void create(SQLiteDatabase db){
		Log.i("DBHelper", "CREATING GAMETEAMS TABLE");
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ TeamPlayersTable.TEAM_ID + " INTEGER, "
				+ TeamPlayersTable.PLAYER_ID + " INTEGER);");
	}

	public static void drop(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}

}