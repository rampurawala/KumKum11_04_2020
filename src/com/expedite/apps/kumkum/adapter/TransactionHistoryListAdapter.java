package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.constants.Constants;

/**
 * Created by Administrator on 15/12/2016.
 */


public class TransactionHistoryListAdapter extends BaseAdapter {
    Context context;
    String[] value;

    public TransactionHistoryListAdapter(Context c, String[] data) {
        context = c;
        value = data;
    }

    @Override
    public int getCount() {
        return value.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        try {
            if (value != null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.transactionhistoryitem, parent, false);
                TextView tvid = (TextView) view.findViewById(R.id.txtid);
                TextView tvamount = (TextView) view.findViewById(R.id.txtAmount);
                TextView tvdate = (TextView) view.findViewById(R.id.txtDate);
                TextView tvstatus = (TextView) view.findViewById(R.id.txtStatus);
                if (value[position] != null && value[position].length() > 0) {
                    String[] parts = value[position].split("##@@");
                    if (parts[0] != null && !parts[0].isEmpty())
                        tvid.setText(parts[0]);
                    if (parts[1] != null && !parts[1].isEmpty())
                        tvamount.setText(parts[1]);
                    if (parts[2] != null && !parts[2].isEmpty())
                        tvdate.setText(parts[2]);
                    if (parts[3] != null && !parts[3].isEmpty())
                        tvstatus.setText(parts[3]);
                }
            }
        } catch (Exception ex) {
            Constants.writelog("TransactionHistoryListAdapter",
                    "Exception 89:" + ex.getMessage() + ":::::" + ex.getStackTrace());
            Constants.Logwrite("TransactionHistoryListAdapter",
                    "Exception 89:" + ex.getMessage() + ":::::" + ex.getStackTrace());
        }
        return view;
    }
}
