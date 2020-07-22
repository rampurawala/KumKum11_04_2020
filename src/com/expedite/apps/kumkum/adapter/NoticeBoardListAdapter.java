package com.expedite.apps.kumkum.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.NoticeBoardActivity;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.SendEmailDownlodModel;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.expedite.apps.kumkum.activity.NoticeBoardActivity.checkBoxselectall;
import static com.expedite.apps.kumkum.activity.NoticeBoardActivity.dowmloadbtn;
import static com.expedite.apps.kumkum.activity.NoticeBoardActivity.mBtnEmail;
import static com.expedite.apps.kumkum.activity.NoticeBoardActivity.mBtnShare;
import static com.expedite.apps.kumkum.activity.NoticeBoardActivity.mLLBtnLayout;


public class NoticeBoardListAdapter extends BaseAdapter {
    private Context context;
    private String[] names, date, groupname, doc, CirId;
    private AlertDialog alert;
    private String SchoolId, StudentId, Year_Id;
    public DatabaseHandler db = null;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressBar progressBar;
    private EditText mEmailid;
    private String alldoc = "", allName = "", allCirId = "";
    private SparseBooleanArray mSparseBooleanArray;
    String A = "";
    String Ext = "";

    public NoticeBoardListAdapter(Context context, String[] names, String[] date, String[] groupname, String[] doc, String[] CirId) {
        this.context = context;
        this.names = names;
        this.date = date;
        this.doc = doc;
        this.CirId = CirId;
        this.groupname = groupname;
        mSparseBooleanArray = new SparseBooleanArray();
    }

//    public NoticeBoardListAdapter(Context context, String[] names, String[] date) {
//        this.context = context;
//        this.names = names;
//        this.date = date;
//    }

    //change by vishwa 16/4/2019
    public HashMap<String, ArrayList<String>> getCheckedItems() {
        HashMap<String, ArrayList<String>> multiMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> mNameArry = new ArrayList<String>();
        ArrayList<String> mDocArry = new ArrayList<String>();
        ArrayList<String> mCirArry = new ArrayList<String>();
        for (int i = 0; i < names.length; i++) {
            if (mSparseBooleanArray.get(i)) {
                mNameArry.add(names[i]);
                mDocArry.add(doc[i]);
                mCirArry.add((CirId[i]));
            }
        }
        multiMap.put("Name", mNameArry);
        multiMap.put("Doc", mDocArry);
        multiMap.put("Cirid", mCirArry);
        return multiMap;
    }

    public int getCount() {
        return names.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public String getItenName(int position) {
        return names[position];
    }

    public View getView(final int position, View arg1, ViewGroup arg2) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_item_noticeboard, arg2, false);
        final TextView txtCircularName = (TextView) view.findViewById(R.id.txtCircularName);
        TextView txtDate = (TextView) view.findViewById(R.id.txtDate);
        TextView txtGroupName = (TextView) view.findViewById(R.id.txtGroupName);
        View Mainview = (View) view.findViewById(R.id.ll_MainView);

        Mainview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String cirname = names[position];
                /*A=txtCircularName.getText().toString();
                String[] parts = A.split(":");
                Ext = parts[1];*/
                intent.putExtra("Position", position);

                intent.putExtra("Name", cirname);
                intent.setAction("ViewPDF");
                context.sendBroadcast(intent);
            }
        });

        // change by vishwa
        final CheckBox mCheckbox = (CheckBox) view.findViewById(R.id.checkBox);
        mCheckbox.setTag(position);
        mCheckbox.setChecked(mSparseBooleanArray.get(position));

        db = new DatabaseHandler(context);
        SchoolId = Datastorage.GetSchoolId(context);
        StudentId = Datastorage.GetStudentId(context);
        String defacnt = db.GetDefaultAcademicYearAccount(Integer.parseInt(SchoolId), Integer.parseInt(StudentId));
        if (defacnt != null && defacnt.length() > 0) {
            String[] splterstr = defacnt.split(",");
            Year_Id = splterstr[1];
        } else {
            Year_Id = Datastorage.GetCurrentYearId(context);
        }
        ImageView imgDownloadbtn = (ImageView) view.findViewById(R.id.imgDownload);
        try {
            if (names[position] != null && !names[position].isEmpty()) {
                txtCircularName.setText(Html.fromHtml("Circular Name:" + names[position]));


                //vishwa  patel on 21/12/18
               /* imgDownloadbtn.setVisibility(View.GONE);
                imgDownloadbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowDialog(position, "");
                        //getEmail(position);
                    }
                });*/

                //chnge by vishwa
                checkBoxselectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            mSparseBooleanArray.clear();
                            dowmloadbtn.setVisibility(View.VISIBLE);
                            mLLBtnLayout.setVisibility(View.VISIBLE);
                            for (int i = 0; i < names.length; i++) {
                                mSparseBooleanArray.put(i, isChecked);
                                mCheckbox.setChecked(mSparseBooleanArray.get(i));
                            }
                        } else {
                            if (mSparseBooleanArray.size() == names.length) {
                                dowmloadbtn.setVisibility(View.GONE);
                                mLLBtnLayout.setVisibility(View.GONE);
                                for (int i = 0; i < names.length; i++) {
                                    mSparseBooleanArray.delete(i);
                                }
                            }
                        }
                        if (mSparseBooleanArray.size() == names.length) {
                            checkBoxselectall.setChecked(true);
                        } else {
                            checkBoxselectall.setChecked(false);
                        }
                        notifyDataSetChanged();
                    }
                });

                //vishwa patel 15/04/2019
                mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            dowmloadbtn.setVisibility(View.VISIBLE);
                            mLLBtnLayout.setVisibility(View.VISIBLE);
                            mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
                        } else {
                            mSparseBooleanArray.delete((Integer) buttonView.getTag());
                        }

                        if (mSparseBooleanArray.size() == 0) {
                            dowmloadbtn.setVisibility(View.GONE);
                            mLLBtnLayout.setVisibility(View.GONE);
                        }

                        if (mSparseBooleanArray.size() == names.length) {
                            checkBoxselectall.setChecked(true);
                        } else {
                            checkBoxselectall.setChecked(false);
                        }
                    }
                });

                //Changes by Vijay Solanki 28-03-2020
                if (dowmloadbtn.getVisibility() == View.VISIBLE) {
                    dowmloadbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_DENIED) {
                                ((NoticeBoardActivity) context).checkpermissionstatus(2);
                            } else {
                                allName = "";
                                alldoc = "";
                                allCirId = "";
                                HashMap<String, ArrayList<String>> selecteditem = getCheckedItems();
                                if (selecteditem != null && selecteditem.size() > 0) {
                                    ArrayList<String> doc = selecteditem.get("Doc");
                                    ArrayList<String> Cirid = selecteditem.get("Cirid");
                                    ArrayList<String> name = selecteditem.get("Name");

                                    String data = "";
                                    for (int i = 0; i < Cirid.size(); i++) {
                                        data += "Title : " + name.get(i) + "Link : " + doc.get(i) + "\n";

                                        ((BaseActivity) context).DownloadPdfOrImageFile(context, name.get(i), doc.get(i));
                                   /* if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                            == PackageManager.PERMISSION_DENIED) {
                                        ((BaseActivity) context).checkpermissionstatus(2, context);
                                    } else {
                                        String path = Environment.getExternalStorageDirectory()
                                                + File.separator
                                                + SchoolDetails.PhotoGalleryFolder + File.separator + "PDF/" + name.get(i) + ".PDF";
                                        Common.showLog("Path", path);
                                        File file = new File(path);
                                        *//*if (file.isFile()) {
                                            Common.showToast(context, "Already Downloaded");
                                        } else {*//*
                                        try {
                                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(doc.get(i)));
                                            request.setDescription("PDF Downloading");
                                            request.setTitle(name.get(i));
                                            request.setShowRunningNotification(true);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                                request.allowScanningByMediaScanner();
                                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                            }
                                            request.setDestinationInExternalPublicDir(File.separator + SchoolDetails.PhotoGalleryFolder
                                                    + File.separator, "PDF/" + name.get(i) + ".PDF");
                                            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                                            manager.enqueue(request);
                                            Common.showToast(context, "Pdf Downloading Start");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        // }

                                    }*/

                                    }
                                }
                            }
                        }
                    });

                    mBtnEmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShowDialog(position, "");
                        }
                    });

                    mBtnShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            allName = "";
                            alldoc = "";
                            allCirId = "";
                            HashMap<String, ArrayList<String>> selecteditem = getCheckedItems();
                            if (selecteditem != null && selecteditem.size() > 0) {
                                ArrayList<String> doc = selecteditem.get("Doc");
                                ArrayList<String> Cirid = selecteditem.get("Cirid");
                                ArrayList<String> name = selecteditem.get("Name");

                                String data = "";
                                for (int i = 0; i < Cirid.size(); i++) {
                                    data += "Title : " + name.get(i) + "\n" + " Link : " + doc.get(i) + "\n";

                                }
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                                sharingIntent.putExtra(Intent.EXTRA_TEXT, data);
                                context.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
                                // Log.e("String data :", data);
                            }
                        }
                    });
                }
            }
            if (groupname[position] != null && !groupname[position].isEmpty()) {
                txtGroupName.setVisibility(View.VISIBLE);
                txtGroupName.setText(Html.fromHtml("(" + groupname[position] + ")"));
            } else {
                txtGroupName.setVisibility(View.GONE);
            }

            if (date[position] != null && !date[position].isEmpty()) {
                txtDate.setVisibility(View.VISIBLE);
                txtDate.setText(((BaseActivity) context).convertDate_v1(context, date[position]));
            } else {
                txtDate.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        return view;
    }


    private void getEmail() {
        try {
            if (((BaseActivity) context).isOnline()) {
                progressBar.setVisibility(View.VISIBLE);
                Call<SendEmailDownlodModel> call = ((MyApplication) context.getApplicationContext())
                        .getmRetrofitInterfaceAppService().GetStudentEmailid(SchoolId, StudentId, Integer.parseInt(Year_Id));
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
                                Common.showToast(context, tmps.getResponse());
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<SendEmailDownlodModel> call, Throwable t) {
                        Constants.writelog("NoticeBoardAdpter",
                                "GetEmail:" + t.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                });

            } else {
                Common.showToast(context, context.getString(R.string.msg_connection));
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void ShowDialog(final int position, String email) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.download_file_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptView);
        mEmailid = (EditText) promptView.findViewById(R.id.editEmailid);
        final TextView mSendbtn = (TextView) promptView.findViewById(R.id.Sendbtn);
        final ImageView mClosebtn = (ImageView) promptView.findViewById(R.id.closebtn);
        progressBar = promptView.findViewById(R.id.progressBar);
        getEmail();
        /*if (email != null && !email.isEmpty()) {
            mEmailid.setText(email);
        }else {
            mEmailid.setText("");
        }
        */
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
                try {
                    String email = mEmailid.getText().toString().trim();
                    if (email.isEmpty()) {
                        Common.showToast(context, "Enter Emailid");
                    } else if (!email.matches(emailPattern)) {
                        Common.showToast(context, "Enter valid Emailid");
                    } else if (email != null && !email.isEmpty() && email.matches(emailPattern)) {
                        //chnage by vishwa  16/4/2019
                        allName = "";
                        alldoc = "";
                        allCirId = "";
                        HashMap<String, ArrayList<String>> selecteditem = getCheckedItems();
                        if (selecteditem != null && selecteditem.size() > 0) {
                            ArrayList<String> doc = selecteditem.get("Doc");
                            ArrayList<String> Cirid = selecteditem.get("Cirid");
                            ArrayList<String> name = selecteditem.get("Name");

                            for (int i = 0; i < Cirid.size(); i++) {
                                allCirId += Cirid.get(i) + "@@@";
                                allName += name.get(i) + "@@@";
                                alldoc += doc.get(i) + "@@@";
                            }

                            allCirId = allCirId.substring(0, allCirId.length() - 3);
                            allName = allName.substring(0, allName.length() - 3);
                            alldoc = alldoc.substring(0, alldoc.length() - 3);

                            // Common.showLog("alldoc", alldoc);
                        }
                        //New service for  downloading  multiple document
                        allName = allName.replaceAll("![()];:&=+$,/?%#", " ");
                        allName = allName.replaceAll("   ", " ");
                        allName = allName.replaceAll("  ", " ");
                        allName = allName.replaceAll(" ", "_");
                        SendEmail(alldoc, allCirId, allName, mEmailid.getText().toString().trim());
                        //for download single document
                        // SendEmail(doc[position], CirId[position], names[position], mEmailid.getText().toString().trim());
                    }
                } catch (Exception ex) {
                    Constants.writelog("NoticeBoardAdpter",
                            "OnmSendbtn_click331:" + ex.getMessage());
                }
            }
        });
        alert = alertDialogBuilder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void SendEmail(String doc, String CirId, String name, String email) {
        try {
            if (((BaseActivity) context).isOnline()) {
                progressBar.setVisibility(View.VISIBLE);
                Call<SendEmailDownlodModel> call = ((MyApplication) context.getApplicationContext())
                        .getmRetrofitInterfaceAppService()
                        .SendEmailid(StudentId, SchoolId, Integer.parseInt(Year_Id), 1, email, doc, CirId, name, Constants.PLATFORM);
                call.enqueue(new Callback<SendEmailDownlodModel>() {
                    @Override
                    public void onResponse(Call<SendEmailDownlodModel> call, Response<SendEmailDownlodModel> response) {
                        try {
                            SendEmailDownlodModel tmps = response.body();
                            if (tmps != null && !tmps.getResponse().isEmpty() && tmps.getResponse().equals("1")) {
                                if (tmps.getStrResult() != null && !tmps.getStrResult().isEmpty()) {
                                    Common.showToast(context, tmps.getStrResult());
                                    if (alert.isShowing())
                                        alert.dismiss();
                                }
                            } else {
                                if (tmps.getStrResult() != null && !tmps.getStrResult().isEmpty()) {
                                    Common.showToast(context, tmps.getStrResult());
                                }
                            }
                        } catch (Exception ex) {
                            Constants.writelog("NoticeBoardAdpter",
                                    "On SendEmail 369:" + ex.getMessage());
                        } finally {
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<SendEmailDownlodModel> call, Throwable t) {
                        Constants.writelog("NoticeBoardAdpter",
                                "SendEmail:" + t.getMessage());
                    }
                });
            } else {
                Common.showToast(context, context.getString(R.string.msg_connection));
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    /*private class MyTaskliarclick extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                String selectedgroup = "";
                try {
                    selectedgroup = ddldata[position];
                } catch (Exception ex) {
                    Constants.Logwrite("NoticeBoardActivity", "Exception 825" + ex.getMessage()
                            + ":::::" + ex.getStackTrace());
                }
                if (selectedgroup.equals("") || selectedgroup.equals("All")
                        || Itmpathsorted == null || Itmpathsorted.length < 1) {
                    String path = "";
                    path = Itmpath[position];
                    File myDir = Constants.CreatePhotoGalleryFolder();
                    File file = new File(myDir + "/" + path);
                    //Uri uri1 = Uri.fromFile(file);
                    //PackageManager packageManager = getPackageManager();
                    try {
                        if (file != null && file.exists() && file.length() > 1) {
                            *//*Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                            intentUrl.setDataAndType(uri1, "application/pdf");
                            List list = packageManager.queryIntentActivities(intentUrl,
                                    PackageManager.MATCH_DEFAULT_ONLY);
                            if (list.size() > 0 && file.isFile()) {
                                intentUrl.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intentUrl);
                            } else {*//*
                            if (isOnline()) {
                                Intent intent = new Intent(context, StudentCircularActivity.class);
                                intent.putExtra("CircularPath", path);
                                intent.putExtra("filename", path);
                                context.startActivity(intent);
                                finish();
                                onClickAnimation();
                            } else {
                                isShowInternetMsg = true;
                            }
                        } else {
                            if (isOnline()) {
                                String fileURL = Constants.circularUrl + path;
                                Constants.SavePdf(NoticeBoardActivity.this, fileURL, path);
                                if (file.exists()) {
                                    *//*Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                                    intentUrl.setDataAndType(uri1, "application/pdf");
                                    List list = packageManager.queryIntentActivities(intentUrl,
                                            PackageManager.MATCH_DEFAULT_ONLY);
                                    if (list.size() > 0 && file.isFile()) {
                                        intentUrl.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        startActivity(intentUrl);
                                        finish();
                                    } else {*//*
                                    Intent intent = new Intent(NoticeBoardActivity.this, StudentCircularActivity.class);
                                    intent.putExtra("CircularPath", path);
                                    startActivity(intent);
                                    finish();
                                    onClickAnimation();
                                    //}
                                } else {
                                    Intent intent = new Intent(NoticeBoardActivity.this, StudentCircularActivity.class);
                                    intent.putExtra("CircularPath", path);
                                    startActivity(intent);
                                    finish();
                                    onClickAnimation();
                                }
                            } else {
                                isShowInternetMsg = true;
                            }
                        }
                    } catch (ActivityNotFoundException e) {
                        Constants.writelog("Viwstudentcircular", "Exception 742" + e.getMessage() + ":::::" + e.getStackTrace());

                        Intent intent = new Intent(NoticeBoardActivity.this, StudentCircularActivity.class);
                        intent.putExtra("CircularPath", path);
                        context.startActivity(intent);
                        finish();
                        onClickAnimation();
                    } catch (Exception e) {
                        Constants.writelog("Viewstudentcircular", "Exception 757" + e.getMessage() + ":::::" + e.getStackTrace());
                        Intent intent = new Intent(NoticeBoardActivity.this, StudentCircularActivity.class);
                        intent.putExtra("CircularPath", path);
                        startActivity(intent);
                        finish();
                        onClickAnimation();
                    }
                } else {
                    try {
                        File myDir = Constants.CreatePhotoGalleryFolder();
                        File file = new File(myDir + "/" + Itmpathsorted[position]);
                        Uri uri1 = Uri.fromFile(file);

                        if (file.exists()) {
                            Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                            intentUrl.setDataAndType(uri1, "application/pdf");
                            intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intentUrl);
                            finish();
                        } else {
                            String fileURL = "http://" + Constants.serverUrl + "/CircularForStudent/" + Itmpathsorted[position];
                            Constants.SavePdf(context, fileURL, Itmpathsorted[position]);
                            if (file.exists()) {
                                Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                                intentUrl.setDataAndType(uri1, "application/pdf");
                                intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intentUrl);
                                finish();
                            } else {
                                if (isOnline()) {
                                    Intent intent = new Intent(context, StudentCircularActivity.class);
                                    intent.putExtra("CircularPath", Itmpathsorted[position]);
                                    context.startActivity(intent);
                                    onClickAnimation();
                                } else {
                                    isShowInternetMsg = true;
                                }
                            }
                        }
                    } catch (ActivityNotFoundException e) {
                        Constants.Logwrite("Viwstudentcircular", "Exception" + e.getMessage() + ":::::" + e.getStackTrace());
                        Intent intent = new Intent(context, StudentCircularActivity.class);
                        intent.putExtra("CircularPath", Itmpathsorted[position]);
                        context.startActivity(intent);
                        context.finish();
                        ((BaseActivity)context).onClickAnimation();
                    }
                }
            } catch (Exception err) {
                Constants.writelog("noticeboard", "Exception:811" + err.getMessage() + ":::::" + err.getStackTrace());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            if (isShowInternetMsg) {
                Toast.makeText(context, SchoolDetails.MsgNoInternet, Toast.LENGTH_LONG).show();
            }
        }
    }*/
}
