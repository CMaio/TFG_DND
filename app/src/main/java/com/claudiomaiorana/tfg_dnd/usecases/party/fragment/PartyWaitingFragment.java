package com.claudiomaiorana.tfg_dnd.usecases.party.fragment;

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
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.model.Party;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.usecases.party.PartyManagerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;


public class PartyWaitingFragment extends Fragment {
    private ListenerRegistration listenerRegistration;
    private FirebaseFirestore db;
    private String partyCodeParam;
    private Party party;

    TextView txt_slot1,txt_slot2,txt_slot3,txt_slot4,txt_masterSlot;

    private static final String PARTY_CODE ="";


   //FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

    public PartyWaitingFragment() {}


    public static PartyWaitingFragment newInstance(String code) {
        PartyWaitingFragment fragment =  new PartyWaitingFragment();
        Bundle args = new Bundle();
        args.putString(PARTY_CODE, code);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            partyCodeParam = getArguments().getString(PARTY_CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_party_waiting, container, false);
        db = FirebaseFirestore.getInstance();
        setElements(v);
        txt_slot1.setText(User.getInstance().getUserName());
        getParty();


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Escucha en tiempo real los mensajes dirigidos al usuario actual
        listenerRegistration = db.collection("parties")
                .document(partyCodeParam)
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        return;
                    }

                    updateScreen(documentSnapshot.toObject(Party.class));
                });
    }

    @Override
    public void onPause() {
        super.onPause();

        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
        ArrayList<String> charConnected = party.getPlayersConnected();
        String nameCharacter = deleteCharacter(party.getPlayers());
        if(charConnected.contains(nameCharacter)){
            charConnected.remove(nameCharacter);
            db.collection("parties").document(partyCodeParam).set(party);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ArrayList<String> charConnected = party.getPlayersConnected();
        String nameCharacter = deleteCharacter(party.getPlayers());
        if(charConnected.contains(nameCharacter)){
            charConnected.remove(nameCharacter);
            db.collection("parties").document(partyCodeParam).set(party);
        }

    }

    private void getParty(){
        System.out.println(partyCodeParam +" -------------------------------------------------" );
        db.collection("parties").document(partyCodeParam).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                party = task.getResult().toObject(Party.class);
                updateParty();
            };
        });
    }

    private void updateParty() {
        ArrayList<String> charConnected = party.getPlayersConnected();
        String nameCharacter = characterName(party.getPlayers());
        if(!nameCharacter.equals("")){
            charConnected.add(nameCharacter);
            db.collection("parties").document(partyCodeParam).set(party).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    updateScreen(party);
                }
            });
        }
    }
    private String deleteCharacter(ArrayList<Character> players) {

        for (Character character : players) {
            if(character.getUserID().equals(User.getInstance().getId())){
                if(party.getPlayersConnected().contains(character.getName())){
                    return character.getName();
                }
            }
        }
        return "";
    }

    private String characterName(ArrayList<Character> players) {

        for (Character character : players) {
            if(character.getUserID().equals(User.getInstance().getId())){
                if(party.getPlayersConnected().contains(character.getName())){
                    return "";
                }else{
                    return character.getName();
                }
            }
        }
        return "";
    }

    private void updateScreen(Party party) {
        List<String> characters = party.getPlayersConnected();
        txt_masterSlot.setText(party.getUsernameMaster());
        setElementsEmpty();
        for (String character:characters){
            Log.d("newChar",character);
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
        if(party.getOpen()){
            startPlaying();
        }
    }

    private void setElementsEmpty() {
        txt_slot1.setText("");
        txt_slot2.setText("");
        txt_slot3.setText("");
        txt_slot4.setText("");
    }

    void startPlaying(){
        Toast.makeText(getContext(), "Game started", Toast.LENGTH_SHORT).show();
        ((PartyManagerActivity)getActivity()).goToPlay(party);
    }

    private void setElements(View v) {
        txt_slot1 = v.findViewById(R.id.txt_slot1);
        txt_slot2 = v.findViewById(R.id.txt_slot2);
        txt_slot3 = v.findViewById(R.id.txt_slot3);
        txt_slot4 = v.findViewById(R.id.txt_slot4);
        txt_masterSlot = v.findViewById(R.id.txt_masterSlot);
    }
}