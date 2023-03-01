module com.odeproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.odeproject to javafx.fxml;
    exports com.odeproject;
}