package com.theironyard;

import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void createTables(Connection conn) throws SQLException{
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, userName VARCHAR, password VARCHAR )");
        stmt.execute("CREATE TABLE IF NOT EXISTS characters (id IDENTITY, user_id INT, name VARCHAR, type VARCHAR, weapon VARCHAR, age INTEGER, weight INTEGER)");

    }

    //addUser if doesn't exist
    public static void addUser(Connection conn, String userName, String password) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?)");
        stmt.setString(1, userName);
        stmt.setString(2, password);
        stmt.execute();
    }
    //selectUser if exists
    public static User selectUser(Connection conn, String userName) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE userName = ?");
        stmt.setString(1, userName);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            int id = results.getInt("id");
            String password = results.getString("password");
            return new User(id, userName, password);
        }
        return null;
    }
    public static void insertCharacter(Connection conn, int userId, String name, String type, String weapon, int age, int weight) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT into characters VALUES(NULL, ?, ?, ?, ?, ?, ?)");
        stmt.setInt(1, userId);
        stmt.setString(2, name);
        stmt.setString(3, type);
        stmt.setString(4, weapon);
        stmt.setInt(5, age);
        stmt.setInt(6, weight);
        stmt.execute();
    }

    public static ArrayList<Character> selectCharacter(Connection conn, String name) throws SQLException{
        ArrayList<Character> characters = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM characters INNER JOIN users ON characters.user_id = users.id AND users.userName = ?");
        stmt.setString(1, name);
        ResultSet results = stmt.executeQuery();

        while (results.next()){
            int id = results.getInt("characters.id");
            String type = results.getString("characters.type");
            String weapon = results.getString("characters.weapon");
            int age = results.getInt("characters.age");
            int weight = results.getInt("characters.weight");
            String charName = results.getString("characters.name");
            Character character = new Character(id, charName, type, weapon, age, weight);
            characters.add(character);
        }

        return characters;
    }

    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);
        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");

                    HashMap m = new HashMap();
                    ArrayList<Character> characters = selectCharacter(conn, userName);
                    m.put("userName", userName);
                    m.put("characters", characters);

                    return new ModelAndView(m, "home.html");

                }),
                    new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                ((request, response) -> {
                    // get the username from a post request
                    String userName = request.queryParams("loginName");
                    String password = request.queryParams("password");

                    // try to find user by username
                    User user = selectUser(conn, userName);
                    if (user == null) {
                        // ..insert the user if there is none
                        addUser(conn, userName, password);
                        Session session = request.session();
                        session.attribute("userName", userName);
                    }
                    if(!(user == null)){
                        if(password == null){
                            response.redirect("/");
                        } else{
                            Session session = request.session();
                            session.attribute("userName", userName);
                        }
                    }
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/addcharacter",
                (request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");

                    if (userName == null){
                        throw new Exception("Not logged in.");
                    }

                    String name = request.queryParams("name");
                    String type = request.queryParams("type");
                    String weapon = request.queryParams("weapon");
                    int age = Integer.valueOf(request.queryParams("age"));
                    int weight = Integer.valueOf(request.queryParams("weight"));

                    User user = selectUser(conn, userName);
                    insertCharacter(conn, user.getId(), name, type, weapon, age, weight);

                    response.redirect("/");
                    return "";
                }
        );
    }
}
