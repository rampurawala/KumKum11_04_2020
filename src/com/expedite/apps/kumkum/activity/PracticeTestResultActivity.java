package com.expedite.apps.kumkum.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.model.AppService;
import com.google.android.gms.ads.AdView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PracticeTestResultActivity extends BaseActivity {
    ProgressBar prg_summmary;
    String testId, testName, flag = "", refereshflag = "0", viewReview = "";
    TextView totalQuestion, value1, value2, value3, value4, value5, totalCorrect, totalWrong, totalTime, totalScore, congo, finished, valueSkip, totalSkip,tvtest,tvlabeltest,tvsub,tvlabelsub,tvname,tvlabelname,tvclass,tvlabelclass,tvnameStudent,testNotAttemptedTv;
    Button btnReview;
    LinearLayout marksLyt, mainLyt, lytSkip;
    RelativeLayout noInternetLayout,testNotAttempted;
    boolean istestExpire;
    Button btnRetry;
    Common common;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_test_result);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            setTitle("Test Summary");
        }
        common = new Common(this);
        mAdView = (AdView) findViewById(R.id.adView);
        marksLyt = findViewById(R.id.marksLyt);
        congo = findViewById(R.id.congo);
        mainLyt = findViewById(R.id.mainLyt);
        lytSkip = findViewById(R.id.lytSkip);
        valueSkip = findViewById(R.id.valueSkip);
        totalSkip = findViewById(R.id.totalSkip);
        btnRetry = findViewById(R.id.btnRetry);
        testNotAttemptedTv = findViewById(R.id.testNotAttemptedTv);
        testNotAttempted = findViewById(R.id.testNotAttempted);
        tvnameStudent = findViewById(R.id.tvnameStudent);
      //  tvlabelname = findViewById(R.id.tvlabelname);
        //tvname = findViewById(R.id.tvname);
        tvlabelsub = findViewById(R.id.tvlabelsub);
        tvsub = findViewById(R.id.tvsub);
        tvlabeltest = findViewById(R.id.tvlabeltest);
        tvtest = findViewById(R.id.tvtest);
        tvlabelclass = findViewById(R.id.tvlabelclass);
        tvclass = findViewById(R.id.tvclass);
        noInternetLayout = findViewById(R.id.noInternetLayout);
        finished = findViewById(R.id.finished);
        showBannerAd(mAdView, ActivityNames.PracticeTestResultActivity);
        showFullScreenAd(PracticeTestResultActivity.this, ActivityNames.PracticeTestResultActivity);
        prg_summmary = findViewById(R.id.prg_summmary);
        totalQuestion = findViewById(R.id.totalQue);
        totalCorrect = findViewById(R.id.totalCorrect);
        totalWrong = findViewById(R.id.totalWrong);
        totalScore = findViewById(R.id.totalScore);
        btnReview = findViewById(R.id.btnReview);
        totalTime = findViewById(R.id.totalTaken);
        value1 = findViewById(R.id.value1);
        value2 = findViewById(R.id.value2);
        value3 = findViewById(R.id.value3);
        value4 = findViewById(R.id.value4);
        value5 = findViewById(R.id.value5);
        testId = getIntent().getExtras().getString("testId", "");
        testName = getIntent().getExtras().getString("testName", "");
        istestExpire = getIntent().getExtras().getBoolean("istestExpire", false);
        viewReview = getIntent().getExtras().getString("viewReview", "");
        flag = getIntent().getExtras().getString("flag", "");
        if (getIntent().getStringExtra("refereshflag") == null || getIntent().getStringExtra("refereshflag").equals("")) {

        } else {
            refereshflag = getIntent().getStringExtra("refereshflag");
        }
        if (viewReview != null && viewReview.equalsIgnoreCase("1")) {
            btnReview.setVisibility(View.VISIBLE);
        } else {
            btnReview.setVisibility(View.GONE);
        }
        getSummaryResult();
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    getSummaryResult();
                } else {
                    Toast.makeText(PracticeTestResultActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    Intent i = new Intent(PracticeTestResultActivity.this, PracticeTestQAActivity.class);
                    i.putExtra("flag", "0");
                    i.putExtra("testId", testId);
                    i.putExtra("testName", testName);
                    i.putExtra("fromCompleted", flag);
                    startActivity(i);
                    onClickAnimation();
                } else {
                    Toast.makeText(PracticeTestResultActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Response{protocol=http/1.1, code=200, message=OK, url=http://testapp.macademic.in/Service.asmx/GetSummary_V2?studId=1&schoolId=8414&yearId=5&testid=4&appName=APPname&VersionCode=4&platform=0}
    private void getSummaryResult() {
        if (isOnline()) {
            noInternetLayout.setVisibility(View.GONE);
            prg_summmary.setVisibility(View.VISIBLE);
            String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
            String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
            String deviceId = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                    .GetCalculateSummary(mStudentId, mSchoolId, mYearId, testId, SchoolDetails.appname+"", Constants.CODEVERSION, Constants.PLATFORM,deviceId);
            call.enqueue(new Callback<AppService>() {
                @Override
                public void onResponse(Call<AppService> call, Response<AppService> response) {
                    try {
                        prg_summmary.setVisibility(View.GONE);
                        noInternetLayout.setVisibility(View.GONE);

                        AppService tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1")) {
                            congo.setVisibility(View.VISIBLE);
                            mainLyt.setVisibility(View.VISIBLE);
                            marksLyt.setVisibility(View.VISIBLE);
                            finished.setText("You Finished The Test!!!");
                           if (tmps.getValue1()!=null && tmps.getValue1().equalsIgnoreCase("1")){
                                btnReview.setVisibility(View.VISIBLE);
                            }else if (viewReview != null && viewReview.equalsIgnoreCase("1")) {
                                btnReview.setVisibility(View.VISIBLE);
                            } else {
                                btnReview.setVisibility(View.GONE);
                            }
                            String totalParts[] = tmps.getStrlist().get(0).getFirst().split("#@#@");
                            String correctParts[] = tmps.getStrlist().get(0).getSecond().split("#@#@");
                            String wrongParts[] = tmps.getStrlist().get(0).getThird().split("#@#@");
                            String timeParts[] = tmps.getStrlist().get(0).getFourth().split("#@#@");
                            String ScoreParts[] = tmps.getStrlist().get(0).getFifth().split("#@#@");
                            String studentNameParts[] = tmps.getStrlist().get(0).getTwelth().split("#@#@");
                            String testParts[] = tmps.getStrlist().get(0).getTenth().split("#@#@");
                            String subjectParts[] = tmps.getStrlist().get(0).getEleventh().split("#@#@");
                            String classnameParts[] = tmps.getStrlist().get(0).getThirteen().split("#@#@");
                            if (tmps.getStrlist().get(0).getSixth() != null && !tmps.getStrlist().get(0).getSixth().isEmpty()) {
                                String SkipParts[] = tmps.getStrlist().get(0).getSixth().split("#@#@");
                                lytSkip.setVisibility(View.VISIBLE);
                                totalSkip.setText(SkipParts[0]);
                                valueSkip.setText(SkipParts[1]);
                            } else {
                                lytSkip.setVisibility(View.GONE);
                            }
                            totalQuestion.setText(totalParts[0]);
                            value1.setText(totalParts[1]);
                            totalCorrect.setText(correctParts[0]);
                            value2.setText(correctParts[1]);
                            totalWrong.setText(wrongParts[0]);
                            value3.setText(wrongParts[1]);
                            totalTime.setText(timeParts[0]);
                            value4.setText(timeParts[1]);
                            totalScore.setText(ScoreParts[0]);
                            value5.setText(ScoreParts[1]);
                            tvlabelsub.setText(subjectParts[0]+" : ");
                            tvsub.setText(subjectParts[1]);
                          /*  tvlabelname.setText(studentNameParts[0]+" : ");*/
                            tvnameStudent.setText(studentNameParts[1]);
                            tvlabeltest.setText(testParts[0]+" : ");
                            tvtest.setText(testParts[1]);
                            tvclass.setText(classnameParts[1]);
                            tvlabelclass.setText(classnameParts[0]+" : ");
                        }/* else if (istestExpire == true) {
                            congo.setVisibility(View.GONE);
                            marksLyt.setVisibility(View.GONE);
                            finished.setText("Test Expired!!!");
                        }*/else  if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("2")) {
                            testNotAttempted.setVisibility(View.VISIBLE);
                            testNotAttemptedTv.setText(tmps.getStrResult());
                            if (tmps.getValue1()!=null && tmps.getValue1().equalsIgnoreCase("1")){
                                btnReview.setVisibility(View.VISIBLE);
                            }else if (viewReview != null && viewReview.equalsIgnoreCase("1")) {
                                btnReview.setVisibility(View.VISIBLE);
                            } else {
                                btnReview.setVisibility(View.GONE);
                            }
                            mainLyt.setVisibility(View.GONE);
                        }
                    } catch (Exception ex) {
                        prg_summmary.setVisibility(View.GONE);
                        Constants.writelog("BtmNavigationActivity", "getPracticetestLiat 107:" + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<AppService> call, Throwable t) {
                    prg_summmary.setVisibility(View.GONE);
                    Constants.writelog("BtmNavigationActivity", "getPracticetestLiat 113:" + t.getMessage());
                }
            });
        } else {
            prg_summmary.setVisibility(View.GONE);
            noInternetLayout.setVisibility(View.VISIBLE);
            mainLyt.setVisibility(View.GONE);
            Toast.makeText(this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                   /* super.onBackPressed();
                    onBackClickAnimation();
                    if (refereshflag != "" && refereshflag.equals("1")) {
                        ((MyApplication) PracticeTestResultActivity.this
                                .getApplicationContext()).getLocalBroadcastInstance().sendBroadcast(
                                new Intent(new Intent(PracticeTestResultActivity.this
                                        .getResources().getString(R.string.broadcast_pending_key))));
                    } else {
                        ((MyApplication) PracticeTestResultActivity.this
                                .getApplicationContext()).getLocalBroadcastInstance().sendBroadcast(
                                new Intent(new Intent(PracticeTestResultActivity.this
                                        .getResources().getString(R.string.broadcast_completed_key))));
                    }*/
                    break;
            }
        } catch (Exception ex) {
            Constants.writelog("onOptionsItemSelected 177:", ex.getMessage());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //   super.onBackPressed();
        // Intent i=new Intent(PracticeTestResultActivity.this,BtmNavigationActivity.class);
        //  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //  i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //  startActivity(i);

        if (refereshflag != "" && refereshflag.equals("1")) {
            ((MyApplication) PracticeTestResultActivity.this
                    .getApplicationContext()).getLocalBroadcastInstance().sendBroadcast(
                    new Intent(new Intent(PracticeTestResultActivity.this
                            .getResources().getString(R.string.broadcast_pending_key))));
        } else {
            ((MyApplication) PracticeTestResultActivity.this
                    .getApplicationContext()).getLocalBroadcastInstance().sendBroadcast(
                    new Intent(new Intent(PracticeTestResultActivity.this
                            .getResources().getString(R.string.broadcast_completed_key))));
        }
        PracticeTestResultActivity.this.finish();
        onBackClickAnimation();
    }
}
