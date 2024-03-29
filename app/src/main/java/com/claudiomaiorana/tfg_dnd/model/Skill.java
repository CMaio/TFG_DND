package com.claudiomaiorana.tfg_dnd.model;

import java.util.Objects;

public class Skill {
    private String code;
    private String name;
    private boolean isSelected;

    public Skill(){}
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill sk = (Skill) o;
        return code == sk.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
