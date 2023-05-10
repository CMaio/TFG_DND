package com.claudiomaiorana.tfg_dnd.util;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class Util {
    //Metodos comunes que puedo usar en todos lados

    private static final String BASE_URL = "https://www.dnd5eapi.co/api/";


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
}
