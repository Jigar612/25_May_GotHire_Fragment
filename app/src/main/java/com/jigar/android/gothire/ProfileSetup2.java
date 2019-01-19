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
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class ProfileSetup2 extends AppCompatActivity {
    // String webservice_AllSkills_url = UrlString.URL + "GetAllSkills/";
    String webservice_AllIndustery_url = UrlString.URL + "GetAllIndusteryList";
    String webservice_Update_profile_url = UrlString.URL + "InsertProfiel/";

    ProgressDialog progressDialog;

    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    String Candidate_id;
    //****
    String str_first_nm="null",str_last_nm="null",str_email_add="null",str_location="null",str_phone="null",profile_path="null";
    String str_education="null",str_school="null",str_job_title="null",str_company_nm="null",str_skills="null";
    String str_next_job_title="null";
    // String industry_nm;

    ArrayList<String> arrayList_industry;
    ArrayList<String> arrayList_industry_id;
    ArrayAdapter<String> adapter_industry;

    String get_industry_id;
    String ans_industry_nm="";

    RecyclerView tagsRecyclerView;
    MultiAutoCompleteTextView ed_all_industries;
    ArrayList<RecyclerModel> recyclerModels = new ArrayList<>(  );
    RecyclerAdapter adapter;
    FlexboxLayoutManager gridLayout;
    LinearLayout linaer_main;

    EditText ed_job_title;

    Button btn_join_me;
    Button btn_back;
    //View view;
    Bundle args;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup2);

        MainContainer.TAG="Profile2";

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent get_data =getIntent();
        Candidate_id = get_data.getStringExtra("Key_candidate_id");

        //Getting Data from ProfileSetup1.java
        str_first_nm = get_data.getStringExtra("Key_first_nm");
        str_last_nm = get_data.getStringExtra("Key_last_nm");
        str_email_add =get_data.getStringExtra("Key_email_add");
        str_location = get_data.getStringExtra("Key_location");
        str_phone = get_data.getStringExtra("Key_phone");
        //******************

        //Getting Data from ProfileSetup1.java
        str_education =get_data.getStringExtra("Key_education");
        str_school = get_data.getStringExtra("Key_school");
        str_job_title =get_data.getStringExtra("Key_job_title");
        str_company_nm =get_data.getStringExtra("Key_companyNm");
        str_skills = get_data.getStringExtra("Key_skills");
        profile_path= get_data.getStringExtra ("Key_profile_path");
        //******************


        //android:id="@+id/tv_lets"
        TextView tv_lets=(TextView) findViewById(R.id.tv_lets);
        tagsRecyclerView = findViewById( R.id.tagsRecyclerView_profile2 );
        ed_all_industries=findViewById(R.id.ed_industry_profile2);
         ed_job_title=findViewById(R.id.ed_nxt_job_title_profile2);
        btn_join_me=(Button) findViewById(R.id.btn_join_me_profile2);
        btn_back=(Button)findViewById(R.id.btn_back_profile_setup2);

        linaer_main = (LinearLayout)findViewById(R.id.linear_main);

        Typeface roboto_Light = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Light.ttf");
        Typeface roboto_reg = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface roboto_Bold = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Bold.ttf");

        tv_lets.setTypeface(roboto_Bold);
        ed_all_industries.setTypeface(roboto_reg);
        ed_job_title.setTypeface(roboto_reg);
        btn_join_me.setTypeface(roboto_Light);

        arrayList_industry_id=new ArrayList<>();
        arrayList_industry=new ArrayList<>();

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

        runTask();
    }
    public void runTask() {
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            call_allIndustries_webservice();
            call_profileGetData_webservices();
            ed_all_industries.setPadding(15,15,15,15);
           //adapter_industry = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, arrayList_industry);
            adapter_industry = new ArrayAdapter<String>(ProfileSetup2.this,R.layout.spinner_text, arrayList_industry);
            adapter_industry.setDropDownViewResource(R.layout.spinner_text);

            adapter = new RecyclerAdapter(ProfileSetup2.this, recyclerModels);
            gridLayout = new FlexboxLayoutManager(ProfileSetup2.this);

            // Must set tokenizer for MultiAutoCompleteTextView object, otherwise it will not take effect.
            ed_all_industries.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


            ed_all_industries.addTextChangedListener(new TextWatcher() {
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
                        if (text.contains(","))
                        {
                          String givenstring = ed_all_industries.getText().toString();
                             //  if (givenstring.contains(",")) {
                            givenstring = givenstring.replaceAll(",", "");
                            givenstring = givenstring.trim();

                            if (givenstring != null && !givenstring.equals(""))
                            {
                                getUpdateData(givenstring);
                                ed_all_industries.setText(null);
                                tagsRecyclerView.setLayoutManager(gridLayout);
                                tagsRecyclerView.setAdapter(adapter);
                            }
                         }
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                  //  ed_all_industries.addTextChangedListener(this);
                }
            });
            btn_join_me.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    update_profile_webservices();
                }
            });

            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();

                }
            });
            //Write here
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            // Toast.makeText(LeadListActivity.this, " Please check your internet connection.", Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSetup2.this);
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
        String url_profile1 = UrlString.URL + "GetCandidate/" +Candidate_id ;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_profile1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetCandidateResult");

                    int count = 0;

                    String Industery=null;

                    while (count < resultJsonArr.length()) {

                        JSONObject jObject = resultJsonArr.getJSONObject(count);

                        Industery = jObject.getString("Industery");
                     //  String Skill = jObject.getString("skill");

                        count++;
                    }
                 //   ed_all_industries.setText(Industery);
//                    String []split_industry = Industery.split(",");
//                    for(int i=0;i<split_industry.length;i++) {
//                        // int count = 0;
//                        String sss = split_industry[i].toString();
//
//                        if (sss != null && !sss.equals("")) {
//                            getUpdateData(sss);
//                            ed_all_industries.setText(null);
//                            tagsRecyclerView.setLayoutManager(gridLayout);
//                            tagsRecyclerView.setAdapter(adapter);
//                        }
//                    }
                    //************
                    get_industry_id=Industery;
                    call_webservice_industryid_to_name();

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ProfileSetup2.this, "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //   listview_home.setEmptyView(tv_empty);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(jsonObjReq);
    }
    public void call_webservice_industryid_to_name()
    {
        progressDialog.show();
        String url_Skill =  UrlString.URL + "GetAllIndusteryList" ;

        JsonObjectRequest json_skill = new JsonObjectRequest(Request.Method.GET, url_Skill, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetAllIndusteryListResult");

                    String []split = get_industry_id.split(",");
                    for(int i=0;i<split.length;i++)
                    {
                        int count = 0;
                        String sss=split[i].toString();
                        // if (!sss.equals("") || !sss.equals(null) || !sss.isEmpty() || sss.equals("0"))
                        if(!sss.equals("") && !sss.equals("0"))
                        {
                            while (count < resultJsonArr.length()) {
                                JSONObject jObject = resultJsonArr.getJSONObject(count);

                                String industy_Id = jObject.getString("industryId");
                                if (sss.equals(industy_Id)) {
                                    String industy_nm = jObject.getString("industryname");

                                    //   sb.append(Skill_nm);

                                    if (ans_industry_nm=="" || ans_industry_nm==null)
                                    {
                                        ans_industry_nm = industy_nm;
                                    }
                                    else
                                    {
                                        ans_industry_nm += "," + industy_nm;
                                    }

                                   // arrayList_industry.add(industy_Id);
                                   // arrayList_industry_id.add(industy_nm);
                                }
                                count++;
                            }
                        }
                    }
                    //tagsEditText_skill.setText(ans_skill_nm);
                    if(!ans_industry_nm.equals("") || !ans_industry_nm.equals(null))
                    {
                        String[] split_industry = ans_industry_nm.split(",");
                        for (int i = 0; i < split_industry.length; i++) {
                            // int count = 0;
                            String sss = split_industry[i].toString();

                            if (sss != null && !sss.equals("") && !sss.equals("0")) {
                                getUpdateData(sss);
                                ed_all_industries.setText(null);
                                tagsRecyclerView.setLayoutManager(gridLayout);
                                tagsRecyclerView.setAdapter(adapter);
                                // String temp= String.valueOf(recyclerModels.get(0));
                            }
                        }
                    }
                    //************
                    //ed_all_industries.setText(ans_industry_nm);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

               // ed_all_industries.setThreshold(1);
               // ed_all_industries.setAdapter(adapter_industry);
                // ed_skill.setThreshold(1);
                //ed_skill.setAdapter(adapter_skill);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ProfileSetup2.this, "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //   listview_home.setEmptyView(tv_empty);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(ProfileSetup2.this);
        rQueue.add(json_skill);
    }


    public void call_allIndustries_webservice()
    {
        progressDialog.show();
        String url_Skill = webservice_AllIndustery_url.toString();

        JsonObjectRequest json_skill = new JsonObjectRequest(Request.Method.GET, url_Skill, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetAllIndusteryListResult");

                    int count = 0;

                    while (count < resultJsonArr.length()) {

                        JSONObject jObject = resultJsonArr.getJSONObject(count);

                        String industy_Id = jObject.getString("industryId");
                        String industy_nm= jObject.getString("industryname");
                        //   rowItem_skill.setSkill_id(Skill_Id);
                        //    rowItem_skill.setSkill_nm(Skill_nm);
                        arrayList_industry.add(industy_nm);
                        arrayList_industry_id.add(industy_Id);
                        count++;
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                ed_all_industries.setThreshold(1);
                ed_all_industries.setAdapter(adapter_industry);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ProfileSetup2.this, "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //   listview_home.setEmptyView(tv_empty);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(json_skill);
    }
    public void update_profile_webservices()
    {

        int recyclerModels_count=recyclerModels.size();
        String ans_industry=null;

        for (int i=0; i<recyclerModels_count;i++) {

            String industry = recyclerModels.get(i).getTagText();

            if (ans_industry == "" || ans_industry == null) {
                ans_industry = industry;
            } else {
                ans_industry += "," + industry;
            }
            ans_industry = ans_industry.replace(" ", "%20");//Remove space
            ans_industry = ans_industry.replace("/", "%20");
            ans_industry = ans_industry.replace("&", "%20");
            ans_industry = ans_industry.replace("\r\n", "");



        }
        str_next_job_title =ed_job_title.getText().toString();
        if(str_next_job_title != null && !str_next_job_title.isEmpty() && !str_next_job_title.equals("null"))
        {
            str_next_job_title= str_next_job_title.replace(" ", "%20");//Remove space
        }


        if(ans_industry == null || ans_industry.isEmpty() || ans_industry.equals("null") || ans_industry.equals(""))
        {
            Toast.makeText(ProfileSetup2.this, "Please enter industry", Toast.LENGTH_LONG).show();

        }
        else  if(str_next_job_title== null || str_next_job_title.isEmpty() || str_next_job_title.equals("null") || str_next_job_title.equals(""))
        {
            Toast.makeText(ProfileSetup2.this, "Please enter next job title", Toast.LENGTH_LONG).show();
        }
        else
        {
            progressDialog.show();


            if(str_location != null && !str_location.isEmpty() && !str_location.equals("null"))
            {
                str_location= str_location.replace(" ", "%20");//Remove space
            }
            if(str_skills != null && !str_skills.isEmpty() && !str_skills.equals("null"))
            {
                str_skills= str_skills.replace(" ", "%20");//Remove space
            }
            if(ans_industry != null && !ans_industry.isEmpty() && !ans_industry.equals("null"))
            {
                ans_industry= ans_industry.replace(" ", "%20");//Remove space
            }



            // ans_industry= ans_industry.replace(" ", "%20");
            //http://34.214.133.27:82/GotHireServices.svc/InsertProfiel/{candidateid}/{CanFirstName}/{CanLastName}/{Email}/{MobileNo}/{skilllst}/{IndusteryList}
            String url_update_profile = webservice_Update_profile_url.toString() + Candidate_id + "/" + str_first_nm + "/" + str_last_nm + "/" + str_email_add + "/" + str_phone + "/" + str_skills + "/" + ans_industry + "/" + profile_path + "/" + str_location + "/" + str_next_job_title + "/" + str_education + "/" + str_school;
            //Toast.makeText(ProfileSetup2.this, "Profile string-"+url_update_profile, Toast.LENGTH_LONG).show();

            JsonObjectRequest json_updt_profile = new JsonObjectRequest(Request.Method.GET, url_update_profile, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Result", response.toString());
                    String result = response.toString();
                    try {
                        String result_status = null;
                        result_status = response.getString("InsertProfielResult");

                        if (result_status.equals("SucessFully Updated")) {
                            Toast.makeText(ProfileSetup2.this, "Profile is updated", Toast.LENGTH_LONG).show();
                            Intent intent_welcome = new Intent(getApplicationContext(), MainContainer.class);
                            intent_welcome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent_welcome.putExtra("Key_candidate_id", Candidate_id);
                            startActivity(intent_welcome);

                            // finish();//Finished current activity and open bacground fragment

//                        args = new Bundle();
//                        args.putString("Key_candidate_id", Candidate_id);
//                        loadFragment(new Home());
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    ed_all_industries.setThreshold(1);
                    ed_all_industries.setAdapter(adapter_industry);
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(ProfileSetup2.this, "Please try again", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    //   listview_home.setEmptyView(tv_empty);
                }
            }) {
            };
            RequestQueue rQueue = Volley.newRequestQueue(this);
            rQueue.add(json_updt_profile);
        }
    }
    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}