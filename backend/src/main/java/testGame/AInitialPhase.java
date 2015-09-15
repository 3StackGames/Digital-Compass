package testGame;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.three_stack.digital_compass.backend.GameState;
import com.three_stack.digital_compass.backend.InitialPhase;
import com.three_stack.digital_compass.backend.Player;

public class AInitialPhase extends InitialPhase {
	public GameState begin(JSONObject details) throws JSONException {
		JSONArray players = details.getJSONArray("players");
		Player[] playerList = new Player[players.length()];
		for (int i = 0; i < players.length(); i++) {
			JSONObject player = players.getJSONObject(i);
			playerList[i] = new Player(player.getString("name"));
		}
		AGameState state = new AGameState();
		state.setPlayers(playerList);
		state.setCurrentPhase(new AMainPhase());
		return state;
	}
}
