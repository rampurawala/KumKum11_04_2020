package com.expedite.apps.kumkum.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;

public class SingleMessageNotificationActivity extends BaseActivity {
    private TextView txtmsg, txtmsgotherdet, tv;
    private Button btn_other;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single__message__notification);
        init();
    }

    public void init() {
        Constants.setActionbar(getSupportActionBar(), SingleMessageNotificationActivity.this,
                getApplicationContext(), "SingleMessageNotification",
                "SingleMessageNotificationActivity", ActivityNames.SingleMessageNotificationActivity);
        try {

            btn_other = (Button) findViewById(R.id.btn_viewotherMsg);
            tv = (TextView) findViewById(R.id.tvmarkqueetextsiglemessage);
            tv.setSelected(true);
            tv.setText(Datastorage.GetLastUpdatedtime(getApplicationContext()));
            Intent intnt = getIntent();
            txtmsg = (TextView) findViewById(R.id.txtsinglemessage);
            txtmsg.setLinksClickable(true);
            txtmsg.setMovementMethod(LinkMovementMethod.getInstance());
            txtmsgotherdet = (TextView) findViewById(R.id.txtotherdetails);
            String message = intnt.getStringExtra("MSG");
            txtmsg.setText(message);
            String msg_oth_det = intnt.getStringExtra("OTH");
            String islasthwmsg = intnt.getStringExtra("IsLastHomeworkMsg");
            String date = intnt.getStringExtra("date");
            if (date == null || date.equals("")) {
                txtmsgotherdet.setText(msg_oth_det);
            } else {
                txtmsgotherdet.setText(date);
            }
            if (islasthwmsg.equals("1")) {
                btn_other.setVisibility(View.VISIBLE);
            }
        } catch (Exception err) {
            Constants.Logwrite("SingleMessageActivity", "msg:" + err.getMessage());
            Constants.writelog(
                    "SingleMessageActivity",
                    "Erorr: 71" + err.getMessage() + "  StackTrace:  "
                            + err.getStackTrace());
        }
    }

}
