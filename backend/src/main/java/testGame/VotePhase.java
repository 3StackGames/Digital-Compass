package testGame;

import com.three_stack.digital_compass.backend.GameState;
import com.three_stack.digital_compass.backend.Phase;
import org.json.JSONObject;

/**
 * Created by Hyunbin on 9/16/15.
 */
public class VotePhase extends Phase{

    @Override
    public GameState processAction(JSONObject action, GameState gameState) {
        SSGameState state = (SSGameState) gameState;

        // action JSONObject contains a player and response


        return state;
    }


}