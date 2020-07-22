package com.expedite.apps.kumkum.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.adapter.FeesPayItemsListAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.ConnectionDetector;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.model.AppService;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.api.GoogleApiClient;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeesPayActivity extends BaseActivity {
    LinearLayout empty_folder_lyt_file_detail, llMain;
    private Menu menu;
    private String[] FeeDetails = null;
    private FeesPayItemsListAdapter adp;
    private ListView lstfeedetails;
    private TextView tv, tvPaymentNote;
    private TextView btnPay;
    private int minAmount = 50, maxAmount = 50000, FeeCardStatus = 1, totalAmount = 0;
    private String SchoolId, StudentId, LastUpdatedTime, Year_Id, log = "FeeCardActivity_Fee_Card", extraDetails = "";
    private GoogleApiClient client;
    private ConnectionDetector cd;
    private String mIsFromHome = "";
    private ProgressBar mProgressBar;
    private String tag = "FeesPayActivity";
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees_pay);
        if (getIntent() != null && getIntent().getExtras() != null)
            mIsFromHome = getIntent().getExtras().getString("IsFromHome", "");
        init();
    }

    public void init() {

        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.FeesPayActivity);
            Constants.setActionbar(getSupportActionBar(), FeesPayActivity.this, FeesPayActivity.this,
                    "Pay Fees Online", "PayFeesOnline",ActivityNames.FeesPayActivity);
            mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
            cd = new ConnectionDetector(FeesPayActivity.this);
            SchoolId = Datastorage.GetSchoolId(FeesPayActivity.this);
            StudentId = Datastorage.GetStudentId(FeesPayActivity.this);
            LastUpdatedTime = Datastorage.GetLastUpdatedtime(FeesPayActivity.this);
            llMain = findViewById(R.id.llMain);
            Year_Id = Datastorage.GetCurrentYearId(FeesPayActivity.this);
            btnPay = (TextView) findViewById(R.id.btnPay);
            empty_folder_lyt_file_detail = findViewById(R.id.empty_folder_lyt_file_detail);
            tvPaymentNote = (TextView) findViewById(R.id.tvPaymentNote);
            tv = (TextView) findViewById(R.id.tvmarkqueetextfeecardlist);
            tv.setText(LastUpdatedTime);
            FeeCardStatus = 3;
            int Cnt_Count = 0;
            Cnt_Count = Datastorage.GetMultipleAccount(FeesPayActivity.this);
            if (Cnt_Count == 1) {
                setTitle("Pay Fee"
                        + "-"
                        + Datastorage
                        .GetStudentName(FeesPayActivity.this));
            } else {
                setTitle("Pay Fee");
            }
            lstfeedetails = (ListView) findViewById(R.id.lstpayfeecardlist);
            lstfeedetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ProgressDialog dialog = new ProgressDialog(FeesPayActivity.this);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setMessage("Please Wait...");
                    dialog.show();
                    try {
                        if (position == 0) {
                            return;
                        }
                        String[] oldvalue = FeeDetails[position].split("##@@");
                        if (oldvalue[3].equals("1")) {
                            oldvalue[3] = "0";
                            String newValue = "";
                            for (int j = 0; j < oldvalue.length; j++) {
                                if (j < (oldvalue.length - 1)) {
                                    newValue += oldvalue[j] + "##@@";
                                } else {
                                    newValue += oldvalue[j];
                                }
                            }
                            FeeDetails[position] = newValue;
                        } else {
                            oldvalue[3] = "1";
                            String newValue = "";
                            for (int j = 0; j < oldvalue.length; j++) {
                                if (j < (oldvalue.length - 1)) {
                                    newValue += oldvalue[j] + "##@@";
                                } else {
                                    newValue += oldvalue[j];
                                }
                            }
                            FeeDetails[position] = newValue;
                        }

                        for (int k = 1; k < FeeDetails.length; k++) {
                            if (k == position) {
                                continue;
                            }
                            String[] newval = FeeDetails[k].split("##@@");
                            if (k < position) {
                                newval[3] = "1";
                            } else {
                                newval[3] = "0";
                            }
                            String newvalstr = "";
                            for (int j = 0; j < newval.length; j++) {
                                if (j < (newval.length - 1)) {
                                    newvalstr += newval[j] + "##@@";
                                } else {
                                    newvalstr += newval[j];
                                }
                            }
                            FeeDetails[k] = newvalstr;
                        }
                        totalAmount = 0;
                        extraDetails = "";
                        List<String> addedFines = new ArrayList<String>();
                        for (int i = 1; i < FeeDetails.length; i++) {
                            String[] parts = FeeDetails[i].split("##@@");
                            parts[2] = parts[2].replace(",", "");
                            if (parts[3].equals("1")) {
                                String[] txtamt;
                                if (parts[2].contains("/")) {
                                    txtamt = parts[2].split("/");
                                    int Diff = Integer.parseInt(txtamt[1]) - Integer.parseInt(txtamt[0]);
                                    totalAmount += Diff;
                                    //totalAmount += Integer.parseInt(txtamt[0]);
                                } else {
                                    totalAmount += Integer.parseInt(parts[2]);
                                }
                                //Add Extra details
                                if (extraDetails.equalsIgnoreCase("")) {
                                    extraDetails += parts[5];
                                } else {
                                    extraDetails += "%%@@%%" + parts[5];
                                }
                                if (parts.length > 6 && parts[6] != null && !parts[6].trim().equalsIgnoreCase("")) {
                                    String[] checkfine = parts[6].split("#");
                                    parts[6] = checkfine[0];
                                    boolean isadded = false;
                                    String[] fines = parts[6].split(",");
                                    int totalfine = 0;
                                    for (int k = 0; k < fines.length; k++) {
                                        String[] values = fines[k].split("::");
                                        if (addedFines != null) {
                                            boolean isExist = addedFines.contains(values[0] + values[1]);
                                            if (isExist) {
                                                isadded = true;
                                            }
                                        }
                                        if (!isadded) {
                                            addedFines.add(values[0] + values[1]);
                                            totalfine += Integer.parseInt(values[2]);
                                        }
                                    }
                                    FeeDetails[i] += "#" + totalfine;
                                    totalAmount += totalfine;
                                }
                            } else {
                                if (parts.length > 6) {
                                    String[] fineDetails = parts[6].split("#");
                                    parts[6] = fineDetails[0];
                                    String newval = "";
                                    for (int e = 0; e < parts.length; e++) {
                                        if (e < (parts.length - 1)) {
                                            newval += parts[e] + "##@@";
                                        } else {
                                            newval += parts[e];
                                        }
                                    }
                                    FeeDetails[i] = newval;
                                }
                            }
                        }
                        adp.notifyDataSetChanged();
                        tvPaymentNote.setText("Total Amout to pay :" + totalAmount);

                    } catch (Exception ex) {
                        Constants.writelog("FeesPayActivity", "LstClick() 202:" + ex.getMessage());
                    } finally {
                        dialog.dismiss();
                    }
                }
            });
            new MyTask().execute();
            btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cd.isConnectingToInternet()) {
                        if (totalAmount > maxAmount) {
                            Toast.makeText(FeesPayActivity.this, "Please Select Fees Amount Less then " + maxAmount + ".", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (totalAmount < minAmount) {
                            Toast.makeText(FeesPayActivity.this, "Please Select Fees Amount More then " + minAmount + ".", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (extraDetails == null || extraDetails.trim().equals("")) {
                            Toast.makeText(FeesPayActivity.this, "Please Select Any One Fees.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Intent intent = new Intent(FeesPayActivity.this, CardPaymentDetailActivity.class);
                        intent.putExtra("Amount", totalAmount);
                        intent.putExtra("ExtraDetails", extraDetails);
                        Constants.writelog("FeesPayActivity", "BtnPayClick()227 Amount:" + totalAmount + " Deta:" + extraDetails);
                        startActivity(intent);
                        /* FeesPayActivity.this.finish();*/
                        onClickAnimation();
                    } else {
                        Toast.makeText(FeesPayActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_LONG).show();
                    }
                }
            });

        } catch (Exception err) {
            Common.printStackTrace(err);
            Constants.Logwrite("Fee_Oncreate", "" + err.getMessage() + ":StackTrace:" + err.getStackTrace());
            Constants.writelog(log, "OnCreate()120 Exception:" + err.getMessage() + ":::" + err.getStackTrace());
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    public void SetMinMax() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_MIN_MAX_AMOUNT_FOR_ONLINE_PAYMENT);
        try {
            SoapObject result = Constants.CallWebMethod(
                    FeesPayActivity.this, request, Constants.GET_MIN_MAX_AMOUNT_FOR_ONLINE_PAYMENT, true);
            if (result != null && result.getPropertyCount() > 0) {
                String res = result.getProperty(0).toString();
                String[] parts = res.split("##@@");
                minAmount = Integer.parseInt(parts[0]);
                maxAmount = Integer.parseInt(parts[1]);
            }
        } catch (Exception err) {
            Constants.Logwrite("FeesPayActivity", "MSG 474:" + err.getMessage() + ":StackTrace:"
                    + err.getStackTrace());
            Constants.writelog(log, "SetMinMax()476 Exception:"
                    + err.getMessage() + ":::" + err.getStackTrace());
        }
    }

    public String[] FeeCardDetails() {
        Constants.Logwrite("Fee-Enter 461", "FeecardDetails");
        // String[] years = null;
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.FEES_CARD_DETAIL_PAY_ONLINE_FEE);
        request.addProperty("studid", Integer.parseInt(StudentId));
        Constants.Logwrite("Fee-Enter", "StudId:" + Integer.parseInt(StudentId));
        request.addProperty("schoolid", Integer.parseInt(SchoolId));
        Constants.Logwrite("Fee-Enter", "SchoolId:" + Integer.parseInt(SchoolId));
        request.addProperty("yearid", Integer.parseInt(Year_Id));
        Constants.Logwrite("Fee-Enter 12", "FeeStatus:" + FeeCardStatus);
        request.addProperty("FeeStatus", FeeCardStatus);
        try {
            SoapObject result = Constants.CallWebMethod(
                    FeesPayActivity.this, request, Constants.FEES_CARD_DETAIL_PAY_ONLINE_FEE, true);
            Constants.Logwrite("FeecardResult:", "Result:" + result.toString());
            if (result != null && result.getPropertyCount() > 0) {
                SoapObject obj2 = (SoapObject) result.getProperty(0);
                Constants.Logwrite("Feecardobject:", "object:" + obj2.toString());
                if (obj2 != null) {
                    int count = obj2.getPropertyCount();
                    FeeDetails = new String[count];
                    for (int i = 0; i < count; i++) {
                        FeeDetails[i] = obj2.getProperty(i).toString();
                        Constants.Logwrite("FeeCardDetails:i", "" + FeeDetails[i].toString());
                    }
                }
            }
        } catch (Exception err) {
            Constants.Logwrite("Fee_FeeCardDetails", "" + err.getMessage() + ":StackTrace:"
                    + err.getStackTrace());
            Constants.writelog(log, "FeeCardDetails()296 Exception:"
                    + err.getMessage() + ":::" + err.getStackTrace());
        }
        return FeeDetails;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            MenuInflater objmenuinfl = getMenuInflater();
            objmenuinfl.inflate(R.menu.activity_payfees_options, menu);
            this.menu = menu;
        } catch (Exception err) {
            Constants.writelog("FeesPayActivity", "onCreateOptionsMenu()535 Error::" + err.getMessage());
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if (item.getItemId() == android.R.id.home) {
            hideKeyboard(FeesPayActivity.this);
            FeesPayActivity.this.finish();
            onBackClickAnimation();
        } else if (item.getItemId() == R.id.photo_view_type) {
            Constants.googleAnalyticEvent(FeesPayActivity.this, Constants.button_click, "Transaction History");
            intent = new Intent(FeesPayActivity.this, TransactionHistoryActivity.class);
            startActivity(intent);
            //FeesPayActivity.this.finish();
            onClickAnimation();
        } else if (item.getItemId() == R.id.tnc) {
            getRefundPolicy();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mIsFromHome != null && !mIsFromHome.isEmpty()) {
            super.onBackPressed();
            onBackClickAnimation();
        } else {
            Intent intent = new Intent(FeesPayActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void getRefundPolicy() {
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            try {
                //http://app.vayuna.com/service.asmx/folderFileList?schoolId=8414&class_id=23&folderId=0
                Call<AppService> call = ((MyApplication) getApplicationContext())
                        .getmRetrofitInterfaceAppService().GetRefundPolicy(StudentId, SchoolId, Constants.PLATFORM);
                call.enqueue(new Callback<AppService>() {
                    @Override
                    public void onResponse(Call<AppService> call, Response<AppService> response) {
                        try {

                            AppService tmp = response.body();
                            if (tmp != null && tmp.getResponse() != null && tmp.getResponse().equals("1")) {
                                empty_folder_lyt_file_detail.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                showTermsConditionsDialog("", tmp.getStrResult());
                            }
                            mProgressBar.setVisibility(View.GONE);
                        } catch (Exception ex) {
                            showTermsConditionsDialog("", "");
                            Constants.writelog(tag, "Exception_113:" + ex.getMessage()
                                    + "::::::" + ex.getStackTrace());
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<AppService> call, Throwable t) {
                        showTermsConditionsDialog("", "");
                        Constants.writelog(tag, "Exception_113:" + t.getMessage());
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            } catch (Exception ex) {
                showTermsConditionsDialog("", "");
                mProgressBar.setVisibility(View.GONE);
                Constants.writelog(tag, "Exception_113:" + ex.getMessage()
                        + "::::::" + ex.getStackTrace());
            }
        } else {
            empty_folder_lyt_file_detail.setVisibility(View.VISIBLE);
            showTermsConditionsDialog("", "");
            llMain.setVisibility(View.GONE);
            Common.showToast(FeesPayActivity.this, getString(R.string.msg_connection));
        }
    }

    public void showTermsConditionsDialog(String url, String Desc) {
        try {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.terms_and_conditions_dialog_layout, null);
            dialogBuilder.setView(dialogView);
            final AlertDialog alertDialog = dialogBuilder.create();
            final ProgressBar mDialogProgressBar = (ProgressBar) dialogView.findViewById(R.id.dialogProgressbar);
            WebView detail = (WebView) dialogView.findViewById(R.id.webDetail);
            detail.getSettings().setJavaScriptEnabled(true);
            detail.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, final String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    mDialogProgressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (view.getProgress() == 100) {
                        mDialogProgressBar.setVisibility(View.GONE);
                    }
                }

            });
            if (url != null && !url.isEmpty()) {
                detail.loadUrl(url);
            } else if (Desc != null && !Desc.isEmpty()) {
                detail.loadData(Desc, "text/html", "UTF-8");
            } else {
                detail.loadData(" <center><b>Refund Policy</b></center></br>If Refund is applicable, It will be initiated and Your entire amount will be automatically credited in your Original payment mode within 4-5 working days.", "text/html", "UTF-8");
            }

            ImageView btnClose = (ImageView) dialogView.findViewById(R.id.btnClose);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            FeeCardDetails();
            SetMinMax();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Constants.isShowInternetMsg) {
                    empty_folder_lyt_file_detail.setVisibility(View.VISIBLE);
                    llMain.setVisibility(View.GONE);
                    Constants.NotifyNoInternet(FeesPayActivity.this);
                } else {
                    empty_folder_lyt_file_detail.setVisibility(View.GONE);
                    llMain.setVisibility(View.VISIBLE);
                    if ((FeeDetails != null && FeeDetails.length > 0)) {
                        // Display no pending fees available.
                        if (FeeDetails.length == 1 && FeeDetails[0].toString().split("##@@")[0].equals("1")) {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    FeesPayActivity.this).create();
                            alertDialog.setTitle("Status");
                            alertDialog.setCancelable(false);
                            alertDialog
                                    .setMessage(FeeDetails[0].toString().split("##@@")[1]);
                            alertDialog.setIcon(R.drawable.information);
                            alertDialog.setCancelable(false);
                            alertDialog.setButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
//                                            Intent intent = new Intent(
//                                                    FeesPayActivity.this,
//                                                    DashboardActivity.class);
//                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                            alertDialog.show();
                            return;
                        }
                        List<String> addedFines = new ArrayList<String>();
                        for (int i = 1; i < FeeDetails.length; i++) {
                            String[] parts = FeeDetails[i].split("##@@");
                            parts[2] = parts[2].replace(",", "");
                            if (parts[3].equals("1")) {
                                String[] txtamt;
                                if (parts[2].contains("/")) {
                                    txtamt = parts[2].split("/");
                                    int Diff = Integer.parseInt(txtamt[1]) - Integer.parseInt(txtamt[0]);
                                    totalAmount += Diff;
                                } else {
                                    totalAmount += Integer.parseInt(parts[2]);
                                }

                                /*if(parts.length > 5 && parts[5]!=null) {
                                    if (extraDetails.equalsIgnoreCase("")) {
                                        extraDetails += parts[5];
                                    } else {
                                        extraDetails += "%%@@%%" + parts[5];
                                    }
                                }
*/
                                if (extraDetails.equalsIgnoreCase("")) {
                                    extraDetails += parts[5];
                                } else {
                                    extraDetails += "%%@@%%" + parts[5];
                                }
                                if (parts.length > 6 && parts[6] != null && !parts[6].trim().equalsIgnoreCase("")) {
                                    boolean isadded = false;
                                    String[] fines = parts[6].split(",");
                                    int totalfine = 0;
                                    for (int k = 0; k < fines.length; k++) {
                                        String[] values = fines[k].split("::");
                                        if (addedFines != null) {
                                            boolean isExist = addedFines.contains(values[0] + values[1]);
                                            if (isExist) {
                                                isadded = true;
                                            }
                                        }
                                        if (!isadded) {
                                            addedFines.add(values[0] + values[1]);
                                            totalfine += Integer.parseInt(values[2]);
                                        }
                                    }
                                    FeeDetails[i] += "#" + totalfine;
                                    totalAmount += totalfine;
                                }
                            }
                        }
                        tvPaymentNote.setText("Total Amout to pay :" + totalAmount);
                        if (FeeDetails[0].toString().contains("##@@")) {
                            adp = new FeesPayItemsListAdapter(
                                    FeesPayActivity.this, FeeDetails,
                                    FeeCardStatus);
//                            lstfeedetails.setBackgroundColor(Color
//                                    .parseColor("#FFA898"));
                            lstfeedetails.setAdapter(adp);
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(FeesPayActivity.this).create();
                            alertDialog.setTitle("Status");
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage("No Pending Fees Available...");
                            alertDialog.setIcon(R.drawable.information);
                            alertDialog.setCancelable(false);
                            alertDialog.setButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
//                                            Intent intent = new Intent(
//                                                    FeesPayActivity.this,
//                                                    DashboardActivity.class);
//                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                            alertDialog.show();
                            return;
                        }
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                FeesPayActivity.this).create();
                        alertDialog.setTitle("Status");
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                        alertDialog.setIcon(R.drawable.information);
                        alertDialog.setButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
//                                        Intent intent = new Intent(
//                                                FeesPayActivity.this,
//                                                DashboardActivity.class);
//                                        startActivity(intent);
                                        finish();
                                    }
                                });
                        alertDialog.show();
                        return;
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                Constants.writelog(log, "onPostExecute()245 Exception:"
                        + err.getMessage() + ":::" + err.getStackTrace());
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }
}