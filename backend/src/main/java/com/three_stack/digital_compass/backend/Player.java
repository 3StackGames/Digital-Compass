package com.three_stack.digital_compass.backend;

import com.google.gson.annotations.Expose;

public class Player {

	@Expose
	private String name;

	/**
	 *
	 * @return
	 * The name
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @param name
	 * The name
	 */
	public void setName(String name) {
		this.name = name;
	}

}