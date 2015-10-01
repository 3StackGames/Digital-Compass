package testGame;

import com.three_stack.digital_compass.backend.PhaseManager;

/**
 * Created by Hyunbin on 9/15/15.
 */
public class Main {
    public static void main(String args[]){
        System.out.println("Starting Subtle Scheme v1e-09");

        PhaseManager phaseManager = new PhaseManager();
        phaseManager.initialize("http://localhost:3333", new GameStateFactory());
    }
}
