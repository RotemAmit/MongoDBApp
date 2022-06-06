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
    private JButton scanButton;
    private JPanel panelMain;
    private JLabel AppHeader;
    private JPanel InputLabelNContent;
    private JLabel InputLabel;
    private JTextField inputFolder;
    private JPanel panelTable;
    private JPanel panelBtn;
    private JTable mpFilesTable;
    private JScrollPane scrolPaneTable;
    private JButton updateBtn;

    private DefaultTableModel model;
    private ArrayList<ArrayList<Object>> dataList = new ArrayList<>();

    public App() {
        MongoDB.initializeMongoDb();
        scanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String folderPath = inputFolder.getText();
                final File folder = new File(folderPath);
                ArrayList<Document> mpFileList = null;
                try {
                    mpFileList = MongoDB.listFilesForFolder(folder);
                    ArrayList<ArrayList<Object>> list = MongoDB.getData(mpFileList);
                    dataList.addAll(list);
                } catch (IOException | UnsupportedAudioFileException ex) {
                    JOptionPane.showMessageDialog(null,"There was a problem entering the data to the DB");
                    throw new RuntimeException(ex);
                }
                MongoDB.insertManyDocuments(mpFileList);
                createTable();
            }
        });
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isUpdated = true;
                for (int i = 0; i < mpFilesTable.getRowCount(); i++) {
                    Boolean checked = Boolean.valueOf(mpFilesTable.getValueAt(i,3).toString());
                    Integer updateId = checkUpdate(checked, i);
                    if (updateId != -1){
                        isUpdated = isUpdated && MongoDB.updateOne(updateId, checked);
                        updateDataList(checked, i);
                    }
                }
                if (isUpdated){
                    JOptionPane.showMessageDialog(null,"The update was successful");
                }
                else {
                    JOptionPane.showMessageDialog(null,"The update failed");
                }
            }
        });
    }

    private void updateDataList(Boolean checked, int row){
        ArrayList<Object> arr = dataList.get(row);
        ArrayList<Object> newArr = new ArrayList<>();
        newArr.add(arr.get(0));
        newArr.add(arr.get(1));
        newArr.add(arr.get(2));
        newArr.add(arr.get(3));
        newArr.add(checked);
        dataList.remove(row);
        dataList.add(row, newArr);
    }

    private Integer checkUpdate(Boolean checked, int row){
        ArrayList<Object> rowObj = dataList.get(row);
        if (rowObj.get(4) == null){
            if (checked){
                return Integer.parseInt(rowObj.get(0).toString());
            }
        }
        else {
            String val = rowObj.get(4).toString();
            if (((val.compareTo("true") == 0) && !checked) || ((val.compareTo("false") == 0) && checked)){
                return Integer.parseInt(rowObj.get(0).toString());
            }
        }
        return -1;
    }

    private void addColumns(){
        model.addColumn("File Name");
        model.addColumn("Size");
        model.addColumn("Length");
        model.addColumn("Is Valid");
    }

    private void addRows(){
        for (int i = 0; i < dataList.size(); i++) {
            ArrayList<Object> rowObj = dataList.get(i);
            model.addRow(new Object[0]);
            model.setValueAt(rowObj.get(1),i, 0);
            model.setValueAt(rowObj.get(2),i, 1);
            model.setValueAt(rowObj.get(3),i, 2);
            if (rowObj.get(4) == null || rowObj.get(4).toString().compareTo("false") == 0){
                model.setValueAt(false,i,3);
            }
            else {
                model.setValueAt(true,i,3);
            }
        }
    }
    private void createTable(){
        model = new DefaultTableModel(){
            public Class<?> getColumnClass(int column){
                switch (column){
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                    case 2:
                        return String.class;
                    case 3:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }
        };

        mpFilesTable.setModel(model);
        addColumns();
        addRows();
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
