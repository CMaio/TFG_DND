package com.claudiomaiorana.tfg_dnd.usecases.game.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.claudiomaiorana.tfg_dnd.R;

import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.model.Item;
import com.claudiomaiorana.tfg_dnd.model.OptionsCharacter;
import com.claudiomaiorana.tfg_dnd.model.Party;

import com.claudiomaiorana.tfg_dnd.model.Spells;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterAbilities;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterSpellsQuantity;
import com.claudiomaiorana.tfg_dnd.usecases.game.GameplayActivity;
import com.claudiomaiorana.tfg_dnd.usecases.game.adapters.AdapterEquipment;
import com.claudiomaiorana.tfg_dnd.usecases.game.adapters.AdapterItemsGame;
import com.claudiomaiorana.tfg_dnd.usecases.game.adapters.AdapterSpellsGame;
import com.claudiomaiorana.tfg_dnd.util.PopUpCustom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameplaySafeFragment extends Fragment implements AdapterSpellsGame.OnItemClickListener{

    private static final String ARG_PARAM1 = "param1";

    private String partyCode;

    private RecyclerView rv_spells,rv_abilities,rv_objects,rv_spQuantity;
    private TextView rtv_name_game,rtv_pronoun_game,rtv_gender_game,rtv_level_game,rtv_race_game,rtv_class_game,txt_life_game;
    private ImageView img_character_game;
    private Button btn_sheathed_unsheathed,btn_goHome,btn_gold_game;

    Party party;
    Character character;

    private FirebaseFirestore db;
    FirebaseStorage dbStorage = FirebaseStorage.getInstance();
    private ListenerRegistration listenerRegistration;

    private AdapterSpellsQuantity adapterSpellsQ;
    private AdapterSpellsGame adapterSpells;
    private AdapterAbilities adapterAbilities;
    private AdapterItemsGame adapterItems;

    public GameplaySafeFragment() {}

    public static GameplaySafeFragment newInstance(String party) {
        GameplaySafeFragment fragment = new GameplaySafeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, party);
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
        View v = inflater.inflate(R.layout.fragment_gameplay_safe, container, false);
        db = FirebaseFirestore.getInstance();
        setElements(v);



        RecyclerView.LayoutManager layoutManagerQS = new GridLayoutManager(getContext(),4);
        rv_spQuantity.setLayoutManager(layoutManagerQS);

        RecyclerView.LayoutManager layoutManagerS = new GridLayoutManager(getContext(),2);
        rv_spells.setLayoutManager(layoutManagerS);

        RecyclerView.LayoutManager layoutManagerAb = new GridLayoutManager(getContext(),2);
        rv_abilities.setLayoutManager(layoutManagerAb);

        RecyclerView.LayoutManager layoutManagerO = new GridLayoutManager(getContext(),2);
        rv_objects.setLayoutManager(layoutManagerO);
        getParty();

        btn_sheathed_unsheathed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //btn_sheathed_unsheathed.setForeground(btn_sheathed_unsheathed.getForeground().equals(ContextCompat.getDrawable(getActivity(), R.drawable.sword)) ? ContextCompat.getDrawable(getActivity(), R.drawable.sword_off) : ContextCompat.getDrawable(getActivity(), R.drawable.sword));
                openPopUpEquipment();
            }
        });

        btn_gold_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPopUpGold();
            }
        });


        btn_goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackHome();
            }
        });
        return v;
    }

    private void goBackHome() {
        getParentFragmentManager().popBackStack();
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
                    Party newParty = documentSnapshot.toObject(Party.class);
                    ArrayList<Character> characters = newParty.getPlayers();

                    if(!newParty.getOpen()){
                        closeParty();
                    }else {
                        if(party != null){
                            System.out.println(party.getTurn());
                            System.out.println(newParty.getFighting());
                            if((!party.getFighting()) && newParty.getFighting()){
                                party = newParty;
                                callPopUpInitiative();
                            }

                            if(character!=null){
                                for (Character charact: characters) {
                                    if(charact.getUserID().equals(User.getInstance().getId())){
                                        if(character.getLevel()<charact.getLevel()){
                                            party = newParty;
                                            callLevelUpPopUp();
                                        }
                                    }
                                }
                            }


                        }
                        int index = -1;
                        for (Character charact: characters) {
                            index ++;
                            if(charact.getUserID().equals(User.getInstance().getId())){
                                character = charact;
                                break;
                            }
                        }

/*
                        // Accede al campo "players" dentro de la Party
                        List<Map<String, Object>> playersList = (List<Map<String, Object>>) documentSnapshot.get("players");

                        Map<String, Object> targetPlayer = null;
                        if (playersList != null) {
                            for (Map<String, Object> playerMap : playersList) {
                                String playerID = (String) playerMap.get("id");
                                if (playerID.equals(character.getID())) {
                                    targetPlayer = playerMap;
                                    break;
                                }
                            }
                        }
                        ArrayList<Item> items = new ArrayList<>();
                        if (targetPlayer != null) {
                            List<Map<String, Object>> itemList = (List<Map<String, Object>>) targetPlayer.get("items");

                            if (itemList != null) {
                                for (Map<String, Object> itemMap : itemList) {
                                    // Realiza las operaciones necesarias en cada elemento
                                    String itemType = (String) itemMap.get("type");
                                    switch (itemType) {
                                        case "usables":
                                            items.add(convertMapToUsable(itemMap));
                                            break;
                                        case "weapons":
                                            items.add(convertMapToWeapons(itemMap));
                                            break;
                                        case "shields":
                                            items.add(convertMapToShield(itemMap));
                                            break;
                                        case "armors":
                                            items.add(convertMapToArmor(itemMap));
                                            break;
                                        default:
                                            // Manejar caso inesperado
                                            break;
                                    }
                                }
                            }
                        }*/
                        party = newParty;
                        setCharacterScreen();
                        AdapterSpells();
                    }



        });
    }

    private void closeParty() {
        System.out.println("error estamos2");

        ((GameplayActivity)getActivity()).backToMainActivity(Activity.RESULT_OK);
    }

    private void callLevelUpPopUp() {
        Button btn_Continue;
        TextView txt_text,txt_value;

        View view = getLayoutInflater().inflate(R.layout.fragment_character_stats, null);

        btn_Continue = view.findViewById(R.id.btn_continue);
        txt_text = view.findViewById(R.id.txt_statToThrow);
        txt_value = view.findViewById(R.id.txt_numberStat);

        PopUpCustom popUp = new PopUpCustom(view);
        popUp.show(getParentFragmentManager(), "initiativeEnemy");
        popUp.setCancelable(false);

        txt_text.setText(getResources().getString(R.string.levelUp).replace("@dIcE@","d"+Integer.toString(character.getTypeHitDice())));

        btn_Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txt_value.getText().toString().equals("")){
                    int tmpValue = Integer.parseInt(txt_value.getText().toString());
                    if(0<tmpValue && tmpValue<character.getTypeHitDice()+1){
                        for(int i =0; i<party.getPlayers().size();i++){
                            if(party.getPlayers().get(i).getID().equals(character.getID())){
                                party.getPlayers().get(i).setMaxHitPoints(party.getPlayers().get(i).getMaxHitPoints() + tmpValue);
                                party.getPlayers().get(i).setCurrentHitPoints(party.getPlayers().get(i).getMaxHitPoints());
                                break;
                            }
                        }
                        db.collection("parties").document(partyCode).set(party).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                popUp.dismiss();
                            }
                        });
                    }else{
                        txt_value.setError(getResources().getString(R.string.errorStat));
                    }
                }else{
                    txt_value.setError(getResources().getString(R.string.errorStat));
                }
            }
        });
    }



    private void startFight(){
        ((GameplayActivity)getActivity()).attackDone(partyCode,"no");
    }

    private void callPopUpInitiative(){
        Button btn_Continue;
        TextView txt_text,txt_value;

        View view = getLayoutInflater().inflate(R.layout.fragment_character_stats, null);

        btn_Continue = view.findViewById(R.id.btn_continue);
        txt_text = view.findViewById(R.id.txt_statToThrow);
        txt_value = view.findViewById(R.id.txt_numberStat);

        PopUpCustom popUp = new PopUpCustom(view);
        popUp.show(getParentFragmentManager(), "initiativePlayer");
        popUp.setCancelable(false);

        txt_text.setText(getResources().getText(R.string.initiativeToPlay));

        btn_Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txt_value.getText().toString().equals("")){
                    int tmpValue = Integer.parseInt(txt_value.getText().toString());
                    if(0<tmpValue && tmpValue<21){

                        for(int i =0; i<party.getPlayers().size();i++){
                            if(party.getPlayers().get(i).getID().equals(character.getID())){
                                if(party.getPlayers().get(i).getCurrentHitPoints()<=0){
                                    party.getPlayers().get(i).setCurrentHitPoints(1);
                                }
                                party.getPlayers().get(i).setInitiative(tmpValue + party.getPlayers().get(i).getInitiativeMod());
                                break;
                            }
                        }

                        db.collection("parties").document(partyCode).set(party).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                popUp.dismiss();
                                startFight();
                            }
                        });


                    }else{
                        txt_value.setError(getResources().getString(R.string.errorStat));
                    }
                }else{
                    txt_value.setError(getResources().getString(R.string.errorStat));
                }
            }
        });
    }




    private void openPopUpGold() {
        Button btn_okay;
        TextView showGold;
        View v = getLayoutInflater().inflate(R.layout.popup_game_player_show_gold, null);

        btn_okay = v.findViewById(R.id.btn_okayMoney);
        showGold = v.findViewById(R.id.txt_showMoney);
        String actualMoney = "pp:" + character.getMoneyPlatinum() + ", gp:"+ character.getMoneyGold() + ", sp:"+ character.getMoneySilver() + ", cp:"+character.getMoneyCopper();
        showGold.setText(actualMoney);

        PopUpCustom popUp = new PopUpCustom(v);
        popUp.show(getParentFragmentManager(), "showMoney");
        popUp.setCancelable(false);


        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
            }
        });
    }

    private void openPopUpEquipment() {

        Button btn_selectItems;
        RecyclerView rv;
        AdapterEquipment adapter;
        View v = getLayoutInflater().inflate(R.layout.popup_gameplay_select_equipment, null);

        rv = v.findViewById(R.id.rv_itemsSelector);
        btn_selectItems = v.findViewById(R.id.btn_continueItems);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        rv.setLayoutManager(layoutManager);

        ArrayList<OptionsCharacter> dataSetSkills = takeAllToSelect();
        adapter = new AdapterEquipment(dataSetSkills, getActivity());
        rv.setAdapter(adapter);


        PopUpCustom popUp = new PopUpCustom(v);
        popUp.show(getParentFragmentManager(), "showEquipment");
        popUp.setCancelable(false);


        btn_selectItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_selectItems.setError(null);
                ArrayList<OptionsCharacter> tmpData = adapter.getData();
                ArrayList<Item> result = new ArrayList<>();
                for (OptionsCharacter sk: tmpData) {
                    if(sk.isSelected()) {

                        for (Item itemCharacter: character.getItems()) {
                            if(sk.getCode().equals(itemCharacter.getCode())){
                                switch (itemCharacter.getType()){
                                    case "armors":
                                        result.add(new Item("armors",itemCharacter.getName(),itemCharacter.getCode(),"",itemCharacter.getBase(),itemCharacter.getMaxBonus(),"","",false));
                                        break;
                                    case "weapons":
                                        result.add(new Item("weapons",itemCharacter.getName(),itemCharacter.getCode(),"","",0,"",itemCharacter.getDamageDice(),itemCharacter.isHitMelee()));
                                        break;
                                    case "shields":
                                        result.add(new Item("shields",itemCharacter.getName(),itemCharacter.getCode(),itemCharacter.getArmorClass(),"",0,"","",false));
                                        break;
                                }
                            }
                        }
                    }
                }
                int shields=0;
                int weapons=0;
                int armor=0;
                for (Item item:result) {
                    switch (item.getType()){
                        case "armors":
                            System.out.println("armor-------------");
                            armor++;
                            break;
                        case "weapons":
                            System.out.println("weapons-------------");

                            weapons ++;
                            break;
                        case "shields":
                            System.out.println("shields-------------");

                            shields++;
                            break;
                    }
                }

                if(weapons>2){
                    btn_selectItems.setError("To many weapons");
                } else if (shields>1) {
                    btn_selectItems.setError("To many shields");
                }else if(armor>1){
                    btn_selectItems.setError("To many armors");
                } else if (weapons==2 && shields ==1) {
                    btn_selectItems.setError("Not enough hands");
                }else{
                    character.setWeaponEquipped(result);
                    updateParty();
                }

                popUp.dismiss();
            }
        });
    }

    private void updateParty() {
        int indexCharacter = -1;
        for (Character characterParty: party.getPlayers()) {
            indexCharacter++;
            if(characterParty.getUserID().equals(characterParty.getID())){
                party.getPlayers().set(indexCharacter,character);
                break;
            }
        }
        db.collection("parties").document(partyCode).set(party).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    private Item getItem(String name) {
        ArrayList<Item> find = character.getItems();
        for (Item item:find) {
            if(item.getName().equals(name)){
                return item;
            }
        }
        return null;
    }

    private ArrayList<OptionsCharacter> takeAllToSelect() {
        ArrayList<OptionsCharacter> result = new ArrayList<>();
        for (Item item: character.getWeaponEquipped()) {
            OptionsCharacter tmp = new OptionsCharacter(item.getCode(),item.getName());
            tmp.setSelected(true);
            result.add(tmp);
        }
        boolean inEquiped = false;
        for (Item item: character.getItems()) {
            for (Item itemEquiped: character.getWeaponEquipped()) {
                if(item.getName().equals(itemEquiped.getName())){
                    inEquiped = true;
                    break;
                }
            }
            if(!item.getType().equals("usables") && !inEquiped){
                OptionsCharacter tmp = new OptionsCharacter(item.getCode(),item.getName());
                tmp.setSelected(false);
                result.add(tmp);
            }
            inEquiped = false;
        }

        return result;
    }





    private void getParty() {
        System.out.println("dd "+partyCode);
        db.collection("parties").document(partyCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    party = task.getResult().toObject(Party.class);
                    ArrayList<Character> characters = party.getPlayers();
                    for (Character charact: characters) {
                        if(charact.getUserID().equals(User.getInstance().getId())){
                            character = charact;
                        }
                    }
                    setCharacterScreen();
                    AdapterSpells();
                }
            }
        });
    }

    private void setCharacterScreen() {
        rtv_name_game.setText(character.getName());
        rtv_pronoun_game.setText(character.getPronoun());
        rtv_gender_game.setText(character.getGender());
        rtv_level_game.setText(Integer.toString(character.getLevel()));
        rtv_race_game.setText(character.getRace());
        rtv_class_game.setText(character.getClassPlayer());
        txt_life_game.setText(Integer.toString(character.getCurrentHitPoints()));
        if(!character.getImgPlayerName().equals("")){
            StorageReference storageRef = dbStorage.getReference();
            String imageName = character.getImgPlayerName();
            StorageReference imageRef = storageRef.child(imageName);

            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getContext())
                            .load(uri)
                            .into(img_character_game);
                }
            });
        }else{
            Glide.with(getContext())
                    .load(R.drawable.avatar_1)
                    .into(img_character_game);
        }
    }

    void setElements(View v){
        rv_spells = v.findViewById(R.id.rv_spells);
        rv_abilities = v.findViewById(R.id.rv_abilities);
        rv_objects = v.findViewById(R.id.rv_objects);
        rv_spQuantity = v.findViewById(R.id.rv_spells_quantity_game);


        rtv_name_game = v.findViewById(R.id.rtv_name_game);
        rtv_pronoun_game = v.findViewById(R.id.rtv_pronoun_game);
        rtv_gender_game = v.findViewById(R.id.rtv_gender_game);
        rtv_level_game = v.findViewById(R.id.rtv_level_game);
        rtv_race_game = v.findViewById(R.id.rtv_race_game);
        rtv_class_game = v.findViewById(R.id.rtv_class_game);
        txt_life_game = v.findViewById(R.id.txt_life_game);

        img_character_game = v.findViewById(R.id.img_character_game);

        btn_gold_game = v.findViewById(R.id.txt_gold_game);
        btn_sheathed_unsheathed = v.findViewById(R.id.btn_sheathed_unsheathed);
        btn_goHome = v.findViewById(R.id.btn_goHome);
    }

    private void AdapterSpells() {
        Map<String,Integer> spellsToAdd = new HashMap<>(character.getSpells().getSpells());
        spellsToAdd.put("0Cantrips",character.getSpells().getCantrips());
        if(character.getSpells().getSpells_known() > 0){
            spellsToAdd.put("0Spells Known",character.getSpells().getSpells_known());
        }
        adapterSpellsQ = new AdapterSpellsQuantity(spellsToAdd, getActivity());
        rv_spQuantity.setAdapter(adapterSpellsQ);

        Map<String, List<Spells.Spell>> tmpSpell = character.getSpells().getSpellsName();
        List<Spells.Spell> spells = new ArrayList<>();
        for(int i=0;i<tmpSpell.size();i++){
            List<Spells.Spell> spellsTMP = tmpSpell.get(Integer.toString(i));
            for(int j = 0;j<spellsTMP.size();j++){
                spells.add(tmpSpell.get(Integer.toString(i)).get(j));
            }
        }
        adapterSpells = new AdapterSpellsGame(spells, getActivity(),this);
        rv_spells.setAdapter(adapterSpells);

        adapterAbilities = new AdapterAbilities(character.getFeaturesAndTraits(), getActivity());
        rv_abilities.setAdapter(adapterAbilities);

        adapterItems = new AdapterItemsGame(character.getItems(), getActivity());
        rv_objects.setAdapter(adapterItems);
    }


    void loadingBar(int visibility){
        ((GameplayActivity)getActivity()).changeLoadingVisibility(visibility);
    }

    @Override
    public void onItemClick(Spells.Spell spell) {

    }



}