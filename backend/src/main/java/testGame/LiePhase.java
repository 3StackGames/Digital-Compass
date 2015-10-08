package testGame;

import com.three_stack.digital_compass.backend.BasicAction;
import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;

/**
 * Created by Hyunbin on 9/16/15.
 */
public class LiePhase extends BasicPhase {

	public LiePhase(GameState gameState) {
		super();
		setAction(LieAction.class);
        setup(gameState);
	}

    private void setup(GameState gameState){
        // The case where we're on a new question cycle
        // TODO: @Hyunbin: Should pull question randomly from bank
        // TODO: @Hyunbin: Set correct answer in the game state to prevent duplicate right answers
        Question question = new Question("Who is Casper?", "My Best Friend");
        gameState.setCurrentQuestion(question);
        gameState.incrementQuestionCount();
    }

    public BasicGameState processAction(BasicAction action, BasicGameState state) {
        GameState gameState = (GameState) state;
        LieAction lieAction = (LieAction) action;

        if(lieAction != null){
            // Process player input
            Lie lie = new Lie(lieAction.getLie(), lieAction.getPlayer());
            gameState.getLies().add(lie);
            if(gameState.getLies().size() == gameState.getPlayers().size()){
                gameState.setCurrentPhase(new VotePhase());
            }
        }
        return gameState;
    }
}