package com.theironyard;

/**
 * Created by Keith on 4/25/17.
 */
public class Items {
    int user_id;
    String weapon;
    int ammunition;
    String shield;
    Boolean isAlive;

    public Items(int user_id, String weapon, int ammunition, String shield, Boolean isAlive) {
        this.user_id = user_id;
        this.weapon = weapon;
        this.ammunition = ammunition;
        this.shield = shield;
        this.isAlive = isAlive;
    }
}
