package com.myinterwebspot.app.dartnight.model;

import com.parse.ParseUser;

public class User extends ParseModel {

	private static String FNAME_FIELD = "first_name";
	private static String LNAME_FIELD = "last_name";

	public User() {
		super(new ParseUser());
	}

	public void setFirstName(String firstName) {
		this.put(FNAME_FIELD, firstName);
	}

	public String getFirstName() {
		return this.getString(FNAME_FIELD);
	}

	public void setLastName(String lastName) {
		this.put(LNAME_FIELD, lastName);
	}

	public String getLastName() {
		return this.getString(LNAME_FIELD);
	}

	// PARSE API
	public String getUsername() {
		return this.asParseUser().getUsername();
	}

	public void setUsername(String username) {
		this.asParseUser().setUsername(username);
	}

	public void setEmail(String email) {
		this.asParseUser().setEmail(email);
	}

	public void setPassword(String password) {
		this.asParseUser().setPassword(password);
	}

	private ParseUser asParseUser() {
		return (ParseUser) this.asParse();
	}
}
