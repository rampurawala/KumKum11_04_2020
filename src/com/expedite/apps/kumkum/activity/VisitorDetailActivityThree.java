package com.expedite.apps.kumkum.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.model.VisitorDetailModel;
import com.google.android.gms.ads.AdView;

public class VisitorDetailActivityThree extends BaseActivity {

    private EditText edtTotalVisitors, edtTab, edtLaptop, edtPendrive, edtOthers;
    private ImageView mAddbtn;
    // private RecyclerView mRecyclerview;
    private TextView mtxtSubmit;
    private String Tag = "VisitorDetailActivityThree";
    private String mTotalVisitor, mTab, mLaptop, mPendrive, mOthers;
    private LinearLayout llExtraVisitor;
    VisitorDetailModel obj = new VisitorDetailModel();
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_detail_three);
        init();
    }

    private void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.VisitorDetailActivityThree);
            showFullScreenAd(VisitorDetailActivityThree.this, ActivityNames.VisitorDetailActivityThree);
            edtTotalVisitors = (EditText) findViewById(R.id.edtTotalVisitors);
            edtTab = (EditText) findViewById(R.id.edtTab);
            mAddbtn = (ImageView) findViewById(R.id.addVisitor);
            edtLaptop = (EditText) findViewById(R.id.edtLaptop);
            edtPendrive = (EditText) findViewById(R.id.edtPendrive);
            edtOthers = (EditText) findViewById(R.id.edtAnyOther);
            mtxtSubmit = (TextView) findViewById(R.id.txtSubmit);
            llExtraVisitor = (LinearLayout) findViewById(R.id.LLextraVisitors);

            mAddbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View mRow = getLayoutInflater().inflate(R.layout.extra_visitor_row, null, false);
                    EditText edtName = mRow.findViewById(R.id.edtName);
                    EditText edtMobile = mRow.findViewById(R.id.edtMobNo);
                    Button mRemove = mRow.findViewById(R.id.btnRemoveRow);
                    mRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            llExtraVisitor.removeView((View) v.getParent());
                        }
                    });
                    llExtraVisitor.addView(mRow);
                }
            });


            // mRecyclerview = (RecyclerView) findViewById(R.id.extravisitorRecyclerview);
            //obj.setTotalVisitor(Integer.parseInt(edtTotalVisitors.getText().toString().trim()));
            //obj.setLaptop(edtLaptop.getText().toString().trim());
            //obj.setPendrive(edtPendrive.getText().toString().trim());
            //obj.setTab(edtTab.getText().toString().trim());
            //obj.setOthers(edtOthers.getText().toString().trim());
            mtxtSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTotalVisitor = edtTotalVisitors.getText().toString().trim();
                    mOthers = edtOthers.getText().toString().trim();
                    mTab = edtTab.getText().toString().trim();
                    mLaptop = edtLaptop.getText().toString().trim();
                    mPendrive = edtPendrive.getText().toString().trim();
                }
            });

        } catch (Exception e) {
            Common.showLog(Tag, "init():::55" + e.getMessage());
        }
    }
}
