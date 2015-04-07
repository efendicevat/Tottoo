package com.ege.tottoo.exceptions;

public class TottooException extends Exception {
	private String message = "";
	
	public TottooException() {
		
	}
	
	public TottooException(String _message) {
		setMessage(_message);
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
