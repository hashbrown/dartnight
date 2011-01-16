package com.myinterwebspot.app.dartnight.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Team {
	
	private String id;
	private String name;
	private Set<Player> players = new HashSet<Player>();
	private Map<String,TeamStat> statMap = new HashMap<String,TeamStat>();

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
	
	public TeamStat getGameStats(Game game){
		return this.statMap.get(game.getId());
	}
	
	public void addGameStat(TeamStat stat){
		this.statMap.put(stat.getGameId(), stat);
	}


}
