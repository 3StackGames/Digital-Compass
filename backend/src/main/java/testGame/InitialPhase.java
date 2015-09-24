package testGame;

import com.three_stack.digital_compass.backend.GameState;
import com.three_stack.digital_compass.backend.Phase;
import org.json.JSONObject;

public class InitialPhase extends Phase {

    @Override
	public GameState processAction(JSONObject action, GameState state) {
        state.setCurrentPhase(new LiePhase());
        return state;
	}

}
