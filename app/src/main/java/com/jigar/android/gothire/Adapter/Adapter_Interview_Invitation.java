package com.jigar.android.gothire.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jigar.android.gothire.Home;
import com.jigar.android.gothire.Interview_Invitation;
import com.jigar.android.gothire.Interviews;
import com.jigar.android.gothire.R;
import com.jigar.android.gothire.SetterGetter.RowItem_InterviewInvitation;
import com.jigar.android.gothire.UrlString;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by COMP11 on 15-Mar-18.
 */

public class Adapter_Interview_Invitation extends BaseAdapter {
    ArrayList<RowItem_InterviewInvitation> arrayList = new ArrayList<RowItem_InterviewInvitation>();
    LayoutInflater inflater;
    Interview_Invitation context;
    ProgressDialog progressDialog;

    EditText ed_st_dt;
    EditText ed_end_dt;
    TextView tv_alert_job_title;
    TextView tv_alert_cpy_nm;
    TextView tv_alert_desc;

    Date start_date;
    Date end_date;
    Date sys_date;
    LinearLayout linear_button,linear_msg,linear_row_select;
    Button btn_accept,btn_reject;
    //,btn_rescedule;

    TextView tv_job_no,tv_job_nm,tv_cpy_nm,tv_accept_period, tv_msg;

    //For Resedule
    Calendar StartCalendar;
    Calendar EndCalendar;

    DatePickerDialog.OnDateSetListener datepicker_st;
    DatePickerDialog.OnDateSetListener datepicker_end;
    Date cnvt_start_date;
    Date cnvt_end_date;

    Bundle args;
    Context context1;
    public String select_st_date, select_end_date;
    //**********

    public Adapter_Interview_Invitation(ArrayList myList, LayoutInflater inflater, Interview_Invitation context,Context context1) {
        this.arrayList = myList;
        this.inflater = inflater;
        this.context = context;
        this.context1=context1;

    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listitem_interview_invitation, null);

         linear_button = (LinearLayout)convertView.findViewById(R.id.line_interv_invi_button);
         linear_msg = (LinearLayout)convertView.findViewById(R.id.line_interv_invi_txt_msg);
         linear_row_select = (LinearLayout)convertView.findViewById(R.id.lin_interv_invitation);
          btn_accept = (Button)convertView.findViewById(R.id.btn_accept);
          btn_reject = (Button)convertView.findViewById(R.id.btn_reject);
        //  btn_rescedule = (Button)convertView.findViewById(R.id.btn_reschedule);
         tv_job_no = (TextView)convertView.findViewById(R.id.tv_job_no_intv_invi);
         tv_job_nm = (TextView)convertView.findViewById(R.id.tv_job_nm_intv_invi);
         tv_cpy_nm = (TextView)convertView.findViewById(R.id.tv_cpy_nm_intv_invi);
         tv_accept_period = (TextView)convertView.findViewById(R.id.tv_accept_period_intv_invi);
         tv_msg = (TextView)convertView.findViewById(R.id.tv_msg_interview_invi);

        TextView tv2 = (TextView)convertView.findViewById(R.id.tv2);
        TextView tv3 = (TextView)convertView.findViewById(R.id.tv3);
        TextView tv4 = (TextView)convertView.findViewById(R.id.tv4);
        TextView tv5 = (TextView)convertView.findViewById(R.id.tv5);




        Typeface roboto_Med = Typeface.createFromAsset(context1.getAssets(),
                "fonts/Roboto-Medium.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(context1.getAssets(),
                "fonts/Roboto-Light.ttf");
        tv2.setTypeface(roboto_Med);
        tv3.setTypeface(roboto_Med);
        tv4.setTypeface(roboto_Med);
        tv5.setTypeface(roboto_Med);

        btn_accept.setTypeface(roboto_Light);
        btn_reject.setTypeface(roboto_Light);
        tv_job_no.setTypeface(roboto_Light);
        tv_job_nm.setTypeface(roboto_Light);
        tv_cpy_nm.setTypeface(roboto_Light);
        tv_accept_period.setTypeface(roboto_Light);
        tv_msg.setTypeface(roboto_Light);

        progressDialog = new ProgressDialog(context.getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);


        String frm_date_str= arrayList.get(position).getInterviewStartDate();
        String to_date_str= arrayList.get(position).getInterviewEndDate();

        String disp_frm_date="";
        String disp_to_date="";

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        try {
            Date date_from = sdf.parse(frm_date_str);
            Date date_to = sdf.parse(to_date_str);

            String pattern = "MM-dd-yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);


              disp_frm_date=simpleDateFormat.format(date_from);
              disp_to_date=simpleDateFormat.format(date_to);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tv_job_no.setText(arrayList.get(position).getJopRefNo());
        tv_job_nm.setText(arrayList.get(position).getJobTitle());
        tv_cpy_nm.setText(arrayList.get(position).getCompanyName());
        tv_accept_period.setText(disp_frm_date+ " to " +disp_to_date);

        final String jobApplication_id = arrayList.get(position).getJobApplicationId();//Get from interv_accept webservice
        //final String jobApplication_id = arrayList.get(position).getJobPostId();//Get from interv_accept webservice
        final String candidate_id = arrayList.get(position).getCandidate_id();




        try {

            String myFormat1 = "dd-MM-yyyy"; //In which you need put here
            SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1);
            //String myFormat_sys_date = "dd-MM-yyyy HH:MM:SS"; //In which you need put here
            SimpleDateFormat sdf_sys_dt1 = new SimpleDateFormat(myFormat1);

             start_date = sdf1.parse(arrayList.get(position).getInterviewStartDate());
             end_date =sdf1.parse(arrayList.get(position).getInterviewEndDate());
             sys_date=sdf_sys_dt1.parse(arrayList.get(position).getSystemDate());


        } catch (ParseException e) {
                e.printStackTrace();
        }
        linear_row_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View mView = context.getActivity().getLayoutInflater().inflate(R.layout.alertbox_intrv_invi_viewdetail, null);
                tv_alert_job_title = (TextView)mView.findViewById(R.id.tv_alert_job_titile);
                tv_alert_cpy_nm = (TextView)mView.findViewById(R.id.tv_alert_cpy_nm);
                tv_alert_desc = (TextView)mView.findViewById(R.id.tv_alert_job_job_desc);

                tv_alert_job_title.setText(arrayList.get(position).getJobTitle());
                tv_alert_cpy_nm.setText(arrayList.get(position).getCompanyName());
                String desc = (arrayList.get(position).getJobDescription());
                tv_alert_desc.setText(Html.fromHtml(desc));

                final android.app.AlertDialog.Builder alertbox = new android.app.AlertDialog.Builder(context.getActivity());
                alertbox.setMessage("Job Details");
                alertbox.setView(mView);
                AlertDialog dialog = alertbox.create();
              //  final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alertbox.setCancelable(false)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
          //      dialog.setCancelable(false);

                AlertDialog alertDialog2 = alertbox.create();
                alertDialog2.show();
                //dialog.show();
            }
        });
        if (start_date.before(sys_date) || start_date.equals(sys_date) && end_date.after(sys_date) || end_date.equals(sys_date)) {
            linear_button.setVisibility(View.VISIBLE);
            linear_msg.setVisibility(View.INVISIBLE);

            //Accept Job
            btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int staus_id = 3;// when accept button click then status id is 3.
                    progressDialog.show();

                    //GetAcceptApplyforJob
                    String webservice_url = UrlString.URL + "GetAcceptApplyforJob/";
                    String url_interviewInvitation = webservice_url.toString() + candidate_id+"/"+jobApplication_id +"/"+staus_id ;
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_interviewInvitation, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Result", response.toString());
                            String result = response.toString();

                            JSONObject jObject = null;
                            try {
                                jObject = new JSONObject(result);
                                String result_response = jObject.getString("GetAcceptApplyforJobResult");

                                //if (result.equals("\"Candidate Reject Interview\""))
                                if (result_response.equals("Candidate Accept Interview"))
                                {
//                                    Intent intent_accept = new Intent(context.getActivity(), Interviews.class);
//                                    intent_accept.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    intent_accept.putExtra("Key_candidate_id", candidate_id);
//                                    context.startActivity(intent_accept);
                                    args = new Bundle();
                                    args.putString("Key_candidate_id",candidate_id);
                                    loadFragment(new Interviews());
                                }
                                progressDialog.dismiss();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(context.getActivity(), "Please try again", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }) {
                    };
                    RequestQueue rQueue = Volley.newRequestQueue(context.getActivity());
                    rQueue.add(jsonObjReq);
                }
            });
            //For Reject
            btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int staus_id = 4;// when reject button click then status id is 4.
                    progressDialog.show();
                    //GetRectedJob
                    String webservice_url = UrlString.URL + "GetAcceptApplyforJob/";
                    String url_interviewInvitation = webservice_url.toString() + candidate_id+"/"+jobApplication_id +"/"+staus_id ;
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_interviewInvitation, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Result", response.toString());
                            String result = response.toString();
                                JSONObject jObject = null;
                            try {
                                    jObject = new JSONObject(result);
                                    String result_response = jObject.getString("GetAcceptApplyforJobResult");

                                    //if (result.equals("\"Candidate Reject Interview\""))
                                    if (result_response.equals("Candidate Reject Interview"))
                                    {
//                                        Intent intent_accept = new Intent(context.getActivity(), Home.class);
//                                        intent_accept.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        intent_accept.putExtra("Key_candidate_id", candidate_id);
//                                        context.startActivity(intent_accept);
                                        args = new Bundle();
                                        args.putString("Key_candidate_id",candidate_id);
                                        loadFragment(new Home());
                                    }
                                    progressDialog.dismiss();
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(context.getActivity(), "Please try again", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }) {

                    };
                    RequestQueue rQueue = Volley.newRequestQueue(context.getActivity());
                    rQueue.add(jsonObjReq);
                }
            });
        }
        else
        {
            linear_button.setVisibility(View.GONE);
            linear_msg.setVisibility(View.VISIBLE);
            if (start_date.after(sys_date))
            {
                tv_msg.setText("Interview Accept time is over.");
                tv_msg.setTextColor(Color.RED);
            }
            else
            {
                tv_msg.setText("Interview Accept Time is not start yet.");
                tv_msg.setTextColor(Color.GREEN);
            }
        }
        return convertView;
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
    private void updateLabelStart()
    {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        select_st_date=sdf.format(StartCalendar.getTime());

//        String myFormat2= "dd/MM/yyyy"; //In which you need put here
//        SimpleDateFormat sdf_new = new SimpleDateFormat(myFormat2, Locale.CANADA);
        ed_st_dt.setText(select_st_date);
    }
    private void updateLabelEnd()
    {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        select_end_date=sdf.format(EndCalendar.getTime());

        ed_end_dt.setText(select_end_date);
    }
}
