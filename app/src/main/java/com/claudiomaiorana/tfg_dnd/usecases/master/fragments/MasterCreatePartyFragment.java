package com.claudiomaiorana.tfg_dnd.usecases.master.fragments;

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
import com.claudiomaiorana.tfg_dnd.usecases.master.MasterManagerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class MasterCreatePartyFragment extends Fragment {

    EditText txt_name;
    Button btn_createParty;
    private FirebaseFirestore db;

    public MasterCreatePartyFragment() { }


    public static MasterCreatePartyFragment newInstance() {
        MasterCreatePartyFragment fragment = new MasterCreatePartyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_master_create_party, container, false);
        setElements(v);
        db = FirebaseFirestore.getInstance();

        btn_createParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_name.getText().toString().equals("")){
                    txt_name.setError("Add a name");
                }
                CollectionReference partiesRef = db.collection("parties");
                partiesRef.whereEqualTo("nameParty",txt_name.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            System.out.println("dddd----------------");
                            QuerySnapshot snapshot = task.getResult();
                            if (snapshot != null && !snapshot.isEmpty()) {
                                txt_name.setError("This party name already exist");
                                System.out.println("EEEEEE----------------");

                                // Existe al menos un documento con el mismo nombre en la colección "parties"
                                // Realiza la acción correspondiente aquí
                            } else {
                                // No hay documentos con el mismo nombre en la colección "parties"
                                System.out.println("BIEN----------------");

                                createParty();


                            }
                        } else {
                            // Error al realizar la consulta
                        }
                    }
                });
            }
        });
        return v;
    }

    private void createParty() {
        Party party = new Party(txt_name.getText().toString(), User.getInstance().getId(),User.getInstance().getUserName());
        db.collection("parties").document(party.getID()).set(party).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("PERFECTo----------------");

                ((MasterManagerActivity)getActivity()).changeFragment("waitingParty",party.getID());

            }
        });

    }


    private void setElements(View v) {
        txt_name = v.findViewById(R.id.etx_setNamePartyMaster);
        btn_createParty = v.findViewById(R.id.btn_createPartyMaster);
    }

}