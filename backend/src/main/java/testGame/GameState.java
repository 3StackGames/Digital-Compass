package testGame;

import com.three_stack.digital_compass.backend.BasicGameState;

import java.util.ArrayList;

public class GameState extends BasicGameState {

    private String currentQuestion;
    private Instruction currentInstruction;
    private ArrayList<Lie> lies = new ArrayList<>();

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

	public ArrayList<Lie> getLies() {
		return lies;
	}

	public void setLies(ArrayList<Lie> lies) {
		this.lies = lies;
	}
}