package com.expedite.apps.kumkum.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;


public class StudentInformationAdapter extends BaseAdapter {
	Context context;
	String[] names;
	public StudentInformationAdapter(Context c, String[] names) {
		// TODO Auto-generated constructor stub
		this.context = c;
		this.names = names;
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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activity_item_list,arg2,false);
		TextView tv = (TextView)view.findViewById(R.id.txtmessages);
		tv.setText(names[arg0]);
		
		return view;
		
	}
}
