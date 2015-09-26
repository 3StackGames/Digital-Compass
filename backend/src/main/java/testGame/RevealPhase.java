package testGame;

import com.three_stack.digital_compass.backend.BasicAction;
import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;

public class RevealPhase extends BasicPhase {

	@Override
	public BasicGameState processAction(BasicAction action, BasicGameState state) {
		GameState gameState = (GameState) state;
        RevealAction revealAction = (RevealAction) action;
        
		// TODO Auto-generated method stub
		if(revealAction.isMoveOn()) {
			gameState.setCurrentPhase(new ScorePhase());
		} else {
			System.out.println("Uh Oh. Received a weird RevealAction");
		}
		return gameState;
	}

}
