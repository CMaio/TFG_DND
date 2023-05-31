package com.claudiomaiorana.tfg_dnd.model;

public class EnemySelector {

    private String name;
    private int hitPoints;
    private int userValue;

    public EnemySelector(String name, int hitPoints) {
        this.name = name;
        this.hitPoints = hitPoints;
        this.userValue = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public int getUserValue() {
        return userValue;
    }

    public void setUserValue(int userValue) {
        this.userValue = userValue;
    }
}
