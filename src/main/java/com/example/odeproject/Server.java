package com.example.odeproject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static DataOutputStream dataOutputStream = null;
    public static void main(String[] args) {

        String data= "";
        try {
            // Try to connect with Server
            ServerSocket server = new ServerSocket(4711);
            System.out.println("Server: waiting for connection on Port: "+ server.getLocalPort());
            Socket client = server.accept();

            //Reading the Weather Data
            File file= new File("C:\\Users\\cnryl\\Fh_technikum\\ODE\\Wetter-App\\src\\main\\java\\weather.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String text;

            while ((text= bufferedReader.readLine()) != null){
                data = bufferedReader.readLine();
            }

            //Sending the Weather Data to Client
            OutputStream stream = client.getOutputStream();
            OutputStream out = client.getOutputStream();
            stream.write(data.getBytes());

        } catch (IOException e) {
            System.out.println("There is an error in Server");
            e.printStackTrace();

        }
    }
}
