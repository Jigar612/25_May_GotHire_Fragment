package com.jigar.android.gothire.Adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.jigar.android.gothire.FileCapture;
import com.jigar.android.gothire.Home;
import com.jigar.android.gothire.JobRound;
import com.jigar.android.gothire.QuestionRound;
import com.jigar.android.gothire.R;
import com.jigar.android.gothire.Ranking;
import com.jigar.android.gothire.SetterGetter.RowItemAnsList;
import com.jigar.android.gothire.SetterGetter.RowItemSingleQuestion;
import com.jigar.android.gothire.SingleQuestion;
import com.jigar.android.gothire.VideoQuestion;
import com.lunger.draglistview.DragListAdapter;
import com.lunger.draglistview.DragListView;
import com.xw.repo.BubbleSeekBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * Created by COMP11 on 08-Mar-18.
 */

public class AdapterSingleQuestion  extends BaseAdapter {
    ArrayList<RowItemSingleQuestion> arrayList = new ArrayList<RowItemSingleQuestion>();
    LayoutInflater inflater;
    LinearLayout linear_main_single_que;
   // Context context;
    SingleQuestion context;
    //SingleQuestion singleQuestion =new SingleQuestion();'
    ProgressDialog progressDialog;

    Context context_new;
    Bundle args;
    List<RowItemAnsList> AnsList = new ArrayList<RowItemAnsList>();
 //   List<String>AnsList;
    public static String flag_single_ans_running = null;
    public EditText edit_ToDate;//txt_desc // edit_fromdate
    public TextView txt_msg_desc, txt_msg_radio, txt_msg_multiselect, txt_msg_dropdown, txt_msg_date1, txt_msg_file;
    //for video related
    public TextView txt_msg_video,txt_minimum_req,txt_max_req;
    public TextView txt_desc_validation, txt_file_validation, txt_cal_validation;
    public TextView tv_question,tv_question_pos;
    public Spinner spinner;
    String file_type;
    String DateValidation;

//    //For required Control
    public RadioGroup rg;
    public RadioButton[] rb;
    public CheckBox[] checkbox;
//    //*********

    //For SeekBar***
    int min_value = 0;
    int max_value = 10;
    //*********

    //ANSWERE STRING
    public String final_ans="";
    String[] split;
    int  countdata;
    public String ans_video="";
    public String ans_select="";
    public String ans_radio="-1";
    public String final_ans_spinner="";
    //public long ans_spinner;
    public String ans_Desc="";
    public String final_video_string="";
    public String ans_fromdate="";
    public String ans_todate="";
    public String ans_file="";
    public String from_date_trim, to_date_trim;
    public String ans_rating_seek="";
    public static String ans_ranking="";
    //***
    //For use calender conrol
//    Calendar FromCalendar;
//    Calendar EndCalendar;
    public String from_date_vali, to_date_vali, date_from, date_to;
//    Date date_to_val, date_frm_val;
//    Date select_date_from_con=new Date();
//    Date select_date_from_con1=new Date();
   // DatePickerDialog.OnDateSetListener date;
   // DatePickerDialog.OnDateSetListener dateTo;
   // TimePickerDialog mTimePicker;
    //********

    String response_ID ;
    String candidate_id ;
    String round_id;
    String job_id;
    String qustion_id;
    String master_que_id;
    String exam_start_id;
    String processtime ;
    String totalprocesstime;
    String startdate ;

    String stage_nm;
    String job_desc,job_name,company_name,accepted_dt;
    //Used in video question
    String videoValidation;
    String QuestionDisplay;
    String second_video_val;

    String LeftPos;
    String RighPos;
    String CenterPos;

    //File Upload
    Button button_upload;
    Button btn_goto_video_question;
    //DiscreteSeekBar seekBar_rating;
    BubbleSeekBar seekBar_rating;
 //   TextView tv_disp_minVal_seekbar,tv_disp_maxVal_seekbar;
    TextView tv_disp_poor,tv_disp_excelent;

   // SingleDateAndTimePicker singleDateAndTimePicker;
   SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleTimeFormat;


    private DragListView mDragListView;
    private ArrayList<String> mDatas;
    private ArrayList<String> arrayList_id;

    ArrayList<String>ArrayList_Option = new ArrayList<>();
    ArrayList<String>ArrayList_RankId = new ArrayList<>();
    HashMap<Integer,String> hashMap;
   // ViewHolder viewHolder=null;

    public static Button btn_submit;

    Context context1;

    public int space_count = 0;
    public AdapterSingleQuestion(ArrayList myList, LayoutInflater inflater, SingleQuestion singleQuestion,Context context1) {
        this.arrayList = myList;
        this.inflater = inflater;
        this.context = singleQuestion;
        this.context1 = context1;
        //this.singleQuestion=singleQuestion;
    }
    @Override
    public int getCount() {
        return 1;
        //return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


      class ViewHolder {

       public EditText txt_desc;
         //*********
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;


       //  if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem_singlequestion, null);

                viewHolder = new ViewHolder();
                viewHolder.txt_desc = (EditText)convertView.findViewById(R.id.txt_description);
               // viewHolder.rb = new RadioButton[25];
            // viewHolder.checkbox = new CheckBox[25];
                convertView.setTag(viewHolder);

                flag_single_ans_running = "over";//change status

                LinearLayout linear_question_disp = (LinearLayout) convertView.findViewById(R.id.linear_quesstion_disp);
                LinearLayout linear_main_single_que = (LinearLayout) convertView.findViewById(R.id.linear_main_single_que);
                LinearLayout linear_desc_msg_validation = (LinearLayout) convertView.findViewById(R.id.linear_desc_msg_validation);

              //  LinearLayout linear_desc = (LinearLayout) convertView.findViewById(R.id.linear_desc);

                ListView lv2 = (ListView) convertView.findViewById(R.id.list2);
                btn_submit = (Button) convertView.findViewById(R.id.btn_SingleQuestion_submit);
                LinearLayout l11 = (LinearLayout) convertView.findViewById(R.id.linear_opt);
                LinearLayout lin_Desc = (LinearLayout) convertView.findViewById(R.id.linear_desc);
                LinearLayout line_multi = (LinearLayout) convertView.findViewById(R.id.linear_multiselect);
                LinearLayout line_multi_msg = (LinearLayout) convertView.findViewById(R.id.linear_multiselect_msg);
                LinearLayout linear_videoview = (LinearLayout) convertView.findViewById(R.id.linear_video);
                LinearLayout lin_flie = (LinearLayout) convertView.findViewById(R.id.linear_file);
                LinearLayout linear_file_msg = (LinearLayout) convertView.findViewById(R.id.linear_file_msg);


              //  txt_desc = (EditText) convertView.findViewById(R.id.txt_description);
                //singleDateAndTimePicker = (SingleDateAndTimePicker) convertView.findViewById(R.id.single_day_picker);
                tv_question_pos = (TextView)convertView.findViewById(R.id.tv_quesround_pos_adapter);
                tv_question = (TextView)convertView.findViewById(R.id.tv_quesround_que_adapter);
                txt_minimum_req = (TextView) convertView.findViewById(R.id.txt_minimum_req);
                txt_max_req = (TextView) convertView.findViewById(R.id.txt_max_req);
                txt_msg_video = (TextView) convertView.findViewById(R.id.txt_msg_video);
                txt_msg_file = (TextView) convertView.findViewById(R.id.txt_msg_file);
                txt_file_validation = (TextView) convertView.findViewById(R.id.txt_file_validation);
                txt_cal_validation = (TextView) convertView.findViewById(R.id.txt_cal_validation);
                txt_msg_date1 = (TextView) convertView.findViewById(R.id.txt_msg_date1);

                Typeface roboto_Light = Typeface.createFromAsset(context1.getAssets(),
                        "fonts/Roboto-Light.ttf");
                Typeface roboto_Med = Typeface.createFromAsset(context1.getAssets(),
                        "fonts/Roboto-Medium.ttf");





                spinner = (Spinner) convertView.findViewById(R.id.spinner_dropdown);
                LinearLayout linear_spinner = (LinearLayout) convertView.findViewById(R.id.linear_dropdown);
                //Button btn_FromDate = (Button) convertView.findViewById(R.id.btn_from_Date);
                Button btn_EndDate = (Button) convertView.findViewById(R.id.btn_to_date);

                //LinearLayout linear_from_button = (LinearLayout) convertView.findViewById(R.id.linear_from_btn_Date);
                LinearLayout linear_to_button = (LinearLayout) convertView.findViewById(R.id.linear_to_btn_Date);
                LinearLayout linear_msg_to = (LinearLayout) convertView.findViewById(R.id.linear_msg_to_date);

             //   edit_fromdate = (EditText) convertView.findViewById(R.id.edittext_from_Date);
                edit_ToDate = (EditText) convertView.findViewById(R.id.edittext_to_date);
                button_upload = (Button) convertView.findViewById(R.id.btn_file_upload);

                LinearLayout linear_rating = (LinearLayout) convertView.findViewById(R.id.linear_rating);
               // LinearLayout linear_seekbar = (LinearLayout) convertView.findViewById(R.id.linear_seekbar_2);
                seekBar_rating = (BubbleSeekBar) convertView.findViewById(R.id.seekbar_rating_singlequestion);
               // tv_disp_minVal_seekbar = (TextView) convertView.findViewById(R.id.tv_disp_minVal_seekbar_singleQue);
               // tv_disp_maxVal_seekbar = (TextView) convertView.findViewById(R.id.tv_disp_maxVal_seekbar_singleQue);
                tv_disp_poor = (TextView) convertView.findViewById(R.id.tv_disp_poor);
                tv_disp_excelent = (TextView) convertView.findViewById(R.id.tv_disp_excelent);

                LinearLayout linear_ranking = (LinearLayout) convertView.findViewById(R.id.linear_ranking);
                mDragListView = (DragListView) convertView.findViewById(R.id.dragListView_singlequestion);

                btn_goto_video_question = (Button) convertView.findViewById(R.id.btn_go_to_video_ans);

                txt_msg_desc = (TextView) convertView.findViewById(R.id.txt_msg_desc);
                txt_desc_validation = (TextView) convertView.findViewById(R.id.txt_desc_validtion);


                txt_minimum_req.setTypeface(roboto_Light);
                txt_max_req.setTypeface(roboto_Light);
                txt_msg_video.setTypeface(roboto_Light);
                txt_msg_file.setTypeface(roboto_Light);
                txt_file_validation.setTypeface(roboto_Light);
                txt_cal_validation.setTypeface(roboto_Light);
                txt_msg_date1.setTypeface(roboto_Light);


               // tv_disp_minVal_seekbar.setTypeface(roboto_Light);
               // tv_disp_maxVal_seekbar.setTypeface(roboto_Light);
                tv_disp_poor.setTypeface(roboto_Light);
                tv_disp_excelent.setTypeface(roboto_Light);
                btn_goto_video_question.setTypeface(roboto_Light);
                txt_msg_desc.setTypeface(roboto_Light);
                txt_desc_validation.setTypeface(roboto_Light);



                AnsList = (arrayList.get(arrayList.size() - 1).getAnsList());
                //AnsList=arrayList.get(position).getAnsList().List<RowItemAnsList>;
                countdata = AnsList.size();
                response_ID = arrayList.get(arrayList.size() - 1).getResponsetype_id();

                candidate_id = arrayList.get(arrayList.size() - 1).getCandidateID();
                round_id = arrayList.get(arrayList.size() - 1).getRoundID();
                job_id = arrayList.get(arrayList.size() - 1).getJobId();
                qustion_id = arrayList.get(arrayList.size() - 1).getQueId();
                // qustion_id=AnsList.get(position).getAnsList_QueId(); //New Changes
                master_que_id = arrayList.get(arrayList.size() - 1).getMsaterQuestionID();
                exam_start_id = arrayList.get(arrayList.size() - 1).getExamstartId();
                processtime = arrayList.get(arrayList.size() - 1).getJobprocesstime();
                totalprocesstime = arrayList.get(arrayList.size() - 1).getTotalprocesstime();
                startdate = arrayList.get(arrayList.size() - 1).getStartdate();
                DateValidation = arrayList.get(arrayList.size() - 1).getDateTimeValidations();


                accepted_dt = arrayList.get(arrayList.size() - 1).getAccepted_on();
                //*******************************
                videoValidation = arrayList.get(arrayList.size() - 1).getVideoValidation();
                QuestionDisplay = arrayList.get(arrayList.size() - 1).getQuestion();
                second_video_val = arrayList.get(arrayList.size() - 1).getSeconds_video_valida();

                int pos = ++SingleQuestion.static_ques_pos;
                tv_question_pos.setText(pos+". ");
                tv_question.setText(QuestionDisplay);


                 progressDialog = new ProgressDialog(context.getActivity());
                 progressDialog.setIndeterminate(true);
                 progressDialog.setMessage("Please wait...");
                 progressDialog.setCancelable(false);

                //Create radioButton.
                rb = new RadioButton[25];
                linear_videoview.setVisibility(View.GONE);
                linear_spinner.setVisibility(View.GONE);
                lin_Desc.setVisibility(View.GONE);
                l11.setVisibility(View.GONE);
                line_multi.setVisibility(View.GONE);
                line_multi_msg.setVisibility(View.GONE);
                lin_flie.setVisibility(View.GONE);
                //linear_from_button.setVisibility(View.GONE);
               // linear_from_button.setVisibility(View.GONE);
                linear_to_button.setVisibility(View.GONE);
                linear_msg_to.setVisibility(View.GONE);

                //New
                linear_rating.setVisibility(View.GONE);
                linear_ranking.setVisibility(View.GONE);
                 //linear_seekbar.setVisibility(View.GONE);

                //String[] split = response_ID.split(String.valueOf(new char[] { ',' }));
                split = response_ID.split(",");

                btn_goto_video_question.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        args = new Bundle();
                        args.putString("CandidateId", candidate_id);
                        args.putString("RoundID", round_id);
                        args.putString("JobId", job_id);
                        args.putString("JobQuesID", qustion_id);
                        args.putString("Question_MasterID", master_que_id);
                        args.putString("process_time", processtime);
                        args.putString("total_process", totalprocesstime);
                        args.putString("response_id", response_ID);
                        args.putString("Exam_StartID", exam_start_id);
                        args.putString("startdate", startdate);
                        //******new 1_oct_18
                        int pos1 = SingleQuestion.static_ques_pos;
                        args.putString("question_pos", String.valueOf(pos1));
                        //**************

                        //*****for Video Validation
                        args.putString("video_validation", videoValidation);
                        //**** for QuestionDisplay in video.
                        args.putString("question_disp", QuestionDisplay);
                        args.putString("Key_job_nm", job_name);
                        args.putString("key_round_name", stage_nm);
                        args.putString("Key_job_desc", job_desc);
                        args.putString("Key_cpy_nm", company_name);
                        args.putString("Key_interview_accepted_date_display", accepted_dt);
                        args.putString("round_defalut_time", second_video_val);

                        if (SingleQuestion.line_video_question.getVisibility() == View.VISIBLE) {
                        //    SingleQuestion.video_question.stopPlayback();
                         //   SingleQuestion.video_question.getPlayer().stop();
                            SingleQuestion.video_question.setVideoPath("").getPlayer().stop();
                           // SingleQuestion.mediaController.setVisibility(View.GONE);
                           // SingleQuestion.video_question.setVisibility(View.GONE);
                        }

                        VideoQuestion videoQuestion = new VideoQuestion();
                        FragmentManager fm = ((Activity) context.getActivity()).getFragmentManager();
                        // create a FragmentTransaction to begin the transaction and replace the Fragment
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        videoQuestion.setArguments(args);
                        fragmentTransaction.add(R.id.frameLayout, videoQuestion);
                        fragmentTransaction.commit(); // save the ch

                    }
                });
                button_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (SingleQuestion.line_video_question.getVisibility() == View.VISIBLE) {
                            //  SingleQuestion.video_question.setVideoPath("").getPlayer().stop();
                           // SingleQuestion.video_question.stopPlayback();
                           // SingleQuestion.mediaController.setVisibility(View.GONE);
                        //    SingleQuestion.video_question.setVisibility(View.GONE);
                        }


                        args = new Bundle();
                        //   intent.SetFlags(ActivityFlags.NewTask);
                        args.putString("CandidateId", candidate_id);
                        args.putString("RoundID", round_id);
                        args.putString("JobId", job_id);
                        args.putString("JobQuesID", qustion_id);
                        args.putString("Question_MasterID", master_que_id);
                        args.putString("process_time", processtime);
                        args.putString("total_process", totalprocesstime);
                        args.putString("response_id", response_ID);
                        args.putString("Exam_StartID", exam_start_id);
                        args.putString("startdate", startdate);
                        args.putString("Key_job_nm", job_name);
                        args.putString("key_round_name", stage_nm);
                        args.putString("key_FileSize", arrayList.get(arrayList.size() - 1).getFilesize());
                        args.putString("key_filesize_type", arrayList.get(arrayList.size() - 1).getFilesizeType());
                        args.putString("key_file_Formatted", arrayList.get(arrayList.size() - 1).getFileformateType());
                        args.putString("Key_job_desc", job_desc);// file format(.doc/.pdf)
                        args.putString("Key_cpy_nm", company_name);
                        //loadFragment(new FileCapture());
                        FileCapture fileCapture = new FileCapture();
                        FragmentManager fm = ((Activity) context.getActivity()).getFragmentManager();
                        // create a FragmentTransaction to begin the transaction and replace the Fragment
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fileCapture.setArguments(args);
                        fragmentTransaction.add(R.id.frameLayout, fileCapture);
                        fragmentTransaction.commit(); // save the ch
                    }
                });
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                     //   progressDialog.show();
                        for (String s : split) {
                            if (!s.trim().equals("") || !s.trim().equals(null)) {
                                //3 means
                                //Video
                                if (s.trim().equals("3")) {
                                    final_video_string = ans_video;
                                    if (final_video_string.equals("") || final_video_string.equals(null)) {
                                        txt_msg_video.setText("Please record video");
                                    } else if (final_video_string != null)
                                        txt_msg_video.setVisibility(View.GONE);
                                }
                                //5 means
                                //Radio
                                if (s.trim().equals("5")) {
                                    ans_radio = String.valueOf(rg.getCheckedRadioButtonId());
                                    //Check Null
                                    if (ans_radio.equals("-1")) //5
                                    {
                                        txt_msg_radio.setText("Please select any one option");
                                    } else if (!ans_radio.equals("-1"))
                                        txt_msg_radio.setVisibility(View.GONE);
                                }
                                //6 means
                                //MultiSelect
                                if (s.trim().equals("6")) {
                                    ans_select = "";
                                    for (int i = 0; i < countdata; i++) {
                                        if (checkbox[i].isChecked()) {
                                            if (ans_select == "" || ans_select == null) {
                                                ans_select = String.valueOf(checkbox[i].getId());
                                            } else {
                                                ans_select += "|" + checkbox[i].getId();
                                            }
                                        }
                                    }
                                    if (ans_select == "" || ans_select == null) {
                                        txt_msg_multiselect.setText("Please select check boxes");
                                    } else if (ans_select != "" || ans_select != null)
                                        txt_msg_multiselect.setVisibility(View.GONE);
                                    ans_select = ans_select.trim();
                                }
                                //7 means
                                //Desc
                                if (s.trim().equals("7")) {
                                  //  hideKeyboard(viewHolder.txt_desc);
                                    String aaaa = viewHolder.txt_desc.getText().toString();
                                    String[] chars = new String[]{"/", ",", "@", "#", "$", "*", "\""};
                                    for (int i = 0; i < chars.length; i++) {
                                        if (aaaa.contains(chars[i])) {
                                            aaaa = aaaa.replace(chars[i], "");
                                        }
                                    }
                                    String bbbb = aaaa.replaceAll("( )+", " ");
                                    ans_Desc = bbbb;
                                    //For Word Counting for Desc
                                    if (ans_Desc != null) {
                                        String remove_space = ans_Desc.trim();
                                        String[] split_text = remove_space.split(" ");
                                        for (String av : split_text) {
                                            space_count++;
                                        }
                                    }
                                    if (ans_Desc == "" || ans_Desc == null) //7
                                    {
                                        txt_msg_desc.setText("Please enter an answer."); //Please enter your answer above. change.
                                    } else if (!arrayList.get(arrayList.size() - 1).getDescriptionValidation().equals(null) || !arrayList.get(arrayList.size() - 1).getDescriptionValidation().equals("")) {

                                        String[] split_desc = arrayList.get(arrayList.size() - 1).getDescriptionValidation().split("-");
                                        String aa = split_desc[0].trim();
                                        String bb = split_desc[1].trim();
                                        int minchar = Integer.parseInt(aa);
                                        int maxchar = Integer.parseInt(bb);
                                        if (space_count < minchar) {
                                            txt_msg_desc.setText("Please enter between" + " " + arrayList.get(arrayList.size() - 1).getDescriptionValidation() + " " + "words only.");
                                        } else if (space_count > maxchar) {
                                            txt_msg_desc.setText("Please enter between" + " " + arrayList.get(arrayList.size() - 1).getDescriptionValidation() + " " + "words only.");
                                        } else {
                                            if (ans_Desc != "" || ans_Desc != null) {
                                                txt_msg_desc.setText("");
                                            }
                                        }
                                    }
                                }
                                //10
                                //DropDown
                                if (s.trim().equals("10")) {
                                    if (final_ans_spinner == "" || final_ans_spinner == null)//10
                                    {
                                        txt_msg_dropdown.setText("Please select option.");
                                    } else if (final_ans_spinner != null || final_ans_spinner != "")
                                        txt_msg_dropdown.setVisibility(View.GONE);
                                }
                                //Calender
                                if (s.trim().equals("4")) {

                                    if (ans_fromdate == null || ans_fromdate == "" || ans_todate == null || ans_todate == "") {
                                        txt_msg_date1.setVisibility(View.VISIBLE);
                                        txt_msg_date1.setText("Please select date");
                                    } else if (ans_todate != null && ans_fromdate != null) {
                                        txt_msg_date1.setVisibility(View.INVISIBLE);
                                    }
                                }
                                if(s.trim().equals("15"))
                                {

                                }

                                //New
                                // Ranking 15
//                                if (s.trim().equals("15")) {
//                                }
                            }//if(s.trim="")
                            String jigar1 = ans_radio;
                            String jigar2 = ans_Desc;
                            String jigar3 = ans_select;
                            String jigar4 = final_ans_spinner;
                            String jigar5 = final_video_string;
                            String jigar6 = ans_fromdate;
                            String jigar7 = ans_todate;
                            String jigar8 = ans_file;
                            String jigar9 = ans_rating_seek;
                            String jigar10 = ans_ranking;

                            // if (jigar1=="" && jigar2 == "" && jigar3 == "" && jigar4 == "" &&  jigar5 == "" && jigar6 == "" && jigar7 == "" && jigar8 == "")
                            if (jigar1.equals("-1") && jigar2.equals("") && jigar3.equals("") && jigar4.equals("") && jigar5.equals("") && jigar6.equals("") && jigar7.equals("") && jigar8.equals("") && jigar9.equals("") && jigar10.equals("")) {
                                Toast.makeText(context.getActivity(), "Please try again give proper answer", Toast.LENGTH_SHORT).show();
                               // progressDialog.dismiss();
                            } else {
                                if (ans_radio == "-1")
                                    ans_radio = "";
                                try {
                                    if (txt_msg_desc.getText().equals("")) {
                                        final_ans = "MultipleOption$" + ans_select + ",RadioOption$" + ans_radio + ",Dropdown$" + final_ans_spinner + ",DataDescription$" + ans_Desc + ",FromDate$" + ans_fromdate + ",ToDate$" + ans_todate + ",FileData$" + ans_file + ",VideoData$" + final_video_string + ",Rating$" + ans_rating_seek + ",Ranking$" + ans_ranking;
                                        //HERE CALL A WEBSERVICES OF SUBMIT SINGLE QUIESTION......
                                        //progressDialog.show();
                                        load_submit_question();
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }//End foreach

                       // progressDialog.dismiss();
                    }
                });//End button.click
                //txt_file_validation.setText(arrayList.get(arrayList.size()-1).getFilesize() +" "+ arrayList.get(arrayList.size()-1).getFileformateType()+", Format - "+file_type+") ");

                for (String snew : split) {
                    //radio
                    if (snew.trim().equals("5")) {
                        l11.setVisibility(View.VISIBLE);
                    }
                    //desc
                    if (snew.trim().equals("7")) {
                        lin_Desc.setVisibility(View.VISIBLE);
                    }
                    //multi select
                    if (snew.trim().equals("6")) {
                        line_multi.setVisibility(View.VISIBLE);
                        line_multi_msg.setVisibility(View.VISIBLE);
                    }
                    //dropdown
                    if (snew.trim().equals("10")) {
                        linear_spinner.setVisibility(View.VISIBLE);
                    }
                    //Video
                    if (snew.trim().equals("3")) {
                        linear_videoview.setVisibility(View.VISIBLE);
                    }
                    //FileUpload
                    if (snew.trim().equals("1")) {
                        lin_flie.setVisibility(View.VISIBLE);
                        //Fileupload formate & size display
                        if (arrayList.get(arrayList.size() - 1).getFilesize() != null) {
                            file_type = arrayList.get(arrayList.size() - 1).getFileformateType();
                            if (arrayList.get(arrayList.size() - 1).getFileformateType().equals("")) {
                                file_type = "Any";
                            }
                            txt_file_validation.setText("( " + arrayList.get(arrayList.size() - 1).getFilesize() + " " + arrayList.get(arrayList.size() - 1).getFilesizeType() + ", Format - " + file_type + " )");
                        }
                    }
                    // Calender
                    if (snew.trim().equals("4")) {
                       // linear_from_button.setVisibility(View.VISIBLE);
                        linear_to_button.setVisibility(View.VISIBLE);
                        // linear_msg_from.Visibility = ViewStates.Visible;
                        linear_msg_to.setVisibility(View.VISIBLE);
                        txt_cal_validation.setVisibility(View.VISIBLE);
                    }
                    //New
                    // Rating
                    if (snew.trim().equals("14")) {
                        linear_rating.setVisibility(View.VISIBLE);
                       // linear_seekbar.setVisibility(View.VISIBLE);
                    }
                    //New
                    //Ranking
                    if (snew.trim().equals("15")) {
                        linear_ranking.setVisibility(View.VISIBLE);
                    }
                }//End forEach
                for (String s : split) {
                    if (!s.trim().equals("") || !s.trim().equals(null)) {
                        //Radio
                        if (s.trim().equals("5")) {
                            rg = new RadioGroup(context.getActivity());
                            //*******This is old code as running....
//                            txt_msg_radio = new TextView(context.getActivity()); // Pass it an Activity or Context
//                            RelativeLayout.LayoutParams Params_radio = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//                            txt_msg_radio.setLayoutParams(Params_radio);
//                            txt_msg_radio.setTextColor(Color.parseColor("#808184"));

                            //**********************
                            //This is new code for large screen 11/Jun****
                            if ((context.getResources().getConfiguration().screenLayout &
                                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                                    Configuration.SCREENLAYOUT_SIZE_LARGE) {

                                txt_msg_radio = new TextView(context.getActivity()); // Pass it an Activity or Context
                                RelativeLayout.LayoutParams Params_radio = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                                txt_msg_radio.setLayoutParams(Params_radio);
                                txt_msg_radio.setTextColor(Color.parseColor("#808184"));
                                txt_msg_radio.setTextSize(22);
                            } else {
                                txt_msg_radio = new TextView(context.getActivity()); // Pass it an Activity or Context
                                RelativeLayout.LayoutParams Params_radio = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                                txt_msg_radio.setLayoutParams(Params_radio);
                                txt_msg_radio.setTextColor(Color.parseColor("#808184"));
                            }
                            //*********************
                            for (int i = 0; i < countdata; i++) {
                                //*******This is old code as running....
//                                rb[i] = new RadioButton(this.context.getActivity());
//                                // rb[i].setId(Integer.parseInt(arrayList.get(i).getAnsList_Id()));
//                                //rb[i].setText( arrayList.get(i).getAnsList_option());
//                                rb[i].setId(Integer.parseInt(AnsList.get(i).getAnsList_Id()));
//                                rb[i].setText(AnsList.get(i).getAnsList_option());
//
//                                rb[i].setTextColor(Color.BLACK);
//
//                                rg.addView(rb[i]);
                                //**********************
                                // This is new code for large screen 11/Jun****
                                if ((context.getResources().getConfiguration().screenLayout &
                                        Configuration.SCREENLAYOUT_SIZE_MASK) ==
                                        Configuration.SCREENLAYOUT_SIZE_LARGE) {

                                    rb[i] = new RadioButton(this.context.getActivity());
                                    rb[i].setId(Integer.parseInt(AnsList.get(i).getAnsList_Id()));
                                    rb[i].setText(AnsList.get(i).getAnsList_option());

                                    rb[i].setTextColor(Color.BLACK);
                                    rb[i].setTextSize(23);
                                    rg.addView(rb[i]);
                                } else {
                                    rb[i] = new RadioButton(this.context.getActivity());
                                    rb[i].setId(Integer.parseInt(AnsList.get(i).getAnsList_Id()));
                                    rb[i].setText(AnsList.get(i).getAnsList_option());

                                    rb[i].setTextColor(Color.BLACK);

                                    rg.addView(rb[i]);
                                }
                                //*********************
                            }
                            l11.addView(rg);
                            l11.addView(txt_msg_radio);
                        }
                        //Desc
                        if (s.trim().equals("7")) {

                            linear_question_disp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    InputMethodManager imm = (InputMethodManager) v.getContext()
                                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                }
                            });
                            lin_Desc.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    InputMethodManager imm = (InputMethodManager) v.getContext()
                                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                }
                            });
                            viewHolder.txt_desc.requestFocus();
                            if (!arrayList.get(arrayList.size() - 1).getDescriptionValidation().equals(null) || !arrayList.get(arrayList.size() - 1).getDescriptionValidation().equals("")) {
                                txt_desc_validation.setText("(" + arrayList.get(arrayList.size() - 1).getDescriptionValidation() + " Words)");
                                ;
                            }
                        }
                        //Multi
                        if (s.trim().equals("6")) {
                            //*******This is old code as running....
//                            txt_msg_multiselect = new TextView(this.context.getActivity()); // Pass it an Activity or Context
//                            RelativeLayout.LayoutParams Parasms_multiSelect = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                            txt_msg_multiselect.setLayoutParams(Parasms_multiSelect);
//                            txt_msg_multiselect.setTextColor(Color.parseColor("#808184"));//808184
                            //************************
                            //This is new code for large screen 11/Jun****
                            if ((context.getResources().getConfiguration().screenLayout &
                                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                                    Configuration.SCREENLAYOUT_SIZE_LARGE) {

                                txt_msg_multiselect = new TextView(this.context.getActivity()); // Pass it an Activity or Context
                                RelativeLayout.LayoutParams Parasms_multiSelect = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                txt_msg_multiselect.setLayoutParams(Parasms_multiSelect);
                                txt_msg_multiselect.setTextSize(22);
                                txt_msg_multiselect.setTextColor(Color.parseColor("#808184"));//808184
                            } else {
                                txt_msg_multiselect = new TextView(this.context.getActivity()); // Pass it an Activity or Context
                                RelativeLayout.LayoutParams Parasms_multiSelect = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                txt_msg_multiselect.setLayoutParams(Parasms_multiSelect);
                                txt_msg_multiselect.setTextColor(Color.parseColor("#808184"));//808184
                            }
                            //*********************
                            checkbox = new CheckBox[25];
                            for (int i = 0; i < countdata; i++) {
                                //Old code as running*********
//                                checkbox[i] = new CheckBox(this.context.getActivity());
//                                checkbox[i].setId(Integer.parseInt(AnsList.get(i).getAnsList_Id()));//New Changes
//                                checkbox[i].setText(AnsList.get(i).getAnsList_option());
//                                checkbox[i].setTextColor(Color.BLACK);
//                                line_multi.addView(checkbox[i]);
                                //********************

                                //New code 11/Jun***
                                if ((context.getResources().getConfiguration().screenLayout &
                                        Configuration.SCREENLAYOUT_SIZE_MASK) ==
                                        Configuration.SCREENLAYOUT_SIZE_LARGE) {
                                    checkbox[i] = new CheckBox(this.context.getActivity());
                                    checkbox[i].setId(Integer.parseInt(AnsList.get(i).getAnsList_Id()));//New Changes
                                    checkbox[i].setText(AnsList.get(i).getAnsList_option());
                                    checkbox[i].setTextColor(Color.BLACK);
                                    checkbox[i].setTextSize(23);
                                    line_multi.addView(checkbox[i]);
                                } else {
                                    checkbox[i] = new CheckBox(this.context.getActivity());
                                    checkbox[i].setId(Integer.parseInt(AnsList.get(i).getAnsList_Id()));//New Changes
                                    checkbox[i].setText(AnsList.get(i).getAnsList_option());
                                    checkbox[i].setTextColor(Color.BLACK);
                                    line_multi.addView(checkbox[i]);
                                }
                                //*********************
                            }
                            line_multi_msg.addView(txt_msg_multiselect);
                        }
                        //DropDown
                        if (s.trim().equals("10")) {
                            txt_msg_dropdown = new TextView(context.getActivity()); // Pass it an Activity or Context
                            RelativeLayout.LayoutParams Parasms_dropDown = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                            txt_msg_dropdown.setLayoutParams(Parasms_dropDown);
                            txt_msg_dropdown.setTextColor(Color.parseColor("#808184"));
//                            List<string> droplist = new List<string>();
                            final List<String> droplist = new ArrayList<String>();
                            for (int i = 0; i < countdata; i++) {
//                                droplist.add(arrayList.get(i).getAnsList_option());
//                                spinner.setId(Integer.parseInt(arrayList.get(i).getAnsList_Id()));
                                droplist.add(AnsList.get(i).getAnsList_option());       //New Changes.
                                spinner.setId(Integer.parseInt(AnsList.get(i).getAnsList_Id()));
                            }
                            linear_spinner.addView(txt_msg_dropdown);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context.getActivity(),
                                    android.R.layout.simple_spinner_item, droplist);
                            spinner.setAdapter(adapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    //Height
                                    int aaa = position;
                                    final_ans_spinner = AnsList.get(aaa).getAnsList_Id();//Mew Chnges
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // TODO Auto-generated method stub
                                }
                            });
                        }

                        //Video
                        if (s.trim().equals("3")) {

                            //create new click event
                            if (!videoValidation.equals("null") || !videoValidation.equals("0")) {
                                String[] split_second = videoValidation.split("-");
                                String split_min_second = split_second[0].trim();
                                String split_max_second = split_second[1].trim();

                                long cnvt_min_sec = Long.parseLong(split_min_second);
                                long mini_Millisec = TimeUnit.SECONDS.toMillis(cnvt_min_sec);

                                long cnvt_max_sec = Long.parseLong(split_max_second);
                                long max_Millisec = TimeUnit.SECONDS.toMillis(cnvt_max_sec);

                                txt_minimum_req.setText(String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(mini_Millisec), TimeUnit.MILLISECONDS.toMinutes(mini_Millisec) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mini_Millisec)), TimeUnit.MILLISECONDS.toSeconds(mini_Millisec) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mini_Millisec))));
                                txt_max_req.setText(String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(max_Millisec), TimeUnit.MILLISECONDS.toMinutes(max_Millisec) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(max_Millisec)), TimeUnit.MILLISECONDS.toSeconds(max_Millisec) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(max_Millisec))));
                            }
                        }
                        //Calender
                        if (s.trim().equals("4")) {

                            //From date datepicker
//                            FromCalendar = Calendar.getInstance();
//                            date = new DatePickerDialog.OnDateSetListener() {
//                                @Override
//                                public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                                      int dayOfMonth) {
//                                    // TODO Auto-generated method stub
//                                    FromCalendar.set(Calendar.YEAR, year);
//                                    FromCalendar.set(Calendar.MONTH, monthOfYear);
//                                    FromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                                    updateLabelFrom();
//                                }
//
//                            };
                            //***********
                            //To date click to open datepicker...
//                            btn_FromDate.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
////                                    new DatePickerDialog(context.getActivity(), date, FromCalendar
////                                            .get(Calendar.YEAR), FromCalendar.get(Calendar.MONTH),
////                                            FromCalendar.get(Calendar.DAY_OF_MONTH)).show();
//                                }
//                            });
                            //***********
                            //EndCalendar = Calendar.getInstance();
                            //To date click to open datepicker...
                            btn_EndDate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Timepicker new
//                                    int hour = EndCalendar.get(Calendar.HOUR_OF_DAY);
//                                    int minute = EndCalendar.get(Calendar.MINUTE);
//                                    mTimePicker = new TimePickerDialog(context.getActivity(), new TimePickerDialog.OnTimeSetListener() {
//                                        @Override
//                                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                                            edit_ToDate.setText(selectedHour + ":" + selectedMinute);
//                                            String time = selectedHour + "-" + selectedMinute;
//                                            // String myFormat2= "hh-mm-ss"; //In which you need put here
////                                            SimpleDateFormat sdf_new = new SimpleDateFormat(myFormat2, Locale.CANADA);
////                                            edit_ToDate.setText(date_to);
//                                            ans_todate = time;
//                                        }
//                                    }, hour, minute, true);//Yes 24 hour time
//                                    mTimePicker.setTitle("Select Time");
//                                    mTimePicker.show();
                                    //************



                                    Calendar calendar = Calendar.getInstance();
                              //      Calendar calendar = new GregorianCalendar(1997, Calendar.JANUARY, 1);
                                    calendar.set(Calendar.HOUR_OF_DAY, 21);
                                    calendar.set(Calendar.MINUTE, 50);
                                    final Date defaultDate = calendar.getTime();

//                                    String dob = "01/08/1990";
//                                    String max_dt = "12/01/2050";
//                                    try {
//
//                                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//                                        Date d = sdf.parse(dob);
//                                        Calendar cal_min = Calendar.getInstance();
//                                        cal_min.setTime(d);
//
//                                        Date d_max = sdf.parse(dob);
//                                        Calendar cal_max = Calendar.getInstance();
//                                        cal_max.setTime(d_max);
//
//
//                                        cal_min.set(Calendar.YEAR, 1990);
//                                        final Date minDate = cal_min.getTime();
//                                        cal_max.set(Calendar.YEAR, 2050);
//                                        final Date maxDate = cal_max.getTime();



                                //    singleDateAndTimePicker.datech
                                    simpleDateFormat = new SimpleDateFormat("dd MMM yyyy ", Locale.getDefault());
                                    simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
//                                    String sDate1="31/12/1950";
//                                    String sDate2="31/12/2050";
//                                    try {
//                                        final Date minDate=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
//                                        final Date maxDate=new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
                                 //   int actionBarBackground = context1.getResources().getColor(R.color.font_heading2_color);
                                    new SingleDateAndTimePickerDialog.Builder(context1)
                                            //.bottomSheet()
                                            //.curved()
                                            //.minutesStep(15)

                                            .displayDays(false)
                                            .displayYears(true)
                                            .displayMonth(true)
                                            .displayDaysOfMonth(true)
                                            .displayAmPm(false)
                                            .defaultDate(defaultDate)
//                                            .minDateRange(minDate)
//                                            .maxDateRange(maxDate)

                                            .mainColor(context1.getResources().getColor(R.color.font_heading2_color))


                                            .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                                                @Override
                                                public void onDisplayed(SingleDateAndTimePicker picker) {
                                                    //retrieve the SingleDateAndTimePicker
                                                    //String date_time= picker.toString();
                                                }
                                            })

                                            .title("Please select date")
                                            .listener(new SingleDateAndTimePickerDialog.Listener() {
                                                @Override
                                                public void onDateSelected(Date date) {
                                                    String date_time= date.toString();
                                                    ans_fromdate = simpleDateFormat.format(date);
                                                    ans_todate = simpleTimeFormat.format(date);

                                                    edit_ToDate.setText(simpleDateFormat.format(date)+" - "+simpleTimeFormat.format(date));
                                                }
                                            }).display();
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }

                                }
                            });


                            //****
                        }
                        //New
                        // Rating
                        if (s.trim().equals("14")) {
                            LeftPos = arrayList.get(arrayList.size() - 1).getLeftLabel();
                            RighPos = arrayList.get(arrayList.size() - 1).getRighLabel();
                            CenterPos = arrayList.get(arrayList.size() - 1).getCenterLabel();


                            String left = "";
                            String left_string = "POOR";
                            String right = "";
                            String right_string = "EXCELLENT";
                            //  if (userEmail != null && !userEmail.isEmpty() && !userEmail.equals("null"))
                            if (LeftPos.equals("-") || LeftPos == null || LeftPos.isEmpty() || LeftPos.equals("null")) {
                                min_value = 0;
                            } else {
                                String[] get_min_value = LeftPos.split(",");
                                left = get_min_value[0];
                                if(get_min_value.length>=2)
                                {
                                    left_string = get_min_value[1];
                                }


                                //left =LeftPos.substring(0, LeftPos.length()-1);
                                min_value = Integer.parseInt(left);
                            }

                            if (RighPos.equals("-") || RighPos == null || RighPos.isEmpty() || RighPos.equals("null")) {
                                max_value = 10;
                            } else {
                                //  right=RighPos.substring(0, RighPos.length()-1);
                                String[] get_max_value = RighPos.split(",");
                                right = get_max_value[0];
                                if(get_max_value.length>=2) {
                                    right_string = get_max_value[1];
                                }
                                max_value = Integer.parseInt(right);
                            }

                          //  tv_disp_minVal_seekbar.setText(String.valueOf(min_value));
                           // tv_disp_maxVal_seekbar.setText(String.valueOf(max_value));

                            tv_disp_poor.setText(String.valueOf(min_value + " - " + left_string));
                            tv_disp_excelent.setText(String.valueOf(max_value + " - " + right_string));

                          //  seekBar_rating.getConfigBuilder().max(max_value);
                          //  seekBar_rating.getConfigBuilder().min(min_value);

                            //  tv_disp_seekbar.setText(String.valueOf(min_value));
                            //ans_rating_seek = String.valueOf(min_value);
                            //seekBar_rating.setLeft(min_value);
                           // seekBar_rating.setRight(max_value);

                            seekBar_rating.getConfigBuilder()
                                    .min(min_value)
                                    .max(max_value)
                                    .sectionCount(1)
                                    .alwaysShowBubble()
                                    .build();



                            seekBar_rating.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                                @Override
                                public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                                   // if (progress>=min_value && progress <= max_value) {
                                        ans_rating_seek = String.valueOf(progress);
                                    //}
                                }

                                @Override
                                public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

                                }

                                @Override
                                public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

                                }
                            });
                        }

                        //Ranking
                        if (s.trim().equals("15")) {
                            ArrayList<String> ArrayList_Option = new ArrayList<>();
                            ArrayList<String> ArrayList_RankingId = new ArrayList<>();

                            for (int i = 0; i < countdata; i++) {
                                ArrayList_Option.add(AnsList.get(i).getAnsList_option());
                                ArrayList_RankingId.add(AnsList.get(i).getAnsList_Id());
                            }
                            if (SingleQuestion.line_video_question.getVisibility() == View.VISIBLE) {
                               // SingleQuestion.video_question.stopPlayback();
                                SingleQuestion.video_question.setVideoPath("").getPlayer().stop();
                               // SingleQuestion.mediaController.setVisibility(View.GONE);
                             //   SingleQuestion.video_question.setVisibility(View.GONE);
                            }
                            args = new Bundle();
                            args.putStringArrayList("List_rankigOption", ArrayList_Option);
                            args.putStringArrayList("List_rankigId", ArrayList_RankingId);
                            args.putString("Key_candidate_id", candidate_id);
                            args.putString("Key_jobPost_id", job_id);
                            args.putString("Key_jobround_id", round_id);
                            args.putString("Key_jobProcess_time", processtime);
                            args.putString("Key_totalProcess_time", totalprocesstime);
                            args.putString("Key_examStart_dt", startdate);
                            args.putString("key_round_name", stage_nm);
                            args.putString("Key_job_nm", job_name);
                            args.putString("Key_cpy_nm", company_name);
                            args.putString("Key_job_desc", job_desc);
                            args.putString("question_disp", QuestionDisplay);
                            args.putString("Key_interview_acceptedownd_date_display", accepted_dt);
                            args.putString("key_questionrRecord", arrayList.get(arrayList.size() - 1).getQuestionRecord());

                            Ranking ranking = new Ranking();
                            FragmentManager fm = ((Activity) context.getActivity()).getFragmentManager();
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            ranking.setArguments(args);
                            fragmentTransaction.add(R.id.frameLayout, ranking);
                            fragmentTransaction.commit(); // save the ch
//                            initData();
//                            initDragListView();

                        }
                    }//End if(!s.trim
                }//End For Each
                 linear_main_single_que.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            hideKeyboard(v);
                        }
                    }
                });

                return convertView;
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
                                    Toast.makeText(context.getActivity(), "Answer submitted successfully", Toast.LENGTH_LONG).show();

                                    progressDialog.dismiss();
                                } else if (result.equals("\"Fail\"")) {
                                    Toast.makeText(context.getActivity(), "There is an error", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                } else if (result.equals("\"Error\"")) {
                                    Toast.makeText(context.getActivity(), "Try Again and please select proper", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(context.getActivity(), "There is an error", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            })  {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("CandidateID", candidate_id);
                    params.put("RoundID", round_id);
                    params.put("JobId", job_id);
                    params.put("QuestionID", qustion_id);
                    params.put("MsaterQuestionID", master_que_id);
                    params.put("ExamstartId", exam_start_id);
                    params.put("OutputString", final_ans);
                   // Toast.makeText(context.getActivity(), "Parameters"+params, Toast.LENGTH_LONG).show();
                    return params;

                }
            };
            RequestQueue rQueue = Volley.newRequestQueue(context.getActivity());
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
//    public static void hideKeyBoardMethod(final Context con, final View view) {
//        try {
//            view.post(new Runnable() {
//                @Override
//                public void run() {
//                    InputMethodManager imm = (InputMethodManager) con.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)context1.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        mDragListView.setDragListAdapter(new MyAdapter(context1, mDatas,arrayList_id));
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

            view = LayoutInflater.from(context1).inflate(
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

}
