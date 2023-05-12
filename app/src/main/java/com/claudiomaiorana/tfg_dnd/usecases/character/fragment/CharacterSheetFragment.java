package com.claudiomaiorana.tfg_dnd.usecases.character.fragment;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.model.ProfLang;
import com.claudiomaiorana.tfg_dnd.model.RCAInfo;
import com.claudiomaiorana.tfg_dnd.model.Skill;
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
import java.util.List;
import java.util.Map;

public class CharacterSheetFragment extends Fragment {

    ImageView profile_img;
    TextView txt_name, txt_race, txt_class, txt_alignment, txt_level;
    Button btn_race, btn_class, btn_alignment, btn_create;
    Spinner spn_gender,spn_pronoun;

    RCAInfo[] rcaInfoSaved = new RCAInfo[3];
    List<Integer> Stats =new ArrayList<>();
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
    Button btn_selectSkills;
    String quantityToChoose="0";
    int typeDice = 0;
    ArrayList<Skill> dataSetSkills = new ArrayList<>();
    ArrayList<String> dataSavingThrows = new ArrayList<>();
    ArrayList<ProfLang> proficienciesAndLanguages = new ArrayList<>();
    ArrayList<ProfLang> dataLanguages = new ArrayList<>();
    String quantityLangToChoose="0";

    //Spinner selecteds
    String genderSelected = "";
    String pronounSelected = "";

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
        ArrayList<String> arrayList = new ArrayList<>();
        for (String gender:getResources().getStringArray(R.array.genders)) {
            arrayList.add(gender);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_gender.setAdapter(arrayAdapter);
        spn_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderSelected = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        ArrayList<String> pronounList = new ArrayList<>();
        for (String gender:getResources().getStringArray(R.array.pronouns)) {
            pronounList.add(gender);
        }
        ArrayAdapter<String> arrayAdapterPronoun = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, pronounList);
        arrayAdapterPronoun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_pronoun.setAdapter(arrayAdapterPronoun);
        spn_pronoun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pronounSelected = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            String type = bundle.getString("typaRCA");
            RCAInfo rcaInfo = (RCAInfo) bundle.getSerializable("rcainfo");

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
            Stats.add(valueStat);
            String stat = stats[indexStats+1];
            callPopUpStats("CreateCharacter", stat);
            indexStats++;
        }else{
            System.out.println("hemos terminado " + Stats);

            indexStats = 0;
            //createCharacter();
            //callPopUpSkills("skills");
            loadingBar(View.VISIBLE);
            quantityChoose();
        }
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

    void prepareSkillPopUp(){

    }

    void loadingBar(int visibility){
        ((CharacterManagerActivity)getActivity()).changeLoadingVisibility(visibility);
    }



    void callPopUpSkills(String tag){

        View view = getLayoutInflater().inflate(R.layout.fragment_character_skills, null);
        rv = view.findViewById(R.id.rv_skillsSelector);
        txt_quantitySkills = view.findViewById(R.id.txv_skillsToSelect);
        btn_selectSkills = view.findViewById(R.id.btn_continueSkills);

        txt_quantitySkills.setText(getResources().getText(R.string.quantitySkills).toString().replace("@numToCh@",quantityToChoose));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        rv.setLayoutManager(layoutManager);
        //Create data

        adapter = new AdapterSkills(dataSetSkills, getActivity());
        rv.setAdapter(adapter);
        loadingBar(View.INVISIBLE);
        PopUpCustom popUp = new PopUpCustom(view);
        popUp.show(getParentFragmentManager(), tag);
        popUp.setCancelable(false);

        btn_selectSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_selectSkills.setError(null);
                ArrayList<Skill> tmpData = adapter.getData();
                int quantity = 0;
                for (Skill sk: tmpData) {
                    if(sk.isSelected()){
                        Log.d("Skill selected",sk.getName());
                        quantity ++;
                    }
                }
                if(quantity>Integer.parseInt(quantityToChoose)){
                    btn_selectSkills.setError("Solo puedes elegir "+quantityToChoose);
                } else if (quantity==Integer.parseInt(quantityToChoose)) {

                    createCharacter(tmpData);

                }
            }
        });
    }

    void callPopUpLanguages(String tag){
        //TODO:Acabar lenguages
        View view = getLayoutInflater().inflate(R.layout.fragment_character_skills, null);
        rv = view.findViewById(R.id.rv_skillsSelector);
        txt_quantitySkills = view.findViewById(R.id.txv_skillsToSelect);
        btn_selectSkills = view.findViewById(R.id.btn_continueSkills);

        txt_quantitySkills.setText(getResources().getText(R.string.quantityLanguage).toString().replace("@numToCh@",quantityToChoose));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        rv.setLayoutManager(layoutManager);
        //Create data

        adapter = new AdapterSkills(dataSetSkills, getActivity());
        rv.setAdapter(adapter);
        loadingBar(View.INVISIBLE);
        PopUpCustom popUp = new PopUpCustom(view);
        popUp.show(getParentFragmentManager(), tag);
        popUp.setCancelable(false);

        btn_selectSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_selectSkills.setError(null);
                ArrayList<Skill> tmpData = adapter.getData();
                int quantity = 0;
                for (Skill sk: tmpData) {
                    if(sk.isSelected()){
                        Log.d("Skill selected",sk.getName());
                        quantity ++;
                    }
                }
                if(quantity>Integer.parseInt(quantityLangToChoose)){
                    btn_selectSkills.setError("Solo puedes elegir "+quantityToChoose);
                } else if (quantity==Integer.parseInt(quantityToChoose)) {

                    createCharacter(tmpData);

                }
            }
        });
    }

    private void quantityChoose() {
        Util.apiGETRequest("classes/"+rcaInfoSaved[1].getCodeApiSearch(), new ApiCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    dataSetSkills = new ArrayList<>();
                    dataSavingThrows = new ArrayList<>();
                    quantityToChoose = String.valueOf(jsonObject.getJSONArray("proficiency_choices").
                            getJSONObject(0).getInt("choose"));


                    JSONArray jsonArray = jsonObject.getJSONArray("proficiency_choices").
                            getJSONObject(0).getJSONObject("from").getJSONArray("options");

                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i).getJSONObject("item");
                        String nameResult=getNameSkill(jsonObject1.getString("name"));
                        Skill tmpResult = new Skill(jsonObject1.getString("index"),nameResult);
                        dataSetSkills.add(tmpResult);
                    }

                    JSONArray arraySaving_Throws = jsonObject.getJSONArray("saving_throws");
                    for (int i = 0;i<arraySaving_Throws.length();i++){
                        dataSavingThrows.add(arraySaving_Throws.getJSONObject(i).getString("index"));
                    }

                    typeDice = jsonObject.getInt("hit_die");


                    callPopUpSkills("skills");

                }catch (Exception e){}
            }
            @Override
            public void onError(VolleyError error) {

            }
        },getActivity());
    }

    private String getNameSkill(String name) {
        String[] nameSkills = getResources().getStringArray(R.array.generalSkills);
        String[] namesGeneralSkills = getResources().getStringArray(R.array.nameGeneralSkills);
        String skillToFind = name.substring(name.indexOf(": ")+2);

        for(int i=0;i<nameSkills.length;i++){
            if(nameSkills[i].contains(skillToFind)){
                return namesGeneralSkills[i];
            }
        }
        return "";
    }

    private String getNameLanguage(String name) {
        String[] nameSkills = getResources().getStringArray(R.array.generalLanguages);
        String[] namesGeneralSkills = getResources().getStringArray(R.array.nameGeneralLanguages);
        //String skillToFind = name.substring(name.indexOf(": ")+2);

        for(int i=0;i<nameSkills.length;i++){
            if(nameSkills[i].contains(name)){
                return namesGeneralSkills[i];
            }
        }
        return "";
    }

    void getLanguages(){
        Util.apiGETRequest("classes/"+rcaInfoSaved[0].getCodeApiSearch(), new ApiCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray jsonArrayLang = jsonObject.getJSONArray("languages");
                    for (int i = 0;i<jsonArrayLang.length();i++) {
                        JSONObject jsonObject1 = jsonArrayLang.getJSONObject(i);
                        String nameResult = getNameLanguage(jsonObject1.getString("name"));
                        ProfLang tmpResult = new ProfLang(jsonObject1.getString("index"),nameResult);
                        proficienciesAndLanguages.add(tmpResult);

                    }
                    if(jsonObject.has("language_options")){
                        dataLanguages = new ArrayList<>();
                        JSONObject jsonObjectLang= jsonObject.getJSONObject("language_options");
                        JSONArray jsonArray = jsonObjectLang.getJSONObject("from").getJSONArray("options");
                        for (int i = 0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i).getJSONObject("item");
                            String nameResult = getNameLanguage(jsonObject1.getString("name"));
                            ProfLang tmpResult = new ProfLang(jsonObject1.getString("index"),nameResult);
                            dataLanguages.add(tmpResult);
                        }
                        callPopUpLanguages("lang");
                    }

                }catch (Exception e){}
            }
            @Override
            public void onError(VolleyError error) {

            }
        },getActivity());
    }






    void createCharacter(ArrayList<Skill> skillsResult) {
        ArrayList<Skill> skills = skillsResult;
        int speed = 0;
        int quantityHitDice = 1;
        int typeHitDice = typeDice;


        Character character = new Character(User.getInstance(),txt_name.getText().toString(),profile_img.getDrawable(),
                rcaInfoSaved,Integer.parseInt(txt_level.getText().toString()),genderSelected,
                pronounSelected,Stats,dataSavingThrows,skills,proficienciesAndLanguages,
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
        //goBackTocharacterList
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
        spn_gender = fragmentV.findViewById(R.id.txt_genderSelector);
        spn_pronoun = fragmentV.findViewById(R.id.txt_pronounSelector);
    }
}