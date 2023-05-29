package com.claudiomaiorana.tfg_dnd.model;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class Armor extends Item{
    private String base;
    private int maxBonus;

    public Armor(){}


    public Armor( String name, String code, String base, int maxBonus) {
        super("armors", name, code);
        this.base = base;
        this.maxBonus = maxBonus;
    }

    public Armor(Parcel in) {
        super(in);
        this.base = in.readString();
        this.maxBonus = in.readInt();
    }

    public static final Creator<Armor> CREATOR = new Creator<Armor>() {
        @Override
        public Armor createFromParcel(Parcel in) {
            return new Armor(in);
        }

        @Override
        public Armor[] newArray(int size) {
            return new Armor[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(base);
        parcel.writeInt(maxBonus);
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public int getMaxBonus() {
        return maxBonus;
    }

    public void setMaxBonus(int maxBonus) {
        this.maxBonus = maxBonus;
    }
}
