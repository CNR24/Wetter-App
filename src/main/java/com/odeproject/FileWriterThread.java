package com.odeproject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/**
 * @brief The FileWriterThread class is a thread that writes data to a file.
 */
class FileWriterThread implements Runnable {
    /** The filename to write the data to. */
    private String fileName;
    /** The data to write to the file. */
    private String data;
    /**
     * @brief Constructs a FileWriterThread object with a given filename and data to write.
     * @param filename The filename to write the data to.
     * @param data The data to write to the file.
     */
    public FileWriterThread(String filename, String data) {
        this.fileName = filename;
        this.data = data;
    }
    /**
     * @brief Runs the FileWriterThread, writing the data to the specified file.
     *
     * This method creates a BufferedWriter object to write the specified data to the specified file.
     * The file is determined using the filename provided in the constructor. The file is assumed to
     * be located in the same directory as the FileWriterThread class file.
     *
     * If an IOException occurs while writing to the file, an error message is printed to the console.
     */
    public void run() {
        try {
            File fileName2 = new File(getClass().getResource(fileName).getPath());
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName2));
            writer.write(data);
            writer.close();
            System.out.println("Data written to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Data COULD`NT written to file: " + fileName);
            e.printStackTrace();
        }
    }
}