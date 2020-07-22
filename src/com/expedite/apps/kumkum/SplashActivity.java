package com.expedite.apps.kumkum;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.expedite.apps.kumkum.activity.ChangePinActivity;
import com.expedite.apps.kumkum.activity.HomeActivity;
import com.expedite.apps.kumkum.activity.LoginNewActivity;
import com.expedite.apps.kumkum.activity.SurveyPollTaskActivity;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.Contact;
import com.expedite.apps.kumkum.model.LoginDetail;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity {
    Common mcommon;
    private ImageView mLogoIcon, mLogoIcon2;
    private String resp, mIsSurveryPollDisplay = "False";
    private ProgressBar mProgressBar;
    private boolean isFirstTime = false, rememberMe = false;
    private String PREFS = "MyPrefs";
    private SharedPreferences mPrefs;
    private String Schl_Id = "", User_Id = "", studid = "", yearid = "", classid = "", classsecid = "";
    private String classname = "", studentenrolldate = "", lastupdatedtime = "", Name = "", academicyear = "",
            Academic_Year_Start_Date = "", Academic_Year_End_Date = "";
    private String Holiday_Start_Date = "", Holiday_End_Date = "", Entered_PhoneNo = "", Entered_Pin = "", info = "";
    private Boolean Is_Continue = true, Is_Update_Available = false, Is_DB_Updated = false;
    private String Control_Message = "", regId = "", Sel_Academic_Year = "", DefaultAcademicYear = "", DefaultAccount = "", weblink = "", Stud_Curr_Year_Id = "", All_Logs = "";
    private int Sel_Academic_Year_Id = 0, Total_Account = 0, Global_Stud_Id = 0, Global_SChool_Id = 0, Start_Time = 0, End_Time = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        // startSmsRecevier();

       /* ArrayList<String> appCodes = new ArrayList<>();
        SmsVerification hash = new SmsVerification(this);
        appCodes = hash.getAppSignatures();
        String yourhash = appCodes.get(0);
        Log.e("Hashkey", yourhash);
        Constants.writelog("AppHashKey 66 : ", yourhash);*/

        init();
    }

    public void init() {
        try {
            mcommon = new Common(SplashActivity.this);
            mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
            mLogoIcon = (ImageView) findViewById(R.id.logo);
            mLogoIcon2 = (ImageView) findViewById(R.id.logo2);

            if (!mcommon.getSession(Constants.APPLOGO).equalsIgnoreCase("")) {
                mLogoIcon2.setVisibility(View.GONE);
                mLogoIcon.setVisibility(View.VISIBLE);

                String path = mcommon.getSession(Constants.APPLOGO);
                String[] patharr = path.split("/");
                String imagename = patharr[patharr.length - 1];
                if (imagename.contains(".gif")) {
                    Glide.with(SplashActivity.this).load(mcommon.getSession(Constants.APPLOGO)).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(mLogoIcon);
                } else {
                    Glide.with(SplashActivity.this)
                            .load(mcommon.getSession(Constants.APPLOGO)).asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(mLogoIcon);
                }
                //Picasso.with(SplashActivity.this).load(mcommon.getSession(Constants.APPLOGO)).placeholder(R.drawable.placeholder).into(mLogoIcon);
                //Picasso.with(SplashActivity.this).load(mcommon.getSession(Constants.APPLOGO)).into(mLogoIcon);

            } else {
                mLogoIcon.setVisibility(View.GONE);
                mLogoIcon2.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        try {
            try {
                isFirstTime = Datastorage.getSessionBoolean(SplashActivity.this, Datastorage.isFirstTime);
                db = new DatabaseHandler(SplashActivity.this);
                Datastorage.SetDeviceId(SplashActivity.this, Secure.getString(getContentResolver(), Secure.ANDROID_ID));
                if (isOnline()) {
                   /* GCMRegistrar.checkDevice(this);
                    GCMRegistrar.checkManifest(this);
                    regId = GCMRegistrar.getRegistrationId(this);

*/
                    regId = FirebaseInstanceId.getInstance().getToken();

                    if (regId != null && !regId.isEmpty()) {
                        Datastorage.SetGetFcmRegId(SplashActivity.this, regId);
                        LoginDetail.setGCM_REG_ID(regId);
                    } else {
                        //Registration is not present, register now with FCM
                        Constants.Logwrite("Main:", "FCM Registration Id Null.....");
                        Constants.writelog("SplashActivity", "RegistrationId is null.");
                        //     GCMRegistrar.register(SplashActivity.this, Constants.SENDER_ID);
                    }
                }
            } catch (Exception err) {
                Constants.writelog("SplashActivity", "Exception:209 " + err.getMessage());
            }

            if (isFirstTime) {
                Intent in = new Intent(SplashActivity.this, ChangePinActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(in);
                onClickAnimation();
                finish();
                return;
            }

            //}
          /*  new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {*/
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            Datastorage.SetAppVersion(SplashActivity.this, pInfo.versionName);
            new GetSchoolData().execute();
                   /* } catch (Exception err) {
                        Constants.writelog("SplashActivity",
                                "Exception:238 " + err.getMessage() + "::::" + err.getStackTrace());
                    }
                }
            }, SPLASH_TIME_OUT);*/
        } catch (Exception ex) {
            Constants.writelog("SplashActivity", "Exception:243 " + ex.getMessage() + "::::" + ex.getStackTrace());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }

    /**
     * @param SchlId
     * @param StdId
     * @param _usrid
     * @param _Phn
     * @param _pin
     * @param clsid
     * @param classsecid
     * @param stdname
     * @param stdenrolldate
     * @param lstutime
     * @param Stud_Aca_Year
     * @param route_id
     */
    private void SetValuetoStudent(int SchlId, int StdId, int _usrid,
                                   String _Phn, String _pin, String clsid, String classsecid,
                                   String stdname, String stdenrolldate, String lstutime,
                                   String Stud_Aca_Year, int route_id) {
        try {
            LoginDetail.setUserId(Integer.toString(_usrid));
            Datastorage.SetUserId(SplashActivity.this,
                    Integer.toString(_usrid));
            LoginDetail.setSchoolId(Integer.toString(SchlId));
            Datastorage.SetSchoolId(SplashActivity.this,
                    Integer.toString(SchlId));

            LoginDetail.setStudId(Integer.toString(StdId));

            Datastorage.SetStudentID(SplashActivity.this,
                    Integer.toString(StdId));

            LoginDetail.setLogPin(Integer.parseInt(_pin));

            LoginDetail.setPhoneNo(_Phn);

            Datastorage.SetPhoneNumber(SplashActivity.this, _Phn);

            LoginDetail.setStudentName(stdname);

            Datastorage.SetStudentName(SplashActivity.this, stdname);

            LoginDetail.setClassId(clsid);

            Datastorage.SetClassId(SplashActivity.this, clsid);

            LoginDetail.setClassSecId(classsecid);

            Datastorage.SetClassSecId(SplashActivity.this, classsecid);

            LoginDetail.setStudentEnrollDate(stdenrolldate);

            Datastorage.SetEnrollDate(SplashActivity.this, stdenrolldate);

            LoginDetail.setLastUpdatedTime(lstutime);

            Datastorage.LastUpdatedtime(SplashActivity.this, lstutime);

            Datastorage.SetRouteId(SplashActivity.this, route_id);

            LoginDetail.setCurrentYearId(Integer.toString(Sel_Academic_Year_Id));

            Datastorage.SetCurrentYearId(SplashActivity.this,
                    Integer.toString(Sel_Academic_Year_Id));

            LoginDetail.setAcademicyear(Stud_Aca_Year);

            Datastorage.SetAcademicYear(SplashActivity.this, Stud_Aca_Year);
        } catch (Exception err) {
            Constants.writelog("SplashActivity",
                    "Exception:1385 SetValuetoStudent " + err.getMessage()
                            + "::::" + err.getStackTrace());
        }
    }

    public void MessageTransferMethod() {
        try {
            List<Contact> contacts = db.GetAllSMSDetails();
            String MessagesCount1 = Integer.toString(contacts.size());
            Constants.Logwrite("MessageCount-1", ":" + MessagesCount1);

            String[] messages = new String[contacts.size()];
            if (messages != null && messages.length > 0) {
                for (Contact cn : contacts) {

                    int MSGID = cn.getMSGID();
                    Constants.Logwrite("MSGId", ":" + MSGID);
                    String SMSTEXT = cn.getSMSText();
                    Constants.Logwrite("SMSTEXT", ":" + SMSTEXT);
                    int STUDID = cn.getSMSStudid();
                    Constants.Logwrite("STUDID", ":" + STUDID);
                    int SCHOOLID = cn.getSMSSchoolId();
                    Constants.Logwrite("SCHOOLID", ":" + SCHOOLID);
                    int SMSDAY = cn.getSMSDAY();
                    Constants.Logwrite("SMSDAY", ":" + SMSDAY);
                    int SMSMONTH = cn.getSMSMONTH();
                    Constants.Logwrite("SMSMONTH", ":" + SMSMONTH);
                    int SMSYEAR = cn.getSMSYEAR();
                    Constants.Logwrite("SMSYEAR", ":" + SMSYEAR);
                    int SMSMODILEID = cn.getSMSMODULEID();
                    Constants.Logwrite("SMSMODILEID", ":" + SMSMODILEID);
                    int SMSYEARID = cn.getSMSYEARID();
                    Constants.Logwrite("SMSYEARID", ":" + SMSYEARID);
                    db.AddSMS(new Contact(MSGID, SMSTEXT, STUDID, SCHOOLID,
                            SMSDAY, SMSMONTH, SMSYEAR, SMSMODILEID, SMSYEARID));
                    Constants.Logwrite("Status", "Messages Sucessfully Inserted");
                }
                // Delete All SMS
                db.DeleteAllOldSMS();
                // Delete Table Also
                db.DeleteOldSMSTable();
            } else {
                Constants.Logwrite("MessageCount-1", ":" + MessagesCount1);
            }
        } catch (Exception err) {
            Constants.Logwrite("MessageTransferMethod:Exception", "" + err.getMessage()
                    + ":::StackTrace:" + err.getStackTrace());
            Constants.writelog("SplashActivity",
                    "Exception:1431 MessageTransferMethod " + err.getMessage()
                            + "::::" + err.getStackTrace());
        }
    }

    public void SetDefaultAcademicYear(int SchoolId, int Studid) {
        try {
            DefaultAcademicYear = db.GetDefaultAcademicYearAccount(SchoolId,
                    Studid);
            if (DefaultAcademicYear != null && DefaultAcademicYear != "") {
                String[] spliterstr = DefaultAcademicYear.split(",");
                String AcademicYear = spliterstr[0];
                String Year_Id = spliterstr[1];
                if (Year_Id != "") {
                    Sel_Academic_Year_Id = Integer.parseInt(Year_Id);
                }
                if (AcademicYear != "") {
                    Sel_Academic_Year = AcademicYear;
                }
            }
        } catch (Exception err) {
            Constants.Logwrite("SetDefaultAcademicYear:Exception", "" + err.getMessage()
                    + ":::StackTrace:" + err.getStackTrace());
            Constants.writelog("SplashActivity",
                    "Exception:1459 SetDefaultAcademicYear " + err.getMessage()
                            + "::::" + err.getStackTrace());
        }
    }

    public void RemoveoldAccountData() {
        try {
            int Record_Count = db.getContactsCountold();
            Constants.Logwrite("StartupActivity", "GetOldAccount:" + Record_Count);
            if (Record_Count > 0) {
                List<Contact> contacts = db.getAllContactsOld();
                int rec_cnt = db.getContactsCountold();
                Constants.Logwrite("StartupActivity ", "Count:" + rec_cnt);
                for (Contact cn : contacts) {
                    int Is_Def = 1;
                    String aa = cn.getPhoneNumber() + "," + cn.getLogPin()
                            + "," + cn.getIsDef();
                    String[] stud_det_splt = aa.split(",");
                    String Ph_No = stud_det_splt[0];
                    Constants.Logwrite("Main:", "Phone:" + Ph_No);
                    String Lg_Pin = stud_det_splt[1];
                    Constants.Logwrite("Main:", "LogPin:" + Lg_Pin);
                    String IsDef = stud_det_splt[2];
                    Constants.Logwrite("Main:", "IsDef:" + IsDef);
                    int stdid = cn.getStudentId();
                    if (IsDef.equals("1")) {
                        Is_Def = 1;
                    } else {
                        Is_Def = 0;
                    }
                    String GetStudDet = GetUserDetailsKumKumvehicle(Ph_No,
                            Lg_Pin, stdid);
                    String[] parts = GetStudDet.split(",");
                    Constants.Logwrite("Main:", "parts:" + parts.length);
                    // db.addContact(contact);

                    String School_ID = parts[0];
                    Constants.Logwrite("Main:", "School_ID:" + School_ID);

                    String Student_ID = parts[2];
                    Constants.Logwrite("Main:", "Student_ID:" + Student_ID);

                    String Student_Name = parts[8];
                    Constants.Logwrite("Main:", "Student_Name:" + Student_Name);

                    String yearid1 = parts[3];
                    Constants.Logwrite("Main:", "yearid1:" + yearid1);

                    String classid1 = parts[4];
                    Constants.Logwrite("Main:", "classid1:" + classid1);

                    String classsecid1 = parts[5];
                    Constants.Logwrite("Main:", "classsecid1:" + classsecid1);

                    String classname1 = parts[6];
                    Constants.Logwrite("Main:", "classname1:" + classname1);

                    String studentenrolldate1 = parts[7];
                    Constants.Logwrite("Main:", "studentenrolldate1:" + studentenrolldate1);

                    String lastupdatedtime1 = parts[10];
                    Constants.Logwrite("Main:", "lastupdatedtime1:" + lastupdatedtime1);

                    String academicyear1 = parts[9];
                    Constants.Logwrite("Main:", "academicyear1:" + academicyear1);

                    String User_Id1 = parts[1];
                    Constants.Logwrite("Main:", "User_Id1:" + User_Id1);

                    String Updatedtime = parts[15];
                    Constants.Logwrite("Main:", "Updatedtime:" + academicyear1);

                    int RouteId = Integer.parseInt(parts[16]);
                    Constants.Logwrite("Main:", "RouteId:" + RouteId);
                    int cnt = db.getContactsCountUsingPhoneandLogpin(Ph_No, Lg_Pin);
                    if (cnt != 1) {
                        db.addContact(new Contact(Student_Name, Ph_No, Integer
                                .parseInt(Lg_Pin), Is_Def, Integer
                                .parseInt(Student_ID), Integer
                                .parseInt(School_ID),
                                Integer.parseInt(yearid1), Integer
                                .parseInt(classid1), Integer
                                .parseInt(classsecid1), Integer
                                .parseInt(User_Id1), classname1,
                                studentenrolldate1, lastupdatedtime1,
                                academicyear1, Updatedtime, RouteId));
                    }
                }
                /*int del_all_cnnt = db.DeleteAllOldContact();

                if (del_all_cnnt == 1) {
                    Constants.Logwrite("Main:", "DeleteAccount-1");
                } else {
                    Constants.Logwrite("Main:", "DeleteAccount-0");
                }*/
            }
        } catch (Exception err) {
            Constants.Logwrite("Main:", "MethodName:RemoveoldAccountData" + err.getMessage());
            Constants.writelog("SplashActivity",
                    "Exception:1385 RemoveAccountData " + err.getMessage()
                            + "::::" + err.getStackTrace());
        }
    }

    public void showversionalert(final String weblink) {

        try {
            Constants.Logwrite("StartupActivity_PostExecute:", "weblink:" + weblink);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this);
            alertDialog.setTitle("Update information");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("New update of apps is available kindly download from play store.");
            alertDialog.setIcon(R.drawable.information);
            alertDialog.setPositiveButton("Download",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent browserIntent = new Intent(
                                    Intent.ACTION_VIEW, Uri.parse(weblink));
                            startActivity(browserIntent);
                            finish();
                        }
                    });
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });
            alertDialog.show();
            return;
        } catch (Exception err) {
            Constants.Logwrite("StartupActivity_PostExecute:", "Exception:" + err.getMessage()
                    + "_Stacktrace:" + err.getStackTrace());
            Constants.writelog("SplashActivity", "Exception:1743 AddMultipleFCMRegistration "
                    + err.getMessage() + "::::" + err.getStackTrace());
        }
    }

    public String AppsControlNew(int SchoolId, String updated_time, int studid,
                                 int userid, int yearid) {
        String Apps_Control = "";
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.APP_CONTROLNEW);
        request.addProperty("user_ver",
                Datastorage.GetAppVersion(SplashActivity.this));
        request.addProperty("appname", SchoolDetails.appname);
        request.addProperty("SchoolId", SchoolId);
        request.addProperty("updated_time", updated_time);
        request.addProperty("studid", studid);
        request.addProperty("userid", userid);
        request.addProperty("yearid", yearid);
        request.addProperty("Device_Id",
                Datastorage.GetDeviceId(SplashActivity.this));
        request.addProperty("Registration_Id",
                Datastorage.GetFcmRegId(SplashActivity.this));
        request.addProperty("Mobile_Number",
                Datastorage.GetPhoneNumber(SplashActivity.this));
        request.addProperty("codeVersion", Constants.CODEVERSION);

        try {
            SoapObject result = Constants.CallWebMethod(
                    SplashActivity.this, request, Constants.APP_CONTROLNEW,
                    false);
            if (result != null && result.getPropertyCount() > 0) {
                //Constants.writelog("School","AppsControl() Reqest:"+request.toString()+"\nresult:" + result.toString());
                Apps_Control = result.getProperty(0).toString();
            } else {
                Apps_Control = "";
            }
        } catch (Exception err) {
            Constants.writelog("SplashActivity", "Exception:1385 AppControlNew() Reqest:" + request + "\n MSG:"
                    + err.getMessage() + "::::" + err.getStackTrace());
            Constants.Logwrite("StartupActivity",
                    "AppsControl()_Exception:" + err.getMessage()
                            + "_Stacktrace:" + err.getStackTrace());
            return "true";
            // e.printStackTrace();
        }
        return Apps_Control;
    }

    public String GetUserDetailsKumKumvehicle(String Phone, String Pin,
                                              int studId) {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_USER_DETAILS_VEHICLE);
        request.addProperty("PhoneNo", Phone);
        request.addProperty("Log_pin", Pin);
        String aa = Integer.toString(Sel_Academic_Year_Id);
        request.addProperty("YearId", Sel_Academic_Year_Id);
        request.addProperty("studid", studId);

        try {

            SoapObject result = Constants.CallWebMethod(
                    SplashActivity.this, request, Constants.GET_USER_DETAILS_VEHICLE,
                    false);
            if (result != null && result.getPropertyCount() > 0) {
                String info = result.getPropertyAsString(0);
                // String[] parts = info.split(",");
                resp = info;
                // return resp;
            }
        } catch (Exception e) {
            Constants.writelog(
                    "SplashActivity",
                    "Exception:1854 GetUserDetailsKumKumvehicle "
                            + e.getMessage() + "::::" + e.getStackTrace());
            Constants.Logwrite("StartupActivity",
                    "GetUserDetails()_Exception:" + e.getMessage()
                            + "_Stacktrace:" + e.getStackTrace());
            return resp;
        }
        return resp;
    }

    private class GetSchoolData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String oth_stud_id = "";
                String oth_school_id = "";
                String Updated_Time = "";
                int Para_SchoolId1 = 0;
                int Para_StudId1 = 0;
                int Para_UserId1 = 0;
                int Para_YearId1 = 0;
                try {
                    Constants.Logwrite("StartupActivity_BackGround", "GetDataofStudent");
                    oth_stud_id = Datastorage.GetStudentId(SplashActivity.this); // LoginDetail.getStudId();
                    Constants.Logwrite("OtherStudentId", "StudentId:" + oth_stud_id);
                    oth_school_id = Datastorage.GetSchoolId(SplashActivity.this);// LoginDetail.getSchoolId();
                    Constants.Logwrite("OtherStudentId", "SchoolId:" + oth_school_id);
                } catch (Exception err) {
                    Constants.Logwrite("StartupActivity_BackGround", "Exception" + err.getMessage());
                }
                String Stud_Details = "";
                if ((oth_stud_id != null && oth_school_id != null)
                        && (!oth_stud_id.equals("") && !oth_school_id.equals(""))) {
                    Stud_Details = db.GetStudentAccountDetails(SplashActivity.this,
                            Integer.parseInt(oth_stud_id), Integer.parseInt(oth_school_id));
                    String[] splt_stud_details = Stud_Details.split(",");
                    Updated_Time = splt_stud_details[11];
                    Para_SchoolId1 = Integer.parseInt(splt_stud_details[1]);
                    Para_StudId1 = Integer.parseInt(splt_stud_details[2]);
                    Para_UserId1 = Integer.parseInt(splt_stud_details[3]);
                    Para_YearId1 = Integer.parseInt(splt_stud_details[0]);
                } else {
                    DefaultAccount = db.getDefaultAccount();
                    if (DefaultAccount != null && !DefaultAccount.isEmpty()) {
                        String[] splt_accnt_det = DefaultAccount.split(",");
                        Updated_Time = splt_accnt_det[7];
                        Para_SchoolId1 = Integer.parseInt(splt_accnt_det[2]);
                        Para_StudId1 = Integer.parseInt(splt_accnt_det[3]);
                        Para_UserId1 = Integer.parseInt(splt_accnt_det[4]);
                        Para_YearId1 = Integer.parseInt(splt_accnt_det[5]);
                        String def = DefaultAccount;
                        Constants.Logwrite("DefaultAccount", "" + def);
                        int Para_SchoolId = 0;
                        int Para_StudId = 0;
                        if (isOnline()) {
                            // This Method is For Remove old Contact Data.
                            RemoveoldAccountData();
                        }
                        if (DefaultAccount != "") {
                            Constants.Logwrite("StartupActivity_BackGround", "GetDatainDefaultAccount");
                            String[] splitrstr = DefaultAccount.split(",");
                            Para_SchoolId = Integer.parseInt(splitrstr[2]);
                            Para_StudId = Integer.parseInt(splitrstr[3]);
                            Global_Stud_Id = Para_StudId;
                            Global_SChool_Id = Para_SchoolId;
                            // Set AcademicYearId
                            SetDefaultAcademicYear(Para_SchoolId, Para_StudId);
                        } else {
                            Constants.writelog("School", "DoInBackground()510 DefaultAccount is null.");
                        }
                    }
                }
                int Contact_Count = db.getContactsCount();
                Total_Account = Contact_Count;
                if (isOnline()) {
                    FirebaseApp.initializeApp(SplashActivity.this);
                    regId = FirebaseInstanceId.getInstance().getToken();
                    //  Log.e("FCMID:",regId);
                    //regId = GCMRegistrar.getRegistrationId(SplashActivity.this);
                    if (regId != null && regId.isEmpty()) {
                        Constants.writelog("SplashActivity", "RegistrationId is null.");
                        // GCMRegistrar.register(SplashActivity.this, Constants.SENDER_ID);
                       /* int notificationtry = Datastorage.GetNotificationTry(SplashActivity.this);
                        Common.showLog("SplaceActivity", "Notificationtry 241:" + notificationtry);
                        Datastorage.SetNotificationTry(SplashActivity.this, notificationtry + 1);
                        notificationtry = Datastorage.GetNotificationTry(SplashActivity.this);
                        Common.showLog("SplaceActivity", "Notificationtry 243:" + notificationtry);
                        if (notificationtry < 4) {
                            if (notificationtry == 3) {
                                Intent mStartActivity = new Intent(SplashActivity.this, SplashActivity.class);
                                int mPendingIntentId = 123456;
                                PendingIntent mPendingIntent = PendingIntent
                                        .getActivity(
                                                SplashActivity.this,
                                                mPendingIntentId,
                                                mStartActivity,
                                                PendingIntent.FLAG_CANCEL_CURRENT);
                                AlarmManager mgr = (AlarmManager) SplashActivity.this
                                        .getSystemService(Context.ALARM_SERVICE);
                                mgr.set(AlarmManager.RTC_WAKEUP,
                                        System.currentTimeMillis() + 100,
                                        mPendingIntent);
                                System.exit(0);
                                return null;
                            }
                          /*  Intent in = new Intent(SplashActivity.this, RegisterGcmActivity.class);
                            startActivity(in);
                            finish();
                            onClickAnimation();
                            return null;*/
                    }
                    //  Common.showLog("SplaceActivity", "Notificationtry out of condition." + notificationtry);
                }
                //Common.showLog("SplaceActivity", "Notificationtry: out.");
                //Datastorage.SetNotificationTry(SplashActivity.this, 0);
                Start_Time = (int) (System.currentTimeMillis());
                String appcntr = AppsControlNew(Para_SchoolId1,
                        Updated_Time, Para_StudId1, Para_UserId1,
                        Para_YearId1);
                int End_Apps_Control = (int) (System.currentTimeMillis());
                int diff = End_Apps_Control - Start_Time;
                All_Logs = All_Logs + "Total_AppsControl:" + diff + ",";
                String[] logodata = appcntr.split("@@#spl#@@");
                // change by Jaydeep patel 21-10-2018
                if (logodata != null && logodata.length > 1 && !logodata[1].isEmpty()) {
                    mIsSurveryPollDisplay = logodata[1];
                }
                if (logodata != null && logodata.length > 2 && !logodata[2].equalsIgnoreCase("")) {
                    // mcommon.setSession(Constants.APPLOGO, logodata[1]);
                    // mcommon.setSession(Constants.APPname, logodata[2]);
                    mcommon.setSession(Constants.APPLOGO, logodata[2]);
                    mcommon.setSession(Constants.APPname, logodata[3]);
                }
                try {
                    if (logodata != null && logodata.length > 4 && !logodata[4].equalsIgnoreCase("") && logodata[4].contains("MenuName")) {
                        mcommon.setSession(Constants.APP_MENU_NAME, logodata[4]);
                    }
                } catch (Exception ex) {
                    Constants.writelog("SplashActivity", "29 in getting menu details " + ex.getMessage() + "::::"
                            + ex.getStackTrace());
                }
                String[] splterstr = logodata[0].split("#@TM#@TM#@");
                //Picasso.with(SplashActivity.this).load("http://vayuna.com/splash/kumkumEng.png").placeholder(R.drawable.placeholder).into(mLogoIcon);
                //String[] splterstr = appcntr.split("#@TM#@TM#@");
                try {
                    String menu = "";
                    for (int i = 0; i < splterstr.length; i++) {
                        String[] parts = splterstr[i].split(":");
                        if (parts[0].equalsIgnoreCase("MenuDetails")) {
                            menu = parts[1];
                            if (menu != null && !menu.equals("")) {
                                db.updateMenu(Para_StudId1 + "", Para_SchoolId1 + "", menu);
                            }
                        }
                    }
                } catch (Exception err) {
                    Constants.writelog(
                            "SplashActivity",
                            "Exception:517 in getting feeshow status"
                                    + err.getMessage() + "::::"
                                    + err.getStackTrace());
                }
                String appcntr1 = splterstr[0];
                String Apkversionstring = "false";
                String Is_DB_Update_String = "false";
                if (splterstr.length > 1) {
                    Apkversionstring = splterstr[1];// CheckApkVersion();
                    Is_DB_Update_String = splterstr[2];
                }
                String Stud_Data_String = "";
                if (Is_DB_Update_String.equals("true")) {
                    Is_DB_Updated = true;
                    Stud_Data_String = splterstr[3];
                } else {
                    Is_DB_Updated = false;
                }
                String status = "false";
                if (Apkversionstring.contains(",")) {
                    String spl[] = Apkversionstring.split(",");
                    status = spl[0];
                    weblink = spl[1];
                }
                if (status.equals("true")) {
                    Is_Update_Available = true;
                } else {
                    Is_Update_Available = false;
                }
                Control_Message = appcntr;
                if (appcntr1 != null && !appcntr1.isEmpty() && appcntr1.equalsIgnoreCase("true")) {
                    if (!Is_Update_Available) {
                        MessageTransferMethod();
                        Constants.Logwrite("StartupActivity_BackGround", "AccountCount:" + Total_Account);
                        Constants.Logwrite("Contact_Count", "" + Contact_Count);
                        if (Contact_Count > 1) {
                            Datastorage.SetMultipleAccount(SplashActivity.this, 1);
                        } else {
                            Datastorage.SetMultipleAccount(SplashActivity.this, 0);
                        }
                        Is_Continue = true;
                        mPrefs = getSharedPreferences(PREFS, 0);
                        rememberMe = mPrefs.getBoolean("rememberMe", false);
                        Constants.Logwrite("StartupActivity_BackGround", "Remember Value:" + rememberMe);

                        if ((oth_stud_id != null && oth_school_id != null)
                                && (!oth_stud_id.isEmpty() && !oth_school_id.isEmpty())) {
                            RedirectStudentLogin(oth_stud_id, oth_school_id, Stud_Details,
                                    Stud_Data_String);
                        } else {
                            DefaultAccountLogin(Stud_Data_String);
                        }
                    }
                }

                // For set current year as default year
                int is_SetCurYearAsDefYear = 0;
                int versionNumber = 0;
                try {
                    mPrefs = getSharedPreferences(PREFS, 0);
                    is_SetCurYearAsDefYear = Datastorage.GetCurrentYearisDefYear(SplashActivity.this);
                    PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    versionNumber = pinfo.versionCode;
                } catch (Exception ex) {
                    Constants.writelog("School", "Exception SetCurrentYearAsDefaultYear() 625 MSG:"
                            + ex.getMessage() + ":::::" + ex.getStackTrace());
                }
                if (is_SetCurYearAsDefYear != versionNumber) {
                    try {
                        List<Contact> contactList = new ArrayList<Contact>();
                        contactList = db.getAllContacts();
                        Contact c;
                        String schoolid = "";
                        String studid = "";
                        int flag = 0;
                        for (int i = 0; i < contactList.size(); i++) {
                            c = contactList.get(i);
                            schoolid = String.valueOf(c.getSchoolId());
                            studid = String.valueOf(c.getStudentId());
                            int result = Constants.SetCurrentYearAsDefYear(SplashActivity.this, schoolid, studid, db);
                            if (result == 1) {
                                flag = 1;
                            } else {
                                flag = 0;
                                break;
                            }
                        }
                        if (flag == 1) {
                            Datastorage.SetCurrentYearisDefYear(SplashActivity.this, versionNumber);
                        }
                    } catch (Exception ex) {
                        Constants.Logwrite("SplashActivity",
                                "set currnt year as default year error:" + ex.getMessage());
                        Constants.writelog("School", "Exception SetCurrentYearAsDefaultYear() 662 MSG:"
                                + ex.getMessage() + ":::::" + ex.getStackTrace());
                    }
                } else {
                    //Common.showToast(SplashActivity.this, "No Internet Connection");
                }
            } catch (Exception err) {
                Constants.Logwrite("StartupActivity", "DoInBackgroundException 747 Message:"
                        + err.getMessage() + "_Stacktrace:" + err.getStackTrace());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Intent intent;
                if (Is_Continue) {
                    if (Is_Update_Available) {
                        showversionalert(weblink);
                    } else {
                        Constants.Logwrite("OnPostExecute", "AcountCount:" + Total_Account);
                        if (Total_Account > 0) {
                            Constants.Logwrite("OnPostExecute", "GetAcountSuccessfully");
                            if (Is_DB_Updated) {
                                Constants.Logwrite("OnPostExecute", "UpdatedDBFound");
                                String[] parts = info.split(",");
                                Constants.Logwrite("OnPostExecute", "GetStringLength:" + parts.length);
                                if (parts.length > 0) {
                                    String logpin = parts[0];
                                    Constants.Logwrite("OnPostExecute", "LoginPin:" + logpin);
                                    yearid = parts[1];
                                    // Sel_Academic_Year_Id = yearid;
                                    Constants.Logwrite("OnPostExecute", "YearId:" + yearid);
                                    classid = parts[2];
                                    Constants.Logwrite("OnPostExecute", "ClassId:" + classid);
                                    classsecid = parts[3];
                                    Constants.Logwrite("OnPostExecute", "ClassSecId:" + classsecid);
                                    classname = parts[4];
                                    Constants.Logwrite("OnPostExecute", "ClassName:" + classname);
                                    studentenrolldate = parts[5];
                                    Constants.Logwrite("OnPostExecute", "studentenrolldate:" + studentenrolldate);
                                    Name = parts[6];
                                    Constants.Logwrite("OnPostExecute", "Name:" + Name);
                                    academicyear = parts[7];
                                    Constants.Logwrite("OnPostExecute", "academicyear:" + academicyear);
                                    Academic_Year_Start_Date = parts[8];
                                    Constants.Logwrite("OnPostExecute", "ASD:" + Academic_Year_Start_Date);
                                    Academic_Year_End_Date = parts[9];
                                    Constants.Logwrite("OnPostExecute", "AED:" + Academic_Year_End_Date);
                                    Holiday_Start_Date = parts[10];
                                    Constants.Logwrite("OnPostExecute", "HSD:" + Holiday_Start_Date);
                                    Holiday_End_Date = parts[11];
                                    Constants.Logwrite("OnPostExecute", "HED:" + Holiday_End_Date);
                                    lastupdatedtime = parts[12];
                                    Constants.Logwrite("OnPostExecute", "lastupdatedtime:" + lastupdatedtime);
                                    String updated_time = parts[13];
                                    Constants.Logwrite("OnPostExecute", "updated_time:" + updated_time);
                                    int RouteId = Integer.parseInt(parts[14]);
                                    Constants.Logwrite("OnPostExecute", "Selected_Year_Id:" + Sel_Academic_Year_Id);
                                    Constants.Logwrite("OnPostExecute", "Selected_Year_Text:" + Sel_Academic_Year);
                                    LoginDetail.setPhoneNo(Entered_PhoneNo);
                                    Datastorage.SetPhoneNumber(SplashActivity.this, Entered_PhoneNo);
                                    Constants.Logwrite("OnPostExecute", "SetPhoneSucessfully");
                                    LoginDetail.setLogPin(Integer.parseInt(logpin));
                                    Constants.Logwrite("OnPostExecute", "SetPinSucessfully");
                                    Datastorage.SetLoginPin(SplashActivity.this, logpin);
                                    LoginDetail.setStudentName(Name);
                                    Datastorage.SetStudentName(SplashActivity.this, Name);
                                    Constants.Logwrite("OnPostExecute", "SetNameSucessfully");
                                    LoginDetail.setClassId(classid);
                                    Datastorage.SetCurrentYearId(SplashActivity.this, yearid);
                                    Datastorage.SetClassId(SplashActivity.this, classid);
                                    Constants.Logwrite("OnPostExecute", "SetClassIdSuccessfully");
                                    LoginDetail.setClassSecId(classsecid);
                                    Datastorage.SetClassSecId(SplashActivity.this, classsecid);
                                    Constants.Logwrite("OnPostExecute", "SetClassSecIdSuccessfully");
                                    LoginDetail.setClassName(classname);
                                    Datastorage.SetClassSecName(SplashActivity.this, classname);
                                    Constants.Logwrite("OnPostExecute", "SetClassNameSuccessfully");
                                    LoginDetail.setStudentEnrollDate(studentenrolldate);
                                    Datastorage.SetEnrollDate(SplashActivity.this, studentenrolldate);
                                    Constants.Logwrite("OnPostExecute", "SetStudentEnollDateSucessfully");
                                    LoginDetail.setLastUpdatedTime(lastupdatedtime);
                                    Datastorage.LastUpdatedtime(SplashActivity.this, lastupdatedtime);
                                    Constants.Logwrite("OnPostExecute", "SetLastUpdatedTimeSucessfully");
                                    LoginDetail.setCurrentYearId(Integer.toString(Sel_Academic_Year_Id));
                                    Datastorage.SetCurrentYearId(SplashActivity.this, Integer.toString(Sel_Academic_Year_Id));
                                    Constants.Logwrite("OnPostExecute", "SetSelectedYearIdSucessfully");
                                    LoginDetail.setAcademicyear(Sel_Academic_Year);
                                    Datastorage.SetAcademicYear(SplashActivity.this, Sel_Academic_Year);
                                    Constants.Logwrite("OnPostExecute", "SetSelecteYearSucessfully");
                                    LoginDetail.setAcademicYearStartDate(Academic_Year_Start_Date);
                                    Datastorage.AcaStartDate(SplashActivity.this, Academic_Year_Start_Date);
                                    LoginDetail.setAcademicYearEndDate(Academic_Year_End_Date);
                                    Datastorage.AcaEndDate(SplashActivity.this, Academic_Year_End_Date);
                                    LoginDetail.setHolidayStartDate(Holiday_Start_Date);
                                    LoginDetail.setHolidayEndDate(Holiday_End_Date);
                                    Datastorage.SetRouteId(SplashActivity.this, RouteId);

                                    int Data_Update = 0;
                                    Constants.Logwrite("YEARID", "STUDENT:" + Stud_Curr_Year_Id);
                                    Constants.Logwrite("YEARID", "SELECTED:" + yearid);
                                    if (Stud_Curr_Year_Id.equals(yearid)) {
                                        Data_Update = db.UpdateStudentDetails(Global_Stud_Id, Global_SChool_Id, info);
                                    }
                                    Constants.Logwrite("Main:OnPostExecute", "Data_Update:" + Data_Update);
                                    mProgressBar.setVisibility(View.GONE);
                                    End_Time = (int) (System.currentTimeMillis());
                                    Datastorage.SetLogTime(SplashActivity.this, Start_Time, End_Time);
                                    if (isFirstTime) {
                                        intent = new Intent(SplashActivity.this, ChangePinActivity.class);
                                    } else {
                                        if (mIsSurveryPollDisplay != null && !mIsSurveryPollDisplay.isEmpty() &&
                                                mIsSurveryPollDisplay.equalsIgnoreCase("True")) {
                                            intent = new Intent(SplashActivity.this, SurveyPollTaskActivity.class);
                                        } else {
                                            intent = new Intent(SplashActivity.this, HomeActivity.class);
                                        }
                                    }
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                                    startActivity(intent);
                                    finish();
                                    onClickAnimation();
                                }
                            } else {
                                mProgressBar.setVisibility(View.GONE);
                                End_Time = (int) (System.currentTimeMillis());
                                Datastorage.SetLogTime(SplashActivity.this, Start_Time, End_Time);
                                if (isFirstTime) {
                                    intent = new Intent(SplashActivity.this, ChangePinActivity.class);
                                } else {
                                    if (mIsSurveryPollDisplay != null && !mIsSurveryPollDisplay.isEmpty() &&
                                            mIsSurveryPollDisplay.equalsIgnoreCase("True")) {
                                        intent = new Intent(SplashActivity.this, SurveyPollTaskActivity.class);
                                    } else {
                                        intent = new Intent(SplashActivity.this, HomeActivity.class);
                                    }
                                }
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();
                                onClickAnimation();
                            }
                        } else {
                            //Jaydeep-01/01/17
                            // intent = new Intent(SplashActivity.this, LoginActivity.class);
                            intent = new Intent(SplashActivity.this, LoginNewActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("lastpage", "SplashActivity");
                            startActivity(intent);
                            finish();
                            onClickAnimation();
                        }
                    }
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    Constants.Logwrite("StartupActivity_PostExecute:", "Activity Control Msg:" + Control_Message);
                    AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
                    alertDialog.setTitle("Alert");
                    if (Control_Message != null && !Control_Message.isEmpty()) {
                        alertDialog.setMessage(Control_Message);
                    } else {
                        alertDialog.setMessage("Please try again later.");
                    }
                    alertDialog.setIcon(R.drawable.alert);
                    alertDialog.setButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                    alertDialog.show();
                }
            } catch (Exception err) {
                Constants.writelog("SplashActivity", "Exception:1022 " + err.getMessage() + "::::" + err.getStackTrace());
                Constants.Logwrite("StartupActivity_PostExecute:", "Message:" + err.getMessage()
                        + "_Stacktrace:" + err.getStackTrace());
                mProgressBar.setVisibility(View.GONE);
            }
        }

        /**
         * @param oth_stud_id
         * @param oth_school_id
         */
        public void RedirectStudentLogin(String oth_stud_id, String oth_school_id, String Stud_Details, String stud_data) {
            //Get Default Academic Year For Student.
            int SchlId = 0;
            int StdId = 0;
            int YrId = 0;
            int _usrid = 0;
            String _Phn = "";
            String _pin = "";
            String clsid = "";
            String classsecid = "";
            String stdname = "";
            String stdenrolldate = "";
            String acayear = "";
            String lstutime = "";
            String utime = "";
            String Stud_Aca_Year = "";
            int route_id = 0;

            try {
                Stud_Details = db.GetStudentAccountDetails(SplashActivity.this,
                        Integer.parseInt(oth_stud_id),
                        Integer.parseInt(oth_school_id));

                Global_Stud_Id = Integer.parseInt(oth_stud_id);
                Global_SChool_Id = Integer.parseInt(oth_school_id);

                Constants.Logwrite("StartupActivity_BackGround", "StudentDetails:"
                        + Stud_Details.length());
                String[] spltrstr = Stud_Details.split(",");

                SchlId = Integer.parseInt(spltrstr[1]);
                Constants.Logwrite("StartupActivity_BackGround", "RedirectActivity:SchoolId:"
                        + SchlId);
                StdId = Integer.parseInt(spltrstr[2]);
                Constants.Logwrite("StartupActivity_BackGround", "RedirectActivity:StdId:" + StdId);
                YrId = Integer.parseInt(spltrstr[0]);
                Stud_Curr_Year_Id = spltrstr[0];
                Constants.Logwrite("StartupActivity_BackGround", "RedirectActivity:YearId:" + YrId);
                _usrid = Integer.parseInt(spltrstr[3]);
                Constants.Logwrite("StartupActivity_BackGround", "RedirectActivity:UserId:" + _usrid);

                _Phn = spltrstr[4];
                Constants.Logwrite("StartupActivity_BackGround", "RedirectActivity:Phone:" + _Phn);
                Entered_PhoneNo = _Phn;
                _pin = spltrstr[5];
                Constants.Logwrite("StartupActivity_BackGround", "RedirectActivity:PIN:" + _pin);
                // -------------------------------------------
                clsid = spltrstr[6];
                Constants.Logwrite("StartupActivity_BackGround", "RedirectActivity:clsid:" + clsid);
                classsecid = spltrstr[7];
                Constants.Logwrite("StartupActivity_BackGround", "RedirectActivity:classsecid:"
                        + classsecid);
                stdname = spltrstr[8];
                Constants.Logwrite("StartupActivity_BackGround", "RedirectActivity:stdname:"
                        + stdname);
                acayear = spltrstr[9];
                Constants.Logwrite("StartupActivity_BackGround", "RedirectActivity:acayear:"
                        + acayear);
                lstutime = spltrstr[10];
                Constants.Logwrite("StartupActivity_BackGround", "RedirectActivity:lstutime:"
                        + lstutime);
                utime = spltrstr[11];

                Constants.Logwrite("StartupActivity_BackGround", "utime:" + utime);
                try {
                    stdenrolldate = spltrstr[12];
                } catch (Exception err) {
                }
                try {
                    String RTID = spltrstr[13];
                    route_id = Integer.parseInt(spltrstr[13]);
                } catch (Exception err) {
                }

                Constants.Logwrite("StartupActivity_BackGround", "RedirectActivity:stdenrolldate:"
                        + stdenrolldate);

                // ============================================

                String Stud_Year_Det = db.GetDefaultAcademicYearAccount(SchlId,
                        StdId);
                Constants.Logwrite("StartupActivity_BackGround", "RedirectActivity:YearDetails:"
                        + Stud_Year_Det);

                if (Stud_Year_Det != null && Stud_Year_Det.length() > 0) {
                    String[] splitryearstr = Stud_Year_Det.split(",");
                    Sel_Academic_Year_Id = Integer.parseInt(splitryearstr[1]
                            .toString());
                    Stud_Aca_Year = splitryearstr[0];
                    Sel_Academic_Year = Stud_Aca_Year;
                } else {
                    Sel_Academic_Year_Id = YrId;
                    Stud_Aca_Year = acayear;
                    Sel_Academic_Year = acayear;
                }
                Constants.Logwrite("StartupActivity_BackGround", "DeafultYearId:"
                        + Sel_Academic_Year_Id);
            } catch (Exception err) {
                Constants.writelog(
                        "SplashActivity",
                        "Exception:1072 " + err.getMessage() + "::::"
                                + err.getStackTrace());
                Constants.Logwrite("StartupActivity_BackGround",
                        "Exception:RedirectActivity" + err.getMessage()
                                + ":::StactTrace:"
                                + err.getStackTrace().toString());
            }
            Constants.Logwrite("StartupActivity_BackGround", "RedirectDetails:" + StdId
                    + "::UserId:" + _usrid + "::SchoolId:" + SchlId + "YearId:"
                    + Sel_Academic_Year_Id);

            Constants.Logwrite("DBUPDATE:RedirectActivity", "Status:" + Is_DB_Updated);
            if (Is_DB_Updated) {
                info = stud_data;
            } else {
                // Set Value to Student
                SetValuetoStudent(SchlId, StdId, _usrid, _Phn, _pin, clsid,
                        classsecid, stdname, stdenrolldate, lstutime,
                        Stud_Aca_Year, route_id);
            }
            Constants.Logwrite("StartupActivity_BackGround",
                    "RedirectDetails:" + info.length());
        }

        /**
         *
         */
        public void DefaultAccountLogin(String stud_data) {
            int Student_Id = 0;
            int School_Id = 0;
            int _User_Id = 0;
            int YearId = 0;
            String _StudentName = "";
            String Updated_Time_For_Check = "";
            String _Class_Id = "";
            String _Class_Sec_Id = "";
            String _lastupdatedtime = "";
            String _studenrolldate = "";
            String _classname = "";
            String _acayear = "";
            String pnumber = "";
            int RtId = 0;
           /* int isfeeshow = 0;
            int ispayshow=0;
            int isqueryshow = 0;*/
            try {
                if (DefaultAccount != "") {
                    String[] splt_accnt_det = DefaultAccount.split(",");
                    Constants.Logwrite("StartupActivity_BackGround", "DefaultAccountDetails"
                            + DefaultAccount);
                    Entered_PhoneNo = splt_accnt_det[0];
                    Constants.Logwrite("StartupActivity_BackGround", "DefaultDetails:Phone"
                            + Entered_PhoneNo);
                    Entered_Pin = splt_accnt_det[1];
                    Constants.Logwrite("StartupActivity_BackGround", "DefaultDetails:Pin"
                            + Entered_Pin);
                    Student_Id = Integer.parseInt(splt_accnt_det[3]);
                    Constants.Logwrite("StartupActivity_BackGround",
                            "DefaultDetails:StudentId:" + Student_Id);
                    School_Id = Integer.parseInt(splt_accnt_det[2]);
                    Constants.Logwrite("StartupActivity_BackGround",
                            "DefaultDetails:SchoolId:" + School_Id);
                    _User_Id = Integer.parseInt(splt_accnt_det[4]);
                    Constants.Logwrite("StartupActivity_BackGround",
                            "DefaultDetails:UserId:" + _User_Id);
                    YearId = Integer.parseInt(splt_accnt_det[5]);
                    Stud_Curr_Year_Id = splt_accnt_det[5];
                    Constants.Logwrite("StartupActivity_BackGround",
                            "DefaultDetails:YearId:" + YearId);
                    _StudentName = splt_accnt_det[6];
                    Constants.Logwrite("StartupActivity_BackGround",
                            "DefaultDetails:StudentName:" + _StudentName);
                    Updated_Time_For_Check = splt_accnt_det[7];
                    Constants.Logwrite("StartupActivity_BackGround",
                            "DefaultDetails:UpdatedTime:"
                                    + Updated_Time_For_Check);
                    _Class_Id = splt_accnt_det[8];
                    Constants.Logwrite("StartupActivity_BackGround",
                            "DefaultDetails:ClassId:" + _Class_Id);
                    _Class_Sec_Id = splt_accnt_det[9];
                    Constants.Logwrite("StartupActivity_BackGround",
                            "DefaultDetails:ClassSecId:" + _Class_Sec_Id);
                    _lastupdatedtime = splt_accnt_det[10];
                    Constants.Logwrite("StartupActivity_BackGround",
                            "DefaultDetails:LastUpdatedTime:" + _Class_Sec_Id);

                    _studenrolldate = splt_accnt_det[11];
                    Constants.Logwrite("StartupActivity_BackGround",
                            "DefaultDetails:StudentEnrollDate:"
                                    + _studenrolldate);
                    _classname = splt_accnt_det[12];
                    Constants.Logwrite("StartupActivity_BackGround",
                            "DefaultDetails:ClassName:" + _classname);

                    _acayear = splt_accnt_det[13];
                    Constants.Logwrite("StartupActivity_BackGround",
                            "DefaultDetails:AcademicYearActivity:" + _acayear);
                    RtId = Integer.parseInt(splt_accnt_det[14]);

                    Constants.Logwrite("StartupActivity_BackGround",
                            "DefaultDetails:RouteId:" + RtId);
                    pnumber = splt_accnt_det[0];
                    Constants.Logwrite("StartupActivity_BackGround",
                            "DefaultDetails:pnumber:" + pnumber);
                    Entered_PhoneNo = pnumber;
                    Constants.Logwrite("StartupActivity_BackGround", "Db_Update_Status:" + Is_DB_Updated);
                    if (Is_DB_Updated) {
                        Constants.Logwrite("StartupActivity_BackGround", "DBUpdatedFound");
                        info = stud_data;
                        Constants.Logwrite("StartupActivity_BackGround",
                                "GetUpdatedStringLength:" + info.length());
                        Constants.Logwrite("StartupActivity",
                                "Getting default account Phone Info:" + info);
                        User_Id = Integer.toString(_User_Id);
                        Constants.Logwrite("StartupActivity_BackGround",
                                "GetUpdatedDB_UserID:" + User_Id);
                        Schl_Id = Integer.toString(School_Id);
                        Constants.Logwrite("StartupActivity_BackGround",
                                "GetUpdatedDB_SchoolId:" + Schl_Id);
                        studid = Integer.toString(Student_Id);
                        Constants.Logwrite("StartupActivity_BackGround",
                                "GetUpdatedDB_StudID:" + studid);
                        LoginDetail.setUserId(User_Id);
                        LoginDetail.setSchoolId(Schl_Id);
                        LoginDetail.setStudId(studid);
                        Datastorage.SetUserId(SplashActivity.this, User_Id);
                        Datastorage.SetSchoolId(SplashActivity.this,
                                Schl_Id);
                        Datastorage.SetStudentID(SplashActivity.this,
                                studid);
                        Stud_Curr_Year_Id = Integer.toString(YearId);

                    } else {
                        Constants.Logwrite("StartupActivity_BackGround", "DBIsUptoDate");

                        LoginDetail.setSchoolId(Integer.toString(School_Id));

                        LoginDetail.setUserId(Integer.toString(_User_Id));

                        LoginDetail.setStudId(Integer.toString(Student_Id));

                        LoginDetail.setStudentName(_StudentName);

                        LoginDetail.setClassId(_Class_Id);

                        LoginDetail.setClassSecId(_Class_Sec_Id);

                        LoginDetail.setClassName(_classname);

                        LoginDetail.setStudentEnrollDate(_studenrolldate);

                        LoginDetail.setLastUpdatedTime(_lastupdatedtime);

                        if (DefaultAcademicYear != null
                                && DefaultAcademicYear != "") {
                            String[] spliterstr = DefaultAcademicYear
                                    .split(",");
                            String AcademicYear = spliterstr[0];
                            String Year_Id = spliterstr[1];

                            if (AcademicYear != "") {
                                LoginDetail.setAcademicyear(AcademicYear);

                                Datastorage.SetAcademicYear(
                                        SplashActivity.this, AcademicYear);

                            } else {
                                LoginDetail.setAcademicyear(_acayear);
                                Datastorage.SetAcademicYear(
                                        SplashActivity.this, _acayear);

                            }
                            if (Year_Id != "") {

                                LoginDetail.setCurrentYearId(Year_Id);
                                Datastorage.SetCurrentYearId(
                                        SplashActivity.this, Year_Id);
                            } else {
                                LoginDetail.setCurrentYearId(Integer
                                        .toString(YearId));
                                Datastorage.SetCurrentYearId(
                                        SplashActivity.this,
                                        Integer.toString(YearId));
                            }
                        } else {
                            LoginDetail
                                    .setCurrentYearId(Integer.toString(YearId));
                            LoginDetail.setAcademicyear(_acayear);

                            Datastorage.SetAcademicYear(
                                    SplashActivity.this, _acayear);
                            Datastorage.SetCurrentYearId(
                                    SplashActivity.this,
                                    Integer.toString(YearId));
                        }

                        // Set Data in

                        Datastorage.SetUserId(SplashActivity.this,
                                Integer.toString(_User_Id));
                        Datastorage.SetSchoolId(SplashActivity.this,
                                Integer.toString(School_Id));
                        Datastorage.SetStudentID(SplashActivity.this,
                                Integer.toString(Student_Id));
                        // Datastorage.SetPhoneNumber(SplashActivity.this,_Phn);
                        Datastorage.SetStudentName(SplashActivity.this,
                                _StudentName);
                        Datastorage.SetClassId(SplashActivity.this,
                                _Class_Id);
                        Datastorage.SetClassSecId(SplashActivity.this,
                                _Class_Sec_Id);
                        Datastorage.SetEnrollDate(SplashActivity.this,
                                _studenrolldate);
                        Datastorage.LastUpdatedtime(SplashActivity.this,
                                _lastupdatedtime);
                        Datastorage.SetRouteId(SplashActivity.this, RtId);
                        //Datastorage.SetIsfeeshow(SplashActivity.this,isfeeshow);
                    }
                }/*
                 * else { if (rememberMe == true) {
                 *
                 * RememberCheckBoxChecked(); }
                 */
            } catch (Exception err) {
                Constants.writelog(
                        "SplashActivity",
                        "Exception:1309 " + err.getMessage() + "::::"
                                + err.getStackTrace());
            }

        }

    }

}
