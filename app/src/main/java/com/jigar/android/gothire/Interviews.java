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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
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
import com.jigar.android.gothire.Adapter.AdapterHomeAdapter;
import com.jigar.android.gothire.Adapter.Adapter_Interview_Invitation;
import com.jigar.android.gothire.Adapter.Adapter_interviews;
import com.jigar.android.gothire.SetterGetter.RowItemGetAcceptedJobResult;
import com.jigar.android.gothire.SetterGetter.RowItems_Interviews;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class Interviews extends Fragment {
    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";

    String webservice_url = UrlString.URL + "GetAcceptedJobs/";
    String candidate_id_value;
    public static String candidate_id_value_static;

 //   TextView Active, under_rev, completed;
    TextView tv_title;
    ListView listview_interview;
    TextView tv_empty;
    ProgressDialog progressDialog;
    ArrayList<RowItems_Interviews> arrayList;
   // String selected_icon_state;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_interviews, container, false);
        MainContainer.TAG="Interviews";

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      //  MainContainer.bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(true);
        MainContainer.bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setCheckable(true).setChecked(true);

        Bundle get_data = getArguments();
        candidate_id_value = get_data.getString("Key_candidate_id");
        candidate_id_value_static=candidate_id_value;

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

            listview_interview = (ListView)view.findViewById(R.id.listview_interview);
            tv_empty = (TextView) view.findViewById(R.id.tv_empty_interview);//tv_empty_list_home
            TextView tv_heading = (TextView) view.findViewById(R.id.tv_heading);//tv_empty_list_home
            arrayList = new ArrayList<RowItems_Interviews>();

            Typeface roboto_Reg = Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/Roboto-Regular.ttf");
            Typeface roboto_Light = Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/Roboto-Light.ttf");
            tv_heading.setTypeface(roboto_Reg);
            tv_empty.setTypeface(roboto_Light);

            call_webservice();


        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
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
    public void call_webservice() {
        progressDialog.show();
        String url_welcome = webservice_url.toString() + candidate_id_value;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_welcome, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetAcceptedJobsResult");
                    int count = 0;
                    String candidat_id = String.valueOf(candidate_id_value);

                    while (count < resultJsonArr.length()) {
                        RowItems_Interviews rowItems_interviews = new RowItems_Interviews();
                        JSONObject jObject = resultJsonArr.getJSONObject(count);

                        String JobTitle = jObject.getString("JobTitle");
                        String cpyName = jObject.getString("CompanyName");
                        //   String Salary=jObject.getString("Salary");
                        // String city=jObject.getString("city");
                        String JobPostId = jObject.getString("JobPostId");
                        String JobDescription = jObject.getString("JobDescription");
                        String InterviewAcceptDateDisplay = jObject.getString("InterviewAcceptDateDisplay");
                        String InterviewSt_date = jObject.getString("InterviewStartDate");
                        String InterviewStatus=jObject.getString("InterviewStatus");
                        String Image_path=jObject.getString("ImagePath"); //New

                        rowItems_interviews.setCandidateID(candidat_id);
                        rowItems_interviews.setJobTitle(JobTitle);
                        rowItems_interviews.setCompanyName(cpyName);
                        rowItems_interviews.setJobPostId(JobPostId);
                        rowItems_interviews.setJobDescription(JobDescription);
                        rowItems_interviews.setInterviewStartDate(InterviewSt_date);
                        rowItems_interviews.setInterviewAcceptDateDisplay(InterviewAcceptDateDisplay);
                        rowItems_interviews.setInterviewStatus(InterviewStatus);
                        rowItems_interviews.setImage_path(Image_path);//new
                        arrayList.add(rowItems_interviews);
                        count++;
                    }
              /*      Collections.sort(arrayList, new Comparator<RowItems_Interviews>() {

                        @Override
                        public int compare(RowItems_Interviews o1, RowItems_Interviews o2) {
                            return (int)(o1.getStatus_int()-o2.getStatus_int());
                        }
                    });

*/

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Adapter_interviews adapter = new Adapter_interviews(arrayList, getActivity().getLayoutInflater(), getActivity());
                adapter.updateResults(arrayList);
                listview_interview.setAdapter(adapter);
                listview_interview.setEmptyView(tv_empty);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                listview_interview.setEmptyView(tv_empty);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjReq);
    }
}