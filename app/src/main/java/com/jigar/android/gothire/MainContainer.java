package com.jigar.android.gothire;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jigar.android.gothire.Adapter.Adapter_ApplyForJob;
import com.jigar.android.gothire.SetterGetter.RowItem_ApplyForJob;

import net.skoumal.fragmentback.BackFragmentHelper;

import java.util.ArrayList;

public class MainContainer extends AppCompatActivity {

    SharedPreferences sharedpreferences_id;
    public static final String mypreference_id = "mypref";
    public static final String CandidateKey = "candidateKey";
    String candidate_id_value;

    Bundle args= new Bundle();

    static BottomNavigationView bottomNavigationView;
   // Toolbar toolbar;
    LinearLayout linearLayout_frameLayout,linearLayout_search;//linearLayout_toolbar

   // SearchView searchView;
    ArrayList<RowItem_ApplyForJob> arrayList_search;
    ArrayList<RowItem_ApplyForJob> arrayList;
  //  String keyword_upper,keyword_lower,keyword_filst_lt_capital;
    Adapter_ApplyForJob adapter;
   // ListView listView_jobs;
   // TextView tv_empty;
    public static String TAG="null";

    String openscreen=null;
  //  FancyShowCaseView fanyview;
    public static String Tag_fancyView="Main";


    @Override
    public void onBackPressed() {

        // first ask your fragments to handle back-pressed event
        if(!BackFragmentHelper.fireOnBackPressedEvent(this)) {
            // lets do the default back action if fragments don't consume it
            //OfferNoDocument offerNoDocumentFragment = new OfferNoDocument();

            if(TAG.equals("null")) {
        //        Intent intent = new Intent(MainContainer.this,Splas.class);
        //        startActivity(intent);
            }
            if(TAG.equals("Home")) {
//                Intent intent = new Intent(MainContainer.this,Splas.class);
//                startActivity(intent);
            }
             if(TAG.equals("JobView"))
            {
              //  Toast.makeText(MainContainer.this, "JobView Visible", Toast.LENGTH_SHORT).show();
                args.putString("Key_candidate_id",candidate_id_value);
                loadFragment(new Home());

            }
            if(TAG.equals("UserProfile"))
            {
                getFragmentManager().popBackStack();
//                //  Toast.makeText(MainContainer.this, "JobView Visible", Toast.LENGTH_SHORT).show();
//                args.putString("Key_candidate_id",candidate_id_value);
//                loadFragment(new Home());

            }
             if(TAG.equals("OfferNoDocument"))
            {
               // String Candidate_id11 =offerNoDocumentFragment.Candidate_id;
               // Toast.makeText(MainContainer.this, "Offer Visible", Toast.LENGTH_SHORT).show();
                args.putString("Key_candidate_id",JobRound.Candidate_id_static);
                args.putString("Key_jobPost_id",JobRound.jobPost_id_static );
                args.putString("Key_job_nm",JobRound.jobname_static );
                args.putString("Key_cpy_nm",JobRound.companyname_static);
                args.putString("Key_job_desc",JobRound.jobdesc_static);
                args.putString("Key_interview_accepted_date_display",JobRound.accepted_date_static);
                args.putString("Key_image_path",JobRound.getPath_static);

                loadFragment(new JobRound());
            }
             if(TAG.equals("FinalStageInterview"))
            {
                args.putString("Key_candidate_id",JobRound.Candidate_id_static);
                args.putString("Key_jobPost_id",JobRound.jobPost_id_static );
                args.putString("Key_job_nm",JobRound.jobname_static );
                args.putString("Key_cpy_nm",JobRound.companyname_static);
                args.putString("Key_job_desc",JobRound.jobdesc_static);
                args.putString("Key_interview_accepted_date_display",JobRound.accepted_date_static);
                args.putString("Key_image_path",JobRound.getPath_static);
                loadFragment(new JobRound());
            }
             if(TAG.equals("OfferWithDocument"))
            {
                args.putString("Key_candidate_id",JobRound.Candidate_id_static);
                args.putString("Key_jobPost_id",JobRound.jobPost_id_static );
                args.putString("Key_job_nm",JobRound.jobname_static );
                args.putString("Key_cpy_nm",JobRound.companyname_static);
                args.putString("Key_job_desc",JobRound.jobdesc_static);
                args.putString("Key_interview_accepted_date_display",JobRound.accepted_date_static);
                args.putString("Key_image_path",JobRound.getPath_static);
                loadFragment(new JobRound());
            }
             if(TAG.equals("RejectedOffer"))
            {
                args.putString("Key_candidate_id",JobRound.Candidate_id_static);
                args.putString("Key_jobPost_id",JobRound.jobPost_id_static );
                args.putString("Key_job_nm",JobRound.jobname_static );
                args.putString("Key_cpy_nm",JobRound.companyname_static);
                args.putString("Key_job_desc",JobRound.jobdesc_static);
                args.putString("Key_interview_accepted_date_display",JobRound.accepted_date_static);
                args.putString("Key_image_path",JobRound.getPath_static);
                loadFragment(new JobRound());
            }
             if(TAG.equals("JobRound"))
            {
                args.putString("Key_candidate_id",Interviews.candidate_id_value_static);
                loadFragment(new Interviews());
            }
             if(TAG.equals("QuestionRound"))
            {
                Toast.makeText(this, "Can't go back", Toast.LENGTH_SHORT).show();
//                args.putString("Key_candidate_id",Interviews.candidate_id_value_static);
//                loadFragment(new Interviews());
            }
             if(TAG.equals("SingleQuestion"))
            {
                Toast.makeText(this, "Can't go back", Toast.LENGTH_SHORT).show();

//                args.putString("Key_candidate_id",Interviews.candidate_id_value_static);
//                loadFragment(new Interviews());
            }
             if(TAG.equals("VideoQuestion"))
            {
                Toast.makeText(this, "Can't go back", Toast.LENGTH_SHORT).show();
            }
             if(TAG.equals("FileQuestion"))
            {
                Toast.makeText(this, "Can't go back", Toast.LENGTH_SHORT).show();
            }
             if(TAG.equals("Ranking"))
            {
                Toast.makeText(this, "Can't go back", Toast.LENGTH_SHORT).show();
            }
             if(TAG.equals("JobSearch"))
            {
//                args.putString("Key_candidate_id",candidate_id_value);
//                loadFragment(new Home());
               // getFragmentManager().popBackStack();
            }
             if(TAG.equals("InterviewInvitation"))
            {
//                args.putString("Key_candidate_id",candidate_id_value);
//                loadFragment(new Home());
                getFragmentManager().popBackStack();
            }
             if(TAG.equals("ApplyForJob"))
            {
//                args.putString("Key_candidate_id",candidate_id_value);
//                loadFragment(new Home());
                getFragmentManager().popBackStack();
            }
            else if(TAG.equals("Notification"))
            {
                getFragmentManager().popBackStack();


            }
            else if(TAG.equals("Profile"))
            {
                //  Toast.makeText(MainContainer.this, "JobView Visible", Toast.LENGTH_SHORT).show();
//                args.putString("Key_candidate_id",candidate_id_value);
//                loadFragment(new Home());
              //  getFragmentManager().beginTransaction().remove(this).commit();
                getFragmentManager().popBackStack();
            }
            else if(TAG.equals("Profile1"))
            {
                //  Toast.makeText(MainContainer.this, "JobView Visible", Toast.LENGTH_SHORT).show();
//                  args.putString("Key_candidate_id",candidate_id_value);
//                loadFragment(new Home());
                // getFragmentManager().beginTransaction().remove(this).commit();
                getFragmentManager().popBackStack();
            }
            else if(TAG.equals("Profile2"))
            {
                //  Toast.makeText(MainContainer.this, "JobView Visible", Toast.LENGTH_SHORT).show();
//                args.putString("Key_candidate_id",candidate_id_value);
//                loadFragment(new Home());
                //  getFragmentManager().beginTransaction().remove(this).commit();
                getFragmentManager().popBackStack();
            }
             else if(TAG.equals("Webview_Lindin"))
             {
                 //  Toast.makeText(MainContainer.this, "JobView Visible", Toast.LENGTH_SHORT).show();
                args.putString("Key_candidate_id",candidate_id_value);
                loadFragment(new Home());
                 //  getFragmentManager().beginTransaction().remove(this).commit();
//                 getFragmentManager().popBackStack();
             }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);//findid from xml custome bottomNavigationView
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);//make class for stop animatiom shift mode


       // if ((getResources().getConfiguration().screenLayout &
//                Configuration.SCREENLAYOUT_SIZE_MASK) ==
//                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView)
                    bottomNavigationView.getChildAt(0);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                final View iconView =
                        menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
                final ViewGroup.LayoutParams layoutParams =
                        iconView.getLayoutParams();

                final DisplayMetrics displayMetrics =
                        getResources().getDisplayMetrics();
                layoutParams.height = (int)
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30,
                                displayMetrics);
                layoutParams.width = (int)
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30,
                                displayMetrics);
                iconView.setLayoutParams(layoutParams);
            }
      //  BottomNavigationView bottomNavigationView = (BottomNavigationView) fragmentActivity.findViewById(R.id.bottom_navigation);
        TextView textView_home = (TextView) bottomNavigationView.findViewById(R.id.bottom_nav_home).findViewById(R.id.largeLabel);
        textView_home.setTextSize(9);
        TextView textView_interivew = (TextView) bottomNavigationView.findViewById(R.id.bottom_nav_interviews).findViewById(R.id.largeLabel);
        textView_interivew.setTextSize(9);
        TextView textView_track = (TextView) bottomNavigationView.findViewById(R.id.bottom_nav_track).findViewById(R.id.largeLabel);
        textView_track.setTextSize(9);
        TextView textView_account = (TextView) bottomNavigationView.findViewById(R.id.bottom_nav_account).findViewById(R.id.largeLabel);
        textView_account.setTextSize(9);
     //   }

       if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
        BottomNavigationMenuView menuView1 = (BottomNavigationMenuView)
                bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView1.getChildCount(); i++) {
            final View iconView =
                    menuView1.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams =
                    iconView.getLayoutParams();

            final DisplayMetrics displayMetrics =
                    getResources().getDisplayMetrics();
            layoutParams.height = (int)
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36,
                            displayMetrics);
            layoutParams.width = (int)
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36,
                            displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
           }
        linearLayout_frameLayout=(LinearLayout)findViewById(R.id.linear_frameLayout);
      //  linearLayout_search=(LinearLayout)findViewById(R.id.linear_container_search_list);
        //listView_jobs = (ListView)findViewById(R.id.listView2_wellcome);
       // tv_empty = (TextView) findViewById(R.id.welcome_tv_empty_list);//tv_empty_list_home

        arrayList = new ArrayList<RowItem_ApplyForJob>();
        arrayList_search = new ArrayList<RowItem_ApplyForJob>();

        Intent get_intent=getIntent();
        candidate_id_value = get_intent.getStringExtra("Key_candidate_id");

        openscreen=get_intent.getStringExtra("Key_open_id");
        bottomMenu();
        //New 13-Jul - for Application Flow
//        View btn = bottomNavigationView.findViewById(R.id.bottom_nav_home);
//        fanyview =new FancyShowCaseView.Builder(MainContainer.this)
//            .focusOn(btn)
//            .title("Display your profile matches jobs.")
//            //.enableTouchOnFocusedView(true)
//            .build();
//
//            fanyview.show();
        //*************End**********

        //New 13-Jul - for Application Flow
//
//        fanyview= new FancyShowCaseView.Builder(MainContainer.this)
//                .focusOn(btn)
//
//                .customView(R.layout.custome_view_fancyview, new OnViewInflateListener() {
//                    @Override
//                    public void onViewInflated(View view) {
//                        view.findViewById(R.id.btn_got_it).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                fanyview.removeView();
//                            }
//                        });
//                    }
//                })
//                .closeOnTouch(false)
//                .focusBorderColor(Color.RED)
//                .titleStyle(R.style.focused_text,Gravity.CENTER)
//                .build();
//        fanyview.show();
        //*************End**********

        //write all fancyshowView without show(); and last show all
//        new FancyShowCaseQueue()
//                .add(fancyShowCaseView1)
//                .add(fancyShowCaseView2)
//                .show();


        //Whenever notfification come then handle tha condigiont and open fragment..
        if(openscreen!=null )
        {
            if(openscreen.equals("1")) {
//                loadFragment(new Interviews());
                Interviews interviews = new Interviews();
                args = new Bundle();
                args.putString("Key_candidate_id",candidate_id_value);
                FragmentManager fm_interview_invitation =  getFragmentManager();
                FragmentTransaction fragmentTransaction_interview_invitation = fm_interview_invitation.beginTransaction();
                interviews.setArguments(args);
                fragmentTransaction_interview_invitation.addToBackStack(null);
                fragmentTransaction_interview_invitation.add(R.id.frameLayout, interviews);
                fragmentTransaction_interview_invitation.commit(); // save the ch
            }
            if(openscreen.equals("7")) {
//                args.putString("Key_candidate_id",candidate_id_value);
//                loadFragment(new Interview_Invitation());
                Interview_Invitation interview_invitation = new Interview_Invitation();
                args = new Bundle();
                args.putString("Key_candidate_id",candidate_id_value);
                FragmentManager fm_interview =  getFragmentManager();
                FragmentTransaction fragmentTransaction_interview = fm_interview.beginTransaction();
                interview_invitation.setArguments(args);
                fragmentTransaction_interview.addToBackStack(null);
                fragmentTransaction_interview.add(R.id.frameLayout, interview_invitation);
                fragmentTransaction_interview.commit(); // save the ch
            }
            if(openscreen.equals("8")) {
//                args.putString("Key_candidate_id",candidate_id_value);
//                loadFragment(new Interview_Invitation());
                ApplyForJob applyForJob = new ApplyForJob();

                args.putString("Key_candidate_id",candidate_id_value);
                FragmentManager fm_apply_job =  getFragmentManager();
                FragmentTransaction fragmentTransaction_apply_job = fm_apply_job.beginTransaction();
                applyForJob.setArguments(args);
                fragmentTransaction_apply_job.addToBackStack(null);
                fragmentTransaction_apply_job.add(R.id.frameLayout, applyForJob);
                fragmentTransaction_apply_job.commit();
            }
        }
        else
        {
            args.putString("Key_candidate_id",candidate_id_value);
            loadFragment(new Home());
        }
        //*************************over notification open fragment.
    }
    public void bottomMenu() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.bottom_nav_home:
                                //fanyview.removeView();
                               // Tag_fancyView ="Home";
                                args.putString("Key_candidate_id",candidate_id_value);
                                loadFragment(new Home());

                                //New 13-Jul - for Application Flow

//                                View btn_interview = bottomNavigationView.findViewById(R.id.bottom_nav_interviews);
//                                fanyview =new FancyShowCaseView.Builder(MainContainer.this)
//                                        .focusOn(btn_interview)
//                                        .title("Interview")
//                                        .enableTouchOnFocusedView(true)
//                                        .build();
//
//                                fanyview.show();

                            //***End Focus**********
                                break;
                            case R.id.bottom_nav_interviews:
                             //   fanyview.removeView();

                                args.putString("Key_candidate_id",candidate_id_value);
                                loadFragment(new Interviews());

                                //New 13-Jul - for Application Flow****
//                                View btn_track = bottomNavigationView.findViewById(R.id.bottom_nav_track);
//                                fanyview =new FancyShowCaseView.Builder(MainContainer.this)
//                                        .focusOn(btn_track)
//                                        .title("Track")
//                                        .enableTouchOnFocusedView(true)
//                                        .build();
//                                fanyview.show();
                                // ***End ****************

                                break;
                            case R.id.bottom_nav_track:
                                //fanyview.removeView();
                                args.putString("Key_candidate_id",candidate_id_value);
                                loadFragment(new Track());

                                //New 13-Jul - for Application Flow****
//                                View btn_account = bottomNavigationView.findViewById(R.id.bottom_nav_account);
//                                fanyview= new FancyShowCaseView.Builder(MainContainer.this)
//                                        .focusOn(btn_account)
//                                        .title("Account")
//                                        .enableTouchOnFocusedView(true)
//                                        .build();
//                                fanyview.show();
                                // ***End ****************
                                break;
                            case R.id.bottom_nav_account:
                               // fanyview.removeView();
                                args.putString("Key_candidate_id",candidate_id_value);
                                loadFragment(new UserProfileView());
                                break;
                        }
                        return true;
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
         MenuItem searchMenuItem=menu.findItem(R.id.search_jobs);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.job_search_filter:
                break;
            case R.id.view_invitation:
                break;
            case R.id.job_apply:
                break;
            case R.id.notification:
                break;
            case R.id.profile:
                break;
            case R.id.logout:
               break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void loadFragment(Fragment fragment) {

        //FragmentManager fm1 = getFragmentManager(); // or 'getSupportFragmentManager();'


        // create a FragmentManager
        //getFragmentManager().beginTransaction().remove(null).commit();
       // JobView jobView= new JobView();
        JobView test = (JobView) getFragmentManager().findFragmentByTag("JobView");
        if (test != null && test.isVisible()) {
            JobView.videoView_jobView.getPlayer().stop();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);//For backpress key (UserProfile main)
        fragmentTransaction.commit(); // save the changes
    }
}
