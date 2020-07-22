package com.expedite.apps.kumkum.adapter;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.AppointmentListModal;

import java.util.ArrayList;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;

public class AppointmentListAdapter extends RecyclerView.Adapter {
    private List<AppointmentListModal.Getappoinment> mList = new ArrayList<>();
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private String Tag = "LeaveListAdapter";


    public AppointmentListAdapter(Activity context, List<AppointmentListModal.Getappoinment> ItemList) {
        mList = ItemList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.appointment_list_row_layout, parent, false);
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
                final AppointmentListModal.Getappoinment tmp = mList.get(position);


                if (tmp.getAppointmentTakenDate() != null && !tmp.getAppointmentTakenDate().isEmpty()) {
                    ((CustomViewHolder) holder).txtApplicationDate.setText
                            (Html.fromHtml(tmp.getAppointmentTakenDate().trim()));
                } else {
                    ((CustomViewHolder) holder).txtApplicationDate.setText("");
                }

                if (tmp.getVisitorname() != null && !tmp.getVisitorname().isEmpty()) {
                    ((CustomViewHolder) holder).txtVisitorName.setText
                            (Html.fromHtml(tmp.getVisitorname().trim().toUpperCase()));
                } else {
                    ((CustomViewHolder) holder).txtVisitorName.setText("");
                }


//                if (tmp.getLeaveStatus() != null && !tmp.getLeaveStatus().isEmpty())
//                    ((CustomViewHolder) holder).txtStatus.setText(tmp.getLeaveStatus().trim());

                if (tmp.getTime() != null && !tmp.getTime().isEmpty()) {
                    ((CustomViewHolder) holder).txtAppointmentTime.setText(Html.fromHtml
                            ("<b> Time : </b>" + tmp.getTime().trim()));
                } else {
                    ((CustomViewHolder) holder).txtAppointmentTime.setText("");
                }

                if (tmp.getDate() != null && !tmp.getDate().isEmpty()) {
                    ((CustomViewHolder) holder).txtAppointmentDate.setText(Html.fromHtml
                            ("<b> Date : </b>" + tmp.getDate().trim()));
                } else {
                    ((CustomViewHolder) holder).txtAppointmentDate.setText("");
                }

                if (tmp.getHostname() != null && !tmp.getHostname().isEmpty()) {
                    ((CustomViewHolder) holder).txtAppointmentMeetTo.setText(Html.fromHtml
                            ("<b> Meet to : </b>" + tmp.getHostname().trim()));
                } else {
                    ((CustomViewHolder) holder).txtAppointmentMeetTo.setText("");
                }

              /*  if (tmp.getLeaveCount() != null && !tmp.getLeaveCount().isEmpty()) {
                    ((CustomViewHolder) holder).txtNoOfDays.setText(Html.fromHtml(tmp.getLeaveCount().trim()));
                } else {
                    ((CustomViewHolder) holder).txtNoOfDays.setText("");
                }*/
               /* if (tmp.getLeaveType() != null && !tmp.getLeaveType().isEmpty()) {
                    ((CustomViewHolder) holder).txtLeaveType.setText(Html.fromHtml("<b> Leave Type : </b>"
                            + tmp.getLeaveType().trim()));
                } else {
                    ((CustomViewHolder) holder).txtLeaveType.setText("");
                }*/

                if (tmp.getPurposeofvisit() != null && !tmp.getPurposeofvisit().isEmpty()) {
                    ((CustomViewHolder) holder).rlAppointmentPurpose.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).txtAppointmentPurpose.setText(Html.fromHtml("<b> Purpose : </b>"
                            + tmp.getPurposeofvisit().trim()));
                } else {
                    ((CustomViewHolder) holder).txtAppointmentPurpose.setText("");
                    ((CustomViewHolder) holder).rlAppointmentPurpose.setVisibility(View.GONE);

                }


                if (tmp.getAppointmentApprovedByDate() != null && !tmp.getAppointmentApprovedByDate().isEmpty()) {
                    ((CustomViewHolder) holder).txtAppointmentApprovedDate.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).txtAppointmentApprovedDate.setText(Html.fromHtml( tmp.getAppointmentApprovedByDate().trim()));
                } else {
                    ((CustomViewHolder) holder).txtAppointmentApprovedDate.setText("");
                    ((CustomViewHolder) holder).txtAppointmentApprovedDate.setVisibility(View.GONE);

                }

                if (tmp.getAppointmentApprovedByName() != null && !tmp.getAppointmentApprovedByName().isEmpty()) {
                    ((CustomViewHolder) holder).txtAppointmentApprovedBy.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).txtAppointmentApprovedBy.setText(Html.fromHtml( tmp.getAppointmentApprovedByName().trim()));
                } else {
                    ((CustomViewHolder) holder).txtAppointmentApprovedBy.setText("");
                    ((CustomViewHolder) holder).txtAppointmentApprovedBy.setVisibility(View.GONE);

                }
               /* if (tmp.getDocument() != null && !tmp.getDocument().isEmpty()) {
                    ((CustomViewHolder) holder).btnDownload.setVisibility(View.VISIBLE);
                } else {
                    ((CustomViewHolder) holder).btnDownload.setVisibility(View.INVISIBLE);
                }*/

                if (tmp.getAppointmentRemarks() != null && !tmp.getAppointmentRemarks().isEmpty()) {
                    ((CustomViewHolder) holder).rlMainManagerRemark.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).txtManagerRemark.setText(Html.fromHtml("<b> Remarks : </b>"+tmp.getAppointmentRemarks().trim()));
                } else {
                    ((CustomViewHolder) holder).txtManagerRemark.setText("");
                    ((CustomViewHolder) holder).rlMainManagerRemark.setVisibility(View.GONE);
                }

                /*if (tmp.getCordinatorRemarks() != null && !tmp.getCordinatorRemarks().isEmpty()) {
                    ((CustomViewHolder) holder).rlMainCoordinateRemark.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).txtCoordinateRemark.setText(Html.fromHtml(tmp.getCordinatorRemarks().trim()));
                } else {
                    ((CustomViewHolder) holder).txtCoordinateRemark.setText("");
                    ((CustomViewHolder) holder).rlMainCoordinateRemark.setVisibility(View.GONE);
                }*/

                ((CustomViewHolder) holder).btnMoreReason.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (((CustomViewHolder) holder).txtAppointmentPurpose.isExpanded()) {
                            ((CustomViewHolder) holder).txtAppointmentPurpose.collapse();
                            ((CustomViewHolder) holder).btnMoreReason.setText("View More");
                        } else {
                            ((CustomViewHolder) holder).txtAppointmentPurpose.expand();
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
               /* ((CustomViewHolder) holder).btnMoreRemarkCoordinate.setOnClickListener(new View.OnClickListener() {
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
                });*/
                /*((CustomViewHolder) holder).btnDownload.setOnClickListener(new View.OnClickListener() {
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
                });*/


            }
        } catch (Exception ex) {
            Constants.writelog(Tag, "Ex 84:" + ex.getMessage());
        }
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtApplicationDate, txtStatus, txtAppointmentTime, txtLeaveType, txtAppointmentDate, txtDate, txtVisitorName;
        private TextView txtNoOfDays, btnMoreReason, txtAppointmentApprovedBy, txtAppointmentApprovedDate, btnMoreRemarkManager,
                btnMoreRemarkCoordinate, btnDownload, txtAppointmentMeetTo;
        private ExpandableTextView txtLeaveReason, txtAppointmentPurpose, txtManagerRemark, txtCoordinateRemark;
        private View ll_DataView, rlMainManagerRemark, rlMainCoordinateRemark, rlAppointmentPurpose;

        public CustomViewHolder(View view) {
            super(view);
            txtApplicationDate = (TextView) view.findViewById(R.id.txtApplicationDate);
            txtVisitorName = (TextView) view.findViewById(R.id.txtVisitorName);
            //  btnDownload = (TextView) view.findViewById(R.id.btnDownload);
            //    txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            txtAppointmentTime = (TextView) view.findViewById(R.id.txtAppointmentTime);
            txtAppointmentDate = (TextView) view.findViewById(R.id.txtAppointmentDate);
            txtAppointmentApprovedDate = (TextView) view.findViewById(R.id.txtAppointmentApprovedDate);
            txtAppointmentApprovedBy = (TextView) view.findViewById(R.id.txtAppointmentApprovedBy);
            //    txtLeaveType = (TextView) view.findViewById(R.id.txtLeaveType);
            //    txtDate = (TextView) view.findViewById(R.id.txtDate);
            // txtNoOfDays = (TextView) view.findViewById(R.id.txtNoOfDays);
            rlAppointmentPurpose = (View) view.findViewById(R.id.rlAppointmentPurpose);
            txtAppointmentMeetTo = (TextView) view.findViewById(R.id.txtAppointmentMeetTo);
            btnMoreReason = (TextView) view.findViewById(R.id.btnMoreReason);
               rlMainManagerRemark = (View) view.findViewById(R.id.rlMainManagerRemark);
               btnMoreRemarkManager = (TextView) view.findViewById(R.id.btnMoreRemarkManager);
            //    rlMainCoordinateRemark = (View) view.findViewById(R.id.rlMainCoordinateRemark);
            //    btnMoreRemarkCoordinate = (TextView) view.findViewById(R.id.btnMoreRemarkCoordinate);
            //  ll_DataView = (View) view.findViewById(R.id.ll_DataView);

            txtAppointmentPurpose = (ExpandableTextView) view.findViewById(R.id.txtAppointmentPurpose);
            txtAppointmentPurpose.setInterpolator(new OvershootInterpolator());
            txtAppointmentPurpose.setExpandInterpolator(new OvershootInterpolator());
            txtAppointmentPurpose.setCollapseInterpolator(new OvershootInterpolator());
            //  txtLeaveReason = (ExpandableTextView) view.findViewById(R.id.txtLeaveReason);
            // set interpolators for both expanding and collapsing animations
          /*  txtLeaveReason.setInterpolator(new OvershootInterpolator());
            txtLeaveReason.setExpandInterpolator(new OvershootInterpolator());
            txtLeaveReason.setCollapseInterpolator(new OvershootInterpolator());*/
            txtManagerRemark = (ExpandableTextView) view.findViewById(R.id.txtManagerRemark);
            txtManagerRemark.setInterpolator(new OvershootInterpolator());
            txtManagerRemark.setExpandInterpolator(new OvershootInterpolator());
            txtManagerRemark.setCollapseInterpolator(new OvershootInterpolator());
           /* txtCoordinateRemark = (ExpandableTextView) view.findViewById(R.id.txtCoordinateRemark);
            txtCoordinateRemark.setInterpolator(new OvershootInterpolator());
            txtCoordinateRemark.setExpandInterpolator(new OvershootInterpolator());
            txtCoordinateRemark.setCollapseInterpolator(new OvershootInterpolator());*/

        }
    }
}



