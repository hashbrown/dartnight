package com.myinterwebspot.app.dartnight.model;

public class Player extends ParseModel {

	// PARSE Fields
	private static String FIELD_SCREEN_NAME = "screen_name";
	private static String FIELD_USER = "user";

	public Player() {
		super(Player.class.getSimpleName());
	}

	public Player(User user) {
		super(Player.class.getSimpleName());
		this.put(FIELD_USER, user);
	}

	public String getScreenName() {
		return this.getString(FIELD_SCREEN_NAME);
	}

	public void setScreenName(String name) {
		this.put(FIELD_SCREEN_NAME, name);
	}

	public User getUser() {
		return ParseModel.fromParseObject(this.getParseUser(FIELD_USER),
				User.class);
	}

}
