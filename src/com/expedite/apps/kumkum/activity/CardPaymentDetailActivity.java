package com.expedite.apps.kumkum.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.atom.mobilepaymentsdk.PayActivity;
import com.atom.mpsdklibrary.PayActivity;
import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.BuildConfig;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;

import org.ksoap2.serialization.SoapObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.paynimo.android.payment.PaymentActivity;
import com.paynimo.android.payment.PaymentModesActivity;
import com.paynimo.android.payment.model.Checkout;

public class CardPaymentDetailActivity extends BaseActivity {
    private RadioGroup cardradioGroup, associatedradioGroup, RGGetway;
    private TextView pay;
    private RadioButton mRbAtom, mRbEasyPay, mRbPaynimo, mRbCc, mRbDc, mRbNb, mVisarb, mMasterrb, mMaestrorb, rupay;//radioButton1, radioButton2;
    private String tag = "CardPaymentDetailActivity";
    public android.widget.Spinner spinner;
    private CardView cardview, cardviewNbid, cardviewNbText, cardviewCardType, cardviewPaymentGetway;
    private ArrayList<Integer> ID;
    private ArrayList<String> Spinner;
    private int Year_id = 0, i = 0, ids = 0;
    private double amount = 0, finalAmount = 0, finalAmountFromServer = 0;
    private String merchantId, loginid, password, clientcode, custacc, referanceNo = "ORD0001", publicKey = "1234-6666-6789-56", reqHashKey, respHashKey;
    private String paymentgateway = "1", date = "", transactionId = "", message = "",
            fullname = "", mobilenumber = "", respKey = "", respVal = "", cardasso = "", channelid = "", url = "", cardType = "",
            bankname = "", productId = "", extradetails = "", radiotxt = "", easyPayurl = "", studId, schoolId, email = "youremail@gmail.com", surCharge = "YES";
    private String[] bankDetails, getwayDetails, paymentgatewayName, cardTypeName;
    private TextView tvnotice1, tvnotice2;
    private ProgressBar mProgressBar;
    private RelativeLayout rlCardType;
    private Common mcommon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment_detail);
        init();
    }

    public void init() {
        try {
            //clear response url
            mcommon = new Common(CardPaymentDetailActivity.this);
            mcommon.setSession(Constants.RESPONSE_URL, "");

            Constants.setActionbar(getSupportActionBar(), CardPaymentDetailActivity.this,
                    CardPaymentDetailActivity.this, "Payment Summary", "PaymentSummary", ActivityNames.CardPaymentDetailActivity);
            mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
            studId = Datastorage.GetStudentId(CardPaymentDetailActivity.this);
            schoolId = Datastorage.GetSchoolId(CardPaymentDetailActivity.this);
            Year_id = Integer.parseInt(Datastorage.GetCurrentYearId(CardPaymentDetailActivity.this));
            cardradioGroup = (RadioGroup) findViewById(R.id.cardtypeRG);
            RGGetway = (RadioGroup) findViewById(R.id.RGGetway);
            associatedradioGroup = (RadioGroup) findViewById(R.id.cardAssociatetypeRG);
            cardview = (CardView) findViewById(R.id.cardassociatetype);
            cardviewCardType = (CardView) findViewById(R.id.cardtype);
            cardviewPaymentGetway = (CardView) findViewById(R.id.CVPaymentGetway);
            cardviewNbid = (CardView) findViewById(R.id.cardnetbanking);
            cardviewNbText = (CardView) findViewById(R.id.cardnetbankingtxt);
            spinner = (android.widget.Spinner) findViewById(R.id.spinnerbnk);
            pay = (TextView) findViewById(R.id.Continuepay);
            rlCardType = (RelativeLayout) findViewById(R.id.rlCardType);
            mRbAtom = (RadioButton) findViewById(R.id.rbAtom);
            mRbEasyPay = (RadioButton) findViewById(R.id.rbEasyPay);
            mRbPaynimo = (RadioButton) findViewById(R.id.rbPaynimo);
            mRbCc = (RadioButton) findViewById(R.id.cc);
            mRbDc = (RadioButton) findViewById(R.id.dc);
            mRbNb = (RadioButton) findViewById(R.id.nb);
            mVisarb = (RadioButton) findViewById(R.id.visarb);
            rupay = (RadioButton) findViewById(R.id.rupay);
            mMasterrb = (RadioButton) findViewById(R.id.masterrb);
            mMaestrorb = (RadioButton) findViewById(R.id.maestrorb);
            //tvnotice = (TextView) findViewById(R.id.txtNotice);
            tvnotice1 = (TextView) findViewById(R.id.tvnote1);
            tvnotice2 = (TextView) findViewById(R.id.tvnote2);
            Spinner = new ArrayList<String>();
            ID = new ArrayList<Integer>();
            Datastorage.SetTransactionDetail(getApplication(), "");
            // amount = getIntent().getExtras().getInt("Amount");
            Intent in = getIntent();
            amount = in.getIntExtra("Amount", 0);
            finalAmount = amount;
            extradetails = in.getStringExtra("ExtraDetails");
            new GetBankDetail().execute();

            cardradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    try {
                        switch (i) {
                            case R.id.cc:
                                if (((RadioButton) findViewById(R.id.cc)).isChecked()) {
                                    cardviewNbid.setVisibility(View.GONE);
                                    cardviewNbText.setVisibility(View.GONE);
                                    rlCardType.setVisibility(View.VISIBLE);
                                    if (paymentgateway.equals("1") || paymentgateway.equals("3")) {
                                        cardview.setVisibility(View.VISIBLE);
                                    } else {
                                        cardview.setVisibility(View.GONE);
                                    }
                                }
                                break;
                            case R.id.dc:
                                if (((RadioButton) findViewById(R.id.dc)).isChecked()) {
                                    cardviewNbid.setVisibility(View.GONE);
                                    cardviewNbText.setVisibility(View.GONE);
                                    rlCardType.setVisibility(View.VISIBLE);
                                    if (paymentgateway.equals("1") || paymentgateway.equals("3")) {
                                        cardview.setVisibility(View.VISIBLE);
                                    } else {
                                        cardview.setVisibility(View.GONE);
                                    }
                                }
                                break;
                            case R.id.nb:
                                if (((RadioButton) findViewById(R.id.nb)).isChecked()) {
                                    cardview.setVisibility(View.GONE);
                                    /*cardviewNbid.setVisibility(View.VISIBLE);
                                    cardviewNbText.setVisibility(View.VISIBLE);*/
                                }
                                break;
                        }
                    } catch (Exception ex) {
                        Constants.Logwrite("CardPaymentDetails:", "Ex 132:" + ex.getMessage());
                    }
                }
            });

            RGGetway.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    try {
                        switch (i) {
                            case R.id.rbAtom:
                                paymentgateway = "1";
                                mRbDc.setVisibility(View.GONE);
                                mRbCc.setVisibility(View.GONE);
                                mRbNb.setVisibility(View.GONE);
                                setVisibilityRadio(paymentgateway);

                                //RadioButton rb = (RadioButton) findViewById(cardradioGroup.getCheckedRadioButtonId());
                                /*if (rb == null) {
                                    rb.setChecked(false); }*/

//                                mRbNb.setVisibility(View.GONE);
//                                mRbDc.setChecked(true);
//                                mRbCc.setChecked(false);
                                cardview.setVisibility(View.VISIBLE);
                                rlCardType.setVisibility(View.VISIBLE);
                                setCardTypeName();
                                break;
                            case R.id.rbPaynimo:
                                paymentgateway = "2";

                                mRbDc.setVisibility(View.GONE);
                                mRbCc.setVisibility(View.GONE);
                                mRbNb.setVisibility(View.GONE);
                                setVisibilityRadio(paymentgateway);

                               /* mRbDc.setChecked(true);
                                mRbNb.setVisibility(View.VISIBLE);
                                mRbNb.setChecked(false);*/

                                cardviewCardType.setVisibility(View.VISIBLE);
                                cardview.setVisibility(View.GONE);
                                rlCardType.setVisibility(View.GONE);
                                break;
                            case R.id.rbEasyPay:
                                paymentgateway = "3";

                                mRbDc.setVisibility(View.GONE);
                                mRbCc.setVisibility(View.GONE);
                                mRbNb.setVisibility(View.GONE);
                                setVisibilityRadio(paymentgateway);

                               /* mRbDc.setChecked(true);
                                mRbNb.setVisibility(View.VISIBLE);
                                mRbNb.setChecked(false);*/
                                cardviewCardType.setVisibility(View.VISIBLE);
                                cardview.setVisibility(View.VISIBLE);
                                rlCardType.setVisibility(View.VISIBLE);
                                setCardTypeName();
                               /* mVisarb.setChecked(true);
                                mMasterrb.setChecked(false);
                                mMaestrorb.setChecked(false);
                                rupay.setChecked(false);*/
                                break;
                        }
                    } catch (Exception ex) {
                        Constants.Logwrite("CardPaymentDetails:", "Ex 132:" + ex.getMessage());
                    }
                }
            });
            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (!isOnline()) {
                            Toast.makeText(CardPaymentDetailActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_LONG).show();
                            return;
                        }
                        int selectedId1 = cardradioGroup.getCheckedRadioButtonId();
                        if (selectedId1 != -1) {
                            RadioButton radioButton1 = (RadioButton) findViewById(selectedId1);
                            if (radioButton1 != null && (!radioButton1.getText().equals("")
                                    || !radioButton1.getText().equals(null))) {
                                if (radioButton1.getText().toString().equalsIgnoreCase("NET BANKING")) {
                                    radiotxt = "NB";
                                    //amount += ((amount * nb) / 100);
                                    i = spinner.getSelectedItemPosition();
                                    ids = ID.get(i);
                                    String bankname = String.valueOf(spinner.getSelectedItem());
                                    pay.setEnabled(false);
                                    getCardDetail mytask = new getCardDetail();
                                    mytask.execute(String.valueOf(finalAmount), radiotxt, "", String.valueOf(ids), bankname);
                                    Constants.writelog("CardPaymentDetailActivity", "BtnContinueClick()135 Amount:" + finalAmount + " rdioTx:" + radiotxt + " Ids:" + ids + " BankName:" + bankname);
                                } else {
                                    if (radioButton1.getText().toString().equalsIgnoreCase("DEBIT CARD")) {
                                        radiotxt = "DC";
                                    } else if (radioButton1.getText().toString().equalsIgnoreCase("CREDIT CARD")) {
                                        radiotxt = "CC";
                                    }
                                    int selectedId2 = associatedradioGroup.getCheckedRadioButtonId();
                                    if (selectedId2 != -1 || paymentgateway.equals("2")) {
                                        if (selectedId2 != -1) {
                                            RadioButton radioButton2 = (RadioButton) findViewById(selectedId2);
                                            cardasso = String.valueOf(radioButton2.getText());
                                            Constants.writelog("CardPaymentDetailActivity", "BtnContinueClick()146 Amount:" + finalAmount + " rdioTx:" + cardasso);
                                        }
                                        pay.setEnabled(false);
                                        getCardDetail mytask = new getCardDetail();
                                        mytask.execute(String.valueOf(finalAmount), radiotxt, "", String.valueOf(ids), bankname);
                                    } else {
                                        Toast.makeText(CardPaymentDetailActivity.this, "Please select appropriate card type.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                Toast.makeText(CardPaymentDetailActivity.this, "Please Select Card Type.", Toast.LENGTH_LONG).show();
                            }
                        } else if (paymentgateway.equals("2")) {
                            Constants.writelog("CardPaymentDetailActivity", "BtnContinueClick()146 Amount:" + finalAmount + " rdioTx:" + cardasso);
                            //old
                            pay.setEnabled(false);
                            getCardDetail mytask = new getCardDetail();
                            mytask.execute(String.valueOf(finalAmount), radiotxt, "", String.valueOf(ids), bankname);
                        } else {
                            Toast.makeText(CardPaymentDetailActivity.this, "Please Select Payment Mode.", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(CardPaymentDetailActivity.this, "Error::\" + ex.getMessage() + \"::StackTrace::\" + ex.getStackTrace()", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception ex) {
            Constants.writelog(tag, "onCreate:Error:198" + ex.getMessage());
        }
    }

    public class GetBankDetail extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String responseString = null;
            SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_BANK_LIST_v3);
            request.addProperty("schoolid", schoolId);
            request.addProperty("studid", studId);
            request.addProperty("yearid", Year_id + "");
            try {
                SoapObject result = Constants.CallWebMethod(
                        CardPaymentDetailActivity.this, request, Constants.GET_BANK_LIST_v3, true);
                Constants.Logwrite("BANK DETAILS:", "Result:" + result.toString());
                if (result != null && result.getPropertyCount() > 0) {
                    SoapObject obj2 = (SoapObject) result.getProperty(0);
                    Constants.Logwrite("BANK DETAILS:", "object:" + obj2.toString());
                    if (obj2 != null) {
                        int count = obj2.getPropertyCount();
                        bankDetails = new String[count];
                      /*  bankDetails[0]="anyType{}";
                        bankDetails[1]="You will be redirected to our payment partners site for Safe &amp; Secure transction.";
                        bankDetails[2]="We do not store your card information.";
                        bankDetails[3]="surcharge#YES@@email#ankit@xpditesolutions.com";
                        bankDetails[4]="icici#2001@@Bank Of Baroda#2002@@City bank#2003";
                        bankDetails[5]="ATOM Payment Getway#1#1,2";
                        bankDetails[6]="1#DEBIT CARD@@2#CREDIT CARD@@3#NET BANNKING";
                        bankDetails[7]="1#VISA@@2#MASTER@@3#MASTERO@@4#RuPay";*/
                        for (int i = 0; i < count; i++) {
                            bankDetails[i] = obj2.getProperty(i).toString();
                            Constants.Logwrite("BANK DETAILS:i", "" + bankDetails[i].toString());
                        }
                    }
                }
            } catch (Exception e) {
                responseString = e.getMessage();
                e.printStackTrace();
                Constants.writelog(tag, "CardPaymentDetails:doInBackground() 166 :IOException Error: " + e.getMessage());
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(final String success) {
            try {
                if (bankDetails != null && bankDetails.length > 0) {
                    /*if (bankDetails[0] != null && !bankDetails[0].equals("") && !bankDetails[0].equals("anyType{}")) {
                        Spanned spanned = Html.fromHtml(bankDetails[0]);
                        tvnotice.setText(spanned.toString());
                        tvnotice.setText(bankDetails[0]);
                    }*/
                    if (bankDetails[1] != null && !bankDetails[1].equals("") && !bankDetails[1].equals("anyType{}")) {
                        Spanned spanned = Html.fromHtml(bankDetails[1]);
                        tvnotice1.setText(spanned.toString());
                        //tvnotice1.setText(bankDetails[1]);
                    }
                    if (bankDetails[2] != null && !bankDetails[2].equals("") && !bankDetails[2].equals("anyType{}")) {
                        Spanned spanned = Html.fromHtml(bankDetails[2]);
                        tvnotice2.setText(spanned.toString());
                        //tvnotice2.setText(bankDetails[2]);
                    }
                    if (bankDetails.length > 3 && bankDetails[3] != null && !bankDetails[3].equals("") && !bankDetails[3].equals("anyType{}")) {
                        String[] parts = bankDetails[3].split("@@");
                        for (int i = 0; i < parts.length; i++) {
                            try {
                                String[] values = parts[i].split("#");
                                if (values[0].equalsIgnoreCase("surcharge") && values.length > 1) {
                                    surCharge = values[1];
                                } else if (values[0].equalsIgnoreCase("email") && values.length > 1) {
                                    email = values[1];
                                }
                            } catch (Exception ex) {
                                Constants.writelog(tag, "onPostExecute() 239 : Error: " + ex.getMessage());
                            }
                        }
                    }
                    if (bankDetails.length > 4 && bankDetails[4] != null && !bankDetails[4].equals("") && !bankDetails[4].equals("anyType{}")) {
                        String[] parts = bankDetails[4].split("@@");

                        for (int i = 0; i < parts.length; i++) {
                            try {
                                String[] values = parts[i].split("#");
                                Spinner.add(values[0]);
                                ID.add(Integer.valueOf(values[1]));
                            } catch (Exception ex) {
                                Constants.writelog(tag, "onPostExecute() 239 : Error: " + ex.getMessage());
                            }
                            ArrayAdapter<String> strings = new ArrayAdapter<String>(CardPaymentDetailActivity.this, android.R.layout.simple_spinner_item, Spinner);
                            strings.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(strings);
                        }
                    }

                    if (bankDetails.length > 5 && !bankDetails[5].isEmpty() && !bankDetails[5].equals("anyType{}")) {
                        getwayDetails = bankDetails[5].split("@@");
                        paymentgatewayName = bankDetails[6].split("@@");
                        cardTypeName = bankDetails[7].split("@@");
                        if (getwayDetails.length > 1) {
                            cardviewPaymentGetway.setVisibility(View.VISIBLE);
                        } else {
                            paymentgateway = getwayDetails[0].split("#")[1];
                            if(paymentgateway.equals("1") || paymentgateway.equals("3")){
                                cardview.setVisibility(View.VISIBLE);
                                setCardTypeName();
                            }
                            cardType = getwayDetails[0].split("#")[2];
                            String values[] = cardType.split(",");
                            if (values.length >= 0)
                                cardviewCardType.setVisibility(View.VISIBLE);
                            for (i = 0; i < values.length; i++) {
                                if (values[i].equals("1")) {
                                    mRbDc.setVisibility(View.VISIBLE);
                                    mRbDc.setText(paymentgatewayName[0].split("#")[1]);
                                    mRbDc.setChecked(true);
                                } else if (values[i].equals("2")) {
                                    if (mRbDc.getVisibility() == View.GONE)
                                        mRbCc.setChecked(true);
                                    mRbCc.setVisibility(View.VISIBLE);
                                    mRbCc.setText(paymentgatewayName[1].split("#")[1]);
                                } else if (values[i].equals("3")) {
                                    if (mRbDc.getVisibility() == View.GONE && mRbCc.getVisibility() == View.GONE)
                                        mRbNb.setChecked(true);
                                    mRbNb.setVisibility(View.VISIBLE);
                                    mRbNb.setText(paymentgatewayName[2].split("#")[1]);
                                }
                            }
                        }

                        for (int i = 0; i < getwayDetails.length; i++) {
                            if (getwayDetails[i].split("#")[1].equals("1")) {
                                mRbAtom.setText(getwayDetails[i].split("#")[0]);
                                mRbAtom.setVisibility(View.VISIBLE);
                            } else if (getwayDetails[i].split("#")[1].equals("2")) {
                                mRbPaynimo.setText(getwayDetails[i].split("#")[0]);
                                mRbPaynimo.setVisibility(View.VISIBLE);
                            } else if (getwayDetails[i].split("#")[1].equals("3")) {
                                mRbEasyPay.setText(getwayDetails[i].split("#")[0]);
                                mRbEasyPay.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        cardviewCardType.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    Constants.NotifyNoInternet(CardPaymentDetailActivity.this);
                }
            } catch (Exception ex) {
                Constants.writelog(tag, "CardPaymentDetailActivity:onPostExecute() 211 : Error: " + ex.getMessage());
            } finally {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    public void setVisibilityRadio(String Id) {
        try {
            if (getwayDetails.length > 1) {
                for (i = 0; i < getwayDetails.length; i++) {
                    String paymentgatewayTemp = getwayDetails[i].split("#")[1];

                    if (paymentgatewayTemp.equals(Id)) {
                        cardType = getwayDetails[i].split("#")[2];
                        String values[] = cardType.split(",");
                        if (values.length >= 0)
                            cardviewCardType.setVisibility(View.VISIBLE);
                        for (i = 0; i < values.length; i++) {
                            if (values[i].equals("1")) {
                                mRbDc.setVisibility(View.VISIBLE);
                                mRbDc.setText(paymentgatewayName[0].split("#")[1]);
                                mRbDc.setChecked(true);
                            } else if (values[i].equals("2")) {
                                if (mRbDc.getVisibility() == View.GONE)
                                    mRbCc.setChecked(true);
                                mRbCc.setVisibility(View.VISIBLE);
                                mRbCc.setText(paymentgatewayName[1].split("#")[1]);
                            } else if (values[i].equals("3")) {
                                if (mRbDc.getVisibility() == View.GONE && mRbCc.getVisibility() == View.GONE)
                                    mRbNb.setChecked(true);
                                mRbNb.setVisibility(View.VISIBLE);
                                mRbNb.setText(paymentgatewayName[2].split("#")[1]);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Constants.writelog(tag, "setVisibilityRadio() 454: " + e.getMessage());
        }
    }

    public void setCardTypeName() {
        for (int j = 0; j < cardTypeName.length; j++) {
            String values[] = cardTypeName[j].split("#");
            if (values[0].equals("1")) {
                mVisarb.setText(values[1]);
                mVisarb.setChecked(true);
            }
            if (values[0].equals("2")) {
                mMasterrb.setText(values[1]);
                mMasterrb.setChecked(false);
            }
            if (values[0].equals("3")) {
                mMaestrorb.setText(values[1]);
                mMaestrorb.setChecked(false);
            }
            if (values[0].equals("4")) {
                rupay.setText(values[1]);
                rupay.setChecked(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CardPaymentDetailActivity.this, FeesPayActivity.class);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        onBackClickAnimation();
    }

    public class getCardDetail extends AsyncTask<String, Void, String> {

        boolean isuserfound = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String responseString = null;
            SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_PAYMENT_TRANSACTION);
            try {
                request.addProperty("Stud_Id", studId);
                request.addProperty("School_Id", schoolId);
                request.addProperty("FeeDetailsText", extradetails);
                request.addProperty("Amount", finalAmount + "");//finalAmount
                request.addProperty("CardType", radiotxt);
                request.addProperty("CardAssociate", cardasso);
                request.addProperty("PaymentGateway", paymentgateway);
                request.addProperty("AppName", SchoolDetails.appname);
                request.addProperty("PaymentMethodType", "1");
                request.addProperty("bankCode", ids);
                request.addProperty("bankName", bankname);
                request.addProperty("year_id", Year_id);
                SoapObject result = Constants.CallWebMethod(CardPaymentDetailActivity.this, request,
                        Constants.GET_PAYMENT_TRANSACTION, false);

                SoapObject obj2 = (SoapObject) result.getProperty(0);
                transactionId = obj2.getProperty(0).toString();
                if (transactionId != null && !transactionId.equalsIgnoreCase("0") && !transactionId.equalsIgnoreCase("")) {
                    Datastorage.SetTransactionDetail(CardPaymentDetailActivity.this, transactionId);
                    date = obj2.getProperty(1).toString();
                    merchantId = obj2.getProperty(2).toString();
                    loginid = obj2.getProperty(3).toString();
                    password = obj2.getProperty(4).toString();
                    clientcode = obj2.getProperty(5).toString();
                    custacc = obj2.getProperty(6).toString();
                    productId = obj2.getProperty(7).toString();
                    channelid = obj2.getProperty(8).toString();
                    url = obj2.getProperty(9).toString();
                    fullname = obj2.getProperty(10).toString();
                    mobilenumber = obj2.getProperty(11).toString();
                    finalAmountFromServer = Double.parseDouble(obj2.getProperty(12).toString());
                    if (obj2.getPropertyCount() > 13) {
                        referanceNo = obj2.getProperty(13).toString();
                    }
                    if (obj2.getPropertyCount() > 14) {
                        publicKey = obj2.getProperty(14).toString();
                    }
                    if (obj2.getPropertyCount() > 15) {
                        reqHashKey = obj2.getProperty(15).toString();
                    }
                    if (obj2.getPropertyCount() > 16) {
                        respHashKey = obj2.getProperty(16).toString();
                        mcommon.setSession(Constants.RESPONSE_URL, respHashKey);
                    }
                    isuserfound = true;
                } else {
                    isuserfound = false;
                }
            } catch (Exception e) {
                responseString = e.getMessage();
                e.printStackTrace();
                Constants.writelog(tag, "getDetailRecipeTask:doInBackground() 213 : Reqest:" + request.toString() + " \n Error: " + e.getMessage());
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(final String success) {
            if (success == null) {
                try {
                    if (isuserfound) {
                        //finalAmountFromServer = 10; for test amount tempering
                        if (finalAmountFromServer != finalAmount) {
                            Intent i = new Intent(CardPaymentDetailActivity.this, PaymentResponceActivity.class);
                            i.addCategory(Intent.CATEGORY_HOME);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            i.putExtra("transactionid", transactionId);
                            i.putExtra("message", "");
                            i.putExtra("key", "{StatusCode,transactionType,transactionSubType,merchant_id,StatusMessage,ErrorMessage,Amount,DateTime,MerchantTransactionIdentifier,Identifier,BankSelectionCode,BankReferenceIdentifier,RefundIdentifier,BalanceAmount,InstrumentAliasName,SI Mandate Id,SI Mandate Status,SI Mandate Error Code,}");
                            i.putExtra("values", "{0,,," + merchantId + ",Transaction Amount Mismatch with the bank.,," + finalAmountFromServer + "," + date + "," + transactionId + ",0,0,,,,,,,,}");
                            startActivity(i);
                            finish();
                            onClickAnimation();
                        } else {
                            if (paymentgateway.equals("1")) {
                               TRANSACTIONDETAIL_ATOM();
                            } else if (paymentgateway.equals("2")) {
                                InitiatePAYNIMOTransaction();
                            } else {
                                InitiateEasyPyaTransaction();
                            }
                        }
                    } else {
                        Constants.NotifyNoInternet(CardPaymentDetailActivity.this);
                    }
                    mProgressBar.setVisibility(View.GONE);
                } catch (Exception ex) {
                    Constants.writelog(tag, "getDetailRecipeTask:onPostExecute() 315 : Error: " + ex.getMessage());
                } finally {
                    pay.setEnabled(true);
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        }

    }

    private void TRANSACTIONDETAIL_ATOM() {
        if (transactionId != null && !transactionId.trim().equalsIgnoreCase("")) {
            // if (radioButton1.getText().equals("Net Banking")) {
            Intent newPayIntent = new Intent(CardPaymentDetailActivity.this, PayActivity.class);
            try {

                //newPayIntent.putExtra("merchantId", "197");
                //// txnscamt Fixed. Must be 0
                //newPayIntent.putExtra("txnscamt", "0");
                //newPayIntent.putExtra("loginid","197" );
                //newPayIntent.putExtra("password", "Test@123");
                //newPayIntent.putExtra("prodid", "NSE");
                //// txncurr Fixed. Must be �INR�
                //newPayIntent.putExtra("txncurr", "INR");
                //newPayIntent.putExtra("clientcode", encodeBase64 ("007") );
                //newPayIntent.putExtra("custacc","100000036600" );
                //newPayIntent.putExtra("channelid", "INT");
                //// amt  Should be 2 decimal number i.e 1.00
                //newPayIntent.putExtra("amt","10.0" );
                //newPayIntent.putExtra("txnid", "013");
                ////Date  Should be in same format
                //newPayIntent.putExtra("date", "01/10/2019 18:31:00 newPayIntent.putExtra("signature_request","KEY123657234" );
                //newPayIntent.putExtra("signature_response","KEYRESP123657234" );
                //newPayIntent.putExtra("discriminator","CCDC");
                //// CC or DC ONLY (value should be same as commented)
                //newPayIntent.putExtra("cardtype","CC");
                //// VISA or MASTER or MAESTRO ONLY (value should be same as commented)
                //newPayIntent.putExtra("cardAssociate”, “VISA");
                ////YES
                //newPayIntent.putExtra("surcharge”,” NO");
                //newPayIntent.putExtra("isLive",false);
                ////Optinal Parameters
                ////Only for Name
                //newPayIntent.putExtra("customerName", "LMN PQR");
                //
                ////Only for Email ID
                //newPayIntent.putExtra("customerEmailID", "Test@gmail.com");
                ////Only for Mobile Number
                //newPayIntent.putExtra("customerMobileNo","8087911057" );
                ////Only for Address
                //newPayIntent.putExtra("billingAddress", "Pune");
                //// Can pass any data
                //newPayIntent.putExtra("optionalUdf9", "OPTIONAL DATA 2");
                //// Pass data in XML format, only for Multi product
                //newPayIntent.putExtra("mprod", mprod);
                //startActivityForResult(newPayIntent,1);
                DecimalFormat precision = new DecimalFormat("0.00");
                newPayIntent.putExtra("merchantId", merchantId);
                newPayIntent.putExtra("txnscamt", "0"); //Fixed. Must be “0”
                newPayIntent.putExtra("loginid", loginid);
                newPayIntent.putExtra("password", password);
                newPayIntent.putExtra("prodid", productId);
                newPayIntent.putExtra("txncurr", "INR"); //Fixed. Must be “INR”
                newPayIntent.putExtra("clientcode", clientcode);
                newPayIntent.putExtra("custacc", custacc);
                newPayIntent.putExtra("channelid", channelid);
                newPayIntent.putExtra("amt", precision.format(finalAmount));//Should be 2 decimal number i.e 1.00
                newPayIntent.putExtra("txnid", transactionId);
                newPayIntent.putExtra("date", date);//Should be in same format
                newPayIntent.putExtra("cardtype", radiotxt);// CC or DC ONLY (value should be same as commented)
                newPayIntent.putExtra("cardAssociate", cardasso);// VISA or MASTER or MAESTRO ONLY (value should be same as commented)
                newPayIntent.putExtra("surcharge", surCharge);
                newPayIntent.putExtra("discriminator",radiotxt);
                boolean isLive=true;
                if(!BuildConfig.DEBUG){
                    isLive=true;
                }
                newPayIntent.putExtra("isLive",isLive);
                newPayIntent.putExtra("customerName", fullname); //Only for Name
                newPayIntent.putExtra("customerMobileNo", mobilenumber);//Only for Mobile Number
                newPayIntent.putExtra("signature_request", reqHashKey);
                newPayIntent.putExtra("signature_response", respHashKey);

                newPayIntent.putExtra("customerEmailID", email);//Only for Email ID
                newPayIntent.putExtra("ru", url);//url https://paynetzuat.atomtech.in/mobilesdk/param
                startActivityForResult(newPayIntent, 1);

            } catch (Exception ex) {
                Constants.writelog(tag, "TRANSACTIONDETAIL() NetBanking 376 : intent:" + newPayIntent.toString() + " \n Error: " + ex.getMessage());
            }
        }
    }

    private void InitiatePAYNIMOTransaction() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String currentDateandTime = sdf.format(new Date());

            DecimalFormat precision = new DecimalFormat("0.00");
            Checkout checkout = new Checkout();
            checkout.setMerchantIdentifier(merchantId);//"T143246");
            checkout.setTransactionIdentifier(transactionId);//"215654321");
            checkout.setTransactionReference(referanceNo);
            checkout.setTransactionType(PaymentActivity.TRANSACTION_TYPE_SALE);
            checkout.setTransactionSubType(PaymentActivity.TRANSACTION_SUBTYPE_DEBIT);
            checkout.setTransactionCurrency("INR");
            checkout.setTransactionAmount(precision.format(finalAmount) + "");//precision.format(finalAmount)
            checkout.setTransactionDateTime(currentDateandTime);
            checkout.setConsumerEmailID(email);
            checkout.setConsumerMobileNumber(mobilenumber);
            checkout.addCartItem(productId, precision.format(finalAmount) + "", "0.0", "0.0", "", "", "", "");

            //checkout.addCartItem(productId, precision.format(finalAmount / 2) + "", "0.0", "0.0", "", "", "", "");
            //checkout.addCartItem("test1", precision.format(finalAmount / 2) + "", "0.0", "0.0", "", "", "", "");
            //checkout.addCartItem("test", "100.00", "0.0", "0.0", "", "", "", "");
            //checkout.addCartItem("test1", "150.00", "0.0", "0.0", "", "", "", "");
            Intent authIntent = new Intent(CardPaymentDetailActivity.this, PaymentModesActivity.class);
            Constants.writelog("Checkout Request Object",
                    checkout.getMerchantRequestPayload().toString());
            //checkout.setTransactionMerchantInitiated("Y");
            authIntent.putExtra(PaymentActivity.ARGUMENT_DATA_CHECKOUT, checkout);
            authIntent.putExtra(PaymentActivity.EXTRA_PUBLIC_KEY, publicKey);
            authIntent.putExtra(PaymentActivity.EXTRA_REQUESTED_PAYMENT_MODE, PaymentActivity.PAYMENT_METHOD_CARDS);

            if (mRbNb.isChecked()) {
                authIntent.putExtra(PaymentActivity.EXTRA_REQUESTED_PAYMENT_MODE, PaymentActivity.PAYMENT_METHOD_NETBANKING);
            }
            startActivityForResult(authIntent, PaymentActivity.REQUEST_CODE);
        } catch (Exception ex) {
            Constants.writelog(tag, "Method Name:InitiatePAYNIMOTransaction() 461:" + ex.getMessage());
        }
    }

    private void InitiateEasyPyaTransaction() {
        try {
            if (isOnline()) {
                //respHashKey
                Intent intent = new Intent(CardPaymentDetailActivity.this, EasyPayActivity.class);
                intent.putExtra("URL", reqHashKey);
                intent.putExtra("transactionId", transactionId);
                startActivity(intent);
                onClickAnimation();
            } else {
                Common.showToast(CardPaymentDetailActivity.this, getString(R.string.msg_connection));
            }
        } catch (Exception ex) {
            Constants.writelog(tag, "Method Name:InitiateEasyPyaTransaction() 492:" + ex.getMessage());
        }
    }

    @Override
    protected void onResume() {
        try {
            if (!Datastorage.GetTransactionDetail(CardPaymentDetailActivity.this).equalsIgnoreCase("")) {
                Intent i = new Intent(CardPaymentDetailActivity.this, PaymentResponceActivity.class);
                i.addCategory(Intent.CATEGORY_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("transactionid", Datastorage.GetTransactionDetail(CardPaymentDetailActivity.this));
                i.putExtra("message", "");
                i.putExtra("key", "");
                i.putExtra("values", "");
                startActivity(i);
                onClickAnimation();
                //finish();
            }
        } catch (Exception ex) {
            Constants.writelog("CardPaymentDetailActivity", "Msg 429:" + ex.getMessage());
        }
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (paymentgateway.equals("1")) {
            //region ATOM
            try {
                if (requestCode == 1) {
                    if (data != null) {
                        message = data.getStringExtra("status");
                        String[] resKey = data.getStringArrayExtra("responseKeyArray");
                        String[] resValue = data.getStringArrayExtra("responseValueArray");
                        String Result = "";
                        if (resKey != null && resValue != null) {
                            respKey = "{";
                            respVal = "{";
                            for (int i = 0; i < resKey.length; i++) {
                                System.out.println("  " + i + " resKey : " + resKey[i] + " resValue : " + resValue[i]);
                                respKey += resKey[i] + ",";
                                respVal += resValue[i] + ",";
                                Result += resKey[i] + ":" + resValue[i] + ",";
                            }
                            respKey += "}";
                            respVal += "}";
                        }
                        //Toast.makeText(CardPaymentDetailActivity.this, message, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(CardPaymentDetailActivity.this, PaymentResponceActivity.class);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        i.putExtra("transactionid", transactionId);
                        i.putExtra("message", message);
                        i.putExtra("key", respKey);
                        i.putExtra("values", respVal);
                        startActivity(i);
                        finish();
                        onClickAnimation();
               /*UpdateTransactionDetail mytask = new UpdateTransactionDetail();
                mytask.execute();*/
                    }
                }
            } catch (Exception ex) {
                Constants.writelog(tag, "onActivityResult() 468 : ReqestCode:" + requestCode + " ResultCode:" + resultCode + " Data:" + data + " \n Error: " + ex.getMessage());
            }
            //endregion
        } else {
            //region PAY NIMO
            String StatusCode = "";
            String transactionType = "";
            String transactionSubType = "";
            String result = "";
            respKey = "{";
            respVal = "{";
            Checkout checkout_res;
            try {
                if (requestCode == PaymentActivity.REQUEST_CODE) {//Gateway_pgTxnId
                    // Make sure the request was successful
                    if (resultCode == PaymentActivity.RESULT_OK) {//Gateway_RespCode
                        Constants.writelog(tag, "Result Code :" + RESULT_OK);
                        if (data != null) {
                            try {
                                checkout_res = (Checkout) data
                                        .getSerializableExtra(PaymentActivity
                                                .ARGUMENT_DATA_CHECKOUT);

                                transactionType = checkout_res.
                                        getMerchantRequestPayload().getTransaction().getType();
                                transactionSubType = checkout_res.
                                        getMerchantRequestPayload().getTransaction().getSubType();

                                StatusCode = checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getStatusCode();

                                message = checkout_res
                                        .getMerchantResponsePayload().getPaymentMethod()
                                        .getPaymentTransaction().getStatusMessage();

                                respKey += "StatusCode,";
                                respVal += StatusCode + ",";

                                respKey += "transactionType,";
                                respVal += transactionType + ",";

                                respKey += "transactionSubType,";
                                respVal += transactionSubType + ",";

                                respKey += "merchant_id,";
                                respVal += checkout_res
                                        .getMerchantResponsePayload().getMerchantCode() + ",";

                                respKey += "StatusMessage,";
                                respVal += checkout_res
                                        .getMerchantResponsePayload().getPaymentMethod()
                                        .getPaymentTransaction().getStatusMessage() + ",";

                                respKey += "ErrorMessage,";
                                respVal += checkout_res
                                        .getMerchantResponsePayload().getPaymentMethod()
                                        .getPaymentTransaction().getErrorMessage() + ",";

                                respKey += "Amount,";
                                respVal += checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getAmount() + ",";

                                respKey += "DateTime,";
                                respVal += checkout_res.
                                        getMerchantResponsePayload().getPaymentMethod()
                                        .getPaymentTransaction().getDateTime() + ",";

                                respKey += "MerchantTransactionIdentifier,";
                                respVal += checkout_res.getMerchantResponsePayload()
                                        .getMerchantTransactionIdentifier() + ",";


                                respKey += "Identifier,";
                                respVal += checkout_res
                                        .getMerchantResponsePayload().getPaymentMethod()
                                        .getPaymentTransaction().getIdentifier() + ",";

                                respKey += "BankSelectionCode,";
                                respVal += checkout_res
                                        .getMerchantResponsePayload().getPaymentMethod()
                                        .getBankSelectionCode() + ",";

                                respKey += "BankReferenceIdentifier,";
                                respVal += checkout_res
                                        .getMerchantResponsePayload().getPaymentMethod()
                                        .getPaymentTransaction().getBankReferenceIdentifier() + ",";

                                respKey += "RefundIdentifier,";
                                respVal += checkout_res
                                        .getMerchantResponsePayload().getPaymentMethod()
                                        .getPaymentTransaction().getRefundIdentifier() + ",";

                                respKey += "BalanceAmount,";
                                respVal += checkout_res
                                        .getMerchantResponsePayload().getPaymentMethod()
                                        .getPaymentTransaction().getBalanceAmount() + ",";

                                respKey += "InstrumentAliasName,";
                                respVal += checkout_res
                                        .getMerchantResponsePayload().getPaymentMethod()
                                        .getInstrumentAliasName() + ",";

                                respKey += "SI Mandate Id,";
                                respVal += checkout_res
                                        .getMerchantResponsePayload().getPaymentMethod()
                                        .getPaymentTransaction().getInstruction().getId() + ",";

                                respKey += "SI Mandate Status,";
                                respVal += checkout_res
                                        .getMerchantResponsePayload().getPaymentMethod()
                                        .getPaymentTransaction().getInstruction().getStatusCode() + ",";

                                respKey += "SI Mandate Error Code,";
                                respVal += checkout_res
                                        .getMerchantResponsePayload().getPaymentMethod()
                                        .getPaymentTransaction().getInstruction().getErrorcode() + ",";

                                result = "StatusCode : " + checkout_res
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

                                Constants.writelog("About", "msg:" + result);
                            } catch (Exception ex) {
                                Constants.writelog(tag, "Paynimo742: ex:" + ex.getMessage());
                            }
                        }
                    } else if (resultCode == PaymentActivity.RESULT_ERROR) {
                        Constants.writelog(tag, "got an error");

                        if (data.hasExtra(PaymentActivity.RETURN_ERROR_CODE) &&
                                data.hasExtra(PaymentActivity.RETURN_ERROR_DESCRIPTION)) {
                            String error_code = (String) data
                                    .getStringExtra(PaymentActivity.RETURN_ERROR_CODE);
                            String error_desc = (String) data
                                    .getStringExtra(PaymentActivity.RETURN_ERROR_DESCRIPTION);

                            Toast.makeText(getApplicationContext(), " Got error :"
                                    + error_code + "--- " + error_desc, Toast.LENGTH_LONG)
                                    .show();
                            checkout_res = (Checkout) data
                                    .getSerializableExtra(PaymentActivity
                                            .ARGUMENT_DATA_CHECKOUT);

                            respKey += "ErrorCode" + ",";
                            respVal += (String) data.getStringExtra(PaymentActivity.RETURN_ERROR_CODE) + ",";

                            respKey += "ErrorDesc" + ",";
                            respVal += (String) data.getStringExtra(PaymentActivity.RETURN_ERROR_DESCRIPTION) + ",";


                            respKey += "StatusMessage" + ",";
                            respVal += checkout_res.getMerchantResponsePayload().getPaymentMethod()
                                    .getPaymentTransaction().getStatusMessage() + ",";
                            Constants.writelog(tag + " Code=>", error_code);
                            Constants.writelog(tag + " Desc=>", error_desc);
                        }
                    } else if (resultCode == PaymentActivity.RESULT_CANCELED) {
                        respKey += "StatusMessage,";
                        respVal += "Transaction Aborted by User,";
                        message = "Transaction Aborted";
                        Toast.makeText(getApplicationContext(), "Transaction Aborted by User",
                                Toast.LENGTH_SHORT).show();
                        Log.d(tag, "User pressed back button");
                    }
                }
            } catch (Exception ex) {
                Constants.writelog(tag, "onActivityResult() 778 : paymentgateway:" + paymentgateway + " ReqestCode:" + requestCode + " ResultCode:" + resultCode + " Data:" + data + " \n Error: " + ex.getMessage());
            }
            respKey += "}";
            respVal += "}";
            Intent i = new Intent(CardPaymentDetailActivity.this, PaymentResponceActivity.class);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.putExtra("transactionid", transactionId);
            i.putExtra("message", message);
            i.putExtra("key", respKey);
            i.putExtra("values", respVal);
            startActivity(i);
            finish();
            onClickAnimation();
            //endregion

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            hideKeyboard(CardPaymentDetailActivity.this);
            CardPaymentDetailActivity.this.finish();
            onBackClickAnimation();
        }
        return super.onOptionsItemSelected(item);
    }
}
