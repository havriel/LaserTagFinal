package com.example.demo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DataBaseHandler extends Configs {
    public Connection dbConnection;

    public Connection getDbConnection() throws SQLException, ClassNotFoundException {
        String connectionString = "jdbc:sqlite:mydatabase.db";
        try {
            dbConnection = DriverManager.getConnection(connectionString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dbConnection;
    }

    public void createTable(){
        try {
            dbConnection = getDbConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String create = "create table players(id int primary key, name text not null,command text not null,weapon int not null,vest int not null,kills int,deaths int ,place int)";
        try {
            Statement statement = dbConnection.createStatement();
            statement.execute(create);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteTable(){
        try {
            dbConnection = getDbConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String delete = "drop table if exists 'mydatabase.players'";
        try {
            Statement statement = dbConnection.createStatement();
            statement.executeUpdate(delete);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void addPlayer(Player player) {
        try {
            dbConnection = getDbConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String insert = "INSERT INTO players (name,command,weapon,vest,kills,deaths,place) VALUES(?,?,?,?,?,?,?)";
        try {
            PreparedStatement prSt = dbConnection.prepareStatement(insert);
            prSt.setString(1, player.getName());
            prSt.setString(2, player.getCommand());
            prSt.setInt(3, player.getWeaponNum());
            prSt.setInt(4, player.getVestNum());
            prSt.setInt(5, player.getKills());
            prSt.setInt(6, player.getDeaths());
            prSt.setInt(7, player.getPlace());
            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("unhandled1", e);
        }
    }

    public void updateDeaths(int vest, Connection dbConnection) {
        String uDeath = "UPDATE players SET deaths=deaths+1 WHERE vest=" + vest;
        try {
            Statement statement = dbConnection.createStatement();
            statement.execute(uDeath);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateKills(int weapon, Connection dbConnection) {
        String uKills = "UPDATE players SET kills=kills+1 WHERE weapon=" + weapon;
        try {
            Statement statement = dbConnection.createStatement();
            statement.execute(uKills);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Player> getRedTable(Connection dbConnection){
        List<Player> playersRed = new ArrayList<>();
        String get = "SELECT * FROM players WHERE command='Red'";
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery(get);
            while (rs.next()){
                Player player = new Player();
                player.setName(rs.getString("name"));
                player.setCommand(rs.getString("command"));
                player.setWeaponNum(rs.getInt("weapon"));
                player.setVestNum(rs.getInt("vest"));
                player.setKills(rs.getInt("kills"));
                player.setDeaths(rs.getInt("deaths"));
                playersRed.add(player);
            }
            playersRed.sort(Comparator.comparingInt(Player::getKills).reversed());
            for (int i=0;i<playersRed.size();i++){
                playersRed.get(i).setPlace(i+1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return playersRed;
    }

    public List<Player> getBlueTable(Connection dbConnection){
        List<Player> playersBlue = new ArrayList<>();
        String get = "SELECT * FROM players WHERE command='Blue'";
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery(get);
            while (rs.next()){
                Player player = new Player();
                player.setName(rs.getString("name"));
                player.setCommand(rs.getString("command"));
                player.setWeaponNum(rs.getInt("weapon"));
                player.setVestNum(rs.getInt("vest"));
                player.setKills(rs.getInt("kills"));
                player.setDeaths(rs.getInt("deaths"));
                playersBlue.add(player);
            }
            playersBlue.sort(Comparator.comparingInt(Player::getKills).reversed());
            for (int i=0;i<playersBlue.size();i++){
                playersBlue.get(i).setPlace(i+1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return playersBlue;
    }
}
