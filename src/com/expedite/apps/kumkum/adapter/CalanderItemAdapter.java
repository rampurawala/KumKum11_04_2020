package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CalanderItemAdapter extends BaseAdapter {

	Context mcontext;

	public CalanderItemAdapter()
	{

	}
	public CalanderItemAdapter(String color) {
		String clr[] = color.split("@#@#");
		for (int i = 1; i < clr.length; i++) {
			
		}
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
}
