package com.claudiomaiorana.tfg_dnd.usecases.game.fragments;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.model.Party;
import com.claudiomaiorana.tfg_dnd.model.ProfLang;
import com.claudiomaiorana.tfg_dnd.model.Spells;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterAbilities;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterObjects;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterSpells;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterSpellsQuantity;
import com.claudiomaiorana.tfg_dnd.usecases.game.GameplayActivity;
import com.claudiomaiorana.tfg_dnd.usecases.game.adapters.AdapterItemsGame;
import com.claudiomaiorana.tfg_dnd.usecases.game.adapters.AdapterSpellsGame;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameplaySafeFragment extends Fragment implements AdapterSpellsGame.OnItemClickListener, AdapterAbilities.OnItemClickListener, AdapterItemsGame.OnItemClickListener {

    private static final String ARG_PARAM1 = "param1";

    private String partyCode;

    private RecyclerView rv_spells,rv_abilities,rv_objects,rv_spQuantity;
    private TextView rtv_name_game,rtv_pronoun_game,rtv_gender_game,rtv_level_game,rtv_race_game,rtv_class_game,txt_gold_game,txt_life_game;
    private ImageView img_character_game;
    private Button btn_sheathed_unsheathed,btn_goHome;

    Party party;
    Character character;

    private FirebaseFirestore db;
    FirebaseStorage dbStorage = FirebaseStorage.getInstance();

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
        getParty();
        setCharacterScreen();
        setImagePlayer();

        RecyclerView.LayoutManager layoutManagerQS = new GridLayoutManager(getContext(),4);
        rv_spQuantity.setLayoutManager(layoutManagerQS);

        RecyclerView.LayoutManager layoutManagerS = new GridLayoutManager(getContext(),2);
        rv_spells.setLayoutManager(layoutManagerS);

        RecyclerView.LayoutManager layoutManagerAb = new GridLayoutManager(getContext(),2);
        rv_abilities.setLayoutManager(layoutManagerAb);

        RecyclerView.LayoutManager layoutManagerO = new GridLayoutManager(getContext(),2);
        rv_objects.setLayoutManager(layoutManagerO);
        AdapterSpells();

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
        rv_spQuantity = v.findViewById(R.id.rv_spells_quantity_game);


        rtv_name_game = v.findViewById(R.id.rtv_name_game);
        rtv_pronoun_game = v.findViewById(R.id.rtv_pronoun_game);
        rtv_gender_game = v.findViewById(R.id.rtv_gender_game);
        rtv_level_game = v.findViewById(R.id.rtv_level_game);
        rtv_race_game = v.findViewById(R.id.rtv_race_game);
        rtv_class_game = v.findViewById(R.id.rtv_class_game);
        txt_gold_game = v.findViewById(R.id.txt_gold_game);
        txt_life_game = v.findViewById(R.id.txt_life_game);

        img_character_game = v.findViewById(R.id.img_character_game);

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

        adapterAbilities = new AdapterAbilities(character.getFeaturesAndTraits(), getActivity(),this);
        rv_abilities.setAdapter(adapterAbilities);

        adapterItems = new AdapterItemsGame(character.getEquipment(), getActivity(),this);
        rv_objects.setAdapter(adapterItems);
    }


    void loadingBar(int visibility){
        ((GameplayActivity)getActivity()).changeLoadingVisibility(visibility);
    }

    @Override
    public void onItemClick(Spells.Spell spell) {

    }

    @Override
    public void onItemClick(ProfLang nameTrait) {

    }

    @Override
    public void onItemClick(String nameObject) {

    }
}