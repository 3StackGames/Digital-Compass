package com.three_stack.digital_compass.backend;

public abstract class BasicPhase {

    protected Class action;

	public abstract BasicGameState processAction(BasicAction action, BasicGameState state);

    public Class getAction() {
        return action;
    }

    public void setAction(Class action) {
        this.action = action;
    }
}
