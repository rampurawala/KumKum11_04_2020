package com.expedite.apps.kumkum.activity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.adapter.SubjectWiseHWAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.google.android.gms.ads.AdView;

public class SubjectWiseHWDetailsActivity extends BaseActivity {

    private String[] sub_name = null, sub_hw_text = null;
    private ListView ItmView;
    private TextView tvhw_date;
    private SubjectWiseHWAdapter subadap;
    private String HW_Date = "", ModuleId = "0", classsectionname = "", yearid = "";
    private int SchoolId, studentid;
    private DatabaseHandler db;
    private AdView mAdView;
    private String mActivityName="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_wise_hwdetails);
        init();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            ItmView = (ListView) findViewById(R.id.lsthomework);
            tvhw_date = (TextView) findViewById(R.id.txthwdate);
            db = new DatabaseHandler(SubjectWiseHWDetailsActivity.this);
            Intent intnt = getIntent();
            String message = intnt.getStringExtra("MSG");
            String date = intnt.getStringExtra("date");
            String[] orimsg = message.split("@@##HWO@@##");
            ModuleId = intnt.getStringExtra("ModuleId");
            if (ModuleId == null) {
                ModuleId = "0";
            }
            if (ModuleId != null && ModuleId.equals("12")) {
                Constants.setActionbar(getSupportActionBar(), SubjectWiseHWDetailsActivity.this,
                        getApplicationContext(), "Uniform Infraction", "SubjectWiseHomework", ActivityNames.SubjectWiseHWDetailsActivity_Uniform_Infraction);
                showBannerAd(mAdView, ActivityNames.SubjectWiseHWDetailsActivity_Uniform_Infraction);
            } else if (ModuleId != null && ModuleId.equals("15")) {
                Constants.setActionbar(getSupportActionBar(), SubjectWiseHWDetailsActivity.this,
                        getApplicationContext(), "Food Infraction", "Food Infraction", ActivityNames.SubjectWiseHWDetailsActivity_Food_Infraction);
                showBannerAd(mAdView, ActivityNames.SubjectWiseHWDetailsActivity_Food_Infraction);
            } else if (ModuleId != null && ModuleId.equals("16")) {
                Constants.setActionbar(getSupportActionBar(), SubjectWiseHWDetailsActivity.this,
                        getApplicationContext(), "Hygiene", "Hygiene", ActivityNames.SubjectWiseHWDetailsActivity_Hygiene);
                showBannerAd(mAdView, ActivityNames.SubjectWiseHWDetailsActivity_Hygiene);
            } else if (ModuleId != null && ModuleId.equals("17")) {
                Constants.setActionbar(getSupportActionBar(), SubjectWiseHWDetailsActivity.this,
                        getApplicationContext(), "Remarks", "Remarks", ActivityNames.SubjectWiseHWDetailsActivity_Remarks);
                showBannerAd(mAdView, ActivityNames.SubjectWiseHWDetailsActivity_Remarks);
            } else {
                Constants.setActionbar(getSupportActionBar(), SubjectWiseHWDetailsActivity.this,
                        getApplicationContext(), "Homework", "SubjectWiseHomework", ActivityNames.SubjectWiseHWDetailsActivity_Homework);
                showBannerAd(mAdView, ActivityNames.SubjectWiseHWDetailsActivity_Homework);
            }

            if (orimsg.length > 1) {
                message = orimsg[1].toString();
            } else {
                message = orimsg[0];
            }
            String[] spltr = message.split("@#@#@#");
            if (spltr.length > 0) {
                sub_name = new String[spltr.length];
                sub_hw_text = new String[spltr.length];

                int incr = 0;
                for (String hwstr : spltr) {
                    String[] strinrsplt = hwstr.split("@@##HW@@##");
//                  sub_name[incr] = strinrsplt[1];
//                  sub_hw_text[incr] = strinrsplt[2];
//                  HW_Date = strinrsplt[3];
                    sub_name[incr] = strinrsplt[0].toString();
                    if (ModuleId.equals("15") || ModuleId.equals("17")) {
                        sub_hw_text[incr] = "";
                    } else {
                        if (strinrsplt.length != 2) {
                            sub_name[incr] = strinrsplt[1];
                            sub_hw_text[incr] = strinrsplt[2].toString();
                        } else {
                            sub_hw_text[incr] = strinrsplt[1].toString();
                        }
                    }
                    incr++;
                }
                if (date == null || date.equalsIgnoreCase("")) {
                    if (ModuleId != null && ModuleId.equals("12")) {
                        tvhw_date.setText("Uniform Infraction: " + HW_Date);
                    } else if (ModuleId != null && ModuleId.equals("15")) {
                        tvhw_date.setText("Food Infraction: " + HW_Date);
                    } else if (ModuleId != null && ModuleId.equals("17")) {
                        tvhw_date.setText("Remarks: " + HW_Date);
                    } else if (ModuleId != null && ModuleId.equals("16")) {
                        tvhw_date.setText("Hygiene: " + HW_Date);
                    } else {
                        tvhw_date.setText("HW: " + HW_Date);
                    }
                } else {
                    if (ModuleId != null && ModuleId.equals("12")) {
                        tvhw_date.setText("Uniform Infraction: " + date);
                    } else if (ModuleId != null && ModuleId.equals("15")) {
                        tvhw_date.setText("Food Infraction: " + date);
                    } else if (ModuleId != null && ModuleId.equals("17")) {
                        tvhw_date.setText("Remarks: " + date);
                    } else if (ModuleId != null && ModuleId.equals("16")) {
                        tvhw_date.setText("Hygiene: " + date);
                    } else {
                        tvhw_date.setText("HW: " + date);
                    }
                }
                try {
                    studentid = Integer.parseInt(Datastorage
                            .GetStudentId(getApplicationContext()));
                    SchoolId = Integer.parseInt(Datastorage
                            .GetSchoolId(getApplicationContext()));
                    yearid = Datastorage
                            .GetCurrentYearId(getApplicationContext());
                    classsectionname = db.getClassSectionNameFromProfile(
                            studentid, SchoolId, yearid);
                    if (classsectionname.equalsIgnoreCase("")
                            || classsectionname.equalsIgnoreCase(" ")) {
                        new MyTask().execute();
                    }
                    if (classsectionname != null && classsectionname != ""
                            && classsectionname != " ") {
                        try {
                            String parts[] = classsectionname.split(":");
                            classsectionname = parts[1].toString();
                        } catch (Exception e) {
                        }
                    }
                    String strdate = classsectionname + " "
                            + tvhw_date.getText();
                    tvhw_date.setText(strdate);
                } catch (Exception ex) {
                    Constants.writelog("SubjectwiseHWDetails",
                            "Exception classSectionName 86:" + ex.getMessage()
                                    + "::::" + ex.getStackTrace());
                }
                // tvhw_date.setText("Today's HW:" + HW_Date);
                if (sub_name != null && sub_name.length > 0) {
                    subadap = new SubjectWiseHWAdapter(SubjectWiseHWDetailsActivity.this,
                            sub_name, sub_hw_text, ModuleId);
                    ItmView.setAdapter(subadap);
                }
            }
        } catch (Exception err) {
            tvhw_date.setText(SchoolDetails.MsgNoDataAvailable);
            Constants.Logwrite("SWHNT", "Method Name:Oncretae:" + err.getMessage()
                    + "StackTrace::" + err.getStackTrace().toString());
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Constants.UpdateStudentprofile(getApplicationContext(),
                        studentid, SchoolId, yearid, db);
            } catch (Exception err) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (item.getItemId() == android.R.id.home) {
                hideKeyboard(SubjectWiseHWDetailsActivity.this);
                SubjectWiseHWDetailsActivity.this.finish();
                onBackClickAnimation();
            }
        } catch (Exception ex) {

            Common.printStackTrace(ex);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(getApplicationContext(), DailyDiaryCalanderActivity.class);
            if (ModuleId != null && ModuleId.equals("12")) {
                intent.putExtra("msgtype", Constants.HW_UNIFORMNOTPROPER);
            } else {
                intent.putExtra("msgtype", Constants.HW_HOMEWORK);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            onBackClickAnimation();
        } catch (Exception err) {
            Constants.Logwrite("StudentInformationActivity:", "BackPressed:" + err.getMessage()
                    + "StackTrace::" + err.getStackTrace().toString());
        }
    }
}
