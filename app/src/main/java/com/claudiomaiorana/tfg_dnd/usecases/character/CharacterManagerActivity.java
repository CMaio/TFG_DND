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
import android.view.View;
import android.widget.LinearLayout;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterCharacters;
import com.claudiomaiorana.tfg_dnd.usecases.character.fragment.CharacterListFragment;
import com.claudiomaiorana.tfg_dnd.usecases.character.fragment.CharacterSheetFragment;
import com.claudiomaiorana.tfg_dnd.usecases.character.fragment.CharacterSkillsFragment;
import com.claudiomaiorana.tfg_dnd.usecases.character.fragment.CharacterStatsFragment;
import com.claudiomaiorana.tfg_dnd.usecases.character.fragment.SheetRCASelectorFragment;
import com.claudiomaiorana.tfg_dnd.usecases.home.MainActivity;
import com.claudiomaiorana.tfg_dnd.util.Constants;
import com.claudiomaiorana.tfg_dnd.util.PopUpCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class CharacterManagerActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> myActivityResultLauncher;

    String state ="";
    private LinearLayout ly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_manager);
        getSupportActionBar().hide();
        state = "listCharacters";
        ly = findViewById(R.id.ly_loading);

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

    public void changeToRCA(String type){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft =  fm.beginTransaction();
        SheetRCASelectorFragment raceClassSelectorFragment = SheetRCASelectorFragment.newInstance(type);
        ft.replace(R.id.Fr_characterManager, raceClassSelectorFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
    private void showFragment(String state){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.Fr_characterManager);
        FragmentTransaction ft =  fm.beginTransaction();
        switch (state){
            case "listCharacters":
                if(fragment == null || !(fragment instanceof CharacterListFragment)){
                    CharacterListFragment listFragment = CharacterListFragment.newInstance();
                    ft.replace(R.id.Fr_characterManager,listFragment,"fr_listFragment");
                }
                break;
            case "sheet":
                if(!(fragment instanceof CharacterSheetFragment)) {
                    CharacterSheetFragment sheetFragment = CharacterSheetFragment.newInstance();
                    ft.replace(R.id.Fr_characterManager, sheetFragment,"fr_sheetFragment");
                }
                break;
            case "rca":
                if(!(fragment instanceof SheetRCASelectorFragment)) {
                    SheetRCASelectorFragment raceClassSelectorFragment = SheetRCASelectorFragment.newInstance(Constants.RACES_SELECTED);
                    ft.replace(R.id.Fr_characterManager, raceClassSelectorFragment);
                }
                break;
            case "stats":
                if(!(fragment instanceof CharacterStatsFragment)) {
                    CharacterStatsFragment characterStatsFragment = CharacterStatsFragment.newInstance();
                    ft.replace(R.id.Fr_characterManager, characterStatsFragment);
                }
                break;
            case "skills":
                if(!(fragment instanceof CharacterSkillsFragment)) {
                    CharacterSkillsFragment characterSkillsFragment = CharacterSkillsFragment.newInstance();
                    ft.replace(R.id.Fr_characterManager, characterSkillsFragment);
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


}