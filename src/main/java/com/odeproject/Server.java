package com.odeproject;

import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.FileWriter;
/**
 * @file Server.java
 * @brief This file contains many methods such as connecting Server and Client, sending data, receiving data, API connection, writing to file, reading from file.
 * @author [Caner Yildiz]
 */
public class Server{
    // VARIABLES
    /** API key for connect API */
    private static final String API_KEY = "8b06e9e136094be6acd171511232702";
    /** Socket for Server */
    private static ServerSocket server;
    /** Socket for Client */
    protected static Socket clientSocket;
    /** Message for Server */
    protected static String messageToClient;
    /** Selected City name  from chosen City */
    public String chosenCity;
    /** The name of location  from chosen City */
    private String locationName;
    /** Weather Condition from chosen City */
    private String weatherCondition;
    /** The temperature from chosen City */
    private double temperature;
    /** The location country from chosen City */
    private String locationCountry;
    /** The local time from chosen City */
    private String localTime;
    /** The perceived temperature from chosen City */
    private double feelsLikeC;
    /** The wind speed from chosen City */
    private double windKph;

    // GETTERS AND SETTERS
    /**
     * Sets the message for Server
     *
     * @param messageToClient keeps weather data
     */
    public static void setMessageToClient(String messageToClient) {
        Server.messageToClient = messageToClient;
    }
    /**
     * Returns the message from Client.
     *
     * @return the chosen city.
     */
    public String getChosenCity() {
        return chosenCity;
    }
    /**
     * Sets chosen City
     * @param choosenCity keeps city name
     */
    public void setChosenCity(String choosenCity) {
        this.chosenCity = choosenCity;
    }
    /**
     * Constructs a new Server object with the specified chosenCity, locationName, weatherCondition, temperature, locationCountry, localTime, feelsLikeC and windKph.
     * @param chosenCity City of the Server
     * @param locationName Name of City
     * @param localTime Localtime of the Server
     * @param weatherCondition Weather Condition of the Server
     * @param temperature Temperature of the Server
     * @param locationCountry Country of the Server
     * @param feelsLikeC Feeling temperature of the Server
     * @param windKph Wind of the Server
     */

    // CONSTRUCTORS
   public Server(String chosenCity, String locationName, String weatherCondition, double temperature, String locationCountry, String localTime, double feelsLikeC, double windKph) {
        this.chosenCity = chosenCity;
        this.locationName = locationName;
        this.weatherCondition = weatherCondition;
        this.temperature = temperature;
        this.locationCountry = locationCountry;
        this.localTime = localTime;
        this.feelsLikeC = feelsLikeC;
        this.windKph = windKph;
    }
    // FUNCTIONS
    /**
     * Instantiates a serverSocket with given port and waits for a client to be connected
     *
     * @param port Port address
     */
    public Server(int port) {
        try {
            server = new ServerSocket(port);
        } catch (SocketException e) {
            System.out.println("There is an error in Server! (Socket Exception)");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("There is an error in Server! (IOException)");
            e.printStackTrace();
        }
    }
    /**
     * Connects to the weather API and retrieves the current weather data for the chosen city.
     * The weather data is then saved to a file and parsed to extract relevant information such as location, weather condition,
     * temperature, etc.
     *
     * @throws IOException if there is an error connecting to the API or saving the weather data to a file
     */
    protected void ConnectAPI(){

        try{
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
                locationName = jsonObj.getJSONObject("location").getString("name");
                weatherCondition = jsonObj.getJSONObject("current").getJSONObject("condition").getString("text");
                temperature = jsonObj.getJSONObject("current").getDouble("temp_c");
                locationCountry = jsonObj.getJSONObject("location").getString("country");
                localTime = jsonObj.getJSONObject("location").getString("localtime");
                feelsLikeC = jsonObj.getJSONObject("current").getDouble("feelslike_c");
                windKph = jsonObj.getJSONObject("current").getDouble("wind_kph");
            } else {
                System.out.println("Error getting weather data");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Sends a message to the client over the network connection.
     *
     * @throws IOException if an I/O error occurs while sending the message
     */
    private void sendToCLient(){
        try {
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            output.writeUTF(messageToClient);
            System.out.println("This message sent to CLient: "+ messageToClient);
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Waits for a connection from a client, reads a city name from the input stream,
     * and sets the chosen city for the server.
     *
     * @throws IOException if there is an error reading from the input stream
     */
    private void receiveFromClient(){
        try {
            System.out.println("Server: waiting for connection on Port: " + server.getLocalPort());
            clientSocket = server.accept();
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            setChosenCity(input.readUTF());
            System.out.println("THis message (city name) received from Client: "+getChosenCity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * The main method of the weather application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args){
        Server Srv = new Server(4711);                    // Server connection
        Srv.receiveFromClient();                               // Receive the chosen city from client
        Srv.ConnectAPI();                                      // Connect API and receive the air condition for chosen city

        String stringLocationName= Srv.locationName;            //
        String stringLocalTime = Srv.localTime;                 //
        String stringCurrentWeather = Srv.weatherCondition;     //
        Double doubleTemperature= Srv.temperature;              // Get the data to be written to the file from json and reconstruct it as a string
        Double doubleFeelsLike= Srv.feelsLikeC;                 //
        Double doubleWind = Srv.windKph;                        //
        String stringCountry = Srv.locationCountry;             //

        String fileName = "myWeatherData.txt";                  // Filename for writing in file and reading from this file
        // data to be written to the file
        String data = "Location: "+stringLocationName+" - Local Time: "+stringLocalTime+" - Current Weather: "+stringCurrentWeather+" - Temperature: "+doubleTemperature+" - Feels like: "+doubleFeelsLike+" - Wind: "+doubleWind+" - Country: "+stringCountry;
        // create and start the writer thread
        Thread writerThread = new Thread(new FileWriterThread(fileName, data));
        writerThread.start();
        // wait until it is done
        try {
            writerThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        DataReader reader = new DataReader(fileName);           // Creates a new FileReaderThread object to read data from the file on a new thread, and starts the thread
        String readData = reader.readDataFromFile();            // Take the information from file and make it string
        setMessageToClient(readData);                           // Set the message to be sent to the client
        Srv.sendToCLient();                                     // Send message to client
    }
}