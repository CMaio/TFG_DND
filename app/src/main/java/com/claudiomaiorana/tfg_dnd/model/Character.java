package com.claudiomaiorana.tfg_dnd.model;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Character implements Parcelable {

    private String ID;
    private String userID;
    private String partyID;

    private Image imgPlayer;
    private String Name;
    private String Gender;
    private String Pronoun;

    private String ClassPlayer;
    private String codeClassPlayer;

    private String Race;
    private String codeRace;

    private String Alignment;
    private String codeAlignment;

    private int Level = 0;
    private int ArmorClass;
    private int Initiative;
    private int Speed;

    private List<String> Items;
    Map<String,Integer> Skills;

    public Character(){}

    public Character(User user,String characterName,String gender,String pronoun,RCAInfo[] rcaInfo,int level,Image imgPlayer){
        this.ID = user.getUserName() + "_" + characterName;
        this.userID = user.getId();
        this.Skills = new TreeMap<String,Integer>();
        this.Items = new ArrayList<String>();
        this.Name = characterName;
        this.Gender = gender;
        this.Pronoun = pronoun;
        saveRCAInfo(rcaInfo);
        this.Level = level;
        this.imgPlayer = imgPlayer;
    }

    private void saveRCAInfo(RCAInfo[] rcaInfo) {
        this.Race = rcaInfo[0].getTittleText();
        this.codeRace = rcaInfo[0].getCodeApiSearch();
        this.ClassPlayer = rcaInfo[1].getTittleText();
        this.codeClassPlayer = rcaInfo[1].getCodeApiSearch();
        this.Alignment = rcaInfo[2].getTittleText();
        this.codeAlignment = rcaInfo[2].getCodeApiSearch();
    }


    public Character(User user, String characterName,String gender,String pronoun, String Race, String classPlayer, String alignment, int level){
        this.ID = user.getUserName() + "_" + characterName;
        this.userID = user.getId();
        this.Skills = new TreeMap<String,Integer>();
        this.Items = new ArrayList<String>();
        this.Name = characterName;
        this.Gender = gender;
        this.Pronoun = pronoun;
        this.Race = Race;
        this.ClassPlayer = classPlayer;
        this.Alignment = alignment;
        this.Level = level;
        this.imgPlayer = null;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPronoun() {
        return Pronoun;
    }

    public void setPronoun(String pronoun) {
        Pronoun = pronoun;
    }

    public String getID() {
        return ID;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getClassPlayer() {
        return ClassPlayer;
    }

    public void setClassPlayer(String classPlayer) {
        ClassPlayer = classPlayer;
    }

    public String getRace() {
        return Race;
    }

    public void setRace(String race) {
        Race = race;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public int getArmorClass() {
        return ArmorClass;
    }

    public void setArmorClass(int armorClass) {
        ArmorClass = armorClass;
    }

    public int getInitiative() {
        return Initiative;
    }

    public void setInitiative(int initiative) {
        Initiative = initiative;
    }

    public int getSpeed() {
        return Speed;
    }

    public void setSpeed(int speed) {
        Speed = speed;
    }

    public String getAlignment() {
        return Alignment;
    }

    public void setAlignment(String alignment) {
        Alignment = alignment;
    }

    public Map<String, Integer> getSkills() {
        return Skills;
    }

    public void setSkills(Map<String, Integer> skills) {
        Skills = skills;
    }

    public Character(Parcel in){
        ArrayList<String> data = new ArrayList<>();
        in.readStringList(data);
        // the order needs to be the same as in writeToParcel() method
        for (String str : data) {
            if(this.Name == null)
                this.Name = str;
            else if(this.ClassPlayer == null)
                this.ClassPlayer = str;
            else if(this.Race == null)
                this.Race = str;
            else if(this.Level == 0){
                this.Level = Integer.parseInt(str);
            }else if(this.Alignment == null){
                this.Alignment = str;
            }else if(this.Pronoun == null){
                this.Pronoun = str;
            }else if(this.Gender == null){
                this.Gender = str;
            }
        }
    }


    public static final Creator<Character> CREATOR = new Creator<Character>() {
        @Override
        public Character createFromParcel(Parcel in) {
            return new Character(in);
        }

        @Override
        public Character[] newArray(int size) {
            return new Character[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int i) {

        dest.writeString(Name);
        dest.writeString(ClassPlayer);
        dest.writeString(Race);
        dest.writeString(String.valueOf(Level));
        dest.writeString(Alignment);
        dest.writeString(Pronoun);
        dest.writeString(Gender);

    }
}
