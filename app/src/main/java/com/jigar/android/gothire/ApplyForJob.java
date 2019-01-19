package com.jigar.android.gothire;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jigar.android.gothire.Adapter.Adapter_ApplyForJob;
import com.jigar.android.gothire.SetterGetter.RowItem_ApplyForJob;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApplyForJob extends Fragment {

    ProgressDialog progressDialog;
    String webservice_url = UrlString.URL + "GetViewJob_Invitation/";
    //For CandidateNm
   // SharedPreferences sharedpreferences_nm;
    public static final String mypreference_nm = "mypref_nm";
    public static final String CandidateNmKey = "candidateNmKey";
    String Candidate_nm;
    //******
    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    //**********
    ArrayList<RowItem_ApplyForJob> arrayList_applyJob;
    ListView listView_applyJob;
    TextView tv_empty;
    String Candidate_id;
    View view;
   // BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_apply_for_job, container, false);

        MainContainer.TAG="ApplyForJob";

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MainContainer.bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);

        listView_applyJob = (ListView)view.findViewById(R.id.list_view_apply_job);
        tv_empty=(TextView)view.findViewById(R.id.job_apply_tv_empty_list);
        TextView tv_heading=(TextView)view.findViewById(R.id.tv_heading);

        Bundle get_data = getArguments();
        Candidate_id = get_data.getString("Key_candidate_id");

        Typeface roboto_Reg = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Light.ttf");


        tv_heading.setTypeface(roboto_Reg);
        tv_empty.setTypeface(roboto_Light);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);



        runTask();

        return view;
    }

    public void runTask()
    {
        ConnectivityManager connec =
                (ConnectivityManager) getActivity().getSystemService(getActivity().getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            arrayList_applyJob = new ArrayList<RowItem_ApplyForJob>();

            load_ApplyForJob_webservice();


        }
        else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
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
    }
    public void load_ApplyForJob_webservice()
    {
        progressDialog.show();
        //By default is active then
        String url_applyForJob = webservice_url.toString() + Candidate_id ;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_applyForJob, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetViewJob_InvitationResult");
                    int count=0;
                    while (count <resultJsonArr.length())
                    {
                        RowItem_ApplyForJob rowItem_applyForJob = new RowItem_ApplyForJob();
                        JSONObject jObject = resultJsonArr.getJSONObject(count);


                        String CompanyName=jObject.getString("CompanyName");
                        String JobTitle=jObject.getString("JobTitle");
                        String ExperinceFrom=jObject.getString("ExperinceFrom");
                        String ExpriceTO=jObject.getString("ExpriceTO");
                        String Skillname=jObject.getString("Skillname");
                        String JobDescription=jObject.getString("JobDescription");
                        String SalaryFrom=jObject.getString("SalaryFrom");
                        String SalaryTo=jObject.getString("SalaryTo");
                        String JobLocation=jObject.getString("JobLocation");
                        String JobPostId=jObject.getString("JobPostId");
                        String JobType=jObject.getString("JobType");
                        String Image_path=jObject.getString("Logo");//new
//                        //  String Image_path="/TestimonialsImageVedio/GotHireSmall.png";//new
//                        String Image_path="";//new

                        //   String Candidate_id=jObject.getString("Candidate_id");
                        rowItem_applyForJob.setCompanyName(CompanyName);
                        rowItem_applyForJob.setJobTitle(JobTitle);
                        rowItem_applyForJob.setExperinceFrom(ExperinceFrom);
                        rowItem_applyForJob.setExpriceTO(ExpriceTO);
                        rowItem_applyForJob.setSkillname(Skillname);
                        rowItem_applyForJob.setJobDescription(JobDescription);
                        rowItem_applyForJob.setSalaryFrom(SalaryFrom);
                        rowItem_applyForJob.setSalaryTo(SalaryTo);
                        rowItem_applyForJob.setJobLocation(JobLocation);
                        rowItem_applyForJob.setJobPostId(JobPostId);
                        rowItem_applyForJob.setCandidateid(Candidate_id);
                        rowItem_applyForJob.setJobType(JobType);

                        rowItem_applyForJob.setImage_path(Image_path);//new

                        arrayList_applyJob.add(rowItem_applyForJob);
                        count++;
                    }
                    Adapter_ApplyForJob adapter = new Adapter_ApplyForJob(arrayList_applyJob,getActivity().getLayoutInflater(),getActivity());
                    listView_applyJob.setAdapter(adapter);
                    listView_applyJob.setEmptyView(tv_empty);
                    progressDialog.dismiss();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //  listView_jobs.setEmptyView(tv_empty);
            }
        }) {

        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjReq);
    }
}
