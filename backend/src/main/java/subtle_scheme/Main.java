package subtle_scheme;

import com.three_stack.digital_compass.backend.PhaseManager;

public class Main {
    public static void main(String args[]){
        System.out.println("Starting Subtle Scheme v1e-09");

        PhaseManager phaseManager = new PhaseManager();
        phaseManager.initialize("http://localhost:3333", new GameStateFactory());
    }
}
