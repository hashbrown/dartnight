package com.myinterwebspot.app.dartnight.model;

public class TeamStats extends ContestantStats {
	
	private String teamId;
	
	public TeamStats(Team team){
		this.teamId = team.getId();
	}
	
	public TeamStats(String teamId){
		this.teamId = teamId;
	}
	
	public String getTeamId(){
		return this.teamId;
	}

}
