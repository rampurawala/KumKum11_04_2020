package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;

public class ExamItemListAdapter extends BaseAdapter {
    private Context context;
    private String[] subname;
    private String[] maxmarks;
    private int SummeryIndex = 0;

    public ExamItemListAdapter(Context c, String[] subname, String[] maxmarks) {
        this.context = c;
        this.subname = subname;
        this.maxmarks = maxmarks;
    }

    public int getCount() {
        return subname.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int arg0, View arg1, ViewGroup arg2) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_exam_itemlist, arg2, false);
        LinearLayout rLayout = (LinearLayout) view.findViewById(R.id.RLayout);
        TextView tv = (TextView) view.findViewById(R.id.txtsubname);
        TextView tv2 = (TextView) view.findViewById(R.id.txtmaxmarks);
        if (subname[arg0].equals(" ") && maxmarks[arg0].equals(" ")) {
            rLayout.setVisibility(View.GONE);
        } else {
            rLayout.setVisibility(View.VISIBLE);
            if (subname[arg0] != null && !subname[arg0].isEmpty()) {
                tv.setVisibility(View.VISIBLE);
                tv.setText(Html.fromHtml(subname[arg0]));
                if (subname[arg0].contains("<center>")) {
                    tv.setGravity(Gravity.CENTER);
                    SummeryIndex = arg0;
                } else {
                    tv.setGravity(Gravity.START);
                }
            } else {
                tv.setVisibility(View.GONE);
            }
            if (maxmarks[arg0] != null && !maxmarks[arg0].trim().isEmpty()) {
                tv2.setVisibility(View.VISIBLE);
                tv2.setText(Html.fromHtml(maxmarks[arg0]));
            } else {
                tv2.setVisibility(View.GONE);
            }
            if (arg0 > SummeryIndex) {
                tv.setTypeface(null, Typeface.BOLD);
                tv2.setTypeface(null, Typeface.BOLD);
            } else {
                tv.setTypeface(null, Typeface.NORMAL);
                tv2.setTypeface(null, Typeface.NORMAL);
            }
        }
        return view;
    }

}