package com.jigar.android.gothire.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jigar.android.gothire.Notification;
import com.jigar.android.gothire.R;
import com.jigar.android.gothire.SetterGetter.RowItemEducation;
import com.jigar.android.gothire.SetterGetter.RowItem_ApplyForJob;
import com.jigar.android.gothire.SetterGetter.RowItem_Notification;

import java.util.ArrayList;

/**
 * Created by COMP11 on 05-Oct-18.
 */

public class Adapter_Education  extends BaseAdapter {
    ArrayList<RowItemEducation> arrayList = new ArrayList<RowItemEducation>();
    LayoutInflater inflater;
    Context context;

    public Adapter_Education(ArrayList<RowItemEducation> myList1, LayoutInflater inflater, Context context) {
        this.arrayList = myList1;
        this.inflater = inflater;
        this.context = context;
    }

    @Override
    public int getCount() {
        //return arrayList.size();
        return  arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(getCount()-position-1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean isEnabled(int position) {
        return false;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listitems_education, null);

        LinearLayout linearLayout_line = (LinearLayout)convertView.findViewById(R.id.linear_line_education);

        TextView tv_education_name_listItem,tv_education_school_nm_listItem,tv_st_dt_education_listitem,tv_location_education_listitem;

        tv_education_name_listItem=(TextView)convertView.findViewById(R.id.tv_education_name_listItem);
        tv_education_school_nm_listItem=(TextView)convertView.findViewById(R.id.tv_education_school_nm_listItem);
        tv_st_dt_education_listitem=(TextView)convertView.findViewById(R.id.tv_st_dt_education_listitem);
        tv_location_education_listitem=(TextView)convertView.findViewById(R.id.tv_location_education_listitem);


        tv_education_name_listItem.setText(arrayList.get(position).getFiledOfStdy());
        tv_education_school_nm_listItem.setText(arrayList.get(position).getSchool());


        String st_end_date = arrayList.get(position).getFromDate()+ "-"+ arrayList.get(position).getTodate();
        if(!st_end_date.equals("null-null"))
        {
            tv_st_dt_education_listitem.setText(st_end_date);
        }

        tv_location_education_listitem.setText(arrayList.get(position).getLocation());


        if (position == arrayList.size() - 1) {
            linearLayout_line.setVisibility(View.GONE);
        }else{
            linearLayout_line.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
