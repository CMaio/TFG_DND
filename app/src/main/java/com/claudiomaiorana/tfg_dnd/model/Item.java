package com.claudiomaiorana.tfg_dnd.model;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class Item implements Parcelable {

    private String type;

    private String name;
    private String code;

    public Item(){

    }

    public Item(String type, String name, String code) {
        this.type = type;
        this.name = name;
        this.code = code;
    }

    protected Item(Parcel in) {
        type = in.readString();
        name = in.readString();
        code = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return null;
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(code);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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
}
