package com.claudiomaiorana.tfg_dnd.model;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.claudiomaiorana.tfg_dnd.util.Constants;
import com.claudiomaiorana.tfg_dnd.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class Character implements Parcelable{

    private String ID, userID, partyID;

    private String name;
    private String imgPlayerName;
    private String classPlayer, race, alignment;
    private String codeClass, codeRace, codeAlignment;
    private int level;
    private String gender, pronoun;

    private List<Integer> stats;
    private List<Integer> stats_mod;

    private boolean inspiration;
    private int profBonus;

    private String spellCastingAbility;
    private int spellCastingStatMod;

    private ArrayList<String> savingThrows;
    private ArrayList<Skill> skills;
    private ArrayList<ProfLang> proficienciesAndLanguages;

    private int armorClass;
    private int initiative;
    private int initiativeMod;
    private int speed;

    private int quantityHitDice;
    private int typeHitDice;
    private int maxHitPoints;
    private int currentHitPoints;

    private ArrayList<Item> weaponEquipped;
    private ArrayList<Item> items;
    private Spells spells;
    private ArrayList<ProfLang> featuresAndTraits;

    private int moneyPlatinum;
    private int moneyGold;
    private int moneySilver;
    private int moneyCopper;

    private boolean selected;

    //Se necesita
    public Character() {
    }

    //Para crear un personaje nuevo
    public Character(User user, String characterName, String imgPlayer, RCAInfo[] rcaInfo,
                     int level, String gender, String pronoun, List<Integer> stats,
                     ArrayList<String> savingThrows, ArrayList<Skill> skills,
                     ArrayList<ProfLang> proficienciesAndLanguages, int speed, int quantityHitDice,
                     int typeHitDice,ArrayList<ProfLang> traits,Spells spells,String spellCastingAbility) {
        this.ID = user.getUserName() + "_" + characterName + "_" + UUID.randomUUID().toString();
        this.userID = user.getId();
        this.partyID = "";
        this.name = characterName;
        this.imgPlayerName = imgPlayer;
        saveRCAInfo(rcaInfo);
        this.level = level;
        this.gender = gender;
        this.pronoun = pronoun;
        this.stats = stats;
        this.stats_mod = new ArrayList<>();
        createMod(stats);
        this.inspiration = false;
        this.profBonus = getProfBonus();
        this.savingThrows = savingThrows;
        this.skills = skills;
        this.proficienciesAndLanguages = proficienciesAndLanguages;
        this.armorClass = 10 + stats_mod.get(Constants.STAT_CON);
        this.initiative = 0;
        this.initiativeMod = stats_mod.get(Constants.STAT_DEX);
        this.speed = speed;
        this.quantityHitDice = quantityHitDice;
        this.typeHitDice = typeHitDice;
        this.maxHitPoints = typeHitDice + stats_mod.get(Constants.STAT_CON);
        this.currentHitPoints = this.maxHitPoints;
        this.weaponEquipped = new ArrayList<Item>();
        this.spells = spells;
        this.items = new ArrayList<Item>();
        this.featuresAndTraits = traits;
        this.moneyPlatinum = 0;
        this.moneyGold = 0;
        this.moneySilver = 0;
        this.moneyCopper = 0;
        this.spellCastingAbility = spellCastingAbility;
        setSpellCastingAbilityMod(this.spellCastingAbility);
        this.selected = false;

    }

    //Para cuando ya hay un personaje creado y tengo toda la info
    private Character(User user, String partyID, String characterName, String imgPlayer, RCAInfo[] rcaInfo,
                      int level, String gender, String pronoun, List<Integer> stats, List<Integer> stats_mod, Boolean inspiration,
                      int profBonus,ArrayList<String> savingThrows, ArrayList<Skill> skills,
                      ArrayList<ProfLang> proficienciesAndLanguages, int armorClass, int initiative,int initiativeMod,
                      int speed, int quantityHitDice, int typeHitDice, int maxHitPoints, int currentHitPoints,
                      ArrayList<Item> weapons, Spells spells, ArrayList<Item> equipment,
                      ArrayList<ProfLang> featuresAndTraits, int moneyPlatinum,int moneyGold,int moneySilver,
                      int moneyCopper,String spellCastingAbility,int spellCastingStatMod) {
        this.ID = user.getUserName() + "_" + characterName + "_" + UUID.randomUUID().toString();
        this.userID = user.getId();
        this.partyID = partyID;
        this.name = characterName;
        this.imgPlayerName = imgPlayer;
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
        this.initiativeMod = initiativeMod;
        this.speed = speed;
        this.quantityHitDice = quantityHitDice;
        this.typeHitDice = typeHitDice;
        this.maxHitPoints = maxHitPoints;
        this.currentHitPoints = currentHitPoints;
        this.weaponEquipped = weapons;
        this.spells = spells;
        this.items = equipment;
        this.featuresAndTraits = featuresAndTraits;
        this.moneyPlatinum = moneyPlatinum;
        this.moneyGold = moneyGold;
        this.moneySilver = moneySilver;
        this.moneyCopper = moneyCopper;
        this.spellCastingAbility = spellCastingAbility;
        this.spellCastingStatMod = spellCastingStatMod;
        this.selected = false;
    }

    private Character(Parcel in) {
        ID = in.readString();
        userID = in.readString();
        partyID = in.readString();
        name = in.readString();
        imgPlayerName = in.readString();
        race = in.readString();
        codeRace = in.readString();
        classPlayer =in.readString();
        codeClass = in.readString();
        alignment = in.readString();
        codeAlignment = in.readString();
        level = in.readInt();
        gender = in.readString();
        pronoun = in.readString();
        stats = new ArrayList<>();
        in.readList(stats, Integer.class.getClassLoader());
        this.stats_mod = new ArrayList<>();
        createMod(stats);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            inspiration = in.readBoolean();
        }
        profBonus = getProfBonus();
        savingThrows = new ArrayList<>();
        in.readList(savingThrows, String.class.getClassLoader());
        skills = new ArrayList<>();
        in.readList(skills, Skill.class.getClassLoader());
        proficienciesAndLanguages = new ArrayList<>();
        in.readList(proficienciesAndLanguages, ProfLang.class.getClassLoader());
        armorClass = 10 + stats_mod.get(Constants.STAT_CON);
        initiative = in.readInt();
        initiativeMod = stats_mod.get(Constants.STAT_DEX);
        speed = in.readInt();
        quantityHitDice = in.readInt();
        typeHitDice = in.readInt();
        maxHitPoints = typeHitDice + stats_mod.get(Constants.STAT_CON);
        currentHitPoints = in.readInt();
        weaponEquipped = new ArrayList<>();
        in.readList(weaponEquipped, Item.class.getClassLoader());
        spells = in.readParcelable(Spells.class.getClassLoader());
        items = new ArrayList<>();
        in.readList(items, Item.class.getClassLoader());
        featuresAndTraits = new ArrayList<>();
        in.readList(featuresAndTraits, ProfLang.class.getClassLoader());
        moneyPlatinum = in.readInt();
        moneyGold = in.readInt();
        moneySilver = in.readInt();
        moneyCopper = in.readInt();
        spellCastingAbility = in.readString();
        spellCastingStatMod = in.readInt();
        this.selected = false;
    }

    private void saveRCAInfo(RCAInfo[] rcaInfo) {
        this.race = rcaInfo[0].getTittleText();
        this.codeRace = rcaInfo[0].getCodeApiSearch();
        this.classPlayer = rcaInfo[1].getTittleText();
        this.codeClass = rcaInfo[1].getCodeApiSearch();
        this.alignment = rcaInfo[2].getTittleText();
        this.codeAlignment = rcaInfo[2].getCodeApiSearch();
    }

    private void createMod(List<Integer> stats) {
        int mod = -6;
        for (int i = 0; i < stats.size(); i++) {
            mod = (int) Math.floor((stats.get(i) - 10) / 2.0);
            stats_mod.add(mod);
        }
    }

    public int getProfBonus() {
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

    public int getSavingThrowSpell(){
        return 8 + stats_mod.get(spellCastingStatMod);
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getImgPlayerName() {
        return imgPlayerName;
    }

    public void setImgPlayerName(String imgPlayerName) {
        this.imgPlayerName = imgPlayerName;
    }

    public String getClassPlayer() {
        return classPlayer;
    }

    public void setClassPlayer(String classPlayer) {
        this.classPlayer = classPlayer;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public String getCodeClass() {
        return codeClass;
    }

    public void setCodeClass(String codeClass) {
        this.codeClass = codeClass;
    }

    public String getCodeRace() {
        return codeRace;
    }

    public void setCodeRace(String codeRace) {
        this.codeRace = codeRace;
    }

    public String getCodeAlignment() {
        return codeAlignment;
    }

    public void setCodeAlignment(String codeAlignment) {
        this.codeAlignment = codeAlignment;
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

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPronoun() {
        return pronoun;
    }

    public void setPronoun(String pronoun) {
        this.pronoun = pronoun;
    }

    public List<Integer> getStats() {
        return stats;
    }

    public void setStats(List<Integer> stats) {
        this.stats = stats;
    }

    public List<Integer> getStats_mod() {
        return stats_mod;
    }

    public int getSpecificStats_Mod(int i){return stats_mod.get(i);}

    public void setStats_mod(List<Integer> stats_mod) {
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

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<Skill> skills) {
        this.skills = skills;
    }

    public ArrayList<ProfLang> getProficienciesAndLanguages() {
        return proficienciesAndLanguages;
    }

    public void setProficienciesAndLanguages(ArrayList<ProfLang> proficienciesAndLanguages) {
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

    public Spells getSpells() {
        return spells;
    }

    public void setSpells(Spells spells) {
        this.spells = spells;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<ProfLang> getFeaturesAndTraits() {
        return featuresAndTraits;
    }

    public void setFeaturesAndTraits(ArrayList<ProfLang> featuresAndTraits) {
        this.featuresAndTraits = featuresAndTraits;
    }

    public ArrayList<Item> getWeaponEquipped() {
        return weaponEquipped;
    }

    public void setWeaponEquipped(ArrayList<Item> weaponEquipped) {
        this.weaponEquipped = weaponEquipped;
    }

    public int getMoneyPlatinum() {
        return moneyPlatinum;
    }

    public void setMoneyPlatinum(int moneyPlatinum) {
        this.moneyPlatinum = moneyPlatinum;
    }

    public int getMoneyGold() {
        return moneyGold;
    }

    public void setMoneyGold(int moneyGold) {
        this.moneyGold = moneyGold;
    }

    public int getMoneySilver() {
        return moneySilver;
    }

    public void setMoneySilver(int moneySilver) {
        this.moneySilver = moneySilver;
    }

    public int getMoneyCopper() {
        return moneyCopper;
    }

    public void setMoneyCopper(int moneyCopper) {
        this.moneyCopper = moneyCopper;
    }

    public String getSpellCastingAbility() {
        return spellCastingAbility;
    }

    public void setSpellCastingAbility(String spellCastingAbility){this.spellCastingAbility = spellCastingAbility; }

    public int getInitiativeMod() {
        return initiativeMod;
    }

    public void setInitiativeMod(int initiativeMod) {
        this.initiativeMod = initiativeMod;
    }

    public void setSpellCastingAbilityMod(String spellCastingAbility) {
        for (int i=0;i< Constants.TYPE_OF_STATS.length;i++){
            if(Constants.TYPE_OF_STATS[i].equals(spellCastingAbility)){
                this.spellCastingStatMod = i;
            }
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(userID);
        dest.writeString(partyID);
        dest.writeString(name);
        dest.writeString(race);
        dest.writeString(codeRace);
        dest.writeString(classPlayer);
        dest.writeString(codeClass);
        dest.writeString(alignment);
        dest.writeInt(level);
        dest.writeString(gender);
        dest.writeString(pronoun);
        dest.writeList(stats);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(inspiration);
        }
        dest.writeList(savingThrows);
        dest.writeList(skills);
        dest.writeList(proficienciesAndLanguages);
        dest.writeInt(initiative);
        dest.writeInt(speed);
        dest.writeInt(quantityHitDice);
        dest.writeInt(typeHitDice);
        dest.writeInt(currentHitPoints);
        dest.writeList(weaponEquipped);
        dest.writeParcelable(spells,0);
        dest.writeList(items);
        dest.writeList(featuresAndTraits);
        dest.writeInt(moneyPlatinum);
        dest.writeInt(moneyGold);
        dest.writeInt(moneySilver);
        dest.writeInt(moneyCopper);
        dest.writeString(spellCastingAbility);
        dest.writeInt(spellCastingStatMod);
        this.selected = false;
    }


    public void addMoney(int copper, int silver, int gold, int platinum) {
        this.moneyPlatinum += platinum;
        this.moneyGold += gold;
        this.moneySilver += silver;
        this.moneyCopper += copper;
        while (this.moneyCopper >= 100) {
            this.moneyCopper -= 100;
            this.moneySilver += 1;
        }

        while (this.moneySilver >= 100) {
            this.moneySilver -= 100;
            this.moneyGold += 100;
        }

        while (this.moneyGold >= 100) {
            this.moneyGold -= 10;
            this.moneyPlatinum += 1;
        }
    }

    public void subtractMoney(int copper, int silver, int gold, int platinum) {
        this.moneyPlatinum -= platinum;
        this.moneyGold -= gold;
        this.moneySilver -= silver;
        this.moneyCopper -= copper;

        while (this.moneyCopper < 0 && this.moneySilver > 0) {
            this.moneyCopper += 100;
            this.moneySilver -= 1;
        }

        while (this.moneySilver < 0 && this.moneyGold > 0) {
            this.moneySilver += 10;
            this.moneyGold -= 1;
        }

        while (this.moneyGold < 0 && this.moneyPlatinum > 0) {
            this.moneyGold += 10;
            this.moneyPlatinum -= 1;
        }
    }


    public static final Parcelable.Creator<Character> CREATOR = new Parcelable.Creator<Character>() {
        public Character createFromParcel(Parcel in) {
            return new Character(in);
        }

        public Character[] newArray(int size) {
            return new Character[size];
        }
    };
}