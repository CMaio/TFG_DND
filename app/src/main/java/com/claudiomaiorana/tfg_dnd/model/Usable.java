package com.claudiomaiorana.tfg_dnd.model;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class Usable extends Item{

    private String desc;

    public Usable(){}

    public Usable(String name, String code,String desc) {
        super("usables", name, code);
        this.desc = desc;
    }

    protected Usable(Parcel in){
        super(in);
        desc = in.readString();
    }

    public static final Creator<Usable> CREATOR = new Creator<Usable>() {
        @Override
        public Usable createFromParcel(Parcel in) {
            return new Usable(in);
        }

        @Override
        public Usable[] newArray(int size) {
            return new Usable[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(desc);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
