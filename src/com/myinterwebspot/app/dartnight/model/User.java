package com.myinterwebspot.app.dartnight.model;

import com.parse.ParseUser;

public class User{
	
	private static String FNAME_FIELD = "first_name";
	private static String LNAME_FIELD = "last_name";
	
	private final ParseUser user;
	
	public User(ParseUser user){
		this.user = user;
	}
	
	public void setFirstName(String firstName) {
		user.put(FNAME_FIELD, firstName);
	}
	
	public String getFirstName(){
		return user.getString(FNAME_FIELD);
	}
	
	public void setLastName(String lastName) {
		user.put(LNAME_FIELD, lastName);
	}
	
	public String getLastName(){
		return user.getString(LNAME_FIELD);
	}
	
	//PARSE API
	public String getUsername(){
		return user.getUsername();
	}
	
	public void setUsername(String username){
		user.setUsername(username);
	}
	
	public void setEmail(String email){
		user.setEmail(email);
	}
	
	public void setPassword(String password){
		user.setPassword(password);
	}
	
}
