package com.expedite.apps.kumkum.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.database.DatabaseHandler;

public class RedirectActivity extends BaseActivity {
    private DatabaseHandler db = new DatabaseHandler(this);
    private String message = "", Stud_Id = "", Module_Id = "", Exam_Id = "", Stud_Name = "";
    private int Type_Id = 0, SchoolId = 0;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);
        init();
    }

    public void init() {
        try {
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            Constants.setActionbar(getSupportActionBar(), RedirectActivity.this,
                    RedirectActivity.this, "Redirect", "RedirectActivity", ActivityNames.RedirectActivity);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowCustomEnabled(false);
            Intent intnt = getIntent();
            message = intnt.getStringExtra("MSG");
            Constants.Logwrite("RedirectActivity", "Details:" + message);
            String[] split = message.split(",");
            Stud_Id = split[0].toString();
            Constants.Logwrite("RedirectActivity", "Stud_Id:" + Stud_Id);
            Module_Id = split[1].toString();
            Constants.Logwrite("RedirectActivity", "Module_Id:" + Module_Id);
            Exam_Id = split[2].toString();
            Constants.Logwrite("RedirectActivity", "Exam_Id:" + Exam_Id);
            Type_Id = Integer.parseInt(split[3].toString());
            Constants.Logwrite("RedirectActivity", "Type_Id:" + Type_Id);
            SchoolId = Integer.parseInt(split[4].toString());
            Constants.Logwrite("RedirectActivity", "SchoolId:" + SchoolId);
            Stud_Name = split[5].toString();
            Constants.Logwrite("RedirectActivity", "StudentName:" + Stud_Name);
            // messageid = Integer.parseInt(split[4].toString());
            // notification_text = split[4].toString();
            // Msg_Id = Integer.parseInt(split[5].toString());
            new MyTask().execute();
        } catch (Exception err) {
            Constants.Logwrite("RedirectActivity:", "Exception:" + err.getMessage() + "StackTrace::"
                    + err.getStackTrace().toString());
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                // Constants.UpdateNotifcationTable(messageid);
                SetStudentData();
            } catch (Exception err) {
                Constants.Logwrite("RedirectActivity:", "Exception:" + err.getMessage()
                        + "StackTrace::" + err.getStackTrace().toString());
            }
            return null;
        }

        private void SetStudentData() {
            try {
                int Record_Count = db.getContactsCount();
                Constants.Logwrite("RedirectActivity:", "Record_Count:" + Record_Count);
                if (Record_Count > 0) {
                    if (Record_Count > 1) {
                        // LoginDetail.setIsMultipleAccount(1);
                        Datastorage.SetMultipleAccount(getApplicationContext(),
                                1);
                    } else {
                        // LoginDetail.setIsMultipleAccount(0);
                        Datastorage.SetMultipleAccount(getApplicationContext(),
                                0);
                    }
                    String Concatestr = db.GetAccount_DetailsUsing_StudID(
                            Integer.parseInt(Stud_Id), SchoolId);
                    Constants.writelog("RedirectActivity",
                            "GetAccount_DetailsUsing_StudID 113 Moduleid:-"
                                    + Module_Id + " :-" + Concatestr);
                    Constants.Logwrite("RedirectActivity:", "Concatestr:" + Concatestr);
                    String[] con_splt = Concatestr.split(",");
                    // String Phone_Number = con_splt[0].toString();
                    //Constants.Logwrite("RedirectActivity:","Phone:"+Phone_Number);
                    String Stud_Name = con_splt[2].toString();
                    Constants.Logwrite("RedirectActivity:", "Name:" + Stud_Name);
                    String School_Id = con_splt[4].toString();
                    Constants.Logwrite("RedirectActivity:", "SchoolId:" + School_Id);
                    String Year_Id = con_splt[3].toString();
                    Constants.Logwrite("RedirectActivity:", "YearId:" + Year_Id);
                    String Class_Id = con_splt[5].toString();
                    Constants.Logwrite("RedirectActivity:", "ClassId:" + Class_Id);
                    String Sec_Id = con_splt[6].toString();
                    Constants.Logwrite("RedirectActivity:", "SecId:" + Sec_Id);
                    String User_Id = con_splt[7].toString();
                    Constants.Logwrite("RedirectActivity:", "UserId:" + Sec_Id);
                    String classname = con_splt[8].toString();
                    Constants.Logwrite("RedirectActivity:", "ClassName:" + classname);
                    String studenrolldate = con_splt[9].toString();
                    Constants.Logwrite("RedirectActivity:", "studenrolldate:" + studenrolldate);
                    String lastupdatedtime = con_splt[10].toString();
                    Constants.Logwrite("RedirectActivity:", "lastupdatedtime:" + lastupdatedtime);
                    String academicyear = con_splt[11].toString();
                    Constants.Logwrite("RedirectActivity:", "academicyear:" + academicyear);
                    String classsecname = con_splt[12].toString();
                    String routeid = con_splt[13].toString();

                    Datastorage.SetSchoolId(getApplicationContext(), School_Id);
                    Datastorage.SetUserId(getApplicationContext(), User_Id);
                    Datastorage.SetStudentID(getApplicationContext(), Stud_Id);
                    Datastorage.SetStudentName(getApplicationContext(), Stud_Name);
                    Datastorage.SetCurrentYearId(getApplicationContext(), Year_Id);
                    Datastorage.SetClassId(getApplicationContext(), Class_Id);
                    Datastorage.SetClassSecId(getApplicationContext(), Sec_Id);
                    Datastorage.SetClassSecName(getApplicationContext(), classname);
                    Datastorage.SetEnrollDate(getApplicationContext(), studenrolldate);
                    Datastorage.LastUpdatedtime(getApplicationContext(), lastupdatedtime);
                    Datastorage.SetAcademicYear(getApplicationContext(), academicyear);
                    Datastorage.SetClassSectionName(getApplicationContext(), classsecname);
                    Datastorage.SetRouteId(getApplicationContext(), Integer.parseInt(routeid));

                } else {
                    Datastorage.SetStudentName(getApplicationContext(), Stud_Name);
                    Datastorage.SetSchoolId(getApplicationContext(), Integer.toString(SchoolId));
                    Datastorage.SetStudentID(getApplicationContext(), Stud_Id);
                }
            } catch (Exception err) {
                Constants.Logwrite("RedirectActivity:",
                        "Exception_SetStudentData:" + err.getMessage()
                                + "StackTrace::"
                                + err.getStackTrace().toString());
                //Constants.Logwrite("");
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent notificationIntent;
            try {
                if (Module_Id.equals("0")) {
                    notificationIntent = new Intent(getApplicationContext(), MessagesExpandableListActivity.class);
                    // LoginDetail.setStudId(Stud_Id);
                    // LoginDetail.setSchoolId(School_Id);
                } else if (Module_Id.equals("1")) {
                    if (Type_Id == 1 || Type_Id == 2) {
                        notificationIntent = new Intent(RedirectActivity.this, StudentResultActvity.class);
                        notificationIntent.putExtra("ExamId", Exam_Id);
                        notificationIntent.putExtra("FROM", "NOT");
                        notificationIntent.putExtra("IsNotification", "1");
                    } else {
                        notificationIntent = new Intent(RedirectActivity.this, MarksheetResultActivity.class);
                        notificationIntent.putExtra("ExamId", Exam_Id);
                        notificationIntent.putExtra("FROM", "NOT");
                        notificationIntent.putExtra("IsNotification", "1");
                    }
                } else if (Module_Id.equals("2")) {
                    notificationIntent = new Intent(getApplicationContext(), DailyDiaryCalanderActivity.class);
                    notificationIntent.putExtra("msgtype", Constants.HW_HOMEWORK);
                    notificationIntent.putExtra("IsNotification", "1");
                    // notificationIntent.putExtra("ExamId", Exam_Id);
                    // notificationIntent.putExtra("FROM", "NOT");
                } else if (Module_Id.equals("3")) {
                    notificationIntent = new Intent(getApplicationContext(), DailyDiaryCalanderActivity.class);
                    notificationIntent.putExtra("msgtype", Constants.HW_HOMEWORKNOTDONE);
                    notificationIntent.putExtra("IsNotification", "1");
                } else if (Module_Id.equals("4")) {
                    notificationIntent = new Intent(getApplicationContext(), DailyDiaryCalanderActivity.class);
                    notificationIntent.putExtra("msgtype", Constants.HW_LATECOME);
                    notificationIntent.putExtra("IsNotification", "1");
                } else if (Module_Id.equals("5")) {
                    notificationIntent = new Intent(getApplicationContext(), DailyDiaryCalanderActivity.class);
                    notificationIntent.putExtra("msgtype", Constants.HW_ABSENT);
                    notificationIntent.putExtra("IsNotification", "1");
                } else if (Module_Id.equals("6")) {
                    notificationIntent = new Intent(getApplicationContext(), DailyDiaryCalanderActivity.class);
                    notificationIntent.putExtra("msgtype", Constants.HW_UNIFORMNOTPROPER);
                    notificationIntent.putExtra("IsNotification", "1");

                } else if (Module_Id.equals("7")) {
                    //change by vishwa "NotBoardActivity--NoticeBoardActivity>"
                    notificationIntent = new Intent(getApplicationContext(), NoticeBoardActivity.class);
                } else if (Module_Id.equals("8")) {
                    notificationIntent = new Intent(getApplicationContext(), PhotoGalleryActivity.class);
                } else {
                    // set intent so it does not start a new activity
                    notificationIntent = new Intent(getApplicationContext(), SingleMessageActivity.class);
                    notificationIntent.putExtra("MSG", message);
                    notificationIntent.putExtra("Studid", Stud_Id);
                    notificationIntent.putExtra("Schoolid", String.valueOf(SchoolId));
                    // notificationIntent.putExtra("OTH", tm);
                }
                notificationIntent.setFlags(notificationIntent.FLAG_ACTIVITY_NEW_TASK
                        | notificationIntent.FLAG_ACTIVITY_CLEAR_TOP
                        | notificationIntent.FLAG_ACTIVITY_SINGLE_TOP
                        | notificationIntent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(notificationIntent);
                finish();
                onClickAnimation();
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                Constants.Logwrite("RedirectActivity:",
                        "Exception_onPostExecute:" + err.getMessage()
                                + "StackTrace::"
                                + err.getStackTrace().toString());
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_redirect, menu);
        return true;
    }

}
