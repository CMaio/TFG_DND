package com.claudiomaiorana.tfg_dnd.usecases.character.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.model.OptionsCharacter;
import com.claudiomaiorana.tfg_dnd.model.Party;
import com.claudiomaiorana.tfg_dnd.model.ProfLang;
import com.claudiomaiorana.tfg_dnd.model.RCAInfo;
import com.claudiomaiorana.tfg_dnd.model.Skill;
import com.claudiomaiorana.tfg_dnd.model.Spells;
import com.claudiomaiorana.tfg_dnd.model.User;
import com.claudiomaiorana.tfg_dnd.usecases.character.CharacterManagerActivity;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterCharacters;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterSkills;
import com.claudiomaiorana.tfg_dnd.util.ApiCallback;
import com.claudiomaiorana.tfg_dnd.util.Constants;
import com.claudiomaiorana.tfg_dnd.util.PopUpCustom;
import com.claudiomaiorana.tfg_dnd.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage dbStorage = FirebaseStorage.getInstance();



    //Info needed from Race of Player
    String quantity_ProficiencyChoices_Race;
    ArrayList<OptionsCharacter> dataSetProficiencyChoicesRace = new ArrayList<>();
    String quantity_abilityBonusChoices;
    ArrayList<OptionsCharacter> dataSetAbilityBonus = new ArrayList<>();
    String quantity_languagesChoices;
    ArrayList<OptionsCharacter> dataLanguagesChoices = new ArrayList<>();
    ArrayList<ProfLang> dataTraitsChoices = new ArrayList<>();


    //Info needed from Class of Player
    String quantity_ProficiencyChoices_Class;
    ArrayList<OptionsCharacter> dataSetSkills = new ArrayList<>();
    ArrayList<Skill> finalSkills = new ArrayList<>();
    ArrayList<String> dataSavingThrows = new ArrayList<>();


    //Info needed in both Race and Class
    ArrayList<ProfLang> proficienciesAndLanguages = new ArrayList<>();
    ArrayList<Skill> allSkillsPlayer = new ArrayList<>();


    //Info needed for spells
    JSONArray allSpellsClass = null;
    ArrayList<JSONArray> allSpellsByLevel = new ArrayList<>();
    ArrayList<Integer> spellsLevelQuantity = new ArrayList<>();
    Spells spells;
    int spellsKnownQuantity;
    String spellCastingAbility = "";



    //Custom pop up stats components
    Button btn_Continue;
    TextView txt_text,txt_value;

    //Custom pop up skills
    RecyclerView rv;
    TextView txt_quantitySkills;
    AdapterSkills adapter;
    Button btn_selectSkills;

    int typeDice = 0;

    int speed = 0;

    //Spinner selecteds
    String genderSelected = "";
    String pronounSelected = "";


    private static  String PARTY_CODE ="";
    String partyCode="";
    Uri urlImg = Uri.parse("");;
    ActivityResultLauncher<Intent> activityResultLauncher;

    public CharacterSheetFragment() {}

    public static CharacterSheetFragment newInstance(String idParty ) {
        CharacterSheetFragment fragment = new CharacterSheetFragment();
        Bundle args = new Bundle();
        args.putString(PARTY_CODE, idParty);
        fragment.setArguments(args);


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            partyCode = getArguments().getString(PARTY_CODE);
            if(partyCode == null){partyCode = "";}
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentV = inflater.inflate(R.layout.fragment_character_sheet, container, false);
        setElements(fragmentV);
        generateSpinners();

        stats = getResources().getStringArray(R.array.statsNames);
        indexStats = 0;
        spells = new Spells();

        Bundle bundle = getArguments();
        if (bundle != null && bundle.getString("typaRCA") != null) {
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
        if(!partyCode.equals("")){
            System.out.println("-------------------------------------- " + partyCode);
        }


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Bundle extras = result.getData().getExtras();
                            System.out.println(result.getData().getData() + "-----------------URI");
                            //Drawable imageBitmap = (Drawable) extras.get("data");
                            urlImg = result.getData().getData();
                            System.out.println(urlImg + "-----------------URI");
                            Glide.with(getContext())
                                    .load(urlImg)
                                    .into(profile_img);
                            //profile_img.setImageBitmap(imageBitmap);
                        }
                    }
        });


        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takeFotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(takeFotoIntent);
            }
        });


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
                if(checkAlldataCorrect()){

                    indexStats = 0;
                    String stat = stats[indexStats];

                    callPopUpStats("CreateCharacter",stat);
                }
            }
        });

        return fragmentV;
    }

    private boolean checkAlldataCorrect() {
        if(txt_name.getText().toString().equals("")){
            txt_name.setError(getResources().getString(R.string.errorNoNameCharacter));
            return false;
        }else if(txt_name.getText().toString().length()>10){
            txt_name.setError(getResources().getString(R.string.errorNameToLongCharacter));
            return false;
        }

        if(txt_race.getText().toString().equals("")){
            txt_race.setError(getResources().getString(R.string.RCAnoChoosed));
            return false;
        }else if(txt_class.getText().toString().equals("")){
            txt_class.setError(getResources().getString(R.string.RCAnoChoosed));
            return false;
        }else if(txt_alignment.getText().toString().equals("")){
            txt_alignment.setError(getResources().getString(R.string.RCAnoChoosed));
            return false;
        }

        if(txt_level.getText().toString().equals("")){
            txt_level.setError(getResources().getString(R.string.errorNoLevelCharacter));
            return false;
        }

        if(!(0< Integer.parseInt(txt_level.getText().toString()) && Integer.parseInt(txt_level.getText().toString())<21)){
            txt_level.setError(getResources().getString(R.string.errorNoCorrectLevelCharacter));
            return false;
        }



        if(genderSelected.equals("")){
            btn_create.setError(getResources().getString(R.string.errorNoGenderCharacter));
            return false;
        }
        if(pronounSelected.equals("")){
            btn_create.setError(getResources().getString(R.string.errorNoPronounCharacter));
            return false;
        }

        return true;
    }

    private void generateSpinners(){
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
                if(parent.getItemAtPosition(position).toString().equals("Select gender...")){
                    genderSelected="";
                    System.out.println("---------------------SelectGender");
                }else{
                    genderSelected = parent.getItemAtPosition(position).toString();
                    System.out.println("---------------------SelectGender + " +genderSelected );
                }

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        ArrayList<String> pronounList = new ArrayList<>();
        for (String gender : getResources().getStringArray(R.array.pronouns)) {
            pronounList.add(gender);
        }
        ArrayAdapter<String> arrayAdapterPronoun = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, pronounList);
        arrayAdapterPronoun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_pronoun.setAdapter(arrayAdapterPronoun);
        spn_pronoun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).toString().equals("Select pronoun...")){
                    pronounSelected="";
                }else{
                    pronounSelected = parent.getItemAtPosition(position).toString();
                }

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
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
        if(!urlImg.equals(" ")){
            try{
                Glide.with(getContext())
                        .load(urlImg)
                        .into(profile_img);
            }catch (Exception e){
                Glide.with(getContext())
                        .load(R.drawable.avatar_1)
                        .into(profile_img);
            }


            System.out.println(urlImg + "-----------------URICharge");

        }
    }

    void goSelectorRCA(String type) {
        ((CharacterManagerActivity) getActivity()).changeToRCA(type);
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
                        popUp.dismiss();
                        showNextStat(tmpValue);
                    }else{
                        txt_value.setError(getResources().getString(R.string.errorStat));
                    }
                }else{
                    txt_value.setError(getResources().getString(R.string.errorStat));
                }
            }
        });
    }

    void callPopUpStatsBonus(String tag){

        if(!quantity_abilityBonusChoices.equals("")){
            Log.d("popup","entering StatsBonus");
            View view = getLayoutInflater().inflate(R.layout.fragment_character_skills, null);
            rv = view.findViewById(R.id.rv_skillsSelector);
            txt_quantitySkills = view.findViewById(R.id.txv_skillsToSelect);
            btn_selectSkills = view.findViewById(R.id.btn_continueSkills);
            String quantityToChoose = quantity_abilityBonusChoices;

            txt_quantitySkills.setText(getResources().getText(R.string.quantityAbilityBonus).toString().replace("@numToCh@",quantityToChoose));
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
            rv.setLayoutManager(layoutManager);
            //Create data

            adapter = new AdapterSkills(dataSetAbilityBonus, getActivity());
            rv.setAdapter(adapter);

            loadingBar(View.INVISIBLE);
            PopUpCustom popUp = new PopUpCustom(view);
            popUp.show(getParentFragmentManager(), tag);
            popUp.setCancelable(false);

            btn_selectSkills.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_selectSkills.setError(null);
                    ArrayList<OptionsCharacter> tmpData = adapter.getData();
                    int quantity = 0;
                    for (OptionsCharacter op: tmpData) {
                        if(op.isSelected()){
                            quantity ++;
                        }
                    }

                    if (quantity==Integer.parseInt(quantityToChoose)){
                        addBonus(tmpData);
                        loadingBar(View.VISIBLE);
                        popUp.dismiss();
                        callPopUpProficienciesRace("skillsRace");
                    } else if (quantity==Integer.parseInt(quantityToChoose)) {
                        btn_selectSkills.setError("Solo puedes elegir "+quantityToChoose);
                    }
                }
            });
        }else{
            callPopUpProficienciesRace("skillsRace");

        }

    }

    void callPopUpProficienciesRace(String tag){
        if(!quantity_ProficiencyChoices_Race.equals("")){
            Log.d("popup","entering proficiencie races");
            View view = getLayoutInflater().inflate(R.layout.fragment_character_skills, null);
            rv = view.findViewById(R.id.rv_skillsSelector);
            txt_quantitySkills = view.findViewById(R.id.txv_skillsToSelect);
            btn_selectSkills = view.findViewById(R.id.btn_continueSkills);
            String quantityToChoose = quantity_ProficiencyChoices_Race;

            txt_quantitySkills.setText(getResources().getText(R.string.quantitySkills).toString().replace("@numToCh@",quantityToChoose));
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
            rv.setLayoutManager(layoutManager);
            //Create data

            adapter = new AdapterSkills(dataSetProficiencyChoicesRace, getActivity());
            rv.setAdapter(adapter);

            loadingBar(View.INVISIBLE);
            PopUpCustom popUp = new PopUpCustom(view);
            popUp.show(getParentFragmentManager(), tag);
            popUp.setCancelable(false);

            btn_selectSkills.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_selectSkills.setError(null);
                    ArrayList<OptionsCharacter> tmpData = adapter.getData();
                    int quantity = 0;
                    for (OptionsCharacter op: tmpData) {
                        if(op.isSelected()){
                            quantity ++;
                        }
                    }
                    if(quantity==Integer.parseInt(quantityToChoose)){
                        addProficiencies(tmpData);
                        loadingBar(View.VISIBLE);
                        popUp.dismiss();
                        callPopUpLanguages("langPopUp");
                    }else{
                        btn_selectSkills.setError("Solo puedes elegir "+quantityToChoose);
                    }
                }
            });
        }else{
            callPopUpLanguages("langPopUp");
        }

    }

    void callPopUpLanguages(String tag){
        if(!quantity_languagesChoices.equals("")){
            Log.d("popup","entering languages");

            View view = getLayoutInflater().inflate(R.layout.fragment_character_skills, null);
            rv = view.findViewById(R.id.rv_skillsSelector);
            txt_quantitySkills = view.findViewById(R.id.txv_skillsToSelect);
            btn_selectSkills = view.findViewById(R.id.btn_continueSkills);
            String quantityToChoose = quantity_languagesChoices;


            txt_quantitySkills.setText(getResources().getText(R.string.quantityLanguage).toString().replace("@numToCh@",quantityToChoose));
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
            rv.setLayoutManager(layoutManager);
            //Create data

            adapter = new AdapterSkills(dataLanguagesChoices, getActivity());
            rv.setAdapter(adapter);
            loadingBar(View.INVISIBLE);
            PopUpCustom popUp = new PopUpCustom(view);
            popUp.show(getParentFragmentManager(), tag);
            popUp.setCancelable(false);

            btn_selectSkills.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_selectSkills.setError(null);
                    ArrayList<OptionsCharacter> tmpData = adapter.getData();
                    int quantity = 0;
                    for (OptionsCharacter sk: tmpData) {
                        if(sk.isSelected()){
                            quantity ++;
                        }
                    }

                    if(quantity==Integer.parseInt(quantityToChoose)){
                        addProficiencies(tmpData);
                        loadingBar(View.VISIBLE);
                        popUp.dismiss();
                        callPopUpSkillsClass("skillsClass");
                    }else{
                        btn_selectSkills.setError("Solo puedes elegir "+quantityToChoose);
                    }
                }
            });
        }else{
            callPopUpSkillsClass("skillsClass");
        }

    }

    void callPopUpSkillsClass(String tag){
        Log.d("popup","entering skillclass");

        View view = getLayoutInflater().inflate(R.layout.fragment_character_skills, null);
        rv = view.findViewById(R.id.rv_skillsSelector);
        txt_quantitySkills = view.findViewById(R.id.txv_skillsToSelect);
        btn_selectSkills = view.findViewById(R.id.btn_continueSkills);
        String quantityToChoose = quantity_ProficiencyChoices_Class;


        txt_quantitySkills.setText(getResources().getText(R.string.quantitySkills).toString().replace("@numToCh@",quantityToChoose));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        rv.setLayoutManager(layoutManager);
        //Create data
        dataSetSkills = checkCorrectData();
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
                ArrayList<OptionsCharacter> tmpData = adapter.getData();
                int quantity = 0;
                for (OptionsCharacter sk: tmpData) {
                    if(sk.isSelected()){
                        finalSkills.add(new Skill(sk.getCode(),sk.getName()));
                        quantity ++;
                    }
                }

                if(quantity==Integer.parseInt(quantityToChoose)){
                    loadingBar(View.VISIBLE);
                    popUp.dismiss();
                    createCharacter(finalSkills);



                } else if (quantity>Integer.parseInt(quantityToChoose)) {
                    btn_selectSkills.setError("Solo puedes elegir "+quantityToChoose);
                    finalSkills.removeAll(finalSkills);
                    finalSkills = new ArrayList<>();
                }
            }
        });
    }

    private ArrayList<OptionsCharacter> checkCorrectData() {
        ArrayList<OptionsCharacter> result = new ArrayList<>();
        for (OptionsCharacter oc: dataSetSkills) {
            if(!allSkillsPlayer.contains(oc)){
                result.add(oc);
            }
        }


        return result;
    }

    void showNextStat(int valueStat){
        if(indexStats+1<stats.length) {
            Stats.add(valueStat);
            String stat = stats[indexStats+1];
            callPopUpStats("CreateCharacter", stat);
            indexStats++;
        }else{
            Stats.add(valueStat);
            indexStats = 0;
            loadingBar(View.VISIBLE);
            getInfoFromRace();
        }
    }

    private void getInfoFromRace(){
        Util.apiGETRequest("races/" + rcaInfoSaved[0].getCodeApiSearch(), new ApiCallback() {
            @Override
            public void onSuccess(JSONObject chosenRace) {
                try {
                    speed = chosenRace.getInt("speed");
                    JSONArray stats_bonuses = chosenRace.getJSONArray("ability_bonuses");
                    JSONObject stat = null;
                    String typeStat="";
                    int statBonus = 0;
                    for(int i=0;i<stats_bonuses.length();i++){
                        stat = stats_bonuses.getJSONObject(i);
                        typeStat = stat.getJSONObject("ability_score").getString("index");
                        statBonus = stat.getInt("bonus");
                        for(int j=0;j<Constants.TYPE_OF_STATS.length;j++){
                            if(Constants.TYPE_OF_STATS[j].equals(typeStat)){
                                int oldValue = Stats.get(j);
                                statBonus += oldValue;
                                Stats.set(j,statBonus);
                                break;
                            }
                        }

                    }
                    if(chosenRace.has("ability_bonus_options")){
                        JSONObject ability_Bonus = chosenRace.getJSONObject("ability_bonus_options");
                        quantity_abilityBonusChoices = String.valueOf(ability_Bonus.getInt("choose"));
                        JSONArray stats_bonuses_options = ability_Bonus.getJSONObject("from").getJSONArray("options");
                        JSONObject tmpStatBonus;
                        for(int i=0;i<stats_bonuses_options.length();i++){
                            tmpStatBonus = stats_bonuses_options.getJSONObject(i).getJSONObject("ability_score");
                            OptionsCharacter tmp = new OptionsCharacter(tmpStatBonus.getString("index"),tmpStatBonus.getString("name"));
                            tmp.setBonus(stats_bonuses_options.getJSONObject(i).getInt("bonus"));
                            dataSetAbilityBonus.add(tmp);
                        }

                    }

                    JSONArray starting_proficiencies = chosenRace.getJSONArray("starting_proficiencies");
                    if(starting_proficiencies.length() > 0){
                        JSONObject choice = null;
                        String nameResult = "";
                        for (int i = 0; i< starting_proficiencies.length(); i++) {
                            choice = starting_proficiencies.getJSONObject(i);
                            if(choice.getString("index").contains("skill-")){
                                nameResult = findCorrectName(choice.getString("name"),getResources().getStringArray(R.array.generalSkills),getResources().getStringArray(R.array.nameGeneralSkills),true);
                                Skill skill = new Skill(choice.getString("index"),nameResult);
                                allSkillsPlayer.add(skill);
                            }else{
                                ProfLang profLang = new ProfLang(choice.getString("index"),choice.getString("name"));
                                proficienciesAndLanguages.add(profLang);
                            }
                        }
                    }



                    if(chosenRace.has("starting_proficiency_options")){
                        JSONObject proficiency_options = chosenRace.getJSONObject("starting_proficiency_options");
                        quantity_ProficiencyChoices_Race = String.valueOf(proficiency_options.getInt("choose"));
                        JSONArray proficiencies_choices_options = proficiency_options.getJSONObject("from").getJSONArray("options");
                        if(proficiencies_choices_options.length() > 0){
                            JSONObject choice = null;
                            String nameResult="";
                            for (int i = 0; i< proficiencies_choices_options.length(); i++) {
                                choice = proficiencies_choices_options.getJSONObject(i).getJSONObject("item");
                                if(choice.getString("index").contains("skill-")) {
                                    nameResult = findCorrectName(choice.getString("name"),getResources().getStringArray(R.array.generalSkills),getResources().getStringArray(R.array.nameGeneralSkills),true);
                                }else{
                                    nameResult = choice.getString("name");}
                                OptionsCharacter tmpOp = new OptionsCharacter(choice.getString("index"),nameResult);
                                dataSetProficiencyChoicesRace.add(tmpOp);
                            }
                        }
                    }

                    JSONArray starting_languages = chosenRace.getJSONArray("languages");
                    if(starting_languages.length() > 0){
                        JSONObject choice = null;
                        for (int i = 0; i< starting_languages.length(); i++) {
                            choice = starting_languages.getJSONObject(i);
                            ProfLang profLang = new ProfLang(choice.getString("index"),getResources().getString(R.string.languageAdd).toString() + ": " + choice.getString("name"));
                            proficienciesAndLanguages.add(profLang);
                        }
                    }


                    if(chosenRace.has("language_options")){
                        JSONObject lang_options = chosenRace.getJSONObject("language_options");
                        quantity_languagesChoices = String.valueOf(lang_options.getInt("choose"));
                        JSONArray starting_languages_options = lang_options.getJSONObject("from").getJSONArray("options");

                        if(starting_languages_options.length() > 0){
                            JSONObject choice = null;
                            for (int i = 0; i< starting_languages_options.length(); i++) {
                                choice = starting_languages_options.getJSONObject(i).getJSONObject("item");
                                OptionsCharacter lang = new OptionsCharacter(choice.getString("index"),getResources().getString(R.string.languageAdd).toString() + ": " + choice.getString("name"));
                                dataLanguagesChoices.add(lang);
                            }
                        }
                    }

                    JSONArray traitsRace = chosenRace.getJSONArray("traits");
                    if(traitsRace.length() > 0){
                        JSONObject choice = null;
                        for (int i = 0; i< traitsRace.length(); i++) {
                            choice = traitsRace.getJSONObject(i);
                            ProfLang profLang = new ProfLang(choice.getString("index"),choice.getString("name"));
                            dataTraitsChoices.add(profLang);
                        }
                    }

                    getInfoFromClass();

                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        },getActivity());
    }

    private void getInfoFromClass(){
        Util.apiGETRequest("classes/" + rcaInfoSaved[1].getCodeApiSearch(), new ApiCallback() {
            @Override
            public void onSuccess(JSONObject chosenClass) {
                try {
                    dataSetSkills = new ArrayList<>();
                    dataSavingThrows = new ArrayList<>();

                    quantity_ProficiencyChoices_Class = String.valueOf(chosenClass.getJSONArray("proficiency_choices").
                            getJSONObject(0).getInt("choose"));

                    JSONArray proficiency_choices = chosenClass.getJSONArray("proficiency_choices").
                            getJSONObject(0).getJSONObject("from").getJSONArray("options");
                    getProficiencyChoices(proficiency_choices);

                    JSONArray proficienciesAdded = chosenClass.getJSONArray("proficiencies");
                    saveProficienciesClass(proficienciesAdded);

                    JSONArray saving_throws = chosenClass.getJSONArray("saving_throws");
                    getSavingThrows(saving_throws);

                    typeDice = chosenClass.getInt("hit_die");

                    if(chosenClass.has("spellcasting")){
                        spellCastingAbility = chosenClass.getJSONObject("spellcasting").getJSONObject("spellcasting_ability").getString("index");
                        getSpells();
                    }else{
                        callPopUpStatsBonus("bonusStats");
                    }
                }catch (Exception e){}
            }

            @Override
            public void onError(VolleyError error) {

            }
        },getActivity());
    }


    private void getSpells(){
        Util.apiGETRequest("classes/" + rcaInfoSaved[1].getCodeApiSearch() + "/spells", new ApiCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject){
                try {
                    allSpellsClass = jsonObject.getJSONArray("results");
                    Util.apiGETRequest("classes/" + rcaInfoSaved[1].getCodeApiSearch() + "/levels/" + txt_level.getText().toString(), new ApiCallback() {
                        @Override
                        public void onSuccess(JSONObject jsonObject){
                            try{
                                JSONObject spellCasting = jsonObject.getJSONObject("spellcasting");
                                spellsLevelQuantity = new ArrayList<>();
                                for (int i = 0; i < 10; i++) {
                                    if (i == 0) {
                                        if (spellCasting.has("cantrips_known")) {
                                            spellsLevelQuantity.add(spellCasting.getInt("cantrips_known"));
                                            if(spellCasting.has("spells_known")){
                                                spellsKnownQuantity = spellCasting.getInt("spells_known");
                                            }else{
                                                spellsKnownQuantity = -1;
                                            }
                                        } else {
                                            spellsLevelQuantity.add(-1);
                                            spellsKnownQuantity = -1;
                                        }
                                    }else{
                                        if(spellCasting.has("spell_slots_level_" + i)){
                                            spellsLevelQuantity.add(spellCasting.getInt("spell_slots_level_" + i));
                                        }else{
                                            spellsLevelQuantity.add(-1);
                                        }
                                    }
                                }

                                addAllSpells(0, Integer.parseInt(txt_level.getText().toString()));
                            }catch (Exception e){
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {

                        }
                    },getActivity());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                callPopUpStatsBonus("bonusStats");
            }
        },getActivity());

    }


    private void getProficiencyChoices(JSONArray proficiency_choices) throws JSONException {
        JSONObject choice = null;
        String nameResult = "";
        for (int i = 0; i< proficiency_choices.length(); i++) {
            choice = proficiency_choices.getJSONObject(i).getJSONObject("item");
            nameResult = findCorrectName(choice.getString("name"),getResources().getStringArray(R.array.generalSkills),getResources().getStringArray(R.array.nameGeneralSkills),true);
            OptionsCharacter tmpResult = new OptionsCharacter(choice.getString("index"),nameResult);
            dataSetSkills.add(tmpResult);
        }
    }

    private void saveProficienciesClass(JSONArray proficienciesAdded) throws JSONException {
        if(proficienciesAdded.length() > 0){
            JSONObject choice = null;
            for (int i = 0; i< proficienciesAdded.length(); i++) {
                choice = proficienciesAdded.getJSONObject(i);
                if(!choice.getString("index").contains("saving-throw")){
                    ProfLang profLang = new ProfLang(choice.getString("index"),choice.getString("name"));
                    proficienciesAndLanguages.add(profLang);
                }
            }
        }

    }

    private void getSavingThrows(JSONArray saving_throws) throws JSONException {
        for (int i = 0; i< saving_throws.length(); i++){
            dataSavingThrows.add(saving_throws.getJSONObject(i).getString("index"));
        }
    }


    private String findCorrectName(String name,String[] generalTypeToFind,String[] langNameToFind,boolean skill){
        String [] nameOfType = generalTypeToFind;
        String[] nameCorrectLangFind = langNameToFind;
        String nameToFind = skill ? name.substring(name.indexOf(": ")+2) : name;

        for(int i=0;i<nameOfType.length;i++){
            if(nameOfType[i].contains(nameToFind)){
                return nameCorrectLangFind[i];
            }
        }
        return "";
    }



    private void addBonus(ArrayList<OptionsCharacter> tmpData) {
        int bonus = 0;
        for (OptionsCharacter op : tmpData) {
            for(int j=0;j<Constants.TYPE_OF_STATS.length;j++){
                if(Constants.TYPE_OF_STATS[j].equals(op.getCode())){
                    int oldValue = Stats.get(j);
                    bonus = oldValue + op.getBonus();
                    Stats.set(j,bonus);
                    break;
                }
            }
        }
    }


    private void addProficiencies(ArrayList<OptionsCharacter> tmpData) {
        for (OptionsCharacter op:tmpData) {
            if(op.isSelected()){
                if(op.getCode().contains("skill-")){
                    allSkillsPlayer.add(new Skill(op.getCode(), op.getName()));
                }else{
                    proficienciesAndLanguages.add(new ProfLang(op.getCode(),op.getName()));
                }
            }
        }
    }



    private void addAllSpells(int level,int finalLevel){
        Util.apiGETRequest("spells?level=" + level, new ApiCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try{
                    allSpellsByLevel.add(jsonObject.getJSONArray("results"));

                    if(level == finalLevel){
                        spells = new Spells(spellsLevelQuantity,allSpellsByLevel,allSpellsClass,spellsKnownQuantity);
                        callPopUpStatsBonus("bonusStats");
                    }else{
                        int tmpLevel = level + 1;
                        addAllSpells(tmpLevel,finalLevel);
                    }
                }catch (Exception e){
                    throw new RuntimeException(e);
                }


            }

            @Override
            public void onError(VolleyError error) {

            }
        },getActivity());
    }


    void createCharacter(ArrayList<Skill> skillsResult) {
        int quantityHitDice = 1;
        int typeHitDice = typeDice;
        for (Skill sk: skillsResult) {
            allSkillsPlayer.add(sk);
        }

        Character character = new Character(User.getInstance(),txt_name.getText().toString(),"",
                rcaInfoSaved,Integer.parseInt(txt_level.getText().toString()),genderSelected,
                pronounSelected,Stats,dataSavingThrows,allSkillsPlayer,proficienciesAndLanguages,
                speed,quantityHitDice,typeHitDice,dataTraitsChoices,spells,spellCastingAbility);

        StorageReference storageRef = dbStorage.getReference();
        String nameImage = "profileCharacter_" + character.getID() + ".jpg";
        StorageReference imageRef = storageRef.child("profileImages/" + nameImage);
        spells.setSpellsDamage(new HashMap<>());
        if(profile_img.getDrawable() != null){
            character.setImgPlayerName("profileImages/" + nameImage);
            Drawable drawable = profile_img.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            imageRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    setDamageSpells(0,character);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    character.setImgPlayerName("");
                    setDamageSpells(0,character);
                }
            });
        }else{
            character.setImgPlayerName("");
            setDamageSpells(0,character);

        }
    }

    private void setDamageSpells(int level, Character character){
        if(level>character.getLevel()){
            setLevelFeatures(1,character);
        }else{
            character.getSpells().getSpellsDamage().put(Integer.toString(level),new ArrayList<>());
            List<Spells.Spell> spellList = character.getSpells().getSpellsName().get(Integer.toString(level));
            if(spellList != null && !spellList.isEmpty()){
                getDamageOfThisSpells(level,0,spellList,character);
            }else{
                setDamageSpells(level+1,character);
            }
        }


    }

    private void getDamageOfThisSpells(int level, int i, List<Spells.Spell> spellList,Character character) {
        if(i<spellList.size()){
            Util.apiGETRequest("spells/" + spellList.get(i).getCode(), new ApiCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        if(jsonObject.has("damage")){
                            if(jsonObject.getJSONObject("damage").has("damage_at_slot_level")){
                                JSONObject damageSlotLevelObject = jsonObject.getJSONObject("damage").getJSONObject("damage_at_slot_level");

                                Iterator<String> slotLevelKeys = damageSlotLevelObject.keys();
                                Spells.Spell spell = null;
                                while (slotLevelKeys.hasNext()) {
                                    String slotLevelKey = slotLevelKeys.next();
                                    String damageValue = damageSlotLevelObject.getString(slotLevelKey);
                                    spell =  spellList.get(i);
                                    spell.setHasDamage(1);
                                    spell.setSlotCharacter(0);
                                    spell.getSlot_level().put(slotLevelKey,damageValue);

                                }
                                Log.d("spell",Integer.toString(level) + " " + spell);
                                character.getSpells().getSpellsDamage().get(Integer.toString(level)).add(spell);
                            }else if(jsonObject.getJSONObject("damage").has("damage_at_character_level")){
                                JSONObject damageSlotLevelObject = jsonObject.getJSONObject("damage").getJSONObject("damage_at_character_level");

                                Iterator<String> slotLevelKeys = damageSlotLevelObject.keys();
                                Spells.Spell spell = null;
                                while (slotLevelKeys.hasNext()) {
                                    String slotLevelKey = slotLevelKeys.next();
                                    String damageValue = damageSlotLevelObject.getString(slotLevelKey);
                                    spell =  spellList.get(i);
                                    spell.setHasDamage(1);
                                    spell.setSlotCharacter(1);
                                    spell.getSlot_level().put(slotLevelKey,damageValue);

                                }
                                character.getSpells().getSpellsDamage().get(Integer.toString(level)).add(spell);
                            }
                        }
                        getDamageOfThisSpells(level,i+1,spellList,character);
                    }catch (Exception e){
                        System.out.println(e);
                        getDamageOfThisSpells(level,i+1,spellList,character);
                    }

                }

                @Override
                public void onError(VolleyError error) {
                    getDamageOfThisSpells(level,i+1,spellList,character);
                }
            },getActivity());

        }else{
            setDamageSpells(level+1,character);
        }
    }

    private void setLevelFeatures(int level, Character character){
        if(level>character.getLevel()){
            saveCharacter(character);
        }else{
            updateLevel(level,character);
        }


    }

    private void updateLevel(int level,Character character) {

        Util.apiGETRequest("classes/"+character.getCodeClass() + "/levels/"+ level, new ApiCallback() {
            @Override
            public void onSuccess(JSONObject levelUpdate) {
                try{
                    if(levelUpdate.has("features")){
                        ArrayList<ProfLang> tmpFeatures = character.getFeaturesAndTraits();
                        JSONArray featuresLevel = levelUpdate.getJSONArray("features");
                        for(int i = 0;i<featuresLevel.length();i++){
                            JSONObject tmp = featuresLevel.getJSONObject(i);
                            tmpFeatures.add(new ProfLang(tmp.getString("index"),tmp.getString("name")));
                        }
                        character.setFeaturesAndTraits(tmpFeatures);
                    }
                    setLevelFeatures(level+1,character);
                }catch (Exception e){
                    setLevelFeatures(level+1,character);
                }
            }
            @Override
            public void onError(VolleyError error) {
                setLevelFeatures(level+1,character);
            }
        },getActivity());


    }

    private void saveCharacter(Character character) {
        if(!partyCode.equals("")){
            System.out.println("-----------------tamo esoty");

            character.setPartyID(partyCode);
            db.collection("characters").document(character.getUserID()).collection(User.getInstance().getUserName())
                    .document(character.getID()).set(character).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            db.collection("parties").document(partyCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        Party party = task.getResult().toObject(Party.class);
                                        ArrayList<Character> characters = party.getPlayers();
                                        characters.add(character);
                                        party.setPlayers(characters);
                                        db.collection("parties").document(party.getID()).set(party).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                User.getInstance().getParties().add(partyCode);
                                                db.collection("users").document(User.getInstance().getId()).set(User.getInstance()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        ((CharacterManagerActivity)getActivity()).goWaitingRoom(party.getID());
                                                    }
                                                });

                                            }
                                        });
                                        //volver a la waiting list
                                    }
                                }
                            });
                        }
                    });
        }else{
            System.out.println("-----------------aqui esoty");
            db.collection("characters").document(character.getUserID()).collection(User.getInstance().getUserName())
                    .document(character.getID()).set(character).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finishCreation();
                        }
                    });
        }

    }

    void finishCreation(){
        getActivity().getSupportFragmentManager().popBackStack();
    }

    void loadingBar(int visibility){
        ((CharacterManagerActivity)getActivity()).changeLoadingVisibility(visibility);
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

        quantity_ProficiencyChoices_Class = "";
        quantity_ProficiencyChoices_Race = "";
        quantity_abilityBonusChoices = "";
        quantity_languagesChoices= "";
    }
}