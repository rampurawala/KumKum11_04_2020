package com.expedite.apps.kumkum.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.Contact;

import org.ksoap2.serialization.SoapObject;

import java.util.List;

public class MarksheetResultActivity extends BaseActivity {
    private WebView resultView;
    private int isref = 0;
    private String SchoolId, StudentId, Year_Id, Marksheet_Foleder_Path = "",
            ExamId, Year = "",pdftitle="",IsNotification="";
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_marksheet_result);
        init();
    }

    public void init() {
        try {
            Constants.setActionbar(getSupportActionBar(), MarksheetResultActivity.this, MarksheetResultActivity.this,
                    "MarksheetResult", "ViewMarksheetPdf", ActivityNames.MarksheetResultActivity);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            SchoolId = Datastorage.GetSchoolId(MarksheetResultActivity.this);
            StudentId = Datastorage.GetStudentId(MarksheetResultActivity.this);
            Year_Id = Datastorage.GetCurrentYearId(MarksheetResultActivity.this);
            db = new DatabaseHandler(MarksheetResultActivity.this);

            resultView = (WebView) findViewById(R.id.webresult);
            resultView.setDownloadListener(null);
            // resultView.addView(resultView.getZoomControls());
            resultView.getSettings().setJavaScriptEnabled(true);
            Intent intnt = getIntent();
            ExamId = intnt.getStringExtra("ExamId");
            pdftitle=intnt.getStringExtra("Name");
            IsNotification=intnt.getStringExtra("IsNotification");
            Year = Datastorage.GetAcademicYear(MarksheetResultActivity.this);
            new MyTask().execute();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (isref == 0) {
                int cntr = 0;
                List<Contact> contacts = db.GetMarksheetPath(Integer.parseInt(SchoolId),
                        Integer.parseInt(StudentId), Integer.parseInt(Year_Id),
                        Integer.parseInt(ExamId));
                if (contacts.size() > 0) {
                    for (Contact cn : contacts) {
                        Marksheet_Foleder_Path = cn.getGlobalText();
                        if (Marksheet_Foleder_Path != "" && Marksheet_Foleder_Path.length() > 0) {
                        } else {
                            getStudentMarksheetPath();
                        }
                    }
                } else {
                    getStudentMarksheetPath();
                }
            } else {
                getStudentMarksheetPath();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(MarksheetResultActivity.this);
                } else {
                    if (Marksheet_Foleder_Path != null
                            && Marksheet_Foleder_Path != "") {
                        resultView.loadUrl("http://docs.google.com/gview?embedded=true&url="
                                + Marksheet_Foleder_Path + "");

                       /* //test by vishwa 21/2/2019
                        Intent intent = new Intent(MarksheetResultActivity.this, CurriculumPdfViewActivity.class);
                        intent.putExtra("Url", Marksheet_Foleder_Path);
                        intent.putExtra("Name", pdftitle);
                        startActivity(intent);
                        onClickAnimation();
*/

                    } else {
                        Toast.makeText(MarksheetResultActivity.this,
                                "Please contact in your school for details..",
                                Toast.LENGTH_LONG).show();
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    public void getStudentMarksheetPath() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_STUDENT_MARKSHEET_PATH_V1);
        request.addProperty("academicyear", Year);
        request.addProperty("class_name", Datastorage.GetClassName(MarksheetResultActivity.this));
        request.addProperty("exam_id", Integer.parseInt(ExamId));
        request.addProperty("school_id", Integer.parseInt(SchoolId));
        request.addProperty("Studid", Integer.parseInt(StudentId));

        try {
            SoapObject result = Constants.CallWebMethod(
                    MarksheetResultActivity.this, request, Constants.GET_STUDENT_MARKSHEET_PATH_V1, true);
            if (result != null && result.getPropertyCount() > 0) {

                String obtained = result.getPropertyAsString(0);
                Marksheet_Foleder_Path = obtained;
                if (Marksheet_Foleder_Path != null
                        && Marksheet_Foleder_Path != "")
                    db.UpdateExamTableSetMarksheetPath(SchoolId, StudentId,
                            Year_Id, ExamId, "1", Marksheet_Foleder_Path);
            }
        } catch (Exception e) {
            Common.printStackTrace(e);
        }
        // return ItmNames;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            menu.add(0, 1, 1, "Refresh").setTitle("Refresh");
        } catch (Exception err) {
            Constants.Logwrite("MarksheetResultActivity", "MainPage:" + err.getMessage());
            return true;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int iid = item.getItemId();
        if (iid == android.R.id.home) {
            hideKeyboard(MarksheetResultActivity.this);
            MarksheetResultActivity.this.finish();
            onBackClickAnimation();
        } else if (iid == 1) {
            isref = 1;
            new MyTask().execute();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent=null;
        if (IsNotification != null && !IsNotification.isEmpty()) {
            super.onBackPressed();
            intent = new Intent(MarksheetResultActivity.this, HomeActivity.class);
        } else {
            MarksheetResultActivity.this.finish();
            onBackClickAnimation();
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
        onBackClickAnimation();
        super.onBackPressed();
    }

}
