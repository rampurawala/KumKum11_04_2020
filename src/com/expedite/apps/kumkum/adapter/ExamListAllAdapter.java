package com.expedite.apps.kumkum.adapter;

import android.app.Activity;
import android.content.Intent;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.StudentResultActvity;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.constants.SchoolDetails;


/**
 * Created by Jaydeep on 20-04-17.
 */
public class ExamListAllAdapter extends RecyclerView.Adapter<ExamListAllAdapter.ViewHolder> {
    String[] messages = {""};
    String[] itmid = {""};
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    int flag = 0;
    String selectedPosition = "";


    public ExamListAllAdapter(Activity context, String[] messages, String[] itmid, int flag) {
        this.messages = messages;
        this.itmid = itmid;
        this.flag = flag;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.exam_list_raw_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExamListAllAdapter.ViewHolder holder, final int position) {
        if (messages[position] != null && !messages[position].isEmpty())
            holder.txtmessages.setText(messages[position]);

        holder.mCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((BaseActivity) mContext).isOnline()) {
                    if (flag == 0) {

                        Intent intent = new Intent(mContext, StudentResultActvity.class);
                        intent.putExtra("examName",messages[position]);
                        intent.putExtra("ExamId", itmid[position]);
                        mContext.startActivity(intent);
                        Common.onClickAnimation(mContext);
                    }
                } else {
                    Toast.makeText(mContext, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
                }

//                } else if (flag == 1) {
//                    selectedPosition = String.valueOf(position);
//                    new MyTaskSavePdf().execute();
//                }
            }
        });

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return messages.length;
    }

    public String getItenName(int arg) {
        return messages[arg];
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtmessages;
        protected CardView mCardview;

        public ViewHolder(View itemView) {
            super(itemView);
            txtmessages = (TextView) itemView.findViewById(R.id.txtmessages);
            mCardview = (CardView) itemView.findViewById(R.id.Cardtextview);
        }
    }

/*    private class MyTaskSavePdf extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mContext.findViewById(R.id.ProgressBar).setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ConnectionDetector cd = new ConnectionDetector(mContext);
            String[] parts = arrpath[selectedPosition].split("//");
            filename = parts[parts.length - 1];
            File myDir = Constants.CreatePhotoGalleryFolder();
            File file = new File(myDir + "/" + filename);
            try {
                if (file == null || !file.exists() || file.length() < 2) {
                    if (cd.isConnectingToInternet()) {
                        fileURL = arrpath[selectedPosition];
                        Constants.SavePdf(ExamListMarksheetActivity.this, fileURL, filename);
                    } else {
                        Constants.isShowInternetMsg = true;
                    }
                }
            } catch (Exception ex) {
                Constants.writelog("ExamListMarksheetActivity", "Exception 156:"
                        + ex.getMessage() + ":::::::" + ex.getStackTrace());
                Constants.Logwrite("ExamListMarksheetActivity", "Exception 156:" + ex.getMessage()
                        + ":::::::" + ex.getStackTrace());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                mProgressBar.setVisibility(View.GONE);
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(getApplicationContext());
                } else {
                    String item = mAdapter.getItenName(selectedPosition).toString();
                    String Exam_Id = map.get(item).toString();
                    String[] parts = arrpath[selectedPosition].split("//");
                    filename = parts[parts.length - 1];
                    File myDir = Constants.CreatePhotoGalleryFolder();
                    File file = new File(myDir + "/" + filename);
                    Uri uri1 = Uri.fromFile(file);
                    PackageManager packageManager = getPackageManager();
                    if (file.exists()) {
                        Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                        intentUrl.setDataAndType(uri1, "application/pdf");
                        List list = packageManager.queryIntentActivities(
                                intentUrl, PackageManager.MATCH_DEFAULT_ONLY);
                        if (list.size() > 0 && file.isFile()) {
                            intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intentUrl.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intentUrl);
                            onClickAnimation();
                        } else {
                            Intent intent = new Intent(ExamListMarksheetActivity.this, MarksheetResultActivity.class);
                            intent.putExtra("ExamId", Exam_Id);
                            startActivity(intent);
                            onClickAnimation();
                        }
                    } else {
                        Intent intent = new Intent(ExamListMarksheetActivity.this, MarksheetResultActivity.class);
                        intent.putExtra("ExamId", Exam_Id);
                        startActivity(intent);
                        onClickAnimation();
                    }
                }
            } catch (Exception ex) {
                Constants.writelog("ExamListMarksheetActivity", "Exception 207:"
                        + ex.getMessage() + ":::::::" + ex.getStackTrace());
                mProgressBar.setVisibility(View.GONE);
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }*/
}



