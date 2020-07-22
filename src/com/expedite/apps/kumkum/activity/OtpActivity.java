package com.expedite.apps.kumkum.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.ConnectionDetector;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import org.ksoap2.serialization.SoapObject;

public class OtpActivity extends BaseActivity {
    private TextView txtCounter, txmobile;
    private Button btnresend;
    private ProgressDialog ringProgressDialog;
    private EditText editverify, otp1, otp2, otp3, otp4, otp5, otp6;
    private DatabaseHandler db;
    private String  studId = "", schoolId = "", serverOtp = "", mobile = "", enteredPin = "", enteredOTP = "", tag = "OtpActivity";
    private ConnectionDetector cd;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        init();
    }

    public void init() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Constants.setActionbar(getSupportActionBar(), OtpActivity.this, OtpActivity.this,
                "OTP Verification", "OTP Verification", ActivityNames.OtpActivity);
        txmobile = (TextView) findViewById(R.id.textmobile);
        editverify = (EditText) findViewById(R.id.editOtp);
        otp1 = (EditText) findViewById(R.id.otp1);
        otp2 = (EditText) findViewById(R.id.otp2);
        otp3 = (EditText) findViewById(R.id.otp3);
        otp4 = (EditText) findViewById(R.id.otp4);
        otp5 = (EditText) findViewById(R.id.otp5);
        otp6 = (EditText) findViewById(R.id.otp6);
        cd = new ConnectionDetector(OtpActivity.this);

        enteredPin = getIntent().getStringExtra("Pin");
        studId = Datastorage.GetStudentId(OtpActivity.this);
        schoolId = Datastorage.GetSchoolId(OtpActivity.this);
        Button btnverify = (Button) findViewById(R.id.btnVerify);
        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });
        btnresend = (Button) findViewById(R.id.btnResend);
        btnresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ResendTask().execute();
            }
        });
        btnresend.setClickable(false);
        btnresend.setTextColor(getResources().getColor(R.color.bolgque));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        txtCounter = (TextView) findViewById(R.id.textView4);
        coudownstart();
        editverify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editverify.getText().toString().length() == 6) {
                    Constants.writelog(tag, "editverify.afterTextChanged() 97:OTP in EditText:" + editverify.getText().toString());
                    verify();
                }
            }
        });

        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!otp1.getText().toString().trim().equalsIgnoreCase("")) {
                        View setfoc = otp2;
                        setfoc.requestFocus();
                        /*otp2.setFocusable(true);
                        otp2.requestFocus()=true;*/
                        if (!otp1.getText().toString().trim().equals("") && !otp2.getText().toString().trim().equals("") && !otp3.getText().toString().trim().equals("") && !otp4.getText().toString().trim().equals("") && !otp5.getText().toString().trim().equals("") && !otp6.getText().toString().trim().equals("")) {
                            Constants.writelog(tag, "otp1.afterTextChanged() 81:OTP in EditText:" + otp1.getText().toString());
                            verify();
                        }
                    } else {
                        View setfoc = otp1;
                        setfoc.requestFocus();
                    }
                } catch (Exception ex) {
                    Constants.writelog(tag, "otp1.onTextChanged() 130 : Error: " + ex.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!otp2.getText().toString().trim().equalsIgnoreCase("")) {
                        View setfoc = otp3;
                        setfoc.requestFocus();
                        if (!otp1.getText().toString().trim().equals("") && !otp2.getText().toString().trim().equals("") && !otp3.getText().toString().trim().equals("") && !otp4.getText().toString().trim().equals("") && !otp5.getText().toString().trim().equals("") && !otp6.getText().toString().trim().equals("")) {
                            Constants.writelog(tag, "otp2.afterTextChanged() 153:OTP in EditText:" + otp2.getText().toString());
                            if (cd.isConnectingToInternet()) {
                                verify();
                            } else {
                                Toast.makeText(OtpActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        View setfoc = otp1;
                        setfoc.requestFocus();
                    }
                } catch (Exception ex) {
                    Constants.writelog(tag, "otp2.onTextChanged() 165 : Error: " + ex.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otp2.getText().toString().trim().equalsIgnoreCase("") && otp1.getText().toString().trim().equalsIgnoreCase("")) {
                    View setfoc = otp1;
                    setfoc.requestFocus();
                }
            }
        });

        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!otp3.getText().toString().trim().equalsIgnoreCase("")) {
                        View setfoc = otp4;
                        setfoc.requestFocus();
                        if (!otp1.getText().toString().trim().equals("") && !otp2.getText().toString().trim().equals("") && !otp3.getText().toString().trim().equals("") && !otp4.getText().toString().trim().equals("") && !otp5.getText().toString().trim().equals("") && !otp6.getText().toString().trim().equals("")) {
                            Constants.writelog(tag, "otp3.afterTextChanged() 187:OTP in EditText:" + otp3.getText().toString());
                            verify();
                        }
                    } else {
                        View setfoc = otp2;
                        setfoc.requestFocus();
                    }
                } catch (Exception ex) {
                    Constants.writelog(tag, "otp3.onTextChanged() 195 : Error: " + ex.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otp3.getText().toString().trim().equalsIgnoreCase("") && otp2.getText().toString().trim().equalsIgnoreCase("")) {
                    View setfoc = otp2;
                    setfoc.requestFocus();
                }
            }
        });

        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!otp4.getText().toString().trim().equalsIgnoreCase("")) {
                        View setfoc = otp5;
                        setfoc.requestFocus();
                        if (!otp1.getText().toString().trim().equals("") && !otp2.getText().toString().trim().equals("") && !otp3.getText().toString().trim().equals("") && !otp4.getText().toString().trim().equals("") && !otp5.getText().toString().trim().equals("") && !otp6.getText().toString().trim().equals("")) {
                            Constants.writelog(tag, "otp3.afterTextChanged() 217:OTP in EditText:" + otp4.getText().toString());
                            verify();
                        }
                    } else {
                        View setfoc = otp3;
                        setfoc.requestFocus();
                    }
                } catch (Exception ex) {
                    Constants.writelog(tag, "otp4.onTextChanged() 225 : Error: " + ex.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otp4.getText().toString().trim().equalsIgnoreCase("") && otp3.getText().toString().trim().equalsIgnoreCase("")) {
                    View setfoc = otp3;
                    setfoc.requestFocus();
                }
            }
        });

        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!otp5.getText().toString().trim().equalsIgnoreCase("")) {
                        View setfoc = otp6;
                        setfoc.requestFocus();
                        if (!otp1.getText().toString().trim().equals("") && !otp2.getText().toString().trim().equals("") && !otp3.getText().toString().trim().equals("") && !otp4.getText().toString().trim().equals("") && !otp5.getText().toString().trim().equals("") && !otp6.getText().toString().trim().equals("")) {
                            Constants.writelog(tag, "otp5.afterTextChanged() 247:OTP in EditText:" + otp5.getText().toString());
                            verify();
                        }
                    } else {
                        View setfoc = otp4;
                        setfoc.requestFocus();
                    }
                } catch (Exception ex) {
                    Constants.writelog(tag, "otp5.onTextChanged() 261 : Error: " + ex.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otp5.getText().toString().trim().equalsIgnoreCase("") && otp4.getText().toString().trim().equalsIgnoreCase("")) {
                    View setfoc = otp4;
                    setfoc.requestFocus();
                }
            }
        });

        otp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!otp6.getText().toString().trim().equalsIgnoreCase("")) {
                        if (!otp1.getText().toString().trim().equals("") && !otp2.getText().toString().trim().equals("") && !otp3.getText().toString().trim().equals("") && !otp4.getText().toString().trim().equals("") && !otp5.getText().toString().trim().equals("") && !otp6.getText().toString().trim().equals("")) {
                            Constants.writelog(tag, "otp6.afterTextChanged() 277:OTP in EditText:" + otp4.getText().toString());
                            verify();
                        }
                    } else {
                        View setfoc = otp5;
                        setfoc.requestFocus();
                    }
                } catch (Exception ex) {
                    Constants.writelog(tag, "otp6.onTextChanged() 295 : Error: " + ex.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otp6.getText().toString().trim().equalsIgnoreCase("") && otp5.getText().toString().trim().equalsIgnoreCase("")) {
                    View setfoc = otp5;
                    setfoc.requestFocus();
                }
            }
        });

        db = new DatabaseHandler(OtpActivity.this);
        mobile = db.getmobileNo(studId, schoolId);
        txmobile.setText(Html.fromHtml("Please enter 6 digit OTP code sent on your mobile (" + mobile + ")."));
        new ResendTask().execute();
    }

    public void coudownstart() {
        try {
            new CountDownTimer(60000, 1000) {
                public void onTick(long millisUntilFinished) {
                    txtCounter.setText("After " + millisUntilFinished / 1000 + " seconds you can Resend OTP.");
                }

                public void onFinish() {
                    txtCounter.setText("Click below Resend link If you did not get OTP.");
                    btnresend.setClickable(true);
                    //  btnresend.setBackgroundColor(getResources().getColor(R.color.otpclickble));
                    btnresend.setTextColor(getResources().getColor(R.color.colorPrimaryDarkNew));

                }
            }.start();
        } catch (Exception ex) {
            Constants.writelog(tag, "coudownstart() 106 : Error: " + ex.getMessage());
        }
    }

    public void verify() {
        // Store values at the time of the login attempt.
        //String name = editverify.getText().toString();
        enteredOTP = otp1.getText().toString().trim() + otp2.getText().toString().trim() + otp3.getText().toString().trim() + otp4.getText().toString().trim() + otp5.getText().toString().trim() + otp6.getText().toString().trim();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(enteredOTP) || enteredOTP.trim().length() != 6) {
            Toast.makeText(OtpActivity.this, "Please Enter OTP Number.", Toast.LENGTH_LONG).show();
            //focusView = editverify;
            // focusView = otp1;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //common.progressDialogOpen();
            if (enteredOTP.equalsIgnoreCase(serverOtp)) {
                new MyTask().execute();
            } else {
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(OtpActivity.this);
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Entered OTP is wrong.");
                alertDialog.setIcon(R.drawable.information);
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                otp1.setText("");
                                otp2.setText("");
                                otp3.setText("");
                                otp4.setText("");
                                otp5.setText("");
                                otp6.setText("");
                                View setfoc = otp1;
                                setfoc.requestFocus();
                            }
                        });
                alertDialog.show();
            }
        }
    }


    public void recivedSms(String message) {
        try {
            otp1.setText(message.substring(0, 1));
            otp2.setText(message.substring(1, 2));
            otp3.setText(message.substring(2, 3));
            otp4.setText(message.substring(3, 4));
            otp5.setText(message.substring(4, 5));
            otp6.setText(message.substring(5, 6));
            verify();
        } catch (Exception e) {
            Constants.writelog(tag, "recivedSms() 312 :OPT Set in the EditText:OTP:" + message + ": Error: " + e.getMessage());
        }
    }

    public class ResendTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            ringProgressDialog = ProgressDialog.show(OtpActivity.this, "", "Loading...", true);
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String responseString = "";
            //GenerateAndSendOTP(string PhoneNo,string studid,string schoolid,string yearid)
            SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GENERATED_AND_SEND_OTP);
            try {
                request.addProperty("PhoneNo", mobile);
                request.addProperty("studid", studId);
                request.addProperty("schoolid", schoolId);
                request.addProperty("yearid", Datastorage.GetCurrentYearId(OtpActivity.this));
                SoapObject result = Constants.CallWebMethod(
                        OtpActivity.this, request, Constants.GENERATED_AND_SEND_OTP, true);
                if (result != null && result.getPropertyCount() > 0) {
                    serverOtp = result.getProperty(0).toString();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                responseString = e.getMessage();
                e.printStackTrace();
                Constants.writelog(tag, "doInBackground() 434:  MobileNo:" + mobile + ": IOException Error: " + e.getMessage());
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            try {
                ringProgressDialog.dismiss();
                if (serverOtp != null && !serverOtp.trim().equals("")) {
                    coudownstart();
                    btnresend.setClickable(false);
                    //  btnresend.setBackgroundColor(getResources().getColor(R.color.lock));
                    btnresend.setTextColor(getResources().getColor(R.color.bolgque));
                    Toast.makeText(OtpActivity.this, "Send OTP Successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OtpActivity.this, "Please Try Again.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                Constants.writelog(tag, "ResendTask:onPostExecute() 289 : Error: " + ex.getMessage());
            } finally {
                if (ringProgressDialog.isShowing()) {
                    ringProgressDialog.dismiss();
                }
            }
        }

        protected void onCancelled() {
            if (ringProgressDialog.isShowing())
                ringProgressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ringProgressDialog != null)
            ringProgressDialog.dismiss();
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        String status = "false";

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.CHANGE_LOGINPIN_WITH_OTP);
                request.addProperty("PhoneNo", mobile);
                request.addProperty("LogPin", enteredPin);
                request.addProperty("User_Id", Integer.parseInt(Datastorage.GetUserId(OtpActivity.this)));
                request.addProperty("otp", enteredOTP);
                SoapObject result = Constants.CallWebMethod(
                        OtpActivity.this, request, Constants.CHANGE_LOGINPIN_WITH_OTP, true);
                if (result != null && result.getPropertyCount() > 0) {
                    if (result.getProperty(0).toString().equals("true")) {
                        status = "true";
                        Datastorage.setSessionBoolean(OtpActivity.this, Datastorage.isFirstTime, false);

                    }
                }
            } catch (Exception e) {
                Constants.writelog("ChangePinActivity", "ChangeLoginPin()301 MSG:" + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(OtpActivity.this);
                } else {
                    if (status.equals("true")) {
                        db.UpdatePin(schoolId, studId, enteredPin);
                        Toast.makeText(OtpActivity.this, "Pin is sucessfully changed.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(OtpActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                        onClickAnimation();
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                OtpActivity.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("Alert");
                        // Setting Dialog Message
                        alertDialog.setMessage("Try to enter another pin.");
                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.information);
                        // Setting OK Button
                        alertDialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        otp1.setText("");
                                        otp2.setText("");
                                        otp3.setText("");
                                        otp4.setText("");
                                    }
                                });
                        // Showing Alert Message
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

}

