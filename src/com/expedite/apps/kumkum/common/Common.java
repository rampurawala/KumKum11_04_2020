package com.expedite.apps.kumkum.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.constants.Constants;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by Jaydeep patel on 14/04/2017.
 */

public class Common {

    Context activity;
    public SharedPreferences settings;

    public Common(Context activity) {
        this.activity = activity;
        settings = activity.getSharedPreferences(Constants.PREF_NAME, 0);
    }

    public static boolean isOnline(Context cn) {
        ConnectivityManager cm = (ConnectivityManager) cn.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }

    public void ClearSharedPreferences(Context cn) {
        SharedPreferences preferences = cn.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }


    public static void setCount(Context context, int count) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(String.valueOf(Constants.adCurrentCount), String.valueOf(count));
        editor.apply();
    }

    public String getSession(String key) {
        return settings.getString(key, "");
    }

    public void setSession(String key, String value) {
        settings.edit().putString(key, value).commit();
    }

    public static void showLog(String str, String log) {
        try {
            Log.e(str, log);
        } catch (Exception ex) {
            printStackTrace(ex);
        }
    }

    public static void printStackTrace(Exception exception) {
        try {
            exception.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showToast(Context context, String message) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            printStackTrace(ex);
        }
    }

    public static void setToastMessage(Activity activity, View viewById, String message) {
        try {
            Snackbar snackbar = Snackbar
                    .make(viewById, message, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(activity.getResources().getColor(R.color.white));
            View snackbarView = snackbar.getView();
            int snackbarTextId =com.google.android.material.R.id.snackbar_text;
            TextView textView = (TextView) snackbarView.findViewById(snackbarTextId);
            textView.setTextColor(activity.getResources().getColor(R.color.white));
            snackbar.show();
        } catch (Exception ex) {
            Constants.writelog("Common", "setToastMessage():: Exception::" + ex.getMessage());
        }
    }

    public static void onClickAnimation(Activity activity) {
        try {
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } catch (Exception ex) {

        }
    }

    public static void onBackClickAnimation(Activity activity) {
        try {
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } catch (Exception ex) {

        }
    }

    public static Long GetTimeTicks() {
        Long tsLong = System.currentTimeMillis() / 1000;
        return tsLong;
    }

    public static void startSmsRecevier(Context cn) {
        try {
            //retrive sms automatically
            SmsRetrieverClient client = SmsRetriever.getClient(cn);
            // Starts SmsRetriever, waits for ONE matching SMS message until timeout
            // (5 minutes).
            Task<Void> task = client.startSmsRetriever();
            // Listen for success/failure of the start Task.
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Toast.makeText(LoginActivity.this, "Successfully started retriever", Toast.LENGTH_SHORT).show();

                    Log.e("282:", "Successfully started retriever");
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("289:", "Failed to start retriever");
                }
            });
        } catch (Exception e) {
            Common.printStackTrace(e);
        }
    }
}
