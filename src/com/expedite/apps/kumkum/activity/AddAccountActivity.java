package com.expedite.apps.kumkum.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.AppService;
import com.expedite.apps.kumkum.model.Contact;
import com.google.android.gms.ads.AdView;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class AddAccountActivity extends BaseActivity {

    String tag = "AddAccountActivity";
    DatabaseHandler db;
    private Menu menu;
    RecyclerView mRecyclerview;
    AccountAdapter madapter;
    ProgressBar mProgressBar;
    String SchoolId = "";
    String mobile = "";
    String StudentId = "";
    String item = "";
    int IsDefSchlId = 0, IsDefStudId = 0;
    int removepos;
    private ArrayList<String> ItmNames = new ArrayList<>();
    private ArrayList<String> Stu_id = new ArrayList<>();
    private HashMap<String, String> map = new HashMap<String, String>();
    int removeSchoolId, removeStudId;
    String Account_Remove_Status = "";
    private AdView mAdView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account_new);
        try {

            init();
        } catch (Exception ex) {
            Constants.writelog(tag, "onCreate:50:" + ex.getMessage());
        }
    }

    private void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.AddAcountActivity);
            Constants.setActionbar(getSupportActionBar(), AddAccountActivity.this, AddAccountActivity.this,
                    "Add Account", "AddAccountNew",ActivityNames.AddAcountActivity);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBaradd);
            db = new DatabaseHandler(AddAccountActivity.this);
            mRecyclerview = (RecyclerView) findViewById(R.id.addAclistrecycler);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(AddAccountActivity.this));
            List<Contact> contacts = db.getPhoneAndPin();
            int record_cnt = db.getContactsCount();
            Constants.Logwrite("Total Record: ", "Count:" + record_cnt);
            int cntr = 0;
            for (Contact cn : contacts) {
                ItmNames.add(cn.getName());
                if (cntr == 0) {
                    mobile = cn.getPhoneNumber();
                    IsDefSchlId = cn.getSchoolId();
                    IsDefStudId = cn.getStudentId();
                }
                cntr++;
                map.put(cn.getName(), cn.getPhoneNumber() + ","
                        + cn.getSchoolId() + "," + cn.getStudentId());

            }
            if (ItmNames != null && ItmNames.size() > 0) {
                madapter = new AccountAdapter(AddAccountActivity.this, ItmNames);
                mRecyclerview.setAdapter(madapter);
            } else {
                Showdilougue();
            }
        } catch (Exception ex) {
            Constants.writelog(tag, "init():55::" + ex.getMessage());
        }
        // Toast.makeText(AddAccountActivity.this, mobile, Toast.LENGTH_SHORT).show();
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
        public void onBindViewHolder(ViewHolder holder, final int position) {
            try {
                holder.mTxtAcName.setText(Html.fromHtml(marray.get(position)));

                holder.mLLAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String item = marray.get(position);
                        String details = map.get(item);
                        String[] cntdet = details.split(",");
                        String phno = cntdet[0];
                        SchoolId = cntdet[1];
                        StudentId = cntdet[2];

                        if (!phno.isEmpty()) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddAccountActivity.this);
                            alertDialog.setTitle("Information");
                            alertDialog.setMessage("Do you really want to make student as default?");
                            alertDialog.setIcon(R.drawable.information);
                            alertDialog.setCancelable(false);
                            alertDialog.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(isOnline()) {

                                                    db.UpdateSetIsDefault_Zero_to_One();
                                                    int Set_Default = db.UpdateContactRecordSetDefaultOne(
                                                            StudentId, SchoolId);
                                                    Datastorage.SetLastAutoUpdateExamDay(AddAccountActivity.this, 0);
                                                    Datastorage.SetLastAutoUpdateMessageDay(AddAccountActivity.this, 0);
                                                    if (Set_Default == 1) {
                                                        RedirectToDefaultStudentAccount();
                                                    } else {
                                                        Toast.makeText(AddAccountActivity.this, "Please try again.", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            else
                                            {
                                                Toast.makeText(activity, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
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

    public void RedirectToDefaultStudentAccount() {
        try {
            Datastorage.SetSchoolId(AddAccountActivity.this, SchoolId);
            Datastorage.SetStudentID(AddAccountActivity.this, StudentId);
            Constants.ResetLastAutoUpdateDay(AddAccountActivity.this);
            Intent intent = new Intent(AddAccountActivity.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            Common.onClickAnimation(AddAccountActivity.this);
            String stud_details = db.GetStudentAccountDetails(AddAccountActivity.this,
                    Integer.parseInt(StudentId), Integer.parseInt(SchoolId));
            String[] parts = stud_details.split(",");
            if (parts != null && parts.length > 7 && !parts[8].isEmpty()) {
                Datastorage.SetStudentName(AddAccountActivity.this, parts[8]);
            } else {
                Datastorage.SetStudentName(AddAccountActivity.this, "");
            }
            db.SetCurrentYearAsDefaultYear(StudentId, SchoolId);
            int yearid = db.GetCurrentAcademicYearId(
                    Integer.parseInt(SchoolId), Integer.parseInt(StudentId));
            Datastorage.SetAcademicYear(AddAccountActivity.this, yearid + "");
        } catch (Exception ex) {
            Constants.writelog("AccountListActivity", "Ex 370:" + ex.getMessage() + "::::::" + ex.getStackTrace());
        }
    }

    public void Showdilougue() {
        AlertDialog alertDialog = new AlertDialog.Builder(AddAccountActivity.this).create();
        alertDialog.setTitle("Information");
        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
        alertDialog.setIcon(R.drawable.information);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(AddAccountActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                Common.onBackClickAnimation(AddAccountActivity.this);
            }
        });
        // Showing Alert Message
        alertDialog.show();
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            MenuInflater objmenuinfl = getMenuInflater();
            objmenuinfl.inflate(R.menu.activity_refresh, menu);
            this.menu = menu;
        } catch (Exception err) {
            Constants.Logwrite("ReportBugActivity", "ReportBugActivity:" + err.getMessage());
            return true;

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        //  int iid = item.getItemId();
        switch (item.getItemId()) {
            case R.id.refresh_type:
                getAccount();
                onClickAnimation();
                break;

            case android.R.id.home:
                AddAccountActivity.this.finish();
                onBackClickAnimation();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void getAccount() {
        try {
            String mDeviceDetail = "";
            try {
                mDeviceDetail = Build.DEVICE + "|||" + Build.MODEL + "|||" + Build.ID
                        + "|||" + Build.PRODUCT + "|||" + Build.VERSION.SDK
                        + "|||" + Build.VERSION.RELEASE + "|||" + Build.VERSION.INCREMENTAL;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (isOnline()) {
                mProgressBar.setVisibility(View.VISIBLE);
                Call<AppService> mCall = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                        .GetAccountDetail(mobile, StudentId, SchoolId, mDeviceDetail, Constants.PLATFORM, Constants.CODEVERSION);
                mCall.enqueue(new retrofit2.Callback<AppService>() {
                    @Override
                    public void onResponse(Call<AppService> call, Response<AppService> response) {
                        try {
                            AppService tmp = response.body();
                            if (tmp != null && tmp.getStrResult() != null && !tmp.getStrResult().isEmpty()
                                    && tmp.getResponse().equalsIgnoreCase("1")) {
                                if (tmp.getListArray().size() > 0) {
                                    db.DeleteAllContact();
                                    ItmNames.clear();
                                    for (int i = 0; i < tmp.getListArray().size(); i++) {
                                        AppService.ListArray tmp1 = tmp.getListArray().get(i);
                                        ItmNames.add(tmp1.getFifth());
                                        map.put(tmp1.getFifth(), mobile + "," + tmp1.getThird() + "," + tmp1.getSecond());
                                        if (tmp1.getSecond() == null || tmp1.getSecond().equalsIgnoreCase("")) {
                                            tmp1.setSecond("0");
                                        } else if (tmp1.getThird() == null || tmp1.getThird().equalsIgnoreCase("")) {
                                            tmp1.setThird("0");
                                        } else if (tmp1.getSixth() == null || tmp1.getSixth().equalsIgnoreCase("")) {
                                            tmp1.setSixth("0");
                                        } else if (tmp1.getSeventh() == null || tmp1.getSeventh().equalsIgnoreCase("")) {
                                            tmp1.setSeventh("0");
                                        } else if (tmp1.getEighth() == null || tmp1.getEighth().equalsIgnoreCase("")) {
                                            tmp1.setEighth("0");
                                        } else if (tmp1.getNineteen() == null || tmp1.getNineteen().equalsIgnoreCase("")) {
                                            tmp1.setNineteen("0");
                                        } else if (tmp1.getSeventeen() == null || tmp1.getSeventeen().equalsIgnoreCase("")) {
                                            tmp1.setSeventeen("0");
                                        }
                                        int Contact_Status = db.addContact(new Contact(
                                                tmp1.getFifth(), mobile, 0, 0, Integer.parseInt(tmp1.getSecond()), Integer.parseInt(tmp1.getThird()),
                                                Integer.parseInt(tmp1.getSixth()), Integer.parseInt(tmp1.getSeventh()),
                                                Integer.parseInt(tmp1.getEighth()), Integer.parseInt(tmp1.getNineteen()), tmp1.getNineth(),
                                                tmp1.getEleventh(), tmp1.getSixteen(), tmp1.getTenth(), tmp1.getTwenty(),
                                                Integer.parseInt(tmp1.getSeventeen()), tmp1.getEighteen()));
                                    }
                                    if (ItmNames != null && ItmNames.size() > 0) {
                                        madapter = new AccountAdapter(AddAccountActivity.this, ItmNames);
                                        mRecyclerview.setAdapter(madapter);
                                    } else {
                                        Showdilougue();
                                    }
                                }
                                mProgressBar.setVisibility(View.GONE);
                            } else {
                                if (tmp != null && tmp.getStrResult() != null && !tmp.getStrResult().isEmpty())
                                    Common.showToast(AddAccountActivity.this, tmp.getStrResult());
                                mProgressBar.setVisibility(View.GONE);
                            }
                        } catch (Exception ex) {
                            Constants.writelog(tag, "GetDetails():onResponse:475:" + ex.getMessage());
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<AppService> call, Throwable t) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            } else {
                Common.showToast(AddAccountActivity.this, "No Interenet Connection.");
            }
        } catch (Exception ex) {
            mProgressBar.setVisibility(View.GONE);
            Constants.writelog(tag, "getAccount:369:" + ex.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackClickAnimation();
    }

    private class MyTask extends AsyncTask<String, Void, String> {

        String stuid, schoolid;

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                stuid = params[0];
                schoolid = params[1];
                Account_Remove_Status = DeleteAccountFromGCMData(stuid, schoolid);
            } catch (Exception err) {
                Common.printStackTrace(err);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                Common.printStackTrace(err);
            }
        }
    }

    public String DeleteAccountFromGCMData(String Student_Id, String School_Id) {
        String status = "";
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.DELETE_ACCOUNT_FROM_GCM_DATA);
        request.addProperty("Device_Id", Datastorage.GetDeviceId(AddAccountActivity.this));
        request.addProperty("Student_Id", Integer.parseInt(Student_Id));
        request.addProperty("School_Id", Integer.parseInt(School_Id));
        try {
            SoapObject result = Constants.CallWebMethod(AddAccountActivity.this, request, Constants.DELETE_ACCOUNT_FROM_GCM_DATA, false);
            if (result != null && result.getPropertyCount() > 0) {
                status = result.getPropertyAsString(0);
            }
        } catch (Exception e) {
            return status;
        }
        return status;
    }

    public void DeleteAccount(String stud_id, String school_id) {
        try {
            String IsDefault = "0";
            int success = 0;
            success = db.deleteContactRecord(stud_id, school_id);
            Constants.Logwrite("AccountRemove:AccountDeleteStatus", "success:" + success);
            int smsdelete = db.DeleteStudentSMS(Integer.parseInt(stud_id), Integer.parseInt(school_id));
            Constants.Logwrite("StudentSMSDelete:", "" + smsdelete);
            int examdelete = db.DeleteStudentExmas(Integer.parseInt(stud_id), Integer.parseInt(school_id));
            int circular = db.DeleteStudentCircular(Integer.parseInt(stud_id), Integer.parseInt(school_id));
            int PhotoDelte = db.DeleteStudentPhotoGallery(Integer.parseInt(stud_id), Integer.parseInt(school_id));
            int studprofile = db.deleteStudAttendanceDetails(stud_id + "", school_id + "");
            int studAttendance = db.deleteStudProfileDetails(stud_id + "", school_id + "");
            Constants.Logwrite("StudentExamsDelete:", "" + examdelete);
            int Yeardelete = db.DeleteStudentYear(Integer.parseInt(stud_id), Integer.parseInt(school_id));
            Constants.Logwrite("StudentYearDelete:", "" + Yeardelete);
            Constants.Logwrite("Remove", "Status_DB:" + success);
           /* if (success == 1) {
                ItmNames.remove(removepos);
                if (removeSchoolId == Integer.parseInt(IsDefSchlId) && removeStudId == Integer.parseInt(IsDefStudId)){
                  *//*  String item = ItmNames.get(0);
                    String details = map.get(item);
                    String[] cntdet = details.split(",");
                    String phno = cntdet[0];
                    SchoolId = cntdet[1];
                    StudentId = cntdet[2];
                    SetDefaultStudentAccount(StudentId, SchoolId);*//*
                    IsDefault = "1";
                }
                madapter.notifyDataSetChanged();
            }*/
            ItmNames.clear();
            if (success == 1) {
                if (removeSchoolId == IsDefSchlId && removeStudId == IsDefStudId) {
                  /*  String item = ItmNames.get(0);
                    String details = map.get(item);
                    String[] cntdet = details.split(",");
                    String phno = cntdet[0];
                    SchoolId = cntdet[1];
                    StudentId = cntdet[2];
                    SetDefaultStudentAccount(StudentId, SchoolId);*/
                    IsDefault = "1";
                }
                if (IsDefault.equals("1")) {
                    int record_cnt = db.getContactsCount();
                    if (record_cnt > 0) {
                        List<Contact> contacts = db.getPhoneAndPin();
                        Constants.Logwrite("Total Record: ", "Count:" + record_cnt);
                        // ItmNames = new String[record_cnt];
                        int cntr = 0;
                        int a = 0;
                        for (Contact cn : contacts) {
                            String aa = cn.getStudentId() + ","
                                    + cn.getSchoolId();
                            if (a == 0) {
                                String[] splt = aa.split(",");
                                String studiid = splt[0].toString();
                                String schooliid = splt[1].toString();

                                int other_def = db
                                        .UpdateContactRecordSetDefaultOne(
                                                studiid, schooliid);
                                int temp = db.UpdateSetIsDefault_Zero_to_One();
                                SetDefaultStudentAccount(studiid, schooliid);
                                a = 1;
                            }
                        }
                        GetStudentAccountList();
                    } else {
                        // LoginDetail.setSchoolId("");
                        Datastorage.SetSchoolId(AddAccountActivity.this, "");
                        try {
                            // LoginDetail.setStudId("");
                            Datastorage.SetStudentID(AddAccountActivity.this, "");
                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                        Intent intent = new Intent(AddAccountActivity.this, SplashActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    Intent intent = new Intent(AddAccountActivity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    GetStudentAccountList();
                }
            } else {
                GetStudentAccountList();
            }
            madapter.notifyDataSetChanged();
        } catch (Exception err) {
            Common.printStackTrace(err);
        }
    }

    private void GetStudentAccountList() {
        try {
            List<Contact> contacts = db.getPhoneAndPin();
            int cntr = 0;
            int a = 0;
            map = new HashMap<String, String>();
            for (Contact cn : contacts) {
                ItmNames.add(cn.getName());
                cntr++;
                map.put(cn.getName(), cn.getIsDef() + "," + cn.getStudentId()
                        + "," + cn.getSchoolId());
            }
            if (ItmNames != null && ItmNames.size() > 0) {
                madapter = new AccountAdapter(AddAccountActivity.this, ItmNames);
                mRecyclerview.setAdapter(madapter);
            } else {
                Intent intent = new Intent(AddAccountActivity.this, LoginNewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("lastpage", "SplashActivity");
                startActivity(intent);
                finish();
                onClickAnimation();
            }
        } catch (Exception err) {
            Constants.Logwrite("AccountRemove:Exception",
                    "GetStudentAccountList:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        }
    }

    public void SetDefaultStudentAccount(String StudentId, String SchoolId) {
        try {
            Datastorage.SetSchoolId(AddAccountActivity.this, SchoolId);
            Datastorage.SetStudentID(AddAccountActivity.this, StudentId);
            String stud_details = db.GetStudentAccountDetails(AddAccountActivity.this,
                    Integer.parseInt(StudentId), Integer.parseInt(SchoolId));
            String[] parts = stud_details.split(",");
            if (parts != null && parts.length > 7 && parts[8].toString() != "") {
                Datastorage.SetStudentName(AddAccountActivity.this,
                        parts[8].toString());
            } else {
                Datastorage.SetStudentName(AddAccountActivity.this, "");
            }
            // db.UpdateContactRecordSetDefaultOne(StudentId, SchoolId);

            // Datastorage.SetStudentName(AccountListRemoveActivity.this, studname);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
