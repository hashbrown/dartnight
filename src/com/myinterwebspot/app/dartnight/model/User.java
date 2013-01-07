package com.myinterwebspot.app.dartnight.model;

import com.parse.ParseUser;

public class User extends ParseUser {
	
	private static String FNAME_FIELD = "first_name";
	private static String LNAME_FIELD = "last_name";
	
	
	
	public void setFirstName(String firstName) {
		this.put(FNAME_FIELD, firstName);
	}
	
	public String getFirstName(){
		return this.getString(FNAME_FIELD);
	}
	
	public void setLastName(String lastName) {
		this.put(LNAME_FIELD, lastName);
	}
	
	public String getLastName(){
		return this.getString(LNAME_FIELD);
	}
	

}
