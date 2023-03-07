package com.odeproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
/**
 * The WeatherApplication class is the main class for the WeatherApp program.
 *
 * This class extends the JavaFX Application class and overrides the start method
 * to set up the user interface and display the main window. The main method is also
 * provided to serve as the entry point for the program.
 */
public class WeatherApplication extends Application {
    /**
     * Overrides the start method of the Application class to set up the user interface and display the main window.
     *
     * @param stage the primary stage for this application
     * @throws IOException if an I/O error occurs while loading the FXML file
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WeatherApplication.class.getResource("client-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 450);
        stage.setTitle("WeatherApp");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * The entry point for the WeatherApplication program.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}