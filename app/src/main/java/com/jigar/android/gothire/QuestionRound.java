package com.jigar.android.gothire;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jigar.android.gothire.Adapter.AdapterQuestionRound;
import com.jigar.android.gothire.SetterGetter.RowItemQuestionDisplay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuestionRound extends Fragment {

    ProgressDialog progressDialog;
    String webservice_url = UrlString.URL + "StartExamQuestion/";
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
    //Button & Textview as requirement
   // TextView timerSeconds;
    TextView timerDays;
    TextView timerMinuts;
    TextView tv_days_disp;
    TextView time_over;

    TextView tv_stage_nm,tv_instruction;
    Button btn_submit_Round11;
    Button btn_start_single_que;
    Button btn_back;
   // Button btn_demo;


    //static Variable for requirement
    public static String flag = null;
    //*****
    //variable
    String candidate_id1,jobname,companyname,jobdesc,accepted_dt,stage_name,round_examStart_dt,round_totProcess_time;
    String jobPost_id, jobRound_id;
    String jobProcess_time;
    String formatted_dt,res_round;
    //For Counter
    Date dt_st, dt_ed;
  //  long count1, count_min, count_hours, count_days;
    long diffInMillisec;
    //********
    CountDownTimer countDownTimer;

    String St_Ex_RoundId,St_Ex_ExamStartid;

    ArrayList<RowItemQuestionDisplay> arrayList_QuestionRound;

    ArrayList<String>ArrayList_QuestionId;
    ArrayList<String>ArrayList_QuestionMstId;
    ArrayList<String>ArraList_statuscode;

    Button btn_video,btn_checkBox,btn_ranking,btn_rating,btn_radio,btn_fileupload,btn_desc,btn_calendar;
    Button btn_dropdown;//Currently no used in new UI

    Bundle args;
    View view;
//      @Override
//      public void onBackPressed() {
//          Toast.makeText(this, "Can't go back", Toast.LENGTH_SHORT).show();
//      }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_question_round, container, false);

        MainContainer.TAG="QuestionRound";

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MainContainer.bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(true);

        time_over = (TextView)view.findViewById(R.id.txt_over_time);
        timerMinuts =(TextView)view.findViewById(R.id.tv_queround_remain_minute);
        tv_days_disp =(TextView)view.findViewById(R.id.tv_queround_days_disp);
        timerDays =(TextView)view.findViewById(R.id.tv_queround_remain_days);

        btn_submit_Round11 = (Button) view.findViewById(R.id.btn_submit_round);
        btn_start_single_que= (Button) view.findViewById(R.id.btn_start_now_que_round);
        btn_back = (Button)view.findViewById(R.id.btn_back_que_round);
       // btn_demo= (Button) findViewById(R.id.btn_demo_interview_brief);

        TextView tv_heading = (TextView) view.findViewById(R.id.tv_heading);
        TextView tv_question_type = (TextView) view.findViewById(R.id.tv_question_type);




        arrayList_QuestionRound = new ArrayList<RowItemQuestionDisplay>();

        ArrayList_QuestionId=new ArrayList<String>();
        ArrayList_QuestionMstId=new ArrayList<String>();
        ArraList_statuscode=new ArrayList<String>();

        // btn_video,btn_checkBox,btn_ranking,btn_rating,btn_radio,btn_fileupload,btn_desc,btn_calendar;
        btn_video=(Button)view.findViewById(R.id.btn_video_interview_brief);
        btn_checkBox=(Button)view.findViewById(R.id.btn_multi_choice_interview_brief);
        btn_ranking=(Button)view.findViewById(R.id.btn_ranking_interview_brief);
        btn_rating=(Button)view.findViewById(R.id.btn_rating_interview_brief);
        btn_radio=(Button)view.findViewById(R.id.btn_yes_no_interview_brief);
        btn_fileupload=(Button)view.findViewById(R.id.btn_file_upload_interview_brief);
        btn_desc=(Button)view.findViewById(R.id.btn_open_ended_interview_brief);
        btn_calendar=(Button)view.findViewById(R.id.btn_date_time_interview_brief);

       // Single_ques_Adapter.flag_single_ans_running = "conti";  Must Required

        tv_stage_nm =(TextView)view.findViewById(R.id.tv_stage_nm_interview_brief);
        tv_instruction =(TextView)view.findViewById(R.id.tv_instruction_interivew_brief);

        TextView tv_queround_msg_leftTime =(TextView)view.findViewById(R.id.tv_queround_msg_leftTime);
        Typeface roboto_Reg = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Light.ttf");
        Typeface roboto_Med = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Medium.ttf");
        tv_heading.setTypeface(roboto_Reg);
        tv_queround_msg_leftTime.setTypeface(roboto_Reg);
        time_over.setTypeface(roboto_Reg);
        timerMinuts.setTypeface(roboto_Reg);
        tv_days_disp.setTypeface(roboto_Reg);
        timerDays.setTypeface(roboto_Reg);

        tv_stage_nm.setTypeface(roboto_Med);
        tv_question_type.setTypeface(roboto_Med);

        btn_video.setTypeface(roboto_Med);
        btn_checkBox.setTypeface(roboto_Med);
        btn_ranking.setTypeface(roboto_Med);
        btn_rating.setTypeface(roboto_Med);
        btn_radio.setTypeface(roboto_Med);
        btn_fileupload.setTypeface(roboto_Med);
        btn_desc.setTypeface(roboto_Med);
        btn_calendar.setTypeface(roboto_Med);



        tv_instruction.setTypeface(roboto_Light);
        btn_submit_Round11.setTypeface(roboto_Light);
        btn_start_single_que.setTypeface(roboto_Light);

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

            flag = "start";
            AdapterQuestionRound.flag_attempted = "0";  //must be requirement....

            Bundle get_data = getArguments();
            candidate_id1 = get_data.getString("Key_candidate_id");
            jobPost_id = get_data.getString("Key_jobPost_id");
            jobRound_id = get_data.getString("Key_jobround_id");
            jobProcess_time = get_data.getString("Key_jobProcess_time");
            round_examStart_dt = get_data.getString("Key_examStart_dt");
            round_totProcess_time =get_data.getString("Key_totalProcess_time");
            //*******New get for JobRound
            jobname = get_data.getString("Key_job_nm");
            companyname = get_data.getString("Key_cpy_nm");
            jobdesc = get_data.getString("Key_job_desc");
            accepted_dt = get_data.getString("Key_interview_accepted_date_display");
            stage_name = get_data.getString("key_round_name");



             tv_stage_nm.setText(stage_name.toUpperCase());
            try
            {
                DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");//here check for date

                Date date = inputFormat.parse(round_examStart_dt);
                formatted_dt=outputFormat.format(date); //set to required String variable
            }
            catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(getActivity(), "Your answer is submitted. ", Toast.LENGTH_SHORT).show();
            }
            load_question_roudnd_webservice();

            int question_starting_pos=0;
//            String check_ques_st_pos=ArraList_statuscode.get(question_starting_pos);
//            while (!check_ques_st_pos.equals("0") || !check_ques_st_pos.equals("1")) {
//                if (ArrayList_QuestionId.size() == question_starting_pos) {
//                    if(ArrayList_QuestionId.size() == question_starting_pos) {
//
//                        btn_start_single_que.setVisibility(View.GONE);
//                    }
//
//                }
           // }


            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    args.putString("Key_candidate_id",candidate_id1);
//                    args.putString("Key_jobPost_id",jobPost_id);
//                    args.putString("Key_job_nm", jobname );
//                    args.putString("Key_cpy_nm",companyname);
//                    args.putString("Key_job_desc",jobdesc);
//                    args.putString("Key_interview_accepted_date_display",accepted_dt);
//                    args.putString("Key_image_path",pa);
//                    loadFragment(new JobRound());
                    args = new Bundle();
                    args.putString("Key_candidate_id",candidate_id1);
                    loadFragment(new Interviews());
                }
            });

            btn_start_single_que.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // load_question_roudnd_webservice();

                    args = new Bundle();
                    args.putStringArrayList("List_qustionId",ArrayList_QuestionId);
                    args.putStringArrayList("List_qustionMstId",ArrayList_QuestionMstId);
                    args.putStringArrayList("List_statusCode",ArraList_statuscode);
                    args.putString("JobId", jobPost_id);
                    args.putString("RoundID", jobRound_id);
                    args.putString("Key_candidate_id", candidate_id1);
                    args.putString("Exam_StartID", St_Ex_ExamStartid );
                    args.putString("Key_examStart_dt", round_examStart_dt );
                    args.putString("process_time",jobProcess_time);
                    args.putString("total_process",round_totProcess_time);
                    args.putString("startdate", formatted_dt);
                    args.putString("key_round_name",stage_name);
                    args.putString("Key_job_nm",jobname);
                    args.putString("Key_cpy_nm", companyname);
                    args.putString("Key_job_desc",jobdesc);
                    args.putString("Key_interview_accepted_date_display",accepted_dt);
                    loadFragment(new SingleQuestion());


                }
            });
//            btn_demo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                Intent howto_interview = new Intent(QuestionRound.this, HowToInterview1.class);
//                howto_interview.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                howto_interview.putExtra("Key_candidate_id", candidate_id1);
//                startActivity(howto_interview);
//                }
//            });
            btn_submit_Round11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert_selected = new AlertDialog.Builder(getActivity());
                    alert_selected.setTitle("Are you sure you want to submit this stage ?");

                    alert_selected.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            load_submit_round_webservices();
                        }
                    });
                    alert_selected.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(true);
                        //    bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(true);
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = alert_selected.create();
                    dialog.show();
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
    public void load_question_roudnd_webservice()
    {
        //+ CandidateId + "/" + Roundid + "/" + Jobid + "/" + processtime + "/" + totalprocess + "/" + examstartdate;
        progressDialog.show();

        //if timeing regarding error then change formated_date dd-MMM-yyyy to dd-MM-yyyy above.
        String url_question_round = webservice_url.toString()+ candidate_id1 + "/" + jobRound_id + "/" + jobPost_id + "/" + jobProcess_time + "/" + round_totProcess_time + "/" + formatted_dt;
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_question_round, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {

                    int count=0;
                    int count_questionDisp=0;
                    String ExamQuestiomasterID,isAttemped,ResoponseType,Second,Question,JobQuesID,status,Timeline,ResponeTypeID;

                    String St_Ex_CandidateId,St_Ex_JobId;
                    String St_Ex_NoOfQue = null,St_Ex_MinAttempt=null;

                    JSONArray resultJsonArr = response.getJSONArray("StartExamQuestionResult");

                    while (count <resultJsonArr.length())
                    {
                        JSONObject jObject = resultJsonArr.getJSONObject(count);

                        St_Ex_RoundId=jObject.getString("RoundId");
                        St_Ex_CandidateId=jObject.getString("CandidateId");
                        St_Ex_JobId=jObject.getString("JobId");
                        St_Ex_ExamStartid=jObject.getString("ExamStartid");
//                        St_Ex_NoOfQue= jObject.getString("NoOfQue");
//                        St_Ex_MinAttempt=jObject.getString("MinAttempt");

                        String startTime =jObject.getString("starttime");
                        String endTime = jObject.getString("Endtime");

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                           dt_st = format.parse(startTime);
                           dt_ed = format.parse(endTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        JSONArray JsonArrQuestionDisplay = jObject.getJSONArray("QuestionDisplay");
                        while (count_questionDisp<JsonArrQuestionDisplay.length())
                        {
                         //   RowItemQuestionDisplay item_QuestionDisplay = new RowItemQuestionDisplay();
                            JSONObject jObject_questionDisp = JsonArrQuestionDisplay.getJSONObject(count_questionDisp);

                            ExamQuestiomasterID=jObject_questionDisp.getString("ExamQuestiomasterID");
                            JobQuesID=jObject_questionDisp.getString("JobQuesID");
                            status=jObject_questionDisp.getString("status");
                            ResponeTypeID=jObject_questionDisp.getString("ResponeTypeID");

                            ArrayList_QuestionId.add(JobQuesID);
                            ArrayList_QuestionMstId.add(ExamQuestiomasterID);
                            ArraList_statuscode.add(status);




                            //}
                            String[] split = ResponeTypeID.split(",");
                            for(String s : split)
                            {
                                if(s.trim().equals("3"))
                                {
                                    btn_video.setBackgroundResource(R.color.button_back_blude);
                                }
                                //4 means
                                //Date/Time
                                if (s.trim().equals("4")) {
                                    btn_calendar.setBackgroundResource(R.color.button_back_blude);
                                }
                                //5 means
                                //Radio
                                if (s.trim().equals("5"))
                                {
                                    btn_radio.setBackgroundResource(R.color.button_back_blude);
                                }
                                //6 means
                                //MultiSelect
                                if (s.trim().equals("6"))
                                {
                                    btn_checkBox.setBackgroundResource(R.color.button_back_blude);
                                }
                                //7 means
                                //Desc
                                if (s.trim().equals("7"))
                                {
                                    btn_desc.setBackgroundResource(R.color.button_back_blude);
                                }
                                //1 means
                                //FileUpload
                                if (s.trim().equals("1"))
                                {
                                    btn_fileupload.setBackgroundResource(R.color.button_back_blude);
                                }
                                //14 means
                                //Rating
                                if (s.trim().equals("14"))
                                {
                                    btn_ranking.setBackgroundResource(R.color.button_back_blude);
                                }
                                //15 means
                                //Ranking
                                if (s.trim().equals("15"))
                                {
                                    btn_rating.setBackgroundResource(R.color.button_back_blude);
                                }
                            }
                            count_questionDisp++;
                        }
                        count++;
                    }
                    List<String> list_1 = new ArrayList<String>();
                    list_1.add("2");

                   // List<String> list_0 = new ArrayList<String>();
                   // list_0.add("0");

                    if(ArraList_statuscode.containsAll(list_1)) // ArraList_statuscode.containsAll(list_0))
                    {
                        btn_start_single_que.setVisibility(View.VISIBLE);
                    }
                 //   int status_count=0;
//                    while (status_count<ArraList_statuscode.size())
//                    {
//                        String checkQue_status_code = ArraList_statuscode.get(status_count);
//
//
//                        status_count++;
//                    }
//                    if(status.equals("0") || status.equals("1")) {
//
//                        btn_start_single_que.setVisibility(View.GONE);
//                    }
//                    else
//                    {
//                        btn_start_single_que.setVisibility(View.VISIBLE);
//                    }
                    //ArraList_statuscode.get(status)

//                    if (ArraList_statuscode.contains(2) || ArraList_statuscode.contains(1) ) {
//                        btn_start_single_que.setVisibility(View.VISIBLE);
//                    } else {
//                        btn_start_single_que.setVisibility(View.GONE);
//                    }
                  //  bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(true);
                    clock_counter();
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
              //  bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(true);
            }
        }) {

        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjReq);
    }
    public void clock_counter()
    {
         diffInMillisec  = dt_ed.getTime() - dt_st.getTime();
         RunUpdateLoop_second();
    }
    public void RunUpdateLoop_second()
    {
         countDownTimer = new CountDownTimer(diffInMillisec, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                timerDays.setText(String.format("%02d",TimeUnit.MILLISECONDS.toDays(millis)));
                timerMinuts.setText(String.format("%02d:%02d:%02d",TimeUnit.MILLISECONDS.toHours(millis),TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
//                timerHours.setText(String.format("%02d",TimeUnit.MILLISECONDS.toHours(millis)));//TimeUnit.MILLISECONDS.toHours(millis)
            }
            public void onFinish() {
              //  timerSeconds.setVisibility(View.GONE);
              //  timerMinuts.setVisibility(View.GONE);
             //   timerHours.setVisibility(View.GONE);
                tv_days_disp.setVisibility(View.GONE);
                timerDays.setVisibility(View.GONE);
                time_over.setVisibility(View.VISIBLE);
                time_over.setTextColor(Color.RED);
                time_over.setText("Your time is over");

                Toast.makeText(getActivity(), "Sorry, stage time over.", Toast.LENGTH_LONG).show();
                //load_submit_round_webservices();
                load_submit_round__time_over_webservices();
            }
        }.start();
    }
    public void load_submit_round_webservices()
    {
        String submit_round_url = UrlString.URL + "SubmitRound/";

        progressDialog.show();

        String url_submitRound= submit_round_url.toString() + candidate_id1 +"/"+jobRound_id +"/"+jobPost_id;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_submitRound, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {

                          JSONObject jObject = new JSONObject(result);
                          String result_submit_round = jObject.getString("SubmitRoundResult");//SubmitRoundResult

                    if (result_submit_round.equals("Success"))
                    {
//                        Intent intent_home = new Intent(getActivity(), Home.class);
//                        intent_home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent_home.putExtra("Key_candidate_id", candidate_id1);
//                        intent_home.putExtra("Key_jobPost_id", jobPost_id);
                        //startActivity(intent_home);
                        countDownTimer.cancel();
                        args =new Bundle();
                        args.putString("Key_candidate_id",candidate_id1);
                        args.putString("Key_jobPost_id",jobPost_id);
                        loadFragment(new Home());

                        //Finish();
                        flag = "stop";
                        Toast.makeText(getActivity(), "Stage submitted successfully", Toast.LENGTH_LONG).show();
                       // progressDialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),""+result_submit_round.toString(), Toast.LENGTH_LONG).show();
                    }
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

            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjReq);
    }
    public void load_submit_round__time_over_webservices()
    {
        String submit_round_url = UrlString.URL + "SubmitRound/";
        progressDialog.show();

        String url_submitRound= submit_round_url.toString() + candidate_id1 +"/"+jobRound_id +"/"+jobPost_id;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_submitRound, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {

                    JSONObject jObject = new JSONObject(result);
                    String result_submit_round = jObject.getString("SubmitRoundResult");//SubmitRoundResult

                    if (result_submit_round.equals("Success"))
                    {
//                        Intent intent_home = new Intent(getApplicationContext(), Home.class);
//                        //intent_home.SetFlags(ActivityFlags.NewTask);
//                        intent_home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent_home.putExtra("Key_candidate_id", candidate_id1);
//                        intent_home.putExtra("Key_jobPost_id", jobPost_id);
//                        startActivity(intent_home);
                        args =new Bundle();
                        args.putString("Key_candidate_id",candidate_id1);
                        args.putString("Key_jobPost_id",jobPost_id);
                        loadFragment(new Home());
                        flag = "stop";
                        Toast.makeText(getActivity(), "Stage successfully submitted.", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                       // Toast.makeText(QuestionRound.this,""+result_submit_round.toString(), Toast.LENGTH_LONG).show();
//                        Intent intent_viewRound = new Intent(getActivity(), JobRound.class);
//                        //intent_viewRound.SetFlags(ActivityFlags.NewTask);
//                        intent_viewRound.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent_viewRound.putExtra("Key_candidate_id", candidate_id1);
//                        intent_viewRound.putExtra("Key_jobPost_id", jobPost_id);
//                        intent_viewRound.putExtra("Key_job_nm", jobname);
//                        intent_viewRound.putExtra("Key_cpy_nm", companyname);
//                        intent_viewRound.putExtra("Key_job_desc", jobdesc);
//                        intent_viewRound.putExtra("Key_interview_accepted_date_display", accepted_dt);
//                        intent_viewRound.putExtra("key_round_name", stage_name);
//                        startActivity(intent_viewRound);

                        args =new Bundle();
                        args.putString("Key_candidate_id",candidate_id1);
                        args.putString("Key_jobPost_id",jobPost_id );
                        args.putString("Key_job_nm",jobname );
                        args.putString("Key_cpy_nm",companyname);
                        args.putString("Key_job_desc",jobdesc);
                        args.putString("Key_interview_accepted_date_display",accepted_dt);
                        args.putString("Key_image_path",stage_name);
                        loadFragment(new JobRound());
                        flag = "stop";
                    }
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
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjReq);
    }
    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}
