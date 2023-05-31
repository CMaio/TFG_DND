package com.claudiomaiorana.tfg_dnd.util;

import com.android.volley.VolleyError;
import com.claudiomaiorana.tfg_dnd.model.Party;

import org.json.JSONObject;

public interface FirebaseCallback {
    void getParty(Party party);
    void getCharacter(String partyCode, String userID);
}
