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
    /**
     * API key for connect API
     */
    private static final String API_KEY = "8b06e9e136094be6acd171511232702";
    /**
     * Socket for Server
     */
    private static ServerSocket server;
    /**
     * Socket for Client
     */
    protected static Socket clientSocket;
    /**
     * Message for Server
     */
    protected static String messageToServer;
    /**
     * Selected City name  from chosen City
     */
    public String chosenCity;
    /**
     * The name of location  from chosen City
     */
    private String locationName;
    /**
     * Weather Condition from chosen City
     */
    private String weatherCondition;
    /**
     * The temperature from chosen City
     */
    private double temperature;
    /**
     * The location country from chosen City
     */
    private String locationCountry;
    /**
     * The local time from chosen City
     */
    private String localTime;
    /**
     * The perceived temperature from chosen City
     */
    private double feelsLikeC;
    /**
     * The wind speed from chosen City
     */
    private double windKph;

    // GETTERS AND SETTERS
    /**
     * Sets the message for Server
     *
     * @param messageToServer keeps weather data
     */
    public static void setMessageToServer(String messageToServer) {
        Server.messageToServer = messageToServer;
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
     * Writes weather data to a file with the specified file name.
     *
     * @param fileName the name of the file to write the weather data to
     * @throws IOException if an error occurs while writing the file
     */
    public void writeFile(String fileName){
        try {
            File fileName2 = new File(getClass().getResource(fileName).getPath());
            FileWriter myWriter = new FileWriter(fileName2);
            myWriter.write("Location: " +locationName + " - \n");
            myWriter.write("Local Time: " + localTime + " - \n");
            myWriter.write("Current Weather: " + weatherCondition + " - \n");
            myWriter.write("Temperature: " + temperature + "° - \n");
            myWriter.write("Feels like: " + feelsLikeC+ "° - \n");
            myWriter.write("Wind: "+ windKph+ " kph - \n");
            myWriter.write("Country: " + locationCountry + "  \n");
            myWriter.close();
            System.out.println("All weather data from this city: "+getChosenCity()+" successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    /**
     * Reads data from the file with the specified file name.
     *
     * @param fileName the name of the file to read
     */
        public void readFile(String fileName){
        File filePath = new File(getClass().getResource(fileName).getPath());
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setMessageToServer(stringBuilder.toString());
    }
    /**
     * Sends a message to the client over the network connection.
     *
     * @throws IOException if an I/O error occurs while sending the message
     */
    private void sendToCLient(){
        try {
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            output.writeUTF(messageToServer);
            System.out.println("This message sent to CLient: "+ messageToServer);
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
        Server Srv = new Server(4711);
        Srv.receiveFromClient();
        Srv.ConnectAPI();
        Srv.writeFile("myWeatherData.txt");
        Srv.readFile("myWeatherData.txt");
        Srv.sendToCLient();
    }
}