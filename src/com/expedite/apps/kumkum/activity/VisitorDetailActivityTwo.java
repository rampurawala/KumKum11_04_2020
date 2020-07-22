package com.expedite.apps.kumkum.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Patterns;
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
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.VisitorDetailModel;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class VisitorDetailActivityTwo extends BaseActivity {
    private Spinner spnProofType;
    private EditText edtEmail, edtAddress;
    private TextView txtNextbtn;
    private ImageView mImageProof, mImgDeleteFroof;
    private View llCaptureImg;
    private List<String> mProofTypeArray = new ArrayList<>(), mProofTypeIDArray = new ArrayList<>();
    private ArrayAdapter<String> mProofTypeAdapter;
    private int selectedProofTypeIndex = 0;
    private String mSelectedProofType;
    private final int requestCode = 111;
    private String mEmail="",mAddress="";
    private String Tag = "VisitorDetailActivityTwo";
    VisitorDetailModel obj = new VisitorDetailModel();
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_detail_two);
        init();
    }

    private void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.VisitorDetailActivityTwo);
            Constants.setActionbar(getSupportActionBar(), VisitorDetailActivityTwo.this,
                    VisitorDetailActivityTwo.this, "Visitor Form", "VisitorForm", ActivityNames.VisitorDetailActivityTwo);
            spnProofType = (Spinner) findViewById(R.id.spnProofType);
            edtEmail = (EditText) findViewById(R.id.edtmailid);
            edtAddress = (EditText) findViewById(R.id.edtaddress);
            llCaptureImg = (View) findViewById(R.id.captureImg);
            txtNextbtn = (TextView) findViewById(R.id.txtNext);
            mImageProof = (ImageView) findViewById(R.id.imgProof);
            mImgDeleteFroof = (ImageView) findViewById(R.id.imgDeletePrrof);

            //obj.setVisitorEMail(edtEmail.getText().toString().trim());
            //obj.setAddress(edtAddress.getText().toString().trim());

            mProofTypeAdapter = new ArrayAdapter<String>(VisitorDetailActivityTwo.this, R.layout.school_spinner_item, mProofTypeArray) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.viewname));
                    ((TextView) v).setTextSize(15);
                    return v;
                }
            };
            spnProofType.setAdapter(mProofTypeAdapter);
            spnProofType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != selectedProofTypeIndex) {
                        selectedProofTypeIndex = position;
                        mSelectedProofType = mProofTypeIDArray.get(position);
                        //obj.setIdentityProofName(mSelectedProofType);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            mImgDeleteFroof.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImgDeleteFroof.setVisibility(View.GONE);
                    mImageProof.setImageResource(0);
                }
            });

            txtNextbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mEmail=edtEmail.getText().toString().trim();
                        mAddress=edtAddress.getText().toString().trim();
                        if(mEmail==null|| mEmail.isEmpty()){
                            Common.showToast(VisitorDetailActivityTwo.this, "Please Enter Email Address");
                        }else if(!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
                            Common.showToast(VisitorDetailActivityTwo.this, "Please Enter valid Email Address");
                        }else if(mAddress==null || mAddress.isEmpty()){
                            Common.showToast(VisitorDetailActivityTwo.this, "Please Enter Address");
                        }else if(mSelectedProofType==null || mSelectedProofType.isEmpty() || mSelectedProofType.equalsIgnoreCase("-1")){
                            Common.showToast(VisitorDetailActivityTwo.this, "Please select any one Identity Proof");
                        }else if(mImageProof.getDrawable()==null){
                            Common.showToast(VisitorDetailActivityTwo.this, "Please upload any one Identity Proof");
                        }else {
                            Intent intent=new Intent(VisitorDetailActivityTwo.this,VisitorDetailActivityThree.class);
                            startActivity(intent);
                            onClickAnimation();
                        }
                    } catch (Exception e) {
                        Constants.writelog(Tag,"NextOnclick():::97 " + e.getMessage());
                    }
                }
            });

            llCaptureImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(photoCaptureIntent, requestCode);
                }
            });

            setIdentityProof();

        } catch (Exception e) {
            Common.showLog(Tag,"init():::131" + e.getMessage());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && resultCode == RESULT_OK) {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                mImageProof.setImageBitmap(bitmap);
                mImgDeleteFroof.setVisibility(View.VISIBLE);
            }catch (Exception e){
                Common.showLog(Tag,"onActivityResult():::146" + e.getMessage());
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
                hideKeyboard(VisitorDetailActivityTwo.this);
                VisitorDetailActivityTwo.this.finish();
                onBackClickAnimation();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setIdentityProof() {
        mProofTypeArray.add("Select Proof");
        mProofTypeArray.add("Aadhar Card");
        mProofTypeArray.add("Driving License");
        mProofTypeArray.add("PAN Card");
        mProofTypeArray.add("Employee Card");
        mProofTypeIDArray.add("-1");
        mProofTypeIDArray.add("1");
        mProofTypeIDArray.add("2");
        mProofTypeIDArray.add("3");
        mProofTypeIDArray.add("4");
        mProofTypeAdapter.notifyDataSetChanged();
    }
}
