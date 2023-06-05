package com.claudiomaiorana.tfg_dnd.model;

import com.claudiomaiorana.tfg_dnd.util.Constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Party {

    String ID;
    String IDMaster;
    String UsernameMaster;
    ArrayList<String> playersConnected;
    ArrayList<Character> players;
    ArrayList<Enemy> enemiesFight;

    String nameParty;

    Boolean open;
    Boolean fighting = false;

    String turn;
    String lastTurn;

    Boolean allReady;

    Map<String,Integer> orderParty;

    public Party() {}

    public Party(String party, String idMaster,String usernameMaster){
        this.ID = generateID();
        this.nameParty = party;
        this.IDMaster = idMaster;
        this.players = new ArrayList<>();
        this.playersConnected = new ArrayList<>();
        this.open = false;
        this.UsernameMaster = usernameMaster;
        this.turn = "none";
        this.lastTurn = "none";
        this.fighting = false;
        this.enemiesFight = new ArrayList<>();
        this.allReady = false;
        this.orderParty = new LinkedHashMap<>();
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

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public ArrayList<Enemy> getEnemiesFight() {
        return enemiesFight;
    }

    public void setEnemiesFight(ArrayList<Enemy> enemiesFight) {
        this.enemiesFight = enemiesFight;
    }

    public Boolean getFighting() {
        return fighting;
    }

    public void setFighting(Boolean fighting) {
        this.fighting = fighting;
    }

    public String getLastTurn() {
        return lastTurn;
    }

    public void setLastTurn(String lastTurn) {
        this.lastTurn = lastTurn;
    }

    public Boolean getAllReady() {
        return allReady;
    }

    public void setAllReady(Boolean allReady) {
        this.allReady = allReady;
    }

    public Map<String, Integer> getOrderParty() {
        return orderParty;
    }

    public void setOrderParty(Map<String, Integer> orderParty) {
        this.orderParty = orderParty;
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
