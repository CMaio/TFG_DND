package com.claudiomaiorana.tfg_dnd.usecases.character.fragment;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Party;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.usecases.character.CharacterManagerActivity;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterCharacters;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class CharacterListFragment extends Fragment implements AdapterCharacters.OnItemClickListener{
    private RecyclerView rv_list;
    private AdapterCharacters adapter;
    private static ArrayList<Character> dataSet;
    private FirebaseFirestore db;

    private static String PARTY_CODE ="partyCode";
    String partyCode="";

    public CharacterListFragment() {}

    public static CharacterListFragment newInstance(String idParty) {
        CharacterListFragment fragment = new CharacterListFragment();
        Bundle args = new Bundle();
        args.putString(PARTY_CODE, idParty);
        fragment.setArguments(args);
        //PARTY_CODE = idParty;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            partyCode = getArguments().getString(PARTY_CODE);
            if(partyCode == null){partyCode = "";}
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentV = inflater.inflate(R.layout.fragment_character_list, container, false);
        rv_list = fragmentV.findViewById(R.id.RV_listCharacters);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_list.setLayoutManager(layoutManager);
        db = FirebaseFirestore.getInstance();
        //generateCharacter();
        loadingBar(View.VISIBLE);
        getData();

        System.out.println("hola--------------------"+dataSet);
        adapter = new AdapterCharacters(dataSet, this, getActivity());
        rv_list.setAdapter(adapter);

        return fragmentV;
    }


    private void getData() {
        System.out.println("entrado a characters-----------------------");
        dataSet = new ArrayList<>();
        dataSet.add(new Character());
        if(partyCode.equals("")){
            db.collection("characters").document(User.getInstance().getId()).collection(User.getInstance().getUserName()).
                    get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(!task.getResult().isEmpty()){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        dataSet.add(document.toObject(Character.class));

                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            loadingBar(View.INVISIBLE);
                        }
                    });
        }else{

            db.collection("characters").document(User.getInstance().getId()).collection(User.getInstance().getUserName()).
                    whereEqualTo("partyID","").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(!task.getResult().isEmpty()){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        dataSet.add(document.toObject(Character.class));

                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            loadingBar(View.INVISIBLE);
                        }
                    });
        }

    }

    @Override
    public void newCharacter() {
        if(!partyCode.equals("")){
            ((CharacterManagerActivity)getActivity()).selectNewCharacter();
        }else{
            ((CharacterManagerActivity)getActivity()).changeFragment("sheet");
        }
    }

    @Override
    public void selectCharacter(Character character, Bitmap bitmap) {
        if(!partyCode.equals("")){
            System.out.println(character.getID() + "--------ID");
            System.out.println(character.getUserID() + "--------UserID");
            System.out.println(character.getName() + "--------Name");
            character.setPartyID(partyCode);
            db.collection("characters").document(character.getUserID()).collection(User.getInstance().getUserName())
                    .document(character.getID()).set(character).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            db.collection("parties").document(partyCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        Party party = task.getResult().toObject(Party.class);
                                        ArrayList<Character> characters = party.getPlayers();
                                        characters.add(character);
                                        party.setPlayers(characters);
                                        db.collection("parties").document(party.getID()).set(party).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                User.getInstance().getParties().add(partyCode);
                                                db.collection("users").document(User.getInstance().getId()).set(User.getInstance()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        ((CharacterManagerActivity)getActivity()).goWaitingRoom(party.getID());
                                                    }
                                                });

                                            }
                                        });
                                        //volver a la waiting list
                                    }
                                }
                            });
                        }
                    });
        }else{

            ((CharacterManagerActivity)getActivity()).changeLoadingVisibility(View.VISIBLE);
            ((CharacterManagerActivity)getActivity()).seeCharacter(character,bitmap);
            Toast.makeText(getActivity(), "The character is: " + character.getName(), Toast.LENGTH_SHORT).show();
        }

    }

    void loadingBar(int visibility){
        ((CharacterManagerActivity)getActivity()).changeLoadingVisibility(visibility);
    }


    private void createUs(){
        //Party party = new Party("coolding",,"Maio");
        /*db.collection("parties").document(party.getID()).set(party).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(),"dads",Toast.LENGTH_LONG).show();
                    }
                });



        ArrayList<String> partys = new ArrayList<>();
        partys.add(party.getID());
        User.getInstance().setParties(partys);*/
    }


}





   /* public static class MyRunneable implements Runnable{

        private final WeakReference<CharacterListFragment> runFragment;

        public MyRunneable(CharacterListFragment runFragment) {
            this.runFragment = new WeakReference<>(runFragment);
        }

        @Override
        public void run() {
            CharacterListFragment rFragment = runFragment.get();
            if(rFragment != null){
                System.out.println("nosoco null------------");
                //Para cuando acaba de cargar la data que aparezca
                rFragment.getData();
                //quitar lo que sea que tape too
                rFragment.adapter.notifyDataSetChanged();


            }
        }
    }*/
