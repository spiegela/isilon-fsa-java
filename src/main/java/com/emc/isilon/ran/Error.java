package com.emc.isilon.ran;

public class Error {
	private String code, message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toString() {
		return this.getCode() + ": " + this.getMessage();
	}
}
