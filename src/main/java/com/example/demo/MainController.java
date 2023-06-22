package com.example.demo;

import com.example.demo.Constants.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainController {
    private FileInput input;
    private Connector connector;

    @FXML
    private Button nextBtn;

    @FXML
    private TextField comText;

    @FXML
    void initialize() {
        SceneController sceneController = new SceneController();
        DataBaseHandler handler = new DataBaseHandler();
        input = new FileInput();
        handler.deleteTable();
        nextBtn.setOnAction(event -> {
            connector = new Connector();
            if (comText.getText().isEmpty()) {
                System.out.println("Error");
            }
            if (connector.connect(comText.getText())) {
                input.writeFile(comText.getText());
                handler.createTable();
                try {
                    handler.getDbConnection().close();
                    sceneController.switchToAddPlayers(event);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                connector.getSerialPort().closePort();
            } else {
                System.out.println("Ошибка соединения с платой");
            }
        });
        try {
            handler.getDbConnection().close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}