module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens com.example.demo to javafx.fxml;
    requires com.fazecast.jSerialComm;
    requires jssc;
    requires mysql.connector.j;
    exports com.example.demo;
}