package com.expedite.apps.kumkum.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.Banner;
import com.google.android.gms.ads.AdView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackVehicle extends BaseActivity {
    private WebView mWebView;
    private String RouteId = "", Tag = "TrackVehicle";
    private ProgressBar mProgressBar;
    private AdView mAdView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_vehicle);
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.TrackVehicle);
            Constants.setActionbar(getSupportActionBar(), TrackVehicle.this, TrackVehicle.this,
                    "Vehicle Location", "vehicleTracking", ActivityNames.TrackVehicle);
            mWebView = findViewById(R.id.webViewTrackVehicle);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            Intent notificationIntent = getIntent();
            String stid = notificationIntent.getStringExtra("Studid");
            String schid = notificationIntent.getStringExtra("Schoolid");
            try {
                if (stid != null && !stid.isEmpty() && schid != null && !schid.isEmpty()) {
                    String Concatestr = db.GetAccount_DetailsUsing_StudID(Integer.parseInt(stid), Integer.parseInt(schid));
                    Constants.Logwrite("RedirectActivity:", "Concatestr:" + Concatestr);
                    String[] con_splt = Concatestr.split(",");
                    String Stud_Name = con_splt[2];
                    Datastorage.SetStudentName(TrackVehicle.this, Stud_Name);
                    Datastorage.SetStudentID(TrackVehicle.this, stid);
                    String School_Id = con_splt[4];
                    Datastorage.SetSchoolId(TrackVehicle.this, School_Id);
                    String Year_Id = con_splt[3];
                    Datastorage.SetCurrentYearId(TrackVehicle.this, Year_Id);
                    String Class_Id = con_splt[5];
                    Datastorage.SetClassId(TrackVehicle.this, Class_Id);
                    String Sec_Id = con_splt[6];
                    Datastorage.SetClassSecId(TrackVehicle.this, Sec_Id);
                    String User_Id = con_splt[7];
                    Datastorage.SetUserId(TrackVehicle.this, User_Id);
                    String classname = con_splt[8];
                    Datastorage.SetClassSecName(TrackVehicle.this, classname);
                    String studenrolldate = con_splt[9];
                    Datastorage.SetEnrollDate(TrackVehicle.this, studenrolldate);
                    String lastupdatedtime = con_splt[10];
                    Datastorage.LastUpdatedtime(TrackVehicle.this, lastupdatedtime);
                    String academicyear = con_splt[11];
                    Datastorage.SetAcademicYear(TrackVehicle.this, academicyear);
                    String classsecname = con_splt[12];
                    Datastorage.SetClassSectionName(TrackVehicle.this, classsecname);
                    RouteId = con_splt[13];
                    Datastorage.SetRouteId(TrackVehicle.this, Integer.parseInt(RouteId));
                }
            } catch (Exception ex) {
                Constants.writelog("VechicleLocationActivity", "Exception 159:"
                        + ex.getMessage() + "::::" + ex.getStackTrace());
            }

            String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
            String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());

            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, final String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    mProgressBar.setVisibility(View.VISIBLE);
                    /*if (url != null && !url.isEmpty() && url.contains("map")) {

                    } else {
                        onBackPressed();
                    }*/
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
            mWebView.setKeepScreenOn(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);
            getMapUrl(mStudentId, mYearId, mSchoolId);
        } catch (Exception ex) {
            Constants.writelog(Tag, "Exception 74:"
                    + ex.getMessage() + "::::" + ex.getStackTrace());
        }
    }

    private void getMapUrl(String StudentId, String Year_Id, String SchoolId) {
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            try {
                // http://app.vayuna.com/service.asmx/SurveyPollList?schoolId=8414&class_section_Id=32&studId=47&year_id=5
                Call<Banner> call = ((MyApplication) getApplicationContext())
                        .getmRetrofitInterfaceAppService().GetMapUrl(StudentId, SchoolId, Year_Id, RouteId, Constants.PLATFORM);

                call.enqueue(new Callback<Banner>() {
                    @Override
                    public void onResponse(Call<Banner> call, Response<Banner> response) {
                        try {
                            Banner result = response.body();
                            if (result != null && result.getResponse() != null && result.getResponse().equals("1")) {
                                mWebView.loadUrl(result.getMessage().toString());
                            }
                            mProgressBar.setVisibility(View.GONE);
                        } catch (Exception ex) {
                            Constants.writelog(Tag, "Exception_113:" + ex.getMessage()
                                    + "::::::" + ex.getStackTrace());
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Banner> call, Throwable t) {
                        Constants.writelog(Tag, "Exception_113:" + t.getMessage());
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

            } catch (Exception ex) {
                mProgressBar.setVisibility(View.GONE);
                Constants.writelog(Tag, "Exception_113:" + ex.getMessage()
                        + "::::::" + ex.getStackTrace());
            }
        } else {
            Common.showToast(TrackVehicle.this, getString(R.string.msg_connection));
        }
    }
}
