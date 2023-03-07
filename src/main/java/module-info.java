/**
 * The com.odeproject module defines the components of the ODE project.
 *
 * This module requires the JavaFX controls and FXML modules, as well as the
 * org.json module for working with JSON data.
 *
 * The com.odeproject package is opened to the javafx.fxml module to allow for
 * FXML controllers to access its components. The entire module is also exported
 * to allow for external use.
 */
module com.odeproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.odeproject to javafx.fxml;
    exports com.odeproject;
}