package com.jigar.android.gothire;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jigar.android.gothire.SetterGetter.RowItem_InterviewInvitation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class FinalStageInterview extends Fragment {

    String webservice_url = UrlString.URL + "InviteForFinalStageAndOffer/";

    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    //**********
    String Candidate_id,JobPostId;
   // BottomNavigationView bottomNavigationView;

    LinearLayout linear_final_stage_option;
    LinearLayout linear_btn_submit;
    LinearLayout linear_text_display;
    Button btn_submit_final_interview;

    public CheckBox[] checkbox;
    public CheckBox allOfAboveCheckBox;
    int chkId = 1001;
    int  countdata;
    String IsAcceptedByCandidate;
    String OfferId;
    String download_path;
    Bundle args;

    //TextView tv_display_date;

    ArrayList<String> ArrayList_ApproveByCandidate;//if all are false then open checkbox screen..
    ArrayList<String> ArrayList_InrweviewDate;
    ArrayList<String> ArrayList_ForFinalStageId;
    String datetime;
    String ans_select;

    View view;
//    TextView tv_remain_days;
//    TextView tv_remain_time;
//    long millis;
    Date date_expDate,date_Created;
    long diffInMillisec;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_final_stage_interview, container, false);

        MainContainer.TAG="FinalStageInterview";

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MainContainer.bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(true);

        Bundle get_data = getArguments();
        Candidate_id = get_data.getString("Key_candidate_id");
        JobPostId = get_data.getString("Key_jobPost_id");

        ArrayList_ApproveByCandidate=new ArrayList<String>();
        ArrayList_InrweviewDate=new ArrayList<>();
        ArrayList_ForFinalStageId=new ArrayList<String>();

        allOfAboveCheckBox = new CheckBox(getActivity());//New changes 03-Oct
        //allOfAboveCheckBox.setId(1);

        linear_final_stage_option=(LinearLayout)view.findViewById(R.id.linear_option_final_interview);
        linear_btn_submit=(LinearLayout)view.findViewById(R.id.linear_buttons_final_interview);
        btn_submit_final_interview=(Button)view.findViewById(R.id.btn_submit_final_interview);
        linear_text_display=(LinearLayout)view.findViewById(R.id.linear_text_disp_final_interview);

        //Regular
        TextView tv_accepted_period_final_interview = (TextView) view.findViewById(R.id.tv_accepted_period_final_interview);//tv_empty_list_home
        TextView tv_remain_acceptance_period_final_interview = (TextView) view.findViewById(R.id.tv_remain_acceptance_period_final_interview);//tv_empty_list_home

        //Bold
        TextView tv_congratulate_msg1_final_inteview = (TextView) view.findViewById(R.id.tv_congratulate_msg1_final_inteview);//tv_empty_list_home
        TextView tv_congratulate_msg2_final_interview = (TextView) view.findViewById(R.id.tv_congratulate_msg2_final_interview);//tv_empty_list_home

        //Light
        TextView tv_msg3_final_interview = (TextView) view.findViewById(R.id.tv_msg3_final_interview);//tv_empty_list_home
        TextView tv_disp_note = (TextView) view.findViewById(R.id.tv_disp_note);//tv_empty_list_home


        Typeface roboto_Bold = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Bold.ttf");
        Typeface roboto_Reg = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Light.ttf");
        tv_accepted_period_final_interview.setTypeface(roboto_Reg);
        tv_remain_acceptance_period_final_interview.setTypeface(roboto_Reg);

        tv_congratulate_msg1_final_inteview.setTypeface(roboto_Bold);
        tv_congratulate_msg2_final_interview.setTypeface(roboto_Bold);

        tv_msg3_final_interview.setTypeface(roboto_Light);
        tv_disp_note.setTypeface(roboto_Light);



       // tv_display_date=(TextView)findViewById(R.id.tv_1_interview_final);
//        tv_remain_days=(TextView)findViewById(R.id.tv_finalRound_remain_days);
//        tv_remain_time=(TextView)findViewById(R.id.tv_finalRound_remain_minute);

      //  bottomMenu();
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
            load_webserives_IntevieeFinalStage();


            allOfAboveCheckBox .setOnCheckedChangeListener(new
                 CompoundButton.OnCheckedChangeListener() {
                     @Override
                     public void onCheckedChanged(CompoundButton buttonView, boolean
                             isChecked) {
//                         if (allOfAboveCheckBox.isChecked()) {
//                             checkbox[0].setChecked(true);
//                             checkbox[1].setChecked(true);
//                         }else {
//                             checkbox[0].setChecked(false);
//                             checkbox[1].setChecked(false);
//                         }
                         if (allOfAboveCheckBox.isChecked()) {
                             for (int i = 0; i < countdata; i++) {
                                 checkbox[i].setChecked(true);
                             }
                         }
                         else
                         {
                             for (int i = 0; i < countdata; i++) {
                                 checkbox[i].setChecked(false);
                         }
                     }


                     }
                 });

            btn_submit_final_interview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ans_select="";
                    for (int i = 0; i < countdata; i++) {
                        if (checkbox[i].isChecked()) {
                            if (ans_select == "" || ans_select == null) {
                                ans_select = String.valueOf(checkbox[i].getId());
                            } else {
                                ans_select += "|" + checkbox[i].getId();
                            }
                        }
                    }
                    Load_submitDate_Webservies();
                }
            });
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
    public void load_webserives_IntevieeFinalStage()
    {
        // progressDialog.show();

        //By default is active then
        String url_interviewInvitation = webservice_url.toString() + Candidate_id +"/"+ JobPostId;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_interviewInvitation, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    JSONArray resultJsonArr = response.getJSONArray("InviteForFinalStageAndOfferResult");
                    int count=0;
                    int count_inviteForInterview=0;
                    int count_GetJobOffer=0;
                    while (count <resultJsonArr.length())
                    {
                       // RowItem_InterviewInvitation item_interviewInvitation = new RowItem_InterviewInvitation();
                        JSONObject jObject = resultJsonArr.getJSONObject(count);

                        //JSONArray JsonArrAnsList = jObject.getJSONArray("AnsList");
                        String getJobOfferJson= jObject.getString("GetJobOffer");
                        if(getJobOfferJson!="null" )
                        {
                            JSONArray JsonArrGetJobOffer = jObject.getJSONArray("GetJobOffer");
                            while (count_GetJobOffer<JsonArrGetJobOffer.length()) {

                                JSONObject jObject_inviteGetJobOffer = JsonArrGetJobOffer.getJSONObject(count_GetJobOffer);
                                 IsAcceptedByCandidate=jObject_inviteGetJobOffer.getString("IsAcceptedByCandidate");// null then accept decline vadi screen open
                                 OfferId =jObject_inviteGetJobOffer.getString("OfferId");
                                 download_path =jObject_inviteGetJobOffer.getString("UploadDocument");

                                String OfferExpiresDate=jObject_inviteGetJobOffer.getString("OfferExpiresDate");
                                Calendar calendar_expDate = Calendar.getInstance();
                                String datereip = OfferExpiresDate.replace("/Date(", "").replace(")/", "");
                                Long timeInMillis = Long.valueOf(datereip);
                                calendar_expDate.setTimeInMillis(timeInMillis);
                                 date_expDate = calendar_expDate.getTime(); //convert to date

//                              SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:aa");
//                              String datetime_exp = format1.format(date_expDate);

                                String CreatedDate=jObject_inviteGetJobOffer.getString("CreatedDate");
                                Calendar calendar_createdDate = Calendar.getInstance();
                                String datereip_created = CreatedDate.replace("/Date(", "").replace(")/", "");
                                Long timeInMillis_created = Long.valueOf(datereip_created);
                                calendar_createdDate.setTimeInMillis(timeInMillis_created);
                                date_Created = calendar_createdDate.getTime(); //convert to date

                                 count_GetJobOffer++;
                            }
                            diffInMillisec  = date_expDate.getTime() - date_Created.getTime();
                        }
                      //  String InterviewForFinalStage=jObject.getString("InviteForFinalStage");
                        JSONArray JsonArrInviteForFinalStage = jObject.getJSONArray("InviteForFinalStage");
                        while (count_inviteForInterview<JsonArrInviteForFinalStage.length()) {

                            JSONObject jObject_inviteForInterview = JsonArrInviteForFinalStage.getJSONObject(count_inviteForInterview);
                            String CandidateId=jObject_inviteForInterview.getString("CandidateId");
                            String IsApproveByCandidate =jObject_inviteForInterview.getString("IsApproveByCandidate");
                            String InviteCandidateForFinalStageId =jObject_inviteForInterview.getString("InviteCandidateForFinalStageId");

                            String InvitedDate =jObject_inviteForInterview.getString("InvitedDate");
                            Calendar calendar = Calendar.getInstance();
                            String datereip = InvitedDate.replace("/Date(", "").replace(")/", "");
                            Long timeInMillis = Long.valueOf(datereip);
                            calendar.setTimeInMillis(timeInMillis);

                            Date date = calendar.getTime();

                            //   SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:aa");
                            TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");

                            String dayNumberSuffix = getDayNumberSuffix(calendar.DAY_OF_MONTH);
                            String str_suffixe = String.valueOf((Html.fromHtml(dayNumberSuffix)));

                            SimpleDateFormat format1 = new SimpleDateFormat("EEEE MMM d'"+str_suffixe + "' hh:mma");
                            format1.setTimeZone(tz);


                            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
                            symbols.setAmPmStrings(new String[] { "am", "pm" });
                            format1.setDateFormatSymbols(symbols);

                            datetime = format1.format(date);

                            // datetime= String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) +"/"+String.valueOf(calendar.get(Calendar.MONTH)+"/"+String.valueOf(calendar.get(Calendar.YEAR))+" "+String.valueOf(calendar.get(Calendar.HOUR))+":"+String.valueOf(calendar.get(Calendar.MINUTE)) +":"+String.valueOf(calendar.get(Calendar.SECOND)));

                            ArrayList_ApproveByCandidate.add(IsApproveByCandidate);
                            ArrayList_ForFinalStageId.add(InviteCandidateForFinalStageId);
                            ArrayList_InrweviewDate.add(datetime);

                            countdata =ArrayList_ApproveByCandidate.size() ;
                            count_inviteForInterview++;
                        }
                        count++;
                    }
                    if(count_GetJobOffer==1) {
                        //IF changes then chages here...***********
                        if (IsAcceptedByCandidate.equals("null") || IsAcceptedByCandidate.equals(null) || IsAcceptedByCandidate.isEmpty()) {

                            Bundle get_data = getArguments();
                            String offer_givien  = get_data.getString("Key_offer_given_id");
                            if(offer_givien.equals("Offer given"))
                            {
                                args = new Bundle();
                                args.putString("Key_candidate_id", Candidate_id);
                                args.putString("Key_offer_id", OfferId);
                                args.putString("Key_jobPost_id", JobPostId);
                                args.putString("Key_download_path", download_path);
                                args.putString("Key_OfferTimeDiff",String.valueOf(diffInMillisec));
                                loadFragment(new OfferWithDocument());
                            }
                        }
                    }
//                   //iF no null then open offer WithDocument.......
                     if(ArrayList_ApproveByCandidate.contains("true")) {

                        linear_text_display.setVisibility(View.VISIBLE);
                        //   tv_display_date.setText(datetime);
                        for (int i = 0; i < countdata; i++) {

                            if(ArrayList_ApproveByCandidate.get(i).equals("true") || ArrayList_ApproveByCandidate.get(i).equals(true))
                            {
                                if ((getResources().getConfiguration().screenLayout &
                                        Configuration.SCREENLAYOUT_SIZE_MASK) ==
                                        Configuration.SCREENLAYOUT_SIZE_LARGE) {
                                    // on a large screen device ...
                                    TextView newTV = new TextView(getActivity());
                                    LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
                                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    newTV.setTextSize(28);
                                    newTV.setTextColor(Color.parseColor("#808284"));
                                    newTV.setLayoutParams(textParam);

                                    newTV.setText(ArrayList_InrweviewDate.get(i));
                                    linear_text_display.addView(newTV);
                                }
                                else
                                {
                                    TextView newTV = new TextView(getActivity());
                                    LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
                                            (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                                            );
                                    newTV.setTextSize(22);
                                    newTV.setTextColor(Color.parseColor("#808284"));
                                    newTV.setLayoutParams(textParam);
                                    newTV.setGravity(Gravity.CENTER);

                                    newTV.setText(ArrayList_InrweviewDate.get(i));
                                    linear_text_display.addView(newTV);
                                }
                            }
                        }
                    }
                    else
                    {
                        linear_final_stage_option.setVisibility(View.VISIBLE);
                        btn_submit_final_interview.setVisibility(View.VISIBLE);
                        call_chkBox();
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjReq);
    }
    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "<sup>th</sup>";
        }
        switch (day % 10) {
            case 1:
                return "<sup>st</sup>";
            case 2:
                return "<sup>nd</sup>";
            case 3:
                return "<sup>rd</sup>";
            default:
                return "<sup>th</sup>";
        }
    }
    public void Load_submitDate_Webservies()
    {
        String webservice_url = UrlString.URL + "SubmiteFinalstage/";
        String url_interviewInvitation = webservice_url.toString() + ans_select+"/"+Candidate_id +"/"+JobPostId ;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_interviewInvitation, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();

                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(result);
                    String result_response = jObject.getString("SubmiteFinalstageResult");

                    args = new Bundle();
                    args.putString("Key_candidate_id",Candidate_id);
                    args.putString("Key_jobPost_id",JobPostId );
                    args.putString("Key_offer_given_id","Final Stage" );
                    loadFragment(new FinalStageInterview());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjReq);
    }
    public void call_chkBox()
    {
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE)
        {
            checkbox = new CheckBox[25];
            LinearLayout.LayoutParams checkParas = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < countdata; i++) {
                checkbox[i] = new CheckBox(getActivity());
//                LinearLayout.LayoutParams checkParas = new LinearLayout.LayoutParams
//                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                checkbox[i].setScaleX(1.6f);
//                checkbox[i].setScaleY(1.6f);
                checkbox[i].setTextSize(28);
                checkbox[i].setLayoutParams(checkParas);
                checkbox[i].setId(Integer.parseInt(ArrayList_ForFinalStageId.get(i)));//New Changes
                checkbox[i].setText(ArrayList_InrweviewDate.get(i));
                checkbox[i].setTextColor(Color.parseColor("#808284"));
                linear_final_stage_option.addView(checkbox[i]);
            }

            allOfAboveCheckBox.setTextSize(28);
            allOfAboveCheckBox.setLayoutParams(checkParas);
            allOfAboveCheckBox.setText("All of the Above");
            allOfAboveCheckBox.setTextColor(Color.parseColor("#808284"));
            linear_final_stage_option.addView(allOfAboveCheckBox);

            //allOfAboveCheckBox.setId(Integer.parseInt(99));

        }
        else
        {
            checkbox = new CheckBox[25];
            LinearLayout.LayoutParams checkParas = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < countdata; i++) {
                checkbox[i] = new CheckBox(getActivity());
//                LinearLayout.LayoutParams checkParas = new LinearLayout.LayoutParams
//                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                checkbox[i].setLayoutParams(checkParas);

                checkbox[i].setId(Integer.parseInt(ArrayList_ForFinalStageId.get(i)));//New Changes
                checkbox[i].setText(ArrayList_InrweviewDate.get(i));
                checkbox[i].setTextColor(Color.parseColor("#808284"));
                checkbox[i].setTextSize(20);
                linear_final_stage_option.addView(checkbox[i]);
            }
            allOfAboveCheckBox.setTextSize(20);
            allOfAboveCheckBox.setLayoutParams(checkParas);
            allOfAboveCheckBox.setText("All of the Above");
            allOfAboveCheckBox.setTextColor(Color.parseColor("#808284"));
            linear_final_stage_option.addView(allOfAboveCheckBox);
        }
    }
    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm =  getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}
