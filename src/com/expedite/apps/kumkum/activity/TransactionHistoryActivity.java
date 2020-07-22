package com.expedite.apps.kumkum.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.adapter.TransactionHistoryListAdapter;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.google.android.gms.ads.AdView;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

public class TransactionHistoryActivity extends BaseActivity {
    private ListView lstTransaction;
    private HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
    private TransactionHistoryListAdapter adp;
    private String[] values;
    private ProgressBar mProgressBar;
    LinearLayout empty_folder_lyt_file_detail;
    private AdView mAdView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        init();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.TransactionHistoryActivity);
            Constants.setActionbar(getSupportActionBar(), TransactionHistoryActivity.this,
                    TransactionHistoryActivity.this, "Transaction History",
                    "Transaction History", ActivityNames.TransactionHistoryActivity);
            mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
            db = new DatabaseHandler(TransactionHistoryActivity.this);
            empty_folder_lyt_file_detail=findViewById(R.id.empty_folder_lyt_file_detail);
            lstTransaction = (ListView) findViewById(R.id.lstTransection);
            new MyTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_ONLINE_PAYMENT_HISTORY_STUDENT);
                request.addProperty("stud_id", Integer.parseInt(Datastorage.GetStudentId(getApplication())));
                request.addProperty("school_id", Integer.parseInt(Datastorage.GetSchoolId(getApplication())));
                request.addProperty("Year_ID", Integer.parseInt(Datastorage.GetCurrentYearId(getApplication())));
                SoapObject result = Constants.CallWebMethod(
                        TransactionHistoryActivity.this, request, Constants.GET_ONLINE_PAYMENT_HISTORY_STUDENT, true);
                if (result != null && result.getPropertyCount() > 0) {
                    SoapObject obj2 = (SoapObject) result.getProperty(0);
                    if (obj2 != null) {
                        int count = obj2.getPropertyCount();
                        values = new String[count];
                        for (int i = 0; i < count; i++) {
                            values[i] = obj2.getProperty(i).toString();
                        }
                    }
                }
            } catch (Exception ex) {
                Constants.Logwrite("TransactionHistoryActivity", "Exception 1079:" + ex.getMessage() + "::::::::::" + ex.getStackTrace());
                Constants.writelog("TransactionHistoryActivity", "Exception 1079:" + ex.getMessage() + "::::::::::" + ex.getStackTrace());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            try {
                if (Constants.isShowInternetMsg) {
                    empty_folder_lyt_file_detail.setVisibility(View.VISIBLE);
                    Constants.NotifyNoInternet(TransactionHistoryActivity.this);
                } else {
                    empty_folder_lyt_file_detail.setVisibility(View.GONE);
                    if (values != null && values.length > 0) {
                        adp = new TransactionHistoryListAdapter(TransactionHistoryActivity.this, values);
                        lstTransaction.setAdapter(adp);
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(TransactionHistoryActivity.this).create();
                        alertDialog.setTitle("Information");
                        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                        alertDialog.setIcon(R.drawable.information);
                        alertDialog.setCancelable(false);
                        alertDialog.setButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(TransactionHistoryActivity.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                        onBackClickAnimation();
                                    }
                                });
                        // Showing Alert Message
                        mProgressBar.setVisibility(View.GONE);
                        alertDialog.show();
                        return;
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                Constants.writelog("TransactionHistoryActivity", "Exception 225:" + err.getMessage() + ":::::" + err.getStackTrace());
                Constants.Logwrite("TransactionHistoryActivity", "Exception 274" + err.getMessage() + "::::::" + err.getStackTrace());
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            menu.clear();
            mapacc = new HashMap<Integer, String>();

            menu.add(0, 1, 1, "Add Account").setTitle("Add Account");
           // menu.add(0, 2, 2, "Remove Account").setTitle("Remove Account");
            menu.add(0, 3, 3, "Set Default Account").setTitle("Set Default Account");
            mapacc = Constants.AddAccount(menu, db);
        } catch (Exception err) {
            Constants.writelog("TransactionHistoryActivity", "Exception: OnprepareOptionsMenu 353" + err.getMessage()
                    + "::::::" + err.getStackTrace());
            Constants.Logwrite("TransactionHistoryActivity:", "onPrepareOptionsMenu:" + err.getMessage() + "StackTrace::"
                    + err.getStackTrace().toString());
            return true;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int iid = item.getItemId();
        try {
            if (iid == android.R.id.home) {
                hideKeyboard(TransactionHistoryActivity.this);
                TransactionHistoryActivity.this.finish();
                onBackClickAnimation();
            } else if (iid == 1 || iid == 2 || iid == 3) {
                if (iid == 1) {
                    addAccountClick(TransactionHistoryActivity.this);
                } else if (iid == 2) {
                    removeAccountClick(TransactionHistoryActivity.this);
                } else {
                    accountListClick(TransactionHistoryActivity.this);
                }
            } else {
                String details = mapacc.get(iid).toString();
                Constants.SetAccountDetails(details, TransactionHistoryActivity.this);
                intent = new Intent(TransactionHistoryActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
                onBackClickAnimation();
                return true;
            }
        } catch (Exception err) {
            Constants.writelog("TransactionHistoryActivity", "Exception: OnoptionItemSelected 353" + err.getMessage()
                    + "::::::" + err.getStackTrace());
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        try {
            String isfullpay = db.getIsfullPay(Datastorage.GetStudentId(TransactionHistoryActivity.this),
                    Datastorage.GetSchoolId(TransactionHistoryActivity.this));
            Intent i = new Intent(TransactionHistoryActivity.this, FeesPayActivity.class);
            if (isfullpay.equalsIgnoreCase("0")) {
                i = new Intent(TransactionHistoryActivity.this, CustomFeesActivity.class);
            }
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
            onBackClickAnimation();
        } catch (Exception ex) {
            Constants.writelog("TransactionHistoryActivity", "OnBackPress()247 " + ex.getMessage());
        }
    }
}
