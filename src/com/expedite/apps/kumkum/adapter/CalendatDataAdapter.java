package com.expedite.apps.kumkum.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.CalendarDataDetailActivity;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.model.CalendarListModel;

import java.util.ArrayList;

/**
 * Created by Jaydeep patel on 11/08/2017.
 */

public class CalendatDataAdapter extends RecyclerView.Adapter<CalendatDataAdapter.ViewHolder> {
    private Activity mContext;
    private ArrayList<CalendarListModel> mList = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public CalendatDataAdapter(Activity activity, ArrayList<CalendarListModel> mList) {
        this.mContext = activity;
        this.mList = mList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.calendar_data_raw_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            holder.txtTitle.setText(Html.fromHtml(mList.get(position).getTitle()));
            holder.txtDate.setText(Html.fromHtml(mList.get(position).getDate()));
            holder.ll_CalendarData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CalendarDataDetailActivity.class);
                    intent.putExtra("Title", mList.get(position).getTitle());
                    intent.putExtra("Date", mList.get(position).getDate());
                    intent.putExtra("Description", mList.get(position).getDescription());
                    intent.putExtra("ImageUrl", mList.get(position).getImageUrl());
                    mContext.startActivity(intent);
                    ((BaseActivity) mContext).onClickAnimation();
                }
            });
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDate;
        CardView ll_CalendarData;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            ll_CalendarData = (CardView) itemView.findViewById(R.id.ll_CalendarData);

        }
    }
}
