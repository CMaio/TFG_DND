package com.claudiomaiorana.tfg_dnd.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.model.Party;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.ArrayList;


public class Util {
    //Metodos comunes que puedo usar en todos lados

    private static final String BASE_URL = "https://www.dnd5eapi.co/api/";
    private FirebaseFirestore db;



    public static void apiGETRequest(String subUrl, final ApiCallback callback, FragmentActivity activity) {
        String url = BASE_URL + subUrl;

        JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }
        );

        Volley.newRequestQueue(activity).add(jsonObjectRequest);
    }

/*
    public static void firebaseGETParty(String partyCode, final FirebaseCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("parties").document(partyCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                callback.getParty(task.getResult().toObject(Party.class));
            }
        });
    }


    private Character getCharacter(String party,String userId){
        return null;
    }


 */
}
