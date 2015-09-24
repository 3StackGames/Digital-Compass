package testGame;

import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;
import org.json.JSONObject;

/**
 * Created by Hyunbin on 9/16/15.
 */
public class VotePhase implements BasicPhase {

    public BasicGameState processAction(JSONObject action, BasicGameState gameState) {
        SSGameState state = (SSGameState) gameState;

        // action JSONObject contains a player and response


        return state;
    }


}
