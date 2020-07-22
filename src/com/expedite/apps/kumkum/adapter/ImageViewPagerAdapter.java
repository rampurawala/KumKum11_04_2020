package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.ImageZoom;
import com.expedite.apps.kumkum.constants.Constants;

import java.io.File;

//created by vishwa on 11/6/2019

public class ImageViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private File filepth, file;
    public Bitmap bmp;
    private String[] localpath,url,album;
    private static LayoutInflater mInflater;

    public ImageViewPagerAdapter(Context mContext, String[] localpath,String[] url,String[] album) {
        this.mContext = mContext;
        this.localpath = localpath;
        this.url=url;
        this.album = album;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        int count = 0;
        {
            if (localpath != null && localpath.length > 0) {
                count = localpath.length;
            }else if(url!=null && url.length>0) {
                count=url.length;
            }
            return count;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mInflater.inflate(R.layout.image_view_pager_raw_layout, null);
        try {
            file = Constants.CreatePhotoGalleryFolder();
            String[] splterstr = localpath[position].split("@@@###");
            final String filepath1 = splterstr[0].toString();
            ImageZoom imageView = (ImageZoom) view.findViewById(R.id.imzview);
            filepth = new File(file + "/" + album[position] + "");
            if (!filepth.exists()) {
                if (url[position]!=null)
                    Glide.with(mContext)
                            .load(url[position]).asBitmap().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.nopics)
                            .into(imageView);
            }else {
                bmp = null;
                try {
                    bmp = BitmapFactory.decodeFile(filepath1);
                } catch (Exception ex) {
                    Constants.writelog("imageViewpagerAdpter", "onBindviewHolder()135 MSG:" + ex.getMessage());
                }
                imageView.setImageBitmap(bmp);
            }
            container.addView(view);
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
