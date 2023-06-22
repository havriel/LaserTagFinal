package com.example.demo;

import com.sun.javafx.stage.WindowCloseRequestHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ResultTableController {
    @FXML
    private VBox redResult;

    @FXML
    private VBox blueResult;

    DataBaseHandler handler = new DataBaseHandler();

    @FXML
    void initialize() {
        try {
            handler.dbConnection = handler.getDbConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        getRed();
        getBlue();
    }

    private void getRed() {
        for (Player players : handler.getRedTable(handler.dbConnection)) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("player_item.fxml"));
            try {
                HBox hBox = fxmlLoader.load();
                PlayerItemController pic = fxmlLoader.getController();
                pic.setData(players);
                redResult.getChildren().add(hBox);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void getBlue() {
        for (Player players : handler.getBlueTable(handler.dbConnection)) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("player_item.fxml"));
            try {
                HBox hBox = fxmlLoader.load();
                PlayerItemController pic = fxmlLoader.getController();
                pic.setData(players);
                blueResult.getChildren().add(hBox);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
