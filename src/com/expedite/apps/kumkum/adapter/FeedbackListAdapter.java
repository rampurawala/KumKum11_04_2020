package com.expedite.apps.kumkum.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.FeedbackDetailActivity;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.constants.Constants;

/**
 * Created by Administrator on 26/07/2017.
 */

public class FeedbackListAdapter extends RecyclerView.Adapter {
    String[] mList;
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private String tag = "FeedbackListAdapter";

    public FeedbackListAdapter(Activity context, String[] ItemList) {
        mList = ItemList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.feedback_list_row_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mList.length;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            if (holder instanceof CustomViewHolder) {

                final String[] tmp = mList[position].split("@@##");
                ((CustomViewHolder) holder).ll_DataView.setVisibility(View.VISIBLE);

                if (tmp[1] != null && !tmp[1].isEmpty())
                    ((CustomViewHolder) holder).txtCategory.setText(tmp[1].trim());
                if (tmp[3] != null && !tmp[3].isEmpty())
                    ((CustomViewHolder) holder).txtDescription.setText(Html.fromHtml
                            ("<b>Description: </b>" + tmp[3]
                                    .trim()));
                if (tmp[6] != null && !tmp[6].isEmpty())
                    ((CustomViewHolder) holder).txtDate.setText(tmp[6]);
                if (tmp[4] != null && !tmp[4].isEmpty())
                    ((CustomViewHolder) holder).txtStatus.setText(tmp[4]);
                if (tmp[2] != null && !tmp[2].isEmpty())
                    ((CustomViewHolder) holder).txtsubject.setText(tmp[2]);
                if (tmp[5] != null && !tmp[5].isEmpty()) {
                    ((CustomViewHolder) holder).txtComment.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).txtComment.setText(Html.fromHtml("<b>Replay: " +
                            "</b>" + tmp[5]));
                } else {
                    ((CustomViewHolder) holder).txtComment.setVisibility(View.GONE);
                }

            /*if (tmp.getComment() != null && !tmp.getComment().isEmpty()) {
                ((CustomViewHolder) holder).txtComment.setVisibility(View.GONE);
                ((CustomViewHolder) holder).txtComment.setText(tmp.getComment().trim());
                ((CustomViewHolder) holder).ll_mainView.setBackgroundColor(
                        mContext.getResources().getColor(R.color.feedback_bg));
            } else {*/
                ((CustomViewHolder) holder).ll_mainView.setBackgroundColor(
                        mContext.getResources().getColor(android.R.color.white));

                ((CustomViewHolder) holder).ll_DataView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, FeedbackDetailActivity.class);
                        intent.putExtra("value", mList[position]);
                        mContext.startActivity(intent);
                        Common.onClickAnimation(mContext);
                        //((Activity) mContext).onClickAnimation();
                    }
                });
            }
        } catch (Exception ex) {
            Constants.writelog(tag,
                    "onBindViewHolder():100 " + ex.getMessage());
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(View v) {
            super(v);
        }
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCategory, txtDescription, txtComment, txtStatus, txtsubject,
                txtDate;
        private View ll_mainView, ll_DataView;

        public CustomViewHolder(View view) {
            super(view);
            txtCategory = (TextView) view.findViewById(R.id.txtCategory);
            txtDescription = (TextView) view.findViewById(R.id.txtDescription);
            txtComment = (TextView) view.findViewById(R.id.txtComment);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            txtsubject = (TextView) view.findViewById(R.id.txtSubject);
            txtDate = (TextView) view.findViewById(R.id.txtDate);

            ll_mainView = (View) view.findViewById(R.id.ll_mainView);
            ll_DataView = (View) view.findViewById(R.id.ll_DataView);
        }
    }
}

