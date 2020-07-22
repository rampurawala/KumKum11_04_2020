package com.expedite.apps.kumkum.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.ActivityVisitedModel;
import com.expedite.apps.kumkum.model.Contact;
import com.paynimo.android.payment.util.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String TABLE_SMS1 = "smstable";
    public static final String TABLE_SMS2 = "smstable1";
    public static final String TABLE_ACADEMIC_YEAR = "Academic_Year";
    public static final String TABLE_EXAM_DETAILS = "Exams";
    public static final String TABLE_STUDENT_ALBUM_DETAILS = "StudentAlbumDetails";
    public static final String TABLE_CIRCULAR_DETAILS = "CircularDetails";
    public static final String TABLE_STUD_PROFILE = "StudentProfile";
    public static final String TABLE_PROFILE = "Profile";
    public static final String TABLE_STUD_ATTENDANCE = "StudentAttendance";
    public static final String TABLE_ACTIVITY_VISITED = "activityVisited";
    public static final String TABLE_PRACTICE_TEST_DETAILS = "practicetestDetils";
    public static final String TABLE_PRACTICE_TEST_ElapsedTimeDETAILS = "practicetestElapsedTimeDetils";
    public static final String KEY_NAME = "name";
    public static final String KEY_PH_NO = "phone_number";
    public static final String KEY_LOG_PIN = "logpin";
    public static final String KEY_IS_DEF = "isdef";
    public static final String KEY_STUD_ID = "Stud_Id";
    public static final String KEY_SCHOOLID = "School_Id";
    public static final String KEY_YEAR_ID = "yearid";
    public static final String KEY_CLASS_ID = "classid";
    public static final String KEY_CLASS_SEC_ID = "classsecid";
    public static final String KEY_USER_ID = "userid";
    public static final String KEY_CLASS_NAME = "classname";
    public static final String KEY_STUD_ENROLL_DATE = "studentenrolldate";
    public static final String KEY_LAST_UPDATED_TIME = "lastupdatedtime";
    public static final String KEY_ACADEMIC_YEAR = "acayear";
    public static final String KEY_UPDATED_TIME = "updatedtime";
    public static final String KEY_ROUTE_ID = "routeid";
    public static final String KEY_CLASS_SECTION_NAME = "classsecname";
    public static final String KEY_ISFEE_SHOW = "isfeeshow";
    public static final String KEY_ISPAY_SHOW = "ispayshow";
    public static final String KEY_MENU = "menu";
    private static final int DATABASE_VERSION = 24;
    // Database Name
    private static final String DATABASE_NAME = "contactsManager";
    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_CONTACTS1 = "contacts1";
    // private static final String TABLE_STUD_DETAILS = "Student_Details";
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    /*
     * private static final String KEY_ACADAMIC_START_DATE =
     * "acayear_start_date"; private static final String KEY_ACADAMIC_END_DATE =
     * "acayear_end_date"; private static final String KEY_Hol_Pro_Start_Date =
     * "holi_prof_start_date"; private static final String KEY_Hol_Pro_End_Date
     * = "holi_prof_end_date"; private static final String KEY_STUDENT_NAME =
     * "studname";
     */
    // Column Containing SMS Table
    private static final String KEY_SMSID = "SmsId";
    private static final String KEY_MSGID = "MsgId";
    private static final String KEY_MSGTEXT = "Message";
    private static final String KEY_SMS_STUDID = "StudId";
    private static final String KEY_SMS_SCHOOLID = "SchoolId";
    private static final String KEY_SMS_DAY = "Sms_Day";
    private static final String KEY_SMS_MONTH = "Sms_Month";
    private static final String KEY_SMS_YEAR = "Sms_Year";
    private static final String KEY_SMS_Moduleid = "Sms_ModuleId";
    private static final String KEY_SMS_YEAR_ID = "Sms_YEARID";
    private static final String KEY_ACADEMIC_ROW_ID = "RowId";

    // Column Containing Academic Year Table
    private static final String KEY_ACADEMIC_YEAR_ID = "YearId";
    private static final String KEY_ACADEMIC_YEAR_TEXT = "Year_Text";
    private static final String KEY_ACADEMIC_IS_CURRENT = "IsCurrent";
    private static final String KEY_ACADEMIC_SCHOOLID = "SchoolId";
    private static final String KEY_ACADEMIC_STUDID = "StudId";
    private static final String KEY_ACADEMIC_IsDef = "IsDef";
    private static final String KEY_EXAM_ROW_ID = "RowId";

    // Column Containing ExamDetails Table
    private static final String KEY_EXAM_SR_NO = "SrNo";
    private static final String KEY_EXAM_YEAR_ID = "YearId";
    private static final String KEY_EXAM_STUDID = "StudId";
    private static final String KEY_EXAM_SCHOOLID = "SchoolId";
    private static final String KEY_EXAM_EXAMID = "ExamId";
    private static final String KEY_EXAM_EXAMNAME = "ExamName";
    private static final String KEY_EXAM_EXAMMARKS = "ExamMarks";
    private static final String KEY_EXAM_ISMARKSHEET = "IsMarksheet";
    private static final String KEY_EXAM_MARKSHEET_PATH = "MarksheetPath";
    private static final String KEY_EXAM_CATEGORY_ID = "CategoryId";
    private static final String KEY_EXAM_CATEGORY_NAME = "CategoryName";
    // Column Containing StudentAlbumDetails Table
    private static final String KEY_ALBUM_MASTER_ID_ = "Album_Master_Id";

    // Column Containing StudentDetails Table

    /*
     * private static final String KEY_STUD_DET_ROWID = "RowId"; private static
     * final String KEY_STUD_DET_STUDID = "StudId"; private static final String
     * KEY_STUD_DET_SCHOOLID = "SchoolId"; private static final String
     * KEY_STUD_DET_YEARID = "YearId"; private static final String
     * KEY_STUD_DET_DETAILS = "Stud_Details";
     */
    /*
     * public static DatabaseHandler getInstance(Context context) {
     *
     * // Use the application context, which will ensure that you // don't
     * accidentally leak an Activity's context. // See this article for more
     * information: http://bit.ly/6LRzfx if (sInstance == null) { sInstance =
     * new DatabaseHandler(context.getApplicationContext()); } return sInstance;
     * }
     */
    private static final String KEY_ALBUM_STUDID = "StudId";
    private static final String KEY_ALBUM_ALBUMID = "AlbumId";
    private static final String KEY_ALBUM_ALBUMURL = "Albumurl";
    private static final String KEY_ALBUM_ALBUMNAME = "AlbumName";
    private static final String KEY_ALBUM_PHOTOFILENAME = "PhotoFileName";
    private static final String KEY_ALBUM_SCHOOLID = "SchoolId";
    private static final String KEY_ALBUM_CLASSSECID = "ClassSecId";
    private static final String KEY_ALBUM_DATETICKS = "DateTicks";
    private static final String KEY_ALBUM_DATETIME = "DateTime";
    // Column Containing CircularDetailsTalbe Table
    private static final String KEY_CIRCULAR_MASTER_ID = "Circular_Master_Id";
    private static final String KEY_CIRCULAR_STUDID = "StudId";
    private static final String KEY_CIRCULAR_SCHOOLID = "SchoolId";
    private static final String KEY_CIRCULAR_YEARID = "YearId";
    private static final String KEY_CIRCULAR_GROUPID = "CirGroupId";
    private static final String KEY_CIRCULAR_GROUPNAME = "CirGroupName";
    private static final String KEY_CIRCULARID = "CirId";
    private static final String KEY_CIRCULAR_NAME = "CirName";
    private static final String KEY_CIRCULAR_DATETEXT = "DateText";
    private static final String KEY_CIRCULAR_PATH = "Path";
    private static final String KEY_CIRCULAR_TICKS = "Ticks";
    private static final String KEY_CIRCULAR_ISDELETED = "IsDelete";
    // Column Containing StudentProfile Table
    private static final String KEY_PROFILE_ID = "StudProfileID";
    private static final String KEY_PROFILE_STUD_ID = "StudId";
    private static final String KEY_PROFILE_SCHOOL_ID = "SchoolId";
    private static final String KEY_PROFILE_YEAR_ID = "YearId";
    private static final String KEY_PROFILE_GRNO = "GrNo";
    private static final String KEY_PROFILE_NAME = "Name";
    private static final String KEY_PROFILE_CLASS = "Class";
    private static final String KEY_PROFILE_CASTE = "Caste";
    private static final String KEY_PROFILE_CATEGORY = "Category";
    private static final String KEY_PROFILE_DOB = "Dob";
    private static final String KEY_PROFILE_CONTACT_NUMBER = "ContactNumber";
    private static final String KEY_PROFILE_IMAGE = "ImageName";
    //Student & parent Profiel 10-9-18
    private static final String KEY_PROFILE_IDS = "Id";
    private static final String KEY_PROFILE_STUD_IDS = "StudId";
    private static final String KEY_PROFILE_SCHOOL_IDS = "SchoolId";
    private static final String KEY_PROFILE_YEAR_IDS = "YearId";
    private static final String KEY_PROFILE_TITLE = "Name";
    private static final String KEY_PROFILE_DESC = "Class";
    private static final String KEY_IS_STUD = "IsStud";
    // Student Attendance details table
    private static final String KEY_ATT_ID = "StudentAttId";
    private static final String KEY_ATT_STUD_ID = "StudId";
    private static final String KEY_ATT_SCHOOL_ID = "SchoolId";
    private static final String KEY_ATT_YEAR_ID = "YearId";
    private static final String KEY_ATT_DETAILS = "AttDetails";
    //Activity Visited Table Column
    private static final String KEY_ACTIVITY_VISITED_ROW_ID = "RowID";
    private static final String KEY_ACTIVITY_VISITED_STUD_ID = "StudId";
    private static final String KEY_ACTIVITY_VISITED_SCHOOL_ID = "SchoolId";
    private static final String KEY_ACTIVITY_VISITED_NAME = "activityVisitedName";
    private static final String KEY_ACTIVITY_VISITED_COUNT = "activityVisitedCount";
    private static final String KEY_ACTIVITY_VISITED_DATE = "activityVisitedDate";
    //Practice Test
    private static final String KEY_PRACTICE_TEST_ROW_ID = "RowID";
    private static final String KEY_PRACTICE_TEST_STUD_ID = "StudId";
    private static final String KEY_PRACTICE_TEST_SCHOOL_ID = "SchoolId";
    private static final String KEY_PRACTICE_TEST_TEST_ID = "TestId";
    private static final String KEY_PRACTICE_TEST_ANS_STRING = "TestAnsString";
    private static final String TEST_TIMESTAMP = "TestTimeStamp";
    private static final String TEST_CURRENTSTART_DATE = "TestCurrentStartDate";


    private static final String KEY_PRACTICE_TEST_ELAPSED_TIME = "TestElapsedTime";
    private static final String KEY_PRACTICE_TEST_QuestionELAPSED_TIME = "TestQuestionElapsedTime";
    private static final String KEY_PRACTICE_TEST_Wise_RowID = "TestrowId";
    private static final String KEY_PRACTICE_TEST_TABLE_RowID = "RowID";


    //Context cntxt = null;
    Context cntxt = null;
    private SQLiteDatabase mSQLiteDatabase;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        cntxt = context;
    }


    public void close(SQLiteDatabase db) {
        try {
            if (db != null) {
                db.close();
                db = null;
            }
        } catch (Exception err) {
        }
    }

    /**
     * Opens the helper object
     *
     * @return Helper object
     * @throws SQLException
     */
    public DatabaseHandler open() throws SQLException {
        mSQLiteDatabase = this.getWritableDatabase();
        return this;
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            try {
                boolean IsTExistContactTable = IsTabExist(db, TABLE_CONTACTS1);
                if (!IsTExistContactTable) {
                    /*String CREATE_CONTACTS_TABLE = "CREATE TABLE "
                            + TABLE_CONTACTS1 + "(" + KEY_ID
                            + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                            + KEY_PH_NO + " TEXT," + KEY_LOG_PIN + " INTEGER,"
                            + KEY_IS_DEF + " INTEGER" + "," + KEY_STUD_ID
                            + " INTEGER," + KEY_SCHOOLID + " INTEGER,"
                            + KEY_YEAR_ID + " INTEGER," + KEY_CLASS_ID
                            + " INTEGER," + KEY_CLASS_SEC_ID + " INTEGER,"
                            + KEY_USER_ID + " INTEGER," + KEY_CLASS_NAME
                            + " TEXT," + KEY_STUD_ENROLL_DATE + " TEXT,"
                            + KEY_LAST_UPDATED_TIME + " TEXT,"
                            + KEY_ACADEMIC_YEAR + " TEXT," + KEY_UPDATED_TIME
                            + " TEXT," + KEY_ROUTE_ID + " INTEGER,"
                            + KEY_CLASS_SECTION_NAME + " TEXT,"
                            + KEY_ISFEE_SHOW + " INTEGER," + KEY_ISPAY_SHOW + " INTEGER)";*/
                    String CREATE_CONTACTS_TABLE = "CREATE TABLE "
                            + TABLE_CONTACTS1 + "(" + KEY_ID
                            + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                            + KEY_PH_NO + " TEXT," + KEY_LOG_PIN + " INTEGER,"
                            + KEY_IS_DEF + " INTEGER" + "," + KEY_STUD_ID
                            + " INTEGER," + KEY_SCHOOLID + " INTEGER,"
                            + KEY_YEAR_ID + " INTEGER," + KEY_CLASS_ID
                            + " INTEGER," + KEY_CLASS_SEC_ID + " INTEGER,"
                            + KEY_USER_ID + " INTEGER," + KEY_CLASS_NAME
                            + " TEXT," + KEY_STUD_ENROLL_DATE + " TEXT,"
                            + KEY_LAST_UPDATED_TIME + " TEXT,"
                            + KEY_ACADEMIC_YEAR + " TEXT," + KEY_UPDATED_TIME
                            + " TEXT," + KEY_ROUTE_ID + " INTEGER,"
                            + KEY_CLASS_SECTION_NAME + " TEXT,"
                            + KEY_MENU + " TEXT)";
                    db.execSQL(CREATE_CONTACTS_TABLE);
                }
            } catch (Exception ex) {
                Constants.writelog(
                        "DBHandler",
                        "OnCreate() Ex:232 " + ex.getMessage() + ":::::::"
                                + ex.getStackTrace());
                Constants.Logwrite("DatabaseHandler 201",
                        "Create contacttable1" + ex.getMessage());
            }
            try {
                boolean IsTExistSMSTable = IsTabExist(db, TABLE_SMS2);
                if (!IsTExistSMSTable) {
                    String CREATE_SMS_TABLE = "CREATE TABLE " + TABLE_SMS2
                            + "(" + KEY_SMSID + " INTEGER PRIMARY KEY,"
                            + KEY_MSGID + " INTEGER," + KEY_MSGTEXT + " TEXT,"
                            + KEY_SMS_STUDID + " INTEGER," + KEY_SMS_SCHOOLID
                            + " INTEGER," + KEY_SMS_DAY + " INTEGER,"
                            + KEY_SMS_MONTH + " INTEGER," + KEY_SMS_YEAR
                            + " INTEGER," + KEY_SMS_Moduleid + " INTEGER,"
                            + KEY_SMS_YEAR_ID + " INTEGER)";
                    db.execSQL(CREATE_SMS_TABLE);
                }
            } catch (Exception err) {
                Constants.Logwrite("DatabaseHandler 217", err.getMessage());
            }
            // Create Year Table Table
            try {
                boolean IsTExistAcademicYear = IsTabExist(db,
                        TABLE_ACADEMIC_YEAR);
                if (!IsTExistAcademicYear) {
                    String CREATE_YEAR_TABLE = "CREATE TABLE "
                            + TABLE_ACADEMIC_YEAR + "(" + KEY_ACADEMIC_ROW_ID
                            + " INTEGER PRIMARY KEY," + KEY_ACADEMIC_YEAR_ID
                            + " INTEGER," + KEY_ACADEMIC_YEAR_TEXT + " TEXT,"
                            + KEY_ACADEMIC_IS_CURRENT + " INTEGER,"
                            + KEY_ACADEMIC_SCHOOLID + " INTEGER,"
                            + KEY_ACADEMIC_STUDID + " INTEGER,"
                            + KEY_ACADEMIC_IsDef + " INTEGER)";
                    db.execSQL(CREATE_YEAR_TABLE);
                }
            } catch (Exception err) {

            }

            // Create Practice test Table
            try {
                boolean IsTExistPracticeTest = IsTabExist(db,
                        TABLE_PRACTICE_TEST_DETAILS);
                if (!IsTExistPracticeTest) {
                   /* String CREATE_PRACTICETEST_TABLE = "CREATE TABLE "
                            + TABLE_PRACTICE_TEST_DETAILS + "(" + KEY_PRACTICE_TEST_ROW_ID
                            + " INTEGER PRIMARY KEY," + KEY_PRACTICE_TEST_SCHOOL_ID
                            + " INTEGER," + KEY_PRACTICE_TEST_STUD_ID + " INTEGER,"
                            + KEY_PRACTICE_TEST_TEST_ID + " INTEGER,"
                            + KEY_PRACTICE_TEST_ANS_STRING + " TEXT," + TEST_TIMESTAMP+ " TEXT," + TEST_CURRENTSTART_DATE + " TEXT)";*/
                   String CREATE_PRACTICETEST_TABLE = "CREATE TABLE "
                            + TABLE_PRACTICE_TEST_DETAILS + "(" + KEY_PRACTICE_TEST_ROW_ID
                            + " INTEGER PRIMARY KEY," + KEY_PRACTICE_TEST_SCHOOL_ID
                            + " INTEGER," + KEY_PRACTICE_TEST_STUD_ID + " INTEGER,"
                            + KEY_PRACTICE_TEST_TEST_ID + " INTEGER,"
                            + KEY_PRACTICE_TEST_ANS_STRING + " TEXT," + TEST_TIMESTAMP + " TEXT)";
                    db.execSQL(CREATE_PRACTICETEST_TABLE);
                }
            } catch (Exception err) {

            }
            try {
                boolean IsTExistPracticeElpaseTest = IsTabExist(db,
                        TABLE_PRACTICE_TEST_ElapsedTimeDETAILS);
                if (!IsTExistPracticeElpaseTest) {
                    String CREATE_PRACTICETEST_ELPASED_TABLE = "CREATE TABLE "
                            + TABLE_PRACTICE_TEST_ElapsedTimeDETAILS + "(" + KEY_PRACTICE_TEST_TABLE_RowID
                            + " INTEGER PRIMARY KEY," + KEY_PRACTICE_TEST_Wise_RowID
                            + " INTEGER," + KEY_PRACTICE_TEST_QuestionELAPSED_TIME + " TEXT,"
                            + KEY_PRACTICE_TEST_ELAPSED_TIME + " TEXT)";
                    db.execSQL(CREATE_PRACTICETEST_ELPASED_TABLE);
                }
            } catch (Exception err) {

            }

            try {
                boolean IsTExistExam = IsTabExist(db, TABLE_EXAM_DETAILS);
                if (!IsTExistExam) {
                    String CREATE_EXAM_TABLE = "CREATE TABLE "
                            + TABLE_EXAM_DETAILS + "(" + KEY_EXAM_ROW_ID
                            + " INTEGER PRIMARY KEY," + KEY_EXAM_YEAR_ID
                            + " INTEGER," + KEY_EXAM_SR_NO + " INTEGER," + KEY_EXAM_STUDID + " INTEGER,"
                            + KEY_EXAM_SCHOOLID + " INTEGER," + KEY_EXAM_EXAMID
                            + " INTEGER," + KEY_EXAM_EXAMNAME + " TEXT,"
                            + KEY_EXAM_EXAMMARKS + " TEXT,"
                            + KEY_EXAM_ISMARKSHEET + " INTEGER,"
                            + KEY_EXAM_MARKSHEET_PATH + " TEXT,"
                            + KEY_EXAM_CATEGORY_ID + " TEXT,"
                            + KEY_EXAM_CATEGORY_NAME + " TEXT)";
                    db.execSQL(CREATE_EXAM_TABLE);
                }
            } catch (Exception err) {

            }
            try {
                boolean IsTExistStudentAlbumDetails = IsTabExist(db,
                        TABLE_STUDENT_ALBUM_DETAILS);
                if (!IsTExistStudentAlbumDetails) {
                    String CREATE_EXAM_TABLE = "CREATE TABLE "
                            + TABLE_STUDENT_ALBUM_DETAILS + "("
                            + KEY_ALBUM_MASTER_ID_ + " INTEGER PRIMARY KEY,"
                            + KEY_ALBUM_STUDID + " INTEGER,"
                            + KEY_ALBUM_ALBUMID + " INTEGER,"
                            + KEY_ALBUM_ALBUMNAME + " TEXT,"
                            + KEY_ALBUM_ALBUMURL + " TEXT,"
                            + KEY_ALBUM_PHOTOFILENAME + " TEXT,"
                            + KEY_ALBUM_SCHOOLID + " INTEGER,"
                            + KEY_ALBUM_CLASSSECID + " INTEGER,"
                            + KEY_ALBUM_DATETICKS + " INTEGER,"
                            + KEY_ALBUM_DATETIME + " TEXT)";
                    db.execSQL(CREATE_EXAM_TABLE);
                }
            } catch (Exception err) {

            }

            try {
                boolean IsTExistCircular = IsTabExist(db,
                        TABLE_CIRCULAR_DETAILS);
                if (!IsTExistCircular) {
                    String CREATE_CIRCULAR_TABLE = "CREATE TABLE "
                            + TABLE_CIRCULAR_DETAILS + "("
                            + KEY_CIRCULAR_MASTER_ID + " INTEGER PRIMARY KEY,"
                            + KEY_CIRCULAR_STUDID + " INTEGER,"
                            + KEY_CIRCULAR_SCHOOLID + " INTEGER,"
                            + KEY_CIRCULAR_YEARID + " INTEGER,"
                            + KEY_CIRCULAR_GROUPID + " INTEGER,"
                            + KEY_CIRCULAR_GROUPNAME + " TEXT,"
                            + KEY_CIRCULARID + " INTEGER," + KEY_CIRCULAR_NAME
                            + " TEXT," + KEY_CIRCULAR_DATETEXT + " TEXT,"
                            + KEY_CIRCULAR_PATH + " TEXT," + KEY_CIRCULAR_TICKS
                            + " TEXT," + KEY_CIRCULAR_ISDELETED + " INTEGER)";
                    db.execSQL(CREATE_CIRCULAR_TABLE);
                }
            } catch (Exception err) {
                Constants.Logwrite("DatabaseHandler", "Exception 302:" + err.getMessage()
                        + ":::::::" + err.getStackTrace());
                Constants.writelog("DatabaseHandler", "Exception 269:"
                        + err.getMessage() + ":::::::" + err.getStackTrace());
            }

            try {
                boolean IsTExistStudProfile = IsTabExist(db, TABLE_STUD_PROFILE);
                if (!IsTExistStudProfile) {
                    String CREATE_STUDPROFILE_TABLE = "CREATE TABLE "
                            + TABLE_STUD_PROFILE + "(" + KEY_PROFILE_ID
                            + " INTEGER PRIMARY KEY," + KEY_PROFILE_STUD_ID
                            + " INTEGER," + KEY_PROFILE_SCHOOL_ID + " INTEGER,"
                            + KEY_PROFILE_YEAR_ID + " INTEGER,"
                            + KEY_PROFILE_GRNO + " TEXT," + KEY_PROFILE_NAME
                            + " TEXT," + KEY_PROFILE_CLASS + " TEXT,"
                            + KEY_PROFILE_CASTE + " TEXT,"
                            + KEY_PROFILE_CATEGORY + " TEXT," + KEY_PROFILE_DOB
                            + " TEXT," + KEY_PROFILE_CONTACT_NUMBER + " TEXT,"
                            + KEY_PROFILE_IMAGE + " TEXT)";

                    try {
                        db.execSQL(CREATE_STUDPROFILE_TABLE);
                        Constants.writelog("DatabaseHandler",
                                "OnCreate:348 studProfile table created");
                    } catch (Exception ex) {
                        Constants.writelog("DatabaseHandler",
                                "OnCreate:352 Exception:" + ex.getMessage()
                                        + ":::::" + ex.getStackTrace());
                    }
                }
            } catch (Exception ex) {
                Constants.writelog("DatabaseHandler",
                        "OnCreate:360 Exception:" + ex.getMessage() + ":::::"
                                + ex.getStackTrace());
            }

            try {
                boolean IsTExistProfile = IsTabExist(db, TABLE_PROFILE);
                if (!IsTExistProfile) {
                    String CREATE_PROFILE_TABLE = "CREATE TABLE "
                            + TABLE_PROFILE + "(" + KEY_PROFILE_IDS
                            + " INTEGER PRIMARY KEY," + KEY_PROFILE_STUD_IDS
                            + " INTEGER," + KEY_PROFILE_SCHOOL_IDS + " INTEGER,"
                            + KEY_PROFILE_YEAR_IDS + " INTEGER,"
                            + KEY_PROFILE_TITLE + " TEXT," + KEY_PROFILE_DESC
                            + " TEXT," + KEY_IS_STUD + " TEXT)";

                    try {
                        db.execSQL(CREATE_PROFILE_TABLE);
                        Constants.writelog("DatabaseHandler",
                                "OnCreate:407 Profile table created");
                    } catch (Exception ex) {
                        Constants.writelog("DatabaseHandler",
                                "OnCreate:410 Exception:" + ex.getMessage()
                                        + ":::::" + ex.getStackTrace());
                    }
                }
            } catch (Exception ex) {
                Constants.writelog("DatabaseHandler",
                        "OnCreate:416 Exception:" + ex.getMessage() + ":::::"
                                + ex.getStackTrace());
            }
            // For Activity Visited Table created By Vijay Solanki 27-02-2020
            try {
                boolean IsTActivityVisited = IsTabExist(db, TABLE_ACTIVITY_VISITED);
                if (!IsTActivityVisited) {
                    String CREATE_ACTIVITY_VISITED_TABLE = "CREATE TABLE " + TABLE_ACTIVITY_VISITED
                            + "(" + KEY_ACTIVITY_VISITED_ROW_ID + " INTEGER PRIMARY KEY,"
                            + KEY_ACTIVITY_VISITED_STUD_ID + " TEXT,"
                            + KEY_ACTIVITY_VISITED_SCHOOL_ID + " TEXT,"
                            + KEY_ACTIVITY_VISITED_NAME + " TEXT,"
                            + KEY_ACTIVITY_VISITED_COUNT + " INTEGER,"
                            + KEY_ACTIVITY_VISITED_DATE + " TEXT)";
                    try {
                        db.execSQL(CREATE_ACTIVITY_VISITED_TABLE);
                        Log.e("Query Crated 433", CREATE_ACTIVITY_VISITED_TABLE);
                        Constants.writelog("DatabaseHandler",
                                "OnCreate:434 ActivityVisited table created" + CREATE_ACTIVITY_VISITED_TABLE);
                    } catch (Exception ex) {
                        Constants.writelog("DatabaseHandler",
                                "OnCreate:433 Exception:" + ex.getMessage()
                                        + ":::::" + ex.getStackTrace());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                boolean IsExistStudAttTable = IsTabExist(db,
                        TABLE_STUD_ATTENDANCE);
                if (!IsExistStudAttTable) {
                    String CREATE_STUDATT_TABLE = "CREATE TABLE "
                            + TABLE_STUD_ATTENDANCE + "(" + KEY_ATT_ID
                            + " INTEGER PRIMARY KEY," + KEY_ATT_STUD_ID
                            + " INTEGER," + KEY_ATT_SCHOOL_ID + " INTEGER,"
                            + KEY_ATT_YEAR_ID + " INTEGER," + KEY_ATT_DETAILS
                            + " TEXT)";

                    try {
                        db.execSQL(CREATE_STUDATT_TABLE);
                        Constants.writelog("DatabaseHandler",
                                "onCreate:378 studattandance table created");
                    } catch (Exception ex) {
                        Constants.writelog(
                                "DatabaseHandler",
                                "onCreate:382 studattandance Exception:"
                                        + ex.getMessage() + ":::::"
                                        + ex.getStackTrace());
                    }
                }
            } catch (Exception ex) {
                Constants.writelog(
                        "DatabaseHandler",
                        "OnCreate: attandance table 390 Exception:"
                                + ex.getMessage() + ":::::"
                                + ex.getStackTrace());
            }
        } catch (Exception err) {
            Constants.Logwrite("DatabaseHandler:", "Exception:" + err.getMessage()
                    + "StackTrace::" + err.getStackTrace().toString());
        }
    }

    public int isAcademicYearExist(String yearid, String schoolid, String studid) {
        int res = 0;
        try {
            Cursor cursor = null;
            String countQuery = "SELECT * FROM " + TABLE_ACADEMIC_YEAR
                    + " where " + KEY_ACADEMIC_YEAR_ID + "=" + yearid + " and "
                    + KEY_ACADEMIC_SCHOOLID + "=" + schoolid + " and "
                    + KEY_ACADEMIC_STUDID + "=" + studid;
            SQLiteDatabase myWritableDb = null;
            myWritableDb = this.getWritableDatabase();
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor.getCount() > 0) {
                res = 1;
            }
        } catch (Exception ex) {
            Constants.writelog("DB", "isAcademicYearExist()425 MSG:" + ex.getMessage());
        }
        return res;
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        try {
            Constants.Logwrite("OnUpgrade", "Start...");
            Constants.Logwrite("newVersion", newVersion + "   :::oldVersion:" + oldVersion);
            // db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS1);
            // Create tables again
            onCreate(db);
            if (oldVersion <= 7) {
                try {
                    // Get All Data And Get Data From Server and Update To
                    // Database.
                    List<Contact> contacts = getAllContacts_for_Database_ver_5(db);
                    Constants.Logwrite("TotalAccount", "" + contacts.size());
                    if (contacts.size() > 1) {
                        for (Contact cn : contacts) {
                            String Phno = cn.getPhoneNumber();
                            Constants.Logwrite("Phone", "" + Phno);
                            int LoginPin = cn.getLogPin();
                            Constants.Logwrite("LogPin", "" + LoginPin);
                            int schoolid = cn.getSchoolId();
                            Constants.Logwrite("SchoolId", "" + schoolid);
                            String GetData = Constants
                                    .GetUserDetailsKumKum(cntxt, Phno,
                                            LoginPin, cn.getStudentId());
                            Constants.Logwrite("Data", "" + GetData.length());
                            if (GetData != "" && GetData.length() > 0) {
                                String[] parts = GetData.split(",");
                                String User_Id = "";
                                String yearid = "";
                                String classid = "";
                                String classsecid = "";
                                String classname = "";
                                String studentenrolldate = "";
                                String Name = "";
                                String academicyear = "";
                                User_Id = parts[1];
                                Constants.Logwrite("User_Id:", "" + User_Id);
                                yearid = parts[3];
                                Constants.Logwrite("YearId:", "" + yearid);
                                classid = parts[4];
                                Constants.Logwrite("ClassId:", "" + classid);
                                classsecid = parts[5];
                                Constants.Logwrite("classsecid:", "" + classsecid);
                                classname = parts[6];
                                Constants.Logwrite("classname:", "" + classname);
                                studentenrolldate = parts[7];
                                Constants.Logwrite("studentenrolldate:", ""
                                        + studentenrolldate);
                                Name = parts[8];
                                Constants.Logwrite("Name:", "" + Name);
                                Constants.Logwrite("StudentId:", "" + cn.getStudentId());
                                academicyear = parts[9];
                                ContentValues values = new ContentValues();
                                values.put(KEY_USER_ID, User_Id);
                                values.put(KEY_YEAR_ID, yearid);
                                values.put(KEY_CLASS_ID, classid);
                                values.put(KEY_CLASS_NAME, classname);
                                values.put(KEY_STUD_ENROLL_DATE,
                                        studentenrolldate);
                                values.put(KEY_NAME, Name);
                                int id = db.update(
                                        TABLE_CONTACTS1,
                                        values,
                                        "School_Id=" + schoolid
                                                + " and Stud_Id="
                                                + cn.getStudentId() + "", null);
                                /*
                                 * int dataupdate = UpdateAllStudentDetails(
                                 * cn.getStudentId(), schoolid, GetData, db);
                                 */
                                if (id > 0) {
                                    Constants.Logwrite("DataUpdated", "Success");
                                } else {
                                    Constants.Logwrite("DataUpdated", "Failure");
                                }
                            } else {
                                Constants.Logwrite("No Data For Student",
                                        "" + GetData.length());
                            }
                            // Make Method Which Give Data of Student and Update
                            // in
                        }
                    }
                } catch (Exception ex) {
                    Constants.writelog("DatabaseHandler",
                            "VirsonControl 7 Exception: " + ex.getMessage()
                                    + "::::" + ex.getStackTrace());
                }
            }
            /*if (oldVersion < 24) {
                // Create Practice test Table
                try {
                    boolean IsTExistPracticeTest = IsTabExist(db,
                            TABLE_PRACTICE_TEST_DETAILS);
                    if (!IsTExistPracticeTest) {
                        String CREATE_PRACTICETEST_TABLE = "CREATE TABLE "
                                + TABLE_PRACTICE_TEST_DETAILS + "(" + KEY_PRACTICE_TEST_ROW_ID
                                + " INTEGER PRIMARY KEY," + KEY_PRACTICE_TEST_SCHOOL_ID
                                + " INTEGER," + KEY_PRACTICE_TEST_STUD_ID + " INTEGER,"
                                + KEY_PRACTICE_TEST_TEST_ID + " INTEGER,"
                                + KEY_PRACTICE_TEST_ANS_STRING + " TEXT)";
                        db.execSQL(CREATE_PRACTICETEST_TABLE);
                    }
                } catch (Exception err) {

                }
                try {
                    boolean IsTExistPracticeElpaseTest = IsTabExist(db,
                            TABLE_PRACTICE_TEST_ElapsedTimeDETAILS);
                    if (!IsTExistPracticeElpaseTest) {
                        String CREATE_PRACTICETEST_ELPASED_TABLE = "CREATE TABLE "
                                + TABLE_PRACTICE_TEST_ElapsedTimeDETAILS + "(" + KEY_PRACTICE_TEST_TABLE_RowID
                                + " INTEGER PRIMARY KEY," + KEY_PRACTICE_TEST_Wise_RowID
                                + " INTEGER," + KEY_PRACTICE_TEST_QuestionELAPSED_TIME + " TEXT,"
                                + KEY_PRACTICE_TEST_ELAPSED_TIME + " TEXT)";
                        db.execSQL(CREATE_PRACTICETEST_ELPASED_TABLE);
                    }
                } catch (Exception err) {

                }
            }*/

            if (oldVersion == 23) {
                Constants.Logwrite("Version23", "Start");
                try {
                    final String ALTER_TBL_PracticeTest = "ALTER TABLE "
                            + TABLE_PRACTICE_TEST_DETAILS + " ADD COLUMN "
                            + TEST_TIMESTAMP + " text";
                    db.execSQL(ALTER_TBL_PracticeTest);
                } catch (Exception ex) {
                    Constants.writelog("DatabaseHandler",
                            "VirsonControl 8 Exception: " + ex.getMessage()
                                    + "::::" + ex.getStackTrace());
                }  /*try {
                    final String ALTER_TBL_PracticeTest = "ALTER TABLE "
                            + TABLE_PRACTICE_TEST_DETAILS + " ADD COLUMN "
                            + TEST_CURRENTSTART_DATE + " text";
                    db.execSQL(ALTER_TBL_PracticeTest);
                } catch (Exception ex) {
                    Constants.writelog("DatabaseHandler",
                            "VirsonControl 8 Exception: " + ex.getMessage()
                                    + "::::" + ex.getStackTrace());
                }*/

                Constants.Logwrite("Version8", "SucessfullyUpdated");
            } else if (oldVersion < 23) {
                // Create Practice test Table
                try {
                    boolean IsTExistPracticeTest = IsTabExist(db,
                            TABLE_PRACTICE_TEST_DETAILS);
                    if (!IsTExistPracticeTest) {
                       /* String CREATE_PRACTICETEST_TABLE = "CREATE TABLE "
                                + TABLE_PRACTICE_TEST_DETAILS + "(" + KEY_PRACTICE_TEST_ROW_ID
                                + " INTEGER PRIMARY KEY," + KEY_PRACTICE_TEST_SCHOOL_ID
                                + " INTEGER," + KEY_PRACTICE_TEST_STUD_ID + " INTEGER,"
                                + KEY_PRACTICE_TEST_TEST_ID + " INTEGER,"
                                + KEY_PRACTICE_TEST_ANS_STRING + " TEXT," + TEST_TIMESTAMP + " TEXT,"  + TEST_CURRENTSTART_DATE + " TEXT)"; */
                       String CREATE_PRACTICETEST_TABLE = "CREATE TABLE "
                                + TABLE_PRACTICE_TEST_DETAILS + "(" + KEY_PRACTICE_TEST_ROW_ID
                                + " INTEGER PRIMARY KEY," + KEY_PRACTICE_TEST_SCHOOL_ID
                                + " INTEGER," + KEY_PRACTICE_TEST_STUD_ID + " INTEGER,"
                                + KEY_PRACTICE_TEST_TEST_ID + " INTEGER,"
                                + KEY_PRACTICE_TEST_ANS_STRING + " TEXT," + TEST_TIMESTAMP  + " TEXT)";
                        db.execSQL(CREATE_PRACTICETEST_TABLE);
                    }
                } catch (Exception err) {

                }
                try {
                    boolean IsTExistPracticeElpaseTest = IsTabExist(db,
                            TABLE_PRACTICE_TEST_ElapsedTimeDETAILS);
                    if (!IsTExistPracticeElpaseTest) {
                        String CREATE_PRACTICETEST_ELPASED_TABLE = "CREATE TABLE "
                                + TABLE_PRACTICE_TEST_ElapsedTimeDETAILS + "(" + KEY_PRACTICE_TEST_TABLE_RowID
                                + " INTEGER PRIMARY KEY," + KEY_PRACTICE_TEST_Wise_RowID
                                + " INTEGER," + KEY_PRACTICE_TEST_QuestionELAPSED_TIME + " TEXT,"
                                + KEY_PRACTICE_TEST_ELAPSED_TIME + " TEXT)";
                        db.execSQL(CREATE_PRACTICETEST_ELPASED_TABLE);
                    }
                } catch (Exception err) {

                }
            }


            if (oldVersion <= 8) {
                Constants.Logwrite("Version8", "Start");
                try {
                    final String ALTER_TBL_lastupdatetime = "ALTER TABLE "
                            + TABLE_CONTACTS1 + " ADD COLUMN "
                            + KEY_UPDATED_TIME + " text";
                    db.execSQL(ALTER_TBL_lastupdatetime);
                } catch (Exception ex) {
                    Constants.writelog("DatabaseHandler",
                            "VirsonControl 8 Exception: " + ex.getMessage()
                                    + "::::" + ex.getStackTrace());
                }
                Constants.Logwrite("Version8", "SucessfullyUpdated");
            }
            if (oldVersion <= 9) {
                try {
                    List<Contact> contacts = getAllContacts(db);
                    Constants.Logwrite("TotalAccount", "" + contacts.size());
                    if (contacts.size() > 0) {
                        for (Contact cn : contacts) {

                            int schoolid = cn.getSchoolId();
                            Constants.Logwrite("SchoolId", "" + schoolid);
                            int studid = cn.getStudentId();
                            int yearid = cn.getYearId();
                            String[] messges = Constants
                                    .GetMessageDetailsLatestKumKum2(cntxt,
                                            studid, schoolid, yearid);
                            if (messges.length > 0) {
                                for (int i = 0; i < messges.length; i++) {
                                    try {
                                        String[] msgitem = messges[i]
                                                .split("##@@##@@");
                                        int SMS_MSG_ID = Integer
                                                .parseInt(msgitem[4]);

                                        Boolean Ismessgaeinsert = CheckMessageInsertorNot(
                                                studid, schoolid, SMS_MSG_ID,
                                                db);
                                        if (Ismessgaeinsert) {

                                        } else {
                                            AddSMS(new Contact(
                                                    Integer.parseInt(msgitem[4]
                                                    ),
                                                    msgitem[6],
                                                    studid, schoolid,
                                                    Integer.parseInt(msgitem[0]
                                                    ),
                                                    Integer.parseInt(msgitem[1]
                                                    ),
                                                    Integer.parseInt(msgitem[2]
                                                    ),
                                                    Integer.parseInt(msgitem[3]
                                                    ),
                                                    Integer.parseInt(msgitem[5]
                                                    )), db);
                                        }
                                    } catch (Exception err) {
                                    }
                                }
                            }
                        }
                    }
                    Constants.writelog("DatabaseHandler",
                            "Virsioncontrol for version 9 Successful.");
                } catch (Exception err) {
                    Constants.writelog("DatabaseHandler",
                            "version 9 Exception: " + err.getMessage() + ":::"
                                    + err.getStackTrace());
                }
            }
            if (oldVersion <= 11) {
                // Vehicle Update
                Constants.Logwrite("Version11", "Start");
                final String ALTER_TBL_Route_ID = "ALTER TABLE "
                        + TABLE_CONTACTS1 + " ADD COLUMN " + KEY_ROUTE_ID
                        + " Integer";
                try {
                    db.execSQL(ALTER_TBL_Route_ID);
                } catch (Exception ex) {
                    Constants.writelog("DatabaseHandler",
                            "Onupdate virsion11:468 " + ex.getMessage() + ":::"
                                    + ex.getStackTrace());
                }
                Constants.Logwrite("Version11", "SucessfullyUpdated");
                try {
                    List<Contact> contacts = getAllContacts_for_Database_ver_5(db);
                    Constants.Logwrite("TotalAccount", "" + contacts.size());
                    if (contacts.size() > 0) {
                        for (Contact cn : contacts) {
                            String schoolid = Integer
                                    .toString(cn.getSchoolId());
                            String studid = Integer.toString(cn.getStudentId());
                            int yearid = cn.getYearId();
                            int Route_Id = 0;
                            String studentinfo = Constants
                                    .GetStudentDetailsKumKumVehicle(cntxt,
                                            schoolid, studid, yearid);
                            if (studentinfo != "") {
                                String[] spltrstr = studentinfo.split(",");
                                if (spltrstr.length > 0) {
                                    Route_Id = Integer.parseInt(spltrstr[13]
                                    );

                                    UpdateContactRouteId(studid, schoolid,
                                            Route_Id, Integer.toString(yearid),
                                            db);
                                }
                            }
                        }
                    }
                    Constants.writelog("DatabaseHandler",
                            "Virsioncontrol for version 11 Successful.");
                } catch (Exception err) {
                    Constants.Logwrite("DatabaseHandler", "Virsioncontrol for version 11"
                            + err.getMessage() + ":::" + err.getStackTrace());
                    Constants.writelog(
                            "DatabaseHandler",
                            "Virsioncontrol for version 11 Exception: "
                                    + err.getMessage() + ":::"
                                    + err.getStackTrace());
                }
                // Get Student Details and and Updated Route Id
            }
            if (oldVersion <= 12) {
                Constants.Logwrite("Version 12", "Start");
                final String ALTER_TBL_Sec_Name = "ALTER TABLE "
                        + TABLE_CONTACTS1 + " ADD COLUMN "
                        + KEY_CLASS_SECTION_NAME + " TEXT";
                try {
                    db.execSQL(ALTER_TBL_Sec_Name);
                } catch (Exception ex) {
                    Constants.Logwrite("DatabaseHandler", "Excepption:" + ex.getMessage()
                            + "::::" + ex.getStackTrace());
                }
                try {
                    List<Contact> contacts = getAllContacts_for_Database_ver_5(db);
                    Constants.Logwrite("TotalAccount", "" + contacts.size());
                    if (contacts.size() > 0) {
                        for (Contact cn : contacts) {
                            String schoolid = Integer
                                    .toString(cn.getSchoolId());
                            int studid = cn.getStudentId();
                            int yearid = cn.getYearId();
                            String classsectionname = Constants
                                    .GetClassSectionName(cntxt, studid, yearid,
                                            schoolid);
                            Constants.Logwrite("Version 12", "ClassSectionName:"
                                    + classsectionname);
                            Constants.writelog("Version 12",
                                    "ClassSectionName:" + classsectionname);
                            UpdateClassSectionName(String.valueOf(studid),
                                    schoolid, classsectionname,
                                    String.valueOf(yearid), db);
                        }
                    }
                    Constants.Logwrite("Version 12", "End");
                    Constants.writelog("DatabaseHandler",
                            "Virsioncontrol for version 12 Successful.");
                } catch (Exception err) {
                    Constants.Logwrite("DatabaseHandler", "Virsioncontrol for version 12"
                            + err.getMessage() + ":::" + err.getStackTrace());
                    Constants.writelog(
                            "DatabaseHandler",
                            "Virsioncontrol for version 12 Exception: "
                                    + err.getMessage() + ":::"
                                    + err.getStackTrace());
                }
            }
            if (oldVersion <= 13) {
                try {
                    boolean IsTExistCircular = IsTabExist(db,
                            TABLE_CIRCULAR_DETAILS);
                    if (!IsTExistCircular) {
                        String CREATE_CIRCULAR_TABLE = "CREATE TABLE "
                                + TABLE_CIRCULAR_DETAILS + "("
                                + KEY_CIRCULAR_MASTER_ID
                                + " INTEGER PRIMARY KEY," + KEY_CIRCULAR_STUDID
                                + " INTEGER," + KEY_CIRCULAR_SCHOOLID
                                + " INTEGER," + KEY_CIRCULAR_YEARID
                                + " INTEGER," + KEY_CIRCULAR_GROUPID
                                + " INTEGER," + KEY_CIRCULAR_GROUPNAME
                                + " TEXT," + KEY_CIRCULARID + " INTEGER,"
                                + KEY_CIRCULAR_NAME + " TEXT,"
                                + KEY_CIRCULAR_DATETEXT + " TEXT,"
                                + KEY_CIRCULAR_PATH + " TEXT,"
                                + KEY_CIRCULAR_TICKS + " TEXT,"
                                + KEY_CIRCULAR_ISDELETED + " INTEGER)";

                        try {
                            db.execSQL(CREATE_CIRCULAR_TABLE);
                            Constants
                                    .writelog("DatabaseHandler",
                                            "VersionControl 13:604 circular table created");
                        } catch (Exception ex) {
                            // TODO Auto-generated catch block
                            Constants.writelog(
                                    "DatabaseHandler",
                                    "VersionControl 13:625 Exception:"
                                            + ex.getMessage() + ":::::"
                                            + ex.getStackTrace());
                            ex.printStackTrace();
                        }
                    }
                    Constants.Logwrite("Version 13", "End");
                    Constants.writelog("DatabaseHandler",
                            "Virsioncontrol for version 13 Successful.");

                } catch (Exception err) {
                    Constants.Logwrite("DatabaseHandler",
                            "Exception 643 Virsion 13:" + err.getMessage()
                                    + ":::::::" + err.getStackTrace());
                    Constants.writelog("DatabaseHandler",
                            "Exception 643 Virsion 13:" + err.getMessage()
                                    + ":::::::" + err.getStackTrace());
                }
            }

            if (oldVersion <= 14) {
                try {
                    if (oldVersion < 14) {
                        try {
                            db.execSQL("delete  from " + TABLE_EXAM_DETAILS);
                        } catch (Exception ex) {
                        }
                    }
                    boolean IsTExistStudProfile = IsTabExist(db,
                            TABLE_STUD_PROFILE);
                    if (!IsTExistStudProfile) {
                        String CREATE_STUDPROFILE_TABLE = "CREATE TABLE "
                                + TABLE_STUD_PROFILE + "(" + KEY_PROFILE_ID
                                + " INTEGER PRIMARY KEY," + KEY_PROFILE_STUD_ID
                                + " INTEGER," + KEY_PROFILE_SCHOOL_ID
                                + " INTEGER," + KEY_PROFILE_YEAR_ID
                                + " INTEGER," + KEY_PROFILE_GRNO + " TEXT,"
                                + KEY_PROFILE_NAME + " TEXT,"
                                + KEY_PROFILE_CLASS + " TEXT,"
                                + KEY_PROFILE_CASTE + " TEXT,"
                                + KEY_PROFILE_CATEGORY + " TEXT,"
                                + KEY_PROFILE_DOB + " TEXT,"
                                + KEY_PROFILE_CONTACT_NUMBER + " TEXT,"
                                + KEY_PROFILE_IMAGE + " TEXT)";

                        try {
                            db.execSQL(CREATE_STUDPROFILE_TABLE);
                            Constants
                                    .writelog("DatabaseHandler",
                                            "VersionControl 14:709 studProfile table created");
                        } catch (Exception ex) {
                            Constants.writelog(
                                    "DatabaseHandler",
                                    "VersionControl 14:713 Exception:"
                                            + ex.getMessage() + ":::::"
                                            + ex.getStackTrace());
                        }
                    }

                    boolean IsExistStudAttTable = IsTabExist(db,
                            TABLE_STUD_ATTENDANCE);
                    if (!IsExistStudAttTable) {
                        String CREATE_STUDATT_TABLE = "CREATE TABLE "
                                + TABLE_STUD_ATTENDANCE + "(" + KEY_ATT_ID
                                + " INTEGER PRIMARY KEY," + KEY_ATT_STUD_ID
                                + " INTEGER," + KEY_ATT_SCHOOL_ID + " INTEGER,"
                                + KEY_ATT_YEAR_ID + " INTEGER,"
                                + KEY_ATT_DETAILS + " TEXT)";

                        try {
                            db.execSQL(CREATE_STUDATT_TABLE);
                            Constants
                                    .writelog("DatabaseHandler",
                                            "VersionControl 14:809 studattandance table created");
                        } catch (Exception ex) {
                            Constants.writelog(
                                    "DatabaseHandler",
                                    "VersionControl 14:813 studattandance Exception:"
                                            + ex.getMessage() + ":::::"
                                            + ex.getStackTrace());
                        }
                    }
                } catch (Exception ex) {
                    Constants.writelog(
                            "DatabaseHandler",
                            "VersionControl 14:822 Exception:"
                                    + ex.getMessage() + ":::::"
                                    + ex.getStackTrace());
                }
            }
            if (oldVersion < 15) {
                Constants.writelog("DatabaseHandler",
                        "VersionControl 15:854");
                db.execSQL("delete from " + TABLE_SMS2);
            }
            if (oldVersion < 16) {
                Constants.writelog("DatabaseHandler",
                        "VersionControl 16:861");
                try {
                    try {
                        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_ALBUM_DETAILS);
                    } catch (Exception ex) {
                        Constants.writelog("DatabaseHandler", "On delete album table MSG:" + ex.getMessage());
                    }
                    String CREATE_EXAM_TABLE = "CREATE TABLE "
                            + TABLE_STUDENT_ALBUM_DETAILS + "("
                            + KEY_ALBUM_MASTER_ID_ + " INTEGER PRIMARY KEY,"
                            + KEY_ALBUM_STUDID + " INTEGER,"
                            + KEY_ALBUM_ALBUMID + " INTEGER,"
                            + KEY_ALBUM_ALBUMNAME + " TEXT,"
                            + KEY_ALBUM_ALBUMURL + " TEXT,"
                            + KEY_ALBUM_PHOTOFILENAME + " TEXT,"
                            + KEY_ALBUM_SCHOOLID + " INTEGER,"
                            + KEY_ALBUM_CLASSSECID + " INTEGER,"
                            + KEY_ALBUM_DATETICKS + " INTEGER,"
                            + KEY_ALBUM_DATETIME + " TEXT)";
                    db.execSQL(CREATE_EXAM_TABLE);
                } catch (Exception ex) {
                    Constants.writelog("DatabaseHandler", "VersionControl 16:884 MSG:" + ex.getMessage());
                }
            }
           /* if (oldVersion < 17) {
                final String ALTER_TBL_ADD_ISPAY_SHOW = "ALTER TABLE "
                        + TABLE_CONTACTS1 + " ADD COLUMN " + KEY_ISPAY_SHOW
                        + " INTEGER";
                try {
                    db.execSQL(ALTER_TBL_ADD_ISPAY_SHOW);
                } catch (Exception ex) {
                    Constants.Logwrite("DatabaseHandler", "Excepption virsion 17 894:"
                            + ex.getMessage() + "::::" + ex.getStackTrace());
                }
            }*/
            if (oldVersion <= 18) {
                final String ALTER_TBL_ADD_MENU_PAY = "ALTER TABLE "
                        + TABLE_CONTACTS1 + " ADD COLUMN " + KEY_MENU
                        + " TEXT";
                try {
                    db.execSQL(ALTER_TBL_ADD_MENU_PAY);
                } catch (Exception ex) {
                    Constants.Logwrite("DatabaseHandler", "Excepption virsion 18 898:"
                            + ex.getMessage() + "::::" + ex.getStackTrace());
                }
                final String ALTER_TBL_REMOVE_EXTRA = "ALTER TABLE "
                        + TABLE_CONTACTS1 + " DROP COLUMN " + KEY_ISFEE_SHOW;
                final String ALTER_TBL_REMOVE_EXTRA1 = "ALTER TABLE "
                        + TABLE_CONTACTS1 + " DROP COLUMN " + KEY_ISPAY_SHOW;
                try {
                    db.execSQL(ALTER_TBL_REMOVE_EXTRA);
                    db.execSQL(ALTER_TBL_REMOVE_EXTRA1);
                } catch (Exception ex) {
                    Constants.Logwrite("DatabaseHandler", "Excepption virsion 18 906:"
                            + ex.getMessage() + "::::" + ex.getStackTrace());
                }
            }
            if (oldVersion <= 20) {
                try {
                    boolean IsTExistProfile = IsTabExist(db, TABLE_PROFILE);
                    if (!IsTExistProfile) {
                        String CREATE_PROFILE_TABLE = "CREATE TABLE "
                                + TABLE_PROFILE + "(" + KEY_PROFILE_IDS
                                + " INTEGER PRIMARY KEY," + KEY_PROFILE_STUD_IDS
                                + " INTEGER," + KEY_PROFILE_SCHOOL_IDS + " INTEGER,"
                                + KEY_PROFILE_YEAR_IDS + " INTEGER,"
                                + KEY_PROFILE_TITLE + " TEXT," + KEY_PROFILE_DESC
                                + " TEXT," + KEY_IS_STUD + " TEXT)";

                        try {
                            db.execSQL(CREATE_PROFILE_TABLE);
                            Constants.writelog("DatabaseHandler",
                                    "OnUpdate:997 Profile table created");
                        } catch (Exception ex) {
                            Constants.writelog("DatabaseHandler",
                                    "OnUpdate:1000 Exception:" + ex.getMessage()
                                            + ":::::" + ex.getStackTrace());
                        }
                    }
                } catch (Exception ex) {
                    Constants.writelog("DatabaseHandler",
                            "OnCreate:1006 Exception:" + ex.getMessage() + ":::::"
                                    + ex.getStackTrace());
                }
            }

            if (oldVersion <= 21) {
                try {
                    try {
                        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAM_DETAILS);
                    } catch (Exception ex) {
                        Constants.writelog("DatabaseHandler", "On delete Exam table 978 MSG:" + ex.getMessage());
                    }
                    String CREATE_EXAM_TABLE = "CREATE TABLE "
                            + TABLE_EXAM_DETAILS + "(" + KEY_EXAM_ROW_ID
                            + " INTEGER PRIMARY KEY," + KEY_EXAM_YEAR_ID
                            + " INTEGER," + KEY_EXAM_SR_NO + " INTEGER," + KEY_EXAM_STUDID + " INTEGER,"
                            + KEY_EXAM_SCHOOLID + " INTEGER," + KEY_EXAM_EXAMID
                            + " INTEGER," + KEY_EXAM_EXAMNAME + " TEXT,"
                            + KEY_EXAM_EXAMMARKS + " TEXT,"
                            + KEY_EXAM_ISMARKSHEET + " INTEGER,"
                            + KEY_EXAM_MARKSHEET_PATH + " TEXT,"
                            + KEY_EXAM_CATEGORY_ID + " TEXT,"
                            + KEY_EXAM_CATEGORY_NAME + " TEXT)";
                    db.execSQL(CREATE_EXAM_TABLE);
                } catch (Exception ex) {
                    Constants.writelog("DatabaseHandler",
                            "OnCreate:999 Exception:" + ex.getMessage() + ":::::"
                                    + ex.getStackTrace());
                }
            }

            if (oldVersion <= 22) {
                SQLiteDatabase myWritableDb = null;
                try {
                    myWritableDb = this.getWritableDatabase();
                    myWritableDb.execSQL("delete from " + TABLE_CIRCULAR_DETAILS);
                } catch (Exception err) {
                    Constants.writelog("DeleteTableRecords()893 ", " Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
                } finally {
                    close(myWritableDb);
                }
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:", "Exception virsion 19 : 1004" + err.getMessage()
                    + "StackTrace:" + err.getStackTrace().toString());
            Constants.writelog("Database:",
                    "Exception:virsion 21  1007" + err.getMessage() + "StackTrace:"
                            + err.getStackTrace().toString());
        }
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    // Adding new contact
    public int addContact(Contact contact) {
        Constants.Logwrite("Database:addContact", "Enter In AddConatatMethod");
        int Ins_Status = 0;
        SQLiteDatabase myWritableDb = null;
        try {
            myWritableDb = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, contact.getName()); // Contact Name 4622 7153 5817 6199
            values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone
            values.put(KEY_LOG_PIN, contact.getLogPin()); // Login Pin
            values.put(KEY_IS_DEF, contact.getIsDef()); // Is Default

            values.put(KEY_STUD_ID, contact.getStudentId()); // Student_Id
            values.put(KEY_SCHOOLID, contact.getSchoolId()); // School_Id
            values.put(KEY_YEAR_ID, contact.getYearId()); // Year_Id

            values.put(KEY_CLASS_ID, contact.getClassId()); // Class_Id
            values.put(KEY_CLASS_SEC_ID, contact.getSecId()); // Sec_Id

            values.put(KEY_USER_ID, contact.getUserId()); // Sec_Id
            values.put(KEY_CLASS_NAME, contact.getClassName()); // Sec_Id

            values.put(KEY_STUD_ENROLL_DATE, contact.getstudentenrolldate()); // Sec_Id
            values.put(KEY_LAST_UPDATED_TIME, contact.getlastupdatedtime()); // Sec_Id

            values.put(KEY_ACADEMIC_YEAR, contact.getAcademicyear()); // Sec_Id
            values.put(KEY_UPDATED_TIME, contact.getUpdatedtime());
            values.put(KEY_ROUTE_ID, contact.getRouteId());// Route_Id
            // new added 17-8-2015
            values.put(KEY_CLASS_SECTION_NAME, contact.getClassSecName());// Class

            // Name
            // Inserting Row
            myWritableDb.insert(TABLE_CONTACTS1, null, values);
            Ins_Status = 1;
        } catch (Exception err) {
            Constants.Logwrite("DatabaseHandler:AddContact", "Exception" + err.getMessage()
                    + ":::StactTrace:" + err.getStackTrace().toString());
            Ins_Status = 0;
            Constants.writelog(
                    "DatabaseHandler",
                    "errr:544 " + err.getMessage() + "::::  StrackTrace"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb); // Closing database connection
        }
        return Ins_Status;
    }




   /* public int addContact(Contact contact) {
        Constants.Logwrite("Database:addContact", "Enter In AddConatatMethod");
        int Ins_Status = 0;
        SQLiteDatabase myWritableDb = null;
        try {
            myWritableDb = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, contact.getName()); // Contact Name
            values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone
            values.put(KEY_LOG_PIN, contact.getLogPin()); // Login Pin
            values.put(KEY_IS_DEF, contact.getIsDef()); // Is Default

            values.put(KEY_STUD_ID, contact.getStudentId()); // Student_Id
            values.put(KEY_SCHOOLID, contact.getSchoolId()); // School_Id
            values.put(KEY_YEAR_ID, contact.getYearId()); // Year_Id

            values.put(KEY_CLASS_ID, contact.getClassId()); // Class_Id
            values.put(KEY_CLASS_SEC_ID, contact.getSecId()); // Sec_Id

            values.put(KEY_USER_ID, contact.getUserId()); // Sec_Id
            values.put(KEY_CLASS_NAME, contact.getClassName()); // Sec_Id

            values.put(KEY_STUD_ENROLL_DATE, contact.getstudentenrolldate()); // Sec_Id
            values.put(KEY_LAST_UPDATED_TIME, contact.getlastupdatedtime()); // Sec_Id

            values.put(KEY_ACADEMIC_YEAR, contact.getAcademicyear()); // Sec_Id
            values.put(KEY_UPDATED_TIME, contact.getUpdatedtime());
            values.put(KEY_ROUTE_ID, contact.getRouteId());// Route_Id
            // new added 17-8-2015
            values.put(KEY_CLASS_SECTION_NAME, contact.getClassSecName());// Class

            // Name
            // Inserting Row
            myWritableDb.insert(TABLE_CONTACTS1, null, values);
            Ins_Status = 1;
        } catch (Exception err) {
            Constants.Logwrite("DatabaseHandler:AddContact", "Exception" + err.getMessage()
                    + ":::StactTrace:" + err.getStackTrace().toString());
            Ins_Status = 0;
            Constants.writelog(
                    "DatabaseHandler",
                    "errr:544 " + err.getMessage() + "::::  StrackTrace"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb); // Closing database connection
        }
        return Ins_Status;
    }*/

    // Getting single contact
    Contact getContact(int id) {
        Contact contact = null;
        Cursor cursor = null;
        SQLiteDatabase myWritableDb = null;

        try {
            myWritableDb = this.getWritableDatabase();
            // jaydeep old before add class section name 17-8-2015
            /*
             * cursor = myWritableDb .query(TABLE_CONTACTS1, new String[] {
             * KEY_ID, KEY_NAME, KEY_PH_NO, KEY_LOG_PIN, KEY_IS_DEF,
             * KEY_STUD_ID, KEY_SCHOOLID, KEY_YEAR_ID, KEY_CLASS_ID,
             * KEY_CLASS_SEC_ID, KEY_USER_ID, KEY_CLASS_NAME,
             * KEY_STUD_ENROLL_DATE, KEY_LAST_UPDATED_TIME, KEY_ACADEMIC_YEAR,
             * KEY_UPDATED_TIME, KEY_ROUTE_ID }, KEY_ID + "=?", new String[] {
             * String.valueOf(id) }, null, null, null, null);
             */
            // jaydeep new after add class section name 17-8-2015
            cursor = myWritableDb
                    .query(TABLE_CONTACTS1, new String[]{KEY_ID, KEY_NAME,
                                    KEY_PH_NO, KEY_LOG_PIN, KEY_IS_DEF, KEY_STUD_ID,
                                    KEY_SCHOOLID, KEY_YEAR_ID, KEY_CLASS_ID,
                                    KEY_CLASS_SEC_ID, KEY_USER_ID, KEY_CLASS_NAME,
                                    KEY_STUD_ENROLL_DATE, KEY_LAST_UPDATED_TIME,
                                    KEY_ACADEMIC_YEAR, KEY_UPDATED_TIME, KEY_ROUTE_ID,
                                    KEY_CLASS_SECTION_NAME}, KEY_ID + "=?",
                            new String[]{String.valueOf(id)}, null, null,
                            null, null);
            if (cursor != null)
                cursor.moveToFirst();

            contact = new Contact(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2),
                    Integer.parseInt(cursor.getString(3)),
                    Integer.parseInt(cursor.getString(4)),
                    Integer.parseInt(cursor.getString(5)),
                    Integer.parseInt(cursor.getString(6)),
                    Integer.parseInt(cursor.getString(7)),
                    Integer.parseInt(cursor.getString(8)),
                    Integer.parseInt(cursor.getString(9)),
                    Integer.parseInt(cursor.getString(10)),
                    cursor.getString(11), cursor.getString(12),
                    cursor.getString(13), cursor.getString(14),
                    cursor.getString(15),
                    Integer.parseInt(cursor.getString(16)),
                    cursor.getString(17));
        } catch (Exception err) {
            Constants.Logwrite("SQL", "getContact:" + err.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        // return contact
        return contact;
    }

    // Getting All Contacts
    public List<Contact> getAllContactsOld() {
        List<Contact> contactList = new ArrayList<Contact>();
        Cursor cursor = null;
        SQLiteDatabase myWritableDb = null;
        try {

            myWritableDb = this.getWritableDatabase();
            String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS
                    + " ORDER BY isdef DESC";
            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.setID(Integer.parseInt(cursor.getString(0)));
                    contact.setName(cursor.getString(1));
                    contact.setPhoneNumber(cursor.getString(2));
                    contact.setLogPin(Integer.parseInt(cursor.getString(3)));
                    contact.setIsDef(Integer.parseInt(cursor.getString(4)));
                    contact.setStudnetId(Integer.parseInt(cursor.getString(5)));
                    contact.SetSchoolId(Integer.parseInt(cursor.getString(6)));
                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }

        } catch (Exception err) {
            Constants.Logwrite("Database:getAllContactsOld", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    // Getting All Contacts
    public List<Contact> getAllContacts_for_Database_ver_5(SQLiteDatabase db) {
        List<Contact> contactList = new ArrayList<Contact>();
        Cursor cursor = null;
        try {

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS1
                    + " ORDER BY isdef DESC";
            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.setID(Integer.parseInt(cursor.getString(0)));
                    contact.setName(cursor.getString(1));
                    contact.setPhoneNumber(cursor.getString(2));
                    contact.setLogPin(Integer.parseInt(cursor.getString(3)));
                    contact.setIsDef(Integer.parseInt(cursor.getString(4)));
                    contact.setStudnetId(Integer.parseInt(cursor.getString(5)));
                    contact.SetSchoolId(Integer.parseInt(cursor.getString(6)));
                    contact.SetYearId(Integer.parseInt(cursor.getString(7)));
                    contact.SetClassId(Integer.parseInt(cursor.getString(8)));
                    contact.SetSecId(Integer.parseInt(cursor.getString(9)));
                    contact.SetUserId(Integer.parseInt(cursor.getString(10)));
                    contact.setClassName(cursor.getString(11));
                    contact.setstudentenrolldate(cursor.getString(12));
                    contact.setlastupdatedtime(cursor.getString(13));
                    contact.setAcademicyear(cursor.getString(14));

                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
            // return count;
            return contactList;
        } catch (Exception err) {
            Constants.Logwrite("Database:getAllContacts_for_Database_ver_5",
                    "" + err.getMessage() + "StackStrace:"
                            + err.getStackTrace() + "");

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return contactList;
    }

    // Getting All Contacts
    public List<Contact> getAllContacts(SQLiteDatabase db) {
        List<Contact> contactList = new ArrayList<Contact>();
        Cursor cursor = null;
        try {

            String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS1
                    + " ORDER BY isdef DESC";
            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.setID(Integer.parseInt(cursor.getString(0)));
                    contact.setName(cursor.getString(1));
                    contact.setPhoneNumber(cursor.getString(2));
                    contact.setLogPin(Integer.parseInt(cursor.getString(3)));
                    contact.setIsDef(Integer.parseInt(cursor.getString(4)));
                    contact.setStudnetId(Integer.parseInt(cursor.getString(5)));
                    contact.SetSchoolId(Integer.parseInt(cursor.getString(6)));
                    contact.SetYearId(Integer.parseInt(cursor.getString(7)));

                    contact.SetClassId(Integer.parseInt(cursor.getString(8)));
                    contact.SetSecId(Integer.parseInt(cursor.getString(9)));

                    contact.SetUserId(Integer.parseInt(cursor.getString(10)));
                    contact.setClassName(cursor.getString(11));
                    contact.setstudentenrolldate(cursor.getString(12));
                    contact.setlastupdatedtime(cursor.getString(13));
                    contact.setAcademicyear(cursor.getString(14));
                    contact.setRouteId(Integer.parseInt(cursor.getString(16)));
                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:getAllContacts", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return contactList;
    }

    // Getting All Contacts
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        Cursor cursor = null;
        SQLiteDatabase myWritableDb = null;
        try {
            myWritableDb = this.getWritableDatabase();
            String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS1
                    + " ORDER BY isdef DESC";
            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.setID(Integer.parseInt(cursor.getString(0)));
                    contact.setName(cursor.getString(1));
                    contact.setPhoneNumber(cursor.getString(2));
                    contact.setLogPin(Integer.parseInt(cursor.getString(3)));
                    contact.setIsDef(Integer.parseInt(cursor.getString(4)));
                    contact.setStudnetId(Integer.parseInt(cursor.getString(5)));
                    contact.SetSchoolId(Integer.parseInt(cursor.getString(6)));
                    contact.SetYearId(Integer.parseInt(cursor.getString(7)));
                    contact.SetClassId(Integer.parseInt(cursor.getString(8)));
                    contact.SetSecId(Integer.parseInt(cursor.getString(9)));
                    contact.SetUserId(Integer.parseInt(cursor.getString(10)));
                    contact.setClassName(cursor.getString(11));
                    contact.setstudentenrolldate(cursor.getString(12));
                    contact.setlastupdatedtime(cursor.getString(13));
                    contact.setAcademicyear(cursor.getString(14));
                    contact.setRouteId(Integer.parseInt(cursor.getString(16)));
                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:getAllContacts", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    // Getting All Contacts
    public List<Contact> getPhoneAndPin() {
        List<Contact> contactList = new ArrayList<Contact>();
        Cursor cursor = null;
        SQLiteDatabase myWritableDb = null;
        try {

            myWritableDb = this.getWritableDatabase();
            String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS1
                    + " ORDER BY isdef DESC";
            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.setName(cursor.getString(1));
                    contact.setPhoneNumber(cursor.getString(2));
                    contact.setLogPin(Integer.parseInt(cursor.getString(3)));
                    contact.setIsDef(Integer.parseInt(cursor.getString(4)));
                    contact.setStudnetId(Integer.parseInt(cursor.getString(5)));
                    contact.SetSchoolId(Integer.parseInt(cursor.getString(6)));
                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:GetPhoneAndPin", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    // Updating single contact
    public int updateContact(Contact contact) {
        int status = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, contact.getName());
            values.put(KEY_PH_NO, contact.getPhoneNumber());
            values.put(KEY_LOG_PIN, contact.getLogPin());
            values.put(KEY_IS_DEF, contact.getIsDef());
            values.put(KEY_STUD_ID, contact.getStudentId());
            values.put(KEY_SCHOOLID, contact.getSchoolId());
            values.put(KEY_YEAR_ID, contact.getYearId());
            values.put(KEY_CLASS_ID, contact.getClassId());
            values.put(KEY_CLASS_SEC_ID, contact.getSecId());
            values.put(KEY_USER_ID, contact.getUserId());
            values.put(KEY_CLASS_NAME, contact.getClassName());
            values.put(KEY_STUD_ENROLL_DATE, contact.getstudentenrolldate());
            values.put(KEY_LAST_UPDATED_TIME, contact.getlastupdatedtime());
            values.put(KEY_ACADEMIC_YEAR, contact.getAcademicyear());

            // updating row
            status = myWritableDb.update(TABLE_CONTACTS1, values, KEY_ID
                    + " = ?", new String[]{String.valueOf(contact.getID())});
        } catch (Exception ex) {
            Constants.Logwrite("Database:updateContact", "Message:" + ex.getMessage()
                    + "StactTrace:" + ex.getStackTrace());
            Constants.writelog("DBHandler", "UpdateContact() Ex:1215 "
                    + ex.getMessage() + ":::::::" + ex.getStackTrace());
        } finally {
            close();
        }
        return status;
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_CONTACTS1, KEY_ID + " = ?",
                    new String[]{String.valueOf(contact.getID())});
        } catch (Exception ex) {
            Constants.Logwrite("Database:deleteContact", "Message:" + ex.getMessage()
                    + "StactTrace:" + ex.getStackTrace());
            Constants.writelog("DBHandler", "DeleteContact() Ex:1231 "
                    + ex.getMessage() + ":::::::" + ex.getStackTrace());
        } finally {
            db.close();
        }
    }

    // Getting contacts Count
    public int getContactsCountold() {
        int count = 0;
        Cursor cursor = null;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed()) {
                count = cursor.getCount();
            }
        } catch (Exception ex) {
            Constants.Logwrite("Database:getContactsCountold", "Message:" + ex.getMessage()
                    + "StactTrace:" + ex.getStackTrace());
            Constants.writelog("DBHandler",
                    "getContactsCountOld() Ex:1826 " + ex.getMessage()
                            + ":::::::" + ex.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return count;
    }

    // Getting contacts Count
    public int getContactsCount() {

        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String countQuery = "SELECT  * FROM " + TABLE_CONTACTS1;
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed()) {
                count = cursor.getCount();
            }
        } catch (Exception ex) {
            Constants.Logwrite("getContactsCount", "Exception:" + ex.getMessage()
                    + "Stacktrace:" + ex.getStackTrace());
            Constants.writelog("DBHandler", "GetContactCount() Ex:1277 "
                    + ex.getMessage() + ":::::::" + ex.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return count;
    }

    // Getting contacts Count
    public int DeleteAllContact() {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            myWritableDb.execSQL("delete from " + TABLE_CONTACTS1);
            count = 1;
        } catch (Exception err) {
            Constants.Logwrite("Database:DeleteAllContact", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    public int DeleteTableRecords(String type) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        try {
            myWritableDb = this.getWritableDatabase();
            Constants.writelog("DeleteTableRecords()1335 ",
                    "Start For Type:" + type);
            if (type.equals("0")) {
                myWritableDb.execSQL("delete from " + TABLE_SMS2);
                myWritableDb.execSQL("delete from " + TABLE_ACADEMIC_YEAR);
                myWritableDb.execSQL("delete from " + TABLE_EXAM_DETAILS);
                myWritableDb.execSQL("delete from "
                        + TABLE_STUDENT_ALBUM_DETAILS);
                myWritableDb.execSQL("delete from " + TABLE_CIRCULAR_DETAILS);
                myWritableDb.execSQL("delete from " + TABLE_STUD_PROFILE);
                myWritableDb.execSQL("delete from " + TABLE_STUD_ATTENDANCE);
            } else if (type.equals("1")) {
                myWritableDb.execSQL("delete from " + TABLE_SMS2);
            } else if (type.equals("2")) {
                myWritableDb.execSQL("delete from " + TABLE_ACADEMIC_YEAR);
            } else if (type.equals("3")) {
                myWritableDb.execSQL("delete from " + TABLE_EXAM_DETAILS);
            } else if (type.equals("4")) {
                myWritableDb.execSQL("delete from " + TABLE_STUDENT_ALBUM_DETAILS);
            } else if (type.equals("5")) {
                myWritableDb.execSQL("delete from " + TABLE_CIRCULAR_DETAILS);
            } else if (type.equals("6")) {
                myWritableDb.execSQL("delete from " + TABLE_STUD_PROFILE);

            } else if (type.equals("7")) {
                myWritableDb.execSQL("delete from " + TABLE_STUD_ATTENDANCE);
            }
            count = 1;
        } catch (Exception err) {
            Constants.writelog("DeleteTableRecords()1376 ", "Type:"
                    + type + " Message:" + err.getMessage() + "StactTrace:"
                    + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    public int ClearAllRecords(String type) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        try {
            myWritableDb = this.getWritableDatabase();
            Constants.writelog("DeleteTableRecords()1335 ",
                    "Start For Type:" + type);
            if (type.equals("0")) {
                myWritableDb.execSQL("delete from " + TABLE_SMS2);
                myWritableDb.execSQL("delete from " + TABLE_ACADEMIC_YEAR);
                myWritableDb.execSQL("delete from " + TABLE_EXAM_DETAILS);
                myWritableDb.execSQL("delete from "
                        + TABLE_STUDENT_ALBUM_DETAILS);
                myWritableDb.execSQL("delete from " + TABLE_CIRCULAR_DETAILS);
                myWritableDb.execSQL("delete from " + TABLE_STUD_PROFILE);
                myWritableDb.execSQL("delete from " + TABLE_STUD_ATTENDANCE);
                myWritableDb.execSQL("delete from " + TABLE_CONTACTS1);
            } else if (type.equals("1")) {
                myWritableDb.execSQL("delete from " + TABLE_SMS2);
            } else if (type.equals("2")) {
                myWritableDb.execSQL("delete from " + TABLE_ACADEMIC_YEAR);
            } else if (type.equals("3")) {
                myWritableDb.execSQL("delete from " + TABLE_EXAM_DETAILS);
            } else if (type.equals("4")) {
                myWritableDb.execSQL("delete from "
                        + TABLE_STUDENT_ALBUM_DETAILS);
            } else if (type.equals("5")) {
                myWritableDb.execSQL("delete from " + TABLE_CIRCULAR_DETAILS);
            } else if (type.equals("6")) {
                myWritableDb.execSQL("delete from " + TABLE_STUD_PROFILE);

            } else if (type.equals("7")) {
                myWritableDb.execSQL("delete from " + TABLE_STUD_ATTENDANCE);
            }
            count = 1;
        } catch (Exception err) {
            Constants.writelog("DeleteTableRecords()1376 ", "Type:"
                    + type + " Message:" + err.getMessage() + "StactTrace:"
                    + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    // Delete
    public int deleteContactRecord(String StudId, String SchoolId) {

        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {

            String table = TABLE_CONTACTS1;
            String whereClause = "School_Id=? AND Stud_Id =?";
            String whereArgs[] = new String[]{SchoolId, StudId};
            count = myWritableDb.delete(table, whereClause, whereArgs);
        } catch (Exception err) {
            Constants.Logwrite("Database:deleteContactRecord", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;

    }

    // UpdateContactDetails
    public int UpdateContactRouteId(String StudID, String SchoolID,
                                    int RouteId, String YearId, SQLiteDatabase myWritableDb) {
        int count = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ROUTE_ID, RouteId);
            String table = TABLE_CONTACTS1;
            String whereClause = "Stud_Id=? AND School_Id =? AND yearid  =?";
            String whereArgs[] = new String[]{StudID, SchoolID, YearId};
            count = myWritableDb.update(table, values, whereClause, whereArgs);
        } catch (Exception err) {
            Constants.Logwrite("UpdateContactRouteId", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());
        } finally {
        }
        return count;
    }

    // Update Contact Details
    public int UpdateClassSectionName(String StudID, String SchoolID,
                                      String ClassSecName, String YearId, SQLiteDatabase myWritableDb) {
        int count = 0;
        try {
            /*Constants.writelog("DatabaseHandler",
                    "UpdateClassSectionName: start");*/
            Constants.Logwrite("DatabaseHandler", "UpdateClassSectionName: start");

            ContentValues values = new ContentValues();
            values.put(KEY_CLASS_SECTION_NAME, ClassSecName);
            String table = TABLE_CONTACTS1;
            String whereClause = "Stud_Id=? AND School_Id =? AND yearid  =?";
            String whereArgs[] = new String[]{StudID, SchoolID, YearId};
            count = myWritableDb.update(table, values, whereClause, whereArgs);

            Constants.writelog("DatabaseHandler",
                    "UpdateClassSectionName: End");
            Constants.Logwrite("DatabaseHandler", "UpdateClassSectionName: End");

        } catch (Exception err) {
            Constants.Logwrite("DatabaseHandler",
                    "UpdateClassSectionName  Message:" + err.getMessage()
                            + "StactTrace:" + err.getStackTrace());
            Constants.writelog("DatabaseHandler",
                    "UpdateClassSectionName  Message:" + err.getMessage()
                            + "StactTrace:" + err.getStackTrace());
        } finally {
        }
        return count;
    }

    // Delete
    public int UpdateContactRecordSetDefaultOne(String StudID, String SchoolID) {

        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_IS_DEF, 1);
            String table = TABLE_CONTACTS1;
            String whereClause = "Stud_Id=? AND School_Id =?";
            String whereArgs[] = new String[]{StudID, SchoolID};
            count = myWritableDb.update(table, values, whereClause, whereArgs);
        } catch (Exception err) {
            Constants.Logwrite("Database:UpdateContactRecordSetDefaultOne",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;

    }

    //update menu
    public int updateMenu(String StudID, String SchoolID, String menu) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            /*String[] partsMenu = menu.split(",");
            menu = "";
            for (int i = 0; i < partsMenu.length; i++) {
                String[] parts = partsMenu[i].split("@");
                if (parts != null && parts.length > 0 && parts[0].equalsIgnoreCase("isfull")) {
                    try {
                        ContentValues values = new ContentValues();
                        values.put(KEY_FULL_PAY, parts[1]);
                        String table = TABLE_CONTACTS1;
                        String whereClause = "Stud_Id=? AND School_Id =? ";
                        String whereArgs[] = new String[]{StudID, SchoolID};
                        count = myWritableDb.update(table, values, whereClause, whereArgs);
                    } catch (Exception ex) {
                        Constants.writelog("DB","UpdateMenu()1621 IsFullPay not found:"+partsMenu[i]);
                    }
                    continue;
                }
                if (i == partsMenu.length - 1) {
                    menu += partsMenu[i];
                } else {
                    menu += partsMenu[i] + ",";
                }
            }*/
            ContentValues values = new ContentValues();
            values.put(KEY_MENU, menu);
            String table = TABLE_CONTACTS1;
            String whereClause = "Stud_Id=? AND School_Id =? ";
            String whereArgs[] = new String[]{StudID, SchoolID};
            count = myWritableDb.update(table, values, whereClause, whereArgs);
        } catch (Exception err) {
            Constants.writelog("DatabaseHandler",
                    "UpdateMenu 1647 Message:" + err.getMessage()
                            + "StactTrace:" + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    public String getMenu(String StudentId, String SchoolId) {
        Cursor cursor = null;
        String menu = "";
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS1 + " where "
                + KEY_STUD_ID + "=" + StudentId + " and " + KEY_SCHOOLID + "="
                + SchoolId + "";
        try {
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                if (cursor.getString(cursor.getColumnIndex(KEY_MENU)) != null) {
                    menu = cursor.getString(cursor.getColumnIndex(KEY_MENU));
                    String[] partsMenu = menu.split(",");
                    menu = "";
                    for (int i = 0; i < partsMenu.length; i++) {
                        String[] parts = partsMenu[i].split("@");
                        if (parts != null && parts.length > 0 && parts[0].equalsIgnoreCase("isfull")) {
                            continue;
                        }
                        menu += partsMenu[i] + ",";
                    }
                }
            }
        } catch (Exception err) {
            Constants.Logwrite("db",
                    "getMenu()1670 Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return menu;
    }

    public String getIsfullPay(String StudentId, String SchoolId) {
        Cursor cursor = null;
        String isfullpay = "";
        String menu = "1";
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS1 + " where "
                + KEY_STUD_ID + "=" + StudentId + " and " + KEY_SCHOOLID + "="
                + SchoolId;
        try {
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                menu = cursor.getString(cursor
                        .getColumnIndex(KEY_MENU));
                String[] partsMenu = menu.split(",");
                for (int i = 0; i < partsMenu.length; i++) {
                    String[] parts = partsMenu[i].split("@");
                    if (parts != null && parts.length > 0 && parts[0].equalsIgnoreCase("isfull")) {
                        isfullpay = parts[1];
                    }
                }
            }
        } catch (Exception err) {
            Constants.Logwrite("db",
                    "getIsfullPay()1722 Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return isfullpay;
    }

    // Getting contacts Count Using Phone Num and login
    public int getContactsCountUsingPhoneandLogpin(String phno, String lgpn) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String countQuery = "SELECT  * FROM " + TABLE_CONTACTS1
                    + " where phone_number='" + phno + "' and logpin='" + lgpn
                    + "'";

            cursor = myWritableDb.rawQuery(countQuery, null);

        } catch (Exception err) {
            Constants.Logwrite("Database:getContactsCountUsingPhoneandLogpin", "Message:"
                    + err.getMessage() + "StactTrace:" + err.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                count = cursor.getCount();
                cursor.close();
            }
            close(myWritableDb);
        }
        return count;
    }

    // Getting contacts Count Using Student_ID Num and School_Id
    public int getContactsCountUsingStud_ID_and_School_Id(Integer Student_Id,
                                                          Integer School_Id) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            String countQuery = "SELECT  * FROM " + TABLE_CONTACTS1
                    + " where Stud_Id=" + Student_Id + " and School_Id="
                    + School_Id + "";

            Cursor cursor = myWritableDb.rawQuery(countQuery, null);

            if (cursor != null && !cursor.isClosed()) {
                count = cursor.getCount();
                cursor.close();
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:getContactsCountUsingStud_ID_and_School_Id",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    // Update Records Set Isdefault to Zero to One
    public int UpdateSetIsDefault_Zero_to_One() {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            String countQuery = "Update " + TABLE_CONTACTS1 + " set isdef=0";

            Cursor cursor = myWritableDb.rawQuery(countQuery, null);

            if (cursor != null && !cursor.isClosed()) {
                count = cursor.getCount();
                cursor.close();
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:UpdateSetIsDefault_Zero_to_One",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }


    public String getClassSectionNameFromProfile(int StudentId, int SchoolId,
                                                 String yearid) {
        Cursor cursor = null;
        String ClassSectionName = " ";
        String countQuery = "SELECT  * FROM " + TABLE_STUD_PROFILE + " where "
                + KEY_PROFILE_STUD_ID + "=" + StudentId + " and "
                + KEY_PROFILE_SCHOOL_ID + "=" + SchoolId + " and "
                + KEY_PROFILE_YEAR_ID + "=" + yearid;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                ClassSectionName = "";
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String parts[] = cursor.getString(i).split(":");
                    if (parts[0].trim().equalsIgnoreCase("class")) {
                        ClassSectionName = cursor.getString(i);
                    }
                }
            }
        } catch (Exception err) {
            Constants.Logwrite("getClassSectionNameFromProfile()1738 ",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return ClassSectionName;
    }


    public Cursor getStudentDetails(int StudentId, int SchoolId) {
        Cursor cursor = null;
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS1 + " where "
                + KEY_STUD_ID + "=" + StudentId + " and " + KEY_SCHOOLID + "="
                + SchoolId;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            cursor = myWritableDb.rawQuery(countQuery, null);
            int count = cursor.getCount();
            Log.e("countData", String.valueOf(count));

        } catch (Exception err) {
            Constants.Logwrite("getClassSectionNameFromProfile()1738 ",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        }
        return cursor;
    }

    // Get Year Id of Student
    public String GetYearId(int StudentId, int SchoolId) {
        Constants.Logwrite("Database:getDefaultAccount", "EnterMethodSuccess");
        Cursor cursor = null;
        String yearid = "";
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS1 + " where "
                + KEY_STUD_ID + "=" + StudentId + " and " + KEY_SCHOOLID + "="
                + SchoolId + "";
        try {
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String YearId = cursor.getString(cursor
                        .getColumnIndex(KEY_YEAR_ID));
                yearid = YearId;
                // cursor.close();
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:getDefaultAccount()", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return yearid;
    }

    public String getDefaultAccount() {
        Constants.Logwrite("Database:getDefaultAccount", "EnterMethodSuccess");
        Cursor cursor = null;
        String std = "";
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS1
                + " where isdef=1";
        try {
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String ph = cursor.getString(cursor
                        .getColumnIndex("phone_number"));
                String lgnpn = cursor
                        .getString(cursor.getColumnIndex("logpin"));
                String schoolid = cursor.getString(cursor
                        .getColumnIndex(KEY_SCHOOLID));
                String studid = cursor.getString(cursor
                        .getColumnIndex(KEY_STUD_ID));
                String userid = cursor.getString(cursor
                        .getColumnIndex(KEY_USER_ID));
                String yearid = cursor.getString(cursor
                        .getColumnIndex(KEY_YEAR_ID));
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                String updatedtime = cursor.getString(cursor
                        .getColumnIndex(KEY_UPDATED_TIME));
                String classid = cursor.getString(cursor
                        .getColumnIndex(KEY_CLASS_ID));
                String classsecid = cursor.getString(cursor
                        .getColumnIndex(KEY_CLASS_SEC_ID));
                String lastupdatedtime = cursor.getString(cursor
                        .getColumnIndex(KEY_LAST_UPDATED_TIME));
                String _studenrolldate = cursor.getString(cursor
                        .getColumnIndex(KEY_STUD_ENROLL_DATE));
                String _classname = cursor.getString(cursor
                        .getColumnIndex(KEY_CLASS_NAME));
                String academicyear = cursor.getString(cursor
                        .getColumnIndex(KEY_ACADEMIC_YEAR));
                int routeid = Integer.parseInt(cursor.getString(cursor
                        .getColumnIndex(KEY_ROUTE_ID)));

                std = ph + "," + lgnpn + "," + schoolid + "," + studid + ","
                        + userid + "," + yearid + "," + name + ","
                        + updatedtime + "," + classid + "," + classsecid + ","
                        + lastupdatedtime + "," + _studenrolldate + ","
                        + _classname + "," + academicyear + "," + routeid;

            }

        } catch (Exception ex) {
            Constants.Logwrite("Database:getDefaultAccount()", "Message:" + ex.getMessage()
                    + "StactTrace:" + ex.getStackTrace());
            Constants.writelog("DBHandler",
                    "getDefaultAccount() Ex:1826 " + ex.getMessage()
                            + ":::::::" + ex.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return std;
    }

    public int UpdateStudentDetails(int stdid, int schlid, String data) {

        Constants.Logwrite("UpdateStudentDetails:", "Entered in UpdateStudentDetails");
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        int count = 0;
        try {
            String[] parts = data.split(",");
            Constants.Logwrite("UpdateStudentDetails:", "StringLength:" + parts.length);
            int Logpin = 0;
            String yearid = "";
            String classid = "";
            String classsecid = "";
            String classname = "";
            String studentenrolldate = "";
            String Name = "";
            String academicyear = "";
            String lastupdatedtime = "";
            String updatedtime = "";
            int RouteId = 0;
            try {
                Logpin = Integer.parseInt(parts[0]);
                Constants.Logwrite("UpdateStudentDetails:", "Logpin:" + Logpin);
                yearid = parts[1];
                Constants.Logwrite("UpdateStudentDetails:", "YearId:" + yearid);
                classid = parts[2];
                Constants.Logwrite("UpdateStudentDetails:", "ClassId:" + classid);
                classsecid = parts[3];
                Constants.Logwrite("UpdateStudentDetails:", "ClassSecId:" + classsecid);
                classname = parts[4];
                Constants.Logwrite("UpdateStudentDetails:", "classname:" + classname);
                studentenrolldate = parts[5];
                Constants.Logwrite("UpdateStudentDetails:", "studentenrolldate:"
                        + studentenrolldate);
                Name = parts[6];
                Constants.Logwrite("UpdateStudentDetails:", "Name:" + Name);
                academicyear = parts[7];
                Constants.Logwrite("UpdateStudentDetails:", "academicyear:" + academicyear);
                lastupdatedtime = parts[12];
                Constants.Logwrite("UpdateStudentDetails:", "lastupdatedtime:"
                        + lastupdatedtime);
                updatedtime = parts[13];
                Constants.Logwrite("UpdateStudentDetails:", "updatedtime:" + updatedtime);
                RouteId = Integer.parseInt(parts[14]);
                Constants.Logwrite("UpdateStudentDetails:", "RouteId:" + RouteId);
            } catch (Exception err) {

            }
            String countQuery = "Update " + TABLE_CONTACTS1 + " set "
                    + KEY_YEAR_ID + "=" + yearid + "," + KEY_CLASS_ID + "="
                    + classid + "," + KEY_CLASS_SEC_ID + "=" + classsecid + ","
                    + KEY_CLASS_NAME + "='" + classname + "',"
                    + KEY_STUD_ENROLL_DATE + "='" + studentenrolldate + "',"
                    + KEY_NAME + "='" + Name + "'," + KEY_LOG_PIN + "="
                    + Logpin + "," + KEY_LAST_UPDATED_TIME + "='"
                    + lastupdatedtime + "'," + KEY_UPDATED_TIME + "='"
                    + updatedtime + "'," + KEY_ROUTE_ID + "=" + RouteId
                    + " where " + KEY_STUD_ID + "=" + stdid + " and "
                    + KEY_SCHOOLID + "=" + schlid + "";

            Cursor cursor = myWritableDb.rawQuery(countQuery, null);
            Constants.Logwrite("UpdateStudentDetails", "Cursor Value:" + cursor.toString());
            if (cursor != null && !cursor.isClosed()) {
                count = cursor.getCount();
                cursor.close();
            }
            Constants.Logwrite("Database:", "Cursor Count:" + count);
        } catch (Exception err) {
            Constants.Logwrite("UpdateStudentDetails", "Exception" + err.getMessage()
                    + ":::StactTrace:" + err.getStackTrace().toString());
            // return 0;
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    public String GetAccount_DetailsUsing_StudID(int StudId, int Schoolid) {
        Cursor cursor = null;
        String std = "";
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS1
                + " where Stud_Id=" + StudId + " and " + KEY_SCHOOLID + "="
                + Schoolid + "";
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            cursor = db.rawQuery(countQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String studname = cursor.getString(cursor
                        .getColumnIndex("name"));
                String ph = cursor.getString(cursor
                        .getColumnIndex("phone_number"));
                String lgnpn = cursor
                        .getString(cursor.getColumnIndex("logpin"));
                String year_id = cursor.getString(cursor
                        .getColumnIndex("yearid"));
                String School_Id = cursor.getString(cursor
                        .getColumnIndex("School_Id"));
                String Class_Id = cursor.getString(cursor
                        .getColumnIndex("classid"));
                String Class_Sec_Id = cursor.getString(cursor
                        .getColumnIndex("classsecid"));

                String User_Id = cursor.getString(cursor
                        .getColumnIndex("userid"));
                String Class_name = cursor.getString(cursor
                        .getColumnIndex("classname"));
                String studenrolldate = cursor.getString(cursor
                        .getColumnIndex("studentenrolldate"));
                String lastupdatedtime = cursor.getString(cursor
                        .getColumnIndex("lastupdatedtime"));

                String academicyr = cursor.getString(cursor
                        .getColumnIndex("acayear"));
                String classsecname = cursor.getString(cursor
                        .getColumnIndex("classsecname"));
                String Routeid = cursor.getString(cursor
                        .getColumnIndex("routeid"));

                std = ph + "," + lgnpn + "," + studname + "," + year_id + ","
                        + School_Id + "," + Class_Id + "," + Class_Sec_Id + ","
                        + User_Id + "," + Class_name + "," + studenrolldate
                        + "," + lastupdatedtime + "," + academicyr + ","
                        + classsecname + "," + Routeid;
                // cursor.close();
            }
        } catch (Exception ex) {
            Constants.writelog(
                    "DbHandler",
                    "GetAccount_DetailsUsing_StudID()2057 Ex:"
                            + ex.getMessage() + "::::::" + ex.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return std;
    }

    public String GetStudentAccountDetails(Context cn, int StudId, int Schoolid) {
        Cursor cursor = null;
        String std = "";
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS1 + " where "
                + KEY_STUD_ID + "=" + StudId + " and " + KEY_SCHOOLID + "="
                + Schoolid + "";
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                String year_id = cursor.getString(cursor
                        .getColumnIndex("yearid"));
                String School_Id = cursor.getString(cursor
                        .getColumnIndex("School_Id"));

                String StudentId = cursor.getString(cursor
                        .getColumnIndex(KEY_STUD_ID));

                String UserId = cursor.getString(cursor
                        .getColumnIndex(KEY_USER_ID));

                String Phone = cursor.getString(cursor
                        .getColumnIndex(KEY_PH_NO));

                String Pin = cursor.getString(cursor
                        .getColumnIndex(KEY_LOG_PIN));

                String ClassId = cursor.getString(cursor
                        .getColumnIndex(KEY_CLASS_ID));

                String ClassSecId = cursor.getString(cursor
                        .getColumnIndex(KEY_CLASS_SEC_ID));

                String Name = cursor.getString(cursor.getColumnIndex(KEY_NAME));

                String AcaYear = cursor.getString(cursor
                        .getColumnIndex(KEY_ACADEMIC_YEAR));

                String lastupdatedtime = cursor.getString(cursor
                        .getColumnIndex(KEY_LAST_UPDATED_TIME));

                String updatedtime = cursor.getString(cursor
                        .getColumnIndex(KEY_UPDATED_TIME));

                String studenrolldate = cursor.getString(cursor
                        .getColumnIndex(KEY_STUD_ENROLL_DATE));
                int routeid = 0;
                try {
                    routeid = Integer.parseInt(cursor.getString(cursor
                            .getColumnIndex(KEY_ROUTE_ID)));
                    Datastorage.SetRouteId(cn, routeid);
                } catch (Exception ex) {
                    Constants.writelog("DB", "GetStudentAccountDetails()2384 MSG:" + ex.getMessage());
                }
                std = year_id + "," + School_Id + "," + StudentId + ","
                        + UserId + "," + Phone + "," + Pin + "," + ClassId
                        + "," + ClassSecId + "," + Name + "," + AcaYear + ","
                        + lastupdatedtime + "," + updatedtime + ","
                        + studenrolldate + "," + routeid;
                // cursor.close();
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:GetStudentAccountDetails",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return std;

    }

    public String GetStudentProfileDetails(int StudId, int Schoolid, int Yearid) {
        Cursor cursor = null;
        String std = "";
        String countQuery = "SELECT  * FROM " + TABLE_STUD_PROFILE + " where "
                + KEY_PROFILE_STUD_ID + "=" + StudId + " and "
                + KEY_PROFILE_SCHOOL_ID + "=" + Schoolid + " and "
                + KEY_PROFILE_YEAR_ID + "=" + Yearid;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String grno = cursor.getString(cursor
                        .getColumnIndex(KEY_PROFILE_GRNO));
                String name = cursor.getString(cursor
                        .getColumnIndex(KEY_PROFILE_NAME));
                String Classname = cursor.getString(cursor
                        .getColumnIndex(KEY_PROFILE_CLASS));
                String Caste = cursor.getString(cursor
                        .getColumnIndex(KEY_PROFILE_CASTE));
                String Category = cursor.getString(cursor
                        .getColumnIndex(KEY_PROFILE_CATEGORY));
                String DOB = cursor.getString(cursor
                        .getColumnIndex(KEY_PROFILE_DOB));
                String ContactNumber = cursor.getString(cursor
                        .getColumnIndex(KEY_PROFILE_CONTACT_NUMBER));
                String imagepath = cursor.getString(cursor
                        .getColumnIndex(KEY_PROFILE_IMAGE));
                std = grno + "," + name + "," + Classname + "," + Caste + ","
                        + Category + "," + DOB + "," + ContactNumber + ","
                        + imagepath;
            } else {
                std = "0";
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:GetStudentProfileDetails2103 ",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return std;
    }

    public void AddStudentProfileDetails(int studid, int schoolid, int yearid,
                                         String grno, String name, String Classname, String Caste,
                                         String Category, String DOB, String ContactNumber, String imagepath) {

        SQLiteDatabase myWritableDb = null;
        try {

            myWritableDb = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_PROFILE_STUD_ID, studid); // MsgId
            values.put(KEY_PROFILE_SCHOOL_ID, schoolid); // Msgtext
            values.put(KEY_PROFILE_YEAR_ID, yearid);
            values.put(KEY_PROFILE_GRNO, grno);
            values.put(KEY_PROFILE_NAME, name);
            values.put(KEY_PROFILE_CLASS, Classname);
            values.put(KEY_PROFILE_CASTE, Caste);
            values.put(KEY_PROFILE_CATEGORY, Category);
            values.put(KEY_PROFILE_DOB, DOB);
            values.put(KEY_PROFILE_CONTACT_NUMBER, ContactNumber);
            values.put(KEY_PROFILE_IMAGE, imagepath);

            // Inserting Row
            myWritableDb.insert(TABLE_STUD_PROFILE, null, values);
        } catch (Exception err) {
            Constants.writelog("DatabaseHandler",
                    "AddStudentProfile() 2167 Message:" + err.getMessage()
                            + "StactTrace:" + err.getStackTrace());
            Constants.Logwrite("Database:AddStudentProfile() 2167",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
    }

    public int updateStudProfile(int studid, int schoolid, int yearid,
                                 String grno, String name, String Classname, String Caste,
                                 String Category, String DOB, String ContactNumber, String imagepath) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_PROFILE_GRNO, grno);
            values.put(KEY_PROFILE_NAME, name);
            values.put(KEY_PROFILE_CLASS, Classname);
            values.put(KEY_PROFILE_CASTE, Caste);
            values.put(KEY_PROFILE_CATEGORY, Category);
            values.put(KEY_PROFILE_DOB, DOB);
            values.put(KEY_PROFILE_CONTACT_NUMBER, ContactNumber);
            values.put(KEY_PROFILE_IMAGE, imagepath);
            String whereClause = "" + KEY_PROFILE_STUD_ID + "=? AND "
                    + KEY_PROFILE_SCHOOL_ID + " =? AND " + KEY_PROFILE_YEAR_ID
                    + " =?";
            String whereArgs[] = new String[]{String.valueOf(studid),
                    String.valueOf(schoolid), String.valueOf(yearid)};
            count = myWritableDb.update(TABLE_STUD_PROFILE, values,
                    whereClause, whereArgs);
        } catch (Exception err) {
            Constants.Logwrite("Database:UpdateStudentProfile ",
                    "Exception 2180 :" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    public int updateStudProfile(int studid, int schoolid, int yearid,
                                 String grno, String name, String Classname, String Caste,
                                 String Category, String DOB, String ContactNumber) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_PROFILE_GRNO, grno);
            values.put(KEY_PROFILE_NAME, name);
            values.put(KEY_PROFILE_CLASS, Classname);
            values.put(KEY_PROFILE_CASTE, Caste);
            values.put(KEY_PROFILE_CATEGORY, Category);
            values.put(KEY_PROFILE_DOB, DOB);
            values.put(KEY_PROFILE_CONTACT_NUMBER, ContactNumber);
            String whereClause = "" + KEY_PROFILE_STUD_ID + "=? AND "
                    + KEY_PROFILE_SCHOOL_ID + " =? AND " + KEY_PROFILE_YEAR_ID
                    + " =?";
            String whereArgs[] = new String[]{String.valueOf(studid),
                    String.valueOf(schoolid), String.valueOf(yearid)};
            count = myWritableDb.update(TABLE_STUD_PROFILE, values,
                    whereClause, whereArgs);
        } catch (Exception err) {
            Constants.Logwrite("Database:UpdateStudentProfile ",
                    "Exception 2180 :" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    public int deleteStudProfileDetails(String studid, String schoolid) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            String whereClause = "" + KEY_PROFILE_STUD_ID + "=? AND "
                    + KEY_PROFILE_SCHOOL_ID + " =?";
            String whereArgs[] = new String[]{studid, schoolid};
            count = myWritableDb.delete(TABLE_STUD_PROFILE, whereClause,
                    whereArgs);
        } catch (Exception err) {

        } finally {
            close(myWritableDb);
        }
        return count;
    }

    public List<Contact> getAlbumDetailsGrid(int StudId, int SchoolId, int limit) {
        List<Contact> contactList = new ArrayList<Contact>();
        Cursor cursor = null;
        SQLiteDatabase myWritableDb = null;
        try {
            myWritableDb = this.getWritableDatabase();
            String condition = "";

            String selectQuery = "SELECT DISTINCT " + KEY_ALBUM_ALBUMNAME + ","
                    + KEY_ALBUM_ALBUMURL + ","
                    + KEY_ALBUM_STUDID + ","
                    + KEY_ALBUM_ALBUMID + ","
                    + KEY_ALBUM_PHOTOFILENAME + ","
                    + KEY_ALBUM_DATETIME + " FROM "
                    + TABLE_STUDENT_ALBUM_DETAILS + " where "
                    + KEY_ALBUM_STUDID + "=" + StudId + " and "
                    + KEY_ALBUM_SCHOOLID + "=" + SchoolId + " ORDER BY "
                    + KEY_ALBUM_DATETICKS + " DESC";
            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(selectQuery, null);
            String check = "";
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    String AlbumName = cursor.getString(0);
                    String PhotoFileName = cursor.getString(1);
                    int AlbumStudId = Integer.parseInt(cursor.getString(2));
                    int AlbumId = Integer.parseInt(cursor.getString(3));
                    String Albumurl = cursor.getString(4);
                    String AlbumDate = cursor.getString(5);

                    if (!check.contains(String.valueOf(AlbumId))) {
                        check += String.valueOf(AlbumId) + ",";
                        contact.SetSchoolId(AlbumStudId);
                        contact.setAlbumId(AlbumId);
                        contact.setAlbumName(AlbumName);
                        contact.setAlbumurl(Albumurl);
                        contact.setAlbumPhotofile(PhotoFileName);
                        contact.setAlbumDatetime(AlbumDate);
                        // Adding contact to list
                        contactList.add(contact);
                        if (limit != 0) {
                            if (check.split(",").length == limit) {
                                break;
                            }
                        }
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:getAlbumDetails", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    public List<Contact> getSpecificAlbumPhotos(int StudId, int SchoolId, int AlbumId) {
        List<Contact> contactList = new ArrayList<Contact>();
        Cursor cursor = null;
        SQLiteDatabase myWritableDb = null;
        try {
            myWritableDb = this.getWritableDatabase();
            String condition = "";

            String selectQuery = "SELECT DISTINCT " + KEY_ALBUM_ALBUMNAME + ","
                    + KEY_ALBUM_ALBUMURL + ","
                    + KEY_ALBUM_STUDID + ","
                    + KEY_ALBUM_ALBUMID + ","
                    + KEY_ALBUM_PHOTOFILENAME + ","
                    + KEY_ALBUM_DATETIME + " FROM "
                    + TABLE_STUDENT_ALBUM_DETAILS + " where "
                    + KEY_ALBUM_STUDID + "=" + StudId + " and "
                    + KEY_ALBUM_SCHOOLID + "=" + SchoolId
                    + " and "
                    + KEY_ALBUM_ALBUMID + "=" + AlbumId;
            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(selectQuery, null);
            String check = "";
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    String AlbumName = cursor.getString(0);
                    String PhotoFileName = cursor.getString(1);
                    int AlbumStudId = Integer.parseInt(cursor.getString(2));
                    int AlbumIdDb = Integer.parseInt(cursor.getString(3));
                    String Albumurl = cursor.getString(4);
                    String AlbumDate = cursor.getString(5);
                    contact.SetSchoolId(AlbumStudId);
                    contact.setAlbumId(AlbumIdDb);
                    contact.setAlbumName(AlbumName);
                    contact.setAlbumurl(Albumurl);
                    contact.setAlbumPhotofile(PhotoFileName);
                    contact.setAlbumDatetime(AlbumDate);
                    // Adding contact to list
                    contactList.add(contact);

                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:getSpecificAlbumPhotos:2559", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    public String GetStudAttendanceDetails(int StudId, int Schoolid, int Yearid) {
        Cursor cursor = null;
        String std = "";
        String countQuery = "SELECT " + KEY_ATT_DETAILS + " FROM "
                + TABLE_STUD_ATTENDANCE + " where " + KEY_ATT_STUD_ID + "="
                + StudId + " and " + KEY_ATT_SCHOOL_ID + "=" + Schoolid
                + " and " + KEY_ATT_YEAR_ID + "=" + Yearid;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                std = cursor.getString(cursor.getColumnIndex(KEY_ATT_DETAILS));
            } else {
                std = "0";
            }
        } catch (Exception err) {
            Constants.Logwrite("Database: GetStuAttendanceDetails 2273",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return std;
    }

    // Methods of activityCount created  by vijay solanki 27-02-2020

    public void addActivityVisitedCount(String studid, String schoolid, String ActivityName) {
        SQLiteDatabase myWritableDb = null;
        try {
            String date = "";
            date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            myWritableDb = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ACTIVITY_VISITED_STUD_ID, studid); // MsgId
            values.put(KEY_ACTIVITY_VISITED_SCHOOL_ID, schoolid); // Msgtext
            values.put(KEY_ACTIVITY_VISITED_NAME, ActivityName);
            values.put(KEY_ACTIVITY_VISITED_COUNT, 1);
            values.put(KEY_ACTIVITY_VISITED_DATE, date);
            // Inserting Row
            myWritableDb.insert(TABLE_ACTIVITY_VISITED, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean ifExists(String Value, String studId, String Schoolid) {
        boolean exists = false;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = null;
            String date = "";
            date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            String checkQuery = "SELECT " + KEY_ACTIVITY_VISITED_NAME + " FROM "
                    + TABLE_ACTIVITY_VISITED + " WHERE " + KEY_ACTIVITY_VISITED_NAME + "= '" + Value + "'"
                    + " and " + KEY_ACTIVITY_VISITED_STUD_ID + "='" + studId + "'"
                    + " and " + KEY_ACTIVITY_VISITED_SCHOOL_ID + "='" + Schoolid + "'"
                    + " and " + KEY_ACTIVITY_VISITED_DATE + "='" + date + "'";
            Common.showLog("IfExits 2567 :", checkQuery);
            cursor = db.rawQuery(checkQuery, null);
            exists = (cursor.getCount() > 0);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }

    public boolean deleteActivityCountTable() {
        boolean isSetSezo = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            //  db.execSQL("UPDATE " + TABLE_ACTIVITY_VISITED + " SET " + KEY_ACTIVITY_VISITED_COUNT + " =0" );
            db.execSQL("delete from " + TABLE_ACTIVITY_VISITED);
            Common.showLog("DeleteQuery 2597", "delete from " + TABLE_ACTIVITY_VISITED);
            isSetSezo = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSetSezo;
    }

    public boolean updateActivityCount(String activtyName, String StudId, String Schoolid) {
        boolean isUpdate = false;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            isUpdate = false;
            String value = "";
            int count = 0;
            Cursor cursor = null;
            String date = "";
            date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String checkQuery = "SELECT " + KEY_ACTIVITY_VISITED_COUNT + " FROM " + TABLE_ACTIVITY_VISITED
                    + " WHERE " + KEY_ACTIVITY_VISITED_NAME + "= '" + activtyName + "'"
                    + " and " + KEY_ACTIVITY_VISITED_STUD_ID + "='" + StudId + "'"
                    + " and " + KEY_ACTIVITY_VISITED_SCHOOL_ID + "='" + Schoolid + "'"
                    + " and " + KEY_ACTIVITY_VISITED_DATE + "='" + date + "'";
            Common.showLog("updateActivityCount 2620 :", checkQuery);
            cursor = db.rawQuery(checkQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                value = cursor.getString(cursor
                        .getColumnIndex(KEY_ACTIVITY_VISITED_COUNT));
            }
            if (value != null && !value.isEmpty()) {
                count = Integer.parseInt(value);
                count++;
            }
            cursor.close();
            ContentValues newValues = new ContentValues();
            newValues.put(KEY_ACTIVITY_VISITED_COUNT, count);
            db.update(TABLE_ACTIVITY_VISITED, newValues, KEY_ACTIVITY_VISITED_NAME + "='" + activtyName
                    + "' and " + KEY_ACTIVITY_VISITED_STUD_ID + "='" + StudId + "'"
                    + " and " + KEY_ACTIVITY_VISITED_SCHOOL_ID + "='" + Schoolid + "'"
                    + " and " + KEY_ACTIVITY_VISITED_DATE + "='" + date + "'", null);

            isUpdate = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpdate;
    }

    public List<ActivityVisitedModel> getActivityVisitedList() {
        List<ActivityVisitedModel> visiTedList = null;
        try {
            visiTedList = new ArrayList<ActivityVisitedModel>();
            String selectQuery = "SELECT  * FROM " + TABLE_ACTIVITY_VISITED;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    ActivityVisitedModel contact = new ActivityVisitedModel();
                    contact.setStudiD(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_VISITED_STUD_ID)));
                    contact.setSchoolId(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_VISITED_SCHOOL_ID)));
                    contact.setActivityName(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_VISITED_NAME)));
                    contact.setCount(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_VISITED_COUNT)));
                    contact.setDateVisited(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_VISITED_DATE)));
                    visiTedList.add(contact);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return visiTedList;
    }


    public void AddStudAttendanceDetails(int studid, int schoolid, int yearid,
                                         String details) {
        SQLiteDatabase myWritableDb = null;
        try {
            //Constants.writelog("AddStudAttendanceDetails()2462", "Details:"+details);
            myWritableDb = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ATT_STUD_ID, studid); // MsgId
            values.put(KEY_ATT_SCHOOL_ID, schoolid); // Msgtext
            values.put(KEY_ATT_YEAR_ID, yearid);
            values.put(KEY_ATT_DETAILS, details);

            // Inserting Row
            myWritableDb.insert(TABLE_STUD_ATTENDANCE, null, values);
        } catch (Exception err) {
            Constants.writelog("DatabaseHandler",
                    "AddStudentProfile() 2167 Message:" + err.getMessage()
                            + "StactTrace:" + err.getStackTrace());
            Constants.Logwrite("Database:AddStudentProfile() 2167",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
    }

    public int updateStudAttendanceDetails(int studid, int schoolid,
                                           int yearid, String details) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ATT_DETAILS, details);
            String whereClause = "" + KEY_ATT_STUD_ID + "=? AND "
                    + KEY_ATT_SCHOOL_ID + " =? AND " + KEY_ATT_YEAR_ID + " =?";
            String whereArgs[] = new String[]{String.valueOf(studid),
                    String.valueOf(schoolid), String.valueOf(yearid)};
            count = myWritableDb.update(TABLE_STUD_ATTENDANCE, values,
                    whereClause, whereArgs);
        } catch (Exception err) {
            Constants.Logwrite("Database:UpdateAttendanceDetails ",
                    "Exception 2325 :" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    public int deleteStudAttendanceDetails(String studid, String schoolid) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            String whereClause = "" + KEY_ATT_STUD_ID + "=? AND "
                    + KEY_ATT_SCHOOL_ID + " =?";
            String whereArgs[] = new String[]{studid, schoolid};
            count = myWritableDb.delete(TABLE_STUD_ATTENDANCE, whereClause,
                    whereArgs);
        } catch (Exception err) {

        } finally {
            close(myWritableDb);
        }
        return count;
    }

    public String GetStudentDetailUsingPhoneAndPin(String Phone, int Pin) {
        Cursor cursor = null;
        String std = "";
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS1 + " where "
                + KEY_PH_NO + "='" + Phone + "' and " + KEY_LOG_PIN + "=" + Pin
                + "";
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {

            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String School_Id = cursor.getString(cursor
                        .getColumnIndex("School_Id"));
                String Stud_Id = cursor.getString(cursor
                        .getColumnIndex("Stud_Id"));
                String Yearid = cursor.getString(cursor
                        .getColumnIndex("yearid"));
                std = School_Id + "," + Stud_Id + "," + Yearid;
                // cursor.close();
            }

        } catch (Exception err) {
            Constants.Logwrite("Database:GetStudentDetailUsingPhoneAndPin",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return std;
    }

    // Update Contact Logpin
    public int UpdatePin(String SchoolId, String StudId, String LogPin) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            Constants.Logwrite("DatabaseHandler:UpdatePin", "Details:SchoolId:::" + SchoolId
                    + "StudentId:" + StudId + ":::LogPin:" + LogPin);

            ContentValues values = new ContentValues();
            values.put(KEY_LOG_PIN, LogPin);
            String table = TABLE_CONTACTS1;
            String whereClause = "" + KEY_SCHOOLID + "=? AND " + KEY_STUD_ID
                    + " =?";
            String whereArgs[] = new String[]{SchoolId, StudId};
            count = myWritableDb.update(table, values, whereClause, whereArgs);
        } catch (Exception err) {
            Constants.Logwrite("Database:UpdatePin", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    //
    // *************************************************************************************************************
    // All Methods of SMS Retrival and Insert
    // Adding SMS in SMSTable
    public void AddSMS(Contact contact) {
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_MSGID, contact.getSMSID());
            values.put(KEY_MSGTEXT, contact.getSMSText());
            values.put(KEY_SMS_STUDID, contact.getSMSStudid());
            values.put(KEY_SMS_SCHOOLID, contact.getSMSSchoolId());
            values.put(KEY_SMS_DAY, contact.getSMSDAY());
            values.put(KEY_SMS_MONTH, contact.getSMSMONTH());
            values.put(KEY_SMS_YEAR, contact.getSMSYEAR());
            values.put(KEY_SMS_Moduleid, contact.getSMSMODULEID());
            values.put(KEY_SMS_YEAR_ID, contact.getSMSYEARID());
            myWritableDb.insert(TABLE_SMS2, null, values);
        } catch (Exception err) {
            Constants.writelog(
                    "DatabaseHandler",
                    "ADDSMS() Exception:1807" + err.getMessage());

        } finally {
            close(myWritableDb);
        }
    }

    public void Updatesms(Contact contact) {
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            // values.put(KEY_MSGID, contact.getSMSID()); // MsgId
            values.put(KEY_MSGTEXT, contact.getSMSText());
            values.put(KEY_SMS_STUDID, contact.getSMSStudid());
            values.put(KEY_SMS_SCHOOLID, contact.getSMSSchoolId());
            values.put(KEY_SMS_DAY, contact.getSMSDAY());
            values.put(KEY_SMS_MONTH, contact.getSMSMONTH());
            values.put(KEY_SMS_YEAR, contact.getSMSYEAR());
            values.put(KEY_SMS_Moduleid, contact.getSMSMODULEID());
            values.put(KEY_SMS_YEAR_ID, contact.getSMSYEARID());

            String whereClause = "" + KEY_MSGID + "=?";
            String whereArgs[] = new String[]{String.valueOf(contact
                    .getSMSID())};
            // Update Row
            myWritableDb.update(TABLE_SMS2, values, whereClause, whereArgs);

        } catch (Exception err) {
            Constants.Logwrite("DatabaseHandler",
                    "UpdateSMS Exception:1850" + err.getMessage()
                            + "StactTrace:" + err.getStackTrace());
            Constants.writelog("DatabaseHandler",
                    "UpdateSMS() Exception:1853" + err.getMessage() + "::::"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
    }

    void AddSMS(Contact contact, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_MSGID, contact.getSMSID()); // MsgId
            values.put(KEY_MSGTEXT, contact.getSMSText()); // Msgtext
            values.put(KEY_SMS_STUDID, contact.getSMSStudid());
            values.put(KEY_SMS_SCHOOLID, contact.getSMSSchoolId());
            values.put(KEY_SMS_DAY, contact.getSMSDAY());
            values.put(KEY_SMS_MONTH, contact.getSMSMONTH());
            values.put(KEY_SMS_YEAR, contact.getSMSYEAR());
            values.put(KEY_SMS_Moduleid, contact.getSMSMODULEID());
            values.put(KEY_SMS_YEAR_ID, contact.getSMSYEARID());
            // Inserting Row
            db.insert(TABLE_SMS2, null, values);
        } catch (Exception err) {
            Constants.writelog("DatabaseHandler",
                    "AddSMS() 1835 Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
            Constants.Logwrite("Database:AddSMS", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());
        } finally {
        }
    }

    // getSMSCount()
    // Getting SMSCount
    public int getSMSCount(int studid, int schoolid) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            String countQuery = "SELECT  * FROM " + TABLE_SMS2 + " where "
                    + KEY_SMS_STUDID + "=" + studid + " and "
                    + KEY_SMS_SCHOOLID + "=" + schoolid + "";

            Cursor cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed()) {
                count = cursor.getCount();
                cursor.close();
            }
            return count;
        } catch (Exception err) {
            Constants.Logwrite("Database:GetPhoneAndPin", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    // GetLatest ID of SMSTABLE
    // Getting contacts Count
    public int GetLatestSMSID(int studid, int schoolid, int yearId) {
        int Latest_SMS_ID = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String countQuery = "SELECT  * FROM " + TABLE_SMS2 + " where "
                    + KEY_SMS_STUDID + "=" + studid + " and "
                    + KEY_SMS_SCHOOLID + "=" + schoolid + " and "
                    + KEY_SMS_YEAR_ID + "=" + yearId + " ORDER BY " + KEY_MSGID
                    + " DESC  LIMIT 1";

            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
                cursor.moveToFirst();
                Latest_SMS_ID = Integer.parseInt(cursor.getString(1));
            }
        } catch (Exception err) {
            Constants.writelog("DatabaseHandler",
                    "GetLatestSMSID() 1936 Exception" + err.getMessage()
                            + "::::" + err.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return Latest_SMS_ID;
    }

    // Getting Message From Message Id
    public List<Contact> GetMessageDetailsFromMessageId(int studid,
                                                        int schoolid, int MsgId) {
        List<Contact> contactList = new ArrayList<Contact>();
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String countQuery = "SELECT  * FROM " + TABLE_SMS2 + " where "
                    + KEY_SMS_STUDID + "=" + studid + " and "
                    + KEY_SMS_SCHOOLID + "=" + schoolid + " and " + KEY_MSGID
                    + " = " + MsgId + "";
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor.moveToFirst()) {

                do {
                    Contact contact = new Contact();
                    contact.setGlobalText(cursor.getString(0) + "##@@"
                            + cursor.getString(1) + "##@@" + cursor.getCount());
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }

        } catch (Exception err) {
            Constants.writelog("db", "GetMessageDetailsFromMessageId()2730 Ex: ");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    // Delete Messages Form Message Id
    public int DeleteMessageRecord(String smdid, String msgid) {

        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            String table = TABLE_SMS2;
            String whereClause = "" + KEY_SMSID + "=? AND " + KEY_MSGID + " =?";
            String whereArgs[] = new String[]{smdid, msgid};
            count = myWritableDb.delete(table, whereClause, whereArgs);
        } catch (Exception err) {

        } finally {
            close(myWritableDb);
        }
        return count;
    }

    // Getting contacts Count
    public boolean CheckAbsentMessageInsertorNot(int studid, int schoolid,
                                                 int Day, int Month, int Year, int ModId) {
        boolean isthere = true;
        int cnt = 0;
        Cursor cursor = null;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            String countQuery = "SELECT  * FROM " + TABLE_SMS2 + " where "
                    + KEY_SMS_STUDID + "=" + studid + " and "
                    + KEY_SMS_SCHOOLID + "=" + schoolid + " and " + KEY_SMS_DAY
                    + "" + "=" + Day + " and " + KEY_SMS_MONTH + "=" + Month
                    + " and " + KEY_SMS_YEAR + "=" + Year + " and "
                    + KEY_SMS_Moduleid + "=" + ModId + "";

            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed()) {
                cursor.moveToFirst();
                cnt = cursor.getCount();
                if (cnt > 0) {
                    isthere = true;
                } else {
                    isthere = false;
                }
                cursor.close();
            } else {
                isthere = false;
            }

        } catch (Exception err) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return isthere;
    }

    public boolean CheckAbsentDateInsertedOrNot(int studid, int schoolid,
                                                int day, int month, int year) {
        boolean isInserted = true;
        int cnt = 0;
        Cursor cursor = null;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            String countQuery = "SELECT  * FROM " + TABLE_SMS2 + " where "
                    + KEY_SMS_STUDID + "=" + studid + " and "
                    + KEY_SMS_SCHOOLID + "=" + schoolid + " and "
                    + KEY_SMS_YEAR + "" + "=" + year + " and " + KEY_SMS_MONTH
                    + "=" + month + " and " + KEY_SMS_DAY + "=" + day + " and "
                    + KEY_SMS_Moduleid + "=" + 5;

            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed()) {
                cursor.moveToFirst();
                cnt = cursor.getCount();
                if (cnt > 0) {
                    isInserted = true;
                } else {
                    isInserted = false;
                }
                cursor.close();
            } else {
                isInserted = false;
            }
        } catch (Exception ex) {
            Constants.writelog(
                    "DatabseHandler",
                    "Exception 2018:" + ex.getMessage() + "::::"
                            + ex.getStackTrace());
        }
        return isInserted;
    }

    // Getting contacts Count
    public boolean CheckMessageInsertorNot(int studid, int schoolid, int MsgId) {
        boolean isthere = true;
        int cnt = 0;
        Cursor cursor = null;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            String countQuery = "SELECT  * FROM " + TABLE_SMS2 + " where "
                    + KEY_SMS_STUDID + "=" + studid + " and "
                    + KEY_SMS_SCHOOLID + "=" + schoolid + " and " + KEY_MSGID
                    + "" + "=" + MsgId + "";

            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed()) {
                cursor.moveToFirst();
                cnt = cursor.getCount();
                if (cnt > 0) {
                    isthere = true;
                } else {
                    isthere = false;
                }
                cursor.close();
            } else {
                isthere = false;
            }

        } catch (Exception err) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return isthere;
    }

    // Getting contacts Count
    public boolean CheckMessageInsertorNot(int studid, int schoolid, int MsgId,
                                           SQLiteDatabase db) {
        boolean isthere = true;
        int cnt = 0;
        Cursor cursor = null;
        try {
            String countQuery = "SELECT  * FROM " + TABLE_SMS2 + " where "
                    + KEY_SMS_STUDID + "=" + studid + " and "
                    + KEY_SMS_SCHOOLID + "=" + schoolid + " and " + KEY_MSGID
                    + "" + "=" + MsgId + "";

            cursor = db.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed()) {
                cursor.moveToFirst();
                cnt = cursor.getCount();
                if (cnt > 0) {
                    isthere = true;
                } else {
                    isthere = false;
                }
                cursor.close();
            } else {
                isthere = false;
            }
        } catch (Exception ex) {
            Constants.writelog(
                    "DatabseHandler",
                    "CheckMessageInsertorNot()2903 Exception:" + ex.getMessage() + "::::"
                            + ex.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return isthere;
    }

    // Getting All Contacts
    public List<Contact> GetAllSMSDetails(int studid, int schoolid) {
        List<Contact> contactList = new ArrayList<Contact>();
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_SMS2 + " where "
                    + KEY_SMS_STUDID + "=" + studid + " and "
                    + KEY_SMS_SCHOOLID + "=" + schoolid + " ORDER BY "
                    + KEY_MSGID + " DESC";
            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.setSMSID(Integer.parseInt(cursor.getString(1)));
                    String aa = cursor.getString(2);
                    contact.setSMSText(cursor.getString(2));
                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:", "GetAllSMSDetails" + err.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    // Getting All Circular details

    public List<Contact> GetAllCircularDetails(int studid, int schoolid,
                                               int yearid, int limit) {
        List<Contact> contactList = new ArrayList<Contact>();
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String condition = "";
            if (limit != 0) {
                condition = " limit " + limit;
            }
            String selectQuery = "SELECT  * FROM " + TABLE_CIRCULAR_DETAILS
                    + " where " + KEY_SMS_STUDID + "=" + studid + " and "
                    + KEY_SMS_SCHOOLID + "=" + schoolid + " and "
                    + KEY_CIRCULAR_YEARID + "=" + yearid + " ORDER BY "
                    + KEY_CIRCULARID + " DESC" + condition;
            cursor = myWritableDb.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.setcirstudid(Integer.parseInt(cursor.getString(1)));
                    contact.setcirschoolid(Integer.parseInt(cursor.getString(2)));
                    contact.setciryearid(Integer.parseInt(cursor.getString(3)));
                    contact.setcirgroupid(Integer.parseInt(cursor.getString(4)));
                    contact.setcirGroupname(cursor.getString(5));
                    contact.setCirId(Integer.parseInt(cursor.getString(6)));
                    contact.setCirName(cursor.getString(7));
                    contact.setcirDateText(cursor.getString(8));
                    contact.setcirPath(cursor.getString(9));
                    contact.setcirTicks(cursor.getString(10));
                    contact.setcirisdeleted(Integer.parseInt(cursor
                            .getString(11)));
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:", "GetAllSMSDetails" + err.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    // Getting All Contacts
    public List<Contact> GetAllSMSDetails() {
        List<Contact> contactList = new ArrayList<Contact>();
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_SMS1 + "";
            cursor = myWritableDb.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    // contact.setSMSID(Integer.parseInt(cursor.getString(0)));
                    contact.setSMSID(Integer.parseInt(cursor.getString(1)));
                    contact.setSMSText(cursor.getString(2));
                    contact.setSMSStudid(Integer.parseInt(cursor.getString(3)));
                    contact.setSMSSchoolId(Integer.parseInt(cursor.getString(4)));
                    contact.setSMSDAY(Integer.parseInt(cursor.getString(5)));
                    contact.setSMSMONTH(Integer.parseInt(cursor.getString(6)));
                    contact.setSMSYEAR(Integer.parseInt(cursor.getString(7)));
                    contact.setSMSMODULEID(Integer.parseInt(cursor.getString(8)));
                    contact.setSMSYEARID(Integer.parseInt(cursor.getString(9)));
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:", "GetAllSMSDetails" + err.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    // DeleteAll Old SMS From Table
    public int DeleteAllOldSMS() {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            myWritableDb.execSQL("delete from " + TABLE_SMS1);
            count = 1;
        } catch (Exception err) {
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    // DeleteAll Old SMS From Table
    public int DeleteOldSMSTable() {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            myWritableDb.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS1);
            count = 1;
        } catch (Exception err) {

        } finally {
            close(myWritableDb);
        }
        return count;
    }

    // DeleteAll Old SMS From Table
    public int DeleteStudentSMS(int studid, int schoolid) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            myWritableDb.execSQL("delete from " + TABLE_SMS2 + " where "
                    + KEY_SMS_STUDID + "=" + studid + " and "
                    + KEY_SMS_SCHOOLID + "=" + schoolid + "");
            count = 1;
        } catch (Exception err) {
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    // Getting All Contacts
    public List<Contact> GetAllSMSDetails(int studid, int schoolid, int yearid, int limit) {
        List<Contact> contactList = new ArrayList<Contact>();
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String condition = "";
            if (limit != 0) {
                condition = " limit " + limit;
            }
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_SMS2 + " where "
                    + KEY_SMS_STUDID + "=" + studid + " and "
                    + KEY_SMS_SCHOOLID + "=" + schoolid + " and "
                    + KEY_SMS_YEAR_ID + "=" + yearid + " and "
                    + KEY_SMS_Moduleid + "=0 ORDER BY " + KEY_MSGID + " DESC" + condition;
            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.setSMSID(Integer.parseInt(cursor.getString(1)));
                    String aa = cursor.getString(2);
                    contact.setSMSText(cursor.getString(2));
                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:", "GetAllSMSDetails" + err.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    // Get MonthList Of Message
    public List<Contact> GetMessageMonthList(int studid, int schoolid,
                                             int YearId) {
        List<Contact> contactList = new ArrayList<Contact>();
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {

            String[] Monthlist = {"January", "February", "March", "April",
                    "May", "June", "July", "August", "September", "October",
                    "November", "December"};
            // Select All Query
            String countQuery = "Select " + KEY_SMS_MONTH + ",Count("
                    + KEY_MSGTEXT + ") AS MessageCount," + KEY_SMS_YEAR
                    + " from " + TABLE_SMS2 + " where " + KEY_SMS_STUDID + "="
                    + studid + " and " + KEY_SMS_SCHOOLID + "=" + schoolid
                    + " and " + KEY_SMS_YEAR_ID + "=" + YearId + " and "
                    + KEY_SMS_Moduleid + "=0 GROUP BY " + KEY_SMS_MONTH
                    + " ORDER BY " + KEY_SMS_MONTH + " DESC";
            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(countQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    int mnth = Integer.parseInt(cursor.getString(0));
                    int msgcount = Integer.parseInt(cursor.getString(1));
                    int smsyear = Integer.parseInt(cursor.getString(2));
                    String monthname = Monthlist[mnth - 1];
                    contact.setSMSText(monthname + "," + mnth + "," + smsyear
                            + "," + msgcount);
                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
            // return contact list
        } catch (Exception err) {
            Constants.Logwrite("Database:", "// Get MonthList Of Message" + err.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    // Get MonthList Of Lession Diary Message
    public List<Contact> GetLessionDiaryMessageMonthList(int studid,
                                                         int schoolid, int YearId, int ModuleId) {
        List<Contact> contactList = new ArrayList<Contact>();
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String[] Monthlist = {"January", "February", "March", "April",
                    "May", "June", "July", "August", "September", "October",
                    "November", "December"};
            String countQuery = "Select " + KEY_SMS_MONTH + ",Count("
                    + KEY_MSGTEXT + ") AS MessageCount," + KEY_SMS_YEAR
                    + " from " + TABLE_SMS2 + " where " + KEY_SMS_STUDID + "="
                    + studid + " and " + KEY_SMS_SCHOOLID + "=" + schoolid
                    + " and " + KEY_SMS_YEAR_ID + "=" + YearId + " and "
                    + KEY_SMS_Moduleid + "=" + ModuleId + " GROUP BY "
                    + KEY_SMS_MONTH + " ORDER BY " + KEY_SMS_MONTH + " DESC";
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    int mnth = Integer.parseInt(cursor.getString(0));
                    int msgcount = Integer.parseInt(cursor.getString(1));
                    int smsyear = Integer.parseInt(cursor.getString(2));
                    String monthname = Monthlist[mnth - 1];
                    contact.setSMSText(monthname + "," + mnth + "," + smsyear
                            + "," + msgcount);
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:", "// Get MonthList Of Message" + err.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    public List<Contact> GetLessionDiaryMessageMonthListSubjectwise(int studid,
                                                                    int schoolid, int YearId, int MsgType) {
        List<Contact> contactList = new ArrayList<Contact>();
        List<Contact> contactListseq = new ArrayList<Contact>();
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        String ModuleId = "2";
        /*
         * if (ModuleId == 2) { mod_id = "2,9"; } else { mod_id = "3,10"; }
         */
        // testing module id 2,9 3,10
        if (MsgType == 1) {
            ModuleId = "2,9";
        } else if (MsgType == 2) {
            ModuleId = "3,10";
        } else if (MsgType == 3) {
            ModuleId = "5";
        } else if (MsgType == 4) {
            ModuleId = "4";
        } else if (MsgType == 5) {
            ModuleId = "6,12";
        } else if (MsgType == 7) {
            ModuleId = "15";
        } else if (MsgType == 8) {
            ModuleId = "16";
        } else if (MsgType == 9) {
            ModuleId = "17";
        }
        try {
            String[] Monthlist = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            String countquery1 = "select " + KEY_SMS_YEAR + "," + KEY_SMS_MONTH
                    + "," + "count(distinct " + KEY_SMS_DAY + ") as count ,"
                    + KEY_SMSID + " from " + TABLE_SMS2 + " Where "
                    + KEY_SMS_STUDID + "=" + studid + " and " + KEY_SMS_YEAR_ID
                    + "=" + YearId + " and " + KEY_SMS_SCHOOLID + "="
                    + schoolid + " and " + KEY_SMS_Moduleid + " in ("
                    + ModuleId + ")" + " group by " + KEY_SMS_MONTH
                    + " ORDER BY " + KEY_SMSID + " DESC";

            cursor = myWritableDb.rawQuery(countquery1, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    int mnth = Integer.parseInt(cursor.getString(1));
                    int smsyear = Integer.parseInt(cursor.getString(0));
                    int msgcount = Integer.parseInt(cursor.getString(2));
                    String monthname = Monthlist[mnth - 1];
                    contact.setSMSText(monthname + "," + mnth + "," + smsyear
                            + "," + msgcount);
                    contactList.add(contact);
                } while (cursor.moveToNext());
                for (int i = 0; i < 12; i++) {
                    String[] arrmonth = null;
                    if (schoolid == 1842 || schoolid == 8595) {
                        arrmonth = new String[]{"3", "2", "1", "12", "11",
                                "10", "9", "8", "7", "6", "5", "4"};
                    } else {
                        arrmonth = new String[]{"4", "3", "2", "1", "12",
                                "11", "10", "9", "8", "7", "6", "5"};
                    }
                    for (int j = 0; j < contactList.size(); j++) {
                        Contact cn = new Contact();
                        cn = contactList.get(j);
                        String item = cn.getSMSText();
                        String[] part = item.split(",");
                        if (part[1].equals(arrmonth[i])) {
                            contactListseq.add(contactList.get(j));
                            break;
                        }
                    }
                }
                contactList = contactListseq;
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:", "// Get MonthList Of Message" + err.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    //Jaydeep

    // jaydeep
    // Get Last Homework msg
    public String GetLatestHomeworkMSG(String studid, String schoolid,
                                       String YearId, String ModuleId) {

        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        String[] Monthlist = {"January", "February", "March", "April", "May",
                "June", "July", "August", "September", "October", "November",
                "December"};
        String msg = "";
        String module;
        try {
            if (ModuleId.equalsIgnoreCase("1")) {
                module = "2,8";
            } else {
                module = "3,9";
            }
            // Select All Query
            String countQuery = "Select " + KEY_MSGTEXT + " , "
                    + KEY_SMS_Moduleid + "," + KEY_SMS_DAY + ","
                    + KEY_SMS_MONTH + "," + KEY_SMS_YEAR + " from "
                    + TABLE_SMS2 + " where " + KEY_SMS_STUDID + "=" + studid
                    + " and " + KEY_SMS_SCHOOLID + "=" + schoolid + " and "
                    + KEY_SMS_YEAR_ID + "=" + YearId + " and "
                    + KEY_SMS_Moduleid + " in (" + module + ") ORDER BY "
                    + KEY_MSGID + " DESC";
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor.moveToFirst()) {
                String datestr = cursor.getString(2).toString() + "-"
                        + Monthlist[Integer.parseInt(cursor.getString(3)) - 1]
                        + "-" + cursor.getString(4);
                String msgtext = cursor.getString(0).split("##,@@")[0];
                msg = msgtext + "@@##" + cursor.getString(1).toString()
                        + "@@##" + datestr;
            }
        } catch (Exception ex) {
            Constants.Logwrite("datahandler 2298", ex.getMessage());
        }
        return msg;
    }

    // Get MonthList Of Message
    public List<Contact> GeSelectedMonthMessageList(int studid, int schoolid,
                                                    int YearId, int Month, int Year) {
        List<Contact> contactList = new ArrayList<Contact>();
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            // Select All Query
            String countQuery = "Select " + KEY_MSGTEXT + " from " + TABLE_SMS2
                    + " where " + KEY_SMS_STUDID + "=" + studid + " and "
                    + KEY_SMS_SCHOOLID + "=" + schoolid + " and "
                    + KEY_SMS_YEAR_ID + "=" + YearId + " and " + KEY_SMS_MONTH
                    + "=" + Month + " and " + KEY_SMS_YEAR + "=" + Year
                    + " and " + KEY_SMS_Moduleid + "=0 ORDER BY " + KEY_MSGID
                    + " DESC";
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    String msg = cursor.getString(0);
                    contact.setGlobalText(msg);
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:", "// Get MonthList Of Message" + err.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    public boolean IsTabExist(SQLiteDatabase mDatabase, String TableName) {
        boolean IsExist = false;
        try {
            Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + TableName,
                    null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.close();
                    IsExist = true;
                }
                cursor.close();
            }
            return IsExist;
        } catch (Exception err) {
            Constants.Logwrite("DatabaseHandler:IsTabExist", "Exception" + err.getMessage()
                    + ":::StactTrace:" + err.getStackTrace().toString());
            return IsExist;
        }
    }

    // changes for subject wise jaydeep
    // Getting All Contacts
    public List<Contact> GetAllHomeWorkandSubjectHomeWorkDetails(int studid,
                                                                 int schoolid, String msgtype, int YearId) {
        List<Contact> contactList = new ArrayList<Contact>();
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String ModIdStr = "2";
            if (msgtype.equals("1")) {
                ModIdStr = "2,9";
            } else if (msgtype.equals("2")) {
                ModIdStr = "3,10";
            } else if (msgtype.equals("3")) {
                ModIdStr = "5";
            } else if (msgtype.equals("4")) {
                ModIdStr = "4";
            } else if (msgtype.equals("5")) {
                ModIdStr = "6,12";
            } else if (msgtype.equals("7")) {
                ModIdStr = "15";
            } else if (msgtype.equals("8")) {
                ModIdStr = "16";
            } else if (msgtype.equals("9")) {
                ModIdStr = "17";
            }
            String selectQuery = "SELECT * FROM " + TABLE_SMS2 + " where "
                    + KEY_SMS_STUDID + "=" + studid + " and "
                    + KEY_SMS_SCHOOLID + "=" + schoolid + " and "
                    + KEY_SMS_Moduleid + " IN (" + ModIdStr + ") and "
                    + KEY_SMS_YEAR_ID + "=" + YearId + " ORDER BY " + KEY_MSGID
                    + " DESC";
            cursor = myWritableDb.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.setSMSID(Integer.parseInt(cursor.getString(1)));
                    String aa = cursor.getString(2);
                    int MSG_ID = Integer.parseInt(cursor.getString(1));
                    int Mod_Id = Integer.parseInt(cursor.getString(8));
                    String[] splitedstr = null;
                    String msg = "";
                    String datetime = "";
                    splitedstr = aa.split("##,@@");
                    msg = splitedstr[0];
                    String day = cursor.getString(5);
                    String month = cursor.getString(6);
                    String year = cursor.getString(7);
                    if (day.length() <= 1) {
                        day = "0" + day;
                    }
                    if (month.length() <= 1) {
                        month = "0" + month;
                    }
                    datetime = day + "/" + month + "/" + year;

                    String Other_Details = MSG_ID + "##HWOTHERDET@@" + msg
                            + "##HWOTHERDET@@" + Mod_Id;
                    contact.setGlobalText(datetime + "##@@USP##@@"
                            + Other_Details);
                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }

        } catch (Exception err) {

            Constants.Logwrite("Database:", "GetAllSMSDetails:2476" + err.getMessage());

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    // Get MonthList Of Message
    public List<Contact> GeSelectedMonthLessionDiaryMessageList(int studid,
                                                                int schoolid, int YearId, int Month, int Year, int ModuleId) {
        List<Contact> contactList = new ArrayList<Contact>();
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String[] Monthlist = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            // Select All Query
            String countQuery = "Select " + KEY_MSGTEXT + "," + KEY_SMS_DAY
                    + "," + KEY_SMS_MONTH + "," + KEY_SMS_YEAR + " from "
                    + TABLE_SMS2 + " where " + KEY_SMS_STUDID + "=" + studid
                    + " and " + KEY_SMS_SCHOOLID + "=" + schoolid + " and "
                    + KEY_SMS_YEAR_ID + "=" + YearId + " and " + KEY_SMS_MONTH
                    + "=" + Month + " and " + KEY_SMS_YEAR + "=" + Year
                    + " and " + KEY_SMS_Moduleid + "=" + ModuleId
                    + " ORDER BY " + KEY_MSGID + " DESC";
            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(countQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String Day;
                    String SMSMONTH;
                    String MonthName = "";
                    String SMSYear = "";
                    int SMONTH = 0;
                    Contact contact = new Contact();
                    String msg = cursor.getString(0);
                    if (ModuleId == 2) {
                        try {
                            // String TodayDate = new
                            // SimpleDateFormat("dd/MM/yyyy").format(new
                            // Date());
                            // String[] splitedstr = TodayDate.split("/");
                            int CheckDay = Integer
                                    .parseInt(cursor.getString(1));
                            int CheckMONTH = Integer.parseInt(cursor
                                    .getString(2));
                            int CheckYear = Integer.parseInt(cursor
                                    .getString(3));
                            String CheckDate = CheckDay + "/" + CheckMONTH
                                    + "/" + CheckYear;

                            String OriginalMessage = cursor.getString(0);
                            String[] spltstring = OriginalMessage
                                    .split("##,@@");

                            String Msg1 = spltstring[0];
                            String Msg2 = CheckDate;

                            contact.setGlobalText(Msg1 + "##,@@" + Msg2);
                        } catch (Exception err) {
                            contact.setGlobalText(cursor.getString(0));
                        }
                    } else {
                        Day = cursor.getString(1);
                        SMSMONTH = cursor.getString(2);
                        SMSYear = cursor.getString(3);

                        int DayNum = Integer.parseInt(cursor.getString(1));
                        int SMSMONTHNum = Integer.parseInt(cursor.getString(2));
                        int SMSYEARNum = Integer.parseInt(cursor.getString(3));

                        MonthName = Monthlist[SMSMONTHNum - 1];
                        SimpleDateFormat inFormat = new SimpleDateFormat(
                                "dd/MM/yyyy");
                        SimpleDateFormat outFormat = new SimpleDateFormat(
                                "EEEE");
                        String dd = DayNum + "/" + SMSMONTHNum + "/"
                                + SMSYEARNum;
                        Date date = inFormat.parse(dd);
                        String goal = outFormat.format(date);
                        msg = Day + "/" + MonthName + "/" + SMSYear + " "
                                + goal;
                        contact.setGlobalText(msg);

                    }
                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }

        } catch (Exception err) {
            Constants.writelog("Database:", "// Get MonthList Of Message" + err.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    //
    // *************************************************************************************************************
    // All Methods of Academic Year Retrival and Insert
    // Adding Academic Year Table
    public void AddYear(Contact contact) {
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_ACADEMIC_YEAR_ID, contact.getYEAR_ID()); // Contac //
            // Phone
            values.put(KEY_ACADEMIC_YEAR_TEXT, contact.getYEAR_TEXT());
            values.put(KEY_ACADEMIC_IS_CURRENT, contact.getISCURRENT());
            values.put(KEY_ACADEMIC_SCHOOLID, contact.getYearSchoolId());
            values.put(KEY_ACADEMIC_STUDID, contact.getYearStudId());
            values.put(KEY_ACADEMIC_IsDef, contact.getISDefAcademicYear());

            // Inserting Row
            myWritableDb.insert(TABLE_ACADEMIC_YEAR, null, values);
        } catch (Exception err) {
            Constants.writelog("SQL", "AddYear:" + err.getMessage());
        } finally {
            close(myWritableDb);
        }
    }

    // Update Academic Year Table
    public int UpdateAcademicYearSetDefaultOne(String Year_Id, String SchoolId,
                                               String StudId) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ACADEMIC_IsDef, 1);
            String table = TABLE_ACADEMIC_YEAR;
            String whereClause = "" + KEY_ACADEMIC_YEAR_ID + "=? AND "
                    + KEY_ACADEMIC_SCHOOLID + " =? AND " + KEY_ACADEMIC_STUDID
                    + " =?";
            String whereArgs[] = new String[]{Year_Id, SchoolId, StudId};
            count = myWritableDb.update(table, values, whereClause, whereArgs);
        } catch (Exception err) {
            Constants.writelog("DatabaseHandler:", "Exception:" + err.getMessage());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    // Getting All Academic Year Records
    public List<Contact> GetAllAcademicYearRecords(int SchoolId, int StudId) {
        List<Contact> contactList = new ArrayList<Contact>();
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_ACADEMIC_YEAR
                    + " where " + KEY_ACADEMIC_SCHOOLID + "=" + SchoolId
                    + " and " + KEY_ACADEMIC_STUDID + "=" + StudId
                    + " ORDER BY " + KEY_ACADEMIC_IsDef + " DESC";
            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();

                    int val = Integer.parseInt(cursor.getString(0));
                    contact.setGlobalText(cursor.getString(2) + ","
                            + cursor.getString(1));
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
            // return contact list
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception err) {
            Constants.writelog("Db", "GetAllAcademicYearRecords()3639: " + err.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    // Getting contacts Count
    public boolean CheckAcademicYear(int studid, int SchoolID, int YearId) {
        boolean isthere = false;
        int cnt = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String countQuery = "SELECT  * FROM " + TABLE_ACADEMIC_YEAR
                    + " where " + KEY_ACADEMIC_STUDID + "=" + studid + " and "
                    + KEY_ACADEMIC_SCHOOLID + "=" + SchoolID + " and "
                    + KEY_ACADEMIC_YEAR_ID + "=" + YearId + "";
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed()) {
                cursor.moveToFirst();
                cnt = cursor.getCount();
                if (cnt > 0) {
                    isthere = true;
                } else {
                    isthere = false;
                }
                cursor.close();
            } else {
                isthere = false;
            }
        } catch (Exception err) {
            Constants.writelog("Db", "CheckAcademicYear()3675: " + err.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return isthere;
    }

    // Getting contacts Count
    public boolean CheckAcademicYearCurrentOrNot(int studid, int SchoolID,
                                                 int YearId) {
        boolean IsCurrent = false;
        int cnt = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String countQuery = "SELECT  * FROM " + TABLE_ACADEMIC_YEAR
                    + " where " + KEY_ACADEMIC_STUDID + "=" + studid + " and "
                    + KEY_ACADEMIC_SCHOOLID + "=" + SchoolID + " and "
                    + KEY_ACADEMIC_YEAR_ID + "=" + YearId + "";

            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed()) {
                cursor.moveToFirst();
                int Is_Current_Academic_Id = Integer.parseInt(cursor
                        .getString(3));
                if (Is_Current_Academic_Id == 1) {
                    IsCurrent = true;
                } else {
                    IsCurrent = false;
                }
                cursor.close();
            } else {
                IsCurrent = false;
            }
        } catch (Exception err) {
            Constants.writelog("Db", "CheckAcademicYearCurrentOrNot()3715: " + err.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return IsCurrent;
    }

    // Update Records Set Isdefault to Zero to One
    public int UpdateAcademicYearSetIsCurrent_Zero_to_One(String schoolid,
                                                          String studid) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_ACADEMIC_IsDef, 0);
            String table = TABLE_ACADEMIC_YEAR;
            String whereClause = "" + KEY_ACADEMIC_SCHOOLID + "=? AND "
                    + KEY_ACADEMIC_STUDID + " =?";
            String whereArgs[] = new String[]{schoolid, studid};
            count = myWritableDb.update(table, values, whereClause, whereArgs);
        } catch (Exception err) {
            Constants.writelog("DatabaseHandler:", "Exception:" + err.getMessage());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    public String GetDefaultAcademicYearAccount(int SchoolId, int StudId) {
        Cursor cursor = null;
        String yeardetails = "";
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + TABLE_ACADEMIC_YEAR + " where "
                + KEY_ACADEMIC_SCHOOLID + "=" + SchoolId + " and "
                + KEY_ACADEMIC_STUDID + "=" + StudId + " and "
                + KEY_ACADEMIC_IsDef + "=1";
        try {
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String year = cursor.getString(cursor
                        .getColumnIndex(KEY_ACADEMIC_YEAR_TEXT));
                String yearid = cursor.getString(cursor
                        .getColumnIndex(KEY_ACADEMIC_YEAR_ID));
                yeardetails = year + "," + yearid;
            } else {
                countQuery = "SELECT  * FROM " + TABLE_CONTACTS1 + " where "
                        + KEY_STUD_ID + "=" + StudId + " AND " + KEY_SCHOOLID + "="
                        + SchoolId + " order by id desc";
                cursor = myWritableDb.rawQuery(countQuery, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    String yearid = cursor.getString(cursor
                            .getColumnIndex(KEY_YEAR_ID));
                    String year = cursor.getString(cursor
                            .getColumnIndex(KEY_ACADEMIC_YEAR));
                    yeardetails = year + "," + yearid;
                }
            }
        } catch (Exception err) {
            Constants.writelog("DatabaseHandler:",
                    "GetDefaultAcademicYearAccount" + err.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return yeardetails;
    }

    public int GetCurrentAcademicYearId(int SchoolId, int StudId) {
        Cursor cursor = null;
        int yearid = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        String countQuery = "SELECT * FROM " + TABLE_ACADEMIC_YEAR + " where "
                + KEY_ACADEMIC_SCHOOLID + "=" + SchoolId + " and "
                + KEY_ACADEMIC_STUDID + "=" + StudId + " and "
                + KEY_ACADEMIC_IS_CURRENT + "=1";
        try {
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                yearid = Integer.parseInt(cursor.getString(cursor
                        .getColumnIndex(KEY_ACADEMIC_YEAR_ID)));
            } else {
                countQuery = "SELECT  * FROM " + TABLE_CONTACTS1 + " where "
                        + KEY_STUD_ID + "=" + StudId + " AND " + KEY_SCHOOLID + "="
                        + SchoolId + " order by id desc";
                cursor = myWritableDb.rawQuery(countQuery, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    yearid = Integer.parseInt(cursor.getString(cursor
                            .getColumnIndex(KEY_YEAR_ID)));
                }
            }
        } catch (Exception err) {
            Constants.writelog("DatabaseHandler:",
                    "GetDefaultAcademicYearAccount" + err.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return yearid;
    }


    public int SetCurrentYearAsDefaultYear(String StudId, String SchoolId) {
        SQLiteDatabase myWritableDb = null;
        int result = 0;
        try {
            int count = 0;
            ContentValues values = new ContentValues();
            values.put(KEY_ACADEMIC_IsDef, 0);
            String table = TABLE_ACADEMIC_YEAR;
            myWritableDb = this.getWritableDatabase();
            String whereClause = "" + KEY_ACADEMIC_SCHOOLID + " =? AND "
                    + KEY_ACADEMIC_STUDID + " =?";
            String whereArgs[] = new String[]{SchoolId, StudId};
            count = myWritableDb.update(table, values, whereClause, whereArgs);
            // set default year
            ContentValues values1 = new ContentValues();
            values1.put(KEY_ACADEMIC_IsDef, 1);
            String whereClause1 = "" + KEY_ACADEMIC_IS_CURRENT + " =? AND "
                    + KEY_ACADEMIC_SCHOOLID + " =? AND " + KEY_ACADEMIC_STUDID
                    + " =?";
            String whereArgs1[] = new String[]{"1", SchoolId, StudId};
            count = myWritableDb.update(table, values1, whereClause1,
                    whereArgs1);
            result = 1;
            myWritableDb.close();
        } catch (Exception ex) {
            Constants.writelog("commonutilities:141",
                    "Exception:" + ex.getMessage() + "StackTrace::"
                            + ex.getStackTrace().toString());
            ex.printStackTrace();
            myWritableDb.close();
        }
        return result;

    }
    /*
     * public String GetDefaultAcademicYearId(int SchoolId, int StudId) { Cursor
     * cursor = null; String yeardetails = ""; SQLiteDatabase myWritableDb =
     * null; myWritableDb = this.getWritableDatabase(); String countQuery =
     * "SELECT  * FROM " + TABLE_ACADEMIC_YEAR + " where " +
     * KEY_ACADEMIC_SCHOOLID + "=" + SchoolId + " and " + KEY_ACADEMIC_STUDID +
     * "=" + StudId + " and " + KEY_ACADEMIC_IsDef + "=1"; try { cursor =
     * myWritableDb.rawQuery(countQuery, null); if (cursor.getCount() > 0) {
     * cursor.moveToFirst(); yeardetails= cursor.getString(cursor
     * .getColumnIndex(KEY_ACADEMIC_YEAR_ID)); } } catch (Exception err) {
     *Constants.Logwrite("DatabaseHandler:", "GetDefaultAcademicYearId 4004" +
     * err.getMessage()); } finally { if (cursor != null && !cursor.isClosed())
     * { cursor.close(); } close(myWritableDb); } return yeardetails; }
     */

    public int getAcademicYearCount(int studid, int schoolid) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String countQuery = "SELECT  * FROM " + TABLE_ACADEMIC_YEAR
                    + " where " + KEY_ACADEMIC_STUDID + "=" + studid + " and "
                    + KEY_ACADEMIC_SCHOOLID + "=" + schoolid + "";

            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed()) {
                count = cursor.getCount();
                cursor.close();
            }
        } catch (Exception err) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return count;
    }

    // DeleteAll Old SMS From Table
    public int DeleteStudentYear(int studid, int schoolid) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {

            myWritableDb.execSQL("delete from " + TABLE_ACADEMIC_YEAR
                    + " where " + KEY_ACADEMIC_STUDID + "=" + studid + " and "
                    + KEY_ACADEMIC_SCHOOLID + "=" + schoolid + "");
            count = 1;
        } catch (Exception err) {
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    // *************************************************************************************************************
    // All Methods of Exam Data and Insert
    // Adding Academic Year Table
    public void AddExams(Contact contact) {
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_EXAM_YEAR_ID, contact.getExamYearId());
            values.put(KEY_EXAM_SR_NO, contact.getExamSrNo());
            values.put(KEY_EXAM_STUDID, contact.getExamStudId());
            values.put(KEY_EXAM_SCHOOLID, contact.getExamSchoolId());
            values.put(KEY_EXAM_EXAMID, contact.getExamId());
            values.put(KEY_EXAM_EXAMNAME, contact.getExamName());
            values.put(KEY_EXAM_EXAMMARKS, contact.getExamMarks());
            values.put(KEY_EXAM_ISMARKSHEET, contact.getExamIsMarksheet());
            values.put(KEY_EXAM_MARKSHEET_PATH, contact.getExamMarksheetPath());
            values.put(KEY_EXAM_CATEGORY_ID, contact.getCATEGORY_ID());
            values.put(KEY_EXAM_CATEGORY_NAME, contact.getCATEGORY_NAME());
            // Inserting Row
            myWritableDb.insert(TABLE_EXAM_DETAILS, null, values);
        } catch (Exception err) {

            Constants.Logwrite("AddExams", "Exception:" + err.getMessage());

        } finally {
            close(myWritableDb);
        }
    }

    public int UpdateExams(String examid, String examname, String path,
                           String yearid, String schoolid, String studid,
                           String isMarksheet, String categoryId, String categoryName, int srNo) {
        SQLiteDatabase myWritableDb = null;
        int count = 0;
        myWritableDb = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_EXAM_EXAMID, examid);
            values.put(KEY_EXAM_EXAMNAME, examname);
            values.put(KEY_EXAM_MARKSHEET_PATH, path);
            values.put(KEY_EXAM_ISMARKSHEET, Integer.parseInt(isMarksheet));
            values.put(KEY_EXAM_CATEGORY_ID, categoryId);
            values.put(KEY_EXAM_CATEGORY_NAME, categoryName);
            values.put(KEY_EXAM_SR_NO, srNo);
            // Update Row
            String whereClause = "" + KEY_EXAM_SCHOOLID + "=? AND "
                    + KEY_EXAM_STUDID + " =? AND " + KEY_EXAM_YEAR_ID
                    + " =? AND " + KEY_EXAM_EXAMID + " =? AND " + KEY_EXAM_ISMARKSHEET + " =? ";

            String whereArgs[] = new String[]{schoolid, studid, yearid,
                    examid, isMarksheet};
            count = myWritableDb.update(TABLE_EXAM_DETAILS, values,
                    whereClause, whereArgs);
        } catch (Exception err) {
            Constants.writelog(
                    "DatabaseHandler",
                    "Exception 3996:" + err.getMessage() + ":::::"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    // Getting All Academic Year Records
    public List<Contact> GetAllExamRecords(int SchoolId, int StudId, int YearId, int ExamCategoryId) {
        List<Contact> contactList = new ArrayList<Contact>();
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {

            // Select All Query
            String selectQuery = "";
            if (ExamCategoryId == -2) {
                selectQuery = "SELECT  * FROM " + TABLE_EXAM_DETAILS
                        + " where " + KEY_EXAM_SCHOOLID + "=" + SchoolId + " and "
                        + KEY_EXAM_STUDID + "=" + StudId + " and "
                        + KEY_EXAM_YEAR_ID + "=" + YearId + " and "
                        + KEY_EXAM_ISMARKSHEET + "= 0 ORDER BY " + KEY_EXAM_SR_NO + " DESC";
            } else {
                // change by Tejas Patel 23-07-2018
                selectQuery = "SELECT  * FROM " + TABLE_EXAM_DETAILS
                        + " where " + KEY_EXAM_SCHOOLID + "=" + SchoolId + " and "
                        + KEY_EXAM_STUDID + "=" + StudId + " and "
                        + KEY_EXAM_YEAR_ID + "=" + YearId + " and "
                        + KEY_EXAM_CATEGORY_ID + "=" + ExamCategoryId + " and "
                        + KEY_EXAM_ISMARKSHEET + "= 0 ORDER BY " + KEY_EXAM_SR_NO + " DESC";
            }
            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    int val = Integer.parseInt(cursor.getString(0));
                    contact.setGlobalText(cursor.getString(5) + ","
                            + cursor.getString(6));
                    contact.setCATEGORY_ID(cursor.getString(10));
                    contact.setCATEGORY_NAME(cursor.getString(11));
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.writelog(
                    "DatabaseHandler",
                    "GetAllExamRecords() 3968:" + err.getMessage() + ":::::"
                            + err.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    // Getting All Academic Year Records
    public List<Contact> GetAllExamMarks(int SchoolId, int StudId, int YearId,
                                         int ExamId) {
        List<Contact> contactList = new ArrayList<Contact>();
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_EXAM_DETAILS
                    + " where " + KEY_EXAM_SCHOOLID + "=" + SchoolId + " and "
                    + KEY_EXAM_STUDID + "=" + StudId + " and "
                    + KEY_EXAM_YEAR_ID + "=" + YearId + " and "
                    + KEY_EXAM_EXAMID + "=" + ExamId + " ORDER BY "
                    + KEY_EXAM_SR_NO + " DESC";
            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    int val = Integer.parseInt(cursor.getString(0));
                    contact.setGlobalText(cursor.getString(7));
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.writelog(
                    "DatabaseHandler",
                    "GetAllExamMarks() 4008:" + err.getMessage() + ":::::"
                            + err.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    // Getting All Academic Year Records
    public List<Contact> GetAllMarksheetExamRecords(int SchoolId, int StudId,
                                                    int YearId, int ExamCategoryId) {
        List<Contact> contactList = new ArrayList<Contact>();
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            // Select All Query
            String selectQuery = "";
            if (ExamCategoryId == -2) {
                selectQuery = "SELECT  * FROM " + TABLE_EXAM_DETAILS
                        + " where " + KEY_EXAM_SCHOOLID + "=" + SchoolId + " and "
                        + KEY_EXAM_STUDID + "=" + StudId + " and "
                        + KEY_EXAM_YEAR_ID + "=" + YearId + " and "
                        + KEY_EXAM_ISMARKSHEET + "=1 ORDER BY " + KEY_EXAM_SR_NO
                        + " DESC";
            } else {
                // change by Tejas Patel 24-07-2018
                selectQuery = "SELECT  * FROM " + TABLE_EXAM_DETAILS
                        + " where " + KEY_EXAM_SCHOOLID + "=" + SchoolId + " and "
                        + KEY_EXAM_STUDID + "=" + StudId + " and "
                        + KEY_EXAM_YEAR_ID + "=" + YearId + " and "
                        + KEY_EXAM_CATEGORY_ID + "=" + ExamCategoryId + " and "
                        + KEY_EXAM_ISMARKSHEET + "=1 ORDER BY " + KEY_EXAM_SR_NO
                        + " DESC";
            }
            cursor = myWritableDb.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    int val = Integer.parseInt(cursor.getString(0));
                    contact.setGlobalText(cursor.getString(5) + ","
                            + cursor.getString(6) + "," + cursor.getString(9));
                    contact.setCATEGORY_ID(cursor.getString(10));
                    contact.setCATEGORY_NAME(cursor.getString(11));
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.writelog("DatabaseHandler",
                    "GetAllMarksheetExamRecords() 4047:" + err.getMessage() + ":::::"
                            + err.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    // Getting Exam MarksheetPath
    public List<Contact> GetMarksheetPath(int SchoolId, int StudId, int YearId,
                                          int ExamId) {
        List<Contact> contactList = new ArrayList<Contact>();
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_EXAM_DETAILS
                    + " where " + KEY_EXAM_SCHOOLID + "=" + SchoolId + " and "
                    + KEY_EXAM_STUDID + "=" + StudId + " and "
                    + KEY_EXAM_YEAR_ID + "=" + YearId + " and "
                    + KEY_EXAM_EXAMID + "=" + ExamId + " and  "
                    + KEY_EXAM_ISMARKSHEET + "=1  ORDER BY " + KEY_EXAM_SR_NO + " DESC";
            Cursor cursor = myWritableDb.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    String path = cursor.getString(9);
                    contact.setGlobalText(path);
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            // return count;
        } catch (Exception err) {
            Constants.Logwrite("Database:GetMarksheetPath", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return contactList;
    }

    public int DeleteDuplicateStudentExmasMarksheet(int studid, int schoolid,
                                                    int ExamId, int YearId) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {

            myWritableDb.execSQL("delete from " + TABLE_EXAM_DETAILS
                    + " where " + KEY_EXAM_STUDID + "=" + studid + " and "
                    + KEY_EXAM_SCHOOLID + "=" + schoolid + " and "
                    + KEY_EXAM_EXAMID + "=" + ExamId + " and "
                    + KEY_EXAM_YEAR_ID + "=" + YearId + " and "
                    + KEY_EXAM_ISMARKSHEET + "=1");
            count = 1;
        } catch (Exception err) {
            Constants.Logwrite("DeleteExtraStudentExmasMarksheet",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    // Check ExamName Inserted
    public boolean CheckExamInserted(int studid, int SchoolID, int YearId,
                                     int ExamId) {
        boolean isthere = true;
        int cnt = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            String countQuery = "SELECT  * FROM " + TABLE_EXAM_DETAILS
                    + " where " + KEY_EXAM_STUDID + "=" + studid + " and "
                    + KEY_EXAM_SCHOOLID + "=" + SchoolID + " and "
                    + KEY_EXAM_YEAR_ID + "=" + YearId + " and "
                    + KEY_EXAM_EXAMID + "=" + ExamId + " and "
                    + KEY_EXAM_ISMARKSHEET + "=0 ";

            Cursor cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed()) {
                cursor.moveToFirst();
                cnt = cursor.getCount();
                if (cnt > 0) {
                    isthere = true;
                } else {
                    isthere = false;
                }
                cursor.close();
            } else {
                isthere = false;
            }

        } catch (Exception err) {
            Constants.Logwrite("Database:CheckExamInserted", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return isthere;
    }

    // Check ExamName Inserted
    public boolean CheckMarksheetExamsInserted(int studid, int SchoolID,
                                               int YearId, int ExamId) {
        boolean isthere = true;
        int cnt = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            String countQuery = "SELECT  * FROM " + TABLE_EXAM_DETAILS
                    + " where " + KEY_EXAM_STUDID + "=" + studid + " and "
                    + KEY_EXAM_SCHOOLID + "=" + SchoolID + " and "
                    + KEY_EXAM_YEAR_ID + "=" + YearId + " and "
                    + KEY_EXAM_EXAMID + "=" + ExamId + " and "
                    + KEY_EXAM_ISMARKSHEET + "=1";

            Cursor cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed()) {
                cursor.moveToFirst();
                cnt = cursor.getCount();
                if (cnt > 0) {
                    isthere = true;
                } else {
                    isthere = false;
                }
                cursor.close();
            } else {
                isthere = false;
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:CheckMarksheetExamsInserted",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return isthere;
    }

    // Update ExamTables Add Exam Marks
    public int UpdateExamTableSetMarks(String SchoolId, String StudId,
                                       String YearId, String ExamId, String ExamMarks) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_EXAM_EXAMMARKS, ExamMarks);
            String table = TABLE_EXAM_DETAILS;
            String whereClause = "" + KEY_EXAM_SCHOOLID + "=? AND "
                    + KEY_EXAM_STUDID + " =? AND " + KEY_EXAM_YEAR_ID
                    + " =? AND " + KEY_EXAM_EXAMID + " =?";
            String whereArgs[] = new String[]{SchoolId, StudId, YearId,
                    ExamId};
            count = myWritableDb.update(table, values, whereClause, whereArgs);
        } catch (Exception err) {

            Constants.Logwrite("Database:UpdateExamTableSetMarks",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    // Update ExamTable Update ExamMarksheetPath
    public int UpdateExamTableSetMarksheetPath(String SchoolId, String StudId,
                                               String YearId, String ExamId, String IsMarksheet,
                                               String MarksheetPath) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_EXAM_MARKSHEET_PATH, MarksheetPath);
            String table = TABLE_EXAM_DETAILS;
            String whereClause = "" + KEY_EXAM_SCHOOLID + "=? AND "
                    + KEY_EXAM_STUDID + " =? AND " + KEY_EXAM_YEAR_ID
                    + " =? AND " + KEY_EXAM_EXAMID + " =? AND "
                    + KEY_EXAM_ISMARKSHEET + " =?";
            String whereArgs[] = new String[]{SchoolId, StudId, YearId,
                    ExamId, IsMarksheet};
            count = myWritableDb.update(table, values, whereClause, whereArgs);
        } catch (Exception err) {
            Constants.Logwrite("Database:UpdateExamTableSetMarksheetPath",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    // DeleteAll Old SMS From Table
    public int DeleteStudentExmas(int studid, int schoolid) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            myWritableDb.execSQL("delete from " + TABLE_EXAM_DETAILS
                    + " where " + KEY_EXAM_STUDID + "=" + studid + " and "
                    + KEY_EXAM_SCHOOLID + "=" + schoolid + "");
            count = 1;
        } catch (Exception err) {
            Constants.Logwrite("Database:DeleteStudentExmas", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    // Delete student all circular details
    public int DeleteStudentCircular(int studid, int schoolid) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            myWritableDb.execSQL("delete from " + TABLE_CIRCULAR_DETAILS
                    + " where " + KEY_CIRCULAR_STUDID + "=" + studid + " and "
                    + KEY_CIRCULAR_SCHOOLID + "=" + schoolid + "");
            count = 1;
        } catch (Exception err) {
            Constants.Logwrite("Database:DeleteStudentCircular",
                    "Exception 3722:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    // DeleteAll Old SMS From Table
    public int DeleteExtraStudentExmas(int studid, int schoolid, int ExamId,
                                       int YearId) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {

            myWritableDb.execSQL("delete from " + TABLE_EXAM_DETAILS
                    + " where " + KEY_EXAM_STUDID + "=" + studid + " and "
                    + KEY_EXAM_SCHOOLID + "=" + schoolid + " and "
                    + KEY_EXAM_EXAMID + "=" + ExamId + " and "
                    + KEY_EXAM_YEAR_ID + "=" + YearId + " and "
                    + KEY_EXAM_ISMARKSHEET + "=0");
            count = 1;
        } catch (Exception err) {
            Constants.Logwrite("DeleteExtraStudentExmas", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }


    // All Methods of Album_Master

    // Adding new contact
    public int AddAlbumDetails(Contact contact) {
        int Ins_Status = 0;
        SQLiteDatabase myWritableDb = null;
        try {
            myWritableDb = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ALBUM_STUDID, contact.getAlbumStudId());
            values.put(KEY_ALBUM_ALBUMID, contact.getAlbumId());
            values.put(KEY_ALBUM_ALBUMNAME, contact.getAlbumName());
            values.put(KEY_ALBUM_ALBUMURL, contact.getAlbumurl());
            values.put(KEY_ALBUM_PHOTOFILENAME, contact.getAlbumPhotofile());
            values.put(KEY_ALBUM_SCHOOLID, contact.getAlbumSchoolId());// Contact
            // Name
            values.put(KEY_ALBUM_CLASSSECID, contact.getAlbumClassSecId());
            values.put(KEY_ALBUM_DATETICKS, contact.getAlbumdateticks());
            values.put(KEY_ALBUM_DATETIME, contact.getAlbumDatetime());
            // Inserting Row
            myWritableDb.insert(TABLE_STUDENT_ALBUM_DETAILS, null, values);
            Ins_Status = 1;
        } catch (Exception err) {
            Constants.Logwrite("DatabaseHandler:AddAlbumDetails",
                    "Exception" + err.getMessage() + ":::StactTrace:"
                            + err.getStackTrace().toString());
            Ins_Status = 0;
        } finally {
            close(myWritableDb); // Closing database connection
        }
        return Ins_Status;
    }

    // Insert Circular Details

    public int AddCircular(Contact contact) {
        int Ins_Status = 0;
        SQLiteDatabase myWritableDb = null;
        try {
            myWritableDb = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_CIRCULAR_STUDID, contact.getcirstudid());
            values.put(KEY_CIRCULAR_SCHOOLID, contact.getcirschoolid());
            values.put(KEY_CIRCULAR_YEARID, contact.getciryearid());
            values.put(KEY_CIRCULAR_GROUPID, contact.getcirgroupid());
            values.put(KEY_CIRCULAR_GROUPNAME, contact.getcirGroupname());
            values.put(KEY_CIRCULARID, contact.getCirId());
            values.put(KEY_CIRCULAR_NAME, contact.getCirName());
            values.put(KEY_CIRCULAR_DATETEXT, contact.getcirDateText());
            values.put(KEY_CIRCULAR_PATH, contact.getcirPath());
            values.put(KEY_CIRCULAR_TICKS, contact.getcirTicks());
            values.put(KEY_CIRCULAR_ISDELETED, contact.getcirisdeleted());

            // Inserting Row
            myWritableDb.insert(TABLE_CIRCULAR_DETAILS, null, values);
            Ins_Status = 1;
        } catch (Exception err) {
            Constants.Logwrite("DatabaseHandler:AddCircularDetails",
                    "Exception 3790" + err.getMessage() + ":::StactTrace:"
                            + err.getStackTrace().toString());

            Constants.writelog("DatabaseHandler:AddCircularDetails",
                    "Exception 3790" + err.getMessage() + ":::StactTrace:"
                            + err.getStackTrace().toString());
            Ins_Status = 0;

        } finally {
            close(myWritableDb); // Closing database connection
        }
        return Ins_Status;
    }

    // Update Circular Table

    public int UpdateCircular(Contact contact) {
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        int result = 0;
        try {
            ContentValues values = new ContentValues();

            // values.put(KEY_CIRCULAR_STUDID, contact.getcirstudid());
            // values.put(KEY_CIRCULAR_SCHOOLID, contact.getcirschoolid());
            values.put(KEY_CIRCULAR_YEARID, contact.getciryearid());
            values.put(KEY_CIRCULAR_GROUPID, contact.getcirgroupid());
            values.put(KEY_CIRCULAR_GROUPNAME, contact.getcirGroupname());
            // values.put(KEY_CIRCULARID, contact.getCirId());
            values.put(KEY_CIRCULAR_NAME, contact.getCirName());
            values.put(KEY_CIRCULAR_DATETEXT, contact.getcirDateText());
            values.put(KEY_CIRCULAR_PATH, contact.getcirPath());
            values.put(KEY_CIRCULAR_TICKS, contact.getcirTicks());
            values.put(KEY_CIRCULAR_ISDELETED, contact.getcirisdeleted());

            /*
             * String whereClause = "" + KEY_ACADEMIC_YEAR_ID + "=? AND " +
             * KEY_ACADEMIC_SCHOOLID + " =? AND " + KEY_ACADEMIC_STUDID + " =?";
             */
            String whereClause = "" + KEY_CIRCULAR_STUDID + "=? AND "
                    + KEY_CIRCULAR_SCHOOLID + "=? AND " + KEY_CIRCULARID + "=?";
            String whereArgs[] = new String[]{
                    String.valueOf(contact.getcirstudid()),
                    String.valueOf(contact.getcirschoolid()),
                    String.valueOf(contact.getCirId())};
            // Update Row
            result = myWritableDb.update(TABLE_CIRCULAR_DETAILS, values,
                    whereClause, whereArgs);
        } catch (Exception err) {

            Constants.Logwrite("DatabaseHandler",
                    "UpdateCircular() Exception:3867" + err.getMessage()
                            + "StactTrace:" + err.getStackTrace());
            Constants.writelog("DatabaseHandler",
                    "UpdateCircular() Exception:3867" + err.getMessage()
                            + "::::" + err.getStackTrace());

        } finally {
            close(myWritableDb);
        }
        return result;
    }

    // Check Circular exist

    public boolean CheckCircularInserted(int Studid, int SchoolID,
                                         int CircularId) {
        boolean isthere = true;
        int cnt = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            String countQuery = "SELECT  * FROM " + TABLE_CIRCULAR_DETAILS
                    + " where " + KEY_CIRCULAR_STUDID + "=" + Studid + " and "
                    + KEY_CIRCULAR_SCHOOLID + "=" + SchoolID + " and "
                    + KEY_CIRCULARID + "=" + CircularId + "";

            Cursor cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed()) {
                cursor.moveToFirst();
                cnt = cursor.getCount();
                if (cnt > 0) {
                    isthere = true;
                } else {
                    isthere = false;
                }
                cursor.close();
            } else {
                isthere = false;
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:CheckCircularInserted",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
            Constants.writelog(
                    "Database:CheckCircularInserted",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return isthere;
    }

    // update Check circular is deleted
    public boolean CheckCircularDeleted(int Studid, int SchoolID,
                                        int CircularId, String CirName, int isdeleted) {
        boolean isthere = true;
        int cnt = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            String countQuery = "SELECT  * FROM " + TABLE_CIRCULAR_DETAILS
                    + " where " + KEY_CIRCULAR_STUDID + "=" + Studid + " and "
                    + KEY_CIRCULAR_SCHOOLID + "=" + SchoolID + " and "
                    + KEY_CIRCULARID + "=" + CircularId + " and "
                    + KEY_CIRCULAR_NAME + "='" + CirName + "' and "
                    + KEY_CIRCULAR_ISDELETED + "=" + isdeleted + "";

            Cursor cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed()) {
                cursor.moveToFirst();
                cnt = cursor.getCount();
                if (cnt > 0) {
                    isthere = true;
                } else {
                    isthere = false;
                }
                cursor.close();
            } else {
                isthere = false;
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:3875 CheckCircularDeleted",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
            Constants.writelog(
                    "Database:3875 CheckCircularDeleted",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return isthere;
    }

    // update circular set is circualar deleted.
    public int SetCircularIsDeleted(String studid, String SchoolID,
                                    String YearId, String circularid, String groupid) {

        int count = 0;
        try {
            SQLiteDatabase myWritableDb = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_CIRCULAR_ISDELETED, 1);
            String table = TABLE_CIRCULAR_DETAILS;
            String whereClause = KEY_CIRCULAR_STUDID + "=? AND "
                    + KEY_CIRCULAR_SCHOOLID + " =? AND " + KEY_CIRCULAR_YEARID
                    + " =? AND " + KEY_CIRCULARID + " =? AND "
                    + KEY_CIRCULAR_GROUPID + " =?";
            String whereArgs[] = new String[]{studid, SchoolID, YearId,
                    circularid, groupid};
            count = myWritableDb.update(table, values, whereClause, whereArgs);
        } catch (Exception err) {
            Constants.Logwrite("SetCircularIsDeleted", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());
        } finally {
        }
        return count;

    }

    // Check Album File Inserted
    public boolean CheckAlbumDetailsInserted(int Studid, int SchoolID,
                                             int ClassSecId, int AlbumId, String filename) {
        boolean isthere = true;
        int cnt = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            //vishwa
            //KEY_ALBUM_PHOTOFILENAME-->KEY_ALBUM_ALBUMURL
            String countQuery = "SELECT  * FROM " + TABLE_STUDENT_ALBUM_DETAILS
                    + " where " + KEY_ALBUM_STUDID + "=" + Studid + " and "
                    + KEY_ALBUM_SCHOOLID + "=" + SchoolID + " and "
                    + KEY_ALBUM_CLASSSECID + "=" + ClassSecId + " and "
                    + KEY_ALBUM_ALBUMID + "=" + AlbumId + " and "
                    + KEY_ALBUM_ALBUMURL + "='" + filename + "'";
            Cursor cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed()) {
                cursor.moveToFirst();
                cnt = cursor.getCount();
                if (cnt > 0) {
                    isthere = true;
                } else {
                    isthere = false;
                }
                cursor.close();
            } else {
                isthere = false;
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:CheckAlbumDetailsInserted",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return isthere;
    }

    // Get Album Details using StudId and SchoolId
    public List<Contact> getAlbumDetails(int StudId, int SchoolId) {
        List<Contact> contactList = new ArrayList<Contact>();
        Cursor cursor = null;
        SQLiteDatabase myWritableDb = null;
        try {
            myWritableDb = this.getWritableDatabase();
            String selectQuery = "SELECT  * FROM "
                    + TABLE_STUDENT_ALBUM_DETAILS + " where "
                    + KEY_ALBUM_STUDID + "=" + StudId + " and "
                    + KEY_ALBUM_SCHOOLID + "=" + SchoolId + " ORDER BY "
                    + KEY_ALBUM_DATETICKS + " ASC";
            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();

                    int AlbumStudId = Integer.parseInt(cursor.getString(1));
                    int AlbumId = Integer.parseInt(cursor.getString(2));
                    String AlbumName = cursor.getString(3);
                    String PhotoFileName = cursor.getString(4);
                    String Albumurl = cursor.getString(5);
                    int AlbumSchoolId = Integer.parseInt(cursor.getString(6));
                    int AlbumClassSecId = Integer.parseInt(cursor.getString(7));
                    long AlbumDateTicks = Long.parseLong(cursor.getString(8));
                    String AlbumDate = cursor.getString(9);

                    contact.SetSchoolId(AlbumStudId);
                    contact.setAlbumId(AlbumId);
                    contact.setAlbumName(AlbumName);
                    contact.setAlbumPhotofile(PhotoFileName);
                    contact.setAlbumurl(Albumurl);
                    contact.setAlbumSchoolId(AlbumSchoolId);
                    contact.setAlbumClassSecId(AlbumClassSecId);
                    contact.setAlbumdateticks(AlbumDateTicks);
                    contact.setAlbumDatetime(AlbumDate);
                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:getAlbumDetails", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }

    //Get Detail album wise

    public List<Contact> getAlbumwiseDetails(int StudId, int SchoolId, int ID) {
        List<Contact> contactList = new ArrayList<Contact>();
        Cursor cursor = null;
        SQLiteDatabase myWritableDb = null;
        try {
            myWritableDb = this.getWritableDatabase();
            String selectQuery = "SELECT  * FROM "
                    + TABLE_STUDENT_ALBUM_DETAILS + " where "
                    + KEY_ALBUM_STUDID + "=" + StudId + " and "
                    + KEY_ALBUM_SCHOOLID + "=" + SchoolId + " and "
                    + KEY_ALBUM_ALBUMID + "=" + ID + " ORDER BY "
                    + KEY_ALBUM_DATETICKS + " ASC";
            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();

                    int AlbumStudId = Integer.parseInt(cursor.getString(1));
                    int AlbumId = Integer.parseInt(cursor.getString(2));
                    String AlbumName = cursor.getString(3);
                    String PhotoFileName = cursor.getString(4);
                    String Albumurl = cursor.getString(5);
                    int AlbumSchoolId = Integer.parseInt(cursor.getString(6));
                    int AlbumClassSecId = Integer.parseInt(cursor.getString(7));
                    long AlbumDateTicks = Long.parseLong(cursor.getString(8));
                    String AlbumDate = cursor.getString(9);

                    contact.SetSchoolId(AlbumStudId);
                    contact.setAlbumId(AlbumId);
                    contact.setAlbumName(AlbumName);
                    contact.setAlbumPhotofile(PhotoFileName);
                    contact.setAlbumurl(Albumurl);
                    contact.setAlbumSchoolId(AlbumSchoolId);
                    contact.setAlbumClassSecId(AlbumClassSecId);
                    contact.setAlbumdateticks(AlbumDateTicks);
                    contact.setAlbumDatetime(AlbumDate);
                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:getAlbumDetails", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }


    // Delete All Album
    public int DeleteAllAlbum() {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {

            myWritableDb.execSQL("delete from " + TABLE_STUDENT_ALBUM_DETAILS);
            count = 1;
        } catch (Exception err) {

        } finally {
            close(myWritableDb);
        }
        return count;
    }

    public String getmobileNo(String studId, String SchoolId) {
        String mobileNo = "";
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT  " + KEY_PH_NO + " FROM "
                    + TABLE_CONTACTS1 + " where "
                    + KEY_STUD_ID + "=" + studId + " and "
                    + KEY_SCHOOLID + "=" + SchoolId + " ORDER BY "
                    + KEY_ID + " DESC";
            cursor = myWritableDb.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                mobileNo = cursor.getString(0);
            }
        } catch (Exception err) {
            Constants.Logwrite("Database",
                    "getmobileNo()5564:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return mobileNo;
    }

    public String getpin(String studId, String SchoolId) {
        String pin = "";
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT  " + KEY_LOG_PIN + " FROM "
                    + TABLE_CONTACTS1 + " where "
                    + KEY_STUD_ID + "=" + studId + " and "
                    + KEY_SCHOOLID + "=" + SchoolId + " ORDER BY "
                    + KEY_ID + " DESC";
            cursor = myWritableDb.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                pin = cursor.getString(0);
            }
        } catch (Exception err) {
            Constants.Logwrite("Database",
                    "getmobileNo()5564:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return pin;
    }

    // Delete All Album
    public int DeleteAllAlbum(int StudId, int SchoolId) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            myWritableDb.execSQL("delete from " + TABLE_STUDENT_ALBUM_DETAILS
                    + " where " + KEY_ALBUM_STUDID + " and "
                    + KEY_ALBUM_SCHOOLID + " ");
            count = 1;
        } catch (Exception err) {

        } finally {
            close(myWritableDb);
        }
        return count;
    }

    // DeleteAll Student DeleteStudentPhotoGallery From Table
    public int DeleteStudentPhotoGallery(int studid, int schoolid) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {

            myWritableDb.execSQL("delete from " + TABLE_STUDENT_ALBUM_DETAILS
                    + " where " + KEY_ALBUM_STUDID + "=" + studid + " and "
                    + KEY_ALBUM_SCHOOLID + "=" + schoolid + "");
            count = 1;
        } catch (Exception err) {
            Constants.Logwrite("Database:DeleteStudentPhotoGallery",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    //practice test added by Jameela

    public List<Contact> getPracticeTestDetailByID(int schoolId, int studId, int testId) {
        List<Contact> contactList = new ArrayList<Contact>();
        Cursor cursor = null;
        SQLiteDatabase myWritableDb = null;
        try {
            myWritableDb = this.getWritableDatabase();
            String countQuery = "SELECT  * FROM " + TABLE_PRACTICE_TEST_DETAILS
                    + " where " + KEY_PRACTICE_TEST_STUD_ID + "=" + studId + " and "
                    + KEY_PRACTICE_TEST_SCHOOL_ID + "=" + schoolId + " and "
                    + KEY_PRACTICE_TEST_TEST_ID + "=" + testId;

            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(countQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    int rowId = cursor.getInt(0);
                    String ansString = cursor.getString(4);
                    String ticksPracticeTest = cursor.getString(5);
                    //changed on 06_06_2020
                  //  String startTimePracticeTest = cursor.getString(6);
                    contact.setROWID(rowId);
                    contact.setTestAnsString(ansString);
                    contact.setTestTicks(ticksPracticeTest);
                 //   contact.setTestStartTime(startTimePracticeTest);
                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:getPracticeTestDetailByID", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return contactList;
    }
    public int  getCountPracticeTestDetail() {
       int count=0;
        Cursor cursor = null;
        SQLiteDatabase myWritableDb = null;
        try {
            myWritableDb = this.getWritableDatabase();
            String countQuery = "SELECT  * FROM " + TABLE_PRACTICE_TEST_DETAILS;

            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(countQuery, null);
            // looping through all rows and adding to list
           if(cursor!=null && cursor.getCount()>0){
               count=cursor.getCount();
           }
        } catch (Exception err) {
            Constants.Logwrite("Database:getPracticeTestDetailByID", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return count;
    }

    public List<Contact> insertTestDetails(int schoolId, int studId, int testId) {
        SQLiteDatabase myWritableDb = null;
        List<Contact> contactList = new ArrayList<Contact>();
        try {
            myWritableDb = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_PRACTICE_TEST_STUD_ID, studId); // MsgId
            values.put(KEY_PRACTICE_TEST_SCHOOL_ID, schoolId); // Msgtext
            values.put(KEY_PRACTICE_TEST_TEST_ID, testId);
            // Inserting Row
            myWritableDb.insert(TABLE_PRACTICE_TEST_DETAILS, null, values);
            contactList = getPracticeTestDetailByID(schoolId, studId, testId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactList;
    }

    public int updateTestTicksAndStartDate(int testrowId, String testTicks/*,String startTestTime*/) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(TEST_TIMESTAMP, testTicks);
         //   values.put(TEST_CURRENTSTART_DATE, startTestTime);
            String table = TABLE_PRACTICE_TEST_DETAILS;
            String whereClause = "" + KEY_PRACTICE_TEST_ROW_ID + "=?";
            String whereArgs[] = new String[]{String.valueOf(testrowId)};
            count = myWritableDb.update(table, values, whereClause, whereArgs);
        } catch (Exception err) {

            Constants.Logwrite("Database:updateTestAnsString",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }


    public int updateTestAnsString(int testrowId, String ansString) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_PRACTICE_TEST_ANS_STRING, ansString);
            String table = TABLE_PRACTICE_TEST_DETAILS;
            String whereClause = "" + KEY_PRACTICE_TEST_ROW_ID + "=?";
            String whereArgs[] = new String[]{String.valueOf(testrowId)};
            count = myWritableDb.update(table, values, whereClause, whereArgs);
        } catch (Exception err) {

            Constants.Logwrite("Database:updateTestAnsString",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    public long getPracticeTestElapsedTimeByID(int testrowId, String quesId) {
        List<Contact> contactList = new ArrayList<Contact>();
        Cursor cursor = null;
        long elapsedTime = 0;
        SQLiteDatabase myWritableDb = null;
        try {
            myWritableDb = this.getWritableDatabase();
            String countQuery = "SELECT  * FROM " + TABLE_PRACTICE_TEST_ElapsedTimeDETAILS
                    + " where " + KEY_PRACTICE_TEST_Wise_RowID + "=" + testrowId + " and "
                    + KEY_PRACTICE_TEST_QuestionELAPSED_TIME + "='" + quesId + "'";

            // String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            cursor = myWritableDb.rawQuery(countQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    int rowId = cursor.getInt(0);
                    elapsedTime = Long.parseLong(cursor.getString(3));

                    contact.setROWID(rowId);
                    contact.setTestElpasedTime(elapsedTime);
                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:getPracticeTestElapsedTimeByID", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return elapsedTime;
    }

    public void insertUpdatePracticeTestElapseTime(int testrowId, String quesId, String elapseTime) {
        boolean isthere = true;
        int cnt = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            //vishwa
            //KEY_ALBUM_PHOTOFILENAME-->KEY_ALBUM_ALBUMURL
            String countQuery = "SELECT  * FROM " + TABLE_PRACTICE_TEST_ElapsedTimeDETAILS
                    + " where " + KEY_PRACTICE_TEST_Wise_RowID + "=" + testrowId + " and "
                    + KEY_PRACTICE_TEST_QuestionELAPSED_TIME + "='" + quesId + "'";

            Cursor cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed()) {
                cursor.moveToFirst();
                cnt = cursor.getCount();
                if (cnt > 0) {
                    isthere = true;
                    updateElpseTime(testrowId, quesId, elapseTime);
                } else {
                    isthere = false;
                    insertElpseTime(testrowId, quesId, elapseTime);
                }
                cursor.close();
            } else {
                isthere = false;
            }
        } catch (Exception err) {
            Constants.Logwrite("Database:CheckAlbumDetailsInserted",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
    }

    public void insertElpseTime(int testrowId, String quesId, String elapseTime) {
        SQLiteDatabase myWritableDb = null;
        try {
            myWritableDb = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_PRACTICE_TEST_Wise_RowID, testrowId); // MsgId
            values.put(KEY_PRACTICE_TEST_QuestionELAPSED_TIME, quesId); // Msgtext
            values.put(KEY_PRACTICE_TEST_ELAPSED_TIME, elapseTime);
            // Inserting Row
            myWritableDb.insert(TABLE_PRACTICE_TEST_ElapsedTimeDETAILS, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int updateElpseTime(int testrowId, String quesId, String elapseTime) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_PRACTICE_TEST_ELAPSED_TIME, elapseTime);
            String table = TABLE_PRACTICE_TEST_ElapsedTimeDETAILS;
            String whereClause = "" + KEY_PRACTICE_TEST_Wise_RowID + "=? AND "
                    + KEY_PRACTICE_TEST_QuestionELAPSED_TIME + " =?";
            String whereArgs[] = new String[]{String.valueOf(testrowId), quesId};
            count = myWritableDb.update(table, values, whereClause, whereArgs);
        } catch (Exception err) {

            Constants.Logwrite("Database:updateElpseTime",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    public int deleteTestRecords(int testrowId) {

        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {

            String table = TABLE_PRACTICE_TEST_ElapsedTimeDETAILS;
            String whereClause = "" + KEY_PRACTICE_TEST_Wise_RowID + "=?";
            String whereArgs[] = new String[]{String.valueOf(testrowId)};
            count = myWritableDb.delete(table, whereClause, whereArgs);

            String table1 = TABLE_PRACTICE_TEST_DETAILS;
            String whereClause1 = "" + KEY_PRACTICE_TEST_ROW_ID + "=?";
            String whereArgs1[] = new String[]{String.valueOf(testrowId)};
            count = myWritableDb.delete(table1, whereClause1, whereArgs1);

        } catch (Exception err) {
            Constants.Logwrite("Database:deleteTestRecords", "Message:" + err.getMessage()
                    + "StactTrace:" + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;

    }

    // Delete specific albums
    public int DeleteSpecificAlbum(String albumIds, int studid, int schoolid) {
        int count = 0;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {

            myWritableDb.execSQL("delete from " + TABLE_STUDENT_ALBUM_DETAILS
                    + " where " + KEY_ALBUM_STUDID + "=" + studid + " and "
                    + KEY_ALBUM_SCHOOLID + "=" + schoolid + " and " + KEY_ALBUM_ALBUMID + " in (" + albumIds + ")");
            count = 1;
        } catch (Exception err) {
            Constants.Logwrite("Database:DeleteStudentPhotoGallery",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            close(myWritableDb);
        }
        return count;
    }

    // Change By Jaydeep 04/05/2017

    public String GetTodayHomeWorkCount(String studid, String schoolid, String msgtype,
                                        String YearId, int days, int months, int years) {
        String result = "0";
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        Cursor cursor = null;
        try {

            String selectQuery = "SELECT * FROM " + TABLE_SMS2 + " where "
                    + KEY_SMS_STUDID + "=" + studid + " and "
                    + KEY_SMS_SCHOOLID + "=" + schoolid + " and "
                    + KEY_SMS_Moduleid + " IN (" + msgtype + ") and "
                    + KEY_SMS_DAY + "=" + days + " and " + KEY_SMS_MONTH + "=" + months + " and " + KEY_SMS_YEAR + "=" + years + " and "
                    + KEY_SMS_YEAR_ID + "=" + YearId + " ORDER BY " + KEY_MSGID
                    + " DESC";

            cursor = myWritableDb.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                //do {
                Contact contact = new Contact();
                contact.setSMSID(Integer.parseInt(cursor.getString(1)));
                String aa = cursor.getString(2);
                String[] splitedstr = null;
                splitedstr = aa.split("##,@@");
                String[] parts = splitedstr[0].split("@#@#@#");
                int size = parts.length;
                result = size + "";
                //} while (cursor.moveToNext());
            }

        } catch (Exception err) {

            Constants.Logwrite("Database:", "GetAllSMSDetails:2476" + err.getMessage());

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return result;
    }

    public String GetStudAttendanceCount(String StudId, String Schoolid, String Yearid) {
        Cursor cursor = null;
        String result = "0/0";
        String countQuery = "SELECT " + KEY_ATT_DETAILS + " FROM "
                + TABLE_STUD_ATTENDANCE + " where " + KEY_ATT_STUD_ID + "="
                + StudId + " and " + KEY_ATT_SCHOOL_ID + "=" + Schoolid
                + " and " + KEY_ATT_YEAR_ID + "=" + Yearid;
        SQLiteDatabase myWritableDb = null;
        myWritableDb = this.getWritableDatabase();
        try {
            cursor = myWritableDb.rawQuery(countQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String std = cursor.getString(cursor.getColumnIndex(KEY_ATT_DETAILS));
                String parts[] = std.split(",");
                String totalDays[] = parts[0].split(":");
                String presentDays[] = parts[1].split(":");
                result = presentDays[1] + "/" + totalDays[1];
            }
        } catch (Exception err) {
            Constants.Logwrite("Database: GetStuAttendanceDetails 2273",
                    "Message:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            close(myWritableDb);
        }
        return result;
    }
}
