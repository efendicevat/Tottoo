package com.ege.tottoo.exceptions;

public class NotPlayableException extends Exception {
	private String message = "";

	public NotPlayableException() {
		
	}
	
	public NotPlayableException(String message) {
		setMessage(message);
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
