package com.claudiomaiorana.tfg_dnd.usecases.user;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.usecases.home.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button b_goback;
    Button b_goSignUp;
    Button b_SignIn;
    TextView txt_mail;
    TextView txt_pass;
    ActivityResultLauncher<Intent> myActivityResultLauncher;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();


        b_goback=findViewById(R.id.btn_goback);
        b_goSignUp=findViewById(R.id.btn_register);
        b_SignIn=findViewById(R.id.signInAccept);
        txt_mail=findViewById(R.id.usercredentials);
        txt_pass=findViewById(R.id.passcredentials);

        myActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() ==  RESULT_OK){
                    Intent intent = result.getData();
                    txt_mail.setText(intent.getStringExtra("mailRegistered"));
                }
            }
        });



        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            b_goback.setVisibility(View.INVISIBLE);
        }else{

        }



        b_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMainMenu(RESULT_CANCELED);
            }
        });

        b_goSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    FirebaseAuth.getInstance().signOut();
                }
                gotToSignUp();
            }
        });

        b_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn(txt_mail.getText().toString(),txt_pass.getText().toString());
            }
        });
    }


    private void gotToSignUp(){
        Intent intent = new Intent(this, RegisterActivity.class);

        myActivityResultLauncher.launch(intent);
    }

    private void logIn(String email, String pass){
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Successful.", Toast.LENGTH_LONG).show();
                            goMainMenu(RESULT_OK);
                            /*Intent intent = new Intent();
                            intent.putExtra("source","login");
                            intent.putExtra("user",mAuth.getCurrentUser().getDisplayName());
                            setResult(RESULT_OK,intent);
                            startActivity(intent);*/
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void goMainMenu(int intentResult){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("source","login");
        setResult(intentResult,intent);
        startActivity(intent);
    }



}