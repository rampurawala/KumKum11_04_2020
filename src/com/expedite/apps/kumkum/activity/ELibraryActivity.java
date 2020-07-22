package com.expedite.apps.kumkum.activity;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.model.AppService;
import com.expedite.apps.kumkum.model.Contact;
import com.expedite.apps.kumkum.model.ExamListModel;

import java.util.HashMap;
import java.util.List;

public class ELibraryActivity extends BaseActivity {
    Activity abc = this;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private String mFrom = "", SchoolId = "", StudentId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elibrary);
        try {
            mWebView = (WebView) findViewById(R.id.webview);
            mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
            SchoolId = Datastorage.GetSchoolId(ELibraryActivity.this);
            StudentId = Datastorage.GetStudentId(ELibraryActivity.this);
            String title = "";
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                setTitle("E-Library");
            }
            mWebView.setWebViewClient(new WebViewClient() {
                /*  @Override
                  public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                      return false;
                  }
  */
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

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    if (error.toString() == "piglet")
                        handler.cancel();
                    else
                        handler.proceed();
                }
            });

            mWebView.setDownloadListener(new DownloadListener() {
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                            long contentLength) {
                    try {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {
            Common.showLog("Webview", "Ex()57:" + ex.getMessage());
        }
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        getELibrary();
    }

    private void getELibrary() {

        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            Call<AppService> call = ((MyApplication)
                    getApplicationContext()).getmRetrofitInterfaceAppService().getELibrary(StudentId, SchoolId,
                    Constants.CODEVERSION, Constants.PLATFORM);
            call.enqueue(new Callback<AppService>() {
                @Override
                public void onResponse(Call<AppService> call, Response<AppService> response) {
                    try {
                        AppService tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty() && tmps.getResponse().equalsIgnoreCase("1")) {
                            if (tmps.getStrResult() != null && tmps.getStrResult() != "") {
                                //    String  mUrl = "https://docs.google.com/gview?embedded=true&url=" + tmps.getStrResult();
                                mWebView.loadUrl(tmps.getStrResult());
                            } else
                                Toast.makeText(ELibraryActivity.this, "No Url Available!", Toast.LENGTH_SHORT).show();
                        }
                        mProgressBar.setVisibility(View.GONE);
                    } catch (Exception ex) {
                        mProgressBar.setVisibility(View.GONE);
                        Constants.writelog("getELibrary", "Exception 116:" + ex.getMessage() + "::::" + ex.getStackTrace());
                    }
                }

                @Override
                public void onFailure(Call<AppService> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    Constants.writelog("getELibrary", "Exception 124:" + t.getMessage());
                }
            });
        } else {
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(ELibraryActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.copyBackForwardList().getCurrentIndex() > 0) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
            ELibraryActivity.this.finish();
            onBackClickAnimation();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return false;
    }
}

