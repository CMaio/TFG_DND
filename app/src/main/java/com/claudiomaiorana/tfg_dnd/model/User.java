package com.claudiomaiorana.tfg_dnd.model;

public class User {
    private static volatile User INSTANCE = null;

    String userName;
    String id;
    String mail;
    String characters;
    String parties;


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
        this.characters = "";
        this.parties = "";
    }

    public void fillUser(String id,String userName,String mail){
        this.userName = userName;
        this.id = id;
        this.mail = mail;
        this.characters = "";
        this.parties = "";
    }

    public String getCharacters() {
        return characters;
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

    public String getParties() {
        return parties;
    }
}
