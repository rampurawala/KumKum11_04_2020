package com.expedite.apps.kumkum.networkinterface;

import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.AppService;
import com.expedite.apps.kumkum.model.AppointmentListModal;
import com.expedite.apps.kumkum.model.Banner;
import com.expedite.apps.kumkum.model.CircularModel;
import com.expedite.apps.kumkum.model.CurriculumListModel;
import com.expedite.apps.kumkum.model.DailyDiaryCalendarModel;
import com.expedite.apps.kumkum.model.Document;
import com.expedite.apps.kumkum.model.ExamListModel;
import com.expedite.apps.kumkum.model.FoodChartListModel;
import com.expedite.apps.kumkum.model.GetGuardianHostModal;
import com.expedite.apps.kumkum.model.GetTimeModal;
import com.expedite.apps.kumkum.model.HomeModel;
import com.expedite.apps.kumkum.model.LeaveListModel;
import com.expedite.apps.kumkum.model.LeaveTypeModel;
import com.expedite.apps.kumkum.model.LibraryListModel;
import com.expedite.apps.kumkum.model.ParentsProfileListModel;
import com.expedite.apps.kumkum.model.PollTaskListModel;
import com.expedite.apps.kumkum.model.SendEmailDownlodModel;
import com.expedite.apps.kumkum.model.SocialMediaListModel;
import com.expedite.apps.kumkum.model.UploadphotosModel;
import com.expedite.apps.kumkum.model.VoiceMessageListModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @GET(Constants.GET_HOME_DETAIL)
    Call<HomeModel> GetHomeDetail(@Query("schoolid") String schoolid,
                                  @Query("yearid") String yearid,
                                  @Query("studentId") String studentId,
                                  @Query("classid") String classid,
                                  @Query("Route_id") String Routeid);

    @GET(Constants.GET_LOGIN_OTP)
    Call<AppService> GetOTPDetail(@Query("PhoneNo") String PhoneNo,
                                  @Query("appName") String appName,
                                  @Query("isDebug") String isdebug,
                                  @Query("deviceid") String deviceid,
                                  @Query("platform") String platform,
                                  @Query("codeVersion") String codeVersion);

    @GET(Constants.GET_VALIDATE_OTP)
    Call<AppService> GetValidate(@Query("PhoneNo") String PhoneNo,
                                 @Query("otp") String otp,
                                 @Query("appName") String appName,
                                 @Query("deviceid") String deviceid,
                                 @Query("platform") String platform,
                                 @Query("codeVersion") String codeVersion);


    @GET(Constants.GET_OTP_VERIFY)
    Call<AppService> GetLoginDetail(@Query("number") String number,
                                    @Query("otp") String otp,
                                    @Query("pin") String pin,
                                    @Query("deviceDetails") String deviceDetails,
                                    @Query("platform") String platform,
                                    @Query("CodeVersion") String CodeVersion);

    @GET(Constants.GET_ACCOUNT_UPDATE)
    Call<AppService> GetAccountDetail(@Query("number") String number,
                                      @Query("studentid") String studentid,
                                      @Query("schoolid") String schoolid,
                                      @Query("deviceDetails") String deviceDetails,
                                      @Query("platform") String platform,
                                      @Query("CodeVersion") String CodeVersion);

    @GET(Constants.GET_MESSAGE_DETAIL)
    Call<DailyDiaryCalendarModel> GetMessageDetail(@Query("StudId") String StudId,
                                                   @Query("SchoolId") String schoolid,
                                                   @Query("MsgId") String MsgId,
                                                   @Query("YearId") String yearid,
                                                   @Query("moduleId") String studentId,
                                                   @Query("platform") String platform);

    @GET(Constants.GET_BANNER_DETAILS)
    Call<Banner> GetBannerList(@Query("schoolid") String Schoolid,
                               @Query("studid") String Studid,
                               @Query("year_id") String year_id,
                               @Query("userVisitDetails") String userVisitDetails,
                               @Query("versionCode") String versionCode,
                               @Query("platform") String Platform);

    @GET(Constants.GET_NEW_MESSAGE_DETAIL)
    Call<DailyDiaryCalendarModel> GetNewMessageDetail(@Query("StudId") String StudId,
                                                      @Query("SchoolId") String schoolid,
                                                      @Query("MsgId") String MsgId,
                                                      @Query("YearId") String yearid,
                                                      @Query("moduleId") String studentId,
                                                      @Query("platform") String platform,
                                                      @Query("codeVersion") String codeVersion);

  /*  @POST(Constants.GET_CALENDER_DETAIL)
    Call<AppService> GetCalendarDetail(@Body("schoolId") String schoolId,
                                       @Query("class_section_Id") String class_section_Id,
                                       @Query("year_id") String year_id,
                                       @Query("platform") String platform);*/

    @GET(Constants.GET_FOOD_CHART)
    Call<FoodChartListModel> GetFoodChartList(@Query("schoolid") String schoolid,
                                              @Query("class_section_Id") String classsection,
                                              @Query("dayId") String day,
                                              @Query("year_id") String year,
                                              @Query("platform") String platform);

    @GET(Constants.GET_ISSUE_BOOK_LIST)
    Call<LibraryListModel> GetLibraryList(@Query("schoolId") String schoolId,
                                          @Query("class_section_Id") String class_section_Id,
                                          @Query("studId") String studId,
                                          @Query("year_id") String year_id,
                                          @Query("type") String type,
                                          @Query("platform") String platform);

    @GET(Constants.GET_TIME_TABLE_LIST)
    Call<FoodChartListModel> GetTimeTableList(@Query("schoolId") String schoolId,
                                              @Query("class_section_Id") String class_section_Id,
                                              @Query("dayId") String dayId,
                                              @Query("year_id") String year_id,
                                              @Query("platform") String platform);

    @GET(Constants.GET_CURRICULUM_LIST)
    Call<CurriculumListModel> GetCurriculumList(@Query("schoolId") String schoolId,
                                                @Query("class_id") String class_id,
                                                @Query("folderId") String folderId,
                                                @Query("studid") String studid,
                                                @Query("yearid") String yearid,
                                                @Query("platform") String platform);

    @GET(Constants.GET_CURRICULUM_LIST_Classroom)
    Call<CurriculumListModel> GetCurriculumListClassroom(@Query("schoolId") String schoolId,
                                                         @Query("class_id") String class_id,
                                                         @Query("folderId") String folderId,
                                                         @Query("studid") String studid,
                                                         @Query("yearid") String yearid,
                                                         @Query("platform") String platform);

    @GET(Constants.GET_CURRICULUM_LIST_Classroom_V2)
    Call<CurriculumListModel> GetCurriculumListClassroom_V2(@Query("schoolId") String schoolId,
                                                            @Query("class_id") String class_id,
                                                            @Query("studid") String studid,
                                                            @Query("yearid") String yearid,
                                                            @Query("platform") String platform);


    @GET(Constants.GET_SURVEY_POLL_TASK)
    Call<PollTaskListModel> GetSurveyPollTask(@Query("schoolId") String schoolId,
                                              @Query("class_section_Id") String class_section_Id,
                                              @Query("studId") String studId,
                                              @Query("year_id") String year_id,
                                              @Query("platform") String platform);

    @GET(Constants.GET_SURVEY_POLL_TASK_SAVE)
    Call<PollTaskListModel> GetSurveyPollTaskSave(@Query("schoolId") String schoolId,
                                                  @Query("class_section_Id") String class_section_Id,
                                                  @Query("studId") String studId,
                                                  @Query("surveyId") String surveyId,
                                                  @Query("ans") String ans,
                                                  @Query("filterids") String filterids,
                                                  @Query("platform") String platform);

    @GET(Constants.GET_VEHICLE_LOCATION_JSON)
    Call<AppService> GetVechicleLocationJson(@Query("stud_id") String stud_id,
                                             @Query("school_id") String school_id,
                                             @Query("year_id") String year_id,
                                             @Query("Route_id") String Route_id,
                                             @Query("Trip_id") String Trip_id,
                                             @Query("Device_id") String Device_id,
                                             @Query("mobileno") String mobileno);

    @GET(Constants.GET_REFUND_POLICY)
    Call<AppService> GetRefundPolicy(@Query("studid") String studid,
                                     @Query("schoolid") String schoolid,
                                     @Query("platform") String platform);

    @GET(Constants.GET_EXAMS_DATA)
    Call<ExamListModel> GetExamData(@Query("stud_id") String stud_id,
                                    @Query("schoolid") String schoolid,
                                    @Query("yearid") String yearid,
                                    @Query("platform") String platform);

    @GET(Constants.GET_EXAM_MARK_DATA)
    Call<ExamListModel> GetExamMarkData(@Query("stud_id") String stud_id,
                                        @Query("schoolid") String schoolid,
                                        @Query("exam_id") String exam_id,
                                        @Query("yr_id") String yearid,
                                        @Query("platform") String platform);

    @GET(Constants.GET_CHECK_TRANSACTION_STATUS)
    Call<AppService> getCheckTransactionStatus(@Query("Stud_Id") String stud_id,
                                               @Query("School_Id") String schoolid,
                                               @Query("transactionId") String transactionId,
                                               @Query("appName") String appName,
                                               @Query("platform") String platform);

    @GET(Constants.GET_MARKSHEET_EXAMS_WITH_PATH_DATA)
    Call<ExamListModel> GetExamMarkSheetData(@Query("schoolid") String schoolid,
                                             @Query("yearid") String yearid,
                                             @Query("stud_id") String stud_id,
                                             @Query("platform") String platform);

    @GET(Constants.GET_LEAVE_HISTORY)
    Call<LeaveListModel> GetLeaveHistoryList(@Query("studid") String studid,
                                             @Query("schoolid") String schoolid,
                                             @Query("VersionCode") String VersionCode,
                                             @Query("platform") String platform);

    @GET(Constants.GET_LEAVE_TYPE)
    Call<LeaveTypeModel> GetLeaveType(@Query("schoolid") String schoolid,
                                      @Query("VersionCode") String VersionCode,
                                      @Query("platform") String platform);

    @GET(Constants.GET_LEAVE_PDF_IMAGE_FTP_DETAIL)
    Call<UploadphotosModel> GetLeavePdfImageFTPDetail1(@Query("userid") String userid,
                                                       @Query("VersionCode") String VersionCode);


    @GET(Constants.GET_LEAVE_PDF_IMAGE_FTP_DETAIL)
    Call<AppService> GetLeavePdfImageFTPDetail(@Query("studid") String studid,
                                               @Query("Schoolid") String Schoolid,
                                               @Query("yearid") String yearid,
                                               @Query("type") String type,
                                               @Query("appName") String appName,
                                               @Query("VersionCode") String VersionCode,
                                               @Query("platform") String platform);

    @GET(Constants.METHOD_NAME_GET_FEEdBACK_CATEGORY)
    Call<AppService> getCategory(@Query("studid") String studid,
                                 @Query("schoolId") String schoolId,
                                 @Query("yearId") String yearId,
                                 @Query("platform") String platform);


    @GET(Constants.GET_SUBMIT_QUERY)
    Call<AppService> getSubmitQuery(@Query("studentid") String studentid,
                                    @Query("schoolid") String schoolid,
                                    @Query("DeviceId") String DeviceId,
                                    @Query("DeviceDetails") String DeviceDetails,
                                    @Query("appname") String appname,
                                    @Query("subject") String subject,
                                    @Query("appVirson") String appVirson,
                                    @Query("Description") String Description,
                                    @Query("yearid") String yearid,
                                    @Query("category") String category,
                                    @Query("email") String email,
                                    @Query("platform") String platform);


    @GET(Constants.GET_LEAVE_APPLY)
    Call<LeaveListModel> GetLeaveApply(@Query("studid") String studid,
                                       @Query("schoolid") String schoolid,
                                       @Query("leaveid") String leaveid,
                                       @Query("startdate") String startdate,
                                       @Query("enddate") String enddate,
                                       @Query("Reason") String Reason,
                                       @Query("Document") String Document,
                                       @Query("ishalfday") String isHalfDay,
                                       @Query("VersionCode") String VersionCode,
                                       @Query("platform") String platform);

    @GET(Constants.GET_SOCIAL_MEDIA_LIST)
    Call<SocialMediaListModel> GetSocialMedia(@Query("schoolid") String schoolid,
                                              @Query("VersionCode") String VersionCode,
                                              @Query("platform") String platform);

    @GET(Constants.GET_PARENTS_PROFILE_LIST)
    Call<ParentsProfileListModel> GetParentsProfile(@Query("studid") String studid,
                                                    @Query("schoolid") String schoolid,
                                                    @Query("VersionCode") String VersionCode,
                                                    @Query("platform") String platform);

    @GET(Constants.GET_MAP_URL)
    Call<Banner> GetMapUrl(@Query("studId") String studId,
                           @Query("schoolId") String schoolId,
                           @Query("yearid") String yearid,
                           @Query("routeid") String routeid,
                           @Query("platform") String platform);


    @GET(Constants.GET_STUDENT_EMAILID)
    Call<SendEmailDownlodModel> GetStudentEmailid(@Query("School_Id") String schoolid,
                                                  @Query("studid") String studentid,
                                                  @Query("YearId") int yearid);

    @GET(Constants.GET_SEND_EMAILID)
    Call<SendEmailDownlodModel> SendEmailid(@Query("studid") String studentid,
                                            @Query("schoolid") String schoolid,
                                            @Query("YearId") int yearid,
                                            @Query("type") int type,
                                            @Query("mailid") String mailid,
                                            @Query("docUrl") String docurl,
                                            @Query("id") String id,
                                            @Query("name") String name,
                                            @Query("platform") String platform);

    @GET(Constants.GET_VALIDATE_ACCOUNT)
    Call<AppService> getValidateAccount(@Query("studId") String studId,
                                        @Query("Schoolid") String Schoolid,
                                        @Query("yearid") String yearid,
                                        @Query("appName") String appName,
                                        @Query("VersionCode") String VersionCode,
                                        @Query("platform") String platform);


    @GET(Constants.GET_CIRCULAR_STUDENT)
    Call<CircularModel> GetCircularDetail(@Query("SchoolId") int schoolid,
                                          @Query("StudId") int studentid,
                                          @Query("Year_Id") int yearid,
                                          @Query("platform") String platform);

    @GET(Constants.GET_PRACTICE_TEST)
    Call<AppService> GetPracticeTestList(@Query("studId") String studId,
                                         @Query("schoolId") String schoolId,
                                         @Query("yearId") String yearId,
                                         @Query("appName") String appName,
                                         @Query("VersionCode") String VersionCode,
                                         @Query("platform") String platform);

    @GET(Constants.GET_PRACTICE_PENDING_TEST_COUNT)
    Call<AppService> GET_PRACTICE_PENDING_TEST_COUNT(@Query("studId") String studId,
                                                     @Query("schoolId") String schoolId,
                                                     @Query("yearId") String yearId,
                                                     @Query("appName") String appName,
                                                     @Query("VersionCode") String VersionCode,
                                                     @Query("platform") String platform);

    @GET(Constants.GET_PRACTICE_TEST_QA)
    Call<AppService> GetPracticeTestQAList(@Query("studId") String studId,
                                           @Query("schoolId") String schoolId,
                                           @Query("yearId") String yearId,
                                           @Query("testid") String testid,
                                           @Query("appName") String appName,
                                           @Query("VersionCode") String VersionCode,
                                           @Query("platform") String platform);

    @GET(Constants.SET_PRACTICE_TEST_ANS)
    Call<AppService> SetPracticeTestAns(@Query("studId") String studId,
                                        @Query("schoolId") String schoolId,
                                        @Query("yearId") String yearId,
                                        @Query("testId") String testId,
                                        @Query("Test_Que_ID") String Test_Que_ID,
                                        @Query("answergiven") String answergiven,
                                        @Query("timetaken") String timetaken,
                                        @Query("islast") String islast,
                                        @Query("appName") String appName,
                                        @Query("VersionCode") String VersionCode,
                                        @Query("platform") String platform);

    @GET(Constants.SET_PRACTICE_TEST_ANS_V2)
    Call<AppService> SetPracticeTestAnsV2(@Query("studId") String studId,
                                          @Query("schoolId") String schoolId,
                                          @Query("yearId") String yearId,
                                          @Query("testId") String testId,
                                          @Query("Data") String data,
                                          @Query("appName") String appName,
                                          @Query("VersionCode") String VersionCode,
                                          @Query("platform") String platform);

    @GET(Constants.SET_PRACTICE_TEST_ANS_V3)
    Call<AppService> SetPracticeTestAnsV3(@Query("studId") String studId,
                                          @Query("schoolId") String schoolId,
                                          @Query("yearId") String yearId,
                                          @Query("testId") String testId,
                                          @Query("Data") String data,
                                          @Query("appName") String appName,
                                          @Query("VersionCode") String VersionCode,
                                          @Query("platform") String platform,
                                          @Query("isfrom") String isfrom,
                                          @Query("starttime") String starttime,
                                          @Query("DeviceId") String DeviceId,
                                          @Query("Info") String Info);

    @GET(Constants.GET_CALCULATE_SUMMARY)
    Call<AppService> GetCalculateSummary(@Query("studId") String studId,
                                         @Query("schoolId") String schoolId,
                                         @Query("yearId") String yearId,
                                         @Query("testid") String testid,
                                         @Query("appName") String appName,
                                         @Query("VersionCode") String VersionCode,
                                         @Query("platform") String platform,
                                         @Query("DeviceId") String DeviceId
    );

    @GET(Constants.GET_GUARDIAN_HOST_LIST)
    Call<GetGuardianHostModal> GetGuardianHostList(@Query("locationid") String locationid,
                                                   @Query("schoolid") String schoolid,
                                                   @Query("studentid") String studentid,
                                                   @Query("year_id") String year_id,
                                                   @Query("mobileno") String mobileno,
                                                   @Query("platform") String platform,
                                                   @Query("appName") String appName,
                                                   @Query("appVersion") String appVersion);

    @GET(Constants.SET_BOOK_APPOINTMENT)
    Call<GetGuardianHostModal> SetBookAppointment(@Query("locationid") String locationid,
                                                  @Query("schoolid") String schoolid,
                                                  @Query("studentid") String studentid,
                                                  @Query("visitormobile") String visitormobile,
                                                  @Query("visitorname") String visitorname,
                                                  @Query("hour") String hour,
                                                  @Query("minutes") String minutes,
                                                  @Query("hostnumber") String hostnumber,
                                                  @Query("hostemail") String hostemail,
                                                  @Query("hostname") String hostname,
                                                  @Query("hostid") String hostid,
                                                  @Query("date") String date,
                                                  @Query("purposeofvisit") String purposeofvisit,
                                                  @Query("year_id") String year_id,
                                                  @Query("mobileno") String mobileno,
                                                  @Query("platform") String platform,
                                                  @Query("appName") String appName,
                                                  @Query("appVersion") String appVersion);

    @GET(Constants.GET_APPOINTMENT_LIST)
    Call<AppointmentListModal> GetAppointmentList(@Query("visitorid") String visitorid,
                                                  @Query("fromdate") String fromdate,
                                                  @Query("todate") String todate,
                                                  @Query("type") String type,
                                                  @Query("schoolid") String schoolid,
                                                  @Query("hostid") String hostid,
                                                  @Query("year_id") String year_id,
                                                  @Query("mobileno") String mobileno,
                                                  @Query("platform") String platform,
                                                  @Query("appName") String appName,
                                                  @Query("appVersion") String appVersion);

    //GetGroupListFromHost?hostid=1&requesteddate=24/07/2019&year_id=&mobileno=&platform=&appName=&appVersion=
    @GET(Constants.GET_TIME_LIST)
    Call<GetTimeModal> GetTimeList(@Query("hostid") String hostid,
                                   @Query("requesteddate") String requesteddate,
                                   @Query("year_id") String year_id,
                                   @Query("mobileno") String mobileno,
                                   @Query("platform") String platform,
                                   @Query("appName") String appName,
                                   @Query("appVersion") String appVersion);

    @GET(Constants.GET_STUDENT_DOCUMENTS)
    Call<Document> GetStudentDocuments(@Query("StudId") String StudId,
                                       @Query("YearId") String yearid,
                                       @Query("Schoolid") String Schoolid,
                                       @Query("platform") String platform,
                                       @Query("versionCode") String versionCode);

//Created By Vijay Solanki 02-01-2020
    //(string studId, string schoolId, string yearId, string appName, string VersionCode, string platform)

    @GET(Constants.GET_VOICE_MESSAGE)
    Call<VoiceMessageListModel> getVoiceMessage(@Query("studId") String studId,
                                                @Query("schoolId") String schoolId,
                                                @Query("yearId") String yearId,
                                                @Query("appName") String appName,
                                                @Query("VersionCode") String VersionCode,
                                                @Query("platform") String platform);

    //public void InsertStudentCurriculumResponse(string studid, string schoolId, string yearid, string curriculum_id,
    // string message, string file, string platform)
    @GET(Constants.GET_INSERT_STUDENT_CURRICULAM_RESPOSE)
    Call<ParentsProfileListModel> GetSubmitCurriculamResponse(@Query("studid") String studid,
                                                              @Query("schoolId") String schoolid,
                                                              @Query("yearid") String yearid,
                                                              @Query("curriculum_id") String curriculum_id,
                                                              @Query("message") String message,
                                                              @Query("file") String file,
                                                              @Query("platform") String platform);

    @GET(Constants.GET_INSERT_STUDENT_CURRICULAM_RESPOSE_Classroom)
    Call<ParentsProfileListModel> GetSubmitCurriculamResponseClassroom(@Query("studid") String studid,
                                                                       @Query("schoolId") String schoolid,
                                                                       @Query("yearid") String yearid,
                                                                       @Query("curriculum_id") String curriculum_id,
                                                                       @Query("message") String message,
                                                                       @Query("file") String file,
                                                                       @Query("platform") String platform);

    //public static object GetStudentCurriculumResponse(string studid, string schoolId, string yearid, string curriculum_id )
    //    {

    @GET(Constants.GET_STUDENT_CURRICULAM_RESPOSE)
    Call<AppService> getStudentCurriculamResponse(@Query("studid") String studid,
                                                  @Query("schoolId") String schoolId,
                                                  @Query("yearid") String yearid,
                                                  @Query("curriculum_id") String curriculum_id,
                                                  @Query("platform") String platform);

    @GET(Constants.GET_STUDENT_CURRICULAM_RESPOSE_Classroom)
    Call<AppService> getStudentCurriculamResponseClassroom(@Query("studid") String studid,
                                                           @Query("schoolId") String schoolId,
                                                           @Query("yearid") String yearid,
                                                           @Query("curriculum_id") String curriculum_id,
                                                           @Query("platform") String platform);

    @GET(Constants.GET_E_LIBRARY)
    Call<AppService> getELibrary(@Query("studid") String studid,
                                 @Query("schoolid") String schoolId,
                                 @Query("VersionCode") String VersionCode,
                                 @Query("platform") String platform);

}