package com.claudiomaiorana.tfg_dnd.usecases.game.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.model.Party;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.usecases.game.GameplayActivity;
import com.claudiomaiorana.tfg_dnd.util.PopUpCustom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;


public class GameplayFightFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String partyCode;
    private String attackDone;

    private FirebaseFirestore db;
    private ListenerRegistration listenerRegistration;

    Party party;
    Character character;
    int indexCharacter = -1;
    int feets;

    private LinearLayout ly_loading;
    private Button btn_movement,btn_attack,btn_bonus,btn_finish;


    public GameplayFightFragment() {}

    public static GameplayFightFragment newInstance(String param1, String param2) {
        GameplayFightFragment fragment = new GameplayFightFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            partyCode = getArguments().getString(ARG_PARAM1);
            attackDone = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_gameplay_fight, container, false);
        db = FirebaseFirestore.getInstance();
        setElements(v);
        getParty();
        ly_loading.setVisibility(View.VISIBLE);



        btn_movement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingMovement();
            }
        });

        if(attackDone.equals("yes")){
            btn_attack.setVisibility(View.INVISIBLE);
        }else{
            btn_attack.setVisibility(View.VISIBLE);
            btn_attack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectAttack();
                }
            });

        }


        btn_bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPopUpBonus();
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishTurn();
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
                    Party tmpParty = documentSnapshot.toObject(Party.class);
                    ArrayList<Character> characters = tmpParty.getPlayers();
                    indexCharacter = -1;
                    for (Character characterParty: characters) {
                        indexCharacter++;
                        if(characterParty.getUserID().equals(User.getInstance().getId())){
                            character = characterParty;
                        }
                    }
                    if(tmpParty.getFighting()){
                        if(tmpParty.getTurn().equals(character.getID())){
                            ly_loading.setVisibility(View.INVISIBLE);

                        }else{
                            ly_loading.setVisibility(View.VISIBLE);

                        }

                    }else if(!tmpParty.getFighting() && party.getFighting()){
                        fightingIsFinished();
                    }
                    
                    party = tmpParty;

                });
    }

    private void fightingIsFinished() {
        //TODO:Change back to safe gameplay fragment
        db.collection("parties").document(partyCode).set(party).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ((GameplayActivity)getActivity()).changeFragment("gameplaySafe");
            }
        });
    }

    private void getParty() {
        db.collection("parties").document(partyCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    party = task.getResult().toObject(Party.class);
                    ArrayList<Character> characters = party.getPlayers();
                    indexCharacter = -1;
                    for (Character charact: characters) {
                        indexCharacter++;
                        if(charact.getUserID().equals(User.getInstance().getId())){
                            character = charact;
                        }
                    }

                }
            }
        });
    }
    private void updateParty(){
        party.getPlayers().set(indexCharacter,character);
        db.collection("parties").document(partyCode).set(party).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    private void settingMovement() {
        TextView movementInfo;
        SeekBar movementCount;
        Button finishMove;
        View v = getLayoutInflater().inflate(R.layout.popup_movement_view, null);

        movementInfo = v.findViewById(R.id.txt_movementInfo);
        movementCount = v.findViewById(R.id.skb_movementNumber);
        finishMove = v.findViewById(R.id.btn_move);
        feets = 0;
        movementCount.setMax(character.getSpeed());
        movementCount.setProgress(feets);

        String movementInfoUpdated = getResources().getString(R.string.popUpMovementInfo);

        movementInfoUpdated.replace("@mov3",Integer.toString(feets));
        movementInfo.setText(movementInfoUpdated);

        PopUpCustom popUp = new PopUpCustom(v);
        popUp.show(getParentFragmentManager(), "showMoney");
        popUp.setCancelable(false);


        movementCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String update = movementInfo.getText().toString();
                update.replace(Integer.toString(feets),Integer.toString(i));
                feets = i;
                movementInfo.setText(update);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        finishMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
            }
        });


    }

    private void selectAttack() {
        ((GameplayActivity) getActivity()).changeToAttack(partyCode);
    }

    private void callPopUpBonus() {
        Button btn_okay;
        TextView showGold;
        View v = getLayoutInflater().inflate(R.layout.popup_game_player_show_gold, null);

        btn_okay = v.findViewById(R.id.txt_showMoney);
        showGold = v.findViewById(R.id.btn_okayMoney);

        showGold.setText(R.string.bonusDescription);

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

    private void finishTurn() {
        party.getPlayers().set(indexCharacter,character);
        party.setTurn("");
        updateParty();

    }

    void setElements(View v){
        ly_loading = v.findViewById(R.id.ly_loading);

        btn_movement = v.findViewById(R.id.btn_movement);
        btn_attack = v.findViewById(R.id.btn_attack);
        btn_bonus = v.findViewById(R.id.btn_bonus);
        btn_finish = v.findViewById(R.id.btn_finish);
    }

    void loadingBar(int visibility){
        ((GameplayActivity)getActivity()).changeLoadingVisibility(visibility);
    }
}