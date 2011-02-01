package com.myinterwebspot.app.dartnight.db;

import android.database.sqlite.SQLiteDatabase;

public interface DBUpgradeStrategy {
	
	public void upgrade(SQLiteDatabase db);

}
