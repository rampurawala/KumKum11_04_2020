package com.expedite.apps.kumkum.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.adapter.CircularListAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoticeActivity extends BaseActivity {
    private ListView ItmView1;
    private CircularListAdapter adp;
    private HashMap<String, String> map1 = new HashMap<String, String>();
    private String[] test = null, ItmNames1 = null;
    private ProgressBar mProgressBar;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        if (getIntent() != null && getIntent().getStringArrayExtra("group name1") != null)
             test = getIntent().getStringArrayExtra("group name1");

        init();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.NoticeActivity);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            String title = "";
            int Cnt_Count = 0;
            Cnt_Count = Datastorage.GetMultipleAccount(NoticeActivity.this);
            if (Cnt_Count == 1) {
                title = "Notice" + "-"
                        + Datastorage.GetStudentName(NoticeActivity.this);
            } else {
                title = "Notice";
            }
            showBannerAd(mAdView, ActivityNames.NoticeActivity);
            Constants.setActionbar(getSupportActionBar(), NoticeActivity.this, NoticeActivity.this,
                    title, "Circular", ActivityNames.NoticeActivity);
            ItmView1 = (ListView) findViewById(R.id.lstcirlist1);
            new MyTask().execute();
            ItmView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    try {
                        String info[] = test[arg2].split("@@##");
                        Intent intent = new Intent(NoticeActivity.this, StudentCircularActivity_New.class);
                        intent.putExtra("CirId", info[0]);
                        startActivity(intent);
                        onClickAnimation();
                    } catch (Exception err) {}
                }
            });
        } catch (Exception err) {
        }
        try {
            int count = test.length;
            List<String> list = new ArrayList<String>();
            for (int i = 0; i < count; i++) {
                // myarray[i] = test[i];
                String info[] = test[i].split("@@##");
                list.add(info[1]);
            }
            ItmNames1 = new String[list.size()];
            for (int j = 0; j < list.size(); j++) {
                map1.put("", list.get(j));
                ItmNames1[j] = list.get(j);
            }

        } catch (Exception ex) {
            Constants.writelog("NoticeActivity", "136 Ex:" + ex.getMessage()
                    + "::::::" + ex.getStackTrace());
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            try {
                if (ItmNames1 != null && ItmNames1.length > 0) {
                    adp = new CircularListAdapter(NoticeActivity.this,
                            ItmNames1);
                    ItmView1.setAdapter(adp);
                } else {

                    AlertDialog alertDialog = new AlertDialog.Builder(NoticeActivity.this).create();
                    alertDialog.setTitle("Information");
                    alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                    alertDialog.setIcon(R.drawable.information);
                    alertDialog.setCancelable(false);
                    alertDialog.setButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(NoticeActivity.this, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    finish();
                                    onBackClickAnimation();
                                }
                            });

                    mProgressBar.setVisibility(View.GONE);
                    alertDialog.show();
                    return;
                }
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                Common.printStackTrace(err);
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
