package com.three_stack.digital_compass.backend;

import com.google.gson.annotations.Expose;

public abstract class BasicAction {

	@Expose
	private String gameCode;

	@Expose
	private Player player;

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
