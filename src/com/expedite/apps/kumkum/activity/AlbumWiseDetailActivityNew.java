package com.expedite.apps.kumkum.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.adapter.AlbumListAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.Contact;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Jaydeep on 06-09-2016.
 */
public class AlbumWiseDetailActivityNew extends BaseActivity {
    private RecyclerView mRecyclerView;
    private DatabaseHandler db = null;
    private String SchoolId, StudentId, URL, PhotoFileName;
    private String[] FilePathStrings, TotalItemList = null, albumlist = null, albumURL = null,
            flexst = null, albumtime = null;
    private Integer[] albumid = null;
    String albumName="";
    private File file, fileexist;
    private AlbumListAdapter mAdapter;
    private int i, mAlbumId;
    private ProgressBar mProgressBar;
    private ImageView mFullImg;
    private LinearLayoutManager mLayoutManager;
    private int overallXScrol = 0;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albumwisedetail_new);
        init();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.AlbumWiseDetailActivityNew);

            if (getIntent() != null && getIntent().getExtras() != null) {
                mAlbumId = Integer.parseInt(getIntent().getStringExtra("ALbUMID"));
                albumName = getIntent().getStringExtra("ALbUMName");
            }
            if(albumName==null || albumName.isEmpty())
                albumName="Photo Gallery";
            Constants.setActionbar(getSupportActionBar(), AlbumWiseDetailActivityNew.this, getApplicationContext(),
                    albumName, "Photo Gallery",ActivityNames.AlbumWiseDetailActivityNew);
            SchoolId = Datastorage.GetSchoolId(getApplicationContext());
            StudentId = Datastorage.GetStudentId(getApplicationContext());
            db = new DatabaseHandler(AlbumWiseDetailActivityNew.this);
            file = Constants.CreatePhotoGalleryFolder();
            mFullImg = (ImageView) findViewById(R.id.fullImg);
            mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);

            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

//            int resId = R.anim.layout_animation_fall_down;
//            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(AlbumWiseDetailActivityNew.this, resId);
//            mRecyclerView.setLayoutAnimation(animation);

            mLayoutManager = new LinearLayoutManager(AlbumWiseDetailActivityNew.this, LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);

            //change by vishwa on 10/6/2019
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    try {
                        int CompleteVisiblePosition;
                        Bitmap bmp;
                        CompleteVisiblePosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                        if (FilePathStrings != null && FilePathStrings.length > 0) {
                            bmp = null;
                            String[] splterstr = FilePathStrings[CompleteVisiblePosition].split("@@@###");
                            String filepath = splterstr[0].toString();
                            try {
                                bmp = BitmapFactory.decodeFile(filepath);
                            } catch (Exception ex) {
                                Constants.writelog("selectAlbumAdapter", "onBindviewHolder()135 MSG:" + ex.getMessage());
                            }
                            if (bmp == null) {
                                mFullImg.setImageResource(R.drawable.nopics);
                            } else {
                                mFullImg.setImageBitmap(bmp);
                            }
                        } else {
                            if (albumURL != null && albumURL.length > 0) {
                                Glide.with(AlbumWiseDetailActivityNew.this).load(albumURL[CompleteVisiblePosition])
                                        .asBitmap().
                                        placeholder(R.drawable.placeholder).error(R.drawable.nopics).
                                        into(new BitmapImageViewTarget(mFullImg));
                            }
                        }
                    } catch (Exception e) {
                        Common.printStackTrace(e);
                    }
                }
            });
            new getPhotosList().execute();
        } catch (Exception ex) {
            Constants.Logwrite("AlbumWiseDetailActivity", "on_create()552 MSG:" + ex.getMessage());
            ex.printStackTrace();
        }
    }


    public void SaveImage(Bitmap finalBitmap, String fileneme) {

        File myDir = Constants.CreatePhotoGalleryFolder();
        String fname = fileneme;
        File file = new File(myDir, fname);
        if (file.exists()) {
            file.delete();
        }
        String extension = "";
        int i = fname.lastIndexOf('.');
        int p = Math.max(fname.lastIndexOf('/'), fname.lastIndexOf('\\'));

        if (i > p) {
            extension = fname.substring(i + 1);
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (extension.equalsIgnoreCase("jpeg")) {
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } else if (extension.equalsIgnoreCase("png")) {
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } else {
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            Constants.writelog("PhotoGalleryActivity", "SaveImage()152 MSG:" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackClickAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    hideKeyboard(AlbumWiseDetailActivityNew.this);
                    AlbumWiseDetailActivityNew.this.finish();
                    onBackClickAnimation();
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        return super.onOptionsItemSelected(item);
    }

    private class getPhotosList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                List<Contact> contacts1 = db.getAlbumwiseDetails(
                        Integer.parseInt(StudentId),
                        Integer.parseInt(SchoolId), Integer.parseInt(String.valueOf(mAlbumId)));
                if (contacts1.size() > 0) {
                    TotalItemList = new String[contacts1.size()];
                    albumlist = new String[contacts1.size()];
                    albumURL = new String[contacts1.size()];
                    flexst = new String[contacts1.size()];
                    albumtime = new String[contacts1.size()];
                    albumid = new Integer[contacts1.size()];
                    FilePathStrings = new String[contacts1.size()];
                    i = 0;
                    String PhotoGallertFolderPath = Constants.GetPhotoGalleryFolderPath();
                    for (Contact cn : contacts1) {
                        PhotoFileName = cn.getAlbumPhotofile();
                        URL = cn.getAlbumurl();
                        String File_Path_Str = PhotoGallertFolderPath
                                + "/" + PhotoFileName + "@@@###" + PhotoFileName;
                        FilePathStrings[i] = File_Path_Str;
                        flexst[i] = PhotoFileName;
                        albumURL[i] = URL;
                        albumlist[i] = cn.getAlbumName();
                        albumtime[i] = cn.getAlbumDatetime();
                        albumid[i] = cn.getAlbumId();
                        i++;
                    }
                }
            } catch (Exception ex) {
                Constants.writelog("PhotoGalleryActivity", "Albumwise_do_in_bck()165 MSG:" + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        //    mProgressBar.setVisibility(View.GONE);
            try {
               /* mAdapter = new AlbumListAdapter(AlbumWiseDetailActivityNew.this, FilePathStrings, albumlist,
                        albumURL, flexst, albumtime);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();*/
                new saveImagetask().execute();
            } catch (Exception ex) {
                Constants.writelog("PhotoGalleryActivity", "Albumwise_postexecution()102 MSG:" + ex.getMessage());
            }
        }
    }

    public class saveImagetask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            List<Contact> contacts1 = db.getAlbumwiseDetails(Integer.parseInt(StudentId),
                    Integer.parseInt(SchoolId), Integer.parseInt(String.valueOf(mAlbumId)));
            if (contacts1.size() > 0) {
                TotalItemList = new String[contacts1.size()];
                i = 0;
                for (Contact cn : contacts1) {
                    PhotoFileName = cn.getAlbumPhotofile();
                    URL = cn.getAlbumurl();
                    fileexist = new File(file + "/" + PhotoFileName + "");
                    if (!fileexist.exists() || fileexist.length()<=0) {
                        Bitmap bm = Constants.getBitmap(URL.toString(), fileexist); // getImageurl for generate Bitmap
                        SaveImage(bm, PhotoFileName);
                    }
                    i++;
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            mProgressBar.setVisibility(View.GONE);
            try {
                mAdapter = new AlbumListAdapter(AlbumWiseDetailActivityNew.this, FilePathStrings, albumlist,
                        albumURL, flexst, albumtime);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            } catch (Exception ex) {
                Constants.writelog("PhotoGalleryActivity", "Albumwise_postexecution()102 MSG:" + ex.getMessage());
            }
        }
    }

}


