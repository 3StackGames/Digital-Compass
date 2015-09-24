package testGame;

import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;
import org.json.JSONObject;

public class InitialPhase extends BasicPhase {

    @Override
	public BasicGameState processAction(JSONObject action, BasicGameState state) {
        state.setCurrentPhase(new LiePhase());
        return state;
	}

}
