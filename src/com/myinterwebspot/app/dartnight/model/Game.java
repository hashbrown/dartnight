package com.myinterwebspot.app.dartnight.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Game {
	
	private String id;
	private String name;
	private GameState state;
	private Game parent;
	private Date creationDate;
	private Date modificationDate;
	private List<Team> teams = new ArrayList<Team>();
	
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

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public void setParent(Game parent) {
		this.parent = parent;
	}

	public Game getParent() {
		return parent;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	public Date getModificationDate() {
		return modificationDate;
	}
	
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}
	
	public List<Team> getTeams(){
		return teams;
	
	}
	
	public void addTeam(Team team){
		this.teams.add(team);
	}

}
