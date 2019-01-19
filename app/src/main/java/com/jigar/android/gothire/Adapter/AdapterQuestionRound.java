package com.jigar.android.gothire.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jigar.android.gothire.QuestionRound;
import com.jigar.android.gothire.R;
import com.jigar.android.gothire.SetterGetter.RowItemQuestionDisplay;
import com.jigar.android.gothire.SingleQuestion;
import com.jigar.android.gothire.UrlString;
import com.jigar.android.gothire.VideoQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by COMP11 on 07-Mar-18.
 */

public class AdapterQuestionRound extends BaseAdapter {

    public static String flag_attempted = "0"; //If flag_attemted = 0 then refresh webservice and attempted button show if 1 then only toast ms print;
    public static int flag_uploding_video = 1; //If Uploading video in app then status is 0, if no video upolading then status is 1;
    //private ProgressDialog pd;
    String video_validation=null;
//    String cand_id;
    String second_from_singlQue=null;

    String cand_id;
    String round_id;
    String job_id;
    String job_que_id ;
    String qest_mst_id;
    String exam_start_id ;
    String job_process_time;
    String tot_process_time;
    String responsetype_id;
    String formated_date;
    String status;
    String second_queRound;
    String question_disp;
    String job_nm;
    String stage_name;
    String job_desc;
    String cpy_nm;
    String interv_accepted;


    Button btn_submit;



    ArrayList<RowItemQuestionDisplay> arrayList = new ArrayList<RowItemQuestionDisplay>();
    LayoutInflater inflater;
   // Context context;
    QuestionRound context;
   public AdapterQuestionRound(ArrayList myList, LayoutInflater inflater, QuestionRound context) {
        this.arrayList = myList;
        this.inflater = inflater;
        this.context = context;
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
        convertView = inflater.inflate(R.layout.listitem_question_round, null);

        TextView tv_Question_no = (TextView)convertView.findViewById(R.id.tv_ques_no);
        TextView tv_Responsetype = (TextView)convertView.findViewById(R.id.tv_queround_responsetype);
        TextView tv_timeline = (TextView)convertView.findViewById(R.id.tv_queround_timeline);
        TextView tv_ismend = (TextView)convertView.findViewById(R.id.tv_queround_mendatory);
        //ListView lv = (ListView) convertView.findViewById(R.id.list1);
        btn_submit = (Button) convertView.findViewById(R.id.btn_start);

        if (arrayList.get(position).getStatus().equals("2"))
        {
            //  btn_submit.Visibility = ViewStates.Visible;
            //  tv_status.Visibility = ViewStates.Invisible;
        }
        else if (arrayList.get(position).getStatus().equals("1"))
        {
            btn_submit.setText("Timeout");
            btn_submit.setEnabled(false);
            btn_submit.setBackgroundColor(Color.parseColor("#ff0000"));
        }
        else if (arrayList.get(position).getStatus().equals("0"))
        {
            btn_submit.setText("Attempted");
            btn_submit.setEnabled(false);
            btn_submit.setBackgroundColor(Color.parseColor("#f58220"));
        }
        int sr_no = position + 1;
        String cnvt_sr_no = String.valueOf(sr_no);
        tv_Question_no.setText(cnvt_sr_no);

        if (arrayList.get(position).getIsAttemped().equals("true"))
        {
            tv_ismend.setText("Yes");
        }
        else
        {
            tv_ismend.setText("No");
        }

        if (arrayList.get(position).getSecond().equals("0"))
        {
            tv_timeline.setText("--");
        }
        else
        {
            tv_timeline.setText(arrayList.get(position).getTimeline());//Changes 17/10/17 as required.
        }
        tv_Responsetype.setText(arrayList.get(position).getResoponseType());
        String job_question_id = arrayList.get(position).getJobQuesID();
        final String rounid = arrayList.get(position).getSt_Ex_RoundId();
        String Jigar =arrayList.get(position).getSt_Ex_CandidateId();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cand_id=arrayList.get(position).getSt_Ex_CandidateId();
                round_id = arrayList.get(position).getSt_Ex_RoundId();
                job_id = arrayList.get(position).getSt_Ex_JobId();
                job_que_id =arrayList.get(position).getJobQuesID();
                qest_mst_id = arrayList.get(position).getExamQuestiomasterID();
                exam_start_id =arrayList.get(position).getSt_Ex_ExamStartid();
                job_process_time=arrayList.get(position).getJobProcess_time();
                tot_process_time=arrayList.get(position).getRound_totProcess_time();
                second_queRound=arrayList.get(position).getSecond();

                responsetype_id=arrayList.get(position).getResponeTypeID();
                formated_date= arrayList.get(position).getFormatted_dt();
                status=arrayList.get(position).getStatus();
                question_disp= arrayList.get(position).getQuestion();
                job_nm= arrayList.get(position).getJobname();
                stage_name= arrayList.get(position).getStageName();
                job_desc= arrayList.get(position).getJob_desc();
                cpy_nm= arrayList.get(position).getCompany_name();
                interv_accepted= arrayList.get(position).getInterview_accepted();

                if (arrayList.get(position).getResponeTypeID().equals("3"))
                {
//                    single_class.singlequestionround(cand_id, round_id, job_id, job_que_id, qest_mst_id, exam_start_id);
//                    var row_single = single_class.StartSingleQuestionResult[0];
//                    video_validation = row_single.VideoValidation;
//                    second = row_single.Second;
                    load_singleQuestion_webservice();


                    if(!video_validation.equals(null) && !second_from_singlQue.equals(null))
                    {
                        Intent intent_video = new Intent(context.getActivity(), VideoQuestion.class);
                        //intent_video.SetFlags(ActivityFlags.NewTask);
                        flag_attempted = "1";
                        intent_video.putExtra("Key_candidate_id", cand_id);
                        intent_video.putExtra("RoundID", round_id);
                        intent_video.putExtra("JobId", job_id);
                        intent_video.putExtra("JobQuesID",job_que_id);
                        intent_video.putExtra("Question_MasterID", qest_mst_id);
                        intent_video.putExtra("process_time",job_process_time);
                        intent_video.putExtra("total_process",tot_process_time);
                        intent_video.putExtra("response_id",responsetype_id);
                        intent_video.putExtra("Exam_StartID", exam_start_id);
                        intent_video.putExtra("startdate", formated_date);
                        intent_video.putExtra("status",status);
                        //*****for Video Validation
                        intent_video.putExtra("video_validation", video_validation);
                        //**** for QuestionDisplay in video.
                        intent_video.putExtra("video_question_time",second_queRound );

                        intent_video.putExtra("question_disp",question_disp);
                        intent_video.putExtra("Key_job_nm", job_nm);
                        intent_video.putExtra("key_round_name",stage_name);
                        intent_video.putExtra("Key_job_desc",job_desc);
                        intent_video.putExtra("Key_cpy_nm",cpy_nm);
                        intent_video.putExtra("Key_interview_accepted_on",interv_accepted);
                        //** fro video_single_round_time_defalut
                        intent_video.putExtra("round_defalut_time", second_from_singlQue);
                        context.startActivity(intent_video);
                    }
                }
                else
                {
                    Intent intent_singleQue = new Intent(context.getActivity(), SingleQuestion.class);
                    flag_attempted = "1";
                    intent_singleQue.putExtra("Question_MasterID",qest_mst_id);
                    intent_singleQue.putExtra("JobQuesID",job_que_id);
                    intent_singleQue.putExtra("JobId", job_id);
                    intent_singleQue.putExtra("RoundID", round_id);
                    intent_singleQue.putExtra("Key_candidate_id", cand_id);
                    intent_singleQue.putExtra("Exam_StartID",exam_start_id);
                    intent_singleQue.putExtra("response_id", responsetype_id);
                    intent_singleQue.putExtra("process_time",job_process_time);
                    intent_singleQue.putExtra("total_process",tot_process_time);
                    intent_singleQue.putExtra("startdate", formated_date);
                    intent_singleQue.putExtra("key_round_name",stage_name);
                    intent_singleQue.putExtra("Key_job_nm",job_nm);
                    intent_singleQue.putExtra("Key_cpy_nm", cpy_nm);
                    intent_singleQue.putExtra("Key_job_desc",job_desc);
                    intent_singleQue.putExtra("Key_interview_accepted_date_display",interv_accepted);
                    context.startActivity(intent_singleQue);
                }

            }
        });
        return convertView;
    }

    public void load_singleQuestion_webservice()
    {
        //"http://34.214.133.27:82/GotHireServices.svc/StartSingleQuestion/" + CandidateId + "/" + Roundid + "/" + Jobid + "/" + quesid + "/" + masterqueid + "/" + examstartid;
       // progressDialog.show();
        String webservice_singleQue_url = UrlString.URL + "StartSingleQuestion/";

         String url_single_question = webservice_singleQue_url.toString()+ cand_id + "/" + round_id + "/" + job_id + "/" + job_que_id + "/" + qest_mst_id + "/" + exam_start_id;
        //http://34.214.133.27:82/GotHireServices.svc/StartExamQuestion/21/1019/1285/271/1/3-7-2018
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_single_question, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Result", response.toString());
                String result = response.toString();
                try {

                    int count=0;
                    int count_ansList=0;
//                    String ExamQuestiomasterID,isAttemped,ResoponseType,Second,Question,JobQuesID,status,Timeline,ResponeTypeID;
//
//                    String St_Ex_RoundId,St_Ex_CandidateId,St_Ex_JobId,St_Ex_ExamStartid;
//                    String St_Ex_NoOfQue = null,St_Ex_MinAttempt=null;

                    JSONArray resultJsonArr = response.getJSONArray("StartSingleQuestionResult");

                    while (count <resultJsonArr.length())
                    {
                        RowItemQuestionDisplay item_QuestionDisplay = new RowItemQuestionDisplay();
                        JSONObject jObject = resultJsonArr.getJSONObject(count);

                        video_validation=jObject.getString("VideoValidation");
                        second_from_singlQue=jObject.getString("Second");//From single questionc get 2 data.

                        //Set in setter getter from StartExamQuestionResult
//                        item_QuestionDisplay.setSt_Ex_ExamStartid(St_Ex_RoundId);
//                        item_QuestionDisplay.setSt_Ex_CandidateId(St_Ex_CandidateId);
//                        item_QuestionDisplay.setSt_Ex_JobId(St_Ex_JobId);
//                        item_QuestionDisplay.setSt_Ex_ExamStartid(St_Ex_ExamStartid);


                        JSONArray JsonArrAnsList = jObject.getJSONArray("AnsList");
                        while (count_ansList<JsonArrAnsList.length())
                        {
                            JSONObject jObject_questionDisp = JsonArrAnsList.getJSONObject(count_ansList);

                            String Istrue=jObject_questionDisp.getString("Istrue");

//                            isAttemped=jObject_questionDisp.getString("isAttemped");
//                            ResoponseType=jObject_questionDisp.getString("ResoponseType");
//                            Second=jObject_questionDisp.getString("Second");
//                            Question=jObject_questionDisp.getString("Question");
//                            JobQuesID=jObject_questionDisp.getString("JobQuesID");
//                            status=jObject_questionDisp.getString("status");
//                            Timeline=jObject_questionDisp.getString("Timeline");
//                            ResponeTypeID=jObject_questionDisp.getString("ResponeTypeID");


                            //set in setter getter from QuestionDisplay
//                            item_QuestionDisplay.setExamQuestiomasterID(ExamQuestiomasterID);
//                            item_QuestionDisplay.setIsAttemped(isAttemped);
//                            item_QuestionDisplay.setResoponseType(ResoponseType);
//                            item_QuestionDisplay.setSecond(Second);
//                            item_QuestionDisplay.setQuestion(Question);
//                            item_QuestionDisplay.setJobQuesID(JobQuesID);
//                            item_QuestionDisplay.setStatus(status);
//                            item_QuestionDisplay.setTimeline(Timeline);
//                            item_QuestionDisplay.setResponeTypeID(ResponeTypeID);


                            //Set In setterGetter for requirement in adaper.
//                            item_QuestionDisplay.setStageName(stage_name);
//                            item_QuestionDisplay.setFormatted_dt(formatted_dt);
//                            item_QuestionDisplay.setJobProcess_time(jobProcess_time);
//                            item_QuestionDisplay.setJobProcess_time(round_totProcess_time);
//                            item_QuestionDisplay.setJobname(jobname);
////
//                            item_QuestionDisplay.setJob_desc(jobdesc);
//                            item_QuestionDisplay.setCompany_name(companyname);
//                            item_QuestionDisplay.setInterview_accepted(accepted_dt);
//
//
//                            arrayList_QuestionRound.add(item_QuestionDisplay);
                            count_ansList++;
                        }
                        count++;
                    }


                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context.getActivity(), "Please try again", Toast.LENGTH_LONG).show();
           //     progressDialog.dismiss();
            }
        }) {

        };
        RequestQueue rQueue = Volley.newRequestQueue(context.getActivity());
        rQueue.add(jsonObjReq);

    }
}
