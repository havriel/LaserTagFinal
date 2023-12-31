package com.example.demo;

import com.example.demo.Constants.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SecondController {
    DataBaseHandler db = new DataBaseHandler();
    PlayerProvider playerProvider = new PlayerProvider();
    @FXML
    private Button addBtn;

    @FXML
    private TextField second_name;

    @FXML
    private ChoiceBox<Integer> second_weapon;

    @FXML
    private ChoiceBox<Integer> second_vest;

    @FXML
    private ChoiceBox<String> second_command;

    @FXML
    private Button startBtn;

    @FXML
    private VBox redLayout;

    @FXML
    private VBox blueLayout;

    @FXML
    void initialize() {
        SceneController sceneController = new SceneController();
        second_name.setStyle("-fx-prompt-text-fill: black");
        second_weapon.getItems().addAll(Constants.WEAPON);
        second_vest.getItems().addAll(Constants.VEST);
        second_command.getItems().addAll(Constants.COMMANDS);
        addBtn.setOnAction(event -> {
            Player player = new Player();
            newPlayer(player);
            db.addPlayer(player);
            playerProvider.clearList();
            playerProvider.addPlayer(player);
            for (Player players : playerProvider.getList()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("player_item.fxml"));
                try {
                    HBox hBox = fxmlLoader.load();
                    PlayerItemController pic = fxmlLoader.getController();
                    pic.setData(players);
                    if (players.getCommand().equals("Red")) {
                        redLayout.getChildren().add(hBox);
                    }
                    if (players.getCommand().equals("Blue")) {
                        blueLayout.getChildren().add(hBox);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            second_name.clear();
            second_vest.setValue(null);
            second_weapon.setValue(null);
        });
        startBtn.setOnAction(event -> {
            try {
                db.getDbConnection().close();
                sceneController.switchToTimer(event);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void newPlayer(Player player) {
        for(int i=0;i<playerProvider.getList().size();i++){
            if(playerProvider.getList().get(i).getWeaponNum() == second_weapon.getValue()||
                    playerProvider.getList().get(i).getVestNum() == second_vest.getValue()){
                return;
            }
        }
        if (second_name.getText().isEmpty() || second_command.getValue().isEmpty() || second_weapon.getValue() == null
                || second_vest.getValue() == null) {
        } else {
            player.setName(second_name.getText());
            if(second_command.getValue().equals("Красная")){
                player.setCommand("Red");
            }else{
                player.setCommand("Blue");
            }
            player.setWeaponNum(second_weapon.getValue());
            player.setVestNum(second_vest.getValue());
            player.setKills(0);
            player.setDeaths(0);
            player.setPlace(0);
        }
    }
}