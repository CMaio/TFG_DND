package com.claudiomaiorana.tfg_dnd.usecases.character.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.claudiomaiorana.tfg_dnd.R;

public class SheetRCASelectorFragment extends Fragment {

    private static final String TYPE_SELECTOR = "typeSelector";

    private String selector;

    public SheetRCASelectorFragment() {}

    public static SheetRCASelectorFragment newInstance(String selector) {
        SheetRCASelectorFragment fragment = new SheetRCASelectorFragment();
        Bundle args = new Bundle();
        args.putString(TYPE_SELECTOR,selector);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selector = getArguments().getString(TYPE_SELECTOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentV = inflater.inflate(R.layout.fragment_sheet_rca_selector, container, false);

        return fragmentV;
    }
}