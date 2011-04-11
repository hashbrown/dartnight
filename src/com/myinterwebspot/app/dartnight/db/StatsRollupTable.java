package com.myinterwebspot.app.dartnight.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StatsRollupTable{
	
	// This class cannot be instantiated
	private StatsRollupTable() {}

	public static final String TABLE_NAME = "StatsRollup";

	public static final String ID            = "_id";
	public static final String TYPE          = "type";
	public static final String HIGH_SCORE    = "high_score";
	public static final String AVG_SCORE     = "avg_score";
	public static final String TOTAL_SCORE   = "total_score";
	public static final String WINS          = "wins";
	public static final String LOSSES        = "losses";

	public static void create(SQLiteDatabase db){
		Log.i("DBHelper", "CREATING STATSROLLUP TABLE");
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ StatsRollupTable.ID + " INTEGER, "
				+ StatsRollupTable.TYPE + " STRING, "
				+ StatsRollupTable.HIGH_SCORE + " REAL, "
				+ StatsRollupTable.AVG_SCORE + " REAL, "
				+ StatsRollupTable.TOTAL_SCORE + " REAL, "
				+ StatsRollupTable.WINS + " INTEGER , "
				+ StatsRollupTable.LOSSES + " INTEGER, "
				+ " PRIMARY KEY(" +  StatsRollupTable.ID + "," +  StatsRollupTable.TYPE +"));");
	}

	public static void drop(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}

}