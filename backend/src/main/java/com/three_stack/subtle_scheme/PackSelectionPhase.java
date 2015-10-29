package com.three_stack.subtle_scheme;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.three_stack.digital_compass.backend.BasicAction;
import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class PackSelectionPhase extends BasicPhase {

    @Override
    public Class getAction() {
        return PackSelectionAction.class;
    }

    @Override
    public void setup(BasicGameState state) {
        GameState gameState = (GameState) state;
        gameState.setPackOptions(getAllPackNames());
    }

    @Override
    public BasicGameState processAction(BasicAction action, BasicGameState state) {
        GameState gameState = (GameState) state;
        PackSelectionAction packSelectionAction = (PackSelectionAction) action;
        //only take input from the first player
        String firstPlayerName = gameState.getPlayers().get(0).getDisplayName();
        if(packSelectionAction.getPlayer().equals(firstPlayerName)) {
            gameState.setPacks(packSelectionAction.getPacks());
            gameState.transitionPhase(new LiePhase());
        }
        return gameState;
    }



    private List<String> getAllPackNames() {
        //Get list of all Packs from MongoDB
        MongoClient mongoClient = MongoClientFactory.getClient();
        MongoDatabase db = mongoClient.getDatabase(Config.MONGO_DATABASE);
        MongoCollection<Document> packCollection = db.getCollection(Config.MONGO_PACK_COLLECTION);

        List<String> packs = new ArrayList<>();
        MongoCursor<Document> cursor = packCollection.find().iterator();
        while (cursor.hasNext()) {
            packs.add(cursor.next().getString("name"));
        }
        cursor.close();
        mongoClient.close();
        return packs;
    }
}
