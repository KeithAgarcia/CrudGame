package com.theironyard;

import java.util.ArrayList;

/**
 * Created by Keith on 4/25/17.
 */
public class User extends ArrayList<Character> {
     private int id; //private
     private String userName; // private
     private String password; //private

    public User(Integer id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
