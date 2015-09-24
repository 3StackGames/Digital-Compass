package testGame;

import com.three_stack.digital_compass.backend.GameState;
import com.three_stack.digital_compass.backend.Phase;
import org.json.JSONObject;

/**
 * Created by Hyunbin on 9/16/15.
 */
public class LiePhase extends Phase{

    @Override
    public GameState processAction(JSONObject action, GameState gameState) {
        AGameState state = (AGameState) gameState;

        // TODO: @HyunbinTodo: Should pull question randomly from bank
        state.setCurrentQuestion("Who is Casper?");

        state.setCurrentPhase(new VotePhase());
        return state;
    }
}
