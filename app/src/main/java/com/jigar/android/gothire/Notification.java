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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.jigar.android.gothire.Adapter.Adapter_Notification;
import com.jigar.android.gothire.SetterGetter.RowItem_Notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class Notification extends Fragment {

    ProgressDialog progressDialog;
    String webservice_url = UrlString.URL + "GetNotifications/";
    //For CandidateNm
//    SharedPreferences sharedpreferences_nm;
//    public static final String mypreference_nm = "mypref_nm";
//    public static final String CandidateNmKey = "candidateNmKey";
//    String Candidate_nm;
    //******
    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    //**********
    ArrayList<RowItem_Notification> arrayList_notification;
    ListView listView_notification;
    String Candidate_id;
    String job_nm, cpy_nm, job_desc, interv_accept_dt;

    TextView tv_empty;

    View view;
  //  Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_notification, container, false);
        MainContainer.TAG="Notification";

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

       // MainContainer.bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(false);
        MainContainer.bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setCheckable(false).setChecked(false);


        listView_notification = (ListView)view.findViewById(R.id.list_notification);
        tv_empty=(TextView)view.findViewById(R.id.notification_tv_empty_list);
        TextView tv_heading = (TextView) view.findViewById(R.id.tv_heading);//tv_empty_list_home

        Typeface roboto_Reg = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Light.ttf");

        tv_heading.setTypeface(roboto_Reg);
        tv_empty.setTypeface(roboto_Light);
//        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        getActivity().setTitle("");

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
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            arrayList_notification = new ArrayList<RowItem_Notification>();

            load_Notification_webservice();
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
    public void load_Notification_webservice()
    {
        progressDialog.show();
        //By default is active then
        String url_notification = webservice_url.toString() + Candidate_id ;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_notification, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetNotificationsResult");
                    int count=0;
                    while (count <resultJsonArr.length())
                    {
                        RowItem_Notification rowItem_notification = new RowItem_Notification();
                        JSONObject jObject = resultJsonArr.getJSONObject(count);

                        String CandidateID=jObject.getString("CandidateID");
                        String msg=jObject.getString("msg");
                        String OpenActivityID=jObject.getString("OpenActivityID");
                        String RoundID=jObject.getString("RoundID");
                        String JobID=jObject.getString("JobID");
                        String Examstartid=jObject.getString("Examstartid");
                        String Creaton=jObject.getString("Creaton");


                        rowItem_notification.setCandidate_id(CandidateID);
                        rowItem_notification.setMsg(msg);
                        rowItem_notification.setOpenActivityID(OpenActivityID);
                        rowItem_notification.setRoundID(RoundID);
                        rowItem_notification.setJobID(JobID);
                        rowItem_notification.setExamstartid(Examstartid);
                        rowItem_notification.setCreatedOn(Creaton);

                        arrayList_notification.add(rowItem_notification);
                        count++;
                    }


                    Collections.reverse(arrayList_notification);
                    Adapter_Notification adapter = new Adapter_Notification(arrayList_notification,getActivity().getLayoutInflater(),Notification.this,getContext());
                    listView_notification.setAdapter(adapter);
                    listView_notification.setEmptyView(tv_empty);
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
