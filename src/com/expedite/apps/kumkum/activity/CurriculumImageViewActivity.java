package com.expedite.apps.kumkum.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.TouchImageViewHelp;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.google.android.youtube.player.YouTubePlayer;
import com.paynimo.android.payment.util.Constant;
import com.thefinestartist.ytpa.YouTubePlayerActivity;
import com.thefinestartist.ytpa.enums.Orientation;
import com.thefinestartist.ytpa.utils.YouTubeUrlParser;


/**
 * Created by Jaydeep patel on 11/21/2017.
 */
public class CurriculumImageViewActivity extends BaseActivity {
    private TouchImageViewHelp mImageView;
    private String mUrl = "", mName = "",isFrom="",parentId="",isVideo="",filenameDownload="";
int isFirstTime=1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view_layout);
        if (getIntent() != null && getIntent().getExtras() != null) {
            mUrl = getIntent().getExtras().getString("Url", "");
            mName = getIntent().getExtras().getString("Name", "");
            parentId = getIntent().getExtras().getString("parentId", "");
            isFrom = getIntent().getExtras().getString("isFrom", "");
            isVideo = getIntent().getExtras().getString("isVideo", "");
            Common.showLog("ImageUrl: 32 ", mUrl);
        }
        if(isVideo.equalsIgnoreCase("1")){
            if (mUrl != null &&
                    !mUrl.isEmpty()) {
                String vidoeId = YouTubeUrlParser.getVideoId(mUrl);
                if (vidoeId != null && !vidoeId.isEmpty()) {
                    Intent intent = new Intent(CurriculumImageViewActivity.this, YouTubePlayerActivity.class);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, vidoeId);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE,
                            YouTubePlayer.PlayerStyle.DEFAULT);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION,
                            Orientation.AUTO);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_ENTER, R.anim.fade_in);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_EXIT, R.anim.fade_out);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }else{
            init();

        }

    }

    public void init() {
        try {
            Activity abc = this;
            Constants.setActionbar(getSupportActionBar(), abc, getApplicationContext(),
                    mName, mName, ActivityNames.CurriculumImageViewActivity);
            mImageView = (TouchImageViewHelp) findViewById(R.id.img_pager_item);
            if (mUrl != null && !mUrl.isEmpty()) {
                Glide.with(CurriculumImageViewActivity.this)
                        .load(mUrl).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(mImageView);
            }


        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isFrom.equalsIgnoreCase("notification")){
            Intent i=new Intent(CurriculumImageViewActivity.this,HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        CurriculumImageViewActivity.this.finish();
        onBackClickAnimation();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isVideo.isEmpty() && isVideo.equalsIgnoreCase("1") && isFirstTime == 0) {
            onBackPressed();
        } else {
            isFirstTime = 0;
        }
    }
}
