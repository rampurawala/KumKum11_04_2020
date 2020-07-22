package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.FeeCardActivity;
import com.expedite.apps.kumkum.activity.MessagesExpandableListActivity;
import com.expedite.apps.kumkum.activity.NoticeBoardActivity;
import com.expedite.apps.kumkum.activity.PhotoGalleryActivity;
import com.expedite.apps.kumkum.activity.TrackVehicle;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.HomeModel;

import java.util.ArrayList;


/**
 * Created by Jaydeep on 17-04-17.
 */
public class HomeDataListAdapter extends RecyclerView.Adapter {
    private ArrayList<HomeModel.HomeList> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    String Pending = "3";

    public HomeDataListAdapter(Context context, ArrayList<HomeModel.HomeList> mArrayList) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mList = mArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.home_data_list_raw_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CustomViewHolder) {
            final HomeModel.HomeList tmp = mList.get(position);

            if (tmp.getTitle() != null && !tmp.getTitle().isEmpty())
                ((CustomViewHolder) holder).txtTitle.setText(tmp.getTitle());

            if (tmp.getType() != null && !tmp.getType().isEmpty()) {
                if (tmp.getType().equalsIgnoreCase("Vehicle Tracking")) {
                    ((CustomViewHolder) holder).imgLogo.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).imgLogo.setImageResource(R.drawable.menu_vehicle_track_icon);
//                    Picasso.with(mContext).load(R.drawable.menu_vehicle_track_icon).into(((CustomViewHolder) holder).imgLogo);
                } else if (tmp.getType().equalsIgnoreCase("Notice Board")) {
                    ((CustomViewHolder) holder).imgLogo.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).imgLogo.setImageResource(R.drawable.menu_noticeboard_icon);
//                    Picasso.with(mContext).load(R.drawable.menu_noticeboard_icon).into(((CustomViewHolder) holder).imgLogo);
                } else if (tmp.getType().equalsIgnoreCase("Photographs")) {
                    ((CustomViewHolder) holder).imgLogo.setVisibility(View.VISIBLE);
//                    Picasso.with(mContext).load(R.drawable.menu_photo_gallery_icon).into(((CustomViewHolder) holder).imgLogo);
                    ((CustomViewHolder) holder).imgLogo.setImageResource(R.drawable.menu_photo_gallery_icon);
                } else if (tmp.getType().equalsIgnoreCase("Message")) {
                    ((CustomViewHolder) holder).imgLogo.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).imgLogo.setImageResource(R.drawable.menu_message_icon);
//                    Picasso.with(mContext).load(R.drawable.menu_message_icon).into(((CustomViewHolder) holder).imgLogo);
                } else if (tmp.getType().equalsIgnoreCase("Fees")) {
                    ((CustomViewHolder) holder).imgLogo.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).imgLogo.setImageResource(R.drawable.menu_fees_icon);
                } else if (tmp.getType().equalsIgnoreCase("Holiday")) {
                    ((CustomViewHolder) holder).imgLogo.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).imgLogo.setImageResource(R.drawable.menu_noticeboard_icon);
                }else {
                    ((CustomViewHolder) holder).imgLogo.setVisibility(View.GONE);
                }
            } else {
                ((CustomViewHolder) holder).imgLogo.setVisibility(View.GONE);
            }
            ((CustomViewHolder) holder).mMainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tmp.getType().equalsIgnoreCase("Vehicle Tracking")) {
                        if (((BaseActivity) mContext).isOnline()) {
                            //Intent intent = new Intent(mContext, VechicleLocationActivity.class);
                            Intent intent = new Intent(mContext, TrackVehicle.class);
                            intent.putExtra("IsFromHome", "IsFromHome");
                            mContext.startActivity(intent);
                            ((BaseActivity) mContext).onClickAnimation();
                        } else {
                            Constants.isShowInternetMsg = true;
                            Constants.NotifyNoInternet(mContext);
                        }
                    } else if (tmp.getType().equalsIgnoreCase("Notice Board")) {
                        Intent intent = new Intent(mContext, NoticeBoardActivity.class);
                        intent.putExtra("IsFromHome", "IsFromHome");
                        mContext.startActivity(intent);
                        ((BaseActivity) mContext).onClickAnimation();
                    }  else if (tmp.getType().equalsIgnoreCase("Fees")) {
                        Intent intent = new Intent(mContext, FeeCardActivity.class);
                        intent.putExtra("FeeStatus", Pending);
                        intent.putExtra("IsFromHome", "IsFromHome");
                        mContext.startActivity(intent);
                        ((BaseActivity) mContext).onClickAnimation();
                    } else if (tmp.getType().equalsIgnoreCase("Photographs")) {
                        Intent intent = new Intent(mContext, PhotoGalleryActivity.class);
                        intent.putExtra("IsFromHome", "IsFromHome");
                        mContext.startActivity(intent);
                        ((BaseActivity) mContext).onClickAnimation();
                    } else if (tmp.getType().equalsIgnoreCase("Message")) {
                        Intent intent = new Intent(mContext, MessagesExpandableListActivity.class);
                        intent.putExtra("IsFromHome", "IsFromHome");
                        mContext.startActivity(intent);
                        ((BaseActivity) mContext).onClickAnimation();
                    }
                }
            });
        }

    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgLogo;
        private TextView txtTitle;
        private View mMainView;

        public CustomViewHolder(View view) {
            super(view);
            imgLogo = (ImageView) view.findViewById(R.id.imgLogo);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
//            txtTitle.setTypeface(sFontRegular);
            mMainView = (View) view.findViewById(R.id.MainView);

        }
    }


}



