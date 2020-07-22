package com.expedite.apps.kumkum.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.google.android.gms.ads.AdView;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

public class CustomFeesActivity extends BaseActivity {

    private TextView btnPay;
    private int minAmount = 50, maxAmount = 50000, totalAmount = 0;
    private Menu menu;
    private HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
    private EditText txtAmount;
    private String mIsFromHome = "";
    private ProgressBar mProgressbar;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_fees);
        if (getIntent() != null && getIntent().getExtras() != null)
            mIsFromHome = getIntent().getExtras().getString("IsFromHome", "");
        init();
    }

    public void init() {
        mAdView = (AdView) findViewById(R.id.adView);
        showBannerAd(mAdView, ActivityNames.CustomFeesActivity);
        mProgressbar = (ProgressBar) findViewById(R.id.ProgressBar);
        db = new DatabaseHandler(this);
        try {
            Constants.setActionbar(getSupportActionBar(), this, CustomFeesActivity.this,
                    "Pay Fees Online", "PayFeesOnlineCustom",ActivityNames.CustomFeesActivity);
            txtAmount = (EditText) findViewById(R.id.amount);
            btnPay = (TextView) findViewById(R.id.btn_payfees);
            btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String amount = txtAmount.getText().toString();
                    if (amount != null && !amount.trim().equals("")) {
                        totalAmount = Integer.parseInt(amount);
                    }
                    if (totalAmount > maxAmount) {
                        Toast.makeText(CustomFeesActivity.this, "Please Select Fees Amount Less then " + maxAmount + ".", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (totalAmount < minAmount) {
                        Toast.makeText(CustomFeesActivity.this, "Please Select Fees Amount More then " + minAmount + ".", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (isOnline()) {
                        Intent intent = new Intent(CustomFeesActivity.this, CardPaymentDetailActivity.class);
                        intent.putExtra("Amount", totalAmount);
                        intent.putExtra("ExtraDetails", "");
                        Constants.writelog("FeesPayActivity", "BtnPayClick()227 Amount:" + totalAmount);
                        startActivity(intent);
                        finish();
                        onClickAnimation();
                    } else {
                        Toast.makeText(CustomFeesActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_LONG).show();
                    }
                }
            });
            new MyTask().execute();
        } catch (Exception err) {
            Constants.writelog(
                    "CustomFeesActivity",
                    "OnCreate()53 Exception:" + err.getMessage() + ":::"
                            + err.getStackTrace());
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            SetMinMax();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(CustomFeesActivity.this);
                }
                mProgressbar.setVisibility(View.GONE);
            } catch (Exception err) {
                Constants.writelog("CustomFeesActivity", "onPostExecute()245 Exception:"
                        + err.getMessage() + ":::" + err.getStackTrace());
                mProgressbar.setVisibility(View.GONE);
            }
        }
    }

    public void SetMinMax() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_MIN_MAX_AMOUNT_FOR_ONLINE_PAYMENT);
        try {
            SoapObject result = Constants.CallWebMethod(
                    CustomFeesActivity.this, request, Constants.GET_MIN_MAX_AMOUNT_FOR_ONLINE_PAYMENT, true);
            if (result != null && result.getPropertyCount() > 0) {
                String res = result.getProperty(0).toString();
                String[] parts = res.split("##@@");
                minAmount = Integer.parseInt(parts[0]);
                maxAmount = Integer.parseInt(parts[1]);
            }
        } catch (Exception err) {
            Constants.Logwrite("FeesPayActivity", "MSG 474:" + err.getMessage() + ":StackTrace:"
                    + err.getStackTrace());
            Constants.writelog("CustomFeesActivity", "SetMinMax()476 Exception:"
                    + err.getMessage() + ":::" + err.getStackTrace());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            menu.clear();
            MenuInflater objmenuinfl = getMenuInflater();
            objmenuinfl.inflate(R.menu.activity_payfees_options, menu);
            mapacc = Constants.AddAccount(menu, db);
            this.menu = menu;

        } catch (Exception err) {
            Constants.writelog("PhotoGalleryActivity", "onCreateOptionsMenu()535 Error::" + err.getMessage());
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int iid = item.getItemId();
        if (iid == android.R.id.home) {
            hideKeyboard(CustomFeesActivity.this);
            CustomFeesActivity.this.finish();
            onBackClickAnimation();
        } else if (iid == 1) {
            Constants.googleAnalyticEvent(CustomFeesActivity.this, Constants.button_click, "Transaction History");
            intent = new Intent(CustomFeesActivity.this,
                    TransactionHistoryActivity.class);
            startActivity(intent);
            finish();

        } else {
            String details = mapacc.get(iid).toString();
            Constants.SetAccountDetails(details,
                    CustomFeesActivity.this);
            intent = new Intent(CustomFeesActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
            onBackClickAnimation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mIsFromHome != null && !mIsFromHome.isEmpty()) {
            super.onBackPressed();
            onBackClickAnimation();
        } else {
            Intent intent = new Intent(CustomFeesActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }
}
