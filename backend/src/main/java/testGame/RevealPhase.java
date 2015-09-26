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
			resetRound(gameState);
			Instruction instruction = gameState.getCurrentInstruction();
			if(gameState.getQuestionCount() < instruction.getQuestionCount()) {
				//new round
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

	private void resetRound(GameState gameState) {
		// TODO Auto-generated method stub
		
	}
	
	

}
