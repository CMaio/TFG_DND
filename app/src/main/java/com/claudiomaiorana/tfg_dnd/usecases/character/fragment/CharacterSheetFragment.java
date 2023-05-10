package com.claudiomaiorana.tfg_dnd.usecases.character.fragment;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.model.RCAInfo;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.usecases.character.CharacterManagerActivity;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterCharacters;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterSkills;
import com.claudiomaiorana.tfg_dnd.util.ApiCallback;
import com.claudiomaiorana.tfg_dnd.util.Constants;
import com.claudiomaiorana.tfg_dnd.util.PopUpCustom;
import com.claudiomaiorana.tfg_dnd.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CharacterSheetFragment extends Fragment{

    ImageView profile_img;
    TextView txt_name, txt_race, txt_class, txt_alignment, txt_gender, txt_pronoun, txt_level;
    Button btn_race, btn_class, btn_alignment, btn_create;

    RCAInfo[] rcaInfoSaved = new RCAInfo[3];
    int[] Stats = {0,0,0,0,0};
    String[] stats;
    int indexStats;
    RequestQueue queue;
    String url = "https://www.dnd5eapi.co/api/";
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    //Custom pop up stats components
    Button btn_Continue;
    TextView txt_text,txt_value;

    //Custom pop up skills
    RecyclerView rv;
    TextView txt_quantitySkills;
    AdapterSkills adapter;
    Button btn_selecSkills;
    String quantityToChoose;

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
        setElements(fragmentV);
        stats = getResources().getStringArray(R.array.statsNames);
        indexStats = 0;

        Bundle bundle = getArguments();
        if (bundle != null) {
            String type = bundle.getString("typaRCA");
            RCAInfo rcaInfo = (RCAInfo) bundle.getSerializable("rcainfo");
            //TODO aqui añadir el valor a la pantalla y tambien añadirlo al character

            if (type != null && rcaInfo != null) {
                switch (type) {
                    case Constants.RACES_SELECTED:
                        rcaInfoSaved[0] = rcaInfo;
                        break;
                    case Constants.CLASS_SELECTED:
                        rcaInfoSaved[1] = rcaInfo;
                        break;
                    case Constants.ALIGNMENT_SELECTED:
                        rcaInfoSaved[2] = rcaInfo;
                        break;
                }
                changeRCAText();
            }
        }


        btn_race.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goSelectorRCA(Constants.RACES_SELECTED);
            }
        });

        btn_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goSelectorRCA(Constants.CLASS_SELECTED);
            }
        });

        btn_alignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goSelectorRCA(Constants.ALIGNMENT_SELECTED);
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexStats = 0;
                String stat = stats[indexStats];
                quantityToChoose = quantityChoose();
                callPopUpStats("CreateCharacter",stat);
            }
        });

        return fragmentV;
    }

    private void changeRCAText() {
        for (int i = 0; i < rcaInfoSaved.length; i++) {
            if (rcaInfoSaved[i] != null) {
                if (i == 0) {
                    txt_race.setText((!rcaInfoSaved[i].getTittleText().equals("")) ? rcaInfoSaved[i].getTittleText() : "Select Race");
                } else if (i == 1) {
                    txt_class.setText((!rcaInfoSaved[i].getTittleText().equals("")) ? rcaInfoSaved[i].getTittleText() : "Select Class");
                } else {
                    txt_alignment.setText((!rcaInfoSaved[i].getTittleText().equals("")) ? rcaInfoSaved[i].getTittleText() : "Select Alignment");
                }
            }

        }
    }

    void goSelectorRCA(String type) {
        ((CharacterManagerActivity) getActivity()).changeToRCA(type);
    }





    void showNextStat(int valueStat){
        if(indexStats+1<stats.length) {
            Stats[indexStats] = valueStat;
            String stat = stats[indexStats+1];
            callPopUpStats("CreateCharacter", stat);
            indexStats++;
        }else{
            System.out.println("hemos terminado " + Stats);

            indexStats = 0;
            //createCharacter();
            callPopUpSkills("skills");
        }
    }





    void apiRequest(String subUrl) {
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url + subUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject firstUser = response;
                            Log.d("API", firstUser.getJSONArray("results").toString());
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

    public void callPopUpStats(String tag,String stat) {
        View view = getLayoutInflater().inflate(R.layout.fragment_character_stats, null);

        btn_Continue = view.findViewById(R.id.btn_continue);
        txt_text = view.findViewById(R.id.txt_statToThrow);
        txt_value = view.findViewById(R.id.txt_numberStat);

        PopUpCustom popUp = new PopUpCustom(view);
        popUp.show(getParentFragmentManager(), tag);
        popUp.setCancelable(false);

        if(!stat.equals("")){
            txt_text.setText(txt_text.getText().toString().replace("@sTat@",stat));
        }
        btn_Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txt_value.getText().toString().equals("")){
                    int tmpValue = Integer.parseInt(txt_value.getText().toString());
                    if(0<tmpValue && tmpValue<21){
                        showNextStat(tmpValue);
                        popUp.dismiss();
                    }else{
                        txt_value.setError(getResources().getString(R.string.errorStat));
                    }
                }else{
                    txt_value.setError(getResources().getString(R.string.errorStat));
                }
            }
        });
    }

    void callPopUpSkills(String tag){
        ArrayList<String> dataSet = new ArrayList<String>();
        View view = getLayoutInflater().inflate(R.layout.fragment_character_skills, null);
        rv = view.findViewById(R.id.rv_skillsSelector);
        txt_quantitySkills = view.findViewById(R.id.txv_skillsToSelect);
        btn_selecSkills = view.findViewById(R.id.btn_continueSkills);

        txt_quantitySkills.setText(getResources().getText(R.string.quantitySkills).toString().replace("@numToCh@",quantityToChoose));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);

        //Create data
        dataSet = generateSkills();
        adapter = new AdapterSkills(dataSet, getActivity());
        rv.setAdapter(adapter);

        PopUpCustom popUp = new PopUpCustom(view);
        popUp.show(getParentFragmentManager(), tag);
        popUp.setCancelable(false);

        btn_selecSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<CheckBox> ckBox = adapter.allChecks();
                for (CheckBox ck:ckBox) {
                    Log.d("skill",ck.getText().toString());
                }
                /*if(!txt_value.getText().toString().equals("")){
                    int tmpValue = Integer.parseInt(txt_value.getText().toString());
                    if(0<tmpValue && tmpValue<21){
                        createCharacter();
                        popUp.dismiss();
                    }else{
                        txt_value.setError(getResources().getString(R.string.errorStat));
                    }
                }else{
                    txt_value.setError(getResources().getString(R.string.errorStat));
                }*/
            }
        });
    }

    private String quantityChoose() {
        final String[] result = {""};
        Util.apiGETRequest("classes/bard/", new ApiCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    result[0] = String.valueOf(jsonObject.getJSONArray("proficiency_choices").
                            getJSONObject(0).getInt("choose"));

                }catch (Exception e){}
            }
            @Override
            public void onError(VolleyError error) {

            }
        },getActivity());
        System.out.println(result[0] + "-------");
        return result[0];

    }

    private ArrayList<String> generateSkills() {
        ArrayList<String> result = new ArrayList<>();
        Util.apiGETRequest("classes/bard/", new ApiCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("proficiency_choices").
                            getJSONObject(0).getJSONObject("from").getJSONArray("options");
                    for (int i = 0;i<jsonArray.length();i++){
                        result.add(jsonArray.getJSONObject(i).getJSONObject("item").getString("name"));
                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e){}
            }

            @Override
            public void onError(VolleyError error) {

            }
        },getActivity());

        return result;
    }
    private ArrayList<String> getSavingThrows(){
        ArrayList<String> result = new ArrayList<>();
        Util.apiGETRequest("classes/bard/", new ApiCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("saving_throws");
                    for (int i = 0;i<jsonArray.length();i++){
                        result.add(jsonArray.getJSONObject(i).getString("index"));
                    }


                }catch (Exception e){}
            }

            @Override
            public void onError(VolleyError error) {

            }
        },getActivity());

        return result;
    }

    private int getTypeOfDice(){
        int[] result = {0};
        Util.apiGETRequest("classes/bard/", new ApiCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    result[0] = jsonObject.getInt("hit_die");
                }catch (Exception e){}
            }
            @Override
            public void onError(VolleyError error) {

            }
        },getActivity());
        return result[0];
    }

    void createCharacter(ArrayList<String> skillsResult, Map<String, String> proficienciesAndLanguagesResult) {
        ArrayList<String> savingThrows = getSavingThrows();
        ArrayList<String> skills = skillsResult;
        Map<String, String> proficienciesAndLanguages = proficienciesAndLanguagesResult;
        int speed = 0;
        int quantityHitDice = 1;
        int typeHitDice = getTypeOfDice();





        Character character = new Character(User.getInstance(),txt_name.getText().toString(),profile_img.getDrawable(),
                rcaInfoSaved,Integer.parseInt(txt_level.getText().toString()),txt_gender.getText().toString(),
                txt_pronoun.getText().toString(),Stats,savingThrows,skills,proficienciesAndLanguages,
                speed,quantityHitDice,typeHitDice);

        saveCharacter(character);


    }

    private void saveCharacter(Character character) {
        db.collection("characters").document(character.getUserID()).collection(User.getInstance().getUserName())
                .document(character.getID()).set(character).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finishCreation();
                    }
                });
    }

    void finishCreation(){

    }


    void setElements(View fragmentV) {
        txt_name = fragmentV.findViewById(R.id.txt_nameSelector);
        txt_race = fragmentV.findViewById(R.id.txv_raceSelected);
        btn_race = fragmentV.findViewById(R.id.btn_raceSelector);
        txt_class = fragmentV.findViewById(R.id.txv_classSelected);
        btn_class = fragmentV.findViewById(R.id.btn_classSelector);
        txt_alignment = fragmentV.findViewById(R.id.txv_alignmentSelected);
        btn_alignment = fragmentV.findViewById(R.id.btn_alignmentSelector);
        btn_create = fragmentV.findViewById(R.id.btn_createCharacter);
        txt_level = fragmentV.findViewById(R.id.txns_levelSelector);
        profile_img = fragmentV.findViewById(R.id.img_character);
        txt_gender = fragmentV.findViewById(R.id.txt_genderSelector);
        txt_pronoun = fragmentV.findViewById(R.id.txt_pronounSelector);
    }
}