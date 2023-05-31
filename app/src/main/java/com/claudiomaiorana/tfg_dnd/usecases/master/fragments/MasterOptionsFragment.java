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
import com.claudiomaiorana.tfg_dnd.model.Weapons;
import com.claudiomaiorana.tfg_dnd.usecases.game.GameplayActivity;
import com.claudiomaiorana.tfg_dnd.usecases.game.adapters.AdapterAttacksPossibles;
import com.claudiomaiorana.tfg_dnd.usecases.game.adapters.AdapterEnemiesShown;
import com.claudiomaiorana.tfg_dnd.usecases.game.fragments.GameplayAttackOptionsFragment;
import com.claudiomaiorana.tfg_dnd.util.Constants;
import com.claudiomaiorana.tfg_dnd.util.PopUpCustom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MasterOptionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MasterOptionsFragment extends Fragment implements AdapterEnemiesShown.OnItemClickListener,AdapterAttacksPossibles.OnItemClickListener {
    //TODO:modificar para que sea como el jugador
    private RecyclerView rv_attackNames,rv_enemiesName;
    private TextView txt_tittleAttack;
    private Button btn_attackSelected;

    private AdapterEnemiesShown adapterEnemies;
    private AdapterAttacksPossibles adapterAttacks;

    private FirebaseFirestore db;

    private static final String ARG_PARAM1 = "param1";

    Party actualParty;
    Character character;

    private String partyCode;

    private ArrayList<PossibleAttack> possibleAttacks;
    Enemy enemySelected = null;
    PossibleAttack possibleAttackSelected = null;
    Weapons attackUsed = null;




    public MasterOptionsFragment() {}

    public static MasterOptionsFragment newInstance(String param1) {
        MasterOptionsFragment fragment = new MasterOptionsFragment();
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
        View v = inflater.inflate(R.layout.fragment_master_options, container, false);
        setElements(v);
        getParty();
        getAllAttacks();

        RecyclerView.LayoutManager lyManagerA = new GridLayoutManager(getContext(),1);
        rv_attackNames.setLayoutManager(lyManagerA);
        adapterAttacks = new AdapterAttacksPossibles(possibleAttacks,getActivity(),this);

        RecyclerView.LayoutManager lyManagerE = new GridLayoutManager(getContext(),1);
        rv_enemiesName.setLayoutManager(lyManagerE);
        adapterEnemies = new AdapterEnemiesShown(actualParty.getEnemiesFight(),getActivity(),this);



        btn_attackSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enemySelected == null || possibleAttackSelected == null ){
                    btn_attackSelected.setError("");
                }else{

                    if(possibleAttackSelected.getType().equals("spells")){
                        popUpSavingThrow();
                    }else{
                        popUpPassingArmor();
                    }
                }
            }
        });
        return v;
    }

    private void popUpSavingThrow() {
        Button btn_Continue;
        TextView txt_text, txt_value;
        View view = getLayoutInflater().inflate(R.layout.fragment_character_stats, null);

        btn_Continue = view.findViewById(R.id.btn_continue);
        txt_text = view.findViewById(R.id.txt_statToThrow);
        txt_value = view.findViewById(R.id.txt_numberStat);

        PopUpCustom popUp = new PopUpCustom(view);
        popUp.show(getParentFragmentManager(), "spellSavingPopUp");
        popUp.setCancelable(false);

        txt_text.setText(getResources().getText(R.string.savingThrow).toString().replace(("@sp3ll@"),possibleAttackSelected.getName()));

        btn_Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txt_value.getText().toString().equals("")){
                    int tmpValue = Integer.parseInt(txt_value.getText().toString());
                    if(0<tmpValue && tmpValue<21){
                        if(hittingSpell(tmpValue)){
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
        int result = valueHit + (possibleAttackSelected.getHitMelee() ? character.getSpecificStats_Mod(Constants.STAT_STR) : character.getSpecificStats_Mod(Constants.STAT_DEX)) +character.getProfBonus();
        return result >= enemySelected.getArmorClass();
    }

    private boolean hittingSpell(int valueHit) {
        return valueHit < character.getSavingThrowSpell();
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
        int actualLife = enemySelected.getCurrentHitPoints() - tmpValue;
        if(actualLife <= 0){
            for(int i = 0;i<actualParty.getEnemiesFight().size();i++){
                if(actualParty.getEnemiesFight().get(i).getID().equals(enemySelected.getID())){
                    actualParty.getEnemiesFight().remove(i);
                }
            }
        }else{
            for(int i = 0;i<actualParty.getEnemiesFight().size();i++){
                if(actualParty.getEnemiesFight().get(i).getID().equals(enemySelected.getID())){
                    actualParty.getEnemiesFight().get(i).setCurrentHitPoints(actualLife);
                }
            }
        }

        returnToOptions();
    }


    private void returnToOptions(){
        db.collection("parties").document(partyCode).set(actualParty).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ((GameplayActivity) getActivity()).attackDone(partyCode);
            }
        });
    }

    private void getAllAttacks() {
        possibleAttacks = new ArrayList<>();
        for (Item item: character.getWeaponEquipped()) {
            if(item.getType().equals("weapons")){
                Weapons weapon = (Weapons) item;
                possibleAttacks.add(new PossibleAttack(weapon.getName(),weapon.getDamageDice(),weapon.getType(),weapon.isHitMelee()));
            }
        }
        if(!character.getSpells().getSpellsDamage().isEmpty()){
            for(int i = 0;i<character.getSpells().getSpellsDamage().keySet().size(); i++){
                List<Spells.Spell> spells = character.getSpells().getSpellsDamage().get(Integer.toString(i));
                for (Spells.Spell spell: spells) {
                    possibleAttacks.add(new PossibleAttack(spell.getName(),spell.getSlot_level().get(Integer.toString(character.getLevel())),"spells",false));
                }
            }
        }
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
                    ArrayList<Character> characters = actualParty.getPlayers();
                    for (Character charact: characters) {
                        if(charact.getUserID().equals(User.getInstance().getId())){
                            character = charact;
                        }
                    }
                }
            }
        });
    }


    @Override
    public void onItemClick(Enemy enemy) {
        enemySelected = enemy;
    }

    @Override
    public void onItemClick(PossibleAttack attack) {
        possibleAttackSelected = attack;
    }
}