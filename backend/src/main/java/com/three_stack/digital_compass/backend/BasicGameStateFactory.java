package com.three_stack.digital_compass.backend;

public abstract class BasicGameStateFactory {

	BasicPhase currentPhase;
	
	public BasicGameStateFactory setPhase(BasicPhase phase) {
		currentPhase = phase;
		return this;
	}
	
	public abstract BasicGameState createState();
}
