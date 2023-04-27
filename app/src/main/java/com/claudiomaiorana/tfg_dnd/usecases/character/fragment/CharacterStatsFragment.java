package com.claudiomaiorana.tfg_dnd.usecases.character.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.claudiomaiorana.tfg_dnd.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CharacterStatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterStatsFragment extends Fragment {


    public CharacterStatsFragment() {}

    public static CharacterStatsFragment newInstance() {return new CharacterStatsFragment();}

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentV = inflater.inflate(R.layout.fragment_character_stats, container, false);

        return fragmentV;
    }
}