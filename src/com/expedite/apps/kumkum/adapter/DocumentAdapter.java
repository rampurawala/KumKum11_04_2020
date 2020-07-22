package com.expedite.apps.kumkum.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.PdfViewAcitivity;

import com.expedite.apps.kumkum.model.Document;

import java.util.ArrayList;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> /*implements Filterable*/ {
    Activity mContext;
    ArrayList<Document.DocumentItemList> mStudentsArray;
    private ArrayList<Document.DocumentItemList> mFilteredList;
    LayoutInflater mViewInflater;
    String tag = "DocumentAdapter";
    int isAll;

    public DocumentAdapter(Activity activity, ArrayList<Document.DocumentItemList> mStudentsArray/*, int isAll*/) {
        mContext = activity;
        this.mStudentsArray = mStudentsArray;
        this.mFilteredList = mStudentsArray;
        /*  this.isAll = isAll;*/
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = (LayoutInflater.from(parent.getContext())).inflate(R.layout.documents, parent, false);
        mViewInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            final Document.DocumentItemList tmp = mFilteredList.get(position);
            /*  if (isAll == 1) {*/
            if (tmp.getYearFlag()!=null && tmp.getYearFlag()!="" && tmp.getYearFlag().equalsIgnoreCase("1")) {
                holder.imgfile.setVisibility(View.GONE);
                holder.DocumentName.setText(Html.fromHtml(tmp.getYear()));
                holder.mainfilell.setBackgroundColor(
                        mContext.getResources().getColor(R.color.grayshade));
                holder.DocumentName.setTextColor(mContext.getResources()
                        .getColor(R.color.colorPrimary));
                holder.DocumentName.setGravity(Gravity.CENTER_HORIZONTAL);
            } else {
                holder.imgfile.setVisibility(View.VISIBLE);
                holder.DocumentName.setText(Html.fromHtml(tmp.getDocumentName()));
                holder.mainfilell.setBackgroundColor(
                        mContext.getResources().getColor(R.color.white));
                holder.DocumentName.setTextColor(mContext.getResources()
                        .getColor(R.color.textbg));
                holder.DocumentName.setGravity(Gravity.LEFT);
            }
          /*  } else {
                holder.imgfile.setVisibility(View.VISIBLE);
                holder.DocumentName.setText(Html.fromHtml(tmp.getDocumentName()));
                holder.mainfilell.setBackgroundColor(
                        mContext.getResources().getColor(R.color.white));
                holder.DocumentName.setTextColor(mContext.getResources()
                        .getColor(R.color.textbg));
                holder.DocumentName.setGravity(Gravity.LEFT);
            }*/

            holder.Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PdfViewAcitivity.class);
                    intent.putExtra("ImageUrl", tmp.getDisplayUrl());
                    intent.putExtra("Name", tmp.getDocumentName());
                    mContext.startActivity(intent);
                }
            });


        } catch (Exception ex) {

        }
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

   /* @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mStudentsArray;
                } else {

                    ArrayList<Document.DocumentItemList> filteredList = new ArrayList<>();

                    for (Document.DocumentItemList documentItemList : mStudentsArray) {

                        if (documentItemList.getDocumentName().toLowerCase().contains(charString)) {

                            filteredList.add(documentItemList);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Document.DocumentItemList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView DocumentName;
        RelativeLayout Layout;
        ImageView imgfile;
        LinearLayout mainfilell;

        public ViewHolder(View itemView) {
            super(itemView);

            DocumentName = (TextView) itemView.findViewById(R.id.DocumentName);
            Layout = (RelativeLayout) itemView.findViewById(R.id.Layout);
            imgfile = itemView.findViewById(R.id.imgfile);
            mainfilell = itemView.findViewById(R.id.mainfilell);
         /*   ShareUrl = (TextView) itemView.findViewById(R.id.ShareUrl);
            DisplayUrl = (TextView) itemView.findViewById(R.id.DisPlayUrl);*/


        }
    }
}

