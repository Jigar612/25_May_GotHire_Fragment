package com.jigar.android.gothire;

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
import com.jigar.android.gothire.Adapter.Adapter_Interview_Invitation;
import com.jigar.android.gothire.SetterGetter.RowItem_InterviewInvitation;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Interview_Invitation extends Fragment {

    ProgressDialog progressDialog;
    String webservice_url = UrlString.URL + "GetJobInvition/";

    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    //**********
    String Candidate_id;
    ArrayList<RowItem_InterviewInvitation> arrayList_interv_invitation;
    ListView listview_invitation;

    TextView tv_empty;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_interview_invitation, container, false);
        MainContainer.TAG="InterviewInvitation";

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        MainContainer.bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(true);

        Bundle get_data = getArguments();
        Candidate_id = get_data.getString("Key_candidate_id");

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
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED)
        {
            listview_invitation = (ListView)view.findViewById(R.id.listView_interview_invitation);
            tv_empty = (TextView)view.findViewById(R.id.tv_empty_list);

            TextView tv_heading = (TextView) view.findViewById(R.id.tv_heading);//tv_empty_list_home
            Typeface roboto_Reg = Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/Roboto-Regular.ttf");
            Typeface roboto_Light = Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/Roboto-Light.ttf");
            tv_heading.setTypeface(roboto_Reg);
            tv_empty.setTypeface(roboto_Light);

            arrayList_interv_invitation = new ArrayList<RowItem_InterviewInvitation>();

            load_interivew_invitation_webservice();
        }
        else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
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
    public void load_interivew_invitation_webservice()
    {
        progressDialog.show();
        //By default is active then
        String url_interviewInvitation = webservice_url.toString() + Candidate_id ;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_interviewInvitation, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetJobInvitionResult");
                    int count=0;
                    while (count <resultJsonArr.length())
                    {
                        RowItem_InterviewInvitation item_interviewInvitation = new RowItem_InterviewInvitation();
                        JSONObject jObject = resultJsonArr.getJSONObject(count);


                        String CompanyName=jObject.getString("CompanyName");
                        String InterviewEndDate=jObject.getString("InterviewEndDate");
                        String InterviewStartDate=jObject.getString("InterviewStartDate");
                        String JobApplicationId=jObject.getString("JobApplicationId");
                        String JobDescription=jObject.getString("JobDescription");
                        String JobPostId=jObject.getString("JobPostId");
                        String JobTitle=jObject.getString("JobTitle");
                        String JopRefNo=jObject.getString("JopRefNo");
                        String SystemDate=jObject.getString("SystemDate");

                        item_interviewInvitation.setCompanyName(CompanyName);
                        item_interviewInvitation.setInterviewEndDate(InterviewEndDate);
                        item_interviewInvitation.setInterviewStartDate(InterviewStartDate);
                        item_interviewInvitation.setJobApplicationId(JobApplicationId);
                        item_interviewInvitation.setJobDescription(JobDescription);
                        item_interviewInvitation.setJobPostId(JobPostId);
                        item_interviewInvitation.setJobTitle(JobTitle);
                        item_interviewInvitation.setJopRefNo(JopRefNo);
                        item_interviewInvitation.setSystemDate(SystemDate);

                        item_interviewInvitation.setCandidate_id(Candidate_id);
                        arrayList_interv_invitation.add(item_interviewInvitation);
                        count++;
                    }
                    Adapter_Interview_Invitation adapter = new Adapter_Interview_Invitation(arrayList_interv_invitation,getActivity().getLayoutInflater(),Interview_Invitation.this,getContext());
                    listview_invitation.setAdapter(adapter);
                    listview_invitation.setEmptyView(tv_empty);
                    progressDialog.dismiss();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    progressDialog.dismiss();
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