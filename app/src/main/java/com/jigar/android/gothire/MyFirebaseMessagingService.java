package com.jigar.android.gothire;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jigar.android.gothire.SetterGetter.RowItemGetAcceptedJobResult;
import com.jigar.android.gothire.SetterGetter.RowItem_ApplyForJob;
import com.jigar.android.gothire.SetterGetter.RowItems_Interviews;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by COMP11 on 11-Nov-17.
 */



public class MyFirebaseMessagingService extends FirebaseMessagingService {//FirebaseMessagingService

    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    int Candidate_id;


    SharedPreferences sharedpreferences_counter;
    public static final String myPref_counter= "mypref_counter";
    public static final String Counter_key = "counter_key";
    int new_notification_counter;

    String jobid = null, candidateid = "0", openscreen = null, RoundId = null, examStartID = "0", body = null;
    int open_id, job_id, Round_id;
  //  public String jobname, compny_nm, job_desc, accepted_on;
    String image_path;
    String JobPostId1;

     String jobnm,cpy_nm,job_des,acep_on;//img_path

    Bundle args;

    public MyFirebaseMessagingService()
    {


    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
     //   super.onMessageReceived(remoteMessage);
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.

       // if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {

        Map<String, String> data = remoteMessage.getData();
        jobid = (data.get("jobid"));
        candidateid =(data.get("candidateid"));
        openscreen = (data.get("Openscreen"));
        RoundId = (data.get("RoundId"));
       // examStartID =(data.get("examStartID"));
        body = (data.get("body"));


       // getAccepted_webservices();

        if (openscreen == null || openscreen == "" || openscreen.isEmpty() || openscreen =="null") {
                open_id = 0;
            } else {
                open_id = Integer.parseInt(openscreen);
            }
             if (jobid == null || jobid == "" || jobid.isEmpty() || jobid =="null") {
                 job_id = 0;
             } else {
                job_id = Integer.parseInt(jobid);
            }
            if (RoundId == null || RoundId == "" || RoundId.isEmpty() || RoundId =="null") {
           // if(!RoundId.equals(null) || !RoundId.equals("") || !RoundId.isEmpty() || RoundId.equals("null")){
                    Round_id = 0;
            } else {
                     Round_id = Integer.parseInt(RoundId);
            }
            Log.d("body", "Notification Message Body: " + body);
            SendNotification(body);

      //  }
    }
    public void SendNotification(String messageBody)
    {
        //For CandidateId
        sharedpreferences_id = getSharedPreferences(mypreference_id,
                Context.MODE_PRIVATE);
        if (sharedpreferences_id.contains(CandidateKey)) {
            Candidate_id = sharedpreferences_id.getInt(CandidateKey, 0);
        }
        ///Currently webservices is not worked..
      // temp_call();

        sharedpreferences_counter = getSharedPreferences(myPref_counter,
                Context.MODE_PRIVATE);


        if (sharedpreferences_counter.contains(Counter_key)) {
            new_notification_counter = sharedpreferences_counter.getInt(Counter_key, 0);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if(Candidate_id != 0)
        {

            if (open_id == 1)//Reattempted stage
            {
                //Interviews class
                Intent intent_jobView = new Intent(this, MainContainer.class);
                intent_jobView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_jobView.putExtra("Key_candidate_id", candidateid);
                intent_jobView.putExtra("Key_open_id", openscreen);


                PendingIntent pIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent_jobView, PendingIntent.FLAG_ONE_SHOT);

                NotificationManager notificationmanager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                String CHANNEL_ID = "my_channel_01";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                    CharSequence name = "my_channel";
                  //  String Description = "Uploading video";
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                  //  mChannel.setDescription(Description);
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mChannel.setShowBadge(false);
                    notificationmanager.createNotificationChannel(mChannel);
                    // notificationManager.notify(notify_id, builder.build());
                }


                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this);
                builder.setSmallIcon(R.drawable.ic_gh_logo_new);
                builder.setChannelId(CHANNEL_ID);
                builder.setContentTitle("GotHire");
                builder.setContentText(messageBody);
                builder.setAutoCancel(true);
                builder.setVibrate(new long[] { 200, 200 ,200,200 });//,300,300
                builder.setSound(defaultSoundUri);
                //builder.setBadgeIconType(new_notification_counter);
//                builder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);
                    new_notification_counter++;

                    SharedPreferences.Editor editor_counter = sharedpreferences_counter.edit();
                    editor_counter.putInt(Counter_key,new_notification_counter);
                    editor_counter.commit();

                //    Home.btn_count.callOnClick();


                //LED
                builder.setLights(Color.RED, 3000, 3000);
                builder.setContentIntent(pIntent);


                if (openscreen == "1")
                {
                    notificationmanager.notify(0, builder.build());
                }
                else
                {
                    notificationmanager.notify(1, builder.build());
                }
            }
//            if (open_id == 2) {
//                Intent intent_viewResult = new Intent(this, ViewResult.class);
//                intent_viewResult.putExtra("Key_candidate_id", candidateid);
//                intent_viewResult.putExtra("Key_jobPost_id", job_id);
//                intent_viewResult.putExtra("Key_job_nm", jobname);
//                intent_viewResult.putExtra("Key_cpy_nm", compny_nm);
//                intent_viewResult.putExtra("Key_jobround_id", Round_id);
//                intent_viewResult.putExtra("Exam_StartID", examStartID);
//                PendingIntent pIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent_viewResult, PendingIntent.FLAG_ONE_SHOT);
//
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this);
//                builder.setSmallIcon(R.drawable.logo_gh_new);
//                builder.setContentTitle("GotHire");
//                builder.setContentText(messageBody);
//                builder.setAutoCancel(true);
//                builder.setVibrate(new long[] { 200, 200 ,200,200 });
//                builder.setSound(defaultSoundUri);
//                //LED
//                builder.setLights(Color.RED, 3000, 3000);
//                builder.setContentIntent(pIntent);
//
//                NotificationManager notificationmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                if (openscreen == "2") {
//                    notificationmanager.notify(1, builder.build());
//                } else {
//                    notificationmanager.notify(0, builder.build());
//                }
//            }
            if (open_id == 7)//Invite for Interview... -> interview InvitationActivity.
            {
                //Open Interview Invitation..
                Intent intent_interview_invitation = new Intent(this, MainContainer.class);
                intent_interview_invitation.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_interview_invitation.putExtra("Key_candidate_id", candidateid);
                intent_interview_invitation.putExtra("Key_open_id", openscreen);
                PendingIntent pIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent_interview_invitation, PendingIntent.FLAG_ONE_SHOT);

                NotificationManager notificationmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                String CHANNEL_ID = "my_channel_01";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                    CharSequence name = "my_channel";
                  //  String Description = "Uploading video";
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                  //  mChannel.setDescription(Description);
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mChannel.setShowBadge(false);
                    notificationmanager.createNotificationChannel(mChannel);
                    // notificationManager.notify(notify_id, builder.build());
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this);
                builder.setSmallIcon(R.drawable.ic_gh_logo_new);
                builder.setChannelId(CHANNEL_ID);
                builder.setContentTitle("GotHire");
                builder.setContentText(messageBody);
                builder.setAutoCancel(true);
                builder.setVibrate(new long[] { 200, 200 ,200,200 });
                builder.setSound(defaultSoundUri);
                new_notification_counter++;
                SharedPreferences.Editor editor_counter = sharedpreferences_counter.edit();
                editor_counter.putInt(Counter_key,new_notification_counter);
                editor_counter.commit();
                //LED
                builder.setLights(Color.RED, 3000, 3000);
                builder.setContentIntent(pIntent);


                if (openscreen == "7")
                {
                    notificationmanager.notify(1, builder.build());
                }
                else
                {
                    notificationmanager.notify(0, builder.build());
                }
            }
            if (open_id == 8)//Invite for job for apply Job.
            {
                Intent intent_Apply_job = new Intent(this, MainContainer.class);
                intent_Apply_job.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_Apply_job.putExtra("Key_candidate_id", candidateid);
                intent_Apply_job.putExtra("Key_open_id", openscreen);
                PendingIntent pIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent_Apply_job, PendingIntent.FLAG_ONE_SHOT);

                NotificationManager notificationmanager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                String CHANNEL_ID = "my_channel_01";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                    CharSequence name = "my_channel";
                    //  String Description = "Uploading video";
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    //  mChannel.setDescription(Description);
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mChannel.setShowBadge(false);
                    notificationmanager.createNotificationChannel(mChannel);
                    // notificationManager.notify(notify_id, builder.build());
                }



                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this);
                builder.setSmallIcon(R.drawable.ic_gh_logo_new);
                builder.setContentTitle("GotHire");
                builder.setChannelId(CHANNEL_ID);
                builder.setContentText(messageBody);
                builder.setAutoCancel(true);
                builder.setVibrate(new long[] { 200, 200 ,200,200 });
                builder.setSound(defaultSoundUri);
                new_notification_counter++;
                SharedPreferences.Editor editor_counter = sharedpreferences_counter.edit();
                editor_counter.putInt(Counter_key,new_notification_counter);
                editor_counter.commit();
                //LED
                builder.setLights(Color.RED, 3000, 3000);
                builder.setContentIntent(pIntent);


                //NotificationManager notificationmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (openscreen == "8")
                {
                    notificationmanager.notify(1, builder.build());
                }
                else
                {
                    notificationmanager.notify(0, builder.build());
                }
            }
            if(open_id==0)//This condition is new if  openScreenId get null then defult this notification fired and go to login page
            {
                Intent intent_login = new Intent(this,Login.class);
                intent_login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent_login, PendingIntent.FLAG_ONE_SHOT);

                NotificationManager notificationmanager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                String CHANNEL_ID = "my_channel_01";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                    CharSequence name = "my_channel";
                    //  String Description = "Uploading video";
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    //  mChannel.setDescription(Description);
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mChannel.setShowBadge(false);
                    notificationmanager.createNotificationChannel(mChannel);
                    // notificationManager.notify(notify_id, builder.build());
                }


                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this);
                builder.setSmallIcon(R.drawable.ic_gh_logo_new);
                builder.setContentTitle("GotHire");
                builder.setChannelId(CHANNEL_ID);
                builder.setContentText(messageBody);
                builder.setAutoCancel(true);
                builder.setVibrate(new long[] { 200, 200 ,200,200 });
                builder.setSound(defaultSoundUri);
                new_notification_counter++;
                SharedPreferences.Editor editor_counter = sharedpreferences_counter.edit();
                editor_counter.putInt(Counter_key,new_notification_counter);
                editor_counter.commit();
                //LED
                builder.setLights(Color.RED, 3000, 3000);
                builder.setContentIntent(pIntent);

              //  NotificationManager notificationmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationmanager.notify(1, builder.build());
            }
        }
        else
        {
            Intent intent_login = new Intent(this,Login.class);
            intent_login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent_login, PendingIntent.FLAG_ONE_SHOT);

            NotificationManager notificationmanager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            String CHANNEL_ID = "my_channel_01";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                CharSequence name = "my_channel";
                //  String Description = "Uploading video";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                //  mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);
                notificationmanager.createNotificationChannel(mChannel);
                // notificationManager.notify(notify_id, builder.build());
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this);
            builder.setSmallIcon(R.drawable.ic_gh_logo_new);
            builder.setContentTitle("GotHire");
            builder.setChannelId(CHANNEL_ID);
            builder.setContentText(messageBody);
            builder.setAutoCancel(true);
            builder.setVibrate(new long[] { 200, 200 ,200,200 });
            builder.setSound(defaultSoundUri);
            new_notification_counter++;
            SharedPreferences.Editor editor_counter = sharedpreferences_counter.edit();
            editor_counter.putInt(Counter_key,new_notification_counter);
            editor_counter.commit();
            //LED
            builder.setLights(Color.RED, 3000, 3000);

            builder.setContentIntent(pIntent);

           // NotificationManager notificationmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (openscreen == "1")
            {
                notificationmanager.notify(0, builder.build());
            }
//            else if (openscreen == "2")
//            {
//                notificationmanager.notify(1, builder.build());
//            }
            else if (openscreen == "7")
            {
                notificationmanager.notify(0, builder.build());
            }
            else if (openscreen == "8")
            {
                notificationmanager.notify(1, builder.build());
            }
        }
//        SharedPreferences sharedpreferences_counter;
//        public static final String myPref_counter= "mypref_counter";
//        public static final String Counter_key = "counter_key";
//        int new_notification_counter=0;


//        SharedPreferences.Editor editor_nm = sharedpreferences_nm.edit();
//        editor_nm.putString(CandidateNmKey,Candidate_nm);
//        editor_nm.commit();
    }

//    public void getAccepted_webservices()
//    {
//        //By default is active then
//        String url_welcome = UrlString.URL + "GetAcceptedJobs/" + candidateid;
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_welcome, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                //Log.d("Result", response.toString());
//                String result = response.toString();
//                try {
//                    JSONArray resultJsonArr = response.getJSONArray("GetAcceptedJobsResult");
//
//                    int count=0;
//
//                    String image_path1,CompanyName,JobTitle,JobPostId,JobDescription,InterviewAcceptDateDisplay;
//                    while (count <resultJsonArr.length())
//                    {
//                       RowItemGetAcceptedJobResult item_getAcceptedJob = new RowItemGetAcceptedJobResult();
//                        JSONObject jObject = resultJsonArr.getJSONObject(count);
//
//
//                        JobPostId=jObject.getString("JobPostId");
//
//                        if (JobPostId.equals(jobid)) {
//
////                            JobTitle = jObject.getString("JobTitle");
////                            CompanyName = jObject.getString("CompanyName");
////                            JobDescription = jObject.getString("JobDescription");
////                            InterviewAcceptDateDisplay = jObject.getString("InterviewAcceptDateDisplay");
////                            image_path1=jObject.getString("ImagePath");
//
////                            compny_nm = CompanyName;
////                            job_desc = JobDescription;
////                            accepted_on = InterviewAcceptDateDisplay;
////                            image_path=image_path1;
//
//                            JobPostId1=JobPostId;
//                            jobnm = jObject.getString("JobTitle").toString();
////                            cpy_nm = jObject.getString("CompanyName").toString();
////                            job_des = jObject.getString("JobDescription").toString();
////                            acep_on = jObject.getString("InterviewAcceptDateDisplay").toString();
////                            image_path=jObject.getString("ImagePath").toString();
//                        }
//                        count++;
//                   }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Toast.makeText(MyFirebaseMessagingService.this, "Please try again", Toast.LENGTH_LONG).show();
//            }
//        }) {
//        };
//        RequestQueue rQueue = Volley.newRequestQueue(this);
//        rQueue.add(jsonObjReq);
//    }
}