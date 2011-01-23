package com.myinterwebspot.app.dartnight.model;


public class PlayerStats extends ContestantStats {
	
	private String playerId;
	
	public PlayerStats(Player player){
		this.playerId = player.getId();
	}
	
	public String getPlayerId(){
		return this.playerId;
	}

}
