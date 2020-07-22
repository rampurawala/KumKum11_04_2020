package com.expedite.apps.kumkum.activity;

import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.adapter.AboutListAdapter;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.google.android.gms.ads.AdView;
import com.paynimo.android.payment.PaymentActivity;
import com.paynimo.android.payment.model.Checkout;
import org.ksoap2.serialization.SoapObject;
import java.util.HashMap;

public class AboutActivity extends BaseActivity {
    private ListView lst;
    private AboutListAdapter adapter;
    private String[] lableval = {}, infoval = {};
    private HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
    private String  SchoolId, UserId,  PhoneNumber, TAG = "About";
    int doubleBackToExitPressedOnce = 0;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.AboutActivity);
            SchoolId = Datastorage.GetSchoolId(AboutActivity.this);
            UserId = Datastorage.GetUserId(AboutActivity.this);
            PhoneNumber = Datastorage.GetPhoneNumber(AboutActivity.this);
            ImageView img = (ImageView) findViewById(R.id.imgBlank);
            db = new DatabaseHandler(this);
            Constants.setActionbar(getSupportActionBar(), AboutActivity.this, AboutActivity.this,
                    "AboutUs", "AboutUs",ActivityNames.AboutActivity);
            lst = (ListView) findViewById(R.id.lstabout);
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                lableval = new String[4];
                infoval = new String[4];
                lableval[0] = " Name";
                lableval[1] = " Version";
                lableval[2] = " Developed By";
                lableval[3] = " Registration Id";
                infoval[0] = SchoolDetails.SchoolName + " ";
                infoval[1] = pInfo.versionName.toString() + " ";
                infoval[2] = SchoolDetails.SchoolAddress + " ";
                infoval[3] = deviceId + " ";
                adapter = new AboutListAdapter(AboutActivity.this, lableval, infoval);
                lst.setAdapter(adapter);
                /*if (BuildConfig.DEBUG) {
                    lst.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                        public boolean onItemLongClick(AdapterView<?> arg0,
                                                       View arg1, int arg2, long arg3) {
                            Toast.makeText(AboutActivity.this, "GCM Registration Id." + Datastorage.GetGcmRegId(AboutActivity.this), Toast.LENGTH_LONG).show();
                            return false;
                        }
                    });
                }*/
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*try {
                            Checkout checkout = new Checkout();
                            checkout.setMerchantIdentifier("T143246");
                            checkout.setTransactionIdentifier("215654321");
                            checkout.setTransactionReference("ORD0001");
                            checkout.setTransactionType(PaymentActivity.TRANSACTION_TYPE_SALE);
                            checkout.setTransactionSubType(PaymentActivity.TRANSACTION_SUBTYPE_DEBIT);
                            checkout.setTransactionCurrency("INR");
                            checkout.setTransactionAmount("250.00");
                            checkout.setTransactionDateTime("21-03-2018");
                            checkout.setConsumerEmailID("test@gmail.com");
                            checkout.setConsumerMobileNumber("9510170224");
                            checkout.setConsumerAccountNo("01890100009056");
                            checkout.addCartItem("test", "100.00", "0.0", "0.0", "", "", "", "");
                            checkout.addCartItem("test1", "150.00", "0.0", "0.0", "", "", "", "");
                            //checkout.addCartItem("test~test1", "100.00~150.00", "0.0", "0.0", "", "", "", "");
                            //checkout.addCartItem("test_10.0_0.0~test1_15.0_0.0");
                            Intent authIntent = new Intent(AboutActivity.this, PaymentModesActivity.class);
                            Log.d("Checkout Request Object",
                                    checkout.getMerchantRequestPayload().toString());
                            authIntent.putExtra(PaymentActivity.ARGUMENT_DATA_CHECKOUT, checkout);
                            authIntent.putExtra(PaymentActivity.EXTRA_PUBLIC_KEY, "1234-6666-6789-56");
                            authIntent.putExtra(PaymentActivity.EXTRA_REQUESTED_PAYMENT_MODE, PaymentActivity.PAYMENT_METHOD_DEFAULT);
                            startActivityForResult(authIntent, PaymentActivity.REQUEST_CODE);
                        } catch (Exception ex) {
                            Constants.writelog(TAG, "Method Name:Oncretae 131:" + ex.getMessage());
                        }*/
                        try {
                            if (doubleBackToExitPressedOnce == 4) {
                                Intent intent = new Intent(AboutActivity.this, AddAccountoldActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            doubleBackToExitPressedOnce++;
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    doubleBackToExitPressedOnce = 0;
                                }
                            }, 3000);
                        } catch (Exception ex) {
                            Constants.writelog("AboutActivity", "Method Name:Oncretae 102:" + ex.getMessage());
                        }
                    }
                });

                lst.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            clipboard.setText(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                            Toast.makeText(AboutActivity.this, "Registration Id Copyed.", Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            Constants.writelog("AboutActivity", "Method Name:Oncretae 102:" + ex.getMessage());
                        }
                        return false;
                    }
                });

                new MyTask().execute();
            } catch (Exception err) {
                Constants.writelog("AboutActivity", "setOnLongClickListener() 102:" + err.getMessage());
            }
        } catch (
                Exception err)

        {
            Constants.writelog("AboutActivity", "Method Name:Oncretae 131:" + err.getMessage());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PaymentActivity.REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == PaymentActivity.RESULT_OK) {
                Log.d(TAG, "Result Code :" + RESULT_OK);
                if (data != null) {

                    try {
                        Checkout checkout_res = (Checkout) data
                                .getSerializableExtra(PaymentActivity
                                        .ARGUMENT_DATA_CHECKOUT);
                        Log.d("Checkout Response Obj", checkout_res
                                .getMerchantResponsePayload().toString());

                        String transactionType = checkout_res.
                                getMerchantRequestPayload().getTransaction().getType();
                        String transactionSubType = checkout_res.
                                getMerchantRequestPayload().getTransaction().getSubType();
                        if (transactionType != null && transactionType.equalsIgnoreCase(PaymentActivity.TRANSACTION_TYPE_PREAUTH)
                                && transactionSubType != null && transactionSubType
                                .equalsIgnoreCase(PaymentActivity.TRANSACTION_SUBTYPE_RESERVE)) {
                            // Transaction Completed and Got SUCCESS
                            if (checkout_res.getMerchantResponsePayload()
                                    .getPaymentMethod().getPaymentTransaction()
                                    .getStatusCode().equalsIgnoreCase(PaymentActivity.TRANSACTION_STATUS_PREAUTH_RESERVE_SUCCESS)) {
                                Toast.makeText(getApplicationContext(), "Transaction Status - Success", Toast.LENGTH_SHORT).show();
                                Log.v("TRANSACTION STATUS=>", "SUCCESS");
                                /*
                                 * TRANSACTION STATUS - SUCCESS (status code
                                 * 0200 means success), NOW MERCHANT CAN PERFORM
                                 * ANY OPERATION OVER SUCCESS RESULT
                                 */
                                if (checkout_res.getMerchantResponsePayload()
                                        .getPaymentMethod().getPaymentTransaction().getInstruction().getStatusCode().equalsIgnoreCase("")) {
                                    /**
                                     * SI TRANSACTION STATUS - SUCCESS (status
                                     * code 0200 means success)
                                     */
                                    Log.v("TRANSACTION SI STATUS=>",
                                            "SI Transaction Not Initiated");
                                }

                            } // Transaction Completed and Got FAILURE

                            else {
                                // some error from bank side
                                Log.v("TRANSACTION STATUS=>", "FAILURE");
                                Toast.makeText(getApplicationContext(),
                                        "Transaction Status - Failure",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Transaction Completed and Got SUCCESS
                            if (checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getStatusCode().equalsIgnoreCase(
                                    PaymentActivity.TRANSACTION_STATUS_SALES_DEBIT_SUCCESS)) {
                                Toast.makeText(getApplicationContext(), "Transaction Status - Success", Toast.LENGTH_SHORT).show();
                                Log.v("TRANSACTION STATUS=>", "SUCCESS");

                                /**
                                 * TRANSACTION STATUS - SUCCESS (status code
                                 * 0300 means success), NOW MERCHANT CAN PERFORM
                                 * ANY OPERATION OVER SUCCESS RESULT
                                 */

                                if (checkout_res.getMerchantResponsePayload().
                                        getPaymentMethod().getPaymentTransaction().
                                        getInstruction().getStatusCode()
                                        .equalsIgnoreCase("")) {
                                    /**
                                     * SI TRANSACTION STATUS - SUCCESS (status
                                     * code 0300 means success)
                                     */
                                    Log.v("TRANSACTION SI STATUS=>",
                                            "SI Transaction Not Initiated");
                                } else if (checkout_res.getMerchantResponsePayload()
                                        .getPaymentMethod().getPaymentTransaction()
                                        .getInstruction()
                                        .getStatusCode().equalsIgnoreCase(
                                                PaymentActivity.TRANSACTION_STATUS_SALES_DEBIT_SUCCESS)) {

                                    /**
                                     * SI TRANSACTION STATUS - SUCCESS (status
                                     * code 0300 means success)
                                     */
                                    Log.v("TRANSACTION SI STATUS=>", "SUCCESS");
                                } else {
                                    /**
                                     * SI TRANSACTION STATUS - Failure (status
                                     * code OTHER THAN 0300 means failure)
                                     */
                                    Log.v("TRANSACTION SI STATUS=>", "FAILURE");
                                }
                            } // Transaction Completed and Got FAILURE
                            else {
                                // some error from bank side
                                Log.v("TRANSACTION STATUS=>", "FAILURE");
                                Toast.makeText(getApplicationContext(),
                                        "Transaction Status - Failure",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                        String result = "StatusCode : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getStatusCode()
                                + "\nStatusMessage : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getStatusMessage()
                                + "\nErrorMessage : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getErrorMessage()
                                + "\nAmount : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getAmount()
                                + "\nDateTime : " + checkout_res.
                                getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getDateTime()
                                + "\nMerchantTransactionIdentifier : "
                                + checkout_res.getMerchantResponsePayload()
                                .getMerchantTransactionIdentifier()
                                + "\nIdentifier : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getIdentifier()
                                + "\nBankSelectionCode : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getBankSelectionCode()
                                + "\nBankReferenceIdentifier : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getBankReferenceIdentifier()
                                + "\nRefundIdentifier : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getRefundIdentifier()
                                + "\nBalanceAmount : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getBalanceAmount()
                                + "\nInstrumentAliasName : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getInstrumentAliasName()
                                + "\nSI Mandate Id : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getInstruction().getId()
                                + "\nSI Mandate Status : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getInstruction().getStatusCode()
                                + "\nSI Mandate Error Code : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getInstruction().getErrorcode();

                        Log.e("About", "msg:" + result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == PaymentActivity.RESULT_ERROR) {
                Log.d(TAG, "got an error");

                if (data.hasExtra(PaymentActivity.RETURN_ERROR_CODE) &&
                        data.hasExtra(PaymentActivity.RETURN_ERROR_DESCRIPTION)) {
                    String error_code = (String) data
                            .getStringExtra(PaymentActivity.RETURN_ERROR_CODE);
                    String error_desc = (String) data
                            .getStringExtra(PaymentActivity.RETURN_ERROR_DESCRIPTION);

                    Toast.makeText(getApplicationContext(), " Got error :"
                            + error_code + "--- " + error_desc, Toast.LENGTH_SHORT)
                            .show();
                    Log.d(TAG + " Code=>", error_code);
                    Log.d(TAG + " Desc=>", error_desc);
                }
            } else if (resultCode == PaymentActivity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Transaction Aborted by User",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "User pressed back button");
            }
        }
    }


    private void LogUserVisted() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.LOG_USER_VISITED);
        request.addProperty("moduleid", 7);
        request.addProperty("schoolid", SchoolId);
        request.addProperty("User_Id", UserId);
        request.addProperty("phoneno", PhoneNumber);
        request.addProperty("pageid", 17);
        try {
            Constants.CallWebMethod(AboutActivity.this, request, Constants.LOG_USER_VISITED, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                LogUserVisted();
            } catch (Exception err) {
                Constants.Logwrite("Error", err.getMessage());
                Constants.writelog("AboutActivity", "Exception 156:" + err.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            menu.add(0, 1, 1, "Add Account").setTitle("Add Account");
            //   menu.add(0, 2, 2, "Remove Account").setTitle("Remove Account");
            menu.add(0, 3, 3, "Set Default Account").setTitle("Set Default Account");
            mapacc = Constants.AddAccount(menu, db);
        } catch (Exception err) {
            Constants.Logwrite("AboutActivity", "AboutActivity:" + err.getMessage());
            return true;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int iid = item.getItemId();
        if (iid == android.R.id.home) {
            hideKeyboard(AboutActivity.this);
            AboutActivity.this.finish();
            onBackClickAnimation();
        } else if (iid == 1 || iid == 2 || iid == 3) {
            if (iid == 1) {
                addAccountClick(AboutActivity.this);
            }/* else if (iid == 2) {
                removeAccountClick(AboutActivity.this);
            }*/ else {
                accountListClick(AboutActivity.this);
            }
        } else {
            String details = mapacc.get(iid).toString();
            Constants.SetAccountDetails(details, AboutActivity.this);
            intent = new Intent(AboutActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
            onBackClickAnimation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AboutActivity.this.finish();
        onBackClickAnimation();
    }
}
