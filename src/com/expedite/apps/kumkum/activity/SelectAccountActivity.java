package com.expedite.apps.kumkum.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class SelectAccountActivity extends BaseActivity {

    String tag="SelectAccountActivity";
    AccountAdapter madapter;
    RecyclerView mRecyclerview;
    ArrayList<String> marray = new ArrayList<>();
    private DatabaseHandler db;
    private AdView mAdView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account);
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.SelectAccountActivity);
            Constants.setActionbar(getSupportActionBar(), SelectAccountActivity.this, SelectAccountActivity.this,
                    "Select Default Account", "SelectAccountActivity", ActivityNames.SelectAccountActivity);
            marray = getIntent().getStringArrayListExtra("AcArray");
            db = new DatabaseHandler(SelectAccountActivity.this);
            mRecyclerview = (RecyclerView) findViewById(R.id.aclistrecycler);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(SelectAccountActivity.this));
            madapter = new AccountAdapter(SelectAccountActivity.this, marray);
            mRecyclerview.setAdapter(madapter);
        }catch (Exception ex){
            Constants.writelog(tag,"onCreate:50:"+ex.getMessage());
        }
    }

    private class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

        Activity activity;
        ArrayList<String> marray;

        public AccountAdapter(Activity activity, ArrayList<String> marray) {
            this.activity = activity;
            this.marray = marray;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            try {
                final String[] parts = marray.get(position).split(",");
                holder.mTxtAcName.setText(Html.fromHtml(parts[0]));

                holder.mLLAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectAccountActivity.this);
                        alertDialog.setTitle("Information");
                        alertDialog.setMessage("Do you really want to make student as default?");
                        alertDialog.setIcon(R.drawable.information);
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            int temp = db.UpdateSetIsDefault_Zero_to_One();
                                            int Set_Default = db.UpdateContactRecordSetDefaultOne(
                                                    parts[1], parts[2]);
                                            Datastorage.SetLastAutoUpdateExamDay(SelectAccountActivity.this, 0);
                                            Datastorage.SetLastAutoUpdateMessageDay(SelectAccountActivity.this, 0);

                                            if (Set_Default == 1) {
                                                RedirectToDefaultStudentAccount(parts[1], parts[2]);
                                            } else {
                                                Toast.makeText(SelectAccountActivity.this, "Please try again.", Toast.LENGTH_LONG).show();
                                            }
                                        }catch (Exception ex) {
                                        }
                                    }
                                });
                        alertDialog.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        alertDialog.show();
                    }

                });

            } catch (Exception ex) {
                Constants.writelog(tag, "");
            }
        }

        @Override
        public int getItemCount() {
            return marray.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout mLLAccount;
            TextView mTxtAcName;

            public ViewHolder(View itemView) {
                super(itemView);
                mLLAccount = (RelativeLayout) itemView.findViewById(R.id.itemll);
                mTxtAcName = (TextView) itemView.findViewById(R.id.txtmessages);
            }
        }
    }

    public void RedirectToDefaultStudentAccount(String studId, String SchoolId) {
        try {
            Datastorage.SetSchoolId(SelectAccountActivity.this, SchoolId);
            Datastorage.SetStudentID(SelectAccountActivity.this, studId);
            Constants.ResetLastAutoUpdateDay(SelectAccountActivity.this);
            Intent intent = new Intent(SelectAccountActivity.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            onClickAnimation();
            String stud_details = db.GetStudentAccountDetails(SelectAccountActivity.this,
                    Integer.parseInt(studId), Integer.parseInt(SchoolId));
            String[] parts = stud_details.split(",");
            if (parts != null && parts.length > 7 && !parts[8].isEmpty()) {
                Datastorage.SetStudentName(SelectAccountActivity.this, parts[8]);
            } else {
                Datastorage.SetStudentName(SelectAccountActivity.this, "");
            }
            db.SetCurrentYearAsDefaultYear(studId, SchoolId);
            int yearid = db.GetCurrentAcademicYearId(
                    Integer.parseInt(SchoolId), Integer.parseInt(studId));
            Datastorage.SetAcademicYear(SelectAccountActivity.this, yearid + "");
        } catch (Exception ex) {
            Constants.writelog("AccountListActivity", "Ex 370:" + ex.getMessage() + "::::::" + ex.getStackTrace());
        }
    }

}
