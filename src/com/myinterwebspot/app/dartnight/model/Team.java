package com.myinterwebspot.app.dartnight.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Team implements Contestant{
	
	private String id;
	private String name;
	private Set<Player> players = new HashSet<Player>();
	private Map<String,TeamGameStat> gameStatMap = new HashMap<String,TeamGameStat>();
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
	
	public TeamGameStat getGameStats(Game game){
		return this.gameStatMap.get(game.getId());
	}
	
	public void addGameStat(TeamGameStat stat){
		this.gameStatMap.put(stat.getGameId(), stat);
	}
	
	public ContestantStats getTeamStats(){
		return this.stats;
	}

	public void setStats(ContestantStats stat) {
		this.stats = stat;
	}


}
