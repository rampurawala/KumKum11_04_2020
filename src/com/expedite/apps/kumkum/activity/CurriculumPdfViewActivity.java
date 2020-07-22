package com.expedite.apps.kumkum.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Jaydeep patel on 11/21/2017.
 */
public class CurriculumPdfViewActivity extends BaseActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {
    private WebView mWebView;
    private ProgressBar mProgressbar;
    private String mUrl = "", mName = "", localPdfPath = "", mImageUrl = "", mStrIsFrom = "",parentId="",filenameDownload="";
    private AdView mAdView;
    private ProgressDialog mPdfProgressDialog;
    private PDFView mPdfView;
    private boolean isSaveSucessFully = false;
    Integer pageNumber = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        if (getIntent() != null && getIntent().getExtras() != null) {
            mUrl = getIntent().getExtras().getString("Url", "");
            mName = getIntent().getExtras().getString("Name", "");
            parentId = getIntent().getExtras().getString("parentId", "");
            mStrIsFrom = getIntent().getExtras().getString("isFrom", "");
            Common.showLog("PDFUrl", mUrl);
        }
        String[] parts = mUrl.split("/");
        filenameDownload = parts[parts.length - 1];
        init();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.CurriculumPdfViewActivity);
            Activity abc = this;
            Constants.setActionbar(getSupportActionBar(), abc, getApplicationContext(),
                    mName, mName, ActivityNames.CurriculumPdfViewActivity);
            mProgressbar = (ProgressBar) findViewById(R.id.progressBar);
            mPdfView = (PDFView) findViewById(R.id.pdfView);
            mPdfProgressDialog = new ProgressDialog(CurriculumPdfViewActivity.this);
         /*   View customview = CurriculumPdfViewActivity.this.getLayoutInflater().inflate(R.layout.mainactionbar, null);
            TextView label = (TextView) customview.findViewById(R.id.label);
            label.setText(mName);
            getSupportActionBar().setCustomView(customview);*/

            //Chnages By Vijay Solanki 13-04-2020
            if (ContextCompat.checkSelfPermission(CurriculumPdfViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                checkpermissionstatus(2);
            } else {
                savePdf();
            }

            mWebView = (WebView) findViewById(R.id.webview_web);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, final String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    mProgressbar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (view.getProgress() == 100) {
                        mProgressbar.setVisibility(View.GONE);
                    }
                }

                /* @Override
                 public boolean shouldOverrideUrlLoading(WebView view, String url) {
                     view.loadUrl(url);
                     return true;
                 }
 */
                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    //Constants.writelog("CurriculumPdfView", "webviewclient():75 " + error);
                    Log.d("Webviewclient", error.toString());
                    mProgressbar.setVisibility(View.GONE);
                    view.loadUrl("http://docs.google.com/gview?embedded=true&url=" + mUrl);
                }
            });
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + mUrl);
            //mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);
            mWebView.setVisibility(View.GONE);

        } catch (Exception ex) {
            Common.printStackTrace(ex);
            Constants.writelog("CurriculumPdfView", "init():75 " + ex.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_refresh, menu);
        return true;
    }


    public void checkpermissionstatus(int i) {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                Boolean isanyDeny = false;
                Boolean isadded = false;
                String permissions = "";
                int permissionStatus = 0;
                if (i == 2) {
                    permissionStatus = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                        isanyDeny = true;
                        isadded = true;
                        if (isadded) {
                            permissions += "," + Manifest.permission.WRITE_EXTERNAL_STORAGE;
                        } else {
                            permissions += Manifest.permission.WRITE_EXTERNAL_STORAGE;
                        }
                    }
                    String[] perm = permissions.split(",");
                    ActivityCompat.requestPermissions(CurriculumPdfViewActivity.this, perm, 1);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:

            case 2:
                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    savePdf();
                } else {
                    Toast.makeText(CurriculumPdfViewActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void savePdf() {
        try {
            // Change by tejas patel 26-11-2019
            String path = Constants.FILE_PDF_PATH + File.separator + filenameDownload;
            File mFile = new File(path);

            if (!mFile.exists() || mFile.length() < 200) {
                if (isOnline()) {
                    new SavePDFTask().execute(path);
                } else {
                    Common.showToast(CurriculumPdfViewActivity.this, getString(R.string.msg_connection));
                }
            } else {
                try {
                    Uri uri = Uri.fromFile(new File(path));
                    mPdfView.fromUri(uri)
                            .defaultPage(pageNumber)
                            .onPageChange(this)
                            .enableAnnotationRendering(true)
                            .onLoad(this)
                            .scrollHandle(new DefaultScrollHandle(this))
                            .spacing(10) // in dp
                            .onPageError(this)
                            .load();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                mProgressbar.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            Constants.writelog("exception:433", String.valueOf(ex.getMessage()));
        }
    }

    private void refreshData() {
        try {
            String path = Constants.FILE_PDF_PATH + File.separator + filenameDownload;
            File mFile = new File(path);
            if (mFile.exists()) {
                mFile.delete();
            }
            if (mUrl != null && !mUrl.isEmpty()) {
                savePdf();
            }
        } catch (Exception ex) {
            Constants.writelog("ImageviewActivity:885", String.valueOf(ex.getMessage()));
        }
    }


    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Constants.writelog("PDFviewActivty:710", "Cannot load page " + page);
    }


    public class SavePDFTask extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            try {
                mPdfProgressDialog.setMessage("Please wait for loading Pdf...");
                mPdfProgressDialog.setIndeterminate(false);
                mPdfProgressDialog.setMax(100);
                mPdfProgressDialog.setCanceledOnTouchOutside(false);
                mPdfProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mPdfProgressDialog.setCancelable(true);
                mPdfProgressDialog.setOnKeyListener(new ProgressDialog.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK) {

                            if (mPdfProgressDialog.isShowing()) {
                                String path = Constants.FILE_PDF_PATH + File.separator +filenameDownload;
                                File mFile = new File(path);
                                if (mFile.exists()) {
                                    mFile.delete();
                                }
                                if (mStrIsFrom != null && mStrIsFrom.equalsIgnoreCase("NoticeBoardActiviy")) {
                                    Intent intent = new Intent(CurriculumPdfViewActivity.this, NoticeBoardActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    onBackClickAnimation();
                                } else if (mStrIsFrom != null && mStrIsFrom.equalsIgnoreCase("CurriculamResponse")) {
                                    Intent intent = new Intent(CurriculumPdfViewActivity.this, TaskCompleteActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    onBackClickAnimation();
                                } else {
                                    mPdfProgressDialog.dismiss();
                                    CurriculumPdfViewActivity.this.finish();
                                    onBackClickAnimation();
                                }
                            }
                        }
                        return true;
                    }
                });
                mPdfProgressDialog.show();
                //   mProgressbar.setVisibility(View.VISIBLE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                localPdfPath = params[0];
                //  mUrl = mImageUrl;
                mUrl = mUrl.replace(" ", "%20");
                File myDir = CreateDownloadPDFFolder();
                final File file = new File(myDir, filenameDownload );
                FileOutputStream f = new FileOutputStream(file);
                URL u = new URL(mUrl);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                // c.setDoOutput(true);
                c.connect();
                int lenghtOfFile = c.getContentLength();
                InputStream in = c.getInputStream();

                byte[] buffer = new byte[2048];
                int len1 = 0;
                long total = 0;

                while ((len1 = in.read(buffer)) > 0) {
                    total += len1;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    f.write(buffer, 0, len1);
                }
                f.close();
//                if (file != null && !file.getAbsolutePath().isEmpty()) {
//                    savePdfmessage = "Save pdf to " + "\"" + file.getAbsolutePath() + "\"" + " Successfully";
//                } else {
//                    savePdfmessage = "Save pdf is Successfully";
//                }
                isSaveSucessFully = true;
            } catch (Exception ex) {
                Constants.writelog("exception:564", String.valueOf(ex.getMessage()));
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            mPdfProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(Void result) {
            // mProgressbar.setVisibility(View.GONE);
            if (mPdfProgressDialog != null && mPdfProgressDialog.isShowing())
                mPdfProgressDialog.dismiss();
            try {
                //Toast.makeText(PDFviewActivity.this, savePdfmessage, Toast.LENGTH_LONG).show();
                if (isSaveSucessFully) {
                    try {
                        Uri uri = Uri.fromFile(new File(localPdfPath));
                        mPdfView.fromUri(uri)
                                .defaultPage(pageNumber)
                                .onPageChange(CurriculumPdfViewActivity.this)
                                .enableAnnotationRendering(true)
                                .onLoad(CurriculumPdfViewActivity.this)
                                .scrollHandle(new DefaultScrollHandle(CurriculumPdfViewActivity.this))
                                .spacing(10) // in dp
                                .onPageError(CurriculumPdfViewActivity.this)
                                .load();
                    } catch (Exception ex) {
                        setWebViewData(mUrl);
                        ex.printStackTrace();
                    }
                    mProgressbar.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                setWebViewData(mUrl);
                ex.printStackTrace();
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.refresh_type:
                refreshData();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setWebViewData(String mUrl) {
        try {
            mPdfView.setVisibility(View.GONE);
            Constants.writelog("url", mImageUrl);
            mWebView.setVisibility(View.VISIBLE);
            mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + mUrl);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (mStrIsFrom != null && mStrIsFrom.equalsIgnoreCase("NoticeBoardActiviy")) {
            Intent intent = new Intent(CurriculumPdfViewActivity.this, NoticeBoardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            onBackClickAnimation();
        }/* else if (mStrIsFrom != null && mStrIsFrom.equalsIgnoreCase("CurriculamResponse")) {
            Intent intent = new Intent(CurriculumPdfViewActivity.this, TaskCompleteActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            onBackClickAnimation();
        }*/ else {
            super.onBackPressed();
            if(mStrIsFrom.equalsIgnoreCase("notification")){

                Intent i=new Intent(CurriculumPdfViewActivity.this,HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);     }
            CurriculumPdfViewActivity.this.finish();
            onBackClickAnimation();
        }
    }

}
