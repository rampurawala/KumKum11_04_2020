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
import com.expedite.apps.kumkum.adapter.MessagesListAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.Contact;
import com.google.android.gms.ads.AdView;

import org.ksoap2.serialization.SoapObject;

import java.util.List;


public class MessagesSelectedMonthActivity extends BaseActivity {
    private ListView lstmsg;
    private String[] messageslist = null;
    private MessagesListAdapter adapter;
    private TextView mTxtUpdate;
    private String Month = "", Year = "", SchoolId = "", StudentId = "", LastUpdatedTime = "", Year_Id = "";
    private ProgressBar mProgressBar;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_selected_month);
        init();
    }

    public void init() {
        mAdView = (AdView) findViewById(R.id.adView);
        showBannerAd(mAdView, ActivityNames.MessagesSelectedMonthActivity);
        db = new DatabaseHandler(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        Constants.setActionbar(getSupportActionBar(), MessagesSelectedMonthActivity.this,
                MessagesSelectedMonthActivity.this,
                "Message", "MessagesOfSelectedMonth",ActivityNames.MessagesSelectedMonthActivity);
        try {
            SchoolId = Datastorage.GetSchoolId(MessagesSelectedMonthActivity.this);
            StudentId = Datastorage.GetStudentId(MessagesSelectedMonthActivity.this);
            LastUpdatedTime = Datastorage
                    .GetLastUpdatedtime(MessagesSelectedMonthActivity.this);
            Year_Id = Datastorage.GetCurrentYearId(MessagesSelectedMonthActivity.this);

            mTxtUpdate = (TextView) findViewById(R.id.tvmarkqueetextmessagesselectedmonth);
            mTxtUpdate.setSelected(true);
            mTxtUpdate.setText(LastUpdatedTime);

            lstmsg = (ListView) findViewById(R.id.lstSelectedMonthMessages);

            Intent intnt = getIntent();
            String str = intnt.getStringExtra("MonthYear");

            String[] parts1 = str.split(",");
            Month = parts1[0].toString();
            Year = parts1[1].toString();
            new MyTask().execute();

            lstmsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    String Messages = adapter.GetMessages(arg2);
                    String Msg_other_det = adapter.GetMessagesOtherdetails(arg2);
                    Intent intent = new Intent(MessagesSelectedMonthActivity.this, SingleMessageActivity.class);
                    intent.putExtra("MSG", Messages);
                    intent.putExtra("OTH", Msg_other_det);
                    startActivity(intent);
                    onClickAnimation();
                }
            });
        } catch (Exception err) {
            Constants.Logwrite("Month List Message:", "On_Create:" + err.getMessage()
                    + "StackTrace::" + err.getStackTrace().toString());
        }
    }

    private String[] GetMessageDetailsForSelectedMonth() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_MESSAGE_DETAIL_SELECTED_MONTH);
        request.addProperty("StudId", StudentId);
        request.addProperty("SchoolId", SchoolId);
        request.addProperty("Para_Month", Month.toString());
        request.addProperty("Para_Year", Year.toString());

        try {
            SoapObject result = Constants.CallWebMethod(
                    MessagesSelectedMonthActivity.this, request, Constants.GET_MESSAGE_DETAIL_SELECTED_MONTH, true);
            if (result != null && result.getPropertyCount() > 0) {
                SoapObject obj2 = (SoapObject) result.getProperty(0);
                if (obj2 != null) {
                    int count = obj2.getPropertyCount();
                    messageslist = new String[count];
                    String[] myarray = new String[count];
                    for (int i = 0; i < count; i++) {
                        myarray[i] = obj2.getProperty(i).toString();
                        messageslist[i] = myarray[i].toString();
                    }
                } else {
                    messageslist = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return messageslist;
        }
        return messageslist;
    }

    private void LogUserVisted() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.LOG_USER_VISITED);
        request.addProperty("moduleid", 5);
        request.addProperty("schoolid", SchoolId);
        request.addProperty("User_Id",
                Datastorage.GetUserId(MessagesSelectedMonthActivity.this));
        request.addProperty("phoneno",
                Datastorage.GetPhoneNumber(MessagesSelectedMonthActivity.this));
        request.addProperty("pageid", 14);

        try {
            Constants.CallWebMethod(MessagesSelectedMonthActivity.this, request,
                    Constants.LOG_USER_VISITED, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            int record_cnt = db.getSMSCount(Integer.parseInt(StudentId),
                    Integer.parseInt(SchoolId));
            Constants.Logwrite("Messagemonthlist", "RecordCount:" + record_cnt);
            if (record_cnt > 0) {
                int cntr = 0;
                List<Contact> contacts = db.GeSelectedMonthMessageList(
                        Integer.parseInt(StudentId),
                        Integer.parseInt(SchoolId), Integer.parseInt(Year_Id),
                        Integer.parseInt(Month), Integer.parseInt(Year));
                messageslist = new String[contacts.size()];
                for (Contact cn : contacts) {
                    String msg = cn.getGlobalText();
                    messageslist[cntr] = msg;
                    cntr++;
                }
            } else {
                LogUserVisted();
                GetMessageDetailsForSelectedMonth();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(MessagesSelectedMonthActivity.this);
                } else {
                    if (messageslist != null && messageslist.length > 0) {
                        adapter = new MessagesListAdapter(MessagesSelectedMonthActivity.this, messageslist);
                        lstmsg.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(MessagesSelectedMonthActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                        alertDialog.setIcon(R.drawable.alert);
                        alertDialog.setButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent intent = new Intent(MessagesSelectedMonthActivity.this, MessagesMonthListActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                        alertDialog.show();
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackClickAnimation();
        MessagesSelectedMonthActivity.this.finish();
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
                    hideKeyboard(MessagesSelectedMonthActivity.this);
                    MessagesSelectedMonthActivity.this.finish();
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
}
