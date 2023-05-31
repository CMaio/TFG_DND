package com.claudiomaiorana.tfg_dnd.model;

public class PossibleAttack {

    private String name;
    private String hitDice;
    private String type;
    private Boolean clicked;
    private Boolean hitMelee;

    public PossibleAttack(String name, String hitDice, String type,Boolean hitMelee) {
        this.name = name;
        this.hitDice = hitDice;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean isClicked() {
        return clicked;
    }

    public void setClicked(Boolean clicked) {
        this.clicked = clicked;
    }

    public Boolean getHitMelee() {
        return hitMelee;
    }

    public void setHitMelee(Boolean hitMelee) {
        this.hitMelee = hitMelee;
    }
}
