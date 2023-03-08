package com.odeproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.io.*;
import java.net.Socket;
/**
 * @file WeatherController.java
 * @brief This file contains the implementation of WeatherController class. It is used for handling the client side functionality of the weather application.
 * @author [Caner Yildiz]
 */
public class WeatherController {
    /**
     * The socket used to communicate with the client.
     */
    private Socket client;
    /**
     * The JavaFX Label object used to display weather information.
     *
     */
    @FXML
    protected Label WeatherApp;
    /**
     * WeatherApp keeps the label name of scene
     *
     */
    @FXML
    private String weatherDate = "";
    /**
     * A text field for inputting data.
     *
     */
    @FXML
    private TextField mytextField;
    /**
     *  The name of the city that the user has selected.
     *
     */
    @FXML
    private String chosenCity;
    /**
     * The JavaFX ListView component for displaying items.
     *
     */
    @FXML
    protected ListView listView;

    /**
     * Attempts to connect the client to the server and send a chosen city to the server.
     * Receives a weather date from the server in response.
     *
     * @throws IOException if an I/O error occurs while connect client
     */
    public void clientConnect() {
        try {
            client = new Socket("localhost", 4711);
            System.out.println("Client: connected to " + client.getInetAddress());
            DataOutputStream output = new DataOutputStream(client.getOutputStream());
            output.writeUTF(chosenCity);
            DataInputStream input = new DataInputStream(client.getInputStream());
            weatherDate = input.readUTF();
            System.out.println("Client sent to Server this chosen City: " + chosenCity);
            System.out.println("Client got from Server this Messeage: " + weatherDate);
            input.close();
            output.close();
            client.close();
        } catch (IOException e) {
            System.out.println("There is a runtime error in Controller!");
            throw new RuntimeException(e);
        }

    }


    /**
     * When the button is pressed it takes the city name and saves it. Then the method to connect to the client is
     * called and finally the data is displayed in the GUI.
     */
    @FXML
    protected void onShowWeatherButtonClick() {
        chosenCity = mytextField.getText();
        clientConnect();
        listView.getItems().add(weatherDate);
    }
}