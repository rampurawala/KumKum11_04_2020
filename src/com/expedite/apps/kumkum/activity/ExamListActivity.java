package com.expedite.apps.kumkum.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.adapter.ExamListAllAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.Contact;
import com.expedite.apps.kumkum.model.ExamListModel;
import com.google.android.gms.ads.AdView;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExamListActivity extends BaseActivity {
    private int isref = 0;
    private String[] ItmNames = new String[0];
    private String[] Itmid = new String[0];
    LinearLayout llMainLayout, empty_examList;
    //    private ListView ItmView;
    private String Year_Id = "", SchoolId, StudentId;
    private Time cur_time = new Time();
    //    private AccountListAdapter mAdapter;
    private ExamListAllAdapter mAdapter;
    private HashMap<String, String> map = new HashMap<String, String>();
    private TextView tv;
    private DatabaseHandler db = null;
    private HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
    private String mIsFromHome = "";
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private GridLayoutManager mGridLayoutManager;
    private int selectedIndex = 0;
    private String mSelectedCategoryId = "";
    private List<String> mSpnCategoryNameArray = new ArrayList<>();
    private List<String> mSpnCategoryIdArray = new ArrayList<>();
    private Spinner mCategorySpinner;
    private ArrayAdapter<String> mCategoryAdapter;
    private List<ExamListModel.ListArray> mExamArrayList = new ArrayList<>();
    private AdView mAdView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_list);

        if (getIntent() != null && getIntent().getExtras() != null)
            mIsFromHome = getIntent().getExtras().getString("IsFromHome", "");

        init();
    }

    public void init() {


        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.ExamListActivity);
            Constants.setActionbar(getSupportActionBar(), ExamListActivity.this, ExamListActivity.this,
                    "ExamList", "ExamListActivity",ActivityNames.ExamListActivity);
            SchoolId = Datastorage.GetSchoolId(ExamListActivity.this);
            StudentId = Datastorage.GetStudentId(ExamListActivity.this);
            Year_Id = Datastorage.GetCurrentYearId(ExamListActivity.this);
            db = new DatabaseHandler(ExamListActivity.this);


            int Cnt_Count = 0;
            Cnt_Count = Datastorage.GetMultipleAccount(ExamListActivity.this);
            if (Cnt_Count == 1) {
                setTitle("All Exam" + "-" + Datastorage.GetStudentName(ExamListActivity.this));
            }
            tv = (TextView) findViewById(R.id.tvmarkqueetextfeecardmenu);
            tv.setSelected(true);
            tv.setText(Datastorage.GetLastUpdatedtime(ExamListActivity.this));
            //ItmView = (ListView) findViewById(R.id.lstcircularlist1);
            mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            llMainLayout = findViewById(R.id.lyt_exam_main);
            empty_examList = findViewById(R.id.empty_ExamList);
            mGridLayoutManager = new GridLayoutManager(ExamListActivity.this, 1);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            mAdapter = new ExamListAllAdapter(ExamListActivity.this, ItmNames, Itmid, 0);
            mRecyclerView.setAdapter(mAdapter);
            try {
                List<Contact> contacts = db.GetAllExamRecords(Integer.parseInt(SchoolId),
                        Integer.parseInt(StudentId), Integer.parseInt(Year_Id), -2);
                int cntr = 0;
                ItmNames = new String[contacts.size()];
                Itmid = new String[contacts.size()];

                mSpnCategoryIdArray.clear();
                mSpnCategoryNameArray.clear();
                mSpnCategoryIdArray.add("-2");
                mSpnCategoryNameArray.add("All Exam");

                if (ItmNames != null && ItmNames.length > 0) {
                    for (Contact cn : contacts) {
                        String msg = cn.getGlobalText();
                        String[] splitrstr = msg.split(",");
                        String ExamName = splitrstr[1].toString();
                        String examid = splitrstr[0].toString();
                        ItmNames[cntr] = ExamName;
                        Itmid[cntr] = examid;
                        if (!mSpnCategoryNameArray.contains(cn.getCATEGORY_NAME())) {
                            if (cn.getCATEGORY_ID() != null && !cn.getCATEGORY_ID().isEmpty() &&
                                    cn.getCATEGORY_NAME() != null && !cn.getCATEGORY_NAME().isEmpty()) {
                                mSpnCategoryIdArray.add(cn.getCATEGORY_ID());
                                mSpnCategoryNameArray.add(cn.getCATEGORY_NAME());
                            }
                        }
                        cntr++;
                    }
                    if (mCategoryAdapter != null)
                        mCategoryAdapter.notifyDataSetChanged();
                    if (ItmNames != null && ItmNames.length > 0) {
                        llMainLayout.setVisibility(View.VISIBLE);
                        empty_examList.setVisibility(View.GONE);
                        mAdapter = new ExamListAllAdapter(ExamListActivity.this, ItmNames, Itmid, 0);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        llMainLayout.setVisibility(View.GONE);
                        empty_examList.setVisibility(View.VISIBLE);
                        Common.showToast(this, SchoolDetails.MsgNoDataAvailable);

                        /*AlertDialog alertDialog = new AlertDialog.Builder(ExamListActivity.this).create();
                        alertDialog.setTitle("Information");
                        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                        alertDialog.setCancelable(false);
                        alertDialog.setIcon(R.drawable.information);
                        alertDialog.setButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        onBackClickAnimation();
                                    }
                                });
                        alertDialog.show();*/
                    }
                } else {
                    // new MyTask().execute();
                    getAllExamListData();
                }
            } catch (Exception ex) {
                Constants.writelog("ExamListActivity", "Init()234:" + ex.getMessage()
                        + "StackTrace::" + ex.getStackTrace().toString());
            }


            int lastautoupdatedate = Datastorage.GetLastAutoUpdateExamMarksheetDay(getApplicationContext());
            cur_time.setToNow();
            if (lastautoupdatedate != cur_time.monthDay) {
                MyTaskRef mytaskRef = new MyTaskRef();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mytaskRef.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    mytaskRef.execute();
                }
            }

            Constants.MyTaskLogVisit LogUserVisited = new Constants.MyTaskLogVisit();
            LogUserVisited.execute(ExamListActivity.this, 2, 6);




           /* MyTaskRef mytaskRef = new MyTaskRef();
            if (mytaskRef.getStatus() != AsyncTask.Status.RUNNING
                    || mytaskRef.getStatus() != AsyncTask.Status.PENDING) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mytaskRef.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    mytaskRef.execute();
                }
            } else {
                Constants.Logwrite("messagelist", "Async taks is running.");
            }*/

            // change by tejas Patel 23-07-2018
            mCategorySpinner = (Spinner) findViewById(R.id.spnCategory);
            mCategoryAdapter = new ArrayAdapter<String>(ExamListActivity.this, R.layout
                    .school_spinner_item, mSpnCategoryNameArray) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    try {
                        ((TextView) v).setTextColor(getResources().getColor(R.color.viewname));
                        ((TextView) v).setTextSize(15);
                    } catch (Exception ex) {
                        Constants.Logwrite("Examlist", "Ex 102:" + ex.getMessage());
                    }
                    return v;
                }
            };
            mCategorySpinner.setAdapter(mCategoryAdapter);
            mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != selectedIndex) {
                        selectedIndex = position;
                        mSelectedCategoryId = mSpnCategoryIdArray.get(position);
                        if (mSelectedCategoryId != null && !mSelectedCategoryId.isEmpty())
                            GetAllExamDataFromDataBase(mSelectedCategoryId, false);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        } catch (Exception err) {
            Constants.writelog("Examlist", "Exception 107:" + err.getMessage());
        }

    }

   /*  public void initListner() {
        mAdapter.setClickListener(ExamListActivity.this);
    }

   @Override
    public void onItemClickListener(int layoutPosition) {
        if (isOnline()) {
            try {
                String item = ItmNames[layoutPosition];
                String Exam_Id = map.get(item);
                if (Exam_Id != null && !Exam_Id.isEmpty()) {
                    Intent intent = new Intent(ExamListActivity.this, StudentResultActvity.class);
                    intent.putExtra("ExamId", Exam_Id);
                    startActivity(intent);
                    onClickAnimation();
                }
            } catch (Exception ex) {
                Constants.writelog("Examlist", "Exception 83 Map:" + map.toString()
                        + " \nAdp:" + mAdapter.toString() + "\nMSG:" + ex.getMessage());
            }
        }
    }*/

//    private class MyTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            mProgressBar.setVisibility(View.VISIBLE);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                getExams(true);
//                GetExamDataFromLocalData();
//            } catch (Exception ex) {
//                Constants.writelog("ExamListActivity", "DoInBackground 205 Ex:"
//                        + ex.getMessage() + ":::::::::" + ex.getStackTrace());
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            try {
//                mProgressBar.setVisibility(View.GONE);
//                if (Constants.isShowInternetMsg) {
//                    Constants.NotifyNoInternet(ExamListActivity.this);
//                } else {
//                    if (ItmNames != null && ItmNames.length > 0) {
//                        mAdapter = new ExamListAllAdapter(ExamListActivity.this, ItmNames, Itmid, 0);
//                        mRecyclerView.setAdapter(mAdapter);
//                        mAdapter.notifyDataSetChanged();
//                    } else {
//                        AlertDialog alertDialog = new AlertDialog.Builder(ExamListActivity.this).create();
//                        alertDialog.setTitle("Information");
//                        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
//                        alertDialog.setCancelable(false);
//                        alertDialog.setIcon(R.drawable.information);
//                        alertDialog.setButton("OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,
//                                                        int which) {
//                                        Intent intent = new Intent(ExamListActivity.this, HomeActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                });
//                        alertDialog.show();
//                    }
//                }
//            } catch (Exception err) {
//                Constants.writelog(
//                        "ExamListActivity",
//                        "Exception 214:" + err.getMessage() + "::::"
//                                + err.getStackTrace());
//            } finally {
//                mProgressBar.setVisibility(View.GONE);
//            }
//        }
//    }

    private class MyTaskRef extends AsyncTask<Void, Void, Void> {
        Boolean isRefreshCall = false;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //LogUserVisted();
                /*cur_time = new Time();
                cur_time.setToNow();*/
                /*int lastautoupdatedExamday = Datastorage
                        .GetLastAutoUpdateExamDay(ExamListActivity.this);
                if (lastautoupdatedExamday != cur_time.monthDay) {*/
                isRefreshCall = true;
                // getExams(false);
                //GetExamDataFromLocalData();
                getAllExamListData();
                /* }*/
            } catch (Exception ex) {
                Constants.writelog("ExamListActivity",
                        "MyTaskRef DoInBackground 291 Ex:" + ex.getMessage()
                                + ":::::::::" + ex.getStackTrace());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (ItmNames != null && ItmNames.length > 0) {
                    mAdapter = new ExamListAllAdapter(ExamListActivity.this, ItmNames, Itmid, 0);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    Time currentTime = new Time();
                    currentTime.setToNow();
                    Datastorage.SetLastAutoUpdateExamDay(
                            ExamListActivity.this, currentTime.monthDay);
                }
            } catch (Exception err) {
                Constants.writelog(
                        "ExamListActivity",
                        "Exception:" + err.getMessage() + "::::"
                                + err.getStackTrace());
            }
        }
    }

//    public void GetExamDataFromLocalData() {
//        try {
//            map = new HashMap<String, String>();
//            List<Contact> contacts;
//            contacts = db.GetAllExamRecords(Integer.parseInt(SchoolId), Integer.parseInt(StudentId), Integer.parseInt(Year_Id));
//            int cntr = 0;
//            ItmNames = new String[contacts.size()];
//            Itmid = new String[contacts.size()];
//            if (ItmNames != null && ItmNames.length > 0) {
//                for (Contact cn : contacts) {
//                    String msg = cn.getGlobalText();
//                    String[] splitrstr = msg.split(",");
//                    String ExamName = splitrstr[1].toString();
//                    String examid = splitrstr[0].toString();
//                    ItmNames[cntr] = ExamName;
//                    Itmid[cntr] = examid;
//                    // map.put(ExamName, examid);
//                    cntr++;
//                }
//            }
//        } catch (Exception err) {
//            Constants.writelog("ExamListActivity", "Ex 382: SchoolId:" + SchoolId + " StudentId:" + StudentId + " YearId:" + Year_Id + "\nMSG:" + err.getMessage());
//        }
//    }

//    public void getExams(Boolean isCompulsory) {
//
//        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_EXAMS);
//        request.addProperty("stud_id", StudentId);
//        request.addProperty("schoolid", SchoolId);
//        request.addProperty("yearid", Year_Id);
//
//        map = new HashMap<String, String>();
//        try {
//            SoapObject result = Constants
//                    .CallWebMethod(ExamListActivity.this, request,
//                            Constants.GET_EXAMS, isCompulsory);
//            if (result != null && result.getPropertyCount() > 0) {
//                SoapObject obj2 = (SoapObject) result.getProperty(0);
//                String[] output = null;
//                if (obj2 != null) {
//                    int count = obj2.getPropertyCount();
//                    output = new String[count];
//                    ItmNames = new String[count];
//                    Itmid = new String[count];
//                    String[] myarray = new String[count];
//                    for (int i = 0; i < count; i++) {
//                        myarray[i] = obj2.getProperty(i).toString();
//                        String[] info = myarray[i].split(",");
//                        String examid = info[0].toString();
//                        String app = "";
//                        for (int j = 1; j < info.length; j++) {
//                            app += info[j].toString() + ",";
//                        }
//                        app = app.substring(0, app.length() - 1);
//                        output[i] = app;
//                        map.put(app, examid);
//
//                        // Check ExamId Is Thers or Not
//
//                        Boolean IsExamInserted = db
//                                .CheckExamInserted(
//                                        Integer.parseInt(StudentId),
//                                        Integer.parseInt(SchoolId),
//                                        Integer.parseInt(Year_Id),
//                                        Integer.parseInt(examid));
//                        if (!IsExamInserted) {
//                            db.AddExams(new Contact(
//                                    Integer.parseInt(Year_Id),
//                                    Integer.parseInt(StudentId), Integer
//                                    .parseInt(SchoolId), Integer
//                                    .parseInt(examid), app, "", 0, "", "", ""));
//                        }
//                    }
//                    ItmNames = output;
//                    // Delete Exams Which Updated
//                    // Get All Exams From Local Database
//                    List<Contact> contacts = db
//                            .GetAllExamRecords(
//                                    Integer.parseInt(SchoolId),
//                                    Integer.parseInt(StudentId),
//                                    Integer.parseInt(Year_Id));
//                    String[] ExamList = new String[contacts.size()];
//                    if (ExamList != null && ExamList.length > 0) {
//                        for (Contact cn : contacts) {
//                            String msg = cn.getGlobalText();
//                            String[] splitrstr = msg.split(",");
//                            String EName = splitrstr[1].toString();
//                            String ExamId = splitrstr[0].toString();
//
//                            // Delete Exam Which is Deleted From Server
//                            int Deleted_Exam_Id = 0;
//                            try {
//                                Deleted_Exam_Id = Integer.parseInt(map.get(
//                                        EName).toString());
//                            } catch (Exception err) {
//                            }
//                            if (Deleted_Exam_Id == 0) {
//                                int To_Del_Exam = Integer.parseInt(ExamId);
//                                int Exam_Delete_Status = db
//                                        .DeleteExtraStudentExmas(
//                                                Integer.parseInt(StudentId),
//                                                Integer.parseInt(SchoolId),
//                                                To_Del_Exam,
//                                                Integer.parseInt(Year_Id));
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception err) {
//            Constants.writelog("ExamListActivity", "Ex 382: Reqest:" + request.toString() + "\nMSG:" + err.getMessage());
//        }
//        // return ItmNames;
//    }

    private void LogUserVisted() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.LOG_USER_VISITED);
        request.addProperty("moduleid", 2);
        request.addProperty("schoolid", SchoolId);
        request.addProperty("User_Id",
                Datastorage.GetUserId(ExamListActivity.this));
        request.addProperty("phoneno",
                Datastorage.GetPhoneNumber(ExamListActivity.this));
        request.addProperty("pageid", 4);
        try {
            Constants.CallWebMethod(ExamListActivity.this, request,
                    Constants.LOG_USER_VISITED, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mIsFromHome != null && !mIsFromHome.isEmpty()) {
                super.onBackPressed();
                onBackClickAnimation();
            } else {
                Intent intent = new Intent(ExamListActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        } catch (Exception err) {
            Constants.Logwrite("StudentInformationActivity:", "BackPressed:" + err.getMessage()
                    + "StackTrace::" + err.getStackTrace().toString());
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            menu.clear();
            menu.add(0, 1, 1, "Refresh").setTitle("Refresh");
            menu.add(0, 2, 2, "Add Account").setTitle("Add Account");
            // menu.add(0, 3, 3, "Remove Account").setTitle("Remove Account");
            menu.add(0, 4, 4, "Set Default Account").setTitle("Set Default Account");
            mapacc = Constants.AddAccount(menu, db);
            return true;
        } catch (Exception err) {
            Constants.Logwrite("Examlist", "msg 465:" + err.getMessage());
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int iid = item.getItemId();
        if (iid == android.R.id.home) {
            hideKeyboard(ExamListActivity.this);
            ExamListActivity.this.finish();
            onBackClickAnimation();
        } else if (iid == 1 || iid == 2 || iid == 3 || iid == 4) {
            if (iid == 1) {
                isref = 1;
                // new MyTask().execute();
                getAllExamListData();
            } else if (iid == 2) {
                addAccountClick(ExamListActivity.this);
            } else if (iid == 3) {
                removeAccountClick(ExamListActivity.this);
            } else if (iid == 4) {
                accountListClick(ExamListActivity.this);
            }
        } else {
            String details = mapacc.get(iid).toString();
            Constants.SetAccountDetails(details, ExamListActivity.this);
            intent = new Intent(ExamListActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
            onBackClickAnimation();
            return true;

            /*
             * String details = mapaccount.get(iid).toString(); String[]
             * split_details = details.split(","); String SchoolId =
             * split_details[1].toString(); String StudId =
             * split_details[2].toString();
             * Datastorage.SetSchoolId(ExamListActivity.this, SchoolId);
             * Datastorage.SetStudentID(ExamListActivity.this, StudId); //
             * LoginDetail.setSchoolId(SchoolId); // LoginDetail.setStudId(StudId);
             *
             * intent = new Intent(ExamListActivity.this, SplashActivity.class);
             * startActivity(intent); finish(); return true;
             */
        }

        return super.onOptionsItemSelected(item);

    }

    private void getAllExamListData() {

        if (isOnline()) {
            empty_examList.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            Call<ExamListModel> call = ((com.expedite.apps.kumkum.MyApplication)
                    getApplicationContext()).getmRetrofitInterfaceAppService().GetExamData(StudentId, SchoolId,
                    Year_Id, Constants.PLATFORM);
            call.enqueue(new Callback<ExamListModel>() {
                @Override
                public void onResponse(Call<ExamListModel> call, Response<ExamListModel> response) {
                    try {
                        ExamListModel tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1")) {
                            map = new HashMap<String, String>();
                            empty_examList.setVisibility(View.GONE);
                            llMainLayout.setVisibility(View.VISIBLE);
                            if (tmps.getListArray() != null && tmps.getListArray().size() > 0) {
                                mExamArrayList.clear();
                                mExamArrayList.addAll(tmps.getListArray());
                                empty_examList.setVisibility(View.GONE);
                                llMainLayout.setVisibility(View.VISIBLE);
                                mProgressBar.setVisibility(View.GONE);
                                try {
                                    ItmNames = new String[mExamArrayList.size()];
                                    Itmid = new String[mExamArrayList.size()];
                                    for (int i = 0; i < mExamArrayList.size(); i++) {
                                        map.put(mExamArrayList.get(i).getSecond(), mExamArrayList.get(i).getFirst());
                                        // Check ExamId Is Thers or Not
                                        int srNo = i;
                                        try {
                                            srNo = Integer.parseInt(mExamArrayList.get(i).getFifth());
                                        } catch (Exception ex) {
                                        }
                                        Boolean IsExamInserted = db
                                                .CheckExamInserted(
                                                        Integer.parseInt(StudentId),
                                                        Integer.parseInt(SchoolId),
                                                        Integer.parseInt(Year_Id),
                                                        Integer.parseInt(mExamArrayList.get(i).getFirst()));
                                        if (!IsExamInserted) {
                                            if (mExamArrayList.get(i).getFirst() != null &&
                                                    !mExamArrayList.get(i).getFirst().trim().isEmpty() &&
                                                    mExamArrayList.get(i).getSecond() != null &&
                                                    !mExamArrayList.get(i).getSecond().trim().isEmpty() &&
                                                    mExamArrayList.get(i).getThird() != null &&
                                                    !mExamArrayList.get(i).getThird().trim().isEmpty() &&
                                                    mExamArrayList.get(i).getFourth() != null &&
                                                    !mExamArrayList.get(i).getFourth().trim().isEmpty()) {
                                                db.AddExams(new Contact(
                                                        Integer.parseInt(Year_Id),
                                                        Integer.parseInt(StudentId), Integer
                                                        .parseInt(SchoolId), Integer
                                                        .parseInt(mExamArrayList.get(i).getFirst()),
                                                        mExamArrayList.get(i).getSecond(),
                                                        "", 0, "",
                                                        mExamArrayList.get(i).getThird(),
                                                        mExamArrayList.get(i).getFourth(), srNo));
                                            }
                                        } else {
                                            // Update record Pending
                                            if (mExamArrayList.get(i).getFirst() != null &&
                                                    !mExamArrayList.get(i).getFirst().trim().isEmpty() &&
                                                    mExamArrayList.get(i).getSecond() != null &&
                                                    !mExamArrayList.get(i).getSecond().trim().isEmpty() &&
                                                    mExamArrayList.get(i).getThird() != null &&
                                                    !mExamArrayList.get(i).getThird().trim().isEmpty() &&
                                                    mExamArrayList.get(i).getFourth() != null &&
                                                    !mExamArrayList.get(i).getFourth().trim().isEmpty()) {
                                                db.UpdateExams(mExamArrayList.get(i).getFirst(),
                                                        mExamArrayList.get(i).getSecond(), "",
                                                        Year_Id, SchoolId, StudentId, "0",
                                                        mExamArrayList.get(i).getThird(),
                                                        mExamArrayList.get(i).getFourth(), srNo);
                                            }
                                        }
                                    }

                                    if (isref == 1) {
                                        // Delete Exams Which Updated
                                        // Get All Exams From Local Database
                                        List<Contact> contacts = db
                                                .GetAllExamRecords(
                                                        Integer.parseInt(SchoolId),
                                                        Integer.parseInt(StudentId),
                                                        Integer.parseInt(Year_Id), -2);
                                        String[] ExamList = new String[contacts.size()];
                                        if (ExamList != null && ExamList.length > 0) {
                                            for (Contact cn : contacts) {
                                                String msg = cn.getGlobalText();
                                                String[] splitrstr = msg.split(",");
                                                String EName = splitrstr[1].toString();
                                                String ExamId = splitrstr[0].toString();

                                                // Delete Exam Which is Deleted From Server
                                                int Deleted_Exam_Id = 0;
                                                try {
                                                    Deleted_Exam_Id = Integer.parseInt(map.get(
                                                            EName).toString());
                                                } catch (Exception err) {
                                                }
                                                if (Deleted_Exam_Id == 0) {
                                                    int To_Del_Exam = Integer.parseInt(ExamId);
                                                    db.DeleteExtraStudentExmas(
                                                            Integer.parseInt(StudentId),
                                                            Integer.parseInt(SchoolId),
                                                            To_Del_Exam,
                                                            Integer.parseInt(Year_Id));
                                                }
                                            }
                                        }
                                    }

                                } catch (Exception err) {
                                    Constants.writelog("ExamListActivity", "Ex 382: Reqest:" + "MSG:" + err.getMessage());
                                }

                                GetAllExamDataFromDataBase("", true);
                            } else {
                                empty_examList.setVisibility(View.VISIBLE);
                                llMainLayout.setVisibility(View.GONE);
                                Common.showToast(ExamListActivity.this, SchoolDetails.MsgNoDataAvailable);
                           /* AlertDialog alertDialog = new AlertDialog.Builder(ExamListActivity.this).create();
                            alertDialog.setTitle("Information");
                            alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                            alertDialog.setCancelable(false);
                            alertDialog.setIcon(R.drawable.information);
                            alertDialog.setButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            Intent intent = new Intent(ExamListActivity.this, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(intent);
                                            finish();
                                            onBackClickAnimation();
                                        }
                                    });
                            alertDialog.show();*/
                            }
                        } else {
                            empty_examList.setVisibility(View.VISIBLE);
                            llMainLayout.setVisibility(View.GONE);
                            Common.showToast(ExamListActivity.this, SchoolDetails.MsgNoDataAvailable);
                        /*AlertDialog alertDialog = new AlertDialog.Builder(ExamListActivity.this).create();
                        alertDialog.setTitle("Information");
                        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                        alertDialog.setCancelable(false);
                        alertDialog.setIcon(R.drawable.information);
                        alertDialog.setButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent intent = new Intent(ExamListActivity.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                        onBackClickAnimation();
                                    }
                                });
                        alertDialog.show();*/
                        }
                        mProgressBar.setVisibility(View.GONE);
                    } catch (Exception ex) {
                        mProgressBar.setVisibility(View.GONE);
                        Constants.writelog("ExamListActivity", "Exception 598:" + ex.getMessage() +
                                "::::" + ex.getStackTrace());
                    }
                    finally {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ExamListModel> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    Constants.writelog("ExamListActivity", "Exception 606:" + t.getMessage());
                }
            });
        } else {
            empty_examList.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(ExamListActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_LONG).show();

        }
    }

    public void GetAllExamDataFromDataBase(String mSelectedId, boolean IsFirstTime) {
        try {
            map = new HashMap<String, String>();
            List<Contact> contacts;
            if (mSelectedId != null && !mSelectedId.trim().isEmpty()) {
                contacts = db.GetAllExamRecords(Integer.parseInt(SchoolId),
                        Integer.parseInt(StudentId),
                        Integer.parseInt(Year_Id), Integer.parseInt(mSelectedId));
            } else {
                contacts = db.GetAllExamRecords(Integer.parseInt(SchoolId),
                        Integer.parseInt(StudentId),
                        Integer.parseInt(Year_Id), -2);
            }
            int cntr = 0;
            ItmNames = new String[contacts.size()];
            Itmid = new String[contacts.size()];
            if (IsFirstTime) {
                mSpnCategoryNameArray.clear();
                mSpnCategoryIdArray.clear();
                mSpnCategoryIdArray.add("-2");
                mSpnCategoryNameArray.add("All Exam");
            }
            if (ItmNames != null && ItmNames.length > 0) {
                for (Contact cn : contacts) {
                    String msg = cn.getGlobalText();
                    String[] splitrstr = msg.split(",");
                    String ExamName = splitrstr[1].toString();
                    String examid = splitrstr[0].toString();
                    ItmNames[cntr] = ExamName;
                    Itmid[cntr] = examid;
                    if (IsFirstTime) {
                        if (cn.getCATEGORY_ID() != null && !cn.getCATEGORY_ID().isEmpty() &&
                                cn.getCATEGORY_NAME() != null && !cn.getCATEGORY_NAME().isEmpty()) {
                            if (!mSpnCategoryNameArray.contains(cn.getCATEGORY_NAME())) {
                                mSpnCategoryIdArray.add(cn.getCATEGORY_ID());
                                mSpnCategoryNameArray.add(cn.getCATEGORY_NAME());
                            }
                        }
                    }
                    cntr++;
                }
            }
            if (IsFirstTime) {
                if (mCategoryAdapter != null) {
                    mCategoryAdapter.notifyDataSetChanged();
                    mCategorySpinner.setSelection(0);
                }
            }


            if (ItmNames != null && ItmNames.length > 0) {
                empty_examList.setVisibility(View.GONE);
                llMainLayout.setVisibility(View.VISIBLE);
                mAdapter = new ExamListAllAdapter(ExamListActivity.this, ItmNames, Itmid, 0);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            } else {
                empty_examList.setVisibility(View.VISIBLE);
                llMainLayout.setVisibility(View.GONE);
                Common.showToast(ExamListActivity.this, SchoolDetails.MsgNoDataAvailable);
               /* AlertDialog alertDialog = new AlertDialog.Builder(ExamListActivity.this).create();
                alertDialog.setTitle("Information");
                alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                alertDialog.setCancelable(false);
                alertDialog.setIcon(R.drawable.information);
                alertDialog.setButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent intent = new Intent(ExamListActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();
                                onBackClickAnimation();
                            }
                        });
                alertDialog.show();*/
            }
        } catch (Exception err) {
            Constants.writelog("ExamListActivity", "Ex 382: SchoolId:" + SchoolId + " StudentId:" + StudentId + " YearId:" + Year_Id + "\nMSG:" + err.getMessage());
        }
    }
}
