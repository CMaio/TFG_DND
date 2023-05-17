package com.claudiomaiorana.tfg_dnd.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static volatile User INSTANCE = null;

    String userName;
    String id;
    String mail;
    ArrayList<String> parties;

    public User(){}

    public static User getInstance(){
        if(INSTANCE == null) {
            synchronized (User.class) {
                if (INSTANCE == null) {
                    INSTANCE = new User();
                }
            }
        }
        return INSTANCE;
    }

    public User(String id,String userName,String mail){
        this.userName = userName;
        this.id = id;
        this.mail = mail;
        this.parties = new ArrayList<>();
    }

    public void fillUser(String id,String userName,String mail){
        this.userName = userName;
        this.id = id;
        this.mail = mail;
        this.parties = new ArrayList<>();
    }


    public void fillUser(String id,String userName,String mail,ArrayList<String> parties){
        this.userName = userName;
        this.id = id;
        this.mail = mail;
        this.parties = parties;
    }

    public String getUserName() {
        return userName;
    }

    public String getId() {
        return id;
    }

    public String getMail() {
        return mail;
    }

    public ArrayList<String> getParties() {
        return parties;
    }

    public void setParties(ArrayList<String> parties) {
        this.parties = parties;
    }
}
