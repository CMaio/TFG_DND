package com.claudiomaiorana.tfg_dnd.usecases.character.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.model.ProfLang;
import com.claudiomaiorana.tfg_dnd.model.Spells;
import com.claudiomaiorana.tfg_dnd.usecases.character.CharacterManagerActivity;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterAbilities;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterObjects;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterSpells;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterSpellsQuantity;
import com.claudiomaiorana.tfg_dnd.util.ApiCallback;
import com.claudiomaiorana.tfg_dnd.util.PopUpCustom;
import com.claudiomaiorana.tfg_dnd.util.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CharacterSheetVisualizerFragment extends Fragment implements AdapterSpells.OnItemClickListener, AdapterAbilities.OnItemClickListener, AdapterObjects.OnItemClickListener {

    private static final String ARG_PARAM1 = "character";
    private static final String ARG_PARAM2 = "imgCharacter";


    private Character mParam1;
    private Bitmap mParam2;

    private RecyclerView rv_spells,rv_abilities,rv_objects,rv_spQuantity;
    private TextView rtv_name_game,rtv_pronoun_game,rtv_gender_game,rtv_level_game,rtv_race_game,rtv_class_game,txt_gold_game,txt_life_game,txt_sheathed_unsheathed;
    private ImageView img_character_game;
    private Button btn_goHome;


    private AdapterSpellsQuantity adapterSpellsQ;
    private AdapterSpells adapterSpells;
    private AdapterAbilities adapterAbilities;
    private AdapterObjects adapterObjects;


    public CharacterSheetVisualizerFragment() {}


    // TODO: Rename and change types and number of parameters
    public static CharacterSheetVisualizerFragment newInstance(Character param1, Bitmap param2) {
        CharacterSheetVisualizerFragment fragment = new CharacterSheetVisualizerFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putParcelable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getParcelable(ARG_PARAM1);
            mParam2 = getArguments().getParcelable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_character_sheet_visualizer, container, false);
        setElements(v);
        setCharacterScreen();
        RecyclerView.LayoutManager layoutManagerQS = new GridLayoutManager(getContext(),4);
        rv_spQuantity.setLayoutManager(layoutManagerQS);

        RecyclerView.LayoutManager layoutManagerS = new GridLayoutManager(getContext(),2);
        rv_spells.setLayoutManager(layoutManagerS);

        RecyclerView.LayoutManager layoutManagerAb = new GridLayoutManager(getContext(),2);
        rv_abilities.setLayoutManager(layoutManagerAb);

        RecyclerView.LayoutManager layoutManagerO = new GridLayoutManager(getContext(),2);
        rv_objects.setLayoutManager(layoutManagerO);
        AdapterSpells();


        ((CharacterManagerActivity)getActivity()).changeLoadingVisibility(View.INVISIBLE);
        btn_goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return v;
    }

    private void AdapterSpells() {
        Map<String,Integer> spellsToAdd = new HashMap<>(mParam1.getSpells().getSpells());
        spellsToAdd.put("0Cantrips",mParam1.getSpells().getCantrips());
        adapterSpellsQ = new AdapterSpellsQuantity(spellsToAdd, getActivity());
        rv_spQuantity.setAdapter(adapterSpellsQ);

        Map<String,List<Spells.Spell>> tmpSpell = mParam1.getSpells().getSpellsName();
        List<Spells.Spell> spells = new ArrayList<>();
        for(int i=0;i<tmpSpell.size();i++){
            List<Spells.Spell> spellsTMP = tmpSpell.get(Integer.toString(i));
            System.out.println("--------- s"+spellsTMP);
            for(int j = 0;j<spellsTMP.size();j++){
                spells.add(tmpSpell.get(Integer.toString(i)).get(j));
            }
        }
        adapterSpells = new AdapterSpells(spells, getActivity(),this);
        rv_spells.setAdapter(adapterSpells);

        adapterAbilities = new AdapterAbilities(mParam1.getFeaturesAndTraits(), getActivity(),this);
        rv_abilities.setAdapter(adapterAbilities);

        adapterObjects = new AdapterObjects(mParam1.getEquipment(), getActivity(),this);
        rv_objects.setAdapter(adapterObjects);
    }

    private void setCharacterScreen() {
        rtv_name_game.setText(mParam1.getName());
        rtv_pronoun_game.setText(mParam1.getPronoun());
        rtv_gender_game.setText(mParam1.getGender());
        rtv_level_game.setText(Integer.toString(mParam1.getLevel()));
        rtv_race_game.setText(mParam1.getRace());
        rtv_class_game.setText(mParam1.getClassPlayer());
        txt_gold_game.setText(Integer.toString(mParam1.getMoney()));
        txt_life_game.setText(Integer.toString(mParam1.getCurrentHitPoints()));
        if(mParam2 != null){
            Drawable drawable = new BitmapDrawable(getResources(), mParam2);
            Glide.with(getContext())
                    .load(drawable)
                    .into(img_character_game);
        }else{
            System.out.println("-----------EST");
            Glide.with(getContext())
                    .load(R.drawable.avatar_1)
                    .into(img_character_game);
        }




    }





    void setElements(View v){
        rv_spells = v.findViewById(R.id.rv_spells_character);
        rv_abilities = v.findViewById(R.id.rv_abilities_character);
        rv_objects = v.findViewById(R.id.rv_objects_character);
        rv_spQuantity = v.findViewById(R.id.rv_spells_quantity_character);

        rtv_name_game = v.findViewById(R.id.rtv_name_game_character);
        rtv_pronoun_game = v.findViewById(R.id.rtv_pronoun_character);
        rtv_gender_game = v.findViewById(R.id.rtv_gender_character);
        rtv_level_game = v.findViewById(R.id.rtv_level_character);
        rtv_race_game = v.findViewById(R.id.rtv_race_character);
        rtv_class_game = v.findViewById(R.id.rtv_class_character);
        txt_gold_game = v.findViewById(R.id.txt_gold_character);
        txt_life_game = v.findViewById(R.id.txt_life_character);
        txt_sheathed_unsheathed = v.findViewById(R.id.txt_sheathed_unsheathed_character);

        img_character_game = v.findViewById(R.id.img_character_character);

        btn_goHome = v.findViewById(R.id.btn_goHome_character);
    }

    @Override
    public void onItemClick(Spells.Spell spell) {
        Toast.makeText(getContext(), spell.getName(), Toast.LENGTH_LONG).show();
        //View view = getLayoutInflater().inflate(R.layout.fragment_character_stats, null);

//        Util.apiGETRequest("spells/" + spell.getCode(), new ApiCallback() {
//            @Override
//            public void onSuccess(JSONObject jsonObject) {
//                if(jsonObject.has("desc")){
//
//                }
//                PopUpCustom popUp = new PopUpCustom(view,this,spell.getName(),);
//                popUp.show(getParentFragmentManager(), tag);
//            }
//
//            @Override
//            public void onError(VolleyError error) {
//
//            }
//        },getActivity());

        //popUp.setCancelable(false);


    }

    @Override
    public void onItemClick(ProfLang nameTrait) {
        Toast.makeText(getContext(), nameTrait.getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(String nameObject) {
        Toast.makeText(getContext(), nameObject, Toast.LENGTH_LONG).show();
    }
}