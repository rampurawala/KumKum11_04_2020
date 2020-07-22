package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.MessagesExpandableListActivity;
import com.expedite.apps.kumkum.common.Common;

/**
 * Created by Jaydeep on 24/09/16.
 */
/*public class HomeMessageAdapter extends PagerAdapter {

    private String[] messages;
    private LayoutInflater inflater;
    private Context mContext;
    private String date = "";


    public HomeMessageAdapter(Context context, String[] messages) {
        this.mContext = context;
        this.messages = messages;
        inflater = LayoutInflater.from(context);


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return messages.length;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View mView = inflater.inflate(R.layout.message_list_raw_layout, null);
        if (messages != null && messages[position] != null) {
                try {
                    String[] items = messages[position].split("##,@@");
                    if (items[0] != null && !items[0].isEmpty())
                        mTxtMessage.setText(Html.fromHtml(items[0]));

                    if (items != null && items.length > 1)
                        if (items[1] != null && !items[1].isEmpty())
                            mTxtDate.setText(((BaseActivity) mContext).convertDate(mContext, items[1]));
                } catch (Exception ex) {
                    Common.printStackTrace(ex);
                }
            }
            ll_MainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MessagesExpandableListActivity.class);
                    intent.putExtra("IsFromHome", "IsFromHome");
                    mContext.startActivity(intent);
                    ((BaseActivity) mContext).onClickAnimation();

                }
            });
            view.addView(mView, 0);
        }
        return mView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}*/
public class HomeMessageAdapter extends RecyclerView.Adapter<HomeMessageAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private String[] messages;
    private Context mcontext;

    public HomeMessageAdapter(String[] messages, Context mcontext) {

        this.messages = messages;
        this.mcontext = mcontext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_raw_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: Images Set");
        if (messages != null && messages[position] != null) {
            try {
                String[] items = messages[position].split("##,@@");
                if (items[0] != null && !items[0].isEmpty())
                    holder.mTxtMessage.setText(Html.fromHtml(items[0]));

                if (items != null && items.length > 1) {
                    if (items[1] != null && !items[1].isEmpty()) {
                        holder.mTxtDate.setVisibility(View.VISIBLE);
                        holder.mTxtDate.setText(((BaseActivity) mcontext).convertDate(mcontext, items[1]));
                    }
                }
            } catch (Exception ex) {
                Common.printStackTrace(ex);
            }
        }
        holder.ll_MainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, MessagesExpandableListActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                mcontext.startActivity(intent);
                ((BaseActivity) mcontext).onClickAnimation();

            }
        });
    }


    @Override
    public int getItemCount() {
        return messages.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTxtMessage,mTxtDate;
        View ll_MainView;


        public ViewHolder( View itemView) {
            super(itemView);
             mTxtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
             mTxtDate = (TextView) itemView.findViewById(R.id.txtDate);
             ll_MainView = (View) itemView.findViewById(R.id.ll_MainView);
        }
    }
}

