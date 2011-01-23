package com.myinterwebspot.app.dartnight.model;

public class PlayerGameStat extends AbstractGameStat {
	
	private String playerId;
	
	public PlayerGameStat(Player player){
		this.playerId = player.getId();
	}
	
	public String getPlayerId(){
		return this.playerId;
	}


}
