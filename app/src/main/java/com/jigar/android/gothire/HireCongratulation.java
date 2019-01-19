package com.jigar.android.gothire;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class HireCongratulation extends AppCompatActivity {
    //For candidateId
    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    String Candidate_id;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_congratulation);


        final Window win= getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);//findid from xml custome bottomNavigationView
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);//make class for stop animatiom shift mode.

        Intent get_intent = getIntent();
        Candidate_id =get_intent.getStringExtra("Key_candidate_id");   //get from finalStageInterview

        TextView tv_congratulate_msg1_hire = (TextView)findViewById(R.id.tv_congratulate_msg1_hire);//tv_empty_list_home
        TextView tv_congratulate_msg2_hire = (TextView)findViewById(R.id.tv_congratulate_msg2_hire);//tv_empty_list_home

        Typeface roboto_Reg = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Light.ttf");
        tv_congratulate_msg1_hire.setTypeface(roboto_Reg);
        tv_congratulate_msg2_hire.setTypeface(roboto_Reg);


        bottomMenu();

        runTask();
    }
    public void runTask()
    {
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED)
        {


            //load_webservices_offertNoDocument();

        }
        else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            // Toast.makeText(LeadListActivity.this, " Please check your internet connection.", Toast.LENGTH_LONG).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    public void bottomMenu()
    {
        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setCheckable(false).setChecked(false);
        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(true);//Checked true for change icon background color.

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottom_nav_home:
                                Intent intent_home = new Intent(getApplicationContext(),Home.class);
                                intent_home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent_home.putExtra("Key_candidate_id", Candidate_id);
                                startActivity(intent_home);
                                break;
                            case R.id.bottom_nav_track:
                                Intent intent_track = new Intent(getApplicationContext(),Track.class);
                                intent_track.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent_track.putExtra("Key_candidate_id", Candidate_id);
                                // intent_track.putExtra("Key_candidate_nm", Candidate_nm);
                                intent_track.putExtra("Key_track", "selectTrack");//for change selected icon color
                                startActivity(intent_track);
                                break;
                            case R.id.bottom_nav_interviews:
                                // startActivity(new Intent(Home.this, Home.class));
                                Intent intent = new Intent(getApplicationContext(),Interviews.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("Key_candidate_id", Candidate_id);
                                //    intent.putExtra("Key_jobPost_id", Jo);
                                intent.putExtra("Key_interview", "selectInterview");//for change selected icon color
                                startActivity(intent);
                                break;
                            case R.id.bottom_nav_account:
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HireCongratulation.this);
                                // Setting Dialog Title
                                alertDialog.setTitle("Confirm Logout");
                                alertDialog.setCancelable(false);
                                // Setting Dialog Message
                                alertDialog.setMessage("Are you sure you want logout ?");

                                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int which) {
                                        sharedpreferences_id = getSharedPreferences(mypreference_id,
                                                Context.MODE_PRIVATE);

                                        SharedPreferences.Editor editor_id = sharedpreferences_id.edit();
                                        editor_id.clear();
                                        editor_id.commit();

                                        startActivity(new Intent(HireCongratulation.this, Login.class));
                                    }
                                });
                                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_interviews).setChecked(false);
                                        dialog.cancel();
                                    }
                                });
                                alertDialog.show();
                                break;
                        }
                        return true;
                    }
                });
    }
}
