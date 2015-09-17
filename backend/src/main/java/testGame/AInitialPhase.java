package testGame;

import com.three_stack.digital_compass.backend.GameState;
import com.three_stack.digital_compass.backend.InitialPhase;

public class AInitialPhase extends InitialPhase {
    public GameState begin(AGameState state) {
        state.setCurrentPhase(new InstructionPhase());
        return state;
    }


}