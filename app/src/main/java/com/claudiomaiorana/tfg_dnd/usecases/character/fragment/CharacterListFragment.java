package com.claudiomaiorana.tfg_dnd.usecases.character.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Party;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.usecases.character.CharacterManagerActivity;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterCharacters;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class CharacterListFragment extends Fragment implements AdapterCharacters.OnItemClickListener{
    private RecyclerView rv_list;
    private AdapterCharacters adapter;
    private static ArrayList<Character> dataSet;
    private FirebaseFirestore db;


    public CharacterListFragment() {}

    public static CharacterListFragment newInstance() {
        return new CharacterListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View fragmentV = inflater.inflate(R.layout.fragment_character_list, container, false);
        rv_list = fragmentV.findViewById(R.id.RV_listCharacters);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_list.setLayoutManager(layoutManager);
        db = FirebaseFirestore.getInstance();
        //generateCharacter();
        loadingBar(View.VISIBLE);
        getData();

        System.out.println("hola--------------------"+dataSet);
        adapter = new AdapterCharacters(dataSet, this, getActivity());
        rv_list.setAdapter(adapter);
        creatUs();
       /* Handler handler = new Handler();
        handler.postDelayed(new CharacterListFragment.MyRunneable(this),100);*/

        return fragmentV;
    }


    private void getData() {
        dataSet = new ArrayList<>();
        dataSet.add(new Character());
        db.collection("characters").document(User.getInstance().getId()).collection(User.getInstance().getUserName())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dataSet.add(document.toObject(Character.class));

                            }
                            adapter.notifyDataSetChanged();
                            loadingBar(View.INVISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onItemClick(Character character) {
        ((CharacterManagerActivity)getActivity()).changeFragment("sheet");
    }

    void loadingBar(int visibility){
        ((CharacterManagerActivity)getActivity()).changeLoadingVisibility(visibility);
    }


    private void creatUs(){
        Party party = new Party("coolding","Maio");
        db.collection("parties").document(party.getID()).set(party).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(),"dads",Toast.LENGTH_LONG).show();
                    }
                });



        ArrayList<String> partys = new ArrayList<>();
        partys.add(party.getID());
        User.getInstance().setParties(partys);
    }


}





   /* public static class MyRunneable implements Runnable{

        private final WeakReference<CharacterListFragment> runFragment;

        public MyRunneable(CharacterListFragment runFragment) {
            this.runFragment = new WeakReference<>(runFragment);
        }

        @Override
        public void run() {
            CharacterListFragment rFragment = runFragment.get();
            if(rFragment != null){
                System.out.println("nosoco null------------");
                //Para cuando acaba de cargar la data que aparezca
                rFragment.getData();
                //quitar lo que sea que tape too
                rFragment.adapter.notifyDataSetChanged();


            }
        }
    }*/
