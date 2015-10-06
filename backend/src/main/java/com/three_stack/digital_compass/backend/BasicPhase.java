package com.three_stack.digital_compass.backend;

import com.google.gson.annotations.Expose;

public abstract class BasicPhase {

    protected Class action;
    
    //meant primarily for the sake of emitting the name of the phase for frontend
    @Expose
    protected static String phaseName;
    
	public abstract BasicGameState processAction(BasicAction action, BasicGameState state);
	
	public BasicPhase() {
		phaseName = this.getClass().getSimpleName();
	}
	
    public Class getAction() {
        return action;
    }

    public void setAction(Class action) {
        this.action = action;
    }

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}
}
