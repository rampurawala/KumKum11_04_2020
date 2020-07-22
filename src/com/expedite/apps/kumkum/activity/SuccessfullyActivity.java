package com.expedite.apps.kumkum.activity;


import android.content.Intent;
import android.os.Bundle;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.google.android.gms.ads.AdView;

public class SuccessfullyActivity extends BaseActivity {

    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfull);
        mAdView = (AdView) findViewById(R.id.adView);
        showBannerAd(mAdView, ActivityNames.SuccessfullyActivity);

        Constants.setActionbar(getSupportActionBar(), SuccessfullyActivity.this,
                SuccessfullyActivity.this, "Successfully Send", "SuccessfullyActivity", ActivityNames.SuccessfullyActivity);
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(SuccessfullyActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            onBackClickAnimation();
        } catch (Exception err) {
            Common.printStackTrace(err);
        }
    }
}
