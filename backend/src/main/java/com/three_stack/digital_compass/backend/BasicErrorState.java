package com.three_stack.digital_compass.backend;

public class BasicErrorState extends BasicGameState {

	private Code code;
	private String message;
	
	public static enum Code {
		INPUT_REJECTED, ILLEGAL_STATE
	}
	
	public BasicErrorState(String message) {
		this.message = message;
	}
	
	public BasicErrorState(Code code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public void setCode(Code code) {
		this.code = code;
	}
	
	public Code getCode() {
		return code;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	

}
