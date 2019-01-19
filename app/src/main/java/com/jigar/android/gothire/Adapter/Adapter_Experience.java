package com.jigar.android.gothire.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jigar.android.gothire.R;
import com.jigar.android.gothire.SetterGetter.RowItemEducation;
import com.jigar.android.gothire.SetterGetter.RowItemExpirence;
import com.jigar.android.gothire.SetterGetter.RowItem_Notification;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by COMP11 on 05-Oct-18.
 */

public class Adapter_Experience  extends BaseAdapter {
    ArrayList<RowItemExpirence> arrayList = new ArrayList<RowItemExpirence>();
    LayoutInflater inflater;
    Context context;

    public Adapter_Experience(ArrayList<RowItemExpirence> myList1, LayoutInflater inflater, Context context) {
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
        convertView = inflater.inflate(R.layout.listitems_experience, null);

        LinearLayout linearLayout_line = (LinearLayout)convertView.findViewById(R.id.linear_line_experiance);

        TextView tv_experiance_position,tv_cpy_nm_experiance,tv_st_dt_education_listitem,tv_location_experiance_listitem,tv_expiriance_detials_listitem;
        tv_experiance_position=(TextView)convertView.findViewById(R.id.tv_experiance_position);
        tv_cpy_nm_experiance=(TextView)convertView.findViewById(R.id.tv_cpy_nm_experiance);
        tv_st_dt_education_listitem=(TextView)convertView.findViewById(R.id.tv_st_dt_education_listitem);
        tv_location_experiance_listitem=(TextView)convertView.findViewById(R.id.tv_location_experiance_listitem);
        tv_expiriance_detials_listitem=(TextView)convertView.findViewById(R.id.tv_expiriance_detials_listitem);

        tv_experiance_position.setText(arrayList.get(position).getWork_hist_Designation());
        tv_cpy_nm_experiance.setText(arrayList.get(position).getWork_hist_CompaneyName());
        String st_end_date = arrayList.get(position).getWork_hist_FromDate()+ "-"+ arrayList.get(position).getWork_hist_TODate();
        tv_st_dt_education_listitem.setText(st_end_date);
        tv_location_experiance_listitem.setText(arrayList.get(position).getWork_hist_City());
        tv_expiriance_detials_listitem.setText(arrayList.get(position).getWork_hist_descriptions());


        if (position == arrayList.size() - 1) {
            linearLayout_line.setVisibility(View.GONE);
        }else{
            linearLayout_line.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
