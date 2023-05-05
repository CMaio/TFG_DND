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

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterCharacters;
import com.claudiomaiorana.tfg_dnd.usecases.character.fragment.CharacterListFragment;
import com.claudiomaiorana.tfg_dnd.usecases.character.fragment.CharacterSheetFragment;
import com.claudiomaiorana.tfg_dnd.usecases.character.fragment.CharacterSkillsFragment;
import com.claudiomaiorana.tfg_dnd.usecases.character.fragment.CharacterStatsFragment;
import com.claudiomaiorana.tfg_dnd.usecases.character.fragment.SheetRCASelectorFragment;
import com.claudiomaiorana.tfg_dnd.util.PopUpCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class CharacterManagerActivity extends AppCompatActivity implements AdapterCharacters.OnItemClickListener {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private User user;
    ActivityResultLauncher<Intent> myActivityResultLauncher;

    String state ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_manager);
        getSupportActionBar().hide();
        user = User.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        state = "listCharacters";

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
        Fragment fragment = fm.findFragmentById(R.id.Fr_characterManager);
        FragmentTransaction ft =  fm.beginTransaction();
        switch (state){
            case "listCharacters":
                if(fragment == null || !(fragment instanceof CharacterListFragment)){
                    CharacterListFragment listFragment = CharacterListFragment.newInstance();
                    ft.replace(R.id.Fr_characterManager,listFragment);
                }
                break;
            case "sheet":
                if(!(fragment instanceof CharacterSheetFragment)) {
                    CharacterSheetFragment sheetFragment = CharacterSheetFragment.newInstance();
                    ft.replace(R.id.Fr_characterManager, sheetFragment);
                }
                break;
            case "rca":
                if(!(fragment instanceof SheetRCASelectorFragment)) {
                    SheetRCASelectorFragment raceClassSelectorFragment = SheetRCASelectorFragment.newInstance("R");
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
        ft.addToBackStack(null).commit();
    }

    @Override
    public void onItemClick(Character character) {

        showFragment("sheet");
    }


    private void backToMainActivity(int result){
        Intent intent = new Intent();
        intent.putExtra("source","login");
        setResult(result,intent);
        finish();
    }

    public void callPopUp(String tag){
        View view = getLayoutInflater().inflate(R.layout.row_new_character,null);

        PopUpCustom popUp = new PopUpCustom(view);
        popUp.show(getSupportFragmentManager(),tag);

    }
}