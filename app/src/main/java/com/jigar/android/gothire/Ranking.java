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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jigar.android.gothire.Adapter.AdapterSingleQuestion;
import com.lunger.draglistview.DragListAdapter;
import com.lunger.draglistview.DragListView;
import tcking.github.com.giraffeplayer2.VideoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Ranking extends Fragment {

    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";

    private DragListView mDragListView;
    private ArrayList<String> mDatas;
    private ArrayList<String> arrayList_id;
    ArrayList<String>arrayList;
    Button btn_skip1,btn_submit;
    long timeDiffrent;
    TextView timerMinuts;
    TextView time_over;
    TextView tv_time_left;
    TextView tv_question_counter,tv_question_disp,tv_question_pos;

    String candidate_id,res_id,totalprocesstime,startdate,stage_name,ans_video,job_name,company_name,job_desc,accepted_on,ans_cal_From,ans_cal_to;
    String roundid,jobid,Jobquesid,quest_masterid,exam_startid,processtime;
    String round_examStart_dt;
    String Question_disp;
    String Question_record;

    String final_ans;

    ArrayList<String>ArrayList_Option = new ArrayList<>();
    ArrayList<String>ArrayList_RankId = new ArrayList<>();
    HashMap<Integer,String> hashMap;

    public CountDownTimer countDownTimer;
    View view;
    Bundle args;

    VideoView video_question;
    String video_url;
    LinearLayout line_video_question;
   // Button btn_full_screen;
   // Button btn_exit_full_screen;
    //ProgressBar progressbar;
    LinearLayout linear_listview;
   // MediaController mediaController;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_ranking, container, false);

        MainContainer.TAG="Ranking";


        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        findView();


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        Bundle get_data = getArguments();
        candidate_id = get_data.getString("Key_candidate_id");
        roundid = get_data.getString("RoundID");
        exam_startid =get_data.getString("Exam_StartID");
        jobid = get_data.getString("JobId");
        processtime = get_data.getString("process_time");
        res_id = get_data.getString("response_id");
        totalprocesstime =get_data.getString("total_process");
        startdate = get_data.getString("startdate");
        stage_name = get_data.getString("key_round_name");
        ans_video = get_data.getString("key_ans_video");
        job_name = get_data.getString("Key_job_nm");
        company_name = get_data.getString("Key_cpy_nm");
        job_desc = get_data.getString("Key_job_desc");
        accepted_on = get_data.getString("Key_interview_accepted_date_display");
        ans_cal_From =get_data.getString("selected_date");
        ans_cal_to = get_data.getString("selectedto");
        round_examStart_dt = get_data.getString("Key_examStart_dt");
        Question_disp = get_data.getString("question_disp");
        ArrayList_Option=get_data.getStringArrayList("List_rankigOption");//Getting arraylist
        ArrayList_RankId=get_data.getStringArrayList("List_rankigId");//Getting arraylist

        Question_record= get_data.getString("key_questionrRecord");


        video_question = (VideoView)view.findViewById(R.id.single_que_video_ranking);
        line_video_question = (LinearLayout)view.findViewById(R.id.linear_video_type_question);
       // btn_full_screen = (Button)view.findViewById(R.id.btn_full_screen);
      //  btn_exit_full_screen = (Button)view.findViewById(R.id.btn_exit_full_screen);
       // progressbar = (ProgressBar)view.findViewById(R.id.progressbar);
        linear_listview=(LinearLayout)view.findViewById(R.id.l1);


       // bottomMenu();
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


            initData();
            initDragListView();

            tv_question_counter.setText((SingleQuestion.static_ques_pos+1)+"/"+SingleQuestion.static_total_question);
            tv_question_pos.setText(SingleQuestion.static_ques_pos+1+". ");
            tv_question_disp.setText(Question_disp);

            //Question Display***************
        //    String Question_record="https://s3.amazonaws.com/androidvideostutorial/862014834.mp4";
//            if(!Question_record.equals("null") )//&& !respo_id_check.equals("15")
//            {
            if( Question_record.equals("null") || Question_record.equals("")  )//!QuestionRecord.equals("") ||!QuestionRecord.equals(null) ||
            {
                line_video_question.setVisibility(View.GONE);

                LinearLayout.LayoutParams parameters_list = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                parameters_list.weight = 63f;
                linear_listview.setLayoutParams(parameters_list);
            }
            else
            {

                line_video_question.setVisibility(View.VISIBLE);
                //progressbar.setVisibility(View.VISIBLE);
                //Oriantation remaining....
                int orientation_initilation = Ranking.this.getResources().getConfiguration().orientation;
                if (orientation_initilation ==  Configuration.ORIENTATION_LANDSCAPE)// Configuration.ORIENTATION_PORTRAIT
                {
                    LinearLayout.LayoutParams parameters_linear_center = new LinearLayout.LayoutParams(300, 200);
                    parameters_linear_center.gravity = Gravity.CENTER;
                    line_video_question.setLayoutParams(parameters_linear_center);
                }


                // video_url = LiveLink.LinkLive2+Question_record;
                //video_question.setVideoPath(video_url).getPlayer().stop();

               // video_question.setVideoPath(video_url).getPlayer().start();


                LinearLayout.LayoutParams parameters_list = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                parameters_list.weight = 45f;
                linear_listview.setLayoutParams(parameters_list);

                String video_Url_link = LiveLink.LinkLive2+Question_record;
                video_question.setVideoPath(video_Url_link).getPlayer();//.start();

            }

            //End Question Video

            if(SingleQuestion.btn_skip.getVisibility()==View.GONE)
            {
                btn_skip1.setVisibility(View.GONE);
            }
            else
            {
                btn_skip1.setVisibility(View.VISIBLE);

            }

            btn_skip1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //finish();

                    if( !Question_record.equals("null") && !Question_record.equals("")  )
                    {
                        video_question.getPlayer().stop();
//                        video_question.setVideoPath(video_url).getPlayer().stop();
//                       // video_question.getPlayer().stop();
                     }
                 //   video_question.getPlayer().stop();
                    getActivity().getFragmentManager().beginTransaction().remove(Ranking.this).commit();
                    countDownTimer.cancel();
                    SingleQuestion.btn_skip.callOnClick();

                    //SingleQuestion.video_question.stopPlayback();
//                    if(SingleQuestion.line_video_question.getVisibility() == View.GONE) {
//                        SingleQuestion.mediaController.setVisibility(View.VISIBLE);
//                        SingleQuestion.video_question.setVisibility(View.VISIBLE);
//                        }
                }
            });
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if( !Question_record.equals("null") && !Question_record.equals("")  )
                    {
                           video_question.getPlayer().stop();
//                        video_question.setVideoPath(video_url).getPlayer().stop();
//                        // video_question.getPlayer().stop();
                    }

                    //video_question.setVideoPath(video_url).getPlayer().stop();
                    String listString = "";
                    for (String s : mDatas)
                    {
                        if(!s.equals(null))
                        {
                            String breacket =s.replace("{","");
                            breacket =breacket.replace("}","");
                            System.out.println(breacket);

                            String[] sp_values=breacket.split("=");
                            String key_id = sp_values[0];
                            System.out.println(key_id);
                            listString += key_id + "|";
                        }
                    }
                    AdapterSingleQuestion.ans_ranking = listString.substring(0, listString.length()-1);
                    String ans_ranking = AdapterSingleQuestion.ans_ranking;
                    final_ans ="MultipleOption$" + ",RadioOption$" + ",Dropdown$" + ",DataDescription$" + ",FromDate$" + ",ToDate$" + ",FileData$" + ",VideoData$"  +",Rating$"+ ",Ranking$"+ans_ranking;
                  //  System.out.println("AnsRanking"+  AdapterSingleQuestion.ans_ranking);
                    load_submit_question();
                    //AdapterSingleQuestion.btn_submit.callOnClick();  //corrent current code 12-Oct-18


                    //finish();
                   // SingleQuestion.btn_skip.callOnClick();
                    //SingleQuestion.video_question.stopPlayback();
                   // if(SingleQuestion.line_video_question.getVisibility() == View.GONE) {
                        //SingleQuestion.mediaController.setVisibility(View.VISIBLE);
                       // SingleQuestion.video_question.setVisibility(View.VISIBLE);
                  //  }
                    countDownTimer.cancel();
                    getActivity().getFragmentManager().beginTransaction().remove(Ranking.this).commit();
                }
            });
            clock_counter();
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
    public void clock_counter()
    {
        timeDiffrent=SingleQuestion.diffInMillisec;
       // timeDiffrent=10000;
         RunUpdateLoop_second();
    }
    private void RunUpdateLoop_second()
    {
        //countDownTimer.start();
        countDownTimer = new CountDownTimer(timeDiffrent, 1000) {

            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                timerMinuts.setText(String.format("%02d:%02d:%02d",TimeUnit.MILLISECONDS.toHours(millis),TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
                // timerHours.setText(String.format("%02d",TimeUnit.MILLISECONDS.toHours(millis)));//TimeUnit.MILLISECONDS.toHours(millis)
                //timerMinuts.setText(String.format("%02d",TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))));
                // timerSeconds.setText(String.format("%02d",TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));

            }
            public void onFinish() {
                Toast.makeText(getActivity(), "Sorry, qustion time is over.", Toast.LENGTH_LONG).show();
                btn_submit.callOnClick();
                //SingleQuestion.btn_skip.callOnClick();

//                args =new Bundle();
//                args.putString("Key_candidate_id",candidate_id);
//                args.putString("Key_jobPost_id",jobid );
//                args.putString("Key_jobround_id",roundid);
//                args.putString("Key_jobProcess_time",processtime );
//                args.putString("Key_examStart_dt",startdate);
//                args.putString("Key_job_nm",job_name);
//                args.putString("key_round_name",stage_name);
//                args.putString("Key_totalProcess_time",totalprocesstime);
//                args.putString("Key_cpy_nm",company_name);
//                args.putString("Key_job_desc",job_desc );
//                args.putString("Key_interview_accepted_date_display",accepted_on);
//                loadFragment(new QuestionRound());
            }
            //  }
        }.start();
    }

    private void findView() {
        mDragListView = (DragListView) view.findViewById(R.id.lv);
        btn_skip1=(Button)view.findViewById(R.id.btn_skip_single_que_ranking);
        btn_submit=(Button)view.findViewById(R.id.btn_submit_ans_single_que_ranking);

        time_over = (TextView)view.findViewById(R.id.txt_sing_que_over_time_ranking);
        tv_time_left = (TextView)view.findViewById(R.id.tv_sing_que_msg_leftTime_ranking);
        timerMinuts = (TextView)view.findViewById(R.id.tv_sing_que_remain_minute_ranking);


        tv_question_counter = (TextView)view.findViewById(R.id.tv_question_counter_single_question_ranking);
        tv_question_disp = (TextView)view.findViewById(R.id.tv_question_disp_ranking);
        tv_question_pos=(TextView)view.findViewById(R.id.tv_quesround_pos_ranking);

        Typeface roboto_Reg = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Light.ttf");
        Typeface roboto_Med = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Medium.ttf");
        time_over.setTypeface(roboto_Reg);
        tv_time_left.setTypeface(roboto_Reg);
        timerMinuts.setTypeface(roboto_Reg);

        tv_question_disp.setTypeface(roboto_Med);
        tv_question_counter.setTypeface(roboto_Light);

    }
    private void initData() {
       // String[] name = { "jigar","kiwi","john","william"};
        //String[] id = { "1","2","3","4"};
        mDatas = new ArrayList<>();
        arrayList_id = new ArrayList<>();
        int count=0;
        while (count < ArrayList_Option.size()) {
            {
                //String nm=name[count];
                String name =ArrayList_Option.get(count);
                String id =ArrayList_RankId.get(count);

                hashMap = new HashMap<>();
                hashMap.put(Integer.valueOf(id),name);
                mDatas.add(String.valueOf(hashMap));
              //  mDatas.add(name);
               // arrayList_id.add(id);
                count++;
            }
        }
    }
    private void initDragListView() {
        mDragListView.setDragListAdapter(new MyAdapter(getActivity(), mDatas,arrayList_id));
        mDragListView.setDragger(R.id.iv_move_drag_list_items);
     ///   mDragListView.setItemFloatColor();
        //mDragListView.setItemFloatColor("#ffffff");
       // mDragListView.setItemFloatColor(String.valueOf(R.color.transperent));
        mDragListView.setItemFloatAlpha(1f);

//        mDragListView.setMyDragListener(new DragListView.MyDragListener() {
//            public void onDragFinish(int srcPositon, int finalPosition) {
//                Toast.makeText(Ranking.this, "beginPosition : " + srcPositon + "...endPosition : " + finalPosition, Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    class MyAdapter extends DragListAdapter {
        public MyAdapter(Context context, ArrayList<String> arrayTitles,ArrayList<String> arrayListId) {
            super(context, arrayTitles);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;

            view = LayoutInflater.from(getActivity()).inflate(
                    R.layout.drag_list_item, null);

            TextView textView = (TextView) view
                    .findViewById(R.id.tv_name_drag_list_items);
          //  textView.setText(mDatas.get(position));
           // textView.setId(Integer.parseInt(arrayList_id.get(position)));
            String get_name=mDatas.get(position);

            if(!get_name.equals(null))
            {

                String breacket =get_name.replace("{","");
                breacket =breacket.replace("}","");

                System.out.println(breacket);
                String[] sp_values=breacket.split("=");
                String values= sp_values[1];
                System.out.println(values);
                textView.setText(values);
            }
            return view;
        }
        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
    public void load_submit_question() {
        try { //New 28/Aug/2018..

            progressDialog.show();
            String submit_single_que = "http://35.162.89.140:83/Default/SubmitSinelQuestion";
            String url_singleQuestion = submit_single_que.toString();// + candidate_id1 +"/"+jobRound_id +"/"+jobPost_id;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_singleQuestion,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Result", response.toString());
                            String result = response.toString();
                            try {
                                if (result.equals("\"Success\"")) {
                                    SingleQuestion.countDownTimer.cancel();
                                    SingleQuestion.btn_skip.callOnClick();
                                    //countedound stop in SinggleQuestion
                                    Toast.makeText(getActivity(), "Answer submitted successfully", Toast.LENGTH_LONG).show();

                                    progressDialog.dismiss();
                                } else if (result.equals("\"Fail\"")) {
                                    Toast.makeText(getActivity(), "There is an error", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                } else if (result.equals("\"Error\"")) {
                                    Toast.makeText(getActivity(), "Try Again and please select proper", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                } else {
                                    //   Toast.makeText(context.getActivity(), "Optrion Response-"+result, Toast.LENGTH_LONG).show();
                                    //  Log.d("Response Option--",result);
                                    progressDialog.dismiss();
                                    //  SingleQuestion.btn_skip.callOnClick();
                                }
                                progressDialog.dismiss();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getActivity(), "There is an error", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            })  {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("CandidateID", candidate_id);
                    params.put("RoundID", roundid);
                    params.put("JobId", jobid);
                    params.put("QuestionID", quest_masterid);
                    params.put("MsaterQuestionID", quest_masterid);
                    params.put("ExamstartId", exam_startid);
                    params.put("OutputString", final_ans);
                    // Toast.makeText(context.getActivity(), "Parameters"+params, Toast.LENGTH_LONG).show();
                    return params;

                }
            };
            RequestQueue rQueue = Volley.newRequestQueue(getActivity());
            rQueue.add(stringRequest);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //New 28/Aug/2018..
        } catch (Exception e2) {
            e2.printStackTrace();
            progressDialog.dismiss();
        }
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
