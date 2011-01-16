package com.myinterwebspot.app.dartnight.model;

public class Player {
	
	private String id;
	private String firstName;
	private String lastName;
	private String nickName;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getNickName() {
		if(nickName == null){
			return firstName;
		}
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}
