package com.three_stack.digital_compass.backend;

public abstract class GameStateFactory {

	Phase currentPhase;
	
	public void setPhase(Phase phase) {
		currentPhase = phase;
	}
	
	public abstract GameState createState();
}
