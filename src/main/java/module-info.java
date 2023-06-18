module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jssc;


    opens com.example.demo to javafx.fxml;
    requires com.fazecast.jSerialComm;
    requires mysql.connector.j;
    exports com.example.demo;
}