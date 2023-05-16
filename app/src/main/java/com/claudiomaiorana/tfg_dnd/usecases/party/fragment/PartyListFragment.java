package com.claudiomaiorana.tfg_dnd.usecases.party.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.claudiomaiorana.tfg_dnd.R;

public class PartyListFragment extends Fragment {


    public PartyListFragment() {}
    public static PartyListFragment newInstance() {return new PartyListFragment();}
    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_party_list, container, false);

        return v;
    }
}