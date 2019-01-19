package com.jigar.android.gothire.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jigar.android.gothire.LiveLink;
import com.jigar.android.gothire.R;
import com.jigar.android.gothire.SetterGetter.RowItems_Interviews;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by COMP11 on 18-May-18.
 */

public class Adapter_save extends BaseAdapter {
    ArrayList<RowItems_Interviews> myList = new ArrayList<RowItems_Interviews>();

    LayoutInflater inflater;
    Context context;

    public Adapter_save(ArrayList myList, LayoutInflater inflater, Context context) {
        this.myList = myList;
        this.inflater = inflater;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listitem_savejob, null);
        TextView tv_job_title = (TextView) convertView.findViewById(R.id.tv_job_nm_saveJob_listitem);
        TextView tv_company_nm = (TextView) convertView.findViewById(R.id.tv_cpy_nm_saveJob_listitem);
        ImageView img_cpy_logo = (ImageView) convertView.findViewById(R.id.img_cpy_logo_saveJob_listitem);

        Typeface roboto_Med = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Medium.ttf");
        Typeface roboto_Light = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Light.ttf");
        tv_job_title.setTypeface(roboto_Med);
        tv_company_nm.setTypeface(roboto_Light);

        tv_job_title.setText(myList.get(position).getJobTitle());
        tv_company_nm.setText(myList.get(position).getCompanyName());


        String getPath = myList.get(position).getImage_path();
        String path = LiveLink.LinkLive2 + getPath;
        Picasso.with(context)
                .load(path)
                .error(R.drawable.ic_briefcase)//if no image on server then show..
                .placeholder(R.drawable.ic_briefcase)
                .into(img_cpy_logo);


        return convertView;
    }
}
