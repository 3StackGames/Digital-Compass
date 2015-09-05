package testGame;

import com.three_stack.digital_compass.backend.*;

import java.util.List;

public class AInitialPhase extends InitialPhase {
	public GameState begin(List<Player> players) {
		AGameState state = new AGameState();
		state.setPlayers(players);
		state.setCurrentPhase(new AMainPhase());
		return state;
	}
}
