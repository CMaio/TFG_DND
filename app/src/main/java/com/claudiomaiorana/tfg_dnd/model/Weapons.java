package com.claudiomaiorana.tfg_dnd.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Weapons implements Parcelable{
    private String type;

    private String name;
    private String code;

    private String damageDice;

    private int armorBase;



    public Weapons(String type, String name, String code, String damageDice) {
        this.name = name;
        this.type = type;
        this.code = code;
        this.damageDice = damageDice;
        this.armorBase = -1;

    }

    public Weapons(String type, String name, String code, int armorBase) {
        this.name = name;
        this.type = type;
        this.code = code;
        this.armorBase = armorBase;
        this.damageDice = "";
    }

    protected Weapons(Parcel in) {
        type = in.readString();
        name = in.readString();
        code = in.readString();
        damageDice = in.readString();
        armorBase = in.readInt();
    }

    public static final Creator<Weapons> CREATOR = new Creator<Weapons>() {
        @Override
        public Weapons createFromParcel(Parcel in) {
            return new Weapons(in);
        }

        @Override
        public Weapons[] newArray(int size) {
            return new Weapons[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDamageDice() {
        return damageDice;
    }

    public void setDamageDice(String damageDice) {
        this.damageDice = damageDice;
    }

    public int getArmorBase() {
        return armorBase;
    }

    public void setArmorBase(int armorBase) {
        this.armorBase = armorBase;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeString(name);
        parcel.writeString(code);
        parcel.writeString(damageDice);
        parcel.writeInt(armorBase);
    }
}
