package com.three_stack.digital_compass.backend;

import org.json.JSONObject;

public abstract class MainPhase extends Phase {
	
	public abstract GameState processAction(JSONObject action); 
}
