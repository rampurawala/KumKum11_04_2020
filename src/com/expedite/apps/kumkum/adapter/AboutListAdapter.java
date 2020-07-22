package com.expedite.apps.kumkum.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;

public class AboutListAdapter extends BaseAdapter {

    Context context;
    String[] label;
    String[] studdata;

    public AboutListAdapter(Context c, String[] label, String[] studdata) {
        this.context = c;
        this.label = label;
        this.studdata = studdata;
    }

    public int getCount() {
        return studdata.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View arg1, ViewGroup arg2) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_about_listrow, arg2, false);
        TextView tv = (TextView) view.findViewById(R.id.txtLabel);
        TextView tv1 = (TextView) view.findViewById(R.id.txtvalue);

        if (label[position] != null && !label[position].isEmpty())
            tv.setText(label[position]);
        if (studdata[position] != null && !studdata[position].isEmpty())
            tv1.setText(studdata[position]);
        return view;
    }
}
