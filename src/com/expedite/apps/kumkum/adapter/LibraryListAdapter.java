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
import com.expedite.apps.kumkum.model.LibraryListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaydeep patel on 20/11/2017.
 */

public class LibraryListAdapter extends RecyclerView.Adapter {
    private List<LibraryListModel.BookIssueList> mList = new ArrayList<>();
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private String tag = "LibraryListAdapter";

    public LibraryListAdapter(Activity context, List<LibraryListModel.BookIssueList> ItemList) {
        mList = ItemList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.library_raw_layout, parent, false);
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
                final LibraryListModel.BookIssueList tmp = mList.get(position);

                if (tmp.getBookName() != null && !tmp.getBookName().isEmpty())
                    ((CustomViewHolder) holder).txtTitle.setText(Html.fromHtml(tmp.getBookName()));
                if (tmp.getStatusName() != null && !tmp.getStatusName().isEmpty())
                    ((CustomViewHolder) holder).txtStatus.setText(Html.fromHtml(tmp.getStatusName()));
                if (tmp.getIssueDate() != null && !tmp.getIssueDate().isEmpty())
                    ((CustomViewHolder) holder).txtIssueDate.setText(Html.fromHtml(tmp.getIssueDate()));
                if (tmp.getReturnDate() != null && !tmp.getReturnDate().isEmpty())
                    ((CustomViewHolder) holder).txtDate.setText(Html.fromHtml(tmp.getReturnDate()));

            }
        } catch (Exception ex) {
            Constants.writelog(tag, "onBindViewHolder():67 " + ex.getMessage());
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(View v) {
            super(v);
        }
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtStatus, txtIssueDate, txtDate;
        private View rlMainDataView;

        public CustomViewHolder(View view) {
            super(view);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            txtIssueDate = (TextView) view.findViewById(R.id.txtIssueDate);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            rlMainDataView = (View) view.findViewById(R.id.rlMainDataView);

        }
    }
}

