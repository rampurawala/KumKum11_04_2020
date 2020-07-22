package com.expedite.apps.kumkum.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.database.DatabaseHandler;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;


public class ChangePinActivity extends BaseActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private TextView ok;
    private EditText txtpin, txtpin1;
    private String status = "";
    private DatabaseHandler db;
    private HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
    private String StudId, SchoolId, UserId, LastUpdatedTime, StudentName, PhoneNumber,
            Year_Id;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mobile);

        init();
    }

    public void init() {
        Constants.setActionbar(getSupportActionBar(), ChangePinActivity.this, ChangePinActivity.this,
                "ChangePin", "ChangePassword", ActivityNames.ChangePinActivity);
        db = new DatabaseHandler(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        try {
            StudId = Datastorage.GetStudentId(ChangePinActivity.this);
            SchoolId = Datastorage.GetSchoolId(ChangePinActivity.this);
            UserId = Datastorage.GetUserId(ChangePinActivity.this);
            LastUpdatedTime = Datastorage.GetLastUpdatedtime(ChangePinActivity.this);
            StudentName = Datastorage.GetStudentName(ChangePinActivity.this);
            PhoneNumber = Datastorage.GetPhoneNumber(ChangePinActivity.this);
            Year_Id = Datastorage.GetCurrentYearId(ChangePinActivity.this);
            txtpin = (EditText) findViewById(R.id.edtMobilenum);
            txtpin1 = (EditText) findViewById(R.id.edtPin);
            txtpin.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            txtpin1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            ok = (TextView) findViewById(R.id.imgbtnChangepin);
            ok.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String Entered_Pin = txtpin.getText().toString();
                    if (!Entered_Pin.trim().equals("")) {
                        if (Entered_Pin.trim().length() < 6) {
                            ShowalertDilougue("Alert", "Please enter 6 digits pin");
                            txtpin.requestFocus();
                            return;
                        }
                    } else {
                        ShowalertDilougue("Alert", "Please Enter Pin");
                        txtpin.requestFocus();
                        return;
                    }

                    String Rentered_Pin = txtpin1.getText().toString();
                    if (!Rentered_Pin.trim().equals("")) {
                        if (Rentered_Pin.trim().length() < 6) {
                            ShowalertDilougue("Alert", "Please Enter 6 digits Re-Pin");
                            txtpin1.requestFocus();
                            return;
                        }
                    } else {
                        ShowalertDilougue("Alert", "Please Enter Re-Pin");
                        txtpin1.requestFocus();
                        return;
                    }
                    String oldpin = db.getpin(StudId, SchoolId);
                    if (oldpin.equalsIgnoreCase(Entered_Pin)) {
                        ShowalertDilougue("Alert", "Old and New pin can not be same.");
                        return;
                    }
                    if (Entered_Pin.equals(Rentered_Pin)) {
                        if (Datastorage.getSessionBoolean(ChangePinActivity.this, Datastorage.isFirstTime)) {
                            Intent intent = new Intent(ChangePinActivity.this, OtpActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("Pin", Entered_Pin);
                            startActivity(intent);
                            finish();
                        } else {
                            new MyTask().execute();
                        }
                    } else {
                        ShowalertDilougue("Alert", "Pin does not Match");
                        return;
                    }
                    // new MyTask().execute();
                }

                private void ShowalertDilougue(String settitle, String setmsg) {
                    try {

                        AlertDialog alertDialog = new AlertDialog.Builder(ChangePinActivity.this).create();
                        alertDialog.setTitle(settitle);
                        alertDialog.setMessage(setmsg);
                        alertDialog.setIcon(R.drawable.alert);
                        alertDialog.setButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });

                        // Showing Alert Message
                        alertDialog.show();

                    } catch (Exception err) {

                    }
                }

            });
            LogUserVisted();
        } catch (Exception err) {
            Constants.writelog("ChangePinActivity", "OnCreate()158 MSG:" + err.getMessage());
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String exist = CheckLoginPinExist();
            if (exist == "true") {
                // alert.showAlertDialog(ChangePinActivity.this,"Alert","Try to enter another pin...",true);
            } else {
                ChangeLoginPin();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(ChangePinActivity.this);
                } else {
                    if (status.equals("true")) {
                        db.UpdatePin(SchoolId, StudId, txtpin.getText()
                                .toString());
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChangePinActivity.this);
                        alertDialog.setTitle("Information");
                        alertDialog.setMessage("Pin is sucessfully changed.");
                        alertDialog.setIcon(R.drawable.information);
                        alertDialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // User pressed YES button. Write Logic
                                        // Here
                                        txtpin.setText("");
                                        txtpin1.setText("");
                                        Intent intent = new Intent(ChangePinActivity.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                        // Showing Alert Message
                        alertDialog.show();
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChangePinActivity.this);
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Try to enter another pin.");
                        alertDialog.setIcon(R.drawable.information);
                        alertDialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        txtpin.setText("");
                                        txtpin1.setText("");
                                    }
                                });
                        alertDialog.show();
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                Constants.writelog("ChangePinActivity", "postex()266 MSG:" + err.getMessage());
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    public void ChangeLoginPin() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.CHANGE_LOGIN_PIN);
        request.addProperty("PhoneNo", PhoneNumber);
        request.addProperty("LogPin", txtpin.getText().toString());
        request.addProperty("User_Id", Integer.parseInt(UserId));
        try {
            SoapObject result = Constants.CallWebMethod(
                    ChangePinActivity.this, request, Constants.CHANGE_LOGIN_PIN, true);
            if (result != null && result.getPropertyCount() > 0) {
                if (result.getProperty(0).toString().equals("true")) {
                    status = "true";
                    Datastorage.setSessionBoolean(ChangePinActivity.this, Datastorage.isFirstTime, false);
                }
            }
        } catch (Exception e) {
            Constants.writelog("ChangePinActivity", "ChangeLoginPin()301 MSG:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void LogUserVisted() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.LOG_USER_VISITED);
        request.addProperty("moduleid", 6);
        request.addProperty("schoolid", SchoolId);
        request.addProperty("User_Id", UserId);
        request.addProperty("phoneno", PhoneNumber);
        request.addProperty("pageid", 16);
        try {
            Constants.CallWebMethod(ChangePinActivity.this, request,
                    Constants.LOG_USER_VISITED, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String CheckLoginPinExist() {

        String Exist = "";
        SoapObject request = new SoapObject(Constants.NAMESPACE,
                Constants.CHECK_LOGIN_PIN_EXIST);
        request.addProperty("PhoneNo", PhoneNumber);
        request.addProperty("LogPin", txtpin.getText().toString());

        try {
            SoapObject result = Constants
                    .CallWebMethod(ChangePinActivity.this, request,
                            Constants.CHECK_LOGIN_PIN_EXIST, true);
            if (result != null && result.getPropertyCount() > 0) {
                Exist = result.getPropertyAsString(0);
            }
        } catch (Exception ex) {
            Constants.writelog("ChangePinActivity", "CheckLoginPinExist()409 Ex:"
                    + ex.getMessage() + "::::" + ex.getStackTrace());
            return Exist;
        }
        return Exist;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            boolean isFirstTime = Datastorage.getSessionBoolean(ChangePinActivity.this, Datastorage.isFirstTime);
            if (!isFirstTime) {
                menu.clear();
                mapacc = new HashMap<Integer, String>();
                menu.add(0, 1, 1, "Add Account").setTitle("Add Account");
              //  menu.add(0, 2, 2, "Remove Account").setTitle("Remove Account");
                menu.add(0, 3, 3, "Set Default Account").setTitle("Set Default Account");
                mapacc = Constants.AddAccount(menu, db);
            }
        } catch (Exception err) {
            Constants.Logwrite("ChangePinActivity:", "onPrepareOptionsMenu:" + err.getMessage()
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
            hideKeyboard(ChangePinActivity.this);
            ChangePinActivity.this.finish();
            onBackClickAnimation();
        } else if (iid == 1 || iid == 2 || iid == 3) {
            if (iid == 1) {
                addAccountClick(ChangePinActivity.this);
            } else if (iid == 2) {
                removeAccountClick(ChangePinActivity.this);
            } else {
                accountListClick(ChangePinActivity.this);
            }
        } else {
            String details = mapacc.get(iid).toString();
            Constants.SetAccountDetails(details, ChangePinActivity.this);
            intent = new Intent(ChangePinActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
            onBackClickAnimation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        boolean isFirstTime = Datastorage.getSessionBoolean(ChangePinActivity.this, Datastorage.isFirstTime);
        if (!isFirstTime) {
            super.onBackPressed();
            ChangePinActivity.this.finish();
            onBackClickAnimation();
        } else {
            if (doubleBackToExitPressedOnce) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 3000);
        }
    }

}
