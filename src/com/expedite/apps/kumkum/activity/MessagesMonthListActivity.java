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
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.adapter.AccountListAdapter;
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


public class MessagesMonthListActivity extends BaseActivity {
    private String[] monthslist = null;
    private ListView lstmonthlist;
    private HashMap<String, String> map = new HashMap<>();
    private AccountListAdapter itemsAdapter;
    private TextView mTxtUpdateHint;
    private String SchoolId = "", StudentId = "", LastUpdatedTime = "", Year_Id = "", mTitle = "",mActivityName="";
    private ProgressBar mProgressBar;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_monthlist);

        init();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);

            db = new DatabaseHandler(MessagesMonthListActivity.this);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            SchoolId = Datastorage.GetSchoolId(MessagesMonthListActivity.this);
            StudentId = Datastorage.GetStudentId(MessagesMonthListActivity.this);
            LastUpdatedTime = Datastorage.GetLastUpdatedtime(MessagesMonthListActivity.this);
            Year_Id = Datastorage.GetCurrentYearId(MessagesMonthListActivity.this);
            int Cnt_Count = Datastorage.GetMultipleAccount(MessagesMonthListActivity.this);
            if (Cnt_Count == 1) {
                mTitle = "Month List" + "-" + Datastorage.GetStudentName(MessagesMonthListActivity.this);
            } else {
                mTitle = "Month List";
            }
            mActivityName = ActivityNames.MessagesMonthListActivity;
            showBannerAd(mAdView, ActivityNames.MessagesMonthListActivity);
            Constants.setActionbar(getSupportActionBar(), MessagesMonthListActivity.this,
                    MessagesMonthListActivity.this, mTitle, "MessagesMonthListActivity",mActivityName);
            mTxtUpdateHint = (TextView) findViewById(R.id.tvmarkqueetextmessagesmonthlist);
            mTxtUpdateHint.setSelected(true);
            mTxtUpdateHint.setText(LastUpdatedTime);
            lstmonthlist = (ListView) findViewById(R.id.lstmessagesmonthlist1);
            new MyTask().execute();
            lstmonthlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
                    String item = itemsAdapter.getItenName(arg2).toString();
                    String Mont_Year = map.get(item).toString();
                    String[] get_det = Mont_Year.split(",");

                    int msg_count = Integer.parseInt(get_det[2]);
                    if (msg_count == 0) {
                        ShowalertDilougue("Information",
                                "No records found in selected month..");
                    } else {
                        Intent intent = new Intent(MessagesMonthListActivity.this, MessagesSelectedMonthActivity.class);
                        intent.putExtra("MonthYear", Mont_Year);
                        startActivity(intent);
                        onClickAnimation();
                    }
                }

                private void ShowalertDilougue(String settitle, String setmsg) {
                    try {
                        AlertDialog alertDialog = new AlertDialog.Builder(MessagesMonthListActivity.this).create();
                        alertDialog.setTitle(settitle);
                        alertDialog.setMessage(setmsg);
                        alertDialog.setIcon(R.drawable.information);
                        alertDialog.setButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        alertDialog.show();

                    } catch (Exception err) {
                        Constants.Logwrite("Month List Message:", "ShowalertDilougue:" + err.getMessage()
                                + "StackTrace::" + err.getStackTrace().toString());

                    }
                }
            });
        } catch (Exception err) {
            Constants.Logwrite("Month List Message:", "On Login Click:" + err.getMessage()
                    + "StackTrace::" + err.getStackTrace().toString());
        }
    }

    private void getmonthlist() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_MONTH_LIST);
        request.addProperty("year_id", Year_Id);
        request.addProperty("Schoolid", SchoolId);
        request.addProperty("Studid", StudentId);

        try {
            SoapObject result = Constants.CallWebMethod(
                    MessagesMonthListActivity.this, request, Constants.GET_MONTH_LIST, true);
            if (result != null && result.getPropertyCount() > 0) {
                SoapObject obj2 = (SoapObject) result.getProperty(0);
                String[] output = null;
                if (obj2 != null) {
                    int count = obj2.getPropertyCount();
                    output = new String[count];
                    monthslist = new String[count];
                    for (int i = 0; i < count; i++) {
                        output[i] = obj2.getProperty(i).toString();
                        String[] info = output[i].split(",");
                        String monthnamewithcount = info[0].toString() + "("
                                + info[3] + ")";
                        String month_and_id = info[1] + ","
                                + info[2] + "," + info[3];
                        monthslist[i] = monthnamewithcount;
                        map.put(monthnamewithcount, month_and_id);
                    }
                }
            }
        } catch (Exception e) {
            Constants.writelog("Messagesmonthlist", "Error: 394 " + e.getMessage() + " StackTrace: " + e.getStackTrace());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MessagesMonthListActivity.this.finish();
        onBackClickAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    hideKeyboard(MessagesMonthListActivity.this);
                    MessagesMonthListActivity.this.finish();
                    onBackClickAnimation();
                    break;
                default:
                    break;
            }

        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // LogUserVisted();
                int record_cnt = db.getSMSCount(Integer.parseInt(StudentId),
                        Integer.parseInt(SchoolId));
                Constants.Logwrite("Messagemonthlist", "RecordCount:" + record_cnt);
                if (record_cnt > 0) {
                    int cntr = 0;
                    List<Contact> contacts = db.GetMessageMonthList(
                            Integer.parseInt(StudentId),
                            Integer.parseInt(SchoolId),
                            Integer.parseInt(Year_Id));
                    monthslist = new String[contacts.size()];
                    if (monthslist != null && monthslist.length > 0) {
                        for (Contact cn : contacts) {
                            String msg = cn.getSMSText();
                            String[] info = msg.split(",");

                            String monthnamewithcount = info[0]
                                    + "(" + info[3] + ")";
                            String month_and_id = info[1].toString() + ","
                                    + info[2] + ","
                                    + info[3];

                            monthslist[cntr] = monthnamewithcount;
                            map.put(monthnamewithcount, month_and_id);
                            cntr++;
                        }
                    } else {
                        getmonthlist();
                    }
                } else {
                    getmonthlist();
                }
            } catch (Exception ex) {
                Constants.writelog("MessageMonthList", "232 Ex:" + ex.getMessage() + "::::::" + ex.getStackTrace());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(MessagesMonthListActivity.this);
                } else {
                    if (monthslist != null && monthslist.length > 0) {
                        itemsAdapter = new AccountListAdapter(MessagesMonthListActivity.this, monthslist);
                        lstmonthlist.setAdapter(itemsAdapter);
                        itemsAdapter.notifyDataSetChanged();
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(MessagesMonthListActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                        alertDialog.setIcon(R.drawable.alert);
                        alertDialog.setButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        MessagesMonthListActivity.this.finish();
                                        onBackClickAnimation();
                                    }
                                });
                        alertDialog.show();
                        return;
                    }

                }
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception ex) {
                Constants.writelog("MessageMonthList", "324 Ex:" + ex.getMessage() + "::::::" + ex.getStackTrace());
                mProgressBar.setVisibility(View.GONE);
            }

        }

    }
}
