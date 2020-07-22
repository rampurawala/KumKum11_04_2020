package com.expedite.apps.kumkum.adapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.constants.Constants;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PhotoGallaryListAdapter extends BaseAdapter {

	Context cntxt;
	private String[] filepath;
	private String[] albumnamne;
	private String[] albumdate;
	private String[] albmurl;
	private String[] albm;
	File filepth;
	private static LayoutInflater inflater = null;

	public PhotoGallaryListAdapter()
	{}
	public PhotoGallaryListAdapter(Context a, String[] fpath, String[] alname,
                                   String[] aldate, String[] albumURL, String[] albmimg) {
		cntxt = a;
		filepath = fpath;
		albumnamne = alname;
		albumdate = aldate;
		albmurl=albumURL;
		albm=albmimg;
		inflater = (LayoutInflater) cntxt
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return filepath.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public String getDateTime(int position) {
		// TODO Auto-generated method stub
		return albumdate[position];
	}
	public String getFileurl(int position) {
		// TODO Auto-generated method stub
			return albmurl[position];
	}
	public String getFileName(int position) {
		// TODO Auto-generated method stub
		String[] splterstr = filepath[position].split("@@@###");
		String filename = splterstr[1].toString();
		return filename;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			if (convertView == null)
				convertView = inflater.inflate(R.layout.activity_list_view_adapter, null);
			// Locate the TextView in gridview_item.xml
			TextView text = (TextView) convertView.findViewById(R.id.txtalbumname);
			// Locate the ImageView in gridview_item.xml
			ImageView image = (ImageView) convertView.findViewById(R.id.image);
			TextView textdatetime = (TextView) convertView
					.findViewById(R.id.txtdatetime);
			File file = Constants.CreatePhotoGalleryFolder();

			// Set file name to the TextView followed by the position

			String[] splterstr = filepath[position].split("@@@###");
			String filepath = splterstr[0].toString();
			text.setText(albumnamne[position]);
			textdatetime.setText(albumdate[position]);
			filepth = new File(file + "/" + albm[position] + "");
			if (!filepth.exists()) {
				Picasso.with(cntxt).load(albmurl[position]).into(image);
			} else {      // Decode the filepath with BitmapFactory followed by the position
				Bitmap bmp = null;
				try {
					bmp = BitmapFactory.decodeFile(filepath);
				} catch (Exception ex) {
					Constants.writelog("PhotoGallaryListAdapter", "view()175 MSG:" + ex.getMessage());
				}
				// Set the decoded bitmap into ImageView
				image.setImageBitmap(bmp);
			}
		} catch (Exception err) {
		}
		return convertView;
	}

}
