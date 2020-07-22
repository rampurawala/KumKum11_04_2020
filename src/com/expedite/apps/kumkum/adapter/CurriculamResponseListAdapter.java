package com.expedite.apps.kumkum.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.CurriculumImageViewActivity;
import com.expedite.apps.kumkum.activity.CurriculumPdfViewActivity;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Utility;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.model.AppService;
import com.expedite.apps.kumkum.model.VoiceMessageListModel;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CurriculamResponseListAdapter extends RecyclerView.Adapter<CurriculamResponseListAdapter.ViewHolder> {

    private List<AppService.ListArray> mList;
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private String mStrCurriculam;

    public CurriculamResponseListAdapter(Activity activity, List<AppService.ListArray> mList, String mStrCurriculam) {
        this.mContext = activity;
        this.mList = mList;
        this.mStrCurriculam = mStrCurriculam;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.curriculam_response_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AppService.ListArray tmp = mList.get(position);

        if (tmp.getFirst() != null && !tmp.getFirst().equalsIgnoreCase("")) {
            holder.mTxtTitle.setText(tmp.getFirst());
        } else {
            holder.mTxtTitle.setText("Document");
        }

        if (tmp.getSecond() != null && !tmp.getSecond().equalsIgnoreCase("") && !tmp.getSecond().isEmpty()) {

        } else {
            holder.mImgArrow.setVisibility(View.GONE);
        }

        if (tmp.getThird() != null && !tmp.getThird().equalsIgnoreCase("")) {
            holder.mTxtDate.setText(tmp.getThird());
        } else {
            holder.mTxtDate.setVisibility(View.GONE);
        }

        holder.mMaincard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (((BaseActivity) mContext).isOnline()) {

                        if (tmp.getSecond() != null && !tmp.getSecond().equalsIgnoreCase("")
                                && !tmp.getSecond().isEmpty()) {
                            Intent intent = null;
                            if (tmp.getSecond().endsWith(".pdf")) {
                                intent = new Intent(mContext, CurriculumPdfViewActivity.class);
                                intent.putExtra("Url", tmp.getSecond());
                                intent.putExtra("Name", mStrCurriculam);
                               intent.putExtra("isFrom", "CurriculamResponse");
                            } else {
                                intent = new Intent(mContext, CurriculumImageViewActivity.class);
                                intent.putExtra("Name", mStrCurriculam);
                                intent.putExtra("Url", tmp.getSecond());
                            }
                            mContext.startActivity(intent);
                            ((BaseActivity) mContext).onClickAnimation();
                        } else {
                            Common.showToast(mContext, "No File Attached");
                        }

                    } else {
                        Common.showToast(mContext, SchoolDetails.MsgNoInternet);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTxtTitle, mTxtDate;
        private ImageView mImgIcon, mImgArrow;
        private LinearLayout Mainlayout;
        private MaterialCardView mMaincard;

        public ViewHolder(View itemView) {
            super(itemView);
            mTxtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            mTxtDate = (TextView) itemView.findViewById(R.id.Date);
            mImgIcon = (ImageView) itemView.findViewById(R.id.imgfile);
            mImgArrow = (ImageView) itemView.findViewById(R.id.imgArrow);
            Mainlayout = (LinearLayout) itemView.findViewById(R.id.Mainlayout);
            mMaincard = (MaterialCardView) itemView.findViewById(R.id.mainCard);
        }
    }
}
