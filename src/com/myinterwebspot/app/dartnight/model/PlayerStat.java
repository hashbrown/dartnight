package com.myinterwebspot.app.dartnight.model;

public class PlayerStat extends AbstractGameStat {
	
	private String playerId;
	
	public PlayerStat(Player player){
		this.playerId = player.getId();
	}
	
	public String getPlayerId(){
		return this.playerId;
	}


}
