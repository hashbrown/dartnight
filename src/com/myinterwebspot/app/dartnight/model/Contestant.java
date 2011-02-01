package com.myinterwebspot.app.dartnight.model;

public interface Contestant {
	
	public String getId();
	
	public void setId(String id);
	
	public GameStat getGameStats(Game game);
	
	public void addGameStat(GameStat stat);
	
	public ContestantStats getContestantStats();

	public void setContestantStats(ContestantStats stat);
	
}
