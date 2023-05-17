package com.claudiomaiorana.tfg_dnd.usecases.party.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Party;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.usecases.party.PartyManagerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PartyJoinFragment extends Fragment {


    EditText txt_code;
    Button btn_joinParty;
    private FirebaseFirestore db;

    public PartyJoinFragment() {}

    public static PartyJoinFragment newInstance() {return new PartyJoinFragment();}

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_party_join, container, false);
        setElements(v);
        db = FirebaseFirestore.getInstance();
        btn_joinParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_code.getText().toString().equals("") || txt_code.getText().toString() == null){
                    btn_joinParty.setError("Add a code");
                }else{
                    if(correctCode(txt_code.getText().toString())){
                        getParty(txt_code.getText().toString(), new PartyCallback() {
                            @Override
                            public void onPartyLoaded(Party party) {
                                List<String> characters = party.getIDCharacters();
                                characters.add(User.getInstance().getUserName());
                                party.setIDCharacters(characters);
                                db.collection("parties").document(party.getID()).set(party);
                                ((PartyManagerActivity)getActivity()).changeFragment("waitingParty",party.getID());
                            }

                            @Override
                            public void onPartyNotFound() {
                                btn_joinParty.setError("Party doesn't exist");
                            }
                        });

                    }else{
                        btn_joinParty.setError("Party doesn't exist");
                    }

                }


            }
        });
        return v;
    }

    private Boolean correctCode(String codeAdded) {
        ArrayList<String>  actualParties = User.getInstance().getParties();
        for (String code:actualParties) {
            if(codeAdded.equals(code)){
                return false;
            }
        }
        return true;
    }

    public void getParty(String codeAdded, PartyCallback callback) {
        CollectionReference partiesRef = db.collection("parties");
        partiesRef.document(codeAdded).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Party party = document.toObject(Party.class);
                        callback.onPartyLoaded(party);
                    } else {
                        callback.onPartyNotFound();
                    }
                } else {
                    callback.onPartyNotFound();
                }
            }
        });
    }

    private void setElements(View v) {
        txt_code = v.findViewById(R.id.etx_setCodeParty);
        btn_joinParty = v.findViewById(R.id.btn_joinParty);
    }


    public interface PartyCallback {
        void onPartyLoaded(Party party);
        void onPartyNotFound();
    }
}