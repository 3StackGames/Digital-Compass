package com.three_stack.subtle_scheme;

import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicGameStateFactory;

/**
 * Created by Hyunbin on 9/23/15.
 */
public class GameStateFactory extends BasicGameStateFactory {
    @Override
    public BasicGameState createState() {
    	GameState gs = new GameState(new Instruction(10, 20, "Here is a new instruction", 5));
        gs.transitionPhase(new PackSelectionPhase());
        return gs;
    }
}
