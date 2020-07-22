package com.expedite.apps.kumkum.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.adapter.ProfileListAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.lazylist.ImageLoader1;
import com.google.android.gms.ads.AdView;

import org.ksoap2.serialization.SoapObject;

public class SchoolMatesListActivity extends BaseActivity {
    private TextView tv;
    private ImageView picId;
    private ListView lstview;
    private String[] StudData = {}, parts = {}, Lable = {};
    private String SchoolId, LastUpdatedTime, studid = "", Stud_Image_Path = "";
    private ProgressBar mProgressBar;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_scool_mates);
        init();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.SchoolMatesListActivity);
            Constants.setActionbar(getSupportActionBar(), SchoolMatesListActivity.this, SchoolMatesListActivity.this,
                    "ViewSchoolMateProfile", "ViewSchoolMateProfile", ActivityNames.SchoolMatesListActivity);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

            SchoolId = Datastorage.GetSchoolId(SchoolMatesListActivity.this);
            LastUpdatedTime = Datastorage.GetLastUpdatedtime(SchoolMatesListActivity.this);
            db = new DatabaseHandler(SchoolMatesListActivity.this);
            tv = (TextView) findViewById(R.id.tvmarkqueetextschoolmatesprofile);
            tv.setSelected(true);
            tv.setText(LastUpdatedTime);

            lstview = (ListView) findViewById(R.id.lstmessages);
            picId = (ImageView) findViewById(R.id.imgbullet);
            Intent intnt = getIntent();
            studid = intnt.getStringExtra("StudId");
            // Stud_Id = Integer.parseInt(str);

            new MyTask().execute();
        } catch (Exception err) {
            Constants.Logwrite("viewprofileMate", "error::14" + err.getMessage());
        }
    }

    public Bitmap processBitmap(Bitmap bitmap) {
        int pixels = 0;
        int mRound = 0;
        if (mRound == 0)
            pixels = 120;
        else
            pixels = mRound;
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);

        BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP,
                TileMode.CLAMP);
        paint.setColor(color);
        paint.setShader(shader);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private void showprofilekumkum() {

        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.SHOW_PROFILE_KUMKUM);
        request.addProperty("stud_id", studid);
        request.addProperty("school_id", SchoolId);
        request.addProperty("Is_All", 0);

        try {
            SoapObject result = Constants.CallWebMethod(
                    SchoolMatesListActivity.this, request, Constants.SHOW_PROFILE_KUMKUM, true);
            if (result != null && result.getPropertyCount() > 0) {
                String info = result.getPropertyAsString(0);
                parts = info.split(",");

                Lable = new String[parts.length];
                StudData = new String[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    String[] data = parts[i].split(":");
                    Lable[i] = data[0].toString();
                    StudData[i] = data[1].toString();
                }
                Stud_Image_Path = getStudentImagePath();
            } else {

            }
        } catch (Exception e) {
            Common.showLog("SchoolMatesListActivity:203", e.getMessage());
        }
    }

    private void LogUserVisted() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.LOG_USER_VISITED);
        request.addProperty("moduleid", 4);
        request.addProperty("schoolid", SchoolId);
        request.addProperty("User_Id", Datastorage.GetUserId(SchoolMatesListActivity.this));
        request.addProperty("phoneno", Datastorage.GetPhoneNumber(SchoolMatesListActivity.this));
        request.addProperty("pageid", 11);
        try {
            Constants.CallWebMethod(SchoolMatesListActivity.this, request,
                    Constants.LOG_USER_VISITED, false);

        } catch (Exception e) {
            Common.showLog("SchoolMatesListActivity:219", e.getMessage());
        }
    }

    private String getStudentImagePath() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_STUDENT_IMAGE_PATH_V1);
        request.addProperty("school_id", SchoolId);
        request.addProperty("Studid", Integer.parseInt(studid));
        try {
            SoapObject result = Constants.CallWebMethod(
                    SchoolMatesListActivity.this, request, Constants.GET_STUDENT_IMAGE_PATH_V1, false);
            if (result != null && result.getPropertyCount() > 0) {
                String info = result.getPropertyAsString(0);
                Stud_Image_Path = info;
            }
        } catch (Exception e) {
            Common.showLog("SchoolMatesListActivity:236", e.getMessage());
        }
        return Stud_Image_Path;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int iid = item.getItemId();
        if (iid == android.R.id.home) {
            hideKeyboard(SchoolMatesListActivity.this);
            SchoolMatesListActivity.this.finish();
            onBackClickAnimation();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SchoolMatesListActivity.this.finish();
        onBackClickAnimation();
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            LogUserVisted();
            showprofilekumkum();
            Stud_Image_Path.replaceAll(" ", "%20");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(SchoolMatesListActivity.this);
                } else {
                    int loader = R.drawable.nopics;
                    try {
                        String image_url = Stud_Image_Path;
                        ImageLoader1 imgLoader = new ImageLoader1(SchoolMatesListActivity.this);
                        imgLoader.DisplayImage(image_url, loader, picId);
                    } catch (Exception ex) {
                        Constants.writelog("ViewProfileSchoolMates", "MSG: photo not found:-" + ex.getMessage());
                    }
                    if (StudData != null && StudData.length > 0) {
                        ProfileListAdapter adapter = new ProfileListAdapter(SchoolMatesListActivity.this, Lable, StudData);
                        lstview.setAdapter(adapter);
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(SchoolMatesListActivity.this).create();
                        alertDialog.setTitle("Information");
                        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                        alertDialog.setIcon(R.drawable.information);
                        alertDialog.setCancelable(false);
                        alertDialog.setButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(SchoolMatesListActivity.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                        onBackClickAnimation();
                                    }
                                });
                        // Showing Alert Message
                        alertDialog.show();
                        return;
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                mProgressBar.setVisibility(View.GONE);
                Constants.Logwrite("Error", "Error msg : " + err.getMessage() + " StackTrace : " + err.getStackTrace());
            }
        }

    }
}
