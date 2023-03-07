package com.odeproject;

import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.FileWriter;


// *****************************    Try to connect with Server      *******************************
public class Server {

    // VARIABLES

    private static final String API_KEY = "8b06e9e136094be6acd171511232702";
    private static ServerSocket server;
    protected static Socket clientSocket;
    protected static String fileContent;
    public String chosenCity;

    // GETTERS AND SETTERS

    public static void setFileContent(String fileContent) {
        Server.fileContent = fileContent;
    }
    public String getChosenCity() {
        return chosenCity;
    }

    public void setChosenCity(String choosenCity) {
        this.chosenCity = choosenCity;
    }

    // CONSTRUCTORS

    public Server(String choosenCity){
        this.chosenCity = choosenCity;
    }

    // FUNCTIONS

    public Server(int port) {

        try {
            server = new ServerSocket(port);
            //server.setSoTimeout(20000); //
        } catch (SocketException e) {
            System.out.println("There is an error in Server! (Socket Exception)");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("There is an error in Server! (IOException)");
            e.printStackTrace();
        }
    }


    protected void CreateFile(String fileName){
        // *******************************    Create a File      ***********************************
        try {
            File myFile = new File(getClass().getResource(fileName).getPath());
            if (myFile.createNewFile()){
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred in file existing");
            e.printStackTrace();
        }
    }

    // *****************************    Try to connect with API      *******************************

    protected void ConnectAPI(){

        try{
            System.out.println("************"+getChosenCity());
            String location = getChosenCity(); // replace with desired location
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
                    myWriter.write("Location: " +locationName + " - \n");
                    myWriter.write("Local Time: " + localTime + " - \n");
                    myWriter.write("Current Weather: " + weatherCondition + " - \n");
                    myWriter.write("Temperature: " + temperature + "° - \n");
                    myWriter.write("Feels like: " + feelsLikeC+ "° - \n");
                    myWriter.write("Wind: "+ windKph+ " kph - \n");
                    myWriter.write("Country: " + locationCountry + "  \n");
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
        setFileContent(stringBuilder.toString());
    }
    private void sendToCLient(){
        try {
            System.out.println("Send to Client: " + server.getLocalPort());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            output.writeUTF(fileContent);
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiveFromClient(){
        try {
            System.out.println("Server: waiting for connection on Port: " + server.getLocalPort());
            clientSocket = server.accept();
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            setChosenCity(input.readUTF());
            // clientSocket.close();
            System.out.println("reviceFromCLient Get chosen CIty: "+getChosenCity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        Server Srv = new Server(4711);
        Srv.CreateFile("myWeatherData.txt");
        Srv.receiveFromClient();
        Srv.ConnectAPI();
        Srv.ReadData();
        Srv.sendToCLient();
    }
}

/*
    private void Run() {
        while (true) {
            try {
                System.out.println("Server: waiting for connection on Port: " + server.getLocalPort());
                clientSocket = server.accept();
                DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                setChosenCity(input.readUTF());
                //System.out.println(clientSocket.getRemoteSocketAddress());
                DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
                output.writeUTF(fileContent);
                clientSocket.close();
                System.out.println("Get chosen CIty: "+getChosenCity());
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
 */