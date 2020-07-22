package com.expedite.apps.kumkum.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurriculumMediaPlayerActivity extends BaseActivity {
    private MediaPlayer mMediaPlayer;
    private SeekBar mSeekBar;
    private ProgressBar mProgressbar;
    private TextView mTotalTime, mCurrentTime;
    private boolean mIsMediaPlayerPrepare = false;
    private AdView mAdView;
    private String mUrl = "", mId = "", mName = "", Tag = "CurriculumMediaPlayerActivity", mAudioUrl = "", localAudioPath = "",isFrom="",parentId="";
    private ProgressDialog mAudioProgressDialog;
    private boolean isSaveSucessFully = false;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_player_layout);
        try {
            if (getIntent() != null && getIntent().getExtras() != null) {
                mId = getIntent().getExtras().getString("Id", "");
                mUrl = getIntent().getExtras().getString("Url", "");
                mName = getIntent().getExtras().getString("Name", "");
                parentId = getIntent().getExtras().getString("parentId", "");
                isFrom = getIntent().getExtras().getString("isFrom", "");
                Common.showLog("ImageUrl:", mUrl);
            }
            init();
        } catch (Exception ex) {
            Constants.writelog(Tag, "MSG 144:" + ex.getMessage());
        }
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.CurriculumMediaPlayerActivity);
            Constants.setActionbar(getSupportActionBar(), this, getApplicationContext(), mName, mName,
                    ActivityNames.CurriculumMediaPlayerActivity);
            mMediaPlayer = new MediaPlayer();
            mProgressbar = (ProgressBar) findViewById(R.id.progressBar);
            mAudioProgressDialog = new ProgressDialog(CurriculumMediaPlayerActivity.this);
            // mProgressbar.setVisibility(View.VISIBLE);
            mSeekBar = (SeekBar) findViewById(R.id.seekBar);
            mTotalTime = (TextView) findViewById(R.id.totalTime);
            mCurrentTime = (TextView) findViewById(R.id.currentTime);
            mSeekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            mSeekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            mMediaPlayer.setDataSource(mUrl);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                public void onPrepared(final MediaPlayer mp) {
                    mIsMediaPlayerPrepare = true;
                    mp.start();
                    mRunnable.run();
                    mProgressbar.setVisibility(View.INVISIBLE);
                }
            });

            //Chnages By Vijay Solanki 13-04-2020
            if (ContextCompat.checkSelfPermission(CurriculumMediaPlayerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                checkpermissionstatus(2);
            } else {
                saveAudio();
            }


        } catch (IOException e) {
            CurriculumMediaPlayerActivity.this.finish();
            Toast.makeText(this, "File not found.", Toast.LENGTH_SHORT).show();
        }
    }


    public void saveAudio() {
        try {
            String path = Constants.FILE_AUDIO_PATH + File.separator + mName + ".mp3";
            File mFile = new File(path);
            if (!mFile.exists() || mFile.length() < 200) {
                if (isOnline())
                    new SaveAudioTask().execute(path);
            } else {
                try {
                    mMediaPlayer.setDataSource(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //  mMediaPlayer.prepareAsync();
                try {
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(final MediaPlayer mp) {
                        try {
                            mIsMediaPlayerPrepare = true;
                            mp.start();
                            mRunnable.run();
                            mMediaPlayer.start();
                           /* if (isFrmBroadcast == 1) {
                                boolean gotFocus = requestAudioFocusForMyApp(CurriculumMediaPlayerActivity.this);
                                if (gotFocus) {
                                    mMediaPlayer.start();  //play audio.
                                }

                            } else
                                mMediaPlayer.pause();*/
                            mProgressbar.setVisibility(View.INVISIBLE);
                        } catch (Exception e) {
                            Constants.writelog("exception:134", String.valueOf(e.getMessage()));
                        }
                    }
                });
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Toast.makeText(CurriculumMediaPlayerActivity.this, "audio finished", Toast.LENGTH_SHORT);
                        Constants.Logwrite("finished", "finished");
                        /*if (index + 1 < listTest.size()) {
                            int pos = index + 1;
                            index += 1;
                            pos = chkRefresh(pos);
                            broadcastIntent(pos);
                           *//* Intent intent = new Intent();
                            intent.putExtra("Name", listTest.get(pos).getName());
                            intent.putExtra("fileId", listTest.get(pos).getId());
                            intent.putExtra("imageUrl", listTest.get(pos).getImageUrl());
                            intent.putExtra("pdfUrl", listTest.get(pos).getPdfUrl());
                            intent.putExtra("videoUrl", listTest.get(pos).getVideoUrl());
                            intent.putExtra("audioUrl", listTest.get(pos).getAudioUrl());
                            intent.putExtra("isDirect", "0");
                            if (listTest.get(pos).getPdfUrl() != null && !listTest.get(pos).getPdfUrl().isEmpty()) {
                                intent.setAction("RefreshPdfActivity");
                            } else {
                                intent.setAction("RefreshImageActivity");
                            }
                            sendBroadcast(intent);*//*
                        } else {
                            index = 0;
                            broadcastIntent(0);
                           *//* findViewById(R.id.float_pause).setVisibility(View.GONE);
                            findViewById(R.id.float_play).setVisibility(View.VISIBLE);*//*
                        }*/
                    }

                });
            }
        } catch (Exception ex) {
            Constants.writelog("exception:514", String.valueOf(ex.getMessage()));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_refresh, menu);
        return true;
    }

    private class SaveAudioTask extends AsyncTask<String, String, Void> {
        @Override
        protected void onPreExecute() {
            try {
                // mProgressbar.setVisibility(View.VISIBLE);
                mAudioProgressDialog.setMessage("Please wait for loading audio data ...");
                mAudioProgressDialog.setIndeterminate(false);
                mAudioProgressDialog.setMax(100);
                mAudioProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mAudioProgressDialog.setCancelable(true);
                mAudioProgressDialog.setCanceledOnTouchOutside(false);
                mAudioProgressDialog.setOnKeyListener(new ProgressDialog.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            if (mAudioProgressDialog.isShowing()) {
                                String path = Constants.FILE_AUDIO_PATH + File.separator + mName + ".mp3";
                                File mFile = new File(path);
                                if (mFile.exists()) {
                                    mFile.delete();
                                }
                                mAudioProgressDialog.dismiss();
                                CurriculumMediaPlayerActivity.this.finish();
                                onBackClickAnimation();
                            }
                        }
                        return true;
                    }
                });
                mAudioProgressDialog.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                localAudioPath = params[0];
                mAudioUrl = mUrl;
                mUrl = mAudioUrl;
                mUrl = mUrl.replace(" ", "%20");
                File myDir = CreateDownloadAudioFolder();
                final File file = new File(myDir, mName + ".mp3");
                FileOutputStream f = new FileOutputStream(file);
                URL u = new URL(mUrl);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                // c.setDoOutput(true);
                c.connect();
                InputStream in = c.getInputStream();
                int lenghtOfFile = c.getContentLength();
                byte[] buffer = new byte[2048];
                int len1 = 0;
                long total = 0;
                while ((len1 = in.read(buffer)) > 0) {
                    total += len1;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    f.write(buffer, 0, len1);
                }
                f.close();
//                if (file != null && !file.getAbsolutePath().isEmpty()) {
//                    saveAudiomessage = "Save Audio to " + "\"" + file.getAbsolutePath() + "\"" + " Successfully";
//                } else {
//                    saveAudiomessage = "Audio Save Successfully";
//                }
                isSaveSucessFully = true;


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            mAudioProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(Void result) {
            // mProgressbar.setVisibility(View.GONE);
            if (mAudioProgressDialog != null && mAudioProgressDialog.isShowing())
                mAudioProgressDialog.dismiss();
            try {
                //Toast.makeText(PDFviewActivity.this, saveAudiomessage, Toast.LENGTH_LONG).show();
                if (isSaveSucessFully) {
                    try {
                        mMediaPlayer.setDataSource(localAudioPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //  mMediaPlayer.prepareAsync();
                    try {
                        mMediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        public void onPrepared(final MediaPlayer mp) {
                            try {
                                mIsMediaPlayerPrepare = true;
                                mp.start();
                                mRunnable.run();
                                mMediaPlayer.start();
                               /* if (isFrmBroadcast == 1) {
                                    boolean gotFocus = requestAudioFocusForMyApp(PDFviewActivity.this);
                                    if (gotFocus) {
                                        mMediaPlayer.start();    //play audio.
                                    }

                                } else
                                    mMediaPlayer.pause();*/
                                mProgressbar.setVisibility(View.GONE);
                            } catch (Exception e) {
                                Constants.writelog("exception:694", String.valueOf(e.getMessage()));
                            }
                        }
                    });

                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Toast.makeText(CurriculumMediaPlayerActivity.this, "audio finished", Toast.LENGTH_SHORT);
                            Constants.Logwrite("finished", "finished");
                          /*  if (index + 1 < listTest.size()) {
                                int pos = index + 1;
                                index += 1;
                                pos = chkRefresh(pos);
                                broadcastIntent(pos);
                            } else {
                                index = 0;
                                broadcastIntent(0);
                               *//* findViewById(R.id.float_pause).setVisibility(View.GONE);
                                findViewById(R.id.float_play).setVisibility(View.VISIBLE);*//*
                            }*/
                        }

                    });

                }
            } catch (Exception ex) {
                Constants.writelog("exception:701", String.valueOf(ex.getMessage()));
            }

        }
    }

    public void checkpermissionstatus(int i) {
        try {
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
                    ActivityCompat.requestPermissions(CurriculumMediaPlayerActivity.this, perm, 1);
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
                    saveAudio();
                } else {
                    Toast.makeText(CurriculumMediaPlayerActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (mMediaPlayer != null) {
                    mMediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
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
                                Constants.writelog(Tag, "MSG 112:" + ex.getMessage());
                            }
                        }
                    });
                }
                mHandler.postDelayed(this, 10);
            } catch (Exception ex) {
                Constants.writelog(Tag, "MSG 120:" + ex.getMessage());
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                case R.id.refresh_type:
                    refreshData();
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            Constants.writelog(Tag, "MSG 126:" + ex.getMessage());
        }
        return super.onOptionsItemSelected(item);
    }


    private void refreshData() {
        try {
            String path = Constants.FILE_AUDIO_PATH + File.separator + mName + ".mp3";
            File mFile = new File(path);
            if (mFile.exists()) {
                mFile.delete();
            }
            if (mUrl != null && !mUrl.isEmpty()) {
                saveAudio();
            }

        } catch (Exception ex) {
            Constants.writelog("ImageviewActivity:885", String.valueOf(ex.getMessage()));
        }
    }

    public void play(View view) {
        try {
            mMediaPlayer.start();
            findViewById(R.id.pause).setVisibility(View.VISIBLE);
            findViewById(R.id.play).setVisibility(View.GONE);
        } catch (Exception ex) {
            Constants.writelog(Tag, "MSG 137:" + ex.getMessage());
        }
    }

    public void pause(View view) {
        try {
            mMediaPlayer.pause();
            findViewById(R.id.pause).setVisibility(View.GONE);
            findViewById(R.id.play).setVisibility(View.VISIBLE);
        } catch (Exception ex) {
            Constants.writelog(Tag, "MSG 147:" + ex.getMessage());
        }
    }

    @Override
    protected void onPause() {
        try {
            mMediaPlayer.pause();
            findViewById(R.id.pause).setVisibility(View.GONE);
            findViewById(R.id.play).setVisibility(View.VISIBLE);
        } catch (Exception ex) {
            Constants.writelog(Tag, "MSG 169:" + ex.getMessage());
        }
        super.onPause();
    }

    public void stop(View view) {
        try {
            mMediaPlayer.seekTo(0);
            mMediaPlayer.pause();
        } catch (Exception ex) {
            Constants.writelog(Tag, "MSG 144:" + ex.getMessage());
        }
    }

  /*  @Override
    protected void onStop() {
        try {
            mMediaPlayer.seekTo(0);
            mMediaPlayer.pause();
            super.onStop();
        } catch (Exception ex) {
            Constants.writelog(Tag, "190 144:" + ex.getMessage());
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if(isFrom.equalsIgnoreCase("notification")){

            Intent i=new Intent(CurriculumMediaPlayerActivity.this,HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        CurriculumMediaPlayerActivity.this.finish();
        onBackClickAnimation();

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
}