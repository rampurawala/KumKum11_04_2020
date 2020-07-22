package com.expedite.apps.kumkum.activity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.adapter.NewMessageListAdapter;
import com.expedite.apps.kumkum.common.ConnectionDetector;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.Contact;
import com.expedite.apps.kumkum.model.DailyDiaryCalendarModel;
import com.google.android.gms.ads.AdView;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MessagesExpandableListActivity extends BaseActivity {
    private Button btnLoadNextMessages;
    private String[] messages;
    private int isref = 0, Latest_SMS_ID = 0, totalmessage = 0;
    private DatabaseHandler db = null;
    private HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
    private String SchoolId, StudentId, Year_Id;
    private Time cur_time = new Time();
    //private MessageExpandableAdapter adapter;
    private NewMessageListAdapter adapter;
    private TextView tv;
    private ListView lstmsg;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private GridLayoutManager mGridLayoutManager;
    private String mIsFromHome = "";
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list_expandable);
        try {
            if (getIntent() != null && getIntent().getExtras() != null)
                mIsFromHome = getIntent().getExtras().getString("IsFromHome", "");
            init();
        } catch (Exception ex) {
            Constants.writelog("onCreate:66:Error:", ex.getMessage());
        }
    }

    public void init() {

        lstmsg = (ListView) findViewById(R.id.lstmsg);
//        lstmsg.setAdapter(adapter);
        mAdView = (AdView) findViewById(R.id.adView);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mGridLayoutManager = new GridLayoutManager(MessagesExpandableListActivity.this, 1);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        adapter = new NewMessageListAdapter(MessagesExpandableListActivity.this, messages);
        mRecyclerView.setAdapter(adapter);
        mProgressBar = (ProgressBar) findViewById(R.id.Progressbar);
        StudentId = Datastorage.GetStudentId(MessagesExpandableListActivity.this);
        SchoolId = Datastorage.GetSchoolId(MessagesExpandableListActivity.this);
        Year_Id = Datastorage.GetCurrentYearId(MessagesExpandableListActivity.this);
        db = new DatabaseHandler(MessagesExpandableListActivity.this);
        String title = "";
        int Cnt_Count = 0;
        Cnt_Count = Datastorage.GetMultipleAccount(MessagesExpandableListActivity.this);
        if (Cnt_Count == 1) {
            title = "Messages" + "-"
                    + Datastorage.GetStudentName(MessagesExpandableListActivity.this);

        } else {
            title = "Messages";
        }
        showBannerAd(mAdView, ActivityNames.MessagesExpandableListActivity);
        Constants.setActionbar(getSupportActionBar(), MessagesExpandableListActivity.this,
                MessagesExpandableListActivity.this, title, "MessageList", ActivityNames.MessagesExpandableListActivity);
        lstmsg = (ListView) findViewById(R.id.lstmsg);
        btnLoadNextMessages = (Button) findViewById(R.id.btn_readmore);

        //New 25-4-2016 jaydeep
        int cntr = 0;
        List<Contact> contacts = db.GetAllSMSDetails(Integer.parseInt(StudentId), Integer.parseInt(SchoolId), Integer.parseInt(Year_Id), 0);
        messages = new String[contacts.size()];
        if (messages != null && messages.length > 0) {
            totalmessage = contacts.size();
            for (Contact cn : contacts) {
                String msg = cn.getSMSText();
                messages[cntr] = msg;
                cntr++;
            }
        } else {
            Constants.writelog("MessageListExpandable", "319 No messgae found");
        }
        if (messages != null && messages.length > 0) {
//            adapter = new MessageExpandableAdapter(MessagesExpandableListActivity.this, messages);
//            lstmsg.setAdapter(adapter);
            adapter = new NewMessageListAdapter(MessagesExpandableListActivity.this, messages);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Latest_SMS_ID = db.GetLatestSMSID(Integer.parseInt(StudentId),
                    Integer.parseInt(SchoolId), Integer.parseInt(Year_Id));
            GetMessageDetail(MessagesExpandableListActivity.this, StudentId, SchoolId, Latest_SMS_ID, false);
        }
        //End Changes

        //Update
        cur_time.setToNow();
        int lastautoupdatedate = Datastorage.GetLastAutoUpdateMessageDay(MessagesExpandableListActivity.this);
        Constants.Logwrite("DailyDiary", "LastUpdatedDay:" + lastautoupdatedate);
        if (cur_time.monthDay != lastautoupdatedate) {
            MyTask_Refresh mytaskRef = new MyTask_Refresh();
            if (mytaskRef.getStatus() != AsyncTask.Status.RUNNING
                    || mytaskRef.getStatus() != AsyncTask.Status.PENDING) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mytaskRef.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    mytaskRef.execute();
                }
            } else {
                Constants.Logwrite("messagelist", "Async taks is running.");
            }
        }
        //End Update

        lstmsg.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                tv = (TextView) view.findViewById(R.id.txt_messages);
                tv.setMaxLines(60);
                android.view.ViewGroup.LayoutParams par = tv.getLayoutParams();
                par.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tv.setLayoutParams(par);
            }
        });

        btnLoadNextMessages.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MessagesExpandableListActivity.this, MessagesMonthListActivity.class);
                startActivity(intent);
                onClickAnimation();
            }
        });
    }

    public void GetMessageDetail(Context activity, final String studId, final String schoolId, int Latest_SMS_ID, boolean isBackground) {
        try {
            final DatabaseHandler db = new DatabaseHandler(activity);
            String MsgId = "";
            ConnectionDetector cd = new ConnectionDetector(activity);

            if (isref == 1) {
                MsgId = "0";
            } else {
                MsgId = String.valueOf(Latest_SMS_ID);
            }

            if (isref == 1 || Latest_SMS_ID < 1) {
                String yearid = Datastorage.GetCurrentYearId(activity);
                if (cd.isConnectingToInternet()) {
                    if (!isBackground) {
                        Constants.writelog("MessagesExpandableListActivity.cs", "Progress Start 245.");
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    Call<DailyDiaryCalendarModel> mCall = ((MyApplication) activity.getApplicationContext()).getmRetrofitInterfaceAppService()
                            .GetMessageDetail(studId, schoolId, MsgId, yearid, "all", Constants.PLATFORM);
                    mCall.enqueue(new retrofit2.Callback<DailyDiaryCalendarModel>() {
                        @Override
                        public void onResponse(Call<DailyDiaryCalendarModel> call, Response<DailyDiaryCalendarModel> response) {
                            DailyDiaryCalendarModel tmp = response.body();
                            try {
                                if (tmp != null && tmp.strlist.size() > 0) {
                                    for (int i = 0; i < tmp.strlist.size(); i++) {
                                        int ModId = Integer.parseInt(tmp.strlist.get(i).fifth);
                                        int SMS_DAY = Integer.parseInt(tmp.strlist.get(i).first);
                                        int SMS_MONTH = Integer.parseInt(tmp.strlist.get(i).second);
                                        int SMS_YEAR = Integer.parseInt(tmp.strlist.get(i).third);
                                        int SMS_MSG_ID = Integer.parseInt(tmp.strlist.get(i).sixth);
                                        Boolean Ismessgaeinsert = false;
                                        String msg = "";
                                        if (ModId == 0) {
                                            msg = tmp.strlist.get(i).eighth + "##,@@" + tmp.strlist.get(i).nineth;
                                        } else {
                                            msg = tmp.strlist.get(i).eighth;
                                        }
                                        if (ModId == 5) {
                                            Ismessgaeinsert = db
                                                    .CheckAbsentMessageInsertorNot(
                                                            Integer.parseInt(studId),
                                                            Integer.parseInt(schoolId),
                                                            SMS_DAY, SMS_MONTH, SMS_YEAR,
                                                            ModId);
                                        } else {
                                            Ismessgaeinsert = db.CheckMessageInsertorNot(
                                                    Integer.parseInt(studId),
                                                    Integer.parseInt(schoolId), SMS_MSG_ID);
                                        }
                                        if (Ismessgaeinsert) {
                                            db.Updatesms(new Contact(
                                                    Integer.parseInt(tmp.strlist.get(i).sixth),
                                                    msg,
                                                    Integer.parseInt(studId),
                                                    Integer.parseInt(schoolId),
                                                    Integer.parseInt(tmp.strlist.get(i).first),
                                                    Integer.parseInt(tmp.strlist.get(i).second),
                                                    Integer.parseInt(tmp.strlist.get(i).third),
                                                    Integer.parseInt(tmp.strlist.get(i).fifth),
                                                    Integer.parseInt(tmp.strlist.get(i).seventh)));
                                            // Constants.writelog("DailyDiary",
                                            // "SmsUpdate()949 NO"+i+" Messageid:"+msgitem[4].toString());
                                        } else {
                                            db.AddSMS(new Contact(
                                                    Integer.parseInt(tmp.strlist.get(i).sixth),
                                                    msg,
                                                    Integer.parseInt(studId),
                                                    Integer.parseInt(schoolId),
                                                    Integer.parseInt(tmp.strlist.get(i).first),
                                                    Integer.parseInt(tmp.strlist.get(i).second),
                                                    Integer.parseInt(tmp.strlist.get(i).third),
                                                    Integer.parseInt(tmp.strlist.get(i).fifth),
                                                    Integer.parseInt(tmp.strlist.get(i).seventh)));
                                        }
                                    }
                                }

                                int cntr = 0;
                                List<Contact> contacts = db
                                        .GetAllSMSDetails(
                                                Integer.parseInt(StudentId),
                                                Integer.parseInt(SchoolId),
                                                Integer.parseInt(Year_Id), 0);
                                messages = new String[contacts.size()];
                                if (messages != null && messages.length > 0) {
                                    totalmessage = contacts.size();
                                    for (Contact cn : contacts) {
                                        String msg = cn.getSMSText();
                                        messages[cntr] = msg;
                                        cntr++;
                                    }
                                } else {
                                    Constants.writelog("MessageListExpandable", "319 No messgae found");
                                }
                                if (messages != null && messages.length > 0) {
                                    adapter = new NewMessageListAdapter(MessagesExpandableListActivity.this, messages);
                                    mRecyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    AlertDialog alertDialog = new AlertDialog.Builder(
                                            MessagesExpandableListActivity.this).create();
                                    // Setting Dialog Title
                                    alertDialog.setTitle("Alert");
                                    // Setting Dialog Message
                                    alertDialog
                                            .setMessage(SchoolDetails.MsgNoDataAvailable);
                                    // Setting Icon to Dialog
                                    alertDialog.setIcon(R.drawable.alert);
                                    alertDialog.setCancelable(false);
                                    // Setting OK Button
                                    alertDialog.setButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    Intent intent = new Intent(
                                                            MessagesExpandableListActivity.this,
                                                            HomeActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                    alertDialog.show();
                                }
                            } catch (Exception ex) {
                                Constants.writelog("GetMessageDetail:374:ErrorMsg::", ex.getMessage());
                            } finally {
                                if (mProgressBar != null && mProgressBar.isShown()) {
                                    mProgressBar.setVisibility(View.GONE);
                                    Constants.writelog("MessagesExpandableListActivity.cs", "Progress End 374.");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DailyDiaryCalendarModel> call, Throwable t) {
                            if (mProgressBar != null && mProgressBar.isShown()) {
                                mProgressBar.setVisibility(View.GONE);
                                Constants.writelog("MessagesExpandableListActivity.cs", "Progress End 374.");
                            }
                        }
                    });
                } else {
                    Toast.makeText(MessagesExpandableListActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception ex) {
            Constants.writelog("GetMessageDetail:387:ErrorMsg::", ex.getMessage());
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            menu.clear();
            mapacc = new HashMap<Integer, String>();
            menu.add(0, 1, 1, "Refresh").setTitle("Refresh");
            menu.add(0, 2, 2, "Add Account").setTitle("Add Account");
            // menu.add(0, 3, 3, "Remove Account").setTitle("Remove Account");
            menu.add(0, 4, 4, "Set Default Account").setTitle("Set Default Account");
            mapacc = Constants.AddAccount(menu, db);

        } catch (Exception err) {
            Constants.Logwrite("MessagesListActivity:", "onPrepareOptionsMenu:" + err.getMessage()
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
            hideKeyboard(MessagesExpandableListActivity.this);
            ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
            if (taskList.get(0).numActivities == 1 &&
                    taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
                MessagesExpandableListActivity.this.finish();
            } else {
                Intent in = new Intent(MessagesExpandableListActivity.this, HomeActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(in);
                finish();
            }
            onBackClickAnimation();
        } else if (iid == 1 || iid == 2 || iid == 3 || iid == 4) {
            if (iid == 1) {
                isref = 1;
                GetMessageDetail(MessagesExpandableListActivity.this, StudentId, SchoolId, 0, false);
            } else if (iid == 2) {
                addAccountClick(MessagesExpandableListActivity.this);
            } else if (iid == 3) {
                removeAccountClick(MessagesExpandableListActivity.this);
            } else {
                accountListClick(MessagesExpandableListActivity.this);
            }
        } else {
            String details = mapacc.get(iid).toString();
            Constants.SetAccountDetails(details, MessagesExpandableListActivity.this);
            intent = new Intent(MessagesExpandableListActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
            onBackClickAnimation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mIsFromHome != null && !mIsFromHome.isEmpty()) {
            super.onBackPressed();
            MessagesExpandableListActivity.this.finish();
            onBackClickAnimation();
        } else {
            Intent intent = new Intent(MessagesExpandableListActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            onBackClickAnimation();
            super.onBackPressed();
        }
    }

    // for referash all message after loading message list
    private class MyTask_Refresh extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // for test Auto refresh
            Constants.Logwrite("MessageListExpandable",
                    "mytask_Refresh: DoInBackground Start");
            Latest_SMS_ID = 0;
            Constants.Logwrite("MessageListExpandable", "mytask_Refresh: latest_sms_id:"
                    + Latest_SMS_ID);
            GetMessageDetail(MessagesExpandableListActivity.this, StudentId, SchoolId, Latest_SMS_ID, true);
            //GetMessageDetailsLatestKumKum2(false);
            Constants.Logwrite("MessageListExpandable", "mytask_Refresh: DoInBackground End");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Constants.Logwrite("MessageListExpandable", "mytask_Refresh: Post Start");
            int cntr = 0;
            try {
                List<Contact> contacts = db.GetAllSMSDetails(Integer
                                .parseInt(StudentId), Integer.parseInt(SchoolId),
                        Integer.parseInt(Year_Id), 0);
                messages = new String[contacts.size()];
                if (contacts != null && contacts.size() != totalmessage) {
                    if (messages != null && messages.length > 0) {
                        for (Contact cn : contacts) {
                            String msg = cn.getSMSText();
                            messages[cntr] = msg;
                            cntr++;
                        }
                    }
                    if (messages != null && messages.length > 0) {
//                        adapter = new MessageExpandableAdapter(MessagesExpandableListActivity.this, messages);
//                        lstmsg.setAdapter(adapter);
                        adapter = new NewMessageListAdapter(MessagesExpandableListActivity.this, messages);
                        mRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        Constants.Logwrite("MessageListExpandable",
                                "mytask_Refresh: listview refresh");
                    }
                } else {
                    Constants.writelog("MessagelistExpandable",
                            "Post 246: No Updated Record Found.");
                }
                Constants.Logwrite("MessageListExpandable", "mytask_Refresh: post end");
                try {
                    Datastorage.SetLastAutoUpdateMessageDay(
                            MessagesExpandableListActivity.this, cur_time.monthDay);
                    // cur_time.monthDay
                } catch (Exception ex) {
                }
            } catch (Exception ex) {
                Constants.writelog("MessageListExpandable",
                        "OnBackRefresh()195 Ex:" + ex.getMessage() + "::::::"
                                + ex.getStackTrace());
            }
        }

        ;

    }
}
