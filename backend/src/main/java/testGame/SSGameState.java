package testGame;

import com.three_stack.digital_compass.backend.BasicGameState;

public class SSGameState extends BasicGameState {


    public SSGameState(){
        setCurrentPhase(new LiePhase());
    }

    private String currentQuestion;
    private Instruction currentInstruction;

    public String getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(String currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public Instruction getCurrentInstruction() {
        return currentInstruction;
    }

    public void setCurrentInstruction(Instruction currentInstruction) {
        this.currentInstruction = currentInstruction;
    }
}