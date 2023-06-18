package com.example.demo;

import com.example.demo.Constants.Constants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TimerController implements Initializable {
    private Timeline timeline;
    private int seconds = 0;
    private int minutes = 0;
    private boolean isStopped = false;
    private DataBaseHandler handler;
    private SerialPort serialPort;

    @FXML
    private Label minute;

    @FXML
    private Label second;

    @FXML
    private Button startTimer;

    @FXML
    private Button stopTimer;

    @FXML
    private ChoiceBox<Integer> time;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = new DataBaseHandler();
        time.getItems().addAll(Constants.TIMES);
        FileInput input = new FileInput();
        serialPort = new SerialPort(input.readFile());
        if(seconds<10){
            second.setText("0"+seconds);
        }else {
            second.setText(Integer.toString(seconds));
        }
        if (minutes < 10) {
            minute.setText("0" + minutes);
        } else {
            minute.setText(Integer.toString(minutes));
        }

        startTimer.setOnAction(event -> {
            if(time.getValue() == null){
                return;
            }else {
                startTimer.setDisable(true);
            }
            time.setDisable(true);


            try {
                serialPort.openPort();
                serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            } catch (SerialPortException e) {
                throw new RuntimeException(e);
            }


            try {
                handler.dbConnection = handler.getDbConnection();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            minutes = time.getValue();
            if (timeline != null) {
                timeline.stop();
            }

            timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event1 -> {
                seconds--;
                if (seconds <= 0) {
                    seconds = 59;
                    minutes--;
                    if (minutes < 0) {
                        minutes = 0;
                        seconds = 0;
                        timeline.stop();
                        isStopped = true;
                    }
                }

                if (!isStopped) {
                    try {
                        String s = serialPort.readString().trim();
                        System.out.println(s);
                        String getVest;
                        String getWeapon;

                        int w = Integer.parseInt(s.substring(s.lastIndexOf('/') + 1));
                        int v = Integer.parseInt(s.substring(0, s.indexOf('/')));

                        if ( v == 10) {
                            getVest = "SELECT vest FROM players WHERE vest =" + 10;
                        } else {
                            getVest = "SELECT vest FROM players WHERE vest =" + Integer.parseInt(s.substring(0, 1));
                        }

                        if (s.endsWith("10")) {
                            getWeapon = "SELECT weapon FROM players WHERE weapon =" + 10;
                        } else {
                            getWeapon = "SELECT weapon FROM players WHERE weapon =" + w;
                        }
                        PreparedStatement statementVest;
                        PreparedStatement statementWeapon;
                        ResultSet rsVest;
                        ResultSet rsWeapon;
                        try {
                            statementVest = handler.dbConnection.prepareStatement(getVest);
                            statementWeapon = handler.dbConnection.prepareStatement(getWeapon);
                            rsVest = statementVest.executeQuery();
                            rsWeapon = statementWeapon.executeQuery();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        if (rsVest.next()) {
                            if (rsVest.getInt("vest") == Integer.parseInt(s.substring(0, 1))) {
                                handler.updateDeaths(Integer.parseInt(s.substring(0, 1)));
                            }else{
                                handler.updateDeaths(v);
                            }
                        }
                        if (rsWeapon.next()) {
                            if (rsWeapon.getInt("weapon") == w) {
                                handler.updateKills(w);
                            }
                        }
                    } catch (SerialPortException | SQLException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Parent root;
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("result_table.fxml"));
                        root = loader.load();
                        Stage stage = new Stage();
                        stage.setTitle(Constants.LABEL);
                        stage.setScene(new Scene(root));
                        stage.show();
                        ((Node) (event.getSource())).getScene().getWindow().hide();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (seconds < 10) {
                    second.setText("0" + seconds);
                } else {
                    second.setText(Integer.toString(seconds));
                }
                if (minutes < 10) {
                    minute.setText("0" + minutes);
                } else {
                    minute.setText(Integer.toString(minutes));
                }
            }));
            timeline.playFromStart();
        });
        stop();
    }
    private void stop(){
        stopTimer.setOnAction(event -> {
            timeline.stop();
            try {
                serialPort.closePort();
            } catch (SerialPortException e) {
                throw new RuntimeException(e);
            }
            stopTimer.setStyle("-fx-background-color: #00FF7F;-fx-background-radius: 50;");
            stopTimer.setText("ПРОДОЛЖИТЬ");
            stopTimer.setOnAction(event1 -> {
                stopTimer.setStyle("-fx-background-color:  #DC143C;-fx-background-radius: 50;");
                stopTimer.setText("СТОП");
                try {
                    serialPort.openPort();
                } catch (SerialPortException e) {
                    throw new RuntimeException(e);
                }
                timeline.play();
                stop();
            });
        });
    }
}
