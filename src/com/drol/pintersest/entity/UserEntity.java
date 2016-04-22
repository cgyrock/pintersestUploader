package com.drol.pintersest.entity;

public class UserEntity {

	private String id;
	
	private String url;
	
	private String firstName;
	
	private String lastName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
	
	public String getFullName() {
		String fullName = "";
		
		if(this.firstName != null && this.firstName.trim().length() > 0) {
			fullName += this.firstName;
		}
		
		if(this.lastName != null && this.lastName.trim().length() > 0) {
			fullName += "_" + this.lastName;
		}
		
		return fullName;
	}
	
}
