package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.constants.Constants;

public class CircularListAdapter extends BaseAdapter {

	Context context;
	String[] names;
	String[] date;
	String[] groupname;

	public CircularListAdapter(Context c, String[] names) {
		this.context = c;
		this.names = names;
	}

	public CircularListAdapter(Context c, String[] names,
                               String[] date, String[] groupname) {
		this.context = c;
		this.names = names;
		this.date = date;
		this.groupname = groupname;
	}

	public CircularListAdapter(Context c, String[] names, String[] date) {
		this.context = c;
		this.names = names;
		this.date = date;
	}

	public int getCount() {
		return names.length;
	}

	public Object getItem(int arg0) {
		return arg0;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public String getItenName(int arg0) {
		return names[arg0].toString();
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View view = null;
		try {
			if (date == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.activity_item_list, arg2, false);
				TextView tv = (TextView) view.findViewById(R.id.txtmessages);
				tv.setText(names[arg0]);
				ImageView iv = (ImageView) view.findViewById(R.id.imgbullet);
			} else {
				if (groupname == null) {
					String gmaildate = Constants
							.getgmaildatestring(date[arg0].toString());
					LayoutInflater inflater = (LayoutInflater) context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					view = inflater.inflate(R.layout.activity_item_noticeboard,
							arg2, false);
					TextView tvname = (TextView) view
							.findViewById(R.id.txtname);
					TextView tvdate = (TextView) view
							.findViewById(R.id.txtdate);
					TextView tvgroup = (TextView) view
							.findViewById(R.id.txtgroup);

					// tvname.setWidth(200); widht not set by declaring hear
					/*
					 * RelativeLayout.LayoutParams lp = new
					 * RelativeLayout.LayoutParams(
					 * RelativeLayout.LayoutParams.WRAP_CONTENT,
					 * RelativeLayout.LayoutParams.WRAP_CONTENT);
					 * lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					 * tvdate.setLayoutParams(lp);
					 */
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					tvdate.setLayoutParams(lp);
					
					tvname.setText(names[arg0]);
					tvdate.setText(gmaildate);
					tvgroup.setVisibility(View.GONE);

				} else {
					String gmaildate = Constants
							.getgmaildatestring(date[arg0].toString());
					LayoutInflater inflater = (LayoutInflater) context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					view = inflater.inflate(R.layout.activity_item_noticeboard,
							arg2, false);
					TextView tvname = (TextView) view
							.findViewById(R.id.txtname);
					TextView tvdate = (TextView) view
							.findViewById(R.id.txtdate);
					TextView tvgroup = (TextView) view
							.findViewById(R.id.txtgroup);
					tvname.setText(names[arg0]);
					tvdate.setText(gmaildate);
					tvgroup.setText(groupname[arg0]);
				}
			}
		} catch (Exception ex) {
			Constants.writelog(
					"CircularListAdapter",
					"Exception 89:" + ex.getMessage() + ":::::"
							+ ex.getStackTrace());
			Constants.Logwrite("CircularListAdapter",
					"Exception 89:" + ex.getMessage() + ":::::"
							+ ex.getStackTrace());
		}
		return view;

	}

}
