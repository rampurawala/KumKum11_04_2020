package com.expedite.apps.kumkum.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.adapter.AccountRemoveListAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.Contact;
import com.google.android.gms.ads.AdView;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

public class AccountListRemoveActivity extends BaseActivity {
    private ListView lvaccount;
    private AccountRemoveListAdapter adp;
    private String[] ItmNames = null;
    private HashMap<String, String> map = new HashMap<String, String>();
    private String IsDefault = "", schid = "", studid = "";
    private int Stud_Id, School_Id;
    private List<Contact> contacts = null;
    private HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
    private ProgressBar mProgressBar;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list_remove);

        init();
    }

    public void init() {
        mAdView = (AdView) findViewById(R.id.adView);
        showBannerAd(mAdView, ActivityNames.AccountListRemoveActivity);
        Constants.setActionbar(getSupportActionBar(), AccountListRemoveActivity.this, AccountListRemoveActivity.this,
                "Remove Account", "RemoveAccount",ActivityNames.AccountListRemoveActivity);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        db = new DatabaseHandler(AccountListRemoveActivity.this);
        try {
            lvaccount = (ListView) findViewById(R.id.lstremovestudaccount);
            Constants.Logwrite("AccountRemove: ", "Reading all contacts..");
            contacts = db.getPhoneAndPin();
            ItmNames = new String[contacts.size()];
            int cntr = 0;
            for (Contact cn : contacts) {

                ItmNames[cntr] = cn.getName();
                cntr++;
                map.put(cn.getName(), cn.getIsDef() + "," + cn.getStudentId() + "," + cn.getSchoolId());
                Constants.Logwrite("Conact1:" + cntr + "," + cn.getName(), cn.getPhoneNumber() + "," + cn.getLogPin() + ","
                        + cn.getIsDef() + "," + cn.getStudentId() + "," + cn.getSchoolId());
            }
            if (ItmNames != null && ItmNames.length > 0) {
                adp = new AccountRemoveListAdapter(AccountListRemoveActivity.this, ItmNames);
                lvaccount.setAdapter(adp);

            } else {
                Showdilougue();
            }
        } catch (Exception err) {
            Constants.Logwrite("AccountRemove:OncreateException", "Message:" + err.getMessage() + "StactTrace:" + err.getStackTrace());
        }
        lvaccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    if (contacts.size() > 1) {
                        String item = adp.getItenName(arg2).toString();
                        String details = map.get(item).toString();
                        String[] cntdet = details.split(",");
                        IsDefault = cntdet[0].toString();
                        Constants.Logwrite("AccountRemove", "IsDeafult:" + IsDefault);
                        Stud_Id = Integer.parseInt(cntdet[1].toString());
                        Constants.Logwrite("AccountRemove", "StudentId:" + Stud_Id);
                        School_Id = Integer.parseInt(cntdet[2].toString());
                        Constants.Logwrite("AccountRemove", "SchoolId:" + School_Id);
                        schid = cntdet[2].toString();
                        Constants.Logwrite("AccountRemove", "SchoolId:" + schid);
                        studid = cntdet[1].toString();
                        Constants.Logwrite("AccountRemove", "StudId:" + studid);
                        if (studid != "" && schid != "") {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AccountListRemoveActivity.this);
                            alertDialog.setTitle("Information");
                            alertDialog.setMessage("Do you really want to remove student account?");
                            alertDialog.setIcon(R.drawable.information);
                            alertDialog.setCancelable(false);
                            alertDialog.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            new MyTask().execute();
                                        }
                                    });

                            alertDialog.setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                            alertDialog.show();
                        }
                    } else {
                        Toast.makeText(AccountListRemoveActivity.this, "You can not remove all account.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception err) {
                    Constants.Logwrite("AccountRemove:Exception", "Message:" + err.getMessage() + "StactTrace:" + err.getStackTrace());
                }
            }
        });
    }

    public String DeleteAccountFromGCMData(int Student_Id, int School_Id) {
        String status = "";
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.DELETE_ACCOUNT_FROM_GCM_DATA);
        request.addProperty("Device_Id", Datastorage.GetDeviceId(AccountListRemoveActivity.this));
        request.addProperty("Student_Id", Student_Id);
        request.addProperty("School_Id", School_Id);
        try {
            SoapObject result = Constants.CallWebMethod(AccountListRemoveActivity.this, request, Constants.DELETE_ACCOUNT_FROM_GCM_DATA, false);
            if (result != null && result.getPropertyCount() > 0) {
                status = result.getPropertyAsString(0);
            }
        } catch (Exception e) {
            return status;
        }
        return status;
    }

    private void GetStudentAccountList() {
        try {
            List<Contact> contacts = db.getPhoneAndPin();
            ItmNames = new String[contacts.size()];
            int cntr = 0;
            int a = 0;
            map = new HashMap<String, String>();
            for (Contact cn : contacts) {

                ItmNames[cntr] = cn.getName();
                cntr++;
                map.put(cn.getName(), cn.getIsDef() + "," + cn.getStudentId()
                        + "," + cn.getSchoolId());
            }
            if (ItmNames != null && ItmNames.length > 0) {
                adp = new AccountRemoveListAdapter(AccountListRemoveActivity.this, ItmNames);
                lvaccount.setAdapter(adp);
            } else {
                lvaccount.setAdapter(null);
            }
        } catch (Exception err) {
            Constants.Logwrite("AccountRemove:Exception",
                    "GetStudentAccountList:" + err.getMessage() + "StactTrace:"
                            + err.getStackTrace());
        }
    }

    public void DeleteAccount() {
        try {
            int success = 0;
            success = db.deleteContactRecord(studid, schid);
            Constants.Logwrite("AccountRemove:AccountDeleteStatus", "success:" + success);
            int smsdelete = db.DeleteStudentSMS(Stud_Id, School_Id);
            Constants.Logwrite("StudentSMSDelete:", "" + smsdelete);
            int examdelete = db.DeleteStudentExmas(Stud_Id, School_Id);
            int circular = db.DeleteStudentCircular(Stud_Id, School_Id);
            int PhotoDelte = db.DeleteStudentPhotoGallery(Stud_Id, School_Id);
            int studprofile = db.deleteStudAttendanceDetails(studid + "", School_Id + "");
            int studAttendance = db.deleteStudProfileDetails(studid + "", School_Id + "");
            Constants.Logwrite("StudentExamsDelete:", "" + examdelete);
            int Yeardelete = db.DeleteStudentYear(Stud_Id, School_Id);
            Constants.Logwrite("StudentYearDelete:", "" + Yeardelete);
            Constants.Logwrite("Remove", "Status_DB:" + success);

            if (success == 1) {
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
                                SetDefaultStudentAccount(studiid, schooliid);
                                a = 1;
                            }
                        }
                        GetStudentAccountList();
                    } else {
                        // LoginDetail.setSchoolId("");
                        Datastorage.SetSchoolId(AccountListRemoveActivity.this, "");
                        try {
                            // LoginDetail.setStudId("");
                            Datastorage.SetStudentID(AccountListRemoveActivity.this, "");
                        } catch (Exception err) {
                        }

                        Intent intent = new Intent(AccountListRemoveActivity.this, SplashActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    Intent intent = new Intent(AccountListRemoveActivity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    GetStudentAccountList();
                }
            } else {
                GetStudentAccountList();
            }
        } catch (Exception err) {
            Common.printStackTrace(err);
        }
    }

    // new
    public void SetDefaultStudentAccount(String StudentId, String SchoolId) {
        try {
            Datastorage.SetSchoolId(AccountListRemoveActivity.this, SchoolId);
            Datastorage.SetStudentID(AccountListRemoveActivity.this, StudentId);
            String stud_details = db.GetStudentAccountDetails(AccountListRemoveActivity.this,
                    Integer.parseInt(StudentId), Integer.parseInt(SchoolId));
            String[] parts = stud_details.split(",");
            if (parts != null && parts.length > 7 && parts[8].toString() != "") {
                Datastorage.SetStudentName(AccountListRemoveActivity.this,
                        parts[8].toString());
            } else {
                Datastorage.SetStudentName(AccountListRemoveActivity.this, "");
            }
            // db.UpdateContactRecordSetDefaultOne(StudentId, SchoolId);

            // Datastorage.SetStudentName(AccountListRemoveActivity.this, studname);
        } catch (Exception err) {

        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            menu.clear();
            menu.add(0, 1, 1, "Add Account").setTitle("Add Account");
            menu.add(0, 2, 2, "Set Default Account").setTitle("Set Default Account");
            mapacc = Constants.AddAccount(menu, db);
        } catch (Exception err) {
            Constants.Logwrite("AccountListRemoveActivity:", "onPrepareOptionsMenu:" + err.getMessage()
                    + "StackTrace::" + err.getStackTrace().toString());
            return true;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int iid = item.getItemId();
        if (iid == android.R.id.home) {
            hideKeyboard(AccountListRemoveActivity.this);
            AccountListRemoveActivity.this.finish();
            onBackClickAnimation();
        } else if (iid == 1 || iid == 2) {
            if (iid == 1) {
                addAccountClick(AccountListRemoveActivity.this);
            } else if (iid == 2) {
                setDefaultAccount(AccountListRemoveActivity.this);
            }
        } else {
            String details = mapacc.get(iid);
            Constants.SetAccountDetails(details, AccountListRemoveActivity.this);
            intent = new Intent(AccountListRemoveActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
            onBackClickAnimation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AccountListRemoveActivity.this.finish();
        onBackClickAnimation();
    }

    public void Showdilougue() {
        AlertDialog alertDialog = new AlertDialog.Builder(AccountListRemoveActivity.this).create();
        alertDialog.setTitle("Information");
        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
        alertDialog.setIcon(R.drawable.information);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(AccountListRemoveActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        alertDialog.show();
        return;
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                DeleteAccountFromGCMData(Stud_Id, School_Id);
            } catch (Exception err) {
                Common.printStackTrace(err);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                DeleteAccount();
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                Common.printStackTrace(err);
            }
        }
    }

}
