package testGame;

import com.three_stack.digital_compass.backend.BasicAction;
import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;

/**
 * Created by Hyunbin on 9/16/15.
 */
public class LiePhase extends BasicPhase {

    public BasicGameState processAction(BasicAction action, BasicGameState gameState) {
        SSGameState state = (SSGameState) gameState;
        LieAction lieAction = (LieAction) action;

        if(state.getCurrentQuestion() == null){
            // TODO: @Hyunbin: Should pull question randomly from bank
            state.setCurrentQuestion("Who is Casper?");
        } else{

        }
        return state;
    }
}
