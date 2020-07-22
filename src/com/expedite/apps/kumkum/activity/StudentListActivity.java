package com.expedite.apps.kumkum.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.adapter.StudentCustomListAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.StudListitemDetails;
import com.google.android.gms.ads.AdView;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentListActivity extends BaseActivity {
    private ListView lv;
    private EditText searchname;
    private TextView tv;
    private String[] StudName = {};
    private int[] StudIds = {};
    private StudListitemDetails item_details;
    private ArrayList<StudListitemDetails> results_search = new ArrayList<StudListitemDetails>(), result1 = new ArrayList<StudListitemDetails>();
    private int textlength = 0;
    private StudentCustomListAdapter adp;
    private HashMap<String, Integer> map = new HashMap<String, Integer>();
    private HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
    private String SchoolId,  LastUpdatedTime, Year_Id;
    private ProgressBar mProgressBar;
    private String mIsFromProfile = "";
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        if (getIntent() != null && getIntent().getExtras() != null)
            mIsFromProfile = getIntent().getExtras().getString("IsFromProfile");

        init();
    }

    public void init() {
        Constants.setActionbar(getSupportActionBar(), StudentListActivity.this, StudentListActivity.this,
                "School Mates ", "School Mates", ActivityNames.StudentListActivity);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.StudentListActivity);
            SchoolId = Datastorage.GetSchoolId(StudentListActivity.this);
            LastUpdatedTime = Datastorage.GetLastUpdatedtime(StudentListActivity.this);
            Year_Id = Datastorage.GetCurrentYearId(StudentListActivity.this);
            db = new DatabaseHandler(StudentListActivity.this);
            // Check if Internet present
            lv = (ListView) findViewById(R.id.lstmessages);
            searchname = (EditText) findViewById(R.id.edtsearchname);
            new MyTask().execute();
            searchname.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        textlength = searchname.getText().length();
                        if (textlength > 0) {
                            results_search.clear();
                            if (StudName != null && StudName.length > 0) {
                                for (int i = 0; i < StudName.length; i++) {
                                    if (textlength <= StudName[i].length()) {
                                        if (StudName[i].toLowerCase().contains(
                                                searchname.getText().toString()
                                                        .toLowerCase().trim())) {
                                            item_details = new StudListitemDetails();
                                            item_details.setName(StudName[i]);
                                            // item_details.setImagePath(StudImagePath[i]);
                                            results_search.add(item_details);
                                        }
                                    }
                                }
                            }
                            // ArrayList<StudListitemDetails> result1 =
                            // GetSearchResults();
                            adp = new StudentCustomListAdapter(results_search, StudentListActivity.this);
                            lv.setAdapter(adp);
                        } else {
                            adp = new StudentCustomListAdapter(result1, StudentListActivity.this);
                            lv.setAdapter(adp);
                        }
                    } catch (Exception ex) {
                        Constants.Logwrite("studentList", "onTextChanged()105 Error::" + ex.getMessage());
                    }
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void afterTextChanged(Editable s) {}

            });

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    String item = adp.getItemName(arg2).toString();
                    String studid = map.get(item).toString();
                    Intent intent = new Intent(StudentListActivity.this, SchoolMatesListActivity.class);
                    intent.putExtra("StudId", studid);
                    startActivity(intent);
                    onClickAnimation();

                }
            });
            tv = (TextView) findViewById(R.id.tvmarkquetext);
            tv.setSelected(true);
            tv.setText(LastUpdatedTime);
        } catch (Exception err) {
            Constants.Logwrite("studentList", "Error::" + err.getMessage());
        }
    }

    private ArrayList<StudListitemDetails> GetSearchResults() {
        ArrayList<StudListitemDetails> results = new ArrayList<StudListitemDetails>();

        for (int i = 0; i < StudName.length; i++) {
            item_details = new StudListitemDetails();
            item_details.setName(StudName[i]);
            // item_details.setImagePath(StudImagePath[i]);
            // item_details.setstudid(StudIds[i]);
            if (StudName[i].trim().equalsIgnoreCase("")) {
            } else {
                results.add(item_details);
            }

        }

        return results;
    }

    public void GetSchoolMatesList() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_SCHOOL_MATE_LIST);
        request.addProperty("SchoolId", SchoolId);
        try {
            SoapObject result = Constants.CallWebMethod(
                    StudentListActivity.this, request, Constants.GET_SCHOOL_MATE_LIST, true);
            if (result != null && result.getPropertyCount() > 0) {
                SoapObject obj2 = (SoapObject) result.getProperty(0);
                String[] output = null;
                if (obj2 != null) {
                    int count = obj2.getPropertyCount();
                    output = new String[count];
                    StudName = new String[count];
                    StudIds = new int[count];
                    for (int i = 0; i < count; i++) {

                        output[i] = obj2.getProperty(i).toString();
                        // messages = output[i].split(",");

                        String[] parts = output[i].split(",");
                        StudName[i] = parts[0].toString();
                        StudIds[i] = Integer.parseInt(parts[1].toString());
                        // String Stud_Image_Path = parts[0].toString();
                        // String image_url =
                        // "http://testm.vayuna.com/SURE_Upload_Data/635181959962230000_Sem1_KumKum_Primary_Gujarati_Noon/Images/IX_A_AMIN_NAMIRA_MUSTAKBHAI634786380118080000.png";
                        // StudImagePath[i] = image_url;
                        map.put(parts[0].toString(),
                                Integer.parseInt(parts[1].toString()));
                    }
                    // return StudName;
                }
                // return StudName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return StudName;
    }

    private void LogUserVisted() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.LOG_USER_VISITED);
        request.addProperty("moduleid", 4);
        request.addProperty("schoolid", SchoolId);
        request.addProperty("User_Id", Datastorage.GetUserId(StudentListActivity.this));
        request.addProperty("phoneno", Datastorage.GetPhoneNumber(StudentListActivity.this));
        request.addProperty("pageid", 10);
        try {
            Constants.CallWebMethod(StudentListActivity.this, request, Constants.LOG_USER_VISITED, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            menu.clear();
            mapacc = new HashMap<Integer, String>();
            menu.add(0, 1, 1, "Add Account").setTitle("Add Account");
           // menu.add(0, 2, 2, "Remove Account").setTitle("Remove Account");
            menu.add(0, 3, 3, "Set Default Account").setTitle("Set Default Account");
            mapacc = Constants.AddAccount(menu, db);
        } catch (Exception err) {
            Constants.Logwrite("StudentListActivity", "MainPage:" + err.getMessage());
            return true;
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int iid = item.getItemId();
        if (iid == android.R.id.home) {
            try {
                hideKeyboard(StudentListActivity.this);
                StudentListActivity.this.finish();
                Intent i = new Intent(StudentListActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();
                onBackClickAnimation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (iid == 1 || iid == 2 || iid == 3) {
            if (iid == 1) {
                addAccountClick(StudentListActivity.this);
            } else if (iid == 2) {
                removeAccountClick(StudentListActivity.this);
            } else {
                accountListClick(StudentListActivity.this);
            }
        } else {

            String details = mapacc.get(iid).toString();
            Constants.SetAccountDetails(details, StudentListActivity.this);
            if(isOnline()) {
                intent = new Intent(StudentListActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
                onBackClickAnimation();
            }else {
                Toast.makeText(this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            LogUserVisted();
            GetSchoolMatesList();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(StudentListActivity.this);
                } else {
                    if (StudName != null && StudName.length > 0) {
                        result1 = GetSearchResults();
                        adp = new StudentCustomListAdapter(result1,
                                StudentListActivity.this);
                        lv.setAdapter(adp);
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(StudentListActivity.this).create();
                        alertDialog.setTitle("Connection Problem");
                        alertDialog.setMessage("OOPS!!It looks like a connection problem.Please try again.");
                        alertDialog.setIcon(R.drawable.alert);
                        alertDialog.setButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(StudentListActivity.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                        onBackClickAnimation();
                                    }
                                });

                        // Showing Alert Message
                        alertDialog.show();
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                Common.printStackTrace(err);
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (mIsFromProfile != null && !mIsFromProfile.isEmpty()) {
            Intent intent = new Intent(StudentListActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
            onBackClickAnimation();
        } else {
            try {
                Intent intent = new Intent(StudentListActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            } catch (Exception err) {
                Constants.Logwrite("StudentInformationActivity:", "BackPressed:" + err.getMessage()
                        + "StackTrace::" + err.getStackTrace().toString());
            }
        }
    }
}
