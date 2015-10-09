package subtle_scheme;

import com.three_stack.digital_compass.backend.BasicAction;
import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;

public class EndPhase extends BasicPhase {
	
	public EndPhase() {
		super();
		setAction(EndAction.class);
	}
	
	@Override
	public BasicGameState processAction(BasicAction action, BasicGameState state) {
		GameState gameState = (GameState) state;
		EndAction revealAction = (EndAction) action;

        if(revealAction.isRestart()){
            gameState.resetGame();
            gameState.setCurrentPhase(new LiePhase(gameState));
        }

		return gameState;
	}

}
