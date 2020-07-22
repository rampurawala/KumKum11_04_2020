package com.expedite.apps.kumkum.adapter;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.model.CalendarImageListModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jaydeep patel on 11/08/2017.
 */

public class CalendatImageDataAdapter extends RecyclerView.Adapter<CalendatImageDataAdapter.ViewHolder> {
    private Activity mContext;
    private ArrayList<CalendarImageListModel> mList = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public CalendatImageDataAdapter(Activity activity, ArrayList<CalendarImageListModel> mList) {
        this.mContext = activity;
        this.mList = mList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.calendar_image_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            if (mList.get(position).getDate() != null && !mList.get(position).getDate().isEmpty())
                holder.txtDate.setText(mList.get(position).getDate());
            holder.txtDate.setVisibility(View.GONE);
            if (mList.get(position).getImageUrl() != null &&
                    !mList.get(position).getImageUrl().isEmpty() && !mList.get(position).getImageUrl().equalsIgnoreCase("false"))
                Picasso.with(mContext).load(mList.get(position).getImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .into(holder.image);
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            txtDate = (TextView) itemView.findViewById(R.id.txtdatetime);
        }
    }
}
