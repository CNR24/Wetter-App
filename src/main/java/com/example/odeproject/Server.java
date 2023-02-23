package com.example.odeproject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static DataOutputStream dataOutputStream = null;
    public static void main(String[] args) {
        String data= "";
        try {
            ServerSocket server = new ServerSocket(4711);
            System.out.println("Server: waiting for connection on Port: "+ server.getLocalPort());
            Socket client = server.accept();

            //Reading the Weather Data
            File file= new File("C:\\Users\\cnryl\\Fh_technikum\\ODE\\Wetter-App\\src\\main\\java\\weather.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;

            while ((st= br.readLine()) !=null){
                data = br.readLine();

            }



            //Sending the Weather Data
            OutputStream stream = client.getOutputStream();
            OutputStream out = client.getOutputStream();
            stream.write(data.getBytes());




        } catch (
                IOException e) {
            e.printStackTrace();

        }
    }
}
