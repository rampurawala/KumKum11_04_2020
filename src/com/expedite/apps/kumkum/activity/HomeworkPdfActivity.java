package com.expedite.apps.kumkum.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.List;

public class HomeworkPdfActivity extends BaseActivity {

    String tag = "HomeworkPdfActivity";
    ProgressBar mProgressBar;
    String filename = "", CircularPath = "";
    WebView resultView;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_pdf);
        try {
            init();
            new loadPDF().execute();
        } catch (Exception ex) {
            Constants.Logwrite(tag, "EX 167::" + ex.getMessage());
        }
    }

    private void loadPDF() {
        try {
            File myDir = Constants.CreatePhotoGalleryFolder();
            int isProcess = 0;
            if (Constants.isShowInternetMsg) {
                Constants.NotifyNoInternet(HomeworkPdfActivity.this);
            } else {
                try {
                    isProcess = 1;
                    File file = new File(myDir, filename);
                    PackageManager packageManager = getPackageManager();
                    Uri uri1 = Uri.fromFile(file);

                    if (file.exists()) {
                        Intent intentUrl = new Intent(
                                Intent.ACTION_VIEW);
                        intentUrl.setDataAndType(uri1,
                                "application/pdf");
                        List list = packageManager
                                .queryIntentActivities(intentUrl, PackageManager.MATCH_DEFAULT_ONLY);
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
                    Constants.Logwrite("HomeworkPdfActivity",
                            "Exception 116:" + ex.getMessage()
                                    + ":::::::::" + ex.getStackTrace());
                }
                if (isProcess == 0) {
                    try {
                        resultView
                                .loadUrl("http://docs.google.com/gview?embedded=true&url=" + Constants.serverUrl + "//" + Constants.HWPDF + "//"
                                        + CircularPath);

                    } catch (Exception ex) {
                        Constants.Logwrite("HomeworkPdfActivity",
                                "Exception 159:" + ex.getMessage()
                                        + ":::::::::" + ex.getStackTrace());
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    private void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.HomeworkPdfActivity);
            Constants.setActionbar(getSupportActionBar(), HomeworkPdfActivity.this, HomeworkPdfActivity.this,
                    "Student Homework", "HomeworkPdfActivity", ActivityNames.HomeworkPdfActivity);
            mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
            filename = getIntent().getStringExtra("filename");
            resultView = (WebView) findViewById(R.id.webViewhm);
            resultView.setDownloadListener(null);
            resultView.getSettings().setJavaScriptEnabled(true);
        } catch (Exception ex) {

        }
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(HomeworkPdfActivity.this,
                    SubjectWiseHWDetailsActivity.class);
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
            Intent intent = new Intent(HomeworkPdfActivity.this, NoticeBoardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            onBackClickAnimation();
        }
        return super.onOptionsItemSelected(item);
    }

    private class loadPDF extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // LogUserVisted();
            if (filename == null || filename.equals("")) {
            } else {
                File myDir = Constants.CreatePhotoGalleryFolder();
                File file = new File(myDir, filename);
                if (!file.exists()) {
                    String fileURL = "http://www.vayuna.com/" + Constants.HWPDF + "/"
                            + filename;
                    Constants.SavePdf(
                            HomeworkPdfActivity.this, fileURL,
                            filename);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                loadPDF();
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }
}
