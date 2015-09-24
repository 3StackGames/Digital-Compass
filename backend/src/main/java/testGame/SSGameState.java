package testGame;

import com.three_stack.digital_compass.backend.GameState;

public class SSGameState extends GameState {

    public SSGameState(){
        setCurrentPhase(new InitialPhase());
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