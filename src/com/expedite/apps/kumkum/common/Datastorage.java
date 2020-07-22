package com.expedite.apps.kumkum.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.expedite.apps.kumkum.constants.Constants;

public class Datastorage {

    public int StudId = 0;
    public int SchoolId = 0;
    public static String isFirstTime = "isFirstTime";
    private static String prename = "DataStorage";
    private static SharedPreferences storagepref;
    private static SharedPreferences.Editor shared_preferences_editor;

    public static void ClearSharedPreferences(Context cn) {
        SharedPreferences preferences = cn.getSharedPreferences(prename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void setSession(Context cntxt, String key, String value) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        storagepref.edit().putString(key, value).apply();
    }

    public static String getSession(Context cntxt, String key) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString(key, "");
    }

    public static Boolean getSessionBoolean(Context cntxt, String key) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getBoolean(key, false);
    }

    public static void setSessionBoolean(Context cntxt, String key, Boolean value) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        storagepref.edit().putBoolean(key, value).apply();
    }

    public static void SetLogTime(Context cntxt, int starttime, int endtime) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            int diff = endtime - starttime;
            shared_preferences_editor.putInt("TotTime", diff);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetSchoolId", "Message:" + err.getMessage());
        }
    }

    public static void SetUserId(Context cntxt, String UserId) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("UserId", UserId);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetSchoolId", "Exception:34 " + err.getMessage());
            Constants.writelog("Datastorage", "SetUserId() Exception:34 "
                    + err.getMessage() + ":::" + err);
        }
    }

    public static String GetLastOpenedGroup(Context cntxt) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            return storagepref.getString("LastOpenedGroup", null);
        } catch (Exception ex) {
            Constants.Logwrite("Datastorage",
                    "GetLastOpenedGroup()45 Exception" + ex.getMessage() + "::::" + ex);
            return "";
        }
    }

    public static void SetLastOpenedGroup(Context cntxt, String lastopendgroup) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("LastOpenedGroup",
                    lastopendgroup);
            shared_preferences_editor.apply();
        } catch (Exception ex) {
            Constants.Logwrite("Datastorage",
                    "SetLastOpenedGroup()45 Exception" + ex.getMessage()
                            + "::::" + ex);
        }
    }

    public static String GetUserId(Context cntxt) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            return storagepref.getString("UserId", null);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void SetLastAbsentMessageIndex(Context cntxt, int number) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putInt("LastAbsentMessageIndex", number);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("datastorage", "Exception 85:" + err.getMessage());
            Constants.writelog("datastorage 85:",
                    "Exception:" + err.getMessage());
        }
    }

    public static int GetLastAbsentMessageIndex(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);

        return storagepref.getInt("LastAbsentMessageIndex", 0);

    }

    public static void SetLastAutoUpdateMessageDay(Context cntxt, int day) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putInt("LastAutoUpdateMsgDay", day);
            shared_preferences_editor.apply();

        } catch (Exception err) {
            Constants.Logwrite("datastorage", "Exception 107:" + err.getMessage());
            Constants.writelog("datastorage 107:",
                    "Exception:" + err.getMessage());
        }
    }

    public static String GetTransactionDetail(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("trDetails", "");
    }

    public static void SetTransactionDetail(Context cntxt, String details) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("trDetails", details);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.writelog("datastorage 122:",
                    "SetTransactionDetail() Exception:" + err.getMessage());
        }
    }

    public static int GetLastAutoUpdateMessageDay(Context cntxt) {

        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getInt("LastAutoUpdateMsgDay", 0);

    }

    public static void SetLastAutoUpdateAttendance(Context cntxt, int day) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putInt("LastAutoUpdateAttendance", day);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("datastorage", "Exception:133:" + err.getMessage());
            Constants.writelog("datastorage 133:",
                    "Exception:" + err.getMessage());
        }
    }

    public static int GetLastAutoUpdateAttendance(Context cntxt) {

        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getInt("LastAutoUpdateAttendance", 0);

    }

    public static void SetLastAutoUpdateProfile(Context cntxt, int day) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putInt("LastAutoUpdateProfile", day);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("datastorage", "Exception:159:" + err.getMessage());
            Constants.writelog("datastorage 159:",
                    "Exception:" + err.getMessage());
        }
    }

    public static int GetLastAutoUpdateProfile(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getInt("LastAutoUpdateProfile", 0);

    }

    public static void SetLastAutoUpdateExamDay(Context cntxt, int day) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putInt("LastAutoUpdateExamDay", day);
            shared_preferences_editor.apply();

        } catch (Exception err) {
            Constants.Logwrite("datastorage 184", "Exception:" + err.getMessage());
            Constants.writelog("datastorage 184",
                    "Exception:" + err.getMessage());
        }
    }

    public static int GetLastAutoUpdateExamDay(Context cntxt) {

        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getInt("LastAutoUpdateExamDay", 0);

    }

    public static void SetLastAutoUpdateExamMarksheetDay(Context cntxt, int day) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putInt("LastAutoUpdateExamMarksheetDay",
                    day);
            shared_preferences_editor.apply();

        } catch (Exception err) {
            Constants.Logwrite("datastorage 206", "Exception:" + err.getMessage());
            Constants.writelog(
                    "datastorage 206:",
                    "SetLastAutoUpdateExamMarksheetDay() Exception:"
                            + err.getMessage());
        }
    }

    public static int GetLastAutoUpdateExamMarksheetDay(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getInt("LastAutoUpdateExamMarksheetDay", 0);
    }

    public static int GetLastAutoUpdateNoticeDay(Context cntxt) {

        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getInt("LastAutoUpdateNoticeDay", 0);
    }

    public static void SetLastAutoUpdateNoticeDay(Context cntxt, int day) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putInt("LastAutoUpdateNoticeDay", day);
            shared_preferences_editor.apply();

        } catch (Exception err) {
            Constants.Logwrite("datastorage 212:", "Exception:" + err.getMessage());
            Constants.writelog("datastorage83",
                    "Exception:" + err.getMessage());
        }
    }

    public static int GetLastAutoUpdatePhotoDay(Context cntxt) {

        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getInt("LastAutoUpdatePhotoDay", 1);
    }

    public static void SetLastAutoUpdatePhotoDay(Context cntxt, int day) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putInt("LastAutoUpdatePhotoDay", day);
            shared_preferences_editor.apply();

        } catch (Exception err) {
            Constants.Logwrite("datastorage 233:", "Exception:" + err.getMessage());
            Constants.writelog("datastorage 231:",
                    "Exception:" + err.getMessage());
        }
    }

    public static void SetLogSendComplete(Context cntxt, int day) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putInt("logsend", day);
            shared_preferences_editor.apply();
            Constants.writelog("datastorage", "SetLogSendComplete() Day:"
                    + day);
        } catch (Exception err) {
            Constants.Logwrite("datastorage", "Exception:270:" + err.getMessage());
            Constants.writelog("datastorage 270:", "Exception:" + err.getMessage());
        }
    }

    public static int GetLogSendComplete(Context cntxt) {

        storagepref = cntxt.getSharedPreferences(prename, 1);
        return storagepref.getInt("logsend", 1);
    }


    public static void SetCurrentYearisDefYear(Context cntxt, int flag) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putInt("CurrYearIsDefYear", flag);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("datastorage63", "Message:" + err.getMessage());
        }
    }

    public static int GetCurrentYearisDefYear(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getInt("CurrYearIsDefYear", 0);
    }

    public static void SetSchoolId(Context cntxt, String SchoolId) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("SchoolId", SchoolId);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetSchoolId", "Message:" + err.getMessage());
        }
    }

    public static String GetSchoolId(Context cntxt) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            return storagepref.getString("SchoolId", null);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void SetlineForlog(Context cntxt, int linecount) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putInt("linecount", linecount);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetlineForlog", "msg:" + err.getMessage());
        }
    }

    public static int GetlineForlog(Context cntxt) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            return storagepref.getInt("linecount", 1000);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void SetStudentID(Context cntxt, String StudentId) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("StudentId", StudentId);
            // For close Last expanded group.
            shared_preferences_editor.putString("LastOpenedGroup", "0");

            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetStudentID", "Message:" + err.getMessage());
        }
    }

    public static String GetStudentId(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("StudentId", null);
    }

    public static void SetPhoneNumber(Context cntxt, String PhoneNumber) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("PhNumber", PhoneNumber);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetPhoneNumber", "Message:" + err.getMessage());
        }
    }

    public static String GetPhoneNumber(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("PhNumber", null);
    }

    public static void SetLoginPin(Context cntxt, String LoginPin) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("LoginPin", LoginPin);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetLoginPin", "Message:" + err.getMessage());
        }
    }

    public static String GetLoginPin(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("LoginPin", null);
    }

    public static void SetStudentName(Context cntxt, String StudentName) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("StudentName", StudentName);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetStudentName", "Message:" + err.getMessage());
        }
    }

    public static String GetStudentName(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("StudentName", null);
    }

    public static void SetClassId(Context cntxt, String ClassId) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("ClassId", ClassId);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetClassId", "Message:" + err.getMessage());
        }
    }

    public static String GetClassId(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("ClassId", null);
    }

    public static void SetClassSecId(Context cntxt, String ClassSecId) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("ClassSecId", ClassSecId);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetClassSecId", "Message:" + err.getMessage());
        }
    }

    public static String GetClassSecId(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("ClassSecId", null);
    }

    public static void SetClassSecName(Context cntxt, String ClassSecName) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("ClassSecName", ClassSecName);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetClassSecName", "Message:" + err.getMessage());
        }
    }

    public static String GetClassName(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("ClassSecName", null);
    }

    public static void SetEnrollDate(Context cntxt, String EnrollDate) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("EnrollDate", EnrollDate);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetEnrollDate", "Message:" + err.getMessage());
        }
    }

    public static String GetEnrollDate(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("EnrollDate", null);
    }

    public static void LastUpdatedtime(Context cntxt, String LastUpdatedTime) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("LastUpdatedTime",
                    LastUpdatedTime);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("LastUpdatedtime", "Message:" + err.getMessage());
        }
    }

    public static String GetLastUpdatedtime(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("LastUpdatedTime", null);
    }

    public static void SetCurrentYearId(Context cntxt, String CurrentYearId) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("CurrentYearId", CurrentYearId);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetCurrentYearId", "Message:" + err.getMessage());
        }
    }

    public static String GetCurrentYearId(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("CurrentYearId", null);
    }

    public static void SetAcademicYear(Context cntxt, String AcademicYear) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("AcademicYearActivity", AcademicYear);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetCurrentYearId", "Message:" + err.getMessage());
        }
    }

    public static String GetAcademicYear(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("AcademicYearActivity", null);
    }

    public static void AcaStartDate(Context cntxt, String StartDate) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("StartDate", StartDate);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("AcaStartDate", "Message:" + err.getMessage());
        }
    }

    public static String GetAcaStartDate(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("StartDate", null);
    }

    public static void AcaEndDate(Context cntxt, String EndDate) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("EndDate", EndDate);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("AcaEndDate", "Message:" + err.getMessage());
        }
    }

    public static String GetAcaEndDate(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("EndDate", null);
    }

    public static void SetHolidayStartDate(Context cntxt, String StartDate) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("HolidayStartDate", StartDate);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("AcaStartDate", "Message:" + err.getMessage());
        }
    }

    public static String GetHolidayStartDate(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("HolidayStartDate", null);
    }

    public static void SetHolidayEndDate(Context cntxt, String EndDate) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("HolidayEndDate", EndDate);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("AcaStartDate", "Message:" + err.getMessage());
        }
    }

    public static String GetHolidayEndDate(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("HolidayEndDate", null);
    }

    public static void SetMultipleAccount(Context cntxt, int flag) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putInt("IsMultipleAccount", flag);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("AcaStartDate", "Message:" + err.getMessage());
        }
    }

    public static int GetMultipleAccount(Context cntxt) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            return storagepref.getInt("IsMultipleAccount", 0);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void SetDeviceId(Context cntxt, String DeviceId) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("DeviceId", DeviceId);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetDeviceId", "Message:" + err.getMessage());
        }
    }

    public static String GetDeviceId(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("DeviceId", null);
    }

    public static void SetGetGcmRegId(Context cntxt, String SetGcmRegId) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("GCMID", SetGcmRegId);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetGetGcmRegId", "Message:" + err.getMessage());
        }
    }

    public static String GetGcmRegId(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("GCMID", null);
    }

    // SetGetGcmRegId
    public static void SetGetFcmRegId(Context cntxt, String SetFcmRegId) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("FCMID", SetFcmRegId);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetGetFcmRegId", "Message:" + err.getMessage());
        }
    }
    //GetGcmRegId
    public static String GetFcmRegId(Context cntxt) {
        storagepref = cntxt.getSharedPreferences(prename, 0);
        return storagepref.getString("FCMID", null);
    }

    public static void SetAppVersion(Context cntxt, String AppVersion) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("AppVersion", AppVersion);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Common.printStackTrace(err);
        }
    }

    public static void SetPhotoGalleryType(Context cntxt, int type) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putInt("PhotoGalleryType", type);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetPhotoGalleryType", "Message:" + err.getMessage());
        }
    }

//    public static int GetPhotoGalleryType(Context cntxt) {
//        try {
//            storagepref = cntxt.getSharedPreferences(prename, 0);
//            return storagepref.getInt("PhotoGalleryType", 0);
//        } catch (Exception err) {
//            Common.printStackTrace(err);
//            return 0;
//        }
//    }

	/*public static void SetPhotoGalleryTypeTicks(Context cntxt, String ticks) {
        try {
			storagepref = cntxt.getSharedPreferences(prename, 0);
			shared_preferences_editor = storagepref.edit();
			shared_preferences_editor.putString("PhotoGalleryTypeTicks", ticks);
			shared_preferences_editor.apply();
		} catch (Exception err) {
			Constants.Logwrite("SetPhotoGalleryType", "Message:" + err.getMessage());
		}
	}

	public static String GetPhotoGalleryTypeTicks(Context cntxt) {
		try {
			storagepref = cntxt.getSharedPreferences(prename, 0);
			return storagepref.getString("PhotoGalleryTypeTicks", "0");
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}*/


    public static String GetAppVersion(Context cntxt) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            return storagepref.getString("AppVersion", "");
        } catch (Exception err) {
            Common.printStackTrace(err);
            return "";
        }
    }

    // Get and Set Route Id
    public static void SetRouteId(Context cntxt, int Route_Id) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putInt("RouteId", Route_Id);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Common.printStackTrace(err);
        }
    }

    public static int GetRouteId(Context cntxt) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            return storagepref.getInt("RouteId", 0);
        } catch (Exception err) {
            Common.printStackTrace(err);
            return 0;
        }
    }

    // Get and Set Class section name
    public static void SetClassSectionName(Context cntxt, String classsecname) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("ClassSecName", classsecname);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Common.printStackTrace(err);
        }
    }

    public static String GetClassSectionName(Context cntxt) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            return storagepref.getString("ClassSecName", " ");
        } catch (Exception err) {
            Common.printStackTrace(err);
            return " ";
        }
    }

	/*public static void SetIsfeeshow(Context cntxt, int isfeeshow) {
        try {
			storagepref = cntxt.getSharedPreferences(prename, 0);
			shared_preferences_editor = storagepref.edit();
			shared_preferences_editor.putInt("isFeeShow", isfeeshow);
			shared_preferences_editor.apply();
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	public static int GetIsfeeshow(Context cntxt) {
		try {
			storagepref = cntxt.getSharedPreferences(prename, 0);
			return storagepref.getInt("isFeeShow", 0);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	public static void SetIsPayShow(Context cntxt, int ispayshow) {
		try {
			storagepref = cntxt.getSharedPreferences(prename, 0);
			shared_preferences_editor = storagepref.edit();
			shared_preferences_editor.putInt("isPayShow", ispayshow);
			shared_preferences_editor.apply();
		} catch (Exception err) {
			Constants.writelog("Datastorage", "SetIsPayShow() MSG:" + err.getMessage());
			err.printStackTrace();
		}
	}

	public static int GetIsPayShow(Context cntxt) {
		try {
			storagepref = cntxt.getSharedPreferences(prename, 0);
			return storagepref.getInt("isPayShow", 0);
		} catch (Exception e) {
			Constants.writelog("Datastorage", "GetIsPayShow() MSG:" + e.getMessage());
			e.printStackTrace();
			return 0;
		}
	}*/

    public static int GetLogTime(Context cntxt) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            return storagepref.getInt("TotTime", 0);
        } catch (Exception err) {
            Common.printStackTrace(err);
            return 0;
        }
    }

    public static void SetDBStatus(Context cntxt, String DBStatus) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("DBStatus", DBStatus);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetSchoolId", "Message:" + err.getMessage());
        }
    }

    public static String GetDBUpdateStatus(Context cntxt) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            return storagepref.getString("DBStatus", "false");
        } catch (Exception err) {
            Common.printStackTrace(err);
            return "";
        }
    }

    public static void SetNotificationTry(Context cntxt, int trynumber) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putInt("NotificationTry", trynumber);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetNotificationTry", "Message:" + err.getMessage());
        }
    }

    public static int GetNotificationTry(Context cntxt) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            return storagepref.getInt("NotificationTry", 0);
        } catch (Exception err) {
            Common.printStackTrace(err);
            return 0;
        }
    }

    public static void SetTripId(Context cntxt, String tripid) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            shared_preferences_editor = storagepref.edit();
            shared_preferences_editor.putString("TripId", tripid);
            shared_preferences_editor.apply();
        } catch (Exception err) {
            Constants.Logwrite("SetTripId", "Message:" + err.getMessage());
        }
    }

    public static String GetTripId(Context cntxt) {
        try {
            storagepref = cntxt.getSharedPreferences(prename, 0);
            return storagepref.getString("TripId", "");
        } catch (Exception err) {
            Common.printStackTrace(err);
            return "";
        }
    }

}
