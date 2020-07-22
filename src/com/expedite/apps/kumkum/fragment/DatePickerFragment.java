package com.expedite.apps.kumkum.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.LeaveApplyActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tejas patel on 31-07-2018.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog
        .OnDateSetListener {
    public DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    TextView edtDate;
    Date mindate = null;
    Date date = null;

    public DatePickerFragment newInstance(TextView editText, String selecteddate, String minimumDate) {
        Bundle args = new Bundle();
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        fragment.setInput(editText);
        fragment.setDate(selecteddate);
        fragment.setDateFormat("dd/MM/yyyy");
        fragment.setMinDate(minimumDate);
        return fragment;
    }


    public void setDateFormat(String format) {
        targetFormat = new SimpleDateFormat(format, Locale.getDefault());
    }

    public void setDate(String dt) {
        try {
            this.date = targetFormat.parse(dt);
        } catch (Exception e) {
            this.date = null;
            e.printStackTrace();
        }
    }

    public void setMinDate(String dt) {
        try {
            this.mindate = targetFormat.parse(dt);
        } catch (Exception e) {
            this.mindate = null;
            e.printStackTrace();
        }
    }

    public void setInput(TextView editText) {
        edtDate = editText;
    }


    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        try {
            Date mindate = targetFormat.parse(edtDate.getText().toString());
            calendar.setTime(mindate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), R.style.appCompatDialog
                , this, year, month, day);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        if (mindate != null) {
            dpd.getDatePicker().setMinDate(mindate.getTime());
        }
        return dpd;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        view.setMinDate(cal.getTimeInMillis());
        cal.set(year, month, day, 0, 0, 0);
        Date chosenDate = cal.getTime();
        String formattedDate = targetFormat.format(chosenDate);
        edtDate.setText(formattedDate);
        try {
            ((LeaveApplyActivity) getActivity()).ShowHalfDay();
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }
}
