package testGame;

import com.three_stack.digital_compass.backend.BasicAction;
import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;

/**
 * Created by Hyunbin on 9/16/15.
 */
public class LiePhase extends BasicPhase {
	public LiePhase() {
		super();
	}
    public BasicGameState processAction(BasicAction action, BasicGameState state) {
        GameState gameState = (GameState) state;
        LieAction lieAction = (LieAction) action;

        if(gameState.getCurrentQuestion() == null){
            // The case where we're on a new question cycle
            // TODO: @Hyunbin: Should pull question randomly from bank
            // TODO: @Hyunbin: Set correct answer in the game state to prevent duplicate right answers
            gameState.getCurrentQuestion().setQuestion("Who is Casper?");
            gameState.getCurrentQuestion().setAnswer("My Best Friend");
            gameState.incrementQuestionCount();
        } else{
            // Process player input
            Lie lie = new Lie(lieAction.getLie(), lieAction.getPlayer().getDisplayName());
            gameState.getLies().add(lie);
            if(gameState.getLies().size() == gameState.getPlayers().size()){
                gameState.setCurrentPhase(new VotePhase());
            }
        }
        return gameState;
    }
}