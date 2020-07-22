package com.expedite.apps.kumkum.adapter;

 

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.lazylist.ImageLoader;

public class LazyAdapter extends BaseAdapter {
    
	Context context;
    private String[] data;
    private String[] albumname;
    private String[] albumtime;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;
    
    public LazyAdapter(Context a, String[] d,String[] alname,String[] altime) {
    	context = a;
        data=d;
        albumname = alname;
        albumtime = altime;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(context.getApplicationContext());
    }
    public int getCount() {
        return data.length;
    }
    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    public String getImageUrl(int arg0) {
		// TODO Auto-generated method stub
		return data[arg0].toString();
	}
    public String getAlbumDateWithTime(int arg0) {
		// TODO Auto-generated method stub
		return albumtime[arg0].toString();
	}
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.activity_photoitem,null);
        TextView text=(TextView)vi.findViewById(R.id.txtalbumname);
        ImageView image=(ImageView)vi.findViewById(R.id.image);
        //TextView txttime=(TextView)vi.findViewById(R.id.txttime);
        try
        {
        text.setText(albumname[position].toString());
        //txttime.setText(albumtime[position].toString());
        }
        catch(Exception err)
        {
        }
        imageLoader.DisplayImage(data[position],image,context);
        return vi;
    } 
}