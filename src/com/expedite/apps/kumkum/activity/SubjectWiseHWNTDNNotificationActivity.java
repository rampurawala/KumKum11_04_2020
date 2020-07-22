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
import com.expedite.apps.kumkum.adapter.SubJectWiseHWNTDNAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class SubjectWiseHWNTDNNotificationActivity extends BaseActivity {
    private TextView tvhw_date;
    private ListView lsthwntdnlistview;
    private List<String> sub_date;
    private ArrayList<String> sub_name, sub_hw_text;
    private SubJectWiseHWNTDNAdapter adapter;
    private int SchoolId, studentid;
    private String yearid = "", classsectionname = "", Is_Notif = "";
    private DatabaseHandler db;
    String isNotification="";
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_wise_hwntdnnotification);
        init();
    }

    public void init() {
        mAdView = (AdView) findViewById(R.id.adView);
        showBannerAd(mAdView, ActivityNames.SubjectWiseHWNTDNNotificationActivity);
        Constants.setActionbar(getSupportActionBar(), SubjectWiseHWNTDNNotificationActivity.this,
                getApplicationContext(), "HomeWork Not Done", "SubjectWiseHwNdNotification", ActivityNames.SubjectWiseHWNTDNNotificationActivity);
        sub_date = new ArrayList<String>();
        sub_hw_text = new ArrayList<String>();
        sub_name = new ArrayList<String>();
        String studid = "";
        String schoolid = "";
        String HW_NTDN_SENT_DATE = "";
        try {
            Is_Notif = getIntent().getStringExtra("IsNotification");
            lsthwntdnlistview = (ListView) findViewById(R.id.lsthwntdn);
            tvhw_date = (TextView) findViewById(R.id.txthwntdndate);
            Intent intnt = getIntent();
            String message = intnt.getStringExtra("MSG");
            String displaydate = intnt.getStringExtra("date");
            String isNotification = intnt.getStringExtra("IsNotification");
            db = new DatabaseHandler(SubjectWiseHWNTDNNotificationActivity.this);

            String[] orimsg = message.split("@@##HWO@@##");
            if (orimsg.length > 1) {
                message = orimsg[1];
            } else {
                message = orimsg[0];
            }
            // String msg =
            String[] spltr = message.split("@#@#@#");
            for (String hwstr : spltr) {
                String[] strinrsplt = hwstr.split("@@##HW@@##");
                if (Is_Notif != null && Is_Notif.equals("0")) {
                    String HW_NT_DATE = displaydate + "";
                    sub_date.add(HW_NT_DATE);
                    String HW_NTDN_SUB = strinrsplt[0];
                    sub_name.add(HW_NTDN_SUB);
                    String HW_NTDN_SUB_TEXT = strinrsplt[1];
                    sub_hw_text.add(HW_NTDN_SUB_TEXT);
                } else {
                    String HW_NT_DATE = strinrsplt[3];
                    sub_date.add(HW_NT_DATE);
                    String HW_NTDN_SUB = strinrsplt[1];
                    sub_name.add(HW_NTDN_SUB);
                    String HW_NTDN_SUB_TEXT = strinrsplt[2];
                    sub_hw_text.add(HW_NTDN_SUB_TEXT);
                    HW_NTDN_SENT_DATE = strinrsplt[4];
                }
                if (tvhw_date == null || tvhw_date.equals("")) {
                    tvhw_date.setText(HW_NTDN_SENT_DATE);
                }
            }

            // Jaydeep 17-8-2015
            // For display class section name
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
            try {
                DatabaseHandler db = new DatabaseHandler(SubjectWiseHWNTDNNotificationActivity.this);
                //String classsecname = "";
                if (studid != "" && studid != null && schoolid != "" && schoolid != null) {
                    studentid = Integer.parseInt(studid);
                    SchoolId = Integer.parseInt(schoolid);

                } else {
                    studentid = Integer.parseInt(Datastorage.GetStudentId(getApplicationContext()));
                    SchoolId = Integer.parseInt(Datastorage.GetSchoolId(getApplicationContext()));
                }
                if (studentid == 0 || SchoolId == 0) {
                    studentid = Integer.parseInt(Datastorage.GetStudentId(getApplicationContext()));
                    SchoolId = Integer.parseInt(Datastorage.GetSchoolId(getApplicationContext()));
                }

                yearid = Datastorage.GetCurrentYearId(getApplicationContext());
                classsectionname = db.getClassSectionNameFromProfile(
                        studentid, SchoolId, yearid);
                if (classsectionname.equalsIgnoreCase("") || classsectionname.equalsIgnoreCase(" ")) {
                    new MyTask().execute();
                    // classsecname =
                    // db.getClassSectionName(studentid,SchoolId);

                }
                if (classsectionname != null && classsectionname != ""
                        && classsectionname != " ") {
                    try {
                        String parts[] = classsectionname.split(":");
                        classsectionname = parts[1];
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
                String strdate = classsectionname + " "
                        + tvhw_date.getText();
                tvhw_date.setText(strdate);
            } catch (Exception ex) {
                Constants.Logwrite("SubjectwiseHWNotification",
                        "Exception classSectionName 123:" + ex.getMessage()
                                + "::::" + ex.getStackTrace());
                Constants.writelog("SubjectwiseHWNotification",
                        "Exception classSectionName 123:" + ex.getMessage()
                                + "::::" + ex.getStackTrace());
            }

            adapter = new SubJectWiseHWNTDNAdapter(getApplicationContext(), sub_date,
                    sub_name, sub_hw_text);
            lsthwntdnlistview.setAdapter(adapter);
            if (tvhw_date.getText().equals("")) {
                tvhw_date.setText("Homework Not Done");
            }
        } catch (Exception err) {
            Constants.writelog("Subjectwisehwntnotification", "MSG 84:"
                    + err.getMessage());
            Constants.Logwrite("Subjectwisehwntnotification", "MSG 84:" + err.getMessage());
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Constants.UpdateStudentprofile(getApplicationContext(),
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
                        classsectionname = parts[1];
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
                hideKeyboard(SubjectWiseHWNTDNNotificationActivity.this);
                ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
                if (taskList.get(0).numActivities == 1 &&
                        taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
                    SubjectWiseHWNTDNNotificationActivity.this.finish();
                } else {
                    Intent intent = new Intent(SubjectWiseHWNTDNNotificationActivity.this, DailyDiaryCalanderActivity.class);
                    intent.putExtra("msgtype", Constants.HW_HOMEWORKNOTDONE);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    onBackClickAnimation();
                    finish();
                }
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
             intent = new Intent(SubjectWiseHWNTDNNotificationActivity.this, HomeActivity.class);



        } else {
             intent = new Intent(SubjectWiseHWNTDNNotificationActivity.this, DailyDiaryCalanderActivity.class);
            intent.putExtra("msgtype", "2");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        onBackClickAnimation();
        finish();

        super.onBackPressed();
    }
}
