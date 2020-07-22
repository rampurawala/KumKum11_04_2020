package com.expedite.apps.kumkum.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.google.android.gms.ads.AdView;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.util.List;

public class StudentCircularActivity extends BaseActivity {
    private String Cir_ID = "", Circular_Foleder_Path = "";
    private WebView mWebView;
    private String CircularPath = "", StudId, SchoolId, Year_Id, filename = "",isFrom="";
    private String studid = "", schoolid = "";
    private ProgressBar mProgressBar;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_circular);
        init();
    }

    public void init() {
        mAdView = (AdView) findViewById(R.id.adView);
        showBannerAd(mAdView, ActivityNames.StudentCircularActivity);
        Constants.setActionbar(getSupportActionBar(), StudentCircularActivity.this, StudentCircularActivity.this,
                "Student Circular", "StudentCircularActivity", ActivityNames.StudentCircularActivity);
        mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        try {
            Intent intnt = getIntent();
            Cir_ID = intnt.getStringExtra("CirId");
            CircularPath = intnt.getStringExtra("CircularPath");
            filename = intnt.getStringExtra("filename");
            isFrom= intnt.getStringExtra("isFrom");
            try {
                studid = intnt.getStringExtra("Studid");
                schoolid = intnt.getStringExtra("Schoolid");
                Year_Id = Datastorage.GetCurrentYearId(StudentCircularActivity.this);
                if (studid != "" && studid != null && schoolid != ""&& schoolid != null) {
                    //
                } else {
                    StudId = Datastorage.GetStudentId(StudentCircularActivity.this);
                    SchoolId = Datastorage.GetSchoolId(StudentCircularActivity.this);
                }
            } catch (Exception ex) {
                Constants.writelog("StudentCircularActivity", "Ex 134:" + ex.getMessage());
            }
            mWebView = (WebView) findViewById(R.id.webView1);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, final String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    mProgressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (view.getProgress() == 100) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }

            });
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);
            if(isFrom!=null && isFrom.equalsIgnoreCase("NoticeBoardActiviy")){
                mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + CircularPath);
            }else {
                new MyTask().execute();
            }
        } catch (Exception err) {
            Constants.Logwrite("StudentCircularActivity", "EX 167::" + err.getMessage());
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // LogUserVisted();
            if (CircularPath == null || CircularPath.equals("")) {
                if (filename == null || filename.equals("")) {
                    getStudentCircularPath();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                File myDir = Constants.CreatePhotoGalleryFolder();
                int isProcess = 0;
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(StudentCircularActivity.this);
                } else {
                    if (filename != null && filename != "") {
                        try {
                            isProcess = 1;
                            File file = new File(myDir, filename);
                            PackageManager packageManager = getPackageManager();
                            Uri uri1 = Uri.fromFile(file);
                            if (!file.exists()) {
                                String fileURL = Constants.circularUrl + filename;
                                Constants.SavePdf(
                                        StudentCircularActivity.this, fileURL,
                                        filename);
                            }
                            if (file.exists()) {
                                Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                                intentUrl.setDataAndType(uri1,"application/pdf");
                                List list = packageManager
                                        .queryIntentActivities(
                                                intentUrl,
                                                PackageManager.MATCH_DEFAULT_ONLY);

                                if (list.size() > 0 && file.isFile()) {
                                    intentUrl
                                            .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(intentUrl);

                                } else {
                                    isProcess = 0;
                                    CircularPath = filename;
                                }
                            } else {
                                isProcess = 0;
                                CircularPath = filename;
                            }
                        } catch (Exception ex) {
                            isProcess = 0;
                            CircularPath = filename;
                            Constants.Logwrite("StudentCircularActivity",
                                    "Exception 116:" + ex.getMessage()
                                            + ":::::::::" + ex.getStackTrace());
                        }
                    }

                    if (isProcess == 0) {
                        try {
                            if (CircularPath == null || CircularPath.equals("")) {
                                mWebView
                                        .loadUrl("http://docs.google.com/gview?embedded=true&url="
                                                + Circular_Foleder_Path + "");
                            } else {
                                mWebView
                                        .loadUrl("http://docs.google.com/gview?embedded=true&url=" + Constants.serverUrl + "//CircularForStudent//"
                                                + CircularPath + "");
                            }
                        } catch (Exception ex) {
                            Constants.Logwrite("StudentCircularActivity",
                                    "Exception 159:" + ex.getMessage()
                                            + ":::::::::" + ex.getStackTrace());
                        }
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    public void getStudentCircularPath() {
        // String[] years = null;
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_STUDENT_CIRCULAR_PATH);
        request.addProperty("cirid", Integer.parseInt(Cir_ID));
        request.addProperty("year_id", Integer.parseInt(Year_Id));
        request.addProperty("studid", Integer.parseInt(StudId));
        request.addProperty("schoolid", Integer.parseInt(SchoolId));
        try {
            SoapObject result = Constants.CallWebMethod(
                    StudentCircularActivity.this, request, Constants.GET_STUDENT_CIRCULAR_PATH, true);
            if (result != null && result.getPropertyCount() > 0) {
                String obtained = result.getPropertyAsString(0);
                Circular_Foleder_Path = obtained;
            }
        } catch (Exception ex) {
            Constants.writelog("StudentCircularActivity",
                    "getStudentCircularPath()275 Ex:" + ex.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(StudentCircularActivity.this,
                    NoticeBoardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            onBackClickAnimation();
        } catch (Exception err) {
            Common.printStackTrace(err);
        }
        super.onBackPressed();
        onBackClickAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            hideKeyboard(StudentCircularActivity.this);
            ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
            if (taskList.get(0).numActivities == 1 &&
                    taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
                StudentCircularActivity.this.finish();
            } else {
                Intent intent = new Intent(StudentCircularActivity.this, NoticeBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
            onBackClickAnimation();
        } else if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
