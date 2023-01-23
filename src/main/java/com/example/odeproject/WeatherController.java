package com.example.odeproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class WeatherController {
    @FXML
    private Label welcomeText;

    @FXML
    private ListView lv;



    @FXML
    protected void onShowWeatherButtonClick() {
        try {
            Socket client = new Socket("localhost", 4711);
            System.out.println("Client: connected to " + client.getInetAddress());


            //Reciving the Weather Data

            InputStream in = client.getInputStream();
            ObservableList<String> cities = FXCollections.observableArrayList();
            byte[] bymessage = new byte[50];
            int bytes = in.read(bymessage);
            System.out.println("Client: received " + bytes + " Bytes from Server");
            String lmessage = new String(bymessage);
            System.out.println(lmessage);
            cities.add(0,lmessage);
            lv.getItems().add(lmessage);
            lv.refresh();
            System.out.println("Message from Server recived" );
            cities.add(lmessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}