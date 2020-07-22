package com.expedite.apps.kumkum.adapter;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.LeaveListModel;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by tejas patel on 31/07/2018.
 */
public class LeaveTypeListAdapter extends RecyclerView.Adapter {
    private List<LeaveListModel.LeaveTypeList> mList = new ArrayList<>();
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private String Tag = "LeaveListAdapter";

    public LeaveTypeListAdapter(Activity context, List<LeaveListModel.LeaveTypeList> ItemList) {
        mList = ItemList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.leave_type_list_row_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            if (holder instanceof CustomViewHolder) {
                final LeaveListModel.LeaveTypeList tmp = mList.get(position);

                if (tmp.getTitle() != null && !tmp.getTitle().isEmpty())
                    ((CustomViewHolder) holder).txtTitle.setText(tmp.getTitle().trim());

                if (tmp.getCount() != null && !tmp.getCount().isEmpty())
                    ((CustomViewHolder) holder).txtCount.setText(tmp.getCount().trim());


            } else {
                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
            }
        } catch (Exception ex) {
            Constants.writelog(Tag, "Ex 67:" + ex.getMessage());
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressbar);
        }
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtCount;
        private View ll_DataView;

        public CustomViewHolder(View view) {
            super(view);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtCount = (TextView) view.findViewById(R.id.txtCount);
            ll_DataView = (View) view.findViewById(R.id.ll_DataView);

        }
    }
}



