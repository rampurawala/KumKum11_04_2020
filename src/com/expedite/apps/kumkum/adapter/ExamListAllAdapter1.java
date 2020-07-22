package com.expedite.apps.kumkum.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.CurriculumPdfViewActivity;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.ConnectionDetector;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.model.SendEmailDownlodModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.expedite.apps.kumkum.activity.ExamListMarksheetActivity.checkBoxselectall;
import static com.expedite.apps.kumkum.activity.ExamListMarksheetActivity.dowmloadbtn;

/**
 * Created by Jaydeep on 20-04-17.
 */
public class ExamListAllAdapter1 extends RecyclerView.Adapter<ExamListAllAdapter1.ViewHolder> {
    String[] messages = {""};
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    int selectedPosition;
    String[] arrpath;
    private String filename = "", fileURL = "";
    String[] itmid;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressBar progressBar;
    private EditText mEmailid;
    private AlertDialog alert;
    private SparseBooleanArray mSparseBooleanArray;
    private String allmsg = "", allarrPath = "", allitmId = "";

    public ExamListAllAdapter1(Activity context, String[] messages, String[] arrpath, String[] itmid) {
        this.messages = messages;
        mContext = context;
        this.arrpath = arrpath;
        this.itmid = itmid;
        mLayoutInflater = LayoutInflater.from(mContext);
        mSparseBooleanArray = new SparseBooleanArray();
    }

    //change by vishwa 16/4/2019
    public HashMap<String, ArrayList<String>> getCheckedItems() {
        HashMap<String, ArrayList<String>> multiMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> mMsgArry = new ArrayList<String>();
        ArrayList<String> mArrypathArry = new ArrayList<String>();
        ArrayList<String> mItemidArry = new ArrayList<String>();
        for (int i = 0; i < messages.length; i++) {
            if (mSparseBooleanArray.get(i)) {
                mMsgArry.add(messages[i]);
                mArrypathArry.add(arrpath[i]);
                mItemidArry.add((itmid[i]));
            }
        }
        multiMap.put("messages", mMsgArry);
        multiMap.put("arrpath", mArrypathArry);
        multiMap.put("itmid", mItemidArry);
        return multiMap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.exam_list_raw_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExamListAllAdapter1.ViewHolder holder, final int position) {
        try {
            // change by vishwa
            holder.mCheckbox.setTag(position);
            holder.mCheckbox.setChecked(mSparseBooleanArray.get(position));
            if (messages[position] != null && !messages[position].isEmpty()) {
                holder.txtmessages.setText(messages[position]);
               /* holder.mImgDownload.setVisibility(View.GONE);
                holder.mImgDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowDialog(position);
                    }
                });*/

                //chnge by vishwa
                checkBoxselectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            mSparseBooleanArray.clear();
                            dowmloadbtn.setVisibility(View.VISIBLE);
                            for (int i = 0; i < messages.length; i++) {
                                mSparseBooleanArray.put(i, isChecked);
                                holder.mCheckbox.setChecked(mSparseBooleanArray.get(i));
                            }
                        } else {
                            if (mSparseBooleanArray.size() == messages.length) {
                                dowmloadbtn.setVisibility(View.GONE);
                                for (int i = 0; i < messages.length; i++) {
                                    mSparseBooleanArray.delete(i);
                                }
                            }
                        }
                        if (mSparseBooleanArray.size() == messages.length) {
                            checkBoxselectall.setChecked(true);
                        } else {
                            checkBoxselectall.setChecked(false);
                        }
                        notifyDataSetChanged();
                    }
                });

                //vishwa patel 15/04/2019
                holder.mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            dowmloadbtn.setVisibility(View.VISIBLE);
                            mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
                        } else {
                            mSparseBooleanArray.delete((Integer) buttonView.getTag());
                        }
                        if (mSparseBooleanArray.size() == 0) {
                            dowmloadbtn.setVisibility(View.GONE);
                        }
                        if (mSparseBooleanArray.size() == messages.length) {
                            checkBoxselectall.setChecked(true);
                        } else {
                            checkBoxselectall.setChecked(false);
                        }
                    }
                });

                if (dowmloadbtn.getVisibility() == View.VISIBLE) {
                    dowmloadbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShowDialog();
                        }
                    });
                }

            } else {
                holder.mImgDownload.setVisibility(View.GONE);
            }

            holder.mCardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((BaseActivity) mContext).isOnline()) {
                        selectedPosition = position;
                        new MyTaskSavePdf().execute();
                    } else {
                        Toast.makeText(mContext, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
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
        protected ImageView mImgDownload;
        CheckBox mCheckbox;

        public ViewHolder(View itemView) {
            super(itemView);
            txtmessages = (TextView) itemView.findViewById(R.id.txtmessages);
            mCardview = (CardView) itemView.findViewById(R.id.Cardtextview);
            mImgDownload = (ImageView) itemView.findViewById(R.id.imgDownload);
            mCheckbox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }

    private class MyTaskSavePdf extends AsyncTask<Void, Void, Void> {
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
                        Constants.SavePdf(mContext, fileURL, filename);
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
                mContext.findViewById(R.id.ProgressBar).setVisibility(View.GONE);
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(mContext);
                } else {
                   /*  String item = messages[selectedPosition];
                    String Exam_Id = itmid[selectedPosition];
                    String[] parts = arrpath[selectedPosition].split("//");
                    filename = parts[parts.length - 1];
                    File myDir = Constants.CreatePhotoGalleryFolder();
                    File file = new File(myDir + "/" + filename);
                    Uri uri1 = Uri.fromFile(file);
                    PackageManager packageManager = mContext.getPackageManager();
                   if (file.exists()) {
                        Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                        intentUrl.setDataAndType(uri1, "application/pdf");
                        List list = packageManager.queryIntentActivities(
                                intentUrl, PackageManager.MATCH_DEFAULT_ONLY);
                        if (list.size() > 0 && file.isFile()) {
                            intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intentUrl.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            mContext.startActivity(intentUrl);
                            Common.onClickAnimation(mContext);
                        } else {
                            Intent intent = new Intent(mContext, MarksheetResultActivity.class);
                            intent.putExtra("ExamId", Exam_Id);
                            mContext.startActivity(intent);
                            Common.onClickAnimation(mContext);
                        }
                    } else {
                   */    /* Intent intent = new Intent(mContext, MarksheetResultActivity.class);
                        intent.putExtra("ExamId", Exam_Id);
                        intent.putExtra("Name",messages[selectedPosition]);
                        mContext.startActivity(intent);
                        Common.onClickAnimation(mContext);*/

                    //test by vishwa 21/2/2019
                    Intent intent = new Intent(mContext, CurriculumPdfViewActivity.class);
                    intent.putExtra("Url", arrpath[selectedPosition]);
                    intent.putExtra("Name", messages[selectedPosition]);
                    mContext.startActivity(intent);
                    ((BaseActivity) mContext).onClickAnimation();

                    //}
                }
            } catch (Exception ex) {
                Constants.writelog("ExamListMarksheetActivity", "Exception 207:"
                        + ex.getMessage() + ":::::::" + ex.getStackTrace());
                mContext.findViewById(R.id.ProgressBar).setVisibility(View.GONE);
            }
            mContext.findViewById(R.id.ProgressBar).setVisibility(View.GONE);
        }

    }

    private void ShowDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View promptView = layoutInflater.inflate(R.layout.download_file_dialog, null);
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(promptView);
        mEmailid = (EditText) promptView.findViewById(R.id.editEmailid);
        final TextView mSendbtn = (TextView) promptView.findViewById(R.id.Sendbtn);
        final ImageView mClosebtn = (ImageView) promptView.findViewById(R.id.closebtn);
        progressBar = promptView.findViewById(R.id.progressBar);
        getEmail();
        mClosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alert.isShowing())
                    alert.dismiss();
            }
        });

        mSendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailid.getText().toString().trim();
                if (email.isEmpty()) {
                    Common.showToast(mContext, "Enter Emailid");
                } else if (!email.matches(emailPattern)) {
                    Common.showToast(mContext, "Enter valid Emailid");
                } else if (email != null && !email.isEmpty() && email.matches(emailPattern)) {
                    //chnage by vishwa  16/4/2019
                    allmsg = "";
                    allarrPath = "";
                    allitmId = "";

                    HashMap<String, ArrayList<String>> selecteditem = getCheckedItems();
                    if (selecteditem != null && selecteditem.size() > 0) {
                        ArrayList<String> msg = selecteditem.get("messages");
                        ArrayList<String> arrpath = selecteditem.get("arrpath");
                        ArrayList<String> itmid = selecteditem.get("itmid");

                        for (int i = 0; i < msg.size(); i++) {
                            allmsg += msg.get(i) + "@@@";
                            allarrPath += arrpath.get(i) + "@@@";
                            allitmId += itmid.get(i) + "@@@";
                        }
                        allmsg = allmsg.substring(0, allmsg.length() - 3);
                        allarrPath = allarrPath.substring(0, allarrPath.length() - 3);
                        allitmId = allitmId.substring(0, allitmId.length() - 3);
                        //Common.showLog("allitmId", allitmId);
                    }

                    //new call to service for multiple download
                    SendEmail(allarrPath, allitmId, allmsg, mEmailid.getText().toString().trim());


                    //OLD SERVICE
                    //SendEmail(arrpath[position], itmid[position], messages[position], mEmailid.getText().toString().trim());
                }
            }
        });
        alert = alertDialogBuilder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void getEmail() {
        try {
            if (((BaseActivity) mContext).isOnline()) {
                progressBar.setVisibility(View.VISIBLE);
                Call<SendEmailDownlodModel> call = ((MyApplication) mContext.getApplicationContext())
                        .getmRetrofitInterfaceAppService().GetStudentEmailid(Datastorage.GetSchoolId(mContext), Datastorage.GetStudentId(mContext), Integer.parseInt(Datastorage.GetCurrentYearId(mContext)));
                call.enqueue(new Callback<SendEmailDownlodModel>() {
                    @Override
                    public void onResponse(Call<SendEmailDownlodModel> call, Response<SendEmailDownlodModel> response) {
                        SendEmailDownlodModel tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1")) {
                            if (tmps.getStrResult() != null && !tmps.getStrResult().isEmpty()) {
                                mEmailid.setText(tmps.getStrResult());
                            }
                        } else {
                            if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty())
                                Common.showToast(mContext, tmps.getResponse());
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<SendEmailDownlodModel> call, Throwable t) {
                        Constants.writelog("ExamListAllAdapter1",
                                "GetEmail:" + t.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                });

            } else {
                Common.showToast(mContext, mContext.getString(R.string.msg_connection));
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void SendEmail(String doc, String CirId, String name, String email) {
        try {
            if (((BaseActivity) mContext).isOnline()) {
                progressBar.setVisibility(View.VISIBLE);
                Call<SendEmailDownlodModel> call = ((MyApplication) mContext.getApplicationContext())
                        .getmRetrofitInterfaceAppService()
                        .SendEmailid(Datastorage.GetStudentId(mContext), Datastorage.GetSchoolId(mContext), Integer.parseInt(Datastorage.GetCurrentYearId(mContext)), 3, email, doc, CirId, name, Constants.PLATFORM);
                call.enqueue(new Callback<SendEmailDownlodModel>() {
                    @Override
                    public void onResponse(Call<SendEmailDownlodModel> call, Response<SendEmailDownlodModel> response) {
                        SendEmailDownlodModel tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1")) {
                            if (tmps.getStrResult() != null && !tmps.getStrResult().isEmpty()) {
                                Common.showToast(mContext, tmps.getStrResult());
                                if (alert.isShowing())
                                    alert.dismiss();
                            }
                        } else {
                            if (tmps.getStrResult() != null && !tmps.getStrResult().isEmpty()) {
                                Common.showToast(mContext, tmps.getStrResult());
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<SendEmailDownlodModel> call, Throwable t) {
                        Constants.writelog("ExamListAllAdapter1",
                                "SendEmail:" + t.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                });
            } else {
                Common.showToast(mContext, mContext.getString(R.string.msg_connection));
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
            progressBar.setVisibility(View.GONE);
        }
    }
}



