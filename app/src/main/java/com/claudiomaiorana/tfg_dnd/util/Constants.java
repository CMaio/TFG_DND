package com.claudiomaiorana.tfg_dnd.util;

import com.claudiomaiorana.tfg_dnd.model.User;

public class Constants {

    //Alignment possible states
    static final String LG = "LAWFUL_GOOD";
    static final String LN = "LAWFUL_NEUTRAL";
    static final String LE = "LAWFUL_EVIL";
    static final String NG = "NEUTRAL_GOOD";
    static final String NN = "NEUTRAL";
    static final String NE = "NEUTRAL_EVIL";
    static final String CG = "CHAOTIC_GOOD";
    static final String CN = "CHAOTIC_NEUTRAL";
    static final String CE = "CHAOTIC_EVIL";

    //Stats to look using API
    static final String[] TYPE_OF_RACES = {"dragonborn","dwarf","elf","gnome","half-elf","half-orc","halfling","human","tiefling"};
    static final String[] TYPE_OF_CLASSES = {"barbarian","bard","cleric","fighter","paladin","ranger","rogue","sorcerer","wizard"};
    private static final String[] TYPE_OF_ALIGNMENT = {"lawful-good","lawful-neutral","lawful-evil","neutral-good","neutral","neutral-evil","chaotic-good","chaotic-neutral","chaotic-evil"};

    //Stats para las recycler views y los headers
    public static final int TYPE_CREATE = 0;
    public static final int TYPE_FILLED = 1;


}
