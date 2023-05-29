package com.claudiomaiorana.tfg_dnd.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Weapons extends Item {

    private String damageDice;


    public Weapons(String name, String code, String damageDice) {
        super("weapons",name,code);
        this.damageDice = damageDice;

    }

    protected Weapons(Parcel in){
        super(in);
        damageDice = in.readString();
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

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(damageDice);
    }

    public String getDamageDice() {
        return damageDice;
    }

    public void setDamageDice(String damageDice) {
        this.damageDice = damageDice;
    }



}
