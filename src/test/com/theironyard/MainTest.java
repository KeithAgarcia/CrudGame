package com.theironyard;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created by Keith on 4/25/17.
 */
public class MainTest {

    public Connection startConnection () throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Main.createTables(conn);
        return conn;
    }
    @Test
    public void testUser() throws SQLException {
        Connection conn = startConnection();
        Main.addUser(conn, "Keith", "james");
        User user = Main.selectUser(conn, "Keith");
        conn.close();
        assertTrue(user != null);
    }
    @Test
    public void testItems() throws SQLException {
        Connection conn = startConnection();
        Main.addUser(conn, "Alice", "");
        User user = Main.selectUser(conn, "Alice");
        Main.insertItems(conn, user.id, "Sword", 247, "steel",false);
        Items items = Main.selectItems(conn, 1);
        conn.close();
        assertTrue(items != null);
    }
}