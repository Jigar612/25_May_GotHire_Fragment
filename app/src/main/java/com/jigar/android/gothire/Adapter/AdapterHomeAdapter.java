package com.jigar.android.gothire.Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jigar.android.gothire.Interviews;
import com.jigar.android.gothire.JobView;

import com.jigar.android.gothire.R;
import com.jigar.android.gothire.SetterGetter.RowItemGetAcceptedJobResult;

import java.util.ArrayList;

/**
 * Created by COMP11 on 06-Mar-18.
 */

public class AdapterHomeAdapter extends BaseAdapter {
    ArrayList<RowItemGetAcceptedJobResult> myList = new ArrayList<RowItemGetAcceptedJobResult>();
    LayoutInflater inflater;
    Context context;
    Bundle args;

    public AdapterHomeAdapter(ArrayList myList, LayoutInflater inflater, Context context) {
        this.myList = myList;
        this.inflater = inflater;
        this.context = context;
    }
    public void updateResults(ArrayList<RowItemGetAcceptedJobResult> results)
    {
        myList = results;
        //Triggers the list update
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listitems_home, null);
//

        TextView tv_job_title = (TextView)convertView.findViewById(R.id.tv_job_nm_home_listitem);
        TextView tv_company_nm = (TextView)convertView.findViewById(R.id.tv_cpy_nm_home_listitem);
        TextView tv_salary =(TextView)convertView.findViewById(R.id.tv_salary_home_listitem);
        TextView tv_city = (TextView)convertView.findViewById(R.id.tv_country_home_listitem);

        LinearLayout line_row_select = (LinearLayout)convertView.findViewById(R.id.linear_main_home_listitem);

        Typeface roboto_Med = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Medium.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Light.ttf");
        tv_job_title.setTypeface(roboto_Med);
        tv_company_nm.setTypeface(roboto_Light);
        tv_salary.setTypeface(roboto_Light);
        tv_city.setTypeface(roboto_Light);


        tv_job_title.setText(myList.get(position).getJobTitle());
        tv_company_nm.setText(myList.get(position).getCompanyName());
        tv_salary.setText(myList.get(position).getSalary());
        tv_city.setText(myList.get(position).getCity());

        line_row_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String candidate_id =myList.get(position).getCandidateID();
                String post_id=myList.get(position).getJobPostId();
                String job_nm = myList.get(position).getJobTitle();
                String cpy_nm= myList.get(position).getCompanyName();
                String job_desc=myList.get(position).getJobDescription();



//                    Intent intent_interviews = new Intent(context,Interviews.class);
//                    intent_interviews.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent_interviews.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent_interviews.putExtra("Key_candidate_id", candidate_id);
//                    context.startActivity(intent_interviews);
                args.putString("Key_candidate_id",candidate_id);
                loadFragment(new Interviews());


            }
        });
//        try
//        {
//            tv_job_title.setText(myList.get(position).getJobTitle());
//            tv_company_nm.setText(myList.get(position).getCompanyName());
//            tv_stage.setText(myList.get(position).getTotalStage());
//            tv_days.setText(myList.get(position).toString());
//        }
   //     catch (Exception ex) { }
//        line_row_select.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                var intent = new Intent(welActivity.ApplicationContext, typeof(JobViewActivity));
////                intent.SetFlags(ActivityFlags.NewTask);
////                intent.PutExtra("Key_candidate_id", arrayList[position].id_fetch);
////                intent.PutExtra("Key_jobPost_id", arrayList[position].JobPostId);
////                intent.PutExtra("Key_job_nm", arrayList[position].JobTitle);
////                intent.PutExtra("Key_cpy_nm", arrayList[position].CompanyName);
////                intent.PutExtra("Key_job_desc", arrayList[position].JobDescription);
////                intent.PutExtra("Key_Interview_start_dt", arrayList[position].InterviewStartDate.ToString());
////                intent.PutExtra("Key_interview_accepted_date_display", arrayList[position].InterviewAcceptDateDisplay.ToString());
////
/// welActivity.StartActivity(intent);
//            }
//        line_row_select.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                intent_jobView.putExtra("Key_candidate_id", myList.get(position).getId_fetch());
//                intent_jobView.putExtra("Key_jobPost_id",  myList.get(position).getJobPostId());
//                intent_jobView.putExtra("Key_job_nm",  myList.get(position).getJobTitle());
//                intent_jobView.putExtra("Key_cpy_nm", myList.get(position).getCompanyName());
//                intent_jobView.putExtra("Key_job_desc",  myList.get(position).getJobDescription());
//                intent_jobView.putExtra("Key_Interview_start_dt",  myList.get(position).getInterviewStartDate().toString());
//                intent_jobView.putExtra("Key_interview_accepted_date_display",  myList.get(position).getInterviewAcceptDateDisplay().toString());
//            }
//        });


        return convertView;
    }
    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm =  ((Activity) context).getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit(); // save the changes
    }
}
