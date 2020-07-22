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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.adapter.FeeCardListAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.google.android.gms.ads.AdView;

import org.ksoap2.serialization.SoapObject;

public class FeeCardActivity extends BaseActivity {
    LinearLayout mTxtheader;
    private String[] FeeDetails = null;
    //private FeesItemsListAdapter adp;
    private FeeCardListAdapter adapter;
    //private ListView lstfeedetails;
    private TextView tv;
    private int FeeCardStatus = 1;
    private String SchoolId, StudentId, LastUpdatedTime, Year_Id, log = "FeeCardActivity_Fee_Card";
    private String mIsFromHome = "";
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private View mMaincardfee, empty_feeCard;
    private GridLayoutManager mGridLayoutManager;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_card1);

        if (getIntent() != null && getIntent().getExtras() != null)
            mIsFromHome = getIntent().getExtras().getString("IsFromHome", "");

        init();
    }

    public void init() {

        try {
            mAdView = (AdView) findViewById(R.id.adView);
            SchoolId = Datastorage.GetSchoolId(getApplicationContext());
            StudentId = Datastorage.GetStudentId(getApplicationContext());
            LastUpdatedTime = Datastorage.GetLastUpdatedtime(getApplicationContext());
            Year_Id = Datastorage.GetCurrentYearId(getApplicationContext());
            tv = (TextView) findViewById(R.id.tvmarkqueetextfeecardlist);
            tv.setText(LastUpdatedTime);
            Intent i = getIntent();
            String Fee_Status = i.getStringExtra("FeeStatus");
            if (Fee_Status.equalsIgnoreCase("1")) {
                Constants.setActionbar(getSupportActionBar(), FeeCardActivity.this, getApplicationContext(), "Fee Card", "Fee card",
                        ActivityNames.FeeCardActivity_Fee_Card);
                showBannerAd(mAdView, ActivityNames.FeeCardActivity_Fee_Card);
            } else if (Fee_Status.equalsIgnoreCase("2")) {
                Constants.setActionbar(getSupportActionBar(), FeeCardActivity.this, getApplicationContext(), "Paid Fees", "Paid Fees",
                        ActivityNames.FeeCardActivity_Paid_Fees);
                showBannerAd(mAdView, ActivityNames.FeeCardActivity_Paid_Fees);
            } else if (Fee_Status.equalsIgnoreCase("3")) {
                showBannerAd(mAdView, ActivityNames.FeeCardActivity_PendingFees);
                Constants.setActionbar(getSupportActionBar(), FeeCardActivity.this, getApplicationContext(), "Pending Fees", "Pending Fees",
                        ActivityNames.FeeCardActivity_PendingFees);
            }
            Constants.Logwrite("FeeStatus:", "" + Fee_Status);
            FeeCardStatus = Integer.parseInt(Fee_Status);
            Constants.Logwrite("FeeStatus:", "" + FeeCardStatus);
        } catch (Exception err) {
            Constants.writelog(
                    log,
                    "OnCreate()79 Exception:" + err.getMessage() + ":::"
                            + err.getStackTrace());
        }
        try {
            int Cnt_Count = 0;
            Cnt_Count = Datastorage.GetMultipleAccount(getApplicationContext());
            if (Cnt_Count == 1) {
                if (FeeCardStatus == 1) {
                    setTitle("FeeCard"
                            + "-"
                            + Datastorage
                            .GetStudentName(getApplicationContext()));
                } else if (FeeCardStatus == 2) {
                    setTitle("Fee Paid"
                            + "-"
                            + Datastorage
                            .GetStudentName(getApplicationContext()));
                } else {
                    setTitle("Fee Pending"
                            + "-"
                            + Datastorage
                            .GetStudentName(getApplicationContext()));
                }

            } else {
                if (FeeCardStatus == 1) {
                    setTitle("FeeCard");
                } else if (FeeCardStatus == 2) {
                    setTitle("Fee Paid");
                } else {
                    setTitle("Fee Pending");
                }
            }
            //lstfeedetails = (ListView) findViewById(R.id.lstfeecardlist);
            mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
            mMaincardfee = (View) findViewById(R.id.Maincardfee);
            empty_feeCard = (View) findViewById(R.id.empty_feeCard);
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            mGridLayoutManager = new GridLayoutManager(FeeCardActivity.this, 1);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            //adapter = new FeeCardListAdapter(FeeCardActivity_Fee_Card.this, FeeDetails, FeeCardStatus);
            //mRecyclerView.setAdapter(adapter);
            new GetFeeCard().execute();
        } catch (Exception err) {
            Constants.Logwrite("Fee_Oncreate",
                    "" + err.getMessage() + ":StackTrace:"
                            + err.getStackTrace());
            Constants.writelog(
                    log,
                    "OnCreate()120 Exception:" + err.getMessage() + ":::"
                            + err.getStackTrace());
        }
    }

    public String[] FeeCardDetails() {
        Constants.Logwrite("Fee-Enter", "FeecardDetails");
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.FEES_CARD_DETAIL_V1);
        request.addProperty("studid", Integer.parseInt(StudentId));
        Constants.Logwrite("Fee-Enter", "StudId:" + Integer.parseInt(StudentId));
        request.addProperty("schoolid", Integer.parseInt(SchoolId));
        Constants.Logwrite("Fee-Enter", "SchoolId:" + Integer.parseInt(SchoolId));
        request.addProperty("yearid", Integer.parseInt(Year_Id));
        Constants.Logwrite("Fee-Enter", "FeeStatus:" + FeeCardStatus);
        request.addProperty("FeeStatus", FeeCardStatus);
        try {
            SoapObject result = Constants.CallWebMethod(
                    getApplicationContext(), request, Constants.FEES_CARD_DETAIL_V1, true);
            if (result != null && result.getPropertyCount() > 0) {
                SoapObject obj2 = (SoapObject) result.getProperty(0);
                Constants.Logwrite("Feecardobject:", "object:" + obj2.toString());
                if (obj2 != null) {
                    int count = obj2.getPropertyCount();
                    FeeDetails = new String[count];
                    int k = 0;
                    for (int i = 0; i < count; i++) {
                       /* if (i == 0 && obj2.getProperty(0) != null && !obj2.getProperty(0).toString().isEmpty() &&
                                obj2.getProperty(0).toString().contains("Month##@@Fee Name##@@Amt")) {
                            FeeDetails = new String[count - 1];
                        } else {*/
                        FeeDetails[k] = obj2.getProperty(i).toString();
                        Constants.Logwrite("FeeCardDetails:i", "" + FeeDetails[k]);
                        k++;
                        //}
                    }
                }
            }
        } catch (Exception err) {
            Constants.writelog(log, "FeeCardDetails()296 Exception:"
                    + err.getMessage() + ":::" + err.getStackTrace());
        }
        return FeeDetails;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    hideKeyboard(FeeCardActivity.this);
                    FeeCardActivity.this.finish();
                    onBackClickAnimation();
                    break;
                default:
                    break;
            }

        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        try {
            if (mIsFromHome != null && !mIsFromHome.isEmpty()) {
                super.onBackPressed();
                onBackClickAnimation();
            } else {
                Intent intent = new Intent(FeeCardActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                super.onBackPressed();
            }
        } catch (Exception ex) {
            Intent intent = new Intent(FeeCardActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            super.onBackPressed();
        }
    }

    private class GetFeeCard extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
//            FeeCardDetails();
            FeeDetails = FeeCardDetails();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Constants.isShowInternetMsg) {
                    empty_feeCard.setVisibility(View.VISIBLE);

                    Constants.NotifyNoInternet(getApplicationContext());
                } else {
                    if ((FeeDetails != null && FeeDetails.length > 0)) {
                        empty_feeCard.setVisibility(View.GONE);
                        if (FeeDetails.length == 1 && FeeDetails[0].toString().split("##@@")[0].equals("1")) {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    FeeCardActivity.this).create();
                            alertDialog.setTitle("Status");
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage(FeeDetails[0].split("##@@")[1]);
                            alertDialog.setIcon(R.drawable.information);
                            alertDialog.setCancelable(false);
                            alertDialog.setButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
//                                            Intent intent = new Intent(
//                                                    getApplicationContext(),
//                                                    DashboardActivity.class);
//                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                            alertDialog.show();
                            return;
                        }
                        if (FeeDetails != null && FeeDetails.length > 0) {
//                            adp = new FeesItemsListAdapter(
//                                    getApplicationContext(), FeeDetails,
//                                    FeeCardStatus);
                            adapter = new FeeCardListAdapter(FeeCardActivity.this, FeeDetails, FeeCardStatus);

//                            if (FeeCardStatus == 1) {
//                                lstfeedetails.setBackgroundColor(Color.parseColor("#6B7CFF"));
//                            } else if (FeeCardStatus == 2) {
//                                lstfeedetails.setBackgroundColor(Color.parseColor("#73F899"));
//                            } else {
//                                lstfeedetails.setBackgroundColor(Color.parseColor("#FFA898"));
//                            }
//                            lstfeedetails.setAdapter(adp);
                            mRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            mMaincardfee.setVisibility(View.VISIBLE);
                            empty_feeCard.setVisibility(View.GONE);
                        } else {
                            mMaincardfee.setVisibility(View.GONE);
                            empty_feeCard.setVisibility(View.VISIBLE);
                            mProgressBar.setVisibility(View.GONE);
                          /*  AlertDialog alertDialog = new AlertDialog.Builder(
                                    FeeCardActivity_Fee_Card.this).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("Status");
                            alertDialog.setCancelable(false);

                            // Setting Dialog Message
                            if (FeeCardStatus == 2) {
                                alertDialog.setMessage("No Paid Fees Available...");
                            } else if (FeeCardStatus == 1) {
                                alertDialog.setMessage("No Fee Card Available...");
                            } else {
                                alertDialog.setMessage("No Pending Fees Available...");
                            }

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.information);
                            alertDialog.setCancelable(false);
                            // Setting OK Button
                            alertDialog.setButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
//                                            Intent intent = new Intent(
//                                                    getApplicationContext(),
//                                                    DashboardActivity.class);
//                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                            // Showing Alert Message
                            alertDialog.show();*/
                            return;
                        }
                    } else {
                        mMaincardfee.setVisibility(View.GONE);
                        empty_feeCard.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                       /* AlertDialog alertDialog = new AlertDialog.Builder(FeeCardActivity_Fee_Card.this).create();
                        alertDialog.setTitle("Status");
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                        alertDialog.setIcon(R.drawable.information);
                        // Setting OK Button
                        alertDialog.setButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                        onBackClickAnimation();
                                    }
                                });

                        // Showing Alert Message
                        alertDialog.show();*/
                        return;
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                mMaincardfee.setVisibility(View.GONE);
                Constants.writelog(log, "onPostExecute()245 Exception:"
                        + err.getMessage() + ":::" + err.getStackTrace());
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

}
