package com.claudiomaiorana.tfg_dnd.usecases.character.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterCharacters;
import com.claudiomaiorana.tfg_dnd.util.PopUpCustom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class CharacterListFragment extends Fragment {
    private RecyclerView rv_list;
    private AdapterCharacters adapter;
    private ArrayList<Character> dataSet;
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

        if (dataSet == null) getData();

        System.out.println("hola--------------------"+dataSet);
        adapter = new AdapterCharacters(dataSet, (AdapterCharacters.OnItemClickListener) getActivity(), getActivity());
        rv_list.setAdapter(adapter);

        Handler handler = new Handler();
        handler.postDelayed(new CharacterListFragment.MyRunneable(this),0);

        return fragmentV;
    }
/*
    private void generateCharacter() {
        Character tmpCharacter = new Character(User.getInstance(), "Pablo", "Male", "He/Him", "Tiefling", "Wizard", "LAWFUL_GOOD", 20);
        db.collection("characters").document("XzdxPSp9IWT3SK9KqBe").collection("maio").document(tmpCharacter.getID()).set(tmpCharacter).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("aqui llego*---------------------------");

            }
        });
    }
*/

    private void getData() {
        dataSet = new ArrayList<>();
        dataSet.add(new Character());
        db.collection("characters").document("XzdxPSp9IWT3SK9KqBe").collection("maio")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dataSet.add(document.toObject(Character.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

   /* public void callPopUp(){
        View view = getLayoutInflater().inflate(R.layout.row_new_character,null);

        PopUpCustom popUp = new PopUpCustom();
        popUp.show(getParentFragmentManager(),"identificarpopup");

    }*/

    public static class MyRunneable implements Runnable{

        private final WeakReference<CharacterListFragment> runFragment;

        public MyRunneable(CharacterListFragment runFragment) {
            this.runFragment = new WeakReference<>(runFragment);
        }

        @Override
        public void run() {
            CharacterListFragment rFragment = runFragment.get();
            if(rFragment != null){
                //Para cuando acaba de cargar la data que aparezca
                rFragment.getData();
                //quitar lo que sea que tape too


            }
        }
    }

}

