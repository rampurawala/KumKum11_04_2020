package com.expedite.apps.kumkum.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.constants.Constants;

public class MessageExpandableAdapter extends BaseAdapter {
	Context context;
	String[] messages;

	public MessageExpandableAdapter(Context c, String[] messages) {
		this.context = c;
		this.messages = messages;
	}

	public int getCount() {
		return messages.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public String GetMessages(int arg0) {
		String[] items = messages[arg0].split("##,@@");
		String date = items[1];
		String msg = items[0];
		// return messages[arg0].toString();
		return msg;
	}

	public String GetMessagesOtherdetails(int arg0) {
		String[] items = messages[arg0].split("##,@@");
		String date = items[1];
		// return details[arg0].toString();
		return date;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View view;
		if (messages[arg0].contains("##@@USP##@@")) {
			view = getViewForDailyDiary(arg0, arg1, arg2);
		} else {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.activity_listview, arg2, false);
			TextView tv = (TextView) view.findViewById(R.id.txt_messages);

			String[] items = messages[arg0].split("##,@@");
			String date = "";
			if (items != null && items.length >= 2) {
				date = items[1];
				// date="14/4/2015 4:48:30 PM";
				//Constants.Logwrite("ListView_Activity Date: ", date);
				// SimpleDateFormat dateFormat = new
				// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"dd/MM/yyyy hh:mm:ss aa");
				try {
					Time cur_time = new Time();
					cur_time.setToNow();
					Date date1 = dateFormat.parse(date);
					int msgday = date1.getDate();
					int currday = cur_time.monthDay;
					int msgyear = date1.getYear() + 1900;
					int curryear = cur_time.year;
					int msgmonth = date1.getMonth();
					int currmonth = cur_time.month;
					if (msgyear == curryear) {
						if (currmonth == msgmonth) {
							if (currday == msgday) {
								SimpleDateFormat formatter = new SimpleDateFormat(
										"hh:mm aa");
								date = formatter.format(date1);
								//Constants.Logwrite("date", "date: " + date);
							} else {
								SimpleDateFormat formatter = new SimpleDateFormat(
										"MMM dd");
								date = formatter.format(date1);
								//Constants.Logwrite("date", "date: " + date);
							}
						} else {
							SimpleDateFormat formatter = new SimpleDateFormat(
									"MMM dd");
							date = formatter.format(date1);
							//Constants.Logwrite("date", "date: " + date);
						}
					} else {
						SimpleDateFormat formatter = new SimpleDateFormat(
								"dd/MM/yyyy");
						date = formatter.format(date1);
						//Constants.Logwrite("date", "date: " + date);
					}
					//Constants.Logwrite("day", "ori day: " + date1.getDay() + " day "+
					// cur_time.weekDay);
					// if(cur_time.monthDay)

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Constants.writelog(
							"MessageExpandableAdapter",
							"Exception 118" + e.getMessage() + ":::::"
									+ e.getStackTrace());
				}

			} else
				Constants.Logwrite("GenListInMonth", "arg_ind " + arg0 + "    "
						+ messages[arg0]);
			String msg = "";
			if (items != null && items.length >= 1)
				msg = items[0];
			else
				Constants.Logwrite("GenListInMonth", "arg_ind " + arg0 + "    "
						+ messages[arg0]);
			tv.setText(msg);
			// tv.setText(Html.fromHtml(msg));
			// tv.setLinksClickable(true);
			// tv.setMovementMethod(LinkMovementMethod.getInstance());
			TextView tv1 = (TextView) view.findViewById(R.id.txtmessagesdate);
			tv1.setText(date);
		}
		return view;
	}

	// *************************************
	public String GetMessagesForDailyDiary(int arg0) {
		// TODO Auto-generated method stub

		String[] items = messages[arg0].split("##@@USP##@@");
		// String date = items[1];
		String msg = items[0];
		// return messages[arg0].toString();
		return msg;
	}

	public String GetMessagesOtherdetailsForDailyDiary(int arg0) {
		// TODO Auto-generated method stub
		String[] items = messages[arg0].split("##@@USP##@@");
		String date = items[1];
		// return details[arg0].toString();
		return date;
	}

	public View getViewForDailyDiary(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(
				R.layout.activity_lesson_messages_item_list, arg2, false);
		TextView tv = (TextView) view.findViewById(R.id.txtlessonmessages);
		// int lastindex = messages[arg0].lastIndexOf(",");
		// String msg =
		// messages[arg0].substring(0,messages[arg0].length()-lastindex);
		// String date =
		// messages[arg0].substring(lastindex,messages[arg0].length());
		String[] items = messages[arg0].split("##@@USP##@@");
		String msg = "";
		int Mod_Id = 0;
		if (items != null && items.length >= 1)
			msg = items[0];
		else
			Constants.Logwrite("GenListInMonth", "arg_ind " + arg0 + "    " + messages[arg0]);
		String[] otherspltr = items[1].split("##HWOTHERDET@@");
		Mod_Id = Integer.parseInt(otherspltr[2].toString());
		if (Mod_Id == 9) {
			tv.setTextSize(25);
		} else {
			tv.setTextSize(25);
		}
		tv.setText(msg);
		return view;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
