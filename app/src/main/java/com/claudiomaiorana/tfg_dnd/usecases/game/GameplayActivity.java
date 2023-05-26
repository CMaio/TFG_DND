package com.claudiomaiorana.tfg_dnd.usecases.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.usecases.party.fragment.PartyListFragment;

public class GameplayActivity extends AppCompatActivity {
    private LinearLayout ly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        ly = findViewById(R.id.ly_loading_gameplay);
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
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.Fr_gameplayManager);

        if (currentFragment instanceof PartyListFragment) {
            backToMainActivity(RESULT_OK);
        }else{
            fragmentManager.popBackStack();
        }
    }
}