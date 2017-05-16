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

    public Character(int id, String name, String type, String weapon, int age, int weight) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.weapon = weapon;
        this.age = age;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
