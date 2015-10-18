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
import org.bson.types.ObjectId;

public class LiePhase extends BasicPhase {

    @Override
    public Class getAction() {
        return LieAction.class;
    }

    @Override
    public void setup(BasicGameState state) {
        GameState gameState = (GameState) state;

        //Connect to MongoDB
        MongoClient mongoClient = MongoClientFactory.getClient();
        MongoDatabase db = mongoClient.getDatabase(Config.MONGO_DATABASE);
        MongoCollection<Document> packCollection = db.getCollection(Config.MONGO_PACK_COLLECTION);
        MongoCollection<Document> questionCollection = db.getCollection(Config.MONGO_QUESTION_COLLECTION);

        Document newQuestionDocument = getNewQuestion(gameState, packCollection, questionCollection);
        mongoClient.close();
        //Create Question Object
        Question newQuestion = new Question(newQuestionDocument.getString(Config.PROMPT_ATTRIBUTE), newQuestionDocument.getString(Config.ANSWER_ATTRIBUTE));
        gameState.setCurrentQuestion(newQuestionDocument.getObjectId(Config.MONGO_ID_ATTRIBUTE), newQuestion);
    }

	@Override
	public BasicGameState processAction(BasicAction action, BasicGameState state) {
		GameState gameState = (GameState) state;
		LieAction lieAction = (LieAction) action;
		
		Lie lie = new Lie(lieAction.getLie(), lieAction.getPlayer());
		gameState.getLies().add(lie);
		if (gameState.getLies().size() == gameState.getPlayers().size()) {
			gameState.transitionPhase(new VotePhase());
		}
		return gameState;
	}

    private Document getNewQuestion(GameState gameState, MongoCollection<Document> packCollection, MongoCollection<Document> questionCollection) {
        List<Document> allUnaskedQuestions = getAllUnaskedQuestions(gameState, packCollection, questionCollection);

        //Randomly Select one
        Random random = new Random();
        int unaskedQuestionCount = allUnaskedQuestions.size();
        if(unaskedQuestionCount < 1) {
            throw new IllegalStateException("Ran out of Questions");
        }
        int randomQuestionIndex = random.nextInt(unaskedQuestionCount);
        return allUnaskedQuestions.get(randomQuestionIndex);
    }

    private List<Document> getAllUnaskedQuestions(GameState gameState, MongoCollection<Document> packCollection, MongoCollection<Document> questionCollection) {
        List<Document> allUnaskedQuestions = new ArrayList<>();
        List<Document> packs = getPacks(gameState, packCollection);

        //Create Query
        Document query;
        //Determine which packs we want
        List<Document> orList = new ArrayList<>();
        Document clause;
        for(Document pack : packs) {
            clause = new Document(Config.PACK_ATTRIBUTE, pack.getObjectId(Config.MONGO_ID_ATTRIBUTE));
            orList.add(clause);
        }
        query = new Document("$or", orList);

        //determine which questions we DON'T want
        List<ObjectId> ninList = new ArrayList<>();
        for(ObjectId questionId : gameState.getAskedQuestionIds()) {
            ninList.add(questionId);
        }
        query.append(Config.MONGO_ID_ATTRIBUTE, new Document("$nin", ninList));
        MongoCursor<Document> allQuestionsIterator = questionCollection.find(query).iterator();

        //add unasked questions to list
        while(allQuestionsIterator.hasNext()) {
            allUnaskedQuestions.add(allQuestionsIterator.next());
        }

        return allUnaskedQuestions;
    }

    private List<Document> getPacks(GameState gameState, MongoCollection<Document> packCollection) {
        List<Document> packs = new ArrayList<>();
        //Create query for specific packs
        List<Document> orList = new ArrayList<>();
        Document clause;
        for(String packName : gameState.getPackOptions()) {
            clause = new Document(Config.PACK_NAME, packName);
            orList.add(clause);
        }
        Document query = new Document("$or", orList);
        //query for packs
        MongoCursor<Document> cursor = packCollection.find(query).iterator();
        while (cursor.hasNext()) {
            packs.add(cursor.next());
        }
        cursor.close();
        return packs;
    }
}