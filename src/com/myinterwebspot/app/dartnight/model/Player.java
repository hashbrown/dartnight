package com.myinterwebspot.app.dartnight.model;

import java.util.HashMap;
import java.util.Map;

public class Player implements Contestant{
	
	private String id;
	private String firstName;
	private String lastName;
	private String nickName;
	private Map<String,PlayerGameStat> gameStatMap = new HashMap<String,PlayerGameStat>();
	private ContestantStats stats;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getNickName() {
		if(nickName == null){
			return firstName;
		}
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public PlayerGameStat getGameStats(Game game){
		return this.gameStatMap.get(game.getId());
	}
	
	public void addGameStat(PlayerGameStat stat){
		this.gameStatMap.put(stat.getGameId(), stat);
	}
	
	public ContestantStats getTeamStats(){
		return this.stats;
	}

	public void setStats(ContestantStats stat) {
		this.stats = stat;
	}

}
