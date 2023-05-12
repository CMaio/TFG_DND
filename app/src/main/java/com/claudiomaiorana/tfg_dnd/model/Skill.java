package com.claudiomaiorana.tfg_dnd.model;

public class Skill {
    private String code;
    private String name;
    private boolean isSelected;

    public Skill(String code, String name) {
        this.code = code;
        this.name = name;
        this.isSelected = false;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
