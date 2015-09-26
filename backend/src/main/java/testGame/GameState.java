package testGame;

import com.three_stack.digital_compass.backend.BasicGameState;

import java.util.ArrayList;

public class GameState extends BasicGameState {

    private String currentQuestion;
    private int questionCount = 0;
    private ArrayList<Lie> lies = new ArrayList<>();
   
    private final Instruction currentInstruction;
    
    private int voteCount = 0;
    
    public void prepareForNewQuestion() {
    	currentQuestion = null;
    	lies = new ArrayList<>();
    }
    
    @Override
    public void resetGame() {
    	super.resetGame();
    	prepareForNewQuestion();
    	questionCount = 0;
    }
    
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