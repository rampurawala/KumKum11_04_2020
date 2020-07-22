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

public class FoodChartListAdapter extends RecyclerView.Adapter {
    private List<FoodChartListModel.TimeTableList> mList = new ArrayList<>();
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private String tag = "FoodChartListAdapter";

    public FoodChartListAdapter(Activity context, List<FoodChartListModel.TimeTableList> ItemList) {
        mList = ItemList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.food_chart_raw_layout, parent, false);
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

                if (tmp.getTtime() != null && !tmp.getTtime().isEmpty())
                    ((CustomViewHolder) holder).txtDay.setText(Html.fromHtml(tmp.getTtime()));
                if (tmp.getLectuername() != null && !tmp.getLectuername().isEmpty())
                    ((CustomViewHolder) holder).txtFood.setText(Html.fromHtml(tmp.getLectuername()));
                if (tmp.getTeachername() != null && !tmp.getTeachername().isEmpty() &&
                        tmp.getTeachername().equalsIgnoreCase("1")) {
                    ((CustomViewHolder) holder).rlMainDataView.setBackgroundColor(
                            mContext.getResources().getColor(R.color.white));
                    ((CustomViewHolder) holder).txtDay.setTextColor(mContext.getResources()
                            .getColor(R.color.colorPrimary));
                    ((CustomViewHolder) holder).txtFood.setTextColor(mContext.getResources()
                            .getColor(R.color.colorPrimary));
                } else {
                    ((CustomViewHolder) holder).rlMainDataView.setBackgroundColor(
                            mContext.getResources().getColor(android.R.color.white));
                    ((CustomViewHolder) holder).txtDay.setTextColor(mContext.getResources()
                            .getColor(R.color.textbg));
                    ((CustomViewHolder) holder).txtFood.setTextColor(mContext.getResources()
                            .getColor(R.color.textbg));
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
        private TextView txtDay, txtFood;
        private View rlMainDataView;

        public CustomViewHolder(View view) {
            super(view);
            txtDay = (TextView) view.findViewById(R.id.txtDay);
            txtFood = (TextView) view.findViewById(R.id.txtFood);
            rlMainDataView = (View) view.findViewById(R.id.rlMainDataView);

        }
    }
}

