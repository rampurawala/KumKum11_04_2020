package com.expedite.apps.kumkum.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.PollTaskListModel;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyPollTaskActivity extends BaseActivity implements View.OnClickListener {
    private ProgressBar mProgressBar;
    private TextView mPollTitle, mPollDescriprion, mBtnOk, mBtnSkip, mBtnSkipAll;
    private ImageView mBtnClosed, mImgServey;
    private RadioGroup mRadioGroupType;
    private String Year_Id = "", SchoolId = "", StudentId = "", ClassSecId = "";
    private String tag = "SurveyPollTaskActivity";
    private ArrayList<PollTaskListModel.SurveyList> mSrveyArrayList = new ArrayList<>();
    private ArrayList<PollTaskListModel.SurveyOption> mSrveyOptionArrayList = new ArrayList<>();
    private String mSelectedOptionId = "", mSelectedSurveyId = "", mCompletedSurveyId = "",
            mAvoidText = "";
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_poll_task_layout);

        init();
        initListner();
        getSurveyPollTask();
    }

    public void init() {
        mAdView = (AdView) findViewById(R.id.adView);
        showBannerAd(mAdView, ActivityNames.SurveyPollTaskActivity);
        Activity abc = this;
        Constants.setActionbar(getSupportActionBar(), abc, getApplicationContext(),
                "Survey Poll", "Survey Poll", ActivityNames.SurveyPollTaskActivity);
        SchoolId = Datastorage.GetSchoolId(SurveyPollTaskActivity.this);
        StudentId = Datastorage.GetStudentId(SurveyPollTaskActivity.this);
        ClassSecId = Datastorage.GetClassSecId(SurveyPollTaskActivity.this);
        Year_Id = Datastorage.GetCurrentYearId(SurveyPollTaskActivity.this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mPollTitle = (TextView) findViewById(R.id.PollTitle);
        mPollDescriprion = (TextView) findViewById(R.id.PollDescriprion);
        mImgServey = (ImageView) findViewById(R.id.imgServey);
        mBtnClosed = (ImageView) findViewById(R.id.BtnClosed);
        mRadioGroupType = (RadioGroup) findViewById(R.id.radioGroupType);
        mBtnOk = (TextView) findViewById(R.id.BtnOk);
        mBtnSkip = (TextView) findViewById(R.id.BtnSkip);
        mBtnSkipAll = (TextView) findViewById(R.id.BtnSkipAll);
    }

    private void initListner() {
        mBtnOk.setOnClickListener(this);
        mBtnSkip.setOnClickListener(this);
        mBtnClosed.setOnClickListener(this);
        mBtnSkipAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.BtnClosed:
                try {
                    showSurveyPollConfirmDialog(mAvoidText);
                } catch (Exception ex) {
                    Common.printStackTrace(ex);
                }
                break;
            case R.id.BtnOk:
                try {
                    if (isOnline()) {
                        if (mSelectedOptionId == null || mSelectedOptionId.isEmpty()) {
                            Common.showToast(SurveyPollTaskActivity.this, "Please select atleast one option.");
                        } else {
                            getSaveSurveyPollTask();
                        }
                    } else {
                        Common.showToast(SurveyPollTaskActivity.this,
                                getString(R.string.msg_connection));
                    }
                } catch (Exception ex) {
                    Common.printStackTrace(ex);
                }
                break;
            case R.id.BtnSkip:
                try {
                    if (isOnline()) {
                        mSelectedOptionId = "-1";
                        getSaveSurveyPollTask();
                    } else {
                        Common.showToast(SurveyPollTaskActivity.this,
                                getString(R.string.msg_connection));
                    }

                } catch (Exception ex) {
                    Common.printStackTrace(ex);
                }
                break;
            case R.id.BtnSkipAll:
                try {
                    Intent intent = new Intent(SurveyPollTaskActivity.this, HomeActivity.class);
                    startActivity(intent);
                    SurveyPollTaskActivity.this.finish();
                    onClickAnimation();
                } catch (Exception ex) {
                    Common.printStackTrace(ex);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SurveyPollTaskActivity.this.finish();
                onBackClickAnimation();
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        SurveyPollTaskActivity.this.finish();
        onBackClickAnimation();
    }

    private void surveyPollOption(int Count) {
        try {
            if (Count > 0) {
                mRadioGroupType.removeAllViews();
                RadioButton[] rgb = new RadioButton[mSrveyOptionArrayList.size()];
                for (int i = 0; i < rgb.length; i++) {
                    PollTaskListModel.SurveyOption tmp = mSrveyOptionArrayList.get(i);
                    LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                    rgb[i] = new RadioButton(this);
                    rgb[i].setLayoutParams(layoutParams);
                    if (tmp.getOptionAns() != null && !tmp.getOptionAns().isEmpty()) {
                        rgb[i].setText(tmp.getOptionAns());
                    } else {
                        rgb[i].setText("");
                    }
                    rgb[i].setTextSize(16);
                    rgb[i].setTextColor(getResources().getColor(R.color.textbg));
                    rgb[i].setGravity(Gravity.TOP);
                    rgb[i].setButtonDrawable(R.drawable.btn_radio);
                    rgb[i].setPadding(pxFromDp(8), 0, pxFromDp(8), pxFromDp(8));
                    rgb[i].setTag(tmp);
                    try {
                        if (tmp.getUrl() != null && tmp.getUrl().isEmpty()) {
                            Drawable mData = drawableFromUrl(tmp.getUrl());
                            if (mData != null) {
                                rgb[i].setCompoundDrawablePadding(5);
                                rgb[i].setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                                        drawableFromUrl(tmp.getUrl()));
                            } else {
                                rgb[i].setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                                        null);
                            }
                        } else {
                            rgb[i].setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                                    null);
                        }
                    } catch (Exception ex) {
                        Common.printStackTrace(ex);
                        rgb[i].setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                                null);
                    }

                    if (android.os.Build.VERSION.SDK_INT < 17) {
                        int padding = (int) (30f * SurveyPollTaskActivity.this.getResources()
                                .getDisplayMetrics().density);
                        rgb[i].setPadding(rgb[i].getPaddingLeft() + padding, 0 /*rgb[i]
                        .getPaddingTop()*/, rgb[i].getPaddingRight(), rgb[i].getPaddingBottom());
                    }
                    try {
                        rgb[i].setId(Integer.valueOf(tmp.getOptionId()));
                    } catch (NumberFormatException e) {
                        rgb[i].setId(0);
                    }
                    mRadioGroupType.addView(rgb[i]);
                    mRadioGroupType
                            .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(
                                        RadioGroup mRadioGroup, int checkedId) {
                                    for (int i = 0; i < mRadioGroup
                                            .getChildCount(); i++) {
                                        if (mRadioGroup.getChildAt(i) instanceof RadioButton) {
                                            RadioButton btn = (RadioButton) mRadioGroup
                                                    .getChildAt(i);
                                            if (btn.getId() == checkedId) {
                                                mSelectedOptionId = String.valueOf(checkedId);
                                                return;
                                            } else {
                                                mSelectedOptionId = "";
                                            }
                                        }
                                    }
                                }
                            });
                }
            }

        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    public int pxFromDp(int dp) {
        int px = Math.round(dp * getResources().getDisplayMetrics().density);
        return px;
    }

    private void showSurveyPollConfirmDialog(String message) {
        try {
            final Dialog mDialogView = new Dialog(SurveyPollTaskActivity.this);
            mDialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialogView.setContentView(R.layout.survey_poll_confirm_dialog_layout);
            mDialogView.getWindow().setBackgroundDrawable(new
                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            // mProgressBar = (ProgressBar) mDialogView.findViewById(R.id.progressBar);
            TextView txtMessage = (TextView) mDialogView.findViewById(R.id.txtMessage);
            txtMessage.setText(message);

            TextView btnOk = (TextView) mDialogView.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isOnline()) {
                        hideKeyboard(SurveyPollTaskActivity.this);
                        mDialogView.dismiss();
                        mSelectedOptionId = "0";
                        getSaveSurveyPollTask();
                    } else {
                        Common.showToast(SurveyPollTaskActivity.this, getString(R.string.msg_connection));
                    }
                }
            });
            TextView btnCancel = (TextView) mDialogView.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard(SurveyPollTaskActivity.this);
                    mDialogView.dismiss();
                }
            });


            mDialogView.show();
        } catch (NullPointerException ex) {
            Common.printStackTrace(ex);
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    private void getSurveyPollTask() {
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            try {
                // http://app.vayuna.com/service.asmx/SurveyPollList?schoolId=8414&class_section_Id=32&studId=47&year_id=5
                Call<PollTaskListModel> call = ((MyApplication) getApplicationContext())
                        .getmRetrofitInterfaceAppService().GetSurveyPollTask(SchoolId, ClassSecId, StudentId, Year_Id
                                , Constants.PLATFORM);

                call.enqueue(new Callback<PollTaskListModel>() {
                    @Override
                    public void onResponse(Call<PollTaskListModel> call, Response<PollTaskListModel> response) {
                        try {
                            PollTaskListModel tmp = response.body();
                            if (tmp != null && tmp.getPollarrays() != null && tmp.getPollarrays().get(0).getResponse() != null
                                    && !tmp.getPollarrays().get(0).getResponse().isEmpty()
                                    && tmp.getPollarrays().get(0).getResponse().equalsIgnoreCase("1")) {
                                if (tmp.getPollarrays().get(0).getSurveyList() != null &&
                                        tmp.getPollarrays().get(0).getSurveyList().size() > 0) {
                                    mSrveyArrayList.clear();
                                    mSrveyArrayList.addAll(tmp.getPollarrays().get(0).getSurveyList());
                                    SetSurveyPollTaskData();
                                }
                            } else if (tmp != null && tmp.getPollarrays() != null && tmp.getPollarrays().get(0).getResponse() != null
                                    && !tmp.getPollarrays().get(0).getResponse().isEmpty()
                                    && tmp.getPollarrays().get(0).getResponse().equalsIgnoreCase("0")) {
                                Intent intent = new Intent(SurveyPollTaskActivity.this, HomeActivity.class);
                                startActivity(intent);
                                SurveyPollTaskActivity.this.finish();
                                onClickAnimation();
                            }
                            mProgressBar.setVisibility(View.GONE);
                        } catch (Exception ex) {
                            Constants.writelog(tag, "Exception_113:" + ex.getMessage()
                                    + "::::::" + ex.getStackTrace());
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<PollTaskListModel> call, Throwable t) {
                        Constants.writelog(tag, "Exception_113:" + t.getMessage());
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

            } catch (Exception ex) {
                mProgressBar.setVisibility(View.GONE);
                Constants.writelog(tag, "Exception_113:" + ex.getMessage()
                        + "::::::" + ex.getStackTrace());
            }
        } else {
            Common.showToast(SurveyPollTaskActivity.this, getString(R.string.msg_connection));
        }
    }

    private void getSaveSurveyPollTask() {
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            try {
                // http://app.vayuna.com/service.asmx/SurveyAnsSave?schoolId=8414&class_section_Id=32
                // &studId=47&surveyId=&ans=&filterids=
                mCompletedSurveyId += mSelectedSurveyId + ",";
                Call<PollTaskListModel> call = ((MyApplication) getApplicationContext())
                        .getmRetrofitInterfaceAppService().GetSurveyPollTaskSave(SchoolId,
                                ClassSecId, StudentId, mSelectedSurveyId, mSelectedOptionId,
                                removeLastComma(mCompletedSurveyId), Constants.PLATFORM);

                call.enqueue(new Callback<PollTaskListModel>() {
                    @Override
                    public void onResponse(Call<PollTaskListModel> call, Response<PollTaskListModel> response) {
                        try {
                            PollTaskListModel tmp = response.body();
                            if (tmp != null && tmp.getPollarrays() != null && tmp.getPollarrays().get(0).getResponse() != null
                                    && !tmp.getPollarrays().get(0).getResponse().isEmpty()
                                    && tmp.getPollarrays().get(0).getResponse().equalsIgnoreCase("1")) {
                                if (tmp.getPollarrays().get(0).getSurveyList() != null &&
                                        tmp.getPollarrays().get(0).getSurveyList().size() > 0) {
                                    if (tmp.getPollarrays().get(0).getMessage() != null &&
                                            !tmp.getPollarrays().get(0).getMessage().isEmpty()) {
                                        Common.showToast(SurveyPollTaskActivity.this,
                                                tmp.getPollarrays().get(0).getMessage());
                                    }
                                    mSrveyArrayList.clear();
                                    mSrveyArrayList.addAll(tmp.getPollarrays().get(0).getSurveyList());
                                    SetSurveyPollTaskData();
                                }
                            } else {
                                Intent intent = new Intent(SurveyPollTaskActivity.this, HomeActivity.class);
                                startActivity(intent);
                                SurveyPollTaskActivity.this.finish();
                                onClickAnimation();
                            }
                            mProgressBar.setVisibility(View.GONE);
                        } catch (Exception ex) {
                            Constants.writelog(tag, "Exception_113:" + ex.getMessage()
                                    + "::::::" + ex.getStackTrace());
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<PollTaskListModel> call, Throwable t) {
                        Constants.writelog(tag, "Exception_113:" + t.getMessage());
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

            } catch (Exception ex) {
                mProgressBar.setVisibility(View.GONE);
                Constants.writelog(tag, "Exception_113:" + ex.getMessage()
                        + "::::::" + ex.getStackTrace());
            }
        } else {
            Common.showToast(SurveyPollTaskActivity.this, getString(R.string.msg_connection));
        }
    }

    public void SetSurveyPollTaskData() {
        if (mSrveyArrayList != null && mSrveyArrayList.size() > 0 &&
                mSrveyArrayList.get(0).getSurveyOptions() != null &&
                mSrveyArrayList.get(0).getSurveyOptions().size() > 0) {

            mSrveyOptionArrayList.clear();
            mSrveyOptionArrayList.addAll(mSrveyArrayList.get(0).getSurveyOptions());
            surveyPollOption(mSrveyOptionArrayList.size());
            mSelectedSurveyId = "";
            mSelectedSurveyId = mSrveyArrayList.get(0).getSurveyId();

            if (mSrveyArrayList.get(0).getSurveyQue() != null &&
                    !mSrveyArrayList.get(0).getSurveyQue().isEmpty()) {
                mPollTitle.setText(mSrveyArrayList.get(0).getSurveyQue());
            } else {
                mPollTitle.setText("");
            }
            if (mSrveyArrayList.get(0).getDescription() != null &&
                    !mSrveyArrayList.get(0).getDescription().isEmpty()) {
                mPollDescriprion.setVisibility(View.VISIBLE);
                mPollDescriprion.setText(mSrveyArrayList.get(0).getDescription());
            } else {
                mPollDescriprion.setVisibility(View.GONE);
            }
            if (mSrveyArrayList.get(0).getUrl() != null &&
                    !mSrveyArrayList.get(0).getUrl().isEmpty()) {
                mImgServey.setVisibility(View.VISIBLE);
                Glide.with(SurveyPollTaskActivity.this)
                        .load(mSrveyArrayList.get(0).getUrl())
                        .asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.nopics)
                        .into(mImgServey);
            } else {
                mImgServey.setVisibility(View.GONE);
            }
            if (mSrveyArrayList.get(0).getAllowToAvoidText() != null &&
                    !mSrveyArrayList.get(0).getAllowToAvoidText().isEmpty()) {
                mAvoidText = mSrveyArrayList.get(0).getAllowToAvoidText();
            } else {
                mAvoidText = "";
            }
            if (mSrveyArrayList.get(0).getAllowToAvoid() != null &&
                    !mSrveyArrayList.get(0).getAllowToAvoid().isEmpty() &&
                    mSrveyArrayList.get(0).getAllowToAvoid().equalsIgnoreCase("1")) {
                mBtnClosed.setVisibility(View.VISIBLE);
            } else {
                mBtnClosed.setVisibility(View.GONE);
            }
            if (mSrveyArrayList.get(0).getAllowToSkip() != null &&
                    !mSrveyArrayList.get(0).getAllowToSkip().isEmpty() &&
                    mSrveyArrayList.get(0).getAllowToSkip().equalsIgnoreCase("1")) {
                mBtnSkip.setVisibility(View.VISIBLE);
            } else {
                mBtnSkip.setVisibility(View.GONE);
            }

            ((View) findViewById(R.id.llMainSurveyLayout)).setVisibility(View.VISIBLE);
        } else {
            ((View) findViewById(R.id.llMainSurveyLayout)).setVisibility(View.GONE);
        }
    }

    public static String removeLastComma(String str) {
        try {
            if (str == null || str.length() == 0) {
                return str = "";
            } else {
                str = str.replaceAll("(,)+", ",");
                if (str.endsWith(",")) {
                    return str.substring(0, str.length() - 1);
                } else {
                    return str;
                }
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
            return str;
        }
    }

    public static Drawable drawableFromUrl(String url) throws IOException {
        try {
            Bitmap bitmap;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
            return new BitmapDrawable(bitmap);
        } catch (IOException ex) {
            Common.printStackTrace(ex);
            return null;
        }
    }
}
