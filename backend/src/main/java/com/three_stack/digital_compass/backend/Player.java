package com.three_stack.digital_compass.backend;

import com.google.gson.annotations.Expose;

public class Player {

	@Expose
	private String displayName;

	@Expose
	private int score;
	
	@Expose
	private String accountName;

	/**
	 *
	 * @return
	 * The name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 *
	 * @param name
	 * The name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 *
	 * @return
	 * The score
	 */
	public int getScore() {
		return score;
	}

	/**
	 *
	 * @param score
	 * The score
	 */
	public void setScore(int score) {
		this.score = score;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
}