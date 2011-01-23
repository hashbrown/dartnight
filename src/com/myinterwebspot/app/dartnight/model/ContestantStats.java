package com.myinterwebspot.app.dartnight.model;

public class ContestantStats{
	
	
	
	private int wins = 0;
	private int losses = 0;
	private double highScore = 0.0;
	private double avgScore = 0.0;
	
	public int getWins() {
		return wins;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public int getLosses() {
		return losses;
	}
	public void setLosses(int losses) {
		this.losses = losses;
	}
	public double getHighScore() {
		return highScore;
	}
	public void setHighScore(double highScore) {
		this.highScore = highScore;
	}
	public double getAvgScore() {
		return avgScore;
	}
	public void setAvgScore(double avgScore) {
		this.avgScore = avgScore;
	}
	
}
