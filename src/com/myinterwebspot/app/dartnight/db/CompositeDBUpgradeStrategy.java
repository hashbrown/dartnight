package com.myinterwebspot.app.dartnight.db;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

public class CompositeDBUpgradeStrategy implements DBUpgradeStrategy {

	List<DBUpgradeStrategy> strategies = new ArrayList<DBUpgradeStrategy>();
	
	public void upgrade(SQLiteDatabase db) {
		for (DBUpgradeStrategy strategy : this.strategies) {
			strategy.upgrade(db);
		}

	}
	
	public void addStrategy(DBUpgradeStrategy strategy){
		this.strategies.add(strategy);
	}

}
