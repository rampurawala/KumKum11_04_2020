package com.expedite.apps.kumkum.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.BuildConfig;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.adapter.ParentsProfileListAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.LoginDetail;
import com.expedite.apps.kumkum.model.ParentsProfileListModel;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ParentsProfileActivity extends BaseActivity {
    private RecyclerView mParentsRecyclerView;
    private ArrayList<ParentsProfileListModel.ParentsProfileList> mProfileArrayList = new ArrayList<>();
    private TextView tv;
    private ImageView mImgMother, mImgFather;
    private ParentsProfileListAdapter adapter;
    private Button btnUpdate;
    private String StudentId = "", SchoolId = "";
    private ProgressBar mProgressBar;
    private View mLlPhotosView;
    private AdView mAdView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_view_profile);

        if (getIntent() != null && getIntent().getExtras() != null) {
            StudentId = getIntent().getExtras().getString("StudentId", "");
            SchoolId = getIntent().getExtras().getString("SchoolId", "");
        }

        init();
        getParentsDetailList();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.ParentsProfileActivity);
            Constants.setActionbar(getSupportActionBar(), ParentsProfileActivity.this,
                    ParentsProfileActivity.this,
                    "Parents Profile", "ParentsProfile", ActivityNames.ParentsProfileActivity);
            tv = (TextView) findViewById(R.id.tvmarkqueetextschoolmatesprofile);
            tv.setSelected(true);
            //tv.setText(LoginDetail.getLastUpdatedTime());
            if (LoginDetail.getLastUpdatedTime() == null || LoginDetail.getLastUpdatedTime() == ""
                    || LoginDetail.getLastUpdatedTime().isEmpty()) {
                tv.setHeight(0);
            } else {
                tv.setText(LoginDetail.getLastUpdatedTime());
            }
            btnUpdate = (Button) findViewById(R.id.btnUpdateProfile);
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getParentsDetailList();
                }
            });
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            mLlPhotosView = (View) (findViewById(R.id.llPhotosView));
            mImgFather = (ImageView) findViewById(R.id.imgbullet);
            mImgMother = (ImageView) findViewById(R.id.imgbullet1);
            mParentsRecyclerView = (RecyclerView) findViewById(R.id.parentsRecyclerView);
            mParentsRecyclerView.setLayoutManager(new GridLayoutManager(ParentsProfileActivity.this, 1));
            adapter = new ParentsProfileListAdapter(ParentsProfileActivity.this, mProfileArrayList);
            mParentsRecyclerView.setAdapter(adapter);

        } catch (Exception err) {
            err.printStackTrace();
        }

    }

    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        try {
            // getMenuInflater().inflate(R.menu.profile_menu, menu);
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int iid = item.getItemId();
        if (iid == android.R.id.home) {
            hideKeyboard(ParentsProfileActivity.this);
            ParentsProfileActivity.this.finish();
            onBackClickAnimation();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
            onBackClickAnimation();
        } catch (Exception err) {
            Constants.writelog("Viewprofile:", "OnBackPressed()625 Exception:" + err.getMessage()
                    + "StackTrace::" + err.getStackTrace().toString());
        }
    }

    private void getParentsDetailList() {
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            Call<ParentsProfileListModel> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                    .GetParentsProfile(StudentId, SchoolId, BuildConfig.VERSION_CODE + "", Constants.PLATFORM);
            call.enqueue(new Callback<ParentsProfileListModel>() {
                @Override
                public void onResponse(Call<ParentsProfileListModel> call, Response<ParentsProfileListModel> response) {
                    try {
                        ParentsProfileListModel tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1")) {
                            if (tmps.getMessage() != null && !tmps.getMessage().isEmpty()) {
                                tv.setText(Html.fromHtml(tmps.getMessage()));
                            }
                            if (tmps.getProfileList() != null && tmps.getProfileList().size() > 0) {
                                mProfileArrayList.clear();
                                for (int i = 0; i < tmps.getProfileList().size(); i++) {
                                    if (tmps.getProfileList().get(i).getIsImageUrl() != null &&
                                            !tmps.getProfileList().get(i).getIsImageUrl().isEmpty() &&
                                            tmps.getProfileList().get(i).getIsImageUrl().equalsIgnoreCase("1")) {
                                        mLlPhotosView.setVisibility(View.VISIBLE);
                                        if (tmps.getProfileList().get(i).getTitle() != null &&
                                                !tmps.getProfileList().get(i).getTitle().isEmpty() &&
                                                tmps.getProfileList().get(i).getTitle().equalsIgnoreCase("Father Photo :")) {
                                            if (tmps.getProfileList().get(i).getDetail() != null &&
                                                    !tmps.getProfileList().get(i).getDetail().isEmpty()) {
                                                mImgFather.setVisibility(View.VISIBLE);
                                                Glide.with(ParentsProfileActivity.this)
                                                        .load(tmps.getProfileList().get(i).getDetail()).asBitmap().dontAnimate()
                                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                        .placeholder(R.drawable.profile1)
                                                        .error(R.drawable.profile1)
                                                        .into(mImgFather);
                                            } else {
                                                mImgFather.setVisibility(View.GONE);
                                            }
                                        }
                                        if (tmps.getProfileList().get(i).getTitle() != null &&
                                                !tmps.getProfileList().get(i).getTitle().isEmpty() &&
                                                tmps.getProfileList().get(i).getTitle().equalsIgnoreCase("Mother Photo :")) {
                                            if (tmps.getProfileList().get(i).getDetail() != null &&
                                                    !tmps.getProfileList().get(i).getDetail().isEmpty()) {
                                                mImgMother.setVisibility(View.VISIBLE);
                                                Glide.with(ParentsProfileActivity.this)
                                                        .load(tmps.getProfileList().get(i).getDetail()).asBitmap().dontAnimate()
                                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                        .placeholder(R.drawable.profile1)
                                                        .error(R.drawable.profile1)
                                                        .into(mImgMother);
                                            } else {
                                                mImgMother.setVisibility(View.GONE);
                                            }
                                        }
                                    } else {
                                        mProfileArrayList.add(tmps.getProfileList().get(i));
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            if (tmps != null && tmps.getMessage() != null && !tmps.getMessage().isEmpty())
                                Common.showToast(ParentsProfileActivity.this, tmps.getMessage());

                        }
                        mProgressBar.setVisibility(View.GONE);
                    } catch (Exception ex) {
                        mProgressBar.setVisibility(View.GONE);
                        Constants.writelog("ParentsProfileActivity",
                                "getParentsDetailList 1877:" + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ParentsProfileListModel> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    Constants.writelog("ParentsProfileActivity",
                            "getParentsDetailList 1877:" + t.getMessage());
                }
            });
        } else {
            Common.showToast(ParentsProfileActivity.this, getString(R.string.msg_connection));
        }
    }


}
