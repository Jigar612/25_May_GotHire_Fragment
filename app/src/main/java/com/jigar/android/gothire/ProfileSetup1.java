package com.jigar.android.gothire;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.allyants.chipview.ChipView;
import com.allyants.chipview.SimpleChipAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.jigar.android.gothire.Adapter.RecyclerAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileSetup1 extends AppCompatActivity {

    String webservice_url = UrlString.URL + "GetCandidate/";
    String webservice_AllSkills_url = UrlString.URL + "GetAllSkills";
    // AutoCompleteTextView ed_skill;

   // ChipView chipDefault;
    EditText ed_education,ed_school,ed_job_title,ed_company;
    Button btn_next,btn_back;
    ProgressDialog progressDialog;

    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    String Candidate_id;
    //****

    String str_first_nm,str_last_nm,str_email_add,str_location,str_phone,profile_path;
    //  String Industries;
    String get_Skill_id;
    String ans_skill_nm="";

    String str_education="null",str_school="null",str_job_title="null",str_company_nm="null",str_skills="null";

    ArrayList<String> arrayList_skill;

    ArrayList<String> arrayList_id;
    ArrayAdapter<String> adapter_skill;


    RecyclerView tagsRecyclerView;
    MultiAutoCompleteTextView tagsEditText_skill;
    ArrayList<RecyclerModel> recyclerModels = new ArrayList<>(  );
    RecyclerAdapter adapter;
    FlexboxLayoutManager gridLayout;
    LinearLayout linaer_main;


   // View view;
    Bundle args;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup1);

        MainContainer.TAG="Profile1";

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent get_data=getIntent();
        Candidate_id = get_data.getStringExtra("Key_candidate_id");
        //Getting Data from ProfileSetup.java
        str_first_nm = get_data.getStringExtra("Key_first_nm");
        str_last_nm = get_data.getStringExtra("Key_last_nm");
        str_email_add = get_data.getStringExtra("Key_email_add");
        str_location = get_data.getStringExtra("Key_location");
        str_phone = get_data.getStringExtra("Key_phone");
        profile_path= get_data.getStringExtra("Key_profile_path");
        //******************


        str_education =get_data.getStringExtra("Key_education");
        str_school = get_data.getStringExtra("Key_school");
        str_job_title =get_data.getStringExtra("Key_job_title");
        str_company_nm =get_data.getStringExtra("Key_companyNm");
        str_skills = get_data.getStringExtra("Key_skills");
        profile_path= get_data.getStringExtra ("Key_profile_path");


        TextView tv_skill_qua = (TextView) findViewById(R.id.tv_skill_qualification);
        ed_education=(EditText) findViewById(R.id.ed_education_profile1);
        ed_school=(EditText)findViewById(R.id.ed_school_profile1);
        ed_job_title=(EditText)findViewById(R.id.ed_job_title_profile1);
        ed_company=(EditText)findViewById(R.id.ed_company_profile1);
       // ed_skill=(MultiAutoCompleteTextView)view.findViewById(R.id.ed_skill_profile1);
       // chipDefault = (ChipView) view.findViewById(R.id.chipview_skill);
        btn_next=(Button)findViewById(R.id.btn_next_profile1);
        btn_back =(Button)findViewById(R.id.btn_back_profile_setup1);

        tagsRecyclerView = findViewById( R.id.tagsRecyclerView );
        tagsEditText_skill = findViewById( R.id.tagsEditText_skill);
        linaer_main = (LinearLayout)findViewById(R.id.linear_main);

        Typeface roboto_Light = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Light.ttf");
        Typeface roboto_Bold = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Bold.ttf");
        tv_skill_qua.setTypeface(roboto_Bold);
        ed_education.setTypeface(roboto_Light);
        ed_school.setTypeface(roboto_Light);
        ed_job_title.setTypeface(roboto_Light);
        ed_company.setTypeface(roboto_Light);
        btn_next.setTypeface(roboto_Light);
        btn_back.setTypeface(roboto_Light);
        tagsEditText_skill.setTypeface(roboto_Light);

       ed_school.setText(str_school);

        linaer_main.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(linaer_main.getWindowToken(), 0);

            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        arrayList_id=new ArrayList<>();
        arrayList_skill=new ArrayList<>();
        runTask();
    }
    public void runTask() {
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            //Call AllSkills Webservices..
            call_allSkills_webservice();

            //AutocompleteTextview for skill from database..
            tagsEditText_skill.setPadding(15,15,15,15);
           // adapter_skill = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.select_dialog_item, arrayList_skill);
            adapter_skill = new ArrayAdapter<String>(ProfileSetup1.this,R.layout.spinner_text, arrayList_skill);
            adapter_skill.setDropDownViewResource(R.layout.spinner_text);

            adapter = new RecyclerAdapter(ProfileSetup1.this, recyclerModels);
            gridLayout = new FlexboxLayoutManager(ProfileSetup1.this);
            // adapter_skill = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.item_skill_chips, arrayList_skill);
            // Must set tokenizer for MultiAutoCompleteTextView object, otherwise it will not take effect.
            tagsEditText_skill.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

            tagsEditText_skill.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        String text = s.toString();
                        if (text.contains(",")) {
                        String givenstring = tagsEditText_skill.getText().toString();
                       // if (givenstring.contains(",")) {
                            givenstring = givenstring.replaceAll(",", "");
                            givenstring = givenstring.trim();
                            if (givenstring != null && !givenstring.equals("")) {
                                getUpdateData(givenstring);
                                tagsEditText_skill.setText(null);
                                tagsRecyclerView.setLayoutManager(gridLayout);
                                tagsRecyclerView.setAdapter(adapter);
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                   // tagsEditText_skill.addTextChangedListener(this);
                }
            });
//            tagsEditText_skill.setOnEditorActionListener( new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                    if (actionId == EditorInfo.IME_ACTION_DONE) {
//                        Toast.makeText( getActivity(),"hello",Toast.LENGTH_SHORT );
//                        String str = tagsEditText_skill.getText().toString();
//                        if(str != null && !str.equals( "" )) {
//                            getUpdateData( str );
//                            tagsEditText_skill.setText( null );
//                            RecyclerAdapter adapter = new RecyclerAdapter( getActivity(), recyclerModels );
//                            FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(getActivity() );
//                            tagsRecyclerView.setLayoutManager( gridLayout );
//                            tagsRecyclerView.setAdapter( adapter );
//
//                            // String temp= String.valueOf(recyclerModels.get(0));
//                        }
//                    }
//                    return false;
//                }
//            } );
            call_profileGetData_webservices();
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent_profile_setup1 = new Intent(getActivity(), ProfileSetup.class);
//                    intent_profile_setup1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    //intent_profile_setup1.putExtra("Key_candidate_id",Candidate_id);
//                    startActivity(intent_profile_setup1);
                   // getActivity().getFragmentManager().beginTransaction().remove(ProfileSetup1.this).commit();
     //               finish();
                    String education= ed_education.getText().toString();
                    String school= ed_school.getText().toString();
                    String job_title= ed_job_title.getText().toString();
                    String company_nm= ed_company.getText().toString();
                    //   String skills= tagsEditText_skill.getText().toString();

                    int recyclerModels_count=recyclerModels.size();
                    String ans_skills=null;
                    for (int i=0; i<recyclerModels_count;i++) {
                        String ans_sss = recyclerModels.get(i).getTagText();
                        if (ans_skills == "" || ans_skills == null) {
                            ans_skills = ans_sss;
                        } else {
                            ans_skills += "," + ans_sss;
                        }
                    }
                    Intent intent_profile_setup1 = new Intent(ProfileSetup1.this, ProfileSetup.class);
                    intent_profile_setup1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent_profile_setup1.putExtra("Key_candidate_id",Candidate_id);
                    //ProfileSetup data send to ProfileSetup1
                    intent_profile_setup1.putExtra("Key_education",education);
                    intent_profile_setup1.putExtra("Key_school",school);
                    intent_profile_setup1.putExtra("Key_job_title",job_title);
                    intent_profile_setup1.putExtra("Key_companyNm",company_nm);
                    intent_profile_setup1.putExtra("Key_skills",ans_skills);

//                    intent_profile_setup1.putExtra("Key_first_nm",str_first_nm);
//                    intent_profile_setup1.putExtra("Key_last_nm",str_last_nm);
//                    intent_profile_setup1.putExtra("Key_email_add",str_email_add);
//                    intent_profile_setup1.putExtra("Key_location",str_location);
//                    intent_profile_setup1.putExtra("Key_phone",str_phone);
//                    intent_profile_setup1.putExtra("Key_profile_path",profile_path);
//                    startActivity(intent_profile_setup1);


                }
            });
            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String education= ed_education.getText().toString();
                    String school= ed_school.getText().toString();
                    String job_title= ed_job_title.getText().toString();
                    String company_nm= ed_company.getText().toString();
                 //   String skills= tagsEditText_skill.getText().toString();

                    int recyclerModels_count=recyclerModels.size();
                    String ans_skills=null;
                    for (int i=0; i<recyclerModels_count;i++) {
                        String ans_sss = recyclerModels.get(i).getTagText();
                        if (ans_skills == "" || ans_skills == null) {
                            ans_skills = ans_sss;
                        } else {
                            ans_skills += "," + ans_sss;
                        }
                    }

                  //  Toast.makeText(getActivity(), ans_skills.toString(), Toast.LENGTH_LONG).show();
//                    args = new Bundle();
//                    args.putString("Key_candidate_id",Candidate_id);
//                    args.putString("Key_education",education);
//                    args.putString("Key_school",school);
//                    args.putString("Key_job_title",job_title);
//                    args.putString("Key_companyNm",company_nm);
//                    args.putString("Key_skills",ans_skills);
//                    //***
//                    //ProfileSetup****
//                    args.putString("Key_first_nm",str_first_nm);
//                    args.putString("Key_last_nm",str_last_nm);
//                    args.putString("Key_email_add",str_email_add);
//                    args.putString("Key_location",str_location);
//                    args.putString("Key_phone",str_phone);
//                    args.putString ("Key_profile_path",profile_path);
                    //****
//                    ProfileSetup2 profileSetup2 = new ProfileSetup2();
//                    FragmentManager fm_profile =  getFragmentManager();
//                    FragmentTransaction fragmentTransaction_profile = fm_profile.beginTransaction();
//                    profileSetup2.setArguments(args);
//                    fragmentTransaction_profile.addToBackStack(null);
//                    fragmentTransaction_profile.add(R.id.frameLayout, profileSetup2);
//                    fragmentTransaction_profile.commit(); // save the ch

                    //***
                    //ProfileSetup****



                    if(education == null || education.isEmpty() || education.equals("null") || education.equals(""))
                    {
                        Toast.makeText(ProfileSetup1.this, "Please enter education", Toast.LENGTH_LONG).show();

                    }
                    else  if(school== null || school.isEmpty() || school.equals("null") || school.equals(""))
                    {
                        Toast.makeText(ProfileSetup1.this, "Please enter school", Toast.LENGTH_LONG).show();
                    }
                    else  if(job_title == null || job_title.isEmpty() || job_title.equals("null")|| job_title.equals(""))
                    {
                        Toast.makeText(ProfileSetup1.this, "Please enter job title", Toast.LENGTH_LONG).show();

                    }
                    else  if(ans_skills == null || ans_skills.isEmpty() || ans_skills.equals("null")|| ans_skills.equals(""))
                    {
                        Toast.makeText(ProfileSetup1.this, "Please enter skills", Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        Intent intent_profile_setup1 = new Intent(ProfileSetup1.this, ProfileSetup2.class);
                        intent_profile_setup1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent_profile_setup1.putExtra("Key_candidate_id",Candidate_id);
                        //ProfileSetup data send to ProfileSetup1
                        intent_profile_setup1.putExtra("Key_education",education);
                        intent_profile_setup1.putExtra("Key_school",school);
                        intent_profile_setup1.putExtra("Key_job_title",job_title);
                        intent_profile_setup1.putExtra("Key_companyNm",company_nm);
                        intent_profile_setup1.putExtra("Key_skills",ans_skills);

                        intent_profile_setup1.putExtra("Key_first_nm",str_first_nm);
                        intent_profile_setup1.putExtra("Key_last_nm",str_last_nm);
                        intent_profile_setup1.putExtra("Key_email_add",str_email_add);
                        intent_profile_setup1.putExtra("Key_location",str_location);
                        intent_profile_setup1.putExtra("Key_phone",str_phone);
                        intent_profile_setup1.putExtra("Key_profile_path",profile_path);
                        startActivity(intent_profile_setup1);
                    }
                }
            });
            //Write here
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            // Toast.makeText(LeadListActivity.this, " Please check your internet connection.", Toast.LENGTH_LONG).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.no_connection);
            builder.setIcon(R.mipmap.ic_internetconnection);
            //builder.setMessage("Sorry there was an error getting data from the Internet.\nNetwork Unavailable!");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    runTask();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    private void getUpdateData(String str) {
        RecyclerModel model = new RecyclerModel( str );
        recyclerModels.add( model );
    }
    public void call_profileGetData_webservices()
    {
        progressDialog.show();
        String url_profile1 = webservice_url.toString() +Candidate_id ;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_profile1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetCandidateResult");

                    int count = 0;
                    String Education = null;
                    String School=null;
                    String JobTitle=null;
                    String Company=null;
                    String Skill=null;
                    while (count < resultJsonArr.length()) {
                        JSONObject jObject = resultJsonArr.getJSONObject(count);
                        Education = jObject.getString("Educationname");
                        School = jObject.getString("Institute");
                        JobTitle = jObject.getString("ResumeTitle");
                        Company = jObject.getString("companyname");
                       // Skill = jObject.getString("SkillName");
                        Skill = jObject.getString("skill");
                        count++;
                    }

                   if(!Education.equals("") &&  !Education.equals("null"))
                   {
                       ed_education.setText(Education);
                   }
                    if(!School.equals("") &&  !School.equals("null")) {
                      //  ed_school.setText(School);
                    }
                    if(!JobTitle.equals("") &&  !JobTitle.equals("null")) {
                        ed_job_title.setText(JobTitle);
                    }
                    ed_company.setText(Company);
                 //   ed_skill.setText(Skill);
                    get_Skill_id=Skill;
                    call_webservice_Skillid_to_name();

                    //New code for chips in skill after ,
//                 //  tagsEditText_skill.setText(Skill);
//                    String []split_skill = Skill.split(",");
//                    for(int i=0;i<split_skill.length;i++) {
//                        // int count = 0;
//                        String sss = split_skill[i].toString();
//
//                        if (sss != null && !sss.equals("")) {
//                            getUpdateData(sss);
//                            tagsEditText_skill.setText(null);
//                            adapter = new RecyclerAdapter(getActivity(), recyclerModels);
//                            gridLayout = new FlexboxLayoutManager(getActivity());
//                            tagsRecyclerView.setLayoutManager(gridLayout);
//                            tagsRecyclerView.setAdapter(adapter);
//                            // String temp= String.valueOf(recyclerModels.get(0));
//                        }
//                    }
//                    //************
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ProfileSetup1.this, "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //   listview_home.setEmptyView(tv_empty);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(jsonObjReq);
    }
    public void call_allSkills_webservice()
    {
        progressDialog.show();
        String url_Skill = webservice_AllSkills_url.toString() ;

        JsonObjectRequest json_skill = new JsonObjectRequest(Request.Method.GET, url_Skill, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetAllSkillsResult");
                    int count = 0;
                    while (count < resultJsonArr.length()) {

                        JSONObject jObject = resultJsonArr.getJSONObject(count);

                        String Skill_Id = jObject.getString("ItSkillId");
                        String Skill_nm= jObject.getString("SkillName");

                        //   rowItem_skill.setSkill_id(Skill_Id);
                        //    rowItem_skill.setSkill_nm(Skill_nm);
                        arrayList_skill.add(Skill_nm);
                        arrayList_id.add(Skill_Id);
                        count++;
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                tagsEditText_skill.setThreshold(1);
                tagsEditText_skill.setAdapter(adapter_skill);
               // chipDefault.setAdapter(adapter_skill_chips);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ProfileSetup1.this, "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //   listview_home.setEmptyView(tv_empty);
            }
        }) {};
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(json_skill);
    }
    public void call_webservice_Skillid_to_name()
    {
        progressDialog.show();
        String url_Skill = webservice_AllSkills_url.toString() ;

        JsonObjectRequest json_skill = new JsonObjectRequest(Request.Method.GET, url_Skill, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetAllSkillsResult");

                 //   StringBuilder sb = new StringBuilder();
                    String []split = get_Skill_id.split(",");
                    for(int i=0;i<split.length;i++)
                    {
                        int count = 0;
                        String sss=split[i].toString();
                       // if (!sss.equals("") || !sss.equals(null) || !sss.isEmpty() || sss.equals("0"))
                        if(!sss.equals("") && !sss.equals("0"))
                        {
                            while (count < resultJsonArr.length()) {
                                JSONObject jObject = resultJsonArr.getJSONObject(count);

                                String Skill_Id = jObject.getString("ItSkillId");
                                if (sss.equals(Skill_Id)) {
                                    String Skill_nm = jObject.getString("SkillName");
                                 //   sb.append(Skill_nm);
                                    if (ans_skill_nm=="" || ans_skill_nm==null)
                                    {
                                        ans_skill_nm = Skill_nm;
                                    }
                                    else
                                    {
                                        ans_skill_nm += "," + Skill_nm;
                                    }
                                }
                                count++;
                            }
                        }
                    }
                    //tagsEditText_skill.setText(ans_skill_nm);
                    if(!ans_skill_nm.equals("") || !ans_skill_nm.equals(null))
                    {
                        String[] split_skill = ans_skill_nm.split(",");
                        for (int i = 0; i < split_skill.length; i++) {
                            // int count = 0;
                            String sss = split_skill[i].toString();

                            if (sss != null && !sss.equals("") && !sss.equals("0")) {
                                getUpdateData(sss);
                                tagsEditText_skill.setText(null);
                                tagsRecyclerView.setLayoutManager(gridLayout);
                                tagsRecyclerView.setAdapter(adapter);
                                // String temp= String.valueOf(recyclerModels.get(0));
                            }
                        }
                    }
                    //************
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ProfileSetup1.this, "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //   listview_home.setEmptyView(tv_empty);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(json_skill);
    }
}