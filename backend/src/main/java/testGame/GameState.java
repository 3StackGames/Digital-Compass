package testGame;

import com.three_stack.digital_compass.backend.BasicGameState;

import java.util.ArrayList;

public class GameState extends BasicGameState {

    private String currentQuestion;
    private int questionCount = 0;
    private Instruction currentInstruction;
    private ArrayList<Lie> lies = new ArrayList<>();
    
    private int voteCount = 0;
    
    public void incrementQuestionCount() {
    	questionCount++;
    }
    
    public void resetQuestionCount() {
    	questionCount = 0;
    }
    
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

	public int getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}

	public int getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}
}