package com.myinterwebspot.app.dartnight.model;

import com.parse.ParseObject;

public class Game extends ParseModel {

	// PARSE Fields
	private static String FIELD_NAME = "name";
	private static String FIELD_PARENT_GAME_ID = "parent_game_id";
	private static String FIELD_LEAGUE_ID = "league_id";
	private static String FIELD_START_TIMESTAMP = "start_time";
	private static String FIELD_END_TIMESTAMP = "end_time";

	public Game(ParseObject parseData) {
		super(parseData);
	}

	public Game(String leagueId) {
		super(Game.class.getSimpleName());
		this.put(FIELD_LEAGUE_ID, leagueId);
	}

	public String getName() {
		return this.getString(FIELD_NAME);
	}

	public void setName(String name) {
		this.put(FIELD_NAME, name);
	}

	public String getParentId() {
		return this.getString(FIELD_PARENT_GAME_ID);
	}

	public void setParentId(String parentId) {
		this.put(FIELD_PARENT_GAME_ID, parentId);
	}

	public long getStartTime() {
		return this.getLong(FIELD_START_TIMESTAMP);
	}

	public void setStartTime(long startTimeMs) {
		this.put(FIELD_START_TIMESTAMP, startTimeMs);
	}

	public long getEndTime() {
		return this.getLong(FIELD_END_TIMESTAMP);
	}

	public void setEndTime(long startTimeMs) {
		this.put(FIELD_END_TIMESTAMP, startTimeMs);
	}

}
