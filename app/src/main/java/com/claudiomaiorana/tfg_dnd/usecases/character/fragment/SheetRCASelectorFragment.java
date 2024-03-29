package com.claudiomaiorana.tfg_dnd.usecases.character.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.RCAInfo;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterRCASelector;
import com.claudiomaiorana.tfg_dnd.util.Constants;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SheetRCASelectorFragment extends Fragment implements AdapterRCASelector.OnItemClickListener{

    private static String TYPE_SELECTOR = "typeSelector";

    private String selector;
    private RecyclerView rv_rca;
    private ArrayList<RCAInfo> data = new ArrayList<RCAInfo>();
    private AdapterRCASelector adapter;
    private Button btn_cancel;

    public SheetRCASelectorFragment() {}

    public static SheetRCASelectorFragment newInstance(String selector) {
        SheetRCASelectorFragment fragment = new SheetRCASelectorFragment();
        Bundle args = new Bundle();
        System.out.println(TYPE_SELECTOR +"-----------------aqui ta type");
        System.out.println(selector +"-----------------aqui ta type2");
        args.putString(TYPE_SELECTOR,selector);
        fragment.setArguments(args);
        TYPE_SELECTOR = selector;
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
        rv_rca = fragmentV.findViewById(R.id.rv_rcaselector);
        btn_cancel = fragmentV.findViewById(R.id.cancel_go_back_to_sheet);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        rv_rca.setLayoutManager(layoutManager);
        if(data == null){
            generateData();
        }

        adapter = new AdapterRCASelector(getActivity(),this,data);
        rv_rca.setAdapter(adapter);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new SheetRCASelectorFragment.MyRunneable(this),0);

        return fragmentV;
    }

    private void generateData(){
        String[] tmpArray = null;
        String[] tmpCode=null;

        switch (TYPE_SELECTOR){
            case Constants.RACES_SELECTED:
                tmpArray = getResources().getStringArray(R.array.racesName);
                tmpCode = Constants.TYPE_OF_RACES;
                break;
            case Constants.CLASS_SELECTED:
                tmpArray = getResources().getStringArray(R.array.classNames);
                tmpCode = Constants.TYPE_OF_CLASSES;
                break;
            case Constants.ALIGNMENT_SELECTED:
                tmpArray = getResources().getStringArray(R.array.alignmentsNames);
                tmpCode = Constants.TYPE_OF_ALIGNMENT;
                break;
        }
        RCAInfo tmpRCA = null;
        for(int i = 0;i < tmpArray.length;i++){
            tmpRCA = new RCAInfo(tmpArray[i],"img_profile_"+tmpCode[i],tmpCode[i]);
            data.add(tmpRCA);
        }
        adapter.notifyDataSetChanged();


    }

    @Override
    public void onItemClick(RCAInfo rcaInfo) {
        Bundle bundle = new Bundle();
        bundle.putString("typaRCA",TYPE_SELECTOR);
        bundle.putSerializable("rcainfo",rcaInfo);

        getActivity().getSupportFragmentManager().popBackStack();

        CharacterSheetFragment characterSheetFragment = (CharacterSheetFragment) getActivity().
                getSupportFragmentManager().findFragmentByTag("fr_sheetFragment");
        if(characterSheetFragment != null){
            characterSheetFragment.setArguments(bundle);
        }
    }

    void goBack(){
        getActivity().getSupportFragmentManager().popBackStack();

    }


    public static class MyRunneable implements Runnable{

        private final WeakReference<SheetRCASelectorFragment> runFragment;

        public MyRunneable(SheetRCASelectorFragment runFragment) {
            this.runFragment = new WeakReference<>(runFragment);
        }

        @Override
        public void run() {
            SheetRCASelectorFragment rFragment = runFragment.get();
            if(rFragment != null){
                //Para cuando acaba de cargar la data que aparezca
                rFragment.generateData();
                //quitar lo que sea que tape too


            }
        }
    }



}