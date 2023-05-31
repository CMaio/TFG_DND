package com.claudiomaiorana.tfg_dnd.model;

import android.os.Build;
import android.os.Parcel;

import androidx.annotation.NonNull;

public class Weapons extends Item {

    private String damageDice;
    //TODO:Implementar esto
    private boolean hitMelee;


    public Weapons(String name, String code, String damageDice, boolean hitMelee) {
        super("weapons",name,code);
        this.damageDice = damageDice;
        this.hitMelee = hitMelee;
    }

    protected Weapons(Parcel in){
        super(in);
        damageDice = in.readString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            hitMelee = in.readBoolean();
        }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            parcel.writeBoolean(hitMelee);
        }
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
