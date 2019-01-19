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
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import com.squareup.picasso.Picasso;

import net.skoumal.fragmentback.BackFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import me.toptas.fancyshowcase.FancyShowCaseView;
import tcking.github.com.giraffeplayer2.DefaultPlayerListener;
import tcking.github.com.giraffeplayer2.GiraffePlayer;
import tcking.github.com.giraffeplayer2.VideoInfo;
import tcking.github.com.giraffeplayer2.VideoView;

public class JobView extends Fragment  {//implements BackFragment
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    public String Candidate_id;

    String job_nm, cpy_nm, job_desc,salary,city;
    String jobPost_id,job_type,getPath;
    String skill_nm,lanjuage_knwon;
    String certificate,education;
    ImageView img_cpy_logo;
    TextView tv_job_nm,tv_cpy_nm,tv_country_nm,tv_post_before_day,tv_job_type,tv_salary,tv_job_desc;
    TextView tv_education;//tv_education_job_view
    TextView tv_languages;//tv_language_job_view
    TextView tv_skills;//tv_skill_job_view
    TextView tv_certificate;//tv_certificate_job_view
  //  VideoView videoView_jobView;
    static VideoView videoView_jobView;
    Button btn_apply_now,btn_save;
    Button btn_back;
    String video_Url;

    LinearLayout linearLayout_video;


    ProgressBar pd_video;
    ProgressDialog progressDialog;
    View view;
    Bundle args;
    FancyShowCaseView fanyview_jobview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_job_view, container, false);

        MainContainer.TAG="JobView";

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        MainContainer.bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setCheckable(false).setChecked(false);
        //MainContainer.bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(false);

        Bundle get_data = getArguments();
        Candidate_id = get_data.getString("Key_candidate_id");
        jobPost_id = get_data.getString("Key_jobPost_id");
        job_nm = get_data.getString("Key_job_nm");
        cpy_nm = get_data.getString("Key_cpy_nm");
        job_desc = get_data.getString("Key_job_desc");
        salary = get_data.getString("Key_salary");
        city = get_data.getString("Key_city");
        job_type=get_data.getString("Key_job_type");
        getPath= get_data.getString("Key_image_path");

        skill_nm= get_data.getString("Key_skillname");
        lanjuage_knwon=get_data.getString("Key_language");

        certificate= get_data.getString("Key_certificate");
        education=get_data.getString("Key_education");

        video_Url =get_data.getString("Key_videoDesc");


       // home= new Home();
        //btn_viewRound =(Button)findViewById(R.id.btn_jobv_round);
        img_cpy_logo=(ImageView)view.findViewById(R.id.img_cpy_logo_job_view);
        tv_job_nm = (TextView) view.findViewById(R.id.tv_job_nm_home);
        tv_cpy_nm =(TextView)view.findViewById(R.id.tv_cpy_nm_job_view);
        tv_job_desc = (TextView) view.findViewById(R.id.tv_job_desc_job_view);

        //currently not received from webservices.
        tv_country_nm= (TextView) view.findViewById(R.id.tv_country_job_view);
        tv_post_before_day= (TextView) view.findViewById(R.id.tv_posted_before_time_job_view);
        tv_job_type = (TextView) view.findViewById(R.id.tv_job_type_job_view);
        tv_salary= (TextView) view.findViewById(R.id.tv_salary_job_view);

        tv_education= (TextView) view.findViewById(R.id.tv_education_job_view);//tv_education_job_view
        tv_languages= (TextView)view.findViewById(R.id.tv_language_job_view);//tv_language_job_view
        tv_skills= (TextView) view.findViewById(R.id.tv_skill_job_view);//tv_skill_job_view
        tv_certificate= (TextView) view.findViewById(R.id.tv_certificate_job_view);//tv_certificate_job_view
        TextView tv_disp_jobView= (TextView) view.findViewById(R.id.tv_job_desc_disp_job_view);//tv_certificate_job_view
        TextView tv_disp_Requirement= (TextView) view.findViewById(R.id.tv_requirement_disp_job_view);//tv_certificate_job_view
        TextView tv_salary_disp_job_view= (TextView) view.findViewById(R.id.tv_salary_disp_job_view);//tv_certificate_job_view
        TextView tv_job_type_disp_job_view= (TextView) view.findViewById(R.id.tv_job_type_disp_job_view);//tv_certificate_job_view

        TextView tv_education_disp_job_view= (TextView) view.findViewById(R.id.tv_education_disp_job_view);//tv_certificate_job_view
        TextView tv_language_disp_job_view= (TextView) view.findViewById(R.id.tv_language_disp_job_view);//tv_certificate_job_view
        TextView tv_skill_disp_job_view= (TextView) view.findViewById(R.id.tv_skill_disp_job_view);//tv_certificate_job_view
        TextView tv_certificate_disp_job_view= (TextView) view.findViewById(R.id.tv_certificate_disp_job_view);//tv_certificate_job_view


         linearLayout_video= (LinearLayout) view.findViewById(R.id.linear_video);//tv_certificate_job_view



        videoView_jobView = (VideoView)view.findViewById(R.id.videoview_job_view);


        pd_video=(ProgressBar)view.findViewById(R.id.pd_video);

        btn_apply_now= (Button) view.findViewById(R.id.btn_apply_now_job_view);//tv_certificate_job_view
        btn_save= (Button) view.findViewById(R.id.btn_save_job_view);//tv_certificate_job_view
        btn_back = (Button)view.findViewById(R.id.btn_back_jobView);


        Typeface roboto_Med = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Medium.ttf");
        Typeface roboto_Reg = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Light.ttf");
        tv_job_nm.setTypeface(roboto_Med);
        tv_disp_jobView.setTypeface(roboto_Med);
        tv_disp_Requirement.setTypeface(roboto_Med);
        tv_salary_disp_job_view.setTypeface(roboto_Med);
        tv_job_type_disp_job_view.setTypeface(roboto_Med);

        tv_education_disp_job_view.setTypeface(roboto_Med);
        tv_language_disp_job_view.setTypeface(roboto_Med);
        tv_skill_disp_job_view.setTypeface(roboto_Med);
        tv_certificate_disp_job_view.setTypeface(roboto_Med);


        tv_post_before_day.setTypeface(roboto_Light);
        tv_job_desc.setTypeface(roboto_Light);
        tv_job_type.setTypeface(roboto_Light);
        tv_salary.setTypeface(roboto_Light);
        tv_country_nm.setTypeface(roboto_Light);
      //  tv_country_nm.setTypeface(roboto_Light);
      //  tv_job_type.setTypeface(roboto_Light);

        tv_education.setTypeface(roboto_Light);
        tv_languages.setTypeface(roboto_Light);
        tv_skills.setTypeface(roboto_Light);
        tv_certificate.setTypeface(roboto_Light);
        btn_apply_now.setTypeface(roboto_Light);
        btn_save.setTypeface(roboto_Light);
        btn_back.setTypeface(roboto_Light);


        runTask();
        return view;
    }

//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        progressDialog.dismiss();
//    }
    public void runTask()
    {
        ConnectivityManager connec =
                (ConnectivityManager) getActivity().getSystemService(getActivity().getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);

            job_nm = job_nm.toUpperCase();// Convert to uppercase 3/oct
            tv_job_nm.setText(job_nm);
            tv_cpy_nm.setText(cpy_nm);
            tv_job_desc.setText(Html.fromHtml(job_desc));

            tv_job_type.setText(job_type);
            tv_country_nm.setText(city);
            tv_salary.setText(salary);

            tv_certificate.setText(certificate);
            tv_education.setText(education);

         //   skill_nm = skill_nm.replace(",", ", ");
            tv_skills.setText(skill_nm);
            ///lanjuage_knwon = lanjuage_knwon.replace(",", ", ");
            tv_languages.setText(lanjuage_knwon);

                String path=LiveLink.LinkLive2+getPath;
                Picasso.with(getActivity())
                        .load(path)
                        .error(R.drawable.ic_briefcase)//if no image on server then show..
                        .placeholder(R.drawable.ic_briefcase)
                        .into(img_cpy_logo);

            //video_Url = LiveLink.LinkLive2+"/TestimonialsImageVedio/SampleVideo_1280x720_2mb.mp4";
            // video_Url = "/TestimonialsImageVedio/SampleVideo_1280x720_2mb.mp4";
            if(video_Url== null || video_Url.isEmpty() || video_Url.equals("null") || video_Url.equals(""))
            {
                linearLayout_video.setVisibility(View.GONE);
            }
            {
                String video_Url_link = LiveLink.LinkLive2+video_Url;
                videoView_jobView.setVideoPath(video_Url_link).getPlayer();//.start();
            }



//            fanyview_jobview = new FancyShowCaseView.Builder(getActivity())
//                    .focusOn(btn_apply_now)
//                    .titleStyle(R.style.focused_text, Gravity.BOTTOM|Gravity.CENTER)
//                    .title("Click here for applied this job.")
//                    //.focusCircleRadiusFactor(0.5)
//                    .enableTouchOnFocusedView(true)
//                    .build();
//            fanyview_jobview.show();

//            videoView_jobView.setPlayerListener(new DefaultPlayerListener() {
//                @Override
//                public void onDisplayModelChange(int oldModel, int newModel) {
//                    if (newModel == GiraffePlayer.DISPLAY_FULL_WINDOW) {
//                        //do something
//                        VideoInfo videoInfo = new VideoInfo(Uri.parse(video_Url))
//                      //  .setAspectRatio(1) //aspectRatio
//                        .setShowTopBar(true)//show mediacontroller top bar
//                        .setPortraitWhenFullScreen(true);//portrait when full screen
//                        GiraffePlayer.play(getActivity().getApplicationContext(), videoInfo);
//                    }
//                    else
//                    {
//                         videoView_jobView.setVideoPath(video_Url).getPlayer().start();
//                    }
//                }
//            });
       //     videoView.setVideoPath(videoUri).getPlayer().start();
//            pd_video.setVisibility(View.VISIBLE);
//            Uri uri = Uri.parse(video_Url);
//            videoView_jobView.setVideoURI(uri);
//            MediaController mediaController = new MediaController(getActivity());
//            mediaController.setAnchorView(videoView_jobView);
//            videoView_jobView.setMediaController(mediaController);
//
//            videoView_jobView.start();
//
//            //For used video buffring
//            videoView_jobView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                // Close the progress bar and play the video
//                public void onPrepared(MediaPlayer mp) {
//                    pd_video.setVisibility(View.INVISIBLE);
//                    videoView_jobView.start();
//                }
//            });
//            videoView_jobView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//                public void onCompletion(MediaPlayer mp) {
//                    if (pd_video.isShown()) {
//                        pd_video.setVisibility(View.INVISIBLE);
//                    }
//                    //   finish();
//                }
//            });
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//
                    args = new Bundle();
                    args.putString("Key_candidate_id",Candidate_id);
                    loadFragment(new Home());
                }
            });
            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  fanyview_jobview.removeView();
                    progressDialog.show();
                    String webservice_url_saveJob = UrlString.URL + "InsertSaveJob/";
                    String url_applyForJob = webservice_url_saveJob.toString() + Candidate_id+"/"+jobPost_id ;
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_applyForJob, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Result", response.toString());
                            String result = response.toString();

                            JSONObject jObject = null;
                            try {
                                jObject = new JSONObject(result);
                                String result_response = jObject.getString("InsertSaveJobResult");

                                //if (result.equals("\"Candidate Reject Interview\""))
                                if (result_response.equals("sucess"))
                                {
                                    args = new Bundle();
                                    args.putString("Key_candidate_id",Candidate_id);
                                    loadFragment(new Track());
                                    Toast.makeText(getActivity(), "Your job is saved", Toast.LENGTH_LONG).show();
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
            });
            btn_apply_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                                  //Here call a webservices...
                   // fanyview_jobview.removeView();


//                    fanyview_jobview = new FancyShowCaseView.Builder(getActivity())
//                            .focusOn(btn_save)
//                            .titleStyle(R.style.focused_text, Gravity.BOTTOM|Gravity.CENTER)
//                            .title("Click here for save this job.")
//                            //.focusCircleRadiusFactor(0.5)
//                            .enableTouchOnFocusedView(true)
//                            .build();
//                    fanyview_jobview.show();


                    progressDialog.show();

                    String webservice_url = UrlString.URL + "ApplyViewJobInvitation/";
                    String url_applyForJob = webservice_url.toString() + Candidate_id+"/"+jobPost_id ;
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_applyForJob, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Result", response.toString());
                            String result = response.toString();

                            JSONObject jObject = null;
                            try {
                                jObject = new JSONObject(result);
                                String result_response = jObject.getString("ApplyViewJobInvitationResult");

                                //if (result.equals("\"Candidate Reject Interview\""))
                                if (result_response.equals("Success"))
                                {
                                    args = new Bundle();
                                    args.putString("Key_candidate_id",Candidate_id);
                                    loadFragment(new Track());//changes 27/7/19 by arun sir before change
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
            });
            //Write Here...
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
        //    hideSoftKeyboard(this);
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
//    @Override
//    public boolean onBackPressed() {
//        return false;
//    }
//
//    @Override
//    public int getBackPriority() {
//        return NORMAL_BACK_PRIORITY;
//    }
//    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager =
//                (InputMethodManager) activity.getSystemService(
//                        Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(
//                activity.getCurrentFocus().getWindowToken(), 0);
//    }


}

