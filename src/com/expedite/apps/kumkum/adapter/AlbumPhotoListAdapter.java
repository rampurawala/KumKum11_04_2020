package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.AlbumWiseDetailActivityNew;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.Contact;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jaydeep on 24-04-17.
 */
public class AlbumPhotoListAdapter extends RecyclerView.Adapter  implements Filterable {
    private String[] filePath;
    private String[] albumnamne;
    private String[] albumtime;
    private Integer[] albumId;
    private String[] searchFilePath;
    private String[] searchAlbumnamne;
    private String[] searchAlbumtime;
    private Integer[] searchAlbumId;
    private Context mContext;
    List<Contact> albumList=new ArrayList<>(),valueSearch=new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private int lastPosition = -1;

    public AlbumPhotoListAdapter(Context context,List<Contact> albumList/* String[] filePath, String[] alname,
                                 String[] albumtimes, Integer[] albumIds*/) {
       /* this.filePath = filePath;
        albumnamne = alname;
        albumId = albumIds;
        albumtime = albumtimes;*/
        this.albumList=albumList;
        valueSearch=albumList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.album_photos_item_row_new, parent, false);
        return new CustomViewHolder(view);


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return valueSearch.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CustomViewHolder) {
            final Contact tmp=valueSearch.get(position);
            if (tmp.getAlbumPhotofile() != null) {
                String[] splterstr = tmp.getAlbumPhotofile().split("@@@###");
                String filepath = splterstr[0];
                Bitmap bmp = null;
                try {
                    bmp = BitmapFactory.decodeFile(filepath);
                    ((CustomViewHolder) holder).imgPhotos.setImageBitmap(bmp);
                } catch (Exception ex) {
                    Common.printStackTrace(ex);
                }
            }
            if (  tmp.getAlbumName() != null)
                ((CustomViewHolder) holder).txtTitle.setText(tmp.getAlbumName());

            ((CustomViewHolder) holder).ll_mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tmp.getAlbumId() >0)
                        try {
                            String id =String.valueOf(tmp.getAlbumId());
                            String name =String.valueOf(tmp.getAlbumName());
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

            Animation animation = AnimationUtils.loadAnimation(mContext,
                    (position > lastPosition) ? R.anim.slide_in_right
                            : R.anim.slide_in_left);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }

    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
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
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    valueSearch = albumList;
                } else {
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact row : albumList) {
                        if (row.getAlbumName().toLowerCase().contains(charString.toLowerCase()))/* || row.getNo().contains(charSequence)*/ {
                            filteredList.add(row);
                        }
                    }
                    valueSearch = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = valueSearch;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                valueSearch = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}



