package com.theironyard;
import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.*;
import java.util.HashMap; //request query params

public class Main {

    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users(id IDENTITY, userName VARCHAR, password VARCHAR )");
        stmt.execute("CREATE TABLE IF NOT EXISTS characters(id IDENTITY, name VARCHAR, type VARCHAR, weapon VARCHAR, age INTEGER, weight INTEGER)");
        Spark.init();

//        Spark.post(
//                "/addcharacter",
//                //use sql to insert into table
//                //reqquest.queryparams(<form
//        )

        Spark.post(
                "/login",
                ((request, response) -> {
                    Session session = request.session();

                    // to get the username from the query parameters
                    String userName = request.queryParams("userName");
                    User user = selectUser(conn, userName);

                    if (user == null) {
                        addUser(conn, userName, "");
                        user = selectUser(conn, userName);
                    }

                    session.attribute("userName", user.userName);
                    response.redirect("/");
                    return "";
                })
        );
        Spark.get(
                "/",
                (request, response) -> {
                    HashMap m = new HashMap();
                    Session session = request.session();
                    // String weapon, int ammunition, String shield, Boolean isAlive)


                    String userName = session.attribute("userName");
                    String name = session.attribute("name");
                    String weapon = session.attribute("weapon");
                    String type = session.attribute("type");
                    int age = 0;
                    if (session.attribute("age") != null) {
                        age = session.attribute("age");
                    }
                    int weight = 0;
                    if (session.attribute("weight") != null) {
                        age = session.attribute("weight");
                    }
                    User user = selectUser(conn, userName);
                    if (user == null) {
                        return new ModelAndView(m, "login.html");
                    } else {
                        m.put("name", name);
                        m.put("type", type);
                        m.put("weapon", weapon);
                        m.put("age", age);
                        m.put("weight", weight);
                        return new ModelAndView(userName, "home.html");
                    }
                },
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/addcharacter",
                (request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");

                    String name = request.queryParams("name");
                    String type = request.queryParams("type");
                    String weapon = request.queryParams("weapon");
                    String age = request.queryParams("age");
                    String weight = request.queryParams("weight");

                    int ageNum = Integer.valueOf(age);
                    int weightNum = Integer.valueOf(weight);

                    User user = selectUser(conn, userName);
                    insertCharacter(conn, name, type, weapon, ageNum, weightNum);
                    response.redirect("/");
                    return "";
                }
        );
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
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
        stmt.setString(1, userName);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            int id = results.getInt("id");
            String password = results.getString("password");
            return new User(id, userName, password);
        }
        return null;
    }
    public static void insertCharacter(Connection conn, String name, String type, String weapon, int age, int weight) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO characters VALUES (NULL, ?, ?, ?, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, type);
        stmt.setString(3, weapon);
        stmt.setInt(4, age);
        stmt.setInt(5, weight);
        stmt.execute();
    }
    public static Character selectCharacter(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM characters INNER JOIN users ON characters.user_id = users.id WHERE characters.id = ?");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            String name = results.getString("characters.name");
            String type = results.getString("characters.type");
            String weapon = results.getString("characters.weapon");
            int age = results.getInt("characters.age");
            int weight = results.getInt("characters.weight");
            return new Character(name, type, weapon, age, weight);
        }

        return null;
    }
}
