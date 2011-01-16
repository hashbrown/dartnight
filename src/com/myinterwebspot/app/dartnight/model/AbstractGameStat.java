package com.myinterwebspot.app.dartnight.model;

public abstract class AbstractGameStat {
	
	private String gameId;
	private boolean isWinner;
	private double mpr;
	
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
	public double getMpr() {
		return mpr;
	}
	public void setMpr(double mpr) {
		this.mpr = mpr;
	}

}
