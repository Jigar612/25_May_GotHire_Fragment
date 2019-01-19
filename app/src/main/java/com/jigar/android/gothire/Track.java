package com.jigar.android.gothire;

import android.app.Fragment;
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
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jigar.android.gothire.Adapter.Adapter_Track;
import com.jigar.android.gothire.Adapter.Adapter_save;
import com.jigar.android.gothire.SetterGetter.RowItems_Interviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Track extends Fragment {
    //String webservice_url = UrlString.URL + "GetAcceptedJobs/";
    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    String Candidate_id;
    //****
    Adapter_Track adapter_Track2;
    ListView listview_track;
    TextView tv_empty;
    Button btn_applied,btn_save;

    ProgressDialog progressDialog;
    ArrayList<RowItems_Interviews> arrayList;
    ArrayList<RowItems_Interviews> arrayList_save;

  //  ArrayList<RowItems_Interviews> arrayList_with_status;

    String selected_icon_state;
   // BottomNavigationView bottomNavigationView;

    boolean click_applied = true;
    boolean click_save = true;
    public static String applied;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_track, container, false);
        MainContainer.TAG="Track";
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MainContainer.bottomNavigationView.getMenu().findItem(R.id.bottom_nav_track).setChecked(true);

        Bundle get_data = getArguments();
        Candidate_id = get_data.getString("Key_candidate_id");

        listview_track=(ListView)view.findViewById(R.id.listview_track);
        tv_empty=(TextView)view.findViewById(R.id.tv_empty_list_track);
        btn_applied=(Button)view.findViewById(R.id.btn_applied_track);
        btn_save=(Button)view.findViewById(R.id.btn_save_track);
        TextView tv_heading = (TextView) view.findViewById(R.id.tv_heading);//tv_empty_list_home


        Typeface roboto_Reg = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Light.ttf");
        tv_heading.setTypeface(roboto_Reg);
        tv_empty.setTypeface(roboto_Light);
        btn_applied.setTypeface(roboto_Reg);
        btn_save.setTypeface(roboto_Reg);


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

            arrayList = new ArrayList<RowItems_Interviews>();
            arrayList_save = new ArrayList<RowItems_Interviews>();

            btn_applied.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        arrayList_save.clear();
                    //call_webservice_interviews_underReview();
                        call_applied_webservice();
                        btn_applied.setSelected(true);
                        btn_save.setSelected(false);
                        btn_applied.setBackgroundResource(R.drawable.button_blue_effect_track);
                        click_applied=true;
                        click_save=false;
                    }
                //}
            });
            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        arrayList.clear();
                        call_Save_webservice();
                        btn_save.setSelected(true);
                        btn_applied.setSelected(false);
                        btn_save.setBackgroundResource(R.drawable.button_blue_effect_track);
                        click_applied=false;
                        click_save=true;
                }
            });
            btn_applied.callOnClick();

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
    }
    public void call_applied_webservice() {
        progressDialog.show();
        String webservice_url = UrlString.URL + "AllAppliedJob/";
        String url_welcome = webservice_url.toString() + Candidate_id;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_welcome, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("AllAppliedJobResult");
                    int count = 0;
                    //String id_fetch,JopRefNo,CompanyName,JobTitle,JobDescription,JobPostId,InterviewStartDate,Days,TotalStage,InterviewStatus,InterviewStatusName,InterviewAcceptDateDisplay;
                    String candidat_id = String.valueOf(Candidate_id);
                    while (count < resultJsonArr.length()) {
                        RowItems_Interviews rowItems_interviews = new RowItems_Interviews();
                        JSONObject jObject = resultJsonArr.getJSONObject(count);

                        String JobTitle = jObject.getString("JobTitle");
                        String cpyName = jObject.getString("CompanyName");
                        String JobPostId = jObject.getString("JobPostId");
                        String JobDescription = jObject.getString("JobDescription");
                        String Image_path=jObject.getString("Logo");//ImagePath
                        String JobCurrentStatus=jObject.getString("JobCurrentStatus");//New

                        rowItems_interviews.setCandidateID(candidat_id);
                        rowItems_interviews.setJobTitle(JobTitle);
                        rowItems_interviews.setCompanyName(cpyName);
                        rowItems_interviews.setJobPostId(JobPostId);
                        rowItems_interviews.setJobDescription(JobDescription);
                        rowItems_interviews.setImage_path(Image_path);//new
                        rowItems_interviews.setInterviewStatus(JobCurrentStatus);//new

                        arrayList.add(rowItems_interviews);
                        count++;
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Adapter_Track adapter = new Adapter_Track(arrayList, getActivity().getLayoutInflater(), getActivity());
                listview_track.setAdapter(adapter);
                //listview_track.setAdapter(adapter_Track2);
                tv_empty.setText("No jobs are applied");
                listview_track.setEmptyView(tv_empty);
                progressDialog.dismiss();
              //  bottomNavigationView.getMenu().findItem(R.id.bottom_nav_track).setChecked(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                listview_track.setEmptyView(tv_empty);
                tv_empty.setText("No jobs are applied");
              //  bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(true);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjReq);
    }
    public void call_Save_webservice() {
        progressDialog.show();
        String url_welcome = UrlString.URL + "GetSaveJob/" + Candidate_id;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_welcome, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetSaveJobResult");
                    int count = 0;
                    //String id_fetch,JopRefNo,CompanyName,JobTitle,JobDescription,JobPostId,InterviewStartDate,Days,TotalStage,InterviewStatus,InterviewStatusName,InterviewAcceptDateDisplay;
                    String candidat_id = String.valueOf(Candidate_id);
                    while (count < resultJsonArr.length()) {
                        RowItems_Interviews rowItems_interviews = new RowItems_Interviews();
                        JSONObject jObject = resultJsonArr.getJSONObject(count);

                        String JobTitle = jObject.getString("JobTitle");
                        String cpyName = jObject.getString("CompanyName");
                        String JobPostId = jObject.getString("JobPostId");
                        String JobDescription = jObject.getString("JobDescription");
                        String Image_path=jObject.getString("Logo");//ImagePath

                        rowItems_interviews.setCandidateID(candidat_id);
                        rowItems_interviews.setJobTitle(JobTitle);
                        rowItems_interviews.setCompanyName(cpyName);
                        rowItems_interviews.setJobPostId(JobPostId);
                        rowItems_interviews.setJobDescription(JobDescription);
                        rowItems_interviews.setImage_path(Image_path);//new

                        arrayList_save.add(rowItems_interviews);
                        count++;
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Adapter_save adapter_save = new Adapter_save(arrayList_save, getActivity().getLayoutInflater(), getActivity());
                listview_track.setAdapter(adapter_save);
                tv_empty.setText("No jobs are saved");
                listview_track.setEmptyView(tv_empty);
                progressDialog.dismiss();
              //  bottomNavigationView.getMenu().findItem(R.id.bottom_nav_track).setChecked(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                listview_track.setEmptyView(tv_empty);
                tv_empty.setText("No jobs are saved");
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjReq);
    }
}