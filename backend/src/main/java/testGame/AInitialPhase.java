package testGame;

import com.three_stack.digital_compass.backend.*;

public class AInitialPhase extends InitialPhase {
	public GameState begin(Player[] players) {
		AGameState state = new AGameState();
		state.setPlayers(players);
		state.setCurrentPhase(new AMainPhase());
		return state;
	}
}
