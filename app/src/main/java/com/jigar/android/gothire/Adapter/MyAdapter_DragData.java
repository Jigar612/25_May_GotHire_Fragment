package com.jigar.android.gothire.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jigar.android.gothire.R;
import com.jigar.android.gothire.SetterGetter.RowItemGetRoundWiseJob;
import com.lunger.draglistview.DragListAdapter;

import java.util.ArrayList;

/**
 * Created by COMP11 on 03-May-18.
 */

class MyAdapter_DragData extends DragListAdapter {

    ArrayList<String> arrayList = new ArrayList<String>();
    LayoutInflater inflater;
    Context context;

    public MyAdapter_DragData(Context context, ArrayList<String> arrayTitles, ArrayList<String> arrayList, LayoutInflater inflater, Context context1) {
        super(context, arrayTitles);
        this.arrayList = arrayList;
        this.inflater = inflater;
        this.context = context1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;

        view = LayoutInflater.from(context).inflate(
                R.layout.drag_list_item, null);

        TextView textView = (TextView) view
                .findViewById(R.id.tv_name_drag_list_items);
        textView.setText(arrayList.get(position));
        return view;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}