package com.claudiomaiorana.tfg_dnd.usecases.party;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.usecases.character.fragment.CharacterListFragment;
import com.claudiomaiorana.tfg_dnd.usecases.character.fragment.CharacterSheetFragment;
import com.claudiomaiorana.tfg_dnd.usecases.character.fragment.CharacterSkillsFragment;
import com.claudiomaiorana.tfg_dnd.usecases.character.fragment.CharacterStatsFragment;
import com.claudiomaiorana.tfg_dnd.usecases.character.fragment.SheetRCASelectorFragment;
import com.claudiomaiorana.tfg_dnd.usecases.party.fragment.PartyListFragment;
import com.claudiomaiorana.tfg_dnd.util.Constants;

public class PartyManagerActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> myActivityResultLauncher;
    String state ="";
    private LinearLayout ly;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_manager);
        getSupportActionBar().hide();
        state="listParty";
        ly = findViewById(R.id.ly_party_loading);
        myActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() ==  RESULT_OK){

                }
            }
        });
        changeFragment(state);
    }

    public void changeFragment(String state){
        showFragment(state);
    }


    private void showFragment(String state){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fr_party_manager);
        FragmentTransaction ft =  fm.beginTransaction();
        switch (state){
            case "listParty":
                if(fragment == null || !(fragment instanceof PartyListFragment)){
                    PartyListFragment listFragment = PartyListFragment.newInstance();
                    ft.replace(R.id.fr_party_manager,listFragment,"fr_listFragment_party");
                }
                break;
        }
        ft.addToBackStack(null);
        ft.commit();
    }

    public void changeLoadingVisibility(int visible){
        ly.setVisibility(visible);
    }

    private void backToMainActivity(int result){
        Intent intent = new Intent();
        intent.putExtra("source","party");
        setResult(result,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.Fr_characterManager);

        if (currentFragment instanceof PartyListFragment) {
            backToMainActivity(RESULT_OK);
        }else{
            fragmentManager.popBackStack();
        }
    }
}