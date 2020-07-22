package com.expedite.apps.kumkum;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.expedite.apps.kumkum.activity.OTPNewActivity;
import com.expedite.apps.kumkum.activity.OtpActivity;
import com.expedite.apps.kumkum.constants.Constants;

/**
 * Created by Administrator on 06/01/2017.
 */

public class IncomingSms extends BroadcastReceiver {

    static final String tag = "IncomingSMS";

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String message = currentMessage.getDisplayMessageBody();
                    try {
                        if(message.toLowerCase().contains("is the otp for login."))
                        {
                            OTPNewActivity Sms1 = new OTPNewActivity();
                            if (message.length() >= 6) {
                                String latD = message.substring(0, 6);
                                message = latD;
                                try {
                                    int otp = Integer.parseInt(message);
                                    Sms1.recivedSms(message);
                                    Constants.writelog(tag, "onReceive() 40:OTP From Service:" + otp);
                                } catch (Exception ex) {
                                    Constants.writelog(tag, "onReceive() 42:OTP Convert in Number Check:SMS Text: " + message + "  : Error: " + ex.getMessage());
                                }
                            }
                        }
                        else if (message.toLowerCase().contains("is the otp for change pin.")) {
                            OtpActivity Sms=new OtpActivity();
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
                }
            }
        } catch (Exception e) {
            Constants.writelog(tag, "onReceive() 51 : Error: " + e.getMessage());
        }
    }
}