package com.claudiomaiorana.tfg_dnd.model;

import com.claudiomaiorana.tfg_dnd.util.Constants;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Party {

    String ID;
    String IDMaster;
    List<String> IDCharacters;
    String nameParty;
    Boolean open;


    public Party() {}

    public Party(String party, String idMaster){
        this.ID = generateID();
        this.nameParty = party;
        this.IDMaster = idMaster;
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

    public List<String> getIDCharacters() {
        return IDCharacters;
    }

    public void setIDCharacters(List<String> IDCharacters) {
        this.IDCharacters = IDCharacters;
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
