package com.expedite.apps.kumkum.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.adapter.SubjectWiseHWAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.google.android.gms.ads.AdView;

import java.util.List;

public class SubjectWiseHWNotificationActivity extends BaseActivity {

    private String[] sub_name = null, sub_hw_text = null;
    private ListView ItmView;
    private TextView tvhw_date;
    private SubjectWiseHWAdapter subadap;
    private String moduleid = "0", yearid = "", classsectionname = "",isNotification="";
    private int SchoolId, studentid;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_wise_hwnotification);
        init();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);

            ItmView = (ListView) findViewById(R.id.lsthomework);
            tvhw_date = (TextView) findViewById(R.id.txthwdate);
            Intent intnt = getIntent();
            String message = intnt.getStringExtra("MSG");
            String date = intnt.getStringExtra("date");
            moduleid = intnt.getStringExtra("ModuleId");
            isNotification= intnt.getStringExtra("IsNotification");
            if (moduleid.equals("15")) {
                Constants.setActionbar(getSupportActionBar(), SubjectWiseHWNotificationActivity.this,
                        SubjectWiseHWNotificationActivity.this, "Food Infraction",
                        "Food Infraction", ActivityNames.SubjectWiseHWNotificationActivity_Food_Infraction);
                Constants.googleAnalytic(SubjectWiseHWNotificationActivity.this,
                        "Food Infraction");
                showBannerAd(mAdView, ActivityNames.SubjectWiseHWNotificationActivity_Food_Infraction);
            } else if (moduleid.equals("16")) {
                Constants.setActionbar(getSupportActionBar(), SubjectWiseHWNotificationActivity.this,
                        SubjectWiseHWNotificationActivity.this, "Hygiene", "Hygiene", ActivityNames.SubjectWiseHWNotificationActivity_Hygiene);
                Constants.googleAnalytic(SubjectWiseHWNotificationActivity.this,
                        "Hygiene");
                showBannerAd(mAdView, ActivityNames.SubjectWiseHWNotificationActivity_Hygiene);
            } else if (moduleid.equals("17")) {
                Constants.setActionbar(getSupportActionBar(), SubjectWiseHWNotificationActivity.this,
                        SubjectWiseHWNotificationActivity.this, "Remakrs", "Remarks", ActivityNames.SubjectWiseHWNotificationActivity_Remarks);
                Constants.googleAnalytic(SubjectWiseHWNotificationActivity.this,
                        "Remarks");
                showBannerAd(mAdView, ActivityNames.SubjectWiseHWNotificationActivity_Remarks);
            } else {
                Constants.setActionbar(getSupportActionBar(), SubjectWiseHWNotificationActivity.this,
                        SubjectWiseHWNotificationActivity.this, "HomeWork", "SubjectWishHWNotification", ActivityNames.SubjectWiseHWNotificationActivity_HomeWork);
                Constants.googleAnalytic(SubjectWiseHWNotificationActivity.this,
                        "SubjectWishHWNotification");
                showBannerAd(mAdView, ActivityNames.SubjectWiseHWNotificationActivity_HomeWork);
            }

            String studid = "";
            String schoolid = "";
            try {
                studid = intnt.getStringExtra("Studid");
                schoolid = intnt.getStringExtra("Schoolid");
            } catch (Exception ex) {
                Constants.Logwrite("SubjectwiseHWNotdoneNotification:63",
                        "Exception" + ex.getMessage() + ":::::"
                                + ex.getStackTrace());
                Constants.writelog(
                        "SubjectwiseHWNotdoneNotification:63",
                        "Exception" + ex.getMessage() + ":::::"
                                + ex.getStackTrace());
            }
            String[] orimsg = message.split("@@##HWO@@##");
            if (orimsg.length > 1) {
                message = orimsg[1].toString();
            } else {
                message = orimsg[0].toString();
            }
            String[] spltr = message.split("@#@#@#");
            if (spltr.length > 0) {
                sub_name = new String[spltr.length];
                sub_hw_text = new String[spltr.length];
                int incr = 0;
                for (String hwstr : spltr) {
                    String[] strinrsplt = hwstr.split("@@##HW@@##");
                    if (moduleid.equals("15") || moduleid.equals("17")) {
                        sub_name[incr] = strinrsplt[0].toString();
                        sub_hw_text[incr] = "";
                    } else {
                        sub_name[incr] = strinrsplt[1].toString();
                        sub_hw_text[incr] = strinrsplt[2].toString();
                    }
                    incr++;
                }
                // tvhw_date.setText("HW:" + HW_Date);

                if (date == null || date.equalsIgnoreCase("")) {
                    if (moduleid != null && moduleid.equals("12")) {
                        tvhw_date.setText("Today's Uniform Infraction");
                    } else if (moduleid != null && moduleid.equals("15")) {
                        tvhw_date.setText("Today's Food Infraction");
                    } else if (moduleid != null && moduleid.equals("16")) {
                        tvhw_date.setText("Today's Hygiene");
                    } else if (moduleid != null && moduleid.equals("17")) {
                        tvhw_date.setText("Today's Remarks");
                    } else {
                        tvhw_date.setText("Today's HW");
                    }
                } else {
                    if (moduleid != null && moduleid.equals("12")) {
                        tvhw_date.setText("Uniform Infraction:" + date);
                    } else {
                        tvhw_date.setText("HW:" + date);
                    }
                }

                // Jaydeep 17-8-2015
                // For display class section name
                try {
                    DatabaseHandler db = new DatabaseHandler(
                            SubjectWiseHWNotificationActivity.this);

                    if (studid != null && studid != "" && schoolid != null
                            && schoolid != "") {
                        studentid = Integer.parseInt(studid);
                        SchoolId = Integer.parseInt(schoolid);
                    } else {
                        studentid = Integer.parseInt(Datastorage.GetStudentId(SubjectWiseHWNotificationActivity.this));
                        SchoolId = Integer.parseInt(Datastorage.GetSchoolId(SubjectWiseHWNotificationActivity.this));
                    }
                    if (studentid == 0 || SchoolId == 0) {
                        studentid = Integer.parseInt(Datastorage
                                .GetStudentId(SubjectWiseHWNotificationActivity.this));
                        SchoolId = Integer.parseInt(Datastorage
                                .GetSchoolId(SubjectWiseHWNotificationActivity.this));
                    }
                    //String classsecname = db.getClassSectionName(student_id,School_Id);

                    yearid = Datastorage.GetCurrentYearId(SubjectWiseHWNotificationActivity.this);
                    classsectionname = db.getClassSectionNameFromProfile(
                            studentid, SchoolId, yearid);
                    if (classsectionname.equalsIgnoreCase("")
                            || classsectionname.equalsIgnoreCase(" ")) {
                        new MyTask().execute();
                        // classsecname =
                        // db.getClassSectionName(studentid,SchoolId);

                    }
                    if (classsectionname != null && classsectionname != ""
                            && classsectionname != " ") {
                        try {
                            String parts[] = classsectionname.split(":");
                            classsectionname = parts[1].toString();
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                    String strdate = classsectionname + " "
                            + tvhw_date.getText();
                    tvhw_date.setText(strdate);
                } catch (Exception ex) {
                    Constants.Logwrite("SubjectwiseHWNotification",
                            "Exception classSectionName 86:" + ex.getMessage()
                                    + "::::" + ex.getStackTrace());
                    Constants.writelog("SubjectwiseHWNotification",
                            "Exception classSectionName 86:" + ex.getMessage()
                                    + "::::" + ex.getStackTrace());
                }
                if (sub_name != null && sub_name.length > 0) {
                    subadap = new SubjectWiseHWAdapter(SubjectWiseHWNotificationActivity.this,
                            sub_name, sub_hw_text, moduleid);
                    ItmView.setAdapter(subadap);
                }
            }
        } catch (Exception err) {
            Constants.Logwrite("SWHNT", "Method Name:Oncretae 208:" + err.getMessage()
                    + "StackTrace::" + err.getStackTrace().toString());
            Constants.writelog("SWHNT",
                    "Method Name:Oncretae 208:" + err.getMessage()
                            + "StackTrace::" + err.getStackTrace().toString());
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Constants.UpdateStudentprofile(SubjectWiseHWNotificationActivity.this,
                        studentid, SchoolId, yearid, db);
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
                classsectionname = db.getClassSectionNameFromProfile(studentid,
                        SchoolId, yearid);
                if (classsectionname != null && classsectionname != ""
                        && classsectionname != " ") {
                    try {
                        String parts[] = classsectionname.split(":");
                        classsectionname = parts[1].toString();
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
                String strdate = classsectionname + " "
                        + tvhw_date.getText().toString().trim();
                tvhw_date.setText(strdate);
            } catch (Exception ex) {

            }
        }
    }

    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (item.getItemId() == android.R.id.home) {
                hideKeyboard(SubjectWiseHWNotificationActivity.this);
                ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
                if (taskList.get(0).numActivities == 1 &&
                        taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
                    SubjectWiseHWNotificationActivity.this.finish();
                }else{
                    Intent intent = new Intent(SubjectWiseHWNotificationActivity.this, DailyDiaryCalanderActivity.class);
                    if (moduleid != null && moduleid.equals("12")) {
                        intent.putExtra("msgtype", Constants.HW_UNIFORMNOTPROPER);
                    } else if (moduleid != null && moduleid.equals("15")) {
                        intent.putExtra("msgtype", Constants.FOOD);
                    } else if (moduleid != null && moduleid.equals("16")) {
                        intent.putExtra("msgtype", Constants.HYGIENE);
                    } else if (moduleid != null && moduleid.equals("17")) {
                        intent.putExtra("msgtype", Constants.REMARKS);
                    } else {
                        intent.putExtra("msgtype", Constants.HW_HOMEWORK);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
                onBackClickAnimation();
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent=null;
        if (isNotification != null && !isNotification.isEmpty()) {
            super.onBackPressed();
            intent = new Intent(SubjectWiseHWNotificationActivity.this, HomeActivity.class);

        } else {
             intent = new Intent(SubjectWiseHWNotificationActivity.this, DailyDiaryCalanderActivity.class);
            if (moduleid != null && moduleid.equals("12")) {
                intent.putExtra("msgtype", Constants.HW_UNIFORMNOTPROPER);
            } else if (moduleid != null && moduleid.equals("15")) {
                intent.putExtra("msgtype", Constants.FOOD);
            } else if (moduleid != null && moduleid.equals("16")) {
                intent.putExtra("msgtype", Constants.HYGIENE);
            } else if (moduleid != null && moduleid.equals("17")) {
                intent.putExtra("msgtype", Constants.REMARKS);
            } else {
                intent.putExtra("msgtype", Constants.HW_HOMEWORK);
            }

        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
        onBackClickAnimation();
        super.onBackPressed();
    }

}
