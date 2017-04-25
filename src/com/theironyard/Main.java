package com.theironyard;

import org.h2.tools.Server;
import spark.Spark;

import java.sql.*;

public class Main {

    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
    }
    //Creating a table

    public static void createTables(Connection conn) throws SQLException{
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS user( id IDENTITY, userName VARCHAR, password VARCHAR )");
        stmt.execute("CREATE TABLE IF NOT EXISTS items(id IDENTITY, user_id INTEGER, weapon VARCHAR, ammunition VARCHAR, shield VARCHAR, isAlive BOOLEAN)");

    }

    //addUser if doesn't exist
    public static void addUser(Connection conn, String userName, String password) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO user VALUES (NULL, ?, ?)");
        stmt.setString(1, userName);
        stmt.setString(2, password);
        stmt.execute();
    }
    //selectUser if exists
    public static User selectUser(Connection conn, String userName) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE username = ?");
        stmt.setString(1, userName);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            int id = results.getInt("id");
            String password = results.getString("password");
            return new User(id, userName, password);
        }
        return null;
    }
    public static void insertItems(Connection conn, int user_id, String weapon, int ammunition, String shield, Boolean isAlive) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO items VALUES (NULL, ?, ?, ?, ?, ?)");
        stmt.setInt(1, user_id);
        stmt.setString(2, weapon);
        stmt.setInt(3, ammunition);
        stmt.setString(4, shield);
        stmt.setBoolean(5, isAlive);
        stmt.execute();
    }
    public static Items selectItems(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM items INNER JOIN user ON items.user_id = user.id WHERE items.id = ?");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            String weapon = results.getString("items.weapon");
            int ammunition = results.getInt("items.ammunition");
            String shield = results.getString("items.shield");
            Boolean isAlive = results.getBoolean("items.isAlive");
            return new Items(id, weapon, ammunition, shield, isAlive);
        }

        return null;
    }
}
