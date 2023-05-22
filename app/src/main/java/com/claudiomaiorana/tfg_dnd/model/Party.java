package com.claudiomaiorana.tfg_dnd.model;

import com.claudiomaiorana.tfg_dnd.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Party {

    String ID;
    String IDMaster;
    String UsernameMaster;
    ArrayList<String> playersConnected;
    ArrayList<Character> players;

    String nameParty;
    Boolean open;


    public Party() {}

    public Party(String party, String idMaster,String usernameMaster){
        this.ID = generateID();
        this.nameParty = party;
        this.IDMaster = idMaster;
        this.players = new ArrayList<>();
        this.playersConnected = new ArrayList<>();
        this.open = false;
        this.UsernameMaster = usernameMaster;
    }

    public String getUsernameMaster() {
        return UsernameMaster;
    }

    public void setUsernameMaster(String usernameMaster) {
        UsernameMaster = usernameMaster;
    }

    public ArrayList<String> getPlayersConnected() {
        return playersConnected;
    }

    public void setPlayersConnected(ArrayList<String> playersConnected) {
        this.playersConnected = playersConnected;
    }

    public ArrayList<Character> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Character> players) {
        this.players = players;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIDMaster() {
        return IDMaster;
    }

    public void setIDMaster(String IDMaster) {
        this.IDMaster = IDMaster;
    }

    public String getNameParty() {
        return nameParty;
    }

    public void setNameParty(String nameParty) {
        this.nameParty = nameParty;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }


    public static String generateID() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < Constants.LONGITUD_ID; i++) {
            int index = random.nextInt(Constants.CARACTERES.length());
            char character = Constants.CARACTERES.charAt(index);
            sb.append(character);
        }
        return sb.toString();
    }
}
