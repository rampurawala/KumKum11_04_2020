package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.constants.Constants;

public class LessionMessageListOldAdapter extends BaseAdapter {
	
	Context context;
	String[] messages;
	String[] details;

	public LessionMessageListOldAdapter()
	{}
	public LessionMessageListOldAdapter(Context c, String[] messages) {
		// TODO Auto-generated constructor stub
		this.context = c;
		this.messages = messages;
		//this.details = details;
	}
	public int getCount() {
		// TODO Auto-generated method stub
				  //if(messages.length<=0)
              //return 1;
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
        
		String[] items = messages[arg0].split("##,@@");
		String date = items[1];
		
		String msg = items[0];
						  		 
		//return messages[arg0].toString();
		return msg;
	}
	
	public String GetMessagesOtherdetails(int arg0) {
		// TODO Auto-generated method stub
		String[] items = messages[arg0].split("##,@@");
		String date = items[1];  		 
		//return details[arg0].toString();
		return date;
	}
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activity_lession_message_item_list_new,arg2,false);
		TextView tv = (TextView)view.findViewById(R.id.txtlessonmessages);	
		String[] items = messages[arg0].split("##,@@");
		  String date= "";
		if (items != null && items.length>=2)   date = items[1];
		else Constants.Logwrite("GenListInMonth","arg_ind " + arg0 + "    " + messages[arg0]);
		String msg = "";
		if (items != null && items.length>=1) msg = items[0];
		else Constants.Logwrite("GenListInMonth","arg_ind " + arg0 + "    " + messages[arg0]);
		tv.setText(msg);
		//tv.setLinksClickable(true);
		//tv.setMovementMethod(LinkMovementMethod.getInstance());
		TextView tv1 = (TextView)view.findViewById(R.id.txtlessonmessagesdate);
		tv1.setText(date);
		return view;	
	}	
}
