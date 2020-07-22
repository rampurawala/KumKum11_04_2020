package com.expedite.apps.kumkum.adapter;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;

public class MessagesListAdapter extends BaseAdapter {

    private Context context;
    private String[] messages;
    private LayoutInflater mLayoutInflater;

    public MessagesListAdapter(Context context, String[] messages) {
        this.context = context;
        this.messages = messages;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return messages.length;
    }

    public Object getItem(int arg0) {
        return arg0;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public String GetMessages(int arg0) {
        String[] items = messages[arg0].split("##,@@");
        String date = items[1];
        String msg = items[0];
        return msg;
    }

    public String GetMessagesOtherdetails(int arg0) {
        String[] items = messages[arg0].split("##,@@");
        String date = items[1];
        return date;
    }

    public View getView(int position, View mview, ViewGroup viewGroups) {
        View view = mLayoutInflater.inflate(R.layout.activity_messages_item_list, viewGroups, false);

        TextView message = (TextView) view.findViewById(R.id.txtmessages);
        TextView txtdate = (TextView) view.findViewById(R.id.txtmessagesdate);

        if (messages[position] != null && !messages[position].isEmpty()) {
            String[] items = messages[position].split("##,@@");
            if (items != null && items.length >= 1) {

                message.setText(Html.fromHtml(items[0]));
            }
            if (items != null && items.length >= 2) {
                txtdate.setText(((BaseActivity) context).convertDate(context, items[1]));
            }
        }
        return view;
    }


}
