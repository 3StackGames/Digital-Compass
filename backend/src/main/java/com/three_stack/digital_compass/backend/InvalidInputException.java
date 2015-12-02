package com.three_stack.digital_compass.backend;

public class InvalidInputException extends 	Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5121994118845234438L;
	
	public InvalidInputException() {
		super();
	}
	
	public InvalidInputException(String message) {
		super(message);
	}
	
	public InvalidInputException(Code code, String message) {
		super(message);
		this.code = code;
	}
	
	public static enum Code {
		INPUT_REJECTED
	}
	
	private Code code;
	
	public Code getCode() {
		return code;
	}
}
