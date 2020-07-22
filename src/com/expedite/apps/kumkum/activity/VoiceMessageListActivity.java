package com.expedite.apps.kumkum.activity;

import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.adapter.VoiceMessageListAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.ActivityVisitedModel;
import com.expedite.apps.kumkum.model.VoiceMessageListModel;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

//Created By Vijay Solanki 02-01-2020

public class VoiceMessageListActivity extends BaseActivity {

    public ProgressBar mProgressBar;
    ImageView mImgPlay;
    int flag = 0;
    Common common;
    String mStrData = "";
    String mUpdateedData = "";
    private RecyclerView mVoiceMessageRecyclerView;
    private VoiceMessageListAdapter mVoiceMessageListAdapter;
    private ArrayList<VoiceMessageListModel.Strlist> mVoiceMsgListArray = new ArrayList<>();
    private LinearLayout mLlEmptyLayout;
    private AlertDialog.Builder alertDialog;
    private AlertDialog mSelectImageDialog;
    private LayoutInflater viewInflater = null;
    private MediaPlayer mMediaPlayer;
    private boolean mIsMediaPlayerPrepare = false;
    private SeekBar mSeekBar;
    private TextView mTotalTime, mCurrentTime;
    private AdView mAdView;
    private DatabaseHandler db;
    private ArrayList<ActivityVisitedModel> mActivityVisitedList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_message_list);
        init();
    }

    public void init() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Voice Message");
        }
        db = new DatabaseHandler(VoiceMessageListActivity.this);
        mAdView = (AdView) findViewById(R.id.adView);
        showBannerAd(mAdView, ActivityNames.VoiceMessageListActivity);
        showFullScreenAd(VoiceMessageListActivity.this, ActivityNames.VoiceMessageListActivity);
       /* setZero = (Button) findViewById(R.id.setZero);


        setZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String DataList = "28@8414@02-03-2020###5,1#29,22#56,1#25,2#26,1#18,3#62,1#18.2,1#20,1#82,3#8,1#31,1#24.1,1#24,2#27,1#23,1#22,1#24.2,1#28,1#76,1#34,1#36,1#19.5,1#3,1#@@@63@8414@02-03-2020###29,23#62,2#18,3#18.2,1#76,2#28,1#36,1#19.5,1#14,1#9,2#20,1#34,2#33,2#56,1#25,1#22,1#24,1#27,1#24.1,1#24.2,1#8,1#31,1#82,1#@@@";
                String[] diffrentStudent = DataList.split("@@@");
                List<String> al = new ArrayList<String>();
                al = Arrays.asList(diffrentStudent);
                for (int i = 0; i < al.size(); i++) {
                    Log.e("Da " + i, al.get(i));
                    String data2 = al.get(i);
                    String[] items2 = data2.split("###");
                    String scholStud = items2[0];
                    String[] items5 = scholStud.split("@");
                    Log.e("SchoolStudDate", scholStud);
                    Log.e("StudemtSchol", items5[0] + "@" + items5[1]);
                    String n = items2[1];
                    String[] items3 = n.split("#");
                    List<String> al3 = new ArrayList<String>();
                    al3 = Arrays.asList(items3);
                    for (int k = 0; k < al3.size(); k++) {
                        Log.e("Data 102 " + k, al3.get(k));
                        String acttivty = al3.get(k);
                        String[] items4 = acttivty.split(",");
                        List<String> al4 = new ArrayList<String>();
                        al4 = Arrays.asList(items4);
                        for (int a = 0; a < al4.size(); a++) {
                            Log.e("Data 108 " + a, al4.get(a));
                        }
                    }
                }
            }
        });*/

        common = new Common(VoiceMessageListActivity.this);
        mMediaPlayer = new MediaPlayer();
        mVoiceMessageRecyclerView = (RecyclerView) findViewById(R.id.voiceMsgRecyclerView);
        mVoiceMessageRecyclerView.setLayoutManager(new LinearLayoutManager(VoiceMessageListActivity.this));
        mVoiceMessageListAdapter = new VoiceMessageListAdapter(VoiceMessageListActivity.this, mVoiceMsgListArray);
        mVoiceMessageRecyclerView.setAdapter(mVoiceMessageListAdapter);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mLlEmptyLayout = (LinearLayout) findViewById(R.id.empty_folder_lyt_file_detail);

       /* mActivityVisitedList = (ArrayList<ActivityVisitedModel>) db.getActivityVisitedList();
        ArrayList<String> studlist = new ArrayList<>();
        for (int i = 0; i < mActivityVisitedList.size(); i++) {
            if (!studlist.contains(mActivityVisitedList.get(i).getStudiD().toString() + "@" + mActivityVisitedList.get(i).getSchoolId().toString()
                    + "@" + mActivityVisitedList.get(i).getDateVisited().toString()))
                studlist.add(mActivityVisitedList.get(i).getStudiD().toString() + "@" + mActivityVisitedList.get(i).getSchoolId().toString()
                        + "@" + mActivityVisitedList.get(i).getDateVisited().toString());
        }

        for (int i = 0; i < studlist.size(); i++) {
            mStrData += studlist.get(i) + "###";
            for (int j = 0; j < mActivityVisitedList.size(); j++) {
                if (studlist.get(i).equalsIgnoreCase(mActivityVisitedList.get(j).getStudiD() + "@" + mActivityVisitedList.get(j).getSchoolId()
                        + "@" + mActivityVisitedList.get(j).getDateVisited().toString())) {
                    mStrData += mActivityVisitedList.get(j).getActivityName() + "," +
                            mActivityVisitedList.get(j).getCount() + "#";
                }
            }
            mStrData += "@@@";
        }


        Common.showLog("userVisiDetails", mStrData);*/
        getMessageList();
    }

    private void getMessageList() {
        try {
            if (isOnline()) {
                try {
                    String mStudentId = Datastorage.GetStudentId(getApplicationContext());
                    String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
                    String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
                    mProgressBar.setVisibility(View.VISIBLE);
                    Call<VoiceMessageListModel> mCall = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                            .getVoiceMessage(mStudentId, mSchoolId, mYearId, String.valueOf(SchoolDetails.appname), Constants.CODEVERSION, Constants.PLATFORM);
                    mCall.enqueue(new retrofit2.Callback<VoiceMessageListModel>() {
                        @Override
                        public void onResponse(Call<VoiceMessageListModel> call, Response<VoiceMessageListModel> response) {
                            VoiceMessageListModel tmp = response.body();
                            mVoiceMsgListArray.clear();
                            try {
                                if (tmp != null && tmp.getResponse() != null &&
                                        tmp.getResponse().equalsIgnoreCase("1")) {
                                    mVoiceMsgListArray.addAll(tmp.getStrlist());
                                    mVoiceMessageListAdapter.notifyDataSetChanged();
                                    mVoiceMessageRecyclerView.setVisibility(View.VISIBLE);

                                } else {
                                    Common.showToast(VoiceMessageListActivity.this, tmp.getStrResult());
                                }
                                mProgressBar.setVisibility(View.GONE);

                            } catch (Exception ex) {
                                Constants.writelog("getMessageList:78:ErrorMsg::", ex.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<VoiceMessageListModel> call, Throwable t) {
                            Constants.writelog("getMessageList:84:ErrorMsg::", "onFailure()");
                        }
                    });

                } catch (Exception ex) {
                    Constants.writelog("getMessageList:89:ErrorMsg::", ex.getMessage());
                }
            } else {
                Constants.NotifyNoInternet(VoiceMessageListActivity.this);
            }
        } catch (Exception ex) {
            Constants.writelog("getMessageList:93:ErrorMsg::", ex.getMessage());
        }
    }

   /* public void SelectPhoto(String Url, String title) {
        try {
            alertDialog = new AlertDialog.Builder(VoiceMessageListActivity.this,
                    R.style.AlertDialogCustom);
            viewInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View promptView = viewInflater.inflate(R.layout.audio_alert_dialog_layout, null);
            alertDialog.setView(promptView);
            final boolean result = Utility.checkPermission(VoiceMessageListActivity.this);

            mImgPlay = (ImageView) promptView.findViewById(R.id.float_play);
            ImageView mImgPause = (ImageView) promptView.findViewById(R.id.float_pause);
            TextView mTxtTitle = (TextView) promptView.findViewById(R.id.voiceMsgTitle);
            mTxtTitle.setText(title);
            mSeekBar = (SeekBar) promptView.findViewById(R.id.seekBar_iv);
            mTotalTime = (TextView) promptView.findViewById(R.id.totalTime_iv);
            mCurrentTime = (TextView) promptView.findViewById(R.id.currentTime_iv);
            mMediaPlayer.setDataSource(Url);
            flag = 0;
            mImgPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (flag == 1) {
                            flag = 0;
                            mImgPlay.setImageResource(R.drawable.play_icon);
                            mMediaPlayer.pause();
                        } else {
                            flag = 1;
                            mImgPlay.setImageResource(R.drawable.pause_icon);
                            mMediaPlayer.start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            TextView mTxtcancel = (TextView) promptView.findViewById(R.id.btnCancle);
            mTxtcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mSelectImageDialog.dismiss();
                        mMediaPlayer.pause();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            });
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(final MediaPlayer mp) {
                    mIsMediaPlayerPrepare = true;
                    // mp.start();
                    mRunnable.run();
                    //  mProgressbar.setVisibility(View.INVISIBLE);
                }
            });

            alertDialog.setCancelable(false);
            mSelectImageDialog = alertDialog.create();
            mSelectImageDialog.setCanceledOnTouchOutside(false);
            mSelectImageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mSelectImageDialog.show();
        } catch (NullPointerException ex) {
            Constants.writelog("146", ex.getMessage());
        } catch (Exception ex) {
            Constants.writelog("148", ex.getMessage());
        }
    }

    private String getTimeDisplayString(long millis) {
        StringBuffer buf = new StringBuffer();

        long hours = millis / (1000 * 60 * 60);
        long minutes = (millis % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = ((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000;

        buf.append(String.format("%02d", hours)).append(":")
                .append(String.format("%02d", minutes)).append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            try {
                if (mMediaPlayer != null) {
                    mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                        @Override
                        public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        }
                    });
                    if (mIsMediaPlayerPrepare) {
                        int mDuration = mMediaPlayer.getDuration();
                        mSeekBar.setMax(mDuration);
                        mTotalTime.setText(getTimeDisplayString(mDuration));
                    }
                    mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                    mCurrentTime.setText(getTimeDisplayString(mMediaPlayer.getCurrentPosition()));
                    mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        @Override
                        public void onProgressChanged(SeekBar seekBar,
                                                      int progress, boolean fromUser) {
                            try {
                                if (mMediaPlayer != null && fromUser) {
                                    mMediaPlayer.seekTo(progress);
                                }
                            } catch (Exception ex) {
                                //CommonClass.writelog(Tag, "MSG 112:" + ex.getMessage());
                            }
                        }
                    });
                }
                mHandler.postDelayed(this, 10);
            } catch (Exception ex) {
                //   CommonClass.writelog(Tag, "MSG 120:" + ex.getMessage());
            }
        }
    };*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        try {
            VoiceMessageListActivity.this.finish();
            hideKeyboard(VoiceMessageListActivity.this);
            onBackClickAnimation();
            super.onBackPressed();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
