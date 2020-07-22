package com.expedite.apps.kumkum.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.google.android.gms.ads.AdView;

public class CurriculumTextFileActivity extends BaseActivity {
    TextView curriculumText;
    String mUrl = "", mName = "",isFrom="",parentId="";
    Activity abc = this;
    Common commonClass;
    AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        curriculumText = findViewById(R.id.curriculumText);
        if (getIntent() != null && getIntent().getExtras() != null) {
            mUrl = getIntent().getExtras().getString("Url", "");
            mName = getIntent().getExtras().getString("Name", "Text File");
            parentId = getIntent().getExtras().getString("parentId", "");
            isFrom = getIntent().getExtras().getString("isFrom", "");
        }
        curriculumText.setMovementMethod(new ScrollingMovementMethod());
        commonClass = new Common(this);
        Activity abc = this;
        Constants.setActionbar(getSupportActionBar(), abc, getApplicationContext(),
                mName, "Curriculum", ActivityNames.CurriculumTextFileActivity);
        mAdView = (AdView) findViewById(R.id.adView);
        showBannerAd(mAdView, ActivityNames.CurriculumTextFileActivity);
        showFullScreenAd(CurriculumTextFileActivity.this, ActivityNames.CurriculumTextFileActivity);
        curriculumText.setText(mUrl);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isFrom.equalsIgnoreCase("notification")){

            Intent i=new Intent(CurriculumTextFileActivity.this,HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);    }
        CurriculumTextFileActivity.this.finish();
        onBackClickAnimation();
    }
}
