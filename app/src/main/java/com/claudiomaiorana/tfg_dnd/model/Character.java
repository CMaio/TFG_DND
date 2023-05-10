package com.claudiomaiorana.tfg_dnd.model;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.claudiomaiorana.tfg_dnd.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Character {

    private String ID, userID, partyID;

    private String name;
    private Drawable imgPlayer;
    private String classPlayer, race, alignment;
    private String codeClass, codeRace, codeAlignment;
    private int level;
    private String gender, pronoun;

    private int[] stats;
    private int[] stats_mod;

    private boolean inspiration;
    private int profBonus;

    private ArrayList<String> savingThrows;
    private ArrayList<String> skills;
    private Map<String, String> proficienciesAndLanguages;

    private int armorClass;
    private int initiative;
    private int speed;

    private int quantityHitDice;
    private int typeHitDice;
    private int maxHitPoints;
    private int currentHitPoints;

    private ArrayList<String> weapons;
    private ArrayList<String> spells;
    private ArrayList<String> equipment;
    private ArrayList<String> featuresAndTraits;

    private int money;

    //Se necesita
    public Character() {
    }

    //Para crear un personaje nuevo
    public Character(User user, String characterName, Drawable imgPlayer, RCAInfo[] rcaInfo,
                     int level, String gender, String pronoun, int[] stats,
                     ArrayList<String> savingThrows, ArrayList<String> skills,
                     Map<String, String> proficienciesAndLanguages, int speed, int quantityHitDice,
                     int typeHitDice) {
        this.ID = user.getUserName() + "_" + characterName;
        this.userID = user.getId();
        this.partyID = "";
        this.name = characterName;
        this.imgPlayer = imgPlayer;
        saveRCAInfo(rcaInfo);
        this.level = level;
        this.gender = gender;
        this.pronoun = pronoun;
        this.stats = stats;
        this.stats_mod = new int[6];
        createMod(stats);
        this.inspiration = false;
        this.profBonus = getProfBonus();
        this.savingThrows = savingThrows;
        this.skills = skills;
        this.proficienciesAndLanguages = proficienciesAndLanguages;
        this.armorClass = 10 + stats_mod[Constants.STAT_CON];
        this.initiative = stats_mod[Constants.STAT_DEX];
        this.speed = speed;
        this.quantityHitDice = quantityHitDice;
        this.typeHitDice = typeHitDice;
        this.maxHitPoints = typeHitDice + stats_mod[Constants.STAT_CON];
        this.currentHitPoints = this.maxHitPoints;
        this.weapons = new ArrayList<String>();
        this.spells = new ArrayList<String>();
        this.equipment = new ArrayList<String>();
        this.featuresAndTraits = new ArrayList<String>();
        this.money = 0;

    }

    //Para cuando ya hay un personaje creado y tengo toda la info
    private Character(User user, String partyID, String characterName, Drawable imgPlayer, RCAInfo[] rcaInfo,
                      int level, String gender, String pronoun, int[] stats, int[] stats_mod, Boolean inspiration,
                      int profBonus,ArrayList<String> savingThrows, ArrayList<String> skills,
                      Map<String, String> proficienciesAndLanguages, int armorClass, int initiative,
                      int speed, int quantityHitDice, int typeHitDice, int maxHitPoints, int currentHitPoints,
                      ArrayList<String> weapons, ArrayList<String> spells, ArrayList<String> equipment,
                      ArrayList<String> featuresAndTraits, int money) {
        this.ID = user.getUserName() + "_" + characterName;
        this.userID = user.getId();
        this.partyID = partyID;
        this.name = characterName;
        this.imgPlayer = imgPlayer;
        saveRCAInfo(rcaInfo);
        this.level = level;
        this.gender = gender;
        this.pronoun = pronoun;
        this.stats = stats;
        this.stats_mod = stats_mod;
        this.inspiration = inspiration;
        this.profBonus = profBonus;
        this.savingThrows = savingThrows;
        this.skills = skills;
        this.proficienciesAndLanguages = proficienciesAndLanguages;
        this.armorClass = armorClass;
        this.initiative = initiative;
        this.speed = speed;
        this.quantityHitDice = quantityHitDice;
        this.typeHitDice = typeHitDice;
        this.maxHitPoints = maxHitPoints;
        this.currentHitPoints = currentHitPoints;
        this.weapons = weapons;
        this.spells = spells;
        this.equipment = equipment;
        this.featuresAndTraits = featuresAndTraits;
        this.money = money;
    }

    private void saveRCAInfo(RCAInfo[] rcaInfo) {
        this.race = rcaInfo[0].getTittleText();
        this.codeRace = rcaInfo[0].getCodeApiSearch();
        this.classPlayer = rcaInfo[1].getTittleText();
        this.codeClass = rcaInfo[1].getCodeApiSearch();
        this.alignment = rcaInfo[2].getTittleText();
        this.codeAlignment = rcaInfo[2].getCodeApiSearch();
    }

    private void createMod(int[] stats) {
        int mod = -6;
        for (int i = 0; i < stats.length; i++) {
            mod = (int) Math.floor((stats[i] - 10) / 2.0);
            stats_mod[i] = mod;
        }
    }

    private int getProfBonus() {
        if (0 < level && level < 5) {
            return 2;
        } else if (4 < level && level < 9) {
            return 3;
        } else if (8 < level && level < 13) {
            return 4;
        } else if (12 < level && level < 17) {
            return 5;
        } else if (16 < level && level < 21) {
            return 6;
        }
        return 0;
    }

    public String getID() {
        return ID;
    }

    public String getUserID() {
        return userID;
    }

    public String getPartyID() {
        return partyID;
    }

    public void setPartyID(String partyID) {
        this.partyID = partyID;
    }

    public String getName() {
        return name;
    }

    public Drawable getImgPlayer() {
        return imgPlayer;
    }

    public String getClassPlayer() {
        return classPlayer;
    }

    public String getRace() {
        return race;
    }

    public String getAlignment() {
        return alignment;
    }

    public String getCodeClass() {
        return codeClass;
    }

    public String getCodeRace() {
        return codeRace;
    }

    public String getCodeAlignment() {
        return codeAlignment;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getGender() {
        return gender;
    }

    public String getPronoun() {
        return pronoun;
    }

    public int[] getStats() {
        return stats;
    }

    public void setStats(int[] stats) {
        this.stats = stats;
    }

    public int[] getStats_mod() {
        return stats_mod;
    }

    public void setStats_mod(int[] stats_mod) {
        this.stats_mod = stats_mod;
    }

    public boolean isInspiration() {
        return inspiration;
    }

    public void setInspiration(boolean inspiration) {
        this.inspiration = inspiration;
    }

    public void setProfBonus(int profBonus) {
        this.profBonus = profBonus;
    }

    public ArrayList<String> getSavingThrows() {
        return savingThrows;
    }

    public void setSavingThrows(ArrayList<String> savingThrows) {
        this.savingThrows = savingThrows;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public Map<String, String> getProficienciesAndLanguages() {
        return proficienciesAndLanguages;
    }

    public void setProficienciesAndLanguages(Map<String, String> proficienciesAndLanguages) {
        this.proficienciesAndLanguages = proficienciesAndLanguages;
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

    public int getQuantityHitDice() {
        return quantityHitDice;
    }

    public void setQuantityHitDice(int quantityHitDice) {
        this.quantityHitDice = quantityHitDice;
    }

    public int getTypeHitDice() {
        return typeHitDice;
    }

    public void setTypeHitDice(int typeHitDice) {
        this.typeHitDice = typeHitDice;
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

    public ArrayList<String> getWeapons() {
        return weapons;
    }

    public void setWeapons(ArrayList<String> weapons) {
        this.weapons = weapons;
    }

    public ArrayList<String> getSpells() {
        return spells;
    }

    public void setSpells(ArrayList<String> spells) {
        this.spells = spells;
    }

    public ArrayList<String> getEquipment() {
        return equipment;
    }

    public void setEquipment(ArrayList<String> equipment) {
        this.equipment = equipment;
    }

    public ArrayList<String> getFeaturesAndTraits() {
        return featuresAndTraits;
    }

    public void setFeaturesAndTraits(ArrayList<String> featuresAndTraits) {
        this.featuresAndTraits = featuresAndTraits;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}