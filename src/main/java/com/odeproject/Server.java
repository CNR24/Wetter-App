package com.odeproject;

import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.FileWriter;
import java.util.Scanner;


// *****************************    Try to connect with Server      *******************************
public class Server {
    private static final String API_KEY = "8b06e9e136094be6acd171511232702";
    private static ServerSocket server;
    protected static Socket clientSocket;
    protected static String fileContent;
   // protected static String text;


    public Server(int port) {

        try {
            server = new ServerSocket(port);
            server.setSoTimeout(20000); //
        } catch (SocketException e) {
            System.out.println("Server Socket Exeption!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Server IOExeption!");
            e.printStackTrace();
        }
    }
    public void ReadData(){
        // *****************************       Reading the Weather Data      *******************************
        String filePath = "C:\\AODE\\Wetter-App\\myWeatherData.txt";
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileContent = stringBuilder.toString();
    }

    private void Run() {
        while (true) {
            try {
                System.out.println("Server: waiting for connection on Port: " + server.getLocalPort());
                clientSocket = server.accept();
                DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                System.out.println(input.readUTF());
                System.out.println(clientSocket.getRemoteSocketAddress());
                DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
                output.writeUTF(fileContent);
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    protected void CreateFile(String fileName){
        // *******************************    Create a File      ***********************************
        try {
            File myFile = new File(fileName);
            if (myFile.createNewFile()){
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    protected void ConnectAPI(){
        try{
        String location = "Erzincan"; // replace with desired location
        URL url = new URL("https://api.weatherapi.com/v1/current.json?key=" + API_KEY + "&q=" + location);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // check if connect is made
        int responseCode = con.getResponseCode();

        // The same like 200
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String jsonResponse = response.toString();

            // save JSON response to file
            FileWriter fileWriter = new FileWriter("weather.json");
            fileWriter.write(jsonResponse);
            fileWriter.close();

            // parse JSON response to extract location and current weather
            JSONObject jsonObj = new JSONObject(jsonResponse);
            String locationName = jsonObj.getJSONObject("location").getString("name");
            String weatherCondition = jsonObj.getJSONObject("current").getJSONObject("condition").getString("text");
            double temperature = jsonObj.getJSONObject("current").getDouble("temp_c");
            String locationCountry = jsonObj.getJSONObject("location").getString("country");
            String localTime = jsonObj.getJSONObject("location").getString("localtime");
            double feelsLikeC = jsonObj.getJSONObject("current").getDouble("feelslike_c");
            double windKph = jsonObj.getJSONObject("current").getDouble("wind_kph");

            // Print location, local time, current weather, temparature, feels like, wind and country on terminal

            System.out.println("Location:\t\t " + locationName);
            System.out.println("Local Time:\t\t " + localTime);
            System.out.println("Current Weather: " + weatherCondition);
            System.out.println("Temperature:\t " + temperature + "°");
            System.out.println("Feels like:\t\t " + feelsLikeC+ "°");
            System.out.println("Wind:\t\t\t "+ windKph+ " kph");
            System.out.println("Country:\t\t " + locationCountry);

            // ****************    Write all data into the File myWeatherData.txt    ************************

            try {
                FileWriter myWriter = new FileWriter("myWeatherData.txt");
                myWriter.write("Location: " +locationName + "\n");
                myWriter.write("Local Time: " + localTime + "\n");
                myWriter.write("Current Weather: " + weatherCondition + "\n");
                myWriter.write("Temperature: " + temperature + "°\n");
                myWriter.write("Feels like: " + feelsLikeC+ "°\n");
                myWriter.write("Wind: "+ windKph+ " kph\n");
                myWriter.write("Country: " + locationCountry + "\n");
               // myWriter.write("END");
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

        } else {
            System.out.println("Error getting weather data");
        }
    }catch (IOException e){
        e.printStackTrace();
    }




        // *****************************    Sending the Weather Data to Client   *******************************
/*
        try{
            OutputStream stream = client.getOutputStream();
            stream.write(data.getBytes());
        } catch (IOException e) {
            System.out.println("There is an error in Sending the Weather Data to Client!");
            e.printStackTrace();
        }*/
    }




    public static void main(String[] args) throws IOException {
        Server Srv = new Server(4711);
        Srv.CreateFile("myWeatherData.txt");
        Srv.ConnectAPI();
        Srv.ReadData();
        Srv.Run();


    }
}
        // *****************************    Try to connect with API      *******************************







        /*
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

         */

