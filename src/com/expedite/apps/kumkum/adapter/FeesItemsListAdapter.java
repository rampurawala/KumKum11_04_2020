package com.expedite.apps.kumkum.adapter;

 
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.constants.Constants;

public class FeesItemsListAdapter extends BaseAdapter implements
		OnClickListener {

	Context context;
	String[] messages;
	int FeeStatus = 1;
	public FeesItemsListAdapter()
	{}
	public FeesItemsListAdapter(Context c, String[] messages, int FeeStatus) {
		// TODO Auto-generated constructor stub
		this.context = c;
		this.messages = messages;
		this.FeeStatus = FeeStatus;
		// this.details = details;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return messages.length;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view;
		try {

			Constants.Logwrite("FeeItemListActivity", "Message" + messages[arg0].toString());
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.activity_fee_item_list_activity1, arg2, false);
			String[] items = messages[arg0].split("##@@");
			TextView tvmonthname = (TextView) view.findViewById(R.id.txtFee1);
			TextView tvfee = (TextView) view.findViewById(R.id.txtFee2);
			TextView tvamount = (TextView) view.findViewById(R.id.txtFee3);
			// TextView tvReciptDate = (TextView)
			// view.findViewById(R.id.txtFee4);

			if (FeeStatus == 1) {
				tvmonthname.setBackgroundColor(Color.parseColor("#6B7CFF"));
				tvfee.setBackgroundColor(Color.parseColor("#6B7CFF"));
				tvamount.setBackgroundColor(Color.parseColor("#6B7CFF"));
				tvmonthname.setTextColor(Color.BLACK);
				tvfee.setTextColor(Color.BLACK);
				tvamount.setTextColor(Color.BLACK);
				
			} else if (FeeStatus == 2) {
				tvmonthname.setBackgroundColor(Color.parseColor("#73F899"));
				tvfee.setBackgroundColor(Color.parseColor("#73F899"));
				tvamount.setBackgroundColor(Color.parseColor("#73F899"));
				tvmonthname.setTextColor(Color.BLACK);
				tvfee.setTextColor(Color.BLACK);
				tvamount.setTextColor(Color.BLACK);
			} else {
				tvmonthname.setBackgroundColor(Color.parseColor("#FFA898"));
				tvfee.setBackgroundColor(Color.parseColor("#FFA898"));
				tvamount.setBackgroundColor(Color.parseColor("#FFA898"));
				tvmonthname.setTextColor(Color.BLACK);
				tvfee.setTextColor(Color.BLACK);
				tvamount.setTextColor(Color.BLACK);
			}

			Constants.Logwrite("FeeItemListActivity", "Length" + items.length);
			// MonthName
			if (items.length > 0) {
				// Month Name
				String MonthName = items[0].toString();
				if (MonthName.equals("nomonth") || MonthName.equals("")) {
					tvmonthname.setText("");
				} else {
					tvmonthname.setText(MonthName);
				}
				// FeeName
				String FeeName = items[1].toString();
				if (FeeName.equals("nofee") || FeeName.equals("")) {
					tvfee.setText("");
				} else {
					tvfee.setText(FeeName);
				}
				// Amount
				String Amount = items[2].toString();
				if (Amount.equals("")) {
					tvamount.setText("");
				} else {
					tvamount.setText(Amount);
				}
				// ReceiptDate
			}
		} catch (Exception err) {
			Constants.Logwrite("FeeItemListActivity", "" + err.getMessage() + ":StackTrace:"
					+ err.getStackTrace());
			return null;
		}
		return view;
	}

	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
	}
}
