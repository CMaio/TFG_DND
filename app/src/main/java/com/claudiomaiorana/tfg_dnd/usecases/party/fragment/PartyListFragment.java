package com.claudiomaiorana.tfg_dnd.usecases.party.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.model.Party;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.usecases.party.PartyManagerActivity;
import com.claudiomaiorana.tfg_dnd.usecases.party.adapters.AdapterParties;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PartyListFragment extends Fragment implements AdapterParties.OnItemClickListener{
    private RecyclerView rv_list;
    private AdapterParties adapter;
    private static ArrayList<Party> dataSet;
    private FirebaseFirestore db;


    public PartyListFragment() {}
    public static PartyListFragment newInstance() {return new PartyListFragment();}
    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_party_list, container, false);
        rv_list = v.findViewById(R.id.rv_partyList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_list.setLayoutManager(layoutManager);
        db = FirebaseFirestore.getInstance();
        //generateCharacter();
        loadingBar(View.VISIBLE);
        getData();

        adapter = new AdapterParties(dataSet,this, getActivity());
        rv_list.setAdapter(adapter);

        return v;
    }


    private void getData() {
        dataSet = new ArrayList<>();
        dataSet.add(new Party());
        db.collection("parties").document(User.getInstance().getId()).collection(User.getInstance().getUserName())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dataSet.add(document.toObject(Party.class));
                            }
                            adapter.notifyDataSetChanged();
                            loadingBar(View.INVISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onItemClick(Character character) {
        ((PartyManagerActivity)getActivity()).changeFragment("newParty");
    }


    void loadingBar(int visibility){
        ((PartyManagerActivity)getActivity()).changeLoadingVisibility(visibility);
    }
}