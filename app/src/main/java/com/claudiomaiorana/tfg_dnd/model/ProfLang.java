package com.claudiomaiorana.tfg_dnd.model;

import java.util.Objects;

public class ProfLang {
    private String code;
    private String name;
    private boolean isSelected;

    public ProfLang(){}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfLang profLang = (ProfLang) o;
        return code == profLang.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}

