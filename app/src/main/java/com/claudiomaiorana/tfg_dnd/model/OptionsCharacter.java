package com.claudiomaiorana.tfg_dnd.model;

import java.util.Objects;

public class OptionsCharacter {
    private String code;
    private String name;
    private boolean isSelected;
    private int bonus;

    public OptionsCharacter(){}

    public OptionsCharacter(String code, String name) {
        this.code = code;
        this.name = name;
        this.isSelected = false;
        this.bonus = 0;
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

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptionsCharacter optChar = (OptionsCharacter) o;
        return code == optChar.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
