package com.expedite.apps.kumkum.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.BuildConfig;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.common.MyTextWatcher;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.AppService;
import com.expedite.apps.kumkum.model.Contact;
import com.expedite.apps.kumkum.model.LoginDetail;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class LoginNewActivity extends BaseActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private String tag = "LoginNewActivity";
    private EditText mEdtPinNo, mEdtMobileNumber;
    private TextView mBtnLogin, mBtnExit, mBtnLoginOTP;
    private String Entered_Pin = "", Entered_PhoneNo = "", Schl_Id = "", User_Id = "", studid = "", yearid = "", classid = "";
    private String classsecid = "", classname = "", studentenrolldate = "", lastupdatedtime = "", Name = "",
            academicyear = "", Academic_Year_Start_Date = "", Academic_Year_End_Date = "";
    private String Holiday_Start_Date = "", Holiday_End_Date = "", updatedtime = "", ClassSecName = "";
    private int RouteId = 0;
    private TextInputLayout mInputEdtNumber, mInputEdtPin;
    private ProgressBar mProgressBar;
    private String regId = "", mResponseFlag;
    private ImageView mImgHidden;
    private int mMultiClickCount = 0;
    private int RESOLVE_HINT = 2;
    GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();
        getHintPhoneNumber();
        try {
            init();
            initListner();
            addAutoStartup();
        } catch (Exception ex) {
        }
    }

    public void getHintPhoneNumber() {
        HintRequest hintRequest =
                new HintRequest.Builder()
                        .setPhoneNumberIdentifierSupported(true)
                        .build();
        PendingIntent mIntent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(mIntent.getIntentSender(), RESOLVE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result if we want hint number
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == Activity.RESULT_OK) {

                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                // credential.getId();  <-- will need to process phone number string
                mEdtMobileNumber.setText(credential.getId().substring(3));
            }
        }
    }

    public void init() {
        try {
            //Constants.setActionbar(getSupportActionBar(), LoginNewActivity.this, LoginNewActivity.this,
            //SchoolDetails.SchoolName, "LoginNewActivity");
            db = new DatabaseHandler(LoginNewActivity.this);
            mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
            mEdtPinNo = (EditText) findViewById(R.id.edtPinNumber);
            mEdtMobileNumber = (EditText) findViewById(R.id.edtMobileNumber);
            mInputEdtNumber = (TextInputLayout) findViewById(R.id.input_edt_number);
            mEdtMobileNumber.addTextChangedListener(new MyTextWatcher(mInputEdtNumber));
            mInputEdtPin = (TextInputLayout) findViewById(R.id.input_edt_pin);
            mEdtPinNo.addTextChangedListener(new MyTextWatcher(mInputEdtPin));
            mBtnLogin = (TextView) findViewById(R.id.btnLogin);
            mBtnLoginOTP = (TextView) findViewById(R.id.btnLoginotp);
            mBtnExit = (TextView) findViewById(R.id.btnExit);
            mImgHidden = (ImageView) findViewById(R.id.imgHidden);

            regId = FirebaseInstanceId.getInstance().getToken();

            //   regId = GCMRegistrar.getRegistrationId(LoginNewActivity.this);
            if (regId!=null && regId!="" && regId.isEmpty()) {
                //GCMRegistrar.register(LoginNewActivity.this, Constants.SENDER_ID);
            }



            checkpermissionstatus();
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    private void initListner() {
        mBtnLogin.setOnClickListener(this);
        mBtnLoginOTP.setOnClickListener(this);
        mBtnExit.setOnClickListener(this);
        mImgHidden.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            Intent intent;
            switch (v.getId()) {
                case R.id.btnLogin:
                    if (isOnline()) {
                        try {
                            if (regId!=null && regId.equals("")) {
                                Constants.writelog("LoginNewActivity", "163FCM Not Registered.");
                                //GCMRegistrar.register(LoginNewActivity.this, Constants.SENDER_ID);
                            }



                            Entered_Pin = mEdtPinNo.getText().toString();
                            Entered_PhoneNo = mEdtMobileNumber.getText().toString();
                            if (!PhoneNumberValidation()) {
                                return;
                            } else if (!PinValidation()) {
                                return;
                            } else {
                                new getLogIn().execute();
                            }
                        } catch (Exception err) {
                            Common.printStackTrace(err);
                        }
                    } else {
                        Common.showToast(LoginNewActivity.this, "Please connect to working Internet connection");
                    }
                    break;

                case R.id.btnLoginotp:
                    Common.startSmsRecevier(LoginNewActivity.this);
                    if (isOnline()) {
                        try {
                            if (regId!=null && regId.equals("")) {
                                Constants.writelog("LoginNewActivity", "163 FCM Not Registered.");
                                //GCMRegistrar.register(LoginNewActivity.this, Constants.SENDER_ID);
                            }



                            //checkpermissionstatus();
                            Entered_PhoneNo = mEdtMobileNumber.getText().toString();
                            if (!PhoneNumberValidation()) {
                                return;
                            } else {
                                getLogInWithOtp();
                            }
                        } catch (Exception err) {
                            Common.printStackTrace(err);
                        }
                    } else {
                        Common.showToast(LoginNewActivity.this, "Please connect to working Internet connection");
                    }
                    break;

                case R.id.btnExit:
                    try {
                        intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } catch (Exception err) {
                        Common.printStackTrace(err);
                    }
                    break;
                case R.id.imgHidden:
                    try {
                        if (mMultiClickCount > 5) {
                            intent = new Intent(LoginNewActivity.this, LoginActivity.class);
                            startActivity(intent);
                            onClickAnimation();
                            finish();
                        }
                        mMultiClickCount++;
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                mMultiClickCount = 0;
                            }
                        }, 3000);


                    } catch (Exception err) {
                        Common.printStackTrace(err);
                    }
                    break;

                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


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
                        .GetOTPDetail(Entered_PhoneNo, String.valueOf(SchoolDetails.appname), isdebug, mDeviceid, mPlateform, Constants.CODEVERSION);
                mCall.enqueue(new retrofit2.Callback<AppService>() {
                    @Override
                    public void onResponse(Call<AppService> call, Response<AppService> response) {
                        try {
                            AppService tmp = response.body();
                            if (tmp != null && tmp.getResponse() != null && tmp.getResponse().equalsIgnoreCase("1")) {
                                //String otp = tmp.getStrResult();
                                Intent intent = new Intent(LoginNewActivity.this, OTPNewActivity.class);
                                //intent.putExtra("otp", String.valueOf(otp));
                                intent.putExtra("mobile", String.valueOf(Entered_PhoneNo));
                                startActivity(intent);
                                onClickAnimation();
                            } else {
                                if (tmp != null && tmp.getStrResult() != null && !tmp.getStrResult().isEmpty())
                                    Common.showToast(LoginNewActivity.this, tmp.getStrResult());
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
                Common.showToast(LoginNewActivity.this, "No Interenet Connection.");
            }
        } catch (Exception ex) {
            Constants.writelog(tag, "getLogInWithOtp:162:" + ex.getMessage());
        }
    }

    private class getLogIn extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            CheckLoginPin();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (mResponseFlag != null && mResponseFlag.equals("true")) {
                    mProgressBar.setVisibility(View.GONE);
                    try {
                        if (Constants.isShowInternetMsg) {
                            Constants.NotifyNoInternet(LoginNewActivity.this);
                        } else {
                            Constants.Logwrite("Login:AddDetails:", "Name:" + Name);
                            Constants.Logwrite("Login:AddDetails:", "Entered_PhoneNo:" + Entered_PhoneNo);
                            Constants.Logwrite("Login:AddDetails:", "Entered_Pin:" + Entered_Pin);
                            Constants.Logwrite("Login:AddDetails:", "studid:" + studid);
                            Constants.Logwrite("Login:AddDetails:", "SchlId:" + Schl_Id);
                            Constants.Logwrite("Login:AddDetails:", "yearid:" + yearid);
                            Constants.Logwrite("Login:AddDetails:", "classid:" + classid);
                            Constants.Logwrite("Login:AddDetails:", "classsecid:" + classsecid);
                            Constants.Logwrite("Login:AddDetails:", "User_Id:" + User_Id);
                            Constants.Logwrite("Login:AddDetails:", "classname:" + classname);
                            Constants.Logwrite("Login:AddDetails:", "studentenrolldate:" + studentenrolldate);
                            Constants.Logwrite("Login:AddDetails:", "lastupdatedtime:" + lastupdatedtime);
                            Constants.Logwrite("Login:AddDetails:", "academicyear:" + academicyear);
                            Constants.Logwrite("Login:AddDetails:", "Class Section name:" + ClassSecName);

                            int Contact_Status = db.addContact(new Contact(
                                    Name, Entered_PhoneNo, Integer
                                    .parseInt(Entered_Pin), 1, Integer
                                    .parseInt(studid), Integer
                                    .parseInt(Schl_Id), Integer
                                    .parseInt(yearid), Integer
                                    .parseInt(classid), Integer
                                    .parseInt(classsecid), Integer
                                    .parseInt(User_Id), classname,
                                    studentenrolldate, lastupdatedtime,
                                    academicyear, updatedtime, RouteId,
                                    ClassSecName));


                            if (Contact_Status > 0) {
                                Constants.Logwrite("LoginPage:onPostExecute", "InsertStatus:" + Contact_Status);
                                Intent intent = new Intent(LoginNewActivity.this, SplashActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                onClickAnimation();
                            } else {
                                Constants.Logwrite("LoginPage:onPostExecute", "InsertStatus:" + Contact_Status);
                            }
                        }
                    } catch (Exception err) {
                        Constants.Logwrite("LoginPage:onPostExecute",
                                "Exception" + err.getMessage() + ":::StactTrace:" + err.getStackTrace().toString());
                    }
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginNewActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("you have entered wrong mobile number or pin");
                    alertDialog.setIcon(R.drawable.alert);
                    alertDialog.setCancelable(false);
                    alertDialog.setButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // mEdtPinNo.setText("");
                                    // mEdtMobileNumber.setText("");
                                }
                            });
                    alertDialog.show();
                }
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                Constants.Logwrite("LoginPage:onPostExecute", err.getStackTrace().toString());
                mProgressBar.setVisibility(View.GONE);
            }
            super.onPostExecute(result);
        }

        private String CheckLoginPin() {
            String mDeviceInfo = "";
            try {
                mDeviceInfo = Build.DEVICE + "|||" + Build.MODEL + "|||" + Build.ID
                        + "|||" + Build.PRODUCT + "|||" + Build.VERSION.SDK
                        + "|||" + Build.VERSION.RELEASE + "|||" + Build.VERSION.INCREMENTAL;
                Constants.Logwrite("Login:", mDeviceInfo);
            } catch (Exception err) {
                Constants.Logwrite("Login:", err.getMessage());
            }

            try {
                SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.CHECK_LOGIN_PIN1);
                request.addProperty("PhoneNo", Entered_PhoneNo);
                request.addProperty("LogPin", Integer.parseInt(Entered_Pin));
                request.addProperty("Devicedetails", mDeviceInfo);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;

                SoapObject result = Constants.CallWebMethod(
                        LoginNewActivity.this, request, Constants.CHECK_LOGIN_PIN1, true);
                if (result != null && result.getPropertyCount() > 0) {
                    mResponseFlag = result.getPropertyAsString(0);
                    if (mResponseFlag != null && !mResponseFlag.equals("")) {
                        String[] resultParts = mResponseFlag.split(",");
                        if (resultParts.length > 1 && resultParts[1].equalsIgnoreCase("0")) {
                            Datastorage.setSessionBoolean(LoginNewActivity.this, Datastorage.isFirstTime, true);
                        }
                        mResponseFlag = resultParts[0];
                        if (mResponseFlag.equalsIgnoreCase("true")) {
                            LoginDetail.setPhoneNo(Entered_PhoneNo);
                            Datastorage.SetPhoneNumber(LoginNewActivity.this, Entered_PhoneNo);
                            LoginDetail.setLogPin(Integer.parseInt(Entered_Pin));
                            Datastorage.SetLoginPin(LoginNewActivity.this, Entered_Pin);
                            String info = GetUserDetailsKumKumvehicle();
                            String[] parts = info.split(",");
                            Constants.Logwrite("LoginNewActivity:StudentDetails", "StringLength:" + parts.length);
                            Schl_Id = parts[0];
                            Constants.Logwrite("LoginNewActivity:StudentDetails", "SchoolId:" + Schl_Id);
                            User_Id = parts[1];
                            Constants.Logwrite("LoginNewActivity:StudentDetails", "UserId:" + User_Id);
                            studid = parts[2];
                            Constants.Logwrite("LoginNewActivity:StudentDetails", "StudentId:" + studid);
                            yearid = parts[3];
                            Constants.Logwrite("LoginNewActivity:StudentDetails", "YearId:" + yearid);
                            classid = parts[4];
                            Constants.Logwrite("LoginNewActivity:StudentDetails", "ClassId:" + classid);
                            classsecid = parts[5];
                            Constants.Logwrite("LoginNewActivity:StudentDetails", "ClassSecid:" + classsecid);
                            classname = parts[6];
                            Constants.Logwrite("LoginNewActivity:StudentDetails", "Classname:" + classname);
                            studentenrolldate = parts[7];
                            Constants.Logwrite("LoginNewActivity:StudentDetails", "studentenrolldate:" + studentenrolldate);
                            academicyear = parts[9];
                            Constants.Logwrite("LoginNewActivity:StudentDetails", "academicyear:" + academicyear);
                            Name = parts[8];
                            Constants.Logwrite("LoginNewActivity:StudentDetails", "Name:" + Name);
                            lastupdatedtime = parts[14];
                            Constants.Logwrite("LoginNewActivity:StudentDetails", "LastUpdatedTime:" + lastupdatedtime);
                            updatedtime = parts[15];
                            Constants.Logwrite("LoginNewActivity:StudentDetails", "updatedtime:" + updatedtime);
                            RouteId = Integer.parseInt(parts[16]);
                            try {
                                ClassSecName = parts[17];
                                Constants.Logwrite("LoginNewActivity", "ClassSecName: " + ClassSecName);
                            } catch (Exception ex) {
                                Constants.Logwrite("LoginNewActivity",
                                        "CheckLoginPin ClassSecName not found 462: " + ex.getMessage() + ":::::");
                                Constants.writelog("LoginNewActivity",
                                        "CheckLoginPin ClassSecName not found 462: " + ex.getMessage() + ":::::");
                            }

                            Academic_Year_Start_Date = parts[10];
                            Constants.Logwrite("LoginNewActivity:StudentDetails", "Academic_Year_Start_Date:" + Academic_Year_Start_Date);
                            Academic_Year_End_Date = parts[11];
                            Constants.Logwrite("LoginNewActivity:StudentDetails", "Academic_Year_End_Date:" + Academic_Year_End_Date);
                            Holiday_Start_Date = parts[12];
                            Constants.Logwrite("LoginNewActivity:StudentDetails", "Holiday_Start_Date:" + Holiday_Start_Date);
                            Holiday_End_Date = parts[13];
                            Constants.Logwrite("LoginNewActivity:StudentDetails", "Holiday_End_Date:" + Holiday_End_Date);
                            // int schoolid = (int)(SchoolId.toString());
                            LoginDetail.setSchoolId(Schl_Id);
                            LoginDetail.setUserId(User_Id);
                            LoginDetail.setStudId(studid);
                            Datastorage.SetUserId(LoginNewActivity.this, User_Id);
                            Datastorage.SetSchoolId(LoginNewActivity.this, Schl_Id);
                            Datastorage.SetStudentID(LoginNewActivity.this, studid);
                            // String stud_other_det = GetStudentDetails();
                            // String[] det = stud_other_det.split(",");
                            LoginDetail.setCurrentYearId(yearid);
                            LoginDetail.setClassId(classid);
                            LoginDetail.setClassSecId(classsecid);
                            LoginDetail.setClassName(classname);
                            LoginDetail.setStudentEnrollDate(studentenrolldate);
                            LoginDetail.setLastUpdatedTime(lastupdatedtime);
                            LoginDetail.setAcademicyear(academicyear);
                            LoginDetail.setAcademicYearStartDate(Academic_Year_Start_Date);
                            LoginDetail.setAcademicYearEndDate(Academic_Year_End_Date);
                            LoginDetail.setHolidayStartDate(Holiday_Start_Date);
                            LoginDetail.setHolidayEndDate(Holiday_End_Date);
                            Datastorage.SetStudentName(LoginNewActivity.this, Name);
                            Datastorage.SetClassId(LoginNewActivity.this, classid);
                            Datastorage.SetClassSecId(LoginNewActivity.this, classsecid);
                            Datastorage.SetEnrollDate(LoginNewActivity.this, studentenrolldate);
                            Datastorage.LastUpdatedtime(LoginNewActivity.this, lastupdatedtime);
                            Datastorage.SetCurrentYearId(LoginNewActivity.this, yearid);
                            Datastorage.SetAcademicYear(LoginNewActivity.this, academicyear);
                            Datastorage.AcaStartDate(LoginNewActivity.this, Academic_Year_Start_Date);
                            Datastorage.AcaEndDate(LoginNewActivity.this, Academic_Year_End_Date);
                            Datastorage.SetHolidayStartDate(LoginNewActivity.this, Holiday_Start_Date);
                            Datastorage.SetHolidayEndDate(LoginNewActivity.this, Holiday_End_Date);
                            Datastorage.SetRouteId(LoginNewActivity.this, RouteId);
                            Datastorage.SetClassSectionName(LoginNewActivity.this, ClassSecName);
                            // String last_upd_time = GetLastUpdatedTime();
                            // LoginDetail.setLastUpdatedTime(last_upd_time);
                            // Get User Imp Details
                            LogSucessfullyLogin();
                            // Check for Device Register or not
                            String Is_Device_Register = Constants.CheckDeviceRegistration(LoginNewActivity.this);

                            if (Is_Device_Register.equals("true")) {
                                // Check for GCM Registration
                                String Is_Device_GCM_Done = Constants.CheckGCMRegistrationofDevice(LoginNewActivity.this);
                                if (Is_Device_GCM_Done.equals("true")) {
                                    String IsUserRegister = Constants.CheckUserRegistered(Integer.parseInt(LoginDetail.getStudId()),
                                            Integer.parseInt(LoginDetail.getSchoolId()), LoginNewActivity.this);
                                    if (!IsUserRegister.equals("true"))
                                        Constants.InsertNewAccountGCMData(Integer.parseInt(LoginDetail.getStudId()), Integer.parseInt(LoginDetail.getSchoolId()),
                                                LoginNewActivity.this);
                                } else {
                                    Constants.UpdateGCMRegistration(LoginNewActivity.this);
                                }
                            } else {
                                String Reg_Done = Constants.InsertGCMRegistration(LoginNewActivity.this);
                                if (Reg_Done.equals("true")) {
                                    String IsUserRegister = Constants
                                            .CheckUserRegistered(Integer.parseInt(LoginDetail.getStudId()),
                                                    Integer.parseInt(LoginDetail.getSchoolId()), LoginNewActivity.this);
                                    if (!IsUserRegister.equals("true"))
                                        Constants.InsertNewAccountGCMData(
                                                Integer.parseInt(LoginDetail.getStudId()), Integer
                                                        .parseInt(LoginDetail.getSchoolId()), LoginNewActivity.this);
                                    // Constants.InsertNewAccountGCMData(Integer.parseInt(LoginDetail.getStudId()),Integer.parseInt(LoginDetail.getSchoolId()));
                                }
                            }
                            mResponseFlag = "true";
                        } else {
                            mResponseFlag = "false";
                        }
                    } else {
                        mResponseFlag = "false";
                    }
                } else {
                    mResponseFlag = "false";
                }
            } catch (Exception err) {
                Constants.Logwrite("Login Page:", "Exception:" + err.getMessage());
                return mResponseFlag;
            }
            return mResponseFlag;
        }

        private String GetUserDetailsKumKumvehicle() {
            SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_USER_DETAILS_VEHICLE);
            request.addProperty("PhoneNo", Entered_PhoneNo);
            request.addProperty("Log_pin", Integer.parseInt((mEdtPinNo.getText().toString())));
            request.addProperty("YearId", 0);
            request.addProperty("studid", 0);
            try {
                SoapObject result = Constants.CallWebMethod(
                        LoginNewActivity.this, request, Constants.GET_USER_DETAILS_VEHICLE,
                        true);
                if (result != null && result.getPropertyCount() > 0) {
                    mResponseFlag = result.getPropertyAsString(0);
                    // String[] parts = info.split(",");
                    Constants.Logwrite("LoginPage:GetUserDetailsKumKum", "ResPonsString:" + mResponseFlag);
                    // return mResponseFlag;
                }
            } catch (Exception e) {
                Constants.writelog("LoginNewActivity",
                        "GetUserDetailsKumKumvehicle 654 " + e.getMessage());
                // send.Sendemail(e.getMessage(), LoginDetail.getPhoneNo());
                // return mResponseFlag;
            }
            return mResponseFlag;
        }

        private void LogSucessfullyLogin() {
            SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.LOG_SUCESSFULLY_LOGIN);
            request.addProperty("schoolid", LoginDetail.getSchoolId());
            request.addProperty("User_Id", LoginDetail.getUserId());
            request.addProperty("Phone", LoginDetail.getPhoneNo());
            try {
                SoapObject result = Constants.CallWebMethod(
                        LoginNewActivity.this, request, Constants.LOG_SUCESSFULLY_LOGIN, true);
                if (result != null && result.getPropertyCount() > 0) {
                    // String info = result.getPropertyAsString(0);
                    // String[] parts = info.split(",");
                    // mResponseFlag = info;
                    // return mResponseFlag;
                }
            } catch (Exception err) {
                Constants.Logwrite("Login Page:",
                        "Exception:LogSucessfullyLogin" + err.getMessage());
            }

        }
    }

    private boolean PhoneNumberValidation() {
        boolean isvalidate = true;
        if (mEdtMobileNumber.getText().toString().trim().length() <= 0) {
            mInputEdtNumber.setError("Please Enter Mobile Number");
            Constants.requestFocus(LoginNewActivity.this, mEdtMobileNumber);
            isvalidate = false;
        } else if (mEdtMobileNumber.getText().toString().length() < 10) {
            mInputEdtNumber.setError("Please Enter Valid Mobile Number");
            Constants.requestFocus(LoginNewActivity.this, mEdtMobileNumber);
            isvalidate = false;
            try {
                int number = Integer.parseInt(mEdtMobileNumber.getText().toString().trim());
            } catch (Exception ex) {
                mInputEdtNumber.setError("Please Enter Valid Mobile Number");
                Constants.requestFocus(LoginNewActivity.this, mEdtMobileNumber);
                isvalidate = false;
            }
        }

        return isvalidate;
    }

    private boolean PinValidation() {
        boolean isvalidate = true;
        if (mEdtPinNo.getText().toString().trim().length() <= 0) {
            //  mInputEdtPin.setError("Please Enter Pin");
            Constants.requestFocus(LoginNewActivity.this, mEdtPinNo);
            isvalidate = true;
        } else if (mEdtPinNo.getText().toString().length() < 6) {
            mInputEdtPin.setError("Please Enter Valid Login Pin");
            Constants.requestFocus(LoginNewActivity.this, mEdtPinNo);
            isvalidate = true;
            try {
                int number = Integer.parseInt(mEdtPinNo.getText().toString().trim());
            } catch (Exception ex) {
                mInputEdtPin.setError("Please Enter Valid Login Pin");
                Constants.requestFocus(LoginNewActivity.this, mEdtPinNo);
                isvalidate = true;
            }
        }
        return isvalidate;
    }

    public void checkpermissionstatus() {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                Boolean isadded = false;
                String permissions = "";
                int permissionStatus = 0;
                permissionStatus = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                    permissions += android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
                }

                /*permissionStatus = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                    isanyDeny = true;
                    isadded = true;
                    if (isadded) {
                        permissions += "," + android.Manifest.permission.READ_EXTERNAL_STORAGE;
                    } else {
                        permissions += android.Manifest.permission.READ_EXTERNAL_STORAGE;
                    }
                }

                permissionStatus = checkSelfPermission(Manifest.permission.READ_SMS);
                if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                    isadded = true;
                    if (isadded) {
                        permissions += "," + android.Manifest.permission.READ_SMS;
                    } else {
                        permissions += android.Manifest.permission.READ_SMS;
                    }
                }*/
                if (!permissions.equals("")) {
                    androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(this);
                    alertDialog.setTitle("App Permission");
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("This app required storage permission for better performance.");
                    alertDialog.setPositiveButton("Continue",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        if (Build.VERSION.SDK_INT >= 23) {
                                            int permissionStatus1 = 0;
                                            String permissions1 = "";
                                            Boolean isadded1 = false;
                                            permissionStatus1 = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                            if (permissionStatus1 != PackageManager.PERMISSION_GRANTED) {
                                                permissions1 += android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
                                            }
                                            /*permissionStatus1 = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                                            if (permissionStatus1 != PackageManager.PERMISSION_GRANTED) {

                                                isadded1 = true;
                                                if (isadded1) {
                                                    permissions1 += "," + android.Manifest.permission.READ_EXTERNAL_STORAGE;
                                                } else {
                                                    permissions1 += android.Manifest.permission.READ_EXTERNAL_STORAGE;
                                                }
                                            }
                                            permissionStatus1 = checkSelfPermission(Manifest.permission.READ_SMS);
                                            if (permissionStatus1 != PackageManager.PERMISSION_GRANTED) {

                                                isadded1 = true;
                                                if (isadded1) {
                                                    permissions1 += "," + android.Manifest.permission.READ_SMS;
                                                } else {
                                                    permissions1 += android.Manifest.permission.READ_SMS;
                                                }
                                            }*/
                                            String[] perm = permissions1.split(",");
                                            ActivityCompat.requestPermissions(LoginNewActivity.this, perm, 1);
                                        }
                                    } catch (Exception ex) {
                                        Log.e(tag, "alertDialog onClick():YES: Error:172:" + ex.getMessage());
                                        Constants.writelog(tag, "alertDialog onClick():YES: Error:172:" + ex.getMessage());
                                    }
                                }
                            });
                    alertDialog.show();
                }
            }
        } catch (Exception ex) {
            Constants.writelog(tag, "checkpermissionstatus::263:" + ex.getMessage());
        }
    }

    private void addAutoStartup() {
        try {
            boolean isDisplay = false;
            String manufacturer1 = android.os.Build.MANUFACTURER;
            if (manufacturer1.equalsIgnoreCase("xiaomi") ||
                    manufacturer1.equalsIgnoreCase("oppo") ||
                    manufacturer1.equalsIgnoreCase("vivo") ||
                    manufacturer1.equalsIgnoreCase("Letv") ||
                    manufacturer1.equalsIgnoreCase("Honor") ||
                    manufacturer1.equalsIgnoreCase("Realme") ||
                    manufacturer1.equalsIgnoreCase("oneplus")) {
                isDisplay = true;
            }
            if (isDisplay) {
                //common.setSession(ConstValue.AUTOSTARTUP_PERMISSION_KEY, "true");
                androidx.appcompat.app.AlertDialog.Builder alertDialog = new
                        androidx.appcompat.app.AlertDialog.Builder(this, R.style.appCompatDialog);
                alertDialog.setTitle("App permission");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("This app required auto start permission to receive notification on time.");
                alertDialog.setPositiveButton("Continue",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Intent intent = new Intent();
                                    String manufacturer = android.os.Build.MANUFACTURER;
                                    if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                                        intent.setComponent(new ComponentName("com.miui.securitycenter",
                                                "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                                    } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                                        intent.setComponent(new ComponentName("com.coloros.safecenter",
                                                "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
                                    } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                                        intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                                                "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                                    } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                                        intent.setComponent(new ComponentName("com.letv.android.letvsafe",
                                                "com.letv.android.letvsafe.AutobootManageActivity"));
                                    } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                                        intent.setComponent(new ComponentName("com.huawei.systemmanager",
                                                "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                                    } else if ("oneplus".equalsIgnoreCase(manufacturer)) {
                                        intent.setComponent(new ComponentName("com.oneplus.security",
                                                "com.oneplus.security.chainlaunch.view.ChainLaunchAppListAct‌​ivity"));
                                    } else if ("realme".equalsIgnoreCase(manufacturer)) {
                                        intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                                    }
                                    List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                                    if (list.size() > 0) {
                                        startActivity(intent);
                                    }
                                } catch (Exception ex) {
                                    Constants.writelog(tag, "alertDialog onClick(712):YES: Error:712:" + ex.getMessage());
                                }
                            }
                        });
                alertDialog.show();
            }
        } catch (Exception e) {
            Log.e("exc", String.valueOf(e));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}




