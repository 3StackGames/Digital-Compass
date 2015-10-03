package com.three_stack.digital_compass.backend;

public abstract class BasicPhase {

    protected Class action;
    
    //meant primarily for the sake
    protected String phaseName;
    
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
