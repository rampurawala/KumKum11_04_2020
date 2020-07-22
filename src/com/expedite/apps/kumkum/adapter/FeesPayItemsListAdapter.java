package com.expedite.apps.kumkum.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.constants.Constants;

public class FeesPayItemsListAdapter extends BaseAdapter implements OnClickListener {
    private CheckBox chkbox;
    private Context context;
    private String[] messages;
    private int FeeStatus = 1;

    public FeesPayItemsListAdapter(Context c, String[] messages, int FeeStatus) {
        this.context = c;
        this.messages = messages;
        this.FeeStatus = FeeStatus;
    }

    public int getCount() {
        return messages.length;
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(int position, View arg1, ViewGroup arg2) {
        View view;
        try {
            Constants.Logwrite("FeeItemListActivity", "Message" + messages[position].toString());
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_fee_item_list_pay,
                    arg2, false);
            String[] items = messages[position].split("##@@");
            TextView tvmonthname = (TextView) view.findViewById(R.id.txtMonth);
            TextView tvfee = (TextView) view.findViewById(R.id.txtFeeName);
            TextView tvamount = (TextView) view.findViewById(R.id.txtAmount);
            TextView tvfine = (TextView) view.findViewById(R.id.txtfine);
            CheckBox chkbox = (CheckBox) view.findViewById(R.id.checkBox);
            if (position == 0) {
                chkbox.setVisibility(View.INVISIBLE);
            }
            Constants.Logwrite("FeeItemListActivity", "Length" + items.length);
            // MonthName
            if (items.length > 0) {
                // Month Name
                String MonthName = items[0];
                if (MonthName.equals("nomonth") || MonthName.equals("")) {
                    tvmonthname.setText("");
                } else {
                    tvmonthname.setText(MonthName);
                }
                // FeeName
                String FeeName = items[1];
                if (FeeName.equals("nofee") || FeeName.equals("")) {
                    tvfee.setText("");
                } else {
                    tvfee.setText(FeeName);
                }
                // Amount
                String Amount = items[2];
                if (Amount.equals("")) {
                    tvamount.setText("");
                } else {
                    tvamount.setText(Amount);
                }
                if (position != 0) {
                    if (items[3].equalsIgnoreCase("1")) {
                        chkbox.setChecked(true);
                    } else {
                        chkbox.setChecked(false);
                    }
                }
                if (position == 0) {
                    if (items.length > 3) {
                        tvfine.setText(items[3]);
                        tvamount.setText(items[2]);
                        tvfine.setGravity(Gravity.LEFT);
                        tvamount.setGravity(Gravity.LEFT);
                    } else {
                        tvamount.setText(items[2]);
                        tvfine.setVisibility(View.GONE);
                    }
                } else {
                    if (items.length > 6) {
                        String[] parts = items[6].split("#");
                        if (parts.length > 1) {
                            tvfine.setText(parts[1]);
                        }
                    } else {
                        tvfine.setVisibility(View.GONE);
//                        tvamount.setWidth(130);
//                        tvmonthname.setWidth(160);
                    }
                }
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
        //FeesPayActivity.Selected[]
        chkbox.setSelected(true);
    }
}
