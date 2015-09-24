package testGame;

import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;
import org.json.JSONObject;

/**
 * Created by Hyunbin on 9/16/15.
 */
public class LiePhase implements BasicPhase {

    public BasicGameState processAction(JSONObject action, BasicGameState gameState) {
        SSGameState state = (SSGameState) gameState;

        if(state.getCurrentQuestion() == null){
            // TODO: @Hyunbin: Should pull question randomly from bank
            state.setCurrentQuestion("Who is Casper?");
        } else{

        }
        return state;
    }
}
