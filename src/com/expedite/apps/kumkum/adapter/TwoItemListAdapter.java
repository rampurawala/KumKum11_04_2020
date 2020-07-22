package com.expedite.apps.kumkum.adapter;

 

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;

public class TwoItemListAdapter extends BaseAdapter {

	Context context;
	String[] label;
	String[] studdata;

	public TwoItemListAdapter()
	{}

	public TwoItemListAdapter(Context c, String[] label, String[] studdata) {
		// TODO Auto-generated constructor stub
		this.context = c;
		this.label = label;
		this.studdata = studdata;
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return studdata.length;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2){
		// TODO Auto-generated method stub
		LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activity_two_item_list,arg2,false);
		TextView tv = (TextView)view.findViewById(R.id.txtitem1);
		tv.setText(label[arg0]);
		
		TextView tv1 = (TextView)view.findViewById(R.id.txtitem2);
		tv1.setText(studdata[arg0]);
		
		return view;
	}

}
