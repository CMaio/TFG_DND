package com.claudiomaiorana.tfg_dnd.usecases.home;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Party;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.usecases.character.CharacterManagerActivity;
import com.claudiomaiorana.tfg_dnd.usecases.master.MasterManagerActivity;
import com.claudiomaiorana.tfg_dnd.usecases.party.PartyManagerActivity;
import com.claudiomaiorana.tfg_dnd.usecases.user.LoginActivity;
import com.claudiomaiorana.tfg_dnd.util.ApiCallback;
import com.claudiomaiorana.tfg_dnd.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //Components-----------------------
    Button b_logIn;
    Button b_logOut;
    TextView tx_nameUser;
    Button b_characters;
    Button b_parties;
    Button b_master;


    //Firebase-------------------------
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    ActivityResultLauncher<Intent> myActivityResultLauncher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        b_logIn = findViewById(R.id.btn_login);
        b_logOut = findViewById(R.id.btn_logout);
        b_characters = findViewById(R.id.btn_characters);
        b_parties = findViewById(R.id.btn_parties);
        b_master = findViewById(R.id.btn_master);
        tx_nameUser = findViewById(R.id.tx_nameuser);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        myActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent = result.getData();

                if(result.getResultCode() ==  RESULT_OK){
                    String source = intent.getStringExtra("source");
                    switch (source){
                        case "login":
                            tx_nameUser.setText(intent.getStringExtra("user"));
                            break;

                        case "characters":
                            Log.d(TAG, "coming back from characters");
                            break;

                        case "parties":
                            Log.d(TAG, "coming back from parties");
                            break;

                        case "master":
                            Log.d(TAG, "coming back from master");
                            break;
                    }
                } else if (result.getResultCode() ==  RESULT_CANCELED) {
                    String source = intent.getStringExtra("source");
                    switch (source){
                        case "login":
                            tx_nameUser.setText("no user");
                            break;
                        case "characters":
                            Log.d(TAG, "coming back from characters");
                            break;
                        case "parties":
                            Log.d(TAG, "coming back from parties");
                            break;

                        case "master":
                            Log.d(TAG, "coming back from master");
                            break;
                    }

                }
            }
        });


        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            createUserInstance(user);
            tx_nameUser.setText(user.getDisplayName());
        } else {
            gotToLogIn();
        }


        b_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotToLogIn();
            }
        });
        b_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        b_characters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCharacters();
            }
        });

        b_parties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToParties();
            }
        });
        b_master.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMaster();
            }
        });

    }


    private void createUserInstance(FirebaseUser user) {
        User userD = User.getInstance();
        Log.d("user",userD.getUserName() + " " + userD.getId());
        if(userD.getUserName() == null ||userD.getUserName() != user.getDisplayName()){
            db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    User tmp = task.getResult().toObject(User.class);
                    Log.d("user",tmp.getUserName() + " " + tmp.getId());
                    userD.fillUser(tmp.getId(),tmp.getUserName(),tmp.getMail(),tmp.getParties());
                }
            });

        }
    }



    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        tx_nameUser.setText("no user");
    }



    private void gotToLogIn(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        //myActivityResultLauncher.launch(intent);
        finish();
    }

    private void goToCharacters(){
        Intent intent = new Intent(this, CharacterManagerActivity.class);
        myActivityResultLauncher.launch(intent);
    }


    private void goToParties() {
        Intent intent = new Intent(this, PartyManagerActivity.class);
        myActivityResultLauncher.launch(intent);
    }

    private void goToMaster() {
        Intent intent = new Intent(this, MasterManagerActivity.class);
        myActivityResultLauncher.launch(intent);
    }

}