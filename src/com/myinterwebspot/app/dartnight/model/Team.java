package com.myinterwebspot.app.dartnight.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Team implements Contestant{
	
	private String id;
	private String name;
	private Set<Player> players = new HashSet<Player>();
	private Map<String,GameStat> gameStatMap = new HashMap<String,GameStat>();
	private ContestantStats stats;

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Player> getPlayers(){
		return players;
	
	}

	public void addPlayer(Player player) {
		this.players.add(player);
	}
	
	public GameStat getGameStats(Game game){
		return this.gameStatMap.get(game.getId());
	}
	
	public void addGameStat(GameStat stat){
		this.gameStatMap.put(stat.getGameId(), stat);
	}
	
	public ContestantStats getContestantStats(){
		return this.stats;
	}

	public void setContestantStats(ContestantStats stat) {
		this.stats = stat;
	}


}
