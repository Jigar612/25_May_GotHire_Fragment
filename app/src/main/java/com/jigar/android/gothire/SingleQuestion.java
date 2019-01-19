package com.jigar.android.gothire;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jigar.android.gothire.Adapter.AdapterSingleQuestion;
import com.jigar.android.gothire.SetterGetter.RowItemAnsList;
import com.jigar.android.gothire.SetterGetter.RowItemSingleQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import tcking.github.com.giraffeplayer2.VideoView;

public class SingleQuestion extends Fragment {

    ProgressDialog progressDialog;
    String webservice_url = UrlString.URL + "GetRoundsWiseJobs/";

    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    //**********

    TextView timerMinuts;
    TextView time_over;
    TextView tv_time_left;
   // TextView tv_question;
    TextView tv_question_counter;

   // TextView tv_description;
    ListView listview_singleQuestion ;
   // TextView interview_stage_name;

    LinearLayout linear_left_time;

    public static VideoView video_question;
    public static LinearLayout line_video_question;
//    Button btn_full_screen;
//    Button btn_exit_full_screen;
//    ProgressBar progressbar;
    LinearLayout linear_listview;
   // public static MediaController mediaController;

    ArrayList<String>ArrayList_QuestionId;
    ArrayList<String>ArrayList_QuestionMstId;
    ArrayList<String>ArrayList_statusCode;
    private boolean isCanceled = false;

    //Variable declaration
    String candidate_id,res_id,totalprocesstime,startdate,stage_name,ans_video,job_name,company_name,job_desc,accepted_on,ans_cal_From,ans_cal_to;
    String roundid,jobid,Jobquesid,quest_masterid,exam_startid,processtime;
    String round_examStart_dt;

    String respo_id_check;
    //For required countdown timer.
    String second_singleQues;
    String QuestionRecord;
    public static long diffInMillisec;
    //*****
    Button btn_submit_que;
    public static Button btn_skip;
    int skip;
    int question_starting_pos;
    public static int static_ques_pos;
    public static int static_total_question;

    //*********
    public static CountDownTimer countDownTimer;
    ArrayList<RowItemSingleQuestion>arrayList_singleQuestion;
    View view;
    Bundle args;

//    @Override
//    public void onBackPressed() {
//        Toast.makeText(this, "Can't go back", Toast.LENGTH_SHORT).show();
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_single_question, container, false);

        MainContainer.TAG="SingleQuestion";

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_DOWN){
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return true;
            }
        });
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        MainContainer.bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(true);
        ArrayList_QuestionId=new ArrayList<String>();
        ArrayList_QuestionMstId=new ArrayList<String>();
        ArrayList_statusCode=new ArrayList<String>();

        Bundle get_data = getArguments();;
        candidate_id = get_data.getString("Key_candidate_id");
        roundid = get_data.getString("RoundID");
        exam_startid =get_data.getString("Exam_StartID");
        jobid = get_data.getString("JobId");
        processtime = get_data.getString("process_time");
        res_id =get_data.getString("response_id");
        totalprocesstime =get_data.getString("total_process");
        startdate = get_data.getString("startdate");
        stage_name = get_data.getString("key_round_name");
        ans_video = get_data.getString("key_ans_video");
        job_name = get_data.getString("Key_job_nm");
        company_name = get_data.getString("Key_cpy_nm");
        job_desc =get_data.getString("Key_job_desc");
        accepted_on = get_data.getString("Key_interview_accepted_date_display");
        ans_cal_From =get_data.getString("selected_date");
        ans_cal_to = get_data.getString("selectedto");
        round_examStart_dt = get_data.getString("Key_examStart_dt");

        ArrayList_QuestionId =get_data.getStringArrayList("List_qustionId");//Getting arraylist
        ArrayList_QuestionMstId =get_data.getStringArrayList("List_qustionMstId");//Getting arraylist
        ArrayList_statusCode =get_data.getStringArrayList("List_statusCode");//Getting arraylist

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

                arrayList_singleQuestion = new ArrayList<RowItemSingleQuestion>();
                //All findviewbyid
              //   tv_question = (TextView)view.findViewById(R.id.tv_quesround_que);
              //   tv_description = (TextView)findViewById(R.id.tv_queround_description);
                 listview_singleQuestion = (ListView)view.findViewById(R.id.list2);
                 btn_skip=(Button)view.findViewById(R.id.btn_skip_single_que);
                 btn_submit_que=(Button)view.findViewById(R.id.btn_submit_ans_single_que);
                linear_left_time = (LinearLayout)view.findViewById(R.id.linear_time_left);
                time_over = (TextView)view.findViewById(R.id.txt_sing_que_over_time);
                timerMinuts = (TextView)view.findViewById(R.id.tv_sing_que_remain_minute);
                tv_time_left = (TextView)view.findViewById(R.id.tv_sing_que_msg_leftTime);
                tv_question_counter = (TextView)view.findViewById(R.id.tv_question_counter_single_question);
                video_question = (VideoView)view.findViewById(R.id.single_que_video);
                line_video_question = (LinearLayout)view.findViewById(R.id.linear_video_type_question);
//                btn_full_screen = (Button)view.findViewById(R.id.btn_full_screen);
//                btn_exit_full_screen = (Button)view.findViewById(R.id.btn_exit_full_screen);
//                progressbar = (ProgressBar)view.findViewById(R.id.progressbar);
                linear_listview=(LinearLayout)view.findViewById(R.id.l1);


                Typeface roboto_Reg = Typeface.createFromAsset(getActivity().getAssets(),
                        "fonts/Roboto-Regular.ttf");
                Typeface roboto_Light = Typeface.createFromAsset(getActivity().getAssets(),
                        "fonts/Roboto-Light.ttf");
                Typeface roboto_Med = Typeface.createFromAsset(getActivity().getAssets(),
                        "fonts/Roboto-Medium.ttf");
                time_over.setTypeface(roboto_Reg);
                tv_time_left.setTypeface(roboto_Reg);
                timerMinuts.setTypeface(roboto_Reg);

               // tv_question.setTypeface(roboto_Med);
                tv_question_counter.setTypeface(roboto_Med);

                btn_submit_que.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                   //     progressDialog.show();
                       AdapterSingleQuestion.btn_submit.callOnClick();
                   //     progressDialog.dismiss();
                    }
                });
                btn_skip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countDownTimer.cancel();
                        if(question_starting_pos <ArrayList_QuestionId.size()) {
                           // countDownTimer.cancel();
                            String checkQue_status_code = ArrayList_statusCode.get(question_starting_pos);
                            int count = 0;
                            while (!checkQue_status_code.equals("0") || !checkQue_status_code.equals("1")) {
                                if (checkQue_status_code.equals("1") || checkQue_status_code.equals("0")) {
                                    question_starting_pos++;
                                    if (question_starting_pos < ArrayList_QuestionId.size()) {

                                        checkQue_status_code = ArrayList_statusCode.get(question_starting_pos);
                                    } else {
                                        args =new Bundle();
                                        args.putString("Key_candidate_id",candidate_id);
                                        args.putString("Key_jobPost_id",jobid );
                                        args.putString("Key_jobround_id",roundid);
                                        args.putString("Key_jobProcess_time",processtime );
                                        args.putString("Key_examStart_dt",startdate);
                                        args.putString("Key_job_nm",job_name);
                                        args.putString("key_round_name",stage_name);
                                        args.putString("Key_totalProcess_time",totalprocesstime);
                                        args.putString("Key_cpy_nm",company_name);
                                        args.putString("Key_job_desc",job_desc );
                                        args.putString("Key_interview_accepted_date_display",accepted_on);
                                        loadFragment(new QuestionRound());
                                        break;
                                    }
                                } else {
                                    Jobquesid = ArrayList_QuestionId.get(question_starting_pos);
                                    quest_masterid = ArrayList_QuestionMstId.get(question_starting_pos);

                                    static_ques_pos=question_starting_pos;
                                    static_total_question=ArrayList_QuestionId.size();
                                    tv_question_counter.setText((question_starting_pos+1)+"/"+ArrayList_QuestionId.size());
                                    question_starting_pos++;

                                    if (ArrayList_QuestionId.size() == question_starting_pos)
                                    {
                                        btn_skip.setVisibility(View.GONE);
                                    }
                                    load_singleQuestion_webservice();
                                    break;
                                }
                                count++;
                            }
                        }
                        else
                        {
                            args =new Bundle();
                            args.putString("Key_candidate_id",candidate_id);
                            args.putString("Key_jobPost_id",jobid );
                            args.putString("Key_jobround_id",roundid);
                            args.putString("Key_jobProcess_time",processtime );
                            args.putString("Key_examStart_dt",startdate);
                            args.putString("Key_job_nm",job_name);
                            args.putString("key_round_name",stage_name);
                            args.putString("Key_totalProcess_time",totalprocesstime);
                            args.putString("Key_cpy_nm",company_name);
                            args.putString("Key_job_desc",job_desc );
                            args.putString("Key_interview_accepted_date_display",accepted_on);
                            loadFragment(new QuestionRound());
                        }
                    }
                });
                question_starting_pos=0;
                String check_ques_st_pos=ArrayList_statusCode.get(question_starting_pos);
                int count=0;
                while (!check_ques_st_pos.equals("0") || !check_ques_st_pos.equals("1"))
                {
                    if(ArrayList_QuestionId.size() == question_starting_pos)
                    {
                        Toast.makeText(getActivity(), "All questions have already been attempted", Toast.LENGTH_LONG).show();
                                             // getActivity().finish();
                        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                        args =new Bundle();
                        args.putString("Key_candidate_id",candidate_id);
                        args.putString("Key_jobPost_id",jobid );
                        args.putString("Key_jobround_id",roundid);
                        args.putString("Key_jobProcess_time",processtime );
                        args.putString("Key_examStart_dt",startdate);
                        args.putString("Key_job_nm",job_name);
                        args.putString("key_round_name",stage_name);
                        args.putString("Key_totalProcess_time",totalprocesstime);
                        args.putString("Key_cpy_nm",company_name);
                        args.putString("Key_job_desc",job_desc );
                        args.putString("Key_interview_accepted_date_display",accepted_on);
                        loadFragment(new QuestionRound());
                        break;
                    }
                    if(check_ques_st_pos.equals("0") || check_ques_st_pos.equals("1"))
                    {
                        question_starting_pos++;
//                        check_ques_st_pos=ArrayList_statusCode.get(question_starting_pos);
                        if (question_starting_pos < ArrayList_QuestionId.size()) {

                            check_ques_st_pos = ArrayList_statusCode.get(question_starting_pos);
                        }
                        else
                        {
                           // Toast.makeText(SingleQuestion.this, "All Questions have already been attempted", Toast.LENGTH_LONG).show();
  //                          getActivity().finish();
                            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                            args =new Bundle();
                            args.putString("Key_candidate_id",candidate_id);
                            args.putString("Key_jobPost_id",jobid );
                            args.putString("Key_jobround_id",roundid);
                            args.putString("Key_jobProcess_time",processtime );
                            args.putString("Key_examStart_dt",startdate);
                            args.putString("Key_job_nm",job_name);
                            args.putString("key_round_name",stage_name);
                            args.putString("Key_totalProcess_time",totalprocesstime);
                            args.putString("Key_cpy_nm",company_name);
                            args.putString("Key_job_desc",job_desc );
                            args.putString("Key_interview_accepted_date_display",accepted_on);
                            loadFragment(new QuestionRound());
                        }
                    }
                    else
                    {
                        Jobquesid = ArrayList_QuestionId.get(question_starting_pos);
                        quest_masterid = ArrayList_QuestionMstId.get(question_starting_pos);
//                        Jobquesid="3873";
//                        quest_masterid="8947";
                        static_ques_pos=question_starting_pos;
                        static_total_question=ArrayList_QuestionId.size();
                        tv_question_counter.setText((question_starting_pos+1)+"/"+ArrayList_QuestionId.size());
                        question_starting_pos++;
                        if(ArrayList_QuestionId.size()==question_starting_pos)
                        {
                            btn_skip.setVisibility(View.GONE);
                        }

                        load_singleQuestion_webservice();
                        break;
                    }
                    count++;
                }
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
    public void load_singleQuestion_webservice()
    {
        //"http://34.214.133.27:82/GotHireServices.svc/StartSingleQuestion/" + CandidateId + "/" + Roundid + "/" + Jobid + "/" + quesid + "/" + masterqueid + "/" + examstartid;
        progressDialog.show();
        String webservice_singleQue_url = UrlString.URL + "StartSingleQuestion/";

        //  String url_question_round = webservice_url.toString()+ 21 + "/" + 1019 + "/" + 1285 + "/" + 104 + "/" + 1 + "/" +formatted_dt;
        String url_single_question = webservice_singleQue_url.toString()+ candidate_id + "/" + roundid + "/" + jobid + "/" + Jobquesid + "/" + quest_masterid + "/" + exam_startid;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_single_question, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {
                    int count=0;
                    int count_ansList=0;
                    ArrayList arrayList_AnsList = new ArrayList<RowItemSingleQuestion>();

                    String ResponeTypeID,videoValidation,Question,DateTimeValidations,FilesizeType,filesize,DescriptionValidation,fileformateType;
                    //String QuestionRecord;
                    String QuestionDesc;
                    JSONArray resultJsonArr = response.getJSONArray("StartSingleQuestionResult");
                    while (count <resultJsonArr.length())
                    {
                        RowItemSingleQuestion rowItemSingleQuestion = new RowItemSingleQuestion();
                        JSONObject jObject = resultJsonArr.getJSONObject(count);
                        //get data from stargSingleQuestionResult...
                        ResponeTypeID=jObject.getString("ResponeTypeID");
                        respo_id_check=ResponeTypeID;
                        videoValidation=jObject.getString("VideoValidation");
                        second_singleQues=jObject.getString("Second");
                        Question=jObject.getString("Question");
                        DateTimeValidations=jObject.getString("DateTimeValidations");
                        FilesizeType=jObject.getString("FilesizeType");
                        filesize=jObject.getString("filesize");
                        DescriptionValidation=jObject.getString("DescriptionValidation");
                        fileformateType=jObject.getString("fileformateType");
                        QuestionRecord=jObject.getString("QuestionRecord");//QuestionRecord
                       // QuestionRecord="bfTsy.mp4";
                        QuestionDesc=jObject.getString("QuestionDesc");

                        String LeftLabel=jObject.getString("Leftlabel");
                        String RighLabel=jObject.getString("Rightlabel");
                        String CenterLabel=jObject.getString("Centerlabel");
                        //***********
//                        rowItemSingleQuestion.setArrayList_question_id(ArrayList_QuestionId);
//                        rowItemSingleQuestion.setArrayList_question_mst_id(ArrayList_QuestionMstId);
                        JSONArray JsonArrAnsList = jObject.getJSONArray("AnsList");
                        while (count_ansList<JsonArrAnsList.length())
                        {
                            RowItemAnsList rowItemAnsList = new RowItemAnsList();
                            JSONObject jObject_questionDisp = JsonArrAnsList.getJSONObject(count_ansList);

                            String Id_ansList=jObject_questionDisp.getString("Id");
                            String ansList_Istrue=jObject_questionDisp.getString("Istrue");
                            String ansList_Options=jObject_questionDisp.getString("Options");
                            String ansList_QueId=jObject_questionDisp.getString("QueId");
                            String ansList_ResponseType=jObject_questionDisp.getString("ResponseType");
                           // String ansList_file_ans = jObject_questionDisp.getString("file_ans");
                            rowItemAnsList.setAnsList_Id(Id_ansList);
                            rowItemAnsList.setAnsList_IsTrue(ansList_Istrue);
                            rowItemAnsList.setAnsList_option(ansList_Options);
                            rowItemAnsList.setAnsList_QueId(ansList_QueId);
                            rowItemAnsList.setAnsList_responseType(ansList_ResponseType);
                            rowItemAnsList.setAnsListCount(JsonArrAnsList.length());
                            //Set In setterGetter for requirement in adaper (single_question data).
                            //direct set from intent.
                            arrayList_AnsList.add(rowItemAnsList);
                            count_ansList++;
                        }
                        //set single_quistion data to set in setterGetter
                        rowItemSingleQuestion.setResponsetype_id(ResponeTypeID);
                        rowItemSingleQuestion.setVideoValidation(videoValidation);
                        rowItemSingleQuestion.setSeconds_video_valida(second_singleQues);
                        rowItemSingleQuestion.setQuestion(Question);
                        rowItemSingleQuestion.setDateTimeValidations(DateTimeValidations);
                        rowItemSingleQuestion.setFilesizeType(FilesizeType);
                        rowItemSingleQuestion.setFilesize(filesize);
                        rowItemSingleQuestion.setDescriptionValidation(DescriptionValidation);
                        rowItemSingleQuestion.setFileformateType(fileformateType);
                        rowItemSingleQuestion.setQuestionRecord(QuestionRecord);
                        rowItemSingleQuestion.setQuestionDesc(QuestionDesc);

                        rowItemSingleQuestion.setLeftLabel(LeftLabel);
                        rowItemSingleQuestion.setRighLabel(RighLabel);
                        rowItemSingleQuestion.setCenterLabel(CenterLabel);
                        //***************
                        rowItemSingleQuestion.setCandidateID(candidate_id);
                        rowItemSingleQuestion.setRoundID(roundid);
                        rowItemSingleQuestion.setJobId(jobid);
                        rowItemSingleQuestion.setMsaterQuestionID(quest_masterid);
                        rowItemSingleQuestion.setExamstartId(exam_startid);
                        rowItemSingleQuestion.setQueId(Jobquesid);
                        rowItemSingleQuestion.setJobprocesstime(processtime);
                        rowItemSingleQuestion.setTotalprocesstime(totalprocesstime);
                        rowItemSingleQuestion.setStartdate(startdate);
                        rowItemSingleQuestion.setVideo_ans(ans_video);
                        rowItemSingleQuestion.setJob_name(job_name);
                        rowItemSingleQuestion.setStage_name(stage_name);
                        rowItemSingleQuestion.setCompany_name(company_name);
                        rowItemSingleQuestion.setJob_desc(job_desc);
                        rowItemSingleQuestion.setAccepted_on(accepted_on);
                        //check if required
                        rowItemSingleQuestion.setFrom_date(ans_cal_From);
                        rowItemSingleQuestion.setTo_date(ans_cal_to);
                        rowItemSingleQuestion.setAnsList(arrayList_AnsList);

                        arrayList_singleQuestion.add(rowItemSingleQuestion);
                        count++;
                        //tv_question.setText(question_starting_pos+". "+Question);//display a question
                    }
                   // arrayList_singleQuestion.clear();
                    AdapterSingleQuestion adapterSingleQuestion = new AdapterSingleQuestion(arrayList_singleQuestion,getActivity().getLayoutInflater(),SingleQuestion.this,getContext());
                    listview_singleQuestion.setAdapter(adapterSingleQuestion);
                    progressDialog.dismiss();
                    UIUtils.setListViewHeightBasedOnItems(listview_singleQuestion);


                    //QuestionRecord =LiveLink.LinkLive2+"/TestimonialsImageVedio/SampleVideo_1280x720_2mb.mp4";
                  //  QuestionRecord ="/TestimonialsImageVedio/SampleVideo_1280x720_2mb.mp4";

                    if(QuestionRecord.equals("null") || QuestionRecord.equals("")  )//!QuestionRecord.equals("") ||!QuestionRecord.equals(null) ||
                    {
                        line_video_question.setVisibility(View.GONE);

                        LinearLayout.LayoutParams parameters_list = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                        parameters_list.weight = 80f;
                        linear_listview.setLayoutParams(parameters_list);
                    }
                    else
                    {

                        line_video_question.setVisibility(View.VISIBLE);
                    //    progressbar.setVisibility(View.VISIBLE);
                        //Oriantation remaining....
                        int orientation_initilation = SingleQuestion.this.getResources().getConfiguration().orientation;
                        if (orientation_initilation ==  Configuration.ORIENTATION_LANDSCAPE)// Configuration.ORIENTATION_PORTRAIT
                        {
                            LinearLayout.LayoutParams parameters_linear_center = new LinearLayout.LayoutParams(300, 400);
                            parameters_linear_center.gravity = Gravity.CENTER;
                            line_video_question.setLayoutParams(parameters_linear_center);
                        }

                        //QuestionRecord =LiveLink.LinkLive2+QuestionRecord;
                        String video_url = LiveLink.LinkLive2+QuestionRecord;

                  //      video_url = video_url.replaceAll("( )+", " ");
                       // video_question.setVideoPath("").getPlayer().stop();
                        video_question.setVideoPath(video_url).getPlayer().start();
                        //video_question.setVideoPath(QuestionRecord).getPlayer().start();
                        LinearLayout.LayoutParams parameters_list = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                        parameters_list.weight = 59f;
                        linear_listview.setLayoutParams(parameters_list);
                    }
                    //check condition if single quiestion timer is "0" or "null" then timer hide timer section.
                    if(second_singleQues.equals("0") || second_singleQues.equals(null))
                    {
                   //     timerSeconds.setVisibility(View.GONE);
                        timerMinuts.setVisibility(View.GONE);
                        time_over.setVisibility(View.GONE);
                        linear_left_time.setVisibility(View.GONE);
                    }
                    else
                    {
                        //start a countdown timer for single question....
                        clock_counter();
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
               // Toast.makeText(getActivity(), "There is an error", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

                getActivity().getFragmentManager().beginTransaction().remove(SingleQuestion.this).commit();
                args =new Bundle();
                args.putString("Key_candidate_id",candidate_id);
                args.putString("Key_jobPost_id",jobid );
                args.putString("Key_jobround_id",roundid);
                args.putString("Key_jobProcess_time",processtime );
                args.putString("Key_examStart_dt",startdate);
                args.putString("Key_job_nm",job_name);
                args.putString("key_round_name",stage_name);
                args.putString("Key_totalProcess_time",totalprocesstime);
                args.putString("Key_cpy_nm",company_name);
                args.putString("Key_job_desc",job_desc );
                args.putString("Key_interview_accepted_date_display",accepted_on);
                loadFragment(new QuestionRound());
                //bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(true);
            }
        }) {
        };
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjReq);
    }
    public void clock_counter()
    {
      //  long cnvt_sec=Long.parseLong(second_singleQues);
       // String second_singleQues="60";
        long cnvt_sec=Long.parseLong(second_singleQues);
        diffInMillisec=TimeUnit.SECONDS.toMillis(cnvt_sec);
       //long diffInMillisec  = Long.parseLong(second_singleQues);
        RunUpdateLoop_second();
    }
    private void RunUpdateLoop_second()
    {
            //countDownTimer.start();
             countDownTimer = new CountDownTimer(diffInMillisec, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                timerMinuts.setText(String.format("%02d:%02d:%02d",TimeUnit.MILLISECONDS.toHours(millis),TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
            }
            public void onFinish() {
                Toast.makeText(getActivity(), "Sorry, qustion time is over.", Toast.LENGTH_LONG).show();
                btn_skip.callOnClick();
                    //countDownTimer.cancel();
            }
        }.start();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // countDownTimer.cancel();
        video_question.setVideoPath("").getPlayer().stop();
        if (VideoQuestion.isPrepareTimerRunning==true)
        {
            VideoQuestion.PreparedTimer.cancel();
        }
        if (VideoQuestion.isTimerRunning==true)
        {
            VideoQuestion.countDownQuestionTimer.cancel();
        }
        //PreparedTimer.cancel();
    }
    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.frameLayout, fragment);//.addToBackStack(null).commit()
        fragmentTransaction.commit(); // save the changes
    }

}