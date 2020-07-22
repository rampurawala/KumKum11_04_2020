package com.expedite.apps.kumkum.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.BuildConfig;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.adapter.ClassroomResponseListAdapter;
import com.expedite.apps.kumkum.adapter.CurriculamResponseListAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.common.Utility;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.model.AppService;
import com.expedite.apps.kumkum.model.ParentsProfileListModel;
import com.google.android.gms.ads.AdView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import static com.expedite.apps.kumkum.activity.LeaveApplyActivity.isDownloadsDocument;
import static com.expedite.apps.kumkum.activity.LeaveApplyActivity.isExternalStorageDocument;
import static com.expedite.apps.kumkum.activity.LeaveApplyActivity.isGooglePhotosUri;
import static com.expedite.apps.kumkum.activity.LeaveApplyActivity.isMediaDocument;

public class CurriculumResponseClassroomActivity extends BaseActivity {

    private AdView mAdView;
    private String mStrCurriCulam = "", mStrCurriCulamId = "", mFilename = "", mFilePath = "", bucket = "", secretKey = "", mid = "",CurriculamDueDate="",
            storefilename = "", mStoreImageFile = "", Tag = "", SchoolId = "", StudentId = "", Year_Id = "", mStrMsg = "",isFrom="";
    private TextView mTxtCurriculamName, mTxtSelectFile, mTxtSelectedPath, mTxtSubmit, mTxtNoData,curriculamDueDate;
    private LayoutInflater viewInflater = null;
    private AlertDialog mSelectImageDialog;
    private int mRequestCodePhotoGallery = 1, mRequestCodePDFFile = 2;
    private ProgressBar mProgressBar;
    private Uri selectedImage;
    private Common common;
    private ImageView mImgRemove;
    private TextInputEditText mEdtMsg;
    private RecyclerView mRecyclerView;
    private ArrayList<AppService.ListArray> mCurriculamResponseList = new ArrayList<>();
    private ClassroomResponseListAdapter mClassroomResponseListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curriculum_response_classroom);
        if (getIntent() != null && getIntent().getExtras() != null) {
            mStrCurriCulam = getIntent().getExtras().getString("Curriculam", "");
            isFrom = getIntent().getExtras().getString("isFrom", "");
            mStrCurriCulamId = getIntent().getExtras().getString("CurriculamId", "");
            CurriculamDueDate = getIntent().getExtras().getString("CurriculamDueDate", "");
        }
        init();
    }
    private void init() {
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                setTitle("Submission Complete task");
            }
            common = new Common(this);
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.CurriculumResponseClassroomActivity);
            showFullScreenAd(CurriculumResponseClassroomActivity.this, ActivityNames.CurriculumResponseClassroomActivity);
            mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
            mTxtCurriculamName = (TextView) findViewById(R.id.curriculamname);
            mTxtNoData = (TextView) findViewById(R.id.nodatatxt);
            mEdtMsg = (TextInputEditText) findViewById(R.id.edtMsg);
            mTxtSelectFile = (TextView) findViewById(R.id.SelectFile);
            mTxtSubmit = (TextView) findViewById(R.id.submit);
            curriculamDueDate = (TextView) findViewById(R.id.curriculamDueDate);
            mRecyclerView = (RecyclerView) findViewById(R.id.CurruiculamResponse);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(CurriculumResponseClassroomActivity.this));
            mClassroomResponseListAdapter = new ClassroomResponseListAdapter(CurriculumResponseClassroomActivity.this, mCurriculamResponseList, mStrCurriCulam);
            mRecyclerView.setAdapter(mClassroomResponseListAdapter);
            mImgRemove = (ImageView) findViewById(R.id.remove);
            mTxtCurriculamName.setText(": " + mStrCurriCulam);
            mTxtSelectedPath = (TextView) findViewById(R.id.txtSelectedPath);
            SchoolId = Datastorage.GetSchoolId(CurriculumResponseClassroomActivity.this);
            StudentId = Datastorage.GetStudentId(CurriculumResponseClassroomActivity.this);
            Year_Id = Datastorage.GetCurrentYearId(CurriculumResponseClassroomActivity.this);
            curriculamDueDate.setText(CurriculamDueDate);
            mTxtSelectFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectPhoto();
                }
            });

            mImgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFilePath = "";
                    mFilename = "";
                    mStoreImageFile = "";
                    mTxtSelectedPath.setVisibility(View.GONE);
                    mTxtSelectedPath.setText("");
                    storefilename = "";
                    mImgRemove.setVisibility(View.GONE);
                    Toast.makeText(CurriculumResponseClassroomActivity.this, "File Removed", Toast.LENGTH_LONG).show();
                }
            });
            mTxtSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mStrMsg = mEdtMsg.getText().toString().trim();
                        if (mStrMsg != null && !mStrMsg.equalsIgnoreCase("") && !mStrMsg.isEmpty() ||
                                mFilename != null && !mFilename.equalsIgnoreCase("") && !mFilename.isEmpty()) {
                            mProgressBar.setVisibility(View.VISIBLE);
                            if (mFilename != null && !mFilename.equalsIgnoreCase("")) {
                                mFilename=Common.GetTimeTicks()+"_"+mFilename;
                                BasicAWSCredentials credentials = new BasicAWSCredentials(mid, secretKey);
                                AmazonS3Client s3 = new AmazonS3Client(credentials);
                                s3.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
                                TransferUtility transferUtility = new TransferUtility(s3, CurriculumResponseClassroomActivity.this);
                                TransferObserver observer = transferUtility.upload(
                                        bucket,
                                        mFilename,
                                        new File(mStoreImageFile)
                                );

                                observer.setTransferListener(new TransferListener() {
                                    @Override
                                    public void onStateChanged(int id, TransferState state) {
                                        if (state == TransferState.COMPLETED) {
                                            submitCurriculamResponse();
                                        }
                                    }

                                    @Override
                                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                                    }

                                    @Override
                                    public void onError(int id, Exception ex) {
                                        mProgressBar.setVisibility(View.GONE);
                                        ex.printStackTrace();
                                    }
                                });
                            } else {
                                submitCurriculamResponse();
                            }
                        } else {
                            Common.showToast(CurriculumResponseClassroomActivity.this,"Please Select any File or Enter Any Msg to Submit your response.");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            getCurriculamResponse();
            getFtpDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitCurriculamResponse() {
        try {
            if (isOnline()) {
                mProgressBar.setVisibility(View.VISIBLE);
                Call<ParentsProfileListModel> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                        .GetSubmitCurriculamResponseClassroom(StudentId, SchoolId, Year_Id, mStrCurriCulamId, mStrMsg, mFilename, Constants.PLATFORM);
                call.enqueue(new Callback<ParentsProfileListModel>() {
                    @Override
                    public void onResponse(Call<ParentsProfileListModel> call, Response<ParentsProfileListModel> response) {
                        try {
                            ParentsProfileListModel tmp = response.body();
                            if (tmp.getResponse().equalsIgnoreCase("1")) {
                                ((MyApplication) CurriculumResponseClassroomActivity.this
                                        .getApplicationContext()).getLocalBroadcastInstance().sendBroadcast(
                                        new Intent(new Intent(CurriculumResponseClassroomActivity.this
                                                .getResources().getString(R.string.broadcast_submission_response_submitted))));
                                Common.showToast(CurriculumResponseClassroomActivity.this, tmp.getMessage());
                                CurriculumResponseClassroomActivity.this.finish();
                            } else {
                                Common.showToast(CurriculumResponseClassroomActivity.this, tmp.getMessage());
                            }
                            mProgressBar.setVisibility(View.GONE);
                        } catch (Exception ex) {
                            mProgressBar.setVisibility(View.GONE);
                            Constants.writelog(Tag, "getFtpDetail::701" + ex.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ParentsProfileListModel> call, Throwable t) {
                        Constants.writelog(Tag, "getFtpDetail:709" + t.getMessage());
                        mProgressBar.setVisibility(View.GONE);

                    }
                });
            } else {
                Common.showToast(CurriculumResponseClassroomActivity.this, getString(R.string.msg_connection));
            }
        } catch (Exception ex) {
            mProgressBar.setVisibility(View.GONE);
            Common.printStackTrace(ex);
        }
    }

    private void getCurriculamResponse() {
        try {
            if (isOnline()) {
                Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                        .getStudentCurriculamResponseClassroom(StudentId, SchoolId, Year_Id, mStrCurriCulamId, Constants.PLATFORM);
                call.enqueue(new Callback<AppService>() {
                    @Override
                    public void onResponse(Call<AppService> call, Response<AppService> response) {
                        try {
                            AppService tmp = response.body();
                            mCurriculamResponseList.clear();
                            if (tmp.getResponse().equalsIgnoreCase("1")) {
                                if (tmp.getListArray() != null && tmp.getListArray().size() > 0) {
                                    mCurriculamResponseList.addAll(tmp.getListArray());
                                    mRecyclerView.setVisibility(View.VISIBLE);
                                    mClassroomResponseListAdapter.notifyDataSetChanged();
                                } else {
                                   /* mTxtNoData.setVisibility(View.VISIBLE);
                                    mRecyclerView.setVisibility(View.GONE);*/
                                }
                                //  Common.showToast(CurriculumResponseClassroomActivity.this, tmp.getStrResult());
                            } else {
                               /* mTxtNoData.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                                Common.showToast(CurriculumResponseClassroomActivity.this, tmp.getStrResult());*/
                            }
                            //mProgressBar.setVisibility(View.GONE);
                        } catch (Exception ex) {
                            //mProgressBar.setVisibility(View.GONE);
                            Constants.writelog(Tag, "getFtpDetail::701" + ex.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<AppService> call, Throwable t) {
                        Constants.writelog(Tag, "getFtpDetail:709" + t.getMessage());
                        mProgressBar.setVisibility(View.GONE);

                    }
                });
            } else {
                Common.showToast(CurriculumResponseClassroomActivity.this, getString(R.string.msg_connection));
            }
        } catch (Exception ex) {
            mProgressBar.setVisibility(View.GONE);
            Common.printStackTrace(ex);
        }
    }


    private void SelectPhoto() {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CurriculumResponseClassroomActivity.this,
                    R.style.AlertDialogCustom);
            viewInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View promptView = viewInflater.inflate(R.layout.select_image_dialog_layout, null);
            alertDialog.setView(promptView);
            final boolean result = Utility.checkPermission(CurriculumResponseClassroomActivity.this);
            RelativeLayout mLLCamera = (RelativeLayout) promptView.findViewById(R.id.llcamera);
            RelativeLayout mLLGallery = (RelativeLayout) promptView.findViewById(R.id.llgallery);
            TextView mTxtcancel = (TextView) promptView.findViewById(R.id.btnCancle);
            mLLCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("application/pdf");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Pdf"), mRequestCodePDFFile);

                    mSelectImageDialog.dismiss();
                }
            });

            mLLGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (result) {

                        startActivityForResult(new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                                mRequestCodePhotoGallery);
                    }
                    mSelectImageDialog.dismiss();
                }
            });

            mTxtcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSelectImageDialog.dismiss();
                }
            });

            alertDialog.setCancelable(false);
            mSelectImageDialog = alertDialog.create();
            mSelectImageDialog.setCanceledOnTouchOutside(false);
            mSelectImageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mSelectImageDialog.show();
        } catch (NullPointerException ex) {
            Common.printStackTrace(ex);
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == mRequestCodePhotoGallery && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    mProgressBar.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        final ClipData clipData = data.getClipData();
                        if (clipData == null) {
                            selectedImage = data.getData();
                            Bitmap bitmap = MediaStore.Images.Media.
                                    getBitmap(getContentResolver(), selectedImage);
                            Uri tempUri = getImageUri(CurriculumResponseClassroomActivity.this, bitmap);
                            // CALL THIS METHOD TO GET THE ACTUAL PATH
                            File finalFile = new File(getRealPathFromURI(tempUri));
                            mFilename = finalFile.getName();
                            mTxtSelectedPath.setVisibility(View.VISIBLE);
                            mImgRemove.setVisibility(View.VISIBLE);
                            //mTxtSelectedPath.setText(selectedImage.toString());
                            mTxtSelectedPath.setText(finalFile.getName());
                            mStoreImageFile = finalFile.toString();

                           /* Picasso.with(CurriculumResponseClassroomActivity.this).load("file://" + mStoreImageFile)
                                    .placeholder(R.drawable.account)
                                    .error(R.drawable.account)
                                    .into(imgview);*/
                        } else {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        for (int i = 0; i < clipData.getItemCount(); i++) {
                                            ClipData.Item item = clipData.getItemAt(i);
                                            selectedImage = item.getUri();
                                            Bitmap bitmap = MediaStore.Images.Media
                                                    .getBitmap(getContentResolver(), selectedImage);
                                            //  mTxtSelectedPath.setVisibility(View.VISIBLE);
                                            // mTxtSelectedPath.setText(selectedImage.toString());
                                            Uri tempUri = getImageUri(CurriculumResponseClassroomActivity.this, bitmap);
                                            // CALL THIS METHOD TO GET THE ACTUAL PATH
                                            File finalFile = new File(getRealPathFromURI(tempUri));
                                            mFilename = finalFile.getName();
                                            mTxtSelectedPath.setVisibility(View.VISIBLE);
                                            mImgRemove.setVisibility(View.VISIBLE);
                                            //mTxtSelectedPath.setText(selectedImage.toString());
                                            mTxtSelectedPath.setText(finalFile.getName());
                                            mStoreImageFile = finalFile.toString();

                                          /*  Picasso.with(CurriculumResponseClassroomActivity.this).load("file://" + mStoreImageFile)
                                                    .placeholder(R.drawable.account)
                                                    .error(R.drawable.account)
                                                    .into(imgview);*/
                                        }
                                    } catch (Exception ex) {
                                        Common.printStackTrace(ex);
                                    }
                                }
                            });

                        }
                    } else {
                        selectedImage = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(getContentResolver(), selectedImage);
                        // mTxtSelectedPath.setText(selectedImage.toString());
                        Uri tempUri = getImageUri(CurriculumResponseClassroomActivity.this, bitmap);
                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        File finalFile = new File(getRealPathFromURI(tempUri));
                        mFilename = finalFile.getName();
                        mTxtSelectedPath.setVisibility(View.VISIBLE);
                        mImgRemove.setVisibility(View.VISIBLE);
                        //mTxtSelectedPath.setText(selectedImage.toString());
                        mTxtSelectedPath.setText(finalFile.getName());
                        mStoreImageFile = finalFile.toString();

                        /*Picasso.with(LeaveApplyActivity.this).load("file://" + mStoreImageFile)
                                .placeholder(R.drawable.account)
                                .error(R.drawable.account)
                                .into(imgview);*/
                    }
                    mProgressBar.setVisibility(View.GONE);

                } catch (Exception ex) {
                    if (mProgressBar.isShown())
                        mProgressBar.setVisibility(View.GONE);
                    Common.printStackTrace(ex);
                }
            }
        } else if (requestCode == mRequestCodePDFFile && resultCode == Activity.RESULT_OK) {
            try {
                Uri selectedUri_PDF = data.getData();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mFilePath = getPath(selectedUri_PDF);
                    mStoreImageFile = mFilePath;
                    if (mFilePath != null && !mFilePath.equals("")) {
                        String[] parts = mFilePath.split("/");
                        mFilename = parts[parts.length - 1];
                        if (mFilename.contains(".pdf")) {
                            // mTxtPdf.setText(Html.fromHtml(mFilename));
                            mTxtSelectedPath.setVisibility(View.VISIBLE);
                            mImgRemove.setVisibility(View.VISIBLE);
                            mTxtSelectedPath.setText(Html.fromHtml(mFilename));
                        //    mStoreImageFile=mFilename;
                        } else {
                            Common.showToast(CurriculumResponseClassroomActivity.this, "Please Select PDF.");
                        }
                    } else {
                        Common.showToast(CurriculumResponseClassroomActivity.this, "Please Select from internal memory.");
                    }

                }
            } catch (Exception ex) {
                Common.showToast(CurriculumResponseClassroomActivity.this, "Please Select from internal memory.");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getPath(final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat) {
            // MediaStore (and general)
            return getForApi19(uri);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    private void getFtpDetail() {
        try {
            if (isOnline()) {
                Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                        .GetLeavePdfImageFTPDetail(StudentId, SchoolId, Year_Id, "3", String.valueOf(SchoolDetails.appname), BuildConfig.VERSION_CODE + "", Constants.PLATFORM);
                call.enqueue(new Callback<AppService>() {
                    @Override
                    public void onResponse(Call<AppService> call, Response<AppService> response) {
                        try {
                            AppService tmp = response.body();
                            if (tmp.getResponse().equalsIgnoreCase("1")) {

                                mid = tmp.getStrlist().get(0).getSecond();
                                secretKey = tmp.getStrlist().get(0).getThird();
                                bucket = tmp.getStrlist().get(0).getFirst();

                                Log.e("hbjd", mid + "\n" + secretKey + "\n" + bucket);
                            }
                        } catch (Exception ex) {
                            Constants.writelog(Tag, "getFtpDetail::701" + ex.getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<AppService> call, Throwable t) {
                        Constants.writelog(Tag, "getFtpDetail:709" + t.getMessage());

                    }
                });
            } else {

                Common.showToast(CurriculumResponseClassroomActivity.this, getString(R.string.msg_connection));
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    private String getForApi19(Uri uri) {
        Log.e(Tag, "+++ API 19 URI :: " + uri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                Log.e(Tag, "+++ Document URI");
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    Log.e(Tag, "+++ External Document URI");
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        Log.e(Tag, "+++ Primary External Document URI");
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    Log.e(Tag, "+++ Downloads External Document URI");
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    Log.e(Tag, "+++ Media Document URI");
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        Log.e(Tag, "+++ Image Media Document URI");
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        Log.e(Tag, "+++ Video Media Document URI");
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        Log.e(Tag, "+++ Audio Media Document URI");
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(contentUri, selection, selectionArgs);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                Log.e(Tag, "+++ No DOCUMENT URI :: CONTENT ");

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                Log.e(Tag, "+++ No DOCUMENT URI :: FILE ");
                return uri.getPath();
            }
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,
                "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

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
            if(isFrom.equalsIgnoreCase("notification")){
                Intent i=new Intent(CurriculumResponseClassroomActivity.this,HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
            CurriculumResponseClassroomActivity.this.finish();
            hideKeyboard(CurriculumResponseClassroomActivity.this);
            onBackClickAnimation();
            super.onBackPressed();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

}
