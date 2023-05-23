package com.claudiomaiorana.tfg_dnd.usecases.game.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.model.Party;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.usecases.game.GameplayActivity;
import com.claudiomaiorana.tfg_dnd.usecases.party.PartyManagerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GameplaySafeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String partyCode;

    private RecyclerView rv_spells,rv_abilities,rv_objects;
    private TextView rtv_name_game,rtv_pronoun_game,rtv_gender_game,rtv_level_game,rtv_race_game,rtv_class_game,txt_gold_game,txt_life_game,txt_numSpells;
    private ImageView img_character_game;
    private Button btn_sheathed_unsheathed,btn_goHome;

    Party party;
    Character character;

    private FirebaseFirestore db;
    FirebaseStorage dbStorage = FirebaseStorage.getInstance();
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
        getParty();
        setCharacterScreen();
        setImagePlayer();

        img_character_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_sheathed_unsheathed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                    ArrayList<Character> characters = party.getPlayers();
                    for (Character charact: characters) {
                        if(charact.getUserID().equals(User.getInstance().getId())){
                            character = charact;
                        }
                    }
                }
            }
        });
    }

    private void setCharacterScreen() {
        rtv_name_game.setText(character.getName());
        rtv_pronoun_game.setText(character.getPronoun());
        rtv_gender_game.setText(character.getGender());
        rtv_level_game.setText(character.getLevel());
        rtv_race_game.setText(character.getRace());
        rtv_class_game.setText(character.getClassPlayer());
        txt_gold_game.setText(character.getMoney());
        txt_life_game.setText(character.getCurrentHitPoints());

    }

    private void setImagePlayer() {
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
    }


    void setElements(View v){
        rv_spells = v.findViewById(R.id.rv_spells);
        rv_abilities = v.findViewById(R.id.rv_abilities);
        rv_objects = v.findViewById(R.id.rv_objects);

        rtv_name_game = v.findViewById(R.id.rtv_name_game);
        rtv_pronoun_game = v.findViewById(R.id.rtv_pronoun_game);
        rtv_gender_game = v.findViewById(R.id.rtv_gender_game);
        rtv_level_game = v.findViewById(R.id.rtv_level_game);
        rtv_race_game = v.findViewById(R.id.rtv_race_game);
        rtv_class_game = v.findViewById(R.id.rtv_class_game);
        txt_gold_game = v.findViewById(R.id.txt_gold_game);
        txt_life_game = v.findViewById(R.id.txt_life_game);
        txt_numSpells = v.findViewById(R.id.txt_numSpells);

        img_character_game = v.findViewById(R.id.img_character_game);

        btn_sheathed_unsheathed = v.findViewById(R.id.btn_sheathed_unsheathed);
        btn_goHome = v.findViewById(R.id.btn_goHome);
    }


    void loadingBar(int visibility){
        ((GameplayActivity)getActivity()).changeLoadingVisibility(visibility);
    }

}