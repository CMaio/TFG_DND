package com.claudiomaiorana.tfg_dnd.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

    private String type;

    private String name;
    private String code;

    private String armorClass;

    private String base;
    private int maxBonus;

    private String desc;

    private String damageDice;
    private boolean hitMelee;

    public Item(){

    }

    public Item(String type, String name, String code, String armorClass, String base, int maxBonus, String desc, String damageDice, boolean hitMelee) {
        this.type = type;
        this.name = name;
        this.code = code;
        this.armorClass = armorClass;
        this.base = base;
        this.maxBonus = maxBonus;
        this.desc = desc;
        this.damageDice = damageDice;
        this.hitMelee = hitMelee;
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

    public String getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(String armorClass) {
        this.armorClass = armorClass;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDamageDice() {
        return damageDice;
    }

    public void setDamageDice(String damageDice) {
        this.damageDice = damageDice;
    }

    public boolean isHitMelee() {
        return hitMelee;
    }

    public void setHitMelee(boolean hitMelee) {
        this.hitMelee = hitMelee;
    }
}
