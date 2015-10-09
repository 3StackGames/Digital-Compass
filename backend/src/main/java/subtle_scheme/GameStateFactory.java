package subtle_scheme;

import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicGameStateFactory;

/**
 * Created by Hyunbin on 9/23/15.
 */
public class GameStateFactory extends BasicGameStateFactory {
    @Override
    public BasicGameState createState() {
    	GameState gs = new GameState();
    	gs.transitionPhase(new LiePhase());
        return new GameState();
    }
}
