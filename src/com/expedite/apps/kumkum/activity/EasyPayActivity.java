package com.expedite.apps.kumkum.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

/**
 * Created by Vishwa patel on 26-12-2018.
 */

public class EasyPayActivity extends BaseActivity {
    private WebView mWebView;
    private ProgressBar mProgressbar;
    private String mUrl = "",transactionId="";
    private Common mcommon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        if (getIntent() != null && getIntent().getExtras() != null) {
            mUrl = getIntent().getExtras().getString("URL", "");
            transactionId=getIntent().getExtras().getString("transactionId", "");
            Common.showLog("Easypayurl", mUrl);
        }
        init();
    }

    private void init() {
        try {
            Activity abc = this;
            mcommon = new Common(EasyPayActivity.this);
            Constants.setActionbar(getSupportActionBar(), abc, getApplicationContext(),
                    "Payment", "EasyPay", ActivityNames.EasyPayActivity);
            mProgressbar = (ProgressBar) findViewById(R.id.progressBar);
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
                    if(url.contains(mcommon.getSession(Constants.RESPONSE_URL)))
                    {
                        // Redirect to success page.
                        //pass url to next activity in intent
                        Intent intent = new Intent(EasyPayActivity.this, PaymentResponceActivity.class);
                        intent.putExtra("transactionid",transactionId);
                        intent.putExtra("isEasypay","1");
                        startActivity(intent);
                        onClickAnimation();
                    }
                }
            });
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mWebView.loadUrl(mUrl);
            mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                EasyPayActivity.this.finish();
                onBackClickAnimation();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EasyPayActivity.this.finish();
        onBackClickAnimation();
    }

}
