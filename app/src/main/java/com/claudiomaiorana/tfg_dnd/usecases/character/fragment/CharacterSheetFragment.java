package com.claudiomaiorana.tfg_dnd.usecases.character.fragment;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.usecases.character.CharacterManagerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

public class CharacterSheetFragment extends Fragment {

    Image profile_img;
    TextView txt_name,txt_race,txt_class,txt_alignment,txt_gender,txt_pronoun,txt_level;
    Button btn_race,btn_class,btn_alignment,btn_create;

    RequestQueue queue;
    String url = "https://www.dnd5eapi.co/api/";
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CharacterSheetFragment() {
    }

    public static CharacterSheetFragment newInstance() {
        return new CharacterSheetFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentV = inflater.inflate(R.layout.fragment_character_sheet, container, false);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CharacterManagerActivity) getActivity()).callPopUp("CreateCharacter");
            }
        });

        return fragmentV;
    }

    /*private void createCharacter() {
        Character tmpCharacter = new Character(mAuth.getCurrentUser(),txt_name.getText().toString(),txt_race.getText().toString(),
                txt_class.getText().toString(),0,Integer.parseInt((String) txt_level.getText()),null);

        db.collection("character").document(tmpCharacter.getID()).set(tmpCharacter);


        Intent intent = new Intent();
        intent.putExtra("source","characters");
        setResult(RESULT_CANCELED,intent);
        finish();
    }*/


    void apiRequest(String subUrl){
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url + subUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject firstUser = response;
                            Log.d("API",firstUser.getJSONArray("results").toString());
                        } catch (Exception ex) {
                            Log.d("SwA", "Error parsing json array");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SwA", "Error in request ");
                    }
                }
        );

        queue.add(jsonArrayRequest);
    }


}