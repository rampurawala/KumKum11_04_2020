package com.expedite.apps.kumkum.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.google.android.gms.ads.AdView;


public class SingleMessageActivity extends BaseActivity {
    private TextView txtmsg, tvhw_date, tv;
    private DatabaseHandler db = new DatabaseHandler(this);
    private int SchoolId;
    private int studentid;
    private String yearid = "", classsectionname = "";
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_message);
        init();
    }

    public void init() {

        Constants.setActionbar(getSupportActionBar(), SingleMessageActivity.this,
                SingleMessageActivity.this, "Message", "SingleMessageActivity", ActivityNames.SingleMessageActivity);
        mAdView = (AdView) findViewById(R.id.adView);
        showBannerAd(mAdView, ActivityNames.SingleMessageActivity);
        try {
            tv = (TextView) findViewById(R.id.tvmarkqueetextsiglemessage);
            tv.setSelected(true);
            tv.setText(Datastorage.GetLastUpdatedtime(SingleMessageActivity.this));
            Intent intnt = getIntent();
            txtmsg = (TextView) findViewById(R.id.txtsinglemessage);
            txtmsg.setLinksClickable(true);
            txtmsg.setMovementMethod(LinkMovementMethod.getInstance());
            tvhw_date = (TextView) findViewById(R.id.txtotherdetails);
            String message = intnt.getStringExtra("MSG");
            txtmsg.setText(message);
            String msg_oth_det = intnt.getStringExtra("OTH");
            String islasthwmsg = intnt.getStringExtra("IsLastHomeworkMsg");
            String date = intnt.getStringExtra("date");
            String previousPage = intnt.getStringExtra("previousPage");
            String studid = "";
            String schoolid = "";
            String Display_date = "";

            if (previousPage != null && previousPage.equalsIgnoreCase("DailyDiary")) {
                Display_date = "HW:";
            }
            if (date == null || date.equals("")) {
                tvhw_date.setText(Display_date + " " + msg_oth_det);
            } else {
                tvhw_date.setText(Display_date + " " + date);
            }
            // Jaydeep 17-8-2015
            // For display class section name
            try {
                studid = intnt.getStringExtra("Studid");
                schoolid = intnt.getStringExtra("Schoolid");
            } catch (Exception ex) {
                Constants.writelog("SubjectwiseHWNotdoneNotification:63",
                        "Exception" + ex.getMessage() + ":::::" + ex.getStackTrace());
            }
            try {
                db = new DatabaseHandler(SingleMessageActivity.this);
                studentid = 0;
                SchoolId = 0;

                if (studid != "" && studid != null && schoolid != ""
                        && schoolid != null) {
                    studentid = Integer.parseInt(studid);
                    SchoolId = Integer.parseInt(schoolid);
                } else {
                    studentid = Integer.parseInt(Datastorage.GetStudentId(SingleMessageActivity.this));
                    SchoolId = Integer.parseInt(Datastorage.GetSchoolId(SingleMessageActivity.this));
                }
                if (studentid == 0 || SchoolId == 0) {
                    studentid = Integer.parseInt(Datastorage
                            .GetStudentId(SingleMessageActivity.this));
                    SchoolId = Integer.parseInt(Datastorage
                            .GetSchoolId(SingleMessageActivity.this));
                }
                yearid = Datastorage.GetCurrentYearId(SingleMessageActivity.this);
                classsectionname = db.getClassSectionNameFromProfile(studentid, SchoolId, yearid);

                if (classsectionname.equalsIgnoreCase("")
                        || classsectionname.equalsIgnoreCase(" ")) {
                    new MyTask().execute();

                }
                if (classsectionname != null && classsectionname != ""
                        && classsectionname != " ") {
                    try {
                        String parts[] = classsectionname.split(":");
                        classsectionname = parts[1];
                    } catch (Exception e) {
                        Common.printStackTrace(e);
                    }
                }
                String strdate = classsectionname + " " + tvhw_date.getText();
                tvhw_date.setText(strdate);
            } catch (Exception ex) {
                Constants.Logwrite("SubjectwiseHWNotification",
                        "Exception classSectionName 123:" + ex.getMessage()
                                + "::::" + ex.getStackTrace());
                Constants.writelog("SubjectwiseHWNotification",
                        "Exception classSectionName 123:" + ex.getMessage()
                                + "::::" + ex.getStackTrace());
            }

        } catch (Exception err) {
            Constants.Logwrite("SingleMessageActivity", "msg:" + err.getMessage());
            Constants.writelog("SingleMessageActivity", "Erorr: 71" + err.getMessage() + "  StackTrace:  "
                    + err.getStackTrace());
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Constants.UpdateStudentprofile(SingleMessageActivity.this, studentid, SchoolId, yearid, db);
            } catch (Exception err) {
                Constants.Logwrite("Error", err.getMessage());
                Constants.writelog("SubjectWiseHwDetails",
                        "Exception 144:" + err.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                classsectionname = db.getClassSectionNameFromProfile(studentid, SchoolId, yearid);
                if (classsectionname != null && classsectionname != "" && classsectionname != " ") {
                    try {
                        String parts[] = classsectionname.split(":");
                        classsectionname = parts[1];
                    } catch (Exception e) {
                        Common.printStackTrace(e);
                    }
                }
                String strdate = classsectionname + " " + tvhw_date.getText().toString().trim();
                tvhw_date.setText(strdate);
            } catch (Exception ex) {
                Common.printStackTrace(ex);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackClickAnimation();
        SingleMessageActivity.this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    hideKeyboard(SingleMessageActivity.this);
                    SingleMessageActivity.this.finish();
                    onBackClickAnimation();
                    break;
                default:
                    break;
            }

        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        return super.onOptionsItemSelected(item);
    }
}
