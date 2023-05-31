package com.claudiomaiorana.tfg_dnd.usecases.master.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.android.volley.VolleyError;
import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Armor;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.model.Enemy;
import com.claudiomaiorana.tfg_dnd.model.EnemySelector;
import com.claudiomaiorana.tfg_dnd.model.Item;
import com.claudiomaiorana.tfg_dnd.model.OptionsCharacter;
import com.claudiomaiorana.tfg_dnd.model.Party;
import com.claudiomaiorana.tfg_dnd.model.ProfLang;
import com.claudiomaiorana.tfg_dnd.model.Shield;
import com.claudiomaiorana.tfg_dnd.model.Usable;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.model.Weapons;
import com.claudiomaiorana.tfg_dnd.usecases.master.MasterManagerActivity;
import com.claudiomaiorana.tfg_dnd.usecases.master.adapters.AdapterAttacksEnemy;
import com.claudiomaiorana.tfg_dnd.usecases.master.adapters.AdapterEnemies;
import com.claudiomaiorana.tfg_dnd.usecases.master.adapters.AdapterEnemiesSelect;
import com.claudiomaiorana.tfg_dnd.usecases.master.adapters.AdapterFeaturesEnemy;
import com.claudiomaiorana.tfg_dnd.usecases.master.adapters.AdapterItemsSelect;
import com.claudiomaiorana.tfg_dnd.usecases.master.adapters.AdapterObjects;
import com.claudiomaiorana.tfg_dnd.usecases.master.adapters.AdapterPlayers;
import com.claudiomaiorana.tfg_dnd.util.ApiCallback;
import com.claudiomaiorana.tfg_dnd.util.PopUpCustom;
import com.claudiomaiorana.tfg_dnd.util.Util;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MasterGameplayFragment extends Fragment implements AdapterPlayers.OnItemClickListener, AdapterEnemies.OnItemClickListener, AdapterObjects.OnItemClickListener{

    private static final String ARG_PARAM1 = "param1";

    private RecyclerView rv_playersDisplay,rv_objectsDisplay,rv_enemiesDisplay;
    private Button btn_closeParty,btn_switchBattle;


    private String partyCode;
    Party party;

    AdapterPlayers adapterPlayers;
    AdapterObjects adapterObjects;
    AdapterEnemies adapterEnemies;

    ArrayList<Enemy> enemies;
    ArrayList<Item> items;


    private FirebaseFirestore db;
    private ListenerRegistration listenerRegistration;

    private Button btn_giveMoney,btn_giveLife,btn_giveItem,btn_increaseLevel,btn_takeMoney,btn_takeLife,btn_takeItem;
    String optionGet="";
    String optionType="";
    String optionTypeName="";


    String txt_nameEnemy = "",txt_hitDiceEnemy= "",txt_armorClassEnemy= "",txt_maxHitPoints= "",txt_speed= "";

    ArrayList<ProfLang> features;
    ArrayList<Weapons> attacks;

    ArrayList<JSONArray> allSpellsByLevel = null;
    JSONArray tmpArray = null;

    public MasterGameplayFragment() {}

    public static MasterGameplayFragment newInstance(String param1) {
        MasterGameplayFragment fragment = new MasterGameplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            partyCode = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_master_gameplay, container, false);
        db = FirebaseFirestore.getInstance();
        setElements(v);
        getParty();

        RecyclerView.LayoutManager layoutManagerP = new GridLayoutManager(getContext(),1);
        rv_playersDisplay.setLayoutManager(layoutManagerP);

        RecyclerView.LayoutManager layoutManagerE = new GridLayoutManager(getContext(),1);
        rv_enemiesDisplay.setLayoutManager(layoutManagerE);

        RecyclerView.LayoutManager layoutManagerO = new GridLayoutManager(getContext(),1);
        rv_objectsDisplay.setLayoutManager(layoutManagerO);

        features = new ArrayList<>();
        attacks = new ArrayList<>();


        btn_switchBattle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFight();
            }
        });

        btn_closeParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        listenerRegistration = db.collection("parties")
                .document(partyCode)
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        return;
                    }
                    party = documentSnapshot.toObject(Party.class);
                    getItems();
                });
    }

    private void getParty() {
        db.collection("parties").document(partyCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    party = task.getResult().toObject(Party.class);
                    getItems();
                }
            }
        });
    }


    private void getItems() {
        items =  new ArrayList<>();
        items.add(new Usable());

        try{
            db.collection("items").document(User.getInstance().getId()).collection(User.getInstance().getUserName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document: task.getResult()) {
                            switch (document.get("type",String.class)){
                                case "usables":
                                    items.add(document.toObject(Usable.class));
                                    break;
                                case "weapons":
                                    items.add(document.toObject(Weapons.class));
                                    break;
                                case "shields":
                                    items.add(document.toObject(Shield.class));
                                    break;
                                case "armors":
                                    items.add(document.toObject(Armor.class));
                                    break;
                            }
                        }
                    }

                    getEnemies();
                }
            });
        }catch (Exception e){
            getEnemies();
        }

    }

    private void getEnemies() {
        enemies = new ArrayList<>();
        enemies.add(new Enemy());
        try{
            db.collection("enemies").document(User.getInstance().getId()).collection(User.getInstance().getUserName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        enemies.add(document.toObject(Enemy.class));
                    }
                }
                setAdapters();
            }
        });

        }catch (Exception e){
            setAdapters();
        }

    }

    private void updateParty(){
        db.collection("parties").document(partyCode).set(party).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setAdapters();
            }
        });
    }


    void setElements(View v){
        rv_playersDisplay = v.findViewById(R.id.rv_playersDisplay);
        rv_objectsDisplay = v.findViewById(R.id.rv_itemsDisplay);
        rv_enemiesDisplay = v.findViewById(R.id.rv_enemiesDisplay);

        btn_closeParty = v.findViewById(R.id.btn_closeParty);
        btn_switchBattle = v.findViewById(R.id.btn_switchBattle);

    }

    private void setAdapters(){
        adapterPlayers = new AdapterPlayers(party.getPlayers(),getActivity(),this);
        adapterEnemies = new AdapterEnemies(enemies,getActivity(),this);
        adapterObjects = new AdapterObjects(items,getActivity(),this);

        rv_playersDisplay.setAdapter(adapterPlayers);
        rv_enemiesDisplay.setAdapter(adapterEnemies);
        rv_objectsDisplay.setAdapter(adapterObjects);
    }


    @Override
    public void onItemClick(Character character) {
        View v = getLayoutInflater().inflate(R.layout.popup_master_options_do_player, null);
        btn_giveMoney = v.findViewById(R.id.btn_giveMoney);
        btn_giveLife = v.findViewById(R.id.btn_giveLife);
        btn_giveItem = v.findViewById(R.id.btn_giveItem);
        btn_increaseLevel = v.findViewById(R.id.btn_increaseLevel);
        btn_takeMoney = v.findViewById(R.id.btn_takeMoney);
        btn_takeLife = v.findViewById(R.id.btn_takeLife);
        btn_takeItem = v.findViewById(R.id.btn_takeItem);


        PopUpCustom popUp = new PopUpCustom(v);
        popUp.show(getParentFragmentManager(), "optionsCharacter");


        btn_giveMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMoney(character,true);

            }
        });
        btn_giveLife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLife(character,true);

            }
        });
        btn_giveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setItem(character,true);
            }
        });
        btn_increaseLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                character.setLevel(character.getLevel()+1);
                updateLevel(character);
                popUp.dismiss();
            }
        });
        btn_takeMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMoney(character,false);

            }
        });
        btn_takeLife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLife(character,false);
            }
        });
        btn_takeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setItem(character,false);
            }
        });
    }

    private void updateLevel(Character character) {

        Util.apiGETRequest("classes/"+character.getCodeClass() + "/levels/"+character.getLevel(), new ApiCallback() {
            @Override
            public void onSuccess(JSONObject levelUpdate) {
                try{
                    if(levelUpdate.has("features")){
                        ArrayList<ProfLang> tmpFeatures = character.getFeaturesAndTraits();
                        JSONArray featuresLevel = levelUpdate.getJSONArray("features");
                        for(int i = 0;i<featuresLevel.length();i++){
                            JSONObject tmp = featuresLevel.getJSONObject(i);
                            tmpFeatures.add(new ProfLang(tmp.getString("index"),tmp.getString("name")));
                        }
                        character.setFeaturesAndTraits(tmpFeatures);
                    }

                    if(levelUpdate.has("spellcasting")){
                        JSONObject spellCasting = levelUpdate.getJSONObject("spellcasting");

                        for (int i = 0; i < 10; i++) {
                            if (i == 0) {
                                if (spellCasting.has("cantrips_known")) {
                                    character.getSpells().setCantrips(spellCasting.getInt("cantrips_known"));
                                    if(spellCasting.has("spells_known")){
                                        character.getSpells().setSpells_known(spellCasting.getInt("spells_known"));
                                    }else{
                                        character.getSpells().setSpells_known(0);
                                    }
                                } else {
                                    character.getSpells().setCantrips(0);
                                    character.getSpells().setSpells_known(0);
                                }
                            }else{
                                if(spellCasting.has("spell_slots_level_" + i)){
                                    character.getSpells().getSpells().put(Integer.toString(i),spellCasting.getInt("spell_slots_level_" + i));
                                }else{
                                    character.getSpells().getSpells().put(Integer.toString(i),0);

                                }
                            }
                        }
                    }

                    Util.apiGETRequest("classes/"+character.getCodeClass()+"/spells", new ApiCallback() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            try {
                                tmpArray=  jsonObject.getJSONArray("results");
                                addAllSpells(character.getLevel(),character.getLevel(),character);
                            }catch (Exception e){

                            }
                        }

                        @Override
                        public void onError(VolleyError error) {

                        }
                    },getActivity());

                }catch (Exception e){

                }
            }
            @Override
            public void onError(VolleyError error) {

            }
        },getActivity());


    }


    private void addAllSpells(int level,int finalLevel,Character character){
        Util.apiGETRequest("spells?level=" + level, new ApiCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try{
                    allSpellsByLevel.add(jsonObject.getJSONArray("results"));

                    if(level == finalLevel){

                        character.getSpells().addSpellsName(allSpellsByLevel,tmpArray);
                        updateCharacter(character);
                    }else{
                        int tmpLevel = level + 1;
                        addAllSpells(tmpLevel,finalLevel,character);
                    }
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onError(VolleyError error) {

            }
        },getActivity());
    }

    void setLife(Character character, boolean add){
        TextView txt_actualStat;
        Spinner sp_typeMoney;
        EditText txt_numberToAdd;
        Button btn_cancel,btn_accept;

        View v = getLayoutInflater().inflate(R.layout.popup_master_set_life_money, null);
        txt_actualStat = v.findViewById(R.id.txt_actualStat);
        sp_typeMoney = v.findViewById(R.id.sp_typeMoney);
        txt_numberToAdd = v.findViewById(R.id.txt_numberToAdd);
        btn_cancel = v.findViewById(R.id.btn_cancel);
        btn_accept = v.findViewById(R.id.btn_accept);


        sp_typeMoney.setVisibility(View.INVISIBLE);
        if(add){
            txt_actualStat.setText(getResources().getText(R.string.giveHealthToPlayer).toString().replace("@mon3y@",Integer.toString(character.getCurrentHitPoints())));
        }else{
            txt_actualStat.setText(getResources().getText(R.string.takeHealthToPlayer).toString().replace("@mon3y@",Integer.toString(character.getCurrentHitPoints())));
        }

        PopUpCustom popUp = new PopUpCustom(v);
        popUp.show(getParentFragmentManager(), "optionsLife");
        popUp.setCancelable(false);



        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lifeNumber = Integer.parseInt(txt_numberToAdd.getText().toString());
                if(lifeNumber<1){
                    txt_numberToAdd.setError(getResources().getString(R.string.ErrorValueToPlayer));
                }else{
                    character.setCurrentHitPoints(add ? character.getCurrentHitPoints() + lifeNumber :  character.getCurrentHitPoints() - lifeNumber );
                    updateCharacter(character);
                    popUp.dismiss();
                }

            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
            }
        });

    }

    void setMoney(Character character,boolean add){
        TextView txt_actualStat;
        Spinner sp_typeMoney;
        EditText txt_numberToAdd;
        Button btn_cancel,btn_accept;


        View v = getLayoutInflater().inflate(R.layout.popup_master_set_life_money, null);
        txt_actualStat = v.findViewById(R.id.txt_actualStat);
        sp_typeMoney = v.findViewById(R.id.sp_typeMoney);
        txt_numberToAdd = v.findViewById(R.id.txt_numberToAdd);
        btn_cancel = v.findViewById(R.id.btn_cancel);
        btn_accept = v.findViewById(R.id.btn_accept);

        ArrayList<String> arrayList = new ArrayList<>();
        for (String coin:getResources().getStringArray(R.array.moneyType)) {
            arrayList.add(coin);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_typeMoney.setAdapter(arrayAdapter);
        sp_typeMoney.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).toString().equals(optionGet.equals(getResources().getStringArray(R.array.moneyType)[0]))){
                    optionGet="";
                }else{
                    optionGet = parent.getItemAtPosition(position).toString();
                }

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        String actualMoney = "pp:" + character.getMoneyPlatinum() + ", gp:"+ character.getMoneyGold() + ", sp:"+ character.getMoneySilver() + ", cp:"+character.getMoneyCopper();
        if(add){
            txt_actualStat.setText(getResources().getText(R.string.giveMoneyToPlayer).toString().replace("@mon3y@",actualMoney));
        }else{
            txt_actualStat.setText(getResources().getText(R.string.takeMoneyFromPlayer).toString().replace("@mon3y@",actualMoney));

        }


        PopUpCustom popUp = new PopUpCustom(v);
        popUp.show(getParentFragmentManager(), "optionsMoney");
        popUp.setCancelable(false);




        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(txt_numberToAdd.getText().toString()) <1){
                    txt_numberToAdd.setError(getResources().getString(R.string.ErrorValueToPlayer));
                }else{
                    if(optionGet.equals("")){
                        btn_accept.setError(getResources().getString(R.string.errorNotypeCoin));
                    }else{
                        if(add){
                            switch (optionGet){
                                case "pp":
                                    character.addMoney(0,0,0,Integer.parseInt(txt_numberToAdd.getText().toString()));
                                    break;
                                case "gp":
                                    character.addMoney(0,0,Integer.parseInt(txt_numberToAdd.getText().toString()),0);
                                    break;
                                case "sp":
                                    character.addMoney(0,Integer.parseInt(txt_numberToAdd.getText().toString()),0,0);
                                    break;
                                case "cp":
                                    character.addMoney(Integer.parseInt(txt_numberToAdd.getText().toString()),0,0,0);
                                    break;
                            }
                        }else{
                            switch (optionGet){
                                case "pp":
                                    character.subtractMoney(0,0,0,Integer.parseInt(txt_numberToAdd.getText().toString()));
                                    break;
                                case "gp":
                                    character.subtractMoney(0,0,Integer.parseInt(txt_numberToAdd.getText().toString()),0);
                                    break;
                                case "sp":
                                    character.subtractMoney(0,Integer.parseInt(txt_numberToAdd.getText().toString()),0,0);
                                    break;
                                case "cp":
                                    character.subtractMoney(Integer.parseInt(txt_numberToAdd.getText().toString()),0,0,0);
                                    break;
                            }
                        }
                        updateCharacter(character);
                        popUp.dismiss();
                    }
                }

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
            }
        });
    }

    void setItem(Character character,boolean add){
        RecyclerView rv;
        Button btn_cancel,btn_accept;
        TextView txt_infoItems;
        ArrayList<OptionsCharacter> data;
        AdapterItemsSelect adapter;

        View v = getLayoutInflater().inflate(R.layout.popup_master_items_select, null);
        rv = v.findViewById(R.id.rv_itemsSelectorMater);
        txt_infoItems = v.findViewById(R.id.txv_itemsSelectInfo);
        btn_cancel = v.findViewById(R.id.btn_cancelItems);
        btn_accept = v.findViewById(R.id.btn_acceptItems);

        txt_infoItems.setText(
                add ? getResources().getText(R.string.itemToPlayer) :
                        getResources().getText(R.string.itemFromPlayer)
        );

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        rv.setLayoutManager(layoutManager);
        data = new ArrayList<>();
        data = returnListItems(add ? items : character.getItems());

        adapter = new AdapterItemsSelect(data,getActivity());
        rv.setAdapter(adapter);

        PopUpCustom popUp = new PopUpCustom(v);
        popUp.show(getParentFragmentManager(), "Items");
        popUp.setCancelable(false);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_accept.setError(null);
                ArrayList<OptionsCharacter> tmpData = adapter.getData();
                int quantity = 0;
                for (OptionsCharacter sk: tmpData) {
                    if(sk.isSelected()){
                        quantity ++;
                    }
                }
                if(quantity<1){
                    btn_accept.setError("Select at least 1");
                }else{
                    updateItems(character,tmpData,add);
                    popUp.dismiss();
                }
            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
            }
        });

    }

    private void updateItems(Character character,ArrayList<OptionsCharacter> tmpData, boolean add) {
        ArrayList<Item> characterItems = character.getItems();
        if(add){
            for (OptionsCharacter item: tmpData) {
                for (Item itemMaster: items) {
                    if(item.getCode().equals(itemMaster.getCode())){
                        characterItems.add(itemMaster);
                        break;
                    }
                }
            }
        }else{
            for (OptionsCharacter item: tmpData) {
                for (Item itemCharacter: characterItems) {
                    if(item.getCode().equals(itemCharacter.getCode())){
                        characterItems.remove(itemCharacter);
                        break;
                    }
                }
            }
        }
        character.setItems(characterItems);
        updateCharacter(character);
    }

    private ArrayList<OptionsCharacter> returnListItems(ArrayList<Item> itemsList) {
        ArrayList<OptionsCharacter> result = new ArrayList<>();
        for (Item item: itemsList) {
            result.add(new OptionsCharacter(item.getCode(),item.getName()));
        }
        return result;
    }


    private void updateCharacter(Character character) {
        for(int i=0;i<party.getPlayers().size();i++){
            if(party.getPlayers().get(i).getID().equals(character.getID())){
                party.getPlayers().set(i,character);
                break;
            }
        }
        updateParty();
    }

    @Override
    public void newEnemy() {
        EditText edt_nameEnemy,edt_hitDiceEnemy,edt_armorClassEnemy,edt_maxHitPoints,edt_speed;
        Button btn_addAttack,btn_newFeature,btn_createEnemy;
        View v = getLayoutInflater().inflate(R.layout.popup_master_create_enemy, null);

        edt_nameEnemy = v.findViewById(R.id.edt_nameEnemy);
        edt_hitDiceEnemy = v.findViewById(R.id.edt_hitDiceEnemy);
        edt_armorClassEnemy = v.findViewById(R.id.edt_armorClassEnemy);
        edt_maxHitPoints = v.findViewById(R.id.edt_maxHitPoints);
        edt_speed = v.findViewById(R.id.edt_speed);


        btn_addAttack = v.findViewById(R.id.btn_addAttack);
        btn_newFeature = v.findViewById(R.id.btn_newfeature);
        btn_createEnemy = v.findViewById(R.id.btn_createEnemy);


        edt_nameEnemy.setText(txt_nameEnemy);
        edt_hitDiceEnemy.setText(txt_hitDiceEnemy);
        edt_armorClassEnemy.setText(txt_armorClassEnemy);
        edt_maxHitPoints.setText(txt_maxHitPoints);
        edt_speed.setText(txt_speed);


        PopUpCustom popUp = new PopUpCustom(v);
        popUp.show(getParentFragmentManager(), "createEnemy");
        popUp.setCancelable(false);



        btn_addAttack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_nameEnemy = edt_nameEnemy.getText().toString();
                txt_hitDiceEnemy = edt_hitDiceEnemy.getText().toString();
                txt_armorClassEnemy = edt_armorClassEnemy.getText().toString();
                txt_maxHitPoints = edt_maxHitPoints.getText().toString();
                txt_speed = edt_speed.getText().toString();
                popUp.dismiss();
                popUpAttack();
            }
        });
        btn_newFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_nameEnemy = edt_nameEnemy.getText().toString();
                txt_hitDiceEnemy = edt_hitDiceEnemy.getText().toString();
                txt_armorClassEnemy = edt_armorClassEnemy.getText().toString();
                txt_maxHitPoints = edt_maxHitPoints.getText().toString();
                txt_speed = edt_speed.getText().toString();
                popUp.dismiss();
                popUpFeature();
            }
        });
        btn_createEnemy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_nameEnemy = edt_nameEnemy.getText().toString();
                txt_hitDiceEnemy = edt_hitDiceEnemy.getText().toString();
                txt_armorClassEnemy = edt_armorClassEnemy.getText().toString();
                txt_maxHitPoints = edt_maxHitPoints.getText().toString();
                txt_speed = edt_speed.getText().toString();
                if(txt_nameEnemy.equals("")){
                    edt_nameEnemy.setError("");
                } else if (txt_hitDiceEnemy.equals("")) {
                    edt_hitDiceEnemy.setError("");
                }else if (txt_armorClassEnemy.equals("")) {
                    edt_armorClassEnemy.setError("");
                }else if (txt_maxHitPoints.equals("")) {
                    edt_maxHitPoints.setError("");
                }else if (txt_speed.equals("")) {
                    edt_speed.setError("");
                }else{
                    Enemy enemy = new Enemy(txt_nameEnemy+User.getInstance().getId(),txt_nameEnemy,
                            txt_hitDiceEnemy,Integer.parseInt(txt_maxHitPoints),Integer.parseInt(txt_armorClassEnemy),
                            Integer.parseInt(txt_speed),features,attacks);
                    enemies.add(enemy);
                    setAdapters();
                    popUp.dismiss();
                }
            }
        });
    }


    void popUpAttack(){
        Button createButton,cancelButton;
        EditText nameAttack, hitDice;
        CheckBox isMelee;
        View v = getLayoutInflater().inflate(R.layout.popup_master_create_attack_enemy, null);

        createButton = v.findViewById(R.id.btn_createAttack);
        cancelButton = v.findViewById(R.id.btn_cancelAttack);
        nameAttack = v.findViewById(R.id.edt_nameAttack);
        hitDice = v.findViewById(R.id.edt_hitDiceAttack);
        isMelee = v.findViewById(R.id.chk_isMelee);

        PopUpCustom popUp = new PopUpCustom(v);
        popUp.show(getParentFragmentManager(), "createAttack");
        popUp.setCancelable(false);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameAttack.getText().toString().equals("")){
                    nameAttack.setError("");
                }else{
                    String patron = "^(\\d+)d(\\d+)$";

                    Pattern pattern = Pattern.compile(patron);
                    Matcher matcher = pattern.matcher(hitDice.getText().toString());
                    if (matcher.matches()) {

                        int firstNum = Integer.parseInt(matcher.group(1));
                        int secondNum = Integer.parseInt(matcher.group(2));

                        if (!(secondNum == 4 ||secondNum == 6 ||secondNum == 8 ||secondNum == 10 ||secondNum == 12 ||secondNum == 20)) {
                            hitDice.setError("");
                        }else{
                            if(0>=firstNum){
                                hitDice.setError("");
                            }else{
                                Weapons weapons = new Weapons(nameAttack.getText().toString(),nameAttack.getText().toString(),hitDice.getText().toString(), isMelee.isChecked());
                                attacks.add(weapons);
                                newEnemy();
                                popUp.dismiss();
                            }
                        }
                    }else{
                        hitDice.setError("");
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newEnemy();
                popUp.dismiss();
            }
        });

    }

    void popUpFeature(){
        Button createButton,cancelButton;
        EditText nameAttack;
        View v = getLayoutInflater().inflate(R.layout.popup_master_create_feature_enemy, null);

        createButton = v.findViewById(R.id.btn_createFeature);
        cancelButton = v.findViewById(R.id.btn_cancelFeature);
        nameAttack = v.findViewById(R.id.edt_nameFeature);

        PopUpCustom popUp = new PopUpCustom(v);
        popUp.show(getParentFragmentManager(), "createFeature");
        popUp.setCancelable(false);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameAttack.getText().toString().equals("")){
                    nameAttack.setError("");
                }else{
                    ProfLang profLang = new ProfLang(nameAttack.getText().toString(),nameAttack.getText().toString());
                    features.add(profLang);
                    newEnemy();
                    popUp.dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newEnemy();
                popUp.dismiss();
            }
        });

    }


    @Override
    public void onItemClick(Enemy enemy) {
        TextView edt_nameEnemy,edt_hitDiceEnemy,edt_armorClassEnemy,edt_maxHitPoints,edt_speed;
        Button btn_okayEnemy;
        RecyclerView rvAttacks,rvFeatures;
        AdapterFeaturesEnemy adapterF;
        AdapterAttacksEnemy adapterA;
        View v = getLayoutInflater().inflate(R.layout.popup_master_show_enemy, null);

        edt_nameEnemy = v.findViewById(R.id.edt_nameEnemy);
        edt_hitDiceEnemy = v.findViewById(R.id.edt_hitDiceEnemy);
        edt_armorClassEnemy = v.findViewById(R.id.edt_armorClassEnemy);
        edt_maxHitPoints = v.findViewById(R.id.edt_maxHitPoints);
        edt_speed = v.findViewById(R.id.edt_speed);

        btn_okayEnemy = v.findViewById(R.id.btn_okayEnemy);
        rvAttacks = v.findViewById(R.id.rv_attacks);
        rvFeatures = v.findViewById(R.id.rv_features);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        rvAttacks.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager layoutManagerF = new GridLayoutManager(getContext(),2);
        rvFeatures.setLayoutManager(layoutManagerF);

        adapterF = new AdapterFeaturesEnemy(enemy.getFeaturesAndTraits(),getActivity());
        rvFeatures.setAdapter(adapterF);

        adapterA = new AdapterAttacksEnemy(enemy.getAttacks(),getActivity());
        rvFeatures.setAdapter(adapterA);

        edt_nameEnemy.setText(txt_nameEnemy);
        edt_hitDiceEnemy.setText(txt_hitDiceEnemy);
        edt_armorClassEnemy.setText(txt_armorClassEnemy);
        edt_maxHitPoints.setText(txt_maxHitPoints);
        edt_speed.setText(txt_speed);


        PopUpCustom popUp = new PopUpCustom(v);
        popUp.show(getParentFragmentManager(), "showEnemy");
        popUp.setCancelable(false);



        btn_okayEnemy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
            }
        });
    }

    @Override
    public void newObject() {
        Spinner sp_typeItem;
        Button btn_cancel,btn_accept;

        View v = getLayoutInflater().inflate(R.layout.popup_master_create_object_type, null);
        sp_typeItem = v.findViewById(R.id.sp_typeSelect);
        btn_cancel = v.findViewById(R.id.btn_cancelItemsType);
        btn_accept = v.findViewById(R.id.btn_acceptItemsType);

        ArrayList<String> arrayList = new ArrayList<>();
        for (String coin:getResources().getStringArray(R.array.itemTypeName)) {
            arrayList.add(coin);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_typeItem.setAdapter(arrayAdapter);
        sp_typeItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).toString().equals(optionGet.equals(getResources().getStringArray(R.array.itemTypeName)[0]))){
                    optionType="";
                    optionTypeName="";
                }else{
                    //no se si funcionara
                    optionType = getResources().getStringArray(R.array.itemType)[position];
                    optionTypeName = parent.getItemAtPosition(position).toString();

                }

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        PopUpCustom popUp = new PopUpCustom(v);
        popUp.show(getParentFragmentManager(), "optionsTypeSelect");
        popUp.setCancelable(false);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(optionType.equals("")){
                    btn_accept.setError(getResources().getString(R.string.errorNotypeItem));
                }else{
                    callPopUpItemInfo(optionType);
                    popUp.dismiss();
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
            }
        });
    }

    @Override
    public void onItemClick(Weapons weapons) {
        TextView txt_nameItemMaster,txt_elementAdd,txt_maxBonusArmor, etx_setNameItemMaster,edt_elementAddForType,edt_maxBonus;

        Button btn_okay;

        View view = getLayoutInflater().inflate(R.layout.popup_master_show_object_info, null);


        txt_nameItemMaster = view.findViewById(R.id.txt_nameItemMasterInfoTittle);
        etx_setNameItemMaster = view.findViewById(R.id.txt_nameItemMasterInfo);
        txt_elementAdd = view.findViewById(R.id.txt_elementAddTittle);
        edt_elementAddForType = view.findViewById(R.id.txt_elementAdd);
        txt_maxBonusArmor = view.findViewById(R.id.txt_maxBonusArmorTittle);
        edt_maxBonus = view.findViewById(R.id.txt_maxBonusArmor);
        btn_okay = view.findViewById(R.id.btn_okay);

        txt_nameItemMaster.setText(getResources().getText(R.string.nameItem));
        etx_setNameItemMaster.setText(weapons.getName());

        edt_maxBonus.setVisibility(View.INVISIBLE);
        txt_maxBonusArmor.setVisibility(View.INVISIBLE);
        edt_elementAddForType.setText(weapons.getDamageDice());
        txt_elementAdd.setText(getResources().getText(R.string.elementWeaponItem));



        PopUpCustom popUp = new PopUpCustom(view);
        popUp.show(getParentFragmentManager(), "showWeapon");
        popUp.setCancelable(false);


        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
            }
        });

    }

    @Override
    public void onItemClick(Usable usable) {

        TextView txt_nameItemMaster,txt_elementAdd,txt_maxBonusArmor, etx_setNameItemMaster,edt_elementAddForType,edt_maxBonus;

        Button btn_okay;

        View view = getLayoutInflater().inflate(R.layout.popup_master_show_object_info, null);


        txt_nameItemMaster = view.findViewById(R.id.txt_nameItemMasterInfoTittle);
        etx_setNameItemMaster = view.findViewById(R.id.txt_nameItemMasterInfo);
        txt_elementAdd = view.findViewById(R.id.txt_elementAddTittle);
        edt_elementAddForType = view.findViewById(R.id.txt_elementAdd);
        txt_maxBonusArmor = view.findViewById(R.id.txt_maxBonusArmorTittle);
        edt_maxBonus = view.findViewById(R.id.txt_maxBonusArmor);
        btn_okay = view.findViewById(R.id.btn_okay);

        txt_nameItemMaster.setText(getResources().getText(R.string.nameItem));
        etx_setNameItemMaster.setText(usable.getName());

        edt_maxBonus.setVisibility(View.INVISIBLE);
        txt_maxBonusArmor.setVisibility(View.INVISIBLE);
        txt_elementAdd.setText(getResources().getText(R.string.elementUsableItem));
        edt_elementAddForType.setText(usable.getDesc());



        PopUpCustom popUp = new PopUpCustom(view);
        popUp.show(getParentFragmentManager(), "showUsable");
        popUp.setCancelable(false);


        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
            }
        });



    }

    @Override
    public void onItemClick(Shield shield) {

        TextView txt_nameItemMaster,txt_elementAdd,txt_maxBonusArmor, etx_setNameItemMaster,edt_elementAddForType,edt_maxBonus;

        Button btn_okay;

        View view = getLayoutInflater().inflate(R.layout.popup_master_show_object_info, null);


        txt_nameItemMaster = view.findViewById(R.id.txt_nameItemMasterInfoTittle);
        etx_setNameItemMaster = view.findViewById(R.id.txt_nameItemMasterInfo);
        txt_elementAdd = view.findViewById(R.id.txt_elementAddTittle);
        edt_elementAddForType = view.findViewById(R.id.txt_elementAdd);
        txt_maxBonusArmor = view.findViewById(R.id.txt_maxBonusArmorTittle);
        edt_maxBonus = view.findViewById(R.id.txt_maxBonusArmor);
        btn_okay = view.findViewById(R.id.btn_okay);

        txt_nameItemMaster.setText(getResources().getText(R.string.nameItem));
        etx_setNameItemMaster.setText(shield.getName());

        edt_maxBonus.setVisibility(View.INVISIBLE);
        txt_maxBonusArmor.setVisibility(View.INVISIBLE);
        txt_elementAdd.setText(getResources().getText(R.string.elementShieldArmorItem));
        edt_elementAddForType.setText(shield.getArmorClass());


        PopUpCustom popUp = new PopUpCustom(view);
        popUp.show(getParentFragmentManager(), "showShield");
        popUp.setCancelable(false);


        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
            }
        });


    }

    @Override
    public void onItemClick(Armor armor) {
        TextView txt_nameItemMaster,txt_elementAdd,txt_maxBonusArmor, etx_setNameItemMaster,edt_elementAddForType,edt_maxBonus;

        Button btn_okay;

        View view = getLayoutInflater().inflate(R.layout.popup_master_show_object_info, null);


        txt_nameItemMaster = view.findViewById(R.id.txt_nameItemMasterInfoTittle);
        etx_setNameItemMaster = view.findViewById(R.id.txt_nameItemMasterInfo);
        txt_elementAdd = view.findViewById(R.id.txt_elementAddTittle);
        edt_elementAddForType = view.findViewById(R.id.txt_elementAdd);
        txt_maxBonusArmor = view.findViewById(R.id.txt_maxBonusArmorTittle);
        edt_maxBonus = view.findViewById(R.id.txt_maxBonusArmor);
        btn_okay = view.findViewById(R.id.btn_okay);

        txt_nameItemMaster.setText(getResources().getText(R.string.nameItem));
        etx_setNameItemMaster.setText(armor.getName());

        edt_elementAddForType.setVisibility(View.VISIBLE);
        txt_maxBonusArmor.setVisibility(View.VISIBLE);
        txt_elementAdd.setText(getResources().getText(R.string.elementShieldArmorItem));
        edt_elementAddForType.setText(armor.getBase());
        edt_maxBonus.setText(armor.getMaxBonus());

        PopUpCustom popUp = new PopUpCustom(view);
        popUp.show(getParentFragmentManager(), "showArmor");
        popUp.setCancelable(false);


        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
            }
        });


}

    private void callPopUpItemInfo(@NonNull String optionType) {
        TextView tittleType,txt_nameItemMaster,txt_elementAdd,txt_maxBonusArmor;
        EditText etx_setNameItemMaster,edt_elementAddForType,edt_maxBonus;
        CheckBox isMelee;
        Button btn_cancel,btn_accept;

        View view = getLayoutInflater().inflate(R.layout.popup_master_create_object_info, null);

        tittleType = view.findViewById(R.id.tittleType);
        txt_nameItemMaster = view.findViewById(R.id.txt_nameItemMaster);
        txt_elementAdd = view.findViewById(R.id.txt_elementAdd);
        txt_maxBonusArmor = view.findViewById(R.id.txt_maxBonusArmor);
        etx_setNameItemMaster = view.findViewById(R.id.etx_setNameItemMaster);
        edt_elementAddForType = view.findViewById(R.id.edt_elementAddForType);
        edt_maxBonus = view.findViewById(R.id.edt_maxBonus);
        btn_cancel = view.findViewById(R.id.btn_cancelItemsTypeInfo);
        btn_accept = view.findViewById(R.id.btn_acceptItemsTypeInfo);
        isMelee = view.findViewById(R.id.chk_isMelee);

        tittleType.setText(getResources().getText(R.string.typeElementTittle).toString().replace("@it3m@",optionTypeName));


        txt_nameItemMaster.setText(getResources().getText(R.string.nameItem));
//TODO:poner en gone los layouts y no los elementos
        switch (optionType){
            case "Weapon":
                edt_maxBonus.setVisibility(View.GONE);
                txt_maxBonusArmor.setVisibility(View.GONE);
                isMelee.setVisibility(View.VISIBLE);
                edt_elementAddForType.setHint(getResources().getString(R.string.hintForWeapon));
                txt_elementAdd.setText(getResources().getText(R.string.elementWeaponItem));
                break;
            case "Usable":
                edt_maxBonus.setVisibility(View.GONE);
                txt_maxBonusArmor.setVisibility(View.GONE);
                isMelee.setVisibility(View.GONE);

                edt_elementAddForType.setHint(getResources().getString(R.string.hintForUsable));
                txt_elementAdd.setText(getResources().getText(R.string.elementUsableItem));

                break;
            case "Shield":
                edt_maxBonus.setVisibility(View.GONE);
                txt_maxBonusArmor.setVisibility(View.GONE);
                isMelee.setVisibility(View.GONE);

                edt_elementAddForType.setHint(getResources().getString(R.string.hintForShieldArmor));
                txt_elementAdd.setText(getResources().getText(R.string.elementShieldArmorItem));

                break;
            case "Armor":
                edt_maxBonus.setVisibility(View.VISIBLE);
                txt_maxBonusArmor.setVisibility(View.VISIBLE);
                isMelee.setVisibility(View.INVISIBLE);

                edt_elementAddForType.setHint(getResources().getString(R.string.hintForShieldArmor));
                txt_elementAdd.setText(getResources().getText(R.string.elementShieldArmorItem));
                break;
        }


        PopUpCustom popUp = new PopUpCustom(view);
        popUp.show(getParentFragmentManager(), "createItem");
        popUp.setCancelable(false);


        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(etx_setNameItemMaster.getText().toString().equals("") || edt_elementAddForType.getText().toString().equals("")){
                    btn_accept.setError(getResources().getString(R.string.errorNoItemFill));
                }else if(optionType.equals("Armor")){
                    if(edt_maxBonus.getText().toString().equals("")){
                        btn_accept.setError(getResources().getString(R.string.errorNoItemFill));
                    }else{
                        switch (optionType){
                            case "Weapon":
                                Weapons weapon = new Weapons(txt_nameItemMaster.getText().toString(),txt_nameItemMaster.getText().toString(),edt_elementAddForType.getText().toString(), isMelee.isChecked());
                                items.add(weapon);
                                break;
                            case "Usable":
                                Usable usable = new Usable(txt_nameItemMaster.getText().toString(),txt_nameItemMaster.getText().toString(),edt_elementAddForType.getText().toString());
                                items.add(usable);
                                break;
                            case "Shield":
                                Shield shield = new Shield(txt_nameItemMaster.getText().toString(),txt_nameItemMaster.getText().toString(),edt_elementAddForType.getText().toString());
                                items.add(shield);
                                break;
                            case "Armor":
                                Armor armor = new Armor(txt_nameItemMaster.getText().toString(),txt_nameItemMaster.getText().toString(),edt_elementAddForType.getText().toString(),Integer.parseInt(edt_maxBonus.getText().toString()));
                                items.add(armor);
                                break;
                        }
                        saveItems();
                        setAdapters();
                        popUp.dismiss();
                    }
                }else{
                    switch (optionType){
                        case "Weapon":
                            Weapons weapon = new Weapons(txt_nameItemMaster.getText().toString(),txt_nameItemMaster.getText().toString(),edt_elementAddForType.getText().toString(), isMelee.isChecked());
                            items.add(weapon);
                            break;
                        case "Usable":
                            Usable usable = new Usable(txt_nameItemMaster.getText().toString(),txt_nameItemMaster.getText().toString(),edt_elementAddForType.getText().toString());
                            items.add(usable);
                            break;
                        case "Shield":
                            Shield shield = new Shield(txt_nameItemMaster.getText().toString(),txt_nameItemMaster.getText().toString(),edt_elementAddForType.getText().toString());
                            items.add(shield);
                            break;
                        case "Armor":
                            Armor armor = new Armor(txt_nameItemMaster.getText().toString(),txt_nameItemMaster.getText().toString(),edt_elementAddForType.getText().toString(),Integer.parseInt(edt_maxBonus.getText().toString()));
                            items.add(armor);
                            break;
                    }
                    saveItems();//TODO: guardar items y enemigso

                    setAdapters();
                    popUp.dismiss();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
            }
        });

    }

    private void saveItems() {
        db.collection("items").document(User.getInstance().getId()).collection(User.getInstance().getUserName()).document().set(items).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setAdapters();
            }
        });
    }

    public void startFight(){
        //TODO:Setear enemigos y pedir la inicativa de los enemigos pasar a modo pelea y que pida iniciativa a los jugdores
        party.setFighting(true);
        callPopUpEnemies();

    }

    void callPopUpEnemies(){
        Button selectEnemies;
        ArrayList<EnemySelector> enemiesFighting;
        RecyclerView rv;
        AdapterEnemiesSelect adapter;

        View view = getLayoutInflater().inflate(R.layout.fragment_character_skills, null);

        rv = view.findViewById(R.id.rv_skillsSelector);
        selectEnemies = view.findViewById(R.id.btn_continueSkills);
        enemiesFighting = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        rv.setLayoutManager(layoutManager);

        enemiesFighting = getEnemiesToSelect();

        adapter = new AdapterEnemiesSelect(enemiesFighting, getActivity());
        rv.setAdapter(adapter);

        //loadingBar(View.INVISIBLE);
        PopUpCustom popUp = new PopUpCustom(view);
        popUp.show(getParentFragmentManager(), "enemiesPopUp");
        popUp.setCancelable(false);

        selectEnemies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectEnemies.setError(null);
                ArrayList<EnemySelector> tmpData = adapter.getData();
                int quantity = 0;
                party.setEnemiesFight(new ArrayList<>());
                for (EnemySelector sk: tmpData) {
                    if(sk.getUserValue()>0){
                        quantity ++;
                        Enemy enemy = null;
                        for (Enemy enemyInList:enemies) {
                            if(enemyInList.getName().equals(sk.getName())){
                                enemy = enemyInList;
                            }
                        }
                        if(enemy!=null){
                            for(int i = 0;i<sk.getUserValue();i++){
                                party.getEnemiesFight().add(enemy);
                            }
                        }
                    }
                }
                if(quantity<1){
                    selectEnemies.setError("Add enemies");
                }else{
                    setInitiativeEnemies(0);
                }
            }
        });
    }



    private ArrayList<EnemySelector> getEnemiesToSelect() {
        ArrayList<EnemySelector> result = new ArrayList<>();

        for (Enemy enemyList: enemies) {
            result.add(new EnemySelector(enemyList.getName(),enemyList.getMaxHitPoints()));
        }
        return result;
    }


    private void setInitiativeEnemies(int index) {
        Button btn_Continue;
        TextView txt_text,txt_value;

        View view = getLayoutInflater().inflate(R.layout.fragment_character_stats, null);

        btn_Continue = view.findViewById(R.id.btn_continue);
        txt_text = view.findViewById(R.id.txt_statToThrow);
        txt_value = view.findViewById(R.id.txt_numberStat);

        PopUpCustom popUp = new PopUpCustom(view);
        popUp.show(getParentFragmentManager(), "initiativeEnemy");
        popUp.setCancelable(false);

        txt_text.setText(txt_text.getText().toString().replace("@sTat@",party.getEnemiesFight().get(index).getName()));

        btn_Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txt_value.getText().toString().equals("")){
                    int tmpValue = Integer.parseInt(txt_value.getText().toString());
                    if(0<tmpValue && tmpValue<21){
                        popUp.dismiss();
                        party.getEnemiesFight().get(index).setInitiative(tmpValue);
                        if(index == party.getEnemiesFight().size()){
                            ((MasterManagerActivity)getActivity()).goToPlay(party);
                        }else{
                            setInitiativeEnemies(index+1);
                        }
                    }else{
                        txt_value.setError(getResources().getString(R.string.errorStat));
                    }
                }else{
                    txt_value.setError(getResources().getString(R.string.errorStat));
                }
            }
        });
    }
}