package com.myinterwebspot.app.dartnight.db;

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

import com.myinterwebspot.app.dartnight.model.Game;
import com.myinterwebspot.app.dartnight.model.GameState;
import com.myinterwebspot.app.dartnight.model.Player;
import com.myinterwebspot.app.dartnight.model.PlayerStat;
import com.myinterwebspot.app.dartnight.model.Team;
import com.myinterwebspot.app.dartnight.model.TeamStat;


public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "DartsDB";
	private static final int DATABASE_VERSION = 5;

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
		TeamStatsTable.create(db);
		PlayerStatsTable.create(db);

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

		if(gameCurs.getCount() > 0){
			game = new Game();
			gameCurs.moveToFirst();
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
			
		if(playerCurs.getCount() > 0){
			player = new Player();
			playerCurs.moveToFirst();
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
	
	protected void loadTeams(Game game){
		
		String query = "SELECT t.* FROM " + TeamTable.TABLE_NAME + " t" +
		" LEFT OUTER JOIN " + GameTeamsTable.TABLE_NAME + " gt " + 
		" ON (t._id = gt.team_id) " +
		" WHERE gt.game_id = ?";
		Cursor teamCurs = 
			getReadableDatabase().rawQuery(query, new String[]{game.getId()});

		if(teamCurs.getCount() > 0){
			teamCurs.moveToFirst();
			
			while(!teamCurs.isAfterLast()){
				Team team = new Team();
				team.setId(teamCurs.getString(0));
				team.setName(teamCurs.getString(1));
				loadStats(game,team);
				loadPlayers(team);	
				game.addTeam(team);
				teamCurs.moveToNext();
			}				
		}
		
		teamCurs.close();
	}
	
	private void loadStats(Game game, Team team) {
		
		TeamStat stat = getTeamstatsForGame(team.getId(), game.getId());
		team.addGameStat(stat);
		
	}

	protected void loadPlayers(Team team){
		
		String query = "SELECT p.* FROM " + PlayerTable.TABLE_NAME + " p" +
		" LEFT OUTER JOIN " + TeamPlayersTable.TABLE_NAME + " tp " + 
		" ON (p._id = tp.player_id)" +
		" WHERE tp.team_id = ?";
		Cursor playerCurs = 
			getReadableDatabase().rawQuery(query, new String[]{team.getId()});

		if(playerCurs.getCount() > 0){
			playerCurs.moveToFirst();
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

	public void saveTeamStats(TeamStat teamStats) {
		
		ContentValues values = new ContentValues();
		values.put(TeamStatsTable.TEAM_ID, teamStats.getTeamId());
		values.put(TeamStatsTable.GAME_ID, teamStats.getGameId());		
		values.put(TeamStatsTable.WINNER, teamStats.isWinner());
		values.put(TeamStatsTable.MPR, teamStats.getMpr());
		
		getWritableDatabase().replace(TeamStatsTable.TABLE_NAME, null, values);
	}
	
	public TeamStat getTeamstatsForGame(String teamId, String gameId){
		
		TeamStat stats = new TeamStat(teamId);
		stats.setGameId(gameId);
		
		String whereClause = TeamStatsTable.TEAM_ID + "= ? AND " + TeamStatsTable.GAME_ID + "= ?";
		String[] whereArgs = new String[]{teamId,gameId};
		
		Cursor curs = getReadableDatabase().query(TeamStatsTable.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
		if(curs.moveToFirst()){
			stats.setMpr(curs.getDouble(2));
			stats.setWinner(curs.getInt(3)==1?true:false);
		}
		
		curs.close();
		
		return stats;
	}

	public void savePlayerStats(PlayerStat playerStats) {
		
		ContentValues values = new ContentValues();
		values.put(PlayerStatsTable.PLAYER_ID, playerStats.getPlayerId());
		values.put(PlayerStatsTable.GAME_ID, playerStats.getGameId());
		values.put(PlayerStatsTable.WINNER, playerStats.isWinner());
		values.put(PlayerStatsTable.MPR, playerStats.getMpr());
		
		getWritableDatabase().replace(PlayerStatsTable.TABLE_NAME, null, values);
		
	}
}
