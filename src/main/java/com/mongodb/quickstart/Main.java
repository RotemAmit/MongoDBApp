package com.mongodb.quickstart;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import org.bson.Document;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {




        MongoDB.printInfo();
        MongoDB.deleteOne(1);
    }
}