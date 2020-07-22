package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;


public class HomeMenuAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private String[] mMenuItems;

    public HomeMenuAdapter(Context context, String[] mMenuItems) {
        this.mMenuItems = mMenuItems;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mMenuItems.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView txtTitle;
        ImageView imgLogo;
        View ll_mainMenu;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.home_menu_item_raw_layout, null);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            holder.imgLogo = (ImageView) convertView.findViewById(R.id.imgLogo);
            holder.ll_mainMenu = (View) convertView.findViewById(R.id.ll_mainMenu);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitle.setText(Html.fromHtml(mMenuItems[position]));
        if (mMenuItems[position].equalsIgnoreCase("My Profile")) {
            holder.imgLogo.setImageResource(R.drawable.menu_profile_icon);
        } else if (mMenuItems[position].equalsIgnoreCase("Exam")) {
            holder.imgLogo.setImageResource(R.drawable.menu_all_exam_icon);
        } else if (mMenuItems[position].equalsIgnoreCase("Attendance")) {
            holder.imgLogo.setImageResource(R.drawable.menu_attendance_icon);
        } else if (mMenuItems[position].equalsIgnoreCase("School Mates")) {
            holder.imgLogo.setImageResource(R.drawable.menu_school_mate_icon);
        } else if (mMenuItems[position].equalsIgnoreCase("Messages")) {
            holder.imgLogo.setImageResource(R.drawable.menu_message_icon);
        } else if (mMenuItems[position].equalsIgnoreCase("Daily Diary")) {
            holder.imgLogo.setImageResource(R.drawable.menu_daily_diary_icon);
        } else if (mMenuItems[position].equalsIgnoreCase("Notice Board")) {
            holder.imgLogo.setImageResource(R.drawable.menu_noticeboard_icon);
        } else if (mMenuItems[position].equalsIgnoreCase("Fee Card")) {
            holder.imgLogo.setImageResource(R.drawable.menu_fee_card_iocn);
        } else if (mMenuItems[position].equalsIgnoreCase("Photo Gallery")) {
            holder.imgLogo.setImageResource(R.drawable.menu_photo_gallery_icon);
        } else if (mMenuItems[position].equalsIgnoreCase("Send Query")) {
            holder.imgLogo.setImageResource(R.drawable.menu_send_query_icon);
        } else if (mMenuItems[position].equalsIgnoreCase("Pay Fees Online")) {
            holder.imgLogo.setImageResource(R.drawable.menu_payfees_online_icon);
        } else if (mMenuItems[position].equalsIgnoreCase("Track Vehicle")) {
            holder.imgLogo.setImageResource(R.drawable.menu_vehicle_track_icon);
        } else if (mMenuItems[position].equalsIgnoreCase("Setting")) {
            holder.imgLogo.setImageResource(R.drawable.menu_settings_icon);
        } else if (mMenuItems[position].equalsIgnoreCase("Exit")) {
            holder.imgLogo.setImageResource(R.drawable.setting12);
        } else if (mMenuItems[position].equalsIgnoreCase("Home")) {
            holder.imgLogo.setImageResource(R.drawable.menu_home_icon);
        }else if (mMenuItems[position].equalsIgnoreCase("Calendar")) {
            holder.imgLogo.setImageResource(R.drawable.calendaricon);
        }else if (mMenuItems[position].equalsIgnoreCase("food chart")) {
            holder.imgLogo.setImageResource(R.drawable.food_table_icon);
        }else if (mMenuItems[position].equalsIgnoreCase("library")) {
            holder.imgLogo.setImageResource(R.drawable.library_icon);
        }else if (mMenuItems[position].equalsIgnoreCase("time table")) {
            holder.imgLogo.setImageResource(R.drawable.time_table_icon);
        }else if (mMenuItems[position].equalsIgnoreCase("curriculum")) {
            holder.imgLogo.setImageResource(R.drawable.curriculum_icon);
        }else if (mMenuItems[position].equalsIgnoreCase("Submission")){
            holder.imgLogo.setImageResource(R.drawable.ic_presentation);
        }
        return convertView;
    }

}
