package com.claudiomaiorana.tfg_dnd.usecases.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.claudiomaiorana.tfg_dnd.R;

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
}