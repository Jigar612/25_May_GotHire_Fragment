package com.jigar.android.gothire.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jigar.android.gothire.LiveLink;
import com.jigar.android.gothire.R;
import com.jigar.android.gothire.SetterGetter.RowItemGetAcceptedJobResult;
import com.jigar.android.gothire.SetterGetter.RowItems_Interviews;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by COMP11 on 14-May-18.
 */

public class Adapter_Track  extends BaseAdapter {

    ArrayList<RowItems_Interviews> myList = new ArrayList<RowItems_Interviews>();
    int status_count=0;

 //   ArrayList<RowItems_Interviews> myList_status = new ArrayList<RowItems_Interviews>();
    LayoutInflater inflater;
    Context context;

    public Adapter_Track(ArrayList myList, LayoutInflater inflater, Context context) {
        this.myList = myList;
        this.inflater = inflater;
        this.context = context;
    }
//    public void updateResults(ArrayList<RowItems_Interviews> results)
//    {
//        myList = results;
//        //Triggers the list update
//        notifyDataSetChanged();
//    }

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
    public View getView(int position, View convertView, ViewGroup parent) {

       // View convertView2;
       // int array_length = myList.get(position).getArrayList_status().size();


     //   if(convertView==null ) {



            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem_track, null);
            TextView tv_job_title = (TextView) convertView.findViewById(R.id.tv_job_nm_track_listitem);
            TextView tv_company_nm = (TextView) convertView.findViewById(R.id.tv_cpy_nm_track_listitem);
            ImageView img_cpy_logo = (ImageView) convertView.findViewById(R.id.img_cpy_logo_track_listitem);
            TextView tv_status = (TextView) convertView.findViewById(R.id.tv_status_track_listitem);
            LinearLayout linearColorStatus =(LinearLayout)  convertView.findViewById(R.id.linear_track_color_status);


            Typeface roboto_Med = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Roboto-Medium.ttf");
            Typeface roboto_Light = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Roboto-Light.ttf");
            tv_job_title.setTypeface(roboto_Med);
            tv_company_nm.setTypeface(roboto_Light);
            tv_status.setTypeface(roboto_Light);

          //  int array_length = myList.get(position).getArrayList_status().size();

//            while (status_count < array_length) {
//             //   if (convertView2 == null)
//              //  {
//                convertView2 = inflater.inflate(R.layout.listitem_track, null);
//                TextView tv_job_title1 = (TextView) convertView2.findViewById(R.id.tv_job_nm_track_listitem);
//                TextView tv_company_nm1 = (TextView) convertView2.findViewById(R.id.tv_cpy_nm_track_listitem);
//                String statusCode = myList.get(position).getArrayList_status().get(status_count).getInterviewStatus();
//                String cpyName = myList.get(position).getArrayList_status().get(status_count).getCpyName();
//                String JobTitle = myList.get(position).getArrayList_status().get(status_count).getJobTitle();
//                TextView tv_status1 = (TextView) convertView2.findViewById(R.id.tv_status_track_listitem);
//
//                tv_job_title1.setText(JobTitle);
//                tv_company_nm1.setText(cpyName);
//                tv_status1.setText("Interview");
//                status_count++;
//                // convertView=convertView2;
//                //  convertView=convertView2;
//             //   }
//                return convertView2;
//
//            }

            String Intervie_status=myList.get(position).getInterviewStatus();

            if (Intervie_status.equals("Interviewing "))
            {
                String upperCase_title = myList.get(position).getJobTitle();
                upperCase_title = upperCase_title.toUpperCase();
                tv_job_title.setText(upperCase_title);
                tv_company_nm.setText(myList.get(position).getCompanyName());
                tv_status.setText("Interviewing");
                linearColorStatus.setBackgroundResource(R.color.interviewing_color);

                String getPath = myList.get(position).getImage_path();
                String path = LiveLink.LinkLive2 + getPath;
                Picasso.with(context)
                        .load(path)
                        .error(R.drawable.ic_briefcase)//if no image on server then show..
                        .placeholder(R.drawable.ic_briefcase)
                        .into(img_cpy_logo);
            }
            else if(Intervie_status.equals("Reviewed"))
            {
                String upperCase_title = myList.get(position).getJobTitle();
                upperCase_title = upperCase_title.toUpperCase();
                tv_job_title.setText(upperCase_title);
                tv_company_nm.setText(myList.get(position).getCompanyName());
                tv_status.setText(Intervie_status);
                linearColorStatus.setBackgroundResource(R.color.reviewed_color);
                String getPath = myList.get(position).getImage_path();
                String path = LiveLink.LinkLive2 + getPath;
                Picasso.with(context)
                        .load(path)
                        .placeholder(R.drawable.ic_briefcase)
                        .into(img_cpy_logo);
            }
            else if(Intervie_status.equals("Applied"))
            {
                String upperCase_title = myList.get(position).getJobTitle();
                upperCase_title = upperCase_title.toUpperCase();
                tv_job_title.setText(upperCase_title);
                tv_company_nm.setText(myList.get(position).getCompanyName());
                tv_status.setText(Intervie_status);
                linearColorStatus.setBackgroundResource(R.color.applied_color);
                String getPath = myList.get(position).getImage_path();
                String path = LiveLink.LinkLive2 + getPath;
                Picasso.with(context)
                        .load(path)
                        .placeholder(R.drawable.ic_briefcase)
                        .into(img_cpy_logo);
            }
            else if(Intervie_status.equals("Hired"))
            {
                String upperCase_title = myList.get(position).getJobTitle();
                upperCase_title = upperCase_title.toUpperCase();
                tv_job_title.setText(upperCase_title);
                tv_company_nm.setText(myList.get(position).getCompanyName());
                tv_status.setText(Intervie_status);
                linearColorStatus.setBackgroundResource(R.color.hire_color);
                String getPath = myList.get(position).getImage_path();
                String path = LiveLink.LinkLive2 + getPath;
                Picasso.with(context)
                        .load(path)
                        .placeholder(R.drawable.ic_briefcase)
                        .into(img_cpy_logo);
            }
            else if(Intervie_status.equals("Not Hired"))
            {
                String upperCase_title = myList.get(position).getJobTitle();
                upperCase_title = upperCase_title.toUpperCase();
                tv_job_title.setText(upperCase_title);
                tv_company_nm.setText(myList.get(position).getCompanyName());
                tv_status.setText(Intervie_status);
                linearColorStatus.setBackgroundResource(R.color.withdraw_color);
                String getPath = myList.get(position).getImage_path();
                String path = LiveLink.LinkLive2 + getPath;
                Picasso.with(context)
                        .load(path)
                        .placeholder(R.drawable.ic_briefcase)
                        .into(img_cpy_logo);
            }
            else if(Intervie_status.equals("Withdrawn"))
            {
                String upperCase_title = myList.get(position).getJobTitle();
                upperCase_title = upperCase_title.toUpperCase();
                tv_job_title.setText(upperCase_title);
                tv_company_nm.setText(myList.get(position).getCompanyName());
                tv_status.setText(Intervie_status);
                linearColorStatus.setBackgroundResource(R.color.withdraw_color);
                String getPath = myList.get(position).getImage_path();
                String path = LiveLink.LinkLive2 + getPath;
                Picasso.with(context)
                        .load(path)
                        .placeholder(R.drawable.ic_briefcase)
                        .into(img_cpy_logo);
            }
            else if(Intervie_status.equals("Offer"))
            {
                String upperCase_title = myList.get(position).getJobTitle();
                upperCase_title = upperCase_title.toUpperCase();
                tv_job_title.setText(upperCase_title);
                tv_company_nm.setText(myList.get(position).getCompanyName());
                tv_status.setText(Intervie_status);
                linearColorStatus.setBackgroundResource(R.color.offer_color);
                String getPath = myList.get(position).getImage_path();
                String path = LiveLink.LinkLive2 + getPath;
                Picasso.with(context)
                        .load(path)
                        .placeholder(R.drawable.ic_briefcase)
                        .into(img_cpy_logo);
            }

        // }
            return convertView;

    }
}
