package com.expedite.apps.kumkum.constants;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.BuildConfig;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.ConnectionDetector;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.common.GetIp;
import com.expedite.apps.kumkum.common.Utils;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.Contact;
import com.expedite.apps.kumkum.model.DailyDiaryCalendarModel;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

public final class Constants {
    public static final String BASE_URL = "http://webapi.vayuna.com/api/";
    public static final String APPOINTMENT_URL_VISITOR = "http://visitor.macademicsolutions.com/Api/VisitorApi/";
    public static final String NAMESPACE = "http://tempuri.org/";
    public static final String PLATFORM = "0";
    public static final String CODEVERSION = "5";
    public static final String APPLOGO = "APPLOGO";
    public static final String APP_MENU_NAME = "APP_MENU_NAME";
    public static final String APPname = "APPname";
    public static final String LastAppControlCall = "LastAppControlCall";
    public static final int Timeout = 200000;
    public static final String HWPDF = "HomeworkPdf";
    public static final String serverUrl = "http://www.vayuna.com";
    public static final String circularUrl = "https://s3.ap-south-1.amazonaws.com/espschools/circularforstudent/";
    public static final String SERVER_URL = "http://testm.vayuna.com/gcm_server_php/register.php";
    public static String FILE_AUDIO_PATH = android.os.Environment.getExternalStorageDirectory()
            + File.separator
            + SchoolDetails.PhotoGalleryFolder + "/Audio File";
    public static String FILE_VIDEO_PATH = android.os.Environment.getExternalStorageDirectory()
            + File.separator
            + SchoolDetails.PhotoGalleryFolder + "/Video File";
    public static String FILE_PRACTICETEST_PATH = android.os.Environment.getExternalStorageDirectory()
            + File.separator
            + SchoolDetails.PhotoGalleryFolder + "/Practice Test";

    public static String FILE_PDF_PATH = android.os.Environment.getExternalStorageDirectory()
            + File.separator + SchoolDetails.PhotoGalleryFolder + "/Pdf File";
    public static String FILE_IMG_PATH = android.os.Environment.getExternalStorageDirectory()
            + File.separator + SchoolDetails.PhotoGalleryFolder + "/Image";

    public static final String INTERSTITIAL_ADD_UNIT = "";
    // Google project id
    public static final String SENDER_ID = "1032881273580";
    public final static String adCurrentCount = "addcount";
    public final static String ShowSubNew = "1";
    public final static String isShowAdd = "isShowadd";
    public final static String currentTime = "currenttime";
    public final static String AdFrequencyCount = "addShowingCount";
    public final static String ActivityString = "ActivityString";
    public final static String GET_HOME_DETAIL = "ParentsDashboard/GetParentsDashboard";
    public final static String GET_MESSAGE_DETAIL = "GetMessageDetails_json_V1";
    public final static String GET_NEW_MESSAGE_DETAIL = "GetMessageDetailsForNotification_V1";
    public final static String GET_CALENDAR_DETAIL = "EventListAndroid";
    public final static String GET_FOOD_CHART = "FoodChartListNew";
    public final static String GET_BANNER_DETAILS = "bannerdetails_V2";
    public final static String GET_ISSUE_BOOK_LIST = "BookIssueList";
    public final static String GET_TIME_TABLE_LIST = "TimeTableList";
    public final static String GET_CURRICULUM_LIST = "folderFileList_V1";
    public final static String GET_CURRICULUM_LIST_Classroom = "folderFileListClassRoom_V1";
    //http://testapp.vayuna.com/Service.asmx?op=folderFileListClassRoom_V2
    public final static String GET_CURRICULUM_LIST_Classroom_V2 = "folderFileListClassRoom_V2";
    public final static String GET_SURVEY_POLL_TASK = "SurveyPollList";
    public final static String GET_SURVEY_POLL_TASK_SAVE = "SurveyAnsSave";
    public final static String LOG_USER_VISITED = "LogUserVisted";
    public final static String GET_ALL_ACADEMIC_YEAR = "GetAllAcademicYear";
    public final static String GET_USER_DETAILS_KUMKUM = "GetUserDetailsKumKum";
    public final static String DELETE_ACCOUNT_FROM_GCM_DATA = "DeleteAccountFromGCMData";
    public final static String CHECK_LOGIN_PIN1 = "CheckLoginPin1";
    public final static String GET_LOGIN_OTP = "SendOTP_V2";
    public final static String GET_VALIDATE_OTP = "ValidateOtp";
    public final static String GET_OTP_VERIFY = "GetAllAccountFromNumber";
    public final static String GET_ACCOUNT_UPDATE = "GetAllAccountForUpdate";
    public final static String GET_USER_DETAILS_VEHICLE = "GetUserDetailsKumKumvehicle";
    public final static String GET_PAYMENT_TRANSACTION = "GetPaymentTransaction_V2";
    public final static String UPDATE_PAYMENT_TRANSACTION = "updatePaymentTransaction_V2";
    public final static String GET_BANK_LIST = "GetBankListV2";
    public final static String GET_BANK_LIST_v3 = "GetBankListV3";
    public final static String CHANGE_LOGIN_PIN = "ChangeLoginPin";
    public final static String CHECK_LOGIN_PIN_EXIST = "CheckLoginPinExist";
    public final static String GET_MIN_MAX_AMOUNT_FOR_ONLINE_PAYMENT = "GetMinMaxAmountForOnlinePayment";
    //public final static String GET_STUDENT_IMAGE_PATH = "getStudentImagePath";
    public final static String FEES_CARD_DETAIL_V1 = "FeeCardDetails_v1";
    public final static String FEES_CARD_DETAIL_PAY_ONLINE_FEE = "FeeCardDetailsPayOnlineFee_v1";
    public final static String GET_MAP_URL = "getMapUrl";
    public final static String LOG_SUCESSFULLY_LOGIN = "LogSucessfullyLogin";
    public final static String GET_MONTH_LIST = "getmonthlist";
    public final static String GET_MESSAGE_DETAIL_SELECTED_MONTH = "GetMessageDetailsForSelectedMonth";
    public final static String GET_CIRCULAR_STUDENT_GROUPWISE = "GetCircularForStudentGroupwise";
    public final static String GET_CIRCULAR_STUDENT_GROUPWISE_NEW = "GetCircularForStudentGroupwiseNew";
    public final static String GENERATED_AND_SEND_OTP = "GenerateAndSendOTP";
    public final static String CHANGE_LOGINPIN_WITH_OTP = "ChangeLoginPinWithOtp";
    public final static String ALBUM_DETAILS = "AlbumDetails";
    public final static String APP_CONTROLNEW = "AppsControlNew4";
    public final static String GET_SCHOOL_MATE_LIST = "GetSchoolMatesList";
    public final static String GET_ATTENDANCE_KUMKUM = "GetAttendanceKumKum";
    public final static String GET_ONLINE_PAYMENT_HISTORY_STUDENT = "GetOnlinePaymentHistoryForStudent";
    public final static String GET_VEHICLE_LOCATION_JSON = "GetvehicleLocation_json_V2";
    public final static String GET_STUDENT_MARKSHEET_PATH = "getStudentMarksheetPath";
    public final static String SHOW_PROFILE_KUMKUM = "showprofilekumkum";
    public final static String GET_EXAM_MARK = "getExamsMarks";
    public final static String GET_STUDENT_CIRCULAR_PATH = "getStudentCircularPath";
    public final static String GET_REFUND_POLICY = "GetRefundpolicy";
    //End Change By Jaydeep 10 April 2017
    public final static String METHOD_NAME_GCM_REGISTRATION = "InsertGCMRegistration";
    public final static String METHOD_NAME_InsertNewAccountGCMData = "InsertNewAccountGCMData";
    public final static String METHOD_NAME_DeleteAccountFromGCMData = "DeleteAccountFromGCMData";
    public final static String METHOD_NAME_CheckDeviceRegistration = "CheckDeviceRegistration";
    public final static String METHOD_NAME_REPORT_APP_BUG = "SubmitQuery";
    public final static String METHOD_SEND_LOG = "WriteMobileAppLog";
    public final static String METHOD_NAME_CheckGCMRegistrationofDevice = "CheckGCMRegistrationofDevice";
    public final static String METHOD_NAME_UpdateGCMRegistration = "UpdateGCMRegistration";
    public final static String METHOD_NAME_CheckUserRegistered = "CheckUserRegistered";
    public final static String METHOD_NAME_UpdateMessageTable = "UpdateNotifcationTable";
    public final static String METHOD_NAME_GetSinglExams = "GetSingleExams";
    public final static String METHOD_NAME_SCHOOLID = "GetUserDetailsKumKum";
    public final static String METHOD_NAME_STUDENT_PROFILE = "showprofilekumkum";
    public final static String METHOD_NAME_GET_MSGDETAILS = "GetMessageDetailsLatestKumKum2";
    public final static String METHOD_NAME_GET_AlbumDetails = "AlbumDetailsWithClassSecId";
    public final static String METHOD_NAME_ABSENT = "GetLessonDairyMessageForAbsent";
    public final static String METHOD_NAME_STUDENTDETAILS = "GetStudentDetailsKumKumVehicle";
    public final static String METHOD_NAME_CLASSSECTION_NAME = "GetClassSectionName";
    public final static String METHOD_NAME_ACADEMIC_YEAR = "GetAllAcademicYear";
    public final static String GET_VALIDATE_ACCOUNT = "getValidateAccount";
    // change by Tejas Patel 23-07-2018
    public final static String GET_EXAMS_DATA = "getExams_json";
    public final static String GET_MARKSHEET_EXAMS_WITH_PATH_DATA = "getMarksheetExams_json";
    public final static String GET_EXAM_MARK_DATA = "getExamsMarks_json";
    // change by tejas patel 17-08-2018
    public final static String GET_LEAVE_HISTORY = "leavehistory_json";
    public static final String GET_LEAVE_TYPE = "leavemaster_json";
    public static final String GET_LEAVE_APPLY = "sendleaverequeststudent_Json";
    public static final String GET_LEAVE_PDF_IMAGE_FTP_DETAIL = "GetDocumentUploadFTPDetails_V1";
    // change by tejas patel 06-09-2018
    public final static String GET_SOCIAL_MEDIA_LIST = "socialaccountdetail_json";
    public final static String GET_PARENTS_PROFILE_LIST = "showparentprofile_json";
    public final static String GET_CHECK_TRANSACTION_STATUS = "checkTransactionStatus";
    public static final String TAG = "AndroidHive GCM";
    public static final String DISPLAY_MESSAGE_ACTION = "com.expedite.apps.carmel.DISPLAY_MESSAGE";
    public static final String EXTRA_MESSAGE = "message";
    //change by vishwa patel
    public static final String GET_STUDENT_EMAILID = "getstudentmailid_json";
    public static final String GET_SEND_EMAILID = "sendemail_json";
    public static final String RESPONSE_URL = "ResponseUrl";
    public final static String GET_CIRCULAR_STUDENT = "GetCircular_json";
    public final static String GET_PRACTICE_TEST = "GetTestList_V3";
    public final static String GET_PRACTICE_PENDING_TEST_COUNT = "GetPendingTestCount";
    public final static String GET_PRACTICE_TEST_QA = "GetTestDetails";
    public final static String GET_CALCULATE_SUMMARY = "GetSummary_V3";
    public final static String GET_GUARDIAN_HOST_LIST = "TakeAppointmentserv";
    public final static String GET_APPOINTMENT_LIST = "ListAppointmentVisitor";
    public final static String GET_TIME_LIST = "GetGroupListFromHost";
    public final static String SET_BOOK_APPOINTMENT = "BookAppointment";
    public final static String SET_PRACTICE_TEST_ANS = "GetSaveTestData_V1";
    public final static String CurrentDate = "currentDate";

    public final static String SET_PRACTICE_TEST_ANS_V2 = "GetSaveTestData_V2";
    public final static String SET_PRACTICE_TEST_ANS_V3 = "GetSaveTestData_V3";
    public final static String GET_STUDENT_IMAGE_PATH_V1 = "getStudentImagePath_V1";
    public final static String GET_STUDENT_MARKSHEET_PATH_V1 = "getStudentMarksheetPath_V1";
    public final static String GET_STUDENT_DOCUMENTS = "GetStudentDocuments_V1";
    //vijay solanki 02-01-2020
    public final static String GET_VOICE_MESSAGE = "GetVoiceMessageDetails";
    public static final String GET_INSERT_STUDENT_CURRICULAM_RESPOSE = "InsertStudentCurriculumResponse";
    public static final String GET_INSERT_STUDENT_CURRICULAM_RESPOSE_Classroom = "InsertStudentCurriculumResponseClassRoom";
    public static final String GET_STUDENT_CURRICULAM_RESPOSE = "GetStudentCurriculumResponse";
    public static final String GET_STUDENT_CURRICULAM_RESPOSE_Classroom = "GetStudentCurriculumResponseClassRoom";
    public static final String GET_E_LIBRARY = "getElibraryUrl";
    // For Lesson Diary
    public static final String HW_HOMEWORK = "1";
    public static final String HW_HOMEWORKNOTDONE = "2";
    public static final String HW_ABSENT = "3";
    public static final String HW_LATECOME = "4";
    public static final String HW_UNIFORMNOTPROPER = "5";
    public static final String CALENDER = "6";
    public static final String FOOD = "7";
    public static final String HYGIENE = "8";
    public static final String REMARKS = "9";
    public static final String button_click = "Button_Click";
    public static String PREF_NAME = "vayuna.pref";
    public static Integer NOTIFICATION_ID = 0;
    public static String METHOD_NAME_GET_FEEdBACK_HISTORY = "GetFeedbackHistory";
    //  public static String METHOD_NAME_GET_FEEdBACK_CATEGORY = "GetFeedbackCategory_json";
    public static final String METHOD_NAME_GET_FEEdBACK_CATEGORY = "GetFeedbackCategory_json";
    public final static String GET_SUBMIT_QUERY = "SubmitQuery_json_V1";
    public static Boolean isShowInternetMsg = false;
    public static int RESULT_LEAVE_APPLY_SUCCESS = 2001;
    public static String parentId = "";

    public static final String PractiseTestData = "PractiseTestData";
    public SharedPreferences settings;
//    public static final String FIRST_COLUMN = "Messages";
//    public static final String SECOND_COLUMN = "Date";
//    public static final String THIRD_COLUMN = "Time";
//    public static final String Ui_update = "Ui_Update";
//    public static String COMMON_PAYMENTMESSAGE = "paymentmsg";
//    public static String COMMON_PAYMENTKEY = "paymentkey";
//    public static String COMMON_PAYMENTID = "paymentID";
//    public static String COMMON_PAYMENTKEYVALUE = "paymentkeyvalue";

    /**
     * Notifies UI to display a message.
     * <p/>
     * This method is defined in the common helper because it's used both by the
     * UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }


    public static String InsertGCMRegistration(Context cntxt) {
        String status = "";
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_GCM_REGISTRATION);
        request.addProperty("Registration_Id", Datastorage.GetGcmRegId(cntxt));
        request.addProperty("Device_Id", Datastorage.GetDeviceId(cntxt));
        request.addProperty("Mobile_Number", Datastorage.GetPhoneNumber(cntxt));
        try {
            SoapObject result = Constants.CallWebMethod(cntxt, request,
                    METHOD_NAME_GCM_REGISTRATION, false);
            if (result != null && result.getPropertyCount() > 0) {
                status = result.getPropertyAsString(0);
            }
        } catch (Exception e) {

            return status;
        }
        return status;
    }

    public static String InsertNewAccountGCMData(int Student_Id, int School_Id,
                                                 Context cntxt) {
        String status = "";
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_InsertNewAccountGCMData);

        request.addProperty("Device_Id", Datastorage.GetDeviceId(cntxt));
        request.addProperty("Student_Id", Student_Id);
        request.addProperty("School_Id", School_Id);
        // request.addProperty("pin", pin);

        try {
            SoapObject result = Constants.CallWebMethod(cntxt, request,
                    METHOD_NAME_InsertNewAccountGCMData, false);
            if (result != null && result.getPropertyCount() > 0) {
                status = result.getPropertyAsString(0);
            }
        } catch (Exception e) {

            Constants.Logwrite("InsertNewAccont:", "Message:" + e.getMessage());
            //Constants.Logwrite("InsertNewAccont:"+e.getStackTrace());
            // return status;
        }
        return status;
    }

    public static String getCurrentHour(Context context) {
        SimpleDateFormat format = new SimpleDateFormat("HH", Locale.US);
        String hour = format.format(new Date());
        Common.showLog("Hour 71 Contants", hour);
        return hour;
    }

    public static String DeleteAccountFromGCMData(int Student_Id,
                                                  int School_Id, Context cntxt) {
        Constants.writelog("CommonUtilites",
                "DeleteAccountFromGCMData 222: Studid" + Student_Id
                        + "  schoolid:" + School_Id);
        String status = "";
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_DeleteAccountFromGCMData);
        request.addProperty("Device_Id", Datastorage.GetDeviceId(cntxt));
        request.addProperty("Student_Id", Student_Id);
        request.addProperty("School_Id", School_Id);

        try {

            SoapObject result = Constants.CallWebMethod(cntxt, request,
                    METHOD_NAME_DeleteAccountFromGCMData, false);
            ;
            if (result != null && result.getPropertyCount() > 0) {
                status = result.getPropertyAsString(0);
            }
            Constants.writelog("CommonUtilites",
                    "DeleteAccountFromGCMData 246 result:" + status);

        } catch (Exception e) {
            Constants.writelog("CommonUtilites",
                    "Exception DeleteAccountFromGCMData 250:" + status);
            return status;
        }
        return status;
    }


    public static String CheckUserRegistered(int Student_Id, int School_Id,
                                             Context cntxt) {
        String status = "";
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_CheckUserRegistered);

        request.addProperty("Device_Id", Datastorage.GetDeviceId(cntxt));
        request.addProperty("Student_Id", Student_Id);
        request.addProperty("School_Id", School_Id);

        try {
            SoapObject result = Constants.CallWebMethod(cntxt, request,
                    METHOD_NAME_CheckUserRegistered, false);
            if (result != null && result.getPropertyCount() > 0) {
                status = result.getPropertyAsString(0);
            }
        } catch (Exception e) {
            return status;
        }
        return status;
    }

    public static String CheckDeviceRegistration(Context cntxt) {
        String status = "";
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_CheckDeviceRegistration);
        request.addProperty("Device_Id", Datastorage.GetDeviceId(cntxt));
        try {
            SoapObject result = Constants.CallWebMethod(cntxt, request,
                    METHOD_NAME_CheckDeviceRegistration, false);
            if (result != null && result.getPropertyCount() > 0) {
                status = result.getPropertyAsString(0);
            }
        } catch (Exception e) {
            return status;
        }
        return status;
    }

    public static String CheckGCMRegistrationofDevice(Context cntxt) {
        String status = "";
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_CheckGCMRegistrationofDevice);

        request.addProperty("Device_Id", Datastorage.GetDeviceId(cntxt));
        request.addProperty("Registration_Id", Datastorage.GetGcmRegId(cntxt));
        try {
            SoapObject result = Constants.CallWebMethod(cntxt, request,
                    METHOD_NAME_CheckGCMRegistrationofDevice, false);
            if (result != null && result.getPropertyCount() > 0) {
                status = result.getPropertyAsString(0);
            }
        } catch (Exception e) {

            return status;
        }
        return status;
    }

    public static String UpdateGCMRegistration(Context cntxt) {
        String status = "";
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_UpdateGCMRegistration);

        request.addProperty("Registration_Id", Datastorage.GetGcmRegId(cntxt));
        request.addProperty("Device_Id", Datastorage.GetDeviceId(cntxt));

        try {
            SoapObject result = Constants.CallWebMethod(cntxt, request,
                    METHOD_NAME_UpdateGCMRegistration, false);
            if (result != null && result.getPropertyCount() > 0) {
                status = result.getPropertyAsString(0);
            }
        } catch (Exception e) {
            return status;
        }
        return status;
    }

    public static void NotifyNoInternet(Context cn) {
        if (Constants.isShowInternetMsg) {
            Toast.makeText(cn, SchoolDetails.MsgNoInternet, Toast.LENGTH_LONG).show();
            Constants.isShowInternetMsg = false;
        }
    }

    public static SoapObject CallWebMethod(Context cn, SoapObject request,
                                           String MethodName, Boolean isCompulsory) {
        SoapObject result = null;
        try {
            Constants.Logwrite("Constants.java", "Request:" + request.toString());
            ConnectionDetector cd = new ConnectionDetector(cn);
            if (cd.isConnectingToInternet()) {
                if (isCompulsory) {
                    Constants.isShowInternetMsg = false;
                }
                String NAMESPACE = "http://tempuri.org/";
                String URL = GetIp.ip();
                if (MethodName == "getVechicleLocation"
                        || MethodName == "GetVehicleDetails"
                        || MethodName == "getLocationDetail"
                        || MethodName == "GetVehicleLocationFromRouteId") {
                    URL = GetIp.ipvehicle();
                }
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, Constants.Timeout);
                androidHttpTransport.call(NAMESPACE + MethodName, envelope);
                result = (SoapObject) envelope.bodyIn;
                if (BuildConfig.DEBUG) {
                    Constants.Logwrite("Constants.java", "Request 425 :" + request.toString());
                    Constants.Logwrite("Constants.java", "Responce 426:" + result.toString());
                }
            } else {
                if (!MethodName.equalsIgnoreCase("LogUserVisted") && isCompulsory == true) {
                    Constants.isShowInternetMsg = true;
                }
            }
        } catch (Exception ex) {
            Log.e("CommonUtility", "CallWebMethod()384 Request:" + request + "\nEx:" + ex.getStackTrace());
            Constants.writelog("CommonUtility", "CallWebMethod()384 Ex:"
                    + ex.getMessage() + ":::::::" + ex.getStackTrace());
        }
        return result;
    }

    public static void LogUserVisited(Context cn, int moduleid, int pageId) {
        try {
            Constants.Logwrite("ProfileActivity", "LogUserVisited Start.");
            SoapObject request = new SoapObject(NAMESPACE, LOG_USER_VISITED);
            request.addProperty("moduleid", moduleid);
            request.addProperty("schoolid", Datastorage.GetSchoolId(cn));
            request.addProperty("User_Id", Datastorage.GetUserId(cn));
            request.addProperty("phoneno", Datastorage.GetPhoneNumber(cn));
            request.addProperty("pageid", pageId);
            try {
                CallWebMethod(cn, request, LOG_USER_VISITED, false);
            } catch (Exception err) {
            }
        } catch (Exception ex) {
            Constants.writelog("CommonUtility", "LogUserVisited()235 Ex:"
                    + ex.getMessage() + ":::::::" + ex.getStackTrace());
        }
    }

    public static void GetNewMessageDetail(Context activity, final String studId, final String schoolId, int latest_SMS_ID) {
        try {
            final DatabaseHandler db = new DatabaseHandler(activity);
            ConnectionDetector cd = new ConnectionDetector(activity);
            if (cd.isConnectingToInternet()) {
                try {
                    String yearid = Datastorage.GetCurrentYearId(activity);
                    //mProgressBar.setVisibility(View.VISIBLE);
                    Call<DailyDiaryCalendarModel> mCall = ((MyApplication) activity.getApplicationContext()).getmRetrofitInterfaceAppService()
                            .GetNewMessageDetail(studId, schoolId, latest_SMS_ID + "", yearid, "all", Constants.PLATFORM, Constants.CODEVERSION);
                    mCall.enqueue(new retrofit2.Callback<DailyDiaryCalendarModel>() {
                        @Override
                        public void onResponse(Call<DailyDiaryCalendarModel> call, Response<DailyDiaryCalendarModel> response) {
                            DailyDiaryCalendarModel tmp = response.body();
                            try {
                                if (tmp != null && tmp.strlist.size() > 0) {
                                    for (int i = 0; i < tmp.strlist.size(); i++) {
                                        int ModId = Integer.parseInt(tmp.strlist.get(i).fifth);
                                        int SMS_DAY = Integer.parseInt(tmp.strlist.get(i).first);
                                        int SMS_MONTH = Integer.parseInt(tmp.strlist.get(i).second);
                                        int SMS_YEAR = Integer.parseInt(tmp.strlist.get(i).third);
                                        int SMS_MSG_ID = Integer.parseInt(tmp.strlist.get(i).sixth);
                                        Boolean Ismessgaeinsert = false;
                                        if (ModId == 5) {
                                            Ismessgaeinsert = db
                                                    .CheckAbsentMessageInsertorNot(
                                                            Integer.parseInt(studId),
                                                            Integer.parseInt(schoolId),
                                                            SMS_DAY, SMS_MONTH, SMS_YEAR,
                                                            ModId);
                                        } else {
                                            Ismessgaeinsert = db.CheckMessageInsertorNot(
                                                    Integer.parseInt(studId),
                                                    Integer.parseInt(schoolId), SMS_MSG_ID);
                                        }
                                        String msg = "";
                                        if (ModId == 0) {
                                            msg = tmp.strlist.get(i).eighth + "##,@@" + tmp.strlist.get(i).nineth;
                                        } else {
                                            msg = tmp.strlist.get(i).eighth;
                                        }
                                        if (Ismessgaeinsert) {
                                            db.Updatesms(new Contact(
                                                    Integer.parseInt(tmp.strlist.get(i).sixth),
                                                    msg,
                                                    Integer.parseInt(studId),
                                                    Integer.parseInt(schoolId),
                                                    Integer.parseInt(tmp.strlist.get(i).first),
                                                    Integer.parseInt(tmp.strlist.get(i).second),
                                                    Integer.parseInt(tmp.strlist.get(i).third),
                                                    Integer.parseInt(tmp.strlist.get(i).fifth),
                                                    Integer.parseInt(tmp.strlist.get(i).seventh)));
                                            // Constants.writelog("DailyDiary",
                                            // "SmsUpdate()949 NO"+i+" Messageid:"+msgitem[4]);
                                        } else {
                                            db.AddSMS(new Contact(
                                                    Integer.parseInt(tmp.strlist.get(i).sixth),
                                                    msg,
                                                    Integer.parseInt(studId),
                                                    Integer.parseInt(schoolId),
                                                    Integer.parseInt(tmp.strlist.get(i).first),
                                                    Integer.parseInt(tmp.strlist.get(i).second),
                                                    Integer.parseInt(tmp.strlist.get(i).third),
                                                    Integer.parseInt(tmp.strlist.get(i).fifth),
                                                    Integer.parseInt(tmp.strlist.get(i).seventh)));
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                Constants.writelog("GetNewMessageDetail:537:ErrorMsg::", ex.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<DailyDiaryCalendarModel> call, Throwable t) {
                            Constants.writelog("GetNewMessageDetail:550:ErrorMsg::", "onFailure()");
                        }
                    });

                } catch (Exception ex) {
                    Constants.writelog("GetNewMessageDetail:555:ErrorMsg::", ex.getMessage());
                }
            }
        } catch (Exception ex) {
            Constants.writelog("GetNewMessageDetail:559:ErrorMsg::", ex.getMessage());
        }
    }

    public static String[] GetMessageDetailsLatestKumKum2(Context cntxt,
                                                          int studid, int schoolid, int yearid) {
        String[] messages = null;
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_GET_MSGDETAILS);
        request.addProperty("StudId", studid);
        request.addProperty("SchoolId", schoolid);
        request.addProperty("MsgId", 0);
        request.addProperty("YearId", yearid);

        try {
            SoapObject result = CallWebMethod(cntxt, request,
                    METHOD_NAME_GET_MSGDETAILS, false);
            Constants.Logwrite("MessageList", "Result length is "
                    + result.toString().length());

            if (result != null && result.getPropertyCount() > 0) {
                SoapObject obj2 = (SoapObject) result.getProperty(0);

                Constants.Logwrite("MsgLogCount:", "----------------");
                if (obj2 != null) {
                    int count = obj2.getPropertyCount();
                    messages = new String[count];
                    for (int i = 0; i < count; i++) {
                        messages[i] = obj2.getProperty(i).toString();
                    }
                } else {
                    messages = null;
                }
            }
        } catch (Exception ex) {
            Constants
                    .writelog("Constants", "692 Ex:" + ex.getMessage()
                            + "::::::" + ex.getStackTrace());
            return messages;
        }
        return messages;
    }

    public static String UpdateNotifcationTable(Context cntxt, int MessageId) {
        String status = "";
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_UpdateMessageTable);
        request.addProperty("MessageId", MessageId);
        try {
            SoapObject result = CallWebMethod(cntxt, request,
                    METHOD_NAME_UpdateMessageTable, false);
            if (result != null && result.getPropertyCount() > 0) {
                status = result.getPropertyAsString(0);
            }
        } catch (Exception e) {
            return status;
        }
        return status;
    }

    // Jaydeep 19 April 2017
    public static void setActionbarHome(ActionBar ab, Activity abc, Context cn, String ActivityName, String analyticText) {
        try {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
            ab.setDisplayShowCustomEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                ab.setTitle("");
                View customview = abc.getLayoutInflater().inflate(R.layout.mainactionbar, null);
                TextView label = (TextView) customview.findViewById(R.id.label);
                label.setText(ActivityName);
                ab.setCustomView(customview);
                // ab.setIcon(R.drawable.notification);
            } else {
                ab.setIcon(R.mipmap.notification);
            }
            if (SchoolDetails.appname < 42)
                Constants.googleAnalytic(cn, analyticText);
        } catch (Exception ex) {
            Constants.writelog("CommonUtility_setActionbar()", "MSG:" + ex.getMessage());
        }
    }

    public static void setActionbar1(ActionBar ab, Activity abc, Context cn, String ActivityName, String analyticText) {
        try {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
            ab.setDisplayShowCustomEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                ab.setTitle("");
                View customview = abc.getLayoutInflater().inflate(R.layout.mainactionbar, null);
                TextView label = (TextView) customview.findViewById(R.id.label);

                ab.setCustomView(customview);
                // ab.setIcon(R.drawable.notification);
            } else {
                ab.setIcon(R.mipmap.notification);
            }
            if (SchoolDetails.appname < 42)
                Constants.googleAnalytic(cn, analyticText);
        } catch (Exception ex) {
            Constants.writelog("CommonUtility_setActionbar()", "MSG:" + ex.getMessage());
        }
    }

    public static void setActionbar(ActionBar ab, Activity abc, Context cn, String ActivityName, String analyticText, String activityName) {
        try {
            Constants.writelog("Called", "1");
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
            ab.setDisplayShowCustomEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                ab.setTitle("");
                View customview = abc.getLayoutInflater().inflate(R.layout.mainactionbar, null);
                TextView label = (TextView) customview.findViewById(R.id.label);
                label.setText(ActivityName);
                ab.setCustomView(customview);
                // ab.setIcon(R.drawable.notification);
            } else {
                ab.setIcon(R.mipmap.notification);
            }
            ((BaseActivity) cn).showFullScreenAd(cn, activityName);
            if (SchoolDetails.appname < 42)
                Constants.googleAnalytic(cn, analyticText);
        } catch (Exception ex) {
            Constants.writelog("CommonUtility_setActionbar()", "MSG:" + ex.getMessage());
        }
    }

    public static String GetSingleExams(Context cntxt, int schoolid,
                                        int studid, int examid1, DatabaseHandler db) {
        String aa = "";
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_GetSinglExams);
        request.addProperty("stud_id", studid);
        request.addProperty("schoolid", schoolid);
        request.addProperty("examid1", examid1);

        try {
            SoapObject result = CallWebMethod(cntxt, request,
                    METHOD_NAME_GetSinglExams, false);
            if (result != null && result.getPropertyCount() > 0) {
                aa = result.getPropertyAsString(0);
                Constants.Logwrite("GetSingleExams:", "StringResult:" + aa);
            }
        } catch (Exception err) {
            Constants.Logwrite("GetSingleExams:", "Exception:" + err.getMessage()
                    + "StackTrace::" + err.getStackTrace().toString());
        }
        return aa;
    }

    public static String GetUserDetailsKumKum(Context cntxt, String Phone,
                                              int Pin, int studId) {
        String resp = "";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SCHOOLID);
        request.addProperty("PhoneNo", Phone);
        request.addProperty("Log_pin", Pin);
        request.addProperty("YearId", 0);
        request.addProperty("studid", studId);

        try {
            SoapObject result = CallWebMethod(cntxt, request,
                    METHOD_NAME_SCHOOLID, false);
            if (result != null && result.getPropertyCount() > 0) {
                String info = result.getPropertyAsString(0);
                // String[] parts = info.split(",");
                resp = info;
                // return resp;
            }
        } catch (Exception e) {
            Constants.Logwrite("StartupActivity",
                    "GetUserDetails()_Exception:" + e.getMessage()
                            + "_Stacktrace:" + e.getStackTrace());
            Constants.writelog("CommonUtility",
                    "Exception 542:GetUserDetailsKumKum()" + e.getMessage()
                            + "::::" + e.getStackTrace());
        }
        return resp;
    }

    public static String GetClassSectionName(Context cntxt, int Studid,
                                             int yearid, String schoolid) {

        String res = "";
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_CLASSSECTION_NAME);
        request.addProperty("studid", Studid);
        request.addProperty("year_id", yearid);
        request.addProperty("schoolid", schoolid);

        try {

            SoapObject result = CallWebMethod(cntxt, request,
                    METHOD_NAME_CLASSSECTION_NAME, false);
            if (result != null && result.getPropertyCount() > 0) {
                res = result.getPropertyAsString(0);
                // String[] parts = info.split(",");

                // return resp;
            }
        } catch (Exception e) {
            Constants.Logwrite("CommonUtility",
                    "GetClassSectionName()_Exception:578 " + e.getMessage()
                            + "_Stacktrace:" + e.getStackTrace());
            Constants.writelog("CommonUtility",
                    "GetClassSectionName()_Exception:578 " + e.getMessage()
                            + "_Stacktrace:" + e.getStackTrace());
        }
        Constants.Logwrite("CommonUtility", "GetClassSectionName()_End ");
        Constants.writelog("CommonUtility",
                "GetClassSectionName() End Suceesful Result:" + res);
        return res;
    }

    public static String GetStudentDetailsKumKumVehicle(Context cntxt,
                                                        String School_Id, String studid, int YearId) {
        String resp = "";
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_STUDENTDETAILS);
        request.addProperty("School_Id", School_Id);
        request.addProperty("studid", studid);
        request.addProperty("YearId", YearId);

        try {
            SoapObject result = CallWebMethod(cntxt, request,
                    METHOD_NAME_STUDENTDETAILS, false);
            if (result != null && result.getPropertyCount() > 0) {
                String info = result.getPropertyAsString(0);
                // String[] parts = info.split(",");
                resp = info;
                // return resp;
            }
        } catch (Exception e) {
            Constants.Logwrite("StartupActivity",
                    "GetUserDetails()_Exception:" + e.getMessage()
                            + "_Stacktrace:" + e.getStackTrace());
            Constants.writelog(
                    "CommonUtility",
                    "Exception GetStudentDetailsKumKumVehicle(): 615:"
                            + e.getMessage() + ":::::" + e.getStackTrace());
            return resp;
        }
        return resp;
    }

    public static HashMap<Integer, String> AddAccount(Menu menu, DatabaseHandler db1) {

        HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
        try {
            int Record_Cnt = db1.getContactsCount();
            Constants.Logwrite("Constants", "RecordCount:" + Record_Cnt);
            // List<Contact> contacts = db.getAllContacts();
            List<Contact> contacts = db1.getPhoneAndPin();
            Constants.Logwrite("Constants", "TotalRecord:" + contacts.size());

            if (Record_Cnt > 0) {
                int cntr = menu.size() + 1;
                for (Contact cn : contacts) {

                    if (cn != null) {

                        menu.add(0, cntr, cntr, cn.getName()).setTitle(cn.getName());
                        mapacc.put(cntr, cn.getPhoneNumber() + "," + cn.getSchoolId() + "," + cn.getStudentId());
                        Constants.Logwrite("Constants", "Record_Count:" + cntr + ":"
                                + cn.getPhoneNumber() + "," + cn.getSchoolId()
                                + "," + cn.getStudentId());
                        cntr++;
                    }
                }
            }
        } catch (Exception err) {
            Constants.Logwrite("Constants", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());
        }
        return mapacc;
    }
    /*public static String[] GetMessageDetailsForNotificationNew(Context cntxt,
                                                               String studid, String schoolid, String msgid) {
        String[] messages = null;
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_GetMessageDetailsForNotification);
        request.addProperty("StudId", studid);
        request.addProperty("SchoolId", schoolid);
        request.addProperty("Messageid", Long.parseLong(msgid));

        try {
            SoapObject result = CallWebMethod(cntxt, request,
                    METHOD_NAME_GetMessageDetailsForNotification, false);

            Constants.Logwrite("MessageList", "Result length is "
                    + result.toString().length());

            if (result != null && result.getPropertyCount() > 0) {
                // String info = result.getPropertyAsString(0);

                // String[] parts = info.split(",");
                SoapObject obj2 = (SoapObject) result.getProperty(0);
                Constants.Logwrite("MsgLogCount:", "----------------");
                if (obj2 != null) {
                    int count = obj2.getPropertyCount();
                    messages = new String[count];
                    String[] myarray = new String[count];
                    for (int i = 0; i < count; i++) {
                        myarray[i] = obj2.getProperty(i).toString();
                        messages[i] = myarray[i];
                        Constants.Logwrite("MsgLogCount:", "" + messages[i].length());
                    }
                } else {
                    messages = null;
                }
            }
        } catch (Exception ex) {
            Constants.writelog(
                    "CommonUtility",
                    "GetMessageDetailsForNotificationNew()443 "
                            + ex.getMessage());
            return messages;
        }
        return messages;
    }*/

    public static void Logwrite(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void SetAccountDetails(String details, Activity cntxt) {
        try {
            String[] split_details = details.split(",");
            String SchoolId = split_details[1];
            Log.i("Constants", "SchoolId:" + SchoolId);
            String StudId = split_details[2];
            Log.i("Constants", "StudentId:" + StudId);
            Datastorage.SetSchoolId(cntxt, SchoolId);
            Datastorage.SetStudentID(cntxt, StudId);
            ResetLastAutoUpdateDay(cntxt);
            DatabaseHandler db = new DatabaseHandler(cntxt);
            db.SetCurrentYearAsDefaultYear(StudId, SchoolId);
            int yearid = db.GetCurrentAcademicYearId(
                    Integer.parseInt(SchoolId), Integer.parseInt(StudId));
            Datastorage.SetAcademicYear(cntxt, yearid + "");
            Datastorage.SetCurrentYearId(cntxt, yearid + "");
            String studname = "";
            Cursor cn = db.getStudentDetails(Integer.parseInt(StudId), Integer.parseInt(SchoolId));
            if (cn != null && cn.moveToFirst()) {
                studname = cn.getString(cn
                        .getColumnIndex(DatabaseHandler.KEY_NAME));
                Datastorage.SetStudentName(cntxt, studname);
            }
            cn.close();
        } catch (Exception err) {
            Constants.writelog(
                    "Commonutility",
                    "Exception: 673" + err.getMessage() + ":::::"
                            + err.getStackTrace());
        }
    }

    public static File CreatePhotoGalleryFolder() {
        File photogallerypath = null;
        try {
            Boolean IsSdPresent = CheckExternalSdCardPresent();
            if (IsSdPresent) {
                photogallerypath = new File(
                        Environment.getExternalStorageDirectory()
                                + File.separator
                                + SchoolDetails.PhotoGalleryFolder);
            } else {
                photogallerypath = new File(
                        Environment.getExternalStorageDirectory()
                                + File.separator
                                + SchoolDetails.PhotoGalleryFolder);
            }
            if (!photogallerypath.exists()) {
                photogallerypath.mkdirs();
            }
        } catch (Exception ex) {
            Constants.writelog("Constants",
                    "CreatePhotoGalleryFolder()721 Ex:" + ex.getMessage()
                            + "::::::" + ex.getStackTrace());
        }
        return photogallerypath;
    }

    /*public static int GetIsFeeShowConfig(Context cntxt, int Studid,
                                         String schoolid) {
        Constants.Logwrite("CommonUtility", "GetIsFeeShowConfig() start");
        Constants.writelog("CommonUtility",
                "GetIsFeeShowConfig() start Studentid:" + Studid + " schoolid:"
                        + schoolid);
        int res = 0;
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_ISFEESHOWCONFIG);
        request.addProperty("schoolid", schoolid);

        try {
            SoapObject result = CallWebMethod(cntxt, request,
                    METHOD_NAME_ISFEESHOWCONFIG, false);
            if (result != null && result.getPropertyCount()>0) {
                res = Integer.parseInt(result.getPropertyAsString(0));
            }
        } catch (Exception e) {
            Constants.Logwrite("CommonUtility",
                    "GetIsFeeShowConfig()_Exception:630 " + e.getMessage()
                            + "_Stacktrace:" + e.getStackTrace());
            Constants.writelog("CommonUtility",
                    "GetIsFeeShowConfig()_Exception:630 " + e.getMessage()
                            + "_Stacktrace:" + e.getStackTrace());
        }
        Constants.Logwrite("CommonUtility", "GetIsFeeShowConfig()_End ");
        Constants.writelog("CommonUtility",
                "GetIsFeeShowConfig() End Suceesful Result:" + res);
        return res;
    }*/

    public static String GetPhotoGalleryFolderPath() {
        String PhotoGallereyPath = "";
        try {
            PhotoGallereyPath = Environment.getExternalStorageDirectory()
                    + File.separator + SchoolDetails.PhotoGalleryFolder;

        } catch (Exception ex) {
            Constants.writelog("Constants",
                    "GetPhotoGalleryFolderPath()734 Ex:" + ex.getMessage()
                            + "::::::" + ex.getStackTrace());
        }
        return PhotoGallereyPath;
    }

    public static Bitmap getBitmap(String url, File F) {
        // from web
        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            if (url.contains(" ")) {
                imageUrl = new URL(url.replace(" ", "%20"));
            }

            HttpURLConnection conn = (HttpURLConnection) imageUrl
                    .openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(F);
            Utils.CopyStream(is, os);
            os.close();
            conn.disconnect();
            bitmap = BitmapFactory.decodeFile(String.valueOf(F));
            return bitmap;
        } catch (Exception ex) {
            Constants.Logwrite("Commonutility",
                    "Exception:getBitmap()805:" + ex.getMessage() + ":::::::::"
                            + ex.getStackTrace());
            Constants.writelog("Commonutility",
                    "Exception:getBitmap()805:" + ex.getMessage() + ":::::::::"
                            + ex.getStackTrace());
            return null;
        }
    }

    // decodes image and scales it to reduce memory consumption
    public static Bitmap decodeFile(File f) {
        try {
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = 1;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } catch (IOException ex) {
            Constants.writelog("Constants", "decodeFile()797 Ex:"
                    + ex.getMessage() + "::::::" + ex.getStackTrace());
        }
        return null;
    }

    public static Bitmap getImage(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return BitmapFactory.decodeStream(connection.getInputStream());
            } else
                return null;
        } catch (Exception ex) {
            Constants.writelog("Constants", "getImage()814 Ex:"
                    + ex.getMessage() + "::::::" + ex.getStackTrace());
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


 /*   public static String[] GetMessageDetailsLatestKumKum2(Context cntxt,
                                                          int studid, int schoolid, int yearid) {
        String[] messages = null;
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_GET_MSGDETAILS);
        request.addProperty("StudId", studid);
        request.addProperty("SchoolId", schoolid);
        request.addProperty("MsgId", 0);
        request.addProperty("YearId", yearid);

        try {
            SoapObject result = CallWebMethod(cntxt, request,
                    METHOD_NAME_GET_MSGDETAILS, false);
            Constants.Logwrite("MessageList", "Result length is "
                    + result.toString().length());

            if (result != null && result.getPropertyCount() > 0) {
                SoapObject obj2 = (SoapObject) result.getProperty(0);

                Constants.Logwrite("MsgLogCount:", "----------------");
                if (obj2 != null) {
                    int count = obj2.getPropertyCount();
                    messages = new String[count];
                    for (int i = 0; i < count; i++) {
                        messages[i] = obj2.getProperty(i).toString();
                    }
                } else {
                    messages = null;
                }
            }
        } catch (Exception ex) {
            Constants
                    .writelog("Constants", "692 Ex:" + ex.getMessage()
                            + "::::::" + ex.getStackTrace());
            return messages;
        }
        return messages;
    }*/

    public static Bitmap getImage(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
            //Bitmap img = BitmapFactory.decodeStream(url.openConnection().getInputStream());
           /* String context = url.getProtocol();
            String hostname = url.getHost();
            String thePath = url.getPath();
            int port = url.getPort();
            //thePath = thePath.replaceAll("(^/|/$)", ""); // removes beginning/end slash
            String encodedPath = URLEncoder.encode(thePath, "UTF-8"); // encodes unicode characters
           *//* encodedPath = encodedPath.replace("+", "%20"); // change + to %20 (space)
            encodedPath = encodedPath.replace("%2F", "/"); // change %2F back to slash
            urlString = context + "://" + hostname + ":" + port + "/" + encodedPath;*//*
            url= new URL(encodedPath);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getImage(url);
    }

    public static void SavePdf(Context cntxt, String fileURL, String fileneme) {
        try {
            ConnectionDetector cd = new ConnectionDetector(cntxt);
            if (cd.isConnectingToInternet()) {
                fileURL = fileURL.replace(" ", "%20");
                File myDir = CreatePhotoGalleryFolder();
                File file = new File(myDir, fileneme);
                FileOutputStream f = new FileOutputStream(file);
                URL u = new URL(fileURL);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                // c.setDoOutput(true);
                c.connect();
                InputStream in = c.getInputStream();

                byte[] buffer = new byte[2048];
                int len1 = 0;
                while ((len1 = in.read(buffer)) > 0) {
                    f.write(buffer, 0, len1);
                }
                f.close();
                Constants.Logwrite("Commonutility", "Save Circular successfuly");
            }
        } catch (Exception e) {
            Constants.writelog("Commonutilities",
                    "Exception saveCircular() 889" + e.getMessage() + "::::"
                            + e.getStackTrace());
            e.printStackTrace();
        }
    }

    public static void DeletePdf(String fileneme) {
        try {
            File myDir = CreatePhotoGalleryFolder();
            File file = new File(myDir, fileneme);
            file.delete();
        } catch (Exception e) {
            Constants.writelog(
                    "Commonutilities",
                    "Exception DeletePdf() 959" + e.getMessage() + "::::"
                            + e.getStackTrace());
            e.printStackTrace();
        }
    }

    public static void SaveImage(Bitmap finalBitmap, String fileneme) {

        File myDir = CreatePhotoGalleryFolder();
        String fname = fileneme;
        File file = new File(myDir, fname);
        if (file.exists()) {
            file.delete();
        }
        String extension = "";
        int i = fname.lastIndexOf('.');
        int p = Math.max(fname.lastIndexOf('/'), fname.lastIndexOf('\\'));

        if (i > p) {
            extension = fname.substring(i + 1);
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (extension.equalsIgnoreCase("jpeg")) {
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } else if (extension.equalsIgnoreCase("png")) {
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } else {
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] AlbumDetailsWithClassSecId(Context cntxt,
                                                      int SchoolId, int ClassSecId, int AlbumId, int IsAutoDownload) {
        String[] myarray = null;
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_GET_AlbumDetails);
        request.addProperty("SchoolId", SchoolId);
        request.addProperty("class_sec_id", ClassSecId);
        request.addProperty("PhotType", IsAutoDownload);
        request.addProperty("AlbumId", AlbumId);
        try {
            SoapObject result = CallWebMethod(cntxt, request,
                    METHOD_NAME_GET_AlbumDetails, false);
            Constants.Logwrite("MessageList", "Result length is "
                    + result.toString().length());
            if (result != null && result.getPropertyCount() > 0) {
                SoapObject obj2 = (SoapObject) result.getProperty(0);

                if (obj2 != null) {
                    int count = obj2.getPropertyCount();
                    myarray = new String[count];
                    for (int i = 0; i < count; i++) {
                        myarray[i] = obj2.getProperty(i).toString();
                    }
                } else {
                    myarray = null;
                }
            }
        } catch (Exception e) {
            return myarray;
        }
        return myarray;
    }

    public static void UpdateStudentprofile(Context cn, int studid,
                                            int schoolid, String yearid, DatabaseHandler db) {

        try {
            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME_STUDENT_PROFILE);
            request.addProperty("stud_id", studid);
            request.addProperty("school_id", schoolid);
            request.addProperty("Is_All", 1);
            request.addProperty("yearid", yearid);

            SoapObject result = Constants.CallWebMethod(cn, request,
                    METHOD_NAME_STUDENT_PROFILE, false);
            if (result != null && result.getPropertyCount() > 0) {
                String info = result.getPropertyAsString(0);
                String[] parts = info.split(",");
                Constants.Logwrite("CommonUtility", "parts:" + parts);
                String[] Lable = new String[parts.length];
                Constants.Logwrite("CommonUtility", "parts:" + Lable);
                String[] StudData = new String[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    String[] data = parts[i].split(":");
                    Lable[i] = data[0];
                    StudData[i] = data[1];
                }
                String grno = parts[0];
                String name = parts[1];
                String Classname = parts[2];
                String Caste = parts[3];
                String Category = parts[4];
                String DOB = parts[5];
                String ContactNumber = "";
                try {
                    ContactNumber = parts[6];
                } catch (Exception ex) {
                    Constants.writelog(
                            "ProfileActivity",
                            "Ex401:" + ex.getMessage() + "::::::::"
                                    + ex.getStackTrace());
                }

                int isupdate = db.updateStudProfile(studid, schoolid,
                        Integer.parseInt(yearid), grno, name, Classname, Caste,
                        Category, DOB, ContactNumber);
                if (isupdate != 1) {
                    db.AddStudentProfileDetails(studid, schoolid,
                            Integer.parseInt(yearid), grno, name, Classname,
                            Caste, Category, DOB, ContactNumber, "");
                }
            } else {
                Constants.Logwrite("CommonUtility",
                        "Error: UpdateStudentprofile() No responce from server.");
                Constants
                        .writelog("ProfileActivity",
                                "Error: UpdateStudentprofile() No responce from server.");
            }

        } catch (Exception err) {
            Constants.writelog("CommonUtility:",
                    "UpdateStudentprofile()1060 Exception:" + err.getMessage()
                            + "StackTrace::" + err.getStackTrace().toString());

        }
    }

    public static String[] GetLessonDairyMessageForAbsent(Context cntxt,
                                                          int StudentId, int SchoolId, int Year_Id) {
        String[] messages = null;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_ABSENT);
        request.addProperty("StudId", StudentId);
        request.addProperty("SchoolId", SchoolId);
        request.addProperty("YearId", Year_Id);
        try {
            SoapObject result = CallWebMethod(cntxt, request,
                    METHOD_NAME_ABSENT, false);
            Constants.Logwrite("GetLessonDairyMessageForAbsent", "Result length is "
                    + result.toString().length());
            if (result != null && result.getPropertyCount() > 0) {
                SoapObject obj2 = (SoapObject) result.getProperty(0);
                Constants.Logwrite("MsgLogCount:", "----------------");
                if (obj2 != null) {
                    int count = obj2.getPropertyCount();
                    messages = new String[count];
                    String[] myarray = new String[count];
                    for (int i = 0; i < count; i++) {
                        myarray[i] = obj2.getProperty(i).toString();
                        messages[i] = myarray[i];
                    }
                } else {
                    messages = null;
                }
            }
        } catch (Exception e) {

            return messages;
        }
        return messages;
    }

    public static Boolean CheckExternalSdCardPresent() {
        Boolean isPresent = true;

        try {
            isPresent = Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
        } catch (Exception err) {

        }
        return isPresent;

    }

    public static int setDefaultYear(Context cntxt, String studid,
                                     String schoolid, String yearid, DatabaseHandler db) {
        int res = 0;
        try {
            Constants.writelog("CommonUtility",
                    "SetDefaultYearCall() 1077 Studid:" + studid + " Schoolid:"
                            + schoolid + " Yearid:" + yearid);
            SQLiteDatabase myWritableDb = null;
            String KEY_ACADEMIC_IsDef = "IsDef";
            String TABLE_ACADEMIC_YEAR = "Academic_Year";
            String KEY_ACADEMIC_YEAR_ID = "YearId";
            // String KEY_ACADEMIC_YEAR_TEXT = "Year_Text";
            // String KEY_ACADEMIC_IS_CURRENT = "IsCurrent";
            String KEY_ACADEMIC_SCHOOLID = "SchoolId";
            String KEY_ACADEMIC_STUDID = "StudId";

            int isAcademicYearExist = 0;
            if (yearid != null && yearid != "") {
                isAcademicYearExist = db.isAcademicYearExist(yearid, schoolid,
                        studid);
            }
            if (isAcademicYearExist == 0) {
                int result = SetCurrentYearAsDefYear(cntxt, schoolid, studid,
                        db);
            } else {
                ContentValues values = new ContentValues();
                values.put(KEY_ACADEMIC_IsDef, 0);
                String table = TABLE_ACADEMIC_YEAR;
                myWritableDb = db.getWritableDatabase();
                String whereClause = "" + KEY_ACADEMIC_SCHOOLID + " =? AND "
                        + KEY_ACADEMIC_STUDID + " =?";
                String whereArgs[] = new String[]{schoolid, studid};
                myWritableDb.update(table, values, whereClause,
                        whereArgs);

                // set default year
                ContentValues values1 = new ContentValues();
                values1.put(KEY_ACADEMIC_IsDef, 1);
                String whereClause1 = "" + KEY_ACADEMIC_YEAR_ID + " =? AND "
                        + KEY_ACADEMIC_SCHOOLID + " =? AND "
                        + KEY_ACADEMIC_STUDID + " =?";
                String whereArgs1[] = new String[]{yearid, schoolid, studid};
                myWritableDb.update(table, values1, whereClause1,
                        whereArgs1);
                res = 1;
                Constants.writelog("CommonUtility",
                        "SetDefaultYear() 1138 Studid:" + studid + " Schoolid:"
                                + schoolid);
                db.close();
            }
        } catch (Exception ex) {
            Constants.writelog("commonutilities",
                    "setDefaultYear()1121 Yearid:" + yearid
                            + " Exception 1091:" + ex.getMessage()
                            + "StackTrace::" + ex.getStackTrace().toString());
        }
        return res;
    }

    // jaydeep 11-06-2015
    // This method set current year as default year of the student.
    public static int SetCurrentYearAsDefYear(Context cntxt, String SchoolId,
                                              String StudId, DatabaseHandler db) {
        int res = 0;
        String[] years = null;
        int[] yearsid = null;
        SQLiteDatabase myWritableDb = null;
        String KEY_ACADEMIC_IsDef = "IsDef";
        String TABLE_ACADEMIC_YEAR = "Academic_Year";
        String KEY_ACADEMIC_YEAR_ID = "YearId";
        String KEY_ACADEMIC_IS_CURRENT = "IsCurrent";
        String KEY_ACADEMIC_SCHOOLID = "SchoolId";
        String KEY_ACADEMIC_STUDID = "StudId";

        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_ACADEMIC_YEAR);
        request.addProperty("schoolid", SchoolId);
        request.addProperty("studid", StudId);

        try {
            SoapObject result = CallWebMethod(cntxt, request,
                    METHOD_NAME_ACADEMIC_YEAR, false);
            if (result != null && result.getPropertyCount() > 0) {
                SoapObject obj2 = (SoapObject) result.getProperty(0);
                String[] output = null;
                if (obj2 != null) {
                    // int count= 20; //obj2.getPropertyCount();
                    int count = obj2.getPropertyCount();
                    HashMap<String, Integer> map = new HashMap<String, Integer>();
                    output = new String[count];
                    years = new String[count];
                    yearsid = new int[count];
                    String[] myarray = new String[count];
                    for (int i = 0; i < count; i++) {
                        myarray[i] = obj2.getProperty(i).toString();
                        String[] parts = myarray[i].split("##,@@");
                        output[i] = parts[0];
                        years[i] = parts[0];
                        yearsid[i] = Integer.parseInt(parts[1]);
                        map.put(parts[0],
                                Integer.parseInt(parts[1]));
                        // Check Year Id is Insert Or Not
                        Boolean IsYearIdInsert = true;
                        IsYearIdInsert = db.CheckAcademicYear(
                                Integer.parseInt(StudId),
                                Integer.parseInt(SchoolId),
                                Integer.parseInt(parts[1]));
                        if (IsYearIdInsert) {
                            myWritableDb = db.getWritableDatabase();
                            String table = TABLE_ACADEMIC_YEAR;
                            ContentValues values1 = new ContentValues();
                            values1.put(KEY_ACADEMIC_IS_CURRENT,
                                    parts[2]);
                            String YEARID = parts[1];
                            String whereClause1 = "" + KEY_ACADEMIC_YEAR_ID
                                    + " =? AND " + KEY_ACADEMIC_SCHOOLID
                                    + " =? AND " + KEY_ACADEMIC_STUDID + " =?";
                            String whereArgs1[] = new String[]{YEARID,
                                    SchoolId, StudId};
                            count = myWritableDb.update(table, values1,
                                    whereClause1, whereArgs1);
                            db.close();
                        } else {
                            int Iscurrent = Integer.parseInt(parts[2]
                                    .toString());
                            if (Iscurrent == 1) {
                                db.AddYear(new Contact(Integer
                                        .parseInt(parts[1]),
                                        parts[0], Integer
                                        .parseInt(parts[2]),
                                        Integer.parseInt(SchoolId), Integer
                                        .parseInt(StudId), 1));
                            } else {
                                db.AddYear(new Contact(Integer
                                        .parseInt(parts[1]),
                                        parts[0], Integer
                                        .parseInt(parts[2]),
                                        Integer.parseInt(SchoolId), Integer
                                        .parseInt(StudId), 0));
                            }
                        }
                    }
                    try {
                        ContentValues values = new ContentValues();
                        values.put(KEY_ACADEMIC_IsDef, 0);
                        String table = TABLE_ACADEMIC_YEAR;
                        myWritableDb = db.getWritableDatabase();
                        String whereClause = "" + KEY_ACADEMIC_SCHOOLID
                                + " =? AND " + KEY_ACADEMIC_STUDID + " =?";
                        String whereArgs[] = new String[]{SchoolId, StudId};
                        count = myWritableDb.update(table, values, whereClause,
                                whereArgs);

                        // set default year
                        ContentValues values1 = new ContentValues();
                        values1.put(KEY_ACADEMIC_IsDef, 1);
                        String whereClause1 = "" + KEY_ACADEMIC_IS_CURRENT
                                + " =? AND " + KEY_ACADEMIC_SCHOOLID
                                + " =? AND " + KEY_ACADEMIC_STUDID + " =?";
                        String whereArgs1[] = new String[]{"1", SchoolId,
                                StudId};
                        count = myWritableDb.update(table, values1,
                                whereClause1, whereArgs1);
                        res = 1;
                        db.close();
                    } catch (Exception ex) {
                        Constants.writelog("commonutilities:141",
                                "Exception:" + ex.getMessage() + "StackTrace::"
                                        + ex.getStackTrace().toString());
                        ex.printStackTrace();
                        db.close();
                    }
                }
            }
            db.close();

        } catch (Exception ex) {
            Constants.writelog("commonutilities:1149",
                    "Exception:" + ex.getMessage() + "StackTrace::"
                            + ex.getStackTrace().toString());
            db.close();
            return 0;
        }
        return res;
    }

    public static void ResetLastAutoUpdateDay(Context cn) {
        try {
            Datastorage.SetLastAutoUpdateExamDay(cn, 0);
            Datastorage.SetLastAutoUpdateMessageDay(cn, 0);
            Datastorage.SetLastAutoUpdateAttendance(cn, 0);
            Datastorage.SetLastAutoUpdateNoticeDay(cn, 0);
            Datastorage.SetLastAutoUpdateExamDay(cn, 0);
        } catch (Exception ex) {
            Constants.writelog(
                    "CommonUtilites",
                    "Exception: " + ex.getMessage() + ":::"
                            + ex.getStackTrace());
        }
    }

    public static String getgmaildatestring(String datestring) {
        String date = "";
        try {
            if (datestring.equals("")) {
                return "";
            } else {
                date = datestring;
                // date="14/4/2015 4:48:30 PM";
                //Constants.Logwrite("ListView_Activity Date: ", date);
                // SimpleDateFormat dateFormat = new
                // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "dd/MM/yyyy hh:mm:ss aa");
                Time cur_time = new Time();
                cur_time.setToNow();
                Date date1 = dateFormat.parse(date);
                int msgday = date1.getDate();
                int currday = cur_time.monthDay;
                int msgyear = date1.getYear() + 1900;
                int curryear = cur_time.year;
                int msgmonth = date1.getMonth();
                int currmonth = cur_time.month;
                if (msgyear == curryear) {
                    if (currmonth == msgmonth) {
                        if (currday == msgday) {
                            SimpleDateFormat formatter = new SimpleDateFormat(
                                    "hh:mm aa");
                            date = formatter.format(date1);
                            //Constants.Logwrite("date", "date: " + date);
                        } else {
                            SimpleDateFormat formatter = new SimpleDateFormat(
                                    "dd MMM");
                            date = formatter.format(date1);
                            //Constants.Logwrite("date", "date: " + date);
                        }
                    } else {
                        SimpleDateFormat formatter = new SimpleDateFormat(
                                "dd MMM");
                        date = formatter.format(date1);
                        //Constants.Logwrite("date", "date: " + date);
                    }
                } else {
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "dd/MMM/yy");
                    date = formatter.format(date1);
                    //Constants.Logwrite("date", "date: " + date);
                }
            }
        } catch (Exception ex) {
            Constants.writelog("Commonutility",
                    "Exception getgmaildatestring 1245 " + ex.getMessage()
                            + ":::::" + ex.getStackTrace());
        }
        return date;
    }

    public static void writelog(String File, String log) {
        File myDir = CreatePhotoGalleryFolder();
        File file = new File(myDir, "log.txt");
        long FileSize = file.length();
        long filesizeInKB = FileSize / 1024;
        if (filesizeInKB > 10000) {
            file.delete();
        }
        PrintWriter printWriter = null;
        try {
            Time t = new Time();
            t.setToNow();
            if (!file.exists()) {
                file.createNewFile();
            }
            printWriter = new PrintWriter(new FileOutputStream(file, true));
            printWriter.write("File  : " + File + "    Date:" + t.monthDay
                    + ":" + ((t.month) + 1) + ":" + t.year + "    " + t.hour
                    + ":" + t.minute + ":" + t.second + "\nLog  : " + log
                    + "\n\n");
            //if (BuildConfig.DEBUG) {
            Log.e(File, " Date:" + t.monthDay
                    + ":" + ((t.month) + 1) + ":" + t.year + "    " + t.hour
                    + ":" + t.minute + ":" + t.second + "\nLog  : " + log);
            //}
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.flush();
                printWriter.close();
            }
        }
    }

    public static void googleAnalytic(Context cn, String ScreenName) {
        try {
            ConnectionDetector cd = new ConnectionDetector(cn);
            if (cd.isConnectingToInternet()) {
                Tracker tracker;
                GoogleAnalytics analytics = GoogleAnalytics.getInstance(cn);
                analytics.dispatchLocalHits();
                analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
                tracker = analytics.newTracker(R.xml.app_tracker);
                tracker.setScreenName(ScreenName);
                tracker.setTitle(ScreenName);
                // tracker.set(Fields.SCREEN_NAME, ScreenName);
                tracker.enableAdvertisingIdCollection(true);
                // tracker.send(new HitBuilders.AppViewBuilder().build());
                tracker.send(new HitBuilders.ScreenViewBuilder().build());
                // GAServiceManager.getInstance().setDispatchPeriod(30);
                analytics.dispatchLocalHits();
            }
        } catch (Exception ex) {
            Constants.writelog("Commonutility",
                    "Exception GoogleAnalytics 1226 " + ex.getMessage()
                            + ":::::" + ex.getStackTrace());
            Constants.Logwrite("Commonutility",
                    "Exception GoogleAnalytics 1226 " + ex.getMessage()
                            + ":::::" + ex.getStackTrace());
        }
    }

    public static String SendLogText(Context cn, String deviceDetails,
                                     String text, String deviceId) {
        String resp = "";
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_SEND_LOG);
            request.addProperty("studentid", Datastorage.GetStudentId(cn));
            request.addProperty("schoolid", Datastorage.GetSchoolId(cn));
            request.addProperty("DeviceId", deviceId);
            request.addProperty("DeviceDetails", deviceDetails);
            request.addProperty("appname", SchoolDetails.appname);
            request.addProperty("log", text);
            writelog("CommonUtility", "SendLogText()1547 Req:" + request);
            SoapObject result = CallWebMethod(cn, request, METHOD_SEND_LOG,
                    false);
            if (result != null && result.getPropertyCount() > 0) {
                resp = result.getPropertyAsString(0);
                writelog("CommonUtility", "SendLogText()1552 Res:" + resp);
            }
        } catch (Exception ex) {
            Constants.writelog("CommonUtility", "LogUserVisited()235 Ex:"
                    + ex.getMessage() + ":::::::" + ex.getStackTrace());
        }
        return resp;
    }

    public static void googleAnalyticEvent(Context cn, String EventCategory,
                                           String EventName) {
        try {
            Tracker tracker;
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(cn);
            tracker = analytics.newTracker(R.xml.app_tracker);
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(EventCategory).setAction(EventName).build());

        } catch (Exception ex) {
            Constants.writelog("CommonUtility",
                    "Exception googleAnalyticEvent() 1243" + ex.getMessage()
                            + "::::::" + ex.getStackTrace());
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public static boolean isGujSupported(Context context, String text) {
        boolean res = true;
        try {//યશ્વી, આજનું હોમવર્ક યાદ કરવું યશ્વી 222@#$%^&*<:>.,;'
            text = text.replace(".", "");
            text = text.replaceAll("\\d", "");
            text = text.replace(" ", "");
            text = text.replace("\"", "");
            text = text.replace(",", "");
            text = text.replace("#", "");
            text = text.replace("$", "");
            text = text.replace("%", "");
            text = text.replace("^", "");
            text = text.replace("&", "");
            text = text.replace("/", "");
            text = text.replace(";", "");
            text = text.replace("<", "");
            text = text.replace(">", "");
            text = text.replace(":", "");
            text = text.replace("@", "");
            text = text.replace("!", "");
            text = text.replace("(", "");
            text = text.replace(")", "");
            text = text.replace("'", "");
            text = text.replace("{", "");
            text = text.replace("}", "");
            text = text.replace("*", "");
            text = text.replace("+", "");
            text = text.replace("-", "");
            text = text.replace("=", "");
            text = text.replace("[", "");
            text = text.replace("]", "");
            text = text.replace("_", "");
            text = text.replace("|", "");
            text = text.replace("?", "");
            int WIDTH_PX = 200;
            int HEIGHT_PX = 80;
            int w = WIDTH_PX, h = HEIGHT_PX;
            Resources resources = context.getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = Bitmap.createBitmap(w, h, conf); //this creates a MUTABLE bitmap
            Bitmap orig = bitmap.copy(conf, false);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.rgb(0, 0, 0));
            paint.setTextSize((int) (14 * scale));
            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int x = (bitmap.getWidth() - bounds.width()) / 2;
            int y = (bitmap.getHeight() + bounds.height()) / 2;
            canvas.drawText(text, x, y, paint);
            res = !orig.sameAs(bitmap);
            orig.recycle();
            bitmap.recycle();
        } catch (Exception ex) {
            writelog(TAG, "isGujSupported()1778 Ex:" + ex.getMessage());
        }
        return res;
    }

    /*
     * public static int SetCurrentYearAsDefYearFromLocal(Context cntxt, String
     * SchoolId, String StudId, DatabaseHandler db) { int res = 0;
     * SQLiteDatabase myWritableDb = null; String KEY_ACADEMIC_IsDef = "IsDef";
     * String TABLE_ACADEMIC_YEAR = "Academic_Year"; String
     * KEY_ACADEMIC_IS_CURRENT = "IsCurrent"; String KEY_ACADEMIC_SCHOOLID =
     * "SchoolId"; String KEY_ACADEMIC_STUDID = "StudId";
     *
     * try { ContentValues values = new ContentValues();
     * values.put(KEY_ACADEMIC_IsDef, 0); String table = TABLE_ACADEMIC_YEAR;
     * myWritableDb = db.getWritableDatabase(); String whereClause = "" +
     * KEY_ACADEMIC_SCHOOLID + " =? AND " + KEY_ACADEMIC_STUDID + " =?"; String
     * whereArgs[] = new String[] { SchoolId, StudId };
     * myWritableDb.update(table, values, whereClause, whereArgs); ContentValues
     * values1 = new ContentValues(); values1.put(KEY_ACADEMIC_IsDef, 1); String
     * whereClause1 = "" + KEY_ACADEMIC_IS_CURRENT + " =? AND " +
     * KEY_ACADEMIC_SCHOOLID + " =? AND " + KEY_ACADEMIC_STUDID + " =?"; String
     * whereArgs1[] = new String[] { "1", SchoolId, StudId };
     * myWritableDb.update(table, values1, whereClause1, whereArgs1);
     *
     * res = 1; db.close();
     *
     * } catch (Exception ex) { // e.printStackTrace();
     * Constants.writelog("commonutilities:1192", "Exception:" +
     * ex.getMessage() + "StackTrace::" + ex.getStackTrace().toString());
     * db.close(); return 0; } return res; }
     */

    public static void setText(Context context, String text, TextView tv) {
        try {
            if (!Constants.isGujSupported(context, text)) {
                Typeface font = Typeface.createFromAsset(context.getAssets(), "LOHIT_GU.TTF");
                tv.setTypeface(font);
                tv.setText(text);
            } else {
                tv.setText(text);
            }
        } catch (Exception ex) {
            Constants.writelog("CommonUtility",
                    "Exception setText() 1787" + ex.getMessage());
        }
    }

    public static void requestFocus(Activity mContext, View view) {
        if (view.requestFocus()) {
            mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void setSession(String key, String value) {
        settings.edit().putString(key, value).commit();
    }

    public void setSessionBool(String key, boolean value) {
        settings.edit().putBoolean(key, value).commit();
    }

    public String getSession(String key) {
        return settings.getString(key, "");
    }

    public boolean getSessionBool(String key) {
        return settings.getBoolean(key, false);
    }

    String decodeNumericEntities(String s) {
        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile("\\&#(\\d+);").matcher(s);
        while (m.find()) {
            int uc = Integer.parseInt(m.group(1));
            m.appendReplacement(sb, "");
            sb.append(uc);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public void SetSharedPreference() {
        try {

        } catch (Exception err) {

        }
    }

    public static class MyTaskLogVisit extends
            AsyncTask<Object, Integer, Integer> {

        @Override
        protected Integer doInBackground(Object... arg0) {
            // TODO Auto-generated method stub
            try {
                Context cn = (Context) arg0[0];
                LogUserVisited(cn, (Integer) arg0[1], (Integer) arg0[2]);
            } catch (Exception ex) {
                Constants.writelog("CommonUtility",
                        "MSG:" + ex.getMessage());
            }
            return 1;
        }
    }

    public static class MyTaskSendLog extends AsyncTask<Object, String, String> {

        @Override
        protected String doInBackground(Object... arg0) {
            // TODO Auto-generated method stub
            try {
                writelog("CommonUtility", "MyTaskSendLog()1566 Start.");
                Boolean isprocess = false;
                Context cn = (Context) arg0[0];
                int linecount = Integer.parseInt(arg0[3].toString());
                int limit = Datastorage.GetlineForlog(cn);
                writelog("CommonUtility", "MyTaskSendLog()1571 Line:" + linecount + " Limit:" + limit);
                String resp = "";
                File sdcard = CreatePhotoGalleryFolder();
                // Get the text file
                File file = new File(sdcard, "log.txt");
                // Read text from file
                StringBuilder text = new StringBuilder();
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                int count = 0;
                int total = 0;
                if (linecount == 0) {
                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                        count++;
                        total++;
                        if (count > limit) {
                            isprocess = true;
                            resp = SendLogText(cn, arg0[1].toString(),
                                    text.toString(), arg0[2].toString());
                            if (resp.equalsIgnoreCase("true")) {
                                count = 0;
                                text = new StringBuilder();
                                // line.remove(1);
                            } else {
                                break;
                            }
                        }
                    }
                } else {
                    while ((line = br.readLine()) != null) {
                        total++;
                    }
                    int startLine = total - linecount;
                    br = new BufferedReader(new FileReader(file));
                    int i = 0;
                    while ((line = br.readLine()) != null) {
                        if (i >= startLine) {
                            text.append(line);
                            text.append('\n');
                            count++;
                            if (count > limit) {
                                isprocess = true;
                                resp = SendLogText(cn, arg0[1].toString(),
                                        text.toString(), arg0[2].toString());
                                if (resp.equalsIgnoreCase("true")) {
                                    count = 0;
                                    text = new StringBuilder();
                                    // line.remove(1);
                                } else {
                                    break;
                                }
                            }
                        } else {
                            i++;
                        }
                    }
                }
                if (count != 0) {
                    if (isprocess == true) {
                        if (resp.equalsIgnoreCase("true")) {
                            resp = SendLogText(cn, arg0[1].toString(),
                                    text.toString(), arg0[2].toString());
                        }
                    } else {
                        resp = SendLogText(cn, arg0[1].toString(),
                                text.toString(), arg0[2].toString());
                    }
                }
                br.close();
                if (resp.equals("true")) {
                    Datastorage.SetLogSendComplete(cn, 1);
                }
            } catch (Exception ex) {
                Constants.writelog("CommonUtility",
                        "MyTaskSendLog() 1579 MSG:" + ex.getMessage());
            }
            return "true";
        }
    }
}
