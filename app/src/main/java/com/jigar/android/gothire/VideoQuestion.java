package com.jigar.android.gothire;

import android.*;
import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
//import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jigar.android.gothire.Adapter.AdapterQuestionRound;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class VideoQuestion extends Fragment implements SurfaceHolder.Callback {

    int get_day_min, get_hours_min, get_minute_min, get_second_min;
    int get_day_max, get_hours_max, get_minute_max, get_second_max;
    int counter = 0;
    int min_time_video_counter = 0;
    int response_connection_code;
    //List<Thread> threads;

    String add_pipe_url = "https://s3b.addpipe.com/upload";

    int wait = 0;
    int second_min_conv, second_max_conv, flag_second_max;
    String candidate_id, res_id, totalprocesstime, startdate, video_validation, question_display, Jobquesid, quest_masterid, exam_startid;
    String roundid, jobid, processtime;
    String filename_gui;
    String chk_over = "0";

    String split_min_second="0";
    String split_max_second="0";
    private android.hardware.Camera camera=null;

    VideoView videoview;
    //SurfaceView videoview;
     //   SurfaceHolder surfaceHolder;

    ImageView image_counter;
    MediaRecorder recorder  = new MediaRecorder();
    //DisplayMetrics dm;
    String path;
    String url_video;
    int video_que_time_convert;
    Button button_stop, button_startRecord;
   // TextView tv_display_start_video;
    TextView tv_prepareTime, tv_prepare_record;// tv_question_disp
    TextView tv_question_disp,tv_question_pos;
    ImageView img_video_back;
    Bundle args;
  //  ProgressBar mProgressBar;//hide 10-Apr-2018
  //  ProgressBar mProgressBar_maxtime; //hide 10-Apr-2018
    int setProgressIncrment;
    static CountDownTimer PreparedTimer;
    static CountDownTimer countDownQuestionTimer;
    static boolean isTimerRunning;
    static boolean isPrepareTimerRunning;
    CountDownTimer WaitThreeSecond;

    int progress = 0;
    int progress_prepared=0;
    Object lock_maxtime = new Object();

    long convtInMillisec;


    int second_convert;
    String round_defalut_video_time;
    String video_que_time;
    String company_name, job_desc, acceptedon, jobname, roundname;
    String final_ans;
    int notify_id = 101;
   // ProgressDialog progressDialog;

    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    String selected_icon_state;
    View view;

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        countDownQuestionTimer.cancel();
//        PreparedTimer.cancel();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_video_question, container, false);
        MainContainer.TAG="VideoQuestion";
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        button_startRecord = (Button)view.findViewById(R.id.Record);
        button_stop = (Button)view.findViewById(R.id.Stop);
        tv_prepareTime = (TextView)view.findViewById(R.id.txt_prepare_time);
        tv_prepare_record = (TextView)view.findViewById(R.id.txt_prepare_record);
        //dipslay question
        tv_question_disp = (TextView)view.findViewById(R.id.tv_que_videoQuestion);
        tv_question_pos = (TextView)view.findViewById(R.id.tv_quesround_pos_video);
        videoview = (VideoView)view.findViewById(R.id.SampleVideo);
        image_counter = (ImageView)view.findViewById(R.id.image_countdown);
      //  tv_display_start_video=(TextView)findViewById(R.id.txt_disp_st_recording);
        img_video_back = (ImageView)view.findViewById(R.id.image_video_disp);

        Bundle get_data = getArguments();
        candidate_id = get_data.getString("Key_candidate_id");

        Typeface roboto_Reg = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Light.ttf");
        tv_prepare_record.setTypeface(roboto_Reg);
        tv_prepareTime.setTypeface(roboto_Reg);

        tv_question_disp.setTypeface(roboto_Light);



//                progressDialog = new ProgressDialog(this.getActivity());
//                 progressDialog.setIndeterminate(true);
//                 progressDialog.setMessage("Please wait...");
//                 progressDialog.setCancelable(false);

        runTask();
        return view;
    }
    public void runTask()
    {
        UUID uuid = UUID.randomUUID();
        filename_gui = uuid.toString().replace("-", "") + ".mp4";
        path= Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/" + filename_gui;

        ConnectivityManager connec =
                (ConnectivityManager) getActivity().getSystemService(getActivity().getBaseContext().CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            Bundle get_data = getArguments();
            //Candidate_id = get_data.getString("Key_candidate_id");

            candidate_id = get_data.getString("CandidateId");
            roundid = get_data.getString("RoundID");
            jobid =get_data.getString("JobId");
            Jobquesid = get_data.getString("JobQuesID");
            quest_masterid = get_data.getString("Question_MasterID");
            exam_startid =get_data.getString("Exam_StartID");
            res_id = get_data.getString("response_id");
            processtime = get_data.getString("process_time");
            totalprocesstime = get_data.getString("total_process");
            startdate = get_data.getString("startdate");
            //****for video custom validation get from single_adapter
            video_validation = get_data.getString("video_validation");
            question_display =get_data.getString("question_disp");
            acceptedon = get_data.getString("Key_interview_accepted_on");
            jobname = get_data.getString("Key_job_nm");
            roundname = get_data.getString("key_round_name");
            job_desc = get_data.getString("Key_job_desc");
            company_name = get_data.getString("Key_cpy_nm");
            round_defalut_video_time =get_data.getString("round_defalut_time");
            video_que_time =get_data.getString("video_question_time");
            //*****1_Oct_18
            String pos= get_data.getString("question_pos");
            //*********

          //  int pos = ++SingleQuestion.static_ques_pos;
            tv_question_pos.setText(pos+". ");
            tv_question_disp.setText(question_display);

            if(!video_validation.equals("0"))
            {
                 String[] split_second = video_validation.split("-");
                 split_min_second = split_second[0].trim();
                 split_max_second = split_second[1].trim();
            }
            //maximum time..
            if (split_max_second == "" || split_max_second == "0")
            {
                if (second_convert == 0)
                {
                    second_max_conv = 1800;
                }
                else
                {
                    second_max_conv = second_convert - 10;
                }
            }
            else
            {
                second_max_conv = Integer.parseInt(split_max_second.toString());
            }
            //minimum time..
            if (split_min_second == "" || split_min_second == "0")
            {
                second_min_conv = 10;
            }
            else if (second_min_conv > second_max_conv)
            {
                second_min_conv = 10;
            }
            else
            {
                second_min_conv = Integer.parseInt(split_min_second.toString());
            }

            long cnvt_sec=(second_max_conv);
            setProgressIncrment =second_max_conv;
            convtInMillisec= TimeUnit.SECONDS.toMillis(cnvt_sec);

            if (android.hardware.Camera.getNumberOfCameras() < 2)
            {
                Toast.makeText(getActivity().getApplicationContext(), "Front camera is missing", Toast.LENGTH_SHORT).show();
                return;
            }
            PrepareTimeVideo();

            button_startRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreparedTimer.cancel();
                   // WaitThreeSeconds();
                    //start_Recording();
                    RunUpdateLoop_second();
                    start_Recording();
                }
            });
            button_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countDownQuestionTimer.cancel();
                    submit_video_question();
                }
            });
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
    public void PrepareTimeVideo()
    {
         PreparedTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;

                tv_prepareTime.setText(String.format("%02d:%02d",TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
                isPrepareTimerRunning=true;
            }
            public void onFinish() {
              //  WaitThreeSeconds(); //When thread 30 minutes os over the star countdown
                PreparedTimer.cancel();
                RunUpdateLoop_second();
                isPrepareTimerRunning=false;
                start_Recording();
            }

        }.start();
    }
//    public void WaitThreeSeconds()
//    {
//         WaitThreeSecond = new CountDownTimer(3000, 1000) {
//            public void onTick(long millisUntilFinished) {
//                long millis = millisUntilFinished;
//                wait++;
//                if (wait == 1) {
//                    image_counter.setImageResource(R.drawable.ic_3);
//                }
//                if (wait == 2) {
//                    image_counter.setImageResource(R.drawable.ic_2);
//                }
//                if (wait == 3) {
//                    image_counter.setImageResource(R.drawable.ic_1);
//                }
//            }
//            public void onFinish() {
//                image_counter.setVisibility(View.GONE);
//                PreparedTimer.cancel();
//                WaitThreeSecond.cancel();
//                RunUpdateLoop_second();
//                start_Recording();
//            }
//        }.start();
//    }
    public void RunUpdateLoop_second()
    {
         countDownQuestionTimer = new CountDownTimer(convtInMillisec, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                tv_prepareTime.setText(String.format("%02d:%02d:%02d",TimeUnit.MILLISECONDS.toHours(millis),TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
                isTimerRunning=true;
              //  mProgressBar_maxtime.incrementProgressBy(1);//hide 10-Apr-2018


            }
            public void onFinish() {
                //Here Auto submit Video Question answer.
                //SubmitVideo Question Call a function
                Toast.makeText(getActivity(), "Sorry, qustion time is over.", Toast.LENGTH_LONG).show();
                submit_video_question();
                isTimerRunning=false;
                // SingleQuestion.btn_skip.callOnClick();

            }
        }.start();
      //  Toast.makeText(getApplicationContext(), "Call Function.", Toast.LENGTH_LONG).show();
    }

    public void start_Recording()
    {
        try{
               // mProgressBar_maxtime.setVisibility(View.VISIBLE);//hide 10-Apr-2018
                //mProgressBar.setVisibility(View.GONE);//hide 10-Apr-2018
                tv_prepare_record.setText("TIME REMAINING");
                tv_prepare_record.setTextColor(Color.parseColor("#ff0000"));
                button_startRecord.setVisibility(View.GONE);
              //  tv_display_start_video.setVisibility(View.GONE);
                //videoview.setBackgroundResource(0);
                img_video_back.setVisibility(View.GONE);
                button_stop.setVisibility(View.VISIBLE);



                camera = Camera.open(1);
                Camera.Parameters p = camera.getParameters();

                p.set("camera-id", 1);

            camera.setParameters(p);
            camera.setDisplayOrientation(90);
            camera.unlock();
            //videoview.stopPlayback();

            recorder.setCamera(camera);
            recorder.setOrientationHint(270);

            //***************
            recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
            profile.videoFrameWidth = 640;
            profile.videoFrameHeight = 480;
            recorder.setProfile(profile);

//            DisplayMetrics dm=new DisplayMetrics();
//            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//            int height=dm.heightPixels;
//            int width=dm.widthPixels;
//
//            videoview.setMinimumHeight(height);
//            videoview.setMinimumWidth(width);
//            int left = videoview.getLeft();
//            int top = videoview.getTop();
//            int right = left + (width);
//            int bottom = top + (height);
//            videoview.layout(left, top, right, bottom);
           // recorder.setVideoFrameRate(30);


            //***************

            recorder.setPreviewDisplay(videoview.getHolder().getSurface());

//            recorder.setVideoFrameRate(15);
            int max_dure = (int)second_max_conv * 1000;
           // recorder.setMaxDuration(max_dure);
            //recorder.setOutputFile("/InternalStorage/DCIM/Camera/videocapture_example.mp4");
            String path1 = Environment.getExternalStorageDirectory().toString()+"/DCIM/Camera/"+filename_gui;
            recorder.setOutputFile(path1);

            recorder.prepare();
            recorder.start();


        }  catch(RuntimeException re) {
            re.printStackTrace();
       // }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
    public void submit_video_question()
    {
               button_stop.setVisibility(View.GONE);

                recorder.stop();
                recorder.release();
                camera.stopPreview();
                camera.release();
                //videoview.stopPlayback();//new

        url_video = "/video/"+filename_gui;



//        try {
//
//            final NotificationManager notificationManager =
//                    (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
//            String CHANNEL_ID = "my_channel_01";
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//
//                CharSequence name = "my_channel";
//                String Description = "Uploading video";
//                int importance = NotificationManager.IMPORTANCE_HIGH;
//                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
//                mChannel.setDescription(Description);
//                mChannel.enableLights(true);
//                mChannel.setLightColor(Color.RED);
//                mChannel.enableVibration(true);
//                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//                mChannel.setShowBadge(false);
//                notificationManager.createNotificationChannel(mChannel);
//               // notificationManager.notify(notify_id, builder.build());
//            }
//            final NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
//            builder.setContentTitle("Uploading video");
//            builder.setContentText("Uploading in progress...");
//            builder.setSmallIcon(R.drawable.ic_gh_logo_new);
//            builder.setChannelId(CHANNEL_ID);
//            notify_id++;
//
//                            final String user = "FTP_GH";
//                            final String pass = "Test@123";
//                            final File directory = new File(path);
//                          //   threads = new ArrayList<Thread>();
//                            Thread ftpThread=  new Thread(new Runnable() {
//                            public void run()
//                            {
//                              try
//                                {
//                                    StrictMode.ThreadPolicy policy1 = new StrictMode.ThreadPolicy.Builder()
//                                            .permitAll()
//                                            .build();
//                                    StrictMode.setThreadPolicy(policy1);
//    //                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//    //                                        .detectAll()
//    //                                        .penaltyLog()
//    //                                        .penaltyDeath()
//    //                                        .build();
//    //                                StrictMode.setThreadPolicy(policy);
//                                    FTPClient ftp = new FTPClient();
//                                   // ftp.connect(InetAddress.getByName("34.214.133.27")); //change here
//                                    ftp.connect(InetAddress.getByName("35.162.89.140"));
//                                    ftp.login(user,pass);
//                                    ftp.setFileType(FTP.BINARY_FILE_TYPE);
//                                    //ftp.changeWorkingDirectory("/Gothire/video/"); //change here
//                                    ftp.changeWorkingDirectory("/video/");
//                                    ftp.enterLocalPassiveMode();
//                                    int reply = ftp.getReplyCode();
//                                    System.out.println("Received Reply from FTP Connection:" + reply);
//                                    if (FTPReply.isPositiveCompletion(reply)) {
//                                        System.out.println("Connected Success");
//                                    }
//                                  //  threadMsg(reply);
//                                    FileInputStream in = new FileInputStream(directory);
//                                    builder.setProgress(0, 0, true);
//                                    notificationManager.notify(notify_id, builder.build());
//
//                                    boolean result = ftp.storeFile(filename_gui,in);
//
//                                    ftp.logout();
//                                    ftp.disconnect();
//
//                                        builder.setContentText("Upload completed");
//                                        builder.setProgress(0, 0, false);
//                                        notificationManager.notify(notify_id, builder.build());
//                                        builder.setAutoCancel(true);
//
//                                   // threadMsg(SetServerString);
//                                    //************
//                                   // Toast.makeText(getActivity(), "your video is uploading", Toast.LENGTH_LONG).show();
//
//                                } catch (SocketException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                } catch (IOException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
//
//                            }
////                     private void threadMsg(int msg) {
////
////                         if (msg!=(0)) {
////                             Message msgObj = handler.obtainMessage();
////                             Bundle b = new Bundle();
////                             b.putInt("connection_code", msg);
////                             msgObj.setData(b);
////                             handler.sendMessage(msgObj);
////                         }
////                     }
////                     private final Handler handler = new Handler() {
////
////                         public void handleMessage(Message msg) {
////
////                             //String aResponse = msg.getData().getString("connection_code");
////                           //  MainContainer parent=(MainContainer) getActivity();
////                             response_connection_code = msg.getData().getInt("connection_code");
////
////                         }
////                     };
//
//              });
//                ftpThread.start();
//
////            builder.setContentText("Upload completed");
////            builder.setProgress(0, 0, false);
////            notificationManager.notify(notify_id, builder.build());
////            builder.setAutoCancel(true);
//
//            //     threads.add(ftpThread);
//            //  ftpThread.start();
////            for (int i = 0; i < threads.size(); i++)
////                threads.get(i).join();
//
//
//         } catch (Exception e) {
//            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
         uploadVideo(path);
//        }
//        final_ans ="MultipleOption$" + ",RadioOption$" + ",Dropdown$" + ",DataDescription$" + ",FromDate$" + ",ToDate$" + ",FileData$" + ",VideoData$" + url_video +",Rating$"+ ",Ranking$";
//        load_submit_question();


    }
    public void load_submit_question()
    {
        try {
            //String submit_single_que="http://34.214.133.27:83/Default/SubmitSinelQuestion";
            String submit_single_que=" http://35.162.89.140:83/Default/SubmitSinelQuestion";
            String url_singleQuestion= submit_single_que.toString();// + candidate_id1 +"/"+jobRound_id +"/"+jobPost_id;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_singleQuestion,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Result", response.toString());
                            String result = response.toString();
                            try {
                                if (result.equals("\"Success\""))
                                {
                                        countDownQuestionTimer.cancel();
                                   //   getActivity().getFragmentManager().popBackStack();
                                        getActivity().getFragmentManager().beginTransaction().remove(VideoQuestion.this).commit();
                                        SingleQuestion.btn_skip.callOnClick();
                                        Toast.makeText(getActivity(), "Answer submitted successfully", Toast.LENGTH_LONG).show();
                                        AdapterQuestionRound.flag_uploding_video = 1;

                                       //  SingleQuestion.mediaController.setVisibility(View.VISIBLE);
                                         SingleQuestion.video_question.setVisibility(View.VISIBLE);
                               //     progressDialog.dismiss();
                                }
                                else  if (result.equals("\"Fail\"") )
                                {
                                    Toast.makeText(getActivity(), "There is an error", Toast.LENGTH_LONG).show();
                                 //   progressDialog.dismiss();
                                }
                                else if(result.equals("\"Error\"") )
                                {
                                    Toast.makeText(getActivity(),"Try Again and please select proper", Toast.LENGTH_LONG).show();
                                  //  progressDialog.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"Somthing wrong", Toast.LENGTH_LONG).show();
                                }
                               //   progressDialog.dismiss();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    //Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
                   //  progressDialog.dismiss();
                    countDownQuestionTimer.cancel();
                    getActivity().getFragmentManager().beginTransaction().remove(VideoQuestion.this).commit();
                    SingleQuestion.btn_skip.callOnClick();
                    AdapterQuestionRound.flag_uploding_video = 1;
                   // SingleQuestion.mediaController.setVisibility(View.VISIBLE);
                    SingleQuestion.video_question.setVisibility(View.VISIBLE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("CandidateID",candidate_id);
                    params.put("RoundID",roundid);
                    params.put("JobId",jobid);
                    params.put("QuestionID", Jobquesid);
                    params.put("MsaterQuestionID", quest_masterid);
                    params.put("ExamstartId", exam_startid);
                    params.put("OutputString",final_ans);

                    return params;
                }
            };
            RequestQueue rQueue = Volley.newRequestQueue(getActivity());
            rQueue.add(stringRequest);
            //New 28/Aug/2018..
        } catch (Exception e2) {
            e2.printStackTrace();
            //progressDialog.dismiss();
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void uploadVideo(String VideoPath)
    {
        try
        {
            StrictMode.ThreadPolicy policy1 = new StrictMode.ThreadPolicy.Builder()
                    .permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy1);

            File file = new File(VideoPath);
            HttpClient client = new DefaultHttpClient();

            HttpPost post = new HttpPost(add_pipe_url);

            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntity.addPart("FileInput", new FileBody(file));
            multipartEntity.addPart("accountHash", new StringBody("08da5daa79e634fe9d7d7b45577ff706"));
            multipartEntity.addPart("payload", new StringBody("Custome Ui"));
            multipartEntity.addPart("environmentId", new StringBody("1"));
            multipartEntity.addPart("httpReferer", new StringBody("1539254262160.mp4"));
            multipartEntity.addPart("mrt", new StringBody("1000"));
            multipartEntity.addPart("audioOnly", new StringBody("0"));
            post.setEntity(multipartEntity);

            client.execute(post, new VideoQuestion.PhotoUploadResponseHandler());

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public class PhotoUploadResponseHandler implements ResponseHandler<Object> {

        @Override
        public Object handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {

            HttpEntity r_entity = response.getEntity();
            String responseString = EntityUtils.toString(r_entity);
            Log.d("UPLOAD", responseString);
            //Toast.makeText(getActivity(), "Sucess"+responseString, Toast.LENGTH_SHORT).show();

            String return_file=null;
            JSONObject json_getcart_data = null;
            try {
                json_getcart_data = new JSONObject(responseString);

                String value_f_key=json_getcart_data.getString("f");
                Toast.makeText(getActivity(), "Sucess"+responseString, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }



            final_ans ="MultipleOption$" + ",RadioOption$" + ",Dropdown$" + ",DataDescription$" + ",FromDate$" + ",ToDate$" + ",FileData$" + ",VideoData$" + return_file +",Rating$"+ ",Ranking$";
            load_submit_question();
            return null;
        }

    }

}
