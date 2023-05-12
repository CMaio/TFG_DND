package com.claudiomaiorana.tfg_dnd.model;

public class ProfLang {
    private String code;
    private String name;
    private boolean isSelected;

    public ProfLang(String code, String name) {
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

