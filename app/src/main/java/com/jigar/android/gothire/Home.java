package com.jigar.android.gothire;


import android.*;
import android.Manifest;
import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;


import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jigar.android.gothire.Adapter.AdapterHomeAdapter;
import com.jigar.android.gothire.Adapter.Adapter_ApplyForJob;
import com.jigar.android.gothire.SetterGetter.RowItemGetAcceptedJobResult;
import com.jigar.android.gothire.SetterGetter.RowItem_ApplyForJob;

import com.nex3z.notificationbadge.NotificationBadge;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;

public class Home extends Fragment {

    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";

    SharedPreferences sharedpreferences_counter;
    public static final String myPref_counter= "mypref_counter";
    public static final String Counter_key = "counter_key";
    int new_notification_counter;
    NotificationBadge mBadge;

    String webservice_url = UrlString.URL + "GetAllHomeJob/";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String candidate_id_value;

    TextView Active, under_rev, completed;
    TextView tv_title;
    ListView listView_jobs;
    TextView tv_empty;
    ProgressDialog progressDialog;

    //New changes 24-sept
    int next_page=1;
    String no_of_record="10";
    public  boolean isLoading=false;
    public View ftView;
    public android.os.Handler mHandler;
    int count = 0;
    //
    //For using Searching job...
    SearchView searchView;
    String keyword_upper,keyword_lower,keyword_filst_lt_capital;

    ArrayList<RowItem_ApplyForJob> arrayList;
    ArrayList<RowItem_ApplyForJob> arrayList_search;
    AdapterHomeAdapter searchJob_adapter;
    Adapter_ApplyForJob adapter;
    //**********
    View view;
    Toolbar toolbar;
    Bundle args;
    FrameLayout frameLayout;

  //  public FancyShowCaseView fanyview1;
    //FancyShowCaseView fancyview5;

    //SharedPreferences spf_home_fancyview;
 //   public static final String myPref_home_fancyview = "home_fancyview";
  //  public static final String key_fancyview = "key_fancyview";
  //  int Id_fancy_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        frameLayout = new FrameLayout(getActivity());
        view = inflater.inflate(R.layout.activity_home, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        MainContainer.bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);
        // ***for home_fancyShowcase View
        //spf_home_fancyview = getActivity().getSharedPreferences(myPref_home_fancyview,
         //       Context.MODE_PRIVATE);

//        if (spf_home_fancyview.contains(key_fancyview)) {
//             Id_fancy_view = spf_home_fancyview.getInt(key_fancyview, 0);
//        }
        MainContainer.TAG = "Home";
        //***********
        Bundle get_data = getArguments();
        candidate_id_value = get_data.getString("Key_candidate_id");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        checkAndRequestPermissions();
        runTask();
        frameLayout .addView(view);
        return frameLayout;
    }
    private  boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int capt_audio = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAPTURE_AUDIO_OUTPUT);
        int read_extrnal = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int internet = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET);

        int location_coa = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int location_fine = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

        int mic = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.RECORD_AUDIO);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (internet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (mic != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECORD_AUDIO);
        }
        if (capt_audio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAPTURE_AUDIO_OUTPUT);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (read_extrnal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (location_fine != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (location_coa != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(getActivity(),listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    public void runTask() {
        ConnectivityManager connec =
                (ConnectivityManager) getActivity().getSystemService(getActivity().getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            listView_jobs = (ListView)view.findViewById(R.id.listView2_wellcome);
            tv_empty = (TextView) view.findViewById(R.id.welcome_tv_empty_list);//tv_empty_list_home
            TextView tv_job_got = (TextView) view.findViewById(R.id.job_got);//tv_empty_list_home
          //  TextView tv_sponsred = (TextView) view.findViewById(R.id.sponsored);//tv_empty_list_home

            Typeface roboto_Reg = Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/Roboto-Regular.ttf");
            Typeface roboto_Light = Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/Roboto-Light.ttf");
            tv_empty.setTypeface(roboto_Light);
            tv_job_got.setTypeface(roboto_Reg);
         //   tv_sponsred.setTypeface(roboto_Light);

            toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            getActivity().setTitle("");

             arrayList = new ArrayList<RowItem_ApplyForJob>();
             arrayList_search = new ArrayList<RowItem_ApplyForJob>();

             call_webservice();

        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            // Toast.makeText(LeadListActivity.this, " Please check your internet connection.", Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

        mHandler = new MyHandler();

        LayoutInflater li=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView = li.inflate(R.layout.footer_home_listview,null);

        listView_jobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), " Click on list item"+view.getTag(), Toast.LENGTH_LONG).show();
            }
        });
         listView_jobs.setOnScrollListener(new AbsListView.OnScrollListener() {
             @Override
             public void onScrollStateChanged(AbsListView view, int scrollState) {

             }

             @Override
             public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                 //Check when last items of listview with scrollview
                 if(view.getLastVisiblePosition()== totalItemCount-1 && listView_jobs.getCount()>=10 && isLoading == false){
                     isLoading = true;
                     Thread thread = new ThreadGetMoreData();
                     thread.start();
                 }

             }
         });
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    public void call_webservice() {
        progressDialog.show();


        String url_welcome = webservice_url.toString() + candidate_id_value +"/"+ next_page +"/"+no_of_record ;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_welcome, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();

                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetAllHomeJobResult");

                    while (count <resultJsonArr.length())
                    {
                        RowItem_ApplyForJob rowItem_applyForJob = new RowItem_ApplyForJob();
                        JSONObject jObject = resultJsonArr.getJSONObject(count);

                        String CompanyName=jObject.getString("company");
                        String JobTitle=jObject.getString("jobtitle");
                        //String ExperinceFrom=jObject.getString("ExperinceFrom");
                       // String ExpriceTO=jObject.getString("ExpriceTO");
                        String Skillname=jObject.getString("Skills");

                        String Languageknown = jObject.getString("Langknown");
                        //String JobDescription=jObject.getString("JobDescription");
                        String JobDescription=jObject.getString("snippet");
                        String SalaryFrom=jObject.getString("salaryFrom");
                        String SalaryTo=jObject.getString("salaryTo");
                        String JobLocation=jObject.getString("Location");
                        //String JobPostId=jObject.getString("JobPostId");
                        String JobPostId=jObject.getString("jobID");
                        // String JobPostId=jObject.getString("JobType");
                        String JobType=jObject.getString("EmploymentType");
                        String postedOn=jObject.getString("postedOn");
                        //String Image_path=jObject.getString("Logo");//ImagePath
                        String Image_path=jObject.getString("CompanyLogo");//ImagePath
                        String Certificate=jObject.getString("Certificate");//Ciertificate
                        String Education=jObject.getString("Education");//Education
                        String VideoDesc=jObject.getString("VideoDesc");//videodesc

                        String url=jObject.getString("url");

                        rowItem_applyForJob.setCompanyName(CompanyName);
                        rowItem_applyForJob.setJobTitle(JobTitle);
                        rowItem_applyForJob.setSkillname(Skillname);
                        rowItem_applyForJob.setLanguageKnwon(Languageknown);
                        rowItem_applyForJob.setJobDescription(JobDescription);
                        rowItem_applyForJob.setSalaryFrom(SalaryFrom);
                        rowItem_applyForJob.setSalaryTo(SalaryTo);
                        rowItem_applyForJob.setJobLocation(JobLocation);
                        rowItem_applyForJob.setJobPostId(JobPostId);
                        rowItem_applyForJob.setCandidateid(candidate_id_value);
                        rowItem_applyForJob.setJobType(JobType);
                        //New
                        rowItem_applyForJob.setCertificate(Certificate);
                        rowItem_applyForJob.setEducation(Education);
                        rowItem_applyForJob.setUrl(url);
                        rowItem_applyForJob.setUrl(url);
                        rowItem_applyForJob.setPostedOn(postedOn);
                        rowItem_applyForJob.setImage_path(Image_path);

                        rowItem_applyForJob.setVideoDesc(VideoDesc);

                        arrayList.add(rowItem_applyForJob);
                        count++;
                    }
                    progressDialog.dismiss();
                    adapter = new Adapter_ApplyForJob(arrayList,getActivity().getLayoutInflater(), getActivity());
                    listView_jobs.setAdapter(adapter);
                    listView_jobs.setEmptyView(tv_empty);
                    tv_empty.setText("Oops! no matches jobs");
//                    if(!arrayList.isEmpty())
//                    {
//                        fancyview5.show();
//                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
              //  Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                tv_empty.setText("Oops! no jobs found");
                listView_jobs.setEmptyView(tv_empty);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjReq);
    }

    public class MyHandler extends android.os.Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    listView_jobs.addFooterView(ftView);
                    break;
                case 1:
                    adapter.addListItemAdapter((ArrayList<RowItem_ApplyForJob>)msg.obj);
                    //remove fotterview after update listview
                    listView_jobs.removeFooterView(ftView);
                    isLoading=false;
                    break;
                default:
                    break;

            }
        }
    }
    private ArrayList<RowItem_ApplyForJob> getMoreData()
    {
        ArrayList<RowItem_ApplyForJob>list =new ArrayList<>();
       // call_webservice();
        next_page++;
        call_webservice_with_pageNo(next_page);

        return list;
    }
    public class ThreadGetMoreData extends Thread
    {
        @Override
        public void run() {
            //Add Footer after get data here
            mHandler.sendEmptyMessage(0);
            //Search More data here
            ArrayList<RowItem_ApplyForJob> listResult = getMoreData();

            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = mHandler.obtainMessage(1,listResult);
            mHandler.sendMessage(msg);


        }
    }
    public void call_webservice_with_pageNo(int next_page) {
       // progressDialog.show();


        String url_welcome = webservice_url.toString() + candidate_id_value +"/"+ next_page+"/"+no_of_record ;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_welcome, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();

                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetAllHomeJobResult");
                    int count2 = 0;
                    while (count2 <resultJsonArr.length())
                    {
                        RowItem_ApplyForJob rowItem_applyForJob = new RowItem_ApplyForJob();
                        JSONObject jObject = resultJsonArr.getJSONObject(count2);

                        String CompanyName=jObject.getString("company");
                        String JobTitle=jObject.getString("jobtitle");
                        String Skillname=jObject.getString("Skills");
                        String Languageknown = jObject.getString("Langknown");
                        String JobDescription=jObject.getString("snippet");
                        String SalaryFrom=jObject.getString("salaryFrom");
                        String SalaryTo=jObject.getString("salaryTo");
                        String JobLocation=jObject.getString("Location");
                        //String JobPostId=jObject.getString("JobPostId");
                        String JobPostId=jObject.getString("jobID");
                        String JobType=jObject.getString("EmploymentType");
                        String Image_path=jObject.getString("CompanyLogo");//ImagePath
                        String postedOn=jObject.getString("postedOn");
                        String url=jObject.getString("url");

                        String Certificate=jObject.getString("Certificate");//Ciertificate
                        String Education=jObject.getString("Education");//Education
                        String VideoDesc=jObject.getString("VideoDesc");//videodesc

                        rowItem_applyForJob.setVideoDesc(VideoDesc);

                        rowItem_applyForJob.setCompanyName(CompanyName);
                        rowItem_applyForJob.setJobTitle(JobTitle);
                        rowItem_applyForJob.setSkillname(Skillname);
                        rowItem_applyForJob.setLanguageKnwon(Languageknown);
                        rowItem_applyForJob.setJobDescription(JobDescription);
                        rowItem_applyForJob.setSalaryFrom(SalaryFrom);
                        rowItem_applyForJob.setSalaryTo(SalaryTo);
                        rowItem_applyForJob.setJobLocation(JobLocation);
                        rowItem_applyForJob.setJobPostId(JobPostId);
                        rowItem_applyForJob.setCandidateid(candidate_id_value);
                        rowItem_applyForJob.setJobType(JobType);
                        //New
                        rowItem_applyForJob.setUrl(url);
                        rowItem_applyForJob.setPostedOn(postedOn);
                        rowItem_applyForJob.setImage_path(Image_path);

                        rowItem_applyForJob.setCertificate(Certificate);
                        rowItem_applyForJob.setEducation(Education);
                        arrayList.add(rowItem_applyForJob);
                        count2++;
                    }
                    count=count+count2;
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //  Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
               // progressDialog.dismiss();
             //   tv_empty.setText("Oops! no jobs found");
                //listView_jobs.setEmptyView(tv_empty);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjReq);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
       // this.menu=menu;


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        ImageView imageBtn_notification = (ImageView)getActivity().findViewById(R.id.notification);
        imageBtn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedpreferences_counter = getActivity().getSharedPreferences(myPref_counter,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_counter = sharedpreferences_counter.edit();
                editor_counter.clear();
                editor_counter.commit();


                Home homeFragment = new Home();
                args = new Bundle();
                args.putString("Key_candidate_id",candidate_id_value);

                FragmentManager fm =  getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                homeFragment.setArguments(args);
                fragmentTransaction.replace(R.id.frameLayout, homeFragment);
                fragmentTransaction.commit(); // save the changes

                Notification notification = new Notification();
               // args.putString("Key_candidate_id",candidate_id_value);
                FragmentManager fm_noti=  getFragmentManager();
                FragmentTransaction fragmentTransaction_noti = fm_noti.beginTransaction();
                notification.setArguments(args);
                fragmentTransaction_noti.addToBackStack(null);
                fragmentTransaction_noti.add(R.id.frameLayout, notification);
                fragmentTransaction_noti.commit(); // save the ch
            }
        });
        ImageView imageBtn_menu_job_search_filter = (ImageView)getActivity().findViewById(R.id.job_search_filter);
        imageBtn_menu_job_search_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Home homeFragment = new Home();
//                args = new Bundle();
//                args.putString("Key_candidate_id",candidate_id_value);
//
//                FragmentManager fm =  getFragmentManager();
//                FragmentTransaction fragmentTransaction = fm.beginTransaction();
//                homeFragment.setArguments(args);
//                fragmentTransaction.replace(R.id.frameLayout, homeFragment);
//                fragmentTransaction.commit(); // save the changes
//
//                JobSearchFilter jobSearchFilter = new JobSearchFilter();
//                // args.putString("Key_candidate_id",candidate_id_value);
//                FragmentManager fm_noti=  getFragmentManager();
//                FragmentTransaction fragmentTransaction_noti = fm_noti.beginTransaction();
//                jobSearchFilter.setArguments(args);
//                fragmentTransaction_noti.addToBackStack(null);
//                fragmentTransaction_noti.add(R.id.frameLayout, jobSearchFilter);
//                fragmentTransaction_noti.commit(); // save the ch

                Intent intent_welcome = new Intent(getActivity(),JobSearchFilter.class); //First we opened MainContainer.class
               // intent_welcome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_welcome.putExtra("Key_candidate_id", candidate_id_value);
                //intent_welcome.putExtra("Key_candidate_nm", );
                startActivity(intent_welcome);
            }
        });



//        MenuItem item = menu.findItem(R.id.notification);
//        MenuItemCompat.setActionView(item, R.layout.notification_count);
        //RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(imageBtn_notification);
      //  RelativeLayout notifCount = (RelativeLayout) getActionView(imageBtn_notification);

        //ImageView imageBtn_notification=(ImageView) notifCount.findViewById(R.id.img_notification);
        mBadge = (NotificationBadge)view.findViewById(R.id.badge);
//
        sharedpreferences_counter = getActivity().getSharedPreferences(myPref_counter,
                Context.MODE_PRIVATE);
        if (sharedpreferences_counter.contains(Counter_key)) {
            new_notification_counter = sharedpreferences_counter.getInt(Counter_key, 0);
        }
        if(new_notification_counter==0)
        {
            mBadge.setNumber(0);
            mBadge.setVisibility(View.GONE);
        }
        else
        {
            mBadge.setVisibility(View.VISIBLE);
            mBadge.setNumber(new_notification_counter);
        }
//        imageBtn_notification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sharedpreferences_counter = getActivity().getSharedPreferences(myPref_counter,
//                        Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor_counter = sharedpreferences_counter.edit();
//                editor_counter.clear();
//                editor_counter.commit();
//                //Call  new home fragment(replaced home fragment) for refreshed and add notification fragment
//                Home homeFragment = new Home();
//                args = new Bundle();
//                args.putString("Key_candidate_id",candidate_id_value);
//
//                FragmentManager fm =  getFragmentManager();
//                FragmentTransaction fragmentTransaction = fm.beginTransaction();
//                homeFragment.setArguments(args);
//                fragmentTransaction.replace(R.id.frameLayout, homeFragment);
//                fragmentTransaction.commit(); // save the changes
//
//                Notification notification = new Notification();
//               // args.putString("Key_candidate_id",candidate_id_value);
//                FragmentManager fm_noti=  getFragmentManager();
//                FragmentTransaction fragmentTransaction_noti = fm_noti.beginTransaction();
//                notification.setArguments(args);
//                fragmentTransaction_noti.addToBackStack(null);
//                fragmentTransaction_noti.add(R.id.frameLayout, notification);
//                fragmentTransaction_noti.commit(); // save the ch
//            }
//        });
        final EditText searchView=(EditText)getActivity().findViewById(R.id.searchEditText);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                //adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                //                Log.e("queryText",s);
                String s = searchView.getText().toString().toLowerCase(Locale.getDefault());
                Log.d("Search data",s);
                arrayList_search.clear();
                keyword_upper = s.toUpperCase(); //Get keyword from searchbar editbox.
                keyword_lower = s.toLowerCase();
                keyword_filst_lt_capital= StringUtils.capitalize(s);
                //checking language arraylist for items containing search keyword
                for(int i =0 ;i < arrayList.size();i++){
                    //  RowItemGetAcceptedJobResult rowItemGetAcceptedJobResult = new RowItemGetAcceptedJobResult();
                    RowItem_ApplyForJob rowItemGetAcceptedJobResult=new RowItem_ApplyForJob();
                    // if(arrayList.get(i).getJobTitle().contains(keyword)){
                    if(arrayList.get(i).getJobTitle().toLowerCase().contains(keyword_lower)){// || arrayList.get(i).getJobTitle().contains(keyword_lower) || arrayList.get(i).getJobTitle().contains(keyword_filst_lt_capital)){
                        rowItemGetAcceptedJobResult.setJobTitle(arrayList.get(i).getJobTitle().toString());
                        rowItemGetAcceptedJobResult.setCompanyName(arrayList.get(i).getCompanyName());
                        rowItemGetAcceptedJobResult.setSalaryFrom(arrayList.get(i).getSalaryFrom());
                        rowItemGetAcceptedJobResult.setSalaryTo(arrayList.get(i).getSalaryTo());
                        rowItemGetAcceptedJobResult.setJobLocation(arrayList.get(i).getJobLocation());
                        rowItemGetAcceptedJobResult.setImage_path(arrayList.get(i).getImage_path());
                        rowItemGetAcceptedJobResult.setUrl(arrayList.get(i).getUrl());
                        rowItemGetAcceptedJobResult.setPostedOn(arrayList.get(i).getPostedOn());

                        String JobTitle = arrayList.get(i).getJobTitle();
                        String cpyName = arrayList.get(i).getCompanyName();
                        //   String Salary=jObject.getString("Salary");
                        // String city=jObject.getString("city");
                        String JobPostId = arrayList.get(i).getJobPostId();
                        String JobDescription =arrayList.get(i).getCompanyName();
                        String JobType =arrayList.get(i).getJobType();
                        String Image_path=arrayList.get(i).getImage_path();
                        String url =arrayList.get(i).getUrl();
                        String postedOn=arrayList.get(i).getPostedOn();

                        rowItemGetAcceptedJobResult.setCandidateid(candidate_id_value);
                        rowItemGetAcceptedJobResult.setJobTitle(JobTitle);
                        rowItemGetAcceptedJobResult.setCompanyName(cpyName);
                        rowItemGetAcceptedJobResult.setJobPostId(JobPostId);
                        rowItemGetAcceptedJobResult.setJobDescription(JobDescription);
                        rowItemGetAcceptedJobResult.setImage_path(Image_path);
                        rowItemGetAcceptedJobResult.setJobType(JobType);
                        rowItemGetAcceptedJobResult.setUrl(url);
                        rowItemGetAcceptedJobResult.setPostedOn(postedOn);

                        arrayList_search.add(rowItemGetAcceptedJobResult);//add items in new arraylist_search
                    }
                }
                //New array adapter for searching and add in custome adapter.
                adapter= new Adapter_ApplyForJob(arrayList_search,getActivity().getLayoutInflater(),getActivity());
                listView_jobs.setAdapter(adapter);
                tv_empty.setText("Oops! mo matches found");
                listView_jobs.setEmptyView(tv_empty);
          //      return false;

            }
        });
        // using via menu item..
//        MenuItem searchMenuItem=menu.findItem(R.id.search_jobs);
//        searchView = (SearchView) searchMenuItem.getActionView();

//        Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,Toolbar.LayoutParams.WRAP_CONTENT);
//        searchView.setLayoutParams(params);
//        searchView.setFocusable(false);

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                Log.e("queryText",s);
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String s) {
//                //clear the previous data in search arraylist if exist
//                Log.e("queryText",s);
//                arrayList_search.clear();
//                keyword_upper = s.toUpperCase(); //Get keyword from searchbar editbox.
//                keyword_lower = s.toLowerCase();
//                keyword_filst_lt_capital= StringUtils.capitalize(s);
//                //checking language arraylist for items containing search keyword
//                for(int i =0 ;i < arrayList.size();i++){
//                    //  RowItemGetAcceptedJobResult rowItemGetAcceptedJobResult = new RowItemGetAcceptedJobResult();
//                    RowItem_ApplyForJob rowItemGetAcceptedJobResult=new RowItem_ApplyForJob();
//                    // if(arrayList.get(i).getJobTitle().contains(keyword)){
//                    if(arrayList.get(i).getJobTitle().contains(keyword_upper) || arrayList.get(i).getJobTitle().contains(keyword_lower) || arrayList.get(i).getJobTitle().contains(keyword_filst_lt_capital)){
//                        rowItemGetAcceptedJobResult.setJobTitle(arrayList.get(i).getJobTitle().toString());
//                        rowItemGetAcceptedJobResult.setCompanyName(arrayList.get(i).getCompanyName());
//                        rowItemGetAcceptedJobResult.setSalaryFrom(arrayList.get(i).getSalaryFrom());
//                        rowItemGetAcceptedJobResult.setJobLocation(arrayList.get(i).getJobLocation());
//                        rowItemGetAcceptedJobResult.setImage_path(arrayList.get(i).getImage_path());
//
//                        String JobTitle = arrayList.get(i).getJobTitle();
//                        String cpyName = arrayList.get(i).getCompanyName();
//                        //   String Salary=jObject.getString("Salary");
//                        // String city=jObject.getString("city");
//                        String JobPostId = arrayList.get(i).getJobPostId();
//                        String JobDescription =arrayList.get(i).getCompanyName();
//                        String JobType =arrayList.get(i).getJobType();
//                        String Image_path=arrayList.get(i).getImage_path();
//
//                        rowItemGetAcceptedJobResult.setCandidateid(candidate_id_value);
//                        rowItemGetAcceptedJobResult.setJobTitle(JobTitle);
//                        rowItemGetAcceptedJobResult.setCompanyName(cpyName);
//                        rowItemGetAcceptedJobResult.setJobPostId(JobPostId);
//                        rowItemGetAcceptedJobResult.setJobDescription(JobDescription);
//                        rowItemGetAcceptedJobResult.setImage_path(Image_path);
//
//
//                        rowItemGetAcceptedJobResult.setJobType(JobType);
//                        arrayList_search.add(rowItemGetAcceptedJobResult);//add items in new arraylist_search
//                    }
//                }
//                //New array adapter for searching and add in custome adapter.
//                adapter= new Adapter_ApplyForJob(arrayList_search,getActivity().getLayoutInflater(),getActivity());
//                listView_jobs.setAdapter(adapter);
//                tv_empty.setText("Oops! mo matches found");
//                listView_jobs.setEmptyView(tv_empty);
//                return false;
//            }
//        });
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.job_search_filter:
//                JobSearchFilter jobSearchFilter = new JobSearchFilter();
//                args = new Bundle();
//                args.putString("Key_candidate_id",candidate_id_value);
//                FragmentManager fm_job_search =  getFragmentManager();
//                FragmentTransaction fragmentTransaction_job_search = fm_job_search.beginTransaction();
//                jobSearchFilter.setArguments(args);
//                fragmentTransaction_job_search.addToBackStack(null);
//                fragmentTransaction_job_search.add(R.id.frameLayout, jobSearchFilter);
//                fragmentTransaction_job_search.commit(); // save the ch
//                break;
//            case R.id.view_invitation:
//                Interview_Invitation interview_invitation = new Interview_Invitation();
//                args = new Bundle();
//                args.putString("Key_candidate_id",candidate_id_value);
//                FragmentManager fm_interview_invitation =  getFragmentManager();
//                FragmentTransaction fragmentTransaction_interview_invitation = fm_interview_invitation.beginTransaction();
//                interview_invitation.setArguments(args);
//                fragmentTransaction_interview_invitation.addToBackStack(null);
//                fragmentTransaction_interview_invitation.add(R.id.frameLayout, interview_invitation);
//                fragmentTransaction_interview_invitation.commit(); // save the ch
//                break;
//            case R.id.job_apply:
//               ApplyForJob applyForJob = new ApplyForJob();
//                args = new Bundle();
//                args.putString("Key_candidate_id",candidate_id_value);
//                FragmentManager fm_apply_job =  getFragmentManager();
//                FragmentTransaction fragmentTransaction_apply_job = fm_apply_job.beginTransaction();
//                applyForJob.setArguments(args);
//                fragmentTransaction_apply_job.addToBackStack(null);
//                fragmentTransaction_apply_job.add(R.id.frameLayout, applyForJob);
//                fragmentTransaction_apply_job.commit(); // save the ch
//                break;
//            case R.id.notification:
//                Notification notification = new Notification();
//                args = new Bundle();
//                args.putString("Key_candidate_id",candidate_id_value);
//
//                FragmentManager fm_noti=  getFragmentManager();
//                FragmentTransaction fragmentTransaction_noti = fm_noti.beginTransaction();
//                notification.setArguments(args);
//                fragmentTransaction_noti.addToBackStack(null);
//                fragmentTransaction_noti.add(R.id.frameLayout, notification);
//                fragmentTransaction_noti.commit(); // save the ch
//                break;
//            case R.id.profile:
//                    Intent profile_intent = new Intent(getActivity(), ProfileSetup.class);
//                    profile_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    profile_intent.putExtra("Key_candidate_id",candidate_id_value);
//                    startActivity(profile_intent);
//                break;
//            case R.id.logout:
////                Toast.makeText(getActivity(), "Logout ",Toast.LENGTH_LONG).show();
////                sharedpreferences_id = getActivity().getSharedPreferences(mypreference_id,
////                        Context.MODE_PRIVATE);
////                SharedPreferences.Editor editor_id = sharedpreferences_id.edit();
////                editor_id.clear();
////                editor_id.commit();
////
////                Intent logout_intent = new Intent(getActivity(), Login_with.class);
////                Toast.makeText(getActivity(), "Logout ",Toast.LENGTH_LONG).show();
////                logout_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                this.startActivity(logout_intent);
////                break;
//
//
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//                // Setting Dialog Title
//                alertDialog.setTitle("Confirm Logout");
//                alertDialog.setCancelable(false);
//                alertDialog.setMessage("Are you sure you want logout ?");
//                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        sharedpreferences_id = getActivity().getSharedPreferences(mypreference_id,
//                                Context.MODE_PRIVATE);
//
//                        SharedPreferences.Editor editor_id = sharedpreferences_id.edit();
//                        editor_id.clear();
//                        editor_id.commit();
//                        Toast.makeText(getActivity(), "Logout ",Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(getActivity(), Login_with.class));
//                    }
//                });
//                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        //bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);
//                        dialog.cancel();
//                    }
//                });
//                alertDialog.show();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}