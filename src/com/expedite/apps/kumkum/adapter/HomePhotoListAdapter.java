package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.AlbumWiseDetailActivityNew;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.constants.Constants;


/**
 * Created by Jaydeep on 17-04-17.
 */
public class HomePhotoListAdapter extends RecyclerView.Adapter {
    private String[] filePath;
    private String[] albumnamne;
    private Integer[] albumId;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public HomePhotoListAdapter(Context context, String[] filePath, String[] alname, Integer[] albumIds) {
        this.filePath = filePath;
        albumnamne = alname;
        albumId = albumIds;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.home_photos_item_row, parent, false);
        return new CustomViewHolder(view);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return filePath.length;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CustomViewHolder) {
            if (filePath != null && filePath[position] != null) {
                String[] splterstr = filePath[position].split("@@@###");
                String filepath = splterstr[0];
                Bitmap bmp = null;
                try {
                    bmp = BitmapFactory.decodeFile(filepath);
                    if(bmp==null){
                        ((CustomViewHolder) holder).imgPhotos.setImageResource(R.drawable.nopics1);
                    }else {
                        ((CustomViewHolder) holder).imgPhotos.setImageBitmap(bmp);
                    }
                } catch (Exception ex) {
                    Common.printStackTrace(ex);
                }
            }
            if (albumnamne != null && albumnamne[position] != null)
                ((CustomViewHolder) holder).txtTitle.setText(albumnamne[position]);

            ((CustomViewHolder) holder).ll_mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (albumId != null)
                        try {
                            String id = albumId[position].toString();
                            String name = albumnamne[position].toString();
                            Intent intent = new Intent(mContext, AlbumWiseDetailActivityNew.class);
                            intent.putExtra("ALbUMID", id);
                            intent.putExtra("ALbUMName", name);
                            mContext.startActivity(intent);
                            ((BaseActivity) mContext).onClickAnimation();
                        } catch (Exception err) {
                            Constants.writelog("HomePhotoListAdapter", err.getMessage());
                        }
                }
            });
        }
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPhotos, imgLogo;
        private TextView txtTitle;
        private View ll_mainLayout;

        public CustomViewHolder(View view) {
            super(view);
            imgPhotos = (ImageView) view.findViewById(R.id.imgPhotos);
            imgLogo = (ImageView) view.findViewById(R.id.imgLogo);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            ll_mainLayout = (View) view.findViewById(R.id.ll_mainLayout);
        }
    }


}



