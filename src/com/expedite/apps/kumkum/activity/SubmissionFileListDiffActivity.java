package com.expedite.apps.kumkum.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.BuildConfig;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.CustomProgressBar;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.common.ProgressItem;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.fragment.LeaveApprovedFragment;
import com.expedite.apps.kumkum.fragment.LeavePendingFragment;
import com.expedite.apps.kumkum.fragment.LeaveRejectFragment;
import com.expedite.apps.kumkum.fragment.SubmissionActiveFragment;
import com.expedite.apps.kumkum.fragment.SubmissionMissedFragment;
import com.expedite.apps.kumkum.fragment.SubmissionSubmittedFragment;
import com.expedite.apps.kumkum.model.CurriculumListModel;
import com.expedite.apps.kumkum.model.LeaveListModel;
import com.expedite.apps.kumkum.model.TempSubmissionFileListModel;
import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.expedite.apps.kumkum.fragment.SubmissionActiveFragment.mLeaveAdapterActive;
import static com.expedite.apps.kumkum.fragment.SubmissionMissedFragment.mLeaveAdapterMissed;
import static com.expedite.apps.kumkum.fragment.SubmissionSubmittedFragment.mLeaveAdapterSubmitted;

public class SubmissionFileListDiffActivity extends BaseActivity {
    public static ArrayList<CurriculumListModel.FileList> mActiveArrayList = new ArrayList<>();
    public static ArrayList<CurriculumListModel.FileList> mMissedArrayList = new ArrayList<>();
    public static ArrayList<CurriculumListModel.FileList> mSubmittedArrayList = new ArrayList<>();
    /*   public static ArrayList<TempSubmissionFileListModel> mActiveArrayListTemp = new ArrayList<>();
       public static ArrayList<TempSubmissionFileListModel> mSubmittedArrayListTemp = new ArrayList<>();
       public static ArrayList<TempSubmissionFileListModel> mMissedArrayListTemp = new ArrayList<>();*/
    private ArrayList<CurriculumListModel.FileList> mSubmissionArrayList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private TabLayout tabLayout;
    public static int newTabSelected = 0;
    ImageView gotoOld;
    SubmissionListPagerAdapter adapter;
    private ViewPager mViewPager;
    private String SchoolId = "", StudentId = "", Status = "0", mIsFromHome = "", YearId = "", ClassId = "", mFolderId = "0";
    AppBarLayout appBarLeave;
    LinearLayout empty_folder_lyt_file_detail;
    private AdView mAdView;
    BroadcastReceiver mReceiverFilter;
    private LocalBroadcastManager mBroadcastManager;
    Common common;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission_file_list_diff);
        init();
    }

    public void init() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Submission");
        }

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        common = new Common(this);
        mAdView = (AdView) findViewById(R.id.adView);
        showBannerAd(mAdView, ActivityNames.LeaveListActivity);
        showFullScreenAd(SubmissionFileListDiffActivity.this, ActivityNames.LeaveListActivity);
        SchoolId = Datastorage.GetSchoolId(SubmissionFileListDiffActivity.this);
        StudentId = Datastorage.GetStudentId(SubmissionFileListDiffActivity.this);
        ClassId = Datastorage.GetClassId(SubmissionFileListDiffActivity.this);
        YearId = Datastorage.GetCurrentYearId(SubmissionFileListDiffActivity.this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        empty_folder_lyt_file_detail = findViewById(R.id.empty_folder_lyt_file_detail);
        appBarLeave = findViewById(R.id.appBarLeave);
        gotoOld = findViewById(R.id.gotoOld);
        appBarLeave.setBackgroundColor(getResources().getColor(R.color.white));

        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getString("value") != null) {
            Status = getIntent().getExtras().getString("value");
        }
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getString("IsFromHome") != null) {
            mIsFromHome = getIntent().getExtras().getString("IsFromHome");
        }
        mBroadcastManager = ((MyApplication) getApplicationContext())
                .getLocalBroadcastInstance();
        mReceiverFilter = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
                getSubmissionFileList();
            }
        };

        gotoOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubmissionFileListDiffActivity.this, CurriculumClassroomActivity.class);
                intent.putExtra("IsFromHome", "IsFromHome");
                startActivity(intent);
                common.setSession(Constants.ShowSubNew, "0");
                SubmissionFileListDiffActivity.this.finish();
                onClickAnimation();
            }
        });
        getSubmissionFileList();
    }


    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager viewPager) {

        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            //    mLLShareDownloadAll.setVisibility(View.GONE);
                int tab_position = tabLayout.getSelectedTabPosition();
                Common.showLog("Selected Position::", "" + tab_position);
                newTabSelected = 0;
                if(tab_position==0){
                    if(mActiveArrayList!=null && mActiveArrayList.size()>0){
                        if(mLeaveAdapterActive!=null)
                        mLeaveAdapterActive.notifyDataSetChanged();
                    }
                }else if(tab_position==1){
                    if(mMissedArrayList!=null && mMissedArrayList.size()>0){
                        if(mLeaveAdapterMissed!=null)
                        mLeaveAdapterMissed.notifyDataSetChanged();
                    }
                }else if(tab_position==2){
                    if(mSubmittedArrayList!=null && mSubmittedArrayList.size()>0){
                        if(mLeaveAdapterSubmitted!=null)
                        mLeaveAdapterSubmitted.notifyDataSetChanged();
                    }
                }
             }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }

    private void getSubmissionFileList() {
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            Call<CurriculumListModel> call = ((MyApplication) getApplicationContext())
                    .getmRetrofitInterfaceAppService().GetCurriculumListClassroom_V2(SchoolId, ClassId, StudentId, YearId, Constants.PLATFORM);
            call.enqueue(new Callback<CurriculumListModel>() {
                @Override
                public void onResponse(Call<CurriculumListModel> call, Response<CurriculumListModel> response) {
                    try {
                        CurriculumListModel tmps = response.body();
                        if (tmps != null && tmps.getArray() != null && tmps.getArray().get(0).getResponse() != null
                                && !tmps.getArray().get(0).getResponse().isEmpty()
                                && tmps.getArray().get(0).getResponse().equalsIgnoreCase("1")) {

                            if (tmps.getArray().get(0).getFileList() != null && tmps.getArray().get(0).getFileList().size() > 0) {
                                mSubmissionArrayList.clear();
                                mSubmissionArrayList.addAll(tmps.getArray().get(0).getFileList());
                            }

                            if (mSubmissionArrayList != null && mSubmissionArrayList.size() > 0) {
                                mSubmittedArrayList.clear();
                                mActiveArrayList.clear();
                                mMissedArrayList.clear();

                                for (int i = 0; i < mSubmissionArrayList.size(); i++) {
                                    // 0 Pending,1 Approved, 2 reject,
                                    if (mSubmissionArrayList.get(i).getFileStatus().equalsIgnoreCase("0")) {
                                        mActiveArrayList.add(mSubmissionArrayList.get(i));
                                    }
                                    if (mSubmissionArrayList.get(i).getFileStatus().equalsIgnoreCase("1")) {
                                        mSubmittedArrayList.add(mSubmissionArrayList.get(i));
                                    }
                                    if (mSubmissionArrayList.get(i).getFileStatus().equalsIgnoreCase("2")) {
                                        mMissedArrayList.add(mSubmissionArrayList.get(i));
                                    }
                                }

                                 adapter = new SubmissionListPagerAdapter(getSupportFragmentManager());
                                adapter.addFragment(new SubmissionActiveFragment(), "Active (" + mActiveArrayList.size() + ")");
                                adapter.addFragment(new SubmissionMissedFragment(), "Missed (" + mMissedArrayList.size() + ")");
                                adapter.addFragment(new SubmissionSubmittedFragment(), "Submitted (" + mSubmittedArrayList.size() + ")");
                                mViewPager.setAdapter(adapter);
                                tabLayout = (TabLayout) findViewById(R.id.id_tabs);
                                tabLayout.setupWithViewPager(mViewPager);
                                tabLayout.setOnTabSelectedListener(onTabSelectedListener(mViewPager));
                                //    changeTabsFont();
                                if (Status != null && Status.equals("1")) {
                                    mViewPager.setCurrentItem(1, true);
                                } else if (Status != null && Status.equals("2")) {
                                    mViewPager.setCurrentItem(2, true);
                                } else {
                                    mViewPager.setCurrentItem(0, true);
                                }
                                ((View) findViewById(R.id.rlMainHistory)).setVisibility(View.VISIBLE);
                            } else {
                                ((View) findViewById(R.id.rlMainHistory)).setVisibility(View.GONE);
                            }
                        } else {
                            if (tmps != null && tmps.getArray().get(0).getMessage() != null && !tmps.getArray().get(0).getMessage().isEmpty())
                                Common.showToast(SubmissionFileListDiffActivity.this, tmps.getArray().get(0).getMessage());
                            ((View) findViewById(R.id.rlMainHistory)).setVisibility(View.GONE);
                        }
                        mProgressBar.setVisibility(View.GONE);
                    } catch (Exception ex) {
                        mProgressBar.setVisibility(View.GONE);
                        Constants.writelog("LeaveListActivity",
                                "getLeaveHistoryList 1877:" + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<CurriculumListModel> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    Constants.writelog("LeaveListActivity", "getLeaveHistoryList 1877:" + t.getMessage());
                }
            });
        } else {
            empty_folder_lyt_file_detail.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            Common.showToast(SubmissionFileListDiffActivity.this, getString(R.string.msg_connection));
        }
    }

    /*  private void getSubmissionFileList() {
          TempSubmissionFileListModel tmpSubFL=new TempSubmissionFileListModel("Squares and Cube","23 July","10:00","Maths");
          mMissedArrayListTemp.add(tmpSubFL);
           tmpSubFL=new TempSubmissionFileListModel("Solar Eclipse","22 August","09:00","Science");
          mActiveArrayListTemp.add(tmpSubFL);
           tmpSubFL=new TempSubmissionFileListModel("Types of Tense","20 January","02:50","English");
          mSubmittedArrayListTemp.add(tmpSubFL);
          mViewPager = (ViewPager) findViewById(R.id.viewpager);
          SubmissionListPagerAdapter adapter = new SubmissionListPagerAdapter(getSupportFragmentManager());
          adapter.addFragment(new SubmissionActiveFragment(), "Active (" + mActiveArrayListTemp.size() + ")");
          adapter.addFragment(new SubmissionMissedFragment(), "Missed (" + mMissedArrayListTemp.size() + ")");
          adapter.addFragment(new SubmissionSubmittedFragment(), "Submitted (" + mSubmittedArrayListTemp.size() + ")");
          mViewPager.setAdapter(adapter);
          tabLayout = (TabLayout) findViewById(R.id.id_tabs);
          tabLayout.setupWithViewPager(mViewPager);
        //  changeTabsFont();
          if (Status != null && Status.equals("1")) {
              mViewPager.setCurrentItem(1, true);
          } else if (Status != null && Status.equals("2")) {
              mViewPager.setCurrentItem(2, true);
          } else {
              mViewPager.setCurrentItem(0, true);
          }
          ((View) findViewById(R.id.rlMainHistory)).setVisibility(View.VISIBLE);
      }*/
    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        vg.setPadding(0, 0, 0, 10);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            if (j == 0) {
                vgTab.setBackgroundColor(getResources().getColor(R.color.calendar_homwwork_bg));
            }
            if (j == 1) {
                vgTab.setBackgroundColor(getResources().getColor(R.color.txt_green_bg));
            }
            if (j == 2) {
                vgTab.setBackgroundColor(getResources().getColor(R.color.txt_red_bg));
            }
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setAllCaps(false);
                    ((TextView) tabViewChild).setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiverFilter != null)
            mBroadcastManager.unregisterReceiver(mReceiverFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBroadcastManager.registerReceiver(mReceiverFilter, new IntentFilter(
                getResources().getString(R.string.broadcast_submission_response_submitted)));

    }

    private class SubmissionListPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTitleNames = new ArrayList();

        private SubmissionListPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleNames.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleNames.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SubmissionFileListDiffActivity.this.finish();
                onBackClickAnimation();
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mIsFromHome != null && !mIsFromHome.isEmpty()) {
            super.onBackPressed();
            SubmissionFileListDiffActivity.this.finish();
            onBackClickAnimation();
        } else {
            Intent intent = new Intent(SubmissionFileListDiffActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            super.onBackPressed();
        }
    }

    /*  public class SubmissionFileListAdapter extends RecyclerView.Adapter {
          private List<CurriculumListModel.FileList> mList = new ArrayList<>();
          private Activity mContext;
          private LayoutInflater mLayoutInflater;
          private String Tag = "LeaveListAdapter";

          private ProgressItem mProgressItem;
          public SubmissionFileListAdapter(Activity context, List<CurriculumListModel.FileList> ItemList) {
              mList = ItemList;
              mContext = context;
              mLayoutInflater = LayoutInflater.from(mContext);

          }

          @Override
          public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
              View view = mLayoutInflater.inflate(R.layout.row_tab_submission_file_layout, parent, false);
              return new CustomViewHolder(view);
          }

          @Override
          public int getItemViewType(int position) {
              return position;
          }

          @Override
          public int getItemCount() {
              return mList.size();
          }

          @Override
          public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
              try {
                  if (holder instanceof CustomViewHolder) {
                      final CurriculumListModel.FileList tmp = mList.get(position);
                      ((CustomViewHolder) holder).fileName.setText(tmp.getName());
                      if(tmp.getDueDate()!=null && !tmp.getDueDate().isEmpty()) {
                          String[] splitDate=tmp.getDueDate().split(" ");
                          ((CustomViewHolder) holder).dateTxt.setText(Html.fromHtml("<B>"+splitDate[0]+"</B>"));
                          ((CustomViewHolder) holder).dateTxt.setVisibility(View.VISIBLE);
                          if(splitDate!=null && splitDate.length>1) {
                              ((CustomViewHolder) holder).timeTxt.setText(Html.fromHtml("<B>"+splitDate[1]+ " "+splitDate[2]+"</B>"));
                              ((CustomViewHolder) holder).timeTxt.setVisibility(View.VISIBLE);
                          }else{
                              ((CustomViewHolder) holder).timeTxt.setVisibility(View.GONE);
                          }
                      }else{
                          ((CustomViewHolder) holder).timeTxt.setVisibility(View.GONE);
                          ((CustomViewHolder) holder).dateTxt.setVisibility(View.GONE);
                      }
                      if(tmp.getInstructions()!=null && !tmp.getInstructions().isEmpty()) {
                          ((CustomViewHolder) holder).fileIns.setVisibility(View.VISIBLE);
                          ((CustomViewHolder) holder).fileIns.setText(tmp.getInstructions());
                      }else{
                          ((CustomViewHolder) holder).fileIns.setVisibility(View.GONE);
                      }
                      if (tmp.getType() != null && !tmp.getType().isEmpty()) {
                          if (tmp.getType().equals("1")) {
                              ((CustomViewHolder) holder).imgfile.setImageDrawable(mContext.getResources().getDrawable(R.drawable.pdficon));
                              ((CustomViewHolder) holder).mImgDownload.setVisibility(View.VISIBLE);
                              ((CustomViewHolder) holder).shareLyt.setVisibility(View.VISIBLE);
                          } else if (tmp.getType().equals("2")) {
                              ((CustomViewHolder) holder).imgfile.setImageDrawable(mContext.getResources().getDrawable(R.drawable.imageicon));
                              ((CustomViewHolder) holder).mImgDownload.setVisibility(View.VISIBLE);
                              ((CustomViewHolder) holder).shareLyt.setVisibility(View.VISIBLE);
                          } else if (tmp.getType().equals("3")) {
                              ((CustomViewHolder) holder).imgfile.setImageDrawable(mContext.getResources().getDrawable(R.drawable.audioicon));
                          } else if (tmp.getType().equals("4")) {
                              ((CustomViewHolder) holder).imgfile.setImageDrawable(mContext.getResources().getDrawable(R.drawable.videoicon));
                              ((CustomViewHolder) holder).mImgDownload.setVisibility(View.GONE);
                              ((CustomViewHolder) holder).shareLyt.setVisibility(View.VISIBLE);
                          } else {
                              ((CustomViewHolder) holder).imgfile.setImageDrawable(mContext.getResources().getDrawable(R.drawable.file1));
                              ((CustomViewHolder) holder).mImgDownload.setVisibility(View.GONE);
                              ((CustomViewHolder) holder).shareLyt.setVisibility(View.GONE);
                          }
                      }
                      ((CustomViewHolder) holder).mImgComplete.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              try {
                                  Intent intent = new Intent(mContext, CurriculumResponseClassroomActivity.class);
                                  intent.putExtra("Curriculam", tmp.getName());
                                  intent.putExtra("CurriculamDueDate", tmp.getDueDate());
                                  intent.putExtra("CurriculamId", tmp.getId());
                                  mContext.startActivity(intent);
                                  ((BaseActivity)mContext).onClickAnimation();

                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                          }
                      });

                      ((CustomViewHolder) holder).shareLyt.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                              sharingIntent.setType("text/plain");
                              sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                              sharingIntent.putExtra(Intent.EXTRA_TEXT, tmp.getName() + " : " + tmp.getUrl());
                              mContext.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
                          }
                      });

                      ((CustomViewHolder) holder).mImgDownload.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              try {
                                  if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                          == PackageManager.PERMISSION_DENIED) {
                                      checkpermissionstatus(2);
                                  } else {
                                      if (tmp.getType().equals("3")) {
                                          new SaveAudioTask().execute(tmp.getUrl(), tmp.getName());
                                      } else if (tmp.getType().equals("1") ||
                                              tmp.getType().equals("2")) {
                                          DownloadPdfOrImageFile(mContext, tmp.getName(),
                                                  tmp.getUrl());
                                      } else if (tmp.getType().equals("4")) {
                                          new SaveVideoTask().execute(tmp.getUrl(), tmp.getName());
                                      }
                                  }
                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                          }
                      });

                      if(tmp.getInstructions()!=null && !tmp.getInstructions().isEmpty()) {
                          ((CustomViewHolder) holder).subName.setVisibility(View.VISIBLE);
                          ((CustomViewHolder) holder).subName.setText(tmp.getFolderName());
                      }else{
                          ((CustomViewHolder) holder).subName.setVisibility(View.GONE);
                      }
                      ((CustomViewHolder) holder).progressSeek.getThumb().mutate().setAlpha(0);
                      ArrayList progressItemList = new ArrayList();
                      // red span
                      mProgressItem = new ProgressItem();
                      mProgressItem.progressItemPercentage = 20;
                      Log.i("Mainactivity", mProgressItem.progressItemPercentage + "");
                      mProgressItem.color = R.color.navy;
                      progressItemList.add(mProgressItem);
                      int progress=100-20;
                      mProgressItem = new ProgressItem();
                      mProgressItem.progressItemPercentage = progress;
                      Log.i("Mainactivity", mProgressItem.progressItemPercentage + "");
                      mProgressItem.color = R.color.materialLightGreen;
                      progressItemList.add(mProgressItem);
                      ((CustomViewHolder) holder).progressSeek.initData(progressItemList);
                      ((CustomViewHolder) holder).progressSeek.invalidate();
                  }
              } catch (Exception ex) {
                  Constants.writelog(Tag, "Ex 84:" + ex.getMessage());
              }
          }

          class CustomViewHolder extends RecyclerView.ViewHolder {
              private TextView fileName, timeTxt, dateTxt, subName,fileIns;
              CustomProgressBar progressSeek;
              RelativeLayout imgLyt,shareLyt, mImgDownload, mImgComplete;
              ImageView imgfile;
              public CustomViewHolder(View view) {
                  super(view);
                  fileName = (TextView) view.findViewById(R.id.fileName);
                  timeTxt = (TextView) view.findViewById(R.id.timeTxt);
                  dateTxt = (TextView) view.findViewById(R.id.dateTxt);
                  subName = (TextView) view.findViewById(R.id.subName);
                  fileIns = (TextView) view.findViewById(R.id.fileIns);
                  mImgDownload = itemView.findViewById(R.id.imgDownload);
                  mImgComplete =  itemView.findViewById(R.id.imgComplete);
                  shareLyt =  view.findViewById(R.id.shareLyt);
                  progressSeek = (CustomProgressBar) view.findViewById(R.id.progressSeek);
                  imgLyt =  view.findViewById(R.id.imgLyt);
                  imgfile =  view.findViewById(R.id.imgfile);
              }
          }
      }*/
    public void checkpermissionstatus(int i) {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                Boolean isanyDeny = false;
                Boolean isadded = false;
                String permissions = "";
                int permissionStatus = 0;
                if (i == 2) {
                    permissionStatus = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                        isanyDeny = true;
                        isadded = true;
                        if (isadded) {
                            permissions += "," + Manifest.permission.WRITE_EXTERNAL_STORAGE;
                        } else {
                            permissions += Manifest.permission.WRITE_EXTERNAL_STORAGE;
                        }
                    }
                    String[] perm = permissions.split(",");
                    ActivityCompat.requestPermissions(SubmissionFileListDiffActivity.this, perm, 1);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:

            case 2:
                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(SubmissionFileListDiffActivity.this, "Permission given Now you can download the files...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SubmissionFileListDiffActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}