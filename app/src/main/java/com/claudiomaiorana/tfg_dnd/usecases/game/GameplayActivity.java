package com.claudiomaiorana.tfg_dnd.usecases.game;

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
import com.claudiomaiorana.tfg_dnd.usecases.game.fragments.GameplayAttackOptionsFragment;
import com.claudiomaiorana.tfg_dnd.usecases.game.fragments.GameplayFightFragment;
import com.claudiomaiorana.tfg_dnd.usecases.game.fragments.GameplaySafeFragment;
import com.claudiomaiorana.tfg_dnd.usecases.master.fragments.MasterCreatePartyFragment;
import com.claudiomaiorana.tfg_dnd.usecases.master.fragments.MasterGameplayFragment;
import com.claudiomaiorana.tfg_dnd.usecases.master.fragments.MasterListFragment;
import com.claudiomaiorana.tfg_dnd.usecases.master.fragments.MasterWaitingFragment;
import com.claudiomaiorana.tfg_dnd.usecases.party.fragment.PartyListFragment;

public class GameplayActivity extends AppCompatActivity {
    private LinearLayout ly;
    ActivityResultLauncher<Intent> myActivityResultLauncher;
    String state ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);
        getSupportActionBar().hide();

        ly = findViewById(R.id.ly_loading_gameplay);
        state="gameplaySafe";
        myActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() ==  RESULT_OK){

                }
            }
        });
        changeFragment(state);

    }

    public void changeFragment(String state){showFragment(state,"");}


    private void showFragment(String state,String code){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.Fr_gameplayManager);
        FragmentTransaction ft = fm.beginTransaction();
        switch (state){
            case "gameplaySafe":
                if(fragment == null|| !(fragment instanceof GameplaySafeFragment)){
                    GameplaySafeFragment listFragment = GameplaySafeFragment.newInstance(code);
                    ft.replace(R.id.fr_master_manager,listFragment,"fr_safeGameplay");
                }
                break;
            case "gameplayFight":
                if(fragment == null|| !(fragment instanceof GameplayFightFragment)){
                    GameplayFightFragment listFragment = GameplayFightFragment.newInstance(code,"no");
                    ft.replace(R.id.fr_master_manager,listFragment,"fr_attackGameplay");
                }
                break;
            case "fightOptions":
                if(fragment == null|| !(fragment instanceof GameplayAttackOptionsFragment)){
                    GameplayAttackOptionsFragment listFragment = GameplayAttackOptionsFragment.newInstance(code);
                    ft.replace(R.id.fr_master_manager,listFragment,"fr_optionsAttack");
                }
                break;
        }

        ft.addToBackStack(null);
        ft.commit();
    }

    public void changeLoadingVisibility(int visible){
        ly.setVisibility(visible);
    }




    public void attackDone(String code){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.Fr_gameplayManager);
        FragmentTransaction ft = fm.beginTransaction();
        GameplayFightFragment listFragment = GameplayFightFragment.newInstance(code,"yes");
        ft.replace(R.id.fr_master_manager,listFragment,"fr_attackGameplay");
        ft.addToBackStack(null);
        ft.commit();

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
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.Fr_gameplayManager);

        if (currentFragment instanceof PartyListFragment) {
            backToMainActivity(RESULT_OK);
        }else{
            fragmentManager.popBackStack();
        }
    }

    public void changeToAttack(String code) {
        showFragment("fightOptions",code);
    }
}