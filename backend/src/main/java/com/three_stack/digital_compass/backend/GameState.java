package com.three_stack.digital_compass.backend;

public abstract class GameState {
	protected Phase currentPhase;
	protected Action currentAction;
	protected Player[] players;
	
	public Phase getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(Phase currentPhase) {
		this.currentPhase = currentPhase;
	}

	public Action getCurrentAction() {
		return currentAction;
	}

	public void setCurrentAction(Action currentAction) {
		this.currentAction = currentAction;
	}

	// TODO: @HyunbinTodo: What properties should be in GameState vs AGameState?
	/*
	public Player[] getPlayers() {
		return players;
	}
	*/

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	enum Action {
		DISPLAY, GAMEPAD, SERVER
	}
	
}
