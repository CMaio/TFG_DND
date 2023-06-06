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
import android.widget.TextView;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.model.Enemy;
import com.claudiomaiorana.tfg_dnd.model.Item;
import com.claudiomaiorana.tfg_dnd.model.Party;
import com.claudiomaiorana.tfg_dnd.model.PossibleAttack;
import com.claudiomaiorana.tfg_dnd.model.Spells;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.usecases.game.GameplayActivity;
import com.claudiomaiorana.tfg_dnd.usecases.game.adapters.AdapterAttacksPossibles;
import com.claudiomaiorana.tfg_dnd.usecases.game.adapters.AdapterEnemiesShown;
import com.claudiomaiorana.tfg_dnd.usecases.game.fragments.GameplayAttackOptionsFragment;
import com.claudiomaiorana.tfg_dnd.usecases.master.MasterManagerActivity;
import com.claudiomaiorana.tfg_dnd.usecases.master.adapters.AdapterPlayersShown;
import com.claudiomaiorana.tfg_dnd.util.Constants;
import com.claudiomaiorana.tfg_dnd.util.PopUpCustom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class MasterGameplayAttackEnemyFragment extends Fragment implements AdapterPlayersShown.OnItemClickListener,AdapterAttacksPossibles.OnItemClickListener {


    private RecyclerView rv_attackNames,rv_enemiesName;
    private TextView txt_tittleAttack;
    private Button btn_attackSelected;

    private AdapterPlayersShown adapterPlayers;
    private AdapterAttacksPossibles adapterAttacks;

    private FirebaseFirestore db;

    private static final String ARG_PARAM1 = "param1";

    Party actualParty;
    Enemy enemyTurn;

    private String partyCode;

    private ArrayList<PossibleAttack> possibleAttacks;
    Character enemyPlayer = null;
    PossibleAttack possibleAttackSelected = null;
    Item attackUsed = null;




    public MasterGameplayAttackEnemyFragment() {}

    public static MasterGameplayAttackEnemyFragment newInstance(String param1) {
        MasterGameplayAttackEnemyFragment fragment = new MasterGameplayAttackEnemyFragment();
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
        View v = inflater.inflate(R.layout.fragment_master_gameplay_attack_enemy, container, false);
        db=FirebaseFirestore.getInstance();
        setElements(v);
        getParty();






        btn_attackSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enemyPlayer == null || possibleAttackSelected == null ){
                    btn_attackSelected.setError("");
                }else{
                    popUpPassingArmor();
                }
            }
        });
        return v;
    }

    private void setAdapters() {
        RecyclerView.LayoutManager lyManagerA = new GridLayoutManager(getContext(),1);
        rv_attackNames.setLayoutManager(lyManagerA);
        adapterAttacks = new AdapterAttacksPossibles(possibleAttacks,getActivity(),this);
        rv_attackNames.setAdapter(adapterAttacks);


        RecyclerView.LayoutManager lyManagerE = new GridLayoutManager(getContext(),1);
        rv_enemiesName.setLayoutManager(lyManagerE);
        adapterPlayers = new AdapterPlayersShown(actualParty.getPlayers(),getActivity(),this);
        rv_enemiesName.setAdapter(adapterPlayers);
    }


    private void popUpPassingArmor() {
        Button btn_Continue;
        TextView txt_text, txt_value;
        View view = getLayoutInflater().inflate(R.layout.fragment_character_stats, null);

        btn_Continue = view.findViewById(R.id.btn_continue);
        txt_text = view.findViewById(R.id.txt_statToThrow);
        txt_value = view.findViewById(R.id.txt_numberStat);

        PopUpCustom popUp = new PopUpCustom(view);
        popUp.show(getParentFragmentManager(), "armorClassPopUp");
        popUp.setCancelable(false);

        txt_text.setText(getResources().getText(R.string.armorClassThrow));

        btn_Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txt_value.getText().toString().equals("")){
                    int tmpValue = Integer.parseInt(txt_value.getText().toString());
                    if(0<tmpValue && tmpValue<21){
                        if(hittingWeapon(tmpValue)){
                            popUp.dismiss();
                            hittingPopUp();
                        }else{
                            popUp.dismiss();
                            noHitPopUp();
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

    private boolean hittingWeapon(int valueHit) {
        return valueHit >= enemyPlayer.getArmorClass();
    }


    private void noHitPopUp() {
        Button btn_okay;
        TextView showGold;
        View v = getLayoutInflater().inflate(R.layout.popup_game_player_show_gold, null);

        btn_okay = v.findViewById(R.id.btn_okayMoney);
        showGold = v.findViewById(R.id.txt_showMoney);
        showGold.setText(getResources().getText(R.string.noHitDice));

        PopUpCustom popUp = new PopUpCustom(v);
        popUp.show(getParentFragmentManager(), "noHitShow");
        popUp.setCancelable(false);


        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0;i<actualParty.getEnemiesFight().size();i++){
                    if(actualParty.getPlayers().get(i).getID().equals(enemyPlayer.getID())){
                        actualParty.getPlayers().get(i).setSelected(false);
                    }
                }
                returnToOptions();
                popUp.dismiss();
            }
        });
    }

    private void hittingPopUp() {
        Button btn_Continue;
        TextView txt_text, txt_value;
        View view = getLayoutInflater().inflate(R.layout.fragment_character_stats, null);

        btn_Continue = view.findViewById(R.id.btn_continue);
        txt_text = view.findViewById(R.id.txt_statToThrow);
        txt_value = view.findViewById(R.id.txt_numberStat);

        PopUpCustom popUp = new PopUpCustom(view);
        popUp.show(getParentFragmentManager(), "armorClassPopUp");
        popUp.setCancelable(false);

        txt_text.setText(getResources().getText(R.string.hitDice).toString().replace(("@h1tD1c3@"), possibleAttackSelected.getHitDice()));

        btn_Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txt_value.getText().toString().equals("")){
                    int tmpValue = Integer.parseInt(txt_value.getText().toString());
                    if(0<tmpValue){
                        popUp.dismiss();
                        setDamage(tmpValue);
                    }else{
                        txt_value.setError("");
                    }
                }else{
                    txt_value.setError("");
                }
            }
        });
    }

    private void setDamage(int tmpValue) {
        int actualLife = enemyPlayer.getCurrentHitPoints() - tmpValue;
        for(int i = 0;i<actualParty.getEnemiesFight().size();i++){
            if(actualParty.getPlayers().get(i).getID().equals(enemyPlayer.getID())){
                actualParty.getPlayers().get(i).setCurrentHitPoints(actualLife);
                actualParty.getPlayers().get(i).setSelected(false);
            }
        }
        returnToOptions();
    }


    private void returnToOptions(){
        db.collection("parties").document(partyCode).set(actualParty).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ((MasterManagerActivity) getActivity()).finishAttack(partyCode,1);
            }
        });
    }

    private void getAllAttacks() {
        possibleAttacks = new ArrayList<>();
        for (Item item: enemyTurn.getAttacks()) {
            if(item.getType().equals("weapons")){
                possibleAttacks.add(new PossibleAttack(item.getName(),item.getDamageDice(),item.getType(),item.isHitMelee()));
            }
        }
        setAdapters();
    }

    void setElements(View v){
        rv_attackNames = v.findViewById(R.id.rv_attackNames);
        rv_enemiesName = v.findViewById(R.id.rv_enemiesName);
        txt_tittleAttack = v.findViewById(R.id.txt_tittleAttack);
        btn_attackSelected = v.findViewById(R.id.btn_attackSelected);
    }

    private void getParty() {

        db.collection("parties").document(partyCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    actualParty = task.getResult().toObject(Party.class);
                    for (Enemy enemyTurnList: actualParty.getEnemiesFight()) {
                        if(enemyTurnList.getID().equals(actualParty.getTurn().split("/")[1])){
                            enemyTurn = enemyTurnList;
                            break;
                        }
                    }
                    getAllAttacks();

                }
            }
        });
    }


    @Override
    public void onItemClick(Character character) {
        enemyPlayer = character;
    }

    @Override
    public void onItemClick(PossibleAttack attack) {
        possibleAttackSelected = attack;
    }
}