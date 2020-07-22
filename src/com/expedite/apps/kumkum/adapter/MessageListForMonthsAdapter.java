package com.expedite.apps.kumkum.adapter;

 

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.expedite.apps.kumkum.R;

public class MessageListForMonthsAdapter extends BaseAdapter{

	Context context;
	String[] month1;

	public MessageListForMonthsAdapter()
	{}
	public MessageListForMonthsAdapter(Context c, String[] month1) {
		this.context = c;
		this.month1 = month1;
		// TODO Auto-generated constructor stub
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return month1.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub		
		LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activity_message_item_list_for_months,arg2,false);
		Button b1 = (Button)view.findViewById(R.id.btn_month);
		b1.setBackgroundColor(R.drawable.shapformonthlist);
		b1.setText(month1[arg0].toString());
		return view;	
	}

	/*public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}*/

	
}
