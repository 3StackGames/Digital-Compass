package testGame;

import com.google.gson.Gson;
import com.three_stack.digital_compass.backend.GameState;
import com.three_stack.digital_compass.backend.InitialPhase;
import org.json.JSONException;
import org.json.JSONObject;

public class AInitialPhase extends InitialPhase {
	public GameState begin(AGameState state) {
		state.setCurrentPhase(new AMainPhase());
		return state;
	}
}
