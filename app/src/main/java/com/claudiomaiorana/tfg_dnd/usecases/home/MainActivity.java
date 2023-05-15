package com.claudiomaiorana.tfg_dnd.usecases.home;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.android.volley.VolleyError;
import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.usecases.character.CharacterManagerActivity;
import com.claudiomaiorana.tfg_dnd.usecases.user.LoginActivity;
import com.claudiomaiorana.tfg_dnd.util.ApiCallback;
import com.claudiomaiorana.tfg_dnd.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //Components-----------------------
    Button b_logIn;
    Button b_logOut;
    TextView tx_nameUser;
    Button b_characters;


    //Firebase-------------------------
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    ActivityResultLauncher<Intent> myActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        b_logIn = findViewById(R.id.btn_login);
        b_logOut = findViewById(R.id.btn_logout);
        b_characters = findViewById(R.id.btn_characters);
        tx_nameUser = findViewById(R.id.tx_nameuser);

        mAuth = FirebaseAuth.getInstance();

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

    }

    private void createUserInstance(FirebaseUser user) {
        User userD = User.getInstance();
        if(userD.getUserName() == null ||userD.getUserName() != user.getDisplayName()){
            userD.fillUser(user.getUid(),user.getDisplayName(),user.getEmail());
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


}