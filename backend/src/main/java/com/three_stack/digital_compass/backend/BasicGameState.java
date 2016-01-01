package com.three_stack.digital_compass.backend;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicGameState {
    protected transient BasicPhase currentPhase;
    
    private String currentPhaseName; //only for gson

    protected String gameCode;

    protected List<BasicPlayer> players = new ArrayList<>();
    
    protected boolean displayComplete = false;
    
    protected boolean displayConnected = true;

	public BasicGameState() {

    }

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

    public void resetGame() {
        resetPlayerScores();
    }

    protected void resetPlayerScores() {
        for (BasicPlayer player : players) {
            player.setScore(0);
        }
    }

    public BasicPhase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(BasicPhase currentPhase) {
        this.currentPhase = currentPhase;
    }
    
    public String getCurrentPhaseName() {
    	return currentPhase.getPhaseName();
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
