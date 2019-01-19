package com.jigar.android.gothire.Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jigar.android.gothire.ApplyForJob;
import com.jigar.android.gothire.Interview_Invitation;
import com.jigar.android.gothire.Interviews;
import com.jigar.android.gothire.JobRound;
import com.jigar.android.gothire.Notification;
import com.jigar.android.gothire.R;
import com.jigar.android.gothire.SetterGetter.RowItemGetAcceptedJobResult;
import com.jigar.android.gothire.SetterGetter.RowItem_Notification;
import com.jigar.android.gothire.UrlString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by COMP11 on 19-Mar-18.
 */

public class Adapter_Notification extends BaseAdapter {
    ArrayList<RowItem_Notification> arrayList = new ArrayList<RowItem_Notification>();
    LayoutInflater inflater;
    // Context context;
    Notification context;

    String candidateid;
    String round_id;
    String open_screen_id;
    String jobname, compny_nm, job_desc, accepted_on;
    String examstart_id;
    String job_id;
    Bundle args;
    Context context1;
    Date date_created;

    public Adapter_Notification(ArrayList myList, LayoutInflater inflater, Notification context,Context context1) {
        this.arrayList = myList;
        this.inflater = inflater;
        this.context = context;
        this.context1 = context1;
    }

    @Override
    public int getCount() {
        //return arrayList.size();
        return  arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(getCount()-position-1);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listitem_notification, null);

        TextView tv_notification_msg = (TextView)convertView.findViewById(R.id.tv_notification_msg);
        TextView tv_notification_date = (TextView)convertView.findViewById(R.id.txt_notification_date);

       // TextView tv_notification_number = (TextView)convertView.findViewById(R.id.tv_notification_no);
        //String message = arrayList.get(position).getMsg();

        Typeface roboto_Med = Typeface.createFromAsset(context1.getAssets(),
                "fonts/Roboto-Medium.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(context1.getAssets(),
                "fonts/Roboto-Light.ttf");
        tv_notification_msg.setTypeface(roboto_Med);
        tv_notification_date.setTypeface(roboto_Light);




        String created_date= arrayList.get(position).getCreatedOn();
        String result_created_dt = created_date.substring(0, created_date.indexOf("-"));

        Calendar calendar_expDate = Calendar.getInstance();
        String datereip = result_created_dt.replace("/Date(", "").replace(")/", "");
        Long timeInMillis = Long.valueOf(datereip);
        calendar_expDate.setTimeInMillis(timeInMillis);
        date_created = calendar_expDate.getTime(); //convert to date

        String pattern = "MMMM d yyyy - HH:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        //SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern, new Locale("fr", "FR"));

        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        symbols.setAmPmStrings(new String[] { "am", "pm" });
        simpleDateFormat.setDateFormatSymbols(symbols);

        String disp_dreated_date=simpleDateFormat.format(date_created);


        tv_notification_date.setText(disp_dreated_date);
        tv_notification_msg.setText(arrayList.get(position).getMsg());

        candidateid = arrayList.get(position).getCandidate_id();



        tv_notification_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                open_screen_id =arrayList.get(position).getOpenActivityID();
                job_id= arrayList.get(position).getJobID();

              //  getAccepted_webservices(); currently webservices is not working...
                //call GetAcceptedJobResult webservices remaining here....

                if (open_screen_id.equals("1"))
                {
//                    Intent intent = new Intent(context.getActivity().getApplicationContext(), Interviews.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("Key_candidate_id", candidateid);
//                    context.startActivity(intent);
                    args = new Bundle();
                    args.putString("Key_candidate_id", candidateid);
                    loadFragment(new Interviews());
                }
                if (open_screen_id.equals("2"))
                {

                    getAccepted_webservices();



                   // examstart_id = arrayList.get(position).getExamstartid();
//                    Intent intent = new Intent(context.getApplicationContext(), JobRound.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("Key_candidate_id", candidateid);
//                    intent.putExtra("Key_jobPost_id", job_id);
//                    intent.putExtra("Key_job_nm", jobname);
//                    intent.putExtra("Key_cpy_nm", compny_nm);
//                    intent.putExtra("Key_job_desc", job_desc);
//                    intent.putExtra("Key_interview_accepted_on", accepted_on);
//                    context.startActivity(intent);
                }
                if (open_screen_id.equals("8"))
                {

//                    Intent intent_Apply_job = new Intent(context.getActivity().getApplicationContext(), ApplyForJob.class);
//                    intent_Apply_job.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent_Apply_job.putExtra("Key_candidate_id", candidateid);
//                    context.startActivity(intent_Apply_job);
                    args = new Bundle();
                    args.putString("Key_candidate_id", candidateid);
                    loadFragment(new ApplyForJob());

                }
                if (open_screen_id.equals("7"))
                {
//                    Intent intent_interview_invitation = new Intent(context.getActivity().getApplicationContext(), Interview_Invitation.class);
//                    intent_interview_invitation.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent_interview_invitation.putExtra("Key_candidate_id", candidateid);
//                    context.startActivity(intent_interview_invitation);
                    args = new Bundle();
                    args.putString("Key_candidate_id", candidateid);
                    loadFragment(new Interview_Invitation());
                }
            }
        });
        return convertView;
    }
    public void getAccepted_webservices()
    {

        //By default is active then
        String url_welcome = UrlString.URL + "GetAcceptedJobs/" + candidateid;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_welcome, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("GetAcceptedJobsResult");

                    int count=0;

                    String id_fetch,JopRefNo,CompanyName,JobTitle,JobDescription,JobPostId,InterviewStartDate,Days,TotalStage,InterviewStatus,InterviewStatusName,InterviewAcceptDateDisplay;
                    while (count <resultJsonArr.length())
                    {
                        RowItemGetAcceptedJobResult item_getAcceptedJob = new RowItemGetAcceptedJobResult();
                        JSONObject jObject = resultJsonArr.getJSONObject(count);


                        JobPostId=jObject.getString("JobPostId");

                        if (JobPostId.equals(job_id)) {
//                            jobname = jObject.getString("JobTitle");
//                            compny_nm = jObject.getString("CompanyName");
//                            job_desc = jObject.getString("JobDescription");
//                            accepted_on = jObject.getString("InterviewAcceptDateDisplay");

                            JobTitle = jObject.getString("JobTitle");
                            CompanyName = jObject.getString("CompanyName");
                            JobDescription = jObject.getString("JobDescription");
                            InterviewAcceptDateDisplay = jObject.getString("InterviewAcceptDateDisplay");
                            String Image_path=jObject.getString("ImagePath");

//                            Intent intent = new Intent(context.getActivity().getApplicationContext(), JobRound.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.putExtra("Key_candidate_id", candidateid);
//                            intent.putExtra("Key_jobPost_id", job_id);
//                            intent.putExtra("Key_job_nm", JobTitle);
//                            intent.putExtra("Key_cpy_nm", CompanyName);
//                            intent.putExtra("Key_job_desc", JobDescription);
//                            intent.putExtra("Key_interview_accepted_on", InterviewAcceptDateDisplay);
//                            intent.putExtra("Key_image_path",Image_path);
//                            context.startActivity(intent);

                            args = new Bundle();
                            args.putString("Key_candidate_id", candidateid);
                            args.putString("Key_jobPost_id", job_id);
                            args.putString("Key_job_nm", JobTitle);
                            args.putString("Key_cpy_nm", CompanyName);
                            args.putString("Key_job_desc", JobDescription);
                            args.putString("Key_interview_accepted_on", InterviewAcceptDateDisplay);
                            args.putString("Key_image_path",Image_path);
                            loadFragment(new JobRound());

                        }
                        count++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context.getActivity(), "Please try again", Toast.LENGTH_LONG).show();
            }
        }) {

        };
        RequestQueue rQueue = Volley.newRequestQueue(context.getActivity());
        rQueue.add(jsonObjReq);
    }
    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm =  ((Activity) context.getActivity()).getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit(); // save the changes

    }
}