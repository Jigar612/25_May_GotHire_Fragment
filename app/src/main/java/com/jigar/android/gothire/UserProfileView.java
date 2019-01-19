package com.jigar.android.gothire;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.jigar.android.gothire.Adapter.Adapter_ApplyForJob;
import com.jigar.android.gothire.Adapter.Adapter_Education;
import com.jigar.android.gothire.Adapter.Adapter_Experience;
import com.jigar.android.gothire.Adapter.RecyclerAdapterView;
import com.jigar.android.gothire.SetterGetter.RowItemEducation;
import com.jigar.android.gothire.SetterGetter.RowItemExpirence;
import com.jigar.android.gothire.SetterGetter.RowItem_ApplyForJob;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileView extends Fragment {

    View view;
    Bundle args;
    String Candidate_id;
    String get_Skill_id;
    String ans_skill_nm="";
    String ans_industry_nm="";
    String get_industry_id="";

    TextView tv_user_nm,tv_disp_expiriance,tv_disp_education,tv_disp_skill,tv_disp_industry_fancy,tv_next_job_title;
    TextView tv_user_location, tv_user_profile_desc,tv_expiriance_empty ,tv_education_empty;
    CircleImageView img_user;
    ProgressDialog progressDialog;

    String webservice_url = UrlString.URL + "GetCandidate/";
    String webservice_AllSkills_url = UrlString.URL + "GetAllSkills";

    ArrayList<String> arrayList_skill =new ArrayList<>();;
    ArrayAdapter<String> adapter_skill;

    RecyclerAdapterView adapter;
    RecyclerView tagsRecyclerView;
    ArrayList<RecyclerModel> recyclerModels = new ArrayList<>( );
    ArrayList<String> arrayList_id=new ArrayList<>();
    FlexboxLayoutManager gridLayout;

   // ArrayAdapter<String> adapter_industry;
    RecyclerAdapterView adapter_recycler_industry;
    RecyclerView tagsRecyclerView_industry;
    ArrayList<RecyclerModel> recyclerModels_industry = new ArrayList<>(  );
    //ArrayList<String> arrayList_industry=new ArrayList<>();
   // ArrayList<String> arrayList_industry_id=new ArrayList<>();;
    Button btn_logout;
    ImageView img_btn_edit;


    FlexboxLayoutManager gridLayout_indusrty;

    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";

    //New Changes
    ArrayList<RowItemEducation> arrayList_education = new ArrayList<>();
    ArrayList<RowItemExpirence> arrayList_experience= new ArrayList<>();

    ListView listView_education;
    ListView listView_experience;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_user_profile_view, container, false);
        MainContainer.TAG="UserProfile";
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        MainContainer.bottomNavigationView.getMenu().findItem(R.id.bottom_nav_account).setChecked(true);

        Bundle get_data = getArguments();
        Candidate_id = get_data.getString("Key_candidate_id");


        btn_logout=(Button)view.findViewById(R.id.btn_logount);
        img_btn_edit =(ImageView)view.findViewById(R.id.img_btn_edit_profile);
        img_user=(CircleImageView)view.findViewById(R.id.img_user_profile);
        //Medium Font
        tv_user_nm=(TextView)view.findViewById(R.id.tv_user_nm);
        tv_disp_expiriance=(TextView)view.findViewById(R.id.tv_disp_expiriance);
        tv_disp_education=(TextView)view.findViewById(R.id.tv_disp_education);
        tv_disp_skill=(TextView)view.findViewById(R.id.tv_disp_skill);
        tv_disp_industry_fancy=(TextView)view.findViewById(R.id.tv_disp_industry_fancy);
        tv_next_job_title = (TextView)view.findViewById(R.id.tv_next_job_title);

        tv_user_location=(TextView)view.findViewById(R.id.tv_user_location);
        tv_user_profile_desc=(TextView)view.findViewById(R.id.tv_user_profile_desc);

        listView_education=(ListView)view.findViewById(R.id.listview_education);
        listView_experience=(ListView)view.findViewById(R.id.listview_experience);
        tv_expiriance_empty=(TextView)view.findViewById(R.id.tv_expiriance_empty);
        tv_education_empty=(TextView)view.findViewById(R.id.tv_education_empty);

        tagsRecyclerView = view.findViewById( R.id.tagsRecyclerView );
        tagsRecyclerView_industry= view.findViewById( R.id.tagsRecyclerView_industry );

        Typeface roboto_Med = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Medium.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Light.ttf");

        tv_user_nm.setTypeface(roboto_Med);
        tv_disp_expiriance.setTypeface(roboto_Med);
        tv_disp_education.setTypeface(roboto_Med);
        tv_disp_skill.setTypeface(roboto_Med);
        tv_disp_industry_fancy.setTypeface(roboto_Med);

        tv_user_location.setTypeface(roboto_Light);
        tv_user_profile_desc.setTypeface(roboto_Light);
      //  tv_expiriance.setTypeface(roboto_Light);
    //    tv_education.setTypeface(roboto_Light);
        tv_next_job_title.setTypeface(roboto_Light);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        runTask();


        return view;
    }
    public void runTask() {
        ConnectivityManager connec =
                (ConnectivityManager) getActivity().getSystemService(getActivity().getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            call_allSkills_webservice();

            adapter_skill = new ArrayAdapter<String>(getActivity(),R.layout.spinner_text, arrayList_skill);
            adapter_skill.setDropDownViewResource(R.layout.spinner_text);

            adapter = new RecyclerAdapterView(getActivity(), recyclerModels);
            gridLayout = new FlexboxLayoutManager(getActivity());

            adapter_recycler_industry = new RecyclerAdapterView(getActivity(), recyclerModels_industry);
            gridLayout_indusrty = new FlexboxLayoutManager(getActivity());

            call_profileGetData_webservices();

            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Logout ",Toast.LENGTH_LONG).show();
                sharedpreferences_id = getActivity().getSharedPreferences(mypreference_id,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_id = sharedpreferences_id.edit();
                editor_id.clear();
                editor_id.commit();

                Intent logout_intent = new Intent(getActivity(), Login_with.class);
                Toast.makeText(getActivity(), "Logout ",Toast.LENGTH_LONG).show();
                logout_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(logout_intent);
                }
            });

            img_btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_welcome = new Intent(getActivity(),ProfileSetup.class); //First we opened MainContainer.class
                    intent_welcome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent_welcome.putExtra("Key_candidate_id",Candidate_id);
                   // intent_welcome.putExtra("Key_candidate_nm", Candidate_nm);
                    startActivity(intent_welcome);
                }
            });
        }
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
                    String CanFirstName=null;
                    String CanLastName=null;
                    String CityName=null;
                    String Countryname=null;
                    String ProfileImage=null;
                    String Locations=null;
                 //   String Workexpernice=null;
                //    String Education = null;

                    String JobTitle=null;
                 //   String Company=null;
                    String Skill=null;
                    String Industery=null;
                   // String JobType=null;

                    //For Secon Array
                    int count_edu_master=0;
                    String School=null,FiledOfStdy=null,FromDate=null,Todate=null;

                    //For third Array
                    int count_work_history=0;
                    String work_hist_City=null,work_hist_CompaneyName=null,work_hist_Designation=null,work_hist_FromDate=null;
                    String work_hist_TODate=null,work_hist_descriptions=null;

                    while (count < resultJsonArr.length()) {
                        JSONObject jObject = resultJsonArr.getJSONObject(count);
                        CanFirstName= jObject.getString("CanFirstName");
                        CanLastName= jObject.getString("CanLastName");
                        CityName= jObject.getString("CityName");
                        Countryname= jObject.getString("Countryname");
                        ProfileImage = jObject.getString("ProfileImage");
                        Locations = jObject.getString("Locations");



                        //First Array Education
                        JSONArray JsonArrGetEducationMaster = jObject.getJSONArray("EducationMaster");
                        while (count_edu_master<JsonArrGetEducationMaster.length()) {
                            JSONObject jsonObjectEduMaster = JsonArrGetEducationMaster.getJSONObject(count_edu_master);
                            RowItemEducation rowItemEducation = new RowItemEducation();


                            FiledOfStdy= jsonObjectEduMaster.getString("FiledOfStdy");
                            FromDate= jsonObjectEduMaster.getString("FromDate");
                            School= jsonObjectEduMaster.getString("School");
                            Todate= jsonObjectEduMaster.getString("Todate");

                            rowItemEducation.setFiledOfStdy(FiledOfStdy);
                            rowItemEducation.setFromDate(FromDate);
                            rowItemEducation.setTodate(Todate);
                            rowItemEducation.setSchool(School);

                            arrayList_education.add(rowItemEducation);
                            count_edu_master++;

                        }

                        //Second Array work;
                        JSONArray JsonArrGetWorkHistoryMasters = jObject.getJSONArray("WorkHistoryMasters");
                        while (count_work_history<JsonArrGetWorkHistoryMasters.length()) {
                            JSONObject jsonObjectWorkHisMasters = JsonArrGetWorkHistoryMasters.getJSONObject(count_work_history);
                            RowItemExpirence rowItemExpirence = new RowItemExpirence();


                            work_hist_City= jsonObjectWorkHisMasters.getString("City");
                            work_hist_CompaneyName= jsonObjectWorkHisMasters.getString("CompaneyName");
                            work_hist_Designation= jsonObjectWorkHisMasters.getString("Designation");
                            work_hist_FromDate= jsonObjectWorkHisMasters.getString("FromDate");
                            work_hist_TODate =jsonObjectWorkHisMasters.getString("TODate");
                            work_hist_descriptions =jsonObjectWorkHisMasters.getString("descriptions");

                            rowItemExpirence.setWork_hist_City(work_hist_City);
                            rowItemExpirence.setWork_hist_CompaneyName(work_hist_CompaneyName);
                            rowItemExpirence.setWork_hist_Designation(work_hist_Designation);
                            rowItemExpirence.setWork_hist_FromDate(work_hist_FromDate);
                            rowItemExpirence.setWork_hist_TODate(work_hist_TODate);
                            rowItemExpirence.setWork_hist_descriptions(work_hist_descriptions);
                            arrayList_experience.add(rowItemExpirence);
                            count_work_history++;

                        }


                     //   Workexpernice = jObject.getString("Workexpernice");new change then not required
                    //    Education = jObject.getString("Educationname");new change then not required
                    //    JobType = jObject.getString("JobType");

//                        School = jObject.getString("Institute");
                       JobTitle = jObject.getString("ResumeTitle");
//                        Company = jObject.getString("companyname");
                        // Skill = jObject.getString("SkillName");
                        Skill = jObject.getString("skill");

                        Industery = jObject.getString("Industery");

                        count++;
                    }
                   // listView_experience.setScrollContainer(false);
                  //  listView_education.setScrollContainer(false);

                    Adapter_Education  adapter_education = new Adapter_Education(arrayList_education,getActivity().getLayoutInflater(), getActivity());
                    listView_education.setAdapter(adapter_education);
                    Adapter_Experience  adapter_experience = new Adapter_Experience(arrayList_experience,getActivity().getLayoutInflater(), getActivity());
                    listView_experience.setAdapter(adapter_experience);
                    UIUtils.setListViewHeightBasedOnItems(listView_experience);
                    UIUtils.setListViewHeightBasedOnItems(listView_education);

                    listView_education.setEmptyView(tv_education_empty);
                    tv_education_empty.setText(" ");
                    listView_experience.setEmptyView(tv_expiriance_empty);
                    tv_education_empty.setText(" ");


                    get_industry_id=Industery;
                    call_webservice_industryid_to_name();

                    get_Skill_id=Skill;
                    call_webservice_Skillid_to_name();

                    tv_user_nm.setText(CanFirstName+" "+CanLastName);
//                    if(!CityName.equals("") || !Countryname.equals("")) {
//                        tv_user_location.setText(CityName + "," + Countryname);
//                    }
                    tv_user_location.setText(Locations);


//                    if(!Workexpernice.equals("") && !Workexpernice.equals("0") && !Workexpernice.equals("null")) {
//                        tv_expiriance.setText(Workexpernice);
//                    }
//                    tv_education.setText(Education);

                    if(!JobTitle.equals("") && !JobTitle.equals("null")) {
                        tv_next_job_title.setText(JobTitle);
                    }


                    //set user profile image
                    if(!ProfileImage.equals("null") || !ProfileImage.equals(null) )
                    {
                       // String path=LiveLink.LinkLive2+"/answers/"+ProfileImage;
                    //    String path=LiveLink.LinkLive2+"/CandidateImage/"+ProfileImage;
                        //String path=LiveLink.LinkLive2+"/CandidateImage/"+ans_file_webservices;
                       String path=LiveLink.LinkLive2+ProfileImage;
                        Picasso.with(getActivity())
                                .load(path)
                                .error(R.drawable.ic_no_img_avilible)//if no image on server then show..
                                .into(img_user);
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                //   listview_home.setEmptyView(tv_empty);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
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
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //   listview_home.setEmptyView(tv_empty);
            }
        }) {};
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(json_skill);
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
                        String []split_industry = ans_industry_nm.split(",");
                        for(int i=0;i<split_industry.length;i++) {
                            // int count = 0;
                            String sss = split_industry[i].toString();

                            if (!sss.equals("") && !sss.equals("0")) {
                                getUpdateData_industry(sss);
                                //ed_all_industries.setText(null);
                                tagsRecyclerView_industry.setLayoutManager(gridLayout_indusrty);
                                tagsRecyclerView_industry.setAdapter(adapter_recycler_industry);
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
                Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //   listview_home.setEmptyView(tv_empty);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
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
                    //   get_Skill_id=",0,1,2,3";
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
                    if(!ans_skill_nm.equals("") || !ans_skill_nm.equals(null)) {


                        String[] split_skill = ans_skill_nm.split(",");
                        for (int i = 0; i < split_skill.length; i++) {
                            // int count = 0;
                            String sss = split_skill[i].toString();

                            if (sss != null && !sss.equals("")  && !sss.equals("0")) {
                                getUpdateData(sss);
                                //tagsEditText_skill.setText(null);
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
                Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //   listview_home.setEmptyView(tv_empty);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(json_skill);
    }
    private void getUpdateData(String str) {
        RecyclerModel model = new RecyclerModel( str );
        recyclerModels.add( model );
    }
    private void getUpdateData_industry(String str) {
        RecyclerModel model = new RecyclerModel( str );
        recyclerModels_industry.add( model );
    }


}