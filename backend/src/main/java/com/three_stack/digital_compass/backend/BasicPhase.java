package com.three_stack.digital_compass.backend;

public abstract class BasicPhase {
    //meant primarily for the sake of emitting the name of the phase for frontend
    protected String phaseName;

    //Called before State Updates to clients
    public abstract void setup(BasicGameState state);

    //Called after receiving Gamepad Input
    //Should never return null
    public abstract BasicGameState processAction(BasicAction action, BasicGameState state) throws InvalidInputException;

    public BasicPhase() {
        phaseName = this.getClass().getSimpleName();
    }

    public Class getAction() {
    	return BasicAction.class;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }
    
    public BasicGameState onDisplayActionComplete(BasicGameState state) {
    	state.setDisplayComplete(true);
    	return state;
    }
    
    public BasicGameState onGamepadDisconnect(BasicGameState state, BasicPlayer player) {
    	player.setConnected(false);
    	return state;
    }

    public BasicGameState onGamepadReconnect(BasicGameState state, BasicPlayer player) {
    	player.setConnected(true);
    	return state;
    }
    
    public BasicGameState onDisplayDisconnect(BasicGameState state) {
    	state.setDisplayConnected(false);
    	return state;
    }
    
    public BasicGameState onDisplayReconnect(BasicGameState state) {
    	state.setDisplayConnected(true);
    	return state;
    }
}
