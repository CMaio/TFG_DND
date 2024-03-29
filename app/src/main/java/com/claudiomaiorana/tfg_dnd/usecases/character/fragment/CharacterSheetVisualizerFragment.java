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
import com.claudiomaiorana.tfg_dnd.model.Item;
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


public class CharacterSheetVisualizerFragment extends Fragment implements AdapterSpells.OnItemClickListener, AdapterObjects.OnItemClickListener {

    private static final String ARG_PARAM1 = "character";
    private static final String ARG_PARAM2 = "imgCharacter";


    private Character mParam1;
    private Bitmap mParam2;

    private RecyclerView rv_spells,rv_abilities,rv_objects,rv_spQuantity;
    private TextView rtv_name_game,rtv_pronoun_game,rtv_gender_game,rtv_level_game,rtv_race_game,rtv_class_game,txt_life_game;
    private ImageView img_character_game;
    private Button btn_goHome,btn_gold_game;


    private AdapterSpellsQuantity adapterSpellsQ;
    private AdapterSpells adapterSpells;
    private AdapterAbilities adapterAbilities;
    private AdapterObjects adapterObjects;


    public CharacterSheetVisualizerFragment() {}



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

        btn_gold_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPopUpGold();
            }
        });

        btn_goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackHome();
            }
        });
        return v;
    }

    private void goBackHome() {
        getParentFragmentManager().popBackStack();
    }

    private void openPopUpGold() {
        Button btn_okay;
        TextView showGold;
        View v = getLayoutInflater().inflate(R.layout.popup_game_player_show_gold, null);

        btn_okay = v.findViewById(R.id.btn_okayMoney);
        showGold = v.findViewById(R.id.txt_showMoney);
        String actualMoney = "pp:" + mParam1.getMoneyPlatinum() + ", gp:"+ mParam1.getMoneyGold() + ", sp:"+ mParam1.getMoneySilver() + ", cp:"+mParam1.getMoneyCopper();
        showGold.setText(actualMoney);

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


    private void AdapterSpells() {
        Map<String,Integer> spellsToAdd = new HashMap<>(mParam1.getSpells().getSpells());
        spellsToAdd.put("0Cantrips",mParam1.getSpells().getCantrips());
        if(mParam1.getSpells().getSpells_known() > 0){
            spellsToAdd.put("0Spells Known",mParam1.getSpells().getSpells_known());
        }
        adapterSpellsQ = new AdapterSpellsQuantity(spellsToAdd, getActivity());
        rv_spQuantity.setAdapter(adapterSpellsQ);

        Map<String,List<Spells.Spell>> tmpSpell = mParam1.getSpells().getSpellsName();
        List<Spells.Spell> spells = new ArrayList<>();
        for(int i=0;i<tmpSpell.size();i++){
            List<Spells.Spell> spellsTMP = tmpSpell.get(Integer.toString(i));
            for(int j = 0;j<spellsTMP.size();j++){
                spells.add(tmpSpell.get(Integer.toString(i)).get(j));
            }
        }
        adapterSpells = new AdapterSpells(spells, getActivity(),this);
        rv_spells.setAdapter(adapterSpells);

        adapterAbilities = new AdapterAbilities(mParam1.getFeaturesAndTraits(), getActivity());
        rv_abilities.setAdapter(adapterAbilities);

        adapterObjects = new AdapterObjects(mParam1.getItems(), getActivity(),this);
        rv_objects.setAdapter(adapterObjects);
    }

    private void setCharacterScreen() {
        rtv_name_game.setText(mParam1.getName());
        rtv_pronoun_game.setText(mParam1.getPronoun());
        rtv_gender_game.setText(mParam1.getGender());
        rtv_level_game.setText(Integer.toString(mParam1.getLevel()));
        rtv_race_game.setText(mParam1.getRace());
        rtv_class_game.setText(mParam1.getClassPlayer());
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
        btn_gold_game = v.findViewById(R.id.btn_gold_character);
        txt_life_game = v.findViewById(R.id.txt_life_character);

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
    public void onItemClick(Item nameObject) {
        Toast.makeText(getContext(), nameObject.getName(), Toast.LENGTH_LONG).show();
    }
}