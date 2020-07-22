package com.expedite.apps.kumkum.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.common.ImageZoom;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.fragment.MyDialogFragment;
import com.expedite.apps.kumkum.model.AppService;
import com.expedite.apps.kumkum.model.Contact;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PracticeTestQAActivityTemp extends BaseActivity implements View.OnClickListener, View.OnLongClickListener {
    TextView ques, tvTestTime, correctAnsDisplay, ans_img_opt, tvRemainingCount, tvSkipCount, tvAttemptCount/*, quesNo*/;
    /*RadioGroup rg_opt;*/
    String testId, quesId, correctAns, givenAns, ispeding, testName, isLast = "0", fromCompleted = "0", forExpiretestafterstart = "";
    DatabaseHandler db;
    Button btn_next, previous, next;
    Button skipprevious, skip;
    ProgressBar progressBar;
    int remainingCount = 0, attemptCount = 0;
    Handler mHandler = new Handler();
    Button opt1, opt2, opt3, opt4;
    File mainDirSaveImage;
    boolean containsImg = false;
    MenuItem refereshMenu;
    FrameLayout frame_progress;
    int selected = 0, rowTestID = 0, totalQuestion = 0;
    String testtime = "",testticksfromservice="",teststartdatefromservice="";
    Common common;
    LinearLayout timeLyt;
    RelativeLayout reviewLayout, skipLyt, queinfo;
    String currentDateandTimeTemp = "", testTicks = "";
    public RelativeLayout rl_test;
    Common mcommon;
    String mStudentId, mSchoolId, ansString = "";
    public static final String PractiseTestData = "PractiseTestData";
    ArrayList<Contact> contactsTestList = new ArrayList<>();
    TextView ques_no_img, option_no_4, option_no_3, option_no_2, option_no_1, tvTotalCount;
    /*RadioButton opt1,opt2,opt3,opt4;*/
    long tStart;
    LinearLayout text_ques, back_opt1, back_opt2, back_opt3, back_opt4;
    ImageView img_ques, img_opt1, img_opt2, img_opt3, img_opt4, img_CorrectAns;
    CardView card_img_ques/*, card_img_opt1, card_img_opt2, card_img_opt3, card_img_opt4*/;
    /* FrameLayout frame;*/
    int count = 0, countAll = 0, skipPreviousCount = 0;
    List<AppService.ListArray> listArrays = new ArrayList<>();
    List<AppService.ListArray> allArrays = new ArrayList<>();
    List<AppService.ListArray> SkipArrays = new ArrayList<>();

    long elapsedSeconds = 0, tdelta;
    LinearLayout.LayoutParams lp;
    int after_cal;
    String correctImg = "";
    String question = "";
    //ImageView photoView;
    ImageZoom photoView;
    View mView;
    boolean fromEndTest = false;
    AlertDialog.Builder mBuilder;
    String opt_1 = "", opt_2 = "", opt_3 = "", opt_4 = "";
    AlertDialog mDialog;
    private static CountDownTimer countDownTimer;
    static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
    String n = "";
    long differenceMillisec;
    private AdView mAdView;
    LinearLayout empty_practiceTestBtm;
    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.

    //added on 28-03-2020 by Jameela
   /* private Runnable mStoptest = new Runnable() {
        @Override
        public void run() {
           *//* Intent i = new Intent(PracticeTestQAActivity.this, PracticeTestResultActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra("testId", testId);
            i.putExtra("testName", testName);
            i.putExtra("refereshflag", "1");
            startActivity(i);
            PracticeTestQAActivity.this.finish();*//*
            goToResultActivity();
        }
    };*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_test_qa);
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            db = new DatabaseHandler(this);
            mAdView = (AdView) findViewById(R.id.adView);
            mcommon = new Common(this);
            showFullScreenAd(PracticeTestQAActivityTemp.this, ActivityNames.PracticeTestQAActivity);
            Intent i = getIntent();
            testId = i.getStringExtra("testId");
            ispeding = i.getStringExtra("flag");
            mStudentId = Datastorage.GetStudentId(getApplicationContext());
            mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
            btn_next = findViewById(R.id.btn_next);
            empty_practiceTestBtm = findViewById(R.id.empty_practiceTestBtm);
            testName = i.getStringExtra("testName");
            if (i.getStringExtra("fromCompleted") == null || i.getStringExtra("fromCompleted").equals("")) {

            } else {
                fromCompleted = i.getStringExtra("fromCompleted");
            }

            setTitle(testName);
            ques = findViewById(R.id.ques);

            //  quesNo = findViewById(R.id.quesNo);
            progressBar = findViewById(R.id.pg_QA);
            tvTestTime = findViewById(R.id.tvTestTime);
            timeLyt = findViewById(R.id.timeLyt);
            reviewLayout = findViewById(R.id.reviewLayout);
            text_ques = findViewById(R.id.text_ques);
            card_img_ques = findViewById(R.id.card_img_ques);
            img_ques = findViewById(R.id.img_ques);
            skipLyt = findViewById(R.id.skipLyt);
            tvSkipCount = findViewById(R.id.tvSkipCount);
            tvRemainingCount = findViewById(R.id.tvRemainingCount);
            img_ques.setOnClickListener(this);

            next = findViewById(R.id.next);
            tvAttemptCount = findViewById(R.id.tvAttemptCount);
            queinfo = findViewById(R.id.queinfo);
            skipprevious = findViewById(R.id.skipprevious);
            skip = findViewById(R.id.skip);
            frame_progress = findViewById(R.id.frame_progress);
            back_opt1 = findViewById(R.id.back_opt1);
            back_opt2 = findViewById(R.id.back_opt2);
            back_opt3 = findViewById(R.id.back_opt3);
            back_opt4 = findViewById(R.id.back_opt4);
            img_opt1 = findViewById(R.id.img_opt1);
            img_opt2 = findViewById(R.id.img_opt2);
            img_opt3 = findViewById(R.id.img_opt3);
            img_opt4 = findViewById(R.id.img_opt4);
            option_no_4 = findViewById(R.id.option_no_4);
            option_no_1 = findViewById(R.id.option_no_1);
            tvTotalCount = findViewById(R.id.tvTotalCount);
            option_no_3 = findViewById(R.id.option_no_3);
            option_no_2 = findViewById(R.id.option_no_2);
            ques_no_img = findViewById(R.id.ques_no);
            ans_img_opt = findViewById(R.id.ans_img_opt);
            //   card_img_opt1 = findViewById(R.id.card_img_opt1);
            //   card_img_opt2 = findViewById(R.id.card_img_opt2);
            //   card_img_opt3 = findViewById(R.id.card_img_opt3);
            //  card_img_opt4 = findViewById(R.id.card_img_opt4);

            previous = findViewById(R.id.previous);
            img_CorrectAns = findViewById(R.id.img_CorrectAns);
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT); // or set height to any fixed value you want
            img_opt1.setOnClickListener(this);
            img_opt2.setOnClickListener(this);
            img_opt3.setOnClickListener(this);
            img_opt4.setOnClickListener(this);
            img_opt1.setOnLongClickListener(this);
            img_opt2.setOnLongClickListener(this);
            img_opt3.setOnLongClickListener(this);
            img_opt4.setOnLongClickListener(this);
            img_ques.setOnLongClickListener(this);
            img_CorrectAns.setOnLongClickListener(this);
            //your_layout.setLayoutParams(lp);
            correctAnsDisplay = findViewById(R.id.correctAns);
            // frame = findViewById(R.id.frame);
            rl_test = findViewById(R.id.rl_test);
            tStart = System.currentTimeMillis();
            opt1 = findViewById(R.id.opt1);
            opt2 = findViewById(R.id.opt2);
            opt3 = findViewById(R.id.opt3);
            opt4 = findViewById(R.id.opt4);
            img_CorrectAns.setOnClickListener(this);
            //  frame.bringToFront();
         /*   long tEnd = System.currentTimeMillis();
            long tDelta = tEnd - tStart;
            double elapsedSeconds = tDelta / 1000.0;*/
            if (ispeding.equalsIgnoreCase("0")) {
                showBannerAd(mAdView, ActivityNames.PracticeTestQAActivity_Review);
                showFullScreenAd(PracticeTestQAActivityTemp.this, ActivityNames.PracticeTestQAActivity_Review);
                timeLyt.setVisibility(View.GONE);
                //   tvTestTime.setVisibility(View.GONE);

                queinfo.setVisibility(View.GONE);
                opt1.setEnabled(false);
                opt2.setEnabled(false);
                opt3.setEnabled(false);
                opt4.setEnabled(false);
                back_opt1.setEnabled(false);
                back_opt2.setEnabled(false);
                back_opt3.setEnabled(false);
                back_opt4.setEnabled(false);
                reviewLayout.setVisibility(View.VISIBLE);
                btn_next.setVisibility(View.GONE);
                skipLyt.setVisibility(View.GONE);
            } else {
                mainDirSaveImage = CreatePracticeTestFolder(testId);
                queinfo.setVisibility(View.VISIBLE);
                showBannerAd(mAdView, ActivityNames.PracticeTestQAActivity);
                showFullScreenAd(PracticeTestQAActivityTemp.this, ActivityNames.PracticeTestQAActivity);
                List<Contact> testDetails = db.getPracticeTestDetailByID(Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId), Integer.parseInt(testId));
                if (testDetails == null || testDetails.size() < 1) {
                    testDetails = db.insertTestDetails(Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId), Integer.parseInt(testId));
                }
                if (testDetails != null && testDetails.size() > 0) {
                    rowTestID = testDetails.get(0).getROWID();
                    ansString = testDetails.get(0).getTestAnsString();
                    testTicks = testDetails.get(0).getTestTicks();
                }
                forExpiretestafterstart = getIntent().getExtras().getString("forExpiretestafterstart", "0");
                /*if (!forExpiretestafterstart.equalsIgnoreCase("0")) {
                 *//* SimpleDateFormat df = new SimpleDateFormat("dd-MM-YYYY HH:mm");
                    currentDateandTimeTemp = df.format(new Date());

                    Calendar calCurrent = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY HH:mm", Locale.ENGLISH);
                    calCurrent.setTime(sdf.parse(currentDateandTimeTemp));// all done
*//*
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
                    // String currentDateandTimeTemp = df.format(new Date());
                    Calendar calCurrent = Calendar.getInstance();
                  *//*  Date d1 = null;
                    try {
                        d1 = df.parse(currentDateandTimeTemp);
                        Log.e("dateAfterDate:: ", d1.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }*//*
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<Contact>>() {
                    }
                            .getType();
                    contactsTestList = gson.fromJson(mcommon.getSession(Constants.PractiseTestData), type);
                    boolean isPresent = false;

                        if (contactsTestList != null && contactsTestList.size() > 0) {
                            for (int j = 0; j < contactsTestList.size(); j++) {
                                int index = contactsTestList.indexOf(new Contact(contactsTestList.get(j).getTestId(),Integer.parseInt(mSchoolId),Integer.parseInt(mStudentId)));
                                if (index!=-1) {
                                    isPresent = true;
                                    break;
                                } else {
                                    isPresent = false;
                                }
                                if (isPresent == false) {
                                    String fDate = addMinTocurrentTime(Integer.parseInt(forExpiretestafterstart));
                                    Contact contact = new Contact(testId, fDate,Integer.parseInt(mSchoolId),Integer.parseInt(mStudentId));
                                    ArrayList<Contact> contacts=new ArrayList<>();
                                    contacts.add(contact);
                                    Gson gson1 = new Gson();
                                    String jsonMenu = gson1.toJson(contacts);
                                    mcommon.setSession(Constants.PractiseTestData, jsonMenu);
                                }
                            }
                        } else {
                            String fDate = addMinTocurrentTime(Integer.parseInt(forExpiretestafterstart));
                            Contact contact = new Contact(testId, fDate,Integer.parseInt(mSchoolId),Integer.parseInt(mStudentId));
                            ArrayList<Contact> contacts=new ArrayList<>();
                            contacts.add(contact);
                            Gson gson1 = new Gson();
                            String jsonMenu = gson1.toJson(contacts);
                            mcommon.setSession(Constants.PractiseTestData, jsonMenu);
                        }
                    contactsTestList = gson.fromJson(mcommon.getSession(Constants.PractiseTestData), type);
                    int index = contactsTestList.indexOf(new Contact(testId,Integer.parseInt(mSchoolId),Integer.parseInt(mStudentId)));

                    //     int index=  contactsTestList.indexOf(testId);
                    testtime = contactsTestList.get(index).getTestExpireDateAfterStart();
                    Calendar calGiven = Calendar.getInstance();
                    SimpleDateFormat sdfGiven = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss", Locale.ENGLISH);
                    String splitDateTime[]=testtime.split(" ");
                    String splitDate[]=splitDateTime[0].split("-");
                    String splitTime[]=splitDateTime[1].split(":");
                    calGiven.set(Calendar.MONTH,Integer.parseInt(splitDate[1])-1);
                    calGiven.set(Calendar.DAY_OF_MONTH,Integer.parseInt(splitDate[0]));
                    calGiven.set(Calendar.YEAR,Integer.parseInt(splitDate[2]));
                    calGiven.set(Calendar.HOUR_OF_DAY,Integer.parseInt(splitTime[0]));
                    calGiven.set(Calendar.MINUTE,Integer.parseInt(splitTime[1]));
                    calGiven.set(Calendar.SECOND,Integer.parseInt(splitTime[2]));
                    // calGiven.setTime(sdfGiven.parse(tmps.getStrlist().get(i).getSixth()));
                    Constants.Logwrite("CurrentTime", sdfGiven.format(calGiven.getTime()));
                    Constants.Logwrite("CurrentTimeMillis", String.valueOf(calGiven.getTimeInMillis()));

                    if (calCurrent.getTimeInMillis() < calGiven.getTimeInMillis()) {
                        differenceMillisec = calGiven.getTimeInMillis() - calCurrent.getTimeInMillis();
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(differenceMillisec);
                        timeLyt.setVisibility(View.VISIBLE);
                        long noOfMinutes = minutes * 60 * 1000;
                      //  startTimer(noOfMinutes);
                      startTimer(differenceMillisec);
                        //  mHandler.postDelayed(mStoptest, differenceMillisec);
                    } else {
                       *//* Intent intent = new Intent(PracticeTestQAActivity.this, PracticeTestResultActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("testId", testId);
                        intent.putExtra("testName", testName);
                        intent.putExtra("refereshflag", "1");
                        startActivity(i);
                        PracticeTestQAActivity.this.finish();*//*
                        goToResultActivity();
                    }
                }*/
                reviewLayout.setVisibility(View.GONE);
                btn_next.setVisibility(View.VISIBLE);
                skipLyt.setVisibility(View.VISIBLE);
            }


        /*    skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isDone=false;
                    boolean isContains=false;
                   // int queNo=Integer.parseInt(listArrays.get(count).getSixth());

                    if(SkipArrays!=null && SkipArrays.size()>0){
                        for(int i=0;i<SkipArrays.size();i++){
                            if(SkipArrays.get(i).getFirst().equalsIgnoreCase(quesId)){
                               isContains=true;
                                break;
                            }
                        }
                        if(isContains==false){
                            SkipArrays.add(listArrays.get(count));
                            if(SkipArrays.size()>1)
                            skipprevious.setVisibility(View.VISIBLE);
                            else
                                skipprevious.setVisibility(View.GONE);
                        }
                    }else {
                        SkipArrays.add(listArrays.get(count));
                        skipprevious.setVisibility(View.VISIBLE);
                    }
                    *//*
                        int indexSkip = SkipArrays.indexOf(new AppService().new ListArray(quesId));
                    if(indexSkip==-1)
                    {
                        SkipArrays.add(listArrays.get(count));
                        skipprevious.setVisibility(View.VISIBLE);
                    }*//*
                    skipPreviousCount=SkipArrays.size();
                 //  int indexSkip = SkipArrays.indexOf(new AppService().new ListArray(queNo));
          //       SkipArrays.add(listArrays.get(count));

                 //   int queNo=Integer.parseInt(SkipArrays.get(count).getSixth());
                    count++;
                    if(count>listArrays.size()-1){
                        for(int i=0;i<listArrays.size();i++){
                            if(listArrays.get(i).isAnsSubmited()==false){
                                count=i;
                                isDone=true;
                                skipprevious.setVisibility(View.GONE);
                                break;
                            }
                        }
                        if(isDone==false){
                            Toast.makeText(PracticeTestQAActivity.this, "Last Question Reached!!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        for(int i=count;i<listArrays.size();i++){
                            if(listArrays.get(i).isAnsSubmited()==false){
                                count=i;
                                if(skipPreviousCount>0)
                                    skipprevious.setVisibility(View.VISIBLE);
                                else
                                    skipprevious.setVisibility(View.GONE);
                               // skipprevious.setVisibility(View.VISIBLE);
                                isDone=true;
                                break;
                            }
                        }
                        if(isDone==false){
                            for(int i=0;i<listArrays.size();i++){
                                if(listArrays.get(i).isAnsSubmited()==false){
                                    count=i;
                                    isDone=true;
                                    skipprevious.setVisibility(View.GONE);
                                    break;
                                }
                            }
                            if(isDone==false){
                                Toast.makeText(PracticeTestQAActivity.this, "Last Question Reached!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    setQuestion(count, listArrays,0);
                }
            });


            skipprevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // skipPreviousCount--;
                    boolean isNextAvailable=false;
                   count--;
                   for(int j=count;j>=0;j--){
                       if(listArrays.get(j).isAnsSubmited()==false){
                           count=j;
                           break;
                       }
                   }
                    for(int j=count-1;j>=0;j--){
                        if(listArrays.get(j).isAnsSubmited()==false){
                            skipprevious.setVisibility(View.VISIBLE);
                            isNextAvailable=true;
                            break;
                        }
                    }
                    if(isNextAvailable==false){
                        skipprevious.setVisibility(View.GONE);
                    }

                    setQuestion(count,listArrays,1);

                }
            });*/


            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isDone = false;
                    boolean isContains = false;
                    boolean isfound = false;
                    frame_progress.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    // int queNo=Integer.parseInt(listArrays.get(count).getSixth());
                    if (skip.getText().equals("Remove Selection")) {

                        selected = 0;
                        skip.setText("Skip");
                        //changed on 03/06/2020
                      /*  if ((listArrays.size() - 1) == count) {
                            skip.setVisibility(View.GONE);
                        }else{
                            skip.setVisibility(View.VISIBLE);
                        }*/
                        List<Contact> testDetails = db.getPracticeTestDetailByID(Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId), Integer.parseInt(testId));
                        if (testDetails != null && testDetails.size() > 0) {
                            ansString = testDetails.get(0).getTestAnsString();
                        }
                        if (ansString != null && !ansString.isEmpty()) {
                            String[] splitans = ansString.split("@@");
                            if (splitans != null && splitans.length > 0) {
                                for (int i = 0; i < splitans.length; i++) {
                                    String[] splitforId = splitans[i].split(",");
                                    if (splitforId[0].equalsIgnoreCase(quesId)) {
                                        List<String> list = new ArrayList<>(Arrays.asList(splitans));
                                        list.remove(i);
                                        splitans = list.toArray(new String[0]);
                                        ansString = "";
                                        isfound = true;
                                        attemptCount = attemptCount - 1;
                                        remainingCount = remainingCount + 1;
                                        listArrays.get(count).setThird("");
                                        break;
                                    }
                                }
                            }
                            if (isfound == true) {
                                if (splitans.length <= 0) {
                                    ansString = "";
                                } else {
                                    for (int i = 0; i < splitans.length; i++) {
                                        if (ansString.isEmpty()) {
                                            ansString = splitans[i];
                                        } else {
                                            ansString += "@@" + splitans[i];
                                        }
                                    }
                                }
                            }

                            tvRemainingCount.setText(Html.fromHtml("<b>Pending : </b>" + remainingCount));
                            tvAttemptCount.setText(String.valueOf(Html.fromHtml("<b>Attempted : <b/>" + attemptCount)));

                            db.updateTestAnsString(rowTestID, ansString);
                        }
                        setQuestionDiff(count, listArrays);
                    } else {
                        if (SkipArrays != null && SkipArrays.size() > 0) {
                            for (int i = 0; i < SkipArrays.size(); i++) {
                                if (SkipArrays.get(i).getFirst().equalsIgnoreCase(quesId)) {
                                    isContains = true;
                                    break;
                                }
                            }
                            if (isContains == false) {
                                remainingCount = remainingCount - 1;
                                SkipArrays.add(listArrays.get(count));
                                if (SkipArrays.size() > 1)
                                    skipprevious.setVisibility(View.VISIBLE);
                                else
                                    skipprevious.setVisibility(View.GONE);
                            }
                        } else {
                            SkipArrays.add(listArrays.get(count));
                            remainingCount = remainingCount - 1;
                            skipprevious.setVisibility(View.VISIBLE);
                        }

                        tvSkipCount.setText(String.valueOf(Html.fromHtml("<b>Skipped : <b/>" + SkipArrays.size())));
                        tvRemainingCount.setText(Html.fromHtml("<b>Pending : </b>" + remainingCount));

                    /*
                        int indexSkip = SkipArrays.indexOf(new AppService().new ListArray(quesId));
                    if(indexSkip==-1)
                    {
                        SkipArrays.add(listArrays.get(count));
                        skipprevious.setVisibility(View.VISIBLE);
                    }*/
                        skipPreviousCount = SkipArrays.size();
                        //  int indexSkip = SkipArrays.indexOf(new AppService().new ListArray(queNo));
                        //       SkipArrays.add(listArrays.get(count));

                        //   int queNo=Integer.parseInt(SkipArrays.get(count).getSixth());
                        count++;
                        if (count > listArrays.size() - 1) {
                       /* for (int i = 0; i < listArrays.size(); i++) {
                            if (listArrays.get(i).isAnsSubmited() == false) {
                                count = i;
                                isDone = true;
                                skipprevious.setVisibility(View.GONE);
                                break;
                            }
                        }
                        if (isDone == false) {*/
                            Toast.makeText(PracticeTestQAActivityTemp.this, "Last Question Reached!!", Toast.LENGTH_SHORT).show();
                            /*  }*/
                        } else {
                            for (int i = count; i < listArrays.size(); i++) {
                                /*if (listArrays.get(i).isAnsSubmited() == false) {*/
                                count = i;
                                if (skipPreviousCount > 0)
                                    skipprevious.setVisibility(View.VISIBLE);
                                else
                                    skipprevious.setVisibility(View.GONE);
                                // skipprevious.setVisibility(View.VISIBLE);
                                isDone = true;
                                break;
                                /* }*/
                            }
                            if (isDone == false) {
                           /* for (int i = 0; i < listArrays.size(); i++) {
                                if (listArrays.get(i).isAnsSubmited() == false) {
                                    count = i;
                                    isDone = true;
                                    skipprevious.setVisibility(View.GONE);
                                    break;
                                }
                            }
                            if (isDone == false) {*/
                                Toast.makeText(PracticeTestQAActivityTemp.this, "Last Question Reached!!", Toast.LENGTH_SHORT).show();
                                /* }*/
                            }
                        }
                        setQuestionDiff(count, listArrays);
                    }
                }
            });


            skipprevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // skipPreviousCount--;
                    boolean isNextAvailable = false;
                    count--;
                    for (int j = count; j >= 0; j--) {
                        /* if (listArrays.get(j).isAnsSubmited() == false) {*/
                        count = j;
                        break;
                        /* }*/
                    }
                    for (int j = count - 1; j >= 0; j--) {
                        /*  if (listArrays.get(j).isAnsSubmited() == false) {*/
                        skipprevious.setVisibility(View.VISIBLE);
                        isNextAvailable = true;
                        break;
                        /* }*/
                    }
                    if (isNextAvailable == false) {
                        skipprevious.setVisibility(View.GONE);
                    }

                    setQuestionDiff(count, listArrays);

                }
            });

            opt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (opt1.getVisibility() == View.VISIBLE) {
                        opt1.setBackgroundResource(R.drawable.quiz_box);
                        opt1.setTextColor(getResources().getColor(R.color.colorPrimaryDarkbg));
                        opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt2.setBackgroundResource(R.drawable.quiz_opt);
                        opt3.setBackgroundResource(R.drawable.quiz_opt);
                        opt4.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                        opt1.setPadding(15, 0, 0, 0);
                        opt2.setPadding(15, 0, 0, 0);
                        opt3.setPadding(15, 0, 0, 0);
                        opt4.setPadding(15, 0, 0, 0);
                        back_opt1.setPadding(15, 0, 15, 0);
                        back_opt2.setPadding(15, 0, 15, 0);
                        back_opt3.setPadding(15, 0, 15, 0);
                        back_opt4.setPadding(15, 0, 15, 0);
                        selected = 1;
                    } else {
                        back_opt1.setBackgroundResource(R.drawable.quiz_box);
                        back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt4.setBackgroundResource(R.drawable.quiz_opt);

                        back_opt1.setPadding(15, 0, 15, 0);
                        back_opt2.setPadding(15, 0, 15, 0);
                        back_opt3.setPadding(15, 0, 15, 0);
                        back_opt4.setPadding(15, 0, 15, 0);
                        selected = 1;
                    }
                }
            });
            back_opt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //for zoom
                    back_opt1.setBackgroundResource(R.drawable.quiz_box);
                    back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt4.setBackgroundResource(R.drawable.quiz_opt);

                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt2.setTextColor(getResources().getColor(R.color.colorPrimary));

                    back_opt1.setPadding(15, 0, 15, 0);
                    back_opt2.setPadding(15, 0, 15, 0);
                    back_opt3.setPadding(15, 0, 15, 0);
                    back_opt4.setPadding(15, 0, 15, 0);
                    opt1.setPadding(15, 0, 0, 0);
                    opt2.setPadding(15, 0, 0, 0);
                    opt3.setPadding(15, 0, 0, 0);
                    opt4.setPadding(15, 0, 0, 0);

                    opt3.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setBackgroundResource(R.drawable.quiz_opt);
                    opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt4.setBackgroundResource(R.drawable.quiz_opt);
                    selected = 1;
                    //changed on 03/06/2020
                   /* if ((listArrays.size() - 1) == count) {
                        skip.setText("Remove Selection");
                        skip.setVisibility(View.VISIBLE);
                    }*/
                }
            });

            opt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (opt2.getVisibility() == View.VISIBLE) {
                        //changed on 03/06/2020
                       /* if ((listArrays.size() - 1) == count) {
                            skip.setText("Remove Selection");
                            skip.setVisibility(View.VISIBLE);
                        }*/
                        opt2.setBackgroundResource(R.drawable.quiz_box);
                        opt2.setTextColor(getResources().getColor(R.color.colorPrimaryDarkbg));
                        opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt1.setBackgroundResource(R.drawable.quiz_opt);
                        opt3.setBackgroundResource(R.drawable.quiz_opt);
                        opt4.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                        opt1.setPadding(15, 0, 0, 0);
                        opt2.setPadding(15, 0, 0, 0);
                        opt3.setPadding(15, 0, 0, 0);
                        opt4.setPadding(15, 0, 0, 0);

                        back_opt1.setPadding(15, 0, 15, 0);
                        back_opt2.setPadding(15, 0, 15, 0);
                        back_opt3.setPadding(15, 0, 15, 0);
                        back_opt4.setPadding(15, 0, 15, 0);
                        selected = 2;
                    } else {
                        back_opt2.setBackgroundResource(R.drawable.quiz_box);
                        back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt4.setBackgroundResource(R.drawable.quiz_opt);

                        back_opt1.setPadding(15, 0, 15, 0);
                        back_opt2.setPadding(15, 0, 15, 0);
                        back_opt3.setPadding(15, 0, 15, 0);
                        back_opt4.setPadding(15, 0, 15, 0);
                        selected = 2;
                    }


                    /*opt2.setBackgroundResource(R.drawable.quiz_box);
                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt2.setTextColor(getResources().getColor(R.color.colorPrimaryDarkbg));
                    opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setBackgroundResource(R.drawable.quiz_opt);
                    opt3.setBackgroundResource(R.drawable.quiz_opt);
                    opt4.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setPadding(15, 0, 0, 0);opt2.setPadding(15, 0, 0, 0);
                    opt3.setPadding(15, 0, 0, 0);
                    opt4.setPadding(15, 0, 0, 0);
                    selected = 2;*/
                }
            });

            back_opt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//for zoom
                    //changed on 03/06/2020
                  /*  if ((listArrays.size() - 1) == count) {
                        skip.setText("Remove Selection");
                        skip.setVisibility(View.VISIBLE);
                    }*/

                    opt3.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setBackgroundResource(R.drawable.quiz_opt);
                    opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt4.setBackgroundResource(R.drawable.quiz_opt);

                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt2.setTextColor(getResources().getColor(R.color.colorPrimary));

                    back_opt2.setBackgroundResource(R.drawable.quiz_box);
                    back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setPadding(15, 0, 0, 0);
                    opt2.setPadding(15, 0, 0, 0);
                    opt3.setPadding(15, 0, 0, 0);
                    opt4.setPadding(15, 0, 0, 0);
                    back_opt1.setPadding(15, 0, 15, 0);
                    back_opt2.setPadding(15, 0, 15, 0);
                    back_opt3.setPadding(15, 0, 15, 0);
                    back_opt4.setPadding(15, 0, 15, 0);
                    selected = 2;
                }
            });

            opt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (opt3.getVisibility() == View.VISIBLE) {
                        //changed on 03/06/2020
                        /*if ((listArrays.size() - 1) == count) {
                            skip.setText("Remove Selection");
                            skip.setVisibility(View.VISIBLE);
                        }*/
                        opt3.setBackgroundResource(R.drawable.quiz_box);
                        opt1.setBackgroundResource(R.drawable.quiz_opt);
                        opt2.setBackgroundResource(R.drawable.quiz_opt);
                        opt4.setBackgroundResource(R.drawable.quiz_opt);

                        back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt4.setBackgroundResource(R.drawable.quiz_opt);

                        opt3.setTextColor(getResources().getColor(R.color.colorPrimaryDarkbg));
                        opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt4.setTextColor(getResources().getColor(R.color.colorPrimary));

                        opt1.setPadding(15, 0, 0, 0);
                        opt2.setPadding(15, 0, 0, 0);
                        opt3.setPadding(15, 0, 0, 0);
                        opt4.setPadding(15, 0, 0, 0);

                        back_opt1.setPadding(15, 0, 15, 0);
                        back_opt2.setPadding(15, 0, 15, 0);
                        back_opt3.setPadding(15, 0, 15, 0);
                        back_opt4.setPadding(15, 0, 15, 0);
                        selected = 3;
                    } else {
                        back_opt3.setBackgroundResource(R.drawable.quiz_box);
                        back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt4.setBackgroundResource(R.drawable.quiz_opt);

                        back_opt1.setPadding(15, 0, 15, 0);
                        back_opt2.setPadding(15, 0, 15, 0);
                        back_opt3.setPadding(15, 0, 15, 0);
                        back_opt4.setPadding(15, 0, 15, 0);
                        selected = 3;
                    }

                   /* opt3.setBackgroundResource(R.drawable.quiz_box);
                    opt3.setTextColor(getResources().getColor(R.color.colorPrimaryDarkbg));
                    opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setBackgroundResource(R.drawable.quiz_opt);
                    opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt4.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setPadding(15, 0, 0, 0);opt2.setPadding(15, 0, 0, 0);
                    opt3.setPadding(15, 0, 0, 0);
                    opt4.setPadding(15, 0, 0, 0);
                    selected = 3;*/
                }
            });

            back_opt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//for zoom
                    //changed on 03/06/2020
                    /*if ((listArrays.size() - 1) == count) {
                        skip.setText("Remove Selection");
                        skip.setVisibility(View.VISIBLE);
                    }*/

                    opt3.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setBackgroundResource(R.drawable.quiz_opt);
                    opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt4.setBackgroundResource(R.drawable.quiz_opt);

                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt2.setTextColor(getResources().getColor(R.color.colorPrimary));

                    back_opt3.setBackgroundResource(R.drawable.quiz_box);
                    back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setPadding(15, 0, 0, 0);
                    opt2.setPadding(15, 0, 0, 0);
                    opt3.setPadding(15, 0, 0, 0);
                    opt4.setPadding(15, 0, 0, 0);
                    back_opt1.setPadding(15, 0, 15, 0);
                    back_opt2.setPadding(15, 0, 15, 0);
                    back_opt3.setPadding(15, 0, 15, 0);
                    back_opt4.setPadding(15, 0, 15, 0);
                    selected = 3;
                }
            });
            opt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (opt4.getVisibility() == View.VISIBLE) {
                        //changed on 03/06/2020
                        /*if ((listArrays.size() - 1) == count) {
                            skip.setText("Remove Selection");
                            skip.setVisibility(View.VISIBLE);
                        }*/
                        opt4.setBackgroundResource(R.drawable.quiz_box);
                        opt4.setTextColor(getResources().getColor(R.color.colorPrimaryDarkbg));
                        opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt1.setBackgroundResource(R.drawable.quiz_opt);
                        opt3.setBackgroundResource(R.drawable.quiz_opt);
                        opt2.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                        opt1.setPadding(15, 0, 0, 0);
                        opt2.setPadding(15, 0, 0, 0);
                        opt3.setPadding(15, 0, 0, 0);
                        opt4.setPadding(15, 0, 0, 0);

                        back_opt1.setPadding(15, 0, 15, 0);
                        back_opt2.setPadding(15, 0, 15, 0);
                        back_opt3.setPadding(15, 0, 15, 0);
                        back_opt4.setPadding(15, 0, 15, 0);
                        selected = 4;
                    } else {
                        back_opt4.setBackgroundResource(R.drawable.quiz_box);
                        back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt2.setBackgroundResource(R.drawable.quiz_opt);

                        back_opt1.setPadding(15, 0, 15, 0);
                        back_opt2.setPadding(15, 0, 15, 0);
                        back_opt3.setPadding(15, 0, 15, 0);
                        back_opt4.setPadding(15, 0, 15, 0);
                        selected = 4;
                    }
                }
            });

            back_opt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //for zoom
                    //changed on 03/06/2020
                    /*if ((listArrays.size() - 1) == count) {
                        skip.setText("Remove Selection");
                        skip.setVisibility(View.VISIBLE);
                    }*/

                    opt3.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setBackgroundResource(R.drawable.quiz_opt);
                    opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt4.setBackgroundResource(R.drawable.quiz_opt);

                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt2.setTextColor(getResources().getColor(R.color.colorPrimary));

                    back_opt4.setBackgroundResource(R.drawable.quiz_box);
                    back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setPadding(15, 0, 0, 0);
                    opt2.setPadding(15, 0, 0, 0);
                    opt3.setPadding(15, 0, 0, 0);
                    opt4.setPadding(15, 0, 0, 0);
                    back_opt1.setPadding(15, 0, 15, 0);
                    back_opt2.setPadding(15, 0, 15, 0);
                    back_opt3.setPadding(15, 0, 15, 0);
                    back_opt4.setPadding(15, 0, 15, 0);
                    selected = 4;
                }
            });

            opt1.setPadding(15, 0, 0, 0);
            opt2.setPadding(15, 0, 0, 0);
            opt3.setPadding(15, 0, 0, 0);
            opt4.setPadding(15, 0, 0, 0);

            getQuestion();

            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ispeding.equalsIgnoreCase("1")) {
                        frame_progress.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        if (btn_next.getText().equals("End Test")) {
                            if (selected == 0) {
                                frame_progress.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                //  btn_next.setEnabled(false);
                                //    goToResultActivity();
                                Toast.makeText(PracticeTestQAActivityTemp.this, "Please Select Any Option !", Toast.LENGTH_SHORT).show();

                            } else {
                                fromEndTest = true;
                                btn_next.setEnabled(false);
                                setTestAns();
                            }
                        } else {
                            if (listArrays.get(count).getThird() != null && !listArrays.get(count).getThird().equals("0") && !listArrays.get(count).getThird().equals("") && selected == 0) {

                                /*if ((listArrays.size() - 1) < count) {
                                 *//*for (int i = 0; i < listArrays.size(); i++) {
                                    if (listArrays.get(i).isAnsSubmited() == false) {
                                        count = i;
                                        isDone = true;
                                        skipprevious.setVisibility(View.GONE);
                                        break;
                                    }
                                }
                                if (isDone == false) {*//*
                                    count = listArrays.size();
                                    *//*  }*//*
                                } else {
                                    for (int i = count; i < listArrays.size(); i++) {
                                        *//*if (listArrays.get(count).isAnsSubmited() == false) {*//*
                                        count = i;
                                      //  isDone = true;
                                        break;
                                        *//*}*//*
                                    }

                                }*/
                                count++;
                                setQuestionDiff(count, listArrays);

                                selected = 0;
                                tStart = System.currentTimeMillis();
                                elapsedSeconds = 0;


                            } else if (selected == 0 && (listArrays.get(count).getThird() == null || listArrays.get(count).getThird().equals("0") || listArrays.get(count).getThird().equals(""))) {
                                // frame.bringToFront();
                                progressBar.setVisibility(View.GONE);
                                frame_progress.setVisibility(View.GONE);
                                Toast.makeText(PracticeTestQAActivityTemp.this, "Please Select Any Option !", Toast.LENGTH_SHORT).show();

                            } else {
                                //   frame_progress.setVisibility(View.VISIBLE);
                                //  progressBar.setVisibility(View.VISIBLE);
                                setTestAns();
                            }
                        }
                    } else {
                        countAll++;
                        if (allArrays.size() >= countAll) {
                            quesId = allArrays.get(countAll).getFirst();
                            correctAns = allArrays.get(countAll).getFourth();
                            givenAns = allArrays.get(countAll).getThird();
                            //  quesNo.setText(allArrays.get(countAll).getSixth());
                            question = allArrays.get(countAll).getSecond();
                            if (isURL(question)) {
                                img_ques.setVisibility(View.VISIBLE);
                                text_ques.setVisibility(View.GONE);
                                card_img_ques.setVisibility(View.VISIBLE);
                                getImageSize(question, "que");
                                //test
                               /* Glide.with(PracticeTestQAActivity.this)
                                        .load(question).asBitmap().dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                        .into(img_ques);*/


                                ques_no_img.setText(allArrays.get(countAll).getSixth());
                            } else {
                                img_ques.setVisibility(View.GONE);
                                text_ques.setVisibility(View.VISIBLE);
                                card_img_ques.setVisibility(View.GONE);
                                ques.setText(allArrays.get(countAll).getSixth() + " " + allArrays.get(countAll).getSecond());
                            }
                            //     ques.setText(allArrays.get(countAll).getSixth() + " " + allArrays.get(countAll).getSecond());

                            opt_1 = allArrays.get(countAll).getSeventh();

                            opt_2 = allArrays.get(countAll).getEighth();
                            opt_3 = allArrays.get(countAll).getNineth();
                            opt_4 = allArrays.get(countAll).getTenth();

                            if (isURL(opt_1)) {
                                back_opt1.setVisibility(View.VISIBLE);
                                opt1.setVisibility(View.GONE);
                                back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                                getImageSize(opt_1, "opt_1");
                                //test
//                                Glide.with(PracticeTestQAActivity.this)
//                                        .load(opt_1).asBitmap().dontAnimate()
//                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                        .placeholder(R.drawable.placeholder)
//                                        .error(R.drawable.placeholder)
//                                        .into(img_opt1);
                            } else {
                                //  opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                back_opt1.setVisibility(View.GONE);
                                opt1.setVisibility(View.VISIBLE);
                                opt1.setBackgroundResource(R.drawable.quiz_opt);
                                opt1.setText("[A] " + allArrays.get(countAll).getSeventh());
                            }

                            if (isURL(opt_2)) {
                                back_opt2.setVisibility(View.VISIBLE);
                                opt2.setVisibility(View.GONE);
                                back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                                getImageSize(opt_2, "opt_2");
                                //test
                               /* Glide.with(PracticeTestQAActivity.this)
                                        .load(opt_2).asBitmap().dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                        .into(img_opt2);*/
                            } else {
                                // opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                                opt2.setVisibility(View.VISIBLE);
                                back_opt2.setVisibility(View.GONE);
                                opt2.setBackgroundResource(R.drawable.quiz_opt);
                                opt2.setText("[B] " + allArrays.get(countAll).getEighth());
                            }

                            if (isURL(opt_3)) {
                                back_opt3.setVisibility(View.VISIBLE);
                                opt3.setVisibility(View.GONE);
                                back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                                getImageSize(opt_3, "opt_3");
                                //test
                               /* Glide.with(PracticeTestQAActivity.this)
                                        .load(opt_3).asBitmap().dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                        .into(img_opt3);*/
                            } else {
                                //   opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                                back_opt3.setVisibility(View.GONE);
                                opt3.setVisibility(View.VISIBLE);
                                opt3.setBackgroundResource(R.drawable.quiz_opt);
                                opt3.setText("[C] " + allArrays.get(countAll).getNineth());
                            }

                            if (isURL(opt_4)) {
                                back_opt4.setVisibility(View.VISIBLE);
                                opt4.setVisibility(View.GONE);
                                back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                                getImageSize(opt_4, "opt_4");
                                //test
                               /* Glide.with(PracticeTestQAActivity.this)
                                    .load(opt_4).asBitmap().dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(img_opt4);*/
                            } else {
                                opt4.setVisibility(View.VISIBLE);
                                back_opt4.setVisibility(View.GONE);
                                opt4.setBackgroundResource(R.drawable.quiz_opt);
                                opt4.setText("[D] " + allArrays.get(countAll).getTenth());
                            }

                            if (correctAns == null) {
                                correctAns = "0";
                            }
                            if (givenAns == null) {
                                givenAns = "0";
                            }
                            for (int i = 1; i <= 4; i++) {
                                int color = 0;
                                int colorText = 0;

                                back_opt1.setPadding(15, 0, 15, 0);
                                back_opt2.setPadding(15, 0, 15, 0);
                                back_opt3.setPadding(15, 0, 15, 0);
                                back_opt4.setPadding(15, 0, 15, 0);

                                opt1.setPadding(15, 0, 0, 0);
                                opt2.setPadding(15, 0, 0, 0);
                                opt3.setPadding(15, 0, 0, 0);
                                opt4.setPadding(15, 0, 0, 0);

                                if (correctAns.equals(i + "") && givenAns.equals(i + "")) {
                                    color = R.drawable.quiz_right;
                                    colorText = R.color.White;
                                } else if (correctAns.equals(i + "")) {
                                    color = R.drawable.quiz_right_diff;
                                    colorText = R.color.Black;
                                } else if (givenAns.equals(i + "")) {
                                    color = R.drawable.quiz_wrong;
                                    colorText = R.color.White;
                                }
                                if (color != 0) {
                                    if (i == 1) {
                                        opt1.setBackgroundResource(color);
                                        opt1.setTextColor(colorText);
                                        opt1.setPadding(15, 0, 0, 0);
                                    } else if (i == 2) {
                                        opt2.setBackgroundResource(color);
                                        opt2.setTextColor(colorText);
                                        opt2.setPadding(15, 0, 0, 0);
                                    } else if (i == 3) {
                                        opt3.setBackgroundResource(color);
                                        opt3.setTextColor(colorText);
                                        opt3.setPadding(15, 0, 0, 0);
                                    } else {
                                        opt4.setBackgroundResource(color);
                                        opt4.setTextColor(colorText);
                                        opt4.setPadding(15, 0, 0, 0);
                                    }
                                }
                            }
                        }
                    }

                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countAll++;
                    next.setVisibility(View.VISIBLE);
                    previous.setVisibility(View.VISIBLE);
                    if (allArrays.size() - 1 == countAll) {
                        next.setVisibility(View.GONE);
                        previous.setVisibility(View.VISIBLE);
                    }
                    if (allArrays.size() >= countAll) {
                        quesId = allArrays.get(countAll).getFirst();
                        correctAns = allArrays.get(countAll).getFourth();
                        givenAns = allArrays.get(countAll).getThird();
                        //  quesNo.setText(allArrays.get(countAll).getSixth());
                        question = allArrays.get(countAll).getSecond();
                        if (isURL(question)) {
                            img_ques.setVisibility(View.VISIBLE);
                            text_ques.setVisibility(View.GONE);
                            card_img_ques.setVisibility(View.VISIBLE);
                            getImageSize(question, "que");
                            //test
                           /* Glide.with(PracticeTestQAActivity.this)
                                    .load(question).asBitmap().dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(img_ques);*/
                            ques_no_img.setText(allArrays.get(countAll).getSixth());
                        } else {
                            img_ques.setVisibility(View.GONE);
                            text_ques.setVisibility(View.VISIBLE);
                            card_img_ques.setVisibility(View.GONE);
                            ques.setText(allArrays.get(countAll).getSixth() + " " + allArrays.get(countAll).getSecond());
                        }
                        //   ques.setText(allArrays.get(countAll).getSixth() + " " + allArrays.get(countAll).getSecond());


                      /*  String opt_1 = allArrays.get(countAll).getSeventh();
                        String opt_2 = allArrays.get(countAll).getEighth();
                        String opt_3 = allArrays.get(countAll).getNineth();
                        String opt_4 = allArrays.get(countAll).getTenth();

                        if (isURL(opt_1)) {
                            back_opt1.setVisibility(View.VISIBLE);
                            opt1.setVisibility(View.GONE);
                            back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                            Glide.with(PracticeTestQAActivity.this)
                                    .load(opt_1).asBitmap().dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(img_opt1);
                        } else {
                            opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                            back_opt1.setVisibility(View.GONE);
                            opt1.setVisibility(View.VISIBLE);
                            opt1.setBackgroundResource(R.drawable.quiz_opt);
                            opt1.setText("[A] " + allArrays.get(countAll).getSeventh());
                        }

                        if (isURL(opt_2)) {
                            back_opt2.setVisibility(View.VISIBLE);
                            opt2.setVisibility(View.GONE);

                            back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                            Glide.with(PracticeTestQAActivity.this)
                                    .load(opt_2).asBitmap().dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(img_opt2);
                        } else {
                            opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                            opt2.setVisibility(View.VISIBLE);
                            back_opt2.setVisibility(View.GONE);
                            opt2.setBackgroundResource(R.drawable.quiz_opt);
                            opt2.setText("[B] " + allArrays.get(countAll).getEighth());
                        }

                        if (isURL(opt_3)) {
                            back_opt3.setVisibility(View.VISIBLE);
                            opt3.setVisibility(View.GONE);

                            back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                            Glide.with(PracticeTestQAActivity.this)
                                    .load(opt_3).asBitmap().dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(img_opt3);
                        } else {
                            opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                            back_opt3.setVisibility(View.GONE);
                            opt3.setVisibility(View.VISIBLE);
                            opt3.setBackgroundResource(R.drawable.quiz_opt);
                            opt3.setText("[C] " + allArrays.get(countAll).getNineth());
                        }

                        if (isURL(opt_4)) {
                            back_opt4.setVisibility(View.VISIBLE);
                            opt4.setVisibility(View.GONE);
                            back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                            Glide.with(PracticeTestQAActivity.this)
                                    .load(opt_4).asBitmap().dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(img_opt4);
                        } else {
                            opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                            back_opt4.setVisibility(View.GONE);
                            opt4.setVisibility(View.VISIBLE);
                            opt4.setBackgroundResource(R.drawable.quiz_opt);
                            opt4.setText("[D] " + allArrays.get(countAll).getTenth());
                        }*/

                      /*  opt1.setText("[A] " + allArrays.get(countAll).getSeventh());
                        opt2.setText("[B] " + allArrays.get(countAll).getEighth());
                        opt3.setText("[C] " + allArrays.get(countAll).getNineth());
                        opt4.setText("[D] " + allArrays.get(countAll).getTenth());
                        opt1.setBackgroundResource(R.drawable.quiz_opt);
                        opt2.setBackgroundResource(R.drawable.quiz_opt);
                        opt3.setBackgroundResource(R.drawable.quiz_opt);
                        opt4.setBackgroundResource(R.drawable.quiz_opt);
                        opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt4.setTextColor(getResources().getColor(R.color.colorPrimary));*/
                        setCorrectAns(countAll);
                        /*String correct = "";
                        String correctImg = "";
                        if (allArrays.get(countAll).getFourth().equals("1")) {
                            correctImg = allArrays.get(countAll).getSeventh();
                            correct = "[A] " + allArrays.get(countAll).getSeventh();
                        } else if (allArrays.get(countAll).getFourth().equals("2")) {
                            correctImg = allArrays.get(countAll).getEighth();
                            correct = "[B] " + allArrays.get(countAll).getEighth();
                        } else if (allArrays.get(countAll).getFourth().equals("3")) {
                            correctImg = allArrays.get(countAll).getNineth();
                            correct = "[C] " + allArrays.get(countAll).getNineth();
                        } else if (allArrays.get(countAll).getFourth().equals("4")) {
                            correctImg = allArrays.get(countAll).getTenth();
                            correct = "[D] " + allArrays.get(countAll).getTenth();
                        }

                        if (correctImg != "") {
                            if (isURL(correctImg)) {
                                img_CorrectAns.setVisibility(View.VISIBLE);
                                correctAnsDisplay.setVisibility(View.GONE);
                                Glide.with(PracticeTestQAActivity.this)
                                        .load(correctImg).asBitmap().dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                        .into(img_CorrectAns);

                            } else {
                                img_CorrectAns.setVisibility(View.GONE);
                                correctAnsDisplay.setVisibility(View.VISIBLE);
                                correctAnsDisplay.setText("Correct Ans : " + correct);
                            }
                        }
                        if (correctAns == null) {
                            correctAns = "0";
                        }
                        if (givenAns == null) {
                            givenAns = "0";
                        }
                        for (int i = 1; i <= 4; i++) {
                            int color = 0;
                            int colorText = 0;

                            back_opt1.setPadding(15, 0, 15, 0);
                            back_opt2.setPadding(15, 0, 15, 0);
                            back_opt3.setPadding(15, 0, 15, 0);
                            back_opt4.setPadding(15, 0, 15, 0);

                            opt1.setPadding(15, 0, 0, 0);opt2.setPadding(15, 0, 0, 0);
                            opt3.setPadding(15, 0, 0, 0);
                            opt4.setPadding(15, 0, 0, 0);

                            if (givenAns.equals(i + "")) {
                                color = R.drawable.quiz_box;
                                colorText = R.color.colorPrimaryDarkbg;
                            }

                            if (color != 0) {
                                if (i == 1) {
                                    if (back_opt1.getVisibility() == View.VISIBLE) {
                                        back_opt1.setBackgroundResource(color);
                                        back_opt1.setPadding(15, 0, 15, 0);
                                    } else {
                                        opt1.setBackgroundResource(color);
                                        opt1.setTextColor(getResources().getColor(colorText));
                                        opt1.setPadding(15, 0, 0, 0);}
                                } else if (i == 2) {
                                    if (back_opt2.getVisibility() == View.VISIBLE) {
                                        back_opt2.setBackgroundResource(color);
                                        back_opt2.setPadding(15, 0, 15, 0);
                                    } else {
                                        opt2.setBackgroundResource(color);
                                        opt2.setTextColor(getResources().getColor(colorText));
                                        opt2.setPadding(15, 0, 0, 0);
                                    }
                                } else if (i == 3) {
                                    if (back_opt3.getVisibility() == View.VISIBLE) {
                                        back_opt3.setBackgroundResource(color);
                                        back_opt3.setPadding(15, 0, 15, 0);
                                    } else {
                                        opt3.setBackgroundResource(color);
                                        opt3.setTextColor(getResources().getColor(colorText));
                                        opt3.setPadding(15, 0, 0, 0);
                                    }
                                } else {
                                    if (back_opt4.getVisibility() == View.VISIBLE) {
                                        back_opt4.setBackgroundResource(color);
                                        back_opt4.setPadding(15, 0, 15, 0);
                                    } else {
                                        opt4.setBackgroundResource(color);
                                        opt4.setTextColor(getResources().getColor(colorText));
                                        opt4.setPadding(15, 0, 0, 0);
                                    }
                                }
                            }
                        }*/
                    }
                }
            });

            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countAll--;
                    next.setVisibility(View.VISIBLE);
                    previous.setVisibility(View.VISIBLE);
                    if (countAll == 0) {
                        next.setVisibility(View.VISIBLE);
                        previous.setVisibility(View.GONE);
                    }
                    if (allArrays.size() >= countAll) {
                        quesId = allArrays.get(countAll).getFirst();
                        correctAns = allArrays.get(countAll).getFourth();
                        givenAns = allArrays.get(countAll).getThird();
                        //  quesNo.setText(allArrays.get(countAll).getSixth());
                        question = allArrays.get(countAll).getSecond();
                        if (isURL(question)) {
                            img_ques.setVisibility(View.VISIBLE);
                            text_ques.setVisibility(View.GONE);
                            card_img_ques.setVisibility(View.VISIBLE);
                            getImageSize(question, "que");
                            //test
                           /* Glide.with(PracticeTestQAActivity.this)
                                    .load(question).asBitmap().dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(img_ques);*/
                            ques_no_img.setText(allArrays.get(countAll).getSixth());
                        } else {
                            img_ques.setVisibility(View.GONE);
                            text_ques.setVisibility(View.VISIBLE);
                            card_img_ques.setVisibility(View.GONE);
                            ques.setText(allArrays.get(countAll).getSixth() + " " + allArrays.get(countAll).getSecond());
                        }
                        //    ques.setText(allArrays.get(countAll).getSixth() + " " + allArrays.get(countAll).getSecond());



                        /*
                        opt1.setText("[A] " + allArrays.get(countAll).getSeventh());
                        opt2.setText("[B] " + allArrays.get(countAll).getEighth());
                        opt3.setText("[C] " + allArrays.get(countAll).getNineth());
                        opt4.setText("[D] " + allArrays.get(countAll).getTenth());
                        opt1.setBackgroundResource(R.drawable.quiz_opt);
                        opt2.setBackgroundResource(R.drawable.quiz_opt);
                        opt3.setBackgroundResource(R.drawable.quiz_opt);
                        opt4.setBackgroundResource(R.drawable.quiz_opt);
                        opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt4.setTextColor(getResources().getColor(R.color.colorPrimary));*/

                        //  frame.bringToFront();
                        setCorrectAns(countAll);
                       /* String correct = "";
                        String correctImg = "";
                        if (allArrays.get(countAll).getFourth().equals("1")) {
                            correctImg = allArrays.get(countAll).getSeventh();
                            correct = "[A] " + allArrays.get(countAll).getSeventh();
                        } else if (allArrays.get(countAll).getFourth().equals("2")) {
                            correctImg = allArrays.get(countAll).getEighth();
                            correct = "[B] " + allArrays.get(countAll).getEighth();
                        } else if (allArrays.get(countAll).getFourth().equals("3")) {
                            correctImg = allArrays.get(countAll).getNineth();
                            correct = "[C] " + allArrays.get(countAll).getNineth();
                        } else if (allArrays.get(countAll).getFourth().equals("4")) {
                            correctImg = allArrays.get(countAll).getTenth();
                            correct = "[D] " + allArrays.get(countAll).getTenth();
                        }

                        if (correctImg != "") {
                            if (isURL(correctImg)) {
                                img_CorrectAns.setVisibility(View.VISIBLE);
                                correctAnsDisplay.setVisibility(View.GONE);
                                Glide.with(PracticeTestQAActivity.this)
                                        .load(correctImg).asBitmap().dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                        .into(img_CorrectAns);

                            } else {
                                img_CorrectAns.setVisibility(View.GONE);
                                correctAnsDisplay.setVisibility(View.VISIBLE);
                                correctAnsDisplay.setText("Correct Ans : " + correct);
                            }
                        }

//                        String correct = "";
//
//                        if (allArrays.get(countAll).getFourth().equals("1")) {
//                            correct = "[A] " + allArrays.get(countAll).getSeventh();
//                        } else if (allArrays.get(countAll).getFourth().equals("2")) {
//                            correct = "[B] " + allArrays.get(countAll).getEighth();
//                        } else if (allArrays.get(countAll).getFourth().equals("3")) {
//                            correct = "[C] " + allArrays.get(countAll).getNineth();
//                        } else if (allArrays.get(countAll).getFourth().equals("4")) {
//                            correct = "[D] " + allArrays.get(countAll).getTenth();
//                        }
//                        correctAnsDisplay.setText("Correct Ans : " + correct);

                        if (correctAns == null) {
                            correctAns = "0";
                        }
                        if (givenAns == null) {
                            givenAns = "0";
                        }
                        for (int i = 1; i <= 4; i++) {
                            int color = 0;
                            int colorText = 0;

                            back_opt1.setPadding(15, 0, 15, 0);
                            back_opt2.setPadding(15, 0, 15, 0);
                            back_opt3.setPadding(15, 0, 15, 0);
                            back_opt4.setPadding(15, 0, 15, 0);

                            opt1.setPadding(15, 0, 0, 0);opt2.setPadding(15, 0, 0, 0);
                            opt3.setPadding(15, 0, 0, 0);
                            opt4.setPadding(15, 0, 0, 0);
                            *//*if (correctAns.equals(i + "") && givenAns.equals(i + "")) {
                                color = R.drawable.quiz_right;
                                colorText = R.color.White;
                            } else if (correctAns.equals(i + "")) {
                                color = R.drawable.quiz_right_diff;
                                colorText = R.color.Black;
                            } else *//*
                            if (givenAns.equals(i + "")) {
                                color = R.drawable.quiz_box;
                                colorText = R.color.colorPrimaryDarkbg;
                            }

                            if (color != 0) {
                                if (i == 1) {
                                    if (back_opt1.getVisibility() == View.VISIBLE) {
                                        back_opt1.setBackgroundResource(color);
                                        back_opt1.setPadding(15, 0, 15, 0);
                                    } else {
                                        opt1.setBackgroundResource(color);
                                        opt1.setTextColor(getResources().getColor(colorText));
                                        opt1.setPadding(15, 0, 0, 0);}
                                } else if (i == 2) {
                                    if (back_opt2.getVisibility() == View.VISIBLE) {
                                        back_opt2.setBackgroundResource(color);
                                        back_opt2.setPadding(15, 0, 15, 0);
                                    } else {
                                        opt2.setBackgroundResource(color);
                                        opt2.setTextColor(getResources().getColor(colorText));
                                        opt2.setPadding(15, 0, 0, 0);
                                    }
                                } else if (i == 3) {
                                    if (back_opt3.getVisibility() == View.VISIBLE) {
                                        back_opt3.setBackgroundResource(color);
                                        back_opt3.setPadding(15, 0, 15, 0);
                                    } else {
                                        opt3.setBackgroundResource(color);
                                        opt3.setTextColor(getResources().getColor(colorText));
                                        opt3.setPadding(15, 0, 0, 0);
                                    }
                                } else {
                                    if (back_opt4.getVisibility() == View.VISIBLE) {
                                        back_opt4.setBackgroundResource(color);
                                        back_opt4.setPadding(15, 0, 15, 0);
                                    } else {
                                        opt4.setBackgroundResource(color);
                                        opt4.setTextColor(getResources().getColor(colorText));
                                        opt4.setPadding(15, 0, 0, 0);
                                    }
                                }
                            }

                        }*/
                    }
                }
            });
        } catch (Exception ex) {
            Constants.writelog("PracticeTestQAActivity", "onCreate 1043:" + ex.getMessage());
        }
    }

    private void getQuestion() {
        try {
            if (isOnline()) {
                frame_progress.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                mStudentId = Datastorage.GetStudentId(getApplicationContext());
                mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
                String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
                Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                        .GetPracticeTestQAList(mStudentId, mSchoolId, mYearId, testId, SchoolDetails.appname + "", Constants.CODEVERSION, Constants.PLATFORM);
                call.enqueue(new Callback<AppService>() {
                    @Override
                    public void onResponse(Call<AppService> call, Response<AppService> response) {
                        try {
                            frame_progress.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            rl_test.setVisibility(View.VISIBLE);
                            AppService tmps = response.body();
                            if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                    && tmps.getResponse().equalsIgnoreCase("1") && !tmps.getStrResult().isEmpty()) {
                                empty_practiceTestBtm.setVisibility(View.GONE);
                                if (tmps.getValue() != null && !tmps.getValue().isEmpty()) {
                                    /*if (testTicks == null || testTicks.isEmpty()) {
                                        db.updateTestTicks(rowTestID, tmps.getValue());
                                    }*/
                                    testticksfromservice=tmps.getValue();
                                    teststartdatefromservice=tmps.getValue1();
                                }
                                if (ispeding.equalsIgnoreCase("1")) {
                                    ArrayList<String> quesIds = new ArrayList<>();
                                    ArrayList<String> ansGivenIds = new ArrayList<>();
                                    String lastQuestionId = "";
                                    int skipcount = 0;
                                    int fromDisplayCount = 0;
                                    List<Contact> testDetails = db.getPracticeTestDetailByID(Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId), Integer.parseInt(testId));
                                    if (testDetails != null && testDetails.size() > 0) {
                                        ansString = testDetails.get(0).getTestAnsString();
                                    }
                                    if (ansString != null && !ansString.isEmpty()) {
                                        String[] splitans = ansString.split("@@");
                                        if (splitans != null && splitans.length > 0) {
                                            for (int i = 0; i < splitans.length; i++) {
                                                String[] splitforId = splitans[i].split(",");
                                                quesIds.add(splitforId[0]);
                                                ansGivenIds.add(splitforId[1]);
                                            }
                                            String[] splitforId = splitans[splitans.length - 1].split(",");
                                            lastQuestionId = splitforId[0];
                                        }

                                    }
                                    skipLyt.setVisibility(View.VISIBLE);
                                    if (!forExpiretestafterstart.equalsIgnoreCase("0")) {
                   /* SimpleDateFormat df = new SimpleDateFormat("dd-MM-YYYY HH:mm");
                    currentDateandTimeTemp = df.format(new Date());

                    Calendar calCurrent = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY HH:mm", Locale.ENGLISH);
                    calCurrent.setTime(sdf.parse(currentDateandTimeTemp));// all done
*/
                                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                        // String currentDateandTimeTemp = df.format(new Date());
                                        Calendar calCurrent = Calendar.getInstance();
                                        currentDateandTimeTemp = df.format(calCurrent.getTime());
                  /*  Date d1 = null;
                    try {
                        d1 = df.parse(currentDateandTimeTemp);
                        Log.e("dateAfterDate:: ", d1.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }*/
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<ArrayList<Contact>>() {
                                        }
                                                .getType();
                                        contactsTestList = gson.fromJson(mcommon.getSession(Constants.PractiseTestData), type);
                                        boolean isPresent = false;

                                        if (contactsTestList != null && contactsTestList.size() > 0) {
                                            //    for (int j = 0; j < contactsTestList.size(); j++) {
                                            // int index = contactsTestList.indexOf(new Contact(contactsTestList.get(j).getTestId(), Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId)));
                                            int index = contactsTestList.indexOf(new Contact(testId, Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId)));
                                            if (index != -1) {
                                                isPresent = true;
                                                //break;
                                            } else {
                                                isPresent = false;
                                            }
                                            //   }
                                            if (isPresent == false) {
                                                String fDate = addMinTocurrentTime(Integer.parseInt(forExpiretestafterstart));
                                                Contact contact = new Contact(testId, fDate, Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId));
                                                contactsTestList.add(contact);
                                                Gson gson1 = new Gson();
                                                String jsonMenu = gson1.toJson(contactsTestList);
                                                mcommon.setSession(Constants.PractiseTestData, jsonMenu);
                                            }
                                        } else {
                                            String fDate = addMinTocurrentTime(Integer.parseInt(forExpiretestafterstart));
                                            Contact contact = new Contact(testId, fDate, Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId));
                                            ArrayList<Contact> contacts = new ArrayList<>();
                                            contacts.add(contact);
                                            Gson gson1 = new Gson();
                                            String jsonMenu = gson1.toJson(contacts);
                                            mcommon.setSession(Constants.PractiseTestData, jsonMenu);
                                        }
                                        contactsTestList = gson.fromJson(mcommon.getSession(Constants.PractiseTestData), type);
                                        int index = contactsTestList.indexOf(new Contact(testId, Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId)));

                                        //     int index=  contactsTestList.indexOf(testId);
                                        testtime = contactsTestList.get(index).getTestExpireDateAfterStart();
                                        Calendar calGiven = Calendar.getInstance();
                                        SimpleDateFormat sdfGiven = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
                                        String splitDateTime[] = testtime.split(" ");
                                        String splitDate[] = splitDateTime[0].split("-");
                                        String splitTime[] = splitDateTime[1].split(":");
                                        calGiven.set(Calendar.MONTH, Integer.parseInt(splitDate[1]) - 1);
                                        calGiven.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[0]));
                                        calGiven.set(Calendar.YEAR, Integer.parseInt(splitDate[2]));
                                        calGiven.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTime[0]));
                                        calGiven.set(Calendar.MINUTE, Integer.parseInt(splitTime[1]));
                                        calGiven.set(Calendar.SECOND, Integer.parseInt(splitTime[2]));
                                        // calGiven.setTime(sdfGiven.parse(tmps.getStrlist().get(i).getSixth()));
                                        Constants.Logwrite("CurrentTime", sdfGiven.format(calGiven.getTime()));
                                        Constants.Logwrite("CurrentTimeMillis", String.valueOf(calGiven.getTimeInMillis()));

                                        if (calCurrent.getTimeInMillis() < calGiven.getTimeInMillis()) {
                                            differenceMillisec = calGiven.getTimeInMillis() - calCurrent.getTimeInMillis();
                                            long minutes = TimeUnit.MILLISECONDS.toMinutes(differenceMillisec);
                                            timeLyt.setVisibility(View.VISIBLE);
                                            // tvTestTime.setVisibility(View.VISIBLE);
                                            long noOfMinutes = minutes * 60 * 1000;
                                            //  startTimer(noOfMinutes);
                                            startTimer(differenceMillisec);
                                            //  mHandler.postDelayed(mStoptest, differenceMillisec);
                                        } else {
                       /* Intent intent = new Intent(PracticeTestQAActivity.this, PracticeTestResultActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("testId", testId);
                        intent.putExtra("testName", testName);
                        intent.putExtra("refereshflag", "1");
                        startActivity(i);
                        PracticeTestQAActivity.this.finish();*/
                                            stopCountdown();

                                            // goToResultActivity();
                                        }
                                    }

                                    for (int i = 0; i < tmps.getStrlist().size(); i++) {
                                        if (quesIds != null && quesIds.size() > 0) {
                                            if (!quesIds.contains(tmps.getStrlist().get(i).getFirst())) {
                                                if (tmps.getStrlist().get(i).getThird() == null || tmps.getStrlist().get(i).getThird().equals("0") || tmps.getStrlist().get(i).getThird().equals("")) {
                                                    tmps.getStrlist().get(i).setAnsSubmited(false);
                                                    remainingCount++;
                                                    //  listArrays.add(tmps.getStrlist().get(i));
                                                }
                                            } else {
                                                int index = quesIds.indexOf(tmps.getStrlist().get(i).getFirst());
                                                tmps.getStrlist().get(i).setThird(ansGivenIds.get(index));
                                            }
                                        } else {
                                            if (tmps.getStrlist().get(i).getThird() == null || tmps.getStrlist().get(i).getThird().equals("0") || tmps.getStrlist().get(i).getThird().equals("")) {
                                                tmps.getStrlist().get(i).setAnsSubmited(false);
                                                remainingCount = tmps.getStrlist().size();
                                                // listArrays.add(tmps.getStrlist().get(i));
                                            }
                                        }
                                    }
                                    listArrays.clear();
                                    for (int i = 0; i < tmps.getStrlist().size(); i++) {
                                        //test

                                        if (isURL(tmps.getStrlist().get(i).getSecond())) {
                                            String[] questionPSplit = tmps.getStrlist().get(i).getSecond().split("/");
                                            String questionP = questionPSplit[questionPSplit.length - 1];
                                            new savePracticeTestImg().execute(tmps.getStrlist().get(i).getSecond(), questionP);

                                        }
                                        if (isURL(tmps.getStrlist().get(i).getSeventh())) {
                                            String[] opt1PSplit = tmps.getStrlist().get(i).getSeventh().split("/");
                                            String opt1P = opt1PSplit[opt1PSplit.length - 1];
                                            new savePracticeTestImg().execute(tmps.getStrlist().get(i).getSeventh(), opt1P);

                                        }
                                        if (isURL(tmps.getStrlist().get(i).getEighth())) {
                                            String[] opt2PSplit = tmps.getStrlist().get(i).getEighth().split("/");
                                            String opt2P = opt2PSplit[opt2PSplit.length - 1];
                                            new savePracticeTestImg().execute(tmps.getStrlist().get(i).getEighth(), opt2P);

                                        }
                                        if (isURL(tmps.getStrlist().get(i).getNineth())) {
                                            String[] opt3PSplit = tmps.getStrlist().get(i).getNineth().split("/");
                                            String opt3P = opt3PSplit[opt3PSplit.length - 1];
                                            new savePracticeTestImg().execute(tmps.getStrlist().get(i).getNineth(), opt3P);

                                        }
                                        if (isURL(tmps.getStrlist().get(i).getTenth())) {
                                            String[] opt4PSplit = tmps.getStrlist().get(i).getTenth().split("/");
                                            String opt4P = opt4PSplit[opt4PSplit.length - 1];
                                            new savePracticeTestImg().execute(tmps.getStrlist().get(i).getTenth(), opt4P);

                                        }
                                        listArrays.add(tmps.getStrlist().get(i));
                                    }
                                    //to get skip count and to get display count
                                    for (int i = 0; i < tmps.getStrlist().size(); i++) {
                                        if (lastQuestionId != null && !lastQuestionId.isEmpty()) {
                                            if (Integer.parseInt(lastQuestionId) == Integer.parseInt(tmps.getStrlist().get(i).getFirst())) {
                                                count = i;
                                                continue;

                                            } else if (Integer.parseInt(lastQuestionId) > Integer.parseInt(tmps.getStrlist().get(i).getFirst())) {
                                                if (tmps.getStrlist().get(i).getThird() == null || tmps.getStrlist().get(i).getThird().equals("0") || tmps.getStrlist().get(i).getThird().equals("")) {
                                                    tmps.getStrlist().get(i).setAnsSubmited(false);
                                                    SkipArrays.add(tmps.getStrlist().get(i));
                                                    skipcount++;
                                                    count = i;
                                                }
                                            } else {
                                                count = i;
                                                break;
                                            }
                                        } else {
                                            count = 0;
                                        }
                                    }

                                    totalQuestion = tmps.getStrlist().size();
                                    attemptCount = quesIds.size();
                                    if (skipcount > remainingCount)
                                        remainingCount = skipcount - remainingCount;
                                    else
                                        remainingCount = remainingCount - skipcount;

                                    tvTotalCount.setText(Html.fromHtml("<b>Total : </b>" + totalQuestion));
                                    tvRemainingCount.setText(Html.fromHtml("<b>Pending : </b>" + remainingCount));
                                    tvSkipCount.setText(String.valueOf(Html.fromHtml("<b>Skipped : <b/>" + skipcount)));
                                    tvAttemptCount.setText(String.valueOf(Html.fromHtml("<b>Attempted : <b/>" + quesIds.size())));
                                   /* if(listArrays.size()==1){
                                        skipLyt.setVisibility(View.GONE);
                                    }else{*/
                                    skipLyt.setVisibility(View.VISIBLE);
                                    /* }*/
                                    setQuestionDiff(count, listArrays);
                                    /*if (listArrays != null) {
                                        if (listArrays.size() == 1) {
                                            isLast = "1";
                                        }
                                        back_opt1.setPadding(15, 0, 15, 0);
                                        back_opt2.setPadding(15, 0, 15, 0);
                                        back_opt3.setPadding(15, 0, 15, 0);
                                        back_opt4.setPadding(15, 0, 15, 0);

                                        opt1.setPadding(15, 0, 0, 0);opt2.setPadding(15, 0, 0, 0);
                                        opt3.setPadding(15, 0, 0, 0);
                                        opt4.setPadding(15, 0, 0, 0);

                                        quesId = listArrays.get(0).getFirst();
                                        correctAns = listArrays.get(0).getFourth();
                                        //    quesNo.setText(listArrays.get(0).getSixth());
                                        String question = listArrays.get(0).getSecond();
                                        *//* String question = "https://espschools.s3.ap-south-1.amazonaws.com/Quiz/Q33_2.png";*//*
                                        if (isURL(question)) {
                                            img_ques.setVisibility(View.VISIBLE);
                                            text_ques.setVisibility(View.GONE);
                                            card_img_ques.setVisibility(View.VISIBLE);
                                            Glide.with(PracticeTestQAActivity.this)
                                                    .load(question).asBitmap().dontAnimate()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .placeholder(R.drawable.placeholder)
                                                    .error(R.drawable.placeholder)
                                                    .into(img_ques);
                                            ques_no_img.setText(allArrays.get(0).getSixth());
                                        } else {
                                            img_ques.setVisibility(View.GONE);
                                            card_img_ques.setVisibility(View.GONE);
                                            text_ques.setVisibility(View.VISIBLE);
                                            ques.setText(listArrays.get(0).getSixth() + " " + listArrays.get(0).getSecond());
                                        }

                                        String opt_1 = listArrays.get(0).getSeventh();
                                        String opt_2 = listArrays.get(0).getEighth();
                                        String opt_3 = listArrays.get(0).getNineth();
                                        String opt_4 = listArrays.get(0).getTenth();

                                        if (isURL(opt_1)) {
                                            back_opt1.setVisibility(View.VISIBLE);
                                            opt1.setVisibility(View.GONE);
                                            Glide.with(PracticeTestQAActivity.this)
                                                    .load(opt_1).asBitmap().dontAnimate()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .placeholder(R.drawable.placeholder)
                                                    .error(R.drawable.placeholder)
                                                    .into(img_opt1);

                                            option_no_1.setText("[A] ");
                                           *//* lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT);

                                            back_opt1.setLayoutParams(lp);*//*
                                            // back_opt1.setPadding(15, 0, 0, 0);} else {
                                            back_opt1.setVisibility(View.GONE);
                                            opt1.setVisibility(View.VISIBLE);
                                            opt1.setText("[A] " + listArrays.get(0).getSeventh());
                                        }

                                        if (isURL(opt_2)) {
                                            back_opt2.setVisibility(View.VISIBLE);
                                            opt2.setVisibility(View.GONE);
                                            Glide.with(PracticeTestQAActivity.this)
                                                    .load(opt_2).asBitmap().dontAnimate()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .placeholder(R.drawable.placeholder)
                                                    .error(R.drawable.placeholder)
                                                    .into(img_opt2);
                                            option_no_2.setText("[B] ");
                                        } else {
                                            back_opt2.setVisibility(View.GONE);
                                            opt2.setVisibility(View.VISIBLE);
                                            opt2.setText("[B] " + listArrays.get(0).getEighth());
                                        }

                                        if (isURL(opt_3)) {
                                            back_opt3.setVisibility(View.VISIBLE);
                                            opt3.setVisibility(View.GONE);
                                            Glide.with(PracticeTestQAActivity.this)
                                                    .load(opt_3).asBitmap().dontAnimate()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .placeholder(R.drawable.placeholder)
                                                    .error(R.drawable.placeholder)
                                                    .into(img_opt3);
                                            option_no_3.setText("[C] ");
                                        } else {
                                            back_opt3.setVisibility(View.GONE);
                                            opt3.setVisibility(View.VISIBLE);
                                            opt3.setText("[C] " + listArrays.get(0).getNineth());
                                        }

                                        if (isURL(opt_4)) {
                                            back_opt4.setVisibility(View.VISIBLE);
                                            opt4.setVisibility(View.GONE);
                                            Glide.with(PracticeTestQAActivity.this)
                                                    .load(opt_4).asBitmap().dontAnimate()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .placeholder(R.drawable.placeholder)
                                                    .error(R.drawable.placeholder)
                                                    .into(img_opt4);
                                            option_no_4.setText("[D] ");
                                        } else {
                                            opt4.setVisibility(View.VISIBLE);
                                            back_opt4.setVisibility(View.GONE);
                                            opt4.setText("[D] " + listArrays.get(0).getTenth());
                                        }

                                   *//* opt1.setText("[A] " + listArrays.get(0).getSeventh());
                                    opt2.setText("[B] " + listArrays.get(0).getEighth());
                                    opt3.setText("[C] " + listArrays.get(0).getNineth());
                                    opt4.setText("[D] " + listArrays.get(0).getTenth());*//*

                                    }*/
                                } else {
                                    allArrays = tmps.getStrlist();
                                    opt1.setPadding(15, 0, 0, 0);
                                    opt2.setPadding(15, 0, 0, 0);
                                    opt3.setPadding(15, 0, 0, 0);
                                    opt4.setPadding(15, 0, 0, 0);
                                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    quesId = allArrays.get(0).getFirst();
                                    correctAns = allArrays.get(0).getFourth();
                                    if (allArrays.get(0).getThird() != null && !allArrays.get(0).getThird().isEmpty() && !allArrays.get(0).getThird().equalsIgnoreCase("0"))
                                        givenAns = allArrays.get(0).getThird();
                                    else
                                        givenAns = "0";
                                    //      quesNo.setText(allArrays.get(0).getSixth());
                                    question = allArrays.get(0).getSecond();
                                    if (isURL(question)) {
                                        card_img_ques.setVisibility(View.VISIBLE);
                                        img_ques.setVisibility(View.VISIBLE);
                                        text_ques.setVisibility(View.GONE);
                                        getImageSize(question, "que");
                                        //test
                                       /* Glide.with(PracticeTestQAActivity.this)
                                                .load(question).asBitmap().dontAnimate()
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .placeholder(R.drawable.placeholder)
                                                .error(R.drawable.placeholder)
                                                .into(img_ques);*/
                                        ques_no_img.setText(allArrays.get(0).getSixth());
                                    } else {
                                        card_img_ques.setVisibility(View.GONE);
                                        img_ques.setVisibility(View.GONE);
                                        text_ques.setVisibility(View.VISIBLE);
                                        ques.setText(allArrays.get(0).getSixth() + " " + allArrays.get(0).getSecond());
                                    }
                                    // ques.setText(allArrays.get(0).getSixth() + " " + allArrays.get(0).getSecond());


                                    /*String opt_1 = allArrays.get(0).getSeventh();
                                    String opt_2 = allArrays.get(0).getEighth();
                                    String opt_3 = allArrays.get(0).getNineth();
                                    String opt_4 = allArrays.get(0).getTenth();

                                    if (isURL(opt_1)) {
                                        back_opt1.setVisibility(View.VISIBLE);
                                        opt1.setVisibility(View.GONE);
                                        Glide.with(PracticeTestQAActivity.this)
                                                .load(opt_1).asBitmap().dontAnimate()
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .placeholder(R.drawable.placeholder)
                                                .error(R.drawable.placeholder)
                                                .into(img_opt1);
                                        option_no_1.setText("[A] ");
                                    } else {
                                        opt1.setVisibility(View.VISIBLE);
                                        back_opt1.setVisibility(View.GONE);
                                        opt1.setText("[A] " + allArrays.get(0).getSeventh());
                                    }

                                    if (isURL(opt_2)) {
                                        back_opt2.setVisibility(View.VISIBLE);
                                        opt2.setVisibility(View.GONE);
                                        Glide.with(PracticeTestQAActivity.this)
                                                .load(opt_2).asBitmap().dontAnimate()
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .placeholder(R.drawable.placeholder)
                                                .error(R.drawable.placeholder)
                                                .into(img_opt2);
                                        option_no_2.setText("[B] ");
                                    } else {
                                        opt2.setVisibility(View.VISIBLE);
                                        back_opt2.setVisibility(View.GONE);
                                        opt2.setText("[B] " + allArrays.get(0).getEighth());
                                    }

                                    if (isURL(opt_3)) {
                                        back_opt3.setVisibility(View.VISIBLE);
                                        opt3.setVisibility(View.GONE);
                                        Glide.with(PracticeTestQAActivity.this)
                                                .load(opt_3).asBitmap().dontAnimate()
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .placeholder(R.drawable.placeholder)
                                                .error(R.drawable.placeholder)
                                                .into(img_opt3);
                                        option_no_3.setText("[C] ");
                                    } else {
                                        opt3.setVisibility(View.VISIBLE);
                                        back_opt3.setVisibility(View.GONE);
                                        opt3.setText("[C] " + allArrays.get(0).getNineth());
                                    }

                                    if (isURL(opt_4)) {
                                        back_opt4.setVisibility(View.VISIBLE);
                                        opt4.setVisibility(View.GONE);
                                        Glide.with(PracticeTestQAActivity.this)
                                                .load(opt_4).asBitmap().dontAnimate()
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .placeholder(R.drawable.placeholder)
                                                .error(R.drawable.placeholder)
                                                .into(img_opt4);
                                        option_no_4.setText("[D] ");
                                    } else {
                                        opt4.setVisibility(View.VISIBLE);
                                        back_opt4.setVisibility(View.GONE);
                                        opt4.setText("[D] " + allArrays.get(0).getTenth());
                                    }*/

                               /* opt1.setText("[A] " + allArrays.get(0).getSeventh());
                                opt2.setText("[B] " + allArrays.get(0).getEighth());
                                opt3.setText("[C] " + allArrays.get(0).getNineth());
                                opt4.setText("[D] " + allArrays.get(0).getTenth());*/

                                    /*String correct = "";
                                    String correctImg = "";
                                    String alpha = "";
                                    if (allArrays.get(countAll).getFourth().equals("1")) {
                                        correctImg = allArrays.get(countAll).getSeventh();
                                        alpha = "[A] ";
                                        correct = "[A] " + allArrays.get(countAll).getSeventh();
                                    } else if (allArrays.get(countAll).getFourth().equals("2")) {
                                        correctImg = allArrays.get(countAll).getEighth();
                                        alpha = "[B] ";
                                        correct = "[B] " + allArrays.get(countAll).getEighth();
                                    } else if (allArrays.get(countAll).getFourth().equals("3")) {
                                        correctImg = allArrays.get(countAll).getNineth();
                                        alpha = "[C] ";
                                        correct = "[C] " + allArrays.get(countAll).getNineth();
                                    } else if (allArrays.get(countAll).getFourth().equals("4")) {
                                        correctImg = allArrays.get(countAll).getTenth();
                                        alpha = "[D] ";
                                        correct = "[D] " + allArrays.get(countAll).getTenth();
                                    }

                                    if (correctImg != "") {
                                        if (isURL(correctImg)) {
                                            img_CorrectAns.setVisibility(View.VISIBLE);
                                            correctAnsDisplay.setVisibility(View.GONE);
                                            ans_img_opt.setVisibility(View.VISIBLE);
                                            Glide.with(PracticeTestQAActivity.this)
                                                    .load(correctImg).asBitmap().dontAnimate()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .placeholder(R.drawable.placeholder)
                                                    .error(R.drawable.placeholder)
                                                    .into(img_CorrectAns);
                                            ans_img_opt.setText(alpha);

                                        } else {
                                            ans_img_opt.setVisibility(View.GONE);
                                            img_CorrectAns.setVisibility(View.GONE);
                                            correctAnsDisplay.setVisibility(View.VISIBLE);
                                            correctAnsDisplay.setText("Correct Ans : " + correct);
                                        }
                                    }*/

                                    previous.setVisibility(View.GONE);
                                    if (allArrays.size() > 1)
                                        next.setVisibility(View.VISIBLE);
                                    else
                                        next.setVisibility(View.GONE);

                                    setCorrectAns(0);
                                    //  frame.bringToFront();

                                   /* if (correctAns == null) {
                                        correctAns = "0";
                                    }
                                    givenAns = allArrays.get(0).getThird();
                                    if (givenAns == null) {
                                        givenAns = "0";
                                    }
                                    for (int i = 1; i <= 4; i++) {
                                        int color = 0;
                                        int colorText = 0;

                                        back_opt1.setPadding(15, 0, 15, 0);
                                        back_opt2.setPadding(15, 0, 15, 0);
                                        back_opt3.setPadding(15, 0, 15, 0);
                                        back_opt4.setPadding(15, 0, 15, 0);

                                        opt1.setPadding(15, 0, 0, 0);opt2.setPadding(15, 0, 0, 0);
                                        opt3.setPadding(15, 0, 0, 0);
                                        opt4.setPadding(15, 0, 0, 0);
                                    if (correctAns.equals(i + "") && givenAns.equals(i + "")) {
                                        color = R.drawable.quiz_right;
                                        colorText = R.color.white;
                                    } else if (correctAns.equals(i + "")) {
                                        color = R.drawable.quiz_right_diff;
                                        colorText = R.color.white;
                                    } else
                                        if (givenAns.equals(i + "")) {
                                            color = R.drawable.quiz_box;
                                            colorText = R.color.colorPrimaryDarkbg;
                                        }
                                        if (color != 0) {
                                            if (i == 1) {
                                                if (back_opt1.getVisibility() == View.VISIBLE) {
                                                    back_opt1.setBackgroundResource(color);
                                                    back_opt1.setPadding(15, 0, 15, 0);
                                                } else {
                                                    opt1.setBackgroundResource(color);
                                                    opt1.setTextColor(getResources().getColor(colorText));
                                                    opt1.setPadding(15, 0, 0, 0);}
                                            } else if (i == 2) {
                                                if (back_opt2.getVisibility() == View.VISIBLE) {
                                                    back_opt2.setBackgroundResource(color);
                                                    back_opt2.setPadding(15, 0, 15, 0);
                                                } else {
                                                    opt2.setBackgroundResource(color);
                                                    opt2.setTextColor(getResources().getColor(colorText));
                                                    opt2.setPadding(15, 0, 0, 0);
                                                }
                                            } else if (i == 3) {
                                                if (back_opt3.getVisibility() == View.VISIBLE) {
                                                    back_opt3.setBackgroundResource(color);
                                                    back_opt3.setPadding(15, 0, 15, 0);
                                                } else {
                                                    opt3.setBackgroundResource(color);
                                                    opt3.setTextColor(getResources().getColor(colorText));
                                                    opt3.setPadding(15, 0, 0, 0);
                                                }
                                            } else {
                                                if (back_opt4.getVisibility() == View.VISIBLE) {
                                                    back_opt4.setBackgroundResource(color);
                                                    back_opt4.setPadding(15, 0, 15, 0);
                                                } else {
                                                    opt4.setBackgroundResource(color);
                                                    opt4.setTextColor(getResources().getColor(colorText));
                                                    opt4.setPadding(15, 0, 0, 0);
                                                }
                                            }
                                        }
                                    }*/
                                }
                            } else {
                                Toast.makeText(PracticeTestQAActivityTemp.this, "Test not started yet!", Toast.LENGTH_SHORT).show();
                                PracticeTestQAActivityTemp.this.finish();
                                onBackClickAnimation();
                            }
                        } catch (Exception ex) {
                            frame_progress.setVisibility(View.GONE);
                            empty_practiceTestBtm.setVisibility(View.VISIBLE);
                            skipLyt.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            Constants.writelog("PracticeTestQAActivity", "getQuestion 1382:" + ex.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<AppService> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        frame_progress.setVisibility(View.GONE);
                        empty_practiceTestBtm.setVisibility(View.VISIBLE);
                        Constants.writelog("PracticeTestQAActivity", "getQuestion 1390:" + t.getMessage());
                    }
                });
            } else {
                empty_practiceTestBtm.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            empty_practiceTestBtm.setVisibility(View.VISIBLE);
            Constants.writelog("PracticeTestQAActivity", "getQuestion 1395:" + e.getMessage());
        }
    }

    //added on 28-03-2020 by Jameela
    private void goToResultActivity() {
        try {
            stopCountdown();
            if (isOnline()) {
                List<Contact> testDetails = db.getPracticeTestDetailByID(Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId), Integer.parseInt(testId));
                if (testDetails != null && testDetails.size() > 0) {
                    ansString = testDetails.get(0).getTestAnsString();
                    testTicks = testDetails.get(0).getTestTicks();

                    if (ansString == null) {
                        ansString = "";
                    }
                    Log.e("ansString", ansString);
                    frame_progress.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    String mStudentId = Datastorage.GetStudentId(getApplicationContext());
                    String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
                    String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
                    String   deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    String  mDeviceDetail = Build.DEVICE + "|||" + Build.MODEL + "|||" + Build.ID
                            + "|||" + Build.PRODUCT + "|||" + Build.VERSION.SDK
                            + "|||" + Build.VERSION.RELEASE + "|||" + Build.VERSION.INCREMENTAL;
                    Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                            .SetPracticeTestAnsV3(mStudentId, mSchoolId, mYearId, testId, ansString,SchoolDetails.appname+"", Constants.CODEVERSION, Constants.PLATFORM, "2 Date :: " + currentDateandTimeTemp,testTicks,deviceId,mDeviceDetail);
                    call.enqueue(new Callback<AppService>() {
                        @Override
                        public void onResponse(Call<AppService> call, Response<AppService> response) {
                            try {
                                progressBar.setVisibility(View.GONE);
                                frame_progress.setVisibility(View.GONE);
                                AppService tmps = response.body();
                                if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                        && tmps.getResponse().equalsIgnoreCase("1") && !tmps.getStrResult().isEmpty()) {
                                    db.deleteTestRecords(rowTestID);
                                    Intent i = new Intent(PracticeTestQAActivityTemp.this, PracticeTestResultActivity.class);

                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    //  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    //    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.putExtra("testId", testId);
                                    i.putExtra("testName", testName);
                                    i.putExtra("refereshflag", "1");
                                    startActivity(i);
                                    PracticeTestQAActivityTemp.this.finish();
                                    onClickAnimation();
                                }
                            } catch (Exception ex) {
                                btn_next.setEnabled(true);
                                progressBar.setVisibility(View.GONE);
                                frame_progress.setVisibility(View.GONE);
                                Constants.writelog("PracticeTestQAActivity", "setTestAns 2062:" + ex.getMessage());
                            } finally {
                                ansString = "";
                            }
                        }

                        @Override
                        public void onFailure(Call<AppService> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            btn_next.setEnabled(true);
                            frame_progress.setVisibility(View.GONE);
                            Constants.writelog("PracticeTestQAActivity", "setTestAns 1589:" + t.getMessage());
                        }
                    });
                } else {
                    Intent i = new Intent(PracticeTestQAActivityTemp.this, PracticeTestResultActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtra("testId", testId);
                    i.putExtra("testName", testName);
                    i.putExtra("refereshflag", "1");
                    startActivity(i);
                    PracticeTestQAActivityTemp.this.finish();
                    onClickAnimation();
                }
            } else {
                btn_next.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                frame_progress.setVisibility(View.GONE);
                Toast.makeText(this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            btn_next.setEnabled(true);
            Constants.writelog("PracticeTestQAActivity", "goToResultActivity 1787:" + e.getMessage());
        }
    }

    private void setTestAns() {
        ansString = "";
        if (isOnline()) {
            boolean found = false;
            long tEnd = System.currentTimeMillis();
            long tDelta = tEnd - tStart;
            long dbelapsedSeconds = db.getPracticeTestElapsedTimeByID(rowTestID, quesId);
            if (dbelapsedSeconds != 0) {
                tdelta = tDelta / 1000;
                dbelapsedSeconds = dbelapsedSeconds + tdelta;
            } else {
                dbelapsedSeconds = tDelta / 1000;
            }
            List<Contact> testDetails = db.getPracticeTestDetailByID(Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId), Integer.parseInt(testId));
            if((testTicks==null || testTicks.isEmpty()) && (testticksfromservice!=null && !testticksfromservice.isEmpty())){
                    if (testTicks == null || testTicks.isEmpty()) {
                        int count=db.updateTestTicksAndStartDate(rowTestID,testticksfromservice/*,teststartdatefromservice*/);
                        if(count>0)
                        testTicks=testticksfromservice;
                    }
            }
            if (testDetails != null && testDetails.size() > 0) {
                ansString = testDetails.get(0).getTestAnsString();
            }
            if (ansString == null || ansString.equalsIgnoreCase("") || ansString.isEmpty()) {
                ansString = quesId + "," + String.valueOf(selected) + "," + String.valueOf(dbelapsedSeconds);
                listArrays.get(count).setThird(String.valueOf(selected));
            } else {
                String[] splitans = ansString.split("@@");
                if (splitans != null && splitans.length > 0) {
                    for (int i = 0; i < splitans.length; i++) {
                        String[] splitforId = splitans[i].split(",");
                        if (splitforId[0].equalsIgnoreCase(quesId)) {
                            List<String> list = new ArrayList<>(Arrays.asList(splitans));
                            list.remove(i);
                            list.add(quesId + "," + String.valueOf(selected) + "," + String.valueOf(dbelapsedSeconds));
                            splitans = list.toArray(new String[0]);
                            listArrays.get(count).setThird(String.valueOf(selected));
                            attemptCount = attemptCount - 1;
                            remainingCount = remainingCount + 1;
                            found = true;
                            ansString = "";
                            break;
                        }
                    }
                }
                if (found == true) {
                    for (int i = 0; i < splitans.length; i++) {
                        if (ansString.isEmpty()) {
                            ansString = splitans[i];
                            listArrays.get(count).setThird(String.valueOf(selected));
                        } else {
                            ansString += "@@" + splitans[i];
                            listArrays.get(count).setThird(String.valueOf(selected));
                        }
                    }

                } else if (found == false) {
                    ansString += "@@" + quesId + "," + String.valueOf(selected) + "," + String.valueOf(dbelapsedSeconds);
                    listArrays.get(count).setThird(String.valueOf(selected));
                }
            }
            db.updateTestAnsString(rowTestID, ansString);
            if (fromEndTest == true) {
                goToResultActivity();
            } else {
                boolean isDone = false;
                count++;
                for (int i = 0; i < listArrays.size(); i++) {
                    if (listArrays.get(i).getFirst().equalsIgnoreCase(quesId)) {
                        listArrays.get(i).setAnsSubmited(true);
                        remainingCount = remainingCount - 1;
                        attemptCount = attemptCount + 1;
                        tvRemainingCount.setText(Html.fromHtml("<b>Pending : </b>" + remainingCount));
                        tvAttemptCount.setText(String.valueOf(Html.fromHtml("<b>Attempted : <b/>" + attemptCount)));
                        break;
                    }
                }
                if (SkipArrays != null && SkipArrays.size() > 0) {
                    for (int i = 0; i < SkipArrays.size(); i++) {
                        if (SkipArrays.get(i).getFirst().equalsIgnoreCase(quesId)) {
                            SkipArrays.remove(i);
                            remainingCount = remainingCount + 1;
                            tvRemainingCount.setText(Html.fromHtml("<b>Pending : </b>" + remainingCount));
                            break;
                        }
                    }
                }
                tvSkipCount.setText(String.valueOf(Html.fromHtml("<b>Skipped : <b/>" + SkipArrays.size())));

                if (SkipArrays.size() > 0)
                    skipPreviousCount = SkipArrays.size();
                else
                    skipprevious.setVisibility(View.GONE);
                //   SkipArrays.remove(indexSkip);
                //  contactsTestList.remove(index);
                if ((listArrays.size() - 1) < count) {
                                /*for (int i = 0; i < listArrays.size(); i++) {
                                    if (listArrays.get(i).isAnsSubmited() == false) {
                                        count = i;
                                        isDone = true;
                                        skipprevious.setVisibility(View.GONE);
                                        break;
                                    }
                                }
                                if (isDone == false) {*/
                    count = listArrays.size();
                    /*  }*/
                } else {
                    for (int i = count; i < listArrays.size(); i++) {
                        /*if (listArrays.get(count).isAnsSubmited() == false) {*/
                        count = i;
                        isDone = true;
                        break;
                        /*}*/
                    }
                    if (isDone == false) {
                                   /* for (int i = 0; i < listArrays.size(); i++) {
                                        if (listArrays.get(i).isAnsSubmited() == false) {
                                            count = i;
                                            skipprevious.setVisibility(View.GONE);
                                            isDone = true;
                                            break;
                                        }
                                    }
                                    if (isDone == false) {*/
                        count = listArrays.size();
                        /*  }*/
                    }
                }
                setQuestionDiff(count, listArrays);

                selected = 0;
                tStart = System.currentTimeMillis();
                elapsedSeconds = 0;

            }

            /*
            frame_progress.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
            String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
            Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                    .SetPracticeTestAns(mStudentId, mSchoolId, mYearId, testId, quesId, String.valueOf(selected), String.valueOf(elapsedSeconds), isLast, Constants.APPname, Constants.CODEVERSION, Constants.PLATFORM);
            call.enqueue(new Callback<AppService>() {
                @Override
                public void onResponse(Call<AppService> call, Response<AppService> response) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        frame_progress.setVisibility(View.GONE);
                        AppService tmps = response.body();
                        boolean isDone=false;
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1") && !tmps.getStrResult().isEmpty()) {
                            count++;
                            for(int i=0;i<listArrays.size();i++){
                                if(listArrays.get(i).getFirst().equalsIgnoreCase(quesId)){
                                    listArrays.get(i).setAnsSubmited(true);
                                    break;
                                }
                            }
                            if (SkipArrays != null && SkipArrays.size() > 0) {
                                for (int i = 0; i < SkipArrays.size(); i++) {
                                    if (SkipArrays.get(i).getFirst().equalsIgnoreCase(quesId)) {
                                        SkipArrays.remove(i);
                                        break;
                                    }
                                }
                            }

                            if (SkipArrays.size() > 0)
                                skipPreviousCount = SkipArrays.size();
                            else
                                skipprevious.setVisibility(View.GONE);
                            //   SkipArrays.remove(indexSkip);
                            //  contactsTestList.remove(index);
                            if ((listArrays.size() - 1) < count) {
                                *//*for (int i = 0; i < listArrays.size(); i++) {
                                    if (listArrays.get(i).isAnsSubmited() == false) {
                                        count = i;
                                        isDone = true;
                                        skipprevious.setVisibility(View.GONE);
                                        break;
                                    }
                                }
                                if (isDone == false) {*//*
                                count = listArrays.size();
                                *//*  }*//*
                            } else {
                                for (int i = count; i < listArrays.size(); i++) {
                                    if (listArrays.get(count).isAnsSubmited() == false) {
                                        count = i;
                                        isDone = true;
                                        break;
                                    }
                                }
                                if (isDone == false) {
                                   *//* for (int i = 0; i < listArrays.size(); i++) {
                                        if (listArrays.get(i).isAnsSubmited() == false) {
                                            count = i;
                                            skipprevious.setVisibility(View.GONE);
                                            isDone = true;
                                            break;
                                        }
                                    }
                                    if (isDone == false) {*//*
                                    count = listArrays.size();
                                    *//*  }*//*
                                }
                            }
                            setQuestion(count, listArrays,0);

                            //added on 28-03-2020 by Jameela
                           *//* if (listArrays.size() > 0 && listArrays.size() > count) {
                                if (count == listArrays.size() - 1) {
                                    mHandler.removeCallbacks(mStoptest);
                                }
                            }*//*
                        }
                    } catch (Exception ex) {
                        progressBar.setVisibility(View.GONE);
                        frame_progress.setVisibility(View.GONE);
                        Constants.writelog("PracticeTestQAActivity", "setTestAns 1577:" + ex.getMessage());
                    } finally {
                        selected = 0;
                        tStart = System.currentTimeMillis();
                        elapsedSeconds = 0;
                    }
                }

                @Override
                public void onFailure(Call<AppService> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    frame_progress.setVisibility(View.GONE);
                    Constants.writelog("PracticeTestQAActivity", "setTestAns 1589:" + t.getMessage());
                }
            });*/

        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
        }
    }

    private void setCorrectAns(int setAnsCount) {
        opt_1 = allArrays.get(setAnsCount).getSeventh();
        opt_2 = allArrays.get(setAnsCount).getEighth();
        opt_3 = allArrays.get(setAnsCount).getNineth();
        opt_4 = allArrays.get(setAnsCount).getTenth();

        if (isURL(opt_1)) {
            back_opt1.setVisibility(View.VISIBLE);
            opt1.setVisibility(View.GONE);

            back_opt1.setBackgroundResource(R.drawable.quiz_opt);
            getImageSize(opt_1, "opt_1");
           /* Glide.with(PracticeTestQAActivity.this)
                    .load(opt_1).asBitmap().dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                   *//* .placeholder(R.drawable.placeholder)*//*
                    .error(R.drawable.placeholder)
                    .into(img_opt1);*/
            //test
         /*   Picasso.with(PracticeTestQAActivity.this).load(opt_1)
                    .placeholder(R.drawable.placeholder)
                    .into(img_opt1);*/
            option_no_1.setText("[A] ");

        } else {
            opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
            back_opt1.setVisibility(View.GONE);
            opt1.setVisibility(View.VISIBLE);
            opt1.setBackgroundResource(R.drawable.quiz_opt);
            opt1.setText("[A] " + allArrays.get(setAnsCount).getSeventh());
        }

        if (isURL(opt_2)) {
            back_opt2.setVisibility(View.VISIBLE);
            opt2.setVisibility(View.GONE);

            back_opt2.setBackgroundResource(R.drawable.quiz_opt);
            getImageSize(opt_2, "opt_2");
            /*Glide.with(PracticeTestQAActivity.this)
                    .load(opt_2).asBitmap().dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(img_opt2);*/
            option_no_2.setText("[B] ");
        } else {
            opt2.setVisibility(View.VISIBLE);
            opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
            back_opt2.setVisibility(View.GONE);
            opt2.setBackgroundResource(R.drawable.quiz_opt);
            opt2.setText("[B] " + allArrays.get(setAnsCount).getEighth());
        }

        if (isURL(opt_3)) {
            back_opt3.setVisibility(View.VISIBLE);
            opt3.setVisibility(View.GONE);

            back_opt3.setBackgroundResource(R.drawable.quiz_opt);
            getImageSize(opt_3, "opt_3");
            //test
           /* Glide.with(PracticeTestQAActivity.this)
                    .load(opt_3).asBitmap().dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(img_opt3);*/
            option_no_3.setText("[C] ");
        } else {
            opt3.setVisibility(View.VISIBLE);
            opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
            back_opt3.setVisibility(View.GONE);
            opt3.setBackgroundResource(R.drawable.quiz_opt);
            opt3.setText("[C] " + allArrays.get(setAnsCount).getNineth());
        }

        if (isURL(opt_4)) {
            back_opt4.setVisibility(View.VISIBLE);
            opt4.setVisibility(View.GONE);
            back_opt4.setBackgroundResource(R.drawable.quiz_opt);
            getImageSize(opt_4, "opt_4");
            //test
            /*Glide.with(PracticeTestQAActivity.this)
                    .load(opt_4).asBitmap().dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(img_opt4);*/
            option_no_4.setText("[D] ");
        } else {
            opt4.setVisibility(View.VISIBLE);
            opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
            back_opt4.setVisibility(View.GONE);
            opt4.setBackgroundResource(R.drawable.quiz_opt);
            opt4.setText("[D] " + allArrays.get(setAnsCount).getTenth());
        }
        String correct = "";
        /* String correctImg = "";*/
        String alpha = "";
        if (allArrays.get(setAnsCount).getFourth().equals("1")) {
            correctImg = allArrays.get(setAnsCount).getSeventh();
            alpha = "Correct Ans : [A] ";
            correct = "[A] " + allArrays.get(setAnsCount).getSeventh();
        } else if (allArrays.get(setAnsCount).getFourth().equals("2")) {
            correctImg = allArrays.get(setAnsCount).getEighth();
            alpha = "Correct Ans : [B] ";
            correct = "[B] " + allArrays.get(setAnsCount).getEighth();
        } else if (allArrays.get(setAnsCount).getFourth().equals("3")) {
            correctImg = allArrays.get(setAnsCount).getNineth();
            alpha = "Correct Ans : [C] ";
            correct = "[C] " + allArrays.get(setAnsCount).getNineth();
        } else if (allArrays.get(setAnsCount).getFourth().equals("4")) {
            correctImg = allArrays.get(setAnsCount).getTenth();
            alpha = "Correct Ans : [D] ";
            correct = "[D] " + allArrays.get(setAnsCount).getTenth();
        }

        if (correctImg != "") {
            if (isURL(correctImg)) {
                img_CorrectAns.setVisibility(View.VISIBLE);
                correctAnsDisplay.setVisibility(View.GONE);
                ans_img_opt.setVisibility(View.VISIBLE);
                getImageSize(correctImg, "correctAns");
                //test
              /*  Glide.with(PracticeTestQAActivity.this)
                        .load(correctImg).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img_CorrectAns);*/
                ans_img_opt.setText(alpha);

            } else {
                ans_img_opt.setVisibility(View.GONE);
                img_CorrectAns.setVisibility(View.GONE);
                correctAnsDisplay.setVisibility(View.VISIBLE);
                correctAnsDisplay.setText("Correct Ans : " + correct);
            }
        }

        /*previous.setVisibility(View.GONE);
        next.setVisibility(View.VISIBLE);*/
        //  frame.bringToFront();
        if (correctAns == null) {
            correctAns = "0";
        }
        givenAns = allArrays.get(setAnsCount).getThird();
        if (givenAns == null || givenAns.isEmpty()) {
            givenAns = "0";
        }
        for (int i = 1; i <= 4; i++) {
            int color = 0;
            int colorText = 0;

            back_opt1.setPadding(15, 0, 15, 0);
            back_opt2.setPadding(15, 0, 15, 0);
            back_opt3.setPadding(15, 0, 15, 0);
            back_opt4.setPadding(15, 0, 15, 0);

            opt1.setPadding(15, 0, 0, 0);
            opt2.setPadding(15, 0, 0, 0);
            opt3.setPadding(15, 0, 0, 0);
            opt4.setPadding(15, 0, 0, 0);
                                   /* if (correctAns.equals(i + "") && givenAns.equals(i + "")) {
                                        color = R.drawable.quiz_right;
                                        colorText = R.color.white;
                                    } else if (correctAns.equals(i + "")) {
                                        color = R.drawable.quiz_right_diff;
                                        colorText = R.color.white;
                                    } else */
            if (givenAns.equals(i + "")) {
                if (givenAns.equalsIgnoreCase(correctAns))
                    color = R.drawable.quiz_box;
                else
                    color = R.drawable.quiz_box_wrong;
                colorText = R.color.colorPrimaryDarkbg;
            }
            if (color != 0) {
                if (i == 1) {
                    if (back_opt1.getVisibility() == View.VISIBLE) {
                        back_opt1.setBackgroundResource(color);

                        back_opt1.setPadding(15, 0, 15, 0);
                    } else {

                        opt1.setBackgroundResource(color);

                        //  opt1.setBackgroundResource(color);
                        opt1.setTextColor(getResources().getColor(colorText));
                        opt1.setPadding(15, 0, 0, 0);
                    }
                } else if (i == 2) {
                    if (back_opt2.getVisibility() == View.VISIBLE) {

                        back_opt2.setBackgroundResource(color);

                        // back_opt2.setBackgroundResource(color);
                        back_opt2.setPadding(15, 0, 15, 0);
                    } else {

                        opt2.setBackgroundResource(color);

                        // opt2.setBackgroundResource(color);
                        opt2.setTextColor(getResources().getColor(colorText));
                        opt2.setPadding(15, 0, 0, 0);
                    }
                } else if (i == 3) {
                    if (back_opt3.getVisibility() == View.VISIBLE) {

                        back_opt3.setBackgroundResource(color);

                        // back_opt3.setBackgroundResource(color);
                        back_opt3.setPadding(15, 0, 15, 0);
                    } else {
                        //  opt3.setBackgroundResource(color);

                        opt3.setBackgroundResource(color);
                        opt3.setTextColor(getResources().getColor(colorText));
                        opt3.setPadding(15, 0, 0, 0);
                    }
                } else {
                    if (back_opt4.getVisibility() == View.VISIBLE) {
                        back_opt4.setBackgroundResource(color);
                        back_opt4.setPadding(15, 0, 15, 0);
                    } else {
                        opt4.setBackgroundResource(color);
                        opt4.setTextColor(getResources().getColor(colorText));
                        opt4.setPadding(15, 0, 0, 0);
                    }
                }
            }
        }
    }


    private void setPendingCorrectAns(int setAnsCount, List<AppService.ListArray> array) {
        opt_1 = array.get(setAnsCount).getSeventh();
        opt_2 = array.get(setAnsCount).getEighth();
        opt_3 = array.get(setAnsCount).getNineth();
        opt_4 = array.get(setAnsCount).getTenth();

        if (isURL(opt_1)) {
            back_opt1.setVisibility(View.VISIBLE);
            opt1.setVisibility(View.GONE);

            back_opt1.setBackgroundResource(R.drawable.quiz_opt);
            getImageSize(opt_1, "opt_1");
           /* Glide.with(PracticeTestQAActivity.this)
                    .load(opt_1).asBitmap().dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                   *//* .placeholder(R.drawable.placeholder)*//*
                    .error(R.drawable.placeholder)
                    .into(img_opt1);*/
            //test
         /*   Picasso.with(PracticeTestQAActivity.this).load(opt_1)
                    .placeholder(R.drawable.placeholder)
                    .into(img_opt1);*/
            option_no_1.setText("[A] ");

        } else {
            opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
            back_opt1.setVisibility(View.GONE);
            opt1.setVisibility(View.VISIBLE);
            opt1.setBackgroundResource(R.drawable.quiz_opt);
            opt1.setText("[A] " + array.get(setAnsCount).getSeventh());
        }

        if (isURL(opt_2)) {
            back_opt2.setVisibility(View.VISIBLE);
            opt2.setVisibility(View.GONE);

            back_opt2.setBackgroundResource(R.drawable.quiz_opt);
            getImageSize(opt_2, "opt_2");
            /*Glide.with(PracticeTestQAActivity.this)
                    .load(opt_2).asBitmap().dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(img_opt2);*/
            option_no_2.setText("[B] ");
        } else {
            opt2.setVisibility(View.VISIBLE);
            opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
            back_opt2.setVisibility(View.GONE);
            opt2.setBackgroundResource(R.drawable.quiz_opt);
            opt2.setText("[B] " + array.get(setAnsCount).getEighth());
        }

        if (isURL(opt_3)) {
            back_opt3.setVisibility(View.VISIBLE);
            opt3.setVisibility(View.GONE);

            back_opt3.setBackgroundResource(R.drawable.quiz_opt);
            getImageSize(opt_3, "opt_3");
            //test
           /* Glide.with(PracticeTestQAActivity.this)
                    .load(opt_3).asBitmap().dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(img_opt3);*/
            option_no_3.setText("[C] ");
        } else {
            opt3.setVisibility(View.VISIBLE);
            opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
            back_opt3.setVisibility(View.GONE);
            opt3.setBackgroundResource(R.drawable.quiz_opt);
            opt3.setText("[C] " + array.get(setAnsCount).getNineth());
        }

        if (isURL(opt_4)) {
            back_opt4.setVisibility(View.VISIBLE);
            opt4.setVisibility(View.GONE);
            back_opt4.setBackgroundResource(R.drawable.quiz_opt);
            getImageSize(opt_4, "opt_4");
            //test
            /*Glide.with(PracticeTestQAActivity.this)
                    .load(opt_4).asBitmap().dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(img_opt4);*/
            option_no_4.setText("[D] ");
        } else {
            opt4.setVisibility(View.VISIBLE);
            opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
            back_opt4.setVisibility(View.GONE);
            opt4.setBackgroundResource(R.drawable.quiz_opt);
            opt4.setText("[D] " + array.get(setAnsCount).getTenth());
        }

        givenAns = array.get(setAnsCount).getThird();
        if (givenAns == null || givenAns.isEmpty()) {
            givenAns = "0";
        }
        for (int i = 1; i <= 4; i++) {
            int color = 0;
            int colorText = 0;

            back_opt1.setPadding(15, 0, 15, 0);
            back_opt2.setPadding(15, 0, 15, 0);
            back_opt3.setPadding(15, 0, 15, 0);
            back_opt4.setPadding(15, 0, 15, 0);

            opt1.setPadding(15, 0, 0, 0);
            opt2.setPadding(15, 0, 0, 0);
            opt3.setPadding(15, 0, 0, 0);
            opt4.setPadding(15, 0, 0, 0);
                                   /* if (correctAns.equals(i + "") && givenAns.equals(i + "")) {
                                        color = R.drawable.quiz_right;
                                        colorText = R.color.white;
                                    } else if (correctAns.equals(i + "")) {
                                        color = R.drawable.quiz_right_diff;
                                        colorText = R.color.white;
                                    } else */
            if (givenAns.equals(i + "")) {
                color = R.drawable.quiz_box;
                colorText = R.color.colorPrimaryDarkbg;
            }
            if (color != 0) {
                if (i == 1) {
                    if (back_opt1.getVisibility() == View.VISIBLE) {
                        back_opt1.setBackgroundResource(color);
                        back_opt1.setPadding(15, 0, 15, 0);
                    } else {
                        opt1.setBackgroundResource(color);
                        opt1.setTextColor(getResources().getColor(colorText));
                        opt1.setPadding(15, 0, 0, 0);
                    }
                } else if (i == 2) {
                    if (back_opt2.getVisibility() == View.VISIBLE) {
                        back_opt2.setBackgroundResource(color);
                        back_opt2.setPadding(15, 0, 15, 0);
                    } else {
                        opt2.setBackgroundResource(color);
                        opt2.setTextColor(getResources().getColor(colorText));
                        opt2.setPadding(15, 0, 0, 0);
                    }
                } else if (i == 3) {
                    if (back_opt3.getVisibility() == View.VISIBLE) {
                        back_opt3.setBackgroundResource(color);
                        back_opt3.setPadding(15, 0, 15, 0);
                    } else {
                        opt3.setBackgroundResource(color);
                        opt3.setTextColor(getResources().getColor(colorText));
                        opt3.setPadding(15, 0, 0, 0);
                    }
                } else {
                    if (back_opt4.getVisibility() == View.VISIBLE) {
                        back_opt4.setBackgroundResource(color);
                        back_opt4.setPadding(15, 0, 15, 0);
                    } else {
                        opt4.setBackgroundResource(color);
                        opt4.setTextColor(getResources().getColor(colorText));
                        opt4.setPadding(15, 0, 0, 0);
                    }
                }
            }
        }
        frame_progress.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void setQuestion(int count, List<AppService.ListArray> array, int isFromSkipPrevious) {
        if (array.size() > 0 && array.size() > count) {
         /*     if (count == array.size() - 1) {
                isLast = "1";
            }*/

            int lastCount = 0;
            for (int i = 0; i < array.size(); i++) {
                if (array.get(i).isAnsSubmited() == true) {
                    lastCount++;
                }
            }
            if ((array.size() - 1) == lastCount) {
                // skipLyt.setVisibility(View.GONE);
                skipprevious.setVisibility(View.GONE);
                isLast = "1";
            }

            //   frame.bringToFront();
            quesId = array.get(count).getFirst();
            correctAns = array.get(count).getFourth();
            //    quesNo.setText(listArrays.get(count).getSixth());
            question = array.get(count).getSecond();
            if (isURL(question)) {
                img_ques.setVisibility(View.VISIBLE);
                text_ques.setVisibility(View.GONE);
                card_img_ques.setVisibility(View.VISIBLE);
                int height = getImageSize(question, "que");

                //  TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
                // img_ques.setLayoutParams(layoutParams);
                //test
              /*  Glide.with(PracticeTestQAActivity.this)
                        .load(question).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img_ques);*/
                ques_no_img.setText(array.get(count).getSixth());
            } else {
                img_ques.setVisibility(View.GONE);
                text_ques.setVisibility(View.VISIBLE);
                card_img_ques.setVisibility(View.GONE);
                ques.setText(array.get(count).getSixth() + " " + array.get(count).getSecond());
            }
            //  ques.setText(listArrays.get(count).getSixth() + " " + listArrays.get(count).getSecond());

            opt_1 = array.get(count).getSeventh();
            opt_2 = array.get(count).getEighth();
            opt_3 = array.get(count).getNineth();
            opt_4 = array.get(count).getTenth();

            if (isURL(opt_1)) {
                back_opt1.setVisibility(View.VISIBLE);
                opt1.setVisibility(View.GONE);
                back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                getImageSize(opt_1, "opt_1");
                //test
              /*  Glide.with(PracticeTestQAActivity.this)
                        .load(opt_1).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img_opt1);*/
                back_opt1.setPadding(15, 0, 15, 0);
                option_no_1.setText("[A] ");
            } else {
                opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                back_opt1.setVisibility(View.GONE);
                opt1.setVisibility(View.VISIBLE);
                opt1.setBackgroundResource(R.drawable.quiz_opt);
                opt1.setText("[A] " + array.get(count).getSeventh());
                opt1.setPadding(15, 0, 0, 0);
            }

            if (isURL(opt_2)) {
                back_opt2.setVisibility(View.VISIBLE);
                opt2.setVisibility(View.GONE);

                back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                getImageSize(opt_2, "opt_2");
                //test
                /*Glide.with(PracticeTestQAActivity.this)
                        .load(opt_2).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img_opt2);*/
                back_opt2.setPadding(15, 0, 15, 0);
                option_no_2.setText("[B] ");
            } else {
                opt2.setVisibility(View.VISIBLE);
                opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                back_opt2.setVisibility(View.GONE);
                opt2.setBackgroundResource(R.drawable.quiz_opt);
                opt2.setText("[B] " + array.get(count).getEighth());
                opt2.setPadding(15, 0, 0, 0);
            }

            if (isURL(opt_3)) {
                back_opt3.setVisibility(View.VISIBLE);
                opt3.setVisibility(View.GONE);
                back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                getImageSize(opt_3, "opt_3");
                //test
              /*  Glide.with(PracticeTestQAActivity.this)
                        .load(opt_3).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img_opt3);*/
                back_opt3.setPadding(15, 0, 15, 0);
                option_no_3.setText("[C] ");
            } else {
                opt3.setVisibility(View.VISIBLE);
                opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                back_opt3.setVisibility(View.GONE);
                opt3.setBackgroundResource(R.drawable.quiz_opt);
                opt3.setText("[C] " + array.get(count).getNineth());
                opt3.setPadding(15, 0, 0, 0);
            }

            if (isURL(opt_4)) {
                back_opt4.setVisibility(View.VISIBLE);
                opt4.setVisibility(View.GONE);
                back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                getImageSize(opt_4, "opt_4");
                //test
              /*  Glide.with(PracticeTestQAActivity.this)
                        .load(opt_4).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img_opt4);*/
                back_opt4.setPadding(15, 0, 15, 0);
                option_no_4.setText("[D]");
            } else {
                opt4.setVisibility(View.VISIBLE);
                opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                back_opt4.setVisibility(View.GONE);
                opt4.setBackgroundResource(R.drawable.quiz_opt);
                opt4.setText("[D] " + array.get(count).getTenth());
                opt4.setPadding(15, 0, 0, 0);
            }

            tStart = System.currentTimeMillis();
        } else {
            if (isOnline()) {
                goToResultActivity();
               /* Intent i = new Intent(PracticeTestQAActivity.this, PracticeTestResultActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("testId", testId);
                i.putExtra("testName", testName);
                i.putExtra("refereshflag", "1");
                startActivity(i);
                PracticeTestQAActivity.this.finish();*/
            } else {
                Toast.makeText(this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setQuestionDiff(int count, List<AppService.ListArray> array) {
        containsImg = false;
        if ((refereshMenu != null)) {
            refereshMenu.setVisible(false);
        }
        if (array.size() > 0 /*&& array.size() > count*/) {
         /*     if (count == array.size() - 1) {
                isLast = "1";
            }*/

            //changed on 03/06/2020
            /*if ((array.size() - 1) == count) {
                btn_next.setText("End Test");
                skip.setVisibility(View.GONE);
            } else {
                btn_next.setText("Next");
                skip.setVisibility(View.VISIBLE);
            }*/
            if ((array.size() - 1) == count) {
                btn_next.setText("End Test");
                //   skip.setVisibility(View.GONE);
            } else {
                btn_next.setText("Next");
                //   skip.setVisibility(View.VISIBLE);
            }
            skip.setText("Skip");
            selected = 0;
            if (count == 0) {
                skipprevious.setVisibility(View.GONE);
            } else {
                skipprevious.setVisibility(View.VISIBLE);
            }
            int lastCount = 0;
            if (array.get(count).getThird() != null && !array.get(count).getThird().equals("0") && !array.get(count).getThird().equals("")) {
                // //changed on 03/06/2020
                /*skip.setText("Remove Selection");
                skip.setVisibility(View.VISIBLE);
                */
                quesId = array.get(count).getFirst();
                correctAns = array.get(count).getFourth();
                givenAns = array.get(count).getThird();
                //  quesNo.setText(allArrays.get(countAll).getSixth());
                question = array.get(count).getSecond();
                if (isURL(question)) {
                    img_ques.setVisibility(View.VISIBLE);
                    text_ques.setVisibility(View.GONE);
                    card_img_ques.setVisibility(View.VISIBLE);
                    getImageSize(question, "que");
                    //test
                           /* Glide.with(PracticeTestQAActivity.this)
                                    .load(question).asBitmap().dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(img_ques);*/
                    ques_no_img.setText(array.get(count).getSixth());
                } else {
                    img_ques.setVisibility(View.GONE);
                    text_ques.setVisibility(View.VISIBLE);
                    card_img_ques.setVisibility(View.GONE);
                    ques.setText(array.get(count).getSixth() + " " + array.get(count).getSecond());
                }
                setPendingCorrectAns(count, array);
            } else {
           /* for (int i = 0; i < array.size(); i++) {
                if (array.get(i).isAnsSubmited() == true) {
                    lastCount++;
                }
            }*/
           /* if ((array.size() - 1) == lastCount) {
                // skipLyt.setVisibility(View.GONE);
                skipprevious.setVisibility(View.GONE);
                isLast = "1";
            }*/

                //   frame.bringToFront();
                quesId = array.get(count).getFirst();
                correctAns = array.get(count).getFourth();
                //    quesNo.setText(listArrays.get(count).getSixth());
                question = array.get(count).getSecond();
                if (isURL(question)) {

                    img_ques.setVisibility(View.VISIBLE);
                    text_ques.setVisibility(View.GONE);
                    card_img_ques.setVisibility(View.VISIBLE);
                    int height = getImageSize(question, "que");

                    //  TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
                    // img_ques.setLayoutParams(layoutParams);
                    //test
              /*  Glide.with(PracticeTestQAActivity.this)
                        .load(question).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img_ques);*/
                    ques_no_img.setText(array.get(count).getSixth());
                } else {
                    img_ques.setVisibility(View.GONE);
                    text_ques.setVisibility(View.VISIBLE);
                    card_img_ques.setVisibility(View.GONE);
                    ques.setText(array.get(count).getSixth() + " " + array.get(count).getSecond());
                }
                //  ques.setText(listArrays.get(count).getSixth() + " " + listArrays.get(count).getSecond());

                opt_1 = array.get(count).getSeventh();
                opt_2 = array.get(count).getEighth();
                opt_3 = array.get(count).getNineth();
                opt_4 = array.get(count).getTenth();

                if (isURL(opt_1)) {
                    back_opt1.setVisibility(View.VISIBLE);
                    opt1.setVisibility(View.GONE);
                    back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                    getImageSize(opt_1, "opt_1");
                    //test
              /*  Glide.with(PracticeTestQAActivity.this)
                        .load(opt_1).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img_opt1);*/
                    back_opt1.setPadding(15, 0, 15, 0);
                    option_no_1.setText("[A] ");
                } else {
                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    back_opt1.setVisibility(View.GONE);
                    opt1.setVisibility(View.VISIBLE);
                    opt1.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setText("[A] " + array.get(count).getSeventh());
                    opt1.setPadding(15, 0, 0, 0);
                }

                if (isURL(opt_2)) {
                    back_opt2.setVisibility(View.VISIBLE);
                    opt2.setVisibility(View.GONE);

                    back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                    getImageSize(opt_2, "opt_2");
                    //test
                /*Glide.with(PracticeTestQAActivity.this)
                        .load(opt_2).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img_opt2);*/
                    back_opt2.setPadding(15, 0, 15, 0);
                    option_no_2.setText("[B] ");
                } else {
                    opt2.setVisibility(View.VISIBLE);
                    opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                    back_opt2.setVisibility(View.GONE);
                    opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt2.setText("[B] " + array.get(count).getEighth());
                    opt2.setPadding(15, 0, 0, 0);
                }

                if (isURL(opt_3)) {
                    back_opt3.setVisibility(View.VISIBLE);
                    opt3.setVisibility(View.GONE);
                    back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                    getImageSize(opt_3, "opt_3");
                    //test
              /*  Glide.with(PracticeTestQAActivity.this)
                        .load(opt_3).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img_opt3);*/
                    back_opt3.setPadding(15, 0, 15, 0);
                    option_no_3.setText("[C] ");
                } else {
                    opt3.setVisibility(View.VISIBLE);
                    opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    back_opt3.setVisibility(View.GONE);
                    opt3.setBackgroundResource(R.drawable.quiz_opt);
                    opt3.setText("[C] " + array.get(count).getNineth());
                    opt3.setPadding(15, 0, 0, 0);
                }

                if (isURL(opt_4)) {
                    back_opt4.setVisibility(View.VISIBLE);
                    opt4.setVisibility(View.GONE);
                    back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                    getImageSize(opt_4, "opt_4");
                    //test
              /*  Glide.with(PracticeTestQAActivity.this)
                        .load(opt_4).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img_opt4);*/
                    back_opt4.setPadding(15, 0, 15, 0);
                    option_no_4.setText("[D]");
                } else {
                    opt4.setVisibility(View.VISIBLE);
                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    back_opt4.setVisibility(View.GONE);
                    opt4.setBackgroundResource(R.drawable.quiz_opt);
                    opt4.setText("[D] " + array.get(count).getTenth());
                    opt4.setPadding(15, 0, 0, 0);
                }
            }
            frame_progress.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            tStart = System.currentTimeMillis();
        } else {
            if (isOnline()) {
                goToResultActivity();
               /* Intent i = new Intent(PracticeTestQAActivity.this, PracticeTestResultActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("testId", testId);
                i.putExtra("testName", testName);
                i.putExtra("refereshflag", "1");
                startActivity(i);
                PracticeTestQAActivity.this.finish();*/
            } else {
                Toast.makeText(this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        if (elapsedSeconds != 0) {
            tdelta = tDelta / 1000;
            elapsedSeconds = elapsedSeconds + tdelta;
        } else {
            elapsedSeconds = tDelta / 1000;
        }
        db.insertUpdatePracticeTestElapseTime(rowTestID, quesId, String.valueOf(elapsedSeconds));
    }

    @Override
    protected void onResume() {
        super.onResume();
        tStart = System.currentTimeMillis();
    }

    private boolean isURL(String urlString) {
        try {
            return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches();
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.refresh_type:
                    setQuestionDiff(count, listArrays);
                    break;
                case android.R.id.home:
                    onBackPressed();
                    //   super.onBackPressed();
                   /* if (fromCompleted != "" && fromCompleted.equals("1")) {
                        ((MyApplication) PracticeTestQAActivity.this
                                .getApplicationContext()).getLocalBroadcastInstance().sendBroadcast(
                                new Intent(new Intent(PracticeTestQAActivity.this
                                        .getResources().getString(R.string.broadcast_pending_key))));
                    }
                    onBackClickAnimation();
 */
                    break;
            }
        } catch (Exception ex) {
            Constants.writelog("onOptionsItemSelected 177:", ex.getMessage());
        }
        return super.onOptionsItemSelected(item);
    }

    public int getImageSize(String urlImage, String isFrom) {
        try {
            Bitmap bitmap = null;
            containsImg = true;
            try {
               if (ispeding.equalsIgnoreCase("1")) {
                    String[] qusetionSplit = urlImage.split("/");
                   if (refereshMenu != null) {
                       if (ispeding.equalsIgnoreCase("1")) {
                           refereshMenu.setVisible(true);
                       } else {
                           refereshMenu.setVisible(false);
                           containsImg = false;
                       }
                   }
                    String path = Constants.FILE_PRACTICETEST_PATH + File.separator + testId + File.separator + qusetionSplit[qusetionSplit.length - 1];
                    File mFile = new File(path);
                    if (mFile.exists()) {
                        Display display = getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        int newWidth = size.x;
                        ;
                        Bitmap bitmapTest = BitmapFactory.decodeFile(path);

//Get actual width and height of image
                        int width = bitmapTest.getWidth();
                        int height = bitmapTest.getHeight();

                        float cal = (float) newWidth / width;
                        after_cal = Math.round(cal * height);

// Calculate the ratio between height and width of Original Image
                        float ratio = (float) height / (float) width;
                        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
                        int newHeight = Math.round((width * ratio) / scale);
                        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, after_cal);
                        if (isFrom.equalsIgnoreCase("que")) {
                            img_ques.setLayoutParams(layoutParams);
                            Glide.with(getApplicationContext()).load("file://" + path).asBitmap().into(new BitmapImageViewTarget(img_ques) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    super.setResource(resource);
                                }
                            });
                            /*Glide.with(PracticeTestQAActivity.this)
                                    .load("file://" + path)
                                    .centerCrop()
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.nopics)
                                    .into(img_ques);*/
                        } else if (isFrom.equalsIgnoreCase("opt_1")) {
                            img_opt1.setLayoutParams(layoutParams);
                            Glide.with(getApplicationContext()).load("file://" + path).asBitmap().into(new BitmapImageViewTarget(img_opt1) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    super.setResource(resource);
                                }
                            });
                            /*Glide.with(PracticeTestQAActivity.this)
                                    .load("file://" + path)
                                    .centerCrop()
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.nopics)
                                    .into(img_opt1);*/

                        } else if (isFrom.equalsIgnoreCase("opt_2")) {
                            img_opt2.setLayoutParams(layoutParams);
                           /* Glide.with(PracticeTestQAActivity.this)
                                    .load("file://" + path)
                                    .centerCrop()
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.nopics)
                                    .into(img_opt2);*/
                            Glide.with(getApplicationContext()).load("file://" + path).asBitmap().into(new BitmapImageViewTarget(img_opt2) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    super.setResource(resource);
                                }
                            });
                        } else if (isFrom.equalsIgnoreCase("opt_3")) {
                            img_opt3.setLayoutParams(layoutParams);
                            Glide.with(getApplicationContext()).load("file://" + path).asBitmap().into(new BitmapImageViewTarget(img_opt3) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    super.setResource(resource);
                                }
                            });
                           /* Glide.with(PracticeTestQAActivity.this)
                                    .load("file://" + path)
                                    .centerCrop()
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.nopics1)
                                    .into(img_opt3);*/
                        } else if (isFrom.equalsIgnoreCase("opt_4")) {
                            img_opt4.setLayoutParams(layoutParams);
                            Glide.with(getApplicationContext()).load("file://" + path).asBitmap().into(new BitmapImageViewTarget(img_opt4) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    super.setResource(resource);
                                }
                            });
                         /*   Glide.with(PracticeTestQAActivity.this)
                                    .load("file://" + path)
                                    .centerCrop()
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.nopics)
                                    .into(img_opt4);*/
                        }

                    } else {
                        new DownloadTask().execute(urlImage, isFrom);
                    }
                } else {

                new DownloadTask().execute(urlImage, isFrom);
                 }
            } catch (Exception e) {
                System.out.println(e);
            }
         /*   Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int newWidth = size.x;

//Get actual width and height of image
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

// Calculate the ratio between height and width of Original Image
            float ratio = (float) height / (float) width;
            float scale = getApplicationContext().getResources().getDisplayMetrics().density;
            int newHeight = Math.round((width * ratio) / scale);
            return newHeight;*/
        } catch (Exception e) {
            Log.e("getImageSize 1896", e.getMessage());
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_ques:
                //   setAlertZoom(question);
              /*  mBuilder = new AlertDialog.Builder(PracticeTestQAActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                ImageView photoView = mView.findViewById(R.id.imageView);
                Glide.with(PracticeTestQAActivity.this)
                        .load(question).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(photoView);
                // photoView.setImageURI(Integer.parseInt(question));
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();*/
                break;
            case R.id.img_opt1:
            case R.id.back_opt1:
                if (ispeding.equalsIgnoreCase("1")) {
                    back_opt1.setBackgroundResource(R.drawable.quiz_box);
                    back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt4.setBackgroundResource(R.drawable.quiz_opt);

                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt2.setTextColor(getResources().getColor(R.color.colorPrimary));

                    back_opt1.setPadding(15, 0, 15, 0);
                    back_opt2.setPadding(15, 0, 15, 0);
                    back_opt3.setPadding(15, 0, 15, 0);
                    back_opt4.setPadding(15, 0, 15, 0);
                    opt1.setPadding(15, 0, 0, 0);
                    opt2.setPadding(15, 0, 0, 0);
                    opt3.setPadding(15, 0, 0, 0);
                    opt4.setPadding(15, 0, 0, 0);

                    opt3.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setBackgroundResource(R.drawable.quiz_opt);
                    opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt4.setBackgroundResource(R.drawable.quiz_opt);
                    selected = 1;
                    //changed on 03/06/2020
                    /*if ((listArrays.size() - 1) == count) {
                        skip.setText("Remove Selection");
                        skip.setVisibility(View.VISIBLE);
                    }*/
                }
                //   setAlertZoom(opt_1);
               /* mBuilder = new AlertDialog.Builder(PracticeTestQAActivity.this);
                mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                photoView = mView.findViewById(R.id.imageView);
                Glide.with(PracticeTestQAActivity.this)
                        .load(opt_1).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(photoView);
                // photoView.setImageURI(Integer.parseInt(question));
                mBuilder.setView(mView);
                mDialog = mBuilder.create();
                mDialog.show();*/
                //   Toast.makeText(this, "opt1 clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_opt2:
            case R.id.back_opt2:
                if (ispeding.equalsIgnoreCase("1")) {
                    //changed on 03/06/2020
                    /*if ((listArrays.size() - 1) == count) {
                        skip.setText("Remove Selection");
                        skip.setVisibility(View.VISIBLE);
                    }
*/
                    opt3.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setBackgroundResource(R.drawable.quiz_opt);
                    opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt4.setBackgroundResource(R.drawable.quiz_opt);

                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt2.setTextColor(getResources().getColor(R.color.colorPrimary));

                    back_opt2.setBackgroundResource(R.drawable.quiz_box);
                    back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setPadding(15, 0, 0, 0);
                    opt2.setPadding(15, 0, 0, 0);
                    opt3.setPadding(15, 0, 0, 0);
                    opt4.setPadding(15, 0, 0, 0);
                    back_opt1.setPadding(15, 0, 15, 0);
                    back_opt2.setPadding(15, 0, 15, 0);
                    back_opt3.setPadding(15, 0, 15, 0);
                    back_opt4.setPadding(15, 0, 15, 0);
                    selected = 2;
                }
                // setAlertZoom(opt_2);
              /*  mBuilder = new AlertDialog.Builder(PracticeTestQAActivity.this);
                mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                photoView = mView.findViewById(R.id.imageView);
                Glide.with(PracticeTestQAActivity.this)
                        .load(opt_2).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(photoView);
                // photoView.setImageURI(Integer.parseInt(question));
                mBuilder.setView(mView);
                mDialog = mBuilder.create();
                mDialog.show();*/

                // Toast.makeText(this, "opt1 clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.img_opt3:
            case R.id.back_opt3:
                if (ispeding.equalsIgnoreCase("1")) {
                    //changed on 03/06/2020
                    /*if ((listArrays.size() - 1) == count) {
                        skip.setText("Remove Selection");
                        skip.setVisibility(View.VISIBLE);
                    }
*/
                    opt3.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setBackgroundResource(R.drawable.quiz_opt);
                    opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt4.setBackgroundResource(R.drawable.quiz_opt);

                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt2.setTextColor(getResources().getColor(R.color.colorPrimary));

                    back_opt3.setBackgroundResource(R.drawable.quiz_box);
                    back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setPadding(15, 0, 0, 0);
                    opt2.setPadding(15, 0, 0, 0);
                    opt3.setPadding(15, 0, 0, 0);
                    opt4.setPadding(15, 0, 0, 0);
                    back_opt1.setPadding(15, 0, 15, 0);
                    back_opt2.setPadding(15, 0, 15, 0);
                    back_opt3.setPadding(15, 0, 15, 0);
                    back_opt4.setPadding(15, 0, 15, 0);
                    selected = 3;
                }
                //setAlertZoom(opt_3);
              /*  mBuilder = new AlertDialog.Builder(PracticeTestQAActivity.this);
                mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                photoView = mView.findViewById(R.id.imageView);
                Glide.with(PracticeTestQAActivity.this)
                        .load(opt_3).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(photoView);
                // photoView.setImageURI(Integer.parseInt(question));
                mBuilder.setView(mView);
                mDialog = mBuilder.create();
                mDialog.show();*/
                // Toast.makeText(this, "opt1 clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_opt4:
            case R.id.back_opt4:
                if (ispeding.equalsIgnoreCase("1")) {
                    //changed on 03/06/2020
                    /*if ((listArrays.size() - 1) == count) {
                        skip.setText("Remove Selection");
                        skip.setVisibility(View.VISIBLE);
                    }
                    */
                    opt3.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setBackgroundResource(R.drawable.quiz_opt);
                    opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt4.setBackgroundResource(R.drawable.quiz_opt);

                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt2.setTextColor(getResources().getColor(R.color.colorPrimary));

                    back_opt4.setBackgroundResource(R.drawable.quiz_box);
                    back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setPadding(15, 0, 0, 0);
                    opt2.setPadding(15, 0, 0, 0);
                    opt3.setPadding(15, 0, 0, 0);
                    opt4.setPadding(15, 0, 0, 0);
                    back_opt1.setPadding(15, 0, 15, 0);
                    back_opt2.setPadding(15, 0, 15, 0);
                    back_opt3.setPadding(15, 0, 15, 0);
                    back_opt4.setPadding(15, 0, 15, 0);
                    selected = 4;
                }
                // setAlertZoom(opt_4);
                //Toast.makeText(this, "opt1 clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_CorrectAns:
                // setAlertZoom(correctImg);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_refresh, menu);
        refereshMenu = menu.findItem(R.id.refresh_type);
        if (ispeding.equalsIgnoreCase("1") && containsImg == true)
            refereshMenu.setVisible(true);
        else
            refereshMenu.setVisible(false);
        return true;
    }

    public void setAlertZoom(String imgUrl) {
        mBuilder = new AlertDialog.Builder(PracticeTestQAActivityTemp.this);
        mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
        photoView = mView.findViewById(R.id.imageView);
        Glide.with(PracticeTestQAActivityTemp.this)
                .load(imgUrl).asBitmap().dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(photoView);
        // photoView.setImageURI(Integer.parseInt(question));
        mBuilder.setView(mView);
        mDialog = mBuilder.create();
        mDialog.show();
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        try {
            if (fromCompleted != "" && fromCompleted.equals("1")) {
                ((MyApplication) PracticeTestQAActivityTemp.this
                        .getApplicationContext()).getLocalBroadcastInstance().sendBroadcast(
                        new Intent(new Intent(PracticeTestQAActivityTemp.this
                                .getResources().getString(R.string.broadcast_pending_key))));
            }
            PracticeTestQAActivityTemp.this.finish();
            onBackClickAnimation();
        } catch (Exception e) {
            Constants.writelog("PracticeTestQAActivity", "onBackPressed 740:" + e.getMessage());
        }

    }

    //Stop Countdown method  //added on 28-03-2020 by Jameela
    private void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    //Start Countodwn method   //added on 28-03-2020 by Jameela
    private void startTimer(long noOfMinutes) {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                tvTestTime.setText(hms);//set text
            }

            public void onFinish() {
                //   tvTestTime.setText("TIME'S UP!!"); //On finish change timer text
                countDownTimer = null;//set CountDownTimer to null
                stopCountdown();
                // tvTestTime.setVisibility(View.GONE);
                //goToResultActivity();
            }
        }.start();

    }

    public void zoomFragment(String path) {
        MyDialogFragment dialogFragment = new MyDialogFragment();


        // ft.commit();
        Bundle bundle = new Bundle();
        bundle.putString("imgPath", path);
        bundle.putBoolean("fullScreen", true);
        bundle.putBoolean("notAlertDialog", true);

        dialogFragment.setArguments(bundle);

        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // ft.replace(R.id.frameLayout, dialogFragment).commit();
        if (prev != null) {
            ft.remove(prev);
        }

        dialogFragment.show(ft, "dialog");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);

    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.img_ques:
                //   setAlertZoom(PracticeTestQAActivity.this,question);
                zoomFragment(question);

                break;
            case R.id.img_opt1:
            case R.id.back_opt1:
                //  setAlertZoom(PracticeTestQAActivity.this ,opt_1);
                //  setAlertZoom(opt_1);
                zoomFragment(opt_1);
                break;
            case R.id.img_opt2:
            case R.id.back_opt2:
                //  setAlertZoom(PracticeTestQAActivity.this,opt_2);
                zoomFragment(opt_2);
                break;

            case R.id.img_opt3:
            case R.id.back_opt3:
                // setAlertZoom(PracticeTestQAActivity.this,opt_3);
                zoomFragment(opt_3);
                break;
            case R.id.img_opt4:
            case R.id.back_opt4:
                // setAlertZoom(PracticeTestQAActivity.this,opt_4);
                zoomFragment(opt_4);
                break;
            case R.id.img_CorrectAns:
                // setAlertZoom(PracticeTestQAActivity.this,correctImg);
                zoomFragment(correctImg);
                break;
        }
        return false;

    }

    private class DownloadTask extends AsyncTask<String, Void, Bitmap> {
        String isFrom = "";

        // Before the tasks execution
        protected void onPreExecute() {
            // Display the progress dialog on async task start
            //   mProgressDialog.show();
        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(String... urls) {
            URL url = null;
            try {
                url = new URL(urls[0]);
                isFrom = urls[1];
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection connection = null;

            try {
                // Initialize a new http url connection
                connection = (HttpURLConnection) url.openConnection();

                // Connect the http url connection
                connection.connect();

                // Get the input stream from http url connection
                InputStream inputStream = connection.getInputStream();

                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                //setImageSize(bmp);
                // Return the downloaded bitmap
                return bmp;

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result) {
            // Hide the progress dialog
            //mProgressDialog.dismiss();

            if (result != null) {
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int newWidth = size.x;

//Get actual width and height of image
                int width = result.getWidth();
                int height = result.getHeight();


                float cal = (float) newWidth / width;
                after_cal = Math.round(cal * height);

// Calculate the ratio between height and width of Original Image
                float ratio = (float) height / (float) width;
                float scale = getApplicationContext().getResources().getDisplayMetrics().density;
                int newHeight = Math.round((width * ratio) / scale);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, after_cal);
                if (isFrom.equalsIgnoreCase("que")) {
                    img_ques.setLayoutParams(layoutParams);

                    Glide.with(getApplicationContext()).load(question).asBitmap().into(new BitmapImageViewTarget(img_ques) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                        }
                    });

                   /* Glide.with(PracticeTestQAActivity.this)
                            .load(question).asBitmap().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(img_ques);*/
                } else if (isFrom.equalsIgnoreCase("opt_1")) {
                    img_opt1.setLayoutParams(layoutParams);
                    Glide.with(getApplicationContext()).load(opt_1).asBitmap().into(new BitmapImageViewTarget(img_opt1) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                        }
                    });

                  /*  Glide.with(PracticeTestQAActivity.this)
                            .load(opt_1).asBitmap().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(img_opt1);*/
                } else if (isFrom.equalsIgnoreCase("opt_2")) {
                    img_opt2.setLayoutParams(layoutParams);
                    Glide.with(getApplicationContext()).load(opt_2).asBitmap().into(new BitmapImageViewTarget(img_opt2) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                        }
                    });

                    /*Glide.with(PracticeTestQAActivity.this)
                            .load(opt_2).asBitmap().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(img_opt2);*/
                } else if (isFrom.equalsIgnoreCase("opt_3")) {
                    img_opt3.setLayoutParams(layoutParams);
                    Glide.with(getApplicationContext()).load(opt_3).asBitmap().into(new BitmapImageViewTarget(img_opt3) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                        }
                    });
                   /* Glide.with(PracticeTestQAActivity.this)
                            .load(opt_3).asBitmap().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(img_opt3);*/
                } else if (isFrom.equalsIgnoreCase("opt_4")) {
                    img_opt4.setLayoutParams(layoutParams);
                    Glide.with(getApplicationContext()).load(opt_4).asBitmap().into(new BitmapImageViewTarget(img_opt4) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                        }
                    });
                 /*   Glide.with(PracticeTestQAActivity.this)
                            .load(opt_4).asBitmap().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(img_opt4);*/
                } else if (isFrom.equalsIgnoreCase("correctAns")) {
                    img_CorrectAns.setLayoutParams(layoutParams);
                    Glide.with(getApplicationContext()).load(correctImg).asBitmap().into(new BitmapImageViewTarget(img_CorrectAns) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                        }
                    });
                    /*Glide.with(PracticeTestQAActivity.this)
                            .load(correctImg).asBitmap().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(img_CorrectAns);*/
                }
            } else {
                // Notify user that an error occurred while downloading image

            }

        }
    }

    public class savePracticeTestImg extends AsyncTask<String, String, Void> {
        @Override
        protected void onPreExecute() {
            try {
                /* progressBar.setVisibility(View.VISIBLE);
                 frame_progress.setVisibility(View.VISIBLE);*/

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String mUrl = "";
                // mUrl = mStrAudioUrl;
                mUrl = params[0];
                mUrl = mUrl.replace(" ", "%20");

                final File file = new File(mainDirSaveImage, params[1]);
                FileOutputStream f = new FileOutputStream(file);
                URL u = new URL(mUrl);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                // c.setDoOutput(true);
                c.connect();
                InputStream in = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = in.read(buffer)) > 0) {
                    f.write(buffer, 0, len1);
                }
                f.close();
              /*  int lenghtOfFile = c.getContentLength();
                byte[] buffer = new byte[2048];
                int len1 = 0;
                long total = 0;
                while ((len1 = in.read(buffer)) > 0) {
                    total += len1;
                  //  publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    f.write(buffer, 0, len1);
                }
                f.close();*/
//                if (file != null && !file.getAbsolutePath().isEmpty()) {
//                    saveAudiomessage = "Save Audio to " + "\"" + file.getAbsolutePath() + "\"" + " Successfully";
//                } else {
//                    saveAudiomessage = "Audio Save Successfully";
//                }


            } catch (Exception e) {
                Common.showToast(PracticeTestQAActivityTemp.this, e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // mProgressbar.setVisibility(View.GONE);

        }
    }


}
