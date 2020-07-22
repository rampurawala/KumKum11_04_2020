package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.ExpandableTextView;


/**
 * Created by Jaydeep on 10-09-16.
 */
public class NewMessageListAdapter extends RecyclerView.Adapter {
    String[] messages;
    private Context mContext;
    String date = "";
    private LayoutInflater mLayoutInflater;

    public NewMessageListAdapter(Context context, String[] messages) {
        this.messages = messages;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.activity_listview, parent, false);
        return new CustomViewHolder(view);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int length=0;
        if(messages!=null)
        {
            length=messages.length;
        }
        return length;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CustomViewHolder) {
//            if (messages[position].contains("##@@USP##@@")) {
//                String[] items = messages[position].split("##@@USP##@@");
//                String msg = "";
//                int Mod_Id = 0;
//                if (items != null && items.length >= 1)
//                    msg = items[0];
//                else
//                    Constants.Logwrite("GenListInMonth", "arg_ind " + position + "    " + messages[position]);
//                String[] otherspltr = items[1].split("##HWOTHERDET@@");
//                Mod_Id = Integer.parseInt(otherspltr[2].toString());
//                if (Mod_Id == 9) {
//                    ((CustomViewHolder) holder).tv2.setTextSize(25);
//                } else {
//                    ((CustomViewHolder) holder).tv2.setTextSize(25);
//                }
//                ((CustomViewHolder) holder).tv2.setText(msg);
//            } else {
            String[] items = messages[position].split("##,@@");
            if (items[0] != null && !items[0].isEmpty())
                ((CustomViewHolder) holder).txtMessage.setText(items[0]);

            if (items != null && items.length >= 2) {
                date = items[1];
                if (date != null && !date.isEmpty())
                    ((CustomViewHolder) holder).txtDate.setText(
                            ((BaseActivity) mContext).convertDate(mContext, date));
            }
        }
//        ((CustomViewHolder) holder).txtMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((CustomViewHolder) holder).txtMessage.setMaxLines(99);
//            }
//        });
//        }


    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView  txtDate, tv2;
        ExpandableTextView txtMessage;

        public CustomViewHolder(View view) {
            super(view);
            txtMessage = (ExpandableTextView) view.findViewById(R.id.txt_messages);
            txtDate = (TextView) view.findViewById(R.id.txtmessagesdate);
//            tv2 = (TextView) view.findViewById(R.id.txtlessonmessages);

        }
    }


}



