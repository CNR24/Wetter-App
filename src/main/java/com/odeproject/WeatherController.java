package com.odeproject;


import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class WeatherController {
    @FXML
    private ListView lv;
    @FXML
    protected void onChooseCityPromptText(){
    }
    @FXML
    protected void onShowWeatherButtonClick(){
        try {
            Socket client = new Socket("localhost", 4711);
            System.out.println("Client: connected to " + client.getInetAddress());
            DataOutputStream output =  new DataOutputStream(client.getOutputStream());
            output.writeUTF("Hi I am CLient with :"+ client.getLocalSocketAddress());
            DataInputStream input = new DataInputStream(client.getInputStream());
            System.out.println(input.readUTF());
            String s = "Hi";
            lv.getItems().add(s);
            lv.refresh();
            input.close();
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
}


    /* ****************************************************************++


            BufferedReader reader;

            try {
                reader = new BufferedReader(new FileReader("sample.txt"));
                String line = reader.readLine();

                while (line != null) {
                    System.out.println(line);
                    // read next line
                    line = reader.readLine();
                }

                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        ***************************************************************************


    @FXML
    private ImageView imageView;
    public void initialize (URL url, ResourceBundle rb){
        Image image = new Image(getClass().getResourceAsStream("/image/FocusArea__Weather-02.jpg"));
        imageView.setImage(image);
    }
        *************************************************************************
    */