package com.expedite.apps.kumkum.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.adapter.MainPracticeTestListAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.fragment.PracticeTestFragment;
import com.expedite.apps.kumkum.model.AppService;
import com.expedite.apps.kumkum.model.Contact;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PracticeTestMainSubjectActivity extends BaseActivity {
    RecyclerView subWise;
    ProgressBar prg_test;
    LinearLayout empty_practiceTestBtm;
    Common commonClass;
    String ansString = "", testid = "", isFrom = "";
    ArrayList<String> unique_subject_id = new ArrayList<>();
    MainPracticeTestListAdapter mainPracticeTestListAdapter;
    ArrayList<String> unique_subject_name = new ArrayList<>();
    List<String> unique_count = new ArrayList<>();
    AdView mAdView;
    int testrowid = 0;
    ArrayList<Contact> contactsTestList = new ArrayList<>();
    List<AppService.ListArray> mainArray = new ArrayList<>();
    static String tag = "PracticeTestMainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_test_main_subject);
        if (getIntent() != null && getIntent().getExtras() != null && !getIntent().getExtras().isEmpty()) {
            isFrom = getIntent().getExtras().getString("isFrom", "");
        }
        subWise = findViewById(R.id.subWise);
        empty_practiceTestBtm = findViewById(R.id.empty_practiceTestBtm);
        prg_test = findViewById(R.id.progressbar);
        subWise.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Activity abc = this;
        commonClass = new Common(this);
        Constants.setActionbar(getSupportActionBar(), abc, getApplicationContext(),
                "Practice Test", "Practice Test", ActivityNames.PracticeTestMainSubjectActivity);
        mAdView = (AdView) findViewById(R.id.adView);
        showBannerAd(mAdView, ActivityNames.PracticeTestMainSubjectActivity);
        getPracticeTestList();
    }

    //test on 10/07/2020
    private void getPracticeTestList() {
        if (isOnline()) {
            prg_test.setVisibility(View.VISIBLE);
            empty_practiceTestBtm.setVisibility(View.GONE);
            final String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            final String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
            String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
            Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                    .GetPracticeTestList(mStudentId, mSchoolId, mYearId, String.valueOf(SchoolDetails.appname), Constants.CODEVERSION, Constants.PLATFORM);
            call.enqueue(new Callback<AppService>() {
                @Override
                public void  onResponse(Call<AppService> call, Response<AppService> response) {
                    try {
                        prg_test.setVisibility(View.GONE);

                        AppService tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1") && !tmps.getStrResult().isEmpty()) {
                            PracticeTestFragment practiceTestFragment = new PracticeTestFragment();
                            int size = tmps.getStrlist().size();
                            mainArray.clear();
                            unique_subject_name.clear();
                            unique_subject_id.clear();
                            unique_count.clear();
                            int countPending = 0, totalCount = 0;
                            if (size > 0) {
                                mainArray = tmps.getStrlist();
                              //  setPracticeTestData(mainArray);
                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                              //  Calendar calCurrent = Calendar.getInstance();

                                //Constants.Logwrite("CurrentTimeMillis", String.valueOf(calCurrent.getTimeInMillis()));
                                 /*Calendar calGiven = Calendar.getInstance();
                                calGiven.clear();*/
                               // SimpleDateFormat sdfGiven = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                                for (int i = 0; i < size; i++) {
                                  //  Calendar calGiven = Calendar.getInstance();
                                    //  calGiven.clear();
                                   /* if (mainArray.get(i).getSixth() != null && !mainArray.get(i).getSixth().equalsIgnoreCase("0")) {
                                        //30-03-2020 20:15
                                        String splitDateTime[] = mainArray.get(i).getSixth().split(" ");
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

                                    if (unique_subject_id != null && !unique_subject_id.contains(tmps.getStrlist().get(i).getEighth())) {
                                        countPending = 0;
                                      /*  if ((tmps.getStrlist().get(i).getThird().equalsIgnoreCase("1") && (mainArray.get(i).getSixth() != null && !mainArray.get(i).getSixth().equalsIgnoreCase("0") && (calGiven.getTimeInMillis() >= calCurrent.getTimeInMillis()))) || (tmps.getStrlist().get(i).getThird().equalsIgnoreCase("1") && mainArray.get(i).getSixth() != null && mainArray.get(i).getSixth().equalsIgnoreCase("0")))
                                            countPending++;
                                        else*/ if (tmps.getStrlist().get(i).getThird().equalsIgnoreCase("1")) {
                                           /* if (mainArray.get(i).getSixth() != null) {

                                            } else {*/
                                                countPending++;
                                            /*}*/
                                        }

                                        //    countPending = 0;

                                        unique_subject_id.add(tmps.getStrlist().get(i).getEighth());
                                        unique_subject_name.add(tmps.getStrlist().get(i).getNineth());
                                        unique_count.add(unique_subject_id.size() - 1, countPending + "");

                                    } else {
                                        /*if ((tmps.getStrlist().get(i).getThird().equalsIgnoreCase("1") && (mainArray.get(i).getSixth() != null && !mainArray.get(i).getSixth().equalsIgnoreCase("0") && (calGiven.getTimeInMillis() >= calCurrent.getTimeInMillis()))) || (tmps.getStrlist().get(i).getThird().equalsIgnoreCase("1") && mainArray.get(i).getSixth() != null && mainArray.get(i).getSixth().equalsIgnoreCase("0"))) {
                                            countPending++;
                                        } else*/ if (tmps.getStrlist().get(i).getThird().equalsIgnoreCase("1")) {
                                          /*  if (mainArray.get(i).getSixth() != null) {

                                            } else {*/
                                                countPending++;
                                          /*  }*/
                                        }

                                        unique_count.add(unique_subject_id.size() - 1, countPending + "");
                                    }
                                   /* if (tmps.getStrlist().get(i).getThird().equalsIgnoreCase("1")) {
                                        countPending++;

                                    }*/
                                }
                            } else {
                                prg_test.setVisibility(View.GONE);
                                empty_practiceTestBtm.setVisibility(View.VISIBLE);
                            }
                            mainPracticeTestListAdapter = new MainPracticeTestListAdapter(PracticeTestMainSubjectActivity.this, unique_subject_name, unique_subject_id, mainArray, unique_count);
                            subWise.setAdapter(mainPracticeTestListAdapter);
                            mainPracticeTestListAdapter.notifyDataSetChanged();
                        } else {
                            prg_test.setVisibility(View.GONE);
                            empty_practiceTestBtm.setVisibility(View.VISIBLE);
                            Toast.makeText(PracticeTestMainSubjectActivity.this, "No Test Found!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        //   mSwipeLayout.setRefreshing(false);
                        prg_test.setVisibility(View.GONE);
                        empty_practiceTestBtm.setVisibility(View.VISIBLE);
                        Constants.writelog(tag, "getPracticetestLiat 107:" + ex.getMessage());
                    }/*finally {
                        mSwipeLayout.setRefreshing(false);
                    }*/
                }

                @Override
                public void onFailure(Call<AppService> call, Throwable t) {
                    prg_test.setVisibility(View.GONE);
                    empty_practiceTestBtm.setVisibility(View.VISIBLE);
                    Constants.writelog("BtmNavigationActivity", "getPracticetestLiat 113:" + t.getMessage());
                }
            });
        } else {
            prg_test.setVisibility(View.GONE);
            empty_practiceTestBtm.setVisibility(View.VISIBLE);

            Toast.makeText(this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
        }
    }


    //to save expired test data to server
    private void setPracticeTestData(final List<AppService.ListArray> filterList) {
        try {
            /* Calendar cal = Calendar.getInstance();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss 'GMT'Z yyyy");
                            System.out.println(dateFormat.format(cal.getTime()));
                            */
            final String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            final String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());

            int size = filterList.size();
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
            SimpleDateFormat sdfGiven = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            for (int i = 0; i < size; i++) {
              /*  if (filterList.get(i).getSixth() != null && !filterList.get(i).getSixth().equalsIgnoreCase("0")) {
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
              /*  Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Contact>>() {
                }
                        .getType();

                contactsTestList = gson.fromJson(commonClass.getSession(Constants.PractiseTestData), type);
                int index = -1;
                if (contactsTestList != null && contactsTestList.size() > 0) {
                    index = contactsTestList.indexOf(new Contact(filterList.get(i).getFirst(), Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId)));
                }*/
                String testtime = "0";

                //     int index=  contactsTestList.indexOf(testId);
              //  Calendar calGivenStart = Calendar.getInstance();
               /* if (index != -1) {
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
                testid = filterList.get(i).getFirst();

                if (filterList.get(i).getThird().equals("0") ||filterList.get(i).getThird().equals("2")/* (filterList.get(i).getSixth() != null && !filterList.get(i).getSixth().equalsIgnoreCase("0") && (calCurrent.getTimeInMillis() >= calGiven.getTimeInMillis())) || (index != -1 && (calCurrent.getTimeInMillis() >= calGivenStart.getTimeInMillis()))*/) {
                    List<Contact> testDetails = db.getPracticeTestDetailByID(Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId), Integer.parseInt(testid));
                    ansString = "";
                    if (testDetails != null && testDetails.size() > 0) {
                        ansString = testDetails.get(0).getTestAnsString();
                        testrowid = testDetails.get(0).getROWID();

                        if (ansString == null) {
                            ansString = "";
                        }
                        Log.e("ansString", ansString);
                        if (ansString != null && !ansString.isEmpty()) {
                            String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
                            Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                                    .SetPracticeTestAnsV2(mStudentId, mSchoolId, mYearId, testid, ansString, String.valueOf(SchoolDetails.appname), Constants.CODEVERSION, Constants.PLATFORM);
                            call.enqueue(new Callback<AppService>() {
                                @Override
                                public void onResponse(Call<AppService> call, Response<AppService> response) {
                                    try {

                                        AppService tmps = response.body();
                                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                                && tmps.getResponse().equalsIgnoreCase("1") && !tmps.getStrResult().isEmpty()) {
                                            db.deleteTestRecords(testrowid);

                                        }
                                    } catch (Exception ex) {
                                        Constants.writelog("PracticeTestQAActivity", "setTestAns 2062:" + ex.getMessage());
                                    } finally {
                                        ansString = "";
                                    }
                                }

                                @Override
                                public void onFailure(Call<AppService> call, Throwable t) {
                                    Constants.writelog("PracticeTestQAActivity", "setTestAns 1589:" + t.getMessage());
                                }
                            });

                        } else {
                            db.deleteTestRecords(testrowid);
                        }
                    }
                   /* if (((filterList.get(i).getSixth() != null && !filterList.get(i).getSixth().equalsIgnoreCase("0") && (calCurrent.getTimeInMillis() >= calGiven.getTimeInMillis())) || filterList.get(i).getThird().equals("0")) && index != -1) {
                        contactsTestList.remove(index);
                        Gson gson1 = new Gson();
                        String jsonMenu = gson1.toJson(contactsTestList);
                        commonClass.setSession(Constants.PractiseTestData, jsonMenu);
                    }
                    if (filterList.get(i).getSixth() != null && !filterList.get(i).getSixth().equalsIgnoreCase("0") && (calCurrent.getTimeInMillis() >= calGiven.getTimeInMillis())) {
                        filterList.get(i).setTestExpired(true);
                    } else if (index != -1 && (calCurrent.getTimeInMillis() >= calGivenStart.getTimeInMillis())) {
                        filterList.get(i).setTestTimeOut(true);
                    }*/
                    //   filterList.get(i).setThird("0");
                    //  listCompleted.add(filterList.get(i));
                }/* else {
                    listPending.add(filterList.get(i));
                }*/
            }

        } catch (Exception ex) {
            Constants.writelog(tag, " 107:" + ex.getMessage());
        }

        //mcommon.setSession(Constants.LastAppControlCall, Today.monthDay + "2");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isFrom != null && isFrom.equalsIgnoreCase("notification")) {
            Intent i = new Intent(PracticeTestMainSubjectActivity.this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        PracticeTestMainSubjectActivity.this.finish();
        onBackClickAnimation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
