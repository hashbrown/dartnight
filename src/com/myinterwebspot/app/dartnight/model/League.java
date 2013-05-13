package com.myinterwebspot.app.dartnight.model;

public class League extends ParseModel {

	// PARSE Fields
	private static String FIELD_NAME = "league_name";
	private static String FIELD_OWNER = "owner";

	public League() {
		super(League.class.getName());
	}

	public void setName(String leagueName) {
		this.put(FIELD_NAME, leagueName);
	}

	public String getName() {
		return this.getString(FIELD_NAME);
	}

	public void setOwner(User owner) {
		this.put(FIELD_OWNER, owner);
	}

	public User getOwner() {
		return ParseModel.fromParseObject(this.getParseUser(FIELD_OWNER),
				User.class);
	}

}
