package com.claudiomaiorana.tfg_dnd.usecases.master.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.model.Enemy;
import com.claudiomaiorana.tfg_dnd.model.Party;
import com.claudiomaiorana.tfg_dnd.usecases.master.adapters.AdapterEnemies;
import com.claudiomaiorana.tfg_dnd.usecases.master.adapters.AdapterObjects;
import com.claudiomaiorana.tfg_dnd.usecases.master.adapters.AdapterPlayers;
import com.claudiomaiorana.tfg_dnd.util.PopUpCustom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;


public class MasterGameplayAttackFragment extends Fragment implements AdapterPlayers.OnItemClickListener, AdapterEnemies.OnItemClickListener{


    private static final String ARG_PARAM1 = "param1";

    private String partyCode;
    Party party;

    private FirebaseFirestore db;
    private ListenerRegistration listenerRegistration;

    private RecyclerView rv_playersDisplay,rv_enemiesDisplay;
    AdapterPlayers adapterPlayers;
    AdapterEnemies adapterEnemies;

    private Button btn_finishBattle;
    private TextView turnDisplay;


    public MasterGameplayAttackFragment() {}

    public static MasterGameplayAttackFragment newInstance(String param1) {
        MasterGameplayAttackFragment fragment = new MasterGameplayAttackFragment();
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
        View v = inflater.inflate(R.layout.fragment_master_gameplay_attack, container, false);
        db = FirebaseFirestore.getInstance();
        setElements(v);
        getParty();


        btn_finishBattle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                party.setFighting(false);
                updateParty();
            }
        });
        return v;
    }

    private void getParty() {
        db.collection("parties").document(partyCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    party = task.getResult().toObject(Party.class);
                    setAdapters();
                }
            }
        });
    }

    public void onResume() {
        super.onResume();

        listenerRegistration = db.collection("parties")
                .document(partyCode)
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        return;
                    }
                    party = documentSnapshot.toObject(Party.class);
                    turnDisplay.setText(party.getTurn());
                });
    }


    private void updateParty(){
        db.collection("parties").document(partyCode).set(party).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setAdapters();
            }
        });
    }

    private void setAdapters(){
        RecyclerView.LayoutManager layoutManagerP = new GridLayoutManager(getContext(),1);
        rv_playersDisplay.setLayoutManager(layoutManagerP);

        RecyclerView.LayoutManager layoutManagerE = new GridLayoutManager(getContext(),1);
        rv_enemiesDisplay.setLayoutManager(layoutManagerE);
        adapterPlayers = new AdapterPlayers(party.getPlayers(),getActivity(),this);
        adapterEnemies = new AdapterEnemies(party.getEnemiesFight(),getActivity(),this);

        rv_playersDisplay.setAdapter(adapterPlayers);
        rv_enemiesDisplay.setAdapter(adapterEnemies);
    }



    void setElements(View v){
        rv_playersDisplay = v.findViewById(R.id.rv_playersDisplayMaster);
        rv_enemiesDisplay = v.findViewById(R.id.rv_enemiesDisplayMaster);

        btn_finishBattle = v.findViewById(R.id.btn_switchBattle);
        turnDisplay = v.findViewById(R.id.turnDisplay);
    }

    @Override
    public void newEnemy() {}

    @Override
    public void onItemClick(Enemy enemy) {
        Button btn_giveMoney, btn_giveLife, btn_giveItem, btn_increaseLevel, btn_takeMoney, btn_takeLife, btn_takeItem;

        View v = getLayoutInflater().inflate(R.layout.popup_master_options_do_player, null);

        btn_giveMoney = v.findViewById(R.id.btn_giveMoney);
        btn_giveLife = v.findViewById(R.id.btn_giveLife);
        btn_giveItem = v.findViewById(R.id.btn_giveItem);
        btn_increaseLevel = v.findViewById(R.id.btn_increaseLevel);
        btn_takeMoney = v.findViewById(R.id.btn_takeMoney);
        btn_takeLife = v.findViewById(R.id.btn_takeLife);
        btn_takeItem = v.findViewById(R.id.btn_takeItem);


        btn_giveMoney.setVisibility(View.GONE);
        btn_giveItem.setVisibility(View.GONE);
        btn_increaseLevel.setVisibility(View.GONE);
        btn_takeMoney.setVisibility(View.GONE);
        btn_takeItem.setVisibility(View.GONE);



        PopUpCustom popUp = new PopUpCustom(v);
        popUp.show(getParentFragmentManager(), "optionsCharacter");


        btn_giveLife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLifeEnemy(enemy, true);

            }
        });

        btn_takeLife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLifeEnemy(enemy, false);
            }
        });
    }

    void setLifeEnemy(Enemy enemy, boolean add){
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
            txt_actualStat.setText(getResources().getText(R.string.giveHealthToPlayer).toString().replace("@mon3y@",Integer.toString(enemy.getCurrentHitPoints())));
        }else{
            txt_actualStat.setText(getResources().getText(R.string.takeHealthToPlayer).toString().replace("@mon3y@",Integer.toString(enemy.getCurrentHitPoints())));
        }

        PopUpCustom popUp = new PopUpCustom(v);
        popUp.show(getParentFragmentManager(), "optionsLifeEnemy");
        popUp.setCancelable(false);



        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lifeNumber = Integer.parseInt(txt_numberToAdd.getText().toString());
                if(lifeNumber<1){
                    txt_numberToAdd.setError(getResources().getString(R.string.ErrorValueToPlayer));
                }else{
                    enemy.setCurrentHitPoints(add ? enemy.getCurrentHitPoints() + lifeNumber :  enemy.getCurrentHitPoints() - lifeNumber );
                    updateEnemy(enemy);
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
    public void onItemClick(Character character) {
        Button  btn_giveLife, btn_takeLife;

        View v = getLayoutInflater().inflate(R.layout.popup_master_options_do_player, null);
        btn_giveLife = v.findViewById(R.id.btn_giveLife);
        btn_takeLife = v.findViewById(R.id.btn_takeLife);

        PopUpCustom popUp = new PopUpCustom(v);
        popUp.show(getParentFragmentManager(), "optionsCharacter");


        btn_giveLife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLife(character, true);

            }
        });

        btn_takeLife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLife(character, false);
            }
        });

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

    private void updateCharacter(Character character) {
        for(int i=0;i<party.getPlayers().size();i++){
            if(party.getPlayers().get(i).getID().equals(character.getID())){
                party.getPlayers().set(i,character);
                break;
            }
        }
        updateParty();
    }

    private void updateEnemy(Enemy enemy) {
        for(int i=0;i<party.getEnemiesFight().size();i++){
            if(party.getEnemiesFight().get(i).getID().equals(enemy.getID())){
                party.getEnemiesFight().set(i,enemy);
                break;
            }
        }
        updateParty();
    }
}