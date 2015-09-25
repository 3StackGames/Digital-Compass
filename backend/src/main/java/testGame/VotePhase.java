package testGame;

import com.three_stack.digital_compass.backend.BasicAction;
import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;

/**
 * Created by Hyunbin on 9/16/15.
 */
public class VotePhase extends BasicPhase {

    public BasicGameState processAction(BasicAction action, BasicGameState gameState) {
        SSGameState state = (SSGameState) gameState;

        return state;
    }


}
