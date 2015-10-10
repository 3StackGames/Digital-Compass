package com.three_stack.digital_compass.backend;

public abstract class BasicPhase {

    protected transient static Class action;

    //meant primarily for the sake of emitting the name of the phase for frontend
    protected String phaseName;

    //Called before State Updates to clients
    public BasicGameState setup(BasicGameState state) { return state; }

    //Called after receiving Gamepad Input
    public abstract BasicGameState processAction(BasicAction action, BasicGameState state);

    public BasicPhase() {
        phaseName = this.getClass().getSimpleName();
    }

    public Class getAction() {
        return action;
    }

    public static void setAction(Class newAction) {
        action = newAction;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }
}
