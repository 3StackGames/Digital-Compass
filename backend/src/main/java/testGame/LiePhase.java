package testGame;

import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;
import org.json.JSONObject;

/**
 * Created by Hyunbin on 9/16/15.
 */
public class LiePhase extends BasicPhase{

    @Override
    public BasicGameState processAction(JSONObject action, BasicGameState gameState) {
        SSGameState state = (SSGameState) gameState;

        // TODO: @HyunbinTodo: Should pull question randomly from bank
        state.setCurrentQuestion("Who is Casper?");

        state.setCurrentPhase(new VotePhase());
        return state;
    }
}
