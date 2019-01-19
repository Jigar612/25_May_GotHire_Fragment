package com.jigar.android.gothire.Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jigar.android.gothire.JobRound;
import com.jigar.android.gothire.JobView;

import com.jigar.android.gothire.LiveLink;
import com.jigar.android.gothire.R;
import com.jigar.android.gothire.SetterGetter.RowItems_Interviews;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by COMP11 on 06-Mar-18.
 */

public class Adapter_interviews extends BaseAdapter {
    ArrayList<RowItems_Interviews> myList = new ArrayList<RowItems_Interviews>();
    LayoutInflater inflater;
    Context context;
    Bundle args ;

    public Adapter_interviews(ArrayList myList, LayoutInflater inflater, Context context) {
        this.myList = myList;
        this.inflater = inflater;
        this.context = context;
    }
    public void updateResults(ArrayList<RowItems_Interviews> results)
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
        convertView = inflater.inflate(R.layout.listitem_interviews, null);
        TextView tv_job_title = (TextView)convertView.findViewById(R.id.tv_job_nm_home_interview_listitem);
        TextView tv_company_nm = (TextView)convertView.findViewById(R.id.tv_cpy_nm_home_interview_listitem);

        LinearLayout line_row_select = (LinearLayout)convertView.findViewById(R.id.linear_main_home_interview_listitem);
        LinearLayout line_color_status = (LinearLayout)convertView.findViewById(R.id.linear_interview_color_status);
       // ImageView img_color_staus = (ImageView)convertView.findViewById(R.id.img_color_status);
        LinearLayout line_aerrow_hint_color = (LinearLayout)convertView.findViewById(R.id.linear_aerrow_hint_color);
        ImageView img_cpy_logo=(ImageView)convertView.findViewById(R.id.img_cpy_logo_home_interview_listitem) ;


        Typeface roboto_Med = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Medium.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Light.ttf");
        tv_job_title.setTypeface(roboto_Med);
        tv_company_nm.setTypeface(roboto_Light);


        tv_job_title.setText(myList.get(position).getJobTitle());
        tv_company_nm.setText(myList.get(position).getCompanyName());
        String active=myList.get(position).getInterviewStatus();


        String getPath=myList.get(position).getImage_path();

       // if(!getPath.equals(null)|| !getPath.equals("") || !getPath.equals("null"))
        //{
            String path= LiveLink.LinkLive2+getPath;
            Picasso.with(context)
                    .load(path)
                    .placeholder(R.drawable.ic_briefcase)
                    .into(img_cpy_logo);
        //}

        if (active.equals("1"))
        {
            line_color_status.setBackgroundResource(R.color.my_orange);
            //img_color_staus
        }
        else
        {
            line_aerrow_hint_color.setVisibility(View.GONE);
        }
        line_row_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String active=myList.get(position).getInterviewStatus();
                if (active.equals("1"))
                {
//                    Intent intent_viewRound = new Intent(context, JobRound.class);
//                    intent_viewRound.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent_viewRound.putExtra("Key_candidate_id",myList.get(position).getCandidateID());
//                    intent_viewRound.putExtra("Key_jobPost_id",myList.get(position).getJobPostId());
//                    intent_viewRound.putExtra("Key_job_nm",myList.get(position).getJobTitle());
//                    intent_viewRound.putExtra("Key_cpy_nm",myList.get(position).getCompanyName());
//                    intent_viewRound.putExtra("Key_job_desc",myList.get(position).getJobDescription());
//                    intent_viewRound.putExtra("Key_interview_accepted_date_display",  myList.get(position).getInterviewAcceptDateDisplay().toString());
//
//                    intent_viewRound.putExtra("Key_image_path",myList.get(position).getImage_path());
//                    context.startActivity(intent_viewRound);
                    args = new Bundle();
                    args.putString("Key_candidate_id",myList.get(position).getCandidateID());
                    args.putString("Key_jobPost_id",myList.get(position).getJobPostId() );
                    args.putString("Key_job_nm", myList.get(position).getJobTitle() );
                    args.putString("Key_cpy_nm",myList.get(position).getCompanyName());
                    args.putString("Key_job_desc",myList.get(position).getJobDescription());
                    args.putString("Key_interview_accepted_date_display",myList.get(position).getInterviewAcceptDateDisplay().toString());
                    args.putString("Key_image_path",myList.get(position).getImage_path());
                    loadFragment(new JobRound());



                }
                else
                {
                    args = new Bundle();
                    args.putString("Key_candidate_id",myList.get(position).getCandidateID());
                    args.putString("Key_jobPost_id",myList.get(position).getJobPostId() );
                    args.putString("Key_job_nm", myList.get(position).getJobTitle() );
                    args.putString("Key_cpy_nm",myList.get(position).getCompanyName());
                    args.putString("Key_job_desc",myList.get(position).getJobDescription());
                    args.putString("Key_interview_accepted_date_display",myList.get(position).getInterviewAcceptDateDisplay().toString());
                    args.putString("Key_image_path",myList.get(position).getImage_path());
                    loadFragment(new JobRound());
                }
            }
        });

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
        fragmentTransaction.commit(); // save the changes
    }
}
