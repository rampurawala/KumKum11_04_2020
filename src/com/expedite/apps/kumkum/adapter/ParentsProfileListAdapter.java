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
import com.expedite.apps.kumkum.model.ParentsProfileListModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by tejas patel on 06/09/2018.
 */
public class ParentsProfileListAdapter extends RecyclerView.Adapter {
    private List<ParentsProfileListModel.ParentsProfileList> mList = new ArrayList<>();
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private String Tag = "ParentsProfileListAdapter";

    public ParentsProfileListAdapter(Activity context, List<ParentsProfileListModel.ParentsProfileList> ItemList) {
        mList = ItemList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.activity_parents_profile_row, parent, false);
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
                final ParentsProfileListModel.ParentsProfileList tmp = mList.get(position);
                if (tmp.getTitle() != null && !tmp.getTitle().isEmpty())
                    ((CustomViewHolder) holder).txtLabel.setText(Html.fromHtml(tmp.getTitle().trim()));

                if (tmp.getDetail() != null && !tmp.getDetail().isEmpty())
                    ((CustomViewHolder) holder).txtvalue.setText(Html.fromHtml(tmp.getDetail().trim()));

            }
        } catch (Exception ex) {
            Constants.writelog(Tag, "Ex 67:" + ex.getMessage());
        }
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtLabel, txtvalue;
        private View ll_DataView;

        public CustomViewHolder(View view) {
            super(view);
            txtLabel = (TextView) view.findViewById(R.id.txtLabel);
            txtvalue = (TextView) view.findViewById(R.id.txtvalue);
            ll_DataView = (View) view.findViewById(R.id.ll_DataView);

        }
    }
}



