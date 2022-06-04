package com.mongodb.quickstart;

import org.bson.Document;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
    private JTable mpFilesTable;
    private JScrollPane scrolPaneTable;

    public App() {
        MongoDB.initializeMongoDb();
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
                createTable();
            }
        });
    }

    private Object[][] makeArr(ArrayList<ArrayList<Object>> dataList){
        Object[][] data = new Object[dataList.size()-1][4];
        for (int i = 0; i < data.length; i++) {
            ArrayList<Object> temp = dataList.get(i);
            temp.remove(0);
            data [i] = temp.toArray();
        }
        return data;
    }

    private void createTable(){
        ArrayList<ArrayList<Object>> dataList = MongoDB.getData();
        Object[][] data = makeArr(dataList);
        mpFilesTable.setModel(new DefaultTableModel(
                data,
                new String[]{"File Name", "Size", "Length", "Is Valid"}
        ));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(450, 474));
        frame.pack();
        frame.setVisible(true);
    }
}
