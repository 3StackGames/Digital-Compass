package com.three_stack.digital_compass.backend;

public abstract class BasicGameStateFactory {

	BasicPhase currentPhase;
	
	public void setPhase(BasicPhase phase) {
		currentPhase = phase;
	}
	
	public abstract BasicGameState createState();
}
