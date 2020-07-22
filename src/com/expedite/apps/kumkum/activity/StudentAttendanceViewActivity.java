package com.expedite.apps.kumkum.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.adapter.StudentAttendanceListAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.google.android.gms.ads.AdView;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;


public class StudentAttendanceViewActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener {
    private String[] parts = {""};
    private TextView tv;
    private Button btnUpdate, btnViewMore;
    private boolean isupdate = false;
    private int IsCurrentYear = 0, YearId = 0;
    private HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
    private String attendance = "", SchoolId, StudentId, LastUpdatedTime;
    private Time cur_time = new Time();
    private String mIsFromHome = "";
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private GridLayoutManager mGridLayoutManager;
    private StudentAttendanceListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeView;
    private boolean mShowCentreProgress = true;
    LinearLayout empty_folder_lyt_file_detail;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_view_attendance);

        if (getIntent() != null && getIntent().getExtras() != null)
            mIsFromHome = getIntent().getExtras().getString("IsFromHome", "");


        init();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);

            db = new DatabaseHandler(StudentAttendanceViewActivity.this);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

            String title = "",mActivityName="";
            int Cnt_Count = 0;
            Cnt_Count = Datastorage.GetMultipleAccount(StudentAttendanceViewActivity.this);
            if (Cnt_Count == 1) {
                title = "Attendance" + "-" + Datastorage.GetStudentName(StudentAttendanceViewActivity.this);
            } else {
                title = "Attendance";
            }
            showBannerAd(mAdView, ActivityNames.StudentAttendanceViewActivity);
            mActivityName =  ActivityNames.StudentAttendanceViewActivity;
            Constants.setActionbar(getSupportActionBar(), StudentAttendanceViewActivity.this, StudentAttendanceViewActivity.this,
                    title, "Attendance",mActivityName);
            SchoolId = Datastorage.GetSchoolId(StudentAttendanceViewActivity.this);
            StudentId = Datastorage.GetStudentId(StudentAttendanceViewActivity.this);
            LastUpdatedTime = Datastorage.GetLastUpdatedtime(StudentAttendanceViewActivity.this);
            empty_folder_lyt_file_detail=findViewById(R.id.empty_folder_lyt_file_detail);
            String defacnt = db.GetDefaultAcademicYearAccount(Integer.parseInt(SchoolId), Integer.parseInt(StudentId));
            if (defacnt != null && defacnt.length() > 0) {
                String[] splterstr = defacnt.split(",");
                YearId = Integer.parseInt(splterstr[1]);
            } else {
                YearId = Integer.parseInt(Datastorage.GetCurrentYearId(StudentAttendanceViewActivity.this));
            }
            mSwipeView = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
            mSwipeView.setOnRefreshListener(StudentAttendanceViewActivity.this);
            mRecyclerView = (RecyclerView) findViewById(R.id.RecycleView);
            mGridLayoutManager = new GridLayoutManager(StudentAttendanceViewActivity.this, 1);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            mAdapter = new StudentAttendanceListAdapter(StudentAttendanceViewActivity.this, parts);
            mRecyclerView.setAdapter(mAdapter);

            tv = (TextView) findViewById(R.id.tvmarkqueetextdisplayattendance);
            tv.setSelected(true);
            tv.setText(LastUpdatedTime);
            btnUpdate = (Button) findViewById(R.id.btnUpdateAttandance);
            btnViewMore = (Button) findViewById(R.id.btnViewMoreAttandance);
            btnUpdate.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if(isOnline())
                    {
                        mShowCentreProgress=true;
                        isupdate = true;
                        new GetStudentAttendanceReport().execute();
                    }
                    else
                    {
                        Toast.makeText(StudentAttendanceViewActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
                    }

                }
            });
            btnViewMore.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    // change by Tejas 21-07-2018
                    String isFrom="1";
                    if (!isOnline()) {
                        Toast.makeText(StudentAttendanceViewActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(StudentAttendanceViewActivity.this,
                                DailyDiaryCalanderActivity.class);
                        intent.putExtra("msgtype", Constants.HW_ABSENT);
                        intent.putExtra("isFrom",isFrom);

                        startActivity(intent);
                        onClickAnimation();
                    }
                }
            });
            new GetStudentAttendanceReport().execute();
            int lastautoupdatedate = Datastorage.GetLastAutoUpdateAttendance(StudentAttendanceViewActivity.this);
            cur_time.setToNow();
            if (lastautoupdatedate != cur_time.monthDay) {
                MyTaskRefresh mytaskRef = new MyTaskRefresh();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mytaskRef.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    // mytaskRef.execute();
                } else {
                    mytaskRef.execute();
                }
            }
            Constants.MyTaskLogVisit LogUserVisited = new Constants.MyTaskLogVisit();
            LogUserVisited.execute(StudentAttendanceViewActivity.this, 3, 8);
        } catch (Exception ex) {
            Constants.writelog("StudentAttendanceViewActivity", "MSG 123:" + ex.getMessage());
        }
    }

    // change by Tejas 21-07-2018
    @Override
    public void onRefresh() {
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mShowCentreProgress = true;
                isupdate = true;
                new GetStudentAttendanceReport().execute();
            }
        }, 1000);
    }

    private class MyTaskRefresh extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            GetAttendance(false);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            {
                if (parts != null && parts.length > 0) {
//                    AttandanceListAdapter adapter = new AttandanceListAdapter(
//                            StudentAttendanceViewActivity.this, parts);
//                    ItmNames.setAdapter(adapter);
                    mAdapter = new StudentAttendanceListAdapter(StudentAttendanceViewActivity.this, parts);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }


    private class GetStudentAttendanceReport extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            if (mShowCentreProgress) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (db.getAcademicYearCount(Integer.parseInt(StudentId), Integer.parseInt(SchoolId)) > 0) {
                    Boolean IsCurrentYearId = db.CheckAcademicYearCurrentOrNot(
                            Integer.parseInt(StudentId), Integer.parseInt(SchoolId), YearId);
                    if (IsCurrentYearId) {
                        IsCurrentYear = 1;
                    } else {
                        IsCurrentYear = 0;
                    }
                } else {
                    IsCurrentYear = 1;
                }
                String mData = "";
                if (!isupdate) {
                    mData = db.GetStudAttendanceDetails(Integer.parseInt(StudentId), Integer.parseInt(SchoolId), YearId);
                    if (mData == null || mData.equals("0") || mData.isEmpty()) {
                        mData = GetAttendance(true);
                        Constants.writelog("Attandance", "Data Not Found StudId:" + StudentId + " SchoolId:" + SchoolId + " Yearid:" + YearId);
                    } else {
                        parts = mData.split(",");
                    }
                } else {
                    mData = GetAttendance(true);
                }
            } catch (Exception err) {
                Constants.writelog("StudviewAttendance", "Exception: DoInBackground 175"
                        + err.getMessage() + "::::::" + err.getStackTrace());
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            mShowCentreProgress = false;
            try {
                if (Constants.isShowInternetMsg) {
                    empty_folder_lyt_file_detail.setVisibility(View.VISIBLE);
                    Constants.NotifyNoInternet(getApplicationContext());
                } else {
                    empty_folder_lyt_file_detail.setVisibility(View.GONE);
                    if (parts != null && parts.length > 0) {
                        if (attendance == "No data Available") {
                            mProgressBar.setVisibility(View.GONE);
                            if (mSwipeView.isRefreshing())
                                mSwipeView.setRefreshing(false);
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    StudentAttendanceViewActivity.this).create();
                            alertDialog.setTitle("Information");
                            alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                            alertDialog.setIcon(R.drawable.information);
                            alertDialog.setCancelable(false);
                            alertDialog.setButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            finish();
                                        }
                                    });
                            alertDialog.show();

                            return;
                        } else {
//                            AttandanceListAdapter adapter = new AttandanceListAdapter(
//                                    StudentAttendanceViewActivity.this, parts);
//                            ItmNames.setAdapter(adapter);
                            mAdapter = new StudentAttendanceListAdapter(StudentAttendanceViewActivity.this, parts);
                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        if (mSwipeView.isRefreshing())
                            mSwipeView.setRefreshing(false);
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                StudentAttendanceViewActivity.this).create();
                        alertDialog.setTitle("Information");
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                        alertDialog.setIcon(R.drawable.information);
                        alertDialog.setButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        finish();
                                    }
                                });
                        alertDialog.show();

                        return;
                    }
                }
                mProgressBar.setVisibility(View.GONE);

                if (mSwipeView.isRefreshing())
                    mSwipeView.setRefreshing(false);
            } catch (Exception err) {
                Constants.writelog("StudviewAttendance", "Exception: onPostExecute 264"
                        + err.getMessage() + "::::::" + err.getStackTrace());
                mProgressBar.setVisibility(View.GONE);
                if (mSwipeView.isRefreshing())
                    mSwipeView.setRefreshing(false);
            }
        }
    }

    public String GetAttendance(Boolean isCompulsory) {
        if (isOnline()) {
            try {
                SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_ATTENDANCE_KUMKUM);
                request.addProperty("StudId", StudentId);
                request.addProperty("yearid", YearId);
                request.addProperty("schoolid", SchoolId);
                // request.addProperty("")
                request.addProperty("class_id", Integer.parseInt(Datastorage
                        .GetClassId(getApplicationContext())));
                request.addProperty("class_sec_id", Integer.parseInt(Datastorage
                        .GetClassSecId(getApplicationContext())));
                String EnrollDate = Datastorage
                        .GetEnrollDate(getApplicationContext());
                if (EnrollDate == null) {
                    EnrollDate = "";
                }
                request.addProperty("enroll_date", EnrollDate);

                // String Stdid = LoginDetail.getStudId();
                // String yearid = LoginDetail.getCurrentYearId();
                // String schlid = LoginDetail.getSchoolId();

                if (IsCurrentYear == 1) {
                    String tm = LastUpdatedTime;
                    if (LastUpdatedTime == null) {
                        LastUpdatedTime = "";
                    }
                    request.addProperty("enddate", LastUpdatedTime);
                } else {
                    String Holiday_Prof_End_Date = Datastorage
                            .GetHolidayEndDate(getApplicationContext());
                    if (Holiday_Prof_End_Date == null) {
                        Holiday_Prof_End_Date = "";
                    }
                    request.addProperty("enddate", Holiday_Prof_End_Date);
                }
                Constants.Logwrite("StudentAttendanceViewActivity",
                        "GetAttendanceKumKum() studid: "
                                + StudentId
                                + ":::: Yearid: "
                                + YearId
                                + ":::: schoolid: "
                                + SchoolId
                                + "::: classid: "
                                + Datastorage.GetClassId(getApplicationContext())
                                + ":::: classsectionid: "
                                + Datastorage
                                .GetClassSecId(getApplicationContext())
                                + "::: enroll_date: " + EnrollDate
                                + "::: enddate: "
                                + request.getProperty("enddate").toString());

                SoapObject result = Constants
                        .CallWebMethod(StudentAttendanceViewActivity.this, request,
                                Constants.GET_ATTENDANCE_KUMKUM, isCompulsory);
                if (result != null && result.getPropertyCount() > 0) {
                    String obtained = result.getPropertyAsString(0);
                    attendance = obtained;
                    Constants.Logwrite("ViewAttendance", "UpdateValue 385 Reqest:" + request + " Result:" + attendance);
                    Constants.writelog("ViewAttendance", "UpdateValue 385 Reqest:" + request + " Result:" + attendance);
                    if (!attendance.equalsIgnoreCase("No data Available") || !attendance.equalsIgnoreCase("Nodata Available")) {
                        parts = attendance.split(",");
                        int res = db.updateStudAttendanceDetails(
                                Integer.parseInt(StudentId),
                                Integer.parseInt(SchoolId), YearId, attendance);
                        if (res != 1) {
                            db.AddStudAttendanceDetails(
                                    Integer.parseInt(StudentId),
                                    Integer.parseInt(SchoolId), YearId, attendance);
                        }
                        Datastorage.SetLastAutoUpdateAttendance(
                                StudentAttendanceViewActivity.this, cur_time.monthDay);
                    } else {
                        parts = null;
                    }
                } else {
                    parts = null;
                }
            } catch (Exception err) {
                Constants.writelog("StudviewAttendance",
                        "Exception: GetAttendanceKumKum 334" + err.getMessage()
                                + "::::::" + err.getStackTrace());
                parts = null;
                Constants.Logwrite("ViewAttendance", err.getMessage());
            }
        } else {
            Common.showToast(StudentAttendanceViewActivity.this, "No internation connection available");
        }
        return attendance;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            menu.clear();
            mapacc = new HashMap<Integer, String>();
            menu.add(0, 1, 1, "Add Account").setTitle("Add Account");
            //  menu.add(0, 2, 2, "Remove Account").setTitle("Remove Account");
            menu.add(0, 3, 3, "Set Default Account").setTitle("Set Default Account");
            mapacc = Constants.AddAccount(menu, db);
        } catch (Exception err) {
            Constants.writelog("StudviewAttendance", "Exception: OnprepareOptionsMenu 353" + err.getMessage() + "::::::" + err.getStackTrace());
            Constants.Logwrite("Attendance:", "onPrepareOptionsMenu:" + err.getMessage()
                    + "StackTrace::" + err.getStackTrace().toString());
            return true;

        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int iid = item.getItemId();
        try {
            if (iid == android.R.id.home) {
                hideKeyboard(StudentAttendanceViewActivity.this);
                StudentAttendanceViewActivity.this.finish();
                onBackClickAnimation();

            } else if (iid == 1 || iid == 2 || iid == 3) {
                if (iid == 1) {
                    addAccountClick(StudentAttendanceViewActivity.this);
                } else if (iid == 2) {
                    intent = new Intent(StudentAttendanceViewActivity.this,
                            AccountListRemoveActivity.class);
                    startActivity(intent);
                    finish();
                    removeAccountClick(StudentAttendanceViewActivity.this);
                } else {
                    accountListClick(StudentAttendanceViewActivity.this);
                }
            } else {
                String details = mapacc.get(iid).toString();
                Constants.SetAccountDetails(details, StudentAttendanceViewActivity.this);
                intent = new Intent(StudentAttendanceViewActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
                onBackClickAnimation();
                return true;
            }
        } catch (Exception err) {
            Constants.writelog("StudviewAttendance",
                    "Exception: OnoptionItemSelected 353" + err.getMessage()
                            + "::::::" + err.getStackTrace());
        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onBackPressed() {
        try {
            if (mIsFromHome != null && !mIsFromHome.isEmpty()) {
                super.onBackPressed();
                onBackClickAnimation();
            } else {
                Intent intent = new Intent(StudentAttendanceViewActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        } catch (Exception err) {
            Constants.Logwrite("Settings:", "BackPressed:" + err.getMessage()
                    + "StackTrace::" + err.getStackTrace().toString());
        }
    }

}
