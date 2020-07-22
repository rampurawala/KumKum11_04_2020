package com.expedite.apps.kumkum;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.expedite.apps.kumkum.activity.OTPNewActivity;
import com.expedite.apps.kumkum.activity.OtpActivity;
import com.expedite.apps.kumkum.constants.Constants;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class MySms extends BroadcastReceiver {

    String tag = "MySms";

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Log.e("Otp :", message);
                   // Toast.makeText(context, "Received sms:" + message, Toast.LENGTH_LONG).show();

                    try {
                        if (message.toLowerCase().contains("is the otp for login.")) {
                            OTPNewActivity OtpNew = new OTPNewActivity();
                            if (message.length() >= 6) {
                                String latD = message.substring(0, 6);
                                message = latD;
                                try {
                                    int otp = Integer.parseInt(message);
                                    OtpNew.recivedSms(message);
                                    Constants.writelog(tag, "onReceive() 40:OTP From Service:" + otp);
                                } catch (Exception ex) {
                                    Constants.writelog(tag, "onReceive() 42:OTP Convert in Number Check:SMS Text: " + message + "  : Error: " + ex.getMessage());
                                }
                            }
                        } else if (message.toLowerCase().contains("is the otp for change pin.")) {
                            OtpActivity Sms = new OtpActivity();
                            if (message.length() >= 6) {
                                String latD = message.substring(0, 6);
                                message = latD;
                                try {
                                    int otp = Integer.parseInt(message);
                                    Sms.recivedSms(message);
                                    Constants.writelog(tag, "onReceive() 55:OTP From Service:" + otp);
                                } catch (Exception ex) {
                                    Constants.writelog(tag, "onReceive() 57:OTP Convert in Number Check:SMS Text: " + message + "  : Error: " + ex.getMessage());
                                }
                            }
                        }
                    } catch (Exception e) {
                        Constants.writelog(tag, "onReceive() 46 :: SMS Text: " + message + " : Error: " + e.getMessage());
                    }

                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...
                    break;
            }
        }
    }catch (Exception e){
            Constants.writelog(tag, "onReceive() 76 :: SMS Text: " + e.getMessage() + " : Error: " + e.getMessage());
        }
    }
}