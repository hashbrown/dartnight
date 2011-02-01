package com.myinterwebspot.app.dartnight.db;

import android.database.sqlite.SQLiteDatabase;

public class UpgradeDBToVersion2 implements DBUpgradeStrategy {
	
	
	public void upgrade(SQLiteDatabase db) {
		StringBuffer ddlBuilder = new StringBuffer();
		ddlBuilder.append("ALTER TABLE ");
		ddlBuilder.append(GameTable.TABLE_NAME);
		ddlBuilder.append(" ADD COLUMN ");
		ddlBuilder.append(GameTable.PARENT);
		ddlBuilder.append(" INTEGER");
		
		db.execSQL(ddlBuilder.toString());
	}

}
