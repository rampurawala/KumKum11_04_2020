package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.constants.Constants;

import java.util.ArrayList;
import java.util.List;

public class SubJectWiseHWNTDNAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> date;
    private ArrayList<String> subName;
    private ArrayList<String> subText;
    private LayoutInflater inflater;

    public SubJectWiseHWNTDNAdapter(Context _mContext, List<String> date2,
                                    ArrayList<String> _subDetails, ArrayList<String> _subText) {
        this.mContext = _mContext;
        this.date = date2;
        this.subName = _subDetails;
        this.subText = _subText;
    }

    public int getCount() {
        return date.size();
    }

    public Object getItem(int position) {
        return date.size();
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        try {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_sub_ject_wise_hwntdn, parent, false);
            TextView subname = (TextView) view.findViewById(R.id.txthwntdnsubjectname);
            TextView subtext = (TextView) view.findViewById(R.id.txthwntdnsubjecttext);

            subname.setText(Html.fromHtml(subName.get(position)));
            subtext.setText(Html.fromHtml(subText.get(position)));

        } catch (Exception err) {
            Constants.writelog("SubjectWiseHWND", "Ex 67:" + err.getMessage());
        }
        return view;
    }

}
