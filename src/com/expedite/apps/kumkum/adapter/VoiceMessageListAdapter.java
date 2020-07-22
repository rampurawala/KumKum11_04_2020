package com.expedite.apps.kumkum.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Utility;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.VoiceMessageListModel;

import java.util.List;

public class VoiceMessageListAdapter extends RecyclerView.Adapter<VoiceMessageListAdapter.ViewHolder> {

    int flag = 0;
    ImageView emImgPlay;
    private List<VoiceMessageListModel.Strlist> mList;
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private AlertDialog.Builder alertDialog;
    private AlertDialog mSelectImageDialog;
    private LayoutInflater viewInflater = null;
    private MediaPlayer mMediaPlayer;
    private boolean mIsMediaPlayerPrepare = false;
    private SeekBar mSeekBar;
    private TextView mTotalTime, mCurrentTime;
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
    };

    public VoiceMessageListAdapter(Activity activity, List<VoiceMessageListModel.Strlist> mList) {
        this.mContext = activity;
        this.mList = mList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.voice_msg_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VoiceMessageListModel.Strlist tmp = mList.get(position);

        if (tmp.getSecond() != null && !tmp.getSecond().equalsIgnoreCase("")) {
            holder.mTxtTitle.setText(tmp.getSecond());
        } else {
            holder.mTxtTitle.setText("");
        }
        if (tmp.getFourth() != null && !tmp.getFourth().equalsIgnoreCase("")) {
            holder.mTxtDate.setText(tmp.getFourth());
        } else {
            holder.mTxtDate.setText("");
        }

      /*  holder.Mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((BaseActivity)mContext).isOnline()) {
                    try {
                        ((VoiceMessageListActivity)mContext).SelectPhoto(tmp.getDisplayUrl(),tmp.getDocumentName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Constants.NotifyNoInternet(mContext);
                }
            }
        });*/

        holder.Mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    alertDialog = new AlertDialog.Builder(mContext,
                            R.style.AlertDialogCustom);
                    viewInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View promptView = viewInflater.inflate(R.layout.audio_alert_dialog_layout, null);
                    alertDialog.setView(promptView);
                    final boolean result = Utility.checkPermission(mContext);
                    mMediaPlayer = new MediaPlayer();
                    emImgPlay = (ImageView) promptView.findViewById(R.id.float_play);
                    ImageView mImgPause = (ImageView) promptView.findViewById(R.id.float_pause);
                    TextView mTxtTitle = (TextView) promptView.findViewById(R.id.voiceMsgTitle);
                    mTxtTitle.setText(tmp.getSecond());
                    mSeekBar = (SeekBar) promptView.findViewById(R.id.seekBar_iv);
                    mTotalTime = (TextView) promptView.findViewById(R.id.totalTime_iv);
                    mCurrentTime = (TextView) promptView.findViewById(R.id.currentTime_iv);
                    mMediaPlayer.setDataSource(tmp.getThird());

                    emImgPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if (flag == 1) {
                                    flag = 0;
                                    emImgPlay.setImageResource(R.drawable.play_icon);
                                    mMediaPlayer.pause();
                                } else {
                                    flag = 1;
                                    emImgPlay.setImageResource(R.drawable.pause_icon);
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
                            try {
                                mIsMediaPlayerPrepare = true;
                                emImgPlay.setImageResource(R.drawable.pause_icon);
                                mp.start();
                                mRunnable.run();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
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
        });
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


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void onBackPressed() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mSelectImageDialog.dismiss();
            flag = 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTxtTitle, mTxtDate;
        private ImageView mImgAudioPlay;
        private LinearLayout Mainlayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mTxtTitle = (TextView) itemView.findViewById(R.id.title);
            mTxtDate = (TextView) itemView.findViewById(R.id.date);
            mImgAudioPlay = (ImageView) itemView.findViewById(R.id.playAudio);
            Mainlayout = (LinearLayout) itemView.findViewById(R.id.Mainlayout);
        }
    }
}
