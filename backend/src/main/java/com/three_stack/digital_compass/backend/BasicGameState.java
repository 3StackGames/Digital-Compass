package com.three_stack.digital_compass.backend;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicGameState {

	protected BasicPhase currentPhase;

	@Expose
	protected String gameCode;
	@Expose
	protected List<Player> players = new ArrayList<Player>();
	
	public BasicGameState() {
		
	}
	
	public BasicPhase getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(BasicPhase currentPhase) {
		this.currentPhase = currentPhase;
	}

	/**
	 *
	 * @return
	 * The gameCode
	 */
	public String getGameCode() {
		return gameCode;
	}

	/**
	 *
	 * @param gameCode
	 * The gameCode
	 */
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	/**
	 *
	 * @return
	 * The players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 *
	 * @param players
	 * The players
	 */
	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	enum Action {
		DISPLAY, GAMEPAD, SERVER
	}
	
}