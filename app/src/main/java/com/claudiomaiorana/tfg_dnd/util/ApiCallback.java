package com.claudiomaiorana.tfg_dnd.util;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ApiCallback {
    public abstract void onSuccess(JSONObject jsonObject);
    public abstract void onError(VolleyError error);
}
