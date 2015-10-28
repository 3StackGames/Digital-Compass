package com.three_stack.subtle_scheme;

import com.three_stack.digital_compass.backend.BasicAction;
import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;

public class EndPhase extends BasicPhase {

	@Override
	public Class getAction() {
		return EndAction.class;
	}
	
	@Override
	public BasicGameState processAction(BasicAction action, BasicGameState state) {
		GameState gameState = (GameState) state;
		EndAction revealAction = (EndAction) action;

        if(revealAction.isRestart()){
            gameState.resetGame();
            gameState.transitionPhase(new LiePhase());
        }

		return gameState;
	}

}
