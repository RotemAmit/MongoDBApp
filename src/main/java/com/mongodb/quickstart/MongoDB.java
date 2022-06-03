package com.mongodb.quickstart;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;

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

    private static Double getDurationWithMp3Spi(File file) throws UnsupportedAudioFileException, IOException {
        //File file = new File("filename.mp3");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = audioInputStream.getFormat();
        long frames = audioInputStream.getFrameLength();
        double durationInSeconds = (frames+0.0) / format.getFrameRate();
        return durationInSeconds;
    }

    private static Document getDocument(MpFile mpFile){
        return new Document("ID", mpFile.getId())
                .append("FileName", mpFile.getFileName())
                .append("Size", mpFile.getSize())
                .append("Length", mpFile.getLength())
                .append("IsValid", mpFile.getValid());
    }
    public static ArrayList<Document> listFilesForFolder(final File folder) throws IOException, UnsupportedAudioFileException {
        ArrayList<Document> mpFilesList = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else{
                String end = ".mp3";
                String fileName = fileEntry.getName();
                if (fileName.contains(end)){
                    System.out.println(fileEntry.getName());
                    Path path = Paths.get("C:\\Users\\rotem amit\\Music\\" + fileEntry.getName());
                    long bytes = Files.size(path);
                    System.out.printf("%,d bytes%n", bytes);
                    //Double duration = getDurationWithMp3Spi(fileEntry);
                    //System.out.println("duration: " + duration);
                    MpFile mpFile = new MpFile(fileName, bytes, null, null);
                    mpFilesList.add(getDocument(mpFile));
                }

            }
        }
        return mpFilesList;
    }

    public static void insertOneDocument(Document document) {
        MP3Collection.insertOne(document);
        System.out.println("The new MP3 file was inserted to the DB");
    }

    public static void insertManyDocuments(ArrayList<Document> list){
        MP3Collection.insertMany(list, new InsertManyOptions().ordered(false));
        System.out.println("The new MP3 file list was inserted to the DB");
    }

    public static void printInfo(){
        FindIterable<Document> iterDoc = MP3Collection.find();
        MongoCursor<Document> cursor = iterDoc.iterator();
        while (cursor.hasNext()) {
            System.out.println(cursor.next().toJson());
        }
    }

    public static void deleteOne(Integer id){
        Bson filter = eq("ID", id);
        DeleteResult result = MP3Collection.deleteOne(filter);
        System.out.println(result);
    }
}
