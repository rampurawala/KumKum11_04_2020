package com.expedite.apps.kumkum.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.ShareActionProvider;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;

public class PdfViewAcitivity extends AppCompatActivity {

    private String mImageUrl = "", Name = "";
    private WebView mWebView;
    private ProgressBar mProgressbar;
    private SeekBar mSeekBar;
    Common common;

    private ShareActionProvider shareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view_acitivity);

        if (getIntent() != null && getIntent().getExtras() != null) {
            mImageUrl = getIntent().getExtras().getString("ImageUrl", "");
            Name = getIntent().getExtras().getString("Name", "");
            //   CommonClass.showLog("PDFUrl", mUrl);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(Name);
        }
        init();

    }

    private void init() {
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
            }
        });


        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + mImageUrl);
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                PdfViewAcitivity.this.finish();
                onBackClickAnimation();
                return true;
            default:
                break;
        }
        return false;
    }

    public void onBackClickAnimation() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
