package com.claudiomaiorana.tfg_dnd.usecases.master;

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
import com.claudiomaiorana.tfg_dnd.model.Party;
import com.claudiomaiorana.tfg_dnd.usecases.master.fragments.MasterCreatePartyFragment;
import com.claudiomaiorana.tfg_dnd.usecases.master.fragments.MasterGameplayFragment;
import com.claudiomaiorana.tfg_dnd.usecases.master.fragments.MasterListFragment;
import com.claudiomaiorana.tfg_dnd.usecases.master.fragments.MasterWaitingFragment;
import com.claudiomaiorana.tfg_dnd.usecases.party.fragment.PartyListFragment;

public class MasterManagerActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> myActivityResultLauncher;
    String state ="";
    private LinearLayout ly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_manager);
        getSupportActionBar().hide();

        state="listMaster";
        ly = findViewById(R.id.ly_master_loading);
        myActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() ==  RESULT_OK){

                }
            }
        });
        changeFragment(state);
        System.out.println("-+--------------------------");
    }

    public void changeFragment(String state){showFragment(state,"");}
    public void changeFragment(String state,String code){showFragment(state,code);}


    private void showFragment(String state,String code){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fr_master_manager);
        FragmentTransaction ft = fm.beginTransaction();
        switch (state){
            case "listMaster":
                if(fragment == null|| !(fragment instanceof MasterListFragment)){
                    MasterListFragment listFragment = MasterListFragment.newInstance();
                    ft.replace(R.id.fr_master_manager,listFragment,"fr_listMaster");
                }
                break;
            case "newParty":
                if(fragment == null|| !(fragment instanceof MasterCreatePartyFragment)){
                    MasterCreatePartyFragment listFragment = MasterCreatePartyFragment.newInstance();
                    ft.replace(R.id.fr_master_manager,listFragment,"fr_newPartyMaster");
                }
                break;
            case "waitingParty":
                if(fragment == null|| !(fragment instanceof MasterWaitingFragment)){
                    MasterWaitingFragment listFragment = MasterWaitingFragment.newInstance(code);
                    ft.replace(R.id.fr_master_manager,listFragment,"fr_waitingMaster");
                }
                break;
            case "gameplay":
                if(fragment == null|| !(fragment instanceof MasterGameplayFragment)){
                    MasterGameplayFragment listFragment = MasterGameplayFragment.newInstance(code);
                    ft.replace(R.id.fr_master_manager,listFragment,"fr_gameplayMaster");
                }
                break;
        }

        ft.addToBackStack(null);
        ft.commit();
    }

    public void changeLoadingVisibility(int visible){
        ly.setVisibility(visible);
    }


    public void goToPlay(Party party) {
        showFragment("gameplay",party.getID());
    }

    private void backToMainActivity(int result){
        Intent intent = new Intent();
        intent.putExtra("source","master");
        setResult(result,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fr_master_manager);

        if (currentFragment instanceof MasterListFragment) {
            backToMainActivity(RESULT_OK);
        }else{
            fragmentManager.popBackStack();
        }
    }

    public void finishAttack(String code) {

    }
}