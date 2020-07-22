package com.expedite.apps.kumkum.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.CurriculumListModel;
import com.expedite.apps.kumkum.model.SendEmailDownlodModel;
import com.google.android.gms.ads.AdView;
import com.google.android.material.button.MaterialButton;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurriculumListActivity extends BaseActivity {

    RecyclerView mFolderRecyclerview, mFileRecyclerview;
    CurriculumFolderAdapter mFolderadapter = null;
    CurriculumFileAdapter mFileadapter = null;
    ArrayList<String> mFolderID = new ArrayList<>();
    ArrayList<String> mFolderName = new ArrayList<>();
    String headerText = "";
    RelativeLayout MainLayout;
    LinearLayout empty_folder_lyt_file_detail, mLLShareDownloadAll;
    private ProgressBar mProgressBar;
    private String SchoolId = "", ClassId = "", StudId = "", YearId = "";
    private String tag = "CurriculumListActivity", title = "";
    private String mIsFromHome = "", mFolderId = "0", mFoldername = "";
    private ArrayList<CurriculumListModel.FolderList> mFolderArray = new ArrayList<>();
    private ArrayList<CurriculumListModel.FileList> mFileArray = new ArrayList<>();
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressBar progressBar;
    private EditText mEmailid;
    private AlertDialog alert;
    private AdView mAdView;
    private Button mBtnDownload, mBtnShare;
    private String mStrAudioFileName = "", mStrAudioUrl = "";
    private ProgressDialog mPdfProgressDialog, mAudioProgressDialog;
    private CheckBox mCheckBoxSelectAll;
    private int flag = 0;
    private boolean notSelected = false;
    private boolean isAllSelected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.curriculum_list_layout);
        if (getIntent() != null && getIntent().getExtras() != null)
            mIsFromHome = getIntent().getExtras().getString("IsFromHome", "");
        title = getIntent().getExtras().getString("title", "");
        init();
        getCurriculumList();
    }

    public void init() {
        try {
            MainLayout = findViewById(R.id.Mainlayout);
            title = "Curriculam";

            empty_folder_lyt_file_detail = findViewById(R.id.empty_folder_lyt_file_detail);
            mLLShareDownloadAll = findViewById(R.id.LLShareAndDownlaod);
            mCheckBoxSelectAll = (CheckBox) findViewById(R.id.checkBoxselectall);
            Activity abc = this;
            Constants.setActionbar(getSupportActionBar(), abc, getApplicationContext(),
                    title, title, ActivityNames.CurriculumListActivity);
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.CurriculumListActivity);

            SchoolId = Datastorage.GetSchoolId(CurriculumListActivity.this);
            ClassId = Datastorage.GetClassId(CurriculumListActivity.this);
            StudId = Datastorage.GetStudentId(CurriculumListActivity.this);
            YearId = Datastorage.GetCurrentYearId(CurriculumListActivity.this);
            mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
            mBtnDownload = (Button) findViewById(R.id.downloadbtn);
            mBtnShare = (Button) findViewById(R.id.shareAll);
            mFolderRecyclerview = (RecyclerView) findViewById(R.id.folderrecyclerview);
            mFolderRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mFolderRecyclerview.setNestedScrollingEnabled(false);
            mFolderadapter = new CurriculumFolderAdapter(CurriculumListActivity.this, mFolderArray);
            mFolderRecyclerview.setAdapter(mFolderadapter);

            mFileRecyclerview = (RecyclerView) findViewById(R.id.filerecyclerview);
            mFileRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mFileadapter = new CurriculumFileAdapter(CurriculumListActivity.this, mFileArray);
            mFileRecyclerview.setAdapter(mFileadapter);
            mFileRecyclerview.setNestedScrollingEnabled(false);
        } catch (Exception ex) {
            Constants.writelog(tag, "Exception_72:" + ex.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        try {
            super.onCreateOptionsMenu(menu);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    if (mFolderID.size() > 0) {
                        mFolderId = mFolderID.get(mFolderID.size() - 1);
                        mFolderID.remove(mFolderID.size() - 1);
                        getCurriculumList();
                        onBackClickAnimation();
                        //  onBackPressed();

                    } else {
                        super.onBackPressed();
                        CurriculumListActivity.this.finish();
                        onBackClickAnimation();
                    }
                    return true;
                default:
                    break;
            }
            if (mToggle.onOptionsItemSelected(item)) {
                return true;
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mIsFromHome != null && !mIsFromHome.isEmpty()) {
            if (mFolderID.size() > 0) {
                try {
                    mFolderId = mFolderID.get(mFolderID.size() - 1);
                    mFolderID.remove(mFolderID.size() - 1);
                    mFolderName.remove(mFolderName.size() - 1);
                    if (mFolderName.size() > 1) {
                        mFoldername = mFolderName.get(mFolderName.size() - 1);
                    } else {
                        mFoldername = mFolderName.get(0);
                    }
                } catch (Exception ex) {
                }

               /* for (int i = 0; i < mFileArray.size(); i++) {
                    mFileArray.get(i).setIsChecked("0");
                }
*/
                getCurriculumList();
                mLLShareDownloadAll.setVisibility(View.GONE);

                onBackClickAnimation();
            } else {
                /*for (int i = 0; i < mFileArray.size(); i++) {
                    mFileArray.get(i).setIsChecked("0");
                }*/
                mLLShareDownloadAll.setVisibility(View.GONE);
                super.onBackPressed();
                CurriculumListActivity.this.finish();
                onBackClickAnimation();
            }
        } else {
            Intent intent = new Intent(CurriculumListActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void getCurriculumList() {
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            try {
                //http://app.vayuna.com/service.asmx/folderFileList?schoolId=8414&class_id=23&folderId=0
                Call<CurriculumListModel> call = ((MyApplication) getApplicationContext())
                        .getmRetrofitInterfaceAppService().GetCurriculumList(SchoolId, ClassId, mFolderId, StudId, YearId, Constants.PLATFORM);
                call.enqueue(new Callback<CurriculumListModel>() {
                    @Override
                    public void onResponse(Call<CurriculumListModel> call, Response<CurriculumListModel> response) {
                        try {
                            mFolderArray.clear();
                            mFileArray.clear();
                            CurriculumListModel tmp = response.body();
                            if (tmp != null && tmp.getArray() != null && tmp.getArray().get(0).getResponse() != null
                                    && !tmp.getArray().get(0).getResponse().isEmpty()
                                    && tmp.getArray().get(0).getResponse().equalsIgnoreCase("1")) {
                                mFolderArray.addAll(tmp.getArray().get(0).getFolderList());
                                mFileArray.addAll(tmp.getArray().get(0).getFileList());
                                if (!mFolderName.isEmpty()) {
                                    if (mFolderName.size() > 1) {
                                        headerText = "";
                                        for (int i = 0; i < mFolderName.size() - 1; i++) {
                                            if (i == mFolderName.size() - 1) {
                                                headerText += mFolderName.get(i);
                                            } else {
                                                headerText += mFolderName.get(i) + "->";
                                            }
                                        }
                                    } else {
                                        headerText = "";
                                    }
                                    View customview = CurriculumListActivity.this.getLayoutInflater().inflate(R.layout.mainactionbar, null);
                                    TextView label = (TextView) customview.findViewById(R.id.label);
                                    label.setText(mFoldername);
                                    if (!headerText.isEmpty()) {
                                        TextView label1 = (TextView) customview.findViewById(R.id.label1);
                                        label1.setText(headerText);
                                        label1.setVisibility(View.VISIBLE);
                                    } else {
                                        TextView label1 = (TextView) customview.findViewById(R.id.label1);
                                        label1.setVisibility(View.INVISIBLE);
                                    }
                                    getSupportActionBar().setCustomView(customview);
                                } else {
                                    View customview = CurriculumListActivity.this.getLayoutInflater().inflate(R.layout.mainactionbar, null);
                                    TextView label = (TextView) customview.findViewById(R.id.label);
                                    label.setText(title);
                                    TextView label1 = (TextView) customview.findViewById(R.id.label1);
                                    label1.setVisibility(View.INVISIBLE);
                                    getSupportActionBar().setCustomView(customview);
                                    //Constants.setActionbar(getSupportActionBar(), CurriculumListActivity.this, getApplicationContext(), "Curriculum", "Curriculum");
                                }
                                MainLayout.setVisibility(View.VISIBLE);
                                empty_folder_lyt_file_detail.setVisibility(View.GONE);
                                mFolderadapter.notifyDataSetChanged();
                                mFileadapter.notifyDataSetChanged();
                            } else {
                                if (tmp != null && tmp.getArray() != null && tmp.getArray().get(0).getMessage() != null && tmp.getArray().get(0).getMessage() != "") {
                                    Common.showToast(CurriculumListActivity.this, tmp.getArray().get(0).getMessage());
                                    //   onBackPressed();
                                    MainLayout.setVisibility(View.GONE);
                                    empty_folder_lyt_file_detail.setVisibility(View.VISIBLE);


                                } else {
                                    Common.showToast(CurriculumListActivity.this, "No Curriculum Data Available..");
                                }
                            }
                            mProgressBar.setVisibility(View.GONE);
                        } catch (Exception ex) {
                            Constants.writelog(tag, "Exception_113:" + ex.getMessage()
                                    + "::::::" + ex.getStackTrace());
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<CurriculumListModel> call, Throwable t) {
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
            empty_folder_lyt_file_detail.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            Common.showToast(CurriculumListActivity.this, getString(R.string.msg_connection));
        }
    }

    private void ShowDialog(final String id, final String name, final String url) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.download_file_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        mEmailid = (EditText) promptView.findViewById(R.id.editEmailid);
        final TextView mSendbtn = (TextView) promptView.findViewById(R.id.Sendbtn);
        final ImageView mClosebtn = (ImageView) promptView.findViewById(R.id.closebtn);
        progressBar = promptView.findViewById(R.id.progressBar);
        getEmail();
        mClosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alert.isShowing())
                    alert.dismiss();
            }
        });

        mSendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailid.getText().toString().trim();
                if (email.isEmpty()) {
                    Common.showToast(CurriculumListActivity.this, "Enter Emailid");
                } else if (!email.matches(emailPattern)) {
                    Common.showToast(CurriculumListActivity.this, "Enter valid Emailid");
                } else if (email != null && !email.isEmpty() && email.matches(emailPattern)) {
                    SendEmail(url, id, name, mEmailid.getText().toString().trim());
                }
            }
        });
        alert = alertDialogBuilder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void getEmail() {
        try {
            if (isOnline()) {
                progressBar.setVisibility(View.VISIBLE);
                Call<SendEmailDownlodModel> call = ((MyApplication) getApplicationContext().getApplicationContext())
                        .getmRetrofitInterfaceAppService().GetStudentEmailid(Datastorage.GetSchoolId(this), Datastorage.GetStudentId(this), Integer.parseInt(Datastorage.GetCurrentYearId(this)));
                call.enqueue(new Callback<SendEmailDownlodModel>() {
                    @Override
                    public void onResponse(Call<SendEmailDownlodModel> call, Response<SendEmailDownlodModel> response) {
                        SendEmailDownlodModel tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1")) {
                            if (tmps.getStrResult() != null && !tmps.getStrResult().isEmpty()) {
                                mEmailid.setText(tmps.getStrResult());
                            }
                        } else {
                            if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty())
                                Common.showToast(CurriculumListActivity.this, tmps.getResponse());
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<SendEmailDownlodModel> call, Throwable t) {
                        Constants.writelog("CurriculumListActivity",
                                "GetEmail:" + t.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                });

            } else {
                empty_folder_lyt_file_detail.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                Common.showToast(CurriculumListActivity.this, getString(R.string.msg_connection));
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void SendEmail(String doc, String CirId, String name, String email) {
        try {
            if (isOnline()) {
                progressBar.setVisibility(View.VISIBLE);
                Call<SendEmailDownlodModel> call = ((MyApplication) getApplicationContext().getApplicationContext())
                        .getmRetrofitInterfaceAppService()
                        .SendEmailid(Datastorage.GetStudentId(this), Datastorage.GetSchoolId(this), Integer.parseInt(Datastorage.GetCurrentYearId(this)), 2, email, doc, CirId, name, Constants.PLATFORM);
                call.enqueue(new Callback<SendEmailDownlodModel>() {
                    @Override
                    public void onResponse(Call<SendEmailDownlodModel> call, Response<SendEmailDownlodModel> response) {
                        SendEmailDownlodModel tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1")) {
                            if (tmps.getStrResult() != null && !tmps.getStrResult().isEmpty()) {
                                Common.showToast(CurriculumListActivity.this, tmps.getStrResult());
                                if (alert.isShowing())
                                    alert.dismiss();
                            }
                        } else {
                            if (tmps.getStrResult() != null && !tmps.getStrResult().isEmpty()) {
                                Common.showToast(CurriculumListActivity.this, tmps.getStrResult());
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<SendEmailDownlodModel> call, Throwable t) {
                        Constants.writelog("CurriculumListActivity",
                                "SendEmail:" + t.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                });
            } else {
                Common.showToast(CurriculumListActivity.this, getString(R.string.msg_connection));
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
            progressBar.setVisibility(View.GONE);
        }
    }

    private class CurriculumFolderAdapter extends RecyclerView.Adapter<CurriculumFolderAdapter.ViewHolder> {

        ArrayList<CurriculumListModel.FolderList> mFolderArray;
        Activity activity;

        public CurriculumFolderAdapter(Activity activity, ArrayList<CurriculumListModel.FolderList> mFolderArray) {
            this.activity = activity;
            this.mFolderArray = mFolderArray;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.curriculum_folder_raw_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            try {
                final CurriculumListModel.FolderList tmp = mFolderArray.get(position);
                if (tmp.getName() != null && !tmp.getName().isEmpty())
                    holder.mFolderTxt.setText(Html.fromHtml(tmp.getName()));

                holder.mLLMainFolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mFolderID.add(mFolderId);
                        if (mFolderId.equals("0")) {
                            mFolderName.clear();
                        }
                        if (!mFolderName.contains(tmp.getName()))
                            mFolderName.add(tmp.getName());
                        mFolderId = tmp.getId();
                        mFoldername = tmp.getName();
                        getCurriculumList();

                    }
                });
            } catch (Exception ex) {
                Constants.writelog(tag, "onBindViewHolder():67 " + ex.getMessage());
            }
        }

        @Override
        public int getItemCount() {
            return mFolderArray.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView mFolderTxt;
            LinearLayout mLLMainFolder;

            public ViewHolder(View itemView) {
                super(itemView);
                mFolderTxt = (TextView) itemView.findViewById(R.id.txtTitle);
                mLLMainFolder = (LinearLayout) itemView.findViewById(R.id.mainfolderll);
            }
        }
    }

    private class CurriculumFileAdapter extends RecyclerView.Adapter<CurriculumFileAdapter.ViewHolder> {

        Activity activity;
        ArrayList<CurriculumListModel.FileList> mFileArray;
        ArrayList<CurriculumListModel.FileList> mSelectedArray = new ArrayList<>();

        public CurriculumFileAdapter(Activity activity, ArrayList<CurriculumListModel.FileList> mFileArray) {
            this.activity = activity;
            this.mFileArray = mFileArray;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //  View view = LayoutInflater.from(activity).inflate(R.layout.curriculum_file_raw_layout, parent, false);
            View view = LayoutInflater.from(activity).inflate(R.layout.student_curriculam_raw_layout_new, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            try {
                final CurriculumListModel.FileList tmp = mFileArray.get(position);
                if (tmp.getIsChecked() != null && tmp.getIsChecked().equalsIgnoreCase("1")) {
                    holder.mCheckBox.setChecked(true);
                } else {
                    holder.mCheckBox.setChecked(false);
                }

               /* mCheckBoxSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            flag=1;
                            for (int i = 0; i < mFileArray.size(); i++) {
                                mFileArray.get(i).setIsChecked("1");
                                notSelected = false;
                            }
                        } else {
                            flag=0;
                            for (int i = 0; i < mFileArray.size(); i++) {
                                if (notSelected == false) {
                                    mFileArray.get(i).setIsChecked("0");
                                } else {
                                    if (mFileArray.get(i).getIsChecked().equalsIgnoreCase("1")) {
                                    }
                                }
                            }
                        }
                    }
                });*/
                holder.txtInstruction.setVisibility(View.GONE);
                holder.txtduedate.setVisibility(View.GONE);

                holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (flag == 0) {
                            if (mFileArray.get(position).getIsChecked() != null && !mFileArray.get(position).getIsChecked().equalsIgnoreCase("")
                                    && mFileArray.get(position).getIsChecked().equalsIgnoreCase("1")) {
                                mFileArray.get(position).setIsChecked("0");
                                holder.mCheckBox.setChecked(false);
                            } else {
                                if (isChecked) {
                                    mFileArray.get(position).setIsChecked("1");
                                    holder.mCheckBox.setChecked(true);
                                }
                            }
                        }

                        checkSelectedData();


                    }
                });

                holder.mCheckBox.setTag(position);
                if (tmp.getName() != null && !tmp.getName().isEmpty())
                    holder.mFileTxt.setText(Html.fromHtml(tmp.getName()));
                if (tmp.getType() != null && !tmp.getType().isEmpty()) {
                    if (tmp.getType().equals("1")) {
                        holder.mImgFile.setImageDrawable(getResources().getDrawable(R.drawable.pdficon));
                        holder.mImgDownload.setVisibility(View.VISIBLE);
                    } else if (tmp.getType().equals("2")) {
                        holder.mImgFile.setImageDrawable(getResources().getDrawable(R.drawable.imageicon));
                        holder.mImgDownload.setVisibility(View.VISIBLE);

                    } else if (tmp.getType().equals("3")) {
                        holder.mImgFile.setImageDrawable(getResources().getDrawable(R.drawable.audioicon));

                    } else if (tmp.getType().equals("4")) {
                        holder.mImgFile.setImageDrawable(getResources().getDrawable(R.drawable.videoicon));
                        holder.mImgDownload.setVisibility(View.GONE);
                        holder.mCheckBox.setVisibility(View.GONE);
                    } else {
                        holder.mImgFile.setImageDrawable(getResources().getDrawable(R.drawable.file1));
                        holder.mImgDownload.setVisibility(View.GONE);
                        holder.mCheckBox.setVisibility(View.GONE);
                    }
                }


                holder.mImgComplete.setVisibility(View.GONE);

                holder.mImgDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            /*if (tmp.getUrl().endsWith(".mp3")) {
                                new SaveAudioTask().execute(tmp.getUrl(), tmp.getName());
                            } else if (tmp.getUrl().contains("youtube.com")) {

                            } else {
                                DownloadPdfOrImageFile(CurriculumListActivity.this, tmp.getName(), tmp.getUrl());
                            }*/
                            if (ContextCompat.checkSelfPermission(CurriculumListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_DENIED) {
                                checkpermissionstatus(2);
                            } else {
                                if (tmp.getType().equals("3")) {
                                    new SaveAudioTask().execute(tmp.getUrl(), tmp.getName());
                                } else if (tmp.getType().equals("1") ||
                                        tmp.getType().equals("2")) {
                                    DownloadPdfOrImageFile(CurriculumListActivity.this, tmp.getName(),
                                            tmp.getUrl());
                                } else if (tmp.getType().equals("4")) {
                                    new SaveVideoTask().execute(tmp.getUrl(), tmp.getName());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                holder.mImgComplete.setVisibility(View.GONE);
                holder.mImgComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(CurriculumListActivity.this, TaskCompleteActivity.class);
                            intent.putExtra("Curriculam", tmp.getName());
                            intent.putExtra("CurriculamId", tmp.getId());
                            startActivity(intent);
                            onClickAnimation();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                holder.mImgShare.setVisibility(View.GONE);
                holder.mBtnShare.setVisibility(View.VISIBLE);
                holder.mImgShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareData(tmp.getName(), tmp.getUrl());

                    }
                });

                holder.mBtnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            shareData(tmp.getName(), tmp.getUrl());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                holder.mImgEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            ShowDialog(tmp.getId(), tmp.getName(), tmp.getUrl());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                mBtnDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(CurriculumListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_DENIED) {
                            checkpermissionstatus(2);
                        } else {
                            for (int i = 0; i < mFileArray.size(); i++) {
                                if (mFileArray.get(i).getIsChecked() != null && mFileArray.get(i).getIsChecked().equalsIgnoreCase("1")) {
                                    Common.showLog("Url & Name", mFileArray.get(i).getUrl() + "\n" + mFileArray.get(i).getName());
                                    if (mFileArray.get(i).getType().equals("3")) {
                                        new SaveAudioTask().execute(mFileArray.get(i).getUrl(), mFileArray.get(i).getName());
                                    } else if (mFileArray.get(i).getType().equals("1") ||
                                            mFileArray.get(i).getType().equals("2")) {
                                        DownloadPdfOrImageFile(CurriculumListActivity.this, mFileArray.get(i).getName(),
                                                mFileArray.get(i).getUrl());
                                    } else if (mFileArray.get(i).getType().equals("4")) {
                                        new SaveVideoTask().execute(mFileArray.get(i).getUrl(), mFileArray.get(i).getName());
                                    }

                                }
                            }
                        }
                    }
                });

                mBtnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Data = "";
                        for (int i = 0; i < mFileArray.size(); i++) {
                            if (mFileArray.get(i).getIsChecked() != null
                                    && mFileArray.get(i).getIsChecked().equalsIgnoreCase("1")) {
                                Data += "Title :" + mFileArray.get(i).getName() + "\n" + "Url : " + mFileArray.get(i).getUrl() + "\n";
                            }
                        }
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, Data);
                        startActivity(Intent.createChooser(sharingIntent, "Share Using"));
                    }
                });





               /* if (mSelectedArray != null && mSelectedArray.size() > 1) {
                    mLLShareDownloadAll.setVisibility(View.VISIBLE);
                } else {
                    mLLShareDownloadAll.setVisibility(View.GONE);
                }*/

                holder.mLLFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (tmp.getType() != null && !tmp.getType().isEmpty()) {
                            if (mFolderName.size() > 1) {
                                headerText = "";
                                for (int i = 0; i < mFolderName.size(); i++) {
                                    if (i == mFolderName.size() - 1) {
                                        headerText += mFolderName.get(i);
                                    } else {
                                        headerText += mFolderName.get(i) + "->";
                                    }
                                }
                            }
                            if (tmp.getType().equalsIgnoreCase("1")) {
                                if (tmp.getUrl() != null &&
                                        !tmp.getUrl().isEmpty()) {
                                    Intent intent = new Intent(CurriculumListActivity.this, CurriculumPdfViewActivity.class);
                                    intent.putExtra("Id", tmp.getId());
                                    intent.putExtra("Name", tmp.getName());
                                    intent.putExtra("Url", tmp.getUrl());
                                    intent.putExtra("HeaderText", headerText);
                                    startActivity(intent);
                                    onClickAnimation();
                                }
                            } else if (tmp.getType().equalsIgnoreCase("2")) {
                                if (tmp.getUrl() != null &&
                                        !tmp.getUrl().isEmpty()) {
                                    Intent intent = new Intent(CurriculumListActivity.this, CurriculumImageViewActivity.class);
                                    intent.putExtra("Id", tmp.getId());
                                    intent.putExtra("Name", tmp.getName());
                                    intent.putExtra("Url", tmp.getUrl());
                                    intent.putExtra("HeaderText", headerText);
                                    startActivity(intent);
                                    onClickAnimation();
                                }
                            } else if (tmp.getType().equalsIgnoreCase("3")) {
                                if (tmp.getUrl() != null &&
                                        !tmp.getUrl().isEmpty()) {
                                    Intent intent = new Intent(CurriculumListActivity.this, CurriculumMediaPlayerActivity.class);
                                    intent.putExtra("Id", tmp.getId());
                                    intent.putExtra("Name", tmp.getName());
                                    intent.putExtra("Url", tmp.getUrl());
                                    intent.putExtra("HeaderText", headerText);
                                    startActivity(intent);
                                    onClickAnimation();
                                }
                            } else if (tmp.getType().equalsIgnoreCase("4")) {
                                if (tmp.getUrl() != null &&
                                        !tmp.getUrl().isEmpty()) {
                                    String vidoeId = YouTubeUrlParser.getVideoId(tmp.getUrl());
                                    if (vidoeId != null && !vidoeId.isEmpty()) {
                                        Intent intent = new Intent(CurriculumListActivity.this, YouTubePlayerActivity.class);
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
                            }
                        }

                    }
                });
            } catch (Exception ex) {
                Log.e("vsv", ex.getMessage());
                Constants.writelog(tag, "onBindViewHolder():67 " + ex.getMessage());
            }
        }

        private void shareData(String name, String url) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, name + " : " + url);
            startActivity(Intent.createChooser(sharingIntent, "Share Using"));
        }


        private void checkSelectedData() {
            try {
                for (int i = 0; i < mFileArray.size(); i++) {
                    if (mFileArray.get(i).getIsChecked() != null &&
                            !mFileArray.get(i).getIsChecked().equalsIgnoreCase("") &&
                            mFileArray.get(i).getIsChecked().equalsIgnoreCase("1")) {
                        mLLShareDownloadAll.setVisibility(View.VISIBLE);
                        break;
                    } else {
                        mCheckBoxSelectAll.setChecked(false);
                        mLLShareDownloadAll.setVisibility(View.GONE);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public int getItemCount() {
            return mFileArray.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView mFileTxt, txtInstruction, txtduedate;
            ImageView mImgFile;
            RelativeLayout mLLFile;
            ImageView mImgEmail, mImgShare;
            private CheckBox mCheckBox;
            private LinearLayout mLlmain;
            // private MaterialButton mImgDownload, mImgComplete, mBtnShare;
            private RelativeLayout mImgDownload, mImgComplete, mBtnShare;

            public ViewHolder(View itemView) {
                super(itemView);
                mFileTxt = (TextView) itemView.findViewById(R.id.txtTitle);
                txtduedate = (TextView) itemView.findViewById(R.id.txtduedate);
                txtInstruction = (TextView) itemView.findViewById(R.id.txtInstruction);
                mImgShare = (ImageView) itemView.findViewById(R.id.share);
                mImgFile = (ImageView) itemView.findViewById(R.id.imgfile);
                mLLFile = (RelativeLayout) itemView.findViewById(R.id.mainfilell);
                mLlmain = (LinearLayout) itemView.findViewById(R.id.mainlayout);
                mImgDownload =  itemView.findViewById(R.id.imgDownload);
                mBtnShare =  itemView.findViewById(R.id.Btnshare);
                mImgEmail = (ImageView) itemView.findViewById(R.id.email);
                mImgComplete =  itemView.findViewById(R.id.imgComplete);
                mCheckBox = (CheckBox) itemView.findViewById(R.id.checkBox);

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
                    ActivityCompat.requestPermissions(CurriculumListActivity.this, perm, 1);
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
                    Toast.makeText(CurriculumListActivity.this, "Permission given Now you can download the files...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CurriculumListActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

  /*  public void saveAudio() {
        try {
            String path = Constants.FILE_AUDIO_PATH + File.separator + mStrAudioFileName;
            File mFile = new File(path);
            if (!mFile.exists() || mFile.length() < 200) {
                if (isOnline())
                    new SaveAudioTask().execute();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/

    public class SaveAudioTask extends AsyncTask<String, String, Void> {
        @Override
        protected void onPreExecute() {
            try {
                // mProgressbar.setVisibility(View.VISIBLE);
                mAudioProgressDialog = new ProgressDialog(CurriculumListActivity.this);
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
                File myDir = CreateDownloadAudioFolder();
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
                Common.showToast(CurriculumListActivity.this, e.getMessage());
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
            Common.showToast(CurriculumListActivity.this, "File Downloaded.");

        }
    }


    public class SaveVideoTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            mAudioProgressDialog = new ProgressDialog(CurriculumListActivity.this);
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
                File myDir = CreateVideoDwonloadFolder();

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
            Common.showToast(CurriculumListActivity.this, "File Downloaded.");
        }
    }
}



