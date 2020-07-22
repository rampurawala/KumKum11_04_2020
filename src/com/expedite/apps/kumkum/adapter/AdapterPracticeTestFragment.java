package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.PracticeTestQAActivity;
import com.expedite.apps.kumkum.activity.PracticeTestResultActivity;
import com.expedite.apps.kumkum.activity.ProfileActivity;
import com.expedite.apps.kumkum.common.CustomProgressBar;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.common.ProgressItem;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.fragment.PracticeTestFragment;
import com.expedite.apps.kumkum.model.AppService;
import com.expedite.apps.kumkum.model.Contact;
import com.google.android.material.chip.Chip;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterPracticeTestFragment extends RecyclerView.Adapter<AdapterPracticeTestFragment.ItemViewHolder> {
    Context context;
    List<AppService.ListArray> itemModels;
    int flag, per = 0;
    String msg = "", tag = "AdapterPracticeTestFragment", ansString = "";
    boolean canStart = false;
    DatabaseHandler db;
    int testrowid = 0;
    ProgressBar pg;
    private ProgressItem mProgressItem;

    public AdapterPracticeTestFragment(Context context, List<AppService.ListArray> itemModels, int flag, ProgressBar pg) {
        this.context = context;
        this.itemModels = itemModels;
        this.flag = flag;
        db = new DatabaseHandler(context);
        this.pg = pg;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_practice_test_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        try {
            holder.testName.setVisibility(View.GONE);
            holder.msg.setVisibility(View.GONE);
            holder.cardTest.setVisibility(View.GONE);
            /* holder.date.setText(itemModels.getDate());*/
            /*holder.img.setImageResource(itemModal.getImage());*/
            if (flag == 1 && (itemModels.get(position).getThird().equals("1") || itemModels.get(position).getThird().equals("2"))) {
                holder.cardTest.setVisibility(View.VISIBLE);
                holder.testName.setVisibility(View.VISIBLE);
                holder.testName.setText(itemModels.get(position).getSecond());
                /* holder.msgLayout.setVisibility(View.VISIBLE);*/
//            holder.marksLayout.setVisibility(View.VISIBLE);
           /*  String[] parts = itemModels.get(position).getFourth().split(",");
            if (parts.length > 0) {
                holder.marks.setVisibility(View.VISIBLE);
                holder.outOf.setText(parts[1]);
                holder.get.setText(parts[0]);
            }else {
                holder.marks.setVisibility(View.GONE);
                holder.msg_marks.setText(itemModels.get(position).getFourth());
            }
         holder.msg.setText(itemModels.get(position).getFourth());*/
                if (itemModels.get(position).getFourth().isEmpty() && itemModels.get(position).getFifth().isEmpty()) {
                    holder.msg.setVisibility(View.GONE);
                } else {
                    if (itemModels.get(position).getFourth() != null && !itemModels.get(position).getFourth().isEmpty()) {
                        msg = itemModels.get(position).getFourth() + "  ";
                    }
                    if (itemModels.get(position).getFifth() != null && !itemModels.get(position).getFifth().isEmpty()) {
                        msg += itemModels.get(position).getFifth();
                    }
                    holder.msg.setVisibility(View.VISIBLE);
                    holder.msg.setText(msg);
                }
            }
            if (itemModels.get(position).isTestExpired() == true || itemModels.get(position).isTestTimeOut() == true || itemModels.get(position).getThird().equals("2") || itemModels.get(position).isTestStarted() == true || itemModels.get(position).getThird().equals("1")) {
                holder.status.setVisibility(View.VISIBLE);
                if (itemModels.get(position).getThird().equals("2") && itemModels.get(position).isTestExpired() == true) {
                    //  holder.status.setBackground(context.getResources().getDrawable(R.drawable.practice_test_expired));
                    holder.status.setChipBackgroundColor(ColorStateList.valueOf(context.getResources().getColor(R.color.material_red_red)));
                    holder.status.setText("Expired");
                } else if (itemModels.get(position).getThird().equals("1") && itemModels.get(position).isTestStarted() == true) {
                    // holder.status.setBackground(context.getResources().getDrawable(R.drawable.practice_test_half_done));
                    holder.status.setChipBackgroundColor(ColorStateList.valueOf(context.getResources().getColor(R.color.material_green)));
                    holder.status.setText("Started");
                } else if (itemModels.get(position).getThird().equals("1") && itemModels.get(position).getTwelth() != null && itemModels.get(position).getTwelth().equalsIgnoreCase("1")) {
                    holder.status.setChipBackgroundColor(ColorStateList.valueOf(context.getResources().getColor(R.color.material_violet)));
                    holder.status.setText("Upcoming");
                } else {
                    holder.status.setVisibility(View.GONE);
                }/* else if (itemModels.get(position).isTestTimeOut() == true) {
                    holder.status.setBackground(context.getResources().getDrawable(R.drawable.practice_test_half_done));
                    holder.status.setText("Half Done");
                } else {
                    holder.status.setBackground(context.getResources().getDrawable(R.drawable.practice_test_complete));
                    holder.status.setText("Complete");
                }*/

            } else {
                holder.status.setVisibility(View.GONE);
            }


            if (flag == 0 && (itemModels.get(position).getThird().equals("0") || itemModels.get(position).getThird().equals("2"))) {
                holder.cardTest.setVisibility(View.VISIBLE);
                holder.testName.setVisibility(View.VISIBLE);
                holder.testName.setText(itemModels.get(position).getSecond());
           /* holder.msgLayout.setVisibility(View.GONE);
            holder.marksLayout.setVisibility(View.VISIBLE);
            if (itemModels.get(position).getFourth().isEmpty()) {
                holder.marksLayout.setVisibility(View.GONE);
            } else {
                String[] parts = itemModels.get(position).getFourth().split(",");
                if (parts.length > 1) {
                    holder.marks.setVisibility(View.VISIBLE);
                    holder.outOf.setText(parts[1]);
                    holder.get.setText(parts[0]);
                } else {
                    holder.marks.setVisibility(View.GONE);
                    holder.msg_marks.setText(itemModels.get(position).getFourth());
                }
            }
            if (itemModels.get(position).getFourth().isEmpty())
                holder.marks.setVisibility(View.GONE);
            if (!itemModels.get(position).getFifth().isEmpty())
                holder.msg_marks.setText(itemModels.get(position).getFifth());*/
                if (itemModels.get(position).getFourth().isEmpty() && itemModels.get(position).getFifth().isEmpty()) {
                    holder.msg.setVisibility(View.GONE);
                    holder.progressCompleted.setVisibility(View.GONE);
                } else {
                    if (itemModels.get(position).getFourth() != null && !itemModels.get(position).getFourth().isEmpty()) {
                        msg = itemModels.get(position).getFourth() + "  ";
                        try {
                            String[] markSplit = itemModels.get(position).getFourth().split("/");
                            if (markSplit != null && markSplit.length > 1) {
                                holder.progressCompleted.setVisibility(View.VISIBLE);
                                per = (Integer.parseInt(markSplit[0]) * 100) / Integer.parseInt(markSplit[1]);
                                holder.progressCompleted.getThumb().mutate().setAlpha(0);
                                ArrayList progressItemList = new ArrayList();
                                // red span
                                mProgressItem = new ProgressItem();
                                mProgressItem.progressItemPercentage = per;
                                Log.i("Mainactivity", mProgressItem.progressItemPercentage + "");
                                mProgressItem.color = R.color.green;
                                progressItemList.add(mProgressItem);
                                int progress = 100 - per;
                                mProgressItem = new ProgressItem();
                                mProgressItem.progressItemPercentage = progress;
                                Log.i("Mainactivity", mProgressItem.progressItemPercentage + "");
                                mProgressItem.color = R.color.materialLightGreen;
                                progressItemList.add(mProgressItem);
                                holder.progressCompleted.initData(progressItemList);
                                holder.progressCompleted.invalidate();
                            }
                        } catch (Exception e) {
                            holder.progressCompleted.setVisibility(View.GONE);
                            Constants.writelog("AdapterPracticeTest", "onBindViewHolder 194:" + e.getMessage());
                        }
                    }
                    if (itemModels.get(position).getFifth() != null && !itemModels.get(position).getFifth().isEmpty()) {
                        msg += itemModels.get(position).getFifth();
                    }
                    holder.msg.setVisibility(View.VISIBLE);
                    holder.msg.setText(msg);
                }
            }

            //  holder.date.setText(itemModels.get(position).getMdate());
            // Picasso.with((context)).load(itemModels.get(position).getUProfilePic()).placeholder(R.drawable.person_default_theme).into(holder.img);

            if (itemModels.get(position).getThird().equals("2") && itemModels.get(position).isShowUploadBtn() == true) {
                holder.uploadData.setBackground(context.getResources().getDrawable(R.drawable.practice_test_complete));
                holder.uploadData.setText("Submit Test");
                holder.uploadData.setVisibility(View.VISIBLE);
            } else {
                holder.uploadData.setText("");
                holder.uploadData.setVisibility(View.GONE);
            }

            if (itemModels.get(position).getTenth() != null && !itemModels.get(position).getTenth().equalsIgnoreCase("")) {
                holder.mTxtStartTime.setText(Html.fromHtml("<b> Start Time : </b>" + itemModels.get(position).getFourteen()));
                holder.mTxtStartTime.setVisibility(View.VISIBLE);
            } else {
                holder.mTxtStartTime.setText("");
                holder.mTxtStartTime.setVisibility(View.GONE);
            }

            if (itemModels.get(position).getSixth() != null && !itemModels.get(position).getSixth().equalsIgnoreCase("")) {
                holder.mTxtEndTime.setText(Html.fromHtml("<b> End Time : </b>" + itemModels.get(position).getThirteen()));
                holder.mTxtEndTime.setVisibility(View.VISIBLE);
            } else {
                holder.mTxtEndTime.setText("");
                holder.mTxtEndTime.setVisibility(View.GONE);
            }
            holder.uploadData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitTestDate(itemModels.get(position).getFirst(), holder.uploadData, position);
                }
            });
            holder.cardTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((BaseActivity) context).isOnline()) {
                        if (flag != 0 && !itemModels.get(position).getThird().equals("0") && !itemModels.get(position).getThird().equals("2")) {
                            /*if(itemModels.get(position).getTwelth()!=null && itemModels.get(position).getTwelth().equalsIgnoreCase("1")){
                                String value="";
                                if(itemModels.get(position).getTenth()!=null && !itemModels.get(position).getTenth().isEmpty()){
                                    value=itemModels.get(position).getTenth();
                                }
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                alertDialog.setTitle(context.getString(R.string.title_testnot_started));
                                alertDialog.setMessage(context.getString(R.string.msg_testnot_started,value));
                                alertDialog.setIcon(R.drawable.information);
                                alertDialog.setCancelable(false);
                                alertDialog.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                return;
                                            }
                                        });
                                alertDialog.show();
                            }else {*/
                            Intent i = new Intent(context, PracticeTestQAActivity.class);
                            i.putExtra("testId", itemModels.get(position).getFirst());
                            i.putExtra("testName", itemModels.get(position).getSecond());
                            i.putExtra("forExpiretestafterstart", itemModels.get(position).getSeventh());
                            i.putExtra("testExpireDate", itemModels.get(position).getSixth());
                            i.putExtra("testStartDate", itemModels.get(position).getTenth());
                            i.putExtra("testStartDate12Hour", itemModels.get(position).getFourteen());
                            i.putExtra("testEndDate12Hour", itemModels.get(position).getThirteen());
                            i.putExtra("flag", "1");
                            context.startActivity(i);
                            ((BaseActivity) context).onClickAnimation();
                            /* }
                             */
                        } else {
                            if (itemModels.get(position).isShowUploadBtn() == true) {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                alertDialog.setTitle(context.getString(R.string.title_TestSubmit_incomplete));
                                alertDialog.setMessage(context.getString(R.string.msg_TestSubmit_incomplete));
                                alertDialog.setIcon(R.drawable.information);
                                alertDialog.setCancelable(false);
                                alertDialog.setPositiveButton("Submit",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                submitTestDate(itemModels.get(position).getFirst(), holder.uploadData, position);
                                            }
                                        });
                                alertDialog.show();
                                //   AlertDialog dialog = alertDialog.create();
                                // Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                //  positiveButton.setTextColor(context.getResources().getColor(R.color.grayshade));
                                //   positiveButton.setBackgroundColor(Color.parseColor("#FFE1FCEA"));
                                //   positiveButton.setBackgroundColor(context.getResources().getColor(R.color.material_green));
                            } else {
                                Intent i = new Intent(context, PracticeTestResultActivity.class);
                                i.putExtra("testId", itemModels.get(position).getFirst());
                                i.putExtra("testName", itemModels.get(position).getSecond());
                                i.putExtra("istestExpire", itemModels.get(position).isTestExpired());
                                i.putExtra("viewReview", itemModels.get(position).getEleventh());
                                //    i.putExtra("refereshflag",refreshFlag );
                                i.putExtra("flag", "0");
                                context.startActivity(i);
                                ((BaseActivity) context).onClickAnimation();
                            }
                        }
                    } else {
                        Toast.makeText(context, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
                    }
                    /* int message_id=Integer.parseInt(itemModels.get(position).getMessageMasterId());*//*
                i.putExtra("senderName", itemModels.get(position).getUName());
                i.putExtra("assignmentTitle", itemModels.get(position).getMessageTitle());
                i.putExtra("profilePic", itemModels.get(position).getUProfilePic());
                i.putExtra("message_id", itemModels.get(position).getMessageMasterId());
                context.startActivity(i);*/
                    //Toast.makeText(context, itemModal.getName(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception ex) {
            Constants.writelog("AdapterPracticeTest", "onBindViewHolder 135:" + ex.getMessage());
        }
    }

    private void submitTestDate(String testId, final TextView uploadBtn, final int position) {
        pg.setVisibility(View.VISIBLE);
        String testTicks = "", extraParam = "3000";
        testrowid = 0;
        ansString = "";

        final String mStudentId = Datastorage.GetStudentId(context);
        final String mSchoolId = Datastorage.GetSchoolId(context);

        List<Contact> testDetails = db.getPracticeTestDetailByID(Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId), Integer.parseInt(testId));
        if (testDetails != null && testDetails.size() > 0) {
            ansString = testDetails.get(0).getTestAnsString();
            testrowid = testDetails.get(0).getROWID();
            testTicks = testDetails.get(0).getTestTicks();
        }
        if (ansString == null || ansString.isEmpty()) {
            ansString = "";
            extraParam = "-1";
        }
        if (testTicks == null || testTicks.isEmpty()) {
            testTicks = "";
        }

        String mYearId = Datastorage.GetCurrentYearId(context);
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String mDeviceDetail = Build.DEVICE + "|||" + Build.MODEL + "|||" + Build.ID
                + "|||" + Build.PRODUCT + "|||" + Build.VERSION.SDK
                + "|||" + Build.VERSION.RELEASE + "|||" + Build.VERSION.INCREMENTAL;
        Call<AppService> call = ((MyApplication) context.getApplicationContext()).getmRetrofitInterfaceAppService()
                .SetPracticeTestAnsV3(mStudentId, mSchoolId, mYearId, testId, ansString, SchoolDetails.appname + "", Constants.CODEVERSION, Constants.PLATFORM, extraParam, testTicks, deviceId, mDeviceDetail);
        call.enqueue(new Callback<AppService>() {
            @Override
            public void onResponse(Call<AppService> call, Response<AppService> response) {
                try {
                    AppService tmps = response.body();
                    if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                            && tmps.getResponse().equalsIgnoreCase("1") && !tmps.getStrResult().isEmpty()) {
                        db.deleteTestRecords(testrowid);
                        itemModels.get(position).setShowUploadBtn(false);
                        uploadBtn.setVisibility(View.GONE);
                        pg.setVisibility(View.GONE);
                        AdapterPracticeTestFragment.this.notifyDataSetChanged();
                        Toast.makeText(context, tmps.getStrResult(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, tmps.getStrResult(), Toast.LENGTH_SHORT).show();
                        pg.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                    pg.setVisibility(View.GONE);
                    Constants.writelog(tag, "setTestAns 642:" + ex.getMessage());
                } finally {
                    ansString = "";
                }
            }

            @Override
            public void onFailure(Call<AppService> call, Throwable t) {
                pg.setVisibility(View.GONE);
                Constants.writelog(tag, "setTestAns 650:" + t.getMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        if (itemModels == null)
            return 0;
        else return itemModels.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView /*get, outOf,*/ msg, testName/*, msg_marks*/, /*status,*/
                mTxtStartTime, mTxtEndTime, uploadData;
        CustomProgressBar progressCompleted;
        LinearLayout /*marksLayout, msgLayout,*/ cardTest/*, marks*/;
        Chip status;

        public ItemViewHolder(View itemView) {
            super(itemView);
            //  get = itemView.findViewById(R.id.get);
            //  outOf = itemView.findViewById(R.id.outOf);
            //  marksLayout = itemView.findViewById(R.id.marksLayout);
            msg = itemView.findViewById(R.id.msg);
            status = itemView.findViewById(R.id.status);
            uploadData = itemView.findViewById(R.id.uploadData);
            //  msgLayout = itemView.findViewById(R.id.msgLayout);
            testName = itemView.findViewById(R.id.testName);
            progressCompleted = itemView.findViewById(R.id.progressCompleted);
            // msg_marks = itemView.findViewById(R.id.msg_marks);
            cardTest = itemView.findViewById(R.id.cardTest);
            mTxtStartTime = itemView.findViewById(R.id.startTime);
            mTxtEndTime = itemView.findViewById(R.id.endTime);
            // marks = itemView.findViewById(R.id.marks);
        }
    }
}
