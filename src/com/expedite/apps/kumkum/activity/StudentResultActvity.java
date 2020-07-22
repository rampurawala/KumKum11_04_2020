package com.expedite.apps.kumkum.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.adapter.ExamItemListAdapter;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.Contact;
import com.expedite.apps.kumkum.model.ExamListModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdView;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StudentResultActvity extends BaseActivity {
    private int isref = 0, examid = 0;
    private TextView tv;
    private ListView lstexammarks;
    private ExamItemListAdapter adp;
    private CardView barChartGraphCard;
    private String[] SubNames = null, SubMaxMarks = null;
    private DatabaseHandler db = null;
    private HashMap<String, String> map = new HashMap<String, String>();
    private HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
    private String SchoolId, StudentId, LastUpdatedTime;
    private ProgressBar mProgressBar;
    ArrayList<BarEntry> BARENTRY = new ArrayList<BarEntry>();
    ArrayList<String> BarEntryLabels = new ArrayList<String>();
    BarDataSet Bardataset;
    Boolean ISGraph = true, isDataExist = false, isServiceCalled = false, isTotal = true;
    BarChart barChart;
    String max = "";
    String isNotification="";
    private List<ExamListModel.ListArray> mExamArrayList = new ArrayList<>();
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initLayout() {
        lstexammarks.setNestedScrollingEnabled(false);
    }

    public void init() {

        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.StudentResultActvity);

            SchoolId = Datastorage.GetSchoolId(StudentResultActvity.this);
            StudentId = Datastorage.GetStudentId(StudentResultActvity.this);
            Intent i = getIntent();
           /* String Name=i.getStringExtra("examName");

            setTitle(Name);*/
            String exam_id = i.getStringExtra("ExamId");
            String exam_name = i.getStringExtra("examName");
            Constants.setActionbar(getSupportActionBar(), StudentResultActvity.this, StudentResultActvity.this,
                    exam_name, "StudentResultActvity", ActivityNames.StudentResultActvity);
            isNotification = i.getStringExtra("IsNotification");
            // resultView = (WebView)findViewById(R.id.webviewresult);
            db = new DatabaseHandler(StudentResultActvity.this);
            int Cnt_Count = 0;
            Cnt_Count = Datastorage.GetMultipleAccount(StudentResultActvity.this);
            if (Cnt_Count == 1) {
                setTitle("ExamMarks" + "-"
                        + Datastorage.GetStudentName(StudentResultActvity.this));
            }
            lstexammarks = (ListView) findViewById(R.id.lstexammarks);
            barChartGraphCard = (CardView) findViewById(R.id.barcard);
            barChart = (BarChart) findViewById(R.id.barchart);
            mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
            barChart.setFocusable(true);
            lstexammarks.setFocusable(false);

          /*  String exam_name = i.getStringExtra("examName");
            View customview = StudentResultActvity.this.getLayoutInflater().inflate(R.layout.mainactionbar, null);
            TextView label = (TextView) customview.findViewById(R.id.label);
            label.setText(exam_name);
            getSupportActionBar().setCustomView(customview);
*/
            tv = (TextView) findViewById(R.id.tvmarkqueetextattendancemonthlist);
            tv.setSelected(true);
            tv.setText(Datastorage.GetLastUpdatedtime(StudentResultActvity.this));

            examid = Integer.parseInt(exam_id);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                initLayout();
            }
            new MyTask().execute();
        } catch (Exception err) {
            Constants.Logwrite("viewResult", "error::454" + err.getMessage());
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            if (isref != 1) {
                getMarksFromLocal();
            } else {
                isref = 1;
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                LogUserVisted();
                if (!isDataExist) {
//                    getExamsMarks();
                    //Change by Tejas 24-07-2018
                    getExamMarksData();
                }
            } catch (Exception ex) {
                Constants.writelog("StudentResultActvity", "POST 160::" + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (isServiceCalled) {
                    if (Constants.isShowInternetMsg) {
                        Constants.NotifyNoInternet(StudentResultActvity.this);
                    } else {
                        getMarksFromLocal();
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                Constants.writelog("StudentResultActivity", "Ex 159:" + err.getMessage());
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    public void getMarksFromLocal() {
        try {
            List<Contact> contacts = db.GetAllExamMarks(Integer.parseInt(SchoolId), Integer.parseInt(StudentId),
                    Integer.parseInt(Datastorage.GetCurrentYearId(StudentResultActvity.this)), examid);
            String[] ItmNames = new String[contacts.size()];
            if (ItmNames != null && ItmNames.length > 0) {
                for (Contact cn : contacts) {
                    String msg = cn.getGlobalText();
                    if (msg != null && msg.length() > 0) {
                        String[] splitrstr = msg.split("####");
                        SubNames = new String[splitrstr.length];
                        SubMaxMarks = new String[splitrstr.length];
                        int n = 0;
                        BarEntryLabels.clear();
                        BARENTRY = new ArrayList<BarEntry>();
                        for (int j = 0; j < splitrstr.length; j++) {
                            String[] Splited_Exam_Marks = splitrstr[j].split(",");
                            isDataExist = true;
                            if (ISGraph) {
                                if (!Splited_Exam_Marks[0].equalsIgnoreCase(" ")) {
                                    //to remove total from graph
                                    if (Splited_Exam_Marks[0].equalsIgnoreCase("<center><b>Summary</b></center>")) {
                                        isTotal = false;
                                    } else if (isTotal) {
                                        ISGraph = true;
                                        String[] parts = Splited_Exam_Marks[1].split("/");
                                        try {
                                            if (parts.length>1 && parts[1].matches("[0-9.]+") && parts[0].matches("[0-9.]+")) {
                                                String label = "";
                                                if (Splited_Exam_Marks[0].length() > 5) {
                                                    label = Splited_Exam_Marks[0].substring(0, 4);
                                                } else {
                                                    label = Splited_Exam_Marks[0];
                                                }
                                                BarEntryLabels.add(label);
                                                BARENTRY.add(new BarEntry(Float.valueOf(parts[0]), n));
                                                n++;
                                            }
                                            if (max == null || max == "") {
                                                try {
                                                    max = parts[1];
                                                } catch (Exception ex) {
                                                }
                                            }
                                        } catch (Exception ex) {
                                        }
                                    }
                                    SubNames[j] = Splited_Exam_Marks[0];
                                    SubMaxMarks[j] = Splited_Exam_Marks[1];
                                } else {
                                    ISGraph = false;
                                    SubNames[j] = Splited_Exam_Marks[0];
                                    SubMaxMarks[j] = Splited_Exam_Marks[1];
                                }
                            } else {
                                SubNames[j] = Splited_Exam_Marks[0];
                                SubMaxMarks[j] = Splited_Exam_Marks[1];
                            }
                        }
                    }
                }
                if ((SubNames != null && SubNames.length > 0)
                        && (SubMaxMarks != null && SubMaxMarks.length > 0)) {
                    adp = new ExamItemListAdapter(StudentResultActvity.this,
                            SubNames, SubMaxMarks);
                    lstexammarks.setAdapter(adp);
                    if (BarEntryLabels != null && BarEntryLabels.size() > 1 && BarEntryLabels.size() == BARENTRY.size()) {
                        Bardataset = new BarDataSet(BARENTRY, "Marks out of " + max);
                        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                        BarData BARDATA = new BarData(BarEntryLabels, Bardataset);
                        barChart.setDescription("");
                        barChart.setData(BARDATA);
                        barChartGraphCard.setVisibility(View.VISIBLE);
                    } else {
                        barChartGraphCard.setVisibility(View.GONE);
                        barChart.setVisibility(View.GONE);
                    }
                } else {
                    if (isServiceCalled) {
                        AlertDialog alertDialog = new AlertDialog.Builder(StudentResultActvity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                        alertDialog.setIcon(R.drawable.alert);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        alertDialog.show();
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            Constants.writelog("getMarksFromLocal", "283::" + ex.getMessage());
        }
    }

    public void getExamsMarks() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_EXAM_MARK);
        request.addProperty("stud_id", StudentId);
        request.addProperty("schoolid", SchoolId);
        request.addProperty("exam_id", examid);
        request.addProperty("classid", Datastorage.GetClassId(StudentResultActvity.this));
        request.addProperty("yr_id", Integer.parseInt(Datastorage
                .GetCurrentYearId(StudentResultActvity.this)));

        try {
            isServiceCalled = true;
            SoapObject result = Constants.CallWebMethod(
                    StudentResultActvity.this, request, Constants.GET_EXAM_MARK, true);
            if (result != null && result.getPropertyCount() > 0) {
                SoapObject obj2 = (SoapObject) result.getProperty(0);
                if (obj2 != null) {
                    int count = obj2.getPropertyCount();
                    SubNames = new String[count];
                    SubMaxMarks = new String[count];
                    String[] myarray = new String[count];
                    String ExamMarksSave = "";
                    for (int i = 0; i < count; i++) {
                        myarray[i] = obj2.getProperty(i).toString();
                        ExamMarksSave += myarray[i].toString() + "####";
                        String[] info = myarray[i].split(",");
                        String subname = info[0].toString();
                        String submaxmarks = info[1].toString();
                        SubNames[i] = subname;
                        SubMaxMarks[i] = submaxmarks;

                    }
                    ExamMarksSave = ExamMarksSave.substring(0, ExamMarksSave.length() - 4);
                    db.UpdateExamTableSetMarks(SchoolId, StudentId, Datastorage
                            .GetCurrentYearId(StudentResultActvity.this), Integer
                            .toString(examid), ExamMarksSave);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Change by Tejas 24-07-2018
    private void getExamMarksData() {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<ExamListModel> call = ((com.expedite.apps.kumkum.MyApplication)
                getApplicationContext()).getmRetrofitInterfaceAppService().GetExamMarkData(StudentId, SchoolId,
                Integer.toString(examid),
                Datastorage.GetCurrentYearId(StudentResultActvity.this), Constants.PLATFORM);
        call.enqueue(new Callback<ExamListModel>() {
            @Override
            public void onResponse(Call<ExamListModel> call, Response<ExamListModel> response) {
                try {
                    ExamListModel tmps = response.body();
                    if (tmps != null)
                        if (tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1")) {
                            if (tmps.getListArray() != null && tmps.getListArray().size() > 0) {
                                mExamArrayList.clear();
                                mExamArrayList.addAll(tmps.getListArray());
                                try {
                                    isServiceCalled = true;
                                    SubNames = new String[mExamArrayList.size()];
                                    SubMaxMarks = new String[mExamArrayList.size()];
                                    String[] myarray = new String[mExamArrayList.size()];
                                    String ExamMarksSave = "";
                                    for (int i = 0; i < mExamArrayList.size(); i++) {
                                        myarray[i] = mExamArrayList.get(i).getFirst() + "," + mExamArrayList.get(i).getSecond();
                                        ExamMarksSave += myarray[i].toString() + "####";
                                        SubNames[i] = mExamArrayList.get(i).getFirst();
                                        SubMaxMarks[i] = mExamArrayList.get(i).getSecond();
                                    }
                                    ExamMarksSave = ExamMarksSave.substring(0, ExamMarksSave.length() - 4);
                                    db.UpdateExamTableSetMarks(SchoolId, StudentId, Datastorage
                                            .GetCurrentYearId(StudentResultActvity.this), Integer
                                            .toString(examid), ExamMarksSave);

                                    getMarksFromLocal();
                                } catch (Exception ex) {
                                    Constants.writelog("StudentResultActvity", "Exception 325:" + ex.getMessage() +
                                            "::::" + ex.getStackTrace());
                                }
                            }
                        }
                    mProgressBar.setVisibility(View.GONE);
                } catch (Exception ex) {
                    mProgressBar.setVisibility(View.GONE);
                    Constants.writelog("StudentResultActvity", "Exception 332:" + ex.getMessage() +
                            "::::" + ex.getStackTrace());
                }
            }

            @Override
            public void onFailure(Call<ExamListModel> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Constants.writelog("StudentResultActvity", "Exception 340:" + t.getMessage());
            }
        });
    }

    private void LogUserVisted() {

        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.LOG_USER_VISITED);
        request.addProperty("moduleid", 2);
        request.addProperty("schoolid", SchoolId);
        request.addProperty("User_Id", Datastorage.GetUserId(StudentResultActvity.this));
        request.addProperty("phoneno", Datastorage.GetPhoneNumber(StudentResultActvity.this));
        request.addProperty("pageid", 5);
        try {
            Constants.CallWebMethod(StudentResultActvity.this, request, Constants.LOG_USER_VISITED, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            menu.clear();
            menu.add(0, 1, 1, "Refresh").setTitle("Refresh");
            menu.add(0, 2, 2, "Add Account").setTitle("Add Account");
            //  menu.add(0, 3, 3, "Remove Account").setTitle("Remove Account");
            menu.add(0, 4, 4, "Set Default Account").setTitle("Set Default Account");
            mapacc = Constants.AddAccount(menu, db);
        } catch (Exception err) {
            Constants.Logwrite("StudentResultActvity", "MainPage:" + err.getMessage());
            return true;

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int iid = item.getItemId();
        if (iid == android.R.id.home) {
            hideKeyboard(StudentResultActvity.this);
            StudentResultActvity.this.finish();
            onBackClickAnimation();
        } else if (iid == 1 || iid == 2 || iid == 3 || iid == 4) {
            if (iid == 1) {
                isref = 1;
                isDataExist = false;
                new MyTask().execute();
            } else if (iid == 2) {
                addAccountClick(StudentResultActvity.this);
            } else if (iid == 3) {
                removeAccountClick(StudentResultActvity.this);
            } else {
                accountListClick(StudentResultActvity.this);
            }
        } else {
            String details = mapacc.get(iid).toString();
            Constants.SetAccountDetails(details, StudentResultActvity.this);
            intent = new Intent(StudentResultActvity.this, SplashActivity.class);
            startActivity(intent);
            finish();
            onBackClickAnimation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        Intent intent=null;
        if (isNotification != null && !isNotification.isEmpty()) {
            super.onBackPressed();
            intent = new Intent(StudentResultActvity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else {
            if (mProgressBar.isShown())
                mProgressBar.setVisibility(View.GONE);
            onBackClickAnimation();
            StudentResultActvity.this.finish();
        }

        onBackClickAnimation();
        super.onBackPressed();
    }

}
