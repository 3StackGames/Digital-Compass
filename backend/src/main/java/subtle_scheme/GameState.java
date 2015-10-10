package subtle_scheme;

import com.three_stack.digital_compass.backend.BasicGameState;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class GameState extends BasicGameState {

    private Question currentQuestion;

    //track which questions have been asked
    //question id = [packId]-[questionIndex]
    private transient List<ObjectId> questionIds;

    private List<Lie> lies = new ArrayList<>();

    private final Instruction currentInstruction;

    private int voteCount;
    private int questionCount;

    public GameState(Instruction instruction) {
        this.currentInstruction = instruction;
        this.questionIds = new ArrayList<>();
        prepareForNewQuestion();
    }

    public void prepareForNewQuestion() {
        lies = new ArrayList<>();
        currentQuestion = null;
        voteCount = 0;
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

    public Instruction getCurrentInstruction() {
        return currentInstruction;
    }

    public List<Lie> getLies() {
        return lies;
    }


    public int getVoteCount() {
        return voteCount;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question question) {
        incrementQuestionCount();
        this.currentQuestion = question;
    }

    public List<ObjectId> getQuestionIds() {
        return questionIds;
    }
}