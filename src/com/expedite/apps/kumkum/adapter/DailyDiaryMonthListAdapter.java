package com.expedite.apps.kumkum.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;

public class DailyDiaryMonthListAdapter extends BaseAdapter {
    String monthtext[];
    Context mContext;

    public DailyDiaryMonthListAdapter(Context context, String[] text) {
        this.monthtext = text;
        this.mContext = context;
    }

    public int getCount() {
        return monthtext.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_dailydiary_month_list, parent, false);
        TextView btn = (TextView) view.findViewById(R.id.btn_month_text);
        btn.setId(position);
        if (monthtext[position] != null && !monthtext[position].isEmpty())
            btn.setText(monthtext[position]);
        return view;
    }
}
