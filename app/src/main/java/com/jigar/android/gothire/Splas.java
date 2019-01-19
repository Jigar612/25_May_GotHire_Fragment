package com.jigar.android.gothire;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public class Splas extends AppCompatActivity {

    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    int Candidate_id;
    //****

    //For CandidateNm
    SharedPreferences sharedpreferences_nm;
    public static final String mypreference_nm = "mypref_nm";
    public static final String CandidateNmKey = "candidateNmKey";
    String Candidate_nm;
    //******
  //  Bundle args = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splas);

        final Window win= getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);



        runtask();



    }
    public void runtask()
    {
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            // runtask();
            Thread timer = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //For CandidateId
                    sharedpreferences_id = getSharedPreferences(mypreference_id,
                            Context.MODE_PRIVATE);
                    if (sharedpreferences_id.contains(CandidateKey)) {
                        Candidate_id = sharedpreferences_id.getInt(CandidateKey, 0);
                    }
                    //*****
                    //For CandidateNm
                    sharedpreferences_nm = getSharedPreferences(mypreference_nm,
                            Context.MODE_PRIVATE);
                    if (sharedpreferences_nm.contains(CandidateNmKey)) {
                        Candidate_nm = sharedpreferences_nm.getString(CandidateNmKey, null);
                    }
                    //****
                    //    String refreshedToken= FirebaseInstanceId.getInstance().getToken();
                    // Log.d("Token",refreshedToken);
                    if (Candidate_id == 0) {
//            Intent i = new Intent(getApplicationContext(), Login.class);
//            startActivity(i);
                        Intent i = new Intent(getApplicationContext(), Login_with.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else {
//            Intent i = new Intent(getApplicationContext(), Login_with.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
                        String Candidate_id_st = String.valueOf(Candidate_id);

                        Intent intent_welcome = new Intent(getApplicationContext(), MainContainer.class);
                        intent_welcome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent_welcome.putExtra("Key_candidate_id", Candidate_id_st);
                        intent_welcome.putExtra("Key_candidate_nm", Candidate_nm);
                        startActivity(intent_welcome);
                    }
                }
            };
            timer.start();
        }else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            // Toast.makeText(LeadListActivity.this, " Please check your internet connection.", Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.no_connection);
            builder.setIcon(R.mipmap.ic_internetconnection);
            builder.setCancelable(false);
            //builder.setMessage("Sorry there was an error getting data from the Internet.\nNetwork Unavailable!");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    runtask();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

}
