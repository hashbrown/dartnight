package com.myinterwebspot.app.dartnight.model;

import java.util.List;

import android.database.Cursor;

import com.myinterwebspot.app.dartnight.db.DBHelper;

public class Leaderboard {
	
	public static int MAX_LEADERS = 5;
	
	private DBHelper db;
	
	public Leaderboard(DBHelper dbHelper){
		this.db = dbHelper;
	}
	
	
	public List<Team> getTopScoreTeams(){		
		return db.getLeadingTeams(ContestantStatType.HIGH_SCORE_STAT, MAX_LEADERS);
	}
	
	public List<Team> getAverageScoreTeams(){
		return db.getLeadingTeams(ContestantStatType.AVG_SCORE_STAT, MAX_LEADERS);
	}
	
	public List<Team> getWinningTeams(){
		return db.getLeadingTeams(ContestantStatType.TOP_WINS_STAT, MAX_LEADERS);
	}
	
	public List<Player> getTotalScorePlayers(){
		return db.getLeadingPlayers(ContestantStatType.TOTAL_SCORE_STAT, MAX_LEADERS);
	}

	public List<Player> getHighScorePlayers(){
		return db.getLeadingPlayers(ContestantStatType.HIGH_SCORE_STAT, MAX_LEADERS);
	}

	public List<Player> getAverageScorePlayers(){
		return db.getLeadingPlayers(ContestantStatType.AVG_SCORE_STAT, MAX_LEADERS);
	}
	
	public List<Player> getWinningPlayers(){
		return db.getLeadingPlayers(ContestantStatType.TOP_WINS_STAT, MAX_LEADERS);
	}

}
