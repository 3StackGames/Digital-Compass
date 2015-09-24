package com.three_stack.digital_compass.backend;

import org.json.JSONObject;

public abstract class BasicPhase {
	String name;
	
	//MainPhase (generic)
	//name, begin(), MainPhase(gameState : GameState), InitialPhase()
	//passed in state by manager
	public abstract BasicGameState processAction(JSONObject action, BasicGameState state);
}
