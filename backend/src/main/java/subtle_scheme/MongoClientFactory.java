package subtle_scheme;

import com.mongodb.MongoClient;

/*
 * This factory simplifies client creation and can be later used to limit the number of active clients
 */
public class MongoClientFactory {

    public static MongoClient getClient() {
        String address = Config.getProperty("mongo.address");
        int port = Integer.parseInt(Config.getProperty("mongo.port"));
        return new MongoClient(address, port);
    }
}
