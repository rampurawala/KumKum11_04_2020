package com.expedite.apps.kumkum.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.adapter.NoticeBoardListAdapter;
import com.expedite.apps.kumkum.adapter.SpinnerItemsAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.CircularModel;
import com.expedite.apps.kumkum.model.Contact;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeBoardActivity extends BaseActivity {
    public static Button dowmloadbtn, mBtnEmail, mBtnShare;
    public static LinearLayout mLLBtnLayout;

    public static CheckBox checkBoxselectall;
    private int sorttype = 0, position = 0;
    private String[] ddldata = null, Itmgroup = null;
    private String[] ItmDate = null, CircularName = null, Itmpath = null, Itmgroupsorted = null, doc = null,
            docsorded = null, Id = null;
    private String[] ItmDatesorted = null, CircularNamesorted = null, Itmpathsorted = null, groupforsddl = null,
            sorted = null, Idsorted = null;
    private Boolean isRefreshCircular = false, isShowInternetMsg = false;
    private Time cur_time = new Time();
    private ListView ItmView;
    private NoticeBoardListAdapter adp;
    private SpinnerItemsAdapter adpddl;
    private TextView tvname, tvdate, tvgroup;
    private HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
    private String SchoolId, StudentId, Year_Id;
    private Spinner ddlgroup;
    private String mIsFromHome = "";
    private ProgressBar mProgressBar;

    private BroadcastReceiver mBroadCastReceiver;
    private ArrayList<CircularModel.Strlist> mCircularlist = new ArrayList<>();
    private View llSpinner;
    private String selectedgroup = "";
    private String A = "";
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticeboard);
        if (getIntent() != null && getIntent().getExtras() != null)
            mIsFromHome = getIntent().getExtras().getString("IsFromHome", "");
        init();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);

            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            db = new DatabaseHandler(NoticeBoardActivity.this);
            SchoolId = Datastorage.GetSchoolId(NoticeBoardActivity.this);
            StudentId = Datastorage.GetStudentId(NoticeBoardActivity.this);
            String defacnt = db.GetDefaultAcademicYearAccount(Integer.parseInt(SchoolId), Integer.parseInt(StudentId));
            if (defacnt != null && defacnt.length() > 0) {
                String[] splterstr = defacnt.split(",");
                Year_Id = splterstr[1];
            } else {
                Year_Id = Datastorage.GetCurrentYearId(NoticeBoardActivity.this);
            }
            String title = "", mActivityName = "";
            int Cnt_Count = 0;
            Cnt_Count = Datastorage.GetMultipleAccount(NoticeBoardActivity.this);
            if (Cnt_Count == 1) {
                title = "Notice Board" + "-" + Datastorage.GetStudentName(NoticeBoardActivity.this);
            } else {
                title = "Notice Board";
            }
            mActivityName = ActivityNames.NoticeBoardActivity;
            showBannerAd(mAdView, ActivityNames.NoticeBoardActivity);
            Constants.setActionbar(getSupportActionBar(), NoticeBoardActivity.this, NoticeBoardActivity.this,
                    title, "Circular", mActivityName);
            ItmView = (ListView) findViewById(R.id.lstcirlistnew);
            tvname = (TextView) findViewById(R.id.txtname);
            tvgroup = (TextView) findViewById(R.id.txtgroup);
            tvdate = (TextView) findViewById(R.id.txtdate);
            tvdate.setBackgroundResource(R.drawable.desc60);
            ddlgroup = (Spinner) findViewById(R.id.spinner_group);
            llSpinner = (View) findViewById(R.id.llSpinner);
            //vishwa
            dowmloadbtn = (Button) findViewById(R.id.downloadbtn);
            mBtnEmail = (Button) findViewById(R.id.emailAll);
            mBtnShare = (Button) findViewById(R.id.shareAll);
            mLLBtnLayout = (LinearLayout) findViewById(R.id.llbtnlayout);

            mBtnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Toast.makeText(NoticeBoardActivity.this, "d jbcjdbscd", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < Itmpath.length; i++) {
                            Log.e("data", Itmpath[i].toString());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            checkBoxselectall = (CheckBox) findViewById(R.id.checkBoxselectall);
            GetCircularForStudentNew(false);

            //new MyTask().execute();

           /* ItmView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    position = arg2;
                    new MyTaskliarclick().execute();
                }
            });*/

            //change by vishwa 17/4/19
            mBroadCastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase("ViewPDF")) {
                            position = intent.getExtras().getInt("Position");
                            A = intent.getStringExtra("Name");

                            new MyTaskliarclick().execute();
                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            ddlgroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    try {
                        selectedgroup = "";
                        tvgroup.setBackgroundDrawable(null);
                        tvdate.setBackgroundDrawable(null);
                        tvname.setBackgroundDrawable(null);
                        TextView tv = (TextView) arg0
                                .findViewById(R.id.txt_circulargroupname);
                        selectedgroup = tv.getText().toString();
                        checkBoxselectall.setChecked(false);
                        String[] arrtemp = new String[Itmgroup.length];
                        for (int i = 0; i < Itmgroup.length; i++) {

                            arrtemp[i] = Itmgroup[i].toLowerCase().replace(" ",
                                    "")
                                    + "###@@@" + i;
                        }
                        ArrayList<String> strings = new ArrayList<String>(
                                Arrays.asList(arrtemp));
                        sorted = new String[strings.size()];
                        strings.toArray(sorted);

                        int count = 0;
                        if (selectedgroup.equals("All")) {
                            count = Itmgroup.length;
                        } else {
                            for (int i = 0; i < sorted.length; i++) {
                                if (Itmgroup[i].equals(selectedgroup)) {
                                    count++;
                                }
                            }
                        }
                        CircularNamesorted = new String[count];
                        Itmgroupsorted = new String[count];
                        ItmDatesorted = new String[count];
                        Itmpathsorted = new String[count];
                        docsorded = new String[count];
                        Idsorted = new String[count];
                        count = 0;
                        for (int i = 0; i < sorted.length; i++) {
                            String cir_name = sorted[i];
                            String[] parts = cir_name.split("###@@@");
                            int id = Integer.parseInt(parts[1].toString());
                            if (selectedgroup.equals("All")) {
                                CircularNamesorted[count] = CircularName[id];
                                ItmDatesorted[count] = ItmDate[id];
                                Itmpathsorted[count] = Itmpath[id];
                                Itmgroupsorted[count] = Itmgroup[id];
                                docsorded[count] = doc[id];
                                Idsorted[count] = Id[id];
                                count++;
                            } else {
                                if (Itmgroup[i].equals(selectedgroup)) {
                                    CircularNamesorted[count] = CircularName[id];
                                    ItmDatesorted[count] = ItmDate[id];
                                    Itmpathsorted[count] = Itmpath[id];
                                    Itmgroupsorted[count] = Itmgroup[id];
                                    docsorded[count] = doc[id];
                                    Idsorted[count] = Id[id];
                                    count++;
                                }
                            }
                        }
                        adp = new NoticeBoardListAdapter(
                                NoticeBoardActivity.this, CircularNamesorted,
                                ItmDatesorted, Itmgroupsorted, Itmpathsorted, Idsorted);
                        ItmView.setAdapter(adp);
                        adp.notifyDataSetChanged();
                    } catch (Exception ex) {
                        Constants.Logwrite("NoticeBoardActivity", "Exception 757:" + ex.getMessage()
                                + ":::::::" + ex.getStackTrace());
                        Constants.writelog("NoticeBoardActivity",
                                "Exception 757:" + ex.getMessage() + ":::::::"
                                        + ex.getStackTrace());
                    }
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

        } catch (Exception err) {
            Constants.Logwrite("noticeboard", "Exception: 155" + err.getMessage() + ":::::" + err.getStackTrace());
            Constants.writelog("noticeboard", "Exception: " + err.getMessage() + ":::::" + err.getStackTrace());
        }
    }

    /*@Override
    public void onItemClickListener(int layoutPosition) {
        ShowDialog(layoutPosition);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            menu.add(0, 1, 1, "Refresh").setTitle("Refresh");
            menu.add(0, 2, 2, "Add Account").setTitle("Add Account");
            menu.add(0, 3, 3, "Remove Account").setTitle("Remove Account");
            menu.add(0, 4, 4, "Set Default Account").setTitle("Set Default Account");
            menu.add(0, 5, 5, "Sort By Circular").setTitle("Sort By Circular");
            menu.add(0, 6, 6, "Sort By Groups").setTitle("Sort By Groups");
            menu.add(0, 7, 7, "Sort By Date").setTitle("Sort By Date");
            mapacc = Constants.AddAccount(menu, db);
            return true;
        } catch (Exception err) {
            Constants.Logwrite("NoticeBoardActivity", "Exception:" + err.getMessage() + ":::::" + err.getStackTrace());
            return false;
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
                    ActivityCompat.requestPermissions(NoticeBoardActivity.this, perm, 1);
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
                    Toast.makeText(NoticeBoardActivity.this, "Permission given Now you can download the files...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NoticeBoardActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /*private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                List<Contact> contacts = db.GetAllCircularDetails(Integer.parseInt(StudentId),
                        Integer.parseInt(SchoolId), Integer.parseInt(Year_Id), 0);
                if (contacts.size() > 0 && isRefreshCircular == false) {
                    CircularName = new String[contacts.size()];
                    Itmgroup = new String[contacts.size()];
                    ItmDate = new String[contacts.size()];
                    Itmpath = new String[contacts.size()];
                    doc = new String[contacts.size()];
                    Id = new String[contacts.size()];
                    int i = 0;
                    for (Contact cn : contacts) {
                        CircularName[i] = cn.getCirName();
                        Itmgroup[i] = cn.getcirGroupname();
                        ItmDate[i] = cn.getcirDateText();
                        Itmpath[i] = cn.getcirPath();
                        doc[i] = cn.getcirTicks();
                        Id[i] = String.valueOf(cn.getCirId());
                        i++;
                    }
                    groupforsddl = new HashSet<String>(Arrays.asList(Itmgroup)).toArray(new String[0]);
                } else {
                    //GetCircularForStudent();
                    //chnge by vishwa patel 30/4/2019
                    GetCircularForStudentNew(false);
                    List<Contact> contacts1 = db.GetAllCircularDetails(Integer.parseInt(StudentId),
                            Integer.parseInt(SchoolId), Integer.parseInt(Year_Id), 0);
                    if (contacts1.size() > 0) {
                        CircularName = new String[contacts1.size()];
                        Itmgroup = new String[contacts1.size()];
                        ItmDate = new String[contacts1.size()];
                        Itmpath = new String[contacts1.size()];
                        doc = new String[contacts1.size()];
                        Id = new String[contacts1.size()];
                        int i = 0;
                        for (Contact cn : contacts1) {
                            CircularName[i] = cn.getCirName();
                            Itmgroup[i] = cn.getcirGroupname();
                            ItmDate[i] = cn.getcirDateText();
                            Itmpath[i] = cn.getcirPath();
                            doc[i] = cn.getcirTicks();
                            Id[i] = String.valueOf(cn.getCirId());
                            i++;
                        }
                    }
                }
            } catch (Exception ex) {
                Constants.writelog("NoticeBoardActivity", "Exception 1079:" + ex.getMessage() + "::::::::::" + ex.getStackTrace());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                mProgressBar.setVisibility(View.GONE);
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(NoticeBoardActivity.this);
                } else {
                    if (CircularName != null && CircularName.length > 0) {
                        if (groupforsddl != null && groupforsddl.length > 1) {
                            ArrayList<String> strings = new ArrayList<String>(Arrays.asList(groupforsddl));
                            strings.add(0, "All");
                            ddldata = new String[strings.size()];
                            strings.toArray(ddldata);
                            tvgroup.setText("Group");
                            adpddl = new SpinnerItemsAdapter(NoticeBoardActivity.this, ddldata);
                            llSpinner.setVisibility(View.VISIBLE);
                            ddlgroup.setAdapter(adpddl);
                            adp = new NoticeBoardListAdapter(NoticeBoardActivity.this, CircularName, ItmDate, Itmgroup, Itmpath, Id);
                            ItmView.setAdapter(adp);
                            adp.notifyDataSetChanged();
                        } else {
                            llSpinner.setVisibility(View.GONE);
                            if (CircularName != null && CircularName.length > 0) {
                                adp = new NoticeBoardListAdapter(NoticeBoardActivity.this, CircularName, ItmDate, Itmgroup, Itmpath, Id);
                                ItmView.setAdapter(adp);
                                tvdate.setText("");
                                tvgroup.setText("Date");
                            }
                            adp.notifyDataSetChanged();
                        }
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(NoticeBoardActivity.this).create();
                        alertDialog.setTitle("Information");
                        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                        alertDialog.setIcon(R.drawable.information);
                        alertDialog.setCancelable(false);
                        alertDialog.setButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        NoticeBoardActivity.this.finish();
                                        onClickAnimation();
                                    }
                                });
                        mProgressBar.setVisibility(View.GONE);
                        alertDialog.show();
                        return;
                    }
                }
                int lastautoupdatedate = Datastorage.GetLastAutoUpdateNoticeDay(NoticeBoardActivity.this);
                cur_time.setToNow();
                if (lastautoupdatedate != cur_time.monthDay) {
                    GetCircularForStudentNew(true);
                }
            } catch (Exception err) {
                Constants.writelog("NoticeBoardActivity", "Exception 225:" + err.getMessage() + ":::::" + err.getStackTrace());
                Constants.Logwrite("NoticeBoard", "Exception 274" + err.getMessage() + "::::::" + err.getStackTrace());
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int iid = item.getItemId();
        if (iid == android.R.id.home) {
            onBackPressed();
        } else if (iid == 1 || iid == 2 || iid == 3 || iid == 4 || iid == 5 || iid == 6 || iid == 7) {
            if (iid == 1) {
                isRefreshCircular = true;
                //new MyTask().execute();
                //change by vishwa
                GetCircularForStudentNew(false);

            } else if (iid == 2) {
                addAccountClick(NoticeBoardActivity.this);
            } else if (iid == 3) {
                removeAccountClick(NoticeBoardActivity.this);
            } else if (iid == 4) {
                accountListClick(NoticeBoardActivity.this);
            } else if (iid == 5) {
                sortByCircular();
            } else if (iid == 6) {
                sortByGroups();
            } else if (iid == 7) {
                sortByDate();
            }
        } else {
            String details = mapacc.get(iid);
            Constants.SetAccountDetails(details, NoticeBoardActivity.this);
            intent = new Intent(NoticeBoardActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
            onBackClickAnimation();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void sortByCircular() {
        try {
            int ddlselectedItemPosition = 0;
            // int IsddlGroupVisible = ddlgroup.VISIBLE;
            int IsddlGroupVisible = llSpinner.getVisibility();
            if (IsddlGroupVisible == 0) {
                ddlselectedItemPosition = ddlgroup.getSelectedItemPosition();
            }
            if (sorttype == 1) {
                if (ddlselectedItemPosition == 0 || ItmDatesorted.length == 0) {
                    String[] arrtemp = new String[CircularName.length];
                    for (int i = 0; i < CircularName.length; i++) {
                        arrtemp[i] = CircularName[i].toLowerCase()
                                .replace(" ", "") + "###@@@" + i;
                    }
                    ArrayList<String> strings = new ArrayList<String>(Arrays.asList(arrtemp));
                    Collections.sort(strings, Collections.reverseOrder());
                    sorted = new String[strings.size()];
                    strings.toArray(sorted);
                    CircularNamesorted = new String[sorted.length];
                    Itmgroupsorted = new String[sorted.length];
                    ItmDatesorted = new String[sorted.length];
                    Itmpathsorted = new String[sorted.length];
                    docsorded = new String[sorted.length];
                    Idsorted = new String[sorted.length];
                    for (int i = 0; i < sorted.length; i++) {
                        String cir_name = sorted[i];
                        String[] parts = cir_name.split("###@@@");
                        int id = Integer.parseInt(parts[1]);
                        CircularNamesorted[i] = CircularName[id];
                        ItmDatesorted[i] = ItmDate[id];
                        Itmpathsorted[i] = Itmpath[id];
                        Itmgroupsorted[i] = Itmgroup[id];
                        docsorded[i] = doc[id];
                        Idsorted[i] = Id[id];
                    }
                    if (IsddlGroupVisible == 0) {
                        adp = new NoticeBoardListAdapter(NoticeBoardActivity.this,
                                CircularNamesorted, ItmDatesorted, Itmgroupsorted, Itmpathsorted, Idsorted);
                    } else {
                        adp = new NoticeBoardListAdapter(NoticeBoardActivity.this,
                                CircularNamesorted, ItmDatesorted, Itmgroupsorted, Itmpathsorted, Idsorted);
                    }
                    ItmView.setAdapter(adp);
                    adp.notifyDataSetChanged();
                } else {
                    String[] arrtemp = new String[CircularNamesorted.length];
                    for (int i = 0; i < CircularNamesorted.length; i++) {
                        arrtemp[i] = CircularNamesorted[i]
                                .toLowerCase().replace(" ", "")
                                + "###@@@" + i;
                    }
                    ArrayList<String> strings = new ArrayList<>(Arrays.asList(arrtemp));
                    Collections.sort(strings, Collections.reverseOrder());
                    sorted = new String[strings.size()];
                    strings.toArray(sorted);

                    String[] tempCircularNamesorted = new String[sorted.length];
                    String[] tempItmgroupsorted = new String[sorted.length];
                    String[] tempItmDatesorted = new String[sorted.length];
                    String[] tempItmpathsorted = new String[sorted.length];
                    String[] tempdocsorded = new String[sorted.length];
                    String[] tempIdsorded = new String[sorted.length];

                    for (int i = 0; i < sorted.length; i++) {
                        String cir_name = sorted[i];
                        String[] parts = cir_name.split("###@@@");
                        int id = Integer.parseInt(parts[1].toString());
                        tempCircularNamesorted[i] = CircularNamesorted[id];
                        tempItmDatesorted[i] = ItmDatesorted[id];
                        tempItmpathsorted[i] = Itmpathsorted[id];
                        tempItmgroupsorted[i] = Itmgroupsorted[id];
                        tempdocsorded[i] = docsorded[id];
                        tempIdsorded[i] = Idsorted[id];
                    }
                    for (int i = 0; i < sorted.length; i++) {
                        CircularNamesorted[i] = tempCircularNamesorted[i];
                        ItmDatesorted[i] = tempItmDatesorted[i];
                        Itmpathsorted[i] = tempItmpathsorted[i];
                        Itmgroupsorted[i] = tempItmgroupsorted[i];
                        docsorded[i] = tempdocsorded[i];
                        Idsorted[i] = tempIdsorded[i];
                    }

                    adp = new NoticeBoardListAdapter(NoticeBoardActivity.this,
                            CircularNamesorted, ItmDatesorted, Itmgroupsorted, Itmpathsorted, Idsorted);
                    ItmView.setAdapter(adp);
                    adp.notifyDataSetChanged();
                }

                tvname.setBackgroundDrawable(null);
                tvdate.setBackgroundDrawable(null);
                tvgroup.setBackgroundDrawable(null);
                tvname.setBackgroundResource(R.drawable.desc);
                sorttype = 2;

            } else {
                if (ddlselectedItemPosition == 0 || ItmDatesorted.length == 0) {
                    String[] arrtemp = new String[CircularName.length];
                    for (int i = 0; i < CircularName.length; i++) {
                        arrtemp[i] = CircularName[i].toLowerCase()
                                .replace(" ", "") + "###@@@" + i;
                    }
                    ArrayList<String> strings = new ArrayList<String>(Arrays.asList(arrtemp));
                    Collections.sort(strings);
                    sorted = new String[strings.size()];
                    strings.toArray(sorted);
                    CircularNamesorted = new String[sorted.length];
                    Itmgroupsorted = new String[sorted.length];
                    ItmDatesorted = new String[sorted.length];
                    Itmpathsorted = new String[sorted.length];
                    docsorded = new String[sorted.length];
                    Idsorted = new String[sorted.length];

                    for (int i = 0; i < sorted.length; i++) {
                        String cir_name = sorted[i];
                        String[] parts = cir_name.split("###@@@");
                        int id = Integer.parseInt(parts[1]);
                        CircularNamesorted[i] = CircularName[id];
                        ItmDatesorted[i] = ItmDate[id];
                        Itmpathsorted[i] = Itmpath[id];
                        Itmgroupsorted[i] = Itmgroup[id];
                        docsorded[i] = doc[id];
                        Idsorted[i] = Id[id];
                    }

                    if (IsddlGroupVisible == 0) {
                        adp = new NoticeBoardListAdapter(NoticeBoardActivity.this,
                                CircularNamesorted, ItmDatesorted, Itmgroupsorted, Itmpathsorted, Idsorted);
                    } else {
                        adp = new NoticeBoardListAdapter(NoticeBoardActivity.this,
                                CircularNamesorted, ItmDatesorted, Itmgroupsorted, Itmpathsorted, Idsorted);
                    }

                    ItmView.setAdapter(adp);
                    adp.notifyDataSetChanged();
                } else {
                    String[] arrtemp = new String[CircularNamesorted.length];
                    for (int i = 0; i < CircularNamesorted.length; i++) {
                        arrtemp[i] = CircularNamesorted[i]
                                .toLowerCase().replace(" ", "")
                                + "###@@@" + i;
                    }
                    ArrayList<String> strings = new ArrayList<String>(Arrays.asList(arrtemp));
                    Collections.sort(strings);
                    sorted = new String[strings.size()];
                    strings.toArray(sorted);

                    String[] tempCircularNamesorted = new String[sorted.length];
                    String[] tempItmgroupsorted = new String[sorted.length];
                    String[] tempItmDatesorted = new String[sorted.length];
                    String[] tempItmpathsorted = new String[sorted.length];
                    String[] tempdocsorded = new String[sorted.length];
                    String[] tempIdsorded = new String[sorted.length];

                    for (int i = 0; i < sorted.length; i++) {
                        String cir_name = sorted[i];
                        String[] parts = cir_name.split("###@@@");
                        int id = Integer.parseInt(parts[1]);
                        tempCircularNamesorted[i] = CircularNamesorted[id];
                        tempItmDatesorted[i] = ItmDatesorted[id];
                        tempItmpathsorted[i] = Itmpathsorted[id];
                        tempItmgroupsorted[i] = Itmgroupsorted[id];
                        tempdocsorded[i] = docsorded[id];
                        tempIdsorded[i] = Idsorted[id];
                    }
                    for (int i = 0; i < sorted.length; i++) {
                        CircularNamesorted[i] = tempCircularNamesorted[i];
                        ItmDatesorted[i] = tempItmDatesorted[i];
                        Itmpathsorted[i] = tempItmpathsorted[i];
                        Itmgroupsorted[i] = tempItmgroupsorted[i];
                        docsorded[i] = tempdocsorded[i];
                        Idsorted[i] = tempIdsorded[i];
                    }
                    adp = new NoticeBoardListAdapter(NoticeBoardActivity.this,
                            CircularNamesorted, ItmDatesorted, Itmgroupsorted, Itmpathsorted, Idsorted);
                    ItmView.setAdapter(adp);
                    adp.notifyDataSetChanged();
                }
                tvname.setBackgroundDrawable(null);
                tvdate.setBackgroundDrawable(null);
                tvgroup.setBackgroundDrawable(null);
                tvname.setBackgroundResource(R.drawable.asc);
                sorttype = 1;
            }
        } catch (Exception ex) {
            Constants.Logwrite("NoticeBoard", "Exception 299:" + ex.getMessage() + "::::" + ex.getStackTrace());
            Constants.writelog("NoticeBoard", "Exception 299:" + ex.getMessage() + "::::" + ex.getStackTrace());
        }
    }

    public void sortByGroups() {
        try {
            int ddlselectedItemPosition = 0;
            int IsddlGroupVisible = llSpinner.getVisibility();
            // ddlgroup.setVisibility(0);
            if (IsddlGroupVisible == 0) {
                ddlselectedItemPosition = ddlgroup
                        .getSelectedItemPosition();
            }
            if (IsddlGroupVisible == 0) {
                if (sorttype == 1) {
                    if (ddlselectedItemPosition == 0
                            || ItmDatesorted.length == 0) {
                        String[] arrtemp = new String[Itmgroup.length];
                        for (int i = 0; i < Itmgroup.length; i++) {
                            arrtemp[i] = Itmgroup[i].toLowerCase()
                                    .replace(" ", "")
                                    + "###@@@"
                                    + i;
                        }
                        ArrayList<String> strings = new ArrayList<String>(
                                Arrays.asList(arrtemp));
                        Collections.sort(strings, Collections.reverseOrder());
                        sorted = new String[strings.size()];
                        strings.toArray(sorted);
                        CircularNamesorted = new String[sorted.length];
                        Itmgroupsorted = new String[sorted.length];
                        ItmDatesorted = new String[sorted.length];
                        Itmpathsorted = new String[sorted.length];
                        docsorded = new String[sorted.length];
                        Idsorted = new String[sorted.length];

                        for (int i = 0; i < sorted.length; i++) {
                            String cir_name = sorted[i];
                            String[] parts = cir_name.split("###@@@");
                            int id = Integer.parseInt(parts[1]);
                            CircularNamesorted[i] = CircularName[id];
                            ItmDatesorted[i] = ItmDate[id];
                            Itmpathsorted[i] = Itmpath[id];
                            Itmgroupsorted[i] = Itmgroup[id];
                            docsorded[i] = doc[id];
                            Idsorted = new String[id];
                        }

                        adp = new NoticeBoardListAdapter(NoticeBoardActivity.this,
                                CircularNamesorted, ItmDatesorted, Itmgroupsorted, Itmpathsorted, Idsorted);
                        ItmView.setAdapter(adp);
                        adp.notifyDataSetChanged();
                    } else {
                        String[] arrtemp = new String[Itmgroupsorted.length];
                        for (int i = 0; i < Itmgroupsorted.length; i++) {
                            arrtemp[i] = Itmgroupsorted[i]
                                    .toLowerCase().replace(" ", "") + "###@@@" + i;
                        }
                        ArrayList<String> strings = new ArrayList<String>(Arrays.asList(arrtemp));
                        Collections.sort(strings, Collections.reverseOrder());
                        sorted = new String[strings.size()];
                        strings.toArray(sorted);

                        String[] tempCircularNamesorted = new String[sorted.length];
                        String[] tempItmgroupsorted = new String[sorted.length];
                        String[] tempItmDatesorted = new String[sorted.length];
                        String[] tempItmpathsorted = new String[sorted.length];
                        String[] tempdocsorded = new String[sorted.length];
                        String[] tempIdsorded = new String[sorted.length];
                        for (int i = 0; i < sorted.length; i++) {
                            String cir_name = sorted[i];
                            String[] parts = cir_name.split("###@@@");
                            int id = Integer.parseInt(parts[1]);
                            tempCircularNamesorted[i] = CircularNamesorted[id];
                            tempItmDatesorted[i] = ItmDatesorted[id];
                            tempItmpathsorted[i] = Itmpathsorted[id];
                            tempItmgroupsorted[i] = Itmgroupsorted[id];
                            tempdocsorded[i] = docsorded[id];
                            tempIdsorded[i] = Idsorted[id];
                        }
                        for (int i = 0; i < sorted.length; i++) {
                            CircularNamesorted[i] = tempCircularNamesorted[i];
                            ItmDatesorted[i] = tempItmDatesorted[i];
                            Itmpathsorted[i] = tempItmpathsorted[i];
                            Itmgroupsorted[i] = tempItmgroupsorted[i];
                            docsorded[i] = tempdocsorded[i];
                            Idsorted[i] = tempIdsorded[i];
                        }
                        adp = new NoticeBoardListAdapter(NoticeBoardActivity.this,
                                CircularNamesorted, ItmDatesorted, Itmgroupsorted, docsorded, Idsorted);
                        ItmView.setAdapter(adp);
                        adp.notifyDataSetChanged();
                    }
                    tvgroup.setBackgroundDrawable(null);
                    tvdate.setBackgroundDrawable(null);
                    tvname.setBackgroundDrawable(null);
                    tvgroup.setBackgroundResource(R.drawable.desc70);
                    sorttype = 2;

                } else {
                    if (ddlgroup.getSelectedItemPosition() == 0
                            || ItmDatesorted.length == 0) {
                        String[] arrtemp = new String[Itmgroup.length];
                        for (int i = 0; i < Itmgroup.length; i++) {
                            arrtemp[i] = Itmgroup[i].toLowerCase().replace(" ", "") + "###@@@" + i;
                        }
                        ArrayList<String> strings = new ArrayList<String>(Arrays.asList(arrtemp));
                        Collections.sort(strings);
                        sorted = new String[strings.size()];
                        strings.toArray(sorted);
                        CircularNamesorted = new String[sorted.length];
                        Itmgroupsorted = new String[sorted.length];
                        ItmDatesorted = new String[sorted.length];
                        Itmpathsorted = new String[sorted.length];
                        docsorded = new String[sorted.length];
                        Idsorted = new String[sorted.length];
                        for (int i = 0; i < sorted.length; i++) {
                            String cir_name = sorted[i];
                            String[] parts = cir_name.split("###@@@");
                            int id = Integer.parseInt(parts[1].toString());
                            CircularNamesorted[i] = CircularName[id];
                            ItmDatesorted[i] = ItmDate[id];
                            Itmpathsorted[i] = Itmpath[id];
                            Itmgroupsorted[i] = Itmgroup[id];
                            docsorded[i] = doc[id];
                            Idsorted[i] = Id[id];
                        }
                        adp = new NoticeBoardListAdapter(
                                NoticeBoardActivity.this,
                                CircularNamesorted, ItmDatesorted,
                                Itmgroupsorted, Itmpathsorted, Idsorted);
                        ItmView.setAdapter(adp);
                        adp.notifyDataSetChanged();
                    } else {
                        String[] arrtemp = new String[Itmgroupsorted.length];
                        for (int i = 0; i < Itmgroupsorted.length; i++) {
                            arrtemp[i] = Itmgroupsorted[i]
                                    .toLowerCase().replace(" ", "")
                                    + "###@@@" + i;
                        }
                        ArrayList<String> strings = new ArrayList<String>(
                                Arrays.asList(arrtemp));
                        Collections.sort(strings);
                        sorted = new String[strings.size()];
                        strings.toArray(sorted);

                        String[] tempCircularNamesorted = new String[sorted.length];
                        String[] tempItmgroupsorted = new String[sorted.length];
                        String[] tempItmDatesorted = new String[sorted.length];
                        String[] tempItmpathsorted = new String[sorted.length];
                        String[] tempdocsorded = new String[sorted.length];
                        String[] tempIdsorded = new String[sorted.length];

                        for (int i = 0; i < sorted.length; i++) {
                            String cir_name = sorted[i];
                            String[] parts = cir_name
                                    .split("###@@@");
                            int id = Integer.parseInt(parts[1]
                                    .toString());
                            tempCircularNamesorted[i] = CircularNamesorted[id];
                            tempItmDatesorted[i] = ItmDatesorted[id];
                            tempItmpathsorted[i] = Itmpathsorted[id];
                            tempItmgroupsorted[i] = Itmgroupsorted[id];
                            tempdocsorded[i] = docsorded[id];
                            tempIdsorded[i] = Idsorted[id];
                        }
                        for (int i = 0; i < sorted.length; i++) {
                            CircularNamesorted[i] = tempCircularNamesorted[i];
                            ItmDatesorted[i] = tempItmDatesorted[i];
                            Itmpathsorted[i] = tempItmpathsorted[i];
                            Itmgroupsorted[i] = tempItmgroupsorted[i];
                            docsorded[i] = tempdocsorded[i];
                            Idsorted[i] = tempIdsorded[i];
                        }
                        adp = new NoticeBoardListAdapter(
                                NoticeBoardActivity.this,
                                CircularNamesorted, ItmDatesorted,
                                Itmgroupsorted, Itmpathsorted, Idsorted);
                        ItmView.setAdapter(adp);
                        adp.notifyDataSetChanged();
                    }
                    tvgroup.setBackgroundDrawable(null);
                    tvdate.setBackgroundDrawable(null);
                    tvname.setBackgroundDrawable(null);
                    tvgroup.setBackgroundResource(R.drawable.asc70);
                    sorttype = 1;
                }
            }
        } catch (Exception ex) {
            Constants.Logwrite("NoticeBoard", "Exception 119:" + ex.getMessage()
                    + "::::" + ex.getStackTrace());
        }
    }

    public void sortByDate() {
        try {
            int ddlselectedItemPosition = 0;
            int IsddlGroupVisible = llSpinner.getVisibility();
            if (IsddlGroupVisible == 0) {
                ddlselectedItemPosition = ddlgroup.getSelectedItemPosition();
            }
            if (IsddlGroupVisible == 0) {
                if (sorttype == 1) {
                    if (ddlselectedItemPosition == 0
                            || ItmDatesorted.length == 0) {
                        String[] arrtemp = new String[doc.length];
                        for (int i = 0; i < doc.length; i++) {
                            arrtemp[i] = doc[i].toLowerCase()
                                    .replace(" ", "")
                                    + "###@@@"
                                    + i;
                        }
                        ArrayList<String> strings = new ArrayList<String>(
                                Arrays.asList(arrtemp));
                        Collections.sort(strings,
                                Collections.reverseOrder());
                        sorted = new String[strings.size()];
                        strings.toArray(sorted);
                        CircularNamesorted = new String[sorted.length];
                        Itmgroupsorted = new String[sorted.length];
                        ItmDatesorted = new String[sorted.length];
                        Itmpathsorted = new String[sorted.length];
                        docsorded = new String[sorted.length];
                        Idsorted = new String[sorted.length];

                        for (int i = 0; i < sorted.length; i++) {
                            String cir_name = sorted[i];
                            String[] parts = cir_name
                                    .split("###@@@");
                            int id = Integer.parseInt(parts[1]
                                    .toString());
                            CircularNamesorted[i] = CircularName[id];
                            ItmDatesorted[i] = ItmDate[id];
                            Itmpathsorted[i] = Itmpath[id];
                            Itmgroupsorted[i] = Itmgroup[id];
                            docsorded[i] = doc[id];
                            Idsorted[i] = Id[id];
                        }

                        adp = new NoticeBoardListAdapter(
                                NoticeBoardActivity.this,
                                CircularNamesorted, ItmDatesorted,
                                Itmgroupsorted, docsorded, Idsorted);
                        ItmView.setAdapter(adp);
                        adp.notifyDataSetChanged();
                    } else {
                        String[] arrtemp = new String[docsorded.length];
                        for (int i = 0; i < docsorded.length; i++) {
                            arrtemp[i] = docsorded[i].toLowerCase()
                                    .replace(" ", "")
                                    + "###@@@"
                                    + i;
                        }
                        ArrayList<String> strings = new ArrayList<String>(
                                Arrays.asList(arrtemp));
                        Collections.sort(strings,
                                Collections.reverseOrder());
                        sorted = new String[strings.size()];
                        strings.toArray(sorted);

                        String[] tempCircularNamesorted = new String[sorted.length];
                        String[] tempItmgroupsorted = new String[sorted.length];
                        String[] tempItmDatesorted = new String[sorted.length];
                        String[] tempItmpathsorted = new String[sorted.length];
                        String[] tempdocsorded = new String[sorted.length];
                        String[] tempIdsorded = new String[sorted.length];

                        for (int i = 0; i < sorted.length; i++) {
                            String cir_name = sorted[i];
                            String[] parts = cir_name
                                    .split("###@@@");
                            int id = Integer.parseInt(parts[1]
                                    .toString());
                            tempCircularNamesorted[i] = CircularNamesorted[id];
                            tempItmDatesorted[i] = ItmDatesorted[id];
                            tempItmpathsorted[i] = Itmpathsorted[id];
                            tempItmgroupsorted[i] = Itmgroupsorted[id];
                            tempdocsorded[i] = docsorded[id];
                            tempIdsorded[i] = Idsorted[id];
                        }
                        for (int i = 0; i < sorted.length; i++) {
                            CircularNamesorted[i] = tempCircularNamesorted[i];
                            ItmDatesorted[i] = tempItmDatesorted[i];
                            Itmpathsorted[i] = tempItmpathsorted[i];
                            Itmgroupsorted[i] = tempItmgroupsorted[i];
                            docsorded[i] = tempdocsorded[i];
                            Idsorted[i] = tempIdsorded[i];
                        }

                        adp = new NoticeBoardListAdapter(
                                NoticeBoardActivity.this,
                                CircularNamesorted, ItmDatesorted,
                                Itmgroupsorted, Itmpathsorted, Idsorted);
                        ItmView.setAdapter(adp);
                        adp.notifyDataSetChanged();
                    }
                    sorttype = 2;
                    tvdate.setBackgroundDrawable(null);
                    tvname.setBackgroundDrawable(null);
                    tvgroup.setBackgroundDrawable(null);
                    tvdate.setBackgroundResource(R.drawable.desc60);
                } else {
                    if (ddlselectedItemPosition == 0
                            || ItmDatesorted.length == 0) {
                        String[] arrtemp = new String[doc.length];
                        for (int i = 0; i < doc.length; i++) {
                            arrtemp[i] = doc[i].toLowerCase()
                                    .replace(" ", "")
                                    + "###@@@"
                                    + i;
                        }
                        ArrayList<String> strings = new ArrayList<String>(
                                Arrays.asList(arrtemp));
                        Collections.sort(strings);
                        sorted = new String[strings.size()];
                        strings.toArray(sorted);
                        CircularNamesorted = new String[sorted.length];
                        Itmgroupsorted = new String[sorted.length];
                        ItmDatesorted = new String[sorted.length];
                        Itmpathsorted = new String[sorted.length];
                        docsorded = new String[sorted.length];
                        Idsorted = new String[sorted.length];
                        for (int i = 0; i < sorted.length; i++) {
                            String cir_name = sorted[i];
                            String[] parts = cir_name
                                    .split("###@@@");
                            int id = Integer.parseInt(parts[1]
                                    .toString());
                            CircularNamesorted[i] = CircularName[id];
                            ItmDatesorted[i] = ItmDate[id];
                            Itmpathsorted[i] = Itmpath[id];
                            Itmgroupsorted[i] = Itmgroup[id];
                            docsorded[i] = doc[id];
                            Idsorted[i] = Id[id];
                        }
                        adp = new NoticeBoardListAdapter(
                                NoticeBoardActivity.this,
                                CircularNamesorted, ItmDatesorted,
                                Itmgroupsorted, Itmpathsorted, Idsorted);
                        ItmView.setAdapter(adp);
                        adp.notifyDataSetChanged();
                    } else {
                        String[] arrtemp = new String[docsorded.length];
                        for (int i = 0; i < docsorded.length; i++) {
                            arrtemp[i] = docsorded[i].toLowerCase()
                                    .replace(" ", "")
                                    + "###@@@" + i;
                        }
                        ArrayList<String> strings = new ArrayList<String>(Arrays.asList(arrtemp));
                        Collections.sort(strings);
                        sorted = new String[strings.size()];
                        strings.toArray(sorted);

                        String[] tempCircularNamesorted = new String[sorted.length];
                        String[] tempItmgroupsorted = new String[sorted.length];
                        String[] tempItmDatesorted = new String[sorted.length];
                        String[] tempItmpathsorted = new String[sorted.length];
                        String[] tempdocsorded = new String[sorted.length];
                        String[] tempIdsorded = new String[sorted.length];
                        for (int i = 0; i < sorted.length; i++) {
                            String cir_name = sorted[i];
                            String[] parts = cir_name
                                    .split("###@@@");
                            int id = Integer.parseInt(parts[1]
                                    .toString());
                            tempCircularNamesorted[i] = CircularNamesorted[id];
                            tempItmDatesorted[i] = ItmDatesorted[id];
                            tempItmpathsorted[i] = Itmpathsorted[id];
                            tempItmgroupsorted[i] = Itmgroupsorted[id];
                            tempdocsorded[i] = docsorded[id];
                            tempIdsorded[i] = Idsorted[id];
                        }
                        for (int i = 0; i < sorted.length; i++) {
                            CircularNamesorted[i] = tempCircularNamesorted[i];
                            ItmDatesorted[i] = tempItmDatesorted[i];
                            Itmpathsorted[i] = tempItmpathsorted[i];
                            Itmgroupsorted[i] = tempItmgroupsorted[i];
                            docsorded[i] = tempdocsorded[i];
                            Idsorted[i] = tempIdsorded[i];
                        }
                        adp = new NoticeBoardListAdapter(
                                NoticeBoardActivity.this,
                                CircularNamesorted, ItmDatesorted,
                                Itmgroupsorted, Itmpathsorted, Idsorted);
                        ItmView.setAdapter(adp);
                        adp.notifyDataSetChanged();
                    }
                    tvname.setBackgroundDrawable(null);
                    tvdate.setBackgroundDrawable(null);
                    tvgroup.setBackgroundDrawable(null);
                    tvdate.setBackgroundResource(R.drawable.asc60);
                    sorttype = 1;
                }
            }
        } catch (Exception ex) {
            Constants.Logwrite("NoticeBoard", "Exception 119:" + ex.getMessage()
                    + "::::" + ex.getStackTrace());

        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mIsFromHome != null && !mIsFromHome.isEmpty()) {
                super.onBackPressed();
                onBackClickAnimation();
            } else {
                Intent intent = new Intent(NoticeBoardActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                super.onBackPressed();
            }
        } catch (Exception ex) {
            Intent intent = new Intent(NoticeBoardActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            super.onBackPressed();
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("ViewPDF");
        registerReceiver(mBroadCastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBroadCastReceiver);
    }

    protected boolean displayData() {
        boolean isDataExist = false;
        List<Contact> contacts = db.GetAllCircularDetails(Integer.parseInt(StudentId),
                Integer.parseInt(SchoolId), Integer.parseInt(Year_Id), 0);
        if (contacts.size() > 0) {
            isDataExist = true;
            CircularName = new String[contacts.size()];
            Itmgroup = new String[contacts.size()];
            ItmDate = new String[contacts.size()];
            Itmpath = new String[contacts.size()];
            doc = new String[contacts.size()];
            Id = new String[contacts.size()];
            int i = 0;
            for (Contact cn : contacts) {
                CircularName[i] = cn.getCirName();
                Itmgroup[i] = cn.getcirGroupname();
                ItmDate[i] = cn.getcirDateText();
                Itmpath[i] = cn.getcirPath();
                doc[i] = cn.getcirTicks();
                Id[i] = String.valueOf(cn.getCirId());
                i++;
            }
            groupforsddl = new HashSet<String>(Arrays.asList(Itmgroup)).toArray(new String[0]);
            if (CircularName != null && CircularName.length > 0) {
                if (groupforsddl != null && groupforsddl.length > 1) {
                    ArrayList<String> strings = new ArrayList<String>(Arrays.asList(groupforsddl));
                    strings.add(0, "All");
                    ddldata = new String[strings.size()];
                    strings.toArray(ddldata);
                    tvgroup.setText("Group");
                    adpddl = new SpinnerItemsAdapter(NoticeBoardActivity.this, ddldata);
                    llSpinner.setVisibility(View.VISIBLE);
                    ddlgroup.setAdapter(adpddl);
                    adp = new NoticeBoardListAdapter(NoticeBoardActivity.this, CircularName, ItmDate, Itmgroup, Itmpath, Id);
                    ItmView.setAdapter(adp);
                    adp.notifyDataSetChanged();
                } else {
                    llSpinner.setVisibility(View.GONE);
                    if (CircularName != null && CircularName.length > 0) {
                        adp = new NoticeBoardListAdapter(NoticeBoardActivity.this, CircularName, ItmDate, Itmgroup, Itmpath, Id);
                        ItmView.setAdapter(adp);
                        tvdate.setText("");
                        tvgroup.setText("Date");
                    }
                    adp.notifyDataSetChanged();
                }
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(NoticeBoardActivity.this).create();
                alertDialog.setTitle("Information");
                alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                alertDialog.setIcon(R.drawable.information);
                alertDialog.setCancelable(false);
                alertDialog.setButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                NoticeBoardActivity.this.finish();
                                onClickAnimation();
                            }
                        });
                mProgressBar.setVisibility(View.GONE);
                alertDialog.show();
            }
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(NoticeBoardActivity.this).create();
            alertDialog.setTitle("Information");
            alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
            alertDialog.setIcon(R.drawable.information);
            alertDialog.setCancelable(false);
            alertDialog.setButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            NoticeBoardActivity.this.finish();
                            onClickAnimation();
                        }
                    });
            mProgressBar.setVisibility(View.GONE);
            alertDialog.show();
        }
        return isDataExist;
    }

    public void GetCircularForStudentNew(boolean isBackGround) {
        try {
            boolean isDataExist = false;
            if (!isRefreshCircular) {
                isDataExist = displayData();
            }
            int lastautoupdatedate = Datastorage.GetLastAutoUpdateNoticeDay(NoticeBoardActivity.this);
            cur_time.setToNow();
            if (lastautoupdatedate != cur_time.monthDay) {
                isRefreshCircular = true;
                isBackGround = true;
            }
            if (isRefreshCircular || !isDataExist) {
                if (isOnline()) {
                    if (!isBackGround) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    Call<CircularModel> call = ((MyApplication) getApplicationContext())
                            .getmRetrofitInterfaceAppService().GetCircularDetail(Integer.parseInt(SchoolId),
                                    Integer.parseInt(StudentId),
                                    Integer.parseInt(Year_Id), Constants.PLATFORM);
                    call.enqueue(new Callback<CircularModel>() {
                        @Override
                        public void onResponse(Call<CircularModel> call, Response<CircularModel> response) {
                            CircularModel tmps = response.body();
                            if (tmps != null) {
                                mCircularlist.clear();
                                db.DeleteStudentCircular(Integer.parseInt(StudentId), Integer.parseInt(SchoolId));
                                if (tmps.getStrlist() != null && tmps.getStrlist().size() > 0) {
                                    mCircularlist.addAll(tmps.getStrlist());
                                    int count;
                                    count = mCircularlist.size();
                                    CircularName = new String[count];
                                    ItmDate = new String[count];
                                    Itmgroup = new String[count];
                                    doc = new String[count];
                                    Itmpath = new String[count];
                                    for (int i = 0; i < count; i++) {
                                        CircularName[i] = mCircularlist.get(i).getSecond();
                                        Itmgroup[i] = mCircularlist.get(i).getThird();
                                        ItmDate[i] = mCircularlist.get(i).getEighth();
                                        Itmpath[i] = mCircularlist.get(i).getFifth();
                                        doc[i] = mCircularlist.get(i).getSixth();
                                        // Check it is exist in db or not
                                        Boolean isinserted = db.CheckCircularInserted(
                                                Integer.parseInt(StudentId),
                                                Integer.parseInt(SchoolId),
                                                Integer.parseInt(mCircularlist.get(i).getFirst()));

                                        if (!isinserted) {
                                            int res = db.AddCircular(new Contact(Integer
                                                    .parseInt(StudentId), Integer
                                                    .parseInt(SchoolId), Integer
                                                    .parseInt(Year_Id), Integer
                                                    .parseInt(mCircularlist.get(i).getFourth()), mCircularlist.get(i).getThird(),
                                                    Integer.parseInt(mCircularlist.get(i).getFirst()),
                                                    mCircularlist.get(i).getSecond(), mCircularlist.get(i).getEighth(),
                                                    mCircularlist.get(i).getFifth(),
                                                    mCircularlist.get(i).getSixth(), Integer
                                                    .parseInt(mCircularlist.get(i).getSeventh())));
                                            if (res != 1) {
                                                Constants.Logwrite("NoticeBoardActivity", "1198 AddCircular fail.");
                                                Constants.writelog("NoticeBoardActivity", "1198 AddCircular fail.");
                                            }
                                        } else {
                                            int res = db.UpdateCircular(new Contact(Integer
                                                    .parseInt(StudentId), Integer
                                                    .parseInt(SchoolId), Integer
                                                    .parseInt(Year_Id), Integer
                                                    .parseInt(mCircularlist.get(i).getFourth()), mCircularlist.get(i).getThird(),
                                                    Integer.parseInt(mCircularlist.get(i).getFirst()),
                                                    mCircularlist.get(i).getSecond(), mCircularlist.get(i).getEighth(),
                                                    mCircularlist.get(i).getFifth(),
                                                    mCircularlist.get(i).getSixth(), Integer
                                                    .parseInt(mCircularlist.get(i).getSeventh())));
                                            if (res != 1) {
                                                Constants.Logwrite("NoticeBoardActivity", "1525 UpdateCircular fail.");
                                                Constants.writelog("NoticeBoardActivity", "1526 UpdateCircular fail.");
                                            }
                                        }
                                    }
                                    // Refill adapter
                                    groupforsddl = new HashSet<>(Arrays.asList(Itmgroup)).toArray(new String[0]);
                                }
                                if (mProgressBar.getVisibility() == View.VISIBLE)
                                    mProgressBar.setVisibility(View.GONE);
                            }
                            displayData();
                            Datastorage.SetLastAutoUpdateNoticeDay(NoticeBoardActivity.this, cur_time.monthDay);
                        }

                        @Override
                        public void onFailure(Call<CircularModel> call, Throwable t) {
                            Constants.writelog("NoticeBoardActivity", "Exception_1539:" + t.getMessage() + "::::::" + t.getStackTrace());
                            if (mProgressBar.getVisibility() == View.VISIBLE)
                                mProgressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Common.showToast(NoticeBoardActivity.this, getString(R.string.msg_connection));
                    if (mProgressBar.getVisibility() == View.VISIBLE)
                        mProgressBar.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            Constants.writelog("NoticeBoardActivity", "Exception_1547:" + e.getMessage() + "::::::" + e.getStackTrace());
            if (mProgressBar.getVisibility() == View.VISIBLE)
                mProgressBar.setVisibility(View.GONE);
        }
    }

    private class MyTaskliarclick extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
               /* String selectedgroup = "";
                try {
                    selectedgroup = ddldata[position];
                } catch (Exception ex) {
                    Constants.Logwrite("NoticeBoardActivity", "Exception 825" + ex.getMessage()
                            + ":::::" + ex.getStackTrace());
                }
                */
                if (selectedgroup.equals("") || selectedgroup.equals("All") ||
                        Itmpathsorted == null || Itmpathsorted.length < 1) {
                    String path = "";
                    path = Itmpath[position];
                    try {
                        if (isOnline()) {
                            Intent intent = new Intent(NoticeBoardActivity.this, CurriculumPdfViewActivity.class);
                            intent.putExtra("Url", path);
                            Common.showLog("PDF_URL:NoticeBoard", path);
                            intent.putExtra("filename", path);
                            intent.putExtra("Name", A);
                            intent.putExtra("isFrom", "NoticeBoardActiviy");
                            startActivity(intent);
                            finish();
                            onClickAnimation();
                        } else {
                            isShowInternetMsg = true;
                        }
                    } catch (ActivityNotFoundException e) {
                        Constants.writelog("Viwstudentcircular", "Exception 742" + e.getMessage() + ":::::" + e.getStackTrace());
                        Intent intent = new Intent(NoticeBoardActivity.this, CurriculumPdfViewActivity.class);
                        intent.putExtra("Url", path);
                        intent.putExtra("Name", A);
                        intent.putExtra("isFrom", "NoticeBoardActiviy");
                        startActivity(intent);
                        finish();
                        onClickAnimation();
                    } catch (Exception e) {
                        Constants.writelog("Viewstudentcircular", "Exception 757" + e.getMessage() + ":::::" + e.getStackTrace());
                        Intent intent = new Intent(NoticeBoardActivity.this, CurriculumPdfViewActivity.class);
                        intent.putExtra("Url", path);
                        intent.putExtra("Name", A);
                        intent.putExtra("isFrom", "NoticeBoardActiviy");
                        startActivity(intent);
                        finish();
                        onClickAnimation();
                    }
                } else {
                    try {
                        ///change by vishwa patel 30/4/2019
                        if (isOnline()) {
                            Intent intent = new Intent(NoticeBoardActivity.this, CurriculumPdfViewActivity.class);
                            intent.putExtra("Url", Itmpathsorted[position]);
                            intent.putExtra("Name", A);
                            intent.putExtra("isFrom", "NoticeBoardActiviy");
                            startActivity(intent);
                            onClickAnimation();
                        } else {
                            isShowInternetMsg = true;
                        }
                    } catch (ActivityNotFoundException e) {
                        Constants.Logwrite("Viwstudentcircular", "Exception" + e.getMessage() + ":::::" + e.getStackTrace());
                        Intent intent = new Intent(NoticeBoardActivity.this, CurriculumPdfViewActivity.class);
                        intent.putExtra("Name", A);
                        intent.putExtra("Url", Itmpathsorted[position]);
                        intent.putExtra("isFrom", "NoticeBoardActiviy");
                        startActivity(intent);
                        finish();
                        onClickAnimation();
                    }
                }
            } catch (Exception err) {
                Constants.writelog("noticeboard", "Exception:811" + err.getMessage() + ":::::" + err.getStackTrace());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressBar.setVisibility(View.GONE);
            if (isShowInternetMsg) {
                Toast.makeText(NoticeBoardActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_LONG).show();
            }
        }
    }

}