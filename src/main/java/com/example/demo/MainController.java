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

public class MainController {
    public static Alert nullAlert = new Alert(Alert.AlertType.NONE);
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
        handler.clearBase();
        input = new FileInput();
        nextBtn.setOnAction(event -> {
            connector = new Connector();
            if (comText.getText().isEmpty()) {
                nullAlert.setAlertType(Alert.AlertType.WARNING);
                nullAlert.setTitle(Constants.ERR);
                nullAlert.setContentText("Вы не указали COM порт!");
                nullAlert.show();
                return;
            }
            if (connector.connect(comText.getText())) {
                input.writeFile(comText.getText());
                try {
                    sceneController.switchToAddPlayers(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                connector.getSerialPort().closePort();
            } else {
                nullAlert.setAlertType(Alert.AlertType.ERROR);
                nullAlert.setTitle(Constants.ERR);
                nullAlert.setContentText("Ошибка соединения с платой");
                nullAlert.show();
            }
        });
    }


}