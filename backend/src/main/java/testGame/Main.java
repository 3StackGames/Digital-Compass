package testGame;

import com.three_stack.digital_compass.backend.GameState;
import com.three_stack.digital_compass.backend.PhaseManager;

/**
 * Created by Hyunbin on 9/15/15.
 */
public class Main {
    public static void main(String args[]){
        System.out.println("Starting Subtle Scheme v1e-09");
        GameState defaultGameState = new AGameState();
        PhaseManager phaseManager = new PhaseManager();
        phaseManager.initialize(null, defaultGameState);
    }
}
