package subtle_scheme;

import com.three_stack.digital_compass.backend.BasicAction;
import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;

public class RevealPhase extends BasicPhase {
	static {
		setAction(RevealAction.class);
	}
	@Override
	public BasicGameState processAction(BasicAction action, BasicGameState state) {
		GameState gameState = (GameState) state;
        RevealAction revealAction = (RevealAction) action;
        
		if(revealAction.isMoveOn()) {
			Instruction instruction = gameState.getCurrentInstruction();
			gameState.prepareForNewQuestion();
			if(gameState.getQuestionCount() < instruction.getQuestionLimit()) {
				//new round
				gameState.transitionPhase(new LiePhase());
			} else {
				//game over
				gameState.transitionPhase(new EndPhase());
			}
		} else {
			System.out.println("Uh Oh. Received a weird RevealAction");
		}
		return gameState;
	}
}
