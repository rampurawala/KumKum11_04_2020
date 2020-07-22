package com.expedite.apps.kumkum.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.adapter.AccountListAdapter;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.AppService;
import com.expedite.apps.kumkum.model.Contact;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class AccountListActivity extends BaseActivity {
    LinearLayout empty_folder_lyt_file_detail;
    private DatabaseHandler db = new DatabaseHandler(this);
    private ListView lvaccount;
    private AccountListAdapter adp;
    private String[] ItmNames = null;
    private HashMap<String, String> map = new HashMap<String, String>();
    private String phno = "", logpn = "", SchoolId = "", StudentId = "", tag = "AccountListActivity", mobileno;
    private HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
    private ProgressBar mProgressBar;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);
        init();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.AccountListActivity);
            Constants.setActionbar(getSupportActionBar(), AccountListActivity.this, AccountListActivity.this,
                    "Set Default Account", "SetDefaultAccount", ActivityNames.AccountListActivity);
            mobileno = Datastorage.GetPhoneNumber(AccountListActivity.this);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            empty_folder_lyt_file_detail = findViewById(R.id.empty_folder_lyt_file_detail);
            if (mobileno.isEmpty()) {
                mobileno = db.getmobileNo(StudentId, SchoolId);
            }
            lvaccount = (ListView) findViewById(R.id.lstaccount);
            Constants.Logwrite("Reading: ", "Reading all contacts..");
            getAccount();

        } catch (Exception err) {
            Constants.Logwrite("AccountListActivity:", "Message:" + err.getMessage());
        }
        lvaccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                String item = adp.getItenName(arg2);
                String details = map.get(item);
                String[] cntdet = details.split(",");
                phno = cntdet[0];
                logpn = cntdet[1];
                SchoolId = cntdet[2];
                StudentId = cntdet[3];

                if (!phno.isEmpty() && !logpn.isEmpty()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AccountListActivity.this);
                    alertDialog.setTitle("Information");
                    alertDialog.setMessage("Do you really want to make student as default?");
                    alertDialog.setIcon(R.drawable.information);
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (isOnline()) {
                                        int success = db.UpdateSetIsDefault_Zero_to_One();
                                        int Set_Default = db.UpdateContactRecordSetDefaultOne(
                                                StudentId, SchoolId);
                                        Datastorage.SetLastAutoUpdateExamDay(AccountListActivity.this, 0);
                                        Datastorage.SetLastAutoUpdateMessageDay(AccountListActivity.this, 0);
                                        if (Set_Default == 1) {
                                            RedirectToDefaultStudentAccount();
                                        } else {
                                            Toast.makeText(AccountListActivity.this, "Please try again.", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(AccountListActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            menu.clear();
            mapacc = new HashMap<Integer, String>();
            menu.add(0, 1, 1, "Add Account").setTitle("Add Account");
            // menu.add(0, 2, 2, "Remove Account").setTitle("Remove Account");
            mapacc = Constants.AddAccount(menu, db);
        } catch (Exception err) {
            Constants.Logwrite("AccountListActivity:", "onPrepareOptionsMenu:" + err.getMessage()
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
            hideKeyboard(AccountListActivity.this);
            AccountListActivity.this.finish();
            onBackClickAnimation();
        } else if (iid == 1 || iid == 2) {
            if (iid == 1) {
                addAccountClick(AccountListActivity.this);
            } /*else if (iid == 2) {
                removeAccountClick(AccountListActivity.this);
            }*/ else {
                accountListClick(AccountListActivity.this);
            }
        } else {
            if (isOnline()) {
                String details = mapacc.get(iid).toString();
                Constants.SetAccountDetails(details, AccountListActivity.this);
                intent = new Intent(AccountListActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
                onBackClickAnimation();
                return true;
            } else {
                Toast.makeText(this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AccountListActivity.this.finish();
        onBackClickAnimation();
    }

    public void Showdilougue() {
        AlertDialog alertDialog = new AlertDialog.Builder(AccountListActivity.this).create();
        alertDialog.setTitle("Information");
        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
        alertDialog.setIcon(R.drawable.information);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(AccountListActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                onBackClickAnimation();
            }
        });
        // Showing Alert Message
        alertDialog.show();
        return;
    }

    public void RedirectToDefaultStudentAccount() {
        try {
            Datastorage.SetSchoolId(AccountListActivity.this, SchoolId);
            Datastorage.SetStudentID(AccountListActivity.this, StudentId);
            Constants.ResetLastAutoUpdateDay(AccountListActivity.this);
            Intent intent = new Intent(AccountListActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
            onClickAnimation();
            String stud_details = db.GetStudentAccountDetails(AccountListActivity.this,
                    Integer.parseInt(StudentId), Integer.parseInt(SchoolId));
            String[] parts = stud_details.split(",");
            if (parts != null && parts.length > 7 && !parts[8].isEmpty()) {
                Datastorage.SetStudentName(AccountListActivity.this, parts[8]);
            } else {
                Datastorage.SetStudentName(AccountListActivity.this, "");
            }
            db.SetCurrentYearAsDefaultYear(StudentId, SchoolId);
            int yearid = db.GetCurrentAcademicYearId(
                    Integer.parseInt(SchoolId), Integer.parseInt(StudentId));
            Datastorage.SetAcademicYear(AccountListActivity.this, yearid + "");
        } catch (Exception ex) {
            Constants.writelog("AccountListActivity", "Ex 370:" + ex.getMessage() + "::::::" + ex.getStackTrace());
        }
    }

    private void getAccount() {
        try {
            mProgressBar.setVisibility(View.VISIBLE);
            String mDeviceDetail = "";
            try {
                mDeviceDetail = Build.DEVICE + "|||" + Build.MODEL + "|||" + Build.ID
                        + "|||" + Build.PRODUCT + "|||" + Build.VERSION.SDK
                        + "|||" + Build.VERSION.RELEASE + "|||" + Build.VERSION.INCREMENTAL;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (isOnline()) {
                empty_folder_lyt_file_detail.setVisibility(View.GONE);
                Call<AppService> mCall = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                        .GetAccountDetail(mobileno, StudentId, SchoolId, mDeviceDetail, Constants.PLATFORM, Constants.CODEVERSION);
                mCall.enqueue(new retrofit2.Callback<AppService>() {
                    @Override
                    public void onResponse(Call<AppService> call, Response<AppService> response) {
                        try {
                            AppService tmp = response.body();
                            if (tmp != null && tmp.getStrResult() != null && !tmp.getStrResult().isEmpty()
                                    && tmp.getResponse().equalsIgnoreCase("1")) {
                                if (tmp.getListArray().size() > 0) {
                                    db.DeleteAllContact();
                                    ArrayList<String> ItmNames = new ArrayList<>();
                                    for (int i = 0; i < tmp.getListArray().size(); i++) {
                                        AppService.ListArray tmp1 = tmp.getListArray().get(i);
                                        ItmNames.add(tmp1.getFifth());
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
                                                tmp1.getFifth(), mobileno, 0, 0, Integer.parseInt(tmp1.getSecond()), Integer.parseInt(tmp1.getThird()),
                                                Integer.parseInt(tmp1.getSixth()), Integer.parseInt(tmp1.getSeventh()),
                                                Integer.parseInt(tmp1.getEighth()), Integer.parseInt(tmp1.getNineteen()), tmp1.getNineth(),
                                                tmp1.getEleventh(), tmp1.getSixteen(), tmp1.getTenth(), tmp1.getTwenty(),
                                                Integer.parseInt(tmp1.getSeventeen()), tmp1.getEighteen()));
                                    }
                                }
                            }
                            List<Contact> contacts = db.getPhoneAndPin();
                            int record_cnt = db.getContactsCount();
                            Constants.Logwrite("Total Record: ", "Count:" + record_cnt);
                            ItmNames = new String[record_cnt];
                            int cntr = 0;
                            for (Contact cn : contacts) {
                                ItmNames[cntr] = cn.getName();
                                cntr++;
                                map.put(cn.getName(), cn.getPhoneNumber() + "," + cn.getLogPin() + ","
                                        + cn.getSchoolId() + "," + cn.getStudentId());
                            }
                            if (ItmNames != null && ItmNames.length > 0) {
                                adp = new AccountListAdapter(AccountListActivity.this, ItmNames);
                                lvaccount.setAdapter(adp);
                            } else {
                                Showdilougue();
                            }
                        } catch (Exception ex) {
                            Constants.writelog(tag, "GetDetails():onResponse:1683:" + ex.getMessage());
                        } finally {
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<AppService> call, Throwable t) {
                        Constants.writelog(tag, "GetDetails():onFailure:1684.");
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
            } else {
                mProgressBar.setVisibility(View.GONE);
                empty_folder_lyt_file_detail.setVisibility(View.VISIBLE);
                Toast.makeText(this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Constants.writelog(tag, "getAccount:369:" + ex.getMessage());
        } finally {

        }
    }
}