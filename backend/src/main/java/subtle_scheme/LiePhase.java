package subtle_scheme;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.three_stack.digital_compass.backend.BasicAction;
import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;

/**
 * Created by Hyunbin on 9/16/15.
 */
public class LiePhase extends BasicPhase {
    private static final int MAX_RANDOM_QUESTION_SEARCH_ATTEMPTS = 100;

    public LiePhase() {
        super();
        setAction(LieAction.class);
    }

    @Override
    public BasicGameState setup(BasicGameState state) {
        GameState gameState = (GameState) state;
        gameState.setCurrentQuestion(getNewQuestion(gameState));
        return gameState;
    }

    @Override
    public BasicGameState processAction(BasicAction action, BasicGameState state) {
        GameState gameState = (GameState) state;
        LieAction lieAction = (LieAction) action;

        if (lieAction != null) {
            // Process player input
            Lie lie = new Lie(lieAction.getLie(), lieAction.getPlayer());
            gameState.getLies().add(lie);
            if (gameState.getLies().size() == gameState.getPlayers().size()) {
                gameState.transitionPhase(new VotePhase());
            }
        }
        return gameState;
    }

    private Question getNewQuestion(GameState gameState) {
        List<Document> allQuestions = getAllQuestions(gameState);

        //filter out questions we've already asked
        Iterator<Document> allQuestionsIterator = allQuestions.iterator();
        while(allQuestionsIterator.hasNext()) {
            String curQuestionId = generateQuestionId(allQuestionsIterator.next(),)
            for(String questionId : gameState.getQuestionIds()) {
                String curQuestionId =
            }
        }
        for(Document question : allQuestions) {
            for(String questionId : gameState.getQuestionIds()) {

            }
        }

        //Random Selection
        Random random = new Random();

        //Search until we find one we haven't used
        String questionId = null;
        Document chosenQuestion = null;
        int attempts = 0;
        do {
            //pick a pack
            Document chosenPack = packs.get(random.nextInt(packs.size()));
            //pick a Question
            List<Document> questions = (List<Document>) chosenPack.get("questions");
            int questionIndex = random.nextInt(questions.size());
            chosenQuestion = questions.get(random.nextInt(questions.size()));
            questionId = generateQuestionId(chosenPack, questionIndex);
        } while(gameState.getQuestionIds().contains(questionId) && attempts < MAX_RANDOM_QUESTION_SEARCH_ATTEMPTS);

        //TODO: Find a long-term solution to running out of questions
        if(attempts >= MAX_RANDOM_QUESTION_SEARCH_ATTEMPTS) {
            System.out.println("No new questions found");
            throw new IllegalStateException("No new questions found after 100 attempts");
        } else {
            //add question to list of questions we've used
            gameState.getQuestionIds().add(questionId);
            //Create Question Object
            Question newQuestion = new Question(chosenQuestion.getString("prompt"), chosenQuestion.getString("answer"));
            return newQuestion;
        }
    }

    private List<Document> getAllQuestions(GameState gameState) {
        List<Document> packs = getPacks(gameState);
        List<Document> allQuestions = new ArrayList<>();

        for(Document pack : packs) {
            List<Document> questions = (List<Document>) pack.get("questions");
            int index = 0;
            //assign each 
            for(Document question : questions) {
                question.append("id",generateQuestionId(pack,index++));
            }
            allQuestions.addAll(questions);
        }

        return allQuestions;
    }

    private List<Document> getPacks(GameState gameState) {
        //TODO: Extract this elsewhere to improve performance
        //Connect to MongoDB
        MongoClient mongoClient = new MongoClient(Config.MONGO_ADDRESS, Config.MONGO_PORT);
        MongoDatabase db = mongoClient.getDatabase(Config.MONGO_DATABASE);
        MongoCollection<Document> collection = db.getCollection(Config.MONGO_PACK_COLLECTION);
        //get all packs
        List<Document> packs = new ArrayList<>();
        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            packs.add(cursor.next());
        }
        cursor.close();
        mongoClient.close();
        return packs;
    }

    private String generateQuestionId(Document pack, int index) {
        return pack.get("_id").toString() + "-" + index;
    }
}