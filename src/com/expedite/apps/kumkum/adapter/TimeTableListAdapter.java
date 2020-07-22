package com.expedite.apps.kumkum.adapter;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.FoodChartListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaydeep patel on 20/11/2017.
 */

public class TimeTableListAdapter extends RecyclerView.Adapter {
    private List<FoodChartListModel.TimeTableList> mList = new ArrayList<>();
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private String tag = "TimeTableListAdapter";
    private boolean isTimeShow = true, isTeacherShow = true, isSubjectShow = true;

    public TimeTableListAdapter(Activity context, List<FoodChartListModel.TimeTableList> ItemList, boolean isSubjectShow, boolean isTeacherShow, boolean isTimeShow) {
        //
        mList = ItemList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.isSubjectShow = isSubjectShow;
        this.isTeacherShow = isTeacherShow;
        this.isTimeShow = isTimeShow;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.time_table_raw_layout, parent, false);
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
                final FoodChartListModel.TimeTableList tmp = mList.get(position);

                if (tmp.getTtime() != null && !tmp.getTtime().isEmpty()) {
                    ((CustomViewHolder) holder).txtTime.setText(Html.fromHtml(tmp.getTtime()));
                } else {
                    ((CustomViewHolder) holder).txtTime.setText("");
                }

                if (tmp.getLectuername() != null && !tmp.getLectuername().isEmpty()) {
                    ((CustomViewHolder) holder).txtSubject.setText(Html.fromHtml(tmp.getLectuername()));
                } else {
                    ((CustomViewHolder) holder).txtSubject.setText("");
                }
                if (tmp.getTeachername() != null && !tmp.getTeachername().isEmpty() && isTeacherShow) {
                    ((CustomViewHolder) holder).txtTeacher.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).txtTeacher.setText(Html.fromHtml(tmp.getTeachername()));
                } else {
                    ((CustomViewHolder) holder).txtTeacher.setText("");
                    ((CustomViewHolder) holder).txtTeacher.setVisibility(View.GONE);
                }
                if (isTimeShow) {
                    ((CustomViewHolder) holder).txtTime.setVisibility(View.VISIBLE);
                } else {
                    ((CustomViewHolder) holder).txtTime.setVisibility(View.GONE);
                }

                if (isSubjectShow) {
                    ((CustomViewHolder) holder).txtSubject.setVisibility(View.VISIBLE);
                } else {
                    ((CustomViewHolder) holder).txtSubject.setVisibility(View.GONE);
                }

                if (isTeacherShow) {
                    ((CustomViewHolder) holder).txtTeacher.setVisibility(View.VISIBLE);
                } else {
                    ((CustomViewHolder) holder).txtTeacher.setVisibility(View.GONE);
                }
            }
        } catch (Exception ex) {
            Constants.writelog(tag, "onBindViewHolder():100 " + ex.getMessage());
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(View v) {
            super(v);
        }
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTime, txtSubject, txtTeacher;
        private View rlMainDataView;

        public CustomViewHolder(View view) {
            super(view);
            txtTime = (TextView) view.findViewById(R.id.txtTime);
            txtSubject = (TextView) view.findViewById(R.id.txtSubject);
            txtTeacher = (TextView) view.findViewById(R.id.txtTeacher);
            rlMainDataView = (View) view.findViewById(R.id.rlMainDataView);

        }
    }
}

