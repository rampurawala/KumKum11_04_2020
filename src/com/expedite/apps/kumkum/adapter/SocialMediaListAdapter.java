package com.expedite.apps.kumkum.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.SocialMediaListModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by tejas patel on 06/09/2018.
 */
public class SocialMediaListAdapter extends RecyclerView.Adapter {
    private List<SocialMediaListModel.SocialMediaList> mList = new ArrayList<>();
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private String Tag = "SocialMediaListAdapter";

    public SocialMediaListAdapter(Activity context, List<SocialMediaListModel.SocialMediaList> ItemList) {
        mList = ItemList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.socialmedia_list_raw_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            if (holder instanceof CustomViewHolder) {
                final SocialMediaListModel.SocialMediaList tmp = mList.get(position);

                if (tmp.getTitle() != null && !tmp.getTitle().isEmpty())
                    ((CustomViewHolder) holder).txtTitle.setText(tmp.getTitle().trim());

                if (tmp.getImageUrl() != null && !tmp.getImageUrl().isEmpty()) {
                    ((CustomViewHolder) holder).imgMedia.setVisibility(View.VISIBLE);
                    Glide.with(mContext)
                            .load(tmp.getImageUrl()).asBitmap().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(((CustomViewHolder) holder).imgMedia);
                } else {
                    ((CustomViewHolder) holder).imgMedia.setVisibility(View.INVISIBLE);
                }
                ((CustomViewHolder) holder).ll_DataView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((BaseActivity) mContext).isOnline()) {
                            if (tmp.getLink() != null && !tmp.getLink().isEmpty()) {
                                String url = "";
                                if (!tmp.getLink().startsWith("http://") && !tmp.getLink().startsWith("https://")) {
                                    url = "http://" + tmp.getLink();
                                } else {
                                    url = tmp.getLink();
                                }
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                mContext.startActivity(browserIntent);
                            }
                        } else {
                            Common.showToast(mContext, mContext.getString(R.string.msg_connection));
                        }
                    }
                });


            }
        } catch (Exception ex) {
            Constants.writelog(Tag, "Ex 67:" + ex.getMessage());
        }
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtCount;
        private ImageView imgMedia;
        private View ll_DataView;

        public CustomViewHolder(View view) {
            super(view);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            imgMedia = (ImageView) view.findViewById(R.id.imgMedia);
            ll_DataView = (View) view.findViewById(R.id.ll_DataView);

        }
    }
}



