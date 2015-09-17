package com.three_stack.digital_compass.backend;

import org.json.JSONObject;

public abstract class Phase {
	String name;
	
	//MainPhase (generic), InitialPhase, EndPhase
	//name, begin(), MainPhase(gameState : GameState), InitialPhase()
	//passed in state by manager
	public abstract GameState processAction(JSONObject action, GameState state); 
}
