package com.claudiomaiorana.tfg_dnd.usecases.character.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.usecases.character.CharacterManagerActivity;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterCharacters;
import com.claudiomaiorana.tfg_dnd.usecases.home.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


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

       /* Handler handler = new Handler();
        handler.postDelayed(new CharacterListFragment.MyRunneable(this),100);*/

        return fragmentV;
    }



    /*private void generateCharacter() {
        RCAInfo[] rca = new RCAInfo[3];
        rca[0] = new RCAInfo("Human",null,"human");
        rca[1] = new RCAInfo("Bard",null,"bard");
        rca[2] = new RCAInfo("hi",null,"hi");
        //Integer[] stats = {0,0,0,0,0,0};
        List<Integer> stats = new ArrayList<>();
        stats.add(0);
        stats.add(0);
        stats.add(0);
        stats.add(0);
        stats.add(0);
        stats.add(0);
        Character tmpCharacter = new Character(User.getInstance(),"Pablo",null,rca,1,"male","him",stats,null,null,null,0,1,8);
        db.collection("characters").document(User.getInstance().getId()).collection(User.getInstance().getUserName()).document(tmpCharacter.getID()).set(tmpCharacter).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("aqui llego*---------------------------");

            }
        });
    }*/


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
