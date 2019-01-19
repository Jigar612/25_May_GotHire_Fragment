package com.jigar.android.gothire.Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jigar.android.gothire.ApplyForJob;
import com.jigar.android.gothire.Interviews;
import com.jigar.android.gothire.JobView;
import com.jigar.android.gothire.LiveLink;
import com.jigar.android.gothire.R;
import com.jigar.android.gothire.SetterGetter.RowItemGetAcceptedJobResult;
import com.jigar.android.gothire.SetterGetter.RowItem_ApplyForJob;
import com.jigar.android.gothire.UrlString;
import com.jigar.android.gothire.Home;
import com.jigar.android.gothire.WebView.WebView_Linkdin;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import me.toptas.fancyshowcase.FancyShowCaseView;

/**
 * Created by COMP11 on 16-Mar-18.
 */

public class Adapter_ApplyForJob extends BaseAdapter {
  //  ArrayList<RowItem_ApplyForJob> myList = new ArrayList<RowItem_ApplyForJob>();
  ArrayList<RowItem_ApplyForJob> myList = new ArrayList<RowItem_ApplyForJob>();
  //ArrayList<RowItem_ApplyForJob> arrayList = new ArrayList<RowItem_ApplyForJob>();
    LayoutInflater inflater;
    Context context;
   // ApplyForJob context;
    String Candidate_id;
    String job_postId;
    ProgressDialog progressDialog;
    Bundle args = new Bundle();
    JobView jobViewFragment;
  //  FancyShowCaseView fanyview;

    public Adapter_ApplyForJob(ArrayList<RowItem_ApplyForJob> myList1, LayoutInflater inflater, Context context) {
        this.myList = myList1;
        this.inflater = inflater;
        this.context = context;
    }

    public void addListItemAdapter(ArrayList<RowItem_ApplyForJob> list)
    {
        myList.addAll(list);
        this.notifyDataSetChanged();
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
        convertView = inflater.inflate(R.layout.listitem_applyforjob, null);
//

        TextView tv_job_title = (TextView)convertView.findViewById(R.id.tv_job_nm_apply_job_listitem);
        TextView tv_company_nm = (TextView)convertView.findViewById(R.id.tv_cpy_nm_apply_job_listitem);
        TextView tv_salary =(TextView)convertView.findViewById(R.id.tv_salary_apply_job_listitem);
        TextView tv_city = (TextView)convertView.findViewById(R.id.tv_country_apply_job_listitem);
        LinearLayout line_row_select = (LinearLayout)convertView.findViewById(R.id.linear_listitem_apply_job);

        Typeface roboto_Med = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Medium.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Light.ttf");
        tv_job_title.setTypeface(roboto_Med);
        tv_company_nm.setTypeface(roboto_Light);
        tv_salary.setTypeface(roboto_Light);
        tv_city.setTypeface(roboto_Light);

        ImageView img_cpy_logo = (ImageView)convertView.findViewById(R.id.img_cpy_logo_listitem__apply_job);


        jobViewFragment =new JobView();


        //New 09_oct_18 changes for salary formate like 1,00,000;
        String from_salary = myList.get(position).getSalaryFrom().toString();
        String to_salary = myList.get(position).getSalaryTo().toString();
        float cnvt_from_salary=0,cnvt_to_salary=0;
        if(to_salary!= null && !to_salary.isEmpty() && !to_salary.equals("null") && !to_salary.equals("")) {
            cnvt_from_salary= Float.parseFloat(from_salary);
        }

        if(to_salary!= null && !to_salary.isEmpty() && !to_salary.equals("null") && !to_salary.equals("")) {
            cnvt_to_salary = Float.parseFloat(to_salary);
        }

        DecimalFormat formatter = new DecimalFormat("##,##,###");
        String to_salary_formate,from_salary_formate;

        if(cnvt_from_salary!= 0)
        {
            from_salary_formate= formatter.format(cnvt_from_salary);
        }
        else
        {
            from_salary_formate=myList.get(position).getSalaryFrom();
        }
        if(cnvt_to_salary!= 0)// && !to_salary.isEmpty() && !to_salary.equals("null") && !to_salary.equals(""))
        {
            to_salary_formate= formatter.format(cnvt_to_salary);
        }
        else
        {
            to_salary_formate=myList.get(position).getSalaryTo();
        }
        String salary = "$"+from_salary_formate+"-"+"$"+to_salary_formate;
        //**************************
        //final String salary = "$"+ myList.get(position).getSalaryFrom()+"-"+"$"+ myList.get(position).getSalaryTo();

        String job_title_uppercase = myList.get(position).getJobTitle();
        job_title_uppercase=job_title_uppercase.toUpperCase();
        tv_job_title.setText(job_title_uppercase);
        tv_company_nm.setText(myList.get(position).getCompanyName());
        tv_salary.setText(salary);
        tv_city.setText(myList.get(position).getJobLocation());

        String getPath=myList.get(position).getImage_path();


//        if(getPath!=(null)|| getPath!="" || getPath!="null")
//        {
            String path= LiveLink.LinkLive2+getPath;
            Picasso.with(context)
                    .load(path)
                    .error(R.drawable.ic_briefcase)//if no image on server then show..
                    .placeholder(R.drawable.ic_briefcase)
                    .into(img_cpy_logo);
        //}
        line_row_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //New for focus view
             //    Home.fanyview.removeView();
                //****
                String candidate_id =myList.get(position).getCandidateid();
                String post_id=myList.get(position).getJobPostId();
                String job_nm = myList.get(position).getJobTitle();
                String cpy_nm= myList.get(position).getCompanyName();
                String job_desc=myList.get(position).getJobDescription();
                String job_type =myList.get(position).getJobType().toString();
                String city=  myList.get(position).getJobLocation();
                String postedon=  myList.get(position).getPostedOn();
                String url=  myList.get(position).getUrl();

                String Certificate=  myList.get(position).getCertificate();
                String education=  myList.get(position).getEducation();

                String VideoDesc=myList.get(position).getVideoDesc();



                //New 09_oct_18 changes for salary formate like 1,00,000;
                String from_salary = myList.get(position).getSalaryFrom().toString();
                String to_salary = myList.get(position).getSalaryTo().toString();
                float cnvt_from_salary=0,cnvt_to_salary=0;
                if(to_salary!= null && !to_salary.isEmpty() && !to_salary.equals("null") && !to_salary.equals("")) {
                     cnvt_from_salary= Float.parseFloat(from_salary);
                }

                if(to_salary!= null && !to_salary.isEmpty() && !to_salary.equals("null") && !to_salary.equals("")) {
                    cnvt_to_salary = Float.parseFloat(to_salary);
                }

                DecimalFormat formatter = new DecimalFormat("##,##,###");
                String to_salary_formate,from_salary_formate;

                if(cnvt_from_salary!= 0)
                {
                     from_salary_formate= formatter.format(cnvt_from_salary);
                }
                else
                {
                    from_salary_formate=myList.get(position).getSalaryFrom();
                }
                if(cnvt_to_salary!= 0)// && !to_salary.isEmpty() && !to_salary.equals("null") && !to_salary.equals(""))
                {
                     to_salary_formate= formatter.format(cnvt_to_salary);
                }
                else
                {
                    to_salary_formate=myList.get(position).getSalaryTo();
                }
                String salary = "$"+from_salary_formate+"-"+"$"+to_salary_formate;
                //**************************


              //  String salary = "$"+ myList.get(position).getSalaryFrom()+"-"+"$"+ myList.get(position).getSalaryTo();

                String getSkill = myList.get(position).getSkillname();
                String getLanguage = myList.get(position).getLanguageKnwon();
                //new
                String getPath = myList.get(position).getImage_path();

              //  if(postedon.equals("Posted on GotHire"))
            //    {
                    args = new Bundle();
                    args.putString("Key_candidate_id",candidate_id);
                    args.putString("Key_jobPost_id",post_id );
                    args.putString("Key_job_nm", job_nm );
                    args.putString("Key_cpy_nm",cpy_nm);
                    args.putString("Key_job_desc",job_desc);
                    args.putString("Key_job_type",job_type);
                    args.putString("Key_salary",salary);
                    args.putString("Key_city",city );

                    //New
                    args.putString("Key_skillname",getSkill );
                    args.putString("Key_language",getLanguage );
                    args.putString("Key_image_path",getPath);

                    args.putString("Key_certificate",Certificate);
                    args.putString("Key_education",education);

                    args.putString("Key_videoDesc",VideoDesc);


                    // loadFragment(new JobView());
                    String cand= jobViewFragment.Candidate_id;
                    FragmentManager fm =  ((Activity) context).getFragmentManager();
                    // create a FragmentTransaction to begin the transaction and replace the Fragment
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    // replace the FrameLayout with new Fragment
                    jobViewFragment.setArguments(args);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.frameLayout,jobViewFragment,"JobView");
                    fragmentTransaction.commit(); // save the changes
                //}
                //New if not --> Posted on GotHire then
               // else
                //{
                   // url = "https://google.com/";
//                    args = new Bundle();
//                    args.putString("Key_candidate_id",candidate_id);
//                    args.putString("Key_Url",url);
//
//                    WebView_Linkdin webView_linkdin = new WebView_Linkdin();
//                    FragmentManager fm =  ((Activity) context).getFragmentManager();
//                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
//                    fragmentTransaction.addToBackStack(null);
//                    webView_linkdin.setArguments(args);
//                    fragmentTransaction.replace(R.id.frameLayout,webView_linkdin);
//                    fragmentTransaction.commit(); // save the changes
//                }
            }
        });
        return convertView;
    }
}
