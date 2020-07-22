package com.expedite.apps.kumkum.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.common.MyTextWatcher;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.Contact;
import com.expedite.apps.kumkum.model.LoginDetail;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class LoginActivity extends BaseActivity implements OnClickListener {
    private EditText mEdtPinNo, mEdtMobileNumber;
    private TextView mBtnLogin, mBtnExit;
    private String Entered_Pin = "", Entered_PhoneNo = "", Schl_Id = "", User_Id = "", studid = "", yearid = "", classid = "";
    private String classsecid = "", classname = "", studentenrolldate = "", lastupdatedtime = "", Name = "",
            academicyear = "", Academic_Year_Start_Date = "", Academic_Year_End_Date = "";
    private String Holiday_Start_Date = "", Holiday_End_Date = "", updatedtime = "", ClassSecName = "";
    private int RouteId = 0;
    private TextInputLayout mInputEdtNumber, mInputEdtPin;
    private ProgressBar mProgressBar;
    private String regId = "", mResponseFlag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_apps);
        init();
        initListner();
    }

    public void init() {
        try {
            //  Constants.setActionbar(getSupportActionBar(), LoginActivity.this, LoginActivity.this,
            //  SchoolDetails.SchoolName, "LoginActivity");
            db = new DatabaseHandler(LoginActivity.this);
            mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
            mEdtPinNo = (EditText) findViewById(R.id.edtPinNumber);
            mEdtMobileNumber = (EditText) findViewById(R.id.edtMobileNumber);
            mInputEdtNumber = (TextInputLayout) findViewById(R.id.input_edt_number);
            mEdtMobileNumber.addTextChangedListener(new MyTextWatcher(mInputEdtNumber));
            mInputEdtPin = (TextInputLayout) findViewById(R.id.input_edt_pin);
            mEdtPinNo.addTextChangedListener(new MyTextWatcher(mInputEdtPin));
            mBtnLogin = (TextView) findViewById(R.id.btnLogin);
            mBtnExit = (TextView) findViewById(R.id.btnExit);
            /*regId = GCMRegistrar.getRegistrationId(LoginActivity.this);
            if (regId.isEmpty()) {
                GCMRegistrar.register(LoginActivity.this, Constants.SENDER_ID);
            }*/
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    private void initListner() {
        mBtnLogin.setOnClickListener(this);
        mBtnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            Intent intent;
            switch (v.getId()) {
                case R.id.btnLogin:
                    if (isOnline()) {
                        try {

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
                        Common.showToast(LoginActivity.this, "Please connect to working Internet connection");
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
                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
                            Constants.NotifyNoInternet(LoginActivity.this);
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
                                Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
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
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
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
                        LoginActivity.this, request, Constants.CHECK_LOGIN_PIN1, true);
                if (result != null && result.getPropertyCount() > 0) {
                    mResponseFlag = result.getPropertyAsString(0);
                    if (mResponseFlag != null && !mResponseFlag.equals("")) {
                        String[] resultParts = mResponseFlag.split(",");
                        if (resultParts.length > 1 && resultParts[1].equalsIgnoreCase("0")) {
                            Datastorage.setSessionBoolean(LoginActivity.this, Datastorage.isFirstTime, true);
                        }
                        mResponseFlag = resultParts[0];
                        if (mResponseFlag.equalsIgnoreCase("true")) {
                            LoginDetail.setPhoneNo(Entered_PhoneNo);
                            Datastorage.SetPhoneNumber(LoginActivity.this, Entered_PhoneNo);
                            LoginDetail.setLogPin(Integer.parseInt(Entered_Pin));
                            Datastorage.SetLoginPin(LoginActivity.this, Entered_Pin);
                            String info = GetUserDetailsKumKumvehicle();
                            String[] parts = info.split(",");
                            Constants.Logwrite("LoginActivity:StudentDetails", "StringLength:" + parts.length);
                            Schl_Id = parts[0];
                            Constants.Logwrite("LoginActivity:StudentDetails", "SchoolId:" + Schl_Id);
                            User_Id = parts[1];
                            Constants.Logwrite("LoginActivity:StudentDetails", "UserId:" + User_Id);
                            studid = parts[2];
                            Constants.Logwrite("LoginActivity:StudentDetails", "StudentId:" + studid);
                            yearid = parts[3];
                            Constants.Logwrite("LoginActivity:StudentDetails", "YearId:" + yearid);
                            classid = parts[4];
                            Constants.Logwrite("LoginActivity:StudentDetails", "ClassId:" + classid);
                            classsecid = parts[5];
                            Constants.Logwrite("LoginActivity:StudentDetails", "ClassSecid:" + classsecid);
                            classname = parts[6];
                            Constants.Logwrite("LoginActivity:StudentDetails", "Classname:" + classname);
                            studentenrolldate = parts[7];
                            Constants.Logwrite("LoginActivity:StudentDetails", "studentenrolldate:" + studentenrolldate);
                            academicyear = parts[9];
                            Constants.Logwrite("LoginActivity:StudentDetails", "academicyear:" + academicyear);
                            Name = parts[8];
                            Constants.Logwrite("LoginActivity:StudentDetails", "Name:" + Name);
                            lastupdatedtime = parts[14];
                            Constants.Logwrite("LoginActivity:StudentDetails", "LastUpdatedTime:" + lastupdatedtime);
                            updatedtime = parts[15];
                            Constants.Logwrite("LoginActivity:StudentDetails", "updatedtime:" + updatedtime);
                            RouteId = Integer.parseInt(parts[16]);
                            try {
                                ClassSecName = parts[17];
                                Constants.Logwrite("LoginActivity", "ClassSecName: " + ClassSecName);
                            } catch (Exception ex) {
                                Constants.Logwrite("LoginActivity",
                                        "CheckLoginPin ClassSecName not found 462: " + ex.getMessage() + ":::::");
                                Constants.writelog("LoginActivity",
                                        "CheckLoginPin ClassSecName not found 462: " + ex.getMessage() + ":::::");
                            }

                            Academic_Year_Start_Date = parts[10];
                            Constants.Logwrite("LoginActivity:StudentDetails", "Academic_Year_Start_Date:" + Academic_Year_Start_Date);
                            Academic_Year_End_Date = parts[11];
                            Constants.Logwrite("LoginActivity:StudentDetails", "Academic_Year_End_Date:" + Academic_Year_End_Date);
                            Holiday_Start_Date = parts[12];
                            Constants.Logwrite("LoginActivity:StudentDetails", "Holiday_Start_Date:" + Holiday_Start_Date);
                            Holiday_End_Date = parts[13];
                            Constants.Logwrite("LoginActivity:StudentDetails", "Holiday_End_Date:" + Holiday_End_Date);
                            // int schoolid = (int)(SchoolId.toString());
                            LoginDetail.setSchoolId(Schl_Id);
                            LoginDetail.setUserId(User_Id);
                            LoginDetail.setStudId(studid);
                            Datastorage.SetUserId(LoginActivity.this, User_Id);
                            Datastorage.SetSchoolId(LoginActivity.this, Schl_Id);
                            Datastorage.SetStudentID(LoginActivity.this, studid);
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
                            Datastorage.SetStudentName(LoginActivity.this, Name);
                            Datastorage.SetClassId(LoginActivity.this, classid);
                            Datastorage.SetClassSecId(LoginActivity.this, classsecid);
                            Datastorage.SetEnrollDate(LoginActivity.this, studentenrolldate);
                            Datastorage.LastUpdatedtime(LoginActivity.this, lastupdatedtime);
                            Datastorage.SetCurrentYearId(LoginActivity.this, yearid);
                            Datastorage.SetAcademicYear(LoginActivity.this, academicyear);
                            Datastorage.AcaStartDate(LoginActivity.this, Academic_Year_Start_Date);
                            Datastorage.AcaEndDate(LoginActivity.this, Academic_Year_End_Date);
                            Datastorage.SetHolidayStartDate(LoginActivity.this, Holiday_Start_Date);
                            Datastorage.SetHolidayEndDate(LoginActivity.this, Holiday_End_Date);
                            Datastorage.SetRouteId(LoginActivity.this, RouteId);
                            Datastorage.SetClassSectionName(LoginActivity.this, ClassSecName);
                            // String last_upd_time = GetLastUpdatedTime();
                            // LoginDetail.setLastUpdatedTime(last_upd_time);
                            // Get User Imp Details
                            LogSucessfullyLogin();
                            // Check for Device Register or not
                            String Is_Device_Register = Constants.CheckDeviceRegistration(LoginActivity.this);

                            if (Is_Device_Register.equals("true")) {
                                // Check for GCM Registration
                                String Is_Device_GCM_Done = Constants.CheckGCMRegistrationofDevice(LoginActivity.this);
                                if (Is_Device_GCM_Done.equals("true")) {
                                    String IsUserRegister = Constants.CheckUserRegistered(Integer.parseInt(LoginDetail.getStudId()),
                                            Integer.parseInt(LoginDetail.getSchoolId()), LoginActivity.this);
                                    if (!IsUserRegister.equals("true"))
                                        Constants.InsertNewAccountGCMData(Integer.parseInt(LoginDetail.getStudId()), Integer.parseInt(LoginDetail.getSchoolId()),
                                                LoginActivity.this);
                                } else {
                                    Constants.UpdateGCMRegistration(LoginActivity.this);
                                }
                            } else {
                                String Reg_Done = Constants.InsertGCMRegistration(LoginActivity.this);
                                if (Reg_Done.equals("true")) {
                                    String IsUserRegister = Constants
                                            .CheckUserRegistered(Integer.parseInt(LoginDetail.getStudId()),
                                                    Integer.parseInt(LoginDetail.getSchoolId()), LoginActivity.this);
                                    if (!IsUserRegister.equals("true"))
                                        Constants.InsertNewAccountGCMData(
                                                Integer.parseInt(LoginDetail.getStudId()), Integer
                                                        .parseInt(LoginDetail.getSchoolId()), LoginActivity.this);
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
                        LoginActivity.this, request, Constants.GET_USER_DETAILS_VEHICLE,
                        true);
                if (result != null && result.getPropertyCount() > 0) {
                    mResponseFlag = result.getPropertyAsString(0);
                    // String[] parts = info.split(",");
                    Constants.Logwrite("LoginPage:GetUserDetailsKumKum", "ResString:" + mResponseFlag);
                    // return mResponseFlag;
                }
            } catch (Exception e) {
                Constants.writelog("LoginActivity",
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
                        LoginActivity.this, request, Constants.LOG_SUCESSFULLY_LOGIN, true);
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
            Constants.requestFocus(LoginActivity.this, mEdtMobileNumber);
            isvalidate = false;
        } else if (mEdtMobileNumber.getText().toString().length() < 10) {
            mInputEdtNumber.setError("Please Enter Valid Mobile Number");
            Constants.requestFocus(LoginActivity.this, mEdtMobileNumber);
            isvalidate = false;
            try {
                int number = Integer.parseInt(mEdtMobileNumber.getText().toString().trim());
            } catch (Exception ex) {
                mInputEdtNumber.setError("Please Enter Valid Mobile Number");
                Constants.requestFocus(LoginActivity.this, mEdtMobileNumber);
                isvalidate = false;
            }
        }

        return isvalidate;
    }

    private boolean PinValidation() {
        boolean isvalidate = true;
        if (mEdtPinNo.getText().toString().trim().length() <= 0) {
            mInputEdtPin.setError("Please Enter Pin");
            Constants.requestFocus(LoginActivity.this, mEdtPinNo);
            isvalidate = false;
        } else if (mEdtPinNo.getText().toString().length() < 6) {
            mInputEdtPin.setError("Please Enter Valid Login Pin");
            Constants.requestFocus(LoginActivity.this, mEdtPinNo);
            isvalidate = false;
            try {
                int number = Integer.parseInt(mEdtPinNo.getText().toString().trim());
            } catch (Exception ex) {
                mInputEdtPin.setError("Please Enter Valid Login Pin");
                Constants.requestFocus(LoginActivity.this, mEdtPinNo);
                isvalidate = false;
            }
        }
        return isvalidate;
    }
}
