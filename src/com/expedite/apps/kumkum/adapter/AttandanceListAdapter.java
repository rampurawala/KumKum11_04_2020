package com.expedite.apps.kumkum.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.constants.Constants;

public class AttandanceListAdapter extends BaseAdapter {
    Context context;
    String[] names;
    int iscircular = 0;

    public AttandanceListAdapter() {
    }

    public AttandanceListAdapter(Context c, String[] names) {
        // TODO Auto-generated constructor stub
        this.context = c;
        this.names = names;
    }

    public AttandanceListAdapter(Context c, String[] names, int iscircular) {
        // TODO Auto-generated constructor stub
        this.context = c;
        this.names = names;
        this.iscircular = iscircular;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return names.length;
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getItenName(int arg0) {
        // TODO Auto-generated method stub
        return names[arg0].toString();
    }

    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_item_list_activity_attandance, arg2, false);
        try {
            if (iscircular == 0) {
                TextView tv = (TextView) view.findViewById(R.id.txt_msg);
                TextView tv1 = (TextView) view.findViewById(R.id.txt_values);
                String[] a = names[arg0].split(":");

                tv.setText(a[0].toString());
                tv1.setText(a[1].toString());

                ImageView iv = (ImageView) view.findViewById(R.id.imgbullet);
            } else {
                TextView tv = (TextView) view.findViewById(R.id.txt_msg);
                TextView tv1 = (TextView) view.findViewById(R.id.txt_values);

                tv.setText(names[arg0]);
                tv1.setVisibility(View.GONE);

                ImageView iv = (ImageView) view.findViewById(R.id.imgbullet);
                iv.setVisibility(View.GONE);

            }
        } catch (Exception ex) {
            Constants.writelog("AttandanceListAdapter", "Exception 80:" + ex.getMessage() + ":::::" + ex.getStackTrace());
        }
        return view;
        //iv.setImageDrawable()
        /*if(arg0 == 0)
		{
			tv.setBackgroundColor(Color.GREEN);
		}
		else
		{
			//tv.setBackgroundColor(Color.TRANSPARENT);	
		}*/
    }
}
