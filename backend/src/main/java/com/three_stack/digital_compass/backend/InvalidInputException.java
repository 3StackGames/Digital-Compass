package com.three_stack.digital_compass.backend;

public class InvalidInputException extends 	Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5121994118845234438L;
	
	public static enum Code {
		INPUT_REJECTED
	}
	
	private Code code;
	
	public void setCode(Code code) {
		this.code = code;
	}
	
	public Code getCode() {
		return code;
	}
}
