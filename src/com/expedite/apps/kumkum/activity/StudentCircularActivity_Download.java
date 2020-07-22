package com.expedite.apps.kumkum.activity;


import android.app.ActivityManager;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.PermissionChecker;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StudentCircularActivity_Download extends BaseActivity {
    String permissions = "", fileExtension = "";
    int permissionStatus = 0;
    File file;
    DownloadManager.Request request;
    private WebView mWebView;
    private String CircularPath = "", Name = "";
    private ProgressBar mProgressBar;
    private AdView mAdView;

    private static int getFileSize(URL url) {
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return conn.getContentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_circular);

        mAdView = (AdView) findViewById(R.id.adView);
        showBannerAd(mAdView, ActivityNames.StudentCircularActivity_Download);

        try {
            if (getIntent() != null && getIntent().getExtras() != null) {
                Intent intnt = getIntent();
                CircularPath = intnt.getStringExtra("CircularPath");
                Name = intnt.getStringExtra("Name");
                Common.showLog("PDF_URL:StudentCircularActivity_New", Name);
                Common.showLog("PDF_URL:StudentCircularActivity_New", CircularPath);
            }

            permissionStatus = PermissionChecker.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionStatus == PermissionChecker.PERMISSION_GRANTED) {
                fileExtension = CircularPath.substring(CircularPath.lastIndexOf("."));

                request = new DownloadManager.Request(Uri.parse(CircularPath));
                request.setDescription("Downloading...");
                request.setTitle(Name + fileExtension);
// in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }

                String path = Environment.getExternalStorageDirectory()
                        + File.separator
                        + SchoolDetails.PhotoGalleryFolder + "/" + Name + fileExtension;

                double pathsize = path.length();
                try {
                    URL url = new URL(CircularPath);
                    new DownloadPdfFileTask().execute(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

             /*int length=0;
            try {
                URL url = new URL(CircularPath);
                length=getFileSize(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

                file = new File(path);


                //  File fileUrl = new File(CircularPath);
                //   Log.e("file     fileUrl", String.valueOf(file.length()) + "  " + String.valueOf(length));
                /* if(file.exists())*/
           /* if(file.exists() && file.length()==length)
            {
                Toast.makeText(this, "File Already Exists", Toast.LENGTH_SHORT).show();
            }
            else
            {
                request.setDestinationInExternalPublicDir(SchoolDetails.PhotoGalleryFolder, Name +fileExtension);
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            }*/
/*
            if(!file.exists()){
                //Toast.makeText(this, "File Already Exists", Toast.LENGTH_SHORT).show();
                //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, Name +fileExtension);
                request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory()
                        + File.separator
                        + SchoolDetails.PhotoGalleryFolder, Name +fileExtension);

            }
            else{
                */
/*request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory()
                        + File.separator
                        + SchoolDetails.PhotoGalleryFolder, Name +fileExtension);*//*

                Toast.makeText(this, "File Already Exists", Toast.LENGTH_SHORT).show();
               */
                /* request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, Name +fileExtension);*//*

            }
*/


// get download service and enqueue file

            }

     /*   String fileExtension = CircularPath.substring(CircularPath.lastIndexOf("."));
        //*Constants.writelog("CurriculumPdfView", "47 " + fileExtension);
        *//*String[] parts = mUrl.split(".");
        Constants.writelog("CurriculumPdfView", "47 " + parts);
        String Ext = parts[parts.length - 1];*//*
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(CircularPath));
        request.setDescription("PDF Downloading");
        request.setTitle(Name + fileExtension);
// in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, Name +fileExtension);

// get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);*/
            init();
        } catch (Exception ex) {
            Constants.Logwrite("StudentCircularActivity_New onCreate", "Exception:173" + ex.getMessage());
        }
    }

    public void init() {
        Constants.setActionbar(getSupportActionBar(), StudentCircularActivity_Download.this, StudentCircularActivity_Download.this,
                "Student Circular", "StudentCircularActivity", ActivityNames.StudentCircularActivity_Download);
        mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        try {
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
            mWebView.clearCache(true);
            mWebView.clearView();
            mWebView.reload();
            Common.showLog("PDF_URL:StudentCircularActivity_New", CircularPath);
            mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + CircularPath);
            mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);
            Common.showLog("PDF_URL:StudentCircularActivity_New", CircularPath);
        } catch (Exception err) {
            Constants.Logwrite("StudentCircularActivity_new", "EX 167::" + err.getMessage());
        }
    }


   /* private class MyTaskLength extends AsyncTask<URL, Void,Void> {

        @Override
        protected Void doInBackground(URL... params) {
            try {
               length=getFileSize(params[0]);
            } catch (Exception err) {
                Constants.Logwrite("DailyDiaryCalander", "Exception:1259" + err.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            *//*super.onPostExecute(length);*//*
        }

    }
*/

    private void downloadfilepdf(String pdfurl) {

        SimpleDateFormat sd = new SimpleDateFormat("yymmhh");
        String date = sd.format(new Date());
        String name = Name + fileExtension;
        File rootFile;
        try {
            if (Environment.getExternalStorageDirectory() != null) {
                rootFile = new File(
                        Environment.getExternalStorageDirectory() + SchoolDetails.PhotoGalleryFolder);
            } else {
                rootFile = new File(Environment.getDataDirectory()
                        + SchoolDetails.PhotoGalleryFolder);
            }
            // have the object build the directory structure, if needed.
            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }

            /*String rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + File.separator + "My_Video";
            File rootFile = new File(rootDir);
            rootFile.mkdir();*/
            URL url = new URL(pdfurl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(rootFile,
                    name));

            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();
        } catch (IOException e) {
            Log.d("Error....", e.toString());
        }


    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(StudentCircularActivity_Download.this,
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            hideKeyboard(StudentCircularActivity_Download.this);
            ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
            if (taskList.get(0).numActivities == 1 &&
                    taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
                StudentCircularActivity_Download.this.finish();
            } else {
                Intent intent = new Intent(StudentCircularActivity_Download.this, NoticeBoardActivity.class);
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

    private class DownloadPdfFileTask extends AsyncTask<URL, Void, String> {
        boolean result = false;
        ProgressDialog dialog;
        String isSimilar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // mProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(URL[] params) {
            // do above Server call here
            try {
                final URLConnection connection = params[0].openConnection();
                final int length = connection.getContentLength();
                if (file.exists()) {
                    if (file.length() == length) {
                        isSimilar = "true";
                    }
                } else {
                    downloadfilepdf(CircularPath);
                   /* request.setDestinationInExternalPublicDir(SchoolDetails.PhotoGalleryFolder, Name + fileExtension);
                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);*/
                }
                Log.e("file     fileUrl", String.valueOf(file.length()) + "  " + String.valueOf(length));
            } catch (Exception e) {
                Log.e("count", "error");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String message) {
            //mProgressBar.setVisibility(View.GONE);
            if (isSimilar.equals("true")) {
                Toast.makeText(StudentCircularActivity_Download.this, "File Already Exists", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
