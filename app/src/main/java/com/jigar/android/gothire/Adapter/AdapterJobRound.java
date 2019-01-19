package com.jigar.android.gothire.Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jigar.android.gothire.FinalStageInterview;
import com.jigar.android.gothire.HireCongratulation;
import com.jigar.android.gothire.JobRound;
import com.jigar.android.gothire.OfferNoDocument;
import com.jigar.android.gothire.OfferWithDocument;
import com.jigar.android.gothire.QuestionRound;
import com.jigar.android.gothire.R;
import com.jigar.android.gothire.RejectedOffer;
import com.jigar.android.gothire.SetterGetter.RowItemGetRoundWiseJob;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by COMP11 on 06-Mar-18.
 */

public class AdapterJobRound extends BaseAdapter {
    ArrayList<RowItemGetRoundWiseJob> arrayList = new ArrayList<RowItemGetRoundWiseJob>();
    LayoutInflater inflater;
    Context context;
    Bundle args ;
  //  JobRound context;

    //long date;
//    Date dt1;
//    Date dt2;
   // LinearLayout linear_color_status;
    Date dt_st, dt_ed;
    long diffInMillisec;
    TextView timerDays;
    TextView timerMinuts;
    TextView tv_days_disp;
    CountDownTimer countDownTimer;

    String startTime;
    String endTime;


    public AdapterJobRound(ArrayList myList, LayoutInflater inflater, Context context) {
        this.arrayList = myList;
        this.inflater = inflater;
        this.context = context;
    }
//    public void updateResults(ArrayList<RowItemGetAcceptedJobResult> results)
//    {
//        arrayList = results;
//        //Triggers the list update
//        notifyDataSetChanged();
//    }
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

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listitems_job_round, null);

        TextView tv_round_stage = (TextView)convertView.findViewById(R.id.tv_job_nm_interview_breakdown_listitem);
     //   TextView tv_cpy_nm = (TextView)convertView.findViewById(R.id.tv_cpy_nm_interview_breakdown_listitem);

        TextView tv_round_status =(TextView)convertView.findViewById(R.id.tv_round_status);

        timerDays =(TextView)convertView.findViewById(R.id.tv_remain_days_interview_breakdown_listitem);
        timerMinuts =(TextView)convertView.findViewById(R.id.tv_remain_time_interview_breakdown_listitem);
        tv_days_disp =(TextView)convertView.findViewById(R.id.tv_days_disp_interview_breakdown_listitem);

        LinearLayout linear_time_remain=(LinearLayout)convertView.findViewById(R.id.linear_ramining_time);

        //***new code******
     //    linear_color_status = (LinearLayout)convertView.findViewById(R.id.linear_color_status);
        LinearLayout linear_row_select = (LinearLayout)convertView.findViewById(R.id.linear_select_interview_breakdown);


        Typeface roboto_Med = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Medium.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Light.ttf");
        tv_round_stage.setTypeface(roboto_Med);
        tv_round_status.setTypeface(roboto_Light);
        timerDays.setTypeface(roboto_Light);
        timerMinuts.setTypeface(roboto_Light);
        tv_days_disp.setTypeface(roboto_Light);

        tv_round_stage.setText(arrayList.get(position).getRoundname());

        if(tv_round_stage.getText().equals("Offer genrated"))
        {
            tv_round_stage.setText("Offer");
        }
        


        String round_stauts=arrayList.get(position).getRoundstatus();
        if (arrayList.get(position).getColor().equals(null))
        {
            tv_round_status.setText(round_stauts);
        }
        else
        {
            String color = arrayList.get(position).getColor();
            color = color.toLowerCase();
            String color_trim = color.trim();
            if (color_trim.equals("red"))
            {
                tv_round_status.setTextColor(Color.RED);
                tv_round_status.setText(round_stauts);
                linear_time_remain.setVisibility(View.GONE);
            }
            else if (color_trim.equals("green"))
            {
                tv_round_status.setTextColor(Color.GREEN);
                tv_round_status.setText(round_stauts);
                linear_time_remain.setVisibility(View.GONE);
              //  linear_color_status.setBackgroundColor(Color.GREEN);
            }
            else if (color_trim.equals("orange")) {
                //  tv_round_status.setTextColor(Color.parseColor("#f58220"));
                tv_round_status.setTextColor(context.getResources().getColor(R.color.my_orange));
                tv_round_status.setText(round_stauts);
                linear_time_remain.setVisibility(View.GONE);
             //   linear_color_status.setBackgroundResource(R.color.my_orange);

                if(tv_round_status.getText().equals("Exam Running"))
                {
                    linear_time_remain.setVisibility(View.VISIBLE);
                    //clock_counter();

                }

                if (arrayList.get(position).getRoundstatus() == "" && arrayList.get(position).getIsStart() == "true") {
                    tv_round_status.setText("Start your exam");
                    linear_time_remain.setVisibility(View.VISIBLE);
                }
            }
            else if (color_trim.equals("blue"))
            {
                tv_round_status.setTextColor(Color.BLUE);
                tv_round_status.setText(round_stauts);
                linear_time_remain.setVisibility(View.GONE);
             //   linear_color_status.setBackgroundResource(R.color.my_sky_dark);
            }
            else
            {
                 tv_round_status.setText(round_stauts);
                 linear_time_remain.setVisibility(View.GONE);
            }
                if (tv_round_status.equals(""))
                {
                    tv_round_status.setTextColor(Color.BLACK);
                   // tv_round_left_time.setText("--");
                    tv_round_status.setText("--");
                    linear_time_remain.setVisibility(View.GONE);

                }
                linear_row_select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (arrayList.get(position).getIsStart().equals("true") && (arrayList.get(position).getFinalStage().equals(null) || arrayList.get(position).getFinalStage().equals("null")))
                        {


                          //  intent.SetFlags(ActivityFlags.NewTask);
                            String candidate_id =arrayList.get(position).getCandidate_id();
                            String jobPost_id= String.valueOf(arrayList.get(position).getJobPost_id());
                            String rounId=arrayList.get(position).getRoundID();

                            String ProcessTime=arrayList.get(position).getProcessTIme();
                            String ExamStDate= arrayList.get(position).getExamStartdate().toString();
                            String JobName=arrayList.get(position).getJobName();
                            String RoundNm=arrayList.get(position).getRoundname();
                            String TotalProcessTime=arrayList.get(position).getTotalProcessTime().toString();
                            String company_nm= arrayList.get(position).getCpyName();
                            String desc= arrayList.get(position).getDesc();
                            String IntevAcceptdt=  arrayList.get(position).getIntervAcceptDt();

                            //countDownTimer.cancel();

                            args =new Bundle();
                            args.putString("Key_candidate_id",candidate_id);
                            args.putString("Key_jobPost_id",jobPost_id );
                            args.putString("Key_jobround_id",rounId);
                            args.putString("Key_jobProcess_time",ProcessTime );
                            args.putString("Key_examStart_dt",ExamStDate);
                            args.putString("Key_job_nm",JobName);
                            args.putString("key_round_name",RoundNm);
                            args.putString("Key_totalProcess_time",TotalProcessTime);
                            args.putString("Key_cpy_nm",company_nm);
                            args.putString("Key_job_desc",desc );
                            args.putString("Key_interview_accepted_date_display",IntevAcceptdt);
                            loadFragment(new QuestionRound());

//                            Intent intent_queRound = new Intent(context.getApplicationContext(), QuestionRound.class);
//                            intent_queRound.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent_queRound.putExtra("Key_candidate_id",candidate_id);
//                            intent_queRound.putExtra("Key_jobPost_id",jobPost_id );
//                            intent_queRound.putExtra("Key_jobround_id",rounId);
//                            intent_queRound.putExtra("Key_jobProcess_time",ProcessTime );
//                            intent_queRound.putExtra("Key_examStart_dt",ExamStDate);
//                            intent_queRound.putExtra("Key_job_nm",JobName);
//                            intent_queRound.putExtra("key_round_name",RoundNm);
//                            intent_queRound.putExtra("Key_totalProcess_time",TotalProcessTime);
//                            intent_queRound.putExtra("Key_cpy_nm",company_nm);
//                            intent_queRound.putExtra("Key_job_desc",desc );
//                            intent_queRound.putExtra("Key_interview_accepted_date_display",IntevAcceptdt);
//                            context.startActivity(intent_queRound);



                        }
                      //  String FinalStage=arrayList.get(position).getFinalStage();
                        else if (arrayList.get(position).getFinalStage().equals("true") && ((arrayList.get(position).getRoundstatus().equals("Offer given") || (arrayList.get(position).getRoundstatus().equals("Schedule interview accepted")))))
                        {
                            String candidate_id =arrayList.get(position).getCandidate_id();
                            String jobPost_id= String.valueOf(arrayList.get(position).getJobPost_id());

                        //    countDownTimer.cancel();
//                            Intent intent_final_interview = new Intent(context.getApplicationContext(), FinalStageInterview.class);
//                            intent_final_interview.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent_final_interview.putExtra("Key_candidate_id",candidate_id);
//                            intent_final_interview.putExtra("Key_jobPost_id",jobPost_id );
//                            intent_final_interview.putExtra("Key_offer_given_id","Final Stage" );
//                            context.startActivity(intent_final_interview);
                            args = new Bundle();
                            args.putString("Key_candidate_id",candidate_id);
                            args.putString("Key_jobPost_id",jobPost_id );
                            args.putString("Key_offer_given_id","Final Stage" );
                            loadFragment(new FinalStageInterview());
                        }
                        else if (arrayList.get(position).getFinalStage().equals("true") && arrayList.get(position).getRoundstatus().equals("Schedule interview"))
                        {
                            String candidate_id =arrayList.get(position).getCandidate_id();
                            String jobPost_id= String.valueOf(arrayList.get(position).getJobPost_id());

                           // countDownTimer.cancel();
//                            Intent intent_final_interview = new Intent(context.getApplicationContext(), FinalStageInterview.class);
//                            intent_final_interview.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent_final_interview.putExtra("Key_candidate_id",candidate_id);
//                             intent_final_interview.putExtra("Key_jobPost_id",jobPost_id );
//                            intent_final_interview.putExtra("Key_offer_given_id","Final Stage" );
//                            context.startActivity(intent_final_interview);

                            args = new Bundle();
                            args.putString("Key_candidate_id",candidate_id);
                            args.putString("Key_jobPost_id",jobPost_id );
                            args.putString("Key_offer_given_id","Final Stage" );
                            loadFragment(new FinalStageInterview());
                        }
                        else if (arrayList.get(position).getFinalStage().equals("true") && arrayList.get(position).getRoundstatus().equals("Hire"))
                        {
                            String candidate_id =arrayList.get(position).getCandidate_id();
                            String jobPost_id= String.valueOf(arrayList.get(position).getJobPost_id());

                           // countDownTimer.cancel();
//                            Intent intent_HireContra = new Intent(context.getApplicationContext(), OfferNoDocument.class);
//                            intent_HireContra.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent_HireContra.putExtra("Key_candidate_id",candidate_id);
//                           // intent_final_interview.putExtra("Key_jobPost_id",jobPost_id );
//                            intent_HireContra.putExtra("Key_offer_given_id","Final Stage" );
//                            context.startActivity(intent_HireContra);
                            args = new Bundle();
                            args.putString("Key_candidate_id",candidate_id);
                            args.putString("Key_offer_given_id","Final Stage" );
                            loadFragment(new OfferNoDocument());

                        }
                        //new
                        else if (arrayList.get(position).getIsOffer().equals("true") && arrayList.get(position).getRoundstatus().equals("Hire"))
                        {
                            String candidate_id =arrayList.get(position).getCandidate_id();
                            String jobPost_id= String.valueOf(arrayList.get(position).getJobPost_id());

                           // countDownTimer.cancel();
//                            Intent intent_HireContra = new Intent(context.getApplicationContext(), OfferNoDocument.class);
//                            intent_HireContra.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent_HireContra.putExtra("Key_candidate_id",candidate_id);
//                            context.startActivity(intent_HireContra);
                            args = new Bundle();
                            args.putString("Key_candidate_id",candidate_id);

                            loadFragment(new OfferNoDocument());
                        }
                        else if (arrayList.get(position).getIsOffer().equals("true")  && arrayList.get(position).getRoundstatus().equals("Offer Rejected"))
                        {
                            String candidate_id =arrayList.get(position).getCandidate_id();
                            String jobPost_id= String.valueOf(arrayList.get(position).getJobPost_id());

//                            Intent intent_HireContra = new Intent(context.getApplicationContext(), RejectedOffer.class);
//                            intent_HireContra.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent_HireContra.putExtra("Key_candidate_id",candidate_id);
//                            context.startActivity(intent_HireContra);
                            args = new Bundle();
                            args.putString("Key_candidate_id",candidate_id);
                            loadFragment(new RejectedOffer());
                        }

                        else if (arrayList.get(position).getIsOffer().equals("true") && ((arrayList.get(position).getRoundstatus().equals("Offer given") || (arrayList.get(position).getRoundstatus().equals("Schedule interview accepted")))))
                        {
                            String candidate_id =arrayList.get(position).getCandidate_id();
                            String jobPost_id= String.valueOf(arrayList.get(position).getJobPost_id());

                          //  countDownTimer.cancel();
//                            Intent intent_final_interview = new Intent(context.getApplicationContext(), FinalStageInterview.class);
//                            intent_final_interview.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent_final_interview.putExtra("Key_candidate_id",candidate_id);
//                            intent_final_interview.putExtra("Key_jobPost_id",jobPost_id );
//                            intent_final_interview.putExtra("Key_offer_given_id","Offer given" );
//                            context.startActivity(intent_final_interview);
                            args = new Bundle();
                            args.putString("Key_candidate_id",candidate_id);
                            args.putString("Key_jobPost_id",jobPost_id );
                            args.putString("Key_offer_given_id","Offer given" );

                            loadFragment(new FinalStageInterview());

                        }
                        else if (arrayList.get(position).getIsOffer().equals("true") && arrayList.get(position).getRoundstatus().equals("Schedule interview"))
                        {
                            String candidate_id =arrayList.get(position).getCandidate_id();
                            String jobPost_id= String.valueOf(arrayList.get(position).getJobPost_id());

                           // countDownTimer.cancel();
//                            Intent intent_final_interview = new Intent(context.getApplicationContext(), FinalStageInterview.class);
//                            intent_final_interview.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent_final_interview.putExtra("Key_candidate_id",candidate_id);
//                            intent_final_interview.putExtra("Key_jobPost_id",jobPost_id );
//                            intent_final_interview.putExtra("Key_offer_given_id","Offer given" );
//                            context.startActivity(intent_final_interview);
                            args = new Bundle();
                            args.putString("Key_candidate_id",candidate_id);
                            args.putString("Key_jobPost_id",jobPost_id );
                            args.putString("Key_offer_given_id","Offer given" );

                            loadFragment(new FinalStageInterview());
                        }
//
                    }
                });
            }
            return convertView;
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
                timerDays.setText(String.format("%02d", TimeUnit.MILLISECONDS.toDays(millis)));
                timerMinuts.setText(String.format("%02d:%02d",TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
//              timerHours.setText(String.format("%02d",TimeUnit.MILLISECONDS.toHours(millis)));//TimeUnit.MILLISECONDS.toHours(millis)
            }
            public void onFinish() {
                //  timerSeconds.setVisibility(View.GONE);
                //  timerMinuts.setVisibility(View.GONE);
                //   timerHours.setVisibility(View.GONE);
                tv_days_disp.setVisibility(View.GONE);
                timerDays.setVisibility(View.GONE);
//                time_over.setVisibility(View.VISIBLE);
//                time_over.setTextColor(Color.RED);
//                time_over.setText("Your time is over");

               // Toast.makeText(getApplicationContext(), "Sorry, stage time over.", Toast.LENGTH_LONG).show();

            }
        }.start();
    }

    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm =  ((Activity) context).getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}
