package com.mongodb.quickstart;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonObject;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class MongoDB {
    public static MongoClient mongoClient;
    public static MongoDatabase MongoProjectDB;
    public static MongoCollection<Document> MP3Collection;

    public static void initializeMongoDb(){
        ConnectionString connectionString = new ConnectionString("mongodb+srv://MongoRotem:raj5BNGXXKcKOzUE@cluster0.9wrug.mongodb.net/?retryWrites=true&w=majority");
        MongoDB.mongoClient = MongoClients.create(connectionString);
        MongoDB.MongoProjectDB = MongoDB.mongoClient.getDatabase("MongoProject");
        MongoDB.MP3Collection = MongoDB.MongoProjectDB.getCollection("MP3");
    }

    private static Document getDocument(MpFile mpFile){
        return new Document("ID", mpFile.getId())
                .append("FileName", mpFile.getFileName())
                .append("Size", mpFile.getSize())
                .append("Length", mpFile.getLength())
                .append("IsValid", mpFile.getValid());
    }
    public static ArrayList<Document> listFilesForFolder(final File folder) {
        ArrayList<Document> mpFilesList = new ArrayList<>();
        try {
            for (final File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    String end = ".mp3";
                    String fileName = fileEntry.getName();
                    if (fileName.contains(end)){
                        System.out.println(fileEntry.getName());
                        Path path = Paths.get(folder.getPath()+ "\\" + fileEntry.getName());
                        long bytes = Files.size(path);
                        long megaBytes = bytes/(1024 * 1024);
                        long rest = bytes%(1024 * 1024);
                        Double size = Double.parseDouble(megaBytes+"."+rest);
                        System.out.println("megabytes:" + megaBytes+"."+rest);
                        MpFile mpFile = new MpFile(fileName, size, MpFile.getDuration((path.toString())), null);
                        mpFilesList.add(getDocument(mpFile));
                    }
                }
            }
            return mpFilesList;
        }
        catch (NullPointerException | UnsupportedAudioFileException | IOException e){
            return null;
        }
    }

    public static void insertManyDocuments(ArrayList<Document> list){
        MP3Collection.insertMany(list, new InsertManyOptions().ordered(false));
        System.out.println("The new MP3 file list was inserted to the DB");
    }

    public static ArrayList<ArrayList<Object>> getData(ArrayList<Document> docs){
        ArrayList<ArrayList<Object>> tableInfo = new ArrayList<>();
        for (int i = 0; i < docs.size(); i++) {
            ArrayList<Object> temp = new ArrayList<>();
            Document d = docs.get(i);
            temp.addAll(d.values());
            tableInfo.add(temp);
        }
        return tableInfo;
    }

    public static void deleteOne(Integer id){
        Bson filter = eq("ID", id);
        DeleteResult result = MP3Collection.deleteOne(filter);
        System.out.println(result);
    }

    public static boolean updateOne(Integer id, Boolean isValid){
        Bson filter = eq("ID", id);
        Bson updateOperation = set("IsValid", isValid);
        UpdateResult updateResult = MP3Collection.updateOne(filter, updateOperation);
        System.out.println("=> Updating the doc with id "+ id +". changing isValid.");
        return updateResult.getModifiedCount() == 1;
    }
}
