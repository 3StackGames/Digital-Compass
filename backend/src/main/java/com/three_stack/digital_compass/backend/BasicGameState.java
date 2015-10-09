package com.three_stack.digital_compass.backend;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicGameState {
    protected BasicPhase currentPhase;

    protected String gameCode;

    protected List<Player> players = new ArrayList<>();

    public BasicGameState() {

    }

    public void resetGame() {
        resetPlayerScores();
    }

    protected void resetPlayerScores() {
        for (Player player : players) {
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
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @param players The players
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    enum Action {
        DISPLAY, GAMEPAD, SERVER
    }

}
