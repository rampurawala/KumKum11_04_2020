package com.expedite.apps.kumkum.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.adapter.CalendatImageDataAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.CalendarImageListModel;

import java.util.ArrayList;
import java.util.List;


public class CalendarDataDetailActivity extends BaseActivity {
    private TextView txtTitle, txtDate, txtDescription;
    private String mTitle = "", mDate = "", mDescription = "", mImageUrl = "",isNotification;
    private RecyclerView mEventRecylerview;
    private ArrayList<CalendarImageListModel> mCalendarImageArrayList = new ArrayList<>();
    private CalendatImageDataAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_calendar_detail);

        if (getIntent() != null) {
            mTitle = getIntent().getExtras().getString("Title", "");
            mDate = getIntent().getExtras().getString("Date", "");
            mDescription = getIntent().getExtras().getString("Description", "");
            mImageUrl = getIntent().getExtras().getString("ImageUrl", "");
            isNotification = getIntent().getExtras().getString("IsNotification", "");
        }
        init();
    }

    public void init() {
        Constants.setActionbar(getSupportActionBar(), CalendarDataDetailActivity.this,
                CalendarDataDetailActivity.this,
                "Calendar Detail", "Calendar Detail", ActivityNames.CalendarDataDetailActivity);


        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        if (mTitle != null && !mTitle.isEmpty())
            txtTitle.setText(Html.fromHtml(mTitle));
        if (mDate != null && !mDate.isEmpty())
            txtDate.setText(Html.fromHtml(mDate));
        if (mDescription != null && !mDescription.isEmpty())
            txtDescription.setText(Html.fromHtml(mDescription));
        if (mImageUrl != null && !mImageUrl.isEmpty()) {
            try {
                String[] imageDetailList = mImageUrl.split("#url#");
                for (int i = 0; i < imageDetailList.length; i++) {
                    String[] imageDetail = imageDetailList[i].split("####");
                    if (!imageDetail[0].equalsIgnoreCase("false")) {
                        CalendarImageListModel mdata = new CalendarImageListModel();
                        if (imageDetail[0] != null && !imageDetail[0].isEmpty()) {
                            mdata.setImageUrl(imageDetail[0]);
                            mCalendarImageArrayList.add(mdata);
                        }
                    }
                }
            } catch (Exception ex) {
                Common.printStackTrace(ex);
            }
            ScrollAnimation((int) ((View) findViewById(R.id.ll_mainNestedScroll)).getX(),
                    (int) ((View) findViewById(R.id.ll_mainNestedScroll)).getTop());
        }
        mEventRecylerview = (RecyclerView) findViewById(R.id.imageRecyclerview);
        GridLayoutManager mGridLayoutManager1 = new GridLayoutManager(CalendarDataDetailActivity.this, 1);
        mEventRecylerview.setLayoutManager(mGridLayoutManager1);
        mAdapter = new CalendatImageDataAdapter(CalendarDataDetailActivity.this, mCalendarImageArrayList);
        mEventRecylerview.setAdapter(mAdapter);
        mEventRecylerview.setNestedScrollingEnabled(false);
    }


    @Override
    public void onBackPressed() {
        Intent intent=null;
        if (isNotification != null && !isNotification.isEmpty()) {
            super.onBackPressed();
            intent = new Intent(CalendarDataDetailActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            onBackClickAnimation();

        } else {
            ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
            if (taskList.get(0).numActivities == 1 &&
                    taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
                CalendarDataDetailActivity.this.finish();
            } else {
                intent = new Intent(CalendarDataDetailActivity.this, DailyDiaryCalanderActivityNew.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("msgtype", Constants.CALENDER);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                finish();
            }
        }

        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    hideKeyboard(CalendarDataDetailActivity.this);
                    CalendarDataDetailActivity.this.finish();
                    onBackClickAnimation();
                    break;
                default:
                    break;
            }

        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        return super.onOptionsItemSelected(item);
    }

    public void ScrollAnimation(int x, int y) {
        try {
            ObjectAnimator xTranslate = ObjectAnimator.ofInt(((View) findViewById(R.id.ll_mainNestedScroll)), "scrollX", x);
            ObjectAnimator yTranslate = ObjectAnimator.ofInt(((View) findViewById(R.id.ll_mainNestedScroll)), "scrollY", y);

            AnimatorSet animators = new AnimatorSet();
            animators.setDuration(1000L);
            animators.playTogether(xTranslate, yTranslate);
            animators.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator arg0) {
                }

                @Override
                public void onAnimationRepeat(Animator arg0) {
                }

                @Override
                public void onAnimationEnd(Animator arg0) {
                }

                @Override
                public void onAnimationCancel(Animator arg0) {
                }
            });
            animators.start();
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }
}
