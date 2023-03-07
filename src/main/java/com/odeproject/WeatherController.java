package com.odeproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class WeatherController {
    @FXML
    public Label WeatherApp;
    @FXML
    protected String weatherDate = "";
    @FXML
    Socket client;
    @FXML
    private TextField mytextField;
    @FXML
    private String choosenCity;
    @FXML
    private ListView listView;
    public void clientConnect(){
        try {
            client = new Socket("localhost", 4711);
            System.out.println("Client: connected to " + client.getInetAddress());
            DataOutputStream output =  new DataOutputStream(client.getOutputStream());
            output.writeUTF(choosenCity);
            DataInputStream input = new DataInputStream(client.getInputStream());
            weatherDate = input.readUTF();
            input.close();
            output.close();
            client.close();
        } catch (SocketException e) {
            System.out.println("There is an socket error in Controller!");
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            System.out.println("There is an host connection error in Controller!");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("There is a runtime error in Controller!");
            throw new RuntimeException(e);
        }

    }

    @FXML
    protected void onShowWeatherButtonClick(){
        choosenCity = mytextField.getText();
        clientConnect();
        System.out.println("Chosen City: "+choosenCity);
        listView.getItems().add(weatherDate);
    }
}