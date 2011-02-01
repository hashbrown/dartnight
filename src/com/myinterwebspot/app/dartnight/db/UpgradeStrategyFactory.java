package com.myinterwebspot.app.dartnight.db;

public class UpgradeStrategyFactory {
	
	private DBUpgradeStrategy[] strategies = new DBUpgradeStrategy[]{new UpgradeDBToVersion2()};
	
	public DBUpgradeStrategy getUpgradeStrategy(int oldVersion, int newVersion){
		
		CompositeDBUpgradeStrategy director = new CompositeDBUpgradeStrategy();
		for (int i = oldVersion -1; i < newVersion -1; i++) {
			director.addStrategy(strategies[i]);
		}
		
		return director;
	}

}
