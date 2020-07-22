package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter{

	private Context context;
	private Bitmap[] pics;
	
	
	public ImageAdapter(Context c,Bitmap[] pics2){
		this.context = c;
		this.pics = pics2;
		
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return pics.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 ImageView iv = new ImageView(context);
		    iv.setImageBitmap(pics[position]);
		    iv.setScaleType(ImageView.ScaleType.FIT_XY);
		    iv.setLayoutParams(new Gallery.LayoutParams(150,120));
		   
		    return iv;
	   
	   
	}

}
