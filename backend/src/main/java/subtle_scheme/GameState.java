package subtle_scheme;

import com.three_stack.digital_compass.backend.BasicGameState;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameState extends BasicGameState {

    private Question currentQuestion;

    private List<String> packs;

    private transient Set<ObjectId> askedQuestionIds;

    private List<Lie> lies = new ArrayList<>();

    private final Instruction currentInstruction;

    private int voteCount;
    private int questionCount;

    public GameState(Instruction instruction) {
        this.currentInstruction = instruction;
        this.askedQuestionIds = new HashSet<>();
        this.packs = new ArrayList<>();
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

    public void setCurrentQuestion(ObjectId objectId, Question question) {
        incrementQuestionCount();
        this.askedQuestionIds.add(objectId);
        this.currentQuestion = question;

    }

    public Set<ObjectId> getAskedQuestionIds() {
        return askedQuestionIds;
    }

    public List<String> getPacks() {
        return packs;
    }

    public void setPacks(List<String> packs) {
        this.packs = packs;
    }
}