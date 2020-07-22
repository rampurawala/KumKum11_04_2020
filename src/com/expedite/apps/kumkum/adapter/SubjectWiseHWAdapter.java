package com.expedite.apps.kumkum.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.HomeworkPdfActivity;
import com.expedite.apps.kumkum.constants.Constants;

public class SubjectWiseHWAdapter extends BaseAdapter {

    Activity context;
    String[] names;
    String[] filename;
    String[] hw_text;
    //    String[] names = new String[3];
//    String[] hw_text = new String[3];
    String moduleid = "";


    public SubjectWiseHWAdapter(Activity c, String[] names, String[] hwtext, String moduleid) {
        this.context = c;
        this.names = names;
        this.hw_text = hwtext;
        filename = new String[hwtext.length];
//        this.names[0] = "Maths";
//        this.names[1] = "English";
//        this.names[2] = "Science";
//        this.hw_text[0] = "SWA. POTHI NA PAGE NO - 51, 52 SWA. MA PURN KARVA@@PDF@@mathspdf";
//        this.hw_text[1] = "SWA. POTHI NA PAGE NO - 51, 52 SWA. MA PURN KARVA.";
//        this.hw_text[2] = "@@PDF@@mat@@7635751494007490860.pdf";

        this.moduleid = moduleid;
    }

    public int getCount() {
        return names.length;
    }

    public Object getItem(int arg0) {
        return arg0;
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    public View getView(final int position, View arg1, ViewGroup arg2) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_subject_wise_hw, arg2, false);
        TextView tv = (TextView) view.findViewById(R.id.txtsubjectname);
        TextView tv1 = (TextView) view.findViewById(R.id.txtsubjecttext);
        LinearLayout mLLPdf = (LinearLayout) view.findViewById(R.id.pdfll);


        if (hw_text != null && hw_text.length > 0) {
            try {
                String[] parts = hw_text[position].split("@PDF@");
                if (parts.length > 1) {
                    if (moduleid != null && moduleid != "" && (moduleid.equals("15") || moduleid.equals("17"))) {
                        tv.setText(Html.fromHtml(names[position]));
                        tv1.setVisibility(View.GONE);
                    } else {
                        tv.setText(Html.fromHtml(names[position]));
                        if (!parts[0].equalsIgnoreCase("")) {
                            tv1.setText(Html.fromHtml(parts[0]));
                            tv1.setVisibility(View.VISIBLE);
                        } else {
                            tv1.setVisibility(View.GONE);
                        }
                    }
                    filename[position] = parts[1];
                    //  mTxtPdf.setText(Html.fromHtml(parts[1]));
                    mLLPdf.setVisibility(View.VISIBLE);
                } else {
                    if (moduleid != null && moduleid != "" && (moduleid.equals("15") || moduleid.equals("17"))) {
                        tv.setText(Html.fromHtml(names[position]));
                        tv1.setVisibility(View.GONE);
                    } else {
                        tv.setText(Html.fromHtml(names[position]));
                        tv1.setText(Html.fromHtml(hw_text[position]));
                    }
                    mLLPdf.setVisibility(View.GONE);
                }

            } catch (Exception ex) {
                Constants.writelog("SubjectWiseHWAdapter",
                        "Exception 92:" + ex.getMessage());
            }
        }

        mLLPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(context, HomeworkPdfActivity.class);
                    i.putExtra("filename", filename[position].toString());
                    context.startActivity(i);
                } catch (Exception ex) {
                    Constants.writelog("SubjectWiseHWAdapter",
                            "Exception 104:" + ex.getMessage());
                }
            }
        });
        return view;
    }
}
