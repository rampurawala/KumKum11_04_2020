package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;


/**
 * Created by Jaydeep on 10-09-16.
 */
public class FeeCardListAdapter extends RecyclerView.Adapter {
    String[] messages;
    private Context mContext;
    int FeeStatus = 1;
    private LayoutInflater mLayoutInflater;
    private int lastPosition = -1;
    public FeeCardListAdapter(Context context, String[] messages, int FeeStatus) {
        this.messages = messages;
        mContext = context;
        this.FeeStatus = FeeStatus;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.feecard_raw_layout_new, parent, false);
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
            if (messages[position] != null && !messages[position].isEmpty()) {
                String[] items = messages[position].split("##@@");
                if (items != null && items.length > 0) {
                    if (position == 0) {
                        ((CustomViewHolder) holder).llMain.setBackgroundColor(mContext.getResources().getColor(R.color.greenshade));
                        ((CustomViewHolder) holder).txtDate.setTypeface(null, Typeface.BOLD);
                        ((CustomViewHolder) holder).txtDetail.setTypeface(null, Typeface.BOLD);
                        ((CustomViewHolder) holder).txtAmount.setTypeface(null, Typeface.BOLD);
                    }
                    if (position == messages.length - 1) {
                        // Month Name
                        ((CustomViewHolder) holder).llMain.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                        if (items[0] != null && items[0].equals("nomonth") || items[0].isEmpty()) {
                            ((CustomViewHolder) holder).txtDate.setText("");
                        } else {
                            ((CustomViewHolder) holder).txtDate.setText(Html.fromHtml(items[0].toString()));
                        }
                        // FeeName
                        if (items[1] != null && items[1].equals("nofee") || items[1].isEmpty()) {
                            ((CustomViewHolder) holder).txtDetail.setText("");
                        } else {
                            ((CustomViewHolder) holder).txtDetail.setText(Html.fromHtml(items[1].toString()));
                        }
                        // Amount
                        if (items[2] != null && items[2].isEmpty()) {
                            ((CustomViewHolder) holder).txtAmount.setText("");
                        } else {
                            ((CustomViewHolder) holder).txtAmount.setText(Html.fromHtml(items[2].toString()));
                        }
                        ((CustomViewHolder) holder).txtoffer.setVisibility(View.GONE);
                    } else {

                        if (items[0] != null && items[0].equals("nomonth") || items[0].isEmpty()) {
                            ((CustomViewHolder) holder).txtDate.setText("");
                        } else {
                            ((CustomViewHolder) holder).txtDate.setText(Html.fromHtml(items[0].toString()));
                        }
                        // FeeName
                        if (items[1] != null && items[1].equals("nofee") || items[1].isEmpty()) {
                            ((CustomViewHolder) holder).txtDetail.setText("");
                        } else {
                            ((CustomViewHolder) holder).txtDetail.setText(Html.fromHtml(items[1].toString()));
                        }
                        // Amount
                        if (items[2] != null && items[2].isEmpty()) {
                            ((CustomViewHolder) holder).txtAmount.setText("");
                        } else {
                            ((CustomViewHolder) holder).txtAmount.setText(Html.fromHtml(items[2].toString()));
                        }
                        ((CustomViewHolder) holder).txtoffer.setVisibility(View.GONE);
                    }

                    Animation animation = AnimationUtils.loadAnimation(mContext,
                            (position > lastPosition) ? R.anim.slide_in_right
                                    : R.anim.slide_in_left);
                    holder.itemView.startAnimation(animation);
                    lastPosition = position;

                }
            }
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDate, txtDetail, txtAmount, txtoffer;
        private View llMain;

        private CustomViewHolder(View view) {
            super(view);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtDetail = (TextView) view.findViewById(R.id.txtDetail);
            txtAmount = (TextView) view.findViewById(R.id.txtAmount);
            txtoffer = (TextView) view.findViewById(R.id.offerview);
            llMain = (View) view.findViewById(R.id.llMain);
        }
    }


}



