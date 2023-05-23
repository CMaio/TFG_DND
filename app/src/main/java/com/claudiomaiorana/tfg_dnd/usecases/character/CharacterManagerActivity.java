package com.claudiomaiorana.tfg_dnd.usecases.character;

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
import com.claudiomaiorana.tfg_dnd.usecases.character.fragment.SheetRCASelectorFragment;
import com.claudiomaiorana.tfg_dnd.util.Constants;

public class CharacterManagerActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> myActivityResultLauncher;

    String state ="";
    private LinearLayout ly;
    String source = "";
    String idParty = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_manager);
        getSupportActionBar().hide();
        state = "listCharacters";
        ly = findViewById(R.id.ly_loading);

        Intent intent = getIntent();
        System.out.println("_________________________________ estamos por pillar party");
        if(intent != null){
            source = intent.getStringExtra("source");
            if(source == null){source ="";}
            idParty = intent.getStringExtra("idParty");
            System.out.println("_________________________________ hemos pillado por pillar party " + idParty);
        }



        myActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() ==  RESULT_OK){

                }
            }
        });
        if(source.equals("party")){
            selectCharacter();
        }else{
            changeFragment(state);
        }
    }

    private void selectCharacter() {
        showFragment("listCharacters",idParty);
    }

    public void selectNewCharacter(){
        showFragment("sheet",idParty);
    }

    public void changeFragment(String state){
        showFragment(state,"");
    }

    public void changeToRCA(String type){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft =  fm.beginTransaction();
        SheetRCASelectorFragment raceClassSelectorFragment = SheetRCASelectorFragment.newInstance(type);
        ft.replace(R.id.Fr_characterManager, raceClassSelectorFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
    private void showFragment(String state,String idParty){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.Fr_characterManager);
        FragmentTransaction ft =  fm.beginTransaction();
        switch (state){
            case "listCharacters":
                if(fragment == null || !(fragment instanceof CharacterListFragment)){
                    CharacterListFragment listFragment = CharacterListFragment.newInstance(idParty);
                    ft.replace(R.id.Fr_characterManager,listFragment,"fr_listFragment");
                }
                break;
            case "sheet":
                if(!(fragment instanceof CharacterSheetFragment)) {
                    CharacterSheetFragment sheetFragment = CharacterSheetFragment.newInstance(idParty);
                    ft.replace(R.id.Fr_characterManager, sheetFragment,"fr_sheetFragment");
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
        intent.putExtra("source","characters");
        setResult(result,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.Fr_characterManager);

        if (currentFragment instanceof CharacterListFragment) {
            backToMainActivity(RESULT_OK);
        }else{
            fragmentManager.popBackStack();
        }
    }


    public void goWaitingRoom(String id) {
        Intent intent = new Intent();
        intent.putExtra("source","characterPartyCreated");
        intent.putExtra("party",id);
        setResult(RESULT_OK,intent);
        finish();
    }



}