package testGame;

import com.three_stack.digital_compass.backend.BasicGameState;

import java.util.ArrayList;

public class GameState extends BasicGameState {

    private Question currentQuestion;
    
    private ArrayList<Lie> lies = new ArrayList<>();
   
    private final Instruction currentInstruction;
    
    private int voteCount = 0;
    private int questionCount = 0;
    
    public void prepareForNewQuestion() {
    	lies = new ArrayList<>();
    }
    
    @Override
    public void resetGame() {
    	super.resetGame();
    	prepareForNewQuestion();
    	questionCount = 0;
    }
    
    public void incrementVoteCount() {
    	voteCount++;
    }
    public void incrementQuestionCount() {
    	questionCount++;
    }
    
    public GameState(){
        setCurrentPhase(new LiePhase());
        currentInstruction = new Instruction(10, 20, "Here is a new instruction", 5);
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

	public Question getCurrentQuestion() {
		return currentQuestion;
	}

	public void setCurrentQuestion(Question question) {
		this.currentQuestion = question;
	}

}