package com.three_stack.digital_compass.backend;

import org.json.JSONObject;

public interface BasicPhase {
	
	public abstract BasicGameState processAction(JSONObject action, BasicGameState state);
}
