package com.claudiomaiorana.tfg_dnd.util;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public interface ApiCallback {
    void onSuccess(JSONObject jsonObject);
    void onError(VolleyError error);
}
