package com.expedite.apps.kumkum.activity;

import android.app.ActivityManager;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.RequiresApi;
import androidx.core.content.PermissionChecker;

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

import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class StudentCircularActivity_New extends BaseActivity {
    private WebView mWebView;
    static private String CircularPath = "", Name = "";
    private ProgressBar mProgressBar;
    String permissions = "", fileExtension = "", IsNotification = "";
    int permissionStatus = 0;
    DownloadManager.Request request;
    private Context context;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 125;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_circular);

        if (getIntent() != null && getIntent().getExtras() != null) {
            Intent intnt = getIntent();
            CircularPath = intnt.getStringExtra("CircularPath");
            Name = intnt.getStringExtra("Name");
            IsNotification = intnt.getStringExtra("IsNotification");
            fileExtension = CircularPath.substring(CircularPath.lastIndexOf("."));
            Common.showLog("PDF_URL:StudentCircularActivity_New", Name);
            Common.showLog("PDF_URL:StudentCircularActivity_New", CircularPath);
        }

        //  final boolean resultRead = Utility.checkPermission(StudentCircularActivity_New.this);
        //  final boolean resultWrite = Utility.checkWritePermission(StudentCircularActivity_New.this);

      /*  int currentAPIVersion = Build.VERSION.SDK_INT;


        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            permissionStatus = PermissionChecker.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionStatus != PermissionChecker.PERMISSION_GRANTED)

                ActivityCompat.requestPermissions((Activity) context, new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }else {*/

        /*  }*/
        /*permissionStatus = PermissionChecker.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            new DownloadPdfOrImageFile().execute(CircularPath, Name + fileExtension);
        }*/

        init();


    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {
        Boolean isExists;

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, SchoolDetails.PhotoGalleryFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File pdfFile = new File(folder, fileName);
            String path = Environment.getExternalStorageDirectory()
                    + File.separator
                    + SchoolDetails.PhotoGalleryFolder + "/" + Name + fileExtension;

            File localfile = new File(path);
            if (localfile.exists()) {
                isExists = new FileDownloader().checkExists(fileUrl, localfile.length());
                if (isExists == false) {
                    new FileDownloader().downloadFile(fileUrl, pdfFile);
                }
            } else {
                /*try {
                    pdfFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                new FileDownloader().downloadFile(fileUrl, pdfFile);
            }
            return null;
        }
    }

    public void init() {
        Constants.setActionbar(getSupportActionBar(), StudentCircularActivity_New.this, StudentCircularActivity_New.this,
                "Student Circular", "StudentCircularActivity", ActivityNames.StudentCircularActivity_New);
        mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        try {
            mWebView = (WebView) findViewById(R.id.webView1);
            mWebView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
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

                        new DownloadFile().execute(CircularPath, Name + fileExtension);
                    }
                }
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return false;
                }
            });
            /*//test
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            Constants.writelog("url", CircularPath);
            mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + CircularPath);
            mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);*/




            mWebView.setVisibility(View.VISIBLE);
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

    @Override
    public void onBackPressed() {
        Intent intent = null;
        if (IsNotification != null && !IsNotification.isEmpty()) {
            intent = new Intent(StudentCircularActivity_New.this,
                    HomeActivity.class);

        } else {
            intent = new Intent(StudentCircularActivity_New.this,
                    NoticeBoardActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
        onBackClickAnimation();
        super.onBackPressed();
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
            hideKeyboard(StudentCircularActivity_New.this);
            ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
            if (taskList.get(0).numActivities == 1 &&
                    taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
                StudentCircularActivity_New.this.finish();
            } else {
                Intent intent = new Intent(StudentCircularActivity_New.this, NoticeBoardActivity.class);
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


    public class FileDownloader {
        private final int MEGABYTE = 1024 * 1024;

        public void downloadFile(String fileUrl, File directory) {
            try {
                //       Toast.makeText(context, "File is Downloading", Toast.LENGTH_SHORT).show();
                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.setRequestMethod("GET");
                //urlConnection.setDoOutput(true);
                urlConnection.connect();

                //InputStream inputStream = urlConnection.getInputStream();
                //FileOutputStream fileOutputStream = new FileOutputStream(directory);
                // int totalSize = urlConnection.getContentLength();

                //   final boolean resultRead = Utility.checkPermission(StudentCircularActivity_New.this);
                //     final boolean resultWrite = Utility.checkWritePermission(StudentCircularActivity_New.this);
                //download manager
                permissionStatus = PermissionChecker.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
                    request.setDestinationInExternalPublicDir(SchoolDetails.PhotoGalleryFolder, Name + fileExtension);
                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                    //  }

                /*byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                fileOutputStream.close();*/
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Boolean checkExists(String fileUrl, long length) {
            try {

                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.setRequestMethod("GET");
                //urlConnection.setDoOutput(true);
                urlConnection.connect();
                long totalSize = urlConnection.getContentLength();
                if (totalSize == length) {
                    return true;
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

    }
}
