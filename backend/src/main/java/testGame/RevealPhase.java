package testGame;

import com.three_stack.digital_compass.backend.BasicAction;
import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;

public class RevealPhase extends BasicPhase {
	public RevealPhase() {
		super();
		setAction(RevealAction.class);
	}
	@Override
	public BasicGameState processAction(BasicAction action, BasicGameState state) {
		GameState gameState = (GameState) state;
        RevealAction revealAction = (RevealAction) action;
        
		if(revealAction.isMoveOn()) {
			Instruction instruction = gameState.getCurrentInstruction();
			if(gameState.getQuestionCount() < instruction.getQuestionLimit()) {
				//new round
				gameState.prepareForNewQuestion();
				gameState.setCurrentPhase(new LiePhase());
			} else {
				//game over
				gameState.setCurrentPhase(new EndPhase());
			}
		} else {
			System.out.println("Uh Oh. Received a weird RevealAction");
		}
		return gameState;
	}
}
