package com.myinterwebspot.app.dartnight.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
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
import com.myinterwebspot.app.dartnight.model.GameStat;
import com.myinterwebspot.app.dartnight.model.GameState;
import com.myinterwebspot.app.dartnight.model.Player;
import com.myinterwebspot.app.dartnight.model.Team;


public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "DartsDB";
	private static final int DATABASE_VERSION = 2;

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
		GameStatsTable.create(db);
		PlayerGameStatsTable.create(db);
		StatsRollupTable.create(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("LOG", "Upgrading database from version " + oldVersion + " to " +
				newVersion + ", which will destroy all old data");

		UpgradeStrategyFactory factory = new UpgradeStrategyFactory();
		DBUpgradeStrategy upgradeStrategy = factory.getUpgradeStrategy(oldVersion, newVersion);
		upgradeStrategy.upgrade(db);
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
			game.setId(gameCurs.getString(gameCurs.getColumnIndex(GameTable._ID)));
			game.setName(gameCurs.getString(gameCurs.getColumnIndex(GameTable.NAME)));
			game.setState(GameState.valueOf(gameCurs.getString(gameCurs.getColumnIndex(GameTable.STATE))));
			game.setCreationDate(new Date(gameCurs.getLong(gameCurs.getColumnIndex(GameTable.CREATED_DATE))));
			game.setModificationDate(new Date(gameCurs.getLong(gameCurs.getColumnIndex(GameTable.MODIFIED_DATE))));
			
			String parentId = gameCurs.getString(gameCurs.getColumnIndex(GameTable.PARENT));
			if(parentId != null){
				Game parent = getGame(parentId);
				game.setParent(parent);
			}
			
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
		
		if(game.getParent() != null){
			values.put(GameTable.PARENT, game.getParent().getId());
		}

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
			String id = generateTeamId(team);
			if(teamExists(id)){
				team.setId(id);
				updateTeam(id, values);
			} else {
				values.put(TeamTable._ID,id);
				values.put(TeamTable.CREATED_DATE, now);
				long teamId = insertTeam(values);
				team.setId(id);
			}
		} else {
			updateTeam(team.getId(), values);
		}

		saveTeamPlayers(team.getId(), team.getPlayers());

	}

	private String generateTeamId(Team team) {

		List<Player> players = new ArrayList(team.getPlayers());
		Collections.sort(players, new Comparator<Player>() {

			public int compare(Player player1, Player player2) {
				if (Integer.valueOf(player1.getId()) > Integer.valueOf(player2.getId())){
					return -1;
				} else if (Integer.valueOf(player1.getId()) < Integer.valueOf(player2.getId())){
					return 11;
				}
				return 0;
			}
		});

		StringBuffer id = new StringBuffer();
		for (Player player : players) {
			id.append(player.getId()).append(":");
		}

		id.deleteCharAt(id.lastIndexOf(":"));

		return id.toString();
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
			loadContestantStats(player);

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
		getWritableDatabase().update(TeamTable.TABLE_NAME, values, TeamTable._ID + "= ?", new String[]{teamId});
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
		getWritableDatabase().delete(TeamPlayersTable.TABLE_NAME, TeamPlayersTable.TEAM_ID + "= ?", new String[]{teamId});
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
			getReadableDatabase().query(TeamTable.TABLE_NAME, null, TeamTable._ID + "= ?", new String[]{teamId}, null, null, null);

		if(teamCurs.moveToFirst()){
			team = new Team();			
			team.setId(teamCurs.getString(0));
			team.setName(teamCurs.getString(1));
			loadPlayers(team);	
			loadContestantStats(team);
		}

		teamCurs.close();

		return team;

	}

	public boolean teamExists(String teamId){

		if(teamId == null || teamId.length() == 0){
			return false;
		}

		Cursor teamCurs = 
			getReadableDatabase().query(TeamTable.TABLE_NAME, new String[]{TeamTable._ID}, TeamTable._ID + "= ?", new String[]{teamId}, null, null, null);
		
		boolean exists = teamCurs.moveToFirst();
		teamCurs.close();

		return exists;

	}

	protected void loadTeams(Game game){

		String query = "SELECT t.* FROM " + TeamTable.TABLE_NAME + " t" +
		" LEFT OUTER JOIN " + GameTeamsTable.TABLE_NAME + " gt " + 
		" ON (t._id = gt.team_id) " +
		" WHERE gt.game_id = ? " +
		" ORDER BY gt.rowid";
		Cursor teamCurs = 
			getReadableDatabase().rawQuery(query, new String[]{game.getId()});

		teamCurs.moveToFirst();
		while(!teamCurs.isAfterLast()){
			Team team = new Team();
			team.setId(teamCurs.getString(0));
			team.setName(teamCurs.getString(1));
			loadGameStats(game,team);
			loadPlayers(team);	
			game.addTeam(team);
			teamCurs.moveToNext();
		}				

		teamCurs.close();
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

	private void loadGameStats(Game game, Contestant contestant) {

		GameStat stat = getGameStats(game, contestant);
		contestant.addGameStat(stat);

	}


	private GameStat getGameStats(Game game, Contestant contestant){

		GameStat stats = new GameStat(contestant);
		stats.setGameId(game.getId());

		String whereClause = GameStatsTable.GAME_ID + "= ? AND " +
		GameStatsTable.CONTESTANT_ID + "= ? AND " +
		GameStatsTable.CONTESTANT_TYPE + "= ?";
		String[] whereArgs = new String[]{game.getId(), contestant.getId(), contestant.getClass().getName()};

		Cursor curs = getReadableDatabase().query(GameStatsTable.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
		if(curs.moveToFirst()){
			stats.setScore(curs.getDouble(3));
			stats.setWinner(curs.getInt(4)==1?true:false);
		}

		curs.close();

		return stats;
	}



	public void saveGameStats(GameStat gameStats) {

		ContentValues values = new ContentValues();
		values.put(GameStatsTable.GAME_ID, gameStats.getGameId());		
		values.put(GameStatsTable.CONTESTANT_ID, gameStats.getContestant().getId());
		values.put(GameStatsTable.CONTESTANT_TYPE, gameStats.getContestant().getClass().getName());
		values.put(GameStatsTable.WINNER, gameStats.isWinner());
		values.put(GameStatsTable.SCORE, gameStats.getScore());

		String whereClause = GameStatsTable.GAME_ID + "= ? AND " +
		GameStatsTable.CONTESTANT_ID + "= ? AND " +
		GameStatsTable.CONTESTANT_TYPE + "= ?";

		String[] whereArgs = new String[]{gameStats.getGameId(), 
				gameStats.getContestant().getId(), gameStats.getContestant().getClass().getName()};

		Cursor curs = getReadableDatabase().query(GameStatsTable.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
		if(curs.isAfterLast()){
			insertGameStats(values);
		} else {
			updateGameStats(gameStats, values);
		}

		curs.close();

		updateGameStats(gameStats);

	}



	private long insertGameStats(ContentValues values) {

		long rowId = getWritableDatabase().insert(GameStatsTable.TABLE_NAME, null, values);
		if( rowId < 0){
			throw new SQLException("Failed to insert new TeamStat");
		}

		return rowId;
	}


	private void updateGameStats(GameStat stats, ContentValues values) {
		String whereClause = GameStatsTable.GAME_ID + "= ? AND " +
		GameStatsTable.CONTESTANT_ID + "= ? AND " +
		GameStatsTable.CONTESTANT_TYPE + "= ?";

		String[] whereArgs = new String[]{stats.getGameId(), 
				stats.getContestant().getId(), stats.getContestant().getClass().getName()};

		getWritableDatabase().update(GameStatsTable.TABLE_NAME, values, whereClause, whereArgs);

	}

	protected void loadContestantStats(Contestant contestant) {
		contestant.setContestantStats(getContestantStats(contestant));		
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


	private void updateGameStats(GameStat gameStats) {

		boolean insert = false;

		ContestantStats stats = getContestantStats(gameStats.getContestant());
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
			insertContestantStats(gameStats.getContestant(), values);
		} else {
			updateContestantStats(gameStats.getContestant(), values);
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

		return (List<Team>) getLeadingContestants(Team.class, statType, numLeaders);
	}

	public List<Player> getLeadingPlayers(ContestantStatType statType, int numLeaders) {

		return (List<Player>) getLeadingContestants(Player.class, statType, numLeaders);
	}

	private List<? extends Contestant> getLeadingContestants(Class<? extends Contestant> contestantType, ContestantStatType statType, int numLeaders){

		List<Contestant> leaders = new ArrayList<Contestant>();

		Cursor statCurs = getLeaders(contestantType, statType);
		Contestant contestant = null;
		int idx = 0;
		statCurs.moveToFirst();
		while(!statCurs.isAfterLast() && idx < numLeaders){
			if(contestantType == Team.class){
				contestant = getTeam(statCurs.getString(0));				
			} else {
				contestant = getPlayer(statCurs.getString(0));
			}
			leaders.add(contestant);
			idx++;
			statCurs.moveToNext();
		}

		statCurs.close();

		return leaders;
	}

	private Cursor getLeaders(Class<? extends Contestant> contestantType, ContestantStatType statType){

		String whereClause = StatsRollupTable.TYPE + "= ?";
		String[] whereArgs = new String[]{contestantType.getName()};
		String orderBy = null;

		switch (statType) {
		case TOP_SCORE_STAT:
			orderBy = "high_score desc";
			break;
		case AVG_SCORE_STAT:
			orderBy = "avg_score desc";
			break;
		case TOP_WINS_STAT:
			orderBy = "wins desc";
			break;
		default:
			break;
		}

		Cursor statCurs = 
			getReadableDatabase().query(StatsRollupTable.TABLE_NAME, null, whereClause, whereArgs, null, null, orderBy);


		return statCurs;
	}

}
