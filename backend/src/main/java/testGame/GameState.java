package testGame;

import com.three_stack.digital_compass.backend.BasicGameState;

import java.util.ArrayList;

public class GameState extends BasicGameState {

    private String currentQuestion;
    private Instruction currentInstruction;
    private ArrayList<LieAction> lieActions = new ArrayList<>();

    public GameState(){
        setCurrentPhase(new LiePhase());
    }

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

    public ArrayList<LieAction> getLieActions() {
        return lieActions;
    }

    public void setLieActions(ArrayList<LieAction> lieActions) {
        this.lieActions = lieActions;
    }
}