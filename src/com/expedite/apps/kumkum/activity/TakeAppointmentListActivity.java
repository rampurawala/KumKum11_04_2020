package com.expedite.apps.kumkum.activity;

import android.content.Intent;
import android.graphics.Typeface;

import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.fragment.AppointmentApprovedFragment;
import com.expedite.apps.kumkum.fragment.AppointmentPendingFragment;
import com.expedite.apps.kumkum.fragment.AppointmentRejectFragment;
import com.expedite.apps.kumkum.model.AppointmentListModal;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TakeAppointmentListActivity extends BaseActivity {
    public static ArrayList<AppointmentListModal.Getappoinment> mPendingAppointmentArrayList = new ArrayList<>();
    public static ArrayList<AppointmentListModal.Getappoinment> mApprovedAppointmentArrayList = new ArrayList<>();
    public static ArrayList<AppointmentListModal.Getappoinment> mRejectedAppointmentArrayList = new ArrayList<>();
    private ArrayList<AppointmentListModal.Getappoinment> mAppointmentArrayList = new ArrayList<>();
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    TextView btnTakeAppointment;
    ProgressBar appointmentProgressbar;
    String Status = "0";
    Common common;
    TextView txtAppointmentNoRecordFound;
    AppBarLayout appBarAppointment;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_appointment_list);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            setTitle("Appointment");
        }
        if (getIntent() != null && getIntent().getExtras() != null) {
            Status = getIntent().getExtras().getString("value");
        }
        mAdView = (AdView) findViewById(R.id.adView);
        showBannerAd(mAdView, ActivityNames.TakeAppointmentListActivity);
        showFullScreenAd(TakeAppointmentListActivity.this, ActivityNames.TakeAppointmentListActivity);
        appointmentProgressbar = findViewById(R.id.appointmentProgressbar);
        txtAppointmentNoRecordFound = findViewById(R.id.txtAppointmentNoRecordFound);
        btnTakeAppointment = findViewById(R.id.btnTakeAppointment);
        appBarAppointment = findViewById(R.id.appBarAppointment);
        appBarAppointment.setBackgroundColor(getResources().getColor(R.color.white));
        common = new Common(this);
        getAppointmentList();
        btnTakeAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TakeAppointmentListActivity.this, TakeAppointmentActivity.class);
                startActivity(i);
                onClickAnimation();
            }
        });
    }
    ///ListAppointmentVisitor?visitorid=153&fromdate=2019/08/01&todate=2019/08/13&type=-1&schoolid=8414&hostid=&year_id=&mobileno=&platform=&appName=&appVersion=

    private void getAppointmentList() {
        if (isOnline()) {
            appointmentProgressbar.setVisibility(View.VISIBLE);
            String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
            String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
            Call<AppointmentListModal> call = ((MyApplication) getApplicationContext()).getRetroFitAppointment()
                    .GetAppointmentList(mStudentId, "", "", "-1", mSchoolId, "", mYearId, Datastorage.GetPhoneNumber(getApplicationContext()), Constants.PLATFORM, String.valueOf(SchoolDetails.appname), Datastorage.GetAppVersion(TakeAppointmentListActivity.this));
            call.enqueue(new Callback<AppointmentListModal>() {
                @Override
                public void onResponse(Call<AppointmentListModal> call, Response<AppointmentListModal> response) {
                    try {
                        AppointmentListModal tmps = response.body();
                        if (tmps != null && tmps.getResult() != null && !tmps.getResult().isEmpty()
                                && tmps.getResult().equalsIgnoreCase("1")) {

                            if (tmps.getGetappoinment() != null && tmps.getGetappoinment().size() > 0) {
                                mAppointmentArrayList.clear();
                                mAppointmentArrayList.addAll(tmps.getGetappoinment());
                            }

                            if (mAppointmentArrayList != null && mAppointmentArrayList.size() > 0) {
                                mPendingAppointmentArrayList.clear();
                                mApprovedAppointmentArrayList.clear();
                                mRejectedAppointmentArrayList.clear();

                                for (int i = 0; i < mAppointmentArrayList.size(); i++) {
                                    // 0 Pending,1 Approved, 2 reject,
                                    if (mAppointmentArrayList.get(i).getFlg().equalsIgnoreCase("0")) {
                                        mPendingAppointmentArrayList.add(mAppointmentArrayList.get(i));
                                    }
                                    if (mAppointmentArrayList.get(i).getFlg().equalsIgnoreCase("1")) {
                                        mApprovedAppointmentArrayList.add(mAppointmentArrayList.get(i));
                                    }
                                    if (mAppointmentArrayList.get(i).getFlg().equalsIgnoreCase("2")) {
                                        mRejectedAppointmentArrayList.add(mAppointmentArrayList.get(i));
                                    }
                                }
                                mViewPager = (ViewPager) findViewById(R.id.viewpagerList_appointment);
                                AppointmentListPagerAdapter adapter = new AppointmentListPagerAdapter(getSupportFragmentManager());
                                adapter.addFragment(new AppointmentPendingFragment(), "Pending (" + mPendingAppointmentArrayList.size() + ")");
                                adapter.addFragment(new AppointmentApprovedFragment(), "Approved (" + mApprovedAppointmentArrayList.size() + ")");
                                adapter.addFragment(new AppointmentRejectFragment(), "Rejected (" + mRejectedAppointmentArrayList.size() + ")");
                                mViewPager.setAdapter(adapter);
                                tabLayout = (TabLayout) findViewById(R.id.tabs_status_appointment);
                                tabLayout.setupWithViewPager(mViewPager);
                                changeTabsFont();
                                if (Status != null && Status.equals("1")) {
                                    mViewPager.setCurrentItem(1, true);
                                } else if (Status != null && Status.equals("2")) {
                                    mViewPager.setCurrentItem(2, true);
                                } else {
                                    mViewPager.setCurrentItem(0, true);
                                }
                                ((View) findViewById(R.id.rlMainAppointmentList)).setVisibility(View.VISIBLE);
                            } else {
                                ((View) findViewById(R.id.rlMainAppointmentList)).setVisibility(View.GONE);
                            }
                        } else {
                            if (tmps != null && tmps.getMessage() != null && !tmps.getMessage().isEmpty())
                                Common.showToast(TakeAppointmentListActivity.this, tmps.getMessage());
                            ((View) findViewById(R.id.rlMainAppointmentList)).setVisibility(View.GONE);
                            txtAppointmentNoRecordFound.setVisibility(View.VISIBLE);
                        }
                        appointmentProgressbar.setVisibility(View.GONE);
                    } catch (Exception ex) {
                        appointmentProgressbar.setVisibility(View.GONE);
                        Constants.writelog("AppointmentListActivity",
                                "getAppointmentList 132:" + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<AppointmentListModal> call, Throwable t) {
                    appointmentProgressbar.setVisibility(View.GONE);
                    Constants.writelog("AppointmentListActivity", "getAppointmentList 139:" + t.getMessage());
                }
            });
        } else {
            Common.showToast(TakeAppointmentListActivity.this, getString(R.string.msg_connection));
        }
    }

    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        vg.setPadding(0,0,0,10);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            if (j == 0) {
                vgTab.setBackgroundColor(getResources().getColor(R.color.calendar_homwwork_bg));
            }
            if (j == 1) {
                vgTab.setBackgroundColor(getResources().getColor(R.color.txt_green_bg));
            }
            if (j == 2) {
                vgTab.setBackgroundColor(getResources().getColor(R.color.txt_red_bg));
            }
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setAllCaps(false);
                    ((TextView) tabViewChild).setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                }
            }
        }
    }

    private class AppointmentListPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTitleNames = new ArrayList();

        private AppointmentListPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleNames.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleNames.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                TakeAppointmentListActivity.this.finish();
                Common.onBackClickAnimation(TakeAppointmentListActivity.this);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
