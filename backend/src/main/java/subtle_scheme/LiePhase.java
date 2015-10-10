package subtle_scheme;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.three_stack.digital_compass.backend.BasicAction;
import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;
import org.bson.types.ObjectId;

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

        //Connect to MongoDB
        MongoClient mongoClient = new MongoClient(Config.MONGO_ADDRESS, Config.MONGO_PORT);
        MongoDatabase db = mongoClient.getDatabase(Config.MONGO_DATABASE);
        MongoCollection<Document> packCollection = db.getCollection(Config.MONGO_PACK_COLLECTION);
        MongoCollection<Document> questionCollection = db.getCollection(Config.MONGO_QUESTION_COLLECTION);
        mongoClient.close();

        gameState.setCurrentQuestion(getNewQuestion(gameState, packCollection, questionCollection));
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

    private Question getNewQuestion(GameState gameState, MongoCollection<Document> packCollection, MongoCollection<Document> questionCollection) {
        List<Document> allQuestions = getAllUnaskedQuestions(gameState, packCollection, questionCollection);
        //filter out questions we've already asked
        Iterator<Document> allQuestionsIterator = allQuestions.iterator();

//        //filter out questions we've already asked
//        Iterator<Document> allQuestionsIterator = allQuestions.iterator();
//        while(allQuestionsIterator.hasNext()) {
//            String curQuestionId = generateQuestionId(allQuestionsIterator.next(),)
//            for(String questionId : gameState.getQuestionIds()) {
//                String curQuestionId =
//            }
//        }
//        for(Document question : allQuestions) {
//            for(String questionId : gameState.getQuestionIds()) {
//
//            }
//        }
//
//        //Random Selection
//        Random random = new Random();
//
//        //Search until we find one we haven't used
//        String questionId = null;
//        Document chosenQuestion = null;
//        int attempts = 0;
//        do {
//            //pick a pack
//            Document chosenPack = packs.get(random.nextInt(packs.size()));
//            //pick a Question
//            List<Document> questions = (List<Document>) chosenPack.get("questions");
//            int questionIndex = random.nextInt(questions.size());
//            chosenQuestion = questions.get(random.nextInt(questions.size()));
//            questionId = generateQuestionId(chosenPack, questionIndex);
//        } while(gameState.getQuestionIds().contains(questionId) && attempts < MAX_RANDOM_QUESTION_SEARCH_ATTEMPTS);
//
//        //TODO: Find a long-term solution to running out of questions
//        if(attempts >= MAX_RANDOM_QUESTION_SEARCH_ATTEMPTS) {
//            System.out.println("No new questions found");
//            throw new IllegalStateException("No new questions found after 100 attempts");
//        } else {
//            //add question to list of questions we've used
//            gameState.getQuestionIds().add(questionId);
//            //Create Question Object
//            Question newQuestion = new Question(chosenQuestion.getString("prompt"), chosenQuestion.getString("answer"));
//            return newQuestion;
//        }
    }

    private List<Document> getAllUnaskedQuestions(GameState gameState, MongoCollection<Document> packCollection, MongoCollection<Document> questionCollection) {
        List<Document> allUnaskedQuestions = new ArrayList<>();
        List<Document> packs = getPacks(gameState, packCollection);

        List<Document> packIds = new ArrayList<>();
        Document clause;
        for(Document pack : packs) {
            clause = new Document("_id", pack.getObjectId("_id"));
            packIds.add(clause);
        }
        Document query = new Document("$or", packIds);
        MongoCursor<Document> allQuestionsIterator = questionCollection.find(query).iterator();

        //add unasked questions to list
        while(allQuestionsIterator.hasNext()) {
            Que
            for(ObjectId questionId : gameState.getQuestionIds()) {
                if(question)
            }
            allUnaskedQuestions.add(allQuestionsIterator.next());
        }

        return allUnaskedQuestions;
    }

    private List<Document> getPacks(GameState gameState, MongoCollection<Document> packCollection) {
        //get relevant packs
        //TODO: Only get requested packs
        List<Document> packs = new ArrayList<>();
        MongoCursor<Document> cursor = packCollection.find().iterator();
        while (cursor.hasNext()) {
            packs.add(cursor.next());
        }
        cursor.close();
        return packs;
    }
}