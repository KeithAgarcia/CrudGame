package com.theironyard;

/**
 * Created by Keith on 4/25/17.
 */
public class Character {
    int id;
    String name;
    String type;
    String weapon;
    int age;
    int weight;

    public Character(String name, String type, String weapon, int age, int weight) {
        this.name = name;
        this.type = type;
        this.weapon = weapon;
        this.age = age;
        this.weight = weight;
    }
}
