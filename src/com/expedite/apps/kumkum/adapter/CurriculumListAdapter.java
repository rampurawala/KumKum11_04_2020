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
import com.expedite.apps.kumkum.model.CurriculumListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaydeep patel on 21/11/2017.
 */

public class CurriculumListAdapter extends RecyclerView.Adapter {
    private List<CurriculumListModel.FolderList> mList = new ArrayList<>();
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private String tag = "CurriculumListAdapter";
    private OnClickListener mOnClickListener;

    public CurriculumListAdapter(Activity context, List<CurriculumListModel.FolderList> ItemList) {
        mList = ItemList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.curriculum_raw_layout, parent, false);
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
                final CurriculumListModel.FolderList tmp = mList.get(position);

                if (tmp.getName() != null && !tmp.getName().isEmpty())
                    ((CustomViewHolder) holder).txtTitle.setText(Html.fromHtml(tmp.getName()));


            }
        } catch (Exception ex) {
            Constants.writelog(tag, "onBindViewHolder():67 " + ex.getMessage());
        }
    }

    public void setClickListener(OnClickListener clickListener) {
        this.mOnClickListener = clickListener;
    }

    public interface OnClickListener {
        void onItemClickListener(int layoutPosition);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtTitle;
        private View rlMainDataView;

        public CustomViewHolder(View view) {
            super(view);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            rlMainDataView = (View) view.findViewById(R.id.rlMainDataView);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onItemClickListener(getLayoutPosition());
            }
        }
    }
}

