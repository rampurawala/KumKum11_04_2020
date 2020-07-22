package com.expedite.apps.kumkum.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.CurriculumImageViewActivity;
import com.expedite.apps.kumkum.activity.CurriculumPdfViewActivity;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.model.AppService;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ClassroomResponseListAdapter extends RecyclerView.Adapter<ClassroomResponseListAdapter.ViewHolder> {

    private List<AppService.ListArray> mList;
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private String mStrCurriculam;

    public ClassroomResponseListAdapter(Activity activity, List<AppService.ListArray> mList, String mStrCurriculam) {
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
        holder.ratingReport.setVisibility(View.VISIBLE);
        if (tmp.getSecond() != null && !tmp.getSecond().equalsIgnoreCase("") && !tmp.getSecond().isEmpty()) {

        } else {
            holder.mImgArrow.setVisibility(View.GONE);
        }

        if (tmp.getThird() != null && !tmp.getThird().equalsIgnoreCase("")) {
            holder.mTxtDate.setText(tmp.getThird());
        } else {
            holder.mTxtDate.setVisibility(View.GONE);
        }
        String colorStar = "#000000";
        Drawable progressDrawable = holder.ratingReport.getProgressDrawable();
        Drawable compat = DrawableCompat.wrap(progressDrawable);
        DrawableCompat.setTint(compat, Color.parseColor(colorStar));
        holder.ratingReport.setProgressDrawable(compat);
        if (tmp.getFourth() != null && !tmp.getFourth().equalsIgnoreCase("")) {
            try {
                holder.ratingReport.setVisibility(View.VISIBLE);
                holder.ratingReport.setRating(Float.parseFloat(tmp.getFourth()));

                LayerDrawable stars = (LayerDrawable) holder.ratingReport.getProgressDrawable();
                colorStar = "#2ed193";
                if (tmp.getSixth() != null && !tmp.getSixth().equalsIgnoreCase("")) {
                    colorStar = tmp.getSixth();
                }
                //   stars.getDrawable(2).setColorFilter(Color.parseColor(colorStar), PorterDuff.Mode.SRC_ATOP);
                //changed on 20_07_2020
                progressDrawable = holder.ratingReport.getProgressDrawable();
                compat = DrawableCompat.wrap(progressDrawable);
                DrawableCompat.setTint(compat, Color.parseColor(colorStar));
                holder.ratingReport.setProgressDrawable(compat);

            } catch (Exception ex) {
                holder.ratingReport.setVisibility(View.GONE);
                Constants.writelog("ClassroomResponseListAdapter", "onBindViewHolder::84" + ex.getMessage());
            }
        }

        if (tmp.getFifth() != null && !tmp.getFifth().equalsIgnoreCase("")) {
            holder.mTxtComment.setVisibility(View.VISIBLE);
            holder.mTxtComment.setText(Html.fromHtml("<b> Comments : </b>" + tmp.getFifth()));
        } else {
            holder.mTxtComment.setVisibility(View.GONE);
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
                                //  intent.putExtra("isFrom", "CurriculamResponse");
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

        private TextView mTxtTitle, mTxtDate, mTxtComment;
        private ImageView mImgIcon, mImgArrow;
        private LinearLayout Mainlayout;
        private MaterialCardView mMaincard;
        RatingBar ratingReport;

        public ViewHolder(View itemView) {
            super(itemView);
            mTxtComment = (TextView) itemView.findViewById(R.id.txtComment);
            mTxtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            mTxtDate = (TextView) itemView.findViewById(R.id.Date);
            mImgIcon = (ImageView) itemView.findViewById(R.id.imgfile);
            mImgArrow = (ImageView) itemView.findViewById(R.id.imgArrow);
            Mainlayout = (LinearLayout) itemView.findViewById(R.id.Mainlayout);
            mMaincard = (MaterialCardView) itemView.findViewById(R.id.mainCard);
            ratingReport = itemView.findViewById(R.id.ratingReport);
        }
    }
}
