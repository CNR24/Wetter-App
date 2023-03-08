package com.odeproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
/**
 *  @file FileReaderThread.java
 *  @brief This file contains the implementation of a thread for reading data from a file.
 *  This class implements the Runnable interface to create a thread that reads data from a file.
 *  It reads the data from the file and stores it in a string variable.
 */
class FileReaderThread implements Runnable {
    /** The name of the file to read from. */
    private String fileName;
    /** The data keeps the file data as a string.*/
    private String data;
    /**
     * @brief Returns the data read from the file.
     * @return The data read from the file as a string.
     */
    public String getData() {
        return data;
    }
    /**
     * @brief Constructs a FileReaderThread object with the given filename.
     * @param fileName The name of the file to read.
     */
    public FileReaderThread(String fileName) {
        this.fileName = fileName;
    }
    /**
     * @brief Reads data from the file and stores it in the data member.
     */
    public void run() {
        try {
            data ="";
            File filePath = new File(getClass().getResource(fileName).getPath());
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                data+=line+"\n";
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}