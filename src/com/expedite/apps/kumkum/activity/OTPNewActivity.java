package com.expedite.apps.kumkum.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Bundle;
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
import com.expedite.apps.kumkum.BuildConfig;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.ConnectionDetector;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.AppService;
import com.expedite.apps.kumkum.model.Contact;
import com.expedite.apps.kumkum.model.LoginDetail;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class OTPNewActivity extends BaseActivity {

    String tag = "OtpNewActivity";
    private TextView txmobile, txtCounter;
    private Button btnresend;
    public static EditText editverify, otp1, otp2, otp3, otp4, otp5, otp6;
    private DatabaseHandler db;
    private String studId = "", schoolId = "", mobile = "", enteredOTP = "";
    // String serverOtp = "";
    private ConnectionDetector cd;
    private ProgressBar mProgressBar;
    String widthphone, heightphone;
    private ProgressDialog ringProgressDialog;
    ArrayList<String> mAccount = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpnew);
        try {
            init();
        } catch (Exception ex) {
            Constants.writelog(tag, "onCreate():20:" + ex.getMessage());
        }
    }

    public void init() {
        // serverOtp = getIntent().getStringExtra("otp");
        mobile = getIntent().getStringExtra("mobile");
        db = new DatabaseHandler(OTPNewActivity.this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Constants.setActionbar(getSupportActionBar(), OTPNewActivity.this, OTPNewActivity.this,
                "OTP Verification", "OTP Verification", ActivityNames.OTPNewActivity);
        txmobile = (TextView) findViewById(R.id.textmobile);
        txmobile.setText(Html.fromHtml("Please enter 6 digit OTP code sent on your mobile (" + mobile + ")."));
        editverify = (EditText) findViewById(R.id.editOtp);
        otp1 = (EditText) findViewById(R.id.otp1);
        otp2 = (EditText) findViewById(R.id.otp2);
        otp3 = (EditText) findViewById(R.id.otp3);
        otp4 = (EditText) findViewById(R.id.otp4);
        otp5 = (EditText) findViewById(R.id.otp5);
        otp6 = (EditText) findViewById(R.id.otp6);
        cd = new ConnectionDetector(OTPNewActivity.this);

//        studId = Datastorage.GetStudentId(OTPNewActivity.this);
//        schoolId = Datastorage.GetSchoolId(OTPNewActivity.this);
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
                Common.startSmsRecevier(OTPNewActivity.this);
                getLogInWithOtp();
                //new ResendTask().execute();
            }
        });
        btnresend.setClickable(false);
        btnresend.setTextColor(getResources().getColor(R.color.bolgque));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        widthphone = String.valueOf(dm.widthPixels);
        heightphone = String.valueOf(dm.heightPixels);
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
                                Toast.makeText(OTPNewActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_LONG).show();
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


        // mobile = db.getmobileNo(studId, schoolId);

        //   new ResendTask().execute();
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
        try {
            enteredOTP = otp1.getText().toString().trim() + otp2.getText().toString().trim() + otp3.getText().toString().trim() + otp4.getText().toString().trim() + otp5.getText().toString().trim() + otp6.getText().toString().trim();

            boolean cancel = false;
            View focusView = null;

            // Check for a valid email address.
            if (TextUtils.isEmpty(enteredOTP) || enteredOTP.trim().length() != 6) {
                Toast.makeText(OTPNewActivity.this, "Please Enter OTP Number.", Toast.LENGTH_LONG).show();
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
                validateotp();
            }
        } catch (Exception ex) {
            Constants.writelog(tag, "recivedSms() 407 verify() Error: " + ex.getMessage());
        }
    }

    private void validateotp() {
        String mDeviceid = "", mPlateform = "";
        try {
            mDeviceid = Build.ID;
            mPlateform = Build.VERSION.RELEASE;
        } catch (Exception ex) {

        }
        mProgressBar.setVisibility(View.VISIBLE);
        Call<AppService> mCall = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                .GetValidate(mobile, enteredOTP, String.valueOf(SchoolDetails.appname), mDeviceid, mPlateform, Constants.CODEVERSION);

        mCall.enqueue(new retrofit2.Callback<AppService>() {
            @Override
            public void onResponse(Call<AppService> call, Response<AppService> response) {
                try {
                    AppService tmp = response.body();
                    if (tmp != null && tmp.getResponse() != null && tmp.getResponse().equalsIgnoreCase("1")) {
                        GetDetails();
                    } else {
                        String message = "Entered OTP is wrong.";
                        if (tmp != null && tmp.getStrResult() != null && !tmp.getStrResult().isEmpty()) {
                            message = tmp.getStrResult();
                        }
                        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(OTPNewActivity.this);
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage(message);
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
                        mProgressBar.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                    Constants.writelog(tag, "getLogInWithOtp:162:" + ex.getMessage());
                }
            }

            @Override
            public void onFailure(Call<AppService> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void GetDetails() {
        try {
            String mDeviceDetail = "";
            try {
                mDeviceDetail = Build.DEVICE + "|||" + Build.MODEL + "|||" + Build.ID
                        + "|||" + Build.PRODUCT + "|||" + Build.VERSION.SDK
                        + "|||" + Build.VERSION.RELEASE + "|||" + Build.VERSION.INCREMENTAL;

            } catch (Exception ex) {

            }
            if (isOnline()) {
                mProgressBar.setVisibility(View.VISIBLE);
                Call<AppService> mCall = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                        .GetLoginDetail(mobile, enteredOTP, "", mDeviceDetail, Constants.PLATFORM, Constants.CODEVERSION);
                mCall.enqueue(new retrofit2.Callback<AppService>() {
                    @Override
                    public void onResponse(Call<AppService> call, Response<AppService> response) {
                        try {
                            AppService tmp = response.body();
                            if (tmp != null && tmp.getStrResult() != null && !tmp.getStrResult().isEmpty()
                                    && tmp.getResponse().equalsIgnoreCase("1")) {
                                Boolean userfound = false;
                                for (int i = 0; i < tmp.getListArray().size(); i++) {
                                    AppService.ListArray tmp1 = tmp.getListArray().get(i);
                                    String str = tmp1.getFifth() + "," + tmp1.getSecond() + "," + tmp1.getThird();
                                    mAccount.add(str);
                                    if (tmp1.getSecond() == null || tmp1.getSecond().equalsIgnoreCase("")) {
                                        tmp1.setSecond("0");
                                    } else if (tmp1.getThird() == null || tmp1.getThird().equalsIgnoreCase("")) {
                                        tmp1.setThird("0");
                                    } else if (tmp1.getSixth() == null || tmp1.getSixth().equalsIgnoreCase("")) {
                                        tmp1.setSixth("0");
                                    } else if (tmp1.getSeventh() == null || tmp1.getSeventh().equalsIgnoreCase("")) {
                                        tmp1.setSeventh("0");
                                    } else if (tmp1.getEighth() == null || tmp1.getEighth().equalsIgnoreCase("")) {
                                        tmp1.setEighth("0");
                                    } else if (tmp1.getNineteen() == null || tmp1.getNineteen().equalsIgnoreCase("")) {
                                        tmp1.setNineteen("0");
                                    } else if (tmp1.getSeventeen() == null || tmp1.getSeventeen().equalsIgnoreCase("")) {
                                        tmp1.setSeventeen("0");
                                    }
                                    int Contact_Status = db.addContact(new Contact(
                                            tmp1.getFifth(), mobile, 0, 0, Integer.parseInt(tmp1.getSecond()), Integer.parseInt(tmp1.getThird()),
                                            Integer.parseInt(tmp1.getSixth()), Integer.parseInt(tmp1.getSeventh()),
                                            Integer.parseInt(tmp1.getEighth()), Integer.parseInt(tmp1.getNineteen()), tmp1.getNineth(),
                                            tmp1.getEleventh(), tmp1.getSixteen(), tmp1.getTenth(), tmp1.getTwenty(),
                                            Integer.parseInt(tmp1.getSeventeen()), tmp1.getEighteen()));
                                    if (Contact_Status > 0) {
                                        userfound = true;
                                    }
                                }
                                if (userfound) {
                                    LoginDetail.setPhoneNo(mobile);
                                    Datastorage.SetPhoneNumber(OTPNewActivity.this, mobile);
                                    if (tmp.getListArray().size() > 1) {
                                        Intent intent = new Intent(OTPNewActivity.this, SelectAccountActivity.class);
                                        intent.putExtra("AcArray", mAccount);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                        onClickAnimation();
                                    } else {
                                        studId = tmp.getListArray().get(0).getSecond();
                                        schoolId = tmp.getListArray().get(0).getThird();
                                        LoginDetail.setSchoolId(schoolId);
                                        LoginDetail.setUserId(tmp.getListArray().get(0).getNineteen());
                                        LoginDetail.setStudId(studId);
                                        Datastorage.SetUserId(OTPNewActivity.this, tmp.getListArray().get(0).getNineteen());
                                        Datastorage.SetSchoolId(OTPNewActivity.this, schoolId);
                                        Datastorage.SetStudentID(OTPNewActivity.this, studId);
                                        LoginDetail.setCurrentYearId(tmp.getListArray().get(0).getSixth());
                                        LoginDetail.setClassId(tmp.getListArray().get(0).getSeventh());
                                        LoginDetail.setClassSecId(tmp.getListArray().get(0).getEighth());
                                        LoginDetail.setClassName(tmp.getListArray().get(0).getEighteen());
                                        LoginDetail.setStudentEnrollDate(tmp.getListArray().get(0).getEleventh());
                                        LoginDetail.setLastUpdatedTime(tmp.getListArray().get(0).getSixteen());
                                        LoginDetail.setAcademicyear(tmp.getListArray().get(0).getTenth());
                                        LoginDetail.setHolidayStartDate(tmp.getListArray().get(0).getTwelth());
                                        LoginDetail.setHolidayEndDate(tmp.getListArray().get(0).getThirteen());
                                        Datastorage.SetStudentName(OTPNewActivity.this, tmp.getListArray().get(0).getFifth());
                                        Datastorage.SetClassId(OTPNewActivity.this, tmp.getListArray().get(0).getSeventh());
                                        Datastorage.SetClassSecId(OTPNewActivity.this, tmp.getListArray().get(0).getEighth());
                                        Datastorage.SetEnrollDate(OTPNewActivity.this, tmp.getListArray().get(0).getEleventh());
                                        Datastorage.LastUpdatedtime(OTPNewActivity.this, tmp.getListArray().get(0).getSixteen());
                                        Datastorage.SetCurrentYearId(OTPNewActivity.this, tmp.getListArray().get(0).getSixth());
                                        Datastorage.SetAcademicYear(OTPNewActivity.this, tmp.getListArray().get(0).getTenth());
                                        Datastorage.AcaStartDate(OTPNewActivity.this, tmp.getListArray().get(0).getFourteen());
                                        Datastorage.AcaEndDate(OTPNewActivity.this, tmp.getListArray().get(0).getFifteen());
                                        Datastorage.SetHolidayStartDate(OTPNewActivity.this, tmp.getListArray().get(0).getTwelth());
                                        Datastorage.SetHolidayEndDate(OTPNewActivity.this, tmp.getListArray().get(0).getThirteen());
                                        Datastorage.SetRouteId(OTPNewActivity.this, Integer.parseInt(tmp.getListArray().get(0).getSeventeen()));
                                        Datastorage.SetClassSectionName(OTPNewActivity.this, tmp.getListArray().get(0).getEighteen());

                                        int Set_Default = db.UpdateContactRecordSetDefaultOne(studId, schoolId);
                                        Datastorage.SetLastAutoUpdateExamDay(OTPNewActivity.this, 0);
                                        Datastorage.SetLastAutoUpdateMessageDay(OTPNewActivity.this, 0);
                                        if (Set_Default == 1) {
                                            RedirectToDefaultStudentAccount(studId, schoolId);
                                        } else {
                                            Toast.makeText(OTPNewActivity.this, "Please try again.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            } else {
                                if (tmp != null && tmp.getStrResult() != null && !tmp.getStrResult().isEmpty())
                                    Common.showToast(OTPNewActivity.this, tmp.getStrResult());
                            }
                        } catch (Exception ex) {
                            Constants.writelog(tag, "GetDetails():onResponse:475:" + ex.getMessage());
                        } finally {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<AppService> call, Throwable t) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            } else {
                Common.showToast(OTPNewActivity.this, "No Interenet Connection.");
            }
        } catch (Exception ex) {
            Constants.writelog(tag, "GetDetails()::405:" + ex.getMessage());
        }
    }

    public void RedirectToDefaultStudentAccount(String studId, String SchoolId) {
        try {
            Datastorage.SetSchoolId(OTPNewActivity.this, SchoolId);
            Datastorage.SetStudentID(OTPNewActivity.this, studId);
            Constants.ResetLastAutoUpdateDay(OTPNewActivity.this);
            Intent intent = new Intent(OTPNewActivity.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            onClickAnimation();
            String stud_details = db.GetStudentAccountDetails(OTPNewActivity.this,
                    Integer.parseInt(studId), Integer.parseInt(SchoolId));
            String[] parts = stud_details.split(",");
            if (parts != null && parts.length > 7 && !parts[8].isEmpty()) {
                Datastorage.SetStudentName(OTPNewActivity.this, parts[8]);
            } else {
                Datastorage.SetStudentName(OTPNewActivity.this, "");
            }
            db.SetCurrentYearAsDefaultYear(studId, SchoolId);
            int yearid = db.GetCurrentAcademicYearId(
                    Integer.parseInt(SchoolId), Integer.parseInt(studId));
            Datastorage.SetAcademicYear(OTPNewActivity.this, yearid + "");
        } catch (Exception ex) {
            Constants.writelog("AccountListActivity", "Ex 370:" + ex.getMessage() + "::::::" + ex.getStackTrace());
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
        } catch (Exception ex) {
            Constants.writelog(tag, "recivedSms() 312 :OPT Set in the EditText:OTP:" + message + ": Error: " + ex.getMessage());
        }
    }
/*

    public class ResendTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            ringProgressDialog = ProgressDialog.show(OTPNewActivity.this, "", "Loading...", true);
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
                request.addProperty("yearid", Datastorage.GetCurrentYearId(OTPNewActivity.this));
                SoapObject result = Constants.CallWebMethod(
                        OTPNewActivity.this, request, Constants.GENERATED_AND_SEND_OTP, true);
                */
/*if (result != null && result.getPropertyCount() > 0) {
                    serverOtp = result.getProperty(0).toString();
                }*//*

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
                getLogInWithOtp();
                coudownstart();
                btnresend.setClickable(false);
                //btnresend.setBackgroundColor(getResources().getColor(R.color.lock));
                btnresend.setTextColor(getResources().getColor(R.color.bolgque));
                Toast.makeText(OTPNewActivity.this, "Send OTP Successfully.", Toast.LENGTH_SHORT).show();
               */
/* if (!serverOtp.isEmpty()) {

                } else {
                    Toast.makeText(OTPNewActivity.this, "Please Try Again.", Toast.LENGTH_SHORT).show();
                }*//*

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
*/

    private void getLogInWithOtp() {
        try {
            String mDeviceid = "", mPlateform = "";
            try {
                mDeviceid = Build.ID;
                mPlateform = Build.VERSION.RELEASE;
            } catch (Exception ex) {

            }

            String isdebug = "0";
            if (BuildConfig.DEBUG) {
                isdebug = "1";
            }

            if (isOnline()) {
                mProgressBar.setVisibility(View.VISIBLE);
                Call<AppService> mCall = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                        .GetOTPDetail(mobile, String.valueOf(SchoolDetails.appname), isdebug, mDeviceid, mPlateform, Constants.CODEVERSION);
                mCall.enqueue(new retrofit2.Callback<AppService>() {
                    @Override
                    public void onResponse(Call<AppService> call, Response<AppService> response) {
                        try {
                            AppService tmp = response.body();
                            if (tmp != null && tmp.getResponse() != null && tmp.getResponse().equalsIgnoreCase("1")) {
                                coudownstart();
                                btnresend.setClickable(false);
                                //btnresend.setBackgroundColor(getResources().getColor(R.color.lock));
                                btnresend.setTextColor(getResources().getColor(R.color.bolgque));
                                Toast.makeText(OTPNewActivity.this, "Send OTP Successfully.", Toast.LENGTH_SHORT).show();
                            } else {
                                if (tmp != null && tmp.getStrResult() != null && !tmp.getStrResult().isEmpty()) {
                                    Common.showToast(OTPNewActivity.this, tmp.getStrResult());
                                } else {
                                    Common.showToast(OTPNewActivity.this, "Please Try Again.");
                                }
                            }
                        } catch (Exception ex) {
                            Constants.writelog(tag, "getLogInWithOtp:162:" + ex.getMessage());
                        } finally {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<AppService> call, Throwable t) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            } else {
                Common.showToast(OTPNewActivity.this, "No Interenet Connection.");
            }
        } catch (Exception ex) {
            Constants.writelog(tag, "getLogInWithOtp:162:" + ex.getMessage());
        }
    }
}