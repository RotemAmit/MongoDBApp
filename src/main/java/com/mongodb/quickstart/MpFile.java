package com.mongodb.quickstart;

import it.sauronsoftware.jave.MultimediaInfo;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.beans.Encoder;
import java.io.*;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.BorderFactory;

public class MpFile {
    private static final AtomicInteger count = new AtomicInteger(0);
    private Integer Id;
    private String FileName;
    private Double Size;
    private Double Length;
    private Boolean IsValid = null;

    public MpFile(){

    }

    public MpFile(String fileName, Double size, Double length, Boolean isValid){
        this.FileName = fileName;
        this.Size = size;
        this.Length = length;
        this.IsValid = isValid;
        this.Id = count.incrementAndGet();
    }

    public Integer getId() {
        return Id;
    }

    public String getFileName() {
        return FileName;
    }

    public Double getSize() {
        return Size;
    }

    public Double getLength() {
        return Length;
    }

    public Boolean getValid() {
        return IsValid;
    }

    public static Double getDuration(String path) throws UnsupportedAudioFileException, IOException {
        //ProcessBuilder builder = new ProcessBuilder("python", "C:\\Users\\rotem amit\\PycharmProjects\\pythonProject\\venv\\Scripts\\main.py", path);
        ProcessBuilder builder = new ProcessBuilder("python", "C:\\Users\\rotem amit\\IdeaProjects\\MongoDBApp\\pythonScripts\\main.py", path);
        Process process = builder.start();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader bufferedReaderErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String lines = null;
        String duration = "";
        while ((lines = bufferedReader.readLine()) != null){
            System.out.println("lines: " + lines);
            duration = lines;
        }
        while ((lines = bufferedReaderErr.readLine()) != null){
            System.out.println("Err: " + lines);
        }
        double d = Double.parseDouble(duration);
        int minutes = (int) d/60;
        int seconds = (int) d % 60;
        return Double.parseDouble(minutes + "." + seconds);
    }
}
