package com.claudiomaiorana.tfg_dnd.usecases.character.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.usecases.character.CharacterManagerActivity;


public class CharacterSheetVisualizerFragment extends Fragment {

    private static final String ARG_PARAM1 = "character";
    private static final String ARG_PARAM2 = "imgCharacter";


    private Character mParam1;
    private Bitmap mParam2;

    private RecyclerView rv_spells,rv_abilities,rv_objects;
    private TextView rtv_name_game,rtv_pronoun_game,rtv_gender_game,rtv_level_game,rtv_race_game,rtv_class_game,txt_gold_game,txt_life_game,txt_numSpells,txt_sheathed_unsheathed;
    private ImageView img_character_game;
    private Button btn_goHome;

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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_character_sheet_visualizer, container, false);
        setElements(v);
        setCharacterScreen();

        ((CharacterManagerActivity)getActivity()).changeLoadingVisibility(View.INVISIBLE);
        btn_goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return v;
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

        rtv_name_game = v.findViewById(R.id.rtv_name_game_character);
        rtv_pronoun_game = v.findViewById(R.id.rtv_pronoun_character);
        rtv_gender_game = v.findViewById(R.id.rtv_gender_character);
        rtv_level_game = v.findViewById(R.id.rtv_level_character);
        rtv_race_game = v.findViewById(R.id.rtv_race_character);
        rtv_class_game = v.findViewById(R.id.rtv_class_character);
        txt_gold_game = v.findViewById(R.id.txt_gold_character);
        txt_life_game = v.findViewById(R.id.txt_life_character);
        txt_numSpells = v.findViewById(R.id.txt_numSpells_character);
        txt_sheathed_unsheathed = v.findViewById(R.id.txt_sheathed_unsheathed_character);

        img_character_game = v.findViewById(R.id.img_character_character);

        btn_goHome = v.findViewById(R.id.btn_goHome_character);
    }
}