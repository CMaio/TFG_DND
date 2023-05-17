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
import com.claudiomaiorana.tfg_dnd.model.Party;
import com.claudiomaiorana.tfg_dnd.model.User;
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
    Button startGame;

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

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(party.getIDMaster().equals(User.getInstance().getUserName())){
                    updateParty();
                }
            }
        });
        return v;
    }

    private void updateParty() {
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
    }

    private void getParty(){
        System.out.println(partyCodeParam +" -------------------------------------------------" );
        db.collection("parties").document(partyCodeParam).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                updateScreen(task.getResult().toObject(Party.class));
                party=task.getResult().toObject(Party.class);

            };
        });
    }

    private void updateScreen(Party party) {
        List<String> characters = party.getIDCharacters();
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

    void startPlaying(){
        Toast.makeText(getContext(), "Game started", Toast.LENGTH_SHORT).show();
        /*Intent intent = new Intent(getApplicationContext(), NuevaActivity.class);
        intent.putExtra("remitente", remitente);
        intent.putExtra("contenido", contenido);

        // Inicia la nueva Activity
        startActivity(intent);*/
    }

   /* void timeToStart(){
        RemoteMessage message = new RemoteMessage.Builder("SENDER_ID" + "@fcm.googleapis.com")
                .setMessageId(Integer.toString(messageId++))
                .addData("miCampo", "miValor")
                .setToken(token)  // Reemplaza "token" con el token de registro del dispositivo receptor
                .build();

        firebaseMessaging.send(message)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // El mensaje se envió correctamente
                    } else {
                        // Ocurrió un error al enviar el mensaje
                    }
                });
    }*/



    private void setElements(View v) {
        txt_slot1 = v.findViewById(R.id.txt_slot1);
        txt_slot2 = v.findViewById(R.id.txt_slot2);
        txt_slot3 = v.findViewById(R.id.txt_slot3);
        txt_slot4 = v.findViewById(R.id.txt_slot4);
        txt_masterSlot = v.findViewById(R.id.txt_masterSlot);
        startGame = v.findViewById(R.id.btn_startGame);
    }
}