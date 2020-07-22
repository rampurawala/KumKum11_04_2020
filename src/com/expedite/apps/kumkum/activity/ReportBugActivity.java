package com.expedite.apps.kumkum.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ReportBugActivity extends BaseActivity {
    private EditText txtSubject, txtDescription, mEdtEmail;
    private TextView btnSubmit;
    private String subject = "", description = "", mStrSelectedCategory = "", mStrEmail = "";
    private String res = "", DeviceDetails = "", deviceId = "", category = "";
    private String[] values, categoryname, categry;
    private Spinner group;
    private int categoryid, count;
    private String mIsFromHome = "";
    private ProgressBar mProgressBar;
    private List<String> mCategoryNameArray = new ArrayList<>(), mCategoryIdArray = new ArrayList<>();
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_bug);

        if (getIntent() != null && getIntent().getExtras() != null)
            mIsFromHome = getIntent().getExtras().getString("IsFromHome", "");
        init();
    }

    //Both messgae on this Activity changed soap to retrofit
    // Changes By Vijay Solanki 27-03-2020
    public void init() {
        mAdView = (AdView) findViewById(R.id.adView);
        showBannerAd(mAdView, ActivityNames.ReportBugActivity);

        Constants.setActionbar(getSupportActionBar(), ReportBugActivity.this, ReportBugActivity.this,
                "Send Query", "AppFeedback", ActivityNames.ReportBugActivity);
        mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        try {
            txtSubject = (EditText) findViewById(R.id.txtSubject);
            txtDescription = (EditText) findViewById(R.id.txtDescription);
            mEdtEmail = (EditText) findViewById(R.id.txtEmail);
            btnSubmit = (TextView) findViewById(R.id.btn_submit);
            group = (Spinner) findViewById(R.id.menu);

            group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        mStrSelectedCategory = mCategoryIdArray.get(position);
                    } catch (Exception e) {
                        Constants.writelog("ReportBugActivity 85 :", e.getMessage());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            //   new GetCategory().execute();
            getCategoryList();


            btnSubmit.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    if (isOnline()) {
                        try {


                            subject = txtSubject.getText().toString().trim();
                            description = txtDescription.getText().toString().trim();
                            mStrEmail = mEdtEmail.getText().toString().trim();

                            if (subject.equals("")) {
                                Toast.makeText(getApplicationContext(), "Please Enter Subject.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (description.equals("")) {
                                Toast.makeText(getApplicationContext(), "Please Enter Description.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (mStrEmail.equalsIgnoreCase("") || mStrEmail.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Please Enter Email.", Toast.LENGTH_LONG).show();
                                return;
                            }


                            Log.e("selected data", subject + " " + description + " " + mStrEmail + " " + mStrSelectedCategory);
                            getSubmitQuery();

                             /*for (int i = 0; i < categoryname.length + 1; i++) {
                                try {
                                    String name = group.getSelectedItem().toString();
                                    if (categoryname[i].equals(name)) {
                                        categoryid = Integer.parseInt(values[i]);
                                        break;
                                    }
                                } catch (Exception ex) {
                                    Constants.writelog("ReportBugActivity", "btn_onclick()105 Exception:"
                                            + ex.getMessage() + ":::::" + ex.getStackTrace());
                                }
                            }*/
                            /*Datastorage.SetLogSendComplete(getApplicationContext(), 0);
                            new MyTask().execute();

                            deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                            DeviceDetails = "Details: Mobile:"
                                    + Datastorage
                                    .GetPhoneNumber(getApplicationContext())
                                    + "||| Name:"
                                    + Datastorage
                                    .GetStudentName(getApplicationContext())
                                    + "|||"
                                    + Datastorage.GetGcmRegId(getApplicationContext())
                                    + "||||" + Build.DEVICE + "|||" + Build.MODEL
                                    + "|||" + Build.PRODUCT + "|||" + Build.VERSION.SDK
                                    + "|||" + Build.VERSION.RELEASE;
                            Constants.MyTaskSendLog SendLog = new Constants.MyTaskSendLog();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                SendLog.executeOnExecutor(
                                        AsyncTask.THREAD_POOL_EXECUTOR,
                                        ReportBugActivity.this, DeviceDetails,
                                        deviceId, 1000);
                            } else {
                                SendLog.execute(getApplicationContext(), DeviceDetails,
                                        1000);
                            }*/
                        } catch (Exception ex) {
                            Toast.makeText(ReportBugActivity.this, "Please Try Again..", Toast.LENGTH_SHORT).show();
                            Constants.writelog("ReportBugActivity", "OnCreate()118 Exception:"
                                    + ex.getMessage() + ":::::" + ex.getStackTrace());
                        }
                    } else {
                        Toast.makeText(ReportBugActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception ex) {
            Constants.writelog("ReportBugActivity", "OnCreate()75 Exception:"
                    + ex.getMessage() + ":::::" + ex.getStackTrace());
        }
    }


    private void getCategoryList() {
        try {
            if (isOnline()) {
                try {
                    String mStudentId = Datastorage.GetStudentId(getApplicationContext());
                    String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
                    String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
                    mProgressBar.setVisibility(View.VISIBLE);
                    Call<AppService> mCall = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                            .getCategory(mStudentId, mSchoolId, mYearId, Constants.PLATFORM);
                    mCall.enqueue(new retrofit2.Callback<AppService>() {
                        @Override
                        public void onResponse(Call<AppService> call, Response<AppService> response) {
                            AppService tmp = response.body();

                            if (tmp.getStrResult() != null && !tmp.getStrResult().equalsIgnoreCase("")) {
                                mEdtEmail.setText(tmp.getStrResult());
                            }

                            if (tmp.getStrlist() != null && tmp.getStrlist().size() > 0) {
                                List<AppService.ListArray> list = tmp.getStrlist();
                                if (list != null) {
                                    for (int i = 0; i < list.size(); i++) {
                                        mCategoryIdArray.add(list.get(i).getFirst());
                                        mCategoryNameArray.add(list.get(i).getSecond());
                                    }
                                }
                                ArrayAdapter<String> strings = new ArrayAdapter<String>(ReportBugActivity.this,
                                        android.R.layout.simple_spinner_item, mCategoryNameArray);
                                strings.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                group.setAdapter(strings);
                            } else {
                                Common.showToast(ReportBugActivity.this, tmp.getResponse());
                            }
                            mProgressBar.setVisibility(View.GONE);


                        }

                        @Override
                        public void onFailure(Call<AppService> call, Throwable t) {
                            mProgressBar.setVisibility(View.GONE);
                            Constants.writelog("getCategoryList:84:ErrorMsg::", "onFailure()");
                        }
                    });

                } catch (Exception ex) {
                    mProgressBar.setVisibility(View.GONE);
                    Constants.writelog("getCategoryList:89:ErrorMsg::", ex.getMessage());
                }
            } else {
                Constants.NotifyNoInternet(ReportBugActivity.this);
            }
        } catch (Exception ex) {
            mProgressBar.setVisibility(View.GONE);
            Constants.writelog("getCategoryList:93:ErrorMsg::", ex.getMessage());
        }
    }

    private void getSubmitQuery() {
        try {
            if (isOnline()) {
                try {
                    String mStudentId = Datastorage.GetStudentId(getApplicationContext());
                    String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
                    String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
                    mProgressBar.setVisibility(View.VISIBLE);
                    deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    DeviceDetails = "Details: Mobile:"
                            + Datastorage
                            .GetPhoneNumber(getApplicationContext())
                            + "||| Name:"
                            + Datastorage
                            .GetStudentName(getApplicationContext())
                            + "|||"
                            + Datastorage.GetGcmRegId(getApplicationContext())
                            + "||||" + Build.DEVICE + "|||" + Build.MODEL
                            + "|||" + Build.PRODUCT + "|||" + Build.VERSION.SDK
                            + "|||" + Build.VERSION.RELEASE;
                    PackageInfo pInfo = getPackageManager().getPackageInfo(
                            getPackageName(), 0);
                    Call<AppService> mCall = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                            .getSubmitQuery(mStudentId, mSchoolId, deviceId, DeviceDetails, String.valueOf(SchoolDetails.appname), subject,
                                    pInfo.versionName.toString(), description, mYearId, mStrSelectedCategory, mStrEmail, Constants.PLATFORM);
                    mCall.enqueue(new retrofit2.Callback<AppService>() {
                        @Override
                        public void onResponse(Call<AppService> call, Response<AppService> response) {
                            AppService tmp = response.body();

                            if (tmp.getStrResult() != null && tmp.getStrResult().equalsIgnoreCase("true")) {
                                Toast.makeText(getApplicationContext(), "Successfully Send.", Toast.LENGTH_LONG).show();
                                ReportBugActivity.this.finish();
                                onBackClickAnimation();
                                hideKeyboard(ReportBugActivity.this);
                            } else {
                                Toast.makeText(getApplicationContext(), tmp.getStrResult(), Toast.LENGTH_LONG).show();
                            }

                            mProgressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<AppService> call, Throwable t) {
                            mProgressBar.setVisibility(View.GONE);
                            Constants.writelog("getCategoryList:84:ErrorMsg::", "onFailure()");
                        }
                    });

                } catch (Exception ex) {
                    mProgressBar.setVisibility(View.GONE);
                    Constants.writelog("getCategoryList:89:ErrorMsg::", ex.getMessage());
                }
            } else {
                Constants.NotifyNoInternet(ReportBugActivity.this);
            }
        } catch (Exception ex) {
            mProgressBar.setVisibility(View.GONE);
            Constants.writelog("getCategoryList:93:ErrorMsg::", ex.getMessage());
        }
    }


    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            res = SendData(subject, description);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(getApplicationContext());
                } else {
                    if (res.equalsIgnoreCase("true")) {
//                        Intent intent = new Intent(ReportBugActivity.this, SuccessfullyActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        startActivity(intent);
//                        finish();
                        Toast.makeText(getApplicationContext(), "Successfully Send.", Toast.LENGTH_LONG).show();
                        ReportBugActivity.this.finish();
                        onBackClickAnimation();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Try Again.", Toast.LENGTH_LONG).show();
                    }
                }
                mProgressBar.setVisibility(View.GONE);

            } catch (Exception err) {
                Constants.writelog("ReportBugActivity", "Error :123 " + err.getMessage());
                Intent in = new Intent(ReportBugActivity.this, HomeActivity.class);
                startActivity(in);
                finish();
                onBackClickAnimation();
            }
        }
    }

    private String SendData(String subject, String description) {
        try {
            SoapObject request = new SoapObject(Constants.NAMESPACE,
                    Constants.METHOD_NAME_REPORT_APP_BUG);
            int studId = Integer.parseInt(Datastorage
                    .GetStudentId(getApplicationContext()));
            int schoolId = Integer.parseInt(Datastorage
                    .GetSchoolId(getApplicationContext()));
            int yearid = Integer.parseInt(Datastorage
                    .GetCurrentYearId(getApplicationContext()));
            PackageInfo pInfo = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            request.addProperty("studentid", studId);
            request.addProperty("schoolid", schoolId);
            request.addProperty("DeviceId", deviceId);
            request.addProperty("DeviceDetails", DeviceDetails);
            request.addProperty("yearid", yearid);
            request.addProperty("appname", SchoolDetails.appname);
            request.addProperty("subject", subject);
            request.addProperty("appVirson", pInfo.versionName.toString());
            request.addProperty("Description", description);
            request.addProperty("category", mStrSelectedCategory);
            SoapObject result = Constants.CallWebMethod(
                    ReportBugActivity.this, request,
                    Constants.METHOD_NAME_REPORT_APP_BUG, true);
            if (result != null && result.getPropertyCount() > 0) {
                res = result.getProperty(0).toString();
            } else {
                Constants.Logwrite("ReportBugActivity",
                        "Error: SendData()215 No responce from server.");
                Constants.writelog("ReportBugActivity",
                        "Error: SendData()215 No responce from server.");
            }
        } catch (Exception err) {
            Constants.writelog("ReportBugActivity:", "SendData()221 Exception:"
                    + err.getMessage() + "StackTrace::"
                    + err.getStackTrace().toString());
            Intent in = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(in);
            finish();
        }
        return res;
    }

    public class GetCategory extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            category = GetcategoryData();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(getApplicationContext());
                } else {
                    if (!category.equalsIgnoreCase(null)) {
                        ArrayAdapter<String> strings = new ArrayAdapter<String>(ReportBugActivity.this,
                                android.R.layout.simple_spinner_item, categoryname);
                        strings.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        group.setAdapter(strings);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please Try Again.", Toast.LENGTH_SHORT).show();
                    }
                }
                mProgressBar.setVisibility(View.GONE);

            } catch (Exception err) {
                Constants.writelog("ReportBugActivity",
                        "GetCategory()_Error :183 " + err.getMessage());
                ReportBugActivity.this.finish();
                onBackClickAnimation();
            }
        }
    }

    private String GetcategoryData() {
        try {
            SoapObject request = new SoapObject(Constants.NAMESPACE,
                    Constants.METHOD_NAME_GET_FEEdBACK_CATEGORY);
            int studId = Integer.parseInt(Datastorage
                    .GetStudentId(getApplicationContext()));
            int schoolId = Integer.parseInt(Datastorage
                    .GetSchoolId(getApplicationContext()));
            int yearid = Integer.parseInt(Datastorage
                    .GetCurrentYearId(getApplicationContext()));
            request.addProperty("studentid", studId);
            request.addProperty("schoolid", schoolId);
            request.addProperty("yearid", yearid);
            request.addProperty("platform", Constants.PLATFORM);
            SoapObject result = Constants.CallWebMethod(
                    ReportBugActivity.this, request,
                    Constants.METHOD_NAME_GET_FEEdBACK_CATEGORY, true);
            if (result != null && result.getPropertyCount() > 0) {
                category = result.getProperty(0).toString();
                SoapObject obj2 = (SoapObject) result.getProperty(0);
                if (obj2 != null) {
                    count = obj2.getPropertyCount();
                    values = new String[count];
                    categry = new String[count];
                    categoryname = new String[count];
                    //  String[] msgitem = new String[10];
                    String[] myarray = new String[count];
                    try {
                        for (int i = 0; i < count; i++) {
                            myarray[i] = obj2.getProperty(i).toString();
                            categry[i] = myarray[i];
                            String[] msgitem = myarray[i].split(",");
                            values[i] = msgitem[0];
                            categoryname[i] = msgitem[1];
                        }
                    } catch (Exception ex) {
                        Constants.writelog("ReportBugActivity",
                                "GetcategoryData()_Error :193 " + ex.getMessage());
                    }
                }
                //  categoryname=category.split(",");
            } else {
                Constants.Logwrite("ReportBugActivity",
                        "Error: GetcategoryData()255 No responce from server.");
                Constants.writelog("ReportBugActivity",
                        "Error: GetcategoryData()255 No responce from server.");
            }
        } catch (Exception err) {
            Constants.writelog("ReportBugActivity:", "GetcategoryData()321 Exception:"
                    + err.getMessage() + "StackTrace::"
                    + err.getStackTrace().toString());
            Intent in = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(in);
            finish();
        }

        return category;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            MenuInflater objmenuinfl = getMenuInflater();
            objmenuinfl.inflate(R.menu.activity_options, menu);
         /*   menu.add(0, 1, 1, "Add Account").setTitle("Add Account");
            menu.add(0, 2, 2, "Remove Account").setTitle("Remove Account");
            menu.add(0, 3, 3, "Set Default Account").setTitle("Set Default Account");
            menu.add(0, 4, 4, "History").setTitle("History");
            db = new DatabaseHandler(this);
            mapacc = Constants.AddAccount(menu, db);*/

        } catch (Exception err) {
            Constants.Logwrite("ReportBugActivity", "ReportBugActivity:" + err.getMessage());
            return true;

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        //  int iid = item.getItemId();
        switch (item.getItemId()) {

            case R.id.history_type:
                intent = new Intent(getApplicationContext(),
                        Feedback_History.class);
                startActivity(intent);
                onClickAnimation();
                break;

            case android.R.id.home:
                hideKeyboard(ReportBugActivity.this);
                ReportBugActivity.this.finish();
                onBackClickAnimation();
                break;

            default:
                return super.onOptionsItemSelected(item);
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
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        } catch (Exception err) {
            Constants.writelog("ReportBugActivity:",
                    "OnBackPressed()625 Exception:" + err.getMessage()
                            + "StackTrace::" + err.getStackTrace().toString());
        }
    }
}
