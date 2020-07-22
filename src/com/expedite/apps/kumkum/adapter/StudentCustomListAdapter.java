package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.model.StudListitemDetails;

import java.util.ArrayList;


public class StudentCustomListAdapter extends BaseAdapter {

    private static ArrayList<StudListitemDetails> itemDetailsrrayList;
    private LayoutInflater layoutInflator;
    private Context context;

    public StudentCustomListAdapter(ArrayList<StudListitemDetails> result, Context context) {
        itemDetailsrrayList = result;
        context = context;
        layoutInflator = LayoutInflater.from(context);
    }

    public int getCount() {
        return itemDetailsrrayList.size();
    }

    public Object getItem(int position) {
        return itemDetailsrrayList.get(position);

    }

    public String getItemName(int position) {
        return itemDetailsrrayList.get(position).getName();

    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
//        layoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflator.inflate(R.layout.activity_school_mates_list, parent, false);
        TextView textview = (TextView) row.findViewById(R.id.txtmessages);
        if (itemDetailsrrayList.get(position).getName() != null &&
                !itemDetailsrrayList.get(position).getName().isEmpty())
            textview.setText(itemDetailsrrayList.get(position).getName());
        return (row);

    }


}
