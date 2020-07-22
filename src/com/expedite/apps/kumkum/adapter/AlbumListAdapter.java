package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.ImageDetailPagerActivity;
import com.expedite.apps.kumkum.constants.Constants;

import java.io.File;

public class AlbumListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private String[] filepath, albumnamne, albumurl;
    private File filepth, file;
    private String[] album;
    private String[] albumdate;
    public Bitmap bmp;
    public String filename;
    private LayoutInflater mLayoutInflater;
    private int lastPosition = -1;

    public AlbumListAdapter(Context context, String[] Path, String[] albumlist, String[] albumURL,
                            String[] flexst, String[] albumtime) {
        mContext = context;
        filepath = Path;
        albumnamne = albumlist;
        albumdate = albumtime;
        albumurl = albumURL;
        album = flexst;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = mLayoutInflater.inflate(R.layout.gridalbumdetaillist_new, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CustomViewHolder) {
            try {
                file = Constants.CreatePhotoGalleryFolder();
                String[] splterstr = filepath[position].split("@@@###");
                final String filepath1 = splterstr[0].toString();
                ((CustomViewHolder) holder).text.setText(albumnamne[position]);
                ((CustomViewHolder) holder).textdatetime.setText(albumdate[position]);
                filepth = new File(file + "/" + album[position] + "");
                if (!filepth.exists()) {
                    Glide.with(mContext).load(albumurl[position])
                            .asBitmap().centerCrop().animate(R.anim.fade_in).
                            placeholder(R.drawable.placeholder).error(R.drawable.nopics).
                            into(new BitmapImageViewTarget(((CustomViewHolder) holder).image));
                  /*  Picasso.with(mContext).load(albumurl[position])
                            .placeholder(R.drawable.placeholder)
                            .into(((CustomViewHolder) holder).image);*/
                } else {
                    bmp = null;
                    try {
                        bmp = BitmapFactory.decodeFile(filepath1);
                    } catch (Exception ex) {
                        Constants.writelog("selectAlbumAdapter", "onBindviewHolder()135 MSG:" + ex.getMessage());
                    }
                    // Set the decoded bitmap into ImageView
                    if(bmp==null){
                        ((CustomViewHolder) holder).image.setImageResource(R.drawable.nopics);
                    }else {
                        ((CustomViewHolder) holder).image.setImageBitmap(bmp);
                    }
                }

                /*((CustomViewHolder) holder).image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String[] splterstr = filepath[position].split("@@@###");
                            filename = splterstr[0];
                            Intent intent = new Intent(mContext, ImageZoomActivity.class);
                            intent.putExtra("imgURL", albumurl[position]);
                            mContext.startActivity(intent);
                            ((BaseActivity) mContext).onClickAnimation();
                        } catch (Exception ex) {
                            Constants.writelog("selectAlbumAdapter", "selectAlbum_setonclick()135 MSG:" + ex.getMessage());
                        }
                    }
                });*/

                ((CustomViewHolder) holder).image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Bundle b=new Bundle();
                            b.putStringArray("ImgURL",albumurl);
                            b.putStringArray("localImgURL",filepath);
                            b.putStringArray("album",album);
                            b.putInt("mSelectedPosition",position);
                            Intent intent = new Intent(mContext, ImageDetailPagerActivity.class);
                            intent.putExtras(b);
                            mContext.startActivity(intent);
                            ((BaseActivity) mContext).onClickAnimation();
                        } catch (Exception ex) {
                            Constants.writelog("selectAlbumAdapter", "selectAlbum_setonclick()135 MSG:" + ex.getMessage());
                        }
                    }
                });

               /* Animation animation = AnimationUtils.loadAnimation(mContext,
                        (position > lastPosition) ? R.anim.up_from_bottom
                                : R.anim.down_from_top);
                holder.itemView.startAnimation(animation);
                lastPosition = position;*/

            } catch (Exception ex) {
                Constants.writelog("selectAlbumAdapter", "On_Bindview()147 MSG:" + ex.getMessage());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        int count = 0;
        {
            if (filepath != null && filepath.length > 0) {
                count = filepath.length;
            }
            return count;
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView text, textdatetime;
        ImageView image;
        View llMain;

        public CustomViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.txtalbumname);
            image = (ImageView) itemView.findViewById(R.id.image);
            textdatetime = (TextView) itemView.findViewById(R.id.txtdatetime);
            llMain = (View) itemView.findViewById(R.id.llMain);
        }
    }
}
