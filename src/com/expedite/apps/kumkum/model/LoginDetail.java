package com.expedite.apps.kumkum.model;

public class LoginDetail {

    private static String PhoneNo = "";
    private static String UserId = "";
    private static Integer Log_Pin;
    private static String sid = "";
    private static String pin = "";
    private static String Stud_Id = "";
    private static String School_Id = "";
    private static String Current_Year_Id = "";
    private static String ClassId = "";
    private static String Class_SecId = "";
    private static String Current_Date = "";
    private static String Selected_Exam_Year = "";
    private static String Selected_Exam_Year_Stud_Class_Id = "";
    private static String Selected_Exam_Year_Stud_Class_Sec_Id = "";
    private static String ClassName = "";
    private static Boolean IsSingleUser;
    private static String EnrollDate = "";
    private static String UserPhone = "";
    private static String LastUpdatedTime = "";
    private static String Selected_Attendance_Year_Text = "";
    private static Integer Selected_Attendance_Year_Id;
    private static String App_Version = "";
    private static String GCM_REG_ID = "";
    private static String Device_Id = "";
    private static String academicyear = "";
    private static String OldAccount = "";
    private static String Selected_Exam_Year_Id = "";
    private static String Academic_Year_Start_Date = "";
    private static String Academic_Year_End_Date = "";
    private static String Holiday_Start_Date = "";
    private static String Holiday_End_Date = "";
    private static int Is_Multiple_Account;
    private static String StudentName = "";
    private static String Updated_Time = "";
    private static String LastUpdatedTimeDB = "";
    private static String ClassSectionName = "";
    private static String FCM_REG_ID = "";

    public static String getClassSectionName() {
        return ClassSectionName;
    }

    public static void setClassSectionName(String Classsectionname) {
        LoginDetail.ClassSectionName = Classsectionname;
    }


    // Academic Year Start Date
    public static String getStudentName() {
        return StudentName;
    }

    public static void setStudentName(String Name) {
        LoginDetail.StudentName = Name;
    }

    // Count Account Details
    public static int IsMultipleAccount() {
        return Is_Multiple_Account;
    }

    public static void setIsMultipleAccount(int mul) {
        LoginDetail.Is_Multiple_Account = mul;
    }

    // Academic Year Start Date
    public static String getAcademicYearStartDate() {
        return Academic_Year_Start_Date;
    }

    public static void setAcademicYearStartDate(String AstartDate) {
        LoginDetail.Academic_Year_Start_Date = AstartDate;
    }

    //
    // Academic End Date
    public static String getAcademicYearEndDate() {
        return Academic_Year_End_Date;
    }

    public static void setAcademicYearEndDate(String EDate) {
        LoginDetail.Academic_Year_End_Date = EDate;
    }

    //
    // Holiday Start Date
    public static String getHolidayStartDate() {
        return Holiday_Start_Date;
    }

    public static void setHolidayStartDate(String EDate) {
        LoginDetail.Holiday_Start_Date = EDate;
    }

    //

    // Holiday End Date
    public static String getHolidayEndDate() {
        return Holiday_End_Date;
    }

    public static void setHolidayEndDate(String HEDate) {
        LoginDetail.Holiday_End_Date = HEDate;
    }

    //
    public static String getSelectedExamYearId() {
        return Selected_Exam_Year_Id;
    }

    public static void setSelectedExamYearId(String yearid) {
        LoginDetail.Selected_Exam_Year_Id = yearid;
    }

    public static String getOldAccount() {
        return OldAccount;
    }

    public static void setOldAccount(String OldAccount) {
        LoginDetail.OldAccount = OldAccount;
    }

    public static String getAcademicYear() {
        return academicyear;
    }

    public static void setAcademicyear(String academicyear) {
        LoginDetail.academicyear = academicyear;
    }

    public static String getDeivce_ID() {
        return Device_Id;
    }

    public static void setDeivce_ID(String Deivce_ID) {
        LoginDetail.Device_Id = Deivce_ID;
    }

    public static String getGCM_REG_ID() {
        return GCM_REG_ID;
    }

    public static void setGCM_REG_ID(String GCM_REG_ID) {
        LoginDetail.GCM_REG_ID = GCM_REG_ID;
    }
    // getGCM_REG_ID()
    public static String getFCM_REG_ID() {
        return FCM_REG_ID;
    }
    // setGCM_REG_ID
    public static void setFCM_REG_ID(String FCM_REG_ID) {
        LoginDetail.FCM_REG_ID = FCM_REG_ID;
    }
    public static String getAppVersion() {
        return App_Version;
    }

    public static void setAppVersion(String App_Version) {
        LoginDetail.App_Version = App_Version;
    }

    public static Integer getSelectedAttendanceYearId() {
        return Selected_Attendance_Year_Id;
    }

    public static void setgetSelectedAttendanceYearId(Integer Attend_Id) {
        LoginDetail.Selected_Attendance_Year_Id = Attend_Id;
    }

    public static String getSelectedAttendanceYearText() {
        return Selected_Attendance_Year_Text;
    }

    public static void setgetSelectedAttendanceYearText(String selatttext) {
        LoginDetail.Selected_Attendance_Year_Text = selatttext;
    }

    public static String getLastUpdatedTime() {
        return LastUpdatedTime;
    }

    public static void setLastUpdatedTime(String LastUpdatedTime) {
        LoginDetail.LastUpdatedTime = LastUpdatedTime;
    }
    //
    public static String getLastUpdatedTimeDB() {
        return LastUpdatedTimeDB;
    }

    public static void setLastUpdatedTimeDB(String LastUpdatedTimeDB1) {
        LoginDetail.LastUpdatedTimeDB = LastUpdatedTimeDB1;
    }

    //
    public static String getUserPhoneNumber() {
        return UserPhone;
    }

    public static void setUserPhoneNumber(String UserPhone) {
        LoginDetail.UserPhone = UserPhone;
    }

    public static String getStudentEnrollDate() {
        return EnrollDate;
    }

    public static void setStudentEnrollDate(String EnrollDate) {
        LoginDetail.EnrollDate = EnrollDate;
    }

    public static String getSelectedYearClassSecId() {
        return Selected_Exam_Year_Stud_Class_Sec_Id;
    }

    public static void setSelectedYearClassSecId(String Classsecid) {
        LoginDetail.Selected_Exam_Year_Stud_Class_Sec_Id = Classsecid;
    }

    public static String getSelectedYearClassId() {
        return Selected_Exam_Year_Stud_Class_Id;
    }

    public static void setSelectedYearClassId(String Classid) {
        LoginDetail.Selected_Exam_Year_Stud_Class_Id = Classid;
    }

    public static Boolean getUserStatus() {
        return IsSingleUser;
    }

    public static Boolean setUserStatus(Boolean IsSingleUser) {
        return LoginDetail.IsSingleUser = IsSingleUser;
    }

    public static String getPhoneNo() {
        return PhoneNo;
    }

    public static void setPhoneNo(String PhoneNo) {
        LoginDetail.PhoneNo = PhoneNo;
    }

    // Get Set UserId
    public static String getUserId() {
        return UserId;
    }

    public static void setUserId(String UserId) {
        LoginDetail.UserId = UserId;
    }

    // Get Set Log Pin

    public static Integer getLogPin() {
        return Log_Pin;
    }

    public static void setLogPin(Integer Log_Pin) {
        LoginDetail.Log_Pin = Log_Pin;
    }

    // Get Set sid

    public static String getSid() {
        return sid;
    }

    public static void setsid(String sid) {
        LoginDetail.sid = sid;
    }

    // Get Set pin

    public static String getpin() {
        return pin;
    }

    public static void setpin(String pin) {
        LoginDetail.pin = pin;
    }

    // Get & Set Stud Id

    public static String getStudId() {
        return Stud_Id;
    }

    public static void setStudId(String Stud_Id) {
        LoginDetail.Stud_Id = Stud_Id;
    }

    // Get & Set School_Id

    public static String getSchoolId() {
        return School_Id;
    }

    public static void setSchoolId(String School_Id) {
        LoginDetail.School_Id = School_Id;
    }

    // Get & Set Current Year Id

    public static String getCurrentYearId() {
        return Current_Year_Id;
    }

    public static void setCurrentYearId(String Current_Year_Id) {
        LoginDetail.Current_Year_Id = Current_Year_Id;
    }

    // Get & Set Class Id

    public static String getClassId() {
        return ClassId;
    }

    public static void setClassId(String ClassId) {
        LoginDetail.ClassId = ClassId;
    }

    // Get & Set ClassName

    public static String getClassName() {
        return ClassName;
    }

    public static void setClassName(String ClassName) {
        LoginDetail.ClassName = ClassName;
    }

    // Get & Set Class Sec Id

    public static String getClassSecId() {
        return Class_SecId;
    }

    public static void setClassSecId(String Class_SecId) {
        LoginDetail.Class_SecId = Class_SecId;
    }

    public static String getCurreentDate() {
        return Current_Date;
    }

    static void setCurreentDate(String Current_Date) {
        LoginDetail.Current_Date = Current_Date;
    }

    public static String getSelectedExamYear() {
        return Selected_Exam_Year;
    }

    static void setSelectedExamYear(String Selected_Exam_Year) {
        LoginDetail.Selected_Exam_Year = Selected_Exam_Year;
    }

}
