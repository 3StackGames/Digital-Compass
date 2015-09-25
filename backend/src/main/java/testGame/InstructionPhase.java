package testGame;

import com.three_stack.digital_compass.backend.BasicAction;
import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;

public class InstructionPhase extends BasicPhase {

    public BasicGameState processAction(BasicAction action, BasicGameState gameState) {
        SSGameState state = (SSGameState) gameState;
        Instruction instruction = state.getCurrentInstruction();

        if(instruction == null){
            // First time in instruction phase, so set new instruction
            instruction = new Instruction(5, 10, "Round begins!");
            state.setCurrentInstruction(instruction);
        } else {
            // Doubles points awarded at the start of new round from previous instruction
            instruction.setCorrectAnswerPointValue(2 * instruction.getCorrectAnswerPointValue());
            instruction.setTrickBonusPointValue(2 * instruction.getTrickBonusPointValue());
        }

        state.setCurrentPhase(new LiePhase());
        return state;
    }

}
