package com.expedite.apps.kumkum.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;

import java.util.List;
import java.util.Map;

public class HomeMenuExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> mListCollection;
    private List<String> mList;

    public HomeMenuExpandableListAdapter(Activity context, List<String> mList, Map<String, List<String>> laptopCollections) {
        this.context = context;
        this.mListCollection = laptopCollections;
        this.mList = mList;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return mListCollection.get(mList.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String childTitle = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.child_item, null);
        }
        TextView txtChildTitle = (TextView) convertView.findViewById(R.id.txtChildTitle);
        ImageView imgChildLogo = (ImageView) convertView.findViewById(R.id.imgChildLogo);
        if (childTitle != null && !childTitle.isEmpty()) {
            txtChildTitle.setText(childTitle);
            if (childTitle.equalsIgnoreCase("HomeWork")) {
                imgChildLogo.setVisibility(View.VISIBLE);
                imgChildLogo.setImageResource(R.drawable.menu_home_work_icon);
            } else if (childTitle.equalsIgnoreCase("HomeWork Not Done")) {
                imgChildLogo.setVisibility(View.VISIBLE);
                imgChildLogo.setImageResource(R.drawable.menu_home_work_note_done_icon);
            } else if (childTitle.equalsIgnoreCase("Late Come")) {
                imgChildLogo.setVisibility(View.VISIBLE);
                imgChildLogo.setImageResource(R.drawable.menu_late_come_icon);
            } else if (childTitle.equalsIgnoreCase("Absent")) {
                imgChildLogo.setVisibility(View.VISIBLE);
                imgChildLogo.setImageResource(R.drawable.menu_absent_icon);
            } else if (childTitle.equalsIgnoreCase("Uniform Infraction")) {
                imgChildLogo.setVisibility(View.VISIBLE);
                imgChildLogo.setImageResource(R.drawable.menu_uniform_icon);
            } else if (childTitle.equalsIgnoreCase("Food Infraction")) {
                imgChildLogo.setVisibility(View.VISIBLE);
                imgChildLogo.setImageResource(R.drawable.foodinfectionicon);
            } else if (childTitle.equalsIgnoreCase("Hygiene")) {
                imgChildLogo.setVisibility(View.VISIBLE);
                imgChildLogo.setImageResource(R.drawable.hygineicon);
            } else if (childTitle.equalsIgnoreCase("Remarks")) {
                imgChildLogo.setVisibility(View.VISIBLE);
                imgChildLogo.setImageResource(R.drawable.remarkicon);
            } else if (childTitle.equalsIgnoreCase("All Exams")) {
                imgChildLogo.setVisibility(View.VISIBLE);
                imgChildLogo.setImageResource(R.drawable.all_exam_icon);
            } else if (childTitle.equalsIgnoreCase("Marksheet Exams")) {
                imgChildLogo.setVisibility(View.VISIBLE);
                imgChildLogo.setImageResource(R.drawable.exam_result_icon);
            } else if (childTitle.equalsIgnoreCase("Fee Card")) {
                imgChildLogo.setVisibility(View.VISIBLE);
                imgChildLogo.setImageResource(R.drawable.fee_card_iocn);
            } else if (childTitle.equalsIgnoreCase("Paid Fee")) {
                imgChildLogo.setVisibility(View.VISIBLE);
                imgChildLogo.setImageResource(R.drawable.paid_fees_icon);
            } else if (childTitle.equalsIgnoreCase("Pending Fee")) {
                imgChildLogo.setVisibility(View.VISIBLE);
                imgChildLogo.setImageResource(R.drawable.peding_fees_icon);
            }else if (childTitle.equalsIgnoreCase("Voice Message")) {
                imgChildLogo.setVisibility(View.VISIBLE);
                imgChildLogo.setImageResource(R.drawable.gray_voice_msg);
            }
            /*else if (childTitle.equalsIgnoreCase("Apply Leave")) {
                imgChildLogo.setVisibility(View.VISIBLE);
                imgChildLogo.setImageResource(R.drawable.office_leave_icon);
            } */else {
                imgChildLogo.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        int size = 0;
        if (mList.get(groupPosition).equalsIgnoreCase("Daily Diary")
                || mList.get(groupPosition).equalsIgnoreCase("Setting")
                || mList.get(groupPosition).equalsIgnoreCase("Exam")
                || mList.get(groupPosition).equalsIgnoreCase("Fee Card")) {
            try {
                size = mListCollection.get(mList.get(groupPosition)).size();
            } catch (Exception ex) {
                Common.printStackTrace(ex);
            }
        }
        return size;
    }

    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    public int getGroupCount() {
        int size = 0;
        try {
            size = mList.size();
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        return size;
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        try {
            String groupName = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.group_item, null);
            }

            TextView txtGroupTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            ImageView imgExpandCollapse = (ImageView) convertView.findViewById(R.id.imgExpand);
            ImageView imgLogo = (ImageView) convertView.findViewById(R.id.imgLogo);

            if (groupName != null && !groupName.isEmpty()) {
                txtGroupTitle.setText(groupName);

                if (groupName.equalsIgnoreCase("My Profile")) {
                    imgLogo.setImageResource(R.drawable.menu_profile_icon);
                } else if (groupName.equalsIgnoreCase("Exam")) {
                    imgLogo.setImageResource(R.drawable.menu_exam_icon);
                } else if (groupName.equalsIgnoreCase("Attendance")) {
                    imgLogo.setImageResource(R.drawable.menu_attendance_icon);
                } else if (groupName.equalsIgnoreCase("School Mates")) {
                    imgLogo.setImageResource(R.drawable.menu_school_mate_icon);
                } else if (groupName.equalsIgnoreCase("Messages")) {
                    imgLogo.setImageResource(R.drawable.menu_message_icon);
                } else if (groupName.equalsIgnoreCase("Daily Diary")) {
                    imgLogo.setImageResource(R.drawable.menu_daily_diary_icon);
                } else if (groupName.equalsIgnoreCase("Notice Board")) {
                    imgLogo.setImageResource(R.drawable.noticeboard_icon);
                } else if (groupName.equalsIgnoreCase("Fee Card")) {
                    imgLogo.setImageResource(R.drawable.menu_fees_icon1);
                } else if (groupName.equalsIgnoreCase("Photo Gallery")) {
                    imgLogo.setImageResource(R.drawable.menu_photo_gallery_icon);
                } else if (groupName.equalsIgnoreCase("Send Query")) {
                    imgLogo.setImageResource(R.drawable.menu_send_query_icon);
                } else if (groupName.equalsIgnoreCase("Pay Fees Online")) {
                    imgLogo.setImageResource(R.drawable.menu_payfees_online_icon);
                } else if (groupName.equalsIgnoreCase("calendar")) {
                    imgLogo.setImageResource(R.drawable.calendaricon);
                } else if (groupName.equalsIgnoreCase("food chart")) {
                    imgLogo.setImageResource(R.drawable.food_table_icon_menu);
                } else if (groupName.equalsIgnoreCase("library")) {
                    imgLogo.setImageResource(R.drawable.library_icon_menu);
                } else if (groupName.equalsIgnoreCase("time table")) {
                    imgLogo.setImageResource(R.drawable.time_table_icon_menu);
                } else if (groupName.equalsIgnoreCase("curriculum")) {
                    imgLogo.setImageResource(R.drawable.curriculum_icon_menu);
                }else if (groupName.equalsIgnoreCase("Submission")) {
                    imgLogo.setImageResource(R.drawable.ic_presentation_menu);
                }else if (groupName.equalsIgnoreCase("E-Library")) {
                    imgLogo.setImageResource(R.drawable.ic_smartphone_menu);
                } else if (groupName.equalsIgnoreCase("Track Vehicle")) {
                    imgLogo.setImageResource(R.drawable.menu_vehicle_track_icon1);
                }  else if (groupName.equalsIgnoreCase("LogOut")) {
                    imgLogo.setImageResource(R.drawable.logout_new);
                }else if (groupName.equalsIgnoreCase("take appointment")) {
                    imgLogo.setImageResource(R.drawable.menu_take_appointment);
                }
                else if (groupName.equalsIgnoreCase("Setting")) {
                    imgLogo.setImageResource(R.drawable.menu_settings_icon);
                } else if (groupName.equalsIgnoreCase("Exit")) {
                    imgLogo.setImageResource(R.drawable.setting12);
                } else if (groupName.equalsIgnoreCase("Documents")) {
                    imgLogo.setImageResource(R.drawable.menu_document);
                } else if (groupName.equalsIgnoreCase("Apply Leave")) {
                    imgLogo.setImageResource(R.drawable.menu_office_leave_icon);
                }
                else if (groupName.equalsIgnoreCase("Voice Message")) {
                    imgLogo.setImageResource(R.drawable.gray_voice_msg);
                }
                else if (groupName.equalsIgnoreCase("Practice Test")) {
                    imgLogo.setImageResource(R.drawable.menu_testicon);
                }else if (groupName.equalsIgnoreCase("Home")) {
                    imgLogo.setImageResource(R.drawable.menu_home_icon);
                }
            }

            if (groupName.equalsIgnoreCase("Exam")
                    || groupName.equalsIgnoreCase("Daily Diary")
                    || groupName.equalsIgnoreCase("Fee Card")
                    || groupName.equalsIgnoreCase("Setting")) {
                if (isExpanded) {
                    imgExpandCollapse.setImageResource(R.drawable.minus_icon);
                } else {
                    imgExpandCollapse.setImageResource(R.drawable.plus_icon);
                }
            } else {
                imgExpandCollapse.setImageResource(android.R.color.transparent);
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

/*
 * old code
 *
 * private Context _context; private List<String> _listDataHeader; private
 * List<String> _sub; private List<String> _hwText; private HashMap<String,
 * List<String>> _listDataChild; private LinkedHashMap<String, List<String>>
 * _childL;
 *
 * public HomeMenuExpandableListAdapter(Context context, List<String> listDataHeader,
 * LinkedHashMap<String, List<String>> listChildData) { this._context = context;
 * this._listDataHeader = listDataHeader; this._listDataChild = listChildData;
 * this._childL=listChildData; } public int getGroupCount() { // TODO
 * Auto-generated method stub return this._listDataHeader.size(); }
 *
 * public int getChildrenCount(int groupPosition) { // TODO Auto-generated
 * method stub return
 * this._listDataChild.get(this._listDataHeader.get(groupPosition)) .size(); }
 *
 * public Object getGroup(int groupPosition) { // TODO Auto-generated method
 * stub return this._listDataHeader.get(groupPosition); }
 *
 * public Object getChild(int groupPosition, int childPosition) { // TODO
 * Auto-generated method stub return childPosition; }
 *
 * public long getGroupId(int groupPosition) { // TODO Auto-generated method
 * stub return groupPosition; }
 *
 * public long getChildId(int groupPosition, int childPosition) { // TODO
 * Auto-generated method stub return childPosition; }
 *
 * public boolean hasStableIds() { // TODO Auto-generated method stub return
 * false; }
 *
 * public View getGroupView(int groupPosition, boolean isExpanded, View
 * convertView, ViewGroup parent) { // TODO Auto-generated method stub String
 * headerTitle = (String) getGroup(groupPosition); if (convertView == null) {
 * LayoutInflater infalInflater =
 * (LayoutInflater)this._context.getSystemService(
 * Context.LAYOUT_INFLATER_SERVICE); convertView =
 * infalInflater.inflate(R.layout.activity_expandable__list_adapter,null); }
 * TextView lblListHeader = (TextView) convertView
 * .findViewById(R.id.dateHeader); lblListHeader.setTypeface(null,
 * Typeface.BOLD); lblListHeader.setText(headerTitle); return convertView; }
 *
 * public View getChildView(int groupPosition, int childPosition, boolean
 * isLastChild, View convertView, ViewGroup parent) { final String childText =
 * (String) getChild(groupPosition, childPosition); final String chilString =
 * (String) getChild(groupPosition, childPosition);
 *
 * if (convertView == null) { LayoutInflater infalInflater = (LayoutInflater)
 * this._context .getSystemService(Context.LAYOUT_INFLATER_SERVICE); convertView
 * = infalInflater.inflate(R.layout.child_layout, null); } TextView txtListChild
 * = (TextView) convertView .findViewById(R.id.subName);
 * txtListChild.setText(childText); TextView txtText = (TextView)
 * convertView.findViewById(R.id.subText); txtText.setText(chilString); return
 * convertView; }
 *
 * public boolean isChildSelectable(int groupPosition, int childPosition) { //
 * TODO Auto-generated method stub return false; }
 *
 * }
 */
