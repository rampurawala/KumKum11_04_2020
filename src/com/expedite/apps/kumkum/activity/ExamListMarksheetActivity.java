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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.adapter.ExamListAllAdapter1;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.Contact;
import com.expedite.apps.kumkum.model.ExamListModel;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExamListMarksheetActivity extends BaseActivity {
    private int isref = 0;
    private String[] ItmNames = new String[0];
    private String[] Itmid = new String[0];
    private ProgressBar progressBar;
    private EditText mEmailid;
    //    private ListView ItmView;
//    private AccountListAdapter adp;
    private ExamListAllAdapter1 mAdapter;
    private HashMap<String, String> map = new HashMap<String, String>();
    private TextView tv;
    private DatabaseHandler db = null;
    private HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
    private String SchoolId, StudentId, LastUpdatedTime, Year_Id;
    private String[] arrpath = null;
    private int selectedIndex = 0;
    private Time cur_time = new Time();
    private String mIsFromHome = "";
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private GridLayoutManager mGridLayoutManager;
    private String mSelectedCategoryId = "";
    private List<String> mSpnCategoryNameArray = new ArrayList<>();
    private List<String> mSpnCategoryIdArray = new ArrayList<>();
    private Spinner mCategorySpinner;
    private ArrayAdapter<String> mCategoryAdapter;
    private List<ExamListModel.ListArray> mExamArrayList = new ArrayList<>();
    private androidx.appcompat.app.AlertDialog alert;
    public static Button dowmloadbtn;
    public static CheckBox checkBoxselectall;
    LinearLayout main_examListMarksheet, empty_examListMarksheet;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_list_marksheet);
        if (getIntent() != null && getIntent().getExtras() != null)
            mIsFromHome = getIntent().getExtras().getString("IsFromHome", "");
        init();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.ExamListMarksheetActivity);
            Constants.setActionbar(getSupportActionBar(), ExamListMarksheetActivity.this,
                    ExamListMarksheetActivity.this, "Marksheet Exam", "ExamMarksheet",ActivityNames.ExamListMarksheetActivity);
            SchoolId = Datastorage.GetSchoolId(getApplicationContext());
            StudentId = Datastorage.GetStudentId(getApplicationContext());
            LastUpdatedTime = Datastorage.GetLastUpdatedtime(getApplicationContext());
            Year_Id = Datastorage.GetCurrentYearId(getApplicationContext());
            db = new DatabaseHandler(ExamListMarksheetActivity.this);

            //vishwa 22/4/2019
            dowmloadbtn = (Button) findViewById(R.id.downloadbtn);
            checkBoxselectall = (CheckBox) findViewById(R.id.checkBoxselectall);

            tv = (TextView) findViewById(R.id.tvmarkqueetextexamlistmarksheet);
            tv.setSelected(true);
            tv.setText(LastUpdatedTime);
            mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            main_examListMarksheet = findViewById(R.id.main_examListMarksheet);
            empty_examListMarksheet = findViewById(R.id.empty_examListMarksheet);
            mGridLayoutManager = new GridLayoutManager(ExamListMarksheetActivity.this, 1);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            mAdapter = new ExamListAllAdapter1(ExamListMarksheetActivity.this, ItmNames, arrpath, Itmid);
            mRecyclerView.setAdapter(mAdapter);
            try {
                int cntr = 0;
                ArrayList<String> storedExams = new ArrayList<String>();
                List<Contact> contacts = db.GetAllMarksheetExamRecords(
                        Integer.parseInt(SchoolId),
                        Integer.parseInt(StudentId),
                        Integer.parseInt(Year_Id), -2);
                if (contacts != null && contacts.size() > 0) {
                    int total = contacts.size();
                    for (int i = 0; i < total; i++) {
                        String msg = contacts.get(i).getGlobalText();
                        String[] splitrstr = msg.split(",");
                        int examid = Integer.parseInt(splitrstr[0]);
                        if (storedExams.contains(examid + "")) {
                            int res = db.DeleteDuplicateStudentExmasMarksheet(Integer.parseInt(StudentId),
                                    Integer.parseInt(SchoolId), examid, Integer.parseInt(Year_Id));
                            contacts.remove(i);
                            total--;
                            i--;
                        } else {
                            storedExams.add(examid + "");
                        }
                    }

                    ItmNames = new String[contacts.size()];
                    Itmid = new String[contacts.size()];
                    arrpath = new String[contacts.size()];
                    mSpnCategoryIdArray.clear();
                    mSpnCategoryNameArray.clear();
                    mSpnCategoryIdArray.add("-2");
                    mSpnCategoryNameArray.add("All Marksheet");
                    for (Contact cn : contacts) {
                        String msg = cn.getGlobalText();
                        String[] splitrstr = msg.split(",");
                        String ExamName = splitrstr[1];
                        String examid = splitrstr[0];
                        ItmNames[cntr] = ExamName;
                        Itmid[cntr] = examid;
                        map.put(ExamName, examid);
                        arrpath[cntr] = splitrstr[2];
                        if (!mSpnCategoryNameArray.contains(cn.getCATEGORY_NAME())) {
                            if (cn.getCATEGORY_ID() != null && !cn.getCATEGORY_ID().isEmpty() &&
                                    cn.getCATEGORY_NAME() != null && !cn.getCATEGORY_NAME().isEmpty()) {
                                mSpnCategoryIdArray.add(cn.getCATEGORY_ID());
                                mSpnCategoryNameArray.add(cn.getCATEGORY_NAME());
                            }
                        }
                        //mExamArrayFilterList.add()
                        cntr++;
                    }
                    if (mCategoryAdapter != null)
                        mCategoryAdapter.notifyDataSetChanged();

                    if (ItmNames != null && ItmNames.length > 0) {
                        main_examListMarksheet.setVisibility(View.VISIBLE);
                        empty_examListMarksheet.setVisibility(View.GONE);
                        ((View) findViewById(R.id.llSelectall)).setVisibility(View.VISIBLE);
                        mAdapter = new ExamListAllAdapter1(ExamListMarksheetActivity.this, ItmNames, arrpath, Itmid);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ((View) findViewById(R.id.llSelectall)).setVisibility(View.GONE);
                        main_examListMarksheet.setVisibility(View.GONE);
                        empty_examListMarksheet.setVisibility(View.VISIBLE);
                        Common.showToast(this,SchoolDetails.MsgNoDataAvailable);
                       /* AlertDialog alertDialog = new AlertDialog.Builder(ExamListMarksheetActivity.this).create();
                        alertDialog.setTitle("Information");
                        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                        alertDialog.setIcon(R.drawable.information);
                        alertDialog.setCancelable(false);
                        alertDialog.setButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        onBackClickAnimation();
                                    }
                                });
                        alertDialog.show();*/
                        return;
                    }
                } else {
//                    new MyTask().execute();
                    getAllExamMarkSheetListData();
                }
            } catch (Exception ex) {
                Constants.writelog("ExamListMarksheetActivity", "init()353 Msg 171:" + ex.getMessage());
            }

            int lastautoupdatedate = Datastorage.GetLastAutoUpdateExamMarksheetDay(getApplicationContext());
            cur_time.setToNow();
            if (lastautoupdatedate != cur_time.monthDay) {
                MyTaskRefresh mytaskRef = new MyTaskRefresh();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mytaskRef.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    mytaskRef.execute();
                }
            }

            Constants.MyTaskLogVisit LogUserVisited = new Constants.MyTaskLogVisit();
            LogUserVisited.execute(ExamListMarksheetActivity.this, 2, 6);

            // change by tejas Patel 23-07-2018
            mCategorySpinner = (Spinner) findViewById(R.id.spnCategory);
            mCategoryAdapter = new ArrayAdapter<String>(ExamListMarksheetActivity.this, R.layout
                    .school_spinner_item, mSpnCategoryNameArray) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    try {
                        ((TextView) v).setTextColor(getResources().getColor(R.color.viewname));
                        ((TextView) v).setTextSize(15);
                    } catch (Exception ex) {
                        Constants.Logwrite("ExamListMarksheetActivity", "Ex 197:" + ex.getMessage());
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
                            getAllExamMarkSheetDataFromDataBase(mSelectedCategoryId, false);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        } catch (Exception ex) {
            Constants.writelog("ExamListMarksheetActivity",
                    "MSG 216:" + ex.getMessage());
        }
    }

    private class MyTaskRefresh extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            //getMarksheetExams(false);
            getAllExamMarkSheetListData();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (ItmNames != null && ItmNames.length > 0) {
                ((View) findViewById(R.id.llSelectall)).setVisibility(View.VISIBLE);
                mAdapter = new ExamListAllAdapter1(ExamListMarksheetActivity.this, ItmNames, arrpath, Itmid);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                Datastorage.SetLastAutoUpdateExamMarksheetDay(ExamListMarksheetActivity.this, cur_time.monthDay);
            }
            super.onPostExecute(result);
        }
    }

//    private class MyTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            mProgressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            getMarksheetExams(true);
//            return null;
//        }
//
//        @Override
//        public void onPostExecute(Void result) {
//            try {
//                if (Constants.isShowInternetMsg) {
//                    Constants.NotifyNoInternet(getApplicationContext());
//                } else {
//                    mProgressBar.setVisibility(View.GONE);
//                    try {
//                        if (ItmNames != null && ItmNames.length > 0) {
//                            mAdapter = new ExamListAllAdapter1(ExamListMarksheetActivity.this, ItmNames, arrpath, Itmid);
//                            mRecyclerView.setAdapter(mAdapter);
//                            mAdapter.notifyDataSetChanged();
//                        } else {
//                            AlertDialog alertDialog = new AlertDialog.Builder(ExamListMarksheetActivity.this).create();
//                            alertDialog.setTitle("Information");
//                            alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
//                            alertDialog.setIcon(R.drawable.information);
//                            alertDialog.setCancelable(false);
//                            alertDialog.setButton("OK",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            finish();
//                                        }
//                                    });
//                            alertDialog.show();
//                            return;
//                        }
//                    } catch (Exception ex) {
//                        Constants.writelog("ExamListMarksheetActivity", "initpost()400 Msg:" + ex.getMessage());
//                    }
//                }
//            } catch (Exception err) {
//                Constants.writelog("ExamListMarksheetActivity", "Msg 345:" + err.getMessage());
//            } finally {
//                mProgressBar.setVisibility(View.GONE);
//            }
//        }
//    }


//    public void getMarksheetExams(Boolean isCompulsory) {
//        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_MARKSHEET_EXAMS_WITH_PATH);
//        try {
//            request.addProperty("schoolid", SchoolId);
//            request.addProperty("yearid", Integer.parseInt(Year_Id));
//            request.addProperty("Class_Id", Integer.parseInt(Datastorage.GetClassId(ExamListMarksheetActivity.this)));
//            request.addProperty("stud_id", Integer.parseInt(StudentId));
//            request.addProperty("yeartext", Datastorage.GetAcademicYear(ExamListMarksheetActivity.this));
//            request.addProperty("classname", Datastorage.GetClassName(ExamListMarksheetActivity.this));
//
//            SoapObject result = Constants
//                    .CallWebMethod(ExamListMarksheetActivity.this, request,
//                            Constants.GET_MARKSHEET_EXAMS_WITH_PATH, isCompulsory);
//            if (result != null && result.getPropertyCount() > 0) {
//                map = new HashMap<String, String>();
//                SoapObject obj2 = (SoapObject) result.getProperty(0);
//                String[] output = null;
//                if (obj2 != null) {
//                    int count = obj2.getPropertyCount();
//                    output = new String[count];
//                    ItmNames = new String[count];
//                    Itmid = new String[count];
//                    String[] myarray = new String[count];
//                    arrpath = new String[count];
//                    for (int i = 0; i < count; i++) {
//                        myarray[i] = obj2.getProperty(i).toString();
//                        String[] info = myarray[i].split(",");
//                        String examid = info[0];
//                        String examname = info[1];
//                        String exampath = info[2];
//                        arrpath[i] = info[2];
//                        /*
//                         * String app = ""; for (int j = 1; j < info.length;
//						 * j++) { app += info[j] + ","; } app =
//						 * app.substring(0, app.length() - 1); output[i] = app;
//						 * map.put(app, examid);
//						 */
//                        map.put(examname, examid);
//                        output[i] = examname;
//
//                        Boolean IsExamInserted = db
//                                .CheckMarksheetExamsInserted(
//                                        Integer.parseInt(StudentId),
//                                        Integer.parseInt(SchoolId),
//                                        Integer.parseInt(Year_Id),
//                                        Integer.parseInt(examid));
//                        int res = 0;
//                        String[] parts = exampath.split("//");
//                        if (IsExamInserted) {
//                            res = db.UpdateExams(examid, examname, exampath,
//                                    Year_Id, SchoolId, StudentId, "1");
//                        } else {
//                            db.AddExams(new Contact(Integer.parseInt(Year_Id),
//                                    Integer.parseInt(StudentId), Integer
//                                    .parseInt(SchoolId), Integer
//                                    .parseInt(examid), examname, "", 1,
//                                    exampath, "", ""));
//                        }
//                        Constants.DeletePdf(parts[parts.length - 1]);
//                    }
//                    ItmNames = output;
//                    if (isref == 1) {
//                        List<Contact> contacts = db.GetAllMarksheetExamRecords(
//                                Integer.parseInt(SchoolId),
//                                Integer.parseInt(StudentId),
//                                Integer.parseInt(Year_Id));
//                        String[] ExamMarkseets = new String[contacts.size()];
//                        Constants.Logwrite("ExamListMarksheetActivity", "Total Exam Length:"
//                                + contacts.size());
//                        if (ExamMarkseets != null && ExamMarkseets.length > 0) {
//                            for (Contact cn : contacts) {
//                                String msg = cn.getGlobalText();
//                                Constants.Logwrite("ExamListMarksheetActivity", "ExamString:" + msg);
//                                String[] splitrstr = msg.split(",");
//                                String ExamName = splitrstr[1];
//                                Constants.Logwrite("ExamListMarksheetActivity", "ExamName:"
//                                        + ExamName);
//                                String examid = splitrstr[0];
//                                Constants.Logwrite("ExamListMarksheetActivity", "ExamId:" + examid);
//                                // Delete Exam Which is Deleted From Server
//                                int Deleted_Exam_Id = 0;
//                                try {
//                                    Deleted_Exam_Id = Integer.parseInt(map.get(
//                                            ExamName).toString());
//                                } catch (Exception err) {
//                                }
//                                Constants.Logwrite("ExamListMarksheetActivity", "Deleted_Exam_Id:"
//                                        + Deleted_Exam_Id);
//                                if (Deleted_Exam_Id == 0) {
//                                    int To_Del_Exam = Integer.parseInt(examid);
//                                    int Exam_Delete_Status = db
//                                            .DeleteDuplicateStudentExmasMarksheet(
//                                                    Integer.parseInt(StudentId),
//                                                    Integer.parseInt(SchoolId),
//                                                    To_Del_Exam,
//                                                    Integer.parseInt(Year_Id));
//
//                                    Constants.Logwrite("ExamListMarksheetActivity",
//                                            "ExamDeleteStatus:" + To_Del_Exam);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception err) {
//            Constants.Logwrite("ExamListMarksheetActivity", "Message:" + err.getMessage()
//                    + "StactTrace:" + err.getStackTrace());
//        }
//        // return ItmNames;
//    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            menu.clear();
            mapacc = new HashMap<Integer, String>();
            menu.add(0, 1, 1, "Refresh").setTitle("Refresh");
            menu.add(0, 2, 2, "Add Account").setTitle("Add Account");
            // menu.add(0, 3, 3, "Remove Account").setTitle("Remove Account");
            menu.add(0, 4, 4, "Set Default Account").setTitle(
                    "Set Default Account");
            mapacc = Constants.AddAccount(menu, db);
            return true;
        } catch (Exception err) {
            Constants.Logwrite("ExamListMarksheetActivity:",
                    "onPrepareOptionsMenu:" + err.getMessage() + "StackTrace::"
                            + err.getStackTrace().toString());
            return false;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        try {
            int iid = item.getItemId();
            if (iid == android.R.id.home) {
                hideKeyboard(ExamListMarksheetActivity.this);
                ExamListMarksheetActivity.this.finish();
                onBackClickAnimation();
            } else if (iid == 1 || iid == 2 || iid == 3) {
                if (iid == 1) {
                    isref = 1;
                    // new MyTask().execute();
                    getAllExamMarkSheetListData();
                } else if (iid == 2) {
                    addAccountClick(ExamListMarksheetActivity.this);
                } else if (iid == 3) {
                    removeAccountClick(ExamListMarksheetActivity.this);
                } else {
                    accountListClick(ExamListMarksheetActivity.this);
                }
            } else {
                String details = mapacc.get(iid).toString();
                Constants.SetAccountDetails(details, ExamListMarksheetActivity.this);
                intent = new Intent(ExamListMarksheetActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
                onBackClickAnimation();
                return true;
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
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
                Intent intent = new Intent(ExamListMarksheetActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                onBackClickAnimation();
            }
        } catch (Exception err) {
            Constants.Logwrite("StudentInformationActivity:", "BackPressed:" + err.getMessage()
                    + "StackTrace::" + err.getStackTrace().toString());
        }
    }

    private void getAllExamMarkSheetListData() {
        try {
            if (isOnline()) {

                mProgressBar.setVisibility(View.VISIBLE);
                Call<ExamListModel> call = ((com.expedite.apps.kumkum.MyApplication)
                        getApplicationContext()).getmRetrofitInterfaceAppService().GetExamMarkSheetData(SchoolId, Year_Id,
                        StudentId, Constants.PLATFORM);
                call.enqueue(new Callback<ExamListModel>() {
                    @Override
                    public void onResponse(Call<ExamListModel> call, Response<ExamListModel> response) {
                        try {
                            ExamListModel tmps = response.body();
                            if (tmps != null)
                                if (tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                        && tmps.getResponse().equalsIgnoreCase("1")) {
                                    main_examListMarksheet.setVisibility(View.VISIBLE);
                                    empty_examListMarksheet.setVisibility(View.GONE);
                                    map = new HashMap<String, String>();
                                    if (tmps.getListArray() != null && tmps.getListArray().size() > 0) {
                                        mExamArrayList.clear();
                                        mExamArrayList.addAll(tmps.getListArray());
                                        main_examListMarksheet.setVisibility(View.VISIBLE);
                                        empty_examListMarksheet.setVisibility(View.GONE);
                                        try {
                                            ItmNames = new String[mExamArrayList.size()];
                                            Itmid = new String[mExamArrayList.size()];
                                            for (int i = 0; i < mExamArrayList.size(); i++) {
                                                map.put(mExamArrayList.get(i).getSecond(), mExamArrayList.get(i).getFirst());
                                                // Check ExamId Is There or Not
                                                Boolean IsExamInserted = db
                                                        .CheckMarksheetExamsInserted(
                                                                Integer.parseInt(StudentId),
                                                                Integer.parseInt(SchoolId),
                                                                Integer.parseInt(Year_Id),
                                                                Integer.parseInt(mExamArrayList.get(i).getFirst()));

                                                if (mExamArrayList.get(i).getThird() != null &&
                                                        !mExamArrayList.get(i).getThird().trim().isEmpty()) {
                                                    String[] parts = mExamArrayList.get(i).getThird().split("//");
                                                    Constants.DeletePdf(parts[parts.length - 1]);
                                                }
                                                int srno = i;
                                                try {
                                                    srno = Integer.parseInt(mExamArrayList.get(i).getSixth());
                                                } catch (Exception ex) {
                                                }
                                                if (IsExamInserted) {
                                                    if (mExamArrayList.get(i).getFirst() != null &&
                                                            !mExamArrayList.get(i).getFirst().trim().isEmpty() &&
                                                            mExamArrayList.get(i).getSecond() != null &&
                                                            !mExamArrayList.get(i).getSecond().trim().isEmpty() &&
                                                            mExamArrayList.get(i).getFourth() != null &&
                                                            !mExamArrayList.get(i).getFourth().trim().isEmpty() &&
                                                            mExamArrayList.get(i).getFifth() != null &&
                                                            !mExamArrayList.get(i).getFifth().trim().isEmpty()) {

                                                        db.UpdateExams(mExamArrayList.get(i).getFirst(),
                                                                mExamArrayList.get(i).getSecond(),
                                                                mExamArrayList.get(i).getThird(),
                                                                Year_Id, SchoolId, StudentId, "1",
                                                                mExamArrayList.get(i).getFourth(),
                                                                mExamArrayList.get(i).getFifth(), srno);
                                                    }
                                                } else {
                                                    if (mExamArrayList.get(i).getFirst() != null &&
                                                            !mExamArrayList.get(i).getFirst().trim().isEmpty() &&
                                                            mExamArrayList.get(i).getSecond() != null &&
                                                            !mExamArrayList.get(i).getSecond().trim().isEmpty() &&
                                                            mExamArrayList.get(i).getFourth() != null &&
                                                            !mExamArrayList.get(i).getFourth().trim().isEmpty() &&
                                                            mExamArrayList.get(i).getFifth() != null &&
                                                            !mExamArrayList.get(i).getFifth().trim().isEmpty()) {
                                                        db.AddExams(new Contact(Integer.parseInt(Year_Id),
                                                                Integer.parseInt(StudentId), Integer
                                                                .parseInt(SchoolId), Integer
                                                                .parseInt(mExamArrayList.get(i).getFirst()),
                                                                mExamArrayList.get(i).getSecond(),
                                                                "", 1,
                                                                mExamArrayList.get(i).getThird(),
                                                                mExamArrayList.get(i).getFourth(),
                                                                mExamArrayList.get(i).getFifth(), srno));
                                                    }
                                                }

                                            }
                                            if (isref == 1) {
                                                List<Contact> contacts = db.GetAllMarksheetExamRecords(
                                                        Integer.parseInt(SchoolId),
                                                        Integer.parseInt(StudentId),
                                                        Integer.parseInt(Year_Id), -2);
                                                String[] ExamMarkseets = new String[contacts.size()];
                                                Constants.Logwrite("ExamListMarksheetActivity", "Total Exam Length:"
                                                        + contacts.size());
                                                if (ExamMarkseets != null && ExamMarkseets.length > 0) {
                                                    for (Contact cn : contacts) {
                                                        String msg = cn.getGlobalText();
                                                        Constants.Logwrite("ExamListMarksheetActivity", "ExamString:" + msg);
                                                        String[] splitrstr = msg.split(",");
                                                        String ExamName = splitrstr[1];
                                                        Constants.Logwrite("ExamListMarksheetActivity", "ExamName:"
                                                                + ExamName);
                                                        String examid = splitrstr[0];
                                                        Constants.Logwrite("ExamListMarksheetActivity", "ExamId:" + examid);
                                                        // Delete Exam Which is Deleted From Server
                                                        int Deleted_Exam_Id = 0;
                                                        try {
                                                            Deleted_Exam_Id = Integer.parseInt(map.get(
                                                                    ExamName).toString());
                                                        } catch (Exception err) {
                                                        }
                                                        Constants.Logwrite("ExamListMarksheetActivity", "Deleted_Exam_Id:"
                                                                + Deleted_Exam_Id);
                                                        if (Deleted_Exam_Id == 0) {
                                                            int To_Del_Exam = Integer.parseInt(examid);
                                                            int Exam_Delete_Status = db
                                                                    .DeleteDuplicateStudentExmasMarksheet(
                                                                            Integer.parseInt(StudentId),
                                                                            Integer.parseInt(SchoolId),
                                                                            To_Del_Exam,
                                                                            Integer.parseInt(Year_Id));

                                                            Constants.Logwrite("ExamListMarksheetActivity",
                                                                    "ExamDeleteStatus:" + To_Del_Exam);
                                                        }
                                                    }
                                                }
                                            }

                                        } catch (Exception err) {
                                            Constants.writelog("ExamListMarksheetActivity", "Ex 576: Reqest:" + "MSG:" + err.getMessage());
                                        }

                                        getAllExamMarkSheetDataFromDataBase("", true);
                                    } else {
                                        main_examListMarksheet.setVisibility(View.GONE);
                                        empty_examListMarksheet.setVisibility(View.VISIBLE);
                                        Common.showToast(ExamListMarksheetActivity.this, SchoolDetails.MsgNoDataAvailable);
                                /*AlertDialog alertDialog = new AlertDialog.Builder(ExamListMarksheetActivity.this).create();
                                alertDialog.setTitle("Information");
                                alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                                alertDialog.setIcon(R.drawable.information);
                                alertDialog.setCancelable(false);
                                alertDialog.setButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(ExamListMarksheetActivity.this, HomeActivity.class);
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
                                    main_examListMarksheet.setVisibility(View.GONE);
                                    empty_examListMarksheet.setVisibility(View.VISIBLE);
                                    Common.showToast(ExamListMarksheetActivity.this, SchoolDetails.MsgNoDataAvailable);
                           /* final AlertDialog alertDialog = new AlertDialog.Builder(ExamListMarksheetActivity.this).create();
                            alertDialog.setTitle("Information");
                            alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                            alertDialog.setIcon(R.drawable.information);
                            alertDialog.setCancelable(false);
                            alertDialog.setButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            alertDialog.dismiss();
                                            Intent intent = new Intent(ExamListMarksheetActivity.this, HomeActivity.class);
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
                            Constants.writelog("ExamListMarksheetActivity", "Exception 520:" + ex.getMessage() +
                                    "::::" + ex.getStackTrace());
                        }
                    }

                    @Override
                    public void onFailure(Call<ExamListModel> call, Throwable t) {
                        mProgressBar.setVisibility(View.GONE);
                        Constants.writelog("ExamListMarksheetActivity", "Exception 528:" + t.getMessage());
                    }
                });
            } else {
                main_examListMarksheet.setVisibility(View.GONE);
                empty_examListMarksheet.setVisibility(View.VISIBLE);
                Toast.makeText(ExamListMarksheetActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception err) {
            Constants.writelog("ExamListMarksheetActivity", "Ex 716: Reqest:" + "MSG:" + err.getMessage());
        }
    }

    public void getAllExamMarkSheetDataFromDataBase(String mSelectedId, boolean IsFirstTime) {
        try {
            int cntr = 0;
            ArrayList<String> storedExams = new ArrayList<String>();
            List<Contact> contacts;
            if (mSelectedId != null && !mSelectedId.trim().isEmpty()) {
                contacts = db.GetAllMarksheetExamRecords(
                        Integer.parseInt(SchoolId),
                        Integer.parseInt(StudentId),
                        Integer.parseInt(Year_Id), Integer.parseInt(mSelectedId));
            } else {
                contacts = db.GetAllMarksheetExamRecords(
                        Integer.parseInt(SchoolId),
                        Integer.parseInt(StudentId),
                        Integer.parseInt(Year_Id), -2);
            }
            if (contacts != null && contacts.size() > 0) {
                main_examListMarksheet.setVisibility(View.VISIBLE);
                empty_examListMarksheet.setVisibility(View.GONE);
                int total = contacts.size();
                for (int i = 0; i < total; i++) {
                    String msg = contacts.get(i).getGlobalText();
                    String[] splitrstr = msg.split(",");
                    int examid = Integer.parseInt(splitrstr[0]);
                    if (storedExams.contains(examid + "")) {
                        int res = db.DeleteDuplicateStudentExmasMarksheet(Integer.parseInt(StudentId),
                                Integer.parseInt(SchoolId), examid, Integer.parseInt(Year_Id));
                        contacts.remove(i);
                        total--;
                        i--;
                    } else {
                        storedExams.add(examid + "");
                    }
                }
                ItmNames = new String[contacts.size()];
                Itmid = new String[contacts.size()];
                arrpath = new String[contacts.size()];
                if (IsFirstTime) {
                    mSpnCategoryIdArray.clear();
                    mSpnCategoryNameArray.clear();
                    mSpnCategoryIdArray.add("-2");
                    mSpnCategoryNameArray.add("All Marksheet");
                }
                for (Contact cn : contacts) {
                    String msg = cn.getGlobalText();
                    String[] splitrstr = msg.split(",");
                    String ExamName = splitrstr[1];
                    String examid = splitrstr[0];
                    ItmNames[cntr] = ExamName;
                    Itmid[cntr] = examid;
                    map.put(ExamName, examid);
                    arrpath[cntr] = splitrstr[2];
                    if (IsFirstTime) {
                        if (!mSpnCategoryNameArray.contains(cn.getCATEGORY_NAME())) {
                            if (cn.getCATEGORY_ID() != null && !cn.getCATEGORY_ID().isEmpty() &&
                                    cn.getCATEGORY_NAME() != null && !cn.getCATEGORY_NAME().isEmpty()) {
                                mSpnCategoryIdArray.add(cn.getCATEGORY_ID());
                                mSpnCategoryNameArray.add(cn.getCATEGORY_NAME());
                            }
                        }
                    }
                    cntr++;
                }
                if (IsFirstTime) {
                    if (mCategoryAdapter != null) {
                        mCategoryAdapter.notifyDataSetChanged();
                        mCategorySpinner.setSelection(0);
                    }
                }
                if (ItmNames != null && ItmNames.length > 0) {
                    main_examListMarksheet.setVisibility(View.VISIBLE);
                    empty_examListMarksheet.setVisibility(View.GONE);
                    ((View) findViewById(R.id.llSelectall)).setVisibility(View.VISIBLE);
                    mAdapter = new ExamListAllAdapter1(ExamListMarksheetActivity.this, ItmNames, arrpath, Itmid);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    main_examListMarksheet.setVisibility(View.GONE);
                    empty_examListMarksheet.setVisibility(View.VISIBLE);
                  ((View) findViewById(R.id.llSelectall)).setVisibility(View.GONE);
                    Common.showToast(this,SchoolDetails.MsgNoDataAvailable);
                   /*   AlertDialog alertDialog = new AlertDialog.Builder(ExamListMarksheetActivity.this).create();
                    alertDialog.setTitle("Information");
                    alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                    alertDialog.setIcon(R.drawable.information);
                    alertDialog.setCancelable(false);
                    alertDialog.setButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    onBackClickAnimation();
                                }
                            });
                    alertDialog.show();
                    return;*/
                }

            }else {
                main_examListMarksheet.setVisibility(View.GONE);
                empty_examListMarksheet.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
            Constants.writelog("ExamListMarksheetActivity", "init()353 Msg 394:" + ex.getMessage());
        }
    }
}
