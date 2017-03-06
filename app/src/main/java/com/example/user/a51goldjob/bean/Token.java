package com.example.user.a51goldjob.bean;



public class Token {

	private String token;
	private String userId;
	
	public Token() {
	}
	
	public Token(String token, String userId) {
		this.token = token;
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
