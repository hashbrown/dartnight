package com.myinterwebspot.app.dartnight.model;

public class TeamGameStat extends AbstractGameStat {
	
	private Team team;
	
	public TeamGameStat(Team team, Game game){
		this.team = team;
		setGameId(game.getId());
	}
	
	public TeamGameStat(Team team){
		this.team = team;;
	}
	
	public Team getTeam(){
		return this.team;
	}

}
