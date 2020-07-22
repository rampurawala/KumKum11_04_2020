package com.expedite.apps.kumkum.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.fragment.PracticeTestFragment;
import com.expedite.apps.kumkum.model.AppService;
import com.expedite.apps.kumkum.model.Contact;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BtmNavigationActivity extends BaseActivity {
    FragmentManager manager;
    FragmentTransaction transaction;
    String jsonListPending, jsonListCompleted, selectedName = "", selectedId = "", jsonList = "",isFrom="";
    List<AppService.ListArray> listPending = new ArrayList<>();
    List<AppService.ListArray> listCompleted = new ArrayList<>();
   public ProgressBar prg_test;

    BottomNavigationView navigation;
    LinearLayout empty_practiceTestBtm;
    static String tag = "BtmNavigationActivity";
    List<AppService.ListArray> mainFilteredlist = new ArrayList<>();
    BroadcastReceiver mReceiverFilter, mReceiverFilterCompleted;
    ArrayList<Contact> contactsTestList = new ArrayList<>();
    Common common;
    // private TextView mTextMessage;
    private AdView mAdView;
    boolean isBroadCastReceive = false;
    // SwipeRefreshLayout mSwipeLayout;
    private LocalBroadcastManager mBroadcastManagerCompleted, mBroadcastManager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            try {
                Fragment fragment = null;
                if (isOnline()) {

                    switch (item.getItemId()) {

                        case R.id.navigation_pending:
                            empty_practiceTestBtm.setVisibility(View.GONE);
                            fragment = new PracticeTestFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("flag", "1");
                            bundle.putString("testList", jsonListPending);
                          //  bundle.putString("context", BtmNavigationActivity.this);
                            fragment.setArguments(bundle);
                            break;
                        case R.id.navigation_completed:
                            empty_practiceTestBtm.setVisibility(View.GONE);
                            fragment = new PracticeTestFragment();
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("flag", "0");
                            bundle1.putString("testList", jsonListCompleted);
                            fragment.setArguments(bundle1);
                            break;
                    }
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.f1, fragment).commit();
                    //  transaction.disallowAddToBackStack();
                    return true;
                } else {
                    empty_practiceTestBtm.setVisibility(View.GONE);
                    fragment = new PracticeTestFragment();
                  /*  Bundle bundle = new Bundle();
                    bundle.putString("flag", "1");
                    bundle.putString("testList", jsonListPending);
                    fragment.setArguments(bundle);*/
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.f1, fragment).commit();
                  /*  prg_test.setVisibility(View.GONE);
                    navigation.setVisibility(View.VISIBLE);
                    empty_practiceTestBtm.setVisibility(View.VISIBLE);*/
                    Toast.makeText(BtmNavigationActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Constants.writelog("BtmNavigationActivity", "onNavigationItemSelected 1000:" + e.getMessage());
            }
            return true;
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btm_navigation);
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                setTitle("Practice Test");
            }
            if (getIntent() != null && getIntent().getExtras() != null && !getIntent().getExtras().isEmpty()) {
                jsonList = getIntent().getExtras().getString("jsonList", "");
                selectedId = getIntent().getExtras().getString("selectedId", "");
                isFrom = getIntent().getExtras().getString("isFrom", "");
                selectedName = getIntent().getExtras().getString("selectedName", "");
            }
            setTitle(selectedName + "Practice Test");
            mAdView = (AdView) findViewById(R.id.adView);

            showBannerAd(mAdView, ActivityNames.BtmNavigationActivity);
            showFullScreenAd(BtmNavigationActivity.this, ActivityNames.BtmNavigationActivity);


            // mTextMessage = (TextView) findViewById(R.id.message);
            prg_test = findViewById(R.id.prg_test);
            empty_practiceTestBtm = findViewById(R.id.empty_practiceTestBtm);
            common = new Common(this);
            navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
           /* mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    try {
                        if (isOnline()) {
                            getPracticeTestList();
                        } else {
                            mSwipeLayout.setRefreshing(false);
                        }
                    } catch (Exception ex) {
                        Constants.writelog("BtmNaviagtionActivity", "InitListioner 149::" + ex.getMessage());
                    }
                }
            });*/
            manager = getSupportFragmentManager();
            // set Fragmentclass Arguments
            // getPracticeTestList();
            if(isFrom!=null && isFrom.equalsIgnoreCase("notification")){
                getPracticeTestList();
            }else {
                Gson gson = new Gson();
                Type type = new TypeToken<List<AppService.ListArray>>() {
                }.getType();
                mainFilteredlist = gson.fromJson(jsonList, type);
                getPracticeTestList(mainFilteredlist);
            }
            mBroadcastManager = ((MyApplication) getApplicationContext())
                    .getLocalBroadcastInstance();
            mReceiverFilter = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, final Intent intent) {
                    isBroadCastReceive = true;
                    getPracticeTestList();
                }
            };
            mBroadcastManagerCompleted = ((MyApplication) getApplicationContext())
                    .getLocalBroadcastInstance();
            mReceiverFilterCompleted = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, final Intent intent) {
                    if (!isOnline()) {
                        empty_practiceTestBtm.setVisibility(View.GONE);
                        Fragment fragment = new PracticeTestFragment();
                  /*  Bundle bundle = new Bundle();
                    bundle.putString("flag", "1");
                    bundle.putString("testList", jsonListPending);
                    fragment.setArguments(bundle);*/
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.f1, fragment);
                        transaction.commitAllowingStateLoss();
                  /*  prg_test.setVisibility(View.GONE);
                    navigation.setVisibility(View.VISIBLE);
                    empty_practiceTestBtm.setVisibility(View.VISIBLE);*/
                        Toast.makeText(BtmNavigationActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
                      /*  prg_test.setVisibility(View.GONE);
                        navigation.setVisibility(View.VISIBLE);
                        empty_practiceTestBtm.setVisibility(View.VISIBLE);*/
                    }
                }
            };
        } catch (Exception e) {
            Constants.writelog("BtmNavigationActivity", "onCreate 101:" + e.getMessage());
        }
        // streamFragment.setArguments(bundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBroadcastManager.registerReceiver(mReceiverFilter, new IntentFilter(
                getResources().getString(R.string.broadcast_pending_key)));
        mBroadcastManagerCompleted.registerReceiver(mReceiverFilterCompleted, new IntentFilter(
                getResources().getString(R.string.broadcast_completed_key)));

    }


    private void getPracticeTestList(List<AppService.ListArray> filterList) {
        try {
            navigation.setVisibility(View.VISIBLE);
            final String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            final String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
            prg_test.setVisibility(View.GONE);
            navigation.setSelectedItemId(R.id.navigation_pending);
            navigation.getMenu().getItem(0).setChecked(true);
            //   tmps.getStrlist().get(1).setSixth("31-03-2020 16:53:00");
            //   tmps.getStrlist().get(2).setSeventh("2");
            // tmps.getStrlist().get(1).setSeventh("15");
            PracticeTestFragment practiceTestFragment = new PracticeTestFragment();
            int size = filterList.size();
            listCompleted.clear();
            listPending.clear();

                           /* Calendar cal = Calendar.getInstance();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss 'GMT'Z yyyy");
                            System.out.println(dateFormat.format(cal.getTime()));
                            */

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            // String currentDateandTimeTemp = df.format(new Date());
            Calendar calCurrent = Calendar.getInstance();
            // String currentDiffTime=df.format(calCurrent.getTime());
            // SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY HH:mm");
            // calCurrent.setTime(sdf.parse(currentDateandTimeTemp));

//Log.e("CurrentTime",sdf.format(calCurrent.getTime()));
//Log.e("currentDiffTime",currentDiffTime);
            Constants.Logwrite("CurrentTimeMillis", String.valueOf(calCurrent.getTimeInMillis()));
            //Log.e("currentDateandTimeTemp", currentDateandTimeTemp);
            //28-03-2020 14:20
            //  "dd-MM-YYYY HH:mm

           // Calendar calGiven = Calendar.getInstance();
           // calGiven.clear();
           // SimpleDateFormat sdfGiven = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            for (int i = 0; i < size; i++) {
               /* if (filterList.get(i).getSixth() != null && !filterList.get(i).getSixth().equalsIgnoreCase("0")) {
                    //30-03-2020 20:15
                    String splitDateTime[] = filterList.get(i).getSixth().split(" ");
                    String splitDate[] = splitDateTime[0].split("-");
                    String splitTime[] = splitDateTime[1].split(":");
                    calGiven.set(Calendar.MONTH, Integer.parseInt(splitDate[1]) - 1);
                    calGiven.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[0]));
                    calGiven.set(Calendar.YEAR, Integer.parseInt(splitDate[2]));
                    calGiven.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTime[0]));
                    calGiven.set(Calendar.MINUTE, Integer.parseInt(splitTime[1]));
                    if (splitTime.length >= 3)
                        calGiven.set(Calendar.SECOND, Integer.parseInt(splitTime[2]));
                    // calGiven.setTime(sdfGiven.parse(tmps.getStrlist().get(i).getSixth()));
                    Constants.Logwrite("CurrentTime", String.valueOf(calGiven.getTime()));
                    Constants.Logwrite("CurrentTimeMillis", String.valueOf(calGiven.getTimeInMillis()));
                }*/
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Contact>>() {
                }
                        .getType();

                contactsTestList = gson.fromJson(common.getSession(Constants.PractiseTestData), type);
                int index = -1;
                if (contactsTestList != null && contactsTestList.size() > 0) {
                    index = contactsTestList.indexOf(new Contact(filterList.get(i).getFirst(), Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId)));
                }
                String testtime = "0";

                //     int index=  contactsTestList.indexOf(testId);
                /*Calendar calGivenStart = Calendar.getInstance();
                if (index != -1) {
                    testtime = contactsTestList.get(index).getTestExpireDateAfterStart();

                    //   int index=  contactsTestList.indexOf(tmps.getStrlist().get(i).getFirst());
                    //  testtime=contactsTestList.get(index).getTestExpireDateAfterStart();
                    SimpleDateFormat sdfGivenStart = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
                    String splitDateTime[] = testtime.split(" ");
                    String splitDate[] = splitDateTime[0].split("-");
                    String splitTime[] = splitDateTime[1].split(":");
                    calGivenStart.set(Calendar.MONTH, Integer.parseInt(splitDate[1]) - 1);
                    calGivenStart.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[0]));
                    calGivenStart.set(Calendar.YEAR, Integer.parseInt(splitDate[2]));
                    calGivenStart.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTime[0]));
                    calGivenStart.set(Calendar.MINUTE, Integer.parseInt(splitTime[1]));
                    if (splitTime.length >= 3)
                        calGivenStart.set(Calendar.SECOND, Integer.parseInt(splitTime[2]));
                  Constants.Logwrite("CurrentTime", sdfGiven.format(calGivenStart.getTime()));
                    Constants.Logwrite("CurrentTimeMillis", String.valueOf(calGivenStart.getTimeInMillis()));
                    // calGivenStart.setTime(sdfGivenStart.parse(testtime));// all done
                }*/

                if (filterList.get(i).getThird()!=null && (filterList.get(i).getThird().equals("0") ||filterList.get(i).getThird().equals("2") )/*(filterList.get(i).getSixth() != null && !filterList.get(i).getSixth().equalsIgnoreCase("0") && (calCurrent.getTimeInMillis() >= calGiven.getTimeInMillis())) || (index != -1 && (calCurrent.getTimeInMillis() >= calGivenStart.getTimeInMillis()))*/) {
                    if (/*((filterList.get(i).getSixth() != null && !filterList.get(i).getSixth().equalsIgnoreCase("0") && (calCurrent.getTimeInMillis() >= calGiven.getTimeInMillis())) || filterList.get(i).getThird().equals("0")) && */index != -1) {
                        contactsTestList.remove(index);
                        Gson gson1 = new Gson();
                        String jsonMenu = gson1.toJson(contactsTestList);
                        common.setSession(Constants.PractiseTestData, jsonMenu);
                    }
                    if (filterList.get(i).getThird()!=null && filterList.get(i).getThird().equals("2")) {
                        String ansString="";
                        List<Contact> testDetails = db.getPracticeTestDetailByID(Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId), Integer.parseInt(filterList.get(i).getFirst()));
                        if (testDetails != null && testDetails.size() > 0) /* {
                           ansString = testDetails.get(0).getTestAnsString();
                        }
                        if (ansString == null) {
                            ansString = "";
                        }

                        if(!ansString.isEmpty())*/
                            filterList.get(i).setShowUploadBtn(true);
                        else
                            filterList.get(i).setShowUploadBtn(false);

                        filterList.get(i).setTestExpired(true);
                    }
                    /* else if (index != -1 && (calCurrent.getTimeInMillis() >= calGivenStart.getTimeInMillis())) {
                        filterList.get(i).setTestTimeOut(true);
                    }*/
                  //  filterList.get(i).setThird("0");
                    listCompleted.add(filterList.get(i));
                } else {
                    String ansString="";
                    List<Contact> testDetails = db.getPracticeTestDetailByID(Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId), Integer.parseInt(filterList.get(i).getFirst()));
                    if (testDetails != null && testDetails.size() > 0) {
                     /*   ansString = testDetails.get(0).getTestAnsString();

                    if (ansString == null) {
                        ansString = "";
                    }

                    if(!ansString.isEmpty())*/
                        filterList.get(i).setTestStarted(true);

                    }else
                        filterList.get(i).setTestStarted(false);
                    listPending.add(filterList.get(i));
                }
            }

            Bundle bundle = new Bundle();
            bundle.putString("flag", "1");
            Gson gson = new Gson();
            jsonListPending = gson.toJson(listPending);

            Gson gson1 = new Gson();
            jsonListCompleted = gson1.toJson(listCompleted);

            bundle.putString("testList", jsonListPending);
            practiceTestFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.f1, practiceTestFragment)/*.disallowAddToBackStack()*/
                    .commit();
        } catch (Exception ex) {
            prg_test.setVisibility(View.GONE);
            navigation.setVisibility(View.VISIBLE);
            Constants.writelog(tag, "getPracticetestLiat 107:" + ex.getMessage());
        }

        //mcommon.setSession(Constants.LastAppControlCall, Today.monthDay + "2");

    }


    private void getPracticeTestList() {
        try{
        if (isOnline()) {
            navigation.setVisibility(View.GONE);
            prg_test.setVisibility(View.VISIBLE);
            final String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            final String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
            String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
            Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                    .GetPracticeTestList(mStudentId, mSchoolId, mYearId, String.valueOf(SchoolDetails.appname), Constants.CODEVERSION, Constants.PLATFORM);
            call.enqueue(new Callback<AppService>() {
                @Override
                public void onResponse(Call<AppService> call, Response<AppService> response) {
                    try {
                        navigation.setVisibility(View.VISIBLE);

                        prg_test.setVisibility(View.GONE);
                        AppService tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1") && !tmps.getStrResult().isEmpty()) {
                          /*  Time Today = new Time();
                            Today.setToNow();*/
                            navigation.setSelectedItemId(R.id.navigation_pending);
                            navigation.getMenu().getItem(0).setChecked(true);
                            //   tmps.getStrlist().get(1).setSixth("31-03-2020 16:53:00");
                            //   tmps.getStrlist().get(2).setSeventh("2");
                            // tmps.getStrlist().get(1).setSeventh("15");
                            PracticeTestFragment practiceTestFragment = new PracticeTestFragment();
                            int size = tmps.getStrlist().size();
                            listCompleted.clear();
                            listPending.clear();

                           /* Calendar cal = Calendar.getInstance();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss 'GMT'Z yyyy");
                            System.out.println(dateFormat.format(cal.getTime()));
                            */

                            ArrayList<AppService.ListArray> filteredList=new ArrayList();
                            for(int j=0;j<tmps.getStrlist().size();j++){
                                if(tmps.getStrlist().get(j).getEighth().equalsIgnoreCase(selectedId)){
                                    filteredList.add(tmps.getStrlist().get(j));
                                }
                            }

                            getPracticeTestList(filteredList);



                          /*  SimpleDateFormat df = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
                            // String currentDateandTimeTemp = df.format(new Date());
                            Calendar calCurrent = Calendar.getInstance();
                            // String currentDiffTime=df.format(calCurrent.getTime());
                            // SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY HH:mm");
                            // calCurrent.setTime(sdf.parse(currentDateandTimeTemp));

//Log.e("CurrentTime",sdf.format(calCurrent.getTime()));
//Log.e("currentDiffTime",currentDiffTime);
                            Constants.Logwrite("CurrentTimeMillis", String.valueOf(calCurrent.getTimeInMillis()));
                            //Log.e("currentDateandTimeTemp", currentDateandTimeTemp);
                            //28-03-2020 14:20
                            //  "dd-MM-YYYY HH:mm

                            Calendar calGiven = Calendar.getInstance();
                            calGiven.clear();
                            SimpleDateFormat sdfGiven = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");

                            for (int i = 0; i < size; i++) {
                                if (tmps.getStrlist().get(i).getSixth() != null && !tmps.getStrlist().get(i).getSixth().equalsIgnoreCase("0")) {
                                    //30-03-2020 20:15
                                    String splitDateTime[] = tmps.getStrlist().get(i).getSixth().split(" ");
                                    String splitDate[] = splitDateTime[0].split("-");
                                    String splitTime[] = splitDateTime[1].split(":");
                                    calGiven.set(Calendar.MONTH, Integer.parseInt(splitDate[1]) - 1);
                                    calGiven.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[0]));
                                    calGiven.set(Calendar.YEAR, Integer.parseInt(splitDate[2]));
                                    calGiven.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTime[0]));
                                    calGiven.set(Calendar.MINUTE, Integer.parseInt(splitTime[1]));
                                    if (splitTime.length >= 3)
                                        calGiven.set(Calendar.SECOND, Integer.parseInt(splitTime[2]));
                                    // calGiven.setTime(sdfGiven.parse(tmps.getStrlist().get(i).getSixth()));
                                    Constants.Logwrite("CurrentTime", String.valueOf(calGiven.getTime()));
                                    Constants.Logwrite("CurrentTimeMillis", String.valueOf(calGiven.getTimeInMillis()));
                                }
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<Contact>>() {
                                }
                                        .getType();

                                contactsTestList = gson.fromJson(common.getSession(Constants.PractiseTestData), type);
                                int index = -1;
                                if (contactsTestList != null && contactsTestList.size() > 0) {
                                    index = contactsTestList.indexOf(new Contact(tmps.getStrlist().get(i).getFirst(), Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId)));
                                }
                                String testtime = "0";

                                //     int index=  contactsTestList.indexOf(testId);
                                Calendar calGivenStart = Calendar.getInstance();
                                if (index != -1) {
                                    testtime = contactsTestList.get(index).getTestExpireDateAfterStart();

                                    //   int index=  contactsTestList.indexOf(tmps.getStrlist().get(i).getFirst());
                                    //  testtime=contactsTestList.get(index).getTestExpireDateAfterStart();
                                    SimpleDateFormat sdfGivenStart = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss", Locale.ENGLISH);
                                    String splitDateTime[] = testtime.split(" ");
                                    String splitDate[] = splitDateTime[0].split("-");
                                    String splitTime[] = splitDateTime[1].split(":");
                                    calGivenStart.set(Calendar.MONTH, Integer.parseInt(splitDate[1]) - 1);
                                    calGivenStart.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[0]));
                                    calGivenStart.set(Calendar.YEAR, Integer.parseInt(splitDate[2]));
                                    calGivenStart.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTime[0]));
                                    calGivenStart.set(Calendar.MINUTE, Integer.parseInt(splitTime[1]));
                                    if (splitTime.length >= 3)
                                        calGivenStart.set(Calendar.SECOND, Integer.parseInt(splitTime[2]));
                                    Constants.Logwrite("CurrentTime", sdfGiven.format(calGivenStart.getTime()));
                                    Constants.Logwrite("CurrentTimeMillis", String.valueOf(calGivenStart.getTimeInMillis()));
                                    // calGivenStart.setTime(sdfGivenStart.parse(testtime));// all done
                                }

                                if (tmps.getStrlist().get(i).getThird().equals("0") || (tmps.getStrlist().get(i).getSixth() != null && !tmps.getStrlist().get(i).getSixth().equalsIgnoreCase("0") && (calCurrent.getTimeInMillis() >= calGiven.getTimeInMillis())) || (index != -1 && (calCurrent.getTimeInMillis() >= calGivenStart.getTimeInMillis()))) {
                                    if (((tmps.getStrlist().get(i).getSixth() != null && !tmps.getStrlist().get(i).getSixth().equalsIgnoreCase("0") && (calCurrent.getTimeInMillis() >= calGiven.getTimeInMillis())) || tmps.getStrlist().get(i).getThird().equals("0")) && index != -1) {
                                        contactsTestList.remove(index);
                                        Gson gson1 = new Gson();
                                        String jsonMenu = gson1.toJson(contactsTestList);
                                        common.setSession(Constants.PractiseTestData, jsonMenu);
                                    }
                                    if (tmps.getStrlist().get(i).getSixth() != null && !tmps.getStrlist().get(i).getSixth().equalsIgnoreCase("0") && (calCurrent.getTimeInMillis() >= calGiven.getTimeInMillis())) {
                                        tmps.getStrlist().get(i).setTestExpired(true);
                                    } else if (index != -1 && (calCurrent.getTimeInMillis() >= calGivenStart.getTimeInMillis())) {
                                        tmps.getStrlist().get(i).setTestTimeOut(true);
                                    }
                                    tmps.getStrlist().get(i).setThird("0");
                                    listCompleted.add(tmps.getStrlist().get(i));
                                } else {
                                    listPending.add(tmps.getStrlist().get(i));
                                }
                            }

                            Bundle bundle = new Bundle();
                            bundle.putString("flag", "1");
                            Gson gson = new Gson();
                            jsonListPending = gson.toJson(listPending);

                            Gson gson1 = new Gson();
                            jsonListCompleted = gson1.toJson(listCompleted);

                            bundle.putString("testList", jsonListPending);
                            practiceTestFragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.f1, practiceTestFragment)*//*.disallowAddToBackStack()*//*
                                    .commit();
*/
                            //mcommon.setSession(Constants.LastAppControlCall, Today.monthDay + "2");
                        } else {
                            prg_test.setVisibility(View.GONE);
                            navigation.setVisibility(View.GONE);
                            empty_practiceTestBtm.setVisibility(View.VISIBLE);
                            Toast.makeText(BtmNavigationActivity.this, "No Test Found!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        //   mSwipeLayout.setRefreshing(false);
                        prg_test.setVisibility(View.GONE);
                        navigation.setVisibility(View.VISIBLE);
                        Constants.writelog("BtmNavigationActivity", "getPracticetestLiat 361:" + ex.getMessage());
                    }/*finally {
                        mSwipeLayout.setRefreshing(false);
                    }*/
                }

                @Override
                public void onFailure(Call<AppService> call, Throwable t) {
                    prg_test.setVisibility(View.GONE);
                    //  mSwipeLayout.setRefreshing(false);
                    navigation.setVisibility(View.VISIBLE);
                    Constants.writelog("BtmNavigationActivity", "getPracticetestLiat 113:" + t.getMessage());
                }
            });
        } else {
            //  mSwipeLayout.setRefreshing(false);
            prg_test.setVisibility(View.GONE);
            navigation.setVisibility(View.VISIBLE);
            empty_practiceTestBtm.setVisibility(View.VISIBLE);
            Toast.makeText(this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
        }}catch(Exception e){
            Constants.writelog(tag, "getPracticetestLiat 107:" + e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                  //  onBackClickAnimation();
                    break;
            }
        } catch (Exception ex) {
            Constants.writelog("onOptionsItemSelected 177:", ex.getMessage());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearStack();
        if (isBroadCastReceive == true ||(isFrom!=null && isFrom.equalsIgnoreCase("notification"))) {
            Intent i = new Intent(BtmNavigationActivity.this, PracticeTestMainSubjectActivity.class);
            i.putExtra("isFrom",isFrom);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            BtmNavigationActivity.this.finish();
        }
        BtmNavigationActivity.this.finish();
        onBackClickAnimation();
       /* Intent i=new Intent(BtmNavigationActivity.this,HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);*/
    }

    public void clearStack() {
        //Here we are clearing back stack fragment entries
        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }

        //Here we are removing all the fragment that are shown here
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
                Fragment mFragment = getSupportFragmentManager().getFragments().get(i);
                if (mFragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(mFragment).commit();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiverFilter != null)
            mBroadcastManager.unregisterReceiver(mReceiverFilter);
        if (mReceiverFilterCompleted != null)
            mBroadcastManagerCompleted.unregisterReceiver(mReceiverFilterCompleted);
    }
}
