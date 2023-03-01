package com.odeproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class WeatherController {
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
            byte[] bymessage = new byte[30];
            int bytes = in.read(bymessage);
            System.out.println("Client: received " + bytes + " Bytes from Server");
            String lmessage = new String(bymessage);
            System.out.println(lmessage);
            cities.add(0,lmessage);
            lv.getItems().add(lmessage);
            lv.refresh();
            System.out.println("Message from Server recived" );

        } catch (IOException e) {
            System.out.println("There is an error in Controller!");
            e.printStackTrace();
        }
    }
    @FXML
    private ImageView imageView;

    public void initialize (URL url, ResourceBundle rb){

        Image image = new Image(getClass().getResourceAsStream("/image/FocusArea__Weather-02.jpg"));
        imageView.setImage(image);
    }
}