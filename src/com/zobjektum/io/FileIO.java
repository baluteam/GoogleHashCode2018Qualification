/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zobjektum.io;

import com.zobjektum.bom.SolutionWrapper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Balu_ADMIN
 */
public class FileIO extends JPanel {
    public static final String INPUT_EXTENSION = ".in";
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String FILEPATH_SEPARATOR = System.getProperty("file.separator"); //the \ or / that separates directories in a file name for instance
    public static final String DATA_SEPARATOR = " ";
    private static final String PROJECT_ROOT_NAME = "GoogleHashCode2018Qualification";
    /**
     * Folder name where we have the input data
     */
    private static final String RESOURCES_FOLDER = "resources";
    /**
     * Folder name where we store our solution as an output data
     */
    private static final String OUTPUT_FOLDER = "output";
    private static final String OUTPUT_FILENAME = "solution";
    private static final String OUTPUT_EXTENSION = ".out";
    
    private String usedInputFileName;
    
    /**
      * Helper method to read up .in input files.
      * 
      * @param pathToFile
      * @return
    */
    public File getFile(String pathToFile) {
        File file = new File(pathToFile);
        if(!file.isFile()) {
                throw new RuntimeException(pathToFile + " is not a file.");
        }
        if(!file.getName().endsWith(INPUT_EXTENSION)) {
                throw new RuntimeException(pathToFile + " does not have the expected extension: " + INPUT_EXTENSION);
        }
        return file;
    }
    
    /**
      * Helper method to get back the string content of a file.
      * 
      * @param file
      * @return
    */
    public String getFileContent(File file) {
        if(file == null || !file.isFile() || !file.canRead()) {
            displayError("Cant read as file: " + file);
            System.exit(-1);
        }
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while((line = reader.readLine()) != null) {
                sb.append(line).append(LINE_SEPARATOR);
            }
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
            displayError("File not found: " + file);
            System.exit(-1);
        }
        catch (IOException e) {
            e.printStackTrace();
            displayError("IOException while reading file: " + file);
            System.exit(-1);
        }
        finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
    
    /**
      * Opens a dialog and returns the selected file's string content.
      * 
      * @return
    */
    public String loadUsingGUI() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return "Accepting only files with extension: " + FileIO.INPUT_EXTENSION;
            }
            @Override
            public boolean accept(File f) {
                if(f.isDirectory()) {
                    return true;
                }
                if(f.canRead() && f.getName().endsWith(FileIO.INPUT_EXTENSION)) {
                    return true;
                }
                return false;
            }
        });
        fileChooser.setAcceptAllFileFilterUsed(false);
        try {
            URL inputDirectoryURL = FileIO.class.getResource(".");
//          Main.print("Current directory where program is runnign from: " + inputDirectoryURL);
            File inputDirectory = new File(inputDirectoryURL.toURI());
            String currentDirectoryName = inputDirectory.getName();
            int counter = 10;
            while(0 < counter && !PROJECT_ROOT_NAME.equals(currentDirectoryName)) {
                inputDirectory = inputDirectory.getParentFile();
                currentDirectoryName = inputDirectory.getName();
                counter--;
            }
            File resourcesDirectory = new File(inputDirectory.getAbsolutePath() + File.separator + RESOURCES_FOLDER);
//          Main.print("Resources directory: " + resourcesDirectory);
            if(resourcesDirectory.isDirectory()) {
                fileChooser.setCurrentDirectory(resourcesDirectory);
            }
        } 
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if(selectedFile != null) {
                usedInputFileName = selectedFile.getName();
                int startOfExtension = usedInputFileName.lastIndexOf(".");
                usedInputFileName = usedInputFileName.substring(0, (startOfExtension == -1) ? usedInputFileName.length() : startOfExtension); //remove extension
            }
            return getFileContent(selectedFile);
        }
        return null;
    }
    
    /**
      * Helper method to write out a string into the given file.
      * 
      * @param soultionFile
      * @param fileContent
    */
    public void writeSolution(File soultionFile, String fileContent) {
        if(soultionFile == null) {
            return;
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(soultionFile));
            writer.write(fileContent);
            writer.flush();
        } 
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IOException while writing file: " + soultionFile);
        }
        finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
      * Helper method to show a save dialog with a proposed directory and file name and execute saving the file.
      * 
      * @param solutionWrapper
      * @return
    */
    public File saveSolution(SolutionWrapper solutionWrapper) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        try {
                URL inputDirectoryURL = FileIO.class.getResource(".");
//			Main.print("Current directory where program is runnign from: " + inputDirectoryURL);
                File inputDirectory = new File(inputDirectoryURL.toURI());
                String currentDirectoryName = inputDirectory.getName();
                int counter = 10;
                while(0 < counter && !PROJECT_ROOT_NAME.equals(currentDirectoryName)) {
                        inputDirectory = inputDirectory.getParentFile();
                        currentDirectoryName = inputDirectory.getName();
                        counter--;
                }
                File outputDirectory = new File(inputDirectory.getAbsolutePath() + File.separator + OUTPUT_FOLDER);
//			Main.print("Output directory: " + outputDirectory);
                if(outputDirectory.isDirectory()) {
                        fileChooser.setCurrentDirectory(outputDirectory);
                }
        } catch (URISyntaxException e) {
                e.printStackTrace();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        fileChooser.setSelectedFile(new File(OUTPUT_FILENAME + "_" + (usedInputFileName == null ? "" : (usedInputFileName + "_")) + dateFormat.format(new Date()) + OUTPUT_EXTENSION));
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
                File solutionFile = fileChooser.getSelectedFile();
                writeSolution(solutionFile, solutionWrapper.toString());
        }
        return null;
    }
    
    /**
    * Helper method to display the error in a dialog.
    * 
    * @param error
    */
    public void displayError(String error) {
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
    * Helper method to display the warning in a dialog.
    * 
    * @param error
    */
    public void displayWarning(String warning) {
        JOptionPane.showMessageDialog(this, warning, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    /**
    * Helper method to display the info in a dialog.
    * 
    * @param error
    */
    public void displayInfo(String info) {
        JOptionPane.showMessageDialog(this, info, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
