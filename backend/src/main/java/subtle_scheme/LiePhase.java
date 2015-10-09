package subtle_scheme;


import java.util.ArrayList;
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
    public LiePhase() {
        super();
        setAction(LieAction.class);
    }

    @Override
    public BasicGameState setup(BasicGameState state) {
        GameState gameState = (GameState) state;
        gameState.setCurrentQuestion(getNewQuestion(gameState));
        gameState.incrementQuestionCount();
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
        //pick a pack
        Random random = new Random();
        Document chosenPack = packs.get(random.nextInt(packs.size()));
        //pick a Question
        List<Document> questions = (List<Document>) chosenPack.get("questions");
        Document chosenQuestion = questions.get(random.nextInt(questions.size()));
        //get a
        //Create Question
        Question newQuestion = new Question(chosenQuestion.getString("prompt"), chosenQuestion.getString("answer"));
        return newQuestion;
    }
}