package com.myinterwebspot.app.dartnight.model;

public class ContestantStats{
		
	private int wins = 0;
	private int losses = 0;
	private float highScore = 0.0f;
	private float avgScore = 0.0f;
	private float totalScore = 0.0f;
	
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
	public float getHighScore() {
		return highScore;
	}
	public void setHighScore(float highScore) {
		this.highScore = highScore;
	}
	public float getAvgScore() {
		return avgScore;
	}
	public void setAvgScore(float avgScore) {
		this.avgScore = avgScore;
	}
	public float getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}
	
}
