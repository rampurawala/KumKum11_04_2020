package com.expedite.apps.kumkum.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

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
public class AlbumWiseDetailActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private DatabaseHandler db = null;
    private String SchoolId, StudentId, URL, PhotoFileName;
    private String[] FilePathStrings, TotalItemList = null, albumlist = null, albumURL = null,
            flexst = null, albumtime = null;
    private Integer[] albumid = null;
    private File file, fileexist;
    private AlbumListAdapter mAdapter;
    private int i, mAlbumId;
    private ProgressBar mProgressBar;
    private LinearLayoutManager mLayoutManager;
    private AdView mAdView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albumwisedetail);

        init();
    }

    public void init() {
        try {
            Constants.setActionbar(getSupportActionBar(), AlbumWiseDetailActivity.this, getApplicationContext(),
                    "Photo Gallery", "Photo Gallery", ActivityNames.AlbumWiseDetailActivity);
            if (getIntent() != null && getIntent().getExtras() != null) {
                mAlbumId = Integer.parseInt(getIntent().getStringExtra("ALbUMID"));
            }
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.AlbumWiseDetailActivity);
            SchoolId = Datastorage.GetSchoolId(getApplicationContext());
            StudentId = Datastorage.GetStudentId(getApplicationContext());
            db = new DatabaseHandler(AlbumWiseDetailActivity.this);
            file = Constants.CreatePhotoGalleryFolder();

            mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            mLayoutManager = new LinearLayoutManager(AlbumWiseDetailActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);

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
            mProgressBar.setVisibility(View.GONE);
            try {
                mAdapter = new AlbumListAdapter(AlbumWiseDetailActivity.this, FilePathStrings, albumlist,
                        albumURL, flexst, albumtime);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
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
                    if (!fileexist.exists()) {
                        Bitmap bm = Constants.getBitmap(URL.toString(), fileexist); // getImageurl for generate Bitmap
                        SaveImage(bm, PhotoFileName);
                    }
                    i++;
                }
            }
            return null;
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
                    hideKeyboard(AlbumWiseDetailActivity.this);
                    AlbumWiseDetailActivity.this.finish();
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

}


