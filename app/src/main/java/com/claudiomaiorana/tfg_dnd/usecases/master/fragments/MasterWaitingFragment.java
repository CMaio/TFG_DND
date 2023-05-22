package com.claudiomaiorana.tfg_dnd.usecases.master.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Party;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.usecases.master.MasterManagerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class MasterWaitingFragment extends Fragment {
    private ListenerRegistration listenerRegistration;
    private FirebaseFirestore db;
    private String partyCodeParam;
    private Party party;


    private static final String ARG_PARAM1 = "";

    private String code;

    TextView txt_slot1,txt_slot2,txt_slot3,txt_slot4,txt_masterSlot;
    Button startGame;
    TextView partyCode;


    public MasterWaitingFragment() {}

    public static MasterWaitingFragment newInstance(String PartyCode) {
        MasterWaitingFragment fragment = new MasterWaitingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, PartyCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            code = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_master_waiting, container, false);
        db = FirebaseFirestore.getInstance();
        setElements(v);
        partyCode.setText(getResources().getText(R.string.code_waiting_screen).
                toString().replace("@coD3@",code));
        txt_masterSlot.setText(User.getInstance().getUserName());
        txt_slot1.setText(User.getInstance().getUserName());
        getParty();

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameIni();
            }
        });

        return v;
    }

    private void startGameIni() {
        party.setOpen(true);
        db.collection("parties").document(code).set(party).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startPlaying();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Escucha en tiempo real los mensajes dirigidos al usuario actual
        listenerRegistration = db.collection("parties")
                .document(code)
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        return;
                    }
                    party = documentSnapshot.toObject(Party.class);
                    updateScreen(party);
                });
    }


    @Override
    public void onPause() {
        super.onPause();

        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }

    }

    private void getParty(){
        System.out.println(partyCodeParam +" -------------------------------------------------" );
        db.collection("parties").document(code).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                party = task.getResult().toObject(Party.class);
            };
        });
    }


    private void setElementsEmpty() {
        txt_slot1.setText("");
        txt_slot2.setText("");
        txt_slot3.setText("");
        txt_slot4.setText("");
    }

    private void updateScreen(Party party) {
        List<String> characters = party.getPlayersConnected();
        setElementsEmpty();
        for (String character:characters){
            if(!(txt_slot1.getText().toString().equals(character) || txt_slot2.getText().toString().equals(character) ||txt_slot3.getText().toString().equals(character) ||txt_slot4.getText().toString().equals(character))) {
                if (txt_slot1.getText().toString().equals("") || txt_slot1.getText().toString() == null) {
                    txt_slot1.setText(character);
                } else if (txt_slot2.getText().toString().equals("") || txt_slot2.getText().toString() == null) {
                    txt_slot2.setText(character);
                } else if (txt_slot3.getText().toString().equals("") || txt_slot3.getText().toString() == null) {
                    txt_slot3.setText(character);
                } else if (txt_slot4.getText().toString().equals("") || txt_slot4.getText().toString() == null) {
                    txt_slot4.setText(character);
                }
            }
        }
    }


    void startPlaying(){
        Toast.makeText(getContext(), "Game started", Toast.LENGTH_SHORT).show();
        ((MasterManagerActivity)getActivity()).goToPlay(party);
    }



    private void setElements(View v) {
        txt_slot1 = v.findViewById(R.id.txt_slot1_master);
        txt_slot2 = v.findViewById(R.id.txt_slot2_master);
        txt_slot3 = v.findViewById(R.id.txt_slot3_master);
        txt_slot4 = v.findViewById(R.id.txt_slot4_master);
        txt_masterSlot = v.findViewById(R.id.txt_masterSlot_Master);
        startGame = v.findViewById(R.id.btn_startGame);
        partyCode = v.findViewById(R.id.txt_partyCode);
    }
}