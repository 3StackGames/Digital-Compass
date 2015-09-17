package testGame;

import com.three_stack.digital_compass.backend.GameState;
import com.three_stack.digital_compass.backend.InitialPhase;
import org.json.JSONObject;

public class AInitialPhase extends InitialPhase {
	public GameState begin(AGameState state) {
		state.setCurrentPhase(new AMainPhase());
		return state;
	}
	
	public GameState processAction(JSONObject action, GameState state) {
		return null;
	}
}
