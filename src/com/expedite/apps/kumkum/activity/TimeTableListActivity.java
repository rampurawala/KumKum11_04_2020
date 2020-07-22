package com.expedite.apps.kumkum.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.adapter.TimeTableListAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.FoodChartListModel;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeTableListActivity extends BaseActivity implements View.OnClickListener {
    private ProgressBar mProgressBar;
    private TimeTableListAdapter mTimeTableAdapter;
    private RecyclerView mTimeTableRecycleView;
    private GridLayoutManager mGridLayoutManager;
    private ImageView mBtnPreviousDay, mBtnNextDay;
    private TextView mTxtselectedDay, mTxtNoRecordsFound, mTxtTime, mTxtSubject, mTxtTeacher;
    private String Year_Id = "", SchoolId = "", ClassSecId = "";
    private String tag = "TimeTableListActivity", mIsFromHome = "";
    private int mSelecctedDayId = 0;
    private ArrayList<FoodChartListModel.TimeTableList> mTimeTableList = new ArrayList<>();
    private ArrayList<FoodChartListModel.Array> mTimeTableAllList = new ArrayList<>();
    private final String[] weekdays = new String[]{"Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"};
    private final String[] weekdaysId = new String[]{"0", "1", "2", "3", "4", "5", "6"};
    LinearLayout empty_folder_lyt_file_detail;
    private AdView mAdView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_table_list_layout);
        if (getIntent() != null && getIntent().getExtras() != null)
            mIsFromHome = getIntent().getExtras().getString("IsFromHome", "");
        init();
        initListner();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.TimeTableListActivity);

            Activity abc = this;
            Constants.setActionbar(getSupportActionBar(), abc, getApplicationContext(),
                    "Time Table", "Time Table", ActivityNames.TimeTableListActivity);
            SchoolId = Datastorage.GetSchoolId(TimeTableListActivity.this);
            ClassSecId = Datastorage.GetClassSecId(TimeTableListActivity.this);
            Year_Id = Datastorage.GetCurrentYearId(TimeTableListActivity.this);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            Date date = new Date();
            String dayOfTheWeek = sdf.format(date);

            mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
            empty_folder_lyt_file_detail=(LinearLayout) findViewById(R.id.empty_folder_lyt_file_detail);
            mTimeTableRecycleView = (RecyclerView) findViewById(R.id.timeTableRecycleView);
            mGridLayoutManager = new GridLayoutManager(TimeTableListActivity.this, 1);
            mTimeTableRecycleView.setLayoutManager(mGridLayoutManager);
            //mTimeTableAdapter = new TimeTableListAdapter(TimeTableListActivity.this, mTimeTableList, true, true, true);
            //mTimeTableRecycleView.setAdapter(mTimeTableAdapter);
            mTxtselectedDay = (TextView) findViewById(R.id.selectedDay);
            mTxtTime = (TextView) findViewById(R.id.txtTime);
            mTxtSubject = (TextView) findViewById(R.id.txtSubject);
            mTxtTeacher = (TextView) findViewById(R.id.txtTeacher);
            if (dayOfTheWeek != null && !dayOfTheWeek.isEmpty())
                mTxtselectedDay.setText(getCurrentDay(dayOfTheWeek));
            mBtnPreviousDay = (ImageView) findViewById(R.id.btnPreviousDay);
            mBtnNextDay = (ImageView) findViewById(R.id.btnNextDay);
            mTxtNoRecordsFound = (TextView) findViewById(R.id.TxtNoRecordsFound);
            getTimeTableList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListner() {
        mBtnPreviousDay.setOnClickListener(this);
        mBtnNextDay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNextDay:
                try {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (mSelecctedDayId == mTimeTableAllList.size() - 1) {
                                    mSelecctedDayId = 0;
                                } else {
                                    mSelecctedDayId = Integer.parseInt(weekdaysId[mSelecctedDayId + 1]);
                                }
                                mTxtselectedDay.setText(weekdays[mSelecctedDayId]);
                                mTimeTableList.clear();
                                mTimeTableAdapter.notifyDataSetChanged();
                                mTimeTableList.addAll(mTimeTableAllList.get(mSelecctedDayId).getTimeTableList());
                                mTimeTableAdapter.notifyDataSetChanged();
                                if (mTimeTableList.size() > 0) {
                                    mTimeTableRecycleView.setVisibility(View.VISIBLE);
                                    mTxtNoRecordsFound.setVisibility(View.GONE);
                                } else {
                                    mTimeTableRecycleView.setVisibility(View.GONE);
                                    mTxtNoRecordsFound.setVisibility(View.VISIBLE);
                                }
                                mTimeTableAdapter.notifyDataSetChanged();
                                Common.showLog("SelectedDayId", "" + mSelecctedDayId);
                            } catch (IndexOutOfBoundsException ex) {
                                Common.printStackTrace(ex);
                            }
                        }
                    });

                } catch (Exception ex) {
                    Common.printStackTrace(ex);
                }
                break;
            case R.id.btnPreviousDay:
                try {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (mSelecctedDayId == 0) {
                                    mSelecctedDayId = mTimeTableAllList.size() - 1;
                                } else {
                                    mSelecctedDayId = Integer.parseInt(weekdaysId[mSelecctedDayId - 1]);
                                }
                                mTxtselectedDay.setText(weekdays[mSelecctedDayId]);
                                mTimeTableList.clear();
                                mTimeTableAdapter.notifyDataSetChanged();
                                mTimeTableList.addAll(mTimeTableAllList.get(mSelecctedDayId).getTimeTableList());
                                mTimeTableAdapter.notifyDataSetChanged();
                                if (mTimeTableList.size() > 0) {
                                    mTimeTableRecycleView.setVisibility(View.VISIBLE);
                                    mTxtNoRecordsFound.setVisibility(View.GONE);
                                } else {
                                    mTimeTableRecycleView.setVisibility(View.GONE);
                                    mTxtNoRecordsFound.setVisibility(View.VISIBLE);
                                }
                                Common.showLog("SelectedDayId", "" + mSelecctedDayId);
                            } catch (IndexOutOfBoundsException ex) {
                                Common.printStackTrace(ex);
                            }
                        }
                    });

                } catch (Exception ex) {
                    Common.printStackTrace(ex);
                }
                break;
            default:
                break;
        }
    }


    private void getTimeTableList() {
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            try {
                Call<FoodChartListModel> call = ((MyApplication) getApplicationContext())
                        .getmRetrofitInterfaceAppService().GetTimeTableList(SchoolId, ClassSecId, "all", Year_Id,
                                Constants.PLATFORM);

                call.enqueue(new Callback<FoodChartListModel>() {
                    @Override
                    public void onResponse(Call<FoodChartListModel> call, Response<FoodChartListModel> response) {
                        try {
                            FoodChartListModel tmp = response.body();
                            if (tmp != null && tmp.getArrays() != null && tmp.getArrays().get(0).getResponse() != null
                                    && !tmp.getArrays().get(0).getResponse().isEmpty()
                                    && tmp.getArrays().get(0).getResponse().equalsIgnoreCase("1")) {
                                if (tmp.getArrays().get(0).getTimeTableList() != null &&
                                        tmp.getArrays().get(0).getTimeTableList().size() > 0) {
                                    mTimeTableAdapter = new TimeTableListAdapter(TimeTableListActivity.this, mTimeTableList, tmp.getIsSubjectDisplay(), tmp.getIsTeacherDisplay(), tmp.getIsTimeDisplay());
                                    mTimeTableRecycleView.setAdapter(mTimeTableAdapter);
                                    mTimeTableAllList.clear();
                                    mTimeTableAllList.addAll(tmp.getArrays());
                                    mTimeTableList.addAll(tmp.getArrays().get(mSelecctedDayId).getTimeTableList());
                                    mTimeTableAdapter.notifyDataSetChanged();
                                    if (!tmp.getIsSubjectDisplay()) {
                                        mTxtSubject.setVisibility(View.GONE);
                                    }
                                    if (!tmp.getIsTeacherDisplay()) {
                                        mTxtTeacher.setVisibility(View.GONE);
                                    }
                                    if (!tmp.getIsTimeDisplay()) {
                                        mTxtTime.setVisibility(View.GONE);
                                    }
                                    ((View) findViewById(R.id.llMainLayout)).setVisibility(View.VISIBLE);
                                } else {
                                    ((View) findViewById(R.id.llMainLayout)).setVisibility(View.GONE);
                                }
                                if (mTimeTableList != null && mTimeTableList.size() > 0) {
                                    mTxtNoRecordsFound.setVisibility(View.GONE);
                                    mTimeTableRecycleView.setVisibility(View.VISIBLE);
                                } else {
                                    mTxtNoRecordsFound.setVisibility(View.VISIBLE);
                                    mTimeTableRecycleView.setVisibility(View.GONE);
                                    empty_folder_lyt_file_detail.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (tmp != null && tmp.getArrays() != null && tmp.getArrays().get(0).getMessage() != null && tmp.getArrays().get(0).getMessage() != "") {
                                    Common.showToast(TimeTableListActivity.this, tmp.getArrays().get(0).getMessage());
                                    empty_folder_lyt_file_detail.setVisibility(View.VISIBLE);
                                } else {
                                    empty_folder_lyt_file_detail.setVisibility(View.VISIBLE);
                                    Common.showToast(TimeTableListActivity.this, "No Timetable Data Available..");
                                }
                            }
                            mProgressBar.setVisibility(View.GONE);
                        } catch (Exception ex) {
                            Constants.writelog(tag, "Exception_113:" + ex.getMessage()
                                    + "::::::" + ex.getStackTrace());
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<FoodChartListModel> call, Throwable t) {
                        Constants.writelog(tag, "Exception_113:" + t.getMessage());
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

            } catch (Exception ex) {
                mProgressBar.setVisibility(View.GONE);
                Constants.writelog(tag, "Exception_113:" + ex.getMessage()
                        + "::::::" + ex.getStackTrace());
            }
        } else {
            empty_folder_lyt_file_detail.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            Common.showToast(TimeTableListActivity.this, getString(R.string.msg_connection));
        }
    }

    public String getCurrentDay(String day) {
        String DayName = "";
        for (int i = 0; i < weekdays.length; i++) {
            if (weekdays[i].contains(day)) {
                DayName = weekdays[i];
                mSelecctedDayId = Integer.parseInt(weekdaysId[i]);
            }
        }
        return DayName;
    }

    public String getCurrentDayId(String dayId) {
        String DayId = "";
        for (int i = 0; i < weekdaysId.length; i++) {
            if (weekdaysId[i].contains(dayId)) {
                DayId = weekdaysId[i];
            }
        }
        return DayId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                TimeTableListActivity.this.finish();
                onBackClickAnimation();
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mIsFromHome != null && !mIsFromHome.isEmpty()) {
            super.onBackPressed();
            onBackClickAnimation();
        } else {
            Intent intent = new Intent(TimeTableListActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

}
