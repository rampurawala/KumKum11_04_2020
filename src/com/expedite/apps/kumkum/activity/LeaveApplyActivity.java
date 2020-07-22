package com.expedite.apps.kumkum.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.BuildConfig;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.common.Utility;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.fragment.DatePickerFragment;
import com.expedite.apps.kumkum.model.AppService;
import com.expedite.apps.kumkum.model.LeaveListModel;
import com.expedite.apps.kumkum.model.LeaveTypeModel;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.expedite.apps.kumkum.model.LoginDetail.getUserId;


public class LeaveApplyActivity extends BaseActivity implements View.OnClickListener {
    Common common;
    private ProgressBar mProgressBar;
    private String Tag = "LeaveApplyActivity";
    private TextView mBtnSave, mBtnBrowse, mTxtDate, mTxtEndDate, mTxtSelectedPath;
    private DatePickerFragment dtPickerFragment;
    private View mllDate, mllEndDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private String mCurrentFormatedDate = "", mSelectedLeaveTypeId = "";
    private List<String> mLeaveTypeNameArray = new ArrayList<>();
    private List<String> mLeaveTypeIdArray = new ArrayList<>();
    private Spinner mLeaveTypeSpinner;
    private ArrayAdapter<String> mLeaveTypeAdapter;
    private LayoutInflater viewInflater = null;
    private AlertDialog mSelectImageDialog;
    private int mRequestCodePhotoGallery = 1, mRequestCodePDFFile = 2;
    private String mFilename = "", mFilePath = "", storefilename = "", mStoreImageFile = "";
    private CheckBox mChkHalfDay;
    private String mHostname = "", mPassword = "", mFoldername = "", mPDFuserid = "";
    private Uri selectedImage;
    private EditText mEdtReason;
    private String bucket = "", secretKey = "", mid = "";
    private String SchoolId = "", StudentId = "", Year_Id = "";
    private ImageView remove, imgview;
    private AdView mAdView;

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_apply_layout);
        init();
        initListner();
    }

    public void init() {
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                setTitle("Leave Application");
            }
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.LeaveApplyActivity);
            showFullScreenAd(LeaveApplyActivity.this,ActivityNames.LeaveApplyActivity);
            remove = findViewById(R.id.remove);
            SchoolId = Datastorage.GetSchoolId(LeaveApplyActivity.this);
            StudentId = Datastorage.GetStudentId(LeaveApplyActivity.this);
            Year_Id = Datastorage.GetCurrentYearId(LeaveApplyActivity.this);
            mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
            mllDate = (View) findViewById(R.id.llDate);
            mllEndDate = (View) findViewById(R.id.llEndDate);
            mTxtDate = (TextView) findViewById(R.id.txtDate);
            mTxtEndDate = (TextView) findViewById(R.id.txtEndDate);
            mEdtReason = (EditText) findViewById(R.id.edtReason);
            mBtnSave = (TextView) findViewById(R.id.btnSave);
            imgview = findViewById(R.id.imgview);
            mBtnBrowse = (TextView) findViewById(R.id.btnBrowse);
            Date currentDate = Calendar.getInstance().getTime();
            mCurrentFormatedDate = dateFormat.format(currentDate);
            mTxtDate.setText(mCurrentFormatedDate);
            mTxtEndDate.setText(mCurrentFormatedDate);
            mLeaveTypeSpinner = (Spinner) findViewById(R.id.spnLeaveType);
            mTxtSelectedPath = (TextView) findViewById(R.id.txtSelectedPath);
            mChkHalfDay = (CheckBox) findViewById(R.id.checkBox);
            mChkHalfDay.setButtonDrawable(R.drawable.chk_bg_test);
            common = new Common(this);
            ShowHalfDay();

            mllDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTxtDate.getText() != null && !mTxtDate.getText().toString().isEmpty() &&
                            mTxtDate.getText().equals("Select Date")) {
                        dtPickerFragment = new DatePickerFragment()
                                .newInstance(mTxtDate, mCurrentFormatedDate, mCurrentFormatedDate);
                    } else {
                        dtPickerFragment = new DatePickerFragment()
                                .newInstance(mTxtDate, mTxtDate.getText().toString(), mCurrentFormatedDate);
                    }
                    dtPickerFragment.show(getSupportFragmentManager(), "");
                    // ShowHalfDay();
                }
            });
            mllEndDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTxtDate != null && mTxtDate.getText() != "") {
                        String startDate = mTxtDate.getText().toString();
                        if (mTxtEndDate.getText() != null && !mTxtEndDate.getText().toString().isEmpty() &&
                                mTxtEndDate.getText().equals("Select Date")) {
                            dtPickerFragment = new DatePickerFragment()
                                    .newInstance(mTxtEndDate, startDate, startDate);
                        } else {
                            dtPickerFragment = new DatePickerFragment()
                                    .newInstance(mTxtEndDate, mTxtEndDate.getText().toString(), startDate);
                        }
                    } else {
                        if (mTxtEndDate.getText() != null && !mTxtEndDate.getText().toString().isEmpty() &&
                                mTxtEndDate.getText().equals("Select Date")) {
                            dtPickerFragment = new DatePickerFragment()
                                    .newInstance(mTxtEndDate, mCurrentFormatedDate, mCurrentFormatedDate);
                        } else {
                            dtPickerFragment = new DatePickerFragment()
                                    .newInstance(mTxtEndDate, mTxtEndDate.getText().toString(), mCurrentFormatedDate);
                        }
                    }
                    dtPickerFragment.show(getSupportFragmentManager(), "");
                }
            });
            mLeaveTypeAdapter = new ArrayAdapter<String>(LeaveApplyActivity.this, R.layout
                    .school_spinner_item, mLeaveTypeNameArray) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    try {
                        ((TextView) v).setTextColor(getResources().getColor(R.color.viewname));
                        ((TextView) v).setTextSize(15);
                    } catch (Exception ex) {
                        Constants.writelog(Tag, "Ex 102:" + ex.getMessage());
                    }
                    return v;
                }
            };
            mLeaveTypeSpinner.setAdapter(mLeaveTypeAdapter);
            mLeaveTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mSelectedLeaveTypeId = mLeaveTypeIdArray.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            getLeaveType();
            getFtpDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListner() {
        mBtnSave.setOnClickListener(this);
        mBtnBrowse.setOnClickListener(this);
        remove.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                hideKeyboard(LeaveApplyActivity.this);
                if (mTxtDate.getText() == null || mTxtDate.getText().toString().isEmpty()) {
                    Toast.makeText(LeaveApplyActivity.this, "Please select from date", Toast.LENGTH_LONG).show();
                } else if (mTxtEndDate.getText() == null || mTxtEndDate.getText().toString().isEmpty()) {
                    Toast.makeText(LeaveApplyActivity.this, "Please select to date", Toast.LENGTH_LONG).show();
                } else if (mSelectedLeaveTypeId == null || mSelectedLeaveTypeId.isEmpty()
                        || mSelectedLeaveTypeId.equalsIgnoreCase("0")) {
                    Toast.makeText(LeaveApplyActivity.this, "Please select leave type", Toast.LENGTH_LONG).show();
                } else if (mEdtReason.getText() == null || mEdtReason.getText().toString().trim().isEmpty()) {
                    Toast.makeText(LeaveApplyActivity.this, "Please enter reason", Toast.LENGTH_LONG).show();
                } else {
                    if (isOnline()) {
                        if (mFilename != null && !mFilename.isEmpty()) {
                            uploadAttachedPdf();
                        } else if (mStoreImageFile != null && !mStoreImageFile.isEmpty()) {
                            uploadAttachedImage();
                        } else {
                            mTxtSelectedPath.setText("");
                            getLeaveApply();
                        }
                    } else {
                        Toast.makeText(LeaveApplyActivity.this, R.string.msg_connection, Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.btnBrowse:
                SelectPhoto();
                break;
            case R.id.remove:
                mFilePath = "";
                mFilename = "";
                mStoreImageFile = "";
                mTxtSelectedPath.setVisibility(View.GONE);
                mTxtSelectedPath.setText("");
                storefilename = "";
                imgview.setVisibility(View.GONE);
                remove.setVisibility(View.GONE);
                Toast.makeText(LeaveApplyActivity.this, "File Removed", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    public void ShowHalfDay() {
        try {
            if (mTxtDate != null && mTxtEndDate != null && mChkHalfDay != null) {
                if (mTxtDate.getText() != null && !mTxtDate.getText().toString().isEmpty()
                        && !mTxtDate.getText().equals("Select Date") && mTxtEndDate.getText() != null
                        && !mTxtEndDate.getText().toString().isEmpty() &&
                        !mTxtEndDate.getText().equals("Select Date") &&
                        mTxtDate.getText().toString().equalsIgnoreCase(mTxtEndDate.getText().toString())) {
                    mChkHalfDay.setVisibility(View.VISIBLE);
                } else {
                    mChkHalfDay.setVisibility(View.GONE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getLeaveApply() {
        if (isOnline()) {

            final ProgressDialog dialog = new ProgressDialog(LeaveApplyActivity.this);
            dialog.setMessage("Please Wait....");
            dialog.setCancelable(false);
            dialog.show();

            String mStartDate = "", mToDate = "", mReason = "", mDocument = "", mIsHalfDay = "0";
            if (mTxtDate.getText() != null && !mTxtDate.getText().toString().isEmpty()) {
                mStartDate = mTxtDate.getText().toString().trim();
            } else {
                mStartDate = "";
            }
            if (mTxtEndDate.getText() != null && !mTxtEndDate.getText().toString().isEmpty()) {
                mToDate = mTxtEndDate.getText().toString().trim();
            } else {
                mToDate = "";
            }
            if (mEdtReason.getText() != null && !mEdtReason.getText().toString().trim().isEmpty()) {
                mReason = mEdtReason.getText().toString().trim();
            } else {
                mReason = "";
            }
//            if (mTxtSelectedPath.getVisibility() == View.VISIBLE &&
//                    mTxtSelectedPath.getText() != null &&
//                    !mTxtSelectedPath.getText().toString().trim().isEmpty()) {
//                mDocument = mTxtSelectedPath.getText().toString().trim();
//            } else {
//                mDocument = "";
//            }
            if (storefilename != null && !storefilename.isEmpty()) {
                mDocument = storefilename.trim();
            } else {
                mDocument = "";
            }
            if (mChkHalfDay.getVisibility() == View.VISIBLE && mChkHalfDay.isChecked()) {
                mIsHalfDay = "1";
            } else {
                mIsHalfDay = "0";
            }

            // mProgressBar.setVisibility(View.VISIBLE);
            Call<LeaveListModel> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                    .GetLeaveApply(StudentId, SchoolId, mSelectedLeaveTypeId, mStartDate, mToDate, mReason,
                            mDocument, mIsHalfDay, BuildConfig.VERSION_CODE + "", Constants.PLATFORM);
            call.enqueue(new Callback<LeaveListModel>() {
                @Override
                public void onResponse(Call<LeaveListModel> call, Response<LeaveListModel> response) {
                    try {
                        LeaveListModel tmps = response.body();

                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1")) {
                            if (tmps.getMessage() != null && !tmps.getMessage().isEmpty())
                                Common.showToast(LeaveApplyActivity.this, tmps.getMessage());
                            Intent returnIntent = new Intent();
                            setResult(Constants.RESULT_LEAVE_APPLY_SUCCESS, returnIntent);
                            LeaveApplyActivity.this.finish();
                            onBackClickAnimation();
                        } else {
                            if (tmps != null && tmps.getMessage() != null && !tmps.getMessage().isEmpty())
                                Common.showToast(LeaveApplyActivity.this, tmps.getMessage());
                        }
                        if (dialog.isShowing())
                            dialog.dismiss();
                    } catch (Exception ex) {
                        if (dialog.isShowing())
                            dialog.dismiss();

                    }
                }

                @Override
                public void onFailure(Call<LeaveListModel> call, Throwable t) {
                    if (dialog.isShowing())
                        dialog.dismiss();
                }
            });
        } else {

            Common.showToast(LeaveApplyActivity.this, getString(R.string.msg_connection));
        }
    }

    private void getLeaveType() {
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            Call<LeaveTypeModel> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                    .GetLeaveType(SchoolId, BuildConfig.VERSION_CODE + "", Constants.PLATFORM);
            call.enqueue(new Callback<LeaveTypeModel>() {
                @Override
                public void onResponse(Call<LeaveTypeModel> call, Response<LeaveTypeModel> response) {
                    try {
                        LeaveTypeModel tmps = response.body();
                        mLeaveTypeIdArray.clear();
                        mLeaveTypeNameArray.clear();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1")) {
                            if (tmps.getLeavedetails() != null && tmps.getLeavedetails().size() > 0) {
                                mLeaveTypeIdArray.add("0");
                                mLeaveTypeNameArray.add("Select Leave Type");
                                for (int i = 0; i < tmps.getLeavedetails().size(); i++) {
                                    mLeaveTypeIdArray.add(tmps.getLeavedetails().get(i).getValue());
                                    mLeaveTypeNameArray.add(tmps.getLeavedetails().get(i).getName());
                                    mLeaveTypeAdapter.notifyDataSetChanged();
                                }
                            } else {
                                mLeaveTypeIdArray.add("0");
                                mLeaveTypeNameArray.add("Select Leave Type");
                                mLeaveTypeAdapter.notifyDataSetChanged();
                            }

                        } else {
                            mLeaveTypeIdArray.add("0");
                            mLeaveTypeNameArray.add("Select Leave Type");
                            mLeaveTypeAdapter.notifyDataSetChanged();
                        }
                        mProgressBar.setVisibility(View.GONE);
                    } catch (Exception ex) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<LeaveTypeModel> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Common.showToast(LeaveApplyActivity.this, getString(R.string.msg_connection));
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
                LeaveApplyActivity.this.finish();
                onBackClickAnimation();
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LeaveApplyActivity.this.finish();
        onBackClickAnimation();
    }

    private void SelectPhoto() {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(LeaveApplyActivity.this,
                    R.style.AlertDialogCustom);
            viewInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View promptView = viewInflater.inflate(R.layout.select_image_dialog_layout, null);
            alertDialog.setView(promptView);
            final boolean result = Utility.checkPermission(LeaveApplyActivity.this);
            RelativeLayout mLLCamera = (RelativeLayout) promptView.findViewById(R.id.llcamera);
            RelativeLayout mLLGallery = (RelativeLayout) promptView.findViewById(R.id.llgallery);
            TextView mTxtcancel = (TextView) promptView.findViewById(R.id.btnCancle);
            mLLCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgview.setVisibility(View.GONE);
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
                        // for Multiple Image
//                        startActivityForResult(new Intent(Intent.ACTION_PICK,
//                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                                        .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true),
//                                mRequestCodePhotoGallery);
                        // for Single Image
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

    private void uploadAttachedPdf() {
        try {
            if (mFilename != null && !mFilename.isEmpty()) {
                File file = new File(mFilePath);
                if (file.exists()) {
                    String location = "";
                    UploadPdfFileTask mTask = new UploadPdfFileTask();
                    mTask.execute(location);
                } else {
                    Common.showToast(LeaveApplyActivity.this, "File does not exists.");
                }
            } else {
                getLeaveApply();
            }
        } catch (Exception ex) {
            Constants.writelog(Tag, "uploadAttachedPdf():358:" + ex.getMessage());
        }
    }

    private void uploadAttachedImage() {
        try {
            if (mStoreImageFile != null && !mStoreImageFile.isEmpty()) {
                String location = "";
                UploadImageFileTask mTask = new UploadImageFileTask();
                mTask.execute(location);
            } else {
                getLeaveApply();
            }
        } catch (Exception ex) {
            Constants.writelog(Tag, "uploadAttachedImage():471:" + ex.getMessage());
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;

    }

    private void getFtpDetail() {
        try {
            if (isOnline()) {

                Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                        .GetLeavePdfImageFTPDetail(StudentId, SchoolId, Year_Id,"1",String.valueOf(SchoolDetails.appname), BuildConfig.VERSION_CODE + "", Constants.PLATFORM);
                call.enqueue(new Callback<AppService>() {
                    @Override
                    public void onResponse(Call<AppService> call, Response<AppService> response) {
                        try {
                            AppService tmp = response.body();
                            if (tmp.getResponse().equalsIgnoreCase("1")) {
                             /*   mHostname = tmp.getHost();
                                mFoldername = tmp.getFolder();
                                mPDFuserid = tmp.getUserID();
                                mPassword = tmp.getPwd();*/
                                mid = tmp.getStrlist().get(0).getSecond();
                                secretKey = tmp.getStrlist().get(0).getThird();
                                bucket = tmp.getStrlist().get(0).getFirst();
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

                Common.showToast(LeaveApplyActivity.this, getString(R.string.msg_connection));
            }
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
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        final ClipData clipData = data.getClipData();
                        if (clipData == null) {
                            selectedImage = data.getData();
                            Bitmap bitmap = MediaStore.Images.Media.
                                    getBitmap(getContentResolver(), selectedImage);
                            Uri tempUri = getImageUri(LeaveApplyActivity.this, bitmap);
                            // CALL THIS METHOD TO GET THE ACTUAL PATH
                            File finalFile = new File(getRealPathFromURI(tempUri));
                            mTxtSelectedPath.setVisibility(View.VISIBLE);
                            remove.setVisibility(View.VISIBLE);
                            //mTxtSelectedPath.setText(selectedImage.toString());
                            mTxtSelectedPath.setText(finalFile.getName());
                            mStoreImageFile = finalFile.toString();
                            imgview.setVisibility(View.VISIBLE);
                            Picasso.with(LeaveApplyActivity.this).load("file://" + mStoreImageFile)
                                    .placeholder(R.drawable.account)
                                    .error(R.drawable.account)
                                    .into(imgview);
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
                                            Uri tempUri = getImageUri(LeaveApplyActivity.this, bitmap);
                                            // CALL THIS METHOD TO GET THE ACTUAL PATH
                                            File finalFile = new File(getRealPathFromURI(tempUri));
                                            mTxtSelectedPath.setVisibility(View.VISIBLE);
                                            remove.setVisibility(View.VISIBLE);
                                            //mTxtSelectedPath.setText(selectedImage.toString());
                                            mTxtSelectedPath.setText(finalFile.getName());
                                            mStoreImageFile = finalFile.toString();
                                            imgview.setVisibility(View.VISIBLE);
                                            Picasso.with(LeaveApplyActivity.this).load("file://" + mStoreImageFile)
                                                    .placeholder(R.drawable.account)
                                                    .error(R.drawable.account)
                                                    .into(imgview);
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
                        Uri tempUri = getImageUri(LeaveApplyActivity.this, bitmap);
                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        File finalFile = new File(getRealPathFromURI(tempUri));
                        mTxtSelectedPath.setVisibility(View.VISIBLE);
                        remove.setVisibility(View.VISIBLE);
                        //mTxtSelectedPath.setText(selectedImage.toString());
                        mTxtSelectedPath.setText(finalFile.getName());
                        mStoreImageFile = finalFile.toString();
                        imgview.setVisibility(View.VISIBLE);
                        Picasso.with(LeaveApplyActivity.this).load("file://" + mStoreImageFile)
                                .placeholder(R.drawable.account)
                                .error(R.drawable.account)
                                .into(imgview);
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
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    mFilePath = getPath(selectedUri_PDF);
                    if (mFilePath != null && !mFilePath.equals("")) {
                        String[] parts = mFilePath.split("/");
                        mFilename = parts[parts.length - 1];
                        if (mFilename.contains(".pdf")) {
                            // mTxtPdf.setText(Html.fromHtml(mFilename));
                            mTxtSelectedPath.setVisibility(View.VISIBLE);
                            remove.setVisibility(View.VISIBLE);
                            mTxtSelectedPath.setText(Html.fromHtml(mFilename));
                            imgview.setVisibility(View.GONE);
                        } else {
                            Common.showToast(LeaveApplyActivity.this, "Please Select PDF.");
                        }
                    } else {
                        Common.showToast(LeaveApplyActivity.this, "Please Select from internal memory.");
                    }

                }
            } catch (Exception ex) {
                Common.showToast(LeaveApplyActivity.this, "Please Select from internal memory.");
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

    private class UploadPdfFileTask extends AsyncTask<String, Void, String> {
        boolean result = false;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // mProgressBar.setVisibility(View.VISIBLE);
            dialog = new ProgressDialog(LeaveApplyActivity.this);
            dialog.setMessage("Please Wait....");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String[] params) {
            // do above Server call here
            try {
                if (mFilename != null && !mFilename.equalsIgnoreCase("")) {
                    String mGetIds = "";
                    if (getUserId() != null && !getUserId().isEmpty()) {
                        String getIds = getUserId();
                        mGetIds = getIds.replaceAll(",", "_");
                    }
                    storefilename = SchoolId + "_" + StudentId + "_" + Year_Id + "_"
                            + Common.GetTimeTicks().toString() + ".pdf";
                    BasicAWSCredentials credentials = new BasicAWSCredentials(mid, secretKey);
                    AmazonS3Client s3 = new AmazonS3Client(credentials);
                    s3.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
                    TransferUtility transferUtility = new TransferUtility(s3, LeaveApplyActivity.this);
                    TransferObserver observer = transferUtility.upload(
                            bucket,
                            storefilename,
                            new File(mFilePath)
                    );
                    result = true;
                }
            } catch (Exception e) {
                Log.e("count", "error");
                e.printStackTrace();
            }
            return String.valueOf(result);


            /*FTPClient ftpClient = null;
            try {
                ftpClient = new FTPClient();
                ftpClient.connect(InetAddress.getByName(mHostname));
                if (ftpClient.login(mPDFuserid, mPassword)) {
                    ftpClient.enterLocalPassiveMode(); // important!
                    if (!mFoldername.isEmpty()) {
                    } else {
                        ftpClient.changeWorkingDirectory(mFoldername);
                    }
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    FileInputStream in = null;

                    if (mFilename != null && !mFilename.equalsIgnoreCase("")) {
                        storefilename = SchoolId + "_" + StudentId + "_" + Year_Id + "_"
                                + Common.GetTimeTicks().toString() + ".pdf";
                        //  in = new FileInputStream(new File(data));
                        in = new FileInputStream(new File(mFilePath));
                        result = ftpClient.storeFile(storefilename, in);
                        if (result) {
                            //Common.showToast(LeaveApplyActivity.this, String.valueOf(result));
                            Log.e("upload result", String.valueOf(result));
                        }
                    }

                    if (in != null)
                        in.close();
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (Exception e) {
                Log.e("count", "error");
                e.printStackTrace();
            }
            return String.valueOf(result);*/
        }

        @Override
        protected void onPostExecute(String message) {
            //mProgressBar.setVisibility(View.GONE);
            if (dialog.isShowing())
                dialog.dismiss();
            if (message.equalsIgnoreCase("true")) {
                getLeaveApply();
            } else {
                Common.showToast(LeaveApplyActivity.this, "Please Try Again.");
            }
        }
    }

    private class UploadImageFileTask extends AsyncTask<String, Void, String> {
        boolean result = false;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mProgressBar.setVisibility(View.VISIBLE);
            dialog = new ProgressDialog(LeaveApplyActivity.this);
            dialog.setMessage("Please Wait....");
            dialog.setCancelable(false);
            dialog.show();
        }


        @Override
        protected String doInBackground(String[] params) {
            // do above Server call here
            FTPClient ftpClient = null;

            try {
                if (mStoreImageFile != null && !mStoreImageFile.equalsIgnoreCase("")) {
                    String mGetIds = "";
                    if (getUserId() != null && !getUserId().isEmpty()) {
                        String getIds = getUserId();
                        mGetIds = getIds.replaceAll(",", "_");
                    }
                    storefilename = SchoolId + "_" + StudentId + "_" + Year_Id + "_"
                            + Common.GetTimeTicks().toString() + ".jpg";

                    BasicAWSCredentials credentials = new BasicAWSCredentials(mid, secretKey);
                    AmazonS3Client s3 = new AmazonS3Client(credentials);
                    s3.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
                    TransferUtility transferUtility = new TransferUtility(s3, LeaveApplyActivity.this);
                    TransferObserver observer = transferUtility.upload(
                            bucket,
                            storefilename,
                            new File(mStoreImageFile)
                    );
                    result = true;
                }

            } catch (Exception e) {
                Log.e("count", "error");
                e.printStackTrace();
            }
            return String.valueOf(result);



          /*  FTPClient ftpClient = null;

            try {
                ftpClient = new FTPClient();
                ftpClient.connect(InetAddress.getByName(mHostname));

                if (ftpClient.login(mPDFuserid, mPassword)) {
                    ftpClient.enterLocalPassiveMode();
                    if (mFoldername.equalsIgnoreCase("")) {
                    } else {
                        ftpClient.changeWorkingDirectory(mFoldername);
                    }
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    FileInputStream in = null;
                    String data = "";
                    //Image Resize
                    Bitmap bm = BitmapFactory.decodeFile(mStoreImageFile);
                    int origWidth = bm.getWidth();
                    int origHeight = bm.getHeight();
                    int destWidth = 0;
                    int destHeight = 0;

                    if (origWidth >= origHeight) {
                        destWidth = 1000;
                        destHeight = (origHeight * destWidth) / origWidth;
                        Bitmap b2 = getResizedBitmap(bm, destHeight, destWidth);
                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                        // compress to the format you want, JPEG, PNG...
                        // 70 is the 0-100 quality percentage
                        b2.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                        // we save the file, at least until we have made use of it
                        File f = new File(Environment.getExternalStorageDirectory()
                                + File.separator + "test.jpg");
                        f.createNewFile();
                        //write the bytes in file
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(outStream.toByteArray());
                        // remember close de FileOutput
                        fo.close();
                        data = f.getPath();

                    } else if (origWidth <= origHeight) {
                        destHeight = 1000;
                        destWidth = (origWidth * destHeight) / origHeight;

                        Bitmap b2 = getResizedBitmap(bm, destHeight, destWidth);
                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                        // compress to the format you want, JPEG, PNG...
                        // 70 is the 0-100 quality percentage
                        b2.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                        // we save the file, at least until we have made use of it
                        File f = new File(Environment.getExternalStorageDirectory()
                                + File.separator + "test" + 0 + ".jpg");
                        f.createNewFile();
                        //write the bytes in file
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(outStream.toByteArray());
                        // remember close de FileOutput
                        fo.close();
                        data = f.getPath();
                    } else {
                        data = mStoreImageFile;
                    }
                    storefilename = SchoolId + "_" + StudentId + "_" + Year_Id + "_"
                            + Common.GetTimeTicks().toString() + ".jpg";
                    in = new FileInputStream(new File(data));
                    result = ftpClient.storeFile(storefilename, in);
                    if (result) {
                        // Common.showToast(LeaveApplyActivity.this, String.valueOf(result));
                        Log.e("upload result", String.valueOf(result));

                    }

                    if (in != null)
                        in.close();
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (Exception e) {
                Log.e("count", "error");
                e.printStackTrace();
            }
            return String.valueOf(result);*/
        }

        @Override
        protected void onPostExecute(String message) {
            // mProgressBar.setVisibility(View.GONE);
            if (dialog.isShowing())
                dialog.dismiss();
            if (message.equalsIgnoreCase("true")) {
                getLeaveApply();
            } else {
                Common.showToast(LeaveApplyActivity.this, "Please Try Again.");
            }

        }
    }

}
