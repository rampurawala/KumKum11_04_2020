package com.expedite.apps.kumkum.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.AppService;

import org.ksoap2.serialization.SoapObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jaydeep on 13/12/2016.
 */
public class PaymentResponceActivity extends BaseActivity {
    private TextView transctionstatus;
    private String studId, schoolId, code = "2", status, isfullpay = "1";
    private ImageView Image;
    private TextView feedback, Home, history;
    /*private LinearLayout imglayout;
    private RelativeLayout mainlayout;*/
    private String message, transactionId = "", isEasyPay = "", key, values, tag = "PaymentResponceActivity";
    private String[] data;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_responce);
        init();
    }

    public void init() {
        Constants.setActionbar(getSupportActionBar(), PaymentResponceActivity.this, PaymentResponceActivity.this,
                "Payment Status", "Payment Status", ActivityNames.PaymentResponceActivity);
        mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        studId = Datastorage.GetStudentId(PaymentResponceActivity.this);
        schoolId = Datastorage.GetSchoolId(PaymentResponceActivity.this);
        transctionstatus = (TextView) findViewById(R.id.transctionstatus);
        Image = (ImageView) findViewById(R.id.image);
        feedback = (TextView) findViewById(R.id.feedback);
        Home = (TextView) findViewById(R.id.home);
        history = (TextView) findViewById(R.id.history);
        /*imglayout = (LinearLayout) findViewById(R.id.imagelayout);
        Year_id = Datastorage.GetCurrentYearId(PaymentResponceActivity.this);
        Amounttxt = (TextView) findViewById(R.id.amounttxt);
        errortxt = (TextView) findViewById(R.id.errortxt);
        mainlayout = (RelativeLayout) findViewById(R.id.activity_payment_responce);*/
        message = getIntent().getStringExtra("message");
        isEasyPay = getIntent().getStringExtra("isEasypay");
        key = getIntent().getStringExtra("key");
        values = getIntent().getStringExtra("values");
        transactionId = getIntent().getStringExtra("transactionid");
        transctionstatus.setText("");
        Image.setImageResource(0);
        try {
            final DatabaseHandler db = new DatabaseHandler(PaymentResponceActivity.this);
            isfullpay = db.getIsfullPay(Datastorage.GetStudentId(PaymentResponceActivity.this),
                    Datastorage.GetSchoolId(PaymentResponceActivity.this));
            feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(PaymentResponceActivity.this, ReportBugActivity.class);
                    i.addCategory(Intent.CATEGORY_HOME);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.putExtra("PaymentResponse", "1");
                    startActivity(i);
                    finish();
                    onClickAnimation();
                }
            });

            Home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(PaymentResponceActivity.this, FeesPayActivity.class);
                    if (isfullpay.equalsIgnoreCase("0")) {
                        i = new Intent(PaymentResponceActivity.this, CustomFeesActivity.class);
                    }
                    i.addCategory(Intent.CATEGORY_HOME);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.putExtra("FeeStatus", "3");
                    startActivity(i);
                    finish();
                    onBackClickAnimation();
                }
            });
            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(PaymentResponceActivity.this, TransactionHistoryActivity.class);
                    i.addCategory(Intent.CATEGORY_HOME);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    finish();
                    onClickAnimation();
                }
            });

            if (!isOnline()) {
                Image.setImageResource(R.drawable.sade_smile_icon);
                transctionstatus.setText("Your internet connection may not be working. Please check and try again.");
                return;
            }
            if (isEasyPay != null && isEasyPay.equals("1")) {
                getCheckTransactionStatus();
            } else {
                new UpdateTransactionDetail().execute();
            }
        } catch (Exception ex) {
            Constants.writelog("PaymentResponceActivity", "Ex 95:" + ex.getMessage());
        }
    }

    private void getCheckTransactionStatus() {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<AppService> call = ((MyApplication)
                getApplicationContext()).getmRetrofitInterfaceAppService().getCheckTransactionStatus(studId, schoolId,
                transactionId, SchoolDetails.appname + "", Constants.PLATFORM);
        call.enqueue(new Callback<AppService>() {
            @Override
            public void onResponse(Call<AppService> call, Response<AppService> response) {
                try {
                    AppService tmps = response.body();
                    if (tmps != null)
                        if (tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1")) {
                            data = tmps.getStrResult().split("##@@");
                            if (data != null && data.length > 0 && data[0].equalsIgnoreCase("True")) {
                                transctionstatus.setText("");
                                Image.setImageResource(R.drawable.smile_icon);
                                transctionstatus.setText(data[2]);
                                history.setVisibility(View.VISIBLE);
                            } else {
                                transctionstatus.setTextColor(getResources().getColor(R.color.Red));
                                transctionstatus.setText("");
                                Image.setImageResource(R.drawable.sade_smile_icon);
                                feedback.setVisibility(View.VISIBLE);
                                transctionstatus.setText(data[2]);
                            }
                        } else {
                            transctionstatus.setTextColor(getResources().getColor(R.color.Red));
                            transctionstatus.setText("");
                            Image.setImageResource(R.drawable.sade_smile_icon);
                            feedback.setVisibility(View.VISIBLE);
                        }
                    mProgressBar.setVisibility(View.GONE);
                } catch (Exception ex) {
                    mProgressBar.setVisibility(View.GONE);
                    Constants.writelog("StudentResultActvity", "Exception 332:" + ex.getMessage() +
                            "::::" + ex.getStackTrace());
                }
            }

            @Override
            public void onFailure(Call<AppService> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Constants.writelog("StudentResultActvity", "Exception 340:" + t.getMessage());
                transctionstatus.setTextColor(getResources().getColor(R.color.Red));
                transctionstatus.setText("");
                Image.setImageResource(R.drawable.sade_smile_icon);
                feedback.setVisibility(View.VISIBLE);
                transctionstatus.setText(data[2]);
            }
        });
    }


    public class UpdateTransactionDetail extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String responseString = null;
            try {
                SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.UPDATE_PAYMENT_TRANSACTION);
                request.addProperty("Stud_Id", studId);
                request.addProperty("School_Id", schoolId);
                request.addProperty("transactionId", transactionId);
                request.addProperty("key", key);
                request.addProperty("keyValue", values);
                request.addProperty("message", message);
                request.addProperty("appVersionCode", code);

                SoapObject result = Constants.CallWebMethod(
                        PaymentResponceActivity.this, request, Constants.UPDATE_PAYMENT_TRANSACTION, true);
                if (result != null && result.getPropertyCount() > 0) {
                    status = result.getProperty(0).toString();
                    data = status.split("##@@");
                }
            } catch (Exception e) {
                responseString = e.getMessage();
                e.printStackTrace();
                Constants.writelog(tag, "UpdateTransactionDetail:doInBackground() 159 Error: " + e.getMessage());
            }
            // TODO: register the new account here.
            return responseString;
        }

        @Override
        protected void onPostExecute(final String success) {
            try {
                Datastorage.SetTransactionDetail(getApplication(), "");
                if (success == null) {
                    if (data != null && data.length > 0 && data[0].equalsIgnoreCase("True")) {
                        transctionstatus.setText("");
                        Image.setImageResource(R.drawable.smile_icon);
                        transctionstatus.setText(data[2]);
                        history.setVisibility(View.VISIBLE);
                    } else {
                        transctionstatus.setTextColor(getResources().getColor(R.color.Red));
                        transctionstatus.setText("");
                        Image.setImageResource(R.drawable.sade_smile_icon);
                        feedback.setVisibility(View.VISIBLE);
                        transctionstatus.setText(data[2]);
                    }
                    //Toast.makeText(PaymentResponceActivity.this, status, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                Constants.writelog(tag, "UpdateTransactionDetail:onPostExecute() 315 : Error: " + ex.getMessage());
            } finally {
                mProgressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onCancelled() {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            hideKeyboard(PaymentResponceActivity.this);
            PaymentResponceActivity.this.finish();
            onBackClickAnimation();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(PaymentResponceActivity.this, FeesPayActivity.class);
        if (isfullpay.equalsIgnoreCase("0")) {
            i = new Intent(PaymentResponceActivity.this, CustomFeesActivity.class);
        }
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra("FeeStatus", "3");
        startActivity(i);
        finish();
        onBackClickAnimation();
    }

}
