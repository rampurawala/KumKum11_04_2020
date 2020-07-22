package com.expedite.apps.kumkum.adapter;

 

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.constants.Constants;

public class LessonMessagesListAdapter extends BaseAdapter {
	Context context;
	String[] messages;
	String[] details;

	public LessonMessagesListAdapter(Context c, String[] messages) {
		// TODO Auto-generated constructor stub
		this.context = c;
		this.messages = messages;
		// this.details = details;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		// if(messages.length<=0)
		// return 1;
		return messages.length;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String GetMessages(int arg0) {
		// TODO Auto-generated method stub

		String[] items = messages[arg0].split("##@@USP##@@");
		// String date = items[1];
		String msg = items[0];
		// return messages[arg0].toString();
		return msg;
	}

	public String GetMessagesOtherdetails(int arg0) {
		// TODO Auto-generated method stub
		String[] items = messages[arg0].split("##@@USP##@@");
		String date = items[1];
		// return details[arg0].toString();
		return date;
	}
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(
				R.layout.activity_lesson_messages_item_list, arg2, false);
		try {
			TextView tv = (TextView) view.findViewById(R.id.txtlessonmessages);
			// int lastindex = messages[arg0].lastIndexOf(",");
			// String msg =
			// messages[arg0].substring(0,messages[arg0].length()-lastindex);
			// String date =
			// messages[arg0].substring(lastindex,messages[arg0].length());
			if (messages[arg0].contains("##@@USP##@@")) {
				String[] items = messages[arg0].split("##@@USP##@@");
				String msg = "";
				int Mod_Id = 0;
				if (items != null && items.length >= 1)
					msg = items[0];
				else
					Constants.Logwrite("GenListInMonth", "arg_ind " + arg0 + "    "
							+ messages[arg0]);
				if (items[1].contains("##HWOTHERDET@@")) {
					String[] otherspltr = items[1].split("##HWOTHERDET@@");
					Mod_Id = Integer.parseInt(otherspltr[2].toString());
					if (Mod_Id == 9) {
						tv.setTextSize(25);
					} else {
						tv.setTextSize(25);
					}
					tv.setText(msg);
				}
			}
		} catch (Exception ex) {
			Constants.Logwrite("Eror lesson messageitem list",
					"MSG:" + ex.getMessage().toString() + " Stacktrace:"
							+ ex.getStackTrace());
		}
		return view;
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
}
