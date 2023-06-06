package com.claudiomaiorana.tfg_dnd.model;

import java.util.ArrayList;

public class Enemy {

    private String ID="";
    private String name="";

    private String hitDice;
    private int maxHitPoints;
    private int currentHitPoints;
    private int armorClass;

    private int initiative;
    private int speed;

    private ArrayList<ProfLang> featuresAndTraits;
    private ArrayList<Item> attacks;

    private Boolean selected;

    public Enemy(){}

    public Enemy(String ID, String name, String hitDice, int maxHitPoints, int armorClass, int speed, ArrayList<ProfLang> featuresAndTraits, ArrayList<Item> attacks) {
        this.ID = ID;
        this.name = name;
        this.hitDice = hitDice;
        this.maxHitPoints = maxHitPoints;
        this.currentHitPoints = maxHitPoints;
        this.armorClass = armorClass;
        this.initiative = 0;
        this.speed = speed;
        this.featuresAndTraits = featuresAndTraits;
        this.attacks = attacks;
        this.selected = false;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHitDice() {
        return hitDice;
    }

    public void setHitDice(String hitDice) {
        this.hitDice = hitDice;
    }

    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    public void setMaxHitPoints(int maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public int getCurrentHitPoints() {
        return currentHitPoints;
    }

    public void setCurrentHitPoints(int currentHitPoints) {
        this.currentHitPoints = currentHitPoints;
    }

    public int getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(int armorClass) {
        this.armorClass = armorClass;
    }

    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public ArrayList<ProfLang> getFeaturesAndTraits() {
        return featuresAndTraits;
    }

    public void setFeaturesAndTraits(ArrayList<ProfLang> featuresAndTraits) {
        this.featuresAndTraits = featuresAndTraits;
    }

    public ArrayList<Item> getAttacks() {
        return attacks;
    }

    public void setAttacks(ArrayList<Item> attacks) {
        this.attacks = attacks;
    }

    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
