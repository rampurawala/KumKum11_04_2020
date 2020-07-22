package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.constants.Constants;

public class SpinnerItemsAdapter extends BaseAdapter {

	Context context;
	String[] names;

	public SpinnerItemsAdapter()
	{}
	public SpinnerItemsAdapter(Context c, String[] names) {
		this.context = c;
		this.names = names;

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

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activity_item_list_for_spinner,arg2, false);
		try {
			TextView tv = (TextView) view.findViewById(R.id.txt_circulargroupname);
			tv.setText(names[arg0]);

		} catch (Exception ex) {
			Constants.writelog("SpinnerItemsAdapter", "Exception 49:"
					+ ex.getMessage() + ":::::" + ex.getStackTrace());
		}
		return view;

	}

}
