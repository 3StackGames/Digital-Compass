package testGame;

import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicGameStateFactory;

/**
 * Created by Hyunbin on 9/23/15.
 */
public class SSGameStateFactory extends BasicGameStateFactory {
    @Override
    public BasicGameState createState() {
        return new SSGameState();
    }
}
