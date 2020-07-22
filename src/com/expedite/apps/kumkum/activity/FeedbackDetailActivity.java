package com.expedite.apps.kumkum.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.google.android.gms.ads.AdView;

public class FeedbackDetailActivity extends BaseActivity {

    private ProgressBar mProgressBar;
    private TextView mTxtTitle, mTxtDescription, mTxtComment, mTxtDate;
    //mTxtStudentClass, mTxtSchoolName, mTxtPhoneNumber, mTxtStudentGrno,mTxtStudentName;
    private String mValue, VersionCode = "1";
    private String tag = "Feedback Detail Activity", mSchoolId = "", mStudentClass = "", mStudentId =
            "", mGrNo = "",
            mMobileNo = "", mDate = "";
    //private EditText mEdtCommet;
    private String tmp;
    //private View mLlCommetView;
    String isNotification = "";
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_detail);

        String stid = "";
        String schid = "";

        if (getIntent() != null &&  getIntent().getExtras().getString("isNotification") != null) {
            mValue = getIntent().getExtras().getString("value");
            isNotification = getIntent().getExtras().getString("isNotification");
        }else {
            mValue = getIntent().getExtras().getString("value");
        }
        init();
        DatabaseHandler db = new DatabaseHandler(FeedbackDetailActivity.this);
        if (stid != null && stid != "" && schid != null && schid != "" && isNotification != null &&
                isNotification.equals("")) {
            try {
                String Concatestr = db.GetAccount_DetailsUsing_StudID(
                        Integer.parseInt(stid), Integer.parseInt(schid));
                Constants.Logwrite("Redirect:", "Concatestr:" + Concatestr);
                String[] con_splt = Concatestr.split(",");

                // String Phone_Number = con_splt[0].toString();
                //Constants.Logwrite("Redirect:","Phone:"+Phone_Number);

                String Stud_Name = con_splt[2].toString();
                Datastorage.SetStudentName(getApplicationContext(),
                        Stud_Name);

                Datastorage.SetStudentID(getApplicationContext(), stid);
                String School_Id = con_splt[4].toString();
                Datastorage.SetSchoolId(getApplicationContext(), School_Id);

                String Year_Id = con_splt[3].toString();
                Datastorage.SetCurrentYearId(getApplicationContext(),
                        Year_Id);

                String Class_Id = con_splt[5].toString();
                Datastorage.SetClassId(getApplicationContext(), Class_Id);

                String Sec_Id = con_splt[6].toString();
                Datastorage.SetClassSecId(getApplicationContext(), Sec_Id);

                String User_Id = con_splt[7].toString();
                Datastorage.SetUserId(getApplicationContext(), User_Id);

                String classname = con_splt[8].toString();
                Datastorage.SetClassSecName(getApplicationContext(),
                        classname);

                String studenrolldate = con_splt[9].toString();
                Datastorage.SetEnrollDate(getApplicationContext(),
                        studenrolldate);

                String lastupdatedtime = con_splt[10].toString();
                Datastorage.LastUpdatedtime(getApplicationContext(),
                        lastupdatedtime);

                String academicyear = con_splt[11].toString();
                Datastorage.SetAcademicYear(getApplicationContext(),
                        academicyear);

                String classsecname = con_splt[12].toString();
                Datastorage.SetClassSectionName(getApplicationContext(),
                        classsecname);
            } catch (Exception ex) {
                Constants.writelog(tag,
                        "onCreate()95 Exception:" + ex.getMessage());
            }
        }
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.FeedbackDetailActivity);
            Activity abc = this;
            Constants.setActionbar(getSupportActionBar(), abc, getApplicationContext(),
                    "Query Details", "Query Details",ActivityNames.FeedbackDetailActivity);
            mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
            mTxtDate = (TextView) findViewById(R.id.txtDate);
        /*mTxtStudentName = (TextView) findViewById(R.id.txtStudentName);
        mTxtStudentClass = (TextView) findViewById(R.id.txtStudentClass);
        mTxtStudentGrno = (TextView) findViewById(R.id.txtStudentGrno);
        mTxtPhoneNumber = (TextView) findViewById(R.id.txtPhoneNumber);
        mTxtSchoolName = (TextView) findViewById(R.id.txtSchoolName);*/
            mTxtTitle = (TextView) findViewById(R.id.txtTitle);
            mTxtDescription = (TextView) findViewById(R.id.txtDescription);
            mTxtComment = (TextView) findViewById(R.id.txtComment);
            //mLlCommetView = (View) findViewById(R.id.ll_CommetView);
        /*mEdtCommet = (EditText) findViewById(R.id.edtCommet);
        mEdtCommet.setEnabled(false);*/
            setData();
        } catch (Exception ex) {
            Constants.writelog(tag,
                    "init()122 Exception:" + ex.getMessage());
        }
    }

    public void setData() {
        try {
            if (mValue != null && mValue != "") {
                String[] tmp = mValue.split("@@##");
                if (tmp[6] != null && !tmp[6].isEmpty())
                    mTxtDate.setText(":  " + tmp[6]);
                if (tmp[2] != null && !tmp[2].isEmpty())
                    mTxtTitle.setText(":  " + tmp[2]);
                if (tmp[3] != null && !tmp[3].isEmpty())
                    mTxtDescription.setText(":  " + tmp[3]);
                if (tmp[5] != null && !tmp[5].isEmpty()) {
                    ((View) findViewById(R.id.ll_CommentText)).setVisibility(View.VISIBLE);
                    mTxtComment.setText(":  " + tmp[5]);
                } else {
                    ((View) findViewById(R.id.ll_CommentText)).setVisibility(View.GONE);
                }
            }
        } catch (Exception ex) {
            Constants.writelog(tag,
                    "setData()101 Exception:" + ex.getMessage() + ":::::"
                            + ex.getStackTrace());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.clear();
//        MenuInflater inflater = getMenuInflater();
//        //inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                onBackClickAnimation();
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent=null;
        if (isNotification != null && !isNotification.isEmpty()) {
            super.onBackPressed();
             intent = new Intent(FeedbackDetailActivity.this, HomeActivity.class);
        } else {
             intent = new Intent(FeedbackDetailActivity.this, Feedback_History.class);
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
