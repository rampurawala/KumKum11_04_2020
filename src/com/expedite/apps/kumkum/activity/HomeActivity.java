package com.expedite.apps.kumkum.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.BuildConfig;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.adapter.HomeDataListAdapter;
import com.expedite.apps.kumkum.adapter.HomeMenuExpandableListAdapter;
import com.expedite.apps.kumkum.adapter.HomeMessageAdapter;
import com.expedite.apps.kumkum.adapter.HomeNoticeBoardAdapter;
import com.expedite.apps.kumkum.adapter.HomePhotoListAdapter;
import com.expedite.apps.kumkum.adapter.MainPracticeTestListAdapter;
import com.expedite.apps.kumkum.adapter.SocialMediaListAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.ConnectionDetector;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.fragment.PracticeTestFragment;
import com.expedite.apps.kumkum.model.ActivityVisitedModel;
import com.expedite.apps.kumkum.model.AppService;
import com.expedite.apps.kumkum.model.Banner;
import com.expedite.apps.kumkum.model.CircularModel;
import com.expedite.apps.kumkum.model.Contact;
import com.expedite.apps.kumkum.model.DailyDiaryCalendarModel;
import com.expedite.apps.kumkum.model.HomeModel;
import com.expedite.apps.kumkum.model.SocialMediaListModel;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends BaseActivity implements View.OnClickListener {
    // private ViewPager mNoticeBoardViewPager/*, mMessageViewPager*/;
    RecyclerView mMessageViewPager, mNoticeBoardViewPager;
    int mCurrentYear, mCurrentDay, mCurrentMonth;

    // private int gone;
    Common mcommon;
    List<AppService.ListArray> mainArray = new ArrayList<>();
    private int mMultiClickCount = 0;
    BroadcastReceiver mReceiverFilter;
    SimpleDateFormat formatter_to = new SimpleDateFormat("hh:mm");
    private ArrayList<ActivityVisitedModel> mActivityVisitedList = new ArrayList<>();
    private TextView mNoticeViewMore, mMessageViewMore, mPhotographsViewMore, mTxtHomeWorkCount,
            mTxtAttedenceCount, txtAllExam, txtAllMarksheetExam, txtPracticeTestCount;
    private View mLlHomeWork, mLlAttendance, mSendQuery, mCalendar, mRlFoodChart, mRlLibrary,
            mRlTimeTable, mRlCurriculum, rlCurriculumClassroom, mRlApplyLeave, mRlSocialMedia, rlDocuments, rlPracticeTest, rlTakeAppo, mLLVoiceMsg, rlELibrary;
    private ProgressBar mProgressBar;
    private ArrayList<HomeModel.HomeList> mHomeArrayList = new ArrayList<>();
    private HomeDataListAdapter mHomeDataAdapter;
    private LinearLayout mview;
    private HomeNoticeBoardAdapter mNoticeBoardAdapter;
    private HomeMessageAdapter mMessageAdapter;
    // private CirclePageIndicator indicator, mMessageIndicator;
    private float density;
    private int mMessageLimit = 5, mNoticeLimit = 5, mPhotoLimit = 6;
    private GridLayoutManager mGridLayoutManagerMain, mGridLayoutManager;
    private RecyclerView mRecycleView, mPhotographsRecycle;
    private HomePhotoListAdapter mPhotoAdapter;
    private DrawerLayout mDrawerLayout;
    private Menu mMenu;
    private CardView mCardAttendance, mCardHomework;
    List<AppService.ListArray> mainArrayPracticetest = new ArrayList<>();
    //private Toolbar mToolbar;
    //private NavigationView mNavigationView;
    private ExpandableListView mMainMenuList;
    private HomeMenuExpandableListAdapter mMenuAdapter;
    private boolean doubleBackToExitPressedOnce = false, isCircularShow = false, isPhotoGalleryShow = false, isMessageShow = false;
    private View mHeaderView, mBtnPaidFees, mBtnFreeCard, mBtnOnlinePayment, mBtnPendingFees,
            mBtnAllExam, mBtnAllMarkSheet;
    private String Stud_Image_Path = "", SchoolId, StudentId, Year_Id, ClassId, routeId, tag = "HomeActivity", mobileno, testid = "", ansString = "", currentDiffTime = "", strDate = "";
    private ImageView mProfileImage;
    // for Photographs
    private Integer[] albumid = {};
    private String[] TotalItemList = null, albumlist = {""}, albumtime = null, FilePathStrings = {""}, messages = {""}, groupforsddl = {""}, myarray = {""}, ItmDate = {""}, CircularName = {""}, Itmpath = {""}, doc = {""}, Itmgroup = {""};
    //, albumURL = null,albmimg = null;
    private File file;
    // For Message List
    private int Latest_SMS_ID = 0, testrowid = 0;
    private Time cur_time = new Time();
    // For NoticeBoardList
    private SoapObject obj2;
    Date dateSession, dateCurrrent;
    // private LocalBroadcastManager mBroadcastManager;
    //For Menu List
    private List<String> groupList, childList;
    private Map<String, List<String>> laptopCollection;
    private AlertDialog socialMediaAlert;
    private ArrayList<SocialMediaListModel.SocialMediaList> mSocialMediaArrayList = new ArrayList<>();
    private RecyclerView mSocialRecyclerView;
    private SocialMediaListAdapter mSocialAdapter;
    private RelativeLayout mRLFeeCard, mRLPaidFees, mRLOnlinePayment, mRLPendingFees;
    private ArrayList<CircularModel.Strlist> mCircularlist = new ArrayList<>();
    private AdView mAdView;
    private Button btn;
    private InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.home_activity);
        } catch (Exception ex) {
            Constants.writelog("HomeActivity", "Msg:" + ex.getMessage());
        }

        mcommon = new Common(HomeActivity.this);

        //DateFormat sdf = new DateFormat("dd/MM/yyyy");
        strDate = getTodayDate();
        //changed on 05/06/2020
      /*  if(mcommon.getSession(Constants.CurrentDate)==null || mcommon.getSession(Constants.CurrentDate).isEmpty()){
            mcommon.setSession(Constants.CurrentDate,strDate);
        }*/
        init();
        if (mcommon.getSession(Constants.ShowSubNew) == null || mcommon.getSession(Constants.ShowSubNew).isEmpty()) {
            mcommon.setSession(Constants.ShowSubNew,"1");
        }
        try {
            //   dateCurrrent=new SimpleDateFormat("dd/MM/yyyy").parse(strDate);
            //   dateSession=new SimpleDateFormat("dd/MM/yyyy").parse( mcommon.getSession(Constants.CurrentDate));
            if (mcommon.getSession(Constants.CurrentDate) == null || mcommon.getSession(Constants.CurrentDate).isEmpty() || !mcommon.getSession(Constants.CurrentDate).equalsIgnoreCase(strDate)) {
                Log.i("app", "Date1 is not  same as  Date2");
                getPracticeTestList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initListner();
    }


    private void getPracticeTestList() {
        int count = db.getCountPracticeTestDetail();
        if (count > 0) {
            if (isOnline()) {
                final String mStudentId = Datastorage.GetStudentId(getApplicationContext());
                final String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
                String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
                Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                        .GetPracticeTestList(mStudentId, mSchoolId, mYearId, SchoolDetails.appname + "", Constants.CODEVERSION, Constants.PLATFORM);
                call.enqueue(new Callback<AppService>() {
                    @Override
                    public void onResponse(Call<AppService> call, Response<AppService> response) {
                        try {
                            mProgressBar.setVisibility(View.GONE);

                            AppService tmps = response.body();
                            if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                    && tmps.getResponse().equalsIgnoreCase("1") && !tmps.getStrResult().isEmpty()) {
                                PracticeTestFragment practiceTestFragment = new PracticeTestFragment();
                                int size = tmps.getStrlist().size();

                                if (size > 0) {
                                    mainArrayPracticetest = tmps.getStrlist();
                                    setPracticeTestData(mainArrayPracticetest);

                                } else {
                                    mcommon.setSession(Constants.CurrentDate, strDate);
                                    mProgressBar.setVisibility(View.GONE);
                                }

                            } /*else {

                                Toast.makeText(HomeActivity.this, "No Test Found!", Toast.LENGTH_SHORT).show();
                            }*/
                        } catch (Exception ex) {

                            Constants.writelog(tag, "getPracticetestLiat 568:" + ex.getMessage());
                        }/*finally {
                        mSwipeLayout.setRefreshing(false);
                    }*/
                    }

                    @Override
                    public void onFailure(Call<AppService> call, Throwable t) {
                        Constants.writelog("BtmNavigationActivity", "getPracticetestLiat 578:" + t.getMessage());
                    }
                });
            } /*else {

                Toast.makeText(this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
            }*/
        } else {
            mcommon.setSession(Constants.CurrentDate, strDate);
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
            currentDiffTime = df.format(calCurrent.getTime());
            // SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY HH:mm");
            // calCurrent.setTime(sdf.parse(currentDateandTimeTemp));

//Log.e("CurrentTime",sdf.format(calCurrent.getTime()));
//Log.e("currentDiffTime",currentDiffTime);
            Constants.Logwrite("CurrentTimeMillis", String.valueOf(calCurrent.getTimeInMillis()));

            for (int i = 0; i < size; i++) {

                String testtime = "0", extraParam = "";
                testid = filterList.get(i).getFirst();

                if (filterList.get(i).getThird().equals("0") || filterList.get(i).getThird().equals("2")) {
                    List<Contact> testDetails = db.getPracticeTestDetailByID(Integer.parseInt(mSchoolId), Integer.parseInt(mStudentId), Integer.parseInt(testid));
                    ansString = "";
                    if (testDetails != null && testDetails.size() > 0) {
                        ansString = testDetails.get(0).getTestAnsString();
                        testrowid = testDetails.get(0).getROWID();
                        String testTicks = testDetails.get(0).getTestTicks();
                        extraParam = "2000"; /*Date :: " + currentDiffTime*/
                        ;
                        if (ansString == null || ansString.isEmpty()) {
                            ansString = "";
                            extraParam = "-1";
                        }
                        if (testTicks == null || testTicks.isEmpty()) {
                            testTicks = "";
                        }

                        Log.e("ansString", ansString);
                        /* if (ansString != null && !ansString.isEmpty()) {*/
                        String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
                        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        String mDeviceDetail = Build.DEVICE + "|||" + Build.MODEL + "|||" + Build.ID
                                + "|||" + Build.PRODUCT + "|||" + Build.VERSION.SDK
                                + "|||" + Build.VERSION.RELEASE + "|||" + Build.VERSION.INCREMENTAL;
                        Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                                .SetPracticeTestAnsV3(mStudentId, mSchoolId, mYearId, testid, ansString, SchoolDetails.appname + "", Constants.CODEVERSION, Constants.PLATFORM, extraParam, testTicks, deviceId, mDeviceDetail);
                        call.enqueue(new Callback<AppService>() {
                            @Override
                            public void onResponse(Call<AppService> call, Response<AppService> response) {
                                try {
                                    AppService tmps = response.body();
                                    if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                            && tmps.getResponse().equalsIgnoreCase("1") && !tmps.getStrResult().isEmpty()) {
                                        db.deleteTestRecords(testrowid);
                                        mcommon.setSession(Constants.CurrentDate, strDate);
                                        // Toast.makeText(HomeActivity.this, tmps.getStrResult(), Toast.LENGTH_SHORT).show();
                                    } /*else {
                                            Toast.makeText(HomeActivity.this, tmps.getStrResult(), Toast.LENGTH_SHORT).show();
                                        }*/
                                } catch (Exception ex) {
                                    Constants.writelog(tag, "setTestAns 642:" + ex.getMessage());
                                } finally {
                                    ansString = "";
                                }
                            }

                            @Override
                            public void onFailure(Call<AppService> call, Throwable t) {
                                Constants.writelog(tag, "setTestAns 650:" + t.getMessage());
                            }
                        });

                      /*  } else {
                            mcommon.setSession(Constants.CurrentDate, strDate);
                            db.deleteTestRecords(testrowid);
                        }*/
                    } else {
                        mcommon.setSession(Constants.CurrentDate, strDate);
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
                } else {
                    mcommon.setSession(Constants.CurrentDate, strDate);
                }
            }

        } catch (Exception ex) {
            Constants.writelog(tag, " 107:" + ex.getMessage());
        }

        //mcommon.setSession(Constants.LastAppControlCall, Today.monthDay + "2");

    }


    private void addAutoStartup() {
        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }
            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc", String.valueOf(e));
        }
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.HomeActivity);
            showFullScreenAd(HomeActivity.this, ActivityNames.HomeActivity);
          /*  mBroadcastManager = ((MyApplication) getApplicationContext())
                    .getLocalBroadcastInstance();*/

            mobileno = Datastorage.GetPhoneNumber(getApplicationContext());
            if (mobileno.isEmpty()) {
                mobileno = db.getmobileNo(StudentId, SchoolId);
            }
            db = new DatabaseHandler(HomeActivity.this);
            file = Constants.CreatePhotoGalleryFolder();
            try {
                if (!mcommon.getSession(Constants.APPname).equalsIgnoreCase("")) {
                    Constants.setActionbarHome(getSupportActionBar(), HomeActivity.this, HomeActivity.this,
                            mcommon.getSession(Constants.APPname), "Home");
                } else {
                    Constants.setActionbarHome(getSupportActionBar(), HomeActivity.this, HomeActivity.this,
                            getString(R.string.app_name), "Home");
                }
                getSupportActionBar().setHomeButtonEnabled(true);
            } catch (NullPointerException ex) {
                Common.printStackTrace(ex);
            }

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.VISIBLE);
            mTxtHomeWorkCount = (TextView) findViewById(R.id.txtHomeWorkCount);
            mLlHomeWork = (View) findViewById(R.id.ll_HomeWork);
            mLlAttendance = (View) findViewById(R.id.ll_Attendance);
            mTxtAttedenceCount = (TextView) findViewById(R.id.txtAttedenceCount);

            mRLFeeCard = (RelativeLayout) findViewById(R.id.RlFeeCard);
            mRLPaidFees = (RelativeLayout) findViewById(R.id.RLPaidFees);
            mRLOnlinePayment = (RelativeLayout) findViewById(R.id.RlOnlinePayment);
            mRLPendingFees = (RelativeLayout) findViewById(R.id.RlpendingFees);
            mGridLayoutManagerMain = new GridLayoutManager(HomeActivity.this, 1);
            mRecycleView = (RecyclerView) findViewById(R.id.RecycleView);
            mRecycleView.setLayoutManager(mGridLayoutManagerMain);
            mRecycleView.setNestedScrollingEnabled(false);
            mHomeDataAdapter = new HomeDataListAdapter(HomeActivity.this, mHomeArrayList);
            mRecycleView.setAdapter(mHomeDataAdapter);
            mNoticeViewMore = (TextView) findViewById(R.id.NoticeViewMore);
            mNoticeBoardViewPager = findViewById(R.id.noticeBoardViewPager);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mNoticeBoardViewPager.setLayoutManager(linearLayoutManager);
            mview = (LinearLayout) findViewById(R.id.advertiseView);
            mNoticeBoardAdapter = new HomeNoticeBoardAdapter(HomeActivity.this, CircularName, ItmDate, Itmgroup);
            mNoticeBoardViewPager.setAdapter(mNoticeBoardAdapter);
            //   indicator = (CirclePageIndicator) findViewById(R.id.indicator);
            //    indicator.setViewPager(mNoticeBoardViewPager);
            density = getResources().getDisplayMetrics().density;
            //  indicator.setRadius(5 * density);
            mMessageViewMore = (TextView) findViewById(R.id.MessageViewMore);
            txtPracticeTestCount = findViewById(R.id.txtPracticeTestCount);
            mMessageViewPager = findViewById(R.id.MessageViewPager);

            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mMessageViewPager.setLayoutManager(linearLayoutManager1);
            mMessageAdapter = new HomeMessageAdapter(messages, this);
            mMessageViewPager.setAdapter(mMessageAdapter);


            //  mMessageAdapter = new HomeMessageAdapter(HomeActivity.this, messages);
            // mMessageViewPager.setAdapter(mMessageAdapter);
            //   mMessageIndicator = (CirclePageIndicator) findViewById(R.id.MessageIndicator);
            //   mMessageIndicator.setViewPager(mMessageViewPager);
            density = getResources().getDisplayMetrics().density;
            // mMessageIndicator.setRadius(5 * density);
            mPhotographsViewMore = (TextView) findViewById(R.id.PhotographsViewMore);
            mGridLayoutManager = new GridLayoutManager(HomeActivity.this, 3);
            mPhotographsRecycle = (RecyclerView) findViewById(R.id.PhotographsRecycle);
            mPhotographsRecycle.setLayoutManager(mGridLayoutManager);
            mPhotographsRecycle.setNestedScrollingEnabled(false);
            mPhotoAdapter = new HomePhotoListAdapter(HomeActivity.this, FilePathStrings, albumlist, albumid);
            mPhotographsRecycle.setAdapter(mPhotoAdapter);
            mPhotographsRecycle.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            ((ScrollView) findViewById(R.id.nestedScrollView)).requestDisallowInterceptTouchEvent(true);
                            break;
                        case MotionEvent.ACTION_UP:
                            //Allow ScrollView to intercept touch events once again.
                            ((ScrollView) findViewById(R.id.nestedScrollView)).requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                    v.onTouchEvent(event);
                    return true;
                }
            });
            //ViewCompat.setNestedScrollingEnabled(mPhotographsRecycle, false);
            //mPhotographsRecycle.setNestedScrollingEnabled(false);
            mBtnPaidFees = (View) findViewById(R.id.btnPaidFees);
            mBtnFreeCard = (View) findViewById(R.id.btnFreeCard);
            mBtnOnlinePayment = (View) findViewById(R.id.btnOnlinePayment);
            mBtnPendingFees = (View) findViewById(R.id.btnPendingFees);
            mBtnAllExam = (View) findViewById(R.id.btnAllExam);
            txtAllExam = (TextView) findViewById(R.id.txtAllExam);
            txtAllMarksheetExam = (TextView) findViewById(R.id.txtAllMarkSheet);
            mBtnAllMarkSheet = (View) findViewById(R.id.btnAllMarkSheet);
            mSendQuery = (View) findViewById(R.id.rlSendqueryView);

            mCalendar = (View) findViewById(R.id.rlCalendar);
            mCardAttendance = (CardView) findViewById(R.id.cardviewAttendance);
            mCardHomework = (CardView) findViewById(R.id.cardviewHomework);
            mRlFoodChart = (View) findViewById(R.id.rlFoodChart);
            mRlLibrary = (View) findViewById(R.id.rlLibrary);
            mRlTimeTable = (View) findViewById(R.id.rlTimeTable);
            mRlCurriculum = (View) findViewById(R.id.rlCurriculum);
            rlCurriculumClassroom = (View) findViewById(R.id.rlCurriculumClassroom);
            rlELibrary = (View) findViewById(R.id.rlELibrary);
            // change by tejas patel 17-08-2018
            mRlApplyLeave = (View) findViewById(R.id.rlApplyLeave);
            rlDocuments = findViewById(R.id.rlDocuments);
            rlPracticeTest = findViewById(R.id.rlPracticeTest);
            rlTakeAppo = findViewById(R.id.rlTakeAppo);
            mLLVoiceMsg = findViewById(R.id.llVoiceMsg);
            // change by tejas patel 06-09-2018
            mRlSocialMedia = (View) findViewById(R.id.rlSocialMedia);
            String menuName = mcommon.getSession(Constants.APP_MENU_NAME);
            if (menuName != null && !menuName.isEmpty()) {
                String[] parts = menuName.split("@#mu@#");
                if (parts != null && parts.length > 1)
                    txtAllExam.setText(parts[1]);
                if (parts != null && parts.length > 2)
                    txtAllMarksheetExam.setText(parts[2]);
            }
            setupDrawer();
            setMenu();

            SchoolId = Datastorage.GetSchoolId(HomeActivity.this);
            StudentId = Datastorage.GetStudentId(HomeActivity.this);
            Year_Id = Datastorage.GetCurrentYearId(HomeActivity.this);
            ClassId = Datastorage.GetClassId(HomeActivity.this);
            routeId = String.valueOf(Datastorage.GetRouteId(HomeActivity.this));
            mCurrentYear = Calendar.getInstance().get(Calendar.YEAR);
            mCurrentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
            mCurrentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

            String attendanceDetails = db.GetStudAttendanceCount(StudentId, SchoolId, Year_Id);
            mTxtAttedenceCount.setText(attendanceDetails);
            if (attendanceDetails.equalsIgnoreCase("0/0")) {
                new GetAttendanceDetails().execute();
            }
            mTxtHomeWorkCount.setText(db.GetTodayHomeWorkCount(StudentId, SchoolId, "2,9", Year_Id, mCurrentDay, mCurrentMonth, mCurrentYear));

                              /*  if (mTxtHomeWorkCount.getText() != null && !mTxtHomeWorkCount.getText().toString().isEmpty()
                                        && mTxtHomeWorkCount.getText().equals("0")) {
                                    mTxtHomeWorkCount.setText(tmp.getHomeWork().getCount());
                                }else {
                                    mTxtHomeWorkCount.setText("0");
                                }
        if (tmp.getAttendance() != null) {
            mTxtAttedence.setText(tmp.getAttendance().getTitle());
            if (mTxtAttedenceCount.getText() != null && !mTxtAttedenceCount.getText().toString().isEmpty()
                    && mTxtAttedenceCount.getText().equals("0/0")) {
                mTxtAttedenceCount.setText(tmp.getAttendance().getCount());
            } else {
                mTxtAttedenceCount.setText("0/0");
            }
        } else {
            mTxtAttedenceCount.setText("0/0");
        }*/
            if (SchoolId != null && !SchoolId.isEmpty() && StudentId != null && !StudentId.isEmpty() &&
                    Year_Id != null && !Year_Id.isEmpty() && ClassId != null && !ClassId.isEmpty() &&
                    routeId != null && !routeId.isEmpty())
                GetHomeData(SchoolId, Year_Id, StudentId, ClassId, routeId);
            if (isMessageShow) {
                new GetMessageList().execute();
            } else if (isPhotoGalleryShow) {
                new GetMyPhotoAlbum().execute();
            } else if (isCircularShow) {
                GetCircularForStudentNew_one();
                //new GetNoticeBoard().execute();
            }
            GetBannerDetails();
            getPracticeTestListCount();
            mSocialRecyclerView = (RecyclerView) findViewById(R.id.socialRecyclerView);
            ProgressBar mDialogProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
            mSocialRecyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this, 3));
            mSocialAdapter = new SocialMediaListAdapter(HomeActivity.this,
                    mSocialMediaArrayList);
            mSocialRecyclerView.setAdapter(mSocialAdapter);
            mSocialRecyclerView.setNestedScrollingEnabled(false);

            mReceiverFilter = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, final Intent intent) {
                    getPracticeTestListCount();
                }
            };

            getSocialMediaList();
            getValidateAccount();
        } catch (Exception ex) {
            Constants.writelog("HomeActivity", "Ex:281 :" + ex.getMessage());
        }
    }


    public void GetBannerDetails() {
        try {
            String mStrData = "";
            if (Common.isOnline(HomeActivity.this)) {
                try {
                    if (!getLastVisiSyncHour().equalsIgnoreCase(Constants.getCurrentHour(HomeActivity.this))) {
                        mActivityVisitedList = (ArrayList<ActivityVisitedModel>) db.getActivityVisitedList();
                        ArrayList<String> studlist = new ArrayList<>();
                        for (int i = 0; i < mActivityVisitedList.size(); i++) {
                            if (!studlist.contains(mActivityVisitedList.get(i).getStudiD().toString() + "@" + mActivityVisitedList.get(i).getSchoolId().toString()
                                    + "@" + mActivityVisitedList.get(i).getDateVisited().toString()))
                                studlist.add(mActivityVisitedList.get(i).getStudiD().toString() + "@" + mActivityVisitedList.get(i).getSchoolId().toString()
                                        + "@" + mActivityVisitedList.get(i).getDateVisited().toString());
                        }

                        for (int i = 0; i < studlist.size(); i++) {
                            mStrData += studlist.get(i) + "###";
                            for (int j = 0; j < mActivityVisitedList.size(); j++) {
                                if (studlist.get(i).equalsIgnoreCase(mActivityVisitedList.get(j).getStudiD() + "@" + mActivityVisitedList.get(j).getSchoolId()
                                        + "@" + mActivityVisitedList.get(j).getDateVisited().toString())) {
                                    mStrData += mActivityVisitedList.get(j).getActivityName() + "," +
                                            mActivityVisitedList.get(j).getCount() + "#";
                                }
                            }
                            mStrData += "@@@";
                        }
                        Common.showLog("userVisiDetails", mStrData);
                    }
                    Call<Banner> mCall = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                            .GetBannerList(SchoolId, StudentId, Year_Id, mStrData, BuildConfig.VERSION_CODE + "", Constants.PLATFORM);
                    mCall.enqueue(new retrofit2.Callback<Banner>() {
                        @Override
                        public void onResponse(Call<Banner> call, Response<Banner> response) {
                            Banner tmp = response.body();
                            try {
                                if (tmp.getIsSaveVisitDetails() != null && tmp.getIsSaveVisitDetails().equalsIgnoreCase("1")) {
                                    db.deleteActivityCountTable();
                                    setCurrentTime(HomeActivity.this);
                                }
                                setAdStatus(HomeActivity.this, tmp.getIsShowAd());
                                setAdFrequencyCount(HomeActivity.this, tmp.getAdFrequency());
                                setActivityString(HomeActivity.this, tmp.getShowActivitys());
                                if (tmp != null && tmp.getResponse() != null && tmp.getResponse().equals("1")) {
                                    initSocial(HomeActivity.this, mview, tmp.getBookIssueList().get(0).getImgUrl(), tmp.getBookIssueList().get(0).getRedirectUrl());
                                    //initSocial(HomeActivity.this, mview, "https://s3.ap-south-1.amazonaws.com/espschools/photogallery/6_5_2019_17_25_1_907_636927603088140745.jpg", "");
                                }
                            } catch (Exception ex) {
                                Constants.writelog("GetBannerDetails:534:ErrorMsg::", ex.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<Banner> call, Throwable t) {
                            Constants.writelog("GetBannerDetails:540:ErrorMsg::", "onFailure()");
                        }
                    });

                } catch (Exception ex) {
                    Constants.writelog("GetBannerDetails:545:ErrorMsg::", ex.getMessage());
                }
            }
        } catch (Exception ex) {
            Constants.writelog("GetBannerDetails:549:ErrorMsg::", ex.getMessage());
        }
    }

    public void initListner() {
        mProfileImage.setOnClickListener(this);
        mMessageViewMore.setOnClickListener(this);
        mNoticeViewMore.setOnClickListener(this);
        mPhotographsViewMore.setOnClickListener(this);
        mBtnPaidFees.setOnClickListener(this);
        mBtnFreeCard.setOnClickListener(this);
        mBtnOnlinePayment.setOnClickListener(this);
        mBtnPendingFees.setOnClickListener(this);
        mBtnAllExam.setOnClickListener(this);
        mBtnAllMarkSheet.setOnClickListener(this);
        mLlAttendance.setOnClickListener(this);
        mLlHomeWork.setOnClickListener(this);
        mSendQuery.setOnClickListener(this);
        mCalendar.setOnClickListener(this);
        mRlFoodChart.setOnClickListener(this);
        mRlLibrary.setOnClickListener(this);
        mRlTimeTable.setOnClickListener(this);
        mRlCurriculum.setOnClickListener(this);
        rlCurriculumClassroom.setOnClickListener(this);
        rlELibrary.setOnClickListener(this);
        mRlApplyLeave.setOnClickListener(this);
        rlDocuments.setOnClickListener(this);
        rlPracticeTest.setOnClickListener(this);
        rlTakeAppo.setOnClickListener(this);
        mRlSocialMedia.setOnClickListener(this);
        mLLVoiceMsg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        mDrawerLayout.closeDrawers();
        Intent intent;
        String Feecard = "1";
        String Paid = "2";
        String Pending = "3";
        switch (v.getId()) {
            case R.id.ll_HomeWork:
                intent = new Intent(HomeActivity.this, DailyDiaryCalanderActivity.class);
                intent.putExtra("msgtype", Constants.HW_HOMEWORK);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.ll_Attendance:
                intent = new Intent(HomeActivity.this, StudentAttendanceViewActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.profileImage:
                intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.NoticeViewMore:
                intent = new Intent(HomeActivity.this, NoticeBoardActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.MessageViewMore:
                intent = new Intent(HomeActivity.this, MessagesExpandableListActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.PhotographsViewMore:
                intent = new Intent(HomeActivity.this, PhotoGalleryActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.btnPaidFees:
                intent = new Intent(HomeActivity.this, FeeCardActivity.class);
                intent.putExtra("FeeStatus", Paid);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.btnFreeCard:
                intent = new Intent(HomeActivity.this, FeeCardActivity.class);
                intent.putExtra("FeeStatus", Feecard);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.btnPendingFees:
                intent = new Intent(HomeActivity.this, FeeCardActivity.class);
                intent.putExtra("FeeStatus", Pending);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.btnOnlinePayment:
                String isfullpay = db.getIsfullPay(Datastorage.GetStudentId(HomeActivity.this),
                        Datastorage.GetSchoolId(HomeActivity.this));
                if (isfullpay.equalsIgnoreCase("1")) {
                    intent = new Intent(HomeActivity.this, FeesPayActivity.class);
                } else {
                    intent = new Intent(HomeActivity.this, CustomFeesActivity.class);
                }
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.btnAllExam:
                intent = new Intent(HomeActivity.this, ExamListActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.btnAllMarkSheet:
                intent = new Intent(HomeActivity.this, ExamListMarksheetActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.rlSendqueryView:
                intent = new Intent(HomeActivity.this, ReportBugActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.rlCalendar:
                intent = new Intent(HomeActivity.this, DailyDiaryCalanderActivityNew.class);
                intent.putExtra("msgtype", Constants.CALENDER);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.rlFoodChart:
                intent = new Intent(HomeActivity.this, FoodChartActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.rlLibrary:
                intent = new Intent(HomeActivity.this, LibraryListActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.rlTimeTable:
                intent = new Intent(HomeActivity.this, TimeTableListActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.rlCurriculum:
                intent = new Intent(HomeActivity.this, CurriculumListActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.rlCurriculumClassroom:
                if(mcommon.getSession(Constants.ShowSubNew) != null && mcommon.getSession(Constants.ShowSubNew).equalsIgnoreCase("1")){

                    intent = new Intent(HomeActivity.this, SubmissionFileListDiffActivity.class);
                }else {
                    intent = new Intent(HomeActivity.this, CurriculumClassroomActivity.class);
                }
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.rlELibrary:
                intent = new Intent(HomeActivity.this, ELibraryActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            // change by tejas patel 17-08-2018
            case R.id.rlApplyLeave:
                intent = new Intent(HomeActivity.this, LeaveListActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;
            case R.id.rlDocuments:
                intent = new Intent(HomeActivity.this, DocumentsActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;

            case R.id.rlPracticeTest:
                intent = new Intent(HomeActivity.this, PracticeTestMainSubjectActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;

            case R.id.rlTakeAppo:
                intent = new Intent(HomeActivity.this, TakeAppointmentListActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;

            case R.id.llVoiceMsg:
                intent = new Intent(HomeActivity.this, VoiceMessageListActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                onClickAnimation();
                break;

            // change by tejas patel 06-09-2018
            /*case R.id.rlSocialMedia:
                getSocialMediaList();
                break;*/
            default:
                break;
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT: {
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        Toast.makeText(HomeActivity.this, result.get(0), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }*/

    //setup drawer is method which drawer open and close method for hamburger navigation
    private void setupDrawer() {
        try {
            mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
                //Called when a drawer has settled in a completely open state.
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    //getSupportActionBar().setTitle("Navigation!");
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    hideKeyboard(HomeActivity.this);
                    if (mMenu.hasVisibleItems()) {
                        mMenu.close();
                    }
                }

                // Called when a drawer has settled in a completely closed state.
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    //getSupportActionBar().setTitle(mActivityTitle);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };

            mToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.setDrawerListener(mToggle);
            //mToggle.syncState();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // for hamburger navigation
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        try {
            super.onPostCreate(savedInstanceState);
            // Sync the toggle state after onRestoreInstanceState has occurred.
            mToggle.syncState();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // for navigation
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        try {
            super.onConfigurationChanged(newConfig);
            mToggle.onConfigurationChanged(newConfig);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //    @Override
    //    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    //        return false; }

    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                Datastorage.SetNotificationTry(HomeActivity.this, 0);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                //System.exit(0);
                //super.onBackPressed();
                //return;
            } else {
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            }
            this.doubleBackToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 3000);
        }
    }

    private String getStudentImagePath() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_STUDENT_IMAGE_PATH_V1);
        request.addProperty("school_id", Datastorage.GetSchoolId(HomeActivity.this));
        request.addProperty("Studid", Datastorage.GetStudentId(HomeActivity.this));
        try {
            SoapObject result = Constants.CallWebMethod(
                    HomeActivity.this, request, Constants.GET_STUDENT_IMAGE_PATH_V1, false);
            if (result != null && result.getPropertyCount() > 0) {
                String info = result.getPropertyAsString(0);
                Stud_Image_Path = info;
                Constants.Logwrite("Viewprofile:", "ImagePath:" + Stud_Image_Path);
            } else {
                Constants.Logwrite("ProfileActivity",
                        "Error: getStudentImagePath() No responce from server.");
                Constants
                        .writelog("ProfileActivity",
                                "Error: getStudentImagePath() No responce from server.");
            }
        } catch (Exception err) {
            Constants.writelog("Viewprofile:",
                    "GetStudentImagePath()502 Exception:" + err.getMessage()
                            + "StackTrace::" + err.getStackTrace().toString());
            return Stud_Image_Path;
        }
        return Stud_Image_Path;
    }

    public void GetMessagesFromLocalDatabase() {
        int cntr = 0;
        try {
            List<Contact> contacts = db.GetAllSMSDetails(Integer.parseInt(StudentId),
                    Integer.parseInt(SchoolId), Integer.parseInt(Year_Id), mMessageLimit);
            messages = new String[contacts.size()];
            if (messages != null && messages.length > 0) {
                for (Contact cn : contacts) {
                    String msg = cn.getSMSText();
                    messages[cntr] = msg;
                    cntr++;
                }
            } else {
                Constants.writelog("MessagelistExpandable", "Data Not Found StudId:" + StudentId + " SchoolId:" + SchoolId + " Yearid:" + Year_Id);
                List<Contact> contacts1 = db.GetAllSMSDetails(Integer
                                .parseInt(StudentId), Integer.parseInt(SchoolId),
                        Integer.parseInt(Year_Id), mMessageLimit);
                messages = new String[contacts1.size()];
                if (messages != null && messages.length > 0) {
                    for (Contact cn : contacts1) {
                        String msg = cn.getSMSText();
                        messages[cntr] = msg;
                        cntr++;
                    }
                }
            }
        } catch (Exception ex) {
            Constants
                    .writelog(
                            "MessageListExpandable",
                            "351 Ex:" + ex.getMessage() + "::::::"
                                    + ex.getStackTrace());
        }
    }

    private void GetMessageDetail() {
        try {
            String MsgId = "";
            if (isOnline()) {
                try {
                    MsgId = String.valueOf(Latest_SMS_ID);
                    String yearid = Datastorage.GetCurrentYearId(HomeActivity.this);
                    //mProgressBar.setVisibility(View.VISIBLE);
                    Call<DailyDiaryCalendarModel> mCall = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                            .GetMessageDetail(StudentId, SchoolId, MsgId, yearid, "all", Constants.PLATFORM);
                    mCall.enqueue(new retrofit2.Callback<DailyDiaryCalendarModel>() {
                        @Override
                        public void onResponse(Call<DailyDiaryCalendarModel> call, Response<DailyDiaryCalendarModel> response) {
                            DailyDiaryCalendarModel tmp = response.body();
                            try {
                                if (tmp != null && tmp.strlist.size() > 0) {
                                    for (int i = 0; i < tmp.strlist.size(); i++) {

                                        int ModId = Integer.parseInt(tmp.strlist.get(i).fifth);
                                        int SMS_DAY = Integer.parseInt(tmp.strlist.get(i).first);
                                        int SMS_MONTH = Integer.parseInt(tmp.strlist.get(i).second);
                                        int SMS_YEAR = Integer.parseInt(tmp.strlist.get(i).third);
                                        int SMS_MSG_ID = Integer.parseInt(tmp.strlist.get(i).sixth);
                                        Boolean Ismessgaeinsert = false;
                                        if (ModId == 5) {
                                            Ismessgaeinsert = db
                                                    .CheckAbsentMessageInsertorNot(
                                                            Integer.parseInt(StudentId),
                                                            Integer.parseInt(SchoolId),
                                                            SMS_DAY, SMS_MONTH, SMS_YEAR,
                                                            ModId);
                                        } else {
                                            Ismessgaeinsert = db.CheckMessageInsertorNot(
                                                    Integer.parseInt(StudentId),
                                                    Integer.parseInt(SchoolId), SMS_MSG_ID);
                                        }
                                        String msg = "";
                                        if (ModId == 0) {
                                            msg = tmp.strlist.get(i).eighth + "##,@@" + tmp.strlist.get(i).nineth;
                                        } else {
                                            msg = tmp.strlist.get(i).eighth;
                                        }
                                        if (Ismessgaeinsert) {
                                            db.Updatesms(new Contact(
                                                    Integer.parseInt(tmp.strlist.get(i).sixth),
                                                    msg,
                                                    Integer.parseInt(StudentId),
                                                    Integer.parseInt(SchoolId),
                                                    Integer.parseInt(tmp.strlist.get(i).first),
                                                    Integer.parseInt(tmp.strlist.get(i).second),
                                                    Integer.parseInt(tmp.strlist.get(i).third),
                                                    Integer.parseInt(tmp.strlist.get(i).fifth),
                                                    Integer.parseInt(tmp.strlist.get(i).seventh)));
                                            // Constants.writelog("DailyDiary",
                                            // "SmsUpdate()949 NO"+i+" Messageid:"+msgitem[4]);
                                        } else {
                                            db.AddSMS(new Contact(
                                                    Integer.parseInt(tmp.strlist.get(i).sixth),
                                                    msg,
                                                    Integer.parseInt(StudentId),
                                                    Integer.parseInt(SchoolId),
                                                    Integer.parseInt(tmp.strlist.get(i).first),
                                                    Integer.parseInt(tmp.strlist.get(i).second),
                                                    Integer.parseInt(tmp.strlist.get(i).third),
                                                    Integer.parseInt(tmp.strlist.get(i).fifth),
                                                    Integer.parseInt(tmp.strlist.get(i).seventh)));
                                        }
                                    }
                                    mTxtHomeWorkCount.setText(db.GetTodayHomeWorkCount(StudentId, SchoolId, "2,9", Year_Id, mCurrentDay, mCurrentMonth, mCurrentYear));
                                    int cntr = 0;
                                    List<Contact> contacts = db
                                            .GetAllSMSDetails(
                                                    Integer.parseInt(StudentId),
                                                    Integer.parseInt(SchoolId),
                                                    Integer.parseInt(Year_Id), mMessageLimit);
                                    messages = new String[contacts.size()];
                                    if (messages != null && messages.length > 0) {
                                        for (Contact cn : contacts) {
                                            String msg = cn.getSMSText();
                                            messages[cntr] = msg;
                                            cntr++;
                                        }
                                    } else {
                                        Constants.writelog("MessageListExpandable", "319 No messgae found");
                                    }
                                    if (messages != null && messages.length > 0) {
                                        if (messages.length > 5) {
                                            String[] tmpMessages;
                                            tmpMessages = new String[5];
                                            for (int j = 0; j < 5; j++) {
                                                tmpMessages[j] = messages[j];
                                            }
                                            // mMessageAdapter = new HomeMessageAdapter(HomeActivity.this, tmpMessages);
                                            // mMessageViewPager.setAdapter(mMessageAdapter);
                                            mMessageAdapter = new HomeMessageAdapter(tmpMessages, HomeActivity.this);
                                            mMessageViewPager.setAdapter(mMessageAdapter);


                                            mMessageAdapter.notifyDataSetChanged();
                                        } else {
                                            // mMessageAdapter = new HomeMessageAdapter(HomeActivity.this, messages);
                                            //   mMessageViewPager.setAdapter(mMessageAdapter);
                                            mMessageAdapter = new HomeMessageAdapter(messages, HomeActivity.this);
                                            mMessageViewPager.setAdapter(mMessageAdapter);


                                            mMessageAdapter.notifyDataSetChanged();
                                        }
                                        ((View) findViewById(R.id.rlMessageView)).setVisibility(View.VISIBLE);
                                    } else {
                                        ((View) findViewById(R.id.rlMessageView)).setVisibility(View.GONE);
                                    }
                                }
                            } catch (Exception ex) {
                                Constants.writelog("GetMessageDetail:1459:ErrorMsg::", ex.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<DailyDiaryCalendarModel> call, Throwable t) {
                            // mProgressBar.setVisibility(View.GONE);
                        }
                    });
                } catch (Exception ex) {
                    Constants.writelog("GetMessageDetail:1366:ErrorMsg::", ex.getMessage());
                }
            }
        } catch (Exception ex) {
            Constants.writelog("GetMessageDetail:1370:ErrorMsg::", ex.getMessage());
        }
    }

    public void GetCircularForStudentNew_one() {
        try {
            boolean isDataExist = false;
            isDataExist = DisplayNoticedata();
            if (!isDataExist) {
                try {
                    if (isOnline()) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        Call<CircularModel> call = ((MyApplication) getApplicationContext())
                                .getmRetrofitInterfaceAppService().GetCircularDetail(Integer.parseInt(SchoolId),
                                        Integer.parseInt(StudentId),
                                        Integer.parseInt(Year_Id), Constants.PLATFORM);
                        call.enqueue(new Callback<CircularModel>() {
                            @Override
                            public void onResponse(Call<CircularModel> call, Response<CircularModel> response) {
                                CircularModel tmps = response.body();
                                if (tmps != null) {
                                    mCircularlist.clear();
                                    db.DeleteStudentCircular(Integer.parseInt(StudentId), Integer.parseInt(SchoolId));
                                    if (tmps.getStrlist() != null && tmps.getStrlist().size() > 0) {
                                        mCircularlist.addAll(tmps.getStrlist());
                                        int count;
                                        count = mCircularlist.size();
                                        for (int i = 0; i < count; i++) {

                                            // Check it is exist in db or not
                                            Boolean isinserted = db.CheckCircularInserted(
                                                    Integer.parseInt(StudentId),
                                                    Integer.parseInt(SchoolId),
                                                    Integer.parseInt(mCircularlist.get(i).getFirst()));

                                            if (!isinserted) {
                                                int res = db.AddCircular(new Contact(Integer
                                                        .parseInt(StudentId), Integer
                                                        .parseInt(SchoolId), Integer
                                                        .parseInt(Year_Id), Integer
                                                        .parseInt(mCircularlist.get(i).getFourth()), mCircularlist.get(i).getThird(),
                                                        Integer.parseInt(mCircularlist.get(i).getFirst()),
                                                        mCircularlist.get(i).getSecond(), mCircularlist.get(i).getEighth(),
                                                        mCircularlist.get(i).getFifth(),
                                                        mCircularlist.get(i).getSixth(), Integer
                                                        .parseInt(mCircularlist.get(i).getSeventh())));
                                                if (res != 1) {
                                                    Constants.Logwrite("HomeActivity", "1445 AddCircular fail.");
                                                    Constants.writelog("HomeActivity", "1446 AddCircular fail.");
                                                }
                                            } else {
                                                int res = db.UpdateCircular(new Contact(Integer
                                                        .parseInt(StudentId), Integer
                                                        .parseInt(SchoolId), Integer
                                                        .parseInt(Year_Id), Integer
                                                        .parseInt(mCircularlist.get(i).getFourth()), mCircularlist.get(i).getThird(),
                                                        Integer.parseInt(mCircularlist.get(i).getFirst()),
                                                        mCircularlist.get(i).getSecond(), mCircularlist.get(i).getEighth(),
                                                        mCircularlist.get(i).getFifth(),
                                                        mCircularlist.get(i).getSixth(), Integer
                                                        .parseInt(mCircularlist.get(i).getSeventh())));
                                                if (res != 1) {
                                                    Constants.Logwrite("HomeActivity", "1460 UpdateCircular fail.");
                                                    Constants.writelog("HomeActivity", "1461 UpdateCircular fail.");
                                                }
                                            }
                                        }
                                        groupforsddl = new HashSet<>(Arrays.asList(Itmgroup)).toArray(new String[0]);
                                    }
                                    if (mProgressBar.getVisibility() == View.VISIBLE)
                                        mProgressBar.setVisibility(View.GONE);
                                }
                                DisplayNoticedata();
                            }

                            @Override
                            public void onFailure(Call<CircularModel> call, Throwable t) {
                                Constants.writelog("HomeActivity", "Exception_1474:" + t.getMessage() + "::::::" + t.getStackTrace());
                                if (mProgressBar.getVisibility() == View.VISIBLE)
                                    mProgressBar.setVisibility(View.GONE);
                            }

                        });
                    } else {
                        Common.showToast(HomeActivity.this, getString(R.string.msg_connection));
                    }
                } catch (Exception e) {
                    Constants.writelog("HomeActivity", "Exception_1481:" + e.getMessage() + "::::::" + e.getStackTrace());
                    if (mProgressBar.getVisibility() == View.VISIBLE)
                        mProgressBar.setVisibility(View.GONE);
                }
            }

        } catch (Exception e) {
            Constants.writelog("HomeActivity", "Exception_1491:" + e.getMessage() + "::::::" + e.getStackTrace());
            if (mProgressBar.getVisibility() == View.VISIBLE)
                mProgressBar.setVisibility(View.GONE);
        }
    }

    public boolean DisplayNoticedata() {
        boolean isDataExist = false;
        List<Contact> contacts = db.GetAllCircularDetails(Integer.parseInt(StudentId),
                Integer.parseInt(SchoolId), Integer.parseInt(Year_Id), mNoticeLimit);
        if (contacts.size() > 0) {
            isDataExist = true;
            CircularName = new String[contacts.size()];
            Itmgroup = new String[contacts.size()];
            ItmDate = new String[contacts.size()];
            Itmpath = new String[contacts.size()];
            doc = new String[contacts.size()];
            int i = 0;
            for (Contact cn : contacts) {
                CircularName[i] = cn.getCirName();
                Itmgroup[i] = cn.getcirGroupname();
                ItmDate[i] = cn.getcirDateText();
                Itmpath[i] = cn.getcirPath();
                doc[i] = cn.getcirTicks();
                i++;
            }
            groupforsddl = new HashSet<String>(Arrays.asList(Itmgroup)).toArray(new String[0]);

            if (CircularName != null && CircularName.length > 0 && CircularName[0] != "") {
                if (groupforsddl != null && groupforsddl.length > 1) {
                    mNoticeBoardAdapter = new HomeNoticeBoardAdapter(HomeActivity.this,
                            CircularName, ItmDate, Itmgroup);
                    mNoticeBoardViewPager.setAdapter(mNoticeBoardAdapter);
                    mNoticeBoardAdapter.notifyDataSetChanged();
                } else {
                    if (CircularName != null && CircularName.length > 0) {
                        mNoticeBoardAdapter = new HomeNoticeBoardAdapter(HomeActivity.this,
                                CircularName, ItmDate, Itmgroup);
                        mNoticeBoardViewPager.setAdapter(mNoticeBoardAdapter);
                        mNoticeBoardAdapter.notifyDataSetChanged();
                    }
                }
                ((View) findViewById(R.id.rlNoticeBoardView)).setVisibility(View.VISIBLE);
            } else {
                ((View) findViewById(R.id.rlNoticeBoardView)).setVisibility(View.GONE);
            }
        }
        return isDataExist;
    }

    public void GetHomeData(String schoolid, String yearid, String studentId, String classId, String routeId) {
        try {
            if (isOnline()) {
                //mProgressBar.setVisibility(View.VISIBLE);
                Call<HomeModel> mCall = ((MyApplication) getApplicationContext()).getRetroFitInterface()
                        .GetHomeDetail(schoolid, yearid, studentId, classId, routeId);
                mCall.enqueue(new retrofit2.Callback<HomeModel>() {
                    @Override
                    public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                        try {
                            HomeModel tmp = response.body();
                            if (tmp != null && tmp.getResultflag() != null && !tmp.getResultflag().isEmpty()
                                    && tmp.getResultflag().equalsIgnoreCase("1")) {
                                if (tmp.getHomeList() != null && tmp.getHomeList().size() > 0) {
                                    mRecycleView.setVisibility(View.VISIBLE);
                                    mHomeArrayList.clear();
                                    mHomeArrayList.addAll(tmp.getHomeList());
                                    mHomeDataAdapter.notifyDataSetChanged();
                                } else {
                                    mRecycleView.setVisibility(View.GONE);
                                }
                            } else {
                                if (tmp != null && !tmp.getResultflag().isEmpty() && tmp.getResultflag().equals("4")) {
                                    Common.showToast(HomeActivity.this, tmp.getMessage());
                                    mDrawerLayout.setVisibility(View.GONE);
                                    mMenu.clear();
                                    mMenu.notifyAll();
                                    return;
                                }
                                if (tmp != null && tmp.getMessage() != null && !tmp.getMessage().isEmpty())
                                    Common.showToast(HomeActivity.this, tmp.getMessage());
                            }
                        } catch (Exception ex) {
                            Common.printStackTrace(ex);
                        }
                        //mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<HomeModel> call, Throwable t) {
                        // mProgressBar.setVisibility(View.GONE);
                    }
                });

                if (isOnline())
                    new GetProfile().execute();
            } else {
                // Common.showToast(HomeActivity.this, "No Interenet Connection.");
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    private void getMenuList() {
        String s = "";
        try {
            s = db.getMenu(Datastorage.GetStudentId(HomeActivity.this), Datastorage.GetSchoolId(HomeActivity.this));
            if (s != null && s != "") {
                groupList = new ArrayList<String>(Arrays.asList(s.split(",")));
                laptopCollection = new LinkedHashMap<String, List<String>>();

                groupList.remove("My Profile");
                groupList.remove("Setting");
                groupList.remove("Exit");

                if (groupList.contains("Attendance")) {
                    mCardAttendance.setVisibility(View.VISIBLE);
                } else {
                    mCardAttendance.setVisibility(View.GONE);
                }
                boolean isHomeworkAvailable = false;
                for (int j = 0; j < groupList.size(); j++) {
                    childList = null;
                    String Group = groupList.get(j);
                    String[] parts = Group.split("@");

                    if (parts != null && parts.length > 1) {
                        if (parts[0].equalsIgnoreCase("exam")) {
                            ((View) findViewById(R.id.llExamView)).setVisibility(View.VISIBLE);
                        } else if (parts[0].equalsIgnoreCase("fee card")) {
                            ((View) findViewById(R.id.llFeesView)).setVisibility(View.VISIBLE);
                        }
                        String[] child = new String[parts.length - 1];
                        groupList.set(j, parts[0]);
                        for (int i = 1; i < parts.length; i++) {
                            child[i - 1] = parts[i];
                            if (parts[i].equalsIgnoreCase("Homework"))
                                isHomeworkAvailable = true;

                        }
                        loadChild(child);
                        laptopCollection.put(parts[0], childList);
                    } else {
                        // mRLFeeCard,mRLPaidFees,mRLOnlinePayment,mRLPendingFees
                        if (parts[0].equalsIgnoreCase("pay fees online")) {
                            mBtnOnlinePayment.setVisibility(View.VISIBLE);
                            mRLOnlinePayment.setVisibility(View.VISIBLE);
                        } else if (parts[0].equalsIgnoreCase("calendar")) {
                            mCalendar.setVisibility(View.VISIBLE);
                        } else if (parts[0].equalsIgnoreCase("food chart")) {
                            mRlFoodChart.setVisibility(View.VISIBLE);
                        } else if (parts[0].equalsIgnoreCase("library")) {
                            mRlLibrary.setVisibility(View.VISIBLE);
                        } else if (parts[0].equalsIgnoreCase("time table")) {
                            mRlTimeTable.setVisibility(View.VISIBLE);
                        } else if (parts[0].equalsIgnoreCase("curriculum")) {
                            mRlCurriculum.setVisibility(View.VISIBLE);
                        } else if (parts[0].equalsIgnoreCase("Submission")) {
                            rlCurriculumClassroom.setVisibility(View.VISIBLE);
                        } else if (parts[0].equalsIgnoreCase("E-Library")) {
                            rlELibrary.setVisibility(View.VISIBLE);
                        } else if (parts[0].equalsIgnoreCase("Send Query")) {
                            mSendQuery.setVisibility(View.VISIBLE);
                        } else if (parts[0].equalsIgnoreCase("Apply Leave")) {
                            mRlApplyLeave.setVisibility(View.VISIBLE);
                        } else if (parts[0].equalsIgnoreCase("Documents")) {
                            rlDocuments.setVisibility(View.VISIBLE);
                        } else if (parts[0].equalsIgnoreCase("Practice Test")) {
                            rlPracticeTest.setVisibility(View.VISIBLE);
                        } else if (parts[0].equalsIgnoreCase("Take Appointment")) {
                            rlTakeAppo.setVisibility(View.VISIBLE);
                        } else if (parts[0].equalsIgnoreCase("Voice Message")) {
                            mLLVoiceMsg.setVisibility(View.VISIBLE);
                        } else if (parts[0].equalsIgnoreCase("notice board")) {
                            isCircularShow = true;
                        } else if (parts[0].equalsIgnoreCase("photo gallery")) {
                            isPhotoGalleryShow = true;
                        } else if (parts[0].equalsIgnoreCase("messages")) {
                            isMessageShow = true;
                        }
                    }
                }
                if (isHomeworkAvailable) {
                    mCardHomework.setVisibility(View.VISIBLE);
                } else {
                    mCardHomework.setVisibility(View.GONE);
                }
            } else {
                Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        } catch (Exception ex) {
            Constants.writelog("HomeActivity", "createGroupList()577 Menu=" + s + "\nMSG:" + ex.getMessage());
        }
    }

    private void loadChild(String[] laptopModels) {
        childList = new ArrayList<String>();
        for (String model : laptopModels) {
            childList.add(model);
        }
    }

    public void sendLogDetail() {
        try {
            int isLogSent = Datastorage.GetLogSendComplete(getApplicationContext());

            if (isLogSent != 1) {
                String deviceId = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                String DeviceDetails = "Details: Mobile:"
                        + Datastorage.GetPhoneNumber(getApplicationContext())
                        + "||| Name:"
                        + Datastorage.GetStudentName(getApplicationContext())
                        + "|||"
                        + Datastorage.GetFcmRegId(getApplicationContext())
                        + "||||" + Build.DEVICE + "|||" + Build.MODEL + "|||"
                        + Build.PRODUCT + "|||" + Build.VERSION.SDK + "|||"
                        + Build.VERSION.RELEASE;
                Constants.MyTaskSendLog SendLog = new Constants.MyTaskSendLog();
                // SendLog.execute(getApplicationContext(), DeviceDetails, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    SendLog.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                            getApplicationContext(), DeviceDetails, deviceId,
                            isLogSent);
                    // mytaskRef.execute();
                } else {
                    SendLog.execute(getApplicationContext(), DeviceDetails, isLogSent);
                }
            }
        } catch (Exception ex) {
            Constants.writelog("DashboardActivity",
                    "SendLogToServer 411:" + ex.getMessage());
        }
    }

    private void getValidateAccount() {
        if (isOnline()) {
            Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                    .getValidateAccount(StudentId, SchoolId, Year_Id, SchoolDetails.appname + "", Constants.CODEVERSION, Constants.PLATFORM);
            call.enqueue(new Callback<AppService>() {
                @Override
                public void onResponse(Call<AppService> call, Response<AppService> response) {
                    try {
                        AppService tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1") && !tmps.getStrResult().isEmpty()) {
                            Time Today = new Time();
                            Today.setToNow();
                            //mcommon.setSession(Constants.LastAppControlCall, Today.monthDay + "2");
                            if (!mcommon.getSession(Constants.LastAppControlCall).equals(Today.monthDay + "")) {
                                if (tmps.getStrResult().contains("1,")) {
                                    getAccount();
                                }
                                if (tmps.getStrResult().contains("2,")) {
                                    new MyTaskGetAllYear().execute();
                                }
                                mcommon.setSession(Constants.LastAppControlCall, Today.monthDay + "");
                            } else {
                                Constants.writelog("HomeActivity", "getValidateAccount 1615: call for today.");
                            }
                        }
                    } catch (Exception ex) {
                        Constants.writelog("HomeActivity", "getValidateAccount 1620:" + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<AppService> call, Throwable t) {
                    Constants.writelog("HomeActivity", "getValidateAccount 1626:" + t.getMessage());
                }
            });
        }
    }

    private void getAccount() {
        try {
            String mDeviceDetail = "";
            try {
                mDeviceDetail = Build.DEVICE + "|||" + Build.MODEL + "|||" + Build.ID
                        + "|||" + Build.PRODUCT + "|||" + Build.VERSION.SDK
                        + "|||" + Build.VERSION.RELEASE + "|||" + Build.VERSION.INCREMENTAL;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (isOnline()) {
                Call<AppService> mCall = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                        .GetAccountDetail(mobileno, StudentId, SchoolId, mDeviceDetail, Constants.PLATFORM, Constants.CODEVERSION);
                mCall.enqueue(new retrofit2.Callback<AppService>() {
                    @Override
                    public void onResponse(Call<AppService> call, Response<AppService> response) {
                        try {
                            AppService tmp = response.body();
                            if (tmp != null && tmp.getStrResult() != null && !tmp.getStrResult().isEmpty()
                                    && tmp.getResponse().equalsIgnoreCase("1")) {
                                if (tmp.getListArray().size() > 0) {
                                    db.DeleteAllContact();
                                    ArrayList<String> ItmNames = new ArrayList<>();
                                    for (int i = 0; i < tmp.getListArray().size(); i++) {
                                        AppService.ListArray tmp1 = tmp.getListArray().get(i);
                                        ItmNames.add(tmp1.getFifth());
                                        if (tmp1.getSecond() == null || tmp1.getSecond().equalsIgnoreCase("")) {
                                            tmp1.setSecond("0");
                                        } else if (tmp1.getThird() == null || tmp1.getThird().equalsIgnoreCase("")) {
                                            tmp1.setThird("0");
                                        } else if (tmp1.getSixth() == null || tmp1.getSixth().equalsIgnoreCase("")) {
                                            tmp1.setSixth("0");
                                        } else if (tmp1.getSeventh() == null || tmp1.getSeventh().equalsIgnoreCase("")) {
                                            tmp1.setSeventh("0");
                                        } else if (tmp1.getEighth() == null || tmp1.getEighth().equalsIgnoreCase("")) {
                                            tmp1.setEighth("0");
                                        } else if (tmp1.getNineteen() == null || tmp1.getNineteen().equalsIgnoreCase("")) {
                                            tmp1.setNineteen("0");
                                        } else if (tmp1.getSeventeen() == null || tmp1.getSeventeen().equalsIgnoreCase("")) {
                                            tmp1.setSeventeen("0");
                                        }
                                        int Contact_Status = db.addContact(new Contact(
                                                tmp1.getFifth(), mobileno, 0, 0, Integer.parseInt(tmp1.getSecond()), Integer.parseInt(tmp1.getThird()),
                                                Integer.parseInt(tmp1.getSixth()), Integer.parseInt(tmp1.getSeventh()),
                                                Integer.parseInt(tmp1.getEighth()), Integer.parseInt(tmp1.getNineteen()), tmp1.getNineth(),
                                                tmp1.getEleventh(), tmp1.getSixteen(), tmp1.getTenth(), tmp1.getTwenty(),
                                                Integer.parseInt(tmp1.getSeventeen()), tmp1.getEighteen()));
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            Constants.writelog(tag, "GetDetails():onResponse:1683:" + ex.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<AppService> call, Throwable t) {
                        Constants.writelog(tag, "GetDetails():onFailure:1684.");
                    }
                });
            }
        } catch (Exception ex) {
            Constants.writelog(tag, "getAccount:369:" + ex.getMessage());
        }
    }

    public String[] GetAllAcademicYear() {
        String[] years = null;
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_ALL_ACADEMIC_YEAR);
        request.addProperty("schoolid", SchoolId);
        request.addProperty("studid", StudentId);
        try {
            SoapObject result = Constants.CallWebMethod(
                    HomeActivity.this, request, Constants.GET_ALL_ACADEMIC_YEAR, true);
            if (result != null && result.getPropertyCount() > 0) {
                SoapObject obj2 = (SoapObject) result.getProperty(0);
                String[] output = null;
                if (obj2 != null) {
                    // int count= 20; //obj2.getPropertyCount();
                    int count = obj2.getPropertyCount();
                    output = new String[count];
                    years = new String[count];
                    int[] yearsid = new int[count];
                    String[] myarray = new String[count];
                    if (count > 0) {
                        db.DeleteStudentYear(Integer.parseInt(StudentId), Integer.parseInt(SchoolId));
                    }
                    for (int i = 0; i < count; i++) {
                        myarray[i] = obj2.getProperty(i).toString();
                        String[] parts = myarray[i].split("##,@@");
                        output[i] = parts[0].toString();
                        years[i] = parts[0].toString();
                        yearsid[i] = Integer.parseInt(parts[1].toString());
                        //Check Year Id is Insert Or Not
                        Boolean IsYearIdInsert = true;
                        IsYearIdInsert = db.CheckAcademicYear(
                                Integer.parseInt(StudentId),
                                Integer.parseInt(SchoolId),
                                Integer.parseInt(parts[1].toString()));
                        if (!IsYearIdInsert) {
                            int Iscurrent = Integer.parseInt(parts[2]
                                    .toString());
                            if (Iscurrent == 1) {
                                db.AddYear(new Contact(Integer
                                        .parseInt(parts[1].toString()),
                                        parts[0].toString(), Integer
                                        .parseInt(parts[2].toString()),
                                        Integer.parseInt(SchoolId), Integer
                                        .parseInt(StudentId), 1));
                            } else {
                                db.AddYear(new Contact(Integer
                                        .parseInt(parts[1].toString()),
                                        parts[0].toString(), Integer
                                        .parseInt(parts[2].toString()),
                                        Integer.parseInt(SchoolId), Integer
                                        .parseInt(StudentId), 0));
                            }
                        }
                    }
                    // years = output;
                }
            }
        } catch (Exception err) {
            // e.printStackTrace();
            Constants.Logwrite("Exams:", "Exception:" + err.getMessage() + "StackTrace::"
                    + err.getStackTrace().toString());

        }
        return years;
    }

    private void getSocialMediaList() {
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            Call<SocialMediaListModel> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                    .GetSocialMedia(SchoolId, BuildConfig.VERSION_CODE + "", Constants.PLATFORM);
            call.enqueue(new Callback<SocialMediaListModel>() {
                @Override
                public void onResponse(Call<SocialMediaListModel> call, Response<SocialMediaListModel> response) {
                    try {
                        SocialMediaListModel tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1")) {
                            if (tmps.getSocialMediaList() != null && tmps.getSocialMediaList().size() > 0) {
                                mSocialMediaArrayList.clear();
                                mSocialMediaArrayList.addAll(tmps.getSocialMediaList());
                                mSocialAdapter.notifyDataSetChanged();
                            }
                        } else {
                            if (tmps != null && tmps.getMessage() != null && !tmps.getMessage().isEmpty()) {
                                //Common.showToast(HomeActivity.this, tmps.getMessage());
                            }
                        }
                        if (mSocialMediaArrayList != null && mSocialMediaArrayList.size() > 0) {
                            if (mSocialMediaArrayList.size() == 2) {
                                mSocialRecyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this,
                                        2));
                            } else if (mSocialMediaArrayList.size() == 3) {
                                mSocialRecyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this,
                                        3));
                            } else if (mSocialMediaArrayList.size() == 4) {
                                mSocialRecyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this,
                                        4));
                            } else {
                                mSocialRecyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this,
                                        3));
                            }
                            if (mSocialAdapter != null)
                                mSocialAdapter.notifyDataSetChanged();
                            mRlSocialMedia.setVisibility(View.VISIBLE);
                        } else {
                            mRlSocialMedia.setVisibility(View.GONE);
                        }
                        mProgressBar.setVisibility(View.GONE);
                    } catch (Exception ex) {
                        mProgressBar.setVisibility(View.GONE);
                        Constants.writelog("HomeActivity", "getSocialMediaList 1996:" + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<SocialMediaListModel> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);

                    Constants.writelog("HomeActivity", "getSocialMediaList 2004:" + t.getMessage());
                }
            });
        } else {
            //Common.showToast(HomeActivity.this, getString(R.string.msg_connection));
        }
    }

    private void getPracticeTestListCount() {
        if (isOnline()) {
            //    prg_test.setVisibility(View.VISIBLE);
            String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
            String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
            Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                    .GET_PRACTICE_PENDING_TEST_COUNT(mStudentId, mSchoolId, mYearId, SchoolDetails.appname + "", Constants.CODEVERSION, Constants.PLATFORM);
            call.enqueue(new Callback<AppService>() {
                @Override
                public void onResponse(Call<AppService> call, Response<AppService> response) {
                    try {
                        //     prg_test.setVisibility(View.GONE);
                        AppService tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1") && !tmps.getStrResult().isEmpty()) {
                          /*  Time Today = new Time();
                            Today.setToNow();*/
                            txtPracticeTestCount.setText(tmps.getStrResult());
                            //mcommon.setSession(Constants.LastAppControlCall, Today.monthDay + "2");
                        }
                    } catch (Exception ex) {
                        //  prg_test.setVisibility(View.GONE);
                        Constants.writelog("BtmNavigationActivity", "getPracticetestLiat 107:" + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<AppService> call, Throwable t) {
                    // prg_test.setVisibility(View.GONE);

                    Constants.writelog("BtmNavigationActivity", "getPracticetestLiat 113:" + t.getMessage());
                }
            });
        }
    }

    private void SocialMediaAlert() {
        try {
            if (mSocialMediaArrayList != null && !mSocialMediaArrayList.isEmpty() && mSocialMediaArrayList.size() > 0) {
                LayoutInflater viewInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View promptView = viewInflater.inflate(R.layout.social_media_dialog_layout, null);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                alertDialog.setCancelable(false);
                alertDialog.setView(promptView);
                RecyclerView mRecyclerView = (RecyclerView) promptView.findViewById(R.id.recyclerView);
                ProgressBar mDialogProgressBar = (ProgressBar) promptView.findViewById(R.id.ProgressBar);
                mRecyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this, 3));
                SocialMediaListAdapter mAdapter = new SocialMediaListAdapter(HomeActivity.this,
                        mSocialMediaArrayList);
                mRecyclerView.setAdapter(mAdapter);

                ImageView mImgCancel = (ImageView) promptView.findViewById(R.id.btnCancel);
                mImgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        socialMediaAlert.dismiss();
                    }
                });

                alertDialog.setCancelable(false);
                socialMediaAlert = alertDialog.create();
                socialMediaAlert.setCanceledOnTouchOutside(true);
                socialMediaAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                socialMediaAlert.show();
            }

        } catch (NullPointerException ex) {
            Common.printStackTrace(ex);
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    private void setMenu() {
        try {
            getMenuList();
            mMainMenuList = (ExpandableListView) findViewById(R.id.slideMenuList);
            mMenuAdapter = new HomeMenuExpandableListAdapter(this, groupList, laptopCollection);
            mHeaderView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.nav_header_main, null, true);
            mMainMenuList.addHeaderView(mHeaderView);
            mMainMenuList.setAdapter(mMenuAdapter);
            mProfileImage = (ImageView) mHeaderView.findViewById(R.id.profileImage);

            mMainMenuList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                public void onGroupCollapse(int groupPosition) {
                    Datastorage.SetLastOpenedGroup(HomeActivity.this, "");
                }
            });
            mMainMenuList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    try {

                        String groupname = (String) mMenuAdapter.getGroup(groupPosition);
                        Datastorage.SetLastOpenedGroup(HomeActivity.this, groupname);
                        if (groupname.equalsIgnoreCase("My Profile")) {
                            mDrawerLayout.closeDrawers();
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "My Profile");
                            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (groupname.equalsIgnoreCase("Attendance")) {
                            mDrawerLayout.closeDrawers();
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "Attendance");
                            Intent intent = new Intent(HomeActivity.this, StudentAttendanceViewActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (groupname.equalsIgnoreCase("Notice Board")) {
                            mDrawerLayout.closeDrawers();
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "Circular");
                            //Jaydeep Call service for noticeboard
                            if (((View) findViewById(R.id.rlNoticeBoardView)).getVisibility() == View.GONE) {
                                Common.setToastMessage(HomeActivity.this, findViewById(R.id.drawer_layout), "Notices are not Available.");
                                //Toast.makeText(getApplicationContext(), "Notices are not Availble.", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(HomeActivity.this, NoticeBoardActivity.class);
                                intent.putExtra("IsFromHome", "IsFromHome");
                                startActivity(intent);
                                onClickAnimation();
                            }
                        } else if (groupname.equalsIgnoreCase("Messages")) {
                            mDrawerLayout.closeDrawers();
                            //Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "Messages");
                            if (((View) findViewById(R.id.rlMessageView)).getVisibility() == View.GONE) {
                                Common.setToastMessage(HomeActivity.this, findViewById(R.id.drawer_layout), "Messages are not Availble.");
                                //Toast.makeText(getApplicationContext(), "Messages are not Availble.", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(HomeActivity.this, MessagesExpandableListActivity.class);
                                intent.putExtra("IsFromHome", "IsFromHome");
                                startActivity(intent);
                                onClickAnimation();
                            }
                        } else if (groupname.equalsIgnoreCase("Pay Fees Online")) {
                            mDrawerLayout.closeDrawers();
                            Intent intent;
                            String isfullpay = db.getIsfullPay(Datastorage.GetStudentId(HomeActivity.this), Datastorage.GetSchoolId(HomeActivity.this));
                            if (isfullpay.equalsIgnoreCase("1")) {
                                intent = new Intent(HomeActivity.this, FeesPayActivity.class);
                            } else {
                                intent = new Intent(HomeActivity.this, CustomFeesActivity.class);
                            }
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (groupname.equalsIgnoreCase("calendar")) {
                            mDrawerLayout.closeDrawers();
                            Intent intent = new Intent(HomeActivity.this, DailyDiaryCalanderActivityNew.class);
                            intent.putExtra("msgtype", Constants.CALENDER);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (groupname.equalsIgnoreCase("food chart")) {
                            mDrawerLayout.closeDrawers();
                            Intent intent = new Intent(HomeActivity.this, FoodChartActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (groupname.equalsIgnoreCase("library")) {
                            mDrawerLayout.closeDrawers();
                            Intent intent = new Intent(HomeActivity.this, LibraryListActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (groupname.equalsIgnoreCase("time table")) {
                            mDrawerLayout.closeDrawers();
                            Intent intent = new Intent(HomeActivity.this, TimeTableListActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (groupname.equalsIgnoreCase("curriculum")) {
                            mDrawerLayout.closeDrawers();
                            Intent intent = new Intent(HomeActivity.this, CurriculumListActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (groupname.equalsIgnoreCase("Submission")) {
                            mDrawerLayout.closeDrawers();
                            Intent intent=null;
                            if(mcommon.getSession(Constants.ShowSubNew) != null && mcommon.getSession(Constants.ShowSubNew).equalsIgnoreCase("1")){
                                intent = new Intent(HomeActivity.this, SubmissionFileListDiffActivity.class);
                            }else {
                                intent = new Intent(HomeActivity.this, CurriculumClassroomActivity.class);
                            }
                        //      Intent intent = new Intent(HomeActivity.this, CurriculumClassroomActivity.class);
                        //  Intent intent = new Intent(HomeActivity.this, SubmissionFileListDiffActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (groupname.equalsIgnoreCase("E-Library")) {
                            mDrawerLayout.closeDrawers();
                            Intent intent = new Intent(HomeActivity.this, ELibraryActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (groupname.equalsIgnoreCase("Track Vehicle")) {
                            mDrawerLayout.closeDrawers();
                            ConnectionDetector cd = new ConnectionDetector(HomeActivity.this);
                            if (!cd.isConnectingToInternet()) {
                                Constants.isShowInternetMsg = true;
                                Constants.NotifyNoInternet(HomeActivity.this);
                            } else {
                                //Intent intent = new Intent(HomeActivity.this, VechicleLocationActivity.class);
                                //test jaydeep
                                Intent intent = new Intent(HomeActivity.this, TrackVehicle.class);
                                intent.putExtra("IsFromHome", "IsFromHome");
                                startActivity(intent);
                                onClickAnimation();
                            }
                        } else if (groupname.equalsIgnoreCase("Photo Gallery")) {
                            mDrawerLayout.closeDrawers();
                            //Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "PhotoGalleryActivity");
                            Intent intent = new Intent(HomeActivity.this, PhotoGalleryActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (groupname.equalsIgnoreCase("Send Query")) {
                            mDrawerLayout.closeDrawers();
                            ConnectionDetector cd = new ConnectionDetector(HomeActivity.this);
                            if (!cd.isConnectingToInternet()) {
                                Constants.isShowInternetMsg = true;
                                Constants.NotifyNoInternet(HomeActivity.this);
                            } else {
                                Intent intent = new Intent(HomeActivity.this, ReportBugActivity.class);
                                intent.putExtra("IsFromHome", "IsFromHome");
                                startActivity(intent);
                                onClickAnimation();

                            }
                        } else if (groupname.equalsIgnoreCase("Voice Message")) {
                            mDrawerLayout.closeDrawers();
                            Intent intent = new Intent(HomeActivity.this, VoiceMessageListActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (groupname.equalsIgnoreCase("Apply Leave")) {
                            mDrawerLayout.closeDrawers();
                            Intent intent = new Intent(HomeActivity.this, LeaveListActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (groupname.equalsIgnoreCase("Documents")) {
                            mDrawerLayout.closeDrawers();
                            Intent intent = new Intent(HomeActivity.this, DocumentsActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (groupname.equalsIgnoreCase("Practice Test")) {
                            mDrawerLayout.closeDrawers();
                            Intent intent = new Intent(HomeActivity.this, PracticeTestMainSubjectActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (groupname.equalsIgnoreCase("Take appointment")) {
                            mDrawerLayout.closeDrawers();
                            Intent intent = new Intent(HomeActivity.this, TakeAppointmentListActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (groupname.equalsIgnoreCase("LogOut")) {
                            mDrawerLayout.closeDrawers();
                            AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this)
                                    .setTitle("Logout Confirmation")
                                    .setIcon(R.mipmap.notification)
                                    .setMessage("Do you want to Logout?")
                                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            db.ClearAllRecords("0");
                                            mcommon.ClearSharedPreferences(HomeActivity.this);
                                            Datastorage.ClearSharedPreferences((HomeActivity.this));
                                            Intent intent = new Intent(HomeActivity.this, LoginNewActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | /*Intent.FLAG_ACTIVITY_NEW_TASK | */Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.putExtra("IsFromHome", "IsFromHome");

                                            startActivity(intent);
                                            HomeActivity.this.finish();
                                            onClickAnimation();
                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .create();
                            dialog.show();
                        } else if (groupname.equalsIgnoreCase("Exit")) {
                            mDrawerLayout.closeDrawers();
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "Exit");
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception ex) {
                        Common.printStackTrace(ex);
                    }
                    return false;
                }
            });
            mMainMenuList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    mDrawerLayout.closeDrawers();
                    final String selected = (String) mMenuAdapter.getChild(groupPosition, childPosition);
                    String Fecard = "1";
                    String Paid = "2";
                    String Pending = "3";
                    try {
                        try {
                            Datastorage.SetLastOpenedGroup(HomeActivity.this, String.valueOf(groupPosition));
                        } catch (Exception ex) {
                            Common.printStackTrace(ex);
                        }
                        if (selected.equalsIgnoreCase("Homework")) {
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "Homework");
                            Boolean isSmsExist = false;

                            if (isSmsExist == false) {
                                Intent intent = new Intent(HomeActivity.this, DailyDiaryCalanderActivity.class);
                                intent.putExtra("msgtype", Constants.HW_HOMEWORK);
                                intent.putExtra("IsFromHome", "IsFromHome");
                                startActivity(intent);
                                onClickAnimation();
                            }
                        } else if (selected.equalsIgnoreCase("Homework Not Done")) {
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "HomeworkNotDone");
                            Intent intent = new Intent(HomeActivity.this, DailyDiaryCalanderActivity.class);
                            intent.putExtra("msgtype", Constants.HW_HOMEWORKNOTDONE);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();

                        } else if (selected.equalsIgnoreCase("Late Come")) {
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "LateCome");
                            Intent intent = new Intent(HomeActivity.this, DailyDiaryCalanderActivity.class);
                            intent.putExtra("msgtype", Constants.HW_LATECOME);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();

                        } else if (selected.equalsIgnoreCase("Absent")) {
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "Absent");
                            Intent intent = new Intent(HomeActivity.this, DailyDiaryCalanderActivity.class);
                            intent.putExtra("msgtype", Constants.HW_ABSENT);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (selected.equalsIgnoreCase("Uniform Infraction")) {
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "UniformInfraction");
                            Intent intent = new Intent(HomeActivity.this, DailyDiaryCalanderActivity.class);
                            intent.putExtra("msgtype", Constants.HW_UNIFORMNOTPROPER);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();

                        } else if (selected.equalsIgnoreCase("Food Infraction")) {
                            //Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "UniformInfraction");
                            Intent intent = new Intent(HomeActivity.this, DailyDiaryCalanderActivity.class);
                            intent.putExtra("msgtype", Constants.FOOD);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (selected.equalsIgnoreCase("Hygiene")) {
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "UniformInfraction");
                            Intent intent = new Intent(HomeActivity.this, DailyDiaryCalanderActivity.class);
                            intent.putExtra("msgtype", Constants.HYGIENE);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (selected.equalsIgnoreCase("Remarks")) {
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "UniformInfraction");
                            Intent intent = new Intent(HomeActivity.this, DailyDiaryCalanderActivity.class);
                            intent.putExtra("msgtype", Constants.REMARKS);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (selected.equalsIgnoreCase("All Exams")) {
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "AllExams");
                            Intent intent = new Intent(HomeActivity.this, ExamListActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (selected.equalsIgnoreCase("Marksheet Exams")) {
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "MarksheetExam");
                            Intent intent = new Intent(HomeActivity.this, ExamListMarksheetActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (selected.equalsIgnoreCase("Fee Card")) {
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "FeeCard");
                            Intent intent = new Intent(HomeActivity.this, FeeCardActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            intent.putExtra("FeeStatus", Fecard);
                            startActivity(intent);
                            onClickAnimation();
                        } else if (selected.equalsIgnoreCase("Paid Fee")) {
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "PaidFee");
                            Intent intent = new Intent(HomeActivity.this, FeeCardActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            intent.putExtra("FeeStatus", Paid);
                            startActivity(intent);
                            onClickAnimation();
                        } else if (selected.equalsIgnoreCase("Pending Fee")) {
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "PendingFee");
                            Intent intent = new Intent(HomeActivity.this, FeeCardActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            intent.putExtra("FeeStatus", Pending);
                            startActivity(intent);
                            onClickAnimation();
                        } else if (selected.equalsIgnoreCase("Change Pin")) {
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "ChangePinActivity");
                            Intent intent = new Intent(HomeActivity.this, ChangePinActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (selected.equalsIgnoreCase("Add Account")) {
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "AddAccountActivity");
                            Intent intent = new Intent(HomeActivity.this, AddAccountActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (selected.equalsIgnoreCase("Remove Account")) {
//                            Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "RemoveAccount");
                            Intent intent = new Intent(HomeActivity.this, AccountListRemoveActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (selected.equalsIgnoreCase("Set Default Account")) {
//                          Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "SetDefaultAccount");
                            Intent intent = new Intent(HomeActivity.this, AccountListActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        } else if (selected.equalsIgnoreCase("Set Default Year")) {
//                          Constants.googleAnalyticEvent(HomeActivity.this, Constants.button_click, "SetDefaulYear");
                            Intent intent = new Intent(HomeActivity.this, AcademicYearActivity.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            startActivity(intent);
                            onClickAnimation();
                        }
                    } catch (Exception ex) {
                        Common.printStackTrace(ex);
                    }
                    return true;
                }
            });
            int lastopengroup = Integer.parseInt(Datastorage.GetLastOpenedGroup(HomeActivity.this));
            if (lastopengroup != 0) {
                mMainMenuList.expandGroup(lastopengroup);
            }
            mMenuAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        try {
            super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.dashboard, menu);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (mDrawerLayout.isShown())
                mDrawerLayout.closeDrawers();
            switch (item.getItemId()) {
                case R.id.action_changepin:
                    Intent intent = new Intent(HomeActivity.this, ChangePinActivity.class);
                    startActivity(intent);
                    onClickAnimation();
                    break;

                case R.id.action_add_account:
                    intent = new Intent(HomeActivity.this, AddAccountActivity.class);
                    startActivity(intent);
                    onClickAnimation();
                    break;

                case R.id.action_remove_account:
                    intent = new Intent(HomeActivity.this, AccountListRemoveActivity.class);
                    startActivity(intent);
                    onClickAnimation();
                    break;

                case R.id.action_set_default_account:
                    intent = new Intent(HomeActivity.this, AccountListActivity.class);
                    startActivity(intent);
                    onClickAnimation();
                    break;

                case R.id.action_set_default_year:
                    intent = new Intent(HomeActivity.this, AcademicYearActivity.class);
                    startActivity(intent);
                    onClickAnimation();
                    break;

                case R.id.action_about:
                    intent = new Intent(HomeActivity.this, AboutActivity.class);
                    startActivity(intent);
                    onClickAnimation();
                    break;

                case R.id.action_exit:
                    intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    onClickAnimation();
                    break;

                case R.id.Menu_Stud_profile:

                    /*intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                            "Say something");
                    try {
                        startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);

                    } catch (ActivityNotFoundException a) {
                        Toast.makeText(getApplicationContext(),
                                "Speech not supported",
                                Toast.LENGTH_SHORT).show();
                    }*/

                    intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    intent.putExtra("IsFromHome", "IsFromHome");
                    startActivity(intent);
                    onClickAnimation();
                    break;

                default:
                    break;

            }
            if (mToggle.onOptionsItemSelected(item)) {
                return true;
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        GetBannerDetails();
        showBannerAd(mAdView, ActivityNames.HomeActivity);
        super.onResume();
     /*   mBroadcastManager.registerReceiver(mReceiverFilter, new IntentFilter(
                getResources().getString(R.string.broadcast_pending_key)));*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       /* if (mReceiverFilter != null)
            mBroadcastManager.unregisterReceiver(mReceiverFilter);*/
    }

    private class GetProfile extends AsyncTask<Void, Void, Void> {
        String Path = "";

        @Override
        protected void onPreExecute() {
            // mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Path = getStudentImagePath();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Path != null && !Path.isEmpty()) {
                    Glide.with(HomeActivity.this).load(Path)
                            .asBitmap().centerCrop().placeholder(R.drawable.loading).error(R.drawable.loading).into(new BitmapImageViewTarget(mProfileImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            mProfileImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                   /* Picasso.with(HomeActivity.this).load(Path)
                            .resize(500, 500)
                            .into(mProfileImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap imageBitmap = ((BitmapDrawable) mProfileImage.getDrawable()).getBitmap();
                                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                    imageDrawable.setCircular(true);
                                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                    mProfileImage.setImageDrawable(imageDrawable);
                                }
                                @Override
                                public void onError() {
                                    mProfileImage.setImageResource(R.drawable.nopics);
                                }
                            });*/
                } else {
                    Glide.with(HomeActivity.this).load(Path)
                            .asBitmap().centerCrop().placeholder(R.drawable.nopics).error(R.drawable.nopics).into(new BitmapImageViewTarget(mProfileImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            mProfileImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                   /* Bitmap resource = BitmapFactory.decodeResource(getResources(), R.drawable.nopics);
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    mProfileImage.setImageDrawable(circularBitmapDrawable);*/
                }
            } catch (Exception ex) {
                Common.printStackTrace(ex);
            }
            //mProgressBar.setVisibility(View.GONE);
            if (isOnline())
                sendLogDetail();
        }
    }

    private class GetMyPhotoAlbum extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
//                    AlbumDetails();
                List<Contact> contacts = db.getAlbumDetailsGrid(Integer.parseInt(StudentId), Integer.parseInt(SchoolId), mPhotoLimit);
                if (contacts.size() > 0) {
                    TotalItemList = new String[contacts.size()];
                    albumlist = new String[contacts.size()];
                    albumtime = new String[contacts.size()];
                    albumid = new Integer[contacts.size()];
                    FilePathStrings = new String[contacts.size()];
                    int i = 0;
                    String PhotoGallertFolderPath = Constants.GetPhotoGalleryFolderPath();
                    for (Contact cn : contacts) {
                        String PhotoFileName = cn.getAlbumPhotofile();
                        String URL = cn.getAlbumurl();
                        File fileexist = new File(file + "/" + PhotoFileName);
                        String File_Path_Str = PhotoGallertFolderPath + "/" + PhotoFileName + "@@@###" + PhotoFileName;
                        FilePathStrings[i] = File_Path_Str;
                        albumlist[i] = cn.getAlbumName();
                        albumtime[i] = cn.getAlbumDatetime();
                        albumid[i] = cn.getAlbumId();
                        if (!fileexist.exists()) {
                            Bitmap bmp = Constants.getBitmap(URL, fileexist);
                            Constants.SaveImage(bmp, PhotoFileName);
                        }
                        i++;
                    }
                } else {
                    AlbumDetails();
                    List<Contact> contacts1 = db.getAlbumDetailsGrid(Integer.parseInt(StudentId), Integer.parseInt(SchoolId), mPhotoLimit);
                    if (contacts1.size() > 0) {
                        TotalItemList = new String[contacts1.size()];
                        albumlist = new String[contacts1.size()];
                        albumtime = new String[contacts1.size()];
                        albumid = new Integer[contacts1.size()];
                        FilePathStrings = new String[contacts1.size()];

                        int i = 0;
                        String PhotoGallertFolderPath = Constants.GetPhotoGalleryFolderPath();
                        for (Contact cn : contacts1) {
                            String PhotoFileName = cn.getAlbumPhotofile();
                            String URL = cn.getAlbumurl();
                            String File_Path_Str = PhotoGallertFolderPath
                                    + "/" + PhotoFileName + "@@@###"
                                    + PhotoFileName;
                            File fileexist = new File(file + "/" + PhotoFileName + "");

                            FilePathStrings[i] = File_Path_Str;
                            albumlist[i] = cn.getAlbumName();
                            albumtime[i] = cn.getAlbumDatetime();
                            albumid[i] = cn.getAlbumId();

                            if (!fileexist.exists()) {
                                Bitmap bmp = Constants.getBitmap(URL.toString(), fileexist);
                                Constants.SaveImage(bmp, PhotoFileName);
                            }
                            i++;
                        }
                    }
                }
            } catch (Exception ex) {
                Common.printStackTrace(ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(HomeActivity.this);
                } else {
                    String[] imageUrl = {""}, title = {""};
                    Integer[] albumIds = {};
                    if (TotalItemList != null && TotalItemList.length > 0) {
                       /* if (FilePathStrings.length > 6) {
                            imageUrl = new String[6];
                            title = new String[6];
                            albumIds = new Integer[6];
                            for (int j = 0; j < 6; j++) {
                                imageUrl[j] = FilePathStrings[j];
                                title[j] = albumlist[j];
                                albumIds[j] = albumid[j];
                            }
                            mPhotoAdapter = new HomePhotoListAdapter(HomeActivity.this, imageUrl, title, albumIds);
                            mPhotographsRecycle.setAdapter(mPhotoAdapter);
                            mPhotoAdapter.notifyDataSetChanged();
                        } else {*/
                        mPhotoAdapter = new HomePhotoListAdapter(HomeActivity.this, FilePathStrings, albumlist, albumid);
                        mPhotographsRecycle.setAdapter(mPhotoAdapter);
                        mPhotoAdapter.notifyDataSetChanged();
                        //}
                        ((View) findViewById(R.id.llPhotosView)).setVisibility(View.VISIBLE);
                    } else {
                        ((View) findViewById(R.id.llPhotosView)).setVisibility(View.GONE);
                        //Toast.makeText(HomeActivity.this, "No Photos Available", Toast.LENGTH_LONG).show();
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception ex) {
                Common.printStackTrace(ex);
                mProgressBar.setVisibility(View.GONE);
            }
            if (isCircularShow) {
                GetCircularForStudentNew_one();
                //new GetNoticeBoard().execute();
            }
        }

        public String[] AlbumDetails() {
            if (isOnline()) {
                SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.ALBUM_DETAILS);
                request.addProperty("SchoolId", Integer.parseInt(SchoolId));
                request.addProperty("ClassSecId", Integer.parseInt(Datastorage.GetClassSecId(HomeActivity.this)));
                request.addProperty("PhotType", 1);
                request.addProperty("StudId", Integer.parseInt(StudentId));
                request.addProperty("yearid", Integer.parseInt(Year_Id));
                try {
                    SoapObject result = Constants.CallWebMethod(HomeActivity.this, request, Constants.ALBUM_DETAILS, false);
                    if (result != null && result.getPropertyCount() > 0) {
                        SoapObject obj2 = (SoapObject) result.getProperty(0);
                        if (obj2 != null) {
                            int count = obj2.getPropertyCount();
                            String[] myarray = new String[count];
                            for (int i = 0; i < count; i++) {
                                myarray[i] = obj2.getProperty(i).toString();
                                String[] msgitem = myarray[i].split("##@@");
                                String filename = msgitem[2];
                                int albumid = Integer.parseInt(msgitem[0]);
                                boolean isinserted = db.CheckAlbumDetailsInserted(
                                        Integer.parseInt(StudentId),
                                        Integer.parseInt(SchoolId),
                                        Integer.parseInt(msgitem[4]),
                                        albumid, filename);

                                if (!isinserted) {
                                    db.AddAlbumDetails(new Contact(Integer.parseInt(StudentId),
                                            albumid, msgitem[1], msgitem[2],
                                            msgitem[5], Integer.parseInt(SchoolId),
                                            Integer.parseInt(msgitem[4]),
                                            Long.parseLong(msgitem[8]), msgitem[6]));
                                }
                            }
                        } else {
                            albumlist = null;
                        }
                    }
                } catch (Exception e) {
                    Common.printStackTrace(e);
                    return albumlist;
                }
                return albumlist;
            } else {
                return albumlist;
            }
        }
    }

    // for Message List
    private class GetMessageList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            try {
                Latest_SMS_ID = db.GetLatestSMSID(Integer.parseInt(StudentId),
                        Integer.parseInt(SchoolId), Integer.parseInt(Year_Id));
                GetMessagesFromLocalDatabase();
                mTxtHomeWorkCount.setText(db.GetTodayHomeWorkCount(StudentId, SchoolId, "2,9", Year_Id, mCurrentDay, mCurrentMonth, mCurrentYear));
            } catch (Exception err) {
                Constants.Logwrite("Error", err.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (messages == null || messages.length == 0) {
                GetMessageDetail();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(HomeActivity.this);
                } else {
                    int cntr = 0;
                    List<Contact> contacts = db
                            .GetAllSMSDetails(
                                    Integer.parseInt(StudentId),
                                    Integer.parseInt(SchoolId),
                                    Integer.parseInt(Year_Id), mMessageLimit);
                    messages = new String[contacts.size()];
                    if (messages != null && messages.length > 0) {
                        for (Contact cn : contacts) {
                            String msg = cn.getSMSText();
                            messages[cntr] = msg;
                            cntr++;
                        }
                    } else {
                        Constants.writelog("MessageListExpandable", "319 No messgae found");
                    }
                    if (messages != null && messages.length > 0) {
                        if (messages.length > 5) {
                            String[] tmpMessages;
                            tmpMessages = new String[5];
                            for (int j = 0; j < 5; j++) {
                                tmpMessages[j] = messages[j];
                            }
                            //  mMessageAdapter = new HomeMessageAdapter(HomeActivity.this, tmpMessages);
                            //  mMessageViewPager.setAdapter(mMessageAdapter);
                            mMessageAdapter = new HomeMessageAdapter(tmpMessages, HomeActivity.this);
                            mMessageViewPager.setAdapter(mMessageAdapter);


                            mMessageAdapter.notifyDataSetChanged();
                        } else {
                            // mMessageAdapter = new HomeMessageAdapter(HomeActivity.this, messages);
                            //  mMessageViewPager.setAdapter(mMessageAdapter);
                            mMessageAdapter = new HomeMessageAdapter(messages, HomeActivity.this);
                            mMessageViewPager.setAdapter(mMessageAdapter);

                            mMessageAdapter.notifyDataSetChanged();
                        }
                        ((View) findViewById(R.id.rlMessageView)).setVisibility(View.VISIBLE);
                    } else {
                        ((View) findViewById(R.id.rlMessageView)).setVisibility(View.GONE);
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception ex) {
                Constants.writelog("MessageListExpandable", "309 Ex:" + ex.getMessage() + "::::::" + ex.getStackTrace());
                mProgressBar.setVisibility(View.GONE);
            }
            if (isPhotoGalleryShow) {
                new GetMyPhotoAlbum().execute();
            } else if (isCircularShow) {
                GetCircularForStudentNew_one();
            }
        }
    }

    private class GetAttendanceDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (isOnline()) {
                    SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_ATTENDANCE_KUMKUM);
                    request.addProperty("StudId", StudentId);
                    request.addProperty("yearid", Year_Id);
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
                    int IsCurrentYear = 0;
                    Boolean IsCurrentYearId = db.CheckAcademicYearCurrentOrNot(
                            Integer.parseInt(StudentId), Integer.parseInt(SchoolId), Integer.parseInt(Year_Id));
                    if (IsCurrentYearId) {
                        IsCurrentYear = 1;
                    } else {
                        IsCurrentYear = 0;
                    }
                    String LastUpdatedTime = Datastorage.GetLastUpdatedtime(HomeActivity.this);
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
                                    + Year_Id
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
                            .CallWebMethod(HomeActivity.this, request,
                                    Constants.GET_ATTENDANCE_KUMKUM, false);
                    String attendance = "";
                    String[] parts = null;
                    if (result != null && result.getPropertyCount() > 0) {
                        String obtained = result.getPropertyAsString(0);
                        attendance = obtained;
                        if (!attendance.equalsIgnoreCase("No data Available") || !attendance.equalsIgnoreCase("Nodata Available")) {
                            parts = attendance.split(",");
                            int res = db.updateStudAttendanceDetails(
                                    Integer.parseInt(StudentId),
                                    Integer.parseInt(SchoolId), Integer.parseInt(Year_Id), attendance);
                            if (res != 1) {
                                db.AddStudAttendanceDetails(
                                        Integer.parseInt(StudentId),
                                        Integer.parseInt(SchoolId), Integer.parseInt(Year_Id), attendance);
                            }
                            Datastorage.SetLastAutoUpdateAttendance(
                                    HomeActivity.this, cur_time.monthDay);
                        }
                    }
                }
            } catch (Exception ex) {
                Constants.writelog("HomeActivity",
                        "GetattendanceDetails 1877:" + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            String attendanceDetails = db.GetStudAttendanceCount(StudentId, SchoolId, Year_Id);
            if (!attendanceDetails.isEmpty()) {
                mTxtAttedenceCount.setText(attendanceDetails);
            }
        }
    }

    private class MyTaskGetAllYear extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetAllAcademicYear();
            return null;
        }
    }
  /*  private void getPracticeTestList() {
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            final String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            final String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
            String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
            Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                    .GetPracticeTestList(mStudentId, mSchoolId, mYearId, SchoolDetails.appname+"", Constants.CODEVERSION, Constants.PLATFORM);
            call.enqueue(new Callback<AppService>() {
                @Override
                public void onResponse(Call<AppService> call, Response<AppService> response) {
                    try {
                        mProgressBar.setVisibility(View.GONE);

                        AppService tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1") && !tmps.getStrResult().isEmpty()) {
                            PracticeTestFragment practiceTestFragment = new PracticeTestFragment();
                            int size = tmps.getStrlist().size();

                            if (size > 0) {
                                mainArray = tmps.getStrlist();
                                setPracticeTestData(mainArray);

                            } else {
                                mProgressBar.setVisibility(View.GONE);

                            }

                        } else {
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(HomeActivity.this, "No Test Found!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        //   mSwipeLayout.setRefreshing(false);
                        mProgressBar.setVisibility(View.GONE);
                        Constants.writelog(tag, "getPracticetestLiat 107:" + ex.getMessage());
                    }*//*finally {
                        mSwipeLayout.setRefreshing(false);
                    }*//*
                }

                @Override
                public void onFailure(Call<AppService> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    Constants.writelog("BtmNavigationActivity", "getPracticetestLiat 113:" + t.getMessage());
                }
            });
        } else {
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
        }
    }


    //to save expired test data to server
    private void setPracticeTestData(final List<AppService.ListArray> filterList) {
        try {
            *//* Calendar cal = Calendar.getInstance();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss 'GMT'Z yyyy");
                            System.out.println(dateFormat.format(cal.getTime()));
                            *//*
            final String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            final String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());

            int size = filterList.size();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            // String currentDateandTimeTemp = df.format(new Date());
            Calendar calCurrent = Calendar.getInstance();
             currentDiffTime=df.format(calCurrent.getTime());
            // SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY HH:mm");
            // calCurrent.setTime(sdf.parse(currentDateandTimeTemp));

//Log.e("CurrentTime",sdf.format(calCurrent.getTime()));
//Log.e("currentDiffTime",currentDiffTime);
            Constants.Logwrite("CurrentTimeMillis", String.valueOf(calCurrent.getTimeInMillis()));

            for (int i = 0; i < size; i++) {

                String testtime = "0";

                testid = filterList.get(i).getFirst();

                if (filterList.get(i).getThird().equals("0") ||filterList.get(i).getThird().equals("2")) {
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
                                    .SetPracticeTestAnsV3(mStudentId, mSchoolId, mYearId, testid, ansString, SchoolDetails.appname+"", Constants.CODEVERSION, Constants.PLATFORM,"1002 Date :: "+currentDiffTime);
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
                   *//* if (((filterList.get(i).getSixth() != null && !filterList.get(i).getSixth().equalsIgnoreCase("0") && (calCurrent.getTimeInMillis() >= calGiven.getTimeInMillis())) || filterList.get(i).getThird().equals("0")) && index != -1) {
                        contactsTestList.remove(index);
                        Gson gson1 = new Gson();
                        String jsonMenu = gson1.toJson(contactsTestList);
                        commonClass.setSession(Constants.PractiseTestData, jsonMenu);
                    }
                    if (filterList.get(i).getSixth() != null && !filterList.get(i).getSixth().equalsIgnoreCase("0") && (calCurrent.getTimeInMillis() >= calGiven.getTimeInMillis())) {
                        filterList.get(i).setTestExpired(true);
                    } else if (index != -1 && (calCurrent.getTimeInMillis() >= calGivenStart.getTimeInMillis())) {
                        filterList.get(i).setTestTimeOut(true);
                    }*//*
                    //   filterList.get(i).setThird("0");
                    //  listCompleted.add(filterList.get(i));
                }*//* else {
                    listPending.add(filterList.get(i));
                }*//*
            }

        } catch (Exception ex) {
            Constants.writelog(tag, " 107:" + ex.getMessage());
        }

        //mcommon.setSession(Constants.LastAppControlCall, Today.monthDay + "2");

    }*/
}