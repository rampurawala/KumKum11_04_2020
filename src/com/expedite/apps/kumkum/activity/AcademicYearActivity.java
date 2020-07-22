package com.expedite.apps.kumkum.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.adapter.ExamItemsAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.Contact;
import com.google.android.gms.ads.AdView;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

public class AcademicYearActivity extends BaseActivity {
    private HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
    private int Is_Ref = 0;
    private String[] ItmNames = null;
    private int[] yearsid = null;
    private ListView ItmView;
    private ExamItemsAdapter adapter;
    private String Sel_Year_Id = "", Academic_Year_Start_Date = "";
    private String Academic_Year_End_Date = "", Holiday_Start_Date = "", Holiday_End_Date = "", classid = "";
    private String classsecid = "", classname = "", info = "";
    private HashMap<String, Integer> map = new HashMap<String, Integer>();
    private DatabaseHandler db = new DatabaseHandler(this);
    private String StudId, SchoolId, PhoneNumber, LogPin;
    private String SelectedYear_Id = "", SelectedYearText = "";
    private ProgressBar mProgressBar;
    private AdView mAdView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_year);
        init();
    }

    public void init() {
        mAdView = (AdView) findViewById(R.id.adView);
        showBannerAd(mAdView, ActivityNames.AcademicActity);
        Constants.setActionbar(getSupportActionBar(), AcademicYearActivity.this,
                AcademicYearActivity.this,
                "Set Default Year", "SetDefaultYear", ActivityNames.AcademicActity);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        try {
            StudId = Datastorage.GetStudentId(AcademicYearActivity.this);
            SchoolId = Datastorage.GetSchoolId(AcademicYearActivity.this);
            PhoneNumber = Datastorage.GetPhoneNumber(AcademicYearActivity.this);
            LogPin = Datastorage.GetLoginPin(AcademicYearActivity.this);
            new MyTask().execute();
        } catch (Exception err) {
            Constants.writelog("AcademicYearActivity", "SetDefaultYearId(182):" + err.getMessage());
        }
    }

    private void ListclickEvent() {
        ItmView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (!isOnline()) {
                    Toast.makeText(AcademicYearActivity.this,
                            SchoolDetails.MsgNoInternet, Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                SelectedYear_Id = map.get(ItmNames[arg2].toString()).toString();
                SelectedYearText = ItmNames[arg2].toString();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AcademicYearActivity.this);
                alertDialog.setTitle("Information");
                alertDialog.setMessage("Do you really want to make AcademicYearActivity as Default?");
                alertDialog.setIcon(R.drawable.information);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Constants.writelog("AcademicYearActivity",
                                        "SetDefaultYearId(162):" + SelectedYear_Id
                                                + " SetDefaultYear:"
                                                + SelectedYearText);
                                // User pressed YES button. Write Logic Here
                                new MyTaskChangeYear().execute();
                            }
                        });

                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub

                            }
                        });
                alertDialog.show();
            }
        });
    }

    private class MyTaskChangeYear extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (LogPin == null || LogPin.isEmpty()) {
                try {
                    Cursor cn = db.getStudentDetails(Integer.parseInt(StudId),
                            Integer.parseInt(SchoolId));
                    cn.moveToFirst();
                    LogPin = cn.getString(cn.getColumnIndex(DatabaseHandler.KEY_LOG_PIN));
                    cn.close();
                } catch (Exception ex) {
                    Constants.Logwrite("AcademicYearActivity", "Exception:" + ex.getMessage());
                }
            }
            int success = db.UpdateAcademicYearSetIsCurrent_Zero_to_One(SchoolId, StudId);
            int Set_Default = db.UpdateAcademicYearSetDefaultOne(
                    SelectedYear_Id, SchoolId, StudId);

            if (Set_Default == 1) {
                Sel_Year_Id = SelectedYear_Id;
                try {
                    info = GetUserDetailsKumKum();
                } catch (Exception err) {
                    Common.printStackTrace(err);
                }
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            try {

                if (info != null && !info.isEmpty()) {
                    String[] parts = info.split(",");
                    classid = parts[4];
                    classsecid = parts[5];
                    classname = parts[6];
                    Academic_Year_Start_Date = parts[10];
                    Academic_Year_End_Date = parts[11];
                    Holiday_Start_Date = parts[12];
                    Holiday_End_Date = parts[13];
                    Datastorage.SetClassId(AcademicYearActivity.this, classid);
                    Datastorage.SetClassSecId(AcademicYearActivity.this, classsecid);
                    Datastorage.SetClassSecName(AcademicYearActivity.this, classname);
                    Datastorage.AcaStartDate(AcademicYearActivity.this, Academic_Year_Start_Date);
                    Datastorage.AcaEndDate(AcademicYearActivity.this, Academic_Year_End_Date);
                    Datastorage.SetLoginPin(AcademicYearActivity.this, LogPin);
                    Datastorage.SetHolidayStartDate(AcademicYearActivity.this, Holiday_Start_Date);
                    Datastorage.SetHolidayEndDate(AcademicYearActivity.this, Holiday_End_Date);
                    SetDefalulAccount(SelectedYear_Id, SelectedYearText);
                    Constants.writelog("AcademicYearActivity", "SetDefaultYear 285:" + SelectedYear_Id + " YearText:" + SelectedYearText);
                    //Jaydeep
                    Intent i = new Intent(AcademicYearActivity.this, HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                    Common.onClickAnimation(AcademicYearActivity.this);//
                } else {
                    Toast.makeText(AcademicYearActivity.this, "Please Try Again.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                Constants.writelog("AcademicYearActivity", "Exception 278:" + ex.getMessage());
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public String[] GetAllAcademicYear() {
        String[] years = null;
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_ALL_ACADEMIC_YEAR);
        request.addProperty("schoolid", SchoolId);
        request.addProperty("studid", StudId);
        try {
            SoapObject result = Constants.CallWebMethod(
                    AcademicYearActivity.this, request, Constants.GET_ALL_ACADEMIC_YEAR, true);
            if (result != null && result.getPropertyCount() > 0) {
                SoapObject obj2 = (SoapObject) result.getProperty(0);
                String[] output = null;

                if (obj2 != null) {
                    // int count= 20; //obj2.getPropertyCount();
                    int count = obj2.getPropertyCount();
                    output = new String[count];
                    years = new String[count];
                    yearsid = new int[count];
                    String[] myarray = new String[count];

                    for (int i = 0; i < count; i++) {
                        myarray[i] = obj2.getProperty(i).toString();
                        String[] parts = myarray[i].split("##,@@");
                        output[i] = parts[0].toString();
                        years[i] = parts[0].toString();
                        yearsid[i] = Integer.parseInt(parts[1].toString());
                        map.put(parts[0].toString(),
                                Integer.parseInt(parts[1].toString()));
                        // Check Year Id is Insert Or Not
                        Boolean IsYearIdInsert = true;
                        IsYearIdInsert = db.CheckAcademicYear(
                                Integer.parseInt(StudId),
                                Integer.parseInt(SchoolId),
                                Integer.parseInt(parts[1].toString()));
                        if (IsYearIdInsert) {

                        } else {
                            int Iscurrent = Integer.parseInt(parts[2]
                                    .toString());
                            if (Iscurrent == 1) {
                                db.AddYear(new Contact(Integer
                                        .parseInt(parts[1].toString()),
                                        parts[0].toString(), Integer
                                        .parseInt(parts[2].toString()),
                                        Integer.parseInt(SchoolId), Integer
                                        .parseInt(StudId), 1));
                            } else {
                                db.AddYear(new Contact(Integer
                                        .parseInt(parts[1].toString()),
                                        parts[0].toString(), Integer
                                        .parseInt(parts[2].toString()),
                                        Integer.parseInt(SchoolId), Integer
                                        .parseInt(StudId), 0));
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

    public void SetDefalulAccount(final String Year_Id, final String Year_Text) {

        Constants.Logwrite("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.GetAllAcademicYearRecords(
                Integer.parseInt(SchoolId), Integer.parseInt(StudId));
        // LoginDetail.setCurrentYearId(Year_Id);
        // LoginDetail.setAcademicyear(Year_Text);
        Datastorage.SetCurrentYearId(AcademicYearActivity.this, Year_Id);
        Datastorage.SetAcademicYear(AcademicYearActivity.this, Year_Text);
        ItmNames = new String[contacts.size()];
        int cntr = 0;
        for (Contact cn : contacts) {
            String Msg = cn.getGlobalText();
            String[] spltstr = Msg.split(",");
            String yeartxt = spltstr[0].toString();
            ItmNames[cntr] = yeartxt;
            cntr++;
        }
        adapter = new ExamItemsAdapter(AcademicYearActivity.this, ItmNames);
        ItmView = (ListView) findViewById(R.id.lstAcademicYear);
        ItmView.setAdapter(adapter);

        Toast.makeText(AcademicYearActivity.this, "Set as a default Sucessfully", Toast.LENGTH_LONG).show();
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            int cntr = 0;
            if (Is_Ref == 0) {
                List<Contact> contacts = db.GetAllAcademicYearRecords(
                        Integer.parseInt(SchoolId), Integer.parseInt(StudId));
                ItmNames = new String[contacts.size()];
                yearsid = new int[contacts.size()];
                if (ItmNames != null && ItmNames.length > 0) {
                    for (Contact cn : contacts) {
                        String msg = cn.getGlobalText();
                        String[] parts = msg.split(",");
                        String year = parts[0].toString();
                        int yearid = Integer.parseInt(parts[1].toString());
                        // Assign Value
                        ItmNames[cntr] = parts[0].toString();
                        yearsid[cntr] = Integer.parseInt(parts[1].toString());
                        map.put(year, yearid);
                        cntr++;
                    }
                } else {
                    ItmNames = GetAllAcademicYear();
                }
            } else {
                ItmNames = GetAllAcademicYear();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            try {
                if (ItmNames != null && ItmNames.length > 0) {
                    ExamItemsAdapter adapter = new ExamItemsAdapter(AcademicYearActivity.this,
                            ItmNames);
                    ItmView = (ListView) findViewById(R.id.lstAcademicYear);
                    ItmView.setAdapter(adapter);
                    ListclickEvent();
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    if (Constants.isShowInternetMsg) {
                        Constants
                                .NotifyNoInternet(AcademicYearActivity.this);
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(AcademicYearActivity.this).create();
                        alertDialog.setTitle("Information");
                        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                        alertDialog.setIcon(R.drawable.information);
                        alertDialog.setButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        AcademicYearActivity.this.finish();
                                        onClickAnimation();
                                    }
                                });
                        alertDialog.show();
                    }
                    mProgressBar.setVisibility(View.GONE);
                    return;
                }
            } catch (Exception err) {
                mProgressBar.setVisibility(View.GONE);
            }
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
        } catch (Exception err) {
            Common.printStackTrace(err);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int iid = item.getItemId();
        if (iid == android.R.id.home) {
            hideKeyboard(AcademicYearActivity.this);
            AcademicYearActivity.this.finish();
            onBackClickAnimation();
        } else if (iid == 1 || iid == 2 || iid == 3 || iid == 4) {
            if (iid == 1) {
                Is_Ref = 1;
                new MyTask().execute();
            } else if (iid == 2) {
                addAccountClick(AcademicYearActivity.this);
            } /*else if (iid == 3) {
                removeAccountClick(AcademicYearActivity.this);
            }*/ else if (iid == 4) {
                accountListClick(AcademicYearActivity.this);
            }
        } else {
            // new ac list
            String details = mapacc.get(iid).toString();
            Constants.SetAccountDetails(details, AcademicYearActivity.this);
            intent = new Intent(AcademicYearActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
            onBackClickAnimation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AcademicYearActivity.this.finish();
        onBackClickAnimation();
    }

    public String GetUserDetailsKumKum() {
        String resp = "";
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_USER_DETAILS_KUMKUM);
        request.addProperty("PhoneNo", PhoneNumber);
        request.addProperty("Log_pin", LogPin);
        request.addProperty("YearId", Sel_Year_Id);
        request.addProperty("studid", Integer.parseInt(StudId));
        try {
            SoapObject result = Constants.CallWebMethod(
                    AcademicYearActivity.this, request, Constants.GET_USER_DETAILS_KUMKUM, false);
            if (result != null && result.getPropertyCount() > 0) {
                String info = result.getPropertyAsString(0);
                resp = info;
            }
        } catch (Exception e) {
            Constants.Logwrite("StartupActivity",
                    "GetUserDetails()_Exception:" + e.getMessage()
                            + "_Stacktrace:" + e.getStackTrace());
            return resp;
        }
        return resp;
    }
    /**
     * A placeholder fragment containing a simple view.
     */
}
