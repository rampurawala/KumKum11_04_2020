package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;


/**
 * Created by Jaydeep on 10-09-16.
 */
public class StudentAttendanceListAdapter extends RecyclerView.Adapter {
    String[] messages;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public StudentAttendanceListAdapter(Context context, String[] messages) {
        this.messages = messages;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.student_attendance_listrow_layout, parent, false);
        return new CustomViewHolder(view);

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return messages.length;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CustomViewHolder) {
            try {
                String[] items = messages[position].split(":");
                if (items != null && items.length > 0) {
                    if (items[0] != null && !items[0].isEmpty())
                        ((CustomViewHolder) holder).txtLabel.setText(items[0]);
                }
                if (items != null && items.length > 1) {
                    if (items[1] != null && !items[1].isEmpty())
                        ((CustomViewHolder) holder).txtvalue.setText(items[1]);
                }


            }catch (Exception ex){
                Common.printStackTrace(ex);
            }
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtLabel, txtvalue;

        private CustomViewHolder(View view) {
            super(view);
            txtLabel = (TextView) view.findViewById(R.id.txtLabel);
            txtvalue = (TextView) view.findViewById(R.id.txtvalue);

        }
    }


}



