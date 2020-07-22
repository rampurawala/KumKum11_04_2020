package com.expedite.apps.kumkum.activity;

import android.app.ActivityManager;
import android.content.Intent;
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

public class SubJectWiseHWNTDNDetailsActivity extends BaseActivity {
    private TextView txthwntdate;
    private ListView lsthwntdnlistview;
    private List<String> sub_date = new ArrayList<String>();
    private ArrayList<String> sub_name = new ArrayList<String>(), sub_hw_text = new ArrayList<String>();
    private SubJectWiseHWNTDNAdapter adapter;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_wise_hwntdnnotification);
        init();
    }

    public void init() {
        Constants.setActionbar(getSupportActionBar(), SubJectWiseHWNTDNDetailsActivity.this,
                SubJectWiseHWNTDNDetailsActivity.this, "Homework Not Done", "SubjectWiseHWND",ActivityNames.SubJectWiseHWNTDNDetailsActivity);
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.SubJectWiseHWNTDNDetailsActivity);
            lsthwntdnlistview = (ListView) findViewById(R.id.lsthwntdn);
            txthwntdate = (TextView) findViewById(R.id.txthwntdndate);
            String message = getIntent().getStringExtra("MSG");
            String[] orimsg = message.split("@@##HWO@@##");
            if (orimsg.length > 1) {
                message = orimsg[1].toString();
            } else {
                message = orimsg[0].toString();
            }
            // String msg =
            String[] spltr = message.split("@#@#@#");
            for (String hwstr : spltr) {
                String[] strinrsplt = hwstr.split("@@##HW@@##");
                String HW_NT_DATE = strinrsplt[2].toString();
                sub_date.add(HW_NT_DATE);
                String HW_NTDN_SUB = strinrsplt[0].toString();
                sub_name.add(HW_NTDN_SUB);
                String HW_NTDN_SUB_TEXT = strinrsplt[1].toString();
                sub_hw_text.add(HW_NTDN_SUB_TEXT);
                String HW_NTDN_SENT_DATE = strinrsplt[3].toString();
                txthwntdate.setText(HW_NTDN_SENT_DATE);
            }
            int student_id = Integer.parseInt(Datastorage.GetStudentId(SubJectWiseHWNTDNDetailsActivity.this));
            int School_Id = Integer.parseInt(Datastorage.GetSchoolId(SubJectWiseHWNTDNDetailsActivity.this));
            db = new DatabaseHandler(SubJectWiseHWNTDNDetailsActivity.this);
            String yearid = Datastorage.GetCurrentYearId(SubJectWiseHWNTDNDetailsActivity.this);
            String classsectionname = db.getClassSectionNameFromProfile(student_id, School_Id, yearid);
            txthwntdate.setText(classsectionname + " " + txthwntdate.getText());
            adapter = new SubJectWiseHWNTDNAdapter(SubJectWiseHWNTDNDetailsActivity.this, sub_date, sub_name, sub_hw_text);
            lsthwntdnlistview.setAdapter(adapter);
        } catch (Exception err) {
            Common.printStackTrace(err);
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
                hideKeyboard(SubJectWiseHWNTDNDetailsActivity.this);
                ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
                if (taskList.get(0).numActivities == 1 &&
                        taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
                    SubJectWiseHWNTDNDetailsActivity.this.finish();
                } else {
                    Intent intent = new Intent(SubJectWiseHWNTDNDetailsActivity.this, DailyDiaryCalanderActivity.class);
                    intent.putExtra("msgtype", Constants.HW_HOMEWORKNOTDONE);
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
        if (taskList.get(0).numActivities == 1 &&
                taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
            SubJectWiseHWNTDNDetailsActivity.this.finish();
        } else {
            Intent intent = new Intent(SubJectWiseHWNTDNDetailsActivity.this, DailyDiaryCalanderActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            intent.putExtra("msgtype", Constants.HW_HOMEWORKNOTDONE);
            finish();
        }
        onBackClickAnimation();
    }
}
