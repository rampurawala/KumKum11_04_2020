package com.expedite.apps.kumkum;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.expedite.apps.kumkum.activity.AccountListActivity;
import com.expedite.apps.kumkum.activity.AccountListRemoveActivity;
import com.expedite.apps.kumkum.activity.AddAccountActivity;
import com.expedite.apps.kumkum.activity.CurriculumClassroomActivity;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.common.ImageZoom;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.expedite.apps.kumkum.constants.Constants.CheckExternalSdCardPresent;

public class BaseActivity extends AppCompatActivity {
    public static Typeface sFontRegular, sFontold;
    public DatabaseHandler db = null;
    public ActionBarDrawerToggle mToggle;
    private InterstitialAd mInterstitialAd;
    private String StudId = "", SchoolId = "";
    static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
    private ImageZoom photoView;
    private View mView;
    private AlertDialog mDialog;
    private AlertDialog.Builder mBuilder;
    Context mGlobalContext;

    public static void setCount(Context context, int count) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.adCurrentCount, count + "");
        editor.apply();
    }

    public static void setAdStatus(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.isShowAdd, status);
        editor.apply();
    }

    public static void setSubmissionShow(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.ShowSubNew, status);
        editor.apply();
    }

    public static void setCurrentTime(Context context) {
        SimpleDateFormat format = new SimpleDateFormat("HH", Locale.US);
        String hour = format.format(new Date());
        Common.showLog("Hour 71 BaseActivity", hour);
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.currentTime, hour);
        editor.apply();
    }

    public static void setAdFrequencyCount(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.AdFrequencyCount, status);
        editor.apply();
    }

    public static void setActivityString(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.ActivityString, status);
        editor.apply();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sFontRegular = null;
        sFontold = null;
        sFontRegular = Typeface.createFromAsset(getAssets(), "montserrat_regular.ttf");
        sFontold = Typeface.createFromAsset(getAssets(), "montserrat_bold.ttf");
        StudId = Datastorage.GetStudentId(getApplicationContext());
        SchoolId = Datastorage.GetSchoolId(getApplicationContext());
    }

    public void setAlertZoom(final Context context, String imgUrl) {
        try {
            mBuilder = new AlertDialog.Builder(context);
            mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
            //   this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            photoView = (ImageZoom) mView.findViewById(R.id.imageView);
            ImageView closephotoView = mView.findViewById(R.id.closeimg);

            Glide.with(context)
                    .load(imgUrl).asBitmap().dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder).error(R.drawable.nopics).into(photoView);

            mBuilder.setView(mView);
            mBuilder.setCancelable(true);
            mDialog = mBuilder.create();
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.show();
            closephotoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Created By Vijay Solanki 27-01-2020

    public String getAdCount() {
        SharedPreferences preferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(Constants.adCurrentCount, "0");
    }

    public String getAdStatus() {
        SharedPreferences preferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(Constants.isShowAdd, "");
    }

    public String getSubmissionShow() {
        SharedPreferences preferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(Constants.ShowSubNew, "");
    }

    public String getLastVisiSyncHour() {
        SharedPreferences preferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(Constants.currentTime, "");
    }

    public String getAdFrequencyCount() {
        SharedPreferences preferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(Constants.AdFrequencyCount, "");
    }

    public String getActivityString() {
        SharedPreferences preferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(Constants.ActivityString, "");
    }

    public String getTodayDate() {
        String date = "";
        try {
            date = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    //0=no ad
    //1=banner ad
    //2=Full screen ad
    //3=both ad
    public void showFullScreenAd(final Context context, String activityName) {
        try {

            if (getAdStatus() != null && (getAdStatus().equalsIgnoreCase("3") ||
                    getAdStatus().equalsIgnoreCase("2"))) {
                String list = getActivityString();
                List<String> al = new ArrayList<String>();
                if (list != null && !list.isEmpty()) {
                    String str[] = list.split(",");
                    al = Arrays.asList(str);
                }
                Common.showLog("Name 106", activityName);
                if (al.contains(activityName) || al.size() < 1) {
                    int cnt = Integer.parseInt(getAdCount());
                    Constants.writelog("CountAdded109", String.valueOf(cnt));
                    if (cnt > Integer.parseInt(getAdFrequencyCount())) {
                        mInterstitialAd = new InterstitialAd(this);
                        MobileAds.initialize(this, Constants.INTERSTITIAL_ADD_UNIT);
                        mInterstitialAd.setAdUnitId(Constants.INTERSTITIAL_ADD_UNIT);
                        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                mInterstitialAd.show();
                                setCount(context, 0);
                                Constants.writelog("SuccesFully Loaded", "Ad Loaded");
                                // Code to be executed when an ad finishes loading.
                            }

                            @Override
                            public void onAdFailedToLoad(int errorCode) {
                                Constants.writelog("ad Loading falied with Error Code : 125", String.valueOf(errorCode));
                                // Code to be executed when an ad request fails.
                            }

                            @Override
                            public void onAdOpened() {
                                // Code to be executed when the ad is displayed.
                            }

                            @Override
                            public void onAdClicked() {
                                // Code to be executed when the user clicks on an ad.
                            }

                            @Override
                            public void onAdLeftApplication() {
                                // Code to be executed when the user has left the app.
                            }

                            @Override
                            public void onAdClosed() {
                                // Code to be executed when the interstitial ad is closed.
                            }
                        });
                    } else {
                        cnt++;
                        setCount(context, cnt);
                    }
                }
            } else {
                Constants.writelog("Response : 152 :", "Reason for not loading ad ,getAdStatus() = 0 ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showBannerAd(final AdView mAdView, String activityName) {
        try {
            db = new DatabaseHandler(getApplicationContext());
            if (db.ifExists(activityName, StudId, SchoolId)) {
                db.updateActivityCount(activityName, StudId, SchoolId);
            } else {
                db.addActivityVisitedCount(StudId, SchoolId, activityName);
            }

            if (getAdStatus() != null && (getAdStatus().equalsIgnoreCase("1") ||
                    getAdStatus().equalsIgnoreCase("3"))) {
                String list = getActivityString();
                List<String> al = new ArrayList<String>();
                if (list != null && !list.isEmpty()) {
                    String str[] = list.split(",");
                    al = Arrays.asList(str);
                }
                if (al.contains(activityName) || al.size() <= 1) {
                    mAdView.setVisibility(View.VISIBLE);
                    AdRequest adRequest = new AdRequest.Builder()
                            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                            .build();
                    mAdView.loadAd(adRequest);
                    mAdView.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            Constants.writelog("added Succesfully", "Done");
                            // Code to be executed when an ad finishes loading.
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            mAdView.setVisibility(View.GONE);
                            Constants.writelog("ad Loading falied with Error Code : 214", String.valueOf(errorCode)); // Code to be executed when an ad request fails.
                        }

                        @Override
                        public void onAdOpened() {
                            // Code to be executed when an ad opens an overlay that
                            // covers the screen.
                        }

                        @Override
                        public void onAdClicked() {
                            // Code to be executed when the user clicks on an ad.
                        }

                        @Override
                        public void onAdLeftApplication() {
                            // Code to be executed when the user has left the app.
                        }

                        @Override
                        public void onAdClosed() {
                            // Code to be executed when the user is about to return
                            // to the app after tapping on an ad.
                        }
                    });
                } else {
                    mAdView.setVisibility(View.GONE);
                }
            } else {
                mAdView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkpermissionstatus(int i, Context mContext, Activity activity) {
        try {
            mGlobalContext = mContext;
            if (Build.VERSION.SDK_INT >= 23) {
                Boolean isanyDeny = false;
                Boolean isadded = false;
                String permissions = "";
                int permissionStatus = 0;
                if (i == 2) {
                    permissionStatus = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                        isanyDeny = true;
                        isadded = true;
                        if (isadded) {
                            permissions += "," + Manifest.permission.WRITE_EXTERNAL_STORAGE;
                        } else {
                            permissions += Manifest.permission.WRITE_EXTERNAL_STORAGE;
                        }
                    }
                    String[] perm = permissions.split(",");
                    ActivityCompat.requestPermissions(activity, perm, 1);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:

            case 2:
                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Common.showToast(mGlobalContext, "Permission given Now you can download the files...");
                } else {
                    Common.showToast(mGlobalContext, "Permission Denied");
                }
                break;
        }
    }

    //Download File modified By Jameela 21-07-2020
    public void DownloadPdfOrImageFile(Context mContext, String Title, String Url) {
        String path = "";
        if (Url.endsWith(".pdf")) {
            path = Constants.FILE_PDF_PATH + File.separator + Title + ".pdf";
        } else {
            path = Constants.FILE_IMG_PATH + File.separator + Title + ".jpg";
        }
        Common.showLog("Path", Url);
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Url));
            request.setDescription("File Downloading");
            request.setTitle(Title);
            request.setShowRunningNotification(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (Url.endsWith(".pdf")) {
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, Title + ".pdf");
                } else {
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, Title + ".jpg");
                }
            } else {
                if (Url.endsWith(".pdf")) {
                    request.setDestinationInExternalPublicDir(SchoolDetails.PhotoGalleryFolder
                            + File.separator, "Pdf File/" + Title + ".pdf");
                } else {
                    request.setDestinationInExternalPublicDir(SchoolDetails.PhotoGalleryFolder
                            + File.separator, "Image/" + Title + ".jpg");
                }
            }
            DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
            Common.showToast(mContext, "Downloading Start");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File CreateDownloadPDFFolder() {
        File folderPath = null;
        try {
// Check External SD Card Present or Not
            Boolean IsSdPresent = CheckExternalSdCardPresent();
            if (IsSdPresent) {
                folderPath = new File(Constants.FILE_PDF_PATH);
            } else {
                folderPath = new File(Constants.FILE_PDF_PATH);
            }
            if (!folderPath.exists()) {
                folderPath.mkdirs();
            }
        } catch (Exception err) {
            Constants.writelog("Common", "CreateDownloadFolder() 102 From:" + "Ex:" + err.getMessage());
        }
        return folderPath;
    }


    public static File CreateDownloadAudioFolder() {
        File folderPath = null;
        try {
// Check External SD Card Present or Not
            Boolean IsSdPresent = CheckExternalSdCardPresent();
            if (IsSdPresent) {
                folderPath = new File(Constants.FILE_AUDIO_PATH);
            } else {
                folderPath = new File(Constants.FILE_AUDIO_PATH);
            }
            if (!folderPath.exists()) {
                folderPath.mkdirs();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return folderPath;
    }

    public static File CreateVideoDwonloadFolder() {
        File folderPath = null;
        try {
// Check External SD Card Present or Not
            Boolean IsSdPresent = CheckExternalSdCardPresent();
            if (IsSdPresent) {
                folderPath = new File(Constants.FILE_VIDEO_PATH);
            } else {
                folderPath = new File(Constants.FILE_AUDIO_PATH);
            }
            if (!folderPath.exists()) {
                folderPath.mkdirs();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return folderPath;
    }

    public static File CreatePracticeTestFolder(String testID) {
        File folderPath = null;
        try {
// Check External SD Card Present or Not
            Boolean IsSdPresent = CheckExternalSdCardPresent();
            if (IsSdPresent) {
                folderPath = new File(Constants.FILE_PRACTICETEST_PATH + File.separator + testID);
            } else {
                folderPath = new File(Constants.FILE_PRACTICETEST_PATH + File.separator + testID);
            }
            if (!folderPath.exists()) {
                folderPath.mkdirs();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return folderPath;
    }


    /**
     * Check Internet connection coding
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }


    public void initSocial(Activity activity, LinearLayout mView, String imgUrl, final String imgLink) {
        try {
            if (mView != null && imgUrl != null && !imgUrl.isEmpty()) {
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.adview_row, null);
                mView.addView(view);
                final ImageView img = (ImageView) mView.findViewById(R.id.adview);
                String[] parts = imgUrl.split("\\.");
                if (parts != null && parts.length > 0 && parts[parts.length - 1].equalsIgnoreCase("gif")) {
                    Glide.with(BaseActivity.this)
                            .load(imgUrl)
                            .asGif()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(img);
                } else {
                    Glide.with(BaseActivity.this)
                            .load(imgUrl)
                            .asBitmap().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(img);
                }

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (imgLink != null && !imgLink.isEmpty()) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(imgLink));
                            startActivity(i);
                        }
                    }
                });
            } else {
                mView.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    /**
     * Closes the open keyboard if exist in focus
     */
    public void hideKeyboard(Activity activity) {
        try {
            InputMethodManager keyboard = (InputMethodManager) activity.getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null) {
                IBinder iBinder = activity.getCurrentFocus().getWindowToken();
                if (iBinder != null && keyboard != null)
                    keyboard.hideSoftInputFromWindow(iBinder, 0);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Closes the open keyboard if exist in focus
     */
    public void hideKeyboardInDialog(final View caller) {
        try {
            caller.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) caller.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(caller.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }, 100);
        } catch (Exception exception) {
            Common.printStackTrace(exception);
        }
    }

    public String convertDate(Context context, String dateToBeConverted) {
        String formattedDate = "";
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", locale);
        DateFormat targetFormat = new SimpleDateFormat("dd MMM, yyyy", locale);
        try {
            Date dateConverted = originalFormat.parse(dateToBeConverted);
            formattedDate = targetFormat.format(dateConverted);
        } catch (Exception exception) {
            Constants.writelog(context.getClass().getSimpleName() + " convertDate", exception.toString());
            try {
                formattedDate = targetFormat.format(targetFormat);
            } catch (Exception ex) {
                Common.printStackTrace(ex);
            }
        }
        return formattedDate;
    }

    public String convertDate_v1(Context context, String dateToBeConverted) {
        String formattedDate = "";
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", locale);
        DateFormat targetFormat = new SimpleDateFormat("dd MMM, yyyy", locale);
        try {
            Date dateConverted = originalFormat.parse(dateToBeConverted);
            formattedDate = targetFormat.format(dateConverted);
        } catch (Exception exception) {
            Constants.writelog(context.getClass().getSimpleName() + " convertDate", exception.toString());
            try {
                formattedDate = targetFormat.format(targetFormat);
            } catch (Exception ex) {
                Common.printStackTrace(ex);
            }
        }
        return formattedDate;
    }

    public void accountListClick(Activity mContext) {
        try {
            Intent intent = new Intent(mContext, AccountListActivity.class);
            startActivity(intent);
            mContext.finish();
            onClickAnimation();
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    public void addAccountClick(Activity mContext) {
        try {
            Constants.googleAnalyticEvent(mContext, Constants.button_click, "AddAccountActivity");
            Intent intent = new Intent(mContext, AddAccountActivity.class);
            startActivity(intent);
            mContext.finish();
            onClickAnimation();
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    public void removeAccountClick(Activity mContext) {
        try {
            Constants.googleAnalyticEvent(mContext, Constants.button_click, "RemoveAccount");
            Intent intent = new Intent(mContext, AccountListRemoveActivity.class);
            startActivity(intent);
            mContext.finish();
            onClickAnimation();
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    public String addMinTocurrentTime(int min) {
        Calendar date1 = Calendar.getInstance();
        long t1 = date1.getTimeInMillis();
        Date afterAddingTenMins = new Date(t1 + (min * ONE_MINUTE_IN_MILLIS));
        String fDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(afterAddingTenMins);
        return fDate;
    }

    public String addMinToServerTime(int min, String serverTime) {
        Calendar date1 = Calendar.getInstance();
        //   Calendar calGiven = Calendar.getInstance();
        SimpleDateFormat sdfGiven = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        String splitDateTime[] = serverTime.split(" ");
        String splitDate[] = splitDateTime[0].split("-");
        String splitTime[] = splitDateTime[1].split(":");
        date1.set(Calendar.MONTH, Integer.parseInt(splitDate[1]) - 1);
        date1.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[0]));
        date1.set(Calendar.YEAR, Integer.parseInt(splitDate[2]));
        date1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTime[0]));
        date1.set(Calendar.MINUTE, Integer.parseInt(splitTime[1]));
        date1.set(Calendar.SECOND, Integer.parseInt(splitTime[2]));

        long t1 = date1.getTimeInMillis();
        Date afterAddingTenMins = new Date(t1 + (min * ONE_MINUTE_IN_MILLIS));
        String fDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(afterAddingTenMins);
        return fDate;
    }

    public void setDefaultAccount(Activity mContext) {
        try {
            Intent intent = new Intent(mContext, AccountListActivity.class);
            startActivity(intent);
            mContext.finish();
            onClickAnimation();
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    public void onClickAnimation() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onBackClickAnimation() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
