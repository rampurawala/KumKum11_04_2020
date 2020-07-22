package com.expedite.apps.kumkum.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.CurriculumImageViewActivity;
import com.expedite.apps.kumkum.activity.CurriculumMediaPlayerActivity;
import com.expedite.apps.kumkum.activity.CurriculumPdfViewActivity;
import com.expedite.apps.kumkum.activity.CurriculumResponseClassroomActivity;
import com.expedite.apps.kumkum.activity.CurriculumTextFileActivity;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.ProgressItem;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.CurriculumListModel;
import com.google.android.youtube.player.YouTubePlayer;
import com.thefinestartist.ytpa.YouTubePlayerActivity;
import com.thefinestartist.ytpa.enums.Orientation;
import com.thefinestartist.ytpa.utils.YouTubeUrlParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.expedite.apps.kumkum.activity.SubmissionFileListDiffActivity.newTabSelected;

/**
 * Created by Jameela on 03/07/2020.
 */
public class SubmissionFileListSubmittedAdapter extends RecyclerView.Adapter {
    private List<CurriculumListModel.FileList> mList = new ArrayList<>();
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private String Tag = "LeaveListAdapter";
    private ProgressDialog mPdfProgressDialog, mAudioProgressDialog;
    private ProgressItem mProgressItem;
    private int flag = 0;
    LinearLayout shareDownload;
    AppCompatButton downloadMain,shareMain;
    public SubmissionFileListSubmittedAdapter(Activity context, List<CurriculumListModel.FileList> ItemList, AppCompatButton downloadMain, AppCompatButton shareMain, LinearLayout shareDownload) {
        mList = ItemList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.downloadMain=downloadMain;
        this.shareMain=shareMain;
        this.shareDownload=shareDownload;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.row_tab_submission_file_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void checkSelectedData() {
        try {
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).getIsChecked() != null &&
                        !mList.get(i).getIsChecked().equalsIgnoreCase("") &&
                        mList.get(i).getIsChecked().equalsIgnoreCase("1")) {
                   // ((SubmissionFileListDiffActivity) mContext).mLLShareDownloadAll.setVisibility(View.VISIBLE);
                    shareDownload.setVisibility(View.VISIBLE);
                    break;
                } else {
                //    ((SubmissionFileListDiffActivity) mContext).mLLShareDownloadAll.setVisibility(View.GONE);
                    shareDownload.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            if (holder instanceof CustomViewHolder) {
                final CurriculumListModel.FileList tmp = mList.get(position);
                if (newTabSelected == 0) {
                    for(int i=0;i<mList.size();i++){
                        mList.get(i).setIsChecked("0");
                    }
                    /* tmp.setIsChecked("0");*/
                    //  ((SubmissionFileListDiffActivity) mContext).mLLShareDownloadAll.setVisibility(View.GONE);
                    // if(mList.size()-1==position){
                    newTabSelected=1;
                    //   }
                }
                if (tmp.getIsChecked() != null && tmp.getIsChecked().equalsIgnoreCase("1")) {
                    ((SubmissionFileListSubmittedAdapter.CustomViewHolder) holder).mCheckBox.setChecked(true);
                } else {
                    ((SubmissionFileListSubmittedAdapter.CustomViewHolder) holder).mCheckBox.setChecked(false);
                }
                ((SubmissionFileListSubmittedAdapter.CustomViewHolder) holder).fileName.setText(tmp.getName());
                if(tmp.getDueDate()!=null && !tmp.getDueDate().isEmpty()) {
                    ((CustomViewHolder) holder).dateTxt.setText(Html.fromHtml("<B>"+tmp.getDueDate()+"</B>"));
                    ((CustomViewHolder) holder).dateTxt.setVisibility(View.VISIBLE);

                }else{
                    ((CustomViewHolder) holder).dateTxt.setVisibility(View.GONE);
                }
                if(tmp.getInstructions()!=null && !tmp.getInstructions().isEmpty()) {
                    ((CustomViewHolder) holder).fileIns.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).fileIns.setText(tmp.getInstructions());
                }else{
                    ((CustomViewHolder) holder).fileIns.setVisibility(View.GONE);
                }
                if (tmp.getType() != null && !tmp.getType().isEmpty()) {
                    if (tmp.getType().equals("1")) {
                        ((CustomViewHolder) holder).imgfile.setImageDrawable(mContext.getResources().getDrawable(R.drawable.pdficon));
                        ((CustomViewHolder) holder).mCheckBox.setVisibility(View.VISIBLE);
                        ((CustomViewHolder) holder).mImgDownload.setVisibility(View.VISIBLE);
                        ((CustomViewHolder) holder).shareLyt.setVisibility(View.VISIBLE);
                    } else if (tmp.getType().equals("2")) {
                        ((CustomViewHolder) holder).imgfile.setImageDrawable(mContext.getResources().getDrawable(R.drawable.imageicon));
                        ((CustomViewHolder) holder).mCheckBox.setVisibility(View.VISIBLE);
                        ((CustomViewHolder) holder).mImgDownload.setVisibility(View.VISIBLE);
                        ((CustomViewHolder) holder).shareLyt.setVisibility(View.VISIBLE);
                    } else if (tmp.getType().equals("3")) {
                        ((CustomViewHolder) holder).imgfile.setImageDrawable(mContext.getResources().getDrawable(R.drawable.audioicon));
                        ((CustomViewHolder) holder).mCheckBox.setVisibility(View.VISIBLE);
                    } else if (tmp.getType().equals("4")) {
                        ((CustomViewHolder) holder).imgfile.setImageDrawable(mContext.getResources().getDrawable(R.drawable.videoicon));
                        ((CustomViewHolder) holder).mCheckBox.setVisibility(View.GONE);
                        ((CustomViewHolder) holder).mImgDownload.setVisibility(View.GONE);
                        ((CustomViewHolder) holder).shareLyt.setVisibility(View.VISIBLE);
                    } else {
                        ((CustomViewHolder) holder).mCheckBox.setVisibility(View.GONE);
                        ((CustomViewHolder) holder).imgfile.setImageDrawable(mContext.getResources().getDrawable(R.drawable.file1));
                        ((CustomViewHolder) holder).mImgDownload.setVisibility(View.GONE);
                        ((CustomViewHolder) holder).shareLyt.setVisibility(View.GONE);
                    }
                }

                ((CustomViewHolder) holder).mCheckBox.setTag(position);

                ((CustomViewHolder) holder).mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (flag == 0) {
                            if (mList.get(position).getIsChecked() != null && !mList.get(position).getIsChecked().equalsIgnoreCase("")
                                    && mList.get(position).getIsChecked().equalsIgnoreCase("1")) {
                                mList.get(position).setIsChecked("0");
                                ((CustomViewHolder) holder).mCheckBox.setChecked(false);
                            } else {
                                if (isChecked) {
                                    mList.get(position).setIsChecked("1");
                                    ((CustomViewHolder) holder).mCheckBox.setChecked(true);
                                }
                            }
                        }
                        checkSelectedData();
                    }
                });

                downloadMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_DENIED) {
                            ((BaseActivity) mContext).checkpermissionstatus(2, mContext, mContext);
                        } else {
                            for (int i = 0; i < mList.size(); i++) {
                                if (mList.get(i).getIsChecked() != null && mList.get(i).getIsChecked().equalsIgnoreCase("1")) {
                                    Common.showLog("Url & Name", mList.get(i).getUrl() + "\n" + mList.get(i).getName()+"_"+mList.get(i).getId());
                                    if (mList.get(i).getType().equals("3")) {
                                        new SaveAudioTask().execute(mList.get(i).getUrl(), mList.get(i).getName()+"_"+mList.get(i).getId());
                                    } else if (mList.get(i).getType().equals("1") ||
                                            mList.get(i).getType().equals("2")) {
                                        ((BaseActivity) mContext).DownloadPdfOrImageFile(mContext, mList.get(i).getName()+"_"+mList.get(i).getId(),
                                                mList.get(i).getUrl());
                                    } else if (mList.get(i).getType().equals("4")) {
                                        new SaveVideoTask().execute(mList.get(i).getUrl(), mList.get(i).getName()+"_"+mList.get(i).getId());
                                    }

                                }
                            }
                        }
                    }
                });

                shareMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Data = "";
                        for (int i = 0; i < mList.size(); i++) {
                            if (mList.get(i).getIsChecked() != null
                                    && mList.get(i).getIsChecked().equalsIgnoreCase("1")) {
                                Data += "Title :" + mList.get(i).getName() + "\n" + "Url : " + mList.get(i).getUrl() + "\n";
                            }
                        }
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, Data);
                        mContext.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
                    }
                });

                ((CustomViewHolder) holder).mImgComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                         Intent intent = new Intent(mContext, CurriculumResponseClassroomActivity.class);
                            //Intent intent = new Intent(mContext, SubmissionFileListDiffActivity.class);
                            intent.putExtra("Curriculam", tmp.getName());
                            intent.putExtra("CurriculamDueDate", tmp.getDueDate());
                            intent.putExtra("CurriculamId", tmp.getId());
                            mContext.startActivity(intent);
                            ((BaseActivity)mContext).onClickAnimation();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                ((CustomViewHolder) holder).mImgDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            /*if (tmp.getUrl().endsWith(".mp3")) {
                                new SaveAudioTask().execute(tmp.getUrl(), tmp.getName());
                            } else if (tmp.getUrl().contains("youtube.com")) {

                            } else {
                                DownloadPdfOrImageFile(CurriculumClassroomActivity.this, tmp.getName(), tmp.getUrl());
                            }*/
                            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_DENIED) {
                                ((BaseActivity)mContext).checkpermissionstatus(2,mContext,mContext);
                            } else {
                                if (tmp.getType().equals("3")) {
                                    new SaveAudioTask().execute(tmp.getUrl(), tmp.getName()+"_"+tmp.getId());
                                } else if (tmp.getType().equals("1") ||
                                        tmp.getType().equals("2")) {
                                    ((BaseActivity)mContext).DownloadPdfOrImageFile(mContext, tmp.getName()+"_"+tmp.getId(),
                                            tmp.getUrl());
                                } else if (tmp.getType().equals("4")) {
                                    new SaveVideoTask().execute(tmp.getUrl(), tmp.getName()+"_"+tmp.getId());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                ((CustomViewHolder) holder).mainUpperLyt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (tmp.getType() != null && !tmp.getType().isEmpty()) {

                            if (tmp.getType().equalsIgnoreCase("1")) {
                                if (tmp.getUrl() != null &&
                                        !tmp.getUrl().isEmpty()) {
                                    Intent intent = new Intent(mContext, CurriculumPdfViewActivity.class);
                                    intent.putExtra("Id", tmp.getId());
                                    intent.putExtra("Name", tmp.getName());
                                    intent.putExtra("Url", tmp.getUrl());
                                    intent.putExtra("HeaderText", tmp.getName());
                                    mContext.startActivity(intent);
                                    ((BaseActivity)mContext).onClickAnimation();
                                }
                            } else if (tmp.getType().equalsIgnoreCase("2")) {
                                if (tmp.getUrl() != null &&
                                        !tmp.getUrl().isEmpty()) {
                                    Intent intent = new Intent(mContext, CurriculumImageViewActivity.class);
                                    intent.putExtra("Id", tmp.getId());
                                    intent.putExtra("Name", tmp.getName());
                                    intent.putExtra("Url", tmp.getUrl());
                                    intent.putExtra("HeaderText", tmp.getName());
                                    mContext.startActivity(intent);
                                    ((BaseActivity)mContext).onClickAnimation();
                                }
                            } else if (tmp.getType().equalsIgnoreCase("3")) {
                                if (tmp.getUrl() != null &&
                                        !tmp.getUrl().isEmpty()) {
                                    Intent intent = new Intent(mContext, CurriculumMediaPlayerActivity.class);
                                    intent.putExtra("Id", tmp.getId());
                                    intent.putExtra("Name", tmp.getName());
                                    intent.putExtra("Url", tmp.getUrl());
                                    intent.putExtra("HeaderText", tmp.getName());
                                    mContext.startActivity(intent);
                                    ((BaseActivity)mContext).onClickAnimation();
                                }
                            } else if (tmp.getType().equalsIgnoreCase("4")) {
                                if (tmp.getUrl() != null &&
                                        !tmp.getUrl().isEmpty()) {
                                    String vidoeId = YouTubeUrlParser.getVideoId(tmp.getUrl());
                                    if (vidoeId != null && !vidoeId.isEmpty()) {
                                        Intent intent = new Intent(mContext, YouTubePlayerActivity.class);
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
                                        mContext.startActivity(intent);
                                    }
                                }
                            }
                            if (tmp.getType().equalsIgnoreCase("5")) {
                                if (tmp.getUrl() != null &&
                                        !tmp.getUrl().isEmpty()) {
                                    Intent intent = new Intent(mContext, CurriculumTextFileActivity.class);
                                    intent.putExtra("Id", tmp.getId());
                                    intent.putExtra("Name", tmp.getName());
                                    intent.putExtra("Url", tmp.getUrl());
                                    intent.putExtra("HeaderText", tmp.getName());
                                    mContext.startActivity(intent);
                                    ((BaseActivity)mContext).onClickAnimation();
                                }
                            }
                        }

                    }
                });
                ((CustomViewHolder) holder).shareLyt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, tmp.getName() + " : " + tmp.getUrl());
                        mContext.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
                    }
                });

                if(tmp.getFolderName()!=null && !tmp.getFolderName().isEmpty()) {
                    ((CustomViewHolder) holder).subName.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).subName.setText(tmp.getFolderName());
                }else{
                    ((CustomViewHolder) holder).subName.setVisibility(View.GONE);
                }
            }
        } catch (Exception ex) {
            Constants.writelog(Tag, "Ex 84:" + ex.getMessage());
        }
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView fileName,  dateTxt, subName,fileIns;
        RelativeLayout imgLyt,shareLyt, mImgDownload, mImgComplete,mainUpperLyt;
        ImageView imgfile;
        private CheckBox mCheckBox;
        public CustomViewHolder(View view) {
            super(view);
            fileName = (TextView) view.findViewById(R.id.fileName);
            dateTxt = (TextView) view.findViewById(R.id.dateTxt);
            subName = (TextView) view.findViewById(R.id.subName);
            fileIns = (TextView) view.findViewById(R.id.fileIns);
            mImgDownload = view.findViewById(R.id.imgDownload);
            mImgComplete =  view.findViewById(R.id.imgComplete);
            mainUpperLyt =  view.findViewById(R.id.mainUpperLyt);
            shareLyt =  view.findViewById(R.id.shareLyt);
            imgLyt =  view.findViewById(R.id.imgLyt);
            imgfile =  view.findViewById(R.id.imgfile);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkBox);

        }
    }
    public class SaveAudioTask extends AsyncTask<String, String, Void> {
        @Override
        protected void onPreExecute() {
            try {
                // mProgressbar.setVisibility(View.VISIBLE);
                mAudioProgressDialog = new ProgressDialog(mContext);
                mAudioProgressDialog.setMessage("Please wait for loading audio data ...");
                mAudioProgressDialog.setIndeterminate(false);
                mAudioProgressDialog.setMax(100);
                mAudioProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mAudioProgressDialog.setCancelable(true);
                mAudioProgressDialog.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String mUrl = "";
                // mUrl = mStrAudioUrl;
                mUrl = params[0];
                mUrl = mUrl.replace(" ", "%20");
                File myDir =((BaseActivity)mContext).CreateDownloadAudioFolder();
                final File file = new File(myDir, params[1] + ".mp3");
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


            } catch (Exception e) {
                Common.showToast(mContext, e.getMessage());
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
            Common.showToast(mContext, "File Downloaded.");

        }
    }


    public class SaveVideoTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            mAudioProgressDialog = new ProgressDialog(mContext);
            mAudioProgressDialog.setMessage("Please wait , While we are Your Video File...");
            mAudioProgressDialog.setIndeterminate(false);
            mAudioProgressDialog.setMax(100);
            mAudioProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mAudioProgressDialog.setCancelable(true);
            mAudioProgressDialog.show();
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(String... sUrl) {
         /*   URL u = null;
            InputStream is = null;
            try {
                u = new URL("https://espschools.s3.ap-south-1.amazonaws.com/Temp/1.mp4");
                is = u.openStream();
                HttpURLConnection huc = (HttpURLConnection) u.openConnection(); //to know the size of video
                int size = huc.getContentLength();

                if (huc != null) {
                    String fileName = "FILE.mp4";
                    String storagePath = Environment.getExternalStorageDirectory().toString();
                    File f = new File(storagePath, fileName);

                    FileOutputStream fos = new FileOutputStream(f);
                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    if (is != null) {
                        while ((len1 = is.read(buffer)) > 0) {
                            fos.write(buffer, 0, len1);
                        }
                    }
                    if (fos != null) {
                        fos.close();
                    }
                }
            } catch (MalformedURLException mue) {
                mue.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {
                    // just going to ignore this one
                }
            }*/
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                input = connection.getInputStream();
                File myDir = ((BaseActivity)mContext).CreateVideoDwonloadFolder();

                File filename = new File(myDir, sUrl[1] + ".mp4");
                output = new FileOutputStream(filename);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    if (fileLength > 0)
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

        }

        @Override
        protected void onPostExecute(String result) {
            if (mAudioProgressDialog != null && mAudioProgressDialog.isShowing()) {
                mAudioProgressDialog.dismiss();
            }
            Common.showToast(mContext, "File Downloaded.");
        }
    }

}



