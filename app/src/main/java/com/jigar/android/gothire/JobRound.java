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
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jigar.android.gothire.Adapter.AdapterJobRound;
import com.jigar.android.gothire.SetterGetter.RowItemGetRoundWiseJob;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JobRound extends Fragment {
    ProgressDialog progressDialog;
    String webservice_url = UrlString.URL + "GetRoundsWiseJobs/";
    //For CandidateNm
    SharedPreferences sharedpreferences_nm;
    public static final String mypreference_nm = "mypref_nm";
    public static final String CandidateNmKey = "candidateNmKey";
    String Candidate_nm;
    //******
    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    //**********
    //Requirement control..
    ListView list_job_round;
    TextView tv_empty;
    //*****
    String Candidate_id,jobname,companyname,jobdesc,accepted_date;
    String jobPost_id;
    public static String Candidate_id_static,jobname_static,companyname_static,jobdesc_static,accepted_date_static;
    public static String jobPost_id_static,getPath_static;

    ArrayList<RowItemGetRoundWiseJob> arrayList_job_round;
    String selected_icon_state;
    String getPath;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_job_round, container, false);
        MainContainer.TAG="JobRound";

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        MainContainer.bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(true);
        list_job_round =(ListView)view.findViewById (R.id.list_ViewRound);
        tv_empty=(TextView)view.findViewById(R.id.tv_empty_list_interview);
        TextView tv_job_nm=(TextView)view.findViewById(R.id.tv_heading_jobNm_interview_breakdown);
        TextView tv_cpy_nm=(TextView)view.findViewById(R.id.tv_heading_cpyNm_interview_breakdown);
        ImageView img_cpy_logo = (ImageView)view.findViewById(R.id.img_cpy_logo_interview_breakdown);
        TextView tv_heading = (TextView) view.findViewById(R.id.tv_heading);//tv_empty_list_home

        Typeface roboto_Reg = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Light.ttf");
        Typeface roboto_Med = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Medium.ttf");

        tv_heading.setTypeface(roboto_Reg);
        tv_empty.setTypeface(roboto_Light);
        tv_job_nm.setTypeface(roboto_Med);
        tv_cpy_nm.setTypeface(roboto_Light);




        Bundle get_data = getArguments();
        Candidate_id = get_data.getString("Key_candidate_id");
        jobPost_id = get_data.getString("Key_jobPost_id");
        jobname = get_data.getString("Key_job_nm");
        companyname = get_data.getString("Key_cpy_nm");
        jobdesc = get_data.getString("Key_job_desc");
        accepted_date = get_data.getString("Key_interview_accepted_date_display");
        getPath= get_data.getString("Key_image_path");

        Candidate_id_static=Candidate_id;
        jobname_static=jobname;
        companyname_static=companyname;
        jobdesc_static=jobdesc;
        accepted_date_static=accepted_date;
        jobPost_id_static=jobPost_id;
        getPath_static = getPath;

        tv_job_nm.setText(jobname);
        tv_cpy_nm.setText(companyname);
     //   if(!getPath.equals(null)|| !getPath.equals("") || !getPath.equals("null"))
     //   {
            String path=LiveLink.LinkLive2+getPath;
            Picasso.with(getActivity())
                    .load(path)
                    .placeholder(R.drawable.ic_briefcase)
                    .into(img_cpy_logo);
     //   }
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
            arrayList_job_round = new ArrayList<RowItemGetRoundWiseJob>();
            load_JobRound_webservice();
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
    public void load_JobRound_webservice()
    {
           progressDialog.show();
            //By default is active then
            String url_jobRound = webservice_url.toString() + Candidate_id +"/"+jobPost_id;//42/5301
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_jobRound, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Result", response.toString());
                    String result = response.toString();
                    try {
                        JSONArray resultJsonArr = response.getJSONArray("GetRoundsWiseJobsResult");
                        int count=0;
                        String Roundname,ExamStartdate,ExamEnddate,IsStart,Roundstatus,Color,RoundID,ProcessTIme,TotalProcessTime,status,TotalReamingTime;
                        String FinalStage,IsOffer;
                        while (count <resultJsonArr.length())
                        {
                            RowItemGetRoundWiseJob item_getRoundWiseJob = new RowItemGetRoundWiseJob();
                            JSONObject jObject = resultJsonArr.getJSONObject(count);

                            Roundname=jObject.getString("Roundname");
                            ExamStartdate=jObject.getString("ExamStartdate");
                            ExamEnddate=jObject.getString("ExamEnddate");
                            IsStart=jObject.getString("IsStart");
                            Roundstatus=jObject.getString("Roundstatus");
                            Color=jObject.getString("Color");
                            RoundID=jObject.getString("RoundID");
                            ProcessTIme=jObject.getString("ProcessTIme");
                            TotalProcessTime=jObject.getString("TotalProcessTime");
                            status=jObject.getString("status");
                            TotalReamingTime=jObject.getString("Timeline");
                            //New field
                            FinalStage=jObject.getString("isFinalStage");
                            //New field
                            IsOffer=jObject.getString("IsOffer");
                            int job_post_id_cnvt= Integer.parseInt(jobPost_id);
                            //New 19-March-2018 check it
                            if(Roundname.equals("null") && status.equals("null") && TotalReamingTime.equals("null"))
                            {
                                list_job_round.setEmptyView(tv_empty);
                            }
                            else
                            {//*****
                                item_getRoundWiseJob.setCandidate_id(Candidate_id);
                                item_getRoundWiseJob.setJobPost_id(job_post_id_cnvt);

                                item_getRoundWiseJob.setRoundname(Roundname);//
                                item_getRoundWiseJob.setExamStartdate(ExamStartdate);
                                item_getRoundWiseJob.setExamEnddate(ExamEnddate);
                                item_getRoundWiseJob.setIsStart(IsStart);
                                item_getRoundWiseJob.setRoundstatus(Roundstatus);
                                item_getRoundWiseJob.setColor(Color);
                                item_getRoundWiseJob.setRoundID(RoundID);
                                item_getRoundWiseJob.setProcessTIme(ProcessTIme);
                                item_getRoundWiseJob.setTotalProcessTime(TotalProcessTime);
                                item_getRoundWiseJob.setStatus(status);//
                                item_getRoundWiseJob.setTotalReamingTime(TotalReamingTime);//
                                //New field
                                item_getRoundWiseJob.setFinalStage(FinalStage);
                                item_getRoundWiseJob.setIsOffer(IsOffer);
                                //New for JobRound
                                item_getRoundWiseJob.setDesc(jobdesc);
                                item_getRoundWiseJob.setCpyName(companyname);
                                item_getRoundWiseJob.setJobName(jobname);
                                item_getRoundWiseJob.setIntervAcceptDt(accepted_date);
                                item_getRoundWiseJob.setExamStartID(jObject.getString("ExamStartID"));

                                arrayList_job_round.add(item_getRoundWiseJob);
                            }
                            count++;
                        }
                        AdapterJobRound adapter = new AdapterJobRound(arrayList_job_round,getActivity().getLayoutInflater(),getActivity());
                        list_job_round.setAdapter(adapter);
                        list_job_round.setEmptyView(tv_empty);
                        //adapter.updateResults(arrayList_job_round);
                        //progressDialog.dismiss();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
                    list_job_round.setEmptyView(tv_empty);
                    progressDialog.dismiss();
                 //   bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(true);
                  //  listView_jobs.setEmptyView(tv_empty);
                }
            }) {
            };
            RequestQueue rQueue = Volley.newRequestQueue(getActivity());
            rQueue.add(jsonObjReq);
    }
}
