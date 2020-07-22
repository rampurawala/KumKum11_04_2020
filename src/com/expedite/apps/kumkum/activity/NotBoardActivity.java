package com.expedite.apps.kumkum.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.adapter.CircularListAdapter;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.google.android.gms.ads.AdView;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotBoardActivity extends BaseActivity {
    private ListView ItmView;
    private CircularListAdapter adp;
    private HashMap<String, String> map = new HashMap<String, String>(), map1 = new HashMap<String, String>();
    private String[] ItmNames = null, ItmNames1 = null, test = null;
    private DatabaseHandler db = new DatabaseHandler(this);
    private HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
    private String SchoolId, StudentId, Year_Id;
    private SoapObject obj2;
    private ProgressBar mProgressBar;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_board);
        init();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);

            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            SchoolId = Datastorage.GetSchoolId(NotBoardActivity.this);
            StudentId = Datastorage.GetStudentId(NotBoardActivity.this);
            Year_Id = Datastorage.GetCurrentYearId(NotBoardActivity.this);
            String title = "",mActivityName="";
            int Cnt_Count = 0;
            Cnt_Count = Datastorage.GetMultipleAccount(NotBoardActivity.this);
            if (Cnt_Count == 1) {
                title = "Notice Board" + "-"
                        + Datastorage.GetStudentName(NotBoardActivity.this);
            } else {
                title = "Notice Board";
            }
            mActivityName = ActivityNames.NotBoardActivity;
            showBannerAd(mAdView, ActivityNames.NotBoardActivity);
            Constants.setActionbar(getSupportActionBar(), NotBoardActivity.this, NotBoardActivity.this,
                    title, "Circular",mActivityName);
            ItmView = (ListView) findViewById(R.id.lstcirlist);
            new MyTask().execute();
            ItmView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    try {
                        String gupname = ItmNames[arg2].toString();
                        int count;
                        count = obj2.getPropertyCount();

                        // output = new String[count];
                        String[] myarray = new String[count];
                        List<String> list = new ArrayList<String>();
                        for (int i = 0; i < count; i++) {
                            myarray[i] = obj2.getProperty(i).toString();
                            String info[] = myarray[i].split("@@##");
                            String crname = null;
                            crname = info[2].toString();

                            if (crname.equals(gupname)) {
                                list.add(info[0].toString() + "@@##"
                                        + info[1].toString());
                            }
                        }
                        ItmNames1 = new String[list.size()];
                        for (int j = 0; j < list.size(); j++) {
                            map1.put("", list.get(j));
                            ItmNames1[j] = list.get(j);
                        }

                        Intent intent = new Intent(NotBoardActivity.this,
                                NoticeActivity.class);
                        intent.putExtra("circular detail", test);
                        intent.putExtra("group name", ItmNames[arg2].toString());
                        intent.putExtra("group name1", ItmNames1);
                        startActivity(intent);

                    } catch (Exception err) {
                        Constants.writelog("NotBoardActivity",
                                "Exception: 146" + err.getMessage() + ":::::"
                                        + err.getStackTrace());
                    }
                }
            });
        } catch (Exception err) {
            Constants.Logwrite("NotBoardActivity", "Exception: 155" + err.getMessage() + ":::::"
                    + err.getStackTrace());
            Constants.writelog(
                    "NotBoardActivity",
                    "Exception: " + err.getMessage() + ":::::"
                            + err.getStackTrace());
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // LogUserVisted();
            GetCircularForStudent();
            // GetCircularForStudent1();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(NotBoardActivity.this);
                } else {
                    if (ItmNames != null && ItmNames.length > 0) {
                        adp = new CircularListAdapter(
                                NotBoardActivity.this, ItmNames);
                        ItmView.setAdapter(adp);
                    } else {
                        // alert.showAlertDialog(ExamListMarksheetActivity.this,
                        // "Information",
                        // "No Marksheet Exams are present for selected student",
                        // false);
                        // dialog.setCanceledOnTouchOutside(false);
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                NotBoardActivity.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Information");

                        // Setting Dialog Message
                        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.information);

                        alertDialog.setCancelable(false);

                        // Setting OK Button
                        alertDialog.setButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(
                                                NotBoardActivity.this,
                                                HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                        onBackClickAnimation();
                                    }
                                });

                        // Showing Alert Message
                        mProgressBar.setVisibility(View.GONE);
                        alertDialog.show();
                        return;
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {

            }
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void GetCircularForStudent() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_CIRCULAR_STUDENT_GROUPWISE);
        request.addProperty("SchoolId", Integer.parseInt(SchoolId));
        request.addProperty("StudId", Integer.parseInt(StudentId));
        request.addProperty("Year_Id", Integer.parseInt(Year_Id));
        try {
            SoapObject result = Constants.CallWebMethod(NotBoardActivity.this, request,
                    Constants.GET_CIRCULAR_STUDENT_GROUPWISE, true);
            if (result != null && result.getPropertyCount() > 0) {
                obj2 = (SoapObject) result.getProperty(0);
                if (obj2 != null) {
                    int count;
                    count = obj2.getPropertyCount(); // test.length;
                    ItmNames = new String[count];
                    String[] myarray = new String[count];
                    List<String> list = new ArrayList<String>();
                    for (int i = 0; i < count; i++) {
                        myarray[i] = obj2.getProperty(i).toString();
                        String info[] = myarray[i].split("@@##");
                        String crid = null;
                        crid = info[2].toString();
                        if (!list.contains(crid)) {
                            list.add(crid);
                        }
                    }
                    ItmNames = new String[list.size()];
                    for (int j = 0; j < list.size(); j++) {
                        map.put("", list.get(j));
                        ItmNames[j] = list.get(j);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            menu.add(0, 1, 1, "Add Account").setTitle("Add Account");
            //  menu.add(0, 2, 2, "Remove Account").setTitle("Remove Account");
            menu.add(0, 3, 3, "Set Default Account").setTitle("Set Default Account");

            mapacc = Constants.AddAccount(menu, db);

            return true;
        } catch (Exception err) {
            Constants.Logwrite("NotBoardActivity", "MainPage:" + err.getMessage());
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int iid = item.getItemId();
        if (iid == android.R.id.home) {
            hideKeyboard(NotBoardActivity.this);
            NotBoardActivity.this.finish();
            onBackClickAnimation();
        } else if (iid == 1 || iid == 2 || iid == 3) {
            if (iid == 1) {
                addAccountClick(NotBoardActivity.this);
            } else if (iid == 2) {
                removeAccountClick(NotBoardActivity.this);
            } else {
                accountListClick(NotBoardActivity.this);
            }
        } else {
            String details = mapacc.get(iid).toString();
            Constants.SetAccountDetails(details, NotBoardActivity.this);
            intent = new Intent(NotBoardActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
            onBackClickAnimation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(NotBoardActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            onBackClickAnimation();
        } catch (Exception err) {

        }
    }
}
