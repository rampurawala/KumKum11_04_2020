package com.expedite.apps.kumkum.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Utility;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.VisitorDetailModel;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class VisitorDetailActivityOne extends BaseActivity {

    private Spinner spnVisitorType, spnParentsType, spnGuardianType, spnWhomToMeet;
    private EditText edtOtherType, edtVisitorName, edtVisitorMolNo, edtPurpose;
    private ImageView imgProfile;
    private View llParents, llGaurdian, llOthers;
    private TextView mNextbtn, txtcaptureFrombtn;
    private String Tag = "VisitorDetailActivityOne";
    private List<String> mVisitorTypeArray = new ArrayList<>(), mParentsTypedArray = new ArrayList<>(),
            mGuardianTypeArray = new ArrayList<>(), mWhomToMeetArray = new ArrayList<>(),
            mVisitorTypeIDArray = new ArrayList<>(), mParentsTypedIDArray = new ArrayList<>(),
            mGuardianTypeIdArray = new ArrayList<>(), mWhomToMeetIDArray = new ArrayList<>();
    private String mSelectedVisitorType, mSelectedGaurdianType, mSelectedWhomToMeet, mSelectedParentType;
    private ArrayAdapter<String> mVisitorTypeAdapter, mParentsTypeAdapter, mGuardianTypeAdapter, mWhomToMeetAdpter;
    private int selectedVisitorTypeIndex = 0, selectedParentTypeIndex = 0, selectedGaurdianTypeIndex = 0, selectedWhomeToMeetIndex = 0;
    public static final int CAMERA_PHOTO_REQUEST_CODE = 111;
    VisitorDetailModel obj = new VisitorDetailModel();
    private String mOthers = "", mVisitorName = "", mMobileNumber = "", mPurpose = "";
    private String MobilePattern = "[0-9]{10}";
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_detail_one);
        init();
    }

    private void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.VisitorDetailActivityOne);
            Constants.setActionbar(getSupportActionBar(), VisitorDetailActivityOne.this,
                    VisitorDetailActivityOne.this, "Visitor Form", "VisitorForm", ActivityNames.VisitorDetailActivityOne);
            spnVisitorType = (Spinner) findViewById(R.id.spnVisitorType);
            spnParentsType = (Spinner) findViewById(R.id.spnParentsType);
            spnGuardianType = (Spinner) findViewById(R.id.spnGuardianType);
            spnWhomToMeet = (Spinner) findViewById(R.id.spnWhomToMeet);
            edtOtherType = (EditText) findViewById(R.id.edtOtherType);
            edtVisitorName = (EditText) findViewById(R.id.edtVisitorName);
            edtVisitorMolNo = (EditText) findViewById(R.id.edtVisitorMolNo);
            edtPurpose = (EditText) findViewById(R.id.edtPurpose);
            imgProfile = (ImageView) findViewById(R.id.imgProfile);
            txtcaptureFrombtn = (TextView) findViewById(R.id.txtcaptureFrom);
            mNextbtn = (TextView) findViewById(R.id.txtNextbtn);
            llParents = (View) findViewById(R.id.llParents);
            llGaurdian = (View) findViewById(R.id.llGuardian);
            llOthers = (View) findViewById(R.id.llOther);
            //obj.setVistorName(edtVisitorName.getText().toString().trim());
            //obj.setVistorMobileNumber(edtVisitorMolNo.getText().toString().trim());
            //obj.setVisitorPurpose(edtPurpose.getText().toString().trim());
            mVisitorTypeAdapter = new ArrayAdapter<String>(VisitorDetailActivityOne.this, R.layout.school_spinner_item, mVisitorTypeArray) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.viewname));
                    ((TextView) v).setTextSize(15);
                    return v;
                }
            };
            spnVisitorType.setAdapter(mVisitorTypeAdapter);
            spnVisitorType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != selectedVisitorTypeIndex) {
                        selectedVisitorTypeIndex = position;
                        mSelectedVisitorType = mVisitorTypeIDArray.get(position);
                        if (mSelectedVisitorType != null && !mSelectedVisitorType.isEmpty()) {
                            if (mSelectedVisitorType.equalsIgnoreCase("1")) {
                                llParents.setVisibility(View.VISIBLE);
                                llGaurdian.setVisibility(View.GONE);
                                llOthers.setVisibility(View.GONE);
                            } else if (mSelectedVisitorType.equalsIgnoreCase("2")) {
                                llParents.setVisibility(View.GONE);
                                llGaurdian.setVisibility(View.VISIBLE);
                                llOthers.setVisibility(View.GONE);
                            } else if (mSelectedVisitorType.equalsIgnoreCase("3")) {
                                llParents.setVisibility(View.GONE);
                                llGaurdian.setVisibility(View.GONE);
                                llOthers.setVisibility(View.VISIBLE);
                            } else {
                                llParents.setVisibility(View.GONE);
                                llGaurdian.setVisibility(View.GONE);
                                llOthers.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mParentsTypeAdapter = new ArrayAdapter<String>(VisitorDetailActivityOne.this, R.layout.school_spinner_item, mParentsTypedArray) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.viewname));
                    ((TextView) v).setTextSize(15);
                    return v;
                }
            };
            spnParentsType.setAdapter(mParentsTypeAdapter);
            spnParentsType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != selectedParentTypeIndex) {
                        selectedParentTypeIndex = position;
                        mSelectedParentType = mParentsTypedIDArray.get(position);
                        //obj.setParents(mSelectedParentType);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            mGuardianTypeAdapter = new ArrayAdapter<String>(VisitorDetailActivityOne.this, R.layout.school_spinner_item, mGuardianTypeArray) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.viewname));
                    ((TextView) v).setTextSize(15);
                    return v;
                }
            };
            spnGuardianType.setAdapter(mGuardianTypeAdapter);
            spnGuardianType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != selectedGaurdianTypeIndex) {
                        selectedGaurdianTypeIndex = position;
                        mSelectedGaurdianType = mGuardianTypeIdArray.get(position);
                        //obj.setGaurdian(mSelectedGaurdianType);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mWhomToMeetAdpter = new ArrayAdapter<String>(VisitorDetailActivityOne.this, R.layout.school_spinner_item, mWhomToMeetArray) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.viewname));
                    ((TextView) v).setTextSize(15);
                    return v;
                }
            };
            spnWhomToMeet.setAdapter(mWhomToMeetAdpter);
            spnWhomToMeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position != selectedWhomeToMeetIndex) {
                        selectedWhomeToMeetIndex = position;
                        mSelectedWhomToMeet = mWhomToMeetIDArray.get(position);
                        //obj.setWhomeToMeet(mSelectedWhomToMeet);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mNextbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mOthers = edtOtherType.getText().toString().trim();
                        mVisitorName = edtVisitorName.getText().toString().trim();
                        mMobileNumber = edtVisitorMolNo.getText().toString().trim();
                        mPurpose = edtPurpose.getText().toString().trim();
                        if (mSelectedVisitorType == null || mSelectedVisitorType.isEmpty() || mSelectedVisitorType.equalsIgnoreCase("-1")) {
                            Common.showToast(VisitorDetailActivityOne.this, "Please Select Visitor Type");
                        } else if (llParents.getVisibility() == View.VISIBLE && (mSelectedParentType == null || mSelectedParentType.isEmpty() || mSelectedParentType.equalsIgnoreCase("-1"))) {
                            Common.showToast(VisitorDetailActivityOne.this, "Please Select Parents Type");
                        } else if (llGaurdian.getVisibility() == View.VISIBLE && (mSelectedGaurdianType == null || mSelectedGaurdianType.isEmpty() || mSelectedGaurdianType.equalsIgnoreCase("-1"))) {
                            Common.showToast(VisitorDetailActivityOne.this, "Please Select Gaurdian Type");
                        } else if (llOthers.getVisibility() == View.VISIBLE && (mOthers == null || mOthers.isEmpty())) {
                            Common.showToast(VisitorDetailActivityOne.this, "Please Enter other");
                        } else if (mVisitorName == null || mVisitorName.isEmpty()) {
                            Common.showToast(VisitorDetailActivityOne.this, "Please Enter visitor name");
                        } else if (mMobileNumber == null || mMobileNumber.isEmpty()) {
                            Common.showToast(VisitorDetailActivityOne.this, "Please Enter mobile Number");
                        } else if (!mMobileNumber.matches(MobilePattern)) {
                            Common.showToast(VisitorDetailActivityOne.this, "Please Enter valid mobile Number");
                        } else if (mPurpose == null || mPurpose.isEmpty()) {
                            Common.showToast(VisitorDetailActivityOne.this, "Please Enter Purpose of your Visit");
                        } else if (mSelectedWhomToMeet == null || mSelectedWhomToMeet.isEmpty() || mSelectedWhomToMeet.equalsIgnoreCase("-1")) {
                            Common.showToast(VisitorDetailActivityOne.this, "Please select to whom you want to meet ");
                        } else {
                            Intent intent = new Intent(VisitorDetailActivityOne.this, VisitorDetailActivityTwo.class);
                            startActivity(intent);
                            onClickAnimation();
                        }

                    } catch (Exception e) {
                        Constants.writelog(Tag, "Onclick():::178 " + e.getMessage());
                    }
                }
            });

            txtcaptureFrombtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final boolean result = Utility.checkCameraPermission(VisitorDetailActivityOne.this);
                    if (result) {
                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                            photoCaptureIntent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                            photoCaptureIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                            photoCaptureIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                        } else {
                            photoCaptureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                        }

                        startActivityForResult(photoCaptureIntent, CAMERA_PHOTO_REQUEST_CODE);
                    }
                }
            });
            setVisitorType();
            setParentsType();
            setGardianType();
            setWhomeToMeet();
        } catch (Exception e) {
            Common.showLog(Tag, "init():::193 " + e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PHOTO_REQUEST_CODE) {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgProfile.setImageBitmap(bitmap);
                txtcaptureFrombtn.setVisibility(View.GONE);
            } catch (Exception e) {
                Common.printStackTrace(e);
            }
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
                hideKeyboard(VisitorDetailActivityOne.this);
                VisitorDetailActivityOne.this.finish();
                onBackClickAnimation();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setVisitorType() {
        mVisitorTypeArray.add("Select Type");
        mVisitorTypeArray.add("Parents");
        mVisitorTypeArray.add("Gaurdian");
        mVisitorTypeArray.add("Others");
        mVisitorTypeIDArray.add("-1");
        mVisitorTypeIDArray.add("1");
        mVisitorTypeIDArray.add("2");
        mVisitorTypeIDArray.add("3");
        mVisitorTypeAdapter.notifyDataSetChanged();
    }

    public void setParentsType() {
        mParentsTypedArray.add("Select Type");
        mParentsTypedArray.add("Mother");
        mParentsTypedArray.add("Father");

        mParentsTypedIDArray.add("-1");
        mParentsTypedIDArray.add("1");
        mParentsTypedIDArray.add("2");
        mParentsTypeAdapter.notifyDataSetChanged();
    }

    private void setGardianType() {
        mGuardianTypeArray.add("Select Gaurdian");
        mGuardianTypeArray.add("Brother");
        mGuardianTypeArray.add("Sister");
        mGuardianTypeArray.add("GrandMother");
        mGuardianTypeArray.add("GrandFather");

        mGuardianTypeIdArray.add("-1");
        mGuardianTypeIdArray.add("1");
        mGuardianTypeIdArray.add("2");
        mGuardianTypeIdArray.add("3");
        mGuardianTypeIdArray.add("4");
        mGuardianTypeAdapter.notifyDataSetChanged();

    }

    private void setWhomeToMeet() {
        mWhomToMeetArray.add("Select Whom You want to meet");
        mWhomToMeetArray.add("Teacher1");
        mWhomToMeetArray.add("Teacher2");
        mWhomToMeetArray.add("Teacher3");
        mWhomToMeetArray.add("Teacher4");
        mWhomToMeetIDArray.add("-1");
        mWhomToMeetIDArray.add("1");
        mWhomToMeetIDArray.add("2");
        mWhomToMeetIDArray.add("3");
        mWhomToMeetIDArray.add("4");
        mWhomToMeetAdpter.notifyDataSetChanged();
    }
}
