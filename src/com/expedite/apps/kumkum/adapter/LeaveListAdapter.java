package com.expedite.apps.kumkum.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.CurriculumImageViewActivity;
import com.expedite.apps.kumkum.activity.CurriculumPdfViewActivity;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.LeaveListModel;

import java.util.ArrayList;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;


/**
 * Created by tejas patel on 31/07/2018.
 */
public class LeaveListAdapter extends RecyclerView.Adapter {
    private List<LeaveListModel.LeaveHistory> mList = new ArrayList<>();
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private String Tag = "LeaveListAdapter";


    public LeaveListAdapter(Activity context, List<LeaveListModel.LeaveHistory> ItemList) {
        mList = ItemList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.leave_list_row_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            if (holder instanceof CustomViewHolder) {
                final LeaveListModel.LeaveHistory tmp = mList.get(position);


                if (tmp.getApplicationDate() != null && !tmp.getApplicationDate().isEmpty()) {
                    ((CustomViewHolder) holder).txtApplicationDate.setText
                            (Html.fromHtml(tmp.getApplicationDate().trim()));
                } else {
                    ((CustomViewHolder) holder).txtApplicationDate.setText("");
                }

//                if (tmp.getLeaveStatus() != null && !tmp.getLeaveStatus().isEmpty())
//                    ((CustomViewHolder) holder).txtStatus.setText(tmp.getLeaveStatus().trim());

                if (tmp.getLeaveDate() != null && !tmp.getLeaveDate().isEmpty()) {
                    ((CustomViewHolder) holder).txtLeaveTime.setText(Html.fromHtml
                            ("<b> Leave From : </b>" + tmp.getLeaveDate().trim()));
                } else {
                    ((CustomViewHolder) holder).txtLeaveTime.setText("");
                }
                if (tmp.getLeaveCount() != null && !tmp.getLeaveCount().isEmpty()) {
                    ((CustomViewHolder) holder).txtNoOfDays.setText(Html.fromHtml(tmp.getLeaveCount().trim()));
                } else {
                    ((CustomViewHolder) holder).txtNoOfDays.setText("");
                }
                if (tmp.getLeaveType() != null && !tmp.getLeaveType().isEmpty()) {
                    ((CustomViewHolder) holder).txtLeaveType.setText(Html.fromHtml("<b> Leave Type : </b>"
                            + tmp.getLeaveType().trim()));
                } else {
                    ((CustomViewHolder) holder).txtLeaveType.setText("");
                }

                if (tmp.getLeaverason() != null && !tmp.getLeaverason().isEmpty()) {
                    ((CustomViewHolder) holder).rlMainReason.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).txtLeaveReason.setText(Html.fromHtml("<b> Leave Reason : </b>"
                            + tmp.getLeaverason().trim()));
                } else {
                    ((CustomViewHolder) holder).txtLeaveReason.setText("");
                    ((CustomViewHolder) holder).rlMainReason.setVisibility(View.GONE);

                }

                if (tmp.getApprovedDate() != null && !tmp.getApprovedDate().isEmpty()) {
                    ((CustomViewHolder) holder).txtDate.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).txtDate.setText(Html.fromHtml(tmp.getApprovedDate().trim()));
                } else {
                    ((CustomViewHolder) holder).txtDate.setText("");
                    ((CustomViewHolder) holder).txtDate.setVisibility(View.GONE);

                }

                if (tmp.getApprovedBy() != null && !tmp.getApprovedBy().isEmpty()) {
                    ((CustomViewHolder) holder).txtApprovedBy.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).txtApprovedBy.setText(Html.fromHtml(tmp.getApprovedBy().trim()));
                } else {
                    ((CustomViewHolder) holder).txtApprovedBy.setText("");
                    ((CustomViewHolder) holder).txtApprovedBy.setVisibility(View.GONE);

                }
                if (tmp.getDocument() != null && !tmp.getDocument().isEmpty()) {
                    ((CustomViewHolder) holder).btnDownload.setVisibility(View.VISIBLE);
                } else {
                    ((CustomViewHolder) holder).btnDownload.setVisibility(View.INVISIBLE);
                }

                if (tmp.getManagerRemarks() != null && !tmp.getManagerRemarks().isEmpty()) {
                    ((CustomViewHolder) holder).rlMainManagerRemark.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).txtManagerRemark.setText(Html.fromHtml(tmp.getManagerRemarks().trim()));
                } else {
                    ((CustomViewHolder) holder).txtManagerRemark.setText("");
                    ((CustomViewHolder) holder).rlMainManagerRemark.setVisibility(View.GONE);

                }

                if (tmp.getCordinatorRemarks() != null && !tmp.getCordinatorRemarks().isEmpty()) {
                    ((CustomViewHolder) holder).rlMainCoordinateRemark.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).txtCoordinateRemark.setText(Html.fromHtml(tmp.getCordinatorRemarks().trim()));
                } else {
                    ((CustomViewHolder) holder).txtCoordinateRemark.setText("");
                    ((CustomViewHolder) holder).rlMainCoordinateRemark.setVisibility(View.GONE);
                }

                ((CustomViewHolder) holder).btnMoreReason.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (((CustomViewHolder) holder).txtLeaveReason.isExpanded()) {
                            ((CustomViewHolder) holder).txtLeaveReason.collapse();
                            ((CustomViewHolder) holder).btnMoreReason.setText("View More");
                        } else {
                            ((CustomViewHolder) holder).txtLeaveReason.expand();
                            ((CustomViewHolder) holder).btnMoreReason.setText("View Less");
                        }
                    }
                });
                ((CustomViewHolder) holder).btnMoreRemarkManager.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CustomViewHolder) holder).txtManagerRemark.isExpanded()) {
                            ((CustomViewHolder) holder).txtManagerRemark.collapse();
                            ((CustomViewHolder) holder).btnMoreRemarkManager.setText("View More");
                        } else {
                            ((CustomViewHolder) holder).txtManagerRemark.expand();
                            ((CustomViewHolder) holder).btnMoreRemarkManager.setText("View Less");
                        }

                    }
                });
                ((CustomViewHolder) holder).btnMoreRemarkCoordinate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CustomViewHolder) holder).txtCoordinateRemark.isExpanded()) {
                            ((CustomViewHolder) holder).txtCoordinateRemark.collapse();
                            ((CustomViewHolder) holder).btnMoreRemarkCoordinate.setText("View More");
                        } else {
                            ((CustomViewHolder) holder).txtCoordinateRemark.expand();
                            ((CustomViewHolder) holder).btnMoreRemarkCoordinate.setText("View Less");
                        }

                    }
                });
                ((CustomViewHolder) holder).btnDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tmp.getDocument() != null && !tmp.getDocument().isEmpty()) {
                            try {
                                //1-pdf,2-image jpg or jpeg,3-doc
                                if (tmp.getDocumentType() != null && !tmp.getDocumentType().isEmpty()) {
                                    if (tmp.getDocumentType().equalsIgnoreCase("1")) {
                                        Intent intent = new Intent(mContext, CurriculumPdfViewActivity.class);
                                        intent.putExtra("Url", tmp.getDocument());
                                        intent.putExtra("Name", "Leave Pdf");
                                        mContext.startActivity(intent);
                                        ((BaseActivity) mContext).onClickAnimation();
                                    } else if (tmp.getDocumentType().equalsIgnoreCase("2")) {
                                        Intent intent = new Intent(mContext, CurriculumImageViewActivity.class);
                                        intent.putExtra("Name", "Leave Image");
                                        intent.putExtra("Url", tmp.getDocument());
                                        mContext.startActivity(intent);
                                        ((BaseActivity) mContext).onClickAnimation();
                                    }
                                }
                            } catch (Exception ex) {
                                Constants.writelog(Tag,
                                        "Exception 207:" + ex.getMessage());
                            }
                        }

                    }
                });


            }
        } catch (Exception ex) {
            Constants.writelog(Tag, "Ex 84:" + ex.getMessage());
        }
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtApplicationDate, txtStatus, txtLeaveTime, txtLeaveType, txtDate;
        private TextView txtNoOfDays, btnMoreReason, btnMoreRemarkManager,
                btnMoreRemarkCoordinate, txtApprovedBy;
        private ImageView btnDownload;
        private ExpandableTextView txtLeaveReason, txtManagerRemark, txtCoordinateRemark;
        private View ll_DataView, rlMainReason, rlMainManagerRemark, rlMainCoordinateRemark;

        public CustomViewHolder(View view) {
            super(view);
            txtApplicationDate = (TextView) view.findViewById(R.id.txtApplicationDate);
            btnDownload = (ImageView) view.findViewById(R.id.btnDownload);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            txtLeaveTime = (TextView) view.findViewById(R.id.txtLeaveTime);
            txtLeaveType = (TextView) view.findViewById(R.id.txtLeaveType);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtNoOfDays = (TextView) view.findViewById(R.id.txtNoOfDays);
            rlMainReason = (View) view.findViewById(R.id.rlMainReason);
            //txtLeaveReason = (TextView) view.findViewById(R.id.txtLeaveReason);
            txtApprovedBy = (TextView) view.findViewById(R.id.txtApprovedBy);
            btnMoreReason = (TextView) view.findViewById(R.id.btnMoreReason);
            rlMainManagerRemark = (View) view.findViewById(R.id.rlMainManagerRemark);
            btnMoreRemarkManager = (TextView) view.findViewById(R.id.btnMoreRemarkManager);
            rlMainCoordinateRemark = (View) view.findViewById(R.id.rlMainCoordinateRemark);
            btnMoreRemarkCoordinate = (TextView) view.findViewById(R.id.btnMoreRemarkCoordinate);
            ll_DataView = (View) view.findViewById(R.id.ll_DataView);
            txtLeaveReason = (ExpandableTextView) view.findViewById(R.id.txtLeaveReason);
            // set interpolators for both expanding and collapsing animations
            txtLeaveReason.setInterpolator(new OvershootInterpolator());
            txtLeaveReason.setExpandInterpolator(new OvershootInterpolator());
            txtLeaveReason.setCollapseInterpolator(new OvershootInterpolator());
            txtManagerRemark = (ExpandableTextView) view.findViewById(R.id.txtManagerRemark);
            txtManagerRemark.setInterpolator(new OvershootInterpolator());
            txtManagerRemark.setExpandInterpolator(new OvershootInterpolator());
            txtManagerRemark.setCollapseInterpolator(new OvershootInterpolator());
            txtCoordinateRemark = (ExpandableTextView) view.findViewById(R.id.txtCoordinateRemark);
            txtCoordinateRemark.setInterpolator(new OvershootInterpolator());
            txtCoordinateRemark.setExpandInterpolator(new OvershootInterpolator());
            txtCoordinateRemark.setCollapseInterpolator(new OvershootInterpolator());

        }
    }
}



