package com.myinterwebspot.app.dartnight.model;

public class GameStat {
	
	private String gameId;
	private Contestant contestant;
	private boolean isWinner;
	private float score;
	
	public GameStat(Contestant contestant, Game game){
		this.contestant = contestant;
		setGameId(game.getId());
	}
	
	public GameStat(Contestant contestant){
		this.contestant = contestant;
	}

	public Contestant getContestant(){
		return this.contestant;
	}
	
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	
	
	public boolean isWinner() {
		return isWinner;
	}
	public void setWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}

}
