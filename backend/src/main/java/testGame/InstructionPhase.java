package testGame;

import com.three_stack.digital_compass.backend.GameState;
import com.three_stack.digital_compass.backend.Phase;
import org.json.JSONObject;

public class InstructionPhase extends Phase {

    @Override
    public GameState processAction(JSONObject action, GameState gameState) {
        AGameState state = (AGameState) gameState;
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

        state.setCurrentPhase(new QuestionPhase());
        return state;
    }
}
