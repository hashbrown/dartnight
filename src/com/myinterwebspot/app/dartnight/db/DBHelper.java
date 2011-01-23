package com.myinterwebspot.app.dartnight.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.myinterwebspot.app.dartnight.model.Contestant;
import com.myinterwebspot.app.dartnight.model.ContestantStatType;
import com.myinterwebspot.app.dartnight.model.ContestantStats;
import com.myinterwebspot.app.dartnight.model.Game;
import com.myinterwebspot.app.dartnight.model.GameState;
import com.myinterwebspot.app.dartnight.model.Player;
import com.myinterwebspot.app.dartnight.model.PlayerGameStat;
import com.myinterwebspot.app.dartnight.model.Team;
import com.myinterwebspot.app.dartnight.model.TeamGameStat;


public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "DartsDB";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		GameTable.create(db);
		GameTeamsTable.create(db);
		TeamTable.create(db);
		TeamPlayersTable.create(db);
		PlayerTable.create(db);
		TeamGameStatsTable.create(db);
		PlayerGameStatsTable.create(db);
		StatsRollupTable.create(db);
		LeaderboardTable.create(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("LOG", "Upgrading database from version " + oldVersion + " to " +
				newVersion + ", which will destroy all old data");

		GameTable.drop(db);
		GameTeamsTable.drop(db);
		TeamTable.drop(db);
		TeamPlayersTable.drop(db);
		PlayerTable.drop(db);
		TeamGameStatsTable.drop(db);
		PlayerGameStatsTable.drop(db);
		StatsRollupTable.drop(db);
		LeaderboardTable.drop(db);
		onCreate(db);
	}

	public Cursor getGames(){
		return getReadableDatabase().query(GameTable.TABLE_NAME, null, null, null, null, null, GameTable.DEFAULT_SORT_ORDER);
	}

	public Game getGame(String gameId) {

		if(gameId == null || gameId.length() == 0){
			return null;
		}

		Game game = null;

		Cursor gameCurs = 
			getReadableDatabase().query(GameTable.TABLE_NAME, null, GameTable._ID + "=" + gameId, null, null, null, null);

		if(gameCurs.moveToFirst()){
			game = new Game();
			game.setId(gameCurs.getString(0));
			game.setName(gameCurs.getString(1));
			game.setState(GameState.valueOf(gameCurs.getString(2)));
			game.setCreationDate(new Date(gameCurs.getLong(3)));
			game.setModificationDate(new Date(gameCurs.getLong(4)));

			loadTeams(game);
		}

		gameCurs.close();

		return game;
	}

	public void saveGame(Game game) {
		ContentValues values = new ContentValues();
		values.put(GameTable.STATE, game.getState().toString());
		Long now = Long.valueOf(System.currentTimeMillis());
		values.put(GameTable.MODIFIED_DATE, now);

		if(game.getId() == null){
			values.put(GameTable.NAME, game.getName());
			values.put(GameTable.CREATED_DATE, now);
			long gameId = insertGame(values);
			game.setId(String.valueOf(gameId));
		} else {
			updateGame(game.getId(), values);
		}

		for(Team team:game.getTeams()){
			saveTeam(team);
		}

		saveGameTeams(game.getId(), game.getTeams());

	}

	public void saveTeam(Team team) {
		ContentValues values = new ContentValues();
		Long now = Long.valueOf(System.currentTimeMillis());
		values.put(TeamTable.MODIFIED_DATE, now);
		values.put(TeamTable.NAME, team.getName());

		if(team.getId() == null){
			values.put(TeamTable.CREATED_DATE, now);
			long teamId = insertTeam(values);
			team.setId(String.valueOf(teamId));
		} else {
			updateTeam(team.getId(), values);
		}

		saveTeamPlayers(team.getId(), team.getPlayers());

	}

	private void saveTeamPlayers(String teamId, Set<Player> players) {

		deleteTeamPlayers(teamId);

		ContentValues values = new ContentValues();
		values.put(TeamPlayersTable.TEAM_ID, teamId);

		for(Player player:players){
			values.put(TeamPlayersTable.PLAYER_ID, player.getId());
			insertTeamPlayers(values);
		}

	}

	/**
	 * deletes any existing records and re-inserts
	 * @param game
	 */
	private void saveGameTeams(String gameId, List<Team> teams) {

		deleteGameTeams(gameId);

		ContentValues values = new ContentValues();
		values.put(GameTeamsTable.GAME_ID, gameId);

		for(Team team:teams){
			values.put(GameTeamsTable.TEAM_ID, team.getId());
			insertGameTeams(values);
		}

	}

	public Cursor getPlayers(){
		return getReadableDatabase().query(PlayerTable.TABLE_NAME, null, null, null, null, null, PlayerTable.DEFAULT_SORT_ORDER);
	}


	public Player getPlayer(String playerId){

		if(playerId == null || playerId.length() == 0){
			return null;
		}

		Player player = null;

		Cursor playerCurs = 
			getReadableDatabase().query(PlayerTable.TABLE_NAME, null, PlayerTable._ID + "=" + playerId, null, null, null, null);

		if(playerCurs.moveToFirst()){
			player = new Player();
			player.setId(playerCurs.getString(0));
			player.setFirstName(playerCurs.getString(1));
			player.setLastName(playerCurs.getString(2));
			player.setNickName(playerCurs.getString(3));

		}

		playerCurs.close();
		return player;

	}

	public void savePlayer(Player player){

		ContentValues values = new ContentValues();
		values.put(PlayerTable.FIRST_NAME, player.getFirstName());
		values.put(PlayerTable.LAST_NAME, player.getLastName());
		values.put(PlayerTable.SHORT_NAME, player.getNickName());

		Long now = Long.valueOf(System.currentTimeMillis());
		values.put(PlayerTable.MODIFIED_DATE, now);

		if(player.getId() == null){
			values.put(PlayerTable.CREATED_DATE, now);
			long id = insertPlayer(values);
			player.setId(String.valueOf(id));
		} else {
			updatePlayer(player.getId(), values);
		}
	}

	public void deletePlayer(String playerId){
		getWritableDatabase().delete(PlayerTable.TABLE_NAME, PlayerTable._ID + "=" + playerId, null);
	}


	private void updatePlayer(String playerId, ContentValues values) {
		Log.d("DBHelper", "Updating player[" + playerId + "]");
		getWritableDatabase().update(PlayerTable.TABLE_NAME, values, PlayerTable._ID + "=" + playerId, null);
		Log.d("DBHelper", "Updated player[" + playerId + "]");

	}

	private long insertPlayer(ContentValues values) {
		Log.d("DBHelper", "Inserting new player");
		long rowId = getWritableDatabase().insert(PlayerTable.TABLE_NAME, null, values);
		if( rowId < 0){
			throw new SQLException("Failed to insert player");
		}

		Log.d("DBHelper","Inserted Player[" + rowId + "]");
		return rowId;
	}

	private void updateGame(String gameId, ContentValues values) {
		Log.d("DBHelper", "Updating Game[" + gameId + "]");
		getWritableDatabase().update(GameTable.TABLE_NAME, values, GameTable._ID + "=" + gameId, null);
		Log.d("DBHelper", "Updated Game[" + gameId + "]");

	}

	private long insertGame(ContentValues values) {
		Log.d("DBHelper", "Inserting new Game");
		long rowId = getWritableDatabase().insert(GameTable.TABLE_NAME, null, values);
		if( rowId < 0){
			throw new SQLException("Failed to insert new Game");
		}

		Log.d("DBHelper","Inserted Game[" + rowId + "]");
		return rowId;

	}

	private void updateTeam(String teamId, ContentValues values) {
		Log.d("DBHelper", "Updating Team[" + teamId + "]");
		getWritableDatabase().update(TeamTable.TABLE_NAME, values, TeamTable._ID + "=" + teamId, null);
		Log.d("DBHelper", "Updated Team[" + teamId + "]");

	}

	private long insertTeam(ContentValues values) {
		Log.d("DBHelper", "Inserting new Team");
		long rowId = getWritableDatabase().insert(TeamTable.TABLE_NAME, null, values);
		if( rowId < 0){
			throw new SQLException("Failed to insert new Team");
		}

		Log.d("DBHelper","Inserted Team[" + rowId + "]");
		return rowId;
	}

	public void deleteGameTeams(String gameId){
		getWritableDatabase().delete(GameTeamsTable.TABLE_NAME, GameTeamsTable.GAME_ID + "=" + gameId, null);
	}

	private long insertGameTeams(ContentValues values) {
		Log.d("DBHelper", "Inserting new GameTeams");
		long rowId = getWritableDatabase().insert(GameTeamsTable.TABLE_NAME, null, values);
		if( rowId < 0){
			throw new SQLException("Failed to insert new GameTeams");
		}

		Log.d("DBHelper","Inserted GameTeams[" + rowId + "]");
		return rowId;

	}

	public void deleteTeamPlayers(String teamId){
		getWritableDatabase().delete(TeamPlayersTable.TABLE_NAME, TeamPlayersTable.TEAM_ID + "=" + teamId, null);
	}

	private void insertTeamPlayers(ContentValues values) {
		Log.d("DBHelper", "Inserting new TeamPlayers");
		long rowId = getWritableDatabase().insert(TeamPlayersTable.TABLE_NAME, null, values);
		if( rowId < 0){
			throw new SQLException("Failed to insert new TeamPlayers");
		}

		Log.d("DBHelper","Inserted TeamPlayers[" + rowId + "]");

	}

	public Team getTeam(String teamId){

		if(teamId == null || teamId.length() == 0){
			return null;
		}

		Team team = null;

		Cursor teamCurs = 
			getReadableDatabase().query(TeamTable.TABLE_NAME, null, TeamTable._ID + "=" + teamId, null, null, null, null);

		if(teamCurs.moveToFirst()){
			team = new Team();			
			team.setId(teamCurs.getString(0));
			team.setName(teamCurs.getString(1));
			loadPlayers(team);	
			loadTeamStats(team);
		}

		teamCurs.close();

		return team;

	}

	protected void loadTeams(Game game){

		String query = "SELECT t.* FROM " + TeamTable.TABLE_NAME + " t" +
		" LEFT OUTER JOIN " + GameTeamsTable.TABLE_NAME + " gt " + 
		" ON (t._id = gt.team_id) " +
		" WHERE gt.game_id = ?";
		Cursor teamCurs = 
			getReadableDatabase().rawQuery(query, new String[]{game.getId()});

		teamCurs.moveToFirst();
		while(!teamCurs.isAfterLast()){
			Team team = new Team();
			team.setId(teamCurs.getString(0));
			team.setName(teamCurs.getString(1));
			loadTeamGameStats(game,team);
			loadPlayers(team);	
			game.addTeam(team);
			teamCurs.moveToNext();
		}				

		teamCurs.close();
	}

	private void loadTeamGameStats(Game game, Team team) {

		TeamGameStat stat = getTeamstatsForGame(game, team);
		team.addGameStat(stat);

	}
	
	
	private TeamGameStat getTeamstatsForGame(Game game, Team team){

		TeamGameStat stats = new TeamGameStat(team);
		stats.setGameId(game.getId());

		String whereClause = TeamGameStatsTable.TEAM_ID + "= ? AND " + TeamGameStatsTable.GAME_ID + "= ?";
		String[] whereArgs = new String[]{team.getId(),game.getId()};

		Cursor curs = getReadableDatabase().query(TeamGameStatsTable.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
		if(curs.moveToFirst()){
			stats.setScore(curs.getDouble(2));
			stats.setWinner(curs.getInt(3)==1?true:false);
		}

		curs.close();

		return stats;
	}

	protected void loadPlayers(Team team){

		String query = "SELECT p.* FROM " + PlayerTable.TABLE_NAME + " p" +
		" LEFT OUTER JOIN " + TeamPlayersTable.TABLE_NAME + " tp " + 
		" ON (p._id = tp.player_id)" +
		" WHERE tp.team_id = ?";
		Cursor playerCurs = 
			getReadableDatabase().rawQuery(query, new String[]{team.getId()});

		if(playerCurs.moveToFirst()){
			while(!playerCurs.isAfterLast()){
				Player player = new Player();
				player.setId(playerCurs.getString(0));
				player.setFirstName(playerCurs.getString(1));
				player.setLastName(playerCurs.getString(2));
				player.setNickName(playerCurs.getString(3));

				team.addPlayer(player);		
				playerCurs.moveToNext();
			}

		}

		playerCurs.close();
	}

	public void saveTeamGameStats(TeamGameStat teamStats) {

		ContentValues values = new ContentValues();
		values.put(TeamGameStatsTable.TEAM_ID, teamStats.getTeam().getId());
		values.put(TeamGameStatsTable.GAME_ID, teamStats.getGameId());		
		values.put(TeamGameStatsTable.WINNER, teamStats.isWinner());
		values.put(TeamGameStatsTable.SCORE, teamStats.getScore());

		String whereClause = TeamGameStatsTable.TEAM_ID + "= ? AND " + TeamGameStatsTable.GAME_ID + "= ?";
		String[] whereArgs = new String[]{teamStats.getTeam().getId(),teamStats.getGameId()};

		Cursor curs = getReadableDatabase().query(TeamGameStatsTable.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
		if(curs.isAfterLast()){
			insertTeamGameStats(values);
		} else {
			updateTeamGameStats(teamStats.getTeam().getId(),teamStats.getGameId(), values);
		}
		
		curs.close();

		updateTeamStats(teamStats);
		
	}


	public void savePlayerGameStats(PlayerGameStat playerStats) {

		// TODO Replace the replace!
		ContentValues values = new ContentValues();
		values.put(PlayerGameStatsTable.PLAYER_ID, playerStats.getPlayerId());
		values.put(PlayerGameStatsTable.GAME_ID, playerStats.getGameId());
		values.put(PlayerGameStatsTable.WINNER, playerStats.isWinner());
		values.put(PlayerGameStatsTable.SCORE, playerStats.getScore());

		getWritableDatabase().replace(PlayerGameStatsTable.TABLE_NAME, null, values);

	}

	private long insertTeamGameStats(ContentValues values) {

		long rowId = getWritableDatabase().insert(TeamGameStatsTable.TABLE_NAME, null, values);
		if( rowId < 0){
			throw new SQLException("Failed to insert new TeamStat");
		}

		return rowId;
	}

	private void updateTeamGameStats(String teamId, String gameId, ContentValues values) {
		String whereClause = TeamGameStatsTable.TEAM_ID + "= ? AND " + TeamGameStatsTable.GAME_ID + "= ?";
		String[] whereArgs = new String[]{teamId,gameId};
		getWritableDatabase().update(TeamGameStatsTable.TABLE_NAME, values, whereClause, whereArgs);

	}

	protected void loadTeamStats(Team team) {
		team.setStats(getContestantStats(team));		
	}
	
	private ContestantStats getContestantStats(Contestant contestant) {
		
		String whereClause = StatsRollupTable.ID + "= ? AND " + StatsRollupTable.TYPE + "= ?";
		String[] whereArgs = new String[]{contestant.getId(),contestant.getClass().getName()};

		Cursor statCurs = 
			getReadableDatabase().query(StatsRollupTable.TABLE_NAME, null, whereClause, whereArgs, null, null, null);

		ContestantStats stat = new ContestantStats();
		if(statCurs.moveToFirst()){
			stat.setHighScore(statCurs.getDouble(2));
			stat.setAvgScore(statCurs.getDouble(3));
			stat.setWins(statCurs.getInt(4));
			stat.setLosses(statCurs.getInt(5));
		}

		statCurs.close();
		
		return stat;
		
	}
	
	
	private void updateTeamStats(TeamGameStat gameStats) {
		
		boolean insert = false;
		
		Team team = gameStats.getTeam();
		ContestantStats stats = getContestantStats(team);
		if(stats.getHighScore() == 0.0){
			insert = true;
		}
		
		if(gameStats.isWinner()){
			stats.setWins(stats.getWins() + 1);
		} else {
			stats.setLosses(stats.getLosses() + 1);
		}
		
		if(gameStats.getScore() > stats.getHighScore()){
			stats.setHighScore(gameStats.getScore());
		}
		
		double avgMpr = stats.getAvgScore();
		int totalgames = stats.getWins() + stats.getLosses();
		
		double newAvgMpr = (avgMpr + gameStats.getScore())/totalgames;
		stats.setAvgScore(newAvgMpr);
		
		ContentValues values = new ContentValues();
		values.put(StatsRollupTable.HIGH_SCORE, stats.getHighScore());
		values.put(StatsRollupTable.AVG_SCORE, stats.getAvgScore());
		values.put(StatsRollupTable.WINS, stats.getWins());
		values.put(StatsRollupTable.LOSSES, stats.getLosses());
		
		if(insert){
			insertContestantStats(team, values);
		} else {
			updateContestantStats(team, values);
		}
		
		
	}

	private void insertContestantStats(Contestant contestant, ContentValues values) {
		
		values.put(StatsRollupTable.ID, contestant.getId());
		values.put(StatsRollupTable.TYPE, contestant.getClass().getName());
		
		long rowId = getWritableDatabase().insert(StatsRollupTable.TABLE_NAME, null, values);
		if( rowId < 0){
			throw new SQLException("Failed to insert new TeamPlayers");
		}

		
	}
	
	private void updateContestantStats(Contestant contestant, ContentValues values) {
		
		String whereClause = StatsRollupTable.ID + "= ? AND " + StatsRollupTable.TYPE + "= ?";
		String[] whereArgs = new String[]{contestant.getId(),contestant.getClass().getName()};
		getWritableDatabase().update(StatsRollupTable.TABLE_NAME, values, whereClause, whereArgs);
		
	}

	

	public List<Team> getLeadingTeams(ContestantStatType statType, int numLeaders) {
		
		List<Team> leaders = new ArrayList<Team>();
		
		Cursor statCurs = getLeaders(Team.class, statType);
		
		int idx = 0;
		statCurs.moveToFirst();
		while(!statCurs.isAfterLast() && idx < numLeaders){
			Team team = getTeam(statCurs.getString(0));
			leaders.add(team);
			idx++;
			statCurs.moveToNext();
		}
		
		statCurs.close();
		
		return leaders;
	}
	
	public List<Player> getLeadingPlayers(ContestantStatType statType, int numLeaders) {
		
		List<Player> leaders = new ArrayList<Player>();
		
		Cursor statCurs = getLeaders(Player.class, statType);
		
		int idx = 0;
		statCurs.moveToFirst();
		while(!statCurs.isAfterLast() && idx < numLeaders){
			Player player = getPlayer(statCurs.getString(0));
			leaders.add(player);
			idx++;
			statCurs.moveToNext();
		}
		
		statCurs.close();
		
		return leaders;
	}
	
	private Cursor getLeaders(Class contestantType, ContestantStatType statType){
		
		String whereClause = LeaderboardTable.LEADER_TYPE + "= ? AND " + LeaderboardTable.STAT_TYPE + "= ?";
		String[] whereArgs = new String[]{contestantType.getName(),statType.toString()};

		Cursor statCurs = 
			getReadableDatabase().query(LeaderboardTable.TABLE_NAME, null, whereClause, whereArgs, 
					null, null, LeaderboardTable.DEFAULT_SORT_ORDER);
		
		return statCurs;
	}

}
