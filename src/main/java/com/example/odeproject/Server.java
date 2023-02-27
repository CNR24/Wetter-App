package com.example.odeproject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static DataOutputStream dataOutputStream = null;
    static Socket client;
    public static void main(String[] args) {

        String data= "";
        
        try {               // Try to connect with Server

            ServerSocket server = new ServerSocket(4711);
            System.out.println("Server: waiting for connection on Port: "+ server.getLocalPort());
            client = server.accept();
        } catch (IOException e) {
            System.out.println("There is an error in Server Connection");
            e.printStackTrace();
        }

        try {               //Reading the Weather Data

            File file= new File("C:\\Users\\cnryl\\Fh_technikum\\ODE\\Wetter-App\\src\\main\\java\\weather.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String text;

            while ((text= bufferedReader.readLine()) != null){
                data = bufferedReader.readLine();
            }
        } catch (IOException e1) {
            System.out.println("There is an error in Reading of Weather Data");
            e1.printStackTrace();
        }

        try{                //Sending the Weather Data to Client
            OutputStream stream = client.getOutputStream();
            OutputStream out = client.getOutputStream();
            stream.write(data.getBytes());
        } catch (IOException e2) {
            System.out.println("There is an error in Sending the Weather Data to Client!");
            e2.printStackTrace();
        }




    }
}
