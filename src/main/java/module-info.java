module com.example.odeproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.odeproject to javafx.fxml;
    exports com.example.odeproject;
}