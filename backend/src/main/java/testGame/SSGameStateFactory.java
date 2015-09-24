package testGame;

import com.three_stack.digital_compass.backend.GameState;
import com.three_stack.digital_compass.backend.GameStateFactory;

/**
 * Created by Hyunbin on 9/23/15.
 */
public class SSGameStateFactory extends GameStateFactory {
    @Override
    public GameState createState() {
        return new SSGameState();
    }
}
