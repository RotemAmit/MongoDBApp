package com.mongodb.quickstart;

import org.bson.Document;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class App {
    private JButton button;
    private JPanel panelMain;
    private JLabel AppHeader;
    private JPanel InputLabelNContent;
    private JLabel InputLabel;
    private JTextField inputFolder;
    private JPanel panelTable;
    private JPanel panelBtn;

    public App() {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(null, "Hello World!");
                String folderPath = inputFolder.getText();
                final File folder = new File(folderPath);
                ArrayList<Document> mpFileList = null;
                try {
                    mpFileList = MongoDB.listFilesForFolder(folder);
                } catch (IOException | UnsupportedAudioFileException ex) {
                    throw new RuntimeException(ex);
                }
                MongoDB.insertManyDocuments(mpFileList);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(450, 474));
        frame.pack();
        frame.setVisible(true);
        MongoDB.initializeMongoDb();
    }
}
