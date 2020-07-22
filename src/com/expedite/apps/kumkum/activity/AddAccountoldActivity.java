package com.expedite.apps.kumkum.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.Contact;
import com.google.android.gms.ads.AdView;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

public class AddAccountoldActivity extends BaseActivity {
    private TextView btnadd, btncancel;
    private String Entered_Pin = "", Entered_PhoneNo = "";
    private EditText txtLogin, txtPhone;
    private String resp, Schl_Id = "", studid = "", Name = "", Class_Id = "", Class_Sec_Id = "";
    private String acayear = "", yearid = "", lastupdatedtime = "", academicyear = "", User_Id = "";
    private String classid = "", classsecid = "", classname = "", studentenrolldate = "", updatedtime = "", ClassSecName = "";
    private int RouteId = 0;
    private HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
    private ProgressBar mProgressBar;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        init();
    }

    public void init() {

        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.AddAccountoldActivity);
            Constants.setActionbar(getSupportActionBar(), AddAccountoldActivity.this,
                    AddAccountoldActivity.this, "Add Account", "AddAccountoldActivity",ActivityNames.AddAccountoldActivity);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            db = new DatabaseHandler(AddAccountoldActivity.this);
            btnadd = (TextView) findViewById(R.id.imgbtnaddaccount);
            btncancel = (TextView) findViewById(R.id.imgbtncancelaccount);
            txtLogin = (EditText) findViewById(R.id.edittxtlogin);
            txtPhone = (EditText) findViewById(R.id.edtMobileNumber);
            txtLogin.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            txtPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            btnadd.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onclickLogin();
                }
                public void onclickLogin() {
                    try {
                        Entered_Pin = txtLogin.getText().toString();
                        Entered_PhoneNo = txtPhone.getText().toString();

                        if (!Entered_Pin.trim().equals("")) {
                            if (Entered_Pin.trim().length() < 6) {
                                ShowAlertDiolougue("Alert", "Please Enter Valid Login Pin");
                                txtLogin.requestFocus();
                                return;
                            }
                        } else {
                            ShowAlertDiolougue("Alert", "Please Enter Login Pin");
                            txtLogin.requestFocus();
                            return;
                        }
                        if (!Entered_PhoneNo.trim().equals("")) {
                            if (Entered_PhoneNo.trim().length() < 10) {
                                ShowAlertDiolougue("Alert", "Please Enter Valid Mobile Number");
                                txtPhone.requestFocus();
                                return;
                            }
                        } else {
                            ShowAlertDiolougue("Alert", "Please Enter Mobile Number");
                            txtPhone.requestFocus();
                            return;
                        }
                        new MyTask().execute();
                    } catch (Exception err) {
                        Constants.Logwrite("Login Page:On Login Click", err.getMessage());
                    }
                }

                private void ShowAlertDiolougue(String title, String msg) {
                    AlertDialog alertDialog = new AlertDialog.Builder(AddAccountoldActivity.this).create();
                    alertDialog.setTitle(title);
                    alertDialog.setMessage(msg);
                    alertDialog.setIcon(R.drawable.alert);
                    alertDialog.setCancelable(false);
                    alertDialog.setButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    alertDialog.show();
                    txtLogin.setText("");
                }
            });

            btncancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(AddAccountoldActivity.this, HomeActivity.class);
                        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    } catch (Exception err) {
                        err.printStackTrace();
                    }
                }
            });
        } catch (Exception err) {
            Constants.Logwrite("Erorr add account", "MSG: " + err.getMessage());
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                CheckLoginPinExist();
            } catch (Exception err) {
                Common.printStackTrace(err);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(AddAccountoldActivity.this);
                } else {
                    if (resp.equals("true")) {
                        Toast.makeText(AddAccountoldActivity.this, "Account is sucessfully created...", Toast.LENGTH_LONG).show();
                        int success = db.UpdateSetIsDefault_Zero_to_One();
                        int Set_Default = db.UpdateContactRecordSetDefaultOne(studid, Schl_Id);
                        Datastorage.SetLastAutoUpdateExamDay(AddAccountoldActivity.this, 0);
                        Datastorage.SetLastAutoUpdateMessageDay(AddAccountoldActivity.this, 0);
                        if (Set_Default == 1) {
                            RedirectToDefaultStudentAccount();
                        } else {
                            Toast.makeText(AddAccountoldActivity.this, "Please try again.", Toast.LENGTH_LONG).show();
                        }
                        Intent in = new Intent(AddAccountoldActivity.this, SplashActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(in);
                        finish();
                        onClickAnimation();
                    } else if (resp.equals("there")) {
                        AlertDialog alertDialog = new AlertDialog.Builder(AddAccountoldActivity.this).create();
                        alertDialog.setTitle("Information");
                        alertDialog.setMessage("Account is already exist...");
                        alertDialog.setIcon(R.drawable.alert);
                        alertDialog.setButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        txtLogin.setText("");
                                        txtPhone.setText("");
                                    }
                                });
                        alertDialog.show();
                    } else {//
                        AlertDialog alertDialog = new AlertDialog.Builder(AddAccountoldActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Please enter valid phone or login pin..");
                        alertDialog.setIcon(R.drawable.alert);
                        alertDialog.setButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        alertDialog.show();
                    }
                }
            } catch (Exception err) {
                mProgressBar.setVisibility(View.GONE);
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public String CheckLoginPinExist() {
        String str = "";
        try {
            str = Build.DEVICE + "|||" + Build.MODEL + "|||" + Build.ID
                    + "|||" + Build.PRODUCT + "|||" + Build.VERSION.SDK
                    + "|||" + Build.VERSION.RELEASE + "|||"
                    + Build.VERSION.INCREMENTAL;
        } catch (Exception err) {
            Constants.Logwrite("AddAccountoldActivity", " 296 MSG:" + err.getStackTrace().toString());
        }
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.CHECK_LOGIN_PIN1);
        request.addProperty("PhoneNo", Entered_PhoneNo);
        request.addProperty("LogPin", Integer.parseInt(Entered_Pin));
        request.addProperty("Devicedetails", str);
        try {
            SoapObject result = Constants.CallWebMethod(
                    AddAccountoldActivity.this, request, Constants.CHECK_LOGIN_PIN1, true);
            if (result != null && result.getPropertyCount() > 0) {
                resp = result.getPropertyAsString(0);
                if (resp != null && !resp.equals("")) {
                    String[] resultParts = resp.split(",");
                    if (resultParts.length > 1 && resultParts[1].equalsIgnoreCase("0")) {
                        Datastorage.setSessionBoolean(AddAccountoldActivity.this, Datastorage.isFirstTime, true);
                    }
                    resp = resultParts[0];
                    if (resp.equalsIgnoreCase("true")) {
                        String info = GetUserDetailsKumKumvehicle();
                        String[] parts = info.split(",");
                        Schl_Id = parts[0].toString();
                        studid = parts[2].toString();
                        yearid = parts[3].toString();
                        Name = parts[8].toString();
                        Class_Id = parts[4].toString();
                        Class_Sec_Id = parts[5].toString();
                        acayear = parts[9].toString();
                        User_Id = parts[1].toString();
                        lastupdatedtime = parts[14].toString();
                        updatedtime = parts[15].toString();
                        classname = parts[6].toString();
                        studentenrolldate = parts[7].toString();
                        RouteId = Integer.parseInt(parts[16].toString());
                        try {
                            ClassSecName = parts[17].toString();
                        } catch (Exception ex) {
                            Constants.writelog("AddAccunt",
                                    "Exception: 387 " + ex.getMessage() + ":::::"
                                            + ex.getStackTrace());
                        }

                        int record_cnt = db.getContactsCountUsingStud_ID_and_School_Id(
                                Integer.parseInt(studid), Integer.parseInt(Schl_Id));

                        if (record_cnt > 0) {
                            resp = "there";
                        } else {
                            int stdcnt = db.getContactsCount();
                            if (stdcnt > 0) {
                                db.addContact(new Contact(Name, Entered_PhoneNo,
                                        Integer.parseInt(Entered_Pin), 0, Integer
                                        .parseInt(studid), Integer
                                        .parseInt(Schl_Id), Integer
                                        .parseInt(yearid), Integer
                                        .parseInt(Class_Id), Integer
                                        .parseInt(Class_Sec_Id), Integer
                                        .parseInt(User_Id), classname,
                                        studentenrolldate, lastupdatedtime,
                                        acayear, updatedtime, RouteId, ClassSecName));

                            } else {
                                db.addContact(new Contact(Name, Entered_PhoneNo,
                                        Integer.parseInt(Entered_Pin), 1, Integer
                                        .parseInt(studid), Integer
                                        .parseInt(Schl_Id), Integer
                                        .parseInt(yearid), Integer
                                        .parseInt(Class_Id), Integer
                                        .parseInt(Class_Sec_Id), Integer
                                        .parseInt(User_Id), classname,
                                        studentenrolldate, lastupdatedtime,
                                        acayear, updatedtime, RouteId, ClassSecName));
                            }

                            String checkExist = Constants.CheckUserRegistered(Integer.parseInt(studid),
                                    Integer.parseInt(Schl_Id), AddAccountoldActivity.this);
                            if (checkExist.equals("true")) {
                            } else {
                                Constants.InsertNewAccountGCMData(Integer.parseInt(studid),
                                        Integer.parseInt(Schl_Id), AddAccountoldActivity.this);
                            }
                            resp = "true";
                        }
                    } else {
                        resp = "false";
                    }
                } else {
                    resp = "false";
                }
            }
        } catch (Exception e) {
            Constants.Logwrite("Login Page:CheckLoginPin", "Exception:490 " + e.getMessage());
            Constants.writelog("AddAccountoldActivity",
                    "Exception:491 GetUserDetailsKumKumvehicle 518:" + e.getMessage() + ":::" + e.getStackTrace());
            return resp;
        }
        return resp;
    }

    public void RedirectToDefaultStudentAccount() {
        try {
            Datastorage.SetSchoolId(AddAccountoldActivity.this, Schl_Id);
            Datastorage.SetStudentID(AddAccountoldActivity.this, studid);
            Constants.ResetLastAutoUpdateDay(AddAccountoldActivity.this);
            Intent intent = new Intent(AddAccountoldActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
            String stud_details = db.GetStudentAccountDetails(AddAccountoldActivity.this,
                    Integer.parseInt(studid), Integer.parseInt(Schl_Id));
            String[] parts = stud_details.split(",");
            if (parts != null && parts.length > 7 && parts[8].toString() != "") {
                Datastorage.SetStudentName(AddAccountoldActivity.this,
                        parts[8].toString());
            } else {
                Datastorage.SetStudentName(AddAccountoldActivity.this, "");
            }
            db.SetCurrentYearAsDefaultYear(studid, Schl_Id);
            int yearid = db.GetCurrentAcademicYearId(
                    Integer.parseInt(Schl_Id), Integer.parseInt(studid));
            Datastorage.SetAcademicYear(AddAccountoldActivity.this, yearid + "");
        } catch (Exception ex) {
            Constants.writelog("AccountListActivity", "Ex 370:" + ex.getMessage()
                    + "::::::" + ex.getStackTrace());
        }
    }

    public String GetUserDetailsKumKumvehicle() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_USER_DETAILS_VEHICLE);
        request.addProperty("PhoneNo", Entered_PhoneNo);
        request.addProperty("Log_pin", Integer.parseInt(Entered_Pin));
        request.addProperty("YearId", 0);
        try {
            SoapObject result = Constants.CallWebMethod(AddAccountoldActivity.this, request,
                    Constants.GET_USER_DETAILS_VEHICLE, true);
            if (result != null && result.getPropertyCount() > 0) {
                String info = result.getPropertyAsString(0);
                resp = info;
            }
        } catch (Exception e) {
            Constants.writelog(
                    "AddAccountoldActivity",
                    "Exception:520 GetUserDetailsKumKumvehicle 518"
                            + e.getMessage() + ":::" + e.getStackTrace());
            return "";
        }
        return resp;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            menu.clear();
            mapacc = new HashMap<Integer, String>();
          //  menu.add(0, 1, 1, "Remove Account").setTitle("Remove Account");
            menu.add(0, 2, 2, "Set Default Account").setTitle("Set Default Account");
            mapacc = Constants.AddAccount(menu, db);
        } catch (Exception err) {
            Constants.Logwrite("AddAccountoldActivity:", "onPrepareOptionsMenu:" + err.getMessage()
                    + "StackTrace::" + err.getStackTrace().toString());
            return true;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int iid = item.getItemId();
        if (iid == android.R.id.home) {
            hideKeyboard(AddAccountoldActivity.this);
            AddAccountoldActivity.this.finish();
            onBackClickAnimation();
        } else if (iid == 1 || iid == 2) {
            /*if (iid == 1) {
                removeAccountClick(AddAccountoldActivity.this);
            } else*/ if (iid == 2) {
                setDefaultAccount(AddAccountoldActivity.this);
            }
        } else {
            String details = mapacc.get(iid);
            Constants.SetAccountDetails(details, AddAccountoldActivity.this);
            intent = new Intent(AddAccountoldActivity.this, SplashActivity.class);
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
        AddAccountoldActivity.this.finish();
        onBackClickAnimation();
    }
}
