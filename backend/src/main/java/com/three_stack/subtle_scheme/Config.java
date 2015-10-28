package com.three_stack.subtle_scheme;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static final String CONFIG_FILE = "development.properties";

    public static final String MONGO_ID_ATTRIBUTE = "_id";

    public static final String MONGO_PACK_COLLECTION = "Packs";
    public static final String MONGO_QUESTION_COLLECTION = "Questions";
    public static final String MONGO_DATABASE = "SubtleScheme";

    public static final String PROMPT_ATTRIBUTE = "prompt";
    public static final String ANSWER_ATTRIBUTE = "answer";
    public static final String PACK_ATTRIBUTE = "pack";
    public static final String PACK_NAME = "name";

    private static Properties properties = new Properties();

    static {
        InputStream input = null;
        try {
            input = Config.class.getClass().getResourceAsStream("/" + CONFIG_FILE);
            properties.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("[Exception] File not found: " + CONFIG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[Exception] IO Exception while reading " + CONFIG_FILE);
        }
    }

    public static String getProperty(String property) {
        return properties.getProperty(property);
    }
}
