package com.claudiomaiorana.tfg_dnd.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.android.volley.VolleyError;
import com.claudiomaiorana.tfg_dnd.util.ApiCallback;
import com.claudiomaiorana.tfg_dnd.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spells implements Parcelable {

    private int cantrips = -1;
    private int spells_known = -1;
    private Map<String,Integer> spells = new HashMap<>();
    private Map<String, List<Spell>> spellsName = new HashMap<>();
    private Map<String, List<Spell>> spellsDamage = new HashMap<>();

    public Spells(){}

    public Spells (ArrayList<Integer> quantitySpells, ArrayList<JSONArray> spellsCode,JSONArray spellsClass,int spells_known) throws JSONException {
        this.spells_known = spells_known;
        spells = new HashMap<>();
        for(int i=0;i<quantitySpells.size();i++){
            if(i == 0){cantrips = quantitySpells.get(i);}
            else{
                spells.put(Integer.toString(i),quantitySpells.get(i));
            }
        }
        addSpellsName(spellsCode,spellsClass);

    }

    public void addSpellsName(ArrayList<JSONArray> spellsAllLevels,JSONArray spellsClass) throws JSONException {
        for(int i = 0; i<spellsAllLevels.size();i++){
            JSONArray spellsOfLevel = spellsAllLevels.get(i);
            JSONObject spellOfLevel = null;
            spellsName.put(Integer.toString(i),new ArrayList<>());
            for(int j = 0; j<spellsOfLevel.length();j++) {
                spellOfLevel = spellsOfLevel.getJSONObject(j);
                if (haveSpell(spellsClass, spellOfLevel.getString("index"))){
                    Spell spellSave = new Spell(spellOfLevel.getString("index"), spellOfLevel.getString("name"));
                    spellsName.get(Integer.toString(i)).add(spellSave);
                }
            }
        }
    }

    private boolean haveSpell(JSONArray spellsClass, String index) throws JSONException {
        for(int i = 0;i<spellsClass.length();i++){
            if(spellsClass.getJSONObject(i).getString("index").equals(index)){
                return true;
            }
        }
        return false;
    }


    protected Spells(Parcel in) {
        cantrips = in.readInt();
        spells_known = in.readInt();
        spells = new HashMap<>();
        in.readMap(spells, String.class.getClassLoader());

        int spellsNameSize = in.readInt();
        spellsName = new HashMap<>();
        for (int i = 0; i < spellsNameSize; i++) {
            String key = in.readString();
            List<Spell> value = new ArrayList<>();
            in.readTypedList(value, Spell.CREATOR);
            spellsName.put(key, value);
        }

        int spellsDamageSize = in.readInt();
        spellsDamage = new HashMap<>();
        for (int i = 0; i < spellsDamageSize; i++) {
            String key = in.readString();
            List<Spell> value = new ArrayList<>();
            in.readTypedList(value, Spell.CREATOR);
            spellsDamage.put(key, value);
        }
    }

    public static final Creator<Spells> CREATOR = new Creator<Spells>() {
        @Override
        public Spells createFromParcel(Parcel in) {
            return new Spells(in);
        }

        @Override
        public Spells[] newArray(int size) {
            return new Spells[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(cantrips);
        parcel.writeInt(spells_known);
        parcel.writeMap(spells);
        parcel.writeInt(spellsName.size());
        for (Map.Entry<String, List<Spell>> entry : spellsName.entrySet()) {
            parcel.writeString(entry.getKey());
            parcel.writeTypedList(entry.getValue());
        }

        parcel.writeInt(spellsDamage.size());
        for (Map.Entry<String, List<Spell>> entry : spellsDamage.entrySet()) {
            parcel.writeString(entry.getKey());
            parcel.writeTypedList(entry.getValue());
        }
    }


    public int getCantrips() {
        return cantrips;
    }

    public void setCantrips(int cantrips) {
        this.cantrips = cantrips;
    }

    public int getSpells_known() {
        return spells_known;
    }

    public void setSpells_known(int spells_known) {
        this.spells_known = spells_known;
    }

    public Map<String, Integer> getSpells() {
        return spells;
    }

    public void setSpells(Map<String, Integer> spells) {
        this.spells = spells;
    }

    public Map<String, List<Spell>> getSpellsName() {
        return spellsName;
    }

    public void setSpellsName(Map<String, List<Spell>> spellsName) {
        this.spellsName = spellsName;
    }

    public Map<String, List<Spell>> getSpellsDamage() {
        return spellsDamage;
    }

    public void setSpellsDamage(Map<String, List<Spell>> spellsDamage) {
        this.spellsDamage = spellsDamage;
    }

    public static class Spell implements Parcelable{
        private String code;
        private String name;
        private String desc;
        private int hasDamage;
        private int slotCharacter;
        private Map<String,String>slot_level = new HashMap<>();

        public Spell(){

        }
        public Spell(String code,String name){
            this.code = code;
            this.name = name;
        }

        protected Spell(Parcel in) {
            code = in.readString();
            name = in.readString();
            desc = in.readString();
            hasDamage = in.readInt();
            slot_level = new HashMap<>();
            in.readMap(slot_level, String.class.getClassLoader());
            slotCharacter = in.readInt();
        }

        public static final Creator<Spell> CREATOR = new Creator<Spell>() {
            @Override
            public Spell createFromParcel(Parcel in) {
                return new Spell(in);
            }

            @Override
            public Spell[] newArray(int size) {
                return new Spell[size];
            }
        };

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Map<String, String> getSlot_level() {
            return slot_level;
        }
        public String getSlot_exactLevel(int level) {
            String dice = slot_level.get(Integer.toString(level));
            System.out.println("dice in spell " + dice + "sp " + getName());
            if(dice == null){
                for (int i=level;i>0;i--){
                    if( slot_level.get(Integer.toString(i)) != null){
                        dice =slot_level.get(Integer.toString(i));
                        return dice;
                    }
                }
            }

            return dice;
        }

        public void setSlot_level(Map<String, String> slot_level) {
            this.slot_level = slot_level;
        }

        public int getHasDamage() {
            return hasDamage;
        }

        public void setHasDamage(int hasDamage) {
            this.hasDamage = hasDamage;
        }

        public int getSlotCharacter() {
            return slotCharacter;
        }

        public void setSlotCharacter(int slotCharacter) {
            this.slotCharacter = slotCharacter;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(code);
            parcel.writeString(name);
            parcel.writeString(desc);
            parcel.writeInt(hasDamage);
            parcel.writeMap(slot_level);
            parcel.writeInt(slotCharacter);
        }
    }
}

