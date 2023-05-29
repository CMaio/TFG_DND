package com.claudiomaiorana.tfg_dnd.model;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class Shield extends Item{

    private String armorClass;

    public Shield(){}

    public Shield(String name, String code, String armorClass) {
        super("shields", name, code);
        this.armorClass = armorClass;
    }

    public Shield(Parcel in) {
        super(in);
        this.armorClass = in.readString();
    }

    public static final Creator<Shield> CREATOR = new Creator<Shield>() {
        @Override
        public Shield createFromParcel(Parcel in) {
            return new Shield(in);
        }

        @Override
        public Shield[] newArray(int size) {
            return new Shield[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(armorClass);
    }

    public String getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(String armorClass) {
        this.armorClass = armorClass;
    }
}
