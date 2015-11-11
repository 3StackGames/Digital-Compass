package com.three_stack.digital_compass.backend;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicGameState {
    protected BasicPhase currentPhase;

    protected String gameCode;

    protected List<BasicPlayer> players = new ArrayList<>();
    
    protected boolean displayComplete = false;
    
    protected boolean displayConnected = true;

    public boolean isDisplayConnected() {
		return displayConnected;
	}

	public void setDisplayConnected(boolean displayConnected) {
		this.displayConnected = displayConnected;
	}

	public boolean isDisplayComplete() {
		return displayComplete;
	}

	public void setDisplayComplete(boolean displayComplete) {
		this.displayComplete = displayComplete;
	}

	public BasicGameState() {

    }

    public void resetGame() {
        resetPlayerScores();
    }

    protected void resetPlayerScores() {
        for (BasicPlayer player : players) {
            player.setScore(0);
        }
    }

    /*
     * sets the current phase and runs any pre-methods
     */
    public void transitionPhase(BasicPhase currentPhase) {
        this.currentPhase = currentPhase;
        currentPhase.setup(this);
    }

    public BasicPhase getCurrentPhase() {
        return currentPhase;
    }

    /*
     * @deprecated
     * Use transitionPhase() instead, since it calls the setup method.
     */
    @Deprecated
    public void setCurrentPhase(BasicPhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    /**
     * @return The gameCode
     */
    public String getGameCode() {
        return gameCode;
    }

    /**
     * @param gameCode The gameCode
     */
    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    /**
     * @return The players
     */
    public List<BasicPlayer> getPlayers() {
        return players;
    }

    /**
     * @param players The players
     */
    public void setPlayers(List<BasicPlayer> players) {
        this.players = players;
    }
    
    public BasicPlayer getPlayerByName(String displayName) {
    	for (BasicPlayer player : players) {
    		if (player.getDisplayName().equals(displayName)) {
    			return player;
    		}
    	}
    	
    	return null;
    }

    enum Action {
        DISPLAY, GAMEPAD, SERVER
    }

}
