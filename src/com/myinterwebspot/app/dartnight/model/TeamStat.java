package com.myinterwebspot.app.dartnight.model;

public class TeamStat extends AbstractGameStat {
	
	private String teamId;
	
	public TeamStat(Team team, Game game){
		this.teamId = team.getId();
		setGameId(game.getId());
	}
	
	public TeamStat(String teamId){
		this.teamId = teamId;
	}
	
	public String getTeamId(){
		return this.teamId;
	}

}
