package com.expedite.apps.kumkum.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.SplashActivity;
import com.expedite.apps.kumkum.adapter.CalendatDataAdapter;
import com.expedite.apps.kumkum.adapter.DailyDiaryMonthListAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.ConnectionDetector;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.common.ExpandableHeightGridView;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.CalendarListModel;
import com.expedite.apps.kumkum.model.Contact;
import com.expedite.apps.kumkum.model.DailyDiaryCalendarModel;
import com.google.android.gms.ads.AdView;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class DailyDiaryCalanderActivity extends BaseActivity {
    private static final String tag = "MyCalendarActivity";
    private Time cur_time = new Time();
    private TextView selectedDayMonthYearButton;
    private ExpandableHeightGridView calendarView, gridview_month;
    private GridCellAdapter adapter;
    private Calendar _calendar;
    private int currentmonth = 0, ismsgref = 1, month = 0, year = 0, Latest_SMS_ID = 0, eventCurrentMonth = 0, eventCurrentYear = 0;
    private String[] monthslist = null;
    private int[] monthid, yearid;
    RecyclerView mEventRecylerview;
    private final String[] months = {"January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October", "November", "December"};
    private String msgtype = "1";
    private String[] messages = null, msgday = {""}, msgmont = {""}, msgtxt = {""};
    private ArrayList<String> eventDay, eventMonth, eventDetails, eventDate;
    private ArrayList<Integer> eventMonthId = new ArrayList<Integer>(), eventYearid = new ArrayList<Integer>(), eventCount = new ArrayList<Integer>();
    private HashMap<Integer, String> mapacc = new HashMap<Integer, String>();
    private HashMap<String, String> mapmonth = new HashMap<String, String>();
    private String StudId, SchoolId, Year_Id,IsNotification="",isFrom="";

    private ProgressBar mProgressBar;
    LinearLayout empty_daily_dairy;
    private ArrayList<CalendarListModel> mCalendarArrayList = new ArrayList<>();
    private CalendatDataAdapter mAdapter;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_diary__calander);
        init();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivity);

            db = new DatabaseHandler(this);
            mProgressBar = (ProgressBar) findViewById(R.id.Progressbar1);
            empty_daily_dairy =  findViewById(R.id.empty_daily_dairy);
            try {
                if (getIntent() != null && getIntent().getExtras() != null) {
                    msgtype = getIntent().getStringExtra("msgtype");
                    IsNotification = getIntent().getStringExtra("IsNotification");
                    isFrom=getIntent().getStringExtra("isFrom");
                }
                int Cnt_Count = 0;
                Cnt_Count = Datastorage.GetMultipleAccount(DailyDiaryCalanderActivity.this);
                String title = "DailyDiary";
                String ActivityName=ActivityNames.DailyDiaryCalanderActivity;

                if (Cnt_Count == 1) {
                    if (msgtype.equals("1")) {
                        title = "Homework-" + Datastorage.GetStudentName(DailyDiaryCalanderActivity.this);
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivity);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivity;
                    } else if (msgtype.equals("2")) {
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivity_Homework_Not_Done);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivity_Homework_Not_Done;
                        title = "Homework Not Done-" + Datastorage.GetStudentName(DailyDiaryCalanderActivity.this);
                    } else if (msgtype.equals("3")) {
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivity_Absent);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivity_Absent;
                        title = "Absent-" + Datastorage.GetStudentName(DailyDiaryCalanderActivity.this);
                    } else if (msgtype.equals("4")) {
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivityLate_Come);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivityLate_Come;
                        title = "Late Come-" + Datastorage.GetStudentName(DailyDiaryCalanderActivity.this);
                    } else if (msgtype.equals("5")) {
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivity_Uniform_Infraction);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivity_Uniform_Infraction;
                        title = "Uniform Infraction-" + Datastorage.GetStudentName(DailyDiaryCalanderActivity.this);
                    } else if (msgtype.equals("6")) {
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivity_Calender);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivity_Calender;
                        title = "Calender-" + Datastorage.GetStudentName(DailyDiaryCalanderActivity.this);
                    } else if (msgtype.equals("7")) {
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivity_Food_Infraction);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivity_Food_Infraction;
                        title = "Food Infraction-" + Datastorage.GetStudentName(DailyDiaryCalanderActivity.this);
                    } else if (msgtype.equals("8")) {
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivity_Hygiene);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivity_Hygiene;
                        title = "Hygiene-" + Datastorage.GetStudentName(DailyDiaryCalanderActivity.this);
                    } else if (msgtype.equals("9")) {
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivity_Remarks);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivity_Remarks;
                        title = "Remarks-" + Datastorage.GetStudentName(DailyDiaryCalanderActivity.this);
                    }
                } else {
                    if (msgtype.equals("1")) {
                        title = "Homework";
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivity);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivity;
                    } else if (msgtype.equals("2")) {
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivity_Homework_Not_Done);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivity_Homework_Not_Done;
                        title = "Homework Not Done";
                    } else if (msgtype.equals("3")) {
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivity_Absent);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivity_Absent;
                        title = "Absent";
                    } else if (msgtype.equals("4")) {
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivityLate_Come);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivityLate_Come;
                        title = "Late Come";
                    } else if (msgtype.equals("5")) {
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivity_Uniform_Infraction);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivity_Uniform_Infraction;
                        title = "Uniform Infraction";
                    } else if (msgtype.equals("6")) {
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivity_Calender);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivity_Calender;
                        title = "Calendar";
                    } else if (msgtype.equals("7")) {
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivity_Food_Infraction);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivity_Food_Infraction;
                        title = "Food Infraction";
                    } else if (msgtype.equals("8")) {
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivity_Hygiene);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivity_Hygiene;
                        title = "Hygiene";
                    } else if (msgtype.equals("9")) {
                        showBannerAd(mAdView, ActivityNames.DailyDiaryCalanderActivity_Remarks);
                        ActivityName=ActivityNames.DailyDiaryCalanderActivity_Remarks;
                        title = "Remarks";
                    }
                }

                Constants.setActionbar(getSupportActionBar(), DailyDiaryCalanderActivity.this, DailyDiaryCalanderActivity.this,
                        title, "DailyDiary",ActivityName);
                StudId = Datastorage.GetStudentId(DailyDiaryCalanderActivity.this);
                SchoolId = Datastorage.GetSchoolId(DailyDiaryCalanderActivity.this);
                mEventRecylerview = (RecyclerView) findViewById(R.id.eventRecyclerview);
                GridLayoutManager mGridLayoutManager1 = new GridLayoutManager(DailyDiaryCalanderActivity.this, 1);
                mEventRecylerview.setLayoutManager(mGridLayoutManager1);
                mAdapter = new CalendatDataAdapter(DailyDiaryCalanderActivity.this, mCalendarArrayList);
                mEventRecylerview.setAdapter(mAdapter);
                Year_Id = Datastorage.GetCurrentYearId(DailyDiaryCalanderActivity.this);
                calendarView = (ExpandableHeightGridView) this.findViewById(R.id.calendar);
                _calendar = Calendar.getInstance(Locale.getDefault());
                month = _calendar.get(Calendar.MONTH) + 1;
                currentmonth = month;
                year = _calendar.get(Calendar.YEAR);
                selectedDayMonthYearButton = (TextView) this.findViewById(R.id.selectedDayMonthYear);
                Latest_SMS_ID = db.GetLatestSMSID(Integer.parseInt(StudId), Integer.parseInt(SchoolId)
                        , Integer.parseInt(Year_Id));
                gridview_month = (ExpandableHeightGridView) findViewById(R.id.gv_month);
                gridview_month.setExpanded(true);
                calendarView.setExpanded(true);
                gridview_month.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        try {
                            if (msgtype.equals("6")) {
                                eventCurrentMonth = eventMonthId.get(position);
                                eventCurrentYear = eventYearid.get(position);
                                _calendar.set(eventCurrentYear, eventCurrentMonth, 1);
                                new MyCalenderTask().execute();
                            } else {
                                month = monthid[position];
                                year = yearid[position];
                                currentmonth = month;
                                String temp = monthslist[position];
                                String currentdisplayyear = "20" + temp.substring(5, 7);
                                year = Integer.parseInt(currentdisplayyear);
                                _calendar.set(Integer.parseInt(currentdisplayyear), month, 1);
                                GetMessageDetail(StudId, SchoolId, 0, Latest_SMS_ID, true);
                            }
                        } catch (Exception e) {
                        }
                    }
                });

                if (!msgtype.equals("6")) {
                    try {
                        GetMessagesFromLocalDB();
                        month = monthid[0];
                        year = yearid[0];
                    } catch (Exception ex) {
                        Common.printStackTrace(ex);
                    }

                    try {
                        cur_time.setToNow();
                        int lastautoupdatedate = Datastorage.GetLastAutoUpdateMessageDay(DailyDiaryCalanderActivity.this);
                        Constants.Logwrite("DailyDiary", "LastUpdatedDay:" + lastautoupdatedate);
                        if (cur_time.monthDay != lastautoupdatedate) {
                            MyTaskRefresh mytaskRef = new MyTaskRefresh();
                            if (mytaskRef.getStatus() != AsyncTask.Status.RUNNING
                                    || mytaskRef.getStatus() != AsyncTask.Status.PENDING) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    mytaskRef.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    mytaskRef.execute();
                                }
                            } else {
                                Constants.Logwrite("DailyDiary", "Async taks is running.");
                            }
                        }
                    } catch (Exception ex) {
                        Constants.writelog("DailyDiary", "Msg:1216,ismsgref" + ex.getMessage());
                    }
                    GetMessageDetail(StudId, SchoolId, 0, Latest_SMS_ID, true);
                } else {
                    new MyCalenderTask().execute();
                }
            } catch (Exception ex) {
                Constants.writelog("DailyDiaryCalander", "MSG 235:" + ex.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        Constants.Logwrite(tag, "Destroying View ");
        super.onDestroy();
    }

    // Inner Class
    public class GridCellAdapter extends BaseAdapter implements OnClickListener {
        private static final String tag = "GridCellAdapter";
        private final Context _context;
        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private TextView num_events_per_day;
        private final HashMap<String, Integer> eventsPerMonthMap;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId, int month, int year) {
            super();
            this._context = context;
            this.list = new ArrayList<>();
            Constants.Logwrite(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
            printMonth(month, year);
            // Find Number of Events
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        public int getCount() {
            return list.size();
        }

        private void printMonth(int mm, int yy) {
            Constants.Logwrite(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;
            int currentMonth = month - 1;
            try {
                GregorianCalendar cal = null;
                if (msgtype.equals("6")) {
                    if (eventCurrentMonth != 0) {
                        month = eventCurrentMonth;
                        currentMonth = eventCurrentMonth - 1;
                    }
                    daysInMonth = getNumberOfDaysOfMonth(eventCurrentMonth);
                    cal = new GregorianCalendar(eventCurrentYear, eventCurrentMonth - 1, 1);
                } else {
                    //String currentMonthName = getMonthAsString(currentMonth);
                    daysInMonth = getNumberOfDaysOfMonth(currentMonth);
                    cal = new GregorianCalendar(yy, currentMonth, 1);
                }

                if (currentMonth == 11) {
                    prevMonth = currentMonth - 1;
                    prevYear = yy;
                    nextYear = yy + 1;
                } else if (currentMonth == 0) {
                    prevMonth = 11;
                    prevYear = yy - 1;
                    nextYear = yy;
                    // daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                    nextMonth = 1;
                    /*
                     * Constants.Logwrite(tag, "**-> PrevYear: " + prevYear + " PrevMonth:" +
                     * prevMonth + " NextMonth: " + nextMonth + " NextYear: " +
                     * nextYear);
                     */
                } else {
                    prevMonth = currentMonth - 1;
                    nextMonth = currentMonth + 1;
                    nextYear = yy;
                    prevYear = yy;
                    daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                    /*
                     * Constants.Logwrite(tag, "***> PrevYear: " + prevYear + " PrevMonth:"
                     * + prevMonth + " NextMonth: " + nextMonth + " NextYear: "
                     * + nextYear);
                     */
                }
                int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
                trailingSpaces = currentWeekDay;
                // int m = month - 1;
                // String yeartext =
                // String.valueOf(_calendar.get(Calendar.YEAR));
                selectedDayMonthYearButton.setText(months[currentMonth] + " - "
                        + year);

                if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                    if (mm == 2)
                        ++daysInMonth;
                    else if (mm == 3)
                        ++daysInPrevMonth;

                // Trailing Month days
                for (int i = 0; i < trailingSpaces; i++) {
                    list.add(" " + "-GREY" + "-" + getMonthAsString(prevMonth)
                            + "-" + prevYear);
                }
                Date d = new Date();
                d.getTime();
                //int curmonth = d.getMonth() + 1;
                //Current Month Days
                for (int i = 1; i <= daysInMonth; i++) {
                    if (msgtype.equals("6")) {
                        if (eventDate.contains(i + "-" + (currentMonth + 1) + "-" + yy))// Arrays.asList(yourArray).contains(yourValue)
                        {
                            list.add(String.valueOf(i) + "-GREEN" + "-"
                                    + (currentMonth + 1) + "-" + yy);
                        } else {
                            list.add(String.valueOf(i) + "-WHITE" + "-"
                                    + getMonthAsString(currentMonth) + "-" + yy);
                        }
                    } else {
                        if (Arrays.asList(msgday).contains(String.valueOf(i)))// Arrays.asList(yourArray).contains(yourValue)
                        {
                            if (msgtype.equalsIgnoreCase("1") || msgtype.equalsIgnoreCase("6")) {
                                list.add(String.valueOf(i) + "-GREEN" + "-"
                                        + getMonthAsString(currentMonth) + "-" + yy);
                            } else if (msgtype.equalsIgnoreCase("2")) {
                                list.add(String.valueOf(i) + "-RED" + "-"
                                        + getMonthAsString(currentMonth) + "-" + yy);
                            } else if (msgtype.equals("3") || msgtype.equals("7")) {
                                list.add(String.valueOf(i) + "-BLUE" + "-"
                                        + getMonthAsString(currentMonth) + "-" + yy);
                            } else if (msgtype.equals("4") || msgtype.equals("9")) {
                                list.add(String.valueOf(i) + "-ORANGE" + "-"
                                        + getMonthAsString(currentMonth) + "-" + yy);
                            } else if (msgtype.equals("5") || msgtype.equals("8")) {
                                list.add(String.valueOf(i) + "-MAROON" + "-"
                                        + getMonthAsString(currentMonth) + "-" + yy);
                            } else {
                                list.add(String.valueOf(i) + "-RED" + "-"
                                        + getMonthAsString(currentMonth) + "-" + yy);
                            }
                        } else {
                            list.add(String.valueOf(i) + "-WHITE" + "-"
                                    + getMonthAsString(currentMonth) + "-" + yy);
                        }
                    }
                }
                // Leading Month days
                // static
                // String.valueOf(i + 1)
                /*
                 * for (int i = 0; i < list.size() % 7; i++) { Constants.Logwrite(tag,
                 * "NEXT MONTH:= " + getMonthAsString(nextMonth)); list.add(" "
                 * + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" +
                 * nextYear); }
                 */
            } catch (Exception ex) {
                Constants.writelog("DailyDiaryCalander",
                        "MSG 517:" + ex.getMessage());
                Constants.Logwrite("DailyDiaryCalander", "MSG 517:" + ex.getMessage());
            }
        }

        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            return map;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.screen_gridcell, parent, false);
            }
            // Get a reference to the Day gridcell
            TextView gridcell = (TextView) row.findViewById(R.id.calendar_day_gridcell);
            View rlCellBg = (View) row.findViewById(R.id.rlCellBg);
            gridcell.setOnClickListener(this);
            // ACCOUNT FOR SPACING
            Constants.Logwrite(tag, "Current Day: " + getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
                if (eventsPerMonthMap.containsKey(theday)) {
                    num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
                    Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                    num_events_per_day.setText(numEvents.toString());
                }
            }
            // Set the Day GridCell
            gridcell.setText(theday);

            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            Constants.Logwrite(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);
            if (day_color[1].equalsIgnoreCase("GREEN")) {
                Drawable dr = new ColorDrawable();
                rlCellBg.setBackgroundResource(R.drawable.cell_shape_hw);
            } else if (day_color[1].equalsIgnoreCase("RED")) {
                rlCellBg.setBackgroundResource(R.drawable.cell_shape_hwnotdone);
            } else if (day_color[1].equalsIgnoreCase("GREY")) {
            } else if (day_color[1].equalsIgnoreCase("ORANGE")) {
                rlCellBg.setBackgroundResource(R.drawable.cell_shape_latecome);
            } else if (day_color[1].equalsIgnoreCase("WHITE")) {
            } else if (day_color[1].equalsIgnoreCase("BLUE")) {
                rlCellBg.setBackgroundResource(R.drawable.cell_shape_absent);
            } else if (day_color[1].equalsIgnoreCase("MAROON")) {
                rlCellBg.setBackgroundResource(R.drawable.cell_shape_uniformnotproper);
            }
            return row;
        }

        public void onClick(View view) {
            String date_month_year = (String) view.getTag();
            String[] date_spl = date_month_year.split("-");
            Constants.Logwrite("Selected date", date_month_year);
            try {
                if (msgtype.equals("6")) {
                    mCalendarArrayList.clear();
                    mAdapter.notifyDataSetChanged();
                    int position = eventDate.indexOf(date_month_year);
                    try {
                        for (int i = position; i < eventDate.size(); i++) {
                            if (date_month_year.equalsIgnoreCase(eventDate.get(i))) {
                                String[] events = eventDetails.get(position).split("##part##");
                                CalendarListModel mdata = new CalendarListModel();
                                if (events[1] != null && !events[1].isEmpty()) {
                                    mdata.setTitle(events[1]);
                                } else {
                                    mdata.setTitle("");
                                }
                                mdata.setDate(date_month_year);
                                if (events[2] != null && !events[2].isEmpty()) {
                                    mdata.setDescription(events[2]);
                                } else {
                                    mdata.setDescription("");
                                }
                                try {
                                    if (events[3] != null && !events[3].isEmpty()) {
                                        mdata.setImageUrl(events[3]);
                                    } else {
                                        mdata.setImageUrl("");
                                    }
                                } catch (Exception ex) {
                                    Common.printStackTrace(ex);
                                }
                                mCalendarArrayList.add(mdata);
                            } else {
                                break;
                            }
                        }

                        mAdapter = new CalendatDataAdapter(DailyDiaryCalanderActivity.this, mCalendarArrayList);
                        mEventRecylerview.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        ScrollAnimation((int) ((View) findViewById(R.id.mainNestedScrollView)).getX(),
                                (int) ((View) findViewById(R.id.mainNestedScrollView)).getBottom());
                    } catch (Exception ex) {
                        Common.printStackTrace(ex);
                    }
                } else {
                    Date parsedDate = dateFormatter.parse(date_month_year);
                    Constants.Logwrite(tag, "Parsed Date: " + parsedDate.toString());
                    if (Arrays.asList(msgday).contains(date_spl[0])) {
                        int a = Arrays.asList(msgday).indexOf(
                                date_spl[0]);
                        if (msgtype.equalsIgnoreCase(Constants.HW_HOMEWORK)) {
                            String[] ms = msgtxt[a].split("##HWOTHERDET@@");
                            if (!ms[1].trim().equalsIgnoreCase("")) {
                                int Mod_Id = Integer.parseInt(ms[2]);
                                Intent intent = null;
                                if (Mod_Id == 9) {
                                    intent = new Intent(DailyDiaryCalanderActivity.this,
                                            SubjectWiseHWDetailsActivity.class);
                                } else if (Mod_Id == 2) {
                                    intent = new Intent(DailyDiaryCalanderActivity.this,
                                            SingleMessageActivity.class);
                                    intent.putExtra("previousPage", "DailyDiary");
                                } else {
                                    intent = new Intent(DailyDiaryCalanderActivity.this,
                                            SingleMessageActivity.class);
                                    intent.putExtra("previousPage", "DailyDiary");
                                }
                                intent.putExtra("MSG", ms[1]);
                                intent.putExtra("date", date_month_year);
                                intent.putExtra("OTH", date_month_year);
                                intent.putExtra("ModuleId", Mod_Id + "");
                                startActivity(intent);
                                onClickAnimation();
                            } else {
                                Common.setToastMessage(DailyDiaryCalanderActivity.this, findViewById(R.id.maindailydairy), "No data available.");
                            }

                        } else if (msgtype.equalsIgnoreCase(Constants.HW_HOMEWORKNOTDONE)) {
                            String[] ms = msgtxt[a].split("##HWOTHERDET@@");
                            if (!ms[1].trim().equalsIgnoreCase("")) {
                                int Mod_Id = Integer.parseInt(ms[2]);
                                Intent intent = null;

                                if (Mod_Id == 10) {
                                    // subject wise hw not done msg
                                    intent = new Intent(DailyDiaryCalanderActivity.this,
                                            SubjectWiseHWNTDNNotificationActivity.class);
                                    intent.putExtra("Is_Notification", "0");
                                } else if (Mod_Id == 3) {
                                    intent = new Intent(DailyDiaryCalanderActivity.this,
                                            SingleMessageActivity.class);
                                    intent.putExtra("previousPage", "DailyDiary");
                                }
                                intent.putExtra("MSG", ms[1]);
                                intent.putExtra("date", date_month_year);
                                intent.putExtra("OTH", date_month_year);
                                startActivity(intent);
                                onClickAnimation();

                            } else {
                                Common.setToastMessage(DailyDiaryCalanderActivity.this, findViewById(R.id.maindailydairy), "No data available.");
                            }
                        } else if (msgtype.equals(Constants.HW_LATECOME)) {

                            /*
                             * Intent intent = new Intent(DailyDiaryCalanderActivity.this,
                             * SingleMessageActivity.class); String[] ms =
                             * msgtxt[a].split("##HWOTHERDET@@");
                             * intent.putExtra("MSG", ms[1]);
                             * intent.putExtra("date", date_month_year);
                             * intent.putExtra("OTH", date_month_year);
                             * startActivity(intent);
                             */
                        } else if (msgtype.equals(Constants.HW_ABSENT)) {
                        /*
                         * Intent intent = new Intent(DailyDiaryCalanderActivity.this,
						 * SingleMessageActivity.class); String[] ms =
						 * msgtxt[a].split("##HWOTHERDET@@");
						 * intent.putExtra("MSG", ms[1]);
						 * intent.putExtra("date", date_month_year);
						 * intent.putExtra("OTH", date_month_year);
						 * startActivity(intent);
						 */
                        } else if (msgtype.equals(Constants.HW_UNIFORMNOTPROPER)) {
                            // new after topic wise uniform
                            String[] ms = msgtxt[a].split("##HWOTHERDET@@");
                            if (!ms[1].trim().equalsIgnoreCase("")) {
                                int Mod_Id = Integer.parseInt(ms[2]);
                                Intent intent = null;

                                if (Mod_Id == 12) {
                                    intent = new Intent(DailyDiaryCalanderActivity.this,
                                            SubjectWiseHWDetailsActivity.class);
                                } else {
                                    intent = new Intent(DailyDiaryCalanderActivity.this,
                                            SingleMessageActivity.class);
                                    intent.putExtra("previousPage", "DailyDiary");
                                }
                                intent.putExtra("MSG", ms[1]);
                                intent.putExtra("date", date_month_year);
                                intent.putExtra("OTH", date_month_year);
                                intent.putExtra("ModuleId", Mod_Id + "");
                                startActivity(intent);
                                onClickAnimation();
                            } else {
                                Common.setToastMessage(DailyDiaryCalanderActivity.this, findViewById(R.id.maindailydairy), "No data available.");
                            }
                        } else if (msgtype.equalsIgnoreCase(Constants.FOOD)) {
                            String[] ms = msgtxt[a].split("##HWOTHERDET@@");
                            if (!ms[1].trim().equalsIgnoreCase("")) {
                                int Mod_Id = Integer.parseInt(ms[2]);
                                Intent intent = new Intent(DailyDiaryCalanderActivity.this,
                                        SubjectWiseHWDetailsActivity.class);
                                intent.putExtra("MSG", ms[1]);
                                intent.putExtra("date", date_month_year);
                                intent.putExtra("OTH", date_month_year);
                                intent.putExtra("ModuleId", Mod_Id + "");
                                startActivity(intent);
                                onClickAnimation();
                            } else {
                                Common.setToastMessage(DailyDiaryCalanderActivity.this, findViewById(R.id.maindailydairy), "No data available.");
                            }

                        } else if (msgtype.equalsIgnoreCase(Constants.HYGIENE)) {
                            String[] ms = msgtxt[a].split("##HWOTHERDET@@");
                            if (!ms[1].trim().equalsIgnoreCase("")) {
                                int Mod_Id = Integer.parseInt(ms[2]);
                                Intent intent = new Intent(DailyDiaryCalanderActivity.this,
                                        SubjectWiseHWDetailsActivity.class);
                                intent.putExtra("MSG", ms[1]);
                                intent.putExtra("date", date_month_year);
                                intent.putExtra("OTH", date_month_year);
                                intent.putExtra("ModuleId", Mod_Id + "");
                                startActivity(intent);
                                onClickAnimation();
                            } else {
                                Common.setToastMessage(DailyDiaryCalanderActivity.this, findViewById(R.id.maindailydairy), "No data available.");
                            }
                        } else if (msgtype.equalsIgnoreCase(Constants.REMARKS)) {
                            String[] ms = msgtxt[a].split("##HWOTHERDET@@");
                            if (!ms[1].trim().equalsIgnoreCase("")) {
                                int Mod_Id = Integer.parseInt(ms[2]);
                                Intent intent = new Intent(DailyDiaryCalanderActivity.this,
                                        SubjectWiseHWDetailsActivity.class);

                                intent.putExtra("MSG", ms[1]);
                                intent.putExtra("date", date_month_year);
                                intent.putExtra("OTH", date_month_year);
                                intent.putExtra("ModuleId", Mod_Id + "");
                                startActivity(intent);
                                onClickAnimation();
                            } else {
                                Common.setToastMessage(DailyDiaryCalanderActivity.this, findViewById(R.id.maindailydairy), "No data available.");
                            }
                        } else {
                            Intent intent = new Intent(DailyDiaryCalanderActivity.this,
                                    SingleMessageActivity.class);
                            intent.putExtra("previousPage", "DailyDiary");
                            intent.putExtra("MSG", msgtxt[a]);
                            intent.putExtra("date", date_month_year);
                            startActivity(intent);
                            onClickAnimation();
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Constants.writelog("DailyDiaryCalander", "Exception:806"
                        + e.getMessage());
                Constants.Logwrite("DailyDiaryCalander", "Exception:806" + e.getMessage());
                // e.printStackTrace();
            }
        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }
    }

    // For month list with message count
    private void GetMessagesFromLocalDB() {
        try {
            int cntr = 0;
            List<Contact> contacts = db
                    .GetLessionDiaryMessageMonthListSubjectwise(
                            Integer.parseInt(StudId),
                            Integer.parseInt(SchoolId),
                            Integer.parseInt(Year_Id),
                            Integer.parseInt(msgtype));
            if (contacts == null || contacts.size() == 0) {
                //GetMessageDetail(StudId, SchoolId, 1, Latest_SMS_ID, false);
                contacts = db.GetLessionDiaryMessageMonthListSubjectwise(
                        Integer.parseInt(StudId), Integer.parseInt(SchoolId),
                        Integer.parseInt(Year_Id), Integer.parseInt(msgtype));
            }
            monthslist = new String[contacts.size()];
            monthid = new int[contacts.size()];
            yearid = new int[contacts.size()];
            if (monthslist != null && monthslist.length > 0) {
                for (Contact cn : contacts) {
                    String msg = cn.getSMSText();
                    String[] info = msg.split(",");
                    int smscount = Integer.parseInt(info[3]);
                    String monthnamewithcount = "";
                    if (smscount < 9) {
                        monthnamewithcount = " " + info[0] + " "
                                + info[2].substring(2) + "  (" + smscount
                                + ")  ";
                    } else {
                        monthnamewithcount = " " + info[0] + " "
                                + info[2].substring(2) + "  (" + smscount
                                + ") ";
                    }
                    // new [mar 15]
                    /*
                     * String monthnamewithcount = info[0] + " " +
					 * info[2].substring(2);
					 */
                    String month_and_id = info[1] + ","
                            + info[2] + "," + info[3];
                    monthslist[cntr] = monthnamewithcount;
                    monthid[cntr] = Integer.parseInt(info[1]);
                    yearid[cntr] = Integer.parseInt(info[2]);
                    mapmonth.put(monthnamewithcount, month_and_id);
                    cntr++;
                }
            }
        } catch (Exception e) {
            Constants.writelog("DailyDiary_calander",
                    "Error: GetMessagesFromLocalDB():" + e.getMessage()
                            + "Stacktrace:" + e.getStackTrace());
        }
    }

    public void GetMessageDetail(final String studId, final String schoolId, int ismsgref, int latest_SMS_ID, boolean Isprogress) {
        try {

            final DatabaseHandler db = new DatabaseHandler(DailyDiaryCalanderActivity.this);
            String MsgId = "";
            int record_cnt = db.getSMSCount(Integer.parseInt(StudId), Integer.parseInt(SchoolId));
            boolean isServiceCall = true;
            if (record_cnt > 0 && ismsgref == 0) {
                int cntr = 0;
                List<Contact> contacts = db.GetAllHomeWorkandSubjectHomeWorkDetails(Integer.parseInt(StudId),
                        Integer.parseInt(SchoolId), msgtype, Integer.parseInt(Year_Id));
                messages = new String[contacts.size()];
                msgmont = new String[contacts.size()];
                msgday = new String[contacts.size()];
                msgtxt = new String[contacts.size()];
                int k = 0;
                for (Contact cn : contacts) {
                    try {
                        String msg = cn.getGlobalText();
                        messages[cntr] = msg;
                        String[] msgsplited = msg.split("##@@");
                        String[] date = msgsplited[0].split("/");
                        int mont = Integer.parseInt(date[1]);
                        msgmont[cntr] = String.valueOf(mont);
                        if (msgmont[cntr].equalsIgnoreCase(
                                String.valueOf(month))) {
                            int day = Integer.parseInt(date[0]);
                            msgday[k] = String.valueOf(day);
                            msgtxt[k] = msgsplited[2];
                            k++;
                        }
                    } catch (Exception ex) {
                        Constants.writelog("DailyDiary 1040", "MSG:"
                                + ex.getMessage());
                    }
                    cntr++;
                }

                GetMessagesFromLocalDB();
                int cntr1 = 0;
                List<Contact> contacts1 = db.GetAllHomeWorkandSubjectHomeWorkDetails(
                        Integer.parseInt(StudId), Integer.parseInt(SchoolId), msgtype,
                        Integer.parseInt(Year_Id));
                messages = new String[contacts1.size()];
                msgmont = new String[contacts1.size()];
                msgday = new String[contacts1.size()];
                msgtxt = new String[contacts1.size()];
                int k1 = 0;
                for (Contact cn : contacts1) {
                    try {
                        isServiceCall = false;
                        String msg = cn.getGlobalText();
                        messages[cntr1] = msg;
                        String[] msgsplited = msg.split("##@@");
                        String[] date = msgsplited[0].split("/");
                        int mont = Integer.parseInt(date[1]);
                        msgmont[cntr1] = String.valueOf(mont);
                        if (msgmont[cntr1].equalsIgnoreCase(
                                String.valueOf(month))) {
                            int day = Integer.parseInt(date[0]);
                            msgday[k1] = String.valueOf(day);
                            msgtxt[k1] = msgsplited[2];
                            k1++;
                        }
                    } catch (Exception ex) {
                        Constants.writelog(
                                "DailyDiary 1074",
                                "MSG:" + ex.getMessage() + "::::"
                                        + ex.getStackTrace());
                    }
                    cntr1++;
                }
                empty_daily_dairy.setVisibility(View.GONE);
                ((View) findViewById(R.id.mainNestedScrollView)).setVisibility(View.VISIBLE);
                if (messages != null && messages.length > 0) {
                    if (monthslist != null && monthslist.length > 0) {
                        currentmonth = monthid[0];
                        Constants.Logwrite("DailyDiary", "currentmonth:" + currentmonth);
                    }
                    DailyDiaryMonthListAdapter adp_mnth = new DailyDiaryMonthListAdapter(
                            DailyDiaryCalanderActivity.this, monthslist);
                    gridview_month.setAdapter(adp_mnth);
                    adp_mnth.notifyDataSetChanged();
                    adapter = new GridCellAdapter(DailyDiaryCalanderActivity.this,
                            R.id.calendar_day_gridcell, currentmonth, year);
                    Constants.Logwrite("DailyDiary", "Value to Adapter currentmonth:" + currentmonth + "::::Year:" + year);
                    adapter.notifyDataSetChanged();
                    calendarView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    empty_daily_dairy.setVisibility(View.VISIBLE);
                    ((View) findViewById(R.id.mainNestedScrollView)).setVisibility(View.INVISIBLE);
                   /* AlertDialog alertDialog = new AlertDialog.Builder(DailyDiaryCalanderActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                    if (msgtype.equals("1")) {
                        alertDialog.setMessage("No HomeWork Message Available...");
                    } else if (msgtype.equals("2")) {
                        alertDialog.setMessage("No HomeWork Not done Message Available...");
                    } else if (msgtype.equals("3")) {
                        alertDialog.setMessage("No Absent Message Available...");
                    } else if (msgtype.equals("4")) {
                        alertDialog.setMessage("No Late Come Message Available...");
                    } else if (msgtype.equals("5")) {
                        alertDialog.setMessage("No Uniform Infraction Message Available...");
                    }
                    ((View) findViewById(R.id.mainNestedScrollView)).setVisibility(View.INVISIBLE);
                    alertDialog.setIcon(R.drawable.alert);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alertDialog.show();*/
                }
            }
            if (isServiceCall) {
                ConnectionDetector cd = new ConnectionDetector(DailyDiaryCalanderActivity.this);
                if (cd.isConnectingToInternet()) {
                    empty_daily_dairy.setVisibility(View.GONE);
                    try {
                        if (ismsgref == 1) {
                            MsgId = "0";
                        } else {
                            MsgId = String.valueOf(latest_SMS_ID);
                        }
                        String yearid = Datastorage.GetCurrentYearId(DailyDiaryCalanderActivity.this);
                        if (Isprogress) {
                            mProgressBar.setVisibility(View.VISIBLE);
                        }
                        Call<DailyDiaryCalendarModel> mCall = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
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
                                                // "SmsUpdate()949 NO"+i+" Messageid:"+msgitem[4]);
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

                                    GetMessagesFromLocalDB();
                                    int cntr = 0;
                                    List<Contact> contacts = db.GetAllHomeWorkandSubjectHomeWorkDetails(
                                            Integer.parseInt(StudId), Integer.parseInt(SchoolId), msgtype,
                                            Integer.parseInt(Year_Id));
                                    messages = new String[contacts.size()];
                                    msgmont = new String[contacts.size()];
                                    msgday = new String[contacts.size()];
                                    msgtxt = new String[contacts.size()];
                                    int k = 0;
                                    for (Contact cn : contacts) {
                                        try {
                                            String msg = cn.getGlobalText();
                                            messages[cntr] = msg;
                                            String[] msgsplited = msg.split("##@@");
                                            String[] date = msgsplited[0].split("/");
                                            int mont = Integer.parseInt(date[1]);
                                            msgmont[cntr] = String.valueOf(mont);
                                            if (msgmont[cntr].equalsIgnoreCase(
                                                    String.valueOf(month))) {
                                                int day = Integer.parseInt(date[0]);
                                                msgday[k] = String.valueOf(day);
                                                msgtxt[k] = msgsplited[2];
                                                k++;
                                            }
                                        } catch (Exception ex) {
                                            Constants.writelog(
                                                    "DailyDiary 1074",
                                                    "MSG:" + ex.getMessage() + "::::"
                                                            + ex.getStackTrace());
                                        }
                                        cntr++;
                                    }
                                    empty_daily_dairy.setVisibility(View.GONE);
                                    ((View) findViewById(R.id.mainNestedScrollView)).setVisibility(View.VISIBLE);
                                    if (messages != null && messages.length > 0) {
                                        if (monthslist != null && monthslist.length > 0) {
                                            currentmonth = monthid[0];
                                            Constants.Logwrite("DailyDiary", "currentmonth:" + currentmonth);
                                        }
                                        DailyDiaryMonthListAdapter adp_mnth = new DailyDiaryMonthListAdapter(
                                                DailyDiaryCalanderActivity.this, monthslist);
                                        gridview_month.setAdapter(adp_mnth);
                                        adp_mnth.notifyDataSetChanged();
                                        adapter = new GridCellAdapter(DailyDiaryCalanderActivity.this,
                                                R.id.calendar_day_gridcell, currentmonth, year);
                                        Constants.Logwrite("DailyDiary", "Value to Adapter currentmonth:" + currentmonth + "::::Year:" + year);
                                        adapter.notifyDataSetChanged();
                                        calendarView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        empty_daily_dairy.setVisibility(View.VISIBLE);
                                        ((View) findViewById(R.id.mainNestedScrollView)).setVisibility(View.INVISIBLE);
                                       /* AlertDialog alertDialog = new AlertDialog.Builder(DailyDiaryCalanderActivity.this).create();
                                        alertDialog.setTitle("Alert");
                                        alertDialog.setCancelable(false);
                                        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                                        if (msgtype.equals("1")) {
                                            alertDialog.setMessage("No HomeWork Message Available...");
                                        } else if (msgtype.equals("2")) {
                                            alertDialog.setMessage("No HomeWork Not done Message Available...");
                                        } else if (msgtype.equals("3")) {
                                            alertDialog.setMessage("No Absent Message Available...");
                                        } else if (msgtype.equals("4")) {
                                            alertDialog.setMessage("No Late Come Message Available...");
                                        } else if (msgtype.equals("5")) {
                                            alertDialog.setMessage("No Uniform Infraction Message Available...");
                                        }
                                        ((View) findViewById(R.id.mainNestedScrollView)).setVisibility(View.INVISIBLE);
                                        alertDialog.setIcon(R.drawable.alert);
                                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                        alertDialog.show();*/
                                    }
                                } catch (Exception ex) {
                                    Constants.writelog("GetMessageDetail:1025:ErrorMsg::", ex.getMessage());
                                } finally {
                                    if (mProgressBar != null && mProgressBar.isShown())
                                        mProgressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onFailure(Call<DailyDiaryCalendarModel> call, Throwable t) {
                                if (mProgressBar != null && mProgressBar.isShown())
                                    mProgressBar.setVisibility(View.GONE);
                                Constants.writelog("GetMessageDetail:877:ErrorMsg::", "Onfailuer()");
                            }
                        });
                    } catch (Exception ex) {
                        Constants.writelog("GetMessageDetail:882:ErrorMsg::", ex.getMessage());
                    }
                } else {
                    empty_daily_dairy.setVisibility(View.VISIBLE);
                    Toast.makeText(DailyDiaryCalanderActivity.this, SchoolDetails.MsgNoInternet, Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception ex) {
            Constants.writelog("GetMessageDetail:886:ErrorMsg::", ex.getMessage());
        }
    }

    boolean value = false;

    private class MyCalenderTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (eventMonthId == null || eventMonthId.size() == 0) {
                int count = 0;
                SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_CALENDAR_DETAIL);
                request.addProperty("schoolId", SchoolId + "");
                request.addProperty("class_section_Id", Datastorage.GetClassSecId(DailyDiaryCalanderActivity.this) + "");
                request.addProperty("year_id", Datastorage.GetCurrentYearId(DailyDiaryCalanderActivity.this) + "");
                request.addProperty("platform", "0");
                try {
                    SoapObject result = Constants.CallWebMethod(
                            DailyDiaryCalanderActivity.this, request, Constants.GET_CALENDAR_DETAIL, true);
                    if (result != null && result.getPropertyCount() > 0) {
                        SoapObject obj2 = (SoapObject) result.getProperty(0);
                        Constants.Logwrite("Events:", "----------------");
                        if (obj2 != null) {
                            count = obj2.getPropertyCount();
                            String[] myarray = new String[count];
                            eventMonth = new ArrayList<String>();
                            eventDay = new ArrayList<String>();
                            eventDetails = new ArrayList<String>();
                            eventDate = new ArrayList<String>();
                            try {
                                for (int i = 0; i < count; i++) {
                                    myarray[i] = obj2.getProperty(i).toString();
                                    String[] events = myarray[i].split("##event##");
                                    for (int j = 0; j < events.length; j++) {
                                        String[] msgitem = events[j].split("##ev##");
                                        if (msgitem.length > 1) {
                                            value = true;
                                        }
                                        String[] dateParts = msgitem[0].split("/");
                                        int EVENT_DAY = Integer.parseInt(dateParts[0]
                                                .toString());
                                        int EVENT_MONTH = Integer.parseInt(dateParts[1]
                                                .toString());
                                        int EVENT_YEAR = Integer.parseInt(dateParts[2]);
                                        if (eventMonthId != null && eventMonthId.size() > 0) {
                                            if (eventMonth.contains(EVENT_MONTH + "")) {
                                                int position = eventMonth.indexOf(EVENT_MONTH + "");
                                                eventCount.set(position, eventCount.get(position) + 1);
                                            } else {
                                                eventMonthId.add(EVENT_MONTH);
                                                eventYearid.add(EVENT_YEAR);
                                                eventCount.add(1);
                                            }
                                        } else {
                                            eventMonthId.add(0, EVENT_MONTH);
                                            eventYearid.add(0, EVENT_YEAR);
                                            eventCount.add(0, 1);
                                        }
                                        eventDay.add(String.valueOf(EVENT_DAY));
                                        eventMonth.add(String.valueOf(EVENT_MONTH));
                                        eventDetails.add(msgitem[1]);
                                        eventDate.add(EVENT_DAY + "-" + EVENT_MONTH + "-" + EVENT_YEAR);
                                    }
                                }
                                Constants.writelog("DailyDiaryCalander",
                                        "MyCalenderTask AllSmsUpdated 1274.");
                            } catch (Exception ex) {
                                Constants.writelog("DailyDiaryCalanderActivity",
                                        "MyCalenderTask Exception 1277:" + ex.getMessage() + "::::"
                                                + ex.getStackTrace());
                            }
                        }
                    }
                } catch (Exception e) {
                    Constants.writelog("DailyDiaryCalander",
                            "MSG:970 " + e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                //Toast.makeText(getApplicationContext(), "Value:" + value, Toast.LENGTH_LONG).show();
                if (Constants.isShowInternetMsg) {
                    empty_daily_dairy.setVisibility(View.VISIBLE);
                    Constants.NotifyNoInternet(DailyDiaryCalanderActivity.this);
                } else {
                    empty_daily_dairy.setVisibility(View.GONE);
                    ((View) findViewById(R.id.mainNestedScrollView)).setVisibility(View.VISIBLE);
                    empty_daily_dairy.setVisibility(View.GONE);
                    if (eventDetails != null && eventDetails.size() > 0) {
                        if (eventMonth != null && eventMonth.size() > 0 && eventCurrentMonth == 0) {
                            currentmonth = Integer.parseInt(eventMonth.get(0));
                            Constants.Logwrite("DailyDiary", "currentmonth:" + currentmonth);
                        } else {
                            currentmonth = eventCurrentMonth;
                            year = eventCurrentYear;
                        }
                        if (eventCount != null && eventCount.size() > 0) {
                            String[] EventMonthsText = new String[eventCount.size()];
                            for (int i = 0; i < eventCount.size(); i++) {
                                EventMonthsText[i] = months[eventMonthId.get(i) - 1] + " (" + eventCount.get(i) + ")";
                            }
                            DailyDiaryMonthListAdapter adp_mnth = new DailyDiaryMonthListAdapter(
                                    DailyDiaryCalanderActivity.this, EventMonthsText);
                            gridview_month.setAdapter(adp_mnth);
                            adp_mnth.notifyDataSetChanged();
                        }
//                        LessonMessagesListAdapter adapterlmi = new LessonMessagesListAdapter(
//                                DailyDiaryCalanderActivity.this, messages);
                        adapter = new GridCellAdapter(DailyDiaryCalanderActivity.this,
                                R.id.calendar_day_gridcell, currentmonth, year);
                        Constants.Logwrite("DailyDiary", "Value to Adapter currentmonth:" + currentmonth + "::::Year:" + year);
                        adapter.notifyDataSetChanged();
                        calendarView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        if (mProgressBar != null && mProgressBar.isShown())
                            mProgressBar.setVisibility(View.GONE);
                        empty_daily_dairy.setVisibility(View.VISIBLE);
                        ((View) findViewById(R.id.mainNestedScrollView)).setVisibility(View.INVISIBLE);
                        /*AlertDialog alertDialog = new AlertDialog.Builder(DailyDiaryCalanderActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage(SchoolDetails.MsgNoDataAvailable);
                        if (msgtype.equals("1")) {
                            alertDialog.setMessage("No HomeWork Message Available...");
                        } else if (msgtype.equals("2")) {
                            alertDialog.setMessage("No HomeWork Not done Message Available...");
                        } else if (msgtype.equals("3")) {
                            alertDialog.setMessage("No Absent Message Available...");
                        } else if (msgtype.equals("4")) {
                            alertDialog.setMessage("No Late Come Message Available...");
                        } else if (msgtype.equals("5")) {
                            alertDialog.setMessage("No Uniform Infraction Message Available...");
                        }
                        ((View) findViewById(R.id.mainNestedScrollView)).setVisibility(View.INVISIBLE);
                        alertDialog.setIcon(R.drawable.alert);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        alertDialog.show();
                        return;*/
                    }
                }
                if (mProgressBar != null && mProgressBar.isShown())
                    mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                Constants.writelog("DailyDiaryCalander", "msg 1212:" + err.getMessage());
                if (mProgressBar != null && mProgressBar.isShown())
                    mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    private class MyTaskRefresh extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                GetMessageDetail(StudId, SchoolId, ismsgref, Latest_SMS_ID, false);
            } catch (Exception err) {
                Constants.Logwrite("DailyDiaryCalander", "Exception:1259" + err.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            cur_time.setToNow();
            Datastorage.SetLastAutoUpdateMessageDay(DailyDiaryCalanderActivity.this, cur_time.monthDay);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            menu.add(0, 1, 1, "Refresh").setTitle("Refresh");
            menu.add(0, 2, 2, "Add Account").setTitle("Add Account");
            //   menu.add(0, 3, 3, "Remove Account").setTitle("Remove Account");
            menu.add(0, 4, 4, "Set Default Account").setTitle("Set Default Account");
            mapacc = Constants.AddAccount(menu, db);
        } catch (Exception err) {
            Constants.Logwrite("DailyDiaryCalander", "MainPage 1262:" + err.getMessage());
            return true;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int iid = item.getItemId();
        Intent intent;
        if (iid == android.R.id.home) {
            hideKeyboard(DailyDiaryCalanderActivity.this);
            DailyDiaryCalanderActivity.this.finish();
            onBackClickAnimation();
        } else if (iid == 1 || iid == 2 || iid == 3 || iid == 4) {
            if (iid == 1) {
                GetMessageDetail(StudId, SchoolId, 1, 0, true);
                cur_time = new Time();
                cur_time.setToNow();
                Datastorage.SetLastAutoUpdateMessageDay(DailyDiaryCalanderActivity.this, cur_time.monthDay);
            } else if (iid == 2) {
                addAccountClick(DailyDiaryCalanderActivity.this);
            } else if (iid == 3) {
                removeAccountClick(DailyDiaryCalanderActivity.this);
            } else if (iid == 4) {
                accountListClick(DailyDiaryCalanderActivity.this);
            }
        } else {
            String details = mapacc.get(iid).toString();
            Constants.SetAccountDetails(details, DailyDiaryCalanderActivity.this);
            intent = new Intent(DailyDiaryCalanderActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
            onBackClickAnimation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
/*
        if(isFrom.equalsIgnoreCase("1"))
        {
            super.onBackPressed();
        }
        else
        {*/
            Intent intent=null;
            if (IsNotification != null && !IsNotification.isEmpty()) {
                intent = new Intent(DailyDiaryCalanderActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            } else {
                hideKeyboard(DailyDiaryCalanderActivity.this);
                DailyDiaryCalanderActivity.this.finish();
            }

            onBackClickAnimation();
            super.onBackPressed();
    }

    public void ScrollAnimation(int x, int y) {
        try {
            ObjectAnimator xTranslate = ObjectAnimator.ofInt(((View) findViewById(R.id.mainNestedScrollView)), "scrollX", x);
            ObjectAnimator yTranslate = ObjectAnimator.ofInt(((View) findViewById(R.id.mainNestedScrollView)), "scrollY", y);

            AnimatorSet animators = new AnimatorSet();
            animators.setDuration(1000L);
            animators.playTogether(xTranslate, yTranslate);
            animators.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator arg0) {
                }

                @Override
                public void onAnimationRepeat(Animator arg0) {
                }

                @Override
                public void onAnimationEnd(Animator arg0) {
                }

                @Override
                public void onAnimationCancel(Animator arg0) {
                }
            });
            animators.start();
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }
}