package com.mongodb.quickstart;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Connection {

    public static void main(String[] args) {
        //String connectionString = System.getProperty("mongodb.uri");
        ConnectionString connectionString = new ConnectionString("mongodb+srv://MongoRotem:raj5BNGXXKcKOzUE@cluster0.9wrug.mongodb.net/?retryWrites=true&w=majority");
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            List<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
            databases.forEach(db -> System.out.println(db.toJson()));
        }
    }
}
