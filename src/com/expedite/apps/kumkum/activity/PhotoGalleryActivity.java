package com.expedite.apps.kumkum.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.adapter.AlbumPhotoListAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.CircularModel;
import com.expedite.apps.kumkum.model.Contact;
import com.google.android.gms.ads.AdView;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryActivity extends BaseActivity {
    private Menu menu;
    private String[] TotalItemList = null, albumlist = {""};
    private Integer[] albumid = {};
    private String[] albumtime = {""}, FilePathStrings = {""};
    private File file;
    private int Is_Ref = 0;

    SearchView searchView;
    private String SchoolId, StudentId, Year_Id,deleteAlbumIds="";
    private ProgressBar mProgressbar;
    ArrayList<Integer> locallyPresentAlbumIds = new ArrayList<>();
    ArrayList<Integer> uniqueServerAlbumIds = new ArrayList<>();
    ArrayList<Integer> fromServiceAlbumIds = new ArrayList<>();
    ArrayList<Contact> deleteAlbumIdsArray = new ArrayList<>();
    ArrayList<Contact> locallyAlbumIdsArray = new ArrayList<>();
    ArrayList<Contact> allAlbumsFromSerrvice = new ArrayList<>();
    ArrayList<Contact> newFromServiceAlbums = new ArrayList<>();
    ArrayList<Contact> insertNewAlbumArray = new ArrayList<>();
    ArrayList<Contact> albumDisplayArray = new ArrayList<>();
    private String mIsFromHome = "";
    private GridLayoutManager mGridLayoutManager;
    private RecyclerView mPhotographsRecycle;
    private AlbumPhotoListAdapter mPhotoAdapter;
    private ArrayList<CircularModel.Strlist> mAlbumlist = new ArrayList<>();
    LinearLayout empty_folder_lyt_file_detail;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        if (getIntent() != null && getIntent().getExtras() != null)
            mIsFromHome = getIntent().getExtras().getString("IsFromHome", "");
        init();
    }

    public void init() {
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            showBannerAd(mAdView, ActivityNames.PhotoGalleryActivity);
            Constants.setActionbar(getSupportActionBar(), PhotoGalleryActivity.this, PhotoGalleryActivity.this,
                    "PhotoGallery", "PhotoGalleryActivity", ActivityNames.PhotoGalleryActivity);
            SchoolId = Datastorage.GetSchoolId(PhotoGalleryActivity.this);
            StudentId = Datastorage.GetStudentId(PhotoGalleryActivity.this);
            Year_Id = Datastorage.GetCurrentYearId(PhotoGalleryActivity.this);
            db = new DatabaseHandler(PhotoGalleryActivity.this);
            file = Constants.CreatePhotoGalleryFolder();
            mProgressbar = (ProgressBar) findViewById(R.id.ProgressBar);
            empty_folder_lyt_file_detail = findViewById(R.id.empty_folder_lyt_file_detail);
            mGridLayoutManager = new GridLayoutManager(PhotoGalleryActivity.this, 2);
            mPhotographsRecycle = (RecyclerView) findViewById(R.id.PhotographsRecycle);
            mPhotographsRecycle.setLayoutManager(mGridLayoutManager);
            mPhotoAdapter = new AlbumPhotoListAdapter(PhotoGalleryActivity.this,albumDisplayArray /*FilePathStrings, albumlist,
                    albumtime, albumid*/);
            mPhotographsRecycle.setAdapter(mPhotoAdapter);
            GetAlbumDetails();
            //new GetAlbumList().execute();
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    private void DeleteAllPhotoAlbum() {
        try {
            int DeleteAlbumStatus = db.DeleteStudentPhotoGallery(Integer.parseInt(StudentId), Integer.parseInt(SchoolId));
            if (DeleteAlbumStatus > 0) {
                File[] listFile = file.listFiles();
                for (int i = 0; i < listFile.length; i++) {
                    if (listFile[i].exists()) {
                        listFile[i].delete();
                    }
                }
            }
        } catch (Exception ex) {
            Constants.writelog("PhotoGalleryActivity", "122 Ex:" + ex.getMessage() + "::::::" + ex.getStackTrace());
        }
    }

    private class GetAlbumList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                if (Is_Ref == 1) {
                    AlbumDetails();
                }

            } catch (Exception ex) {
                Constants.writelog("PhotoGalleryActivity", "203 Ex:" + ex.getMessage() + "::::::" + ex.getStackTrace());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(getApplicationContext());
                } else {
                    GetAlbumDetails();
                  /*  if (TotalItemList != null && TotalItemList.length > 0) {
                        mPhotoAdapter = new AlbumPhotoListAdapter(PhotoGalleryActivity.this, FilePathStrings,
                                albumlist, albumtime, albumid);
                        mPhotographsRecycle.setAdapter(mPhotoAdapter);
                        mPhotoAdapter.notifyDataSetChanged();
                        mPhotographsRecycle.setVisibility(View.VISIBLE);
                    } else {
                        mPhotographsRecycle.setVisibility(View.GONE);
                        Toast.makeText(PhotoGalleryActivity.this, "No Photos Available", Toast.LENGTH_LONG).show();
                    }*/
                }
                mProgressbar.setVisibility(View.GONE);
            } catch (Exception ex) {
                Constants.writelog("PhotoGalleryActivity", "233 Ex:" + ex.getMessage() + "::::::" + ex.getStackTrace());
                mProgressbar.setVisibility(View.GONE);
            }
        }
    }

    public void GetAlbumDetails() {
        try {
            List<Contact> contacts1 = db.getAlbumDetailsGrid(Integer.parseInt(StudentId), Integer.parseInt(SchoolId), 0);
          albumDisplayArray.clear();

            if (contacts1.size() > 0) {
                locallyPresentAlbumIds.clear();
                locallyAlbumIdsArray.clear();
                TotalItemList = new String[contacts1.size()];
                albumlist = new String[contacts1.size()];
                albumtime = new String[contacts1.size()];
                albumid = new Integer[contacts1.size()];
                FilePathStrings = new String[contacts1.size()];
                int i = 0;
                String PhotoGallertFolderPath = Constants.GetPhotoGalleryFolderPath();
                for (Contact cn : contacts1) {
                    Contact contactLocal=new Contact();
                    String PhotoFileName = cn.getAlbumPhotofile();
                    String URL = cn.getAlbumurl();
                    String File_Path_Str = PhotoGallertFolderPath
                            + "/" + PhotoFileName + "@@@###"
                            + PhotoFileName;
                    File fileexist = new File(file + "/" + PhotoFileName + "");
                    FilePathStrings[i] = File_Path_Str;
                    albumlist[i] = cn.getAlbumName();
                    albumtime[i] = cn.getAlbumDatetime();
                    albumid[i] = cn.getAlbumId();
                    locallyPresentAlbumIds.add(albumid[i]);
                    contactLocal.setAlbumName(cn.getAlbumName());
                    contactLocal.setAlbumDatetime(cn.getAlbumDatetime());
                    contactLocal.setAlbumId(cn.getAlbumId());
                    contactLocal.setAlbumPhotofile(File_Path_Str);

                    albumDisplayArray.add(contactLocal);
                    if (!fileexist.exists() || fileexist.length()<=0) {
                        Bitmap bmp = Constants.getBitmap(URL.toString(), fileexist);
                        Constants.SaveImage(bmp, PhotoFileName);
                    }
                    i++;
                }
                locallyAlbumIdsArray.addAll(contacts1);

                if (TotalItemList != null && TotalItemList.length > 0) {
                    mPhotoAdapter = new AlbumPhotoListAdapter(PhotoGalleryActivity.this,albumDisplayArray /*FilePathStrings,
                            albumlist, albumtime, albumid*/);
                    mPhotographsRecycle.setAdapter(mPhotoAdapter);
                    mPhotoAdapter.notifyDataSetChanged();
                    mPhotographsRecycle.setVisibility(View.VISIBLE);
                } else {
                    mPhotographsRecycle.setVisibility(View.GONE);
                    Toast.makeText(PhotoGalleryActivity.this, "No Photos Available", Toast.LENGTH_LONG).show();
                }
            } else {
                Is_Ref = 1;
                new GetAlbumList().execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] AlbumDetails() {
        if (isOnline()) {
            mProgressbar.setVisibility(View.VISIBLE);
            empty_folder_lyt_file_detail.setVisibility(View.GONE);
            mPhotographsRecycle.setVisibility(View.VISIBLE);
            uniqueServerAlbumIds.clear();
            newFromServiceAlbums.clear();
            allAlbumsFromSerrvice.clear();
            SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.ALBUM_DETAILS);
            request.addProperty("SchoolId", Integer.parseInt(SchoolId));
            request.addProperty("ClassSecId", Integer.parseInt(Datastorage.GetClassSecId(getApplicationContext())));
            request.addProperty("PhotType", 1);
            request.addProperty("StudId", Integer.parseInt(StudentId));
            request.addProperty("yearid", Integer.parseInt(Year_Id));
            try {
                SoapObject result = Constants.CallWebMethod(PhotoGalleryActivity.this, request, Constants.ALBUM_DETAILS, true);
                Constants.Logwrite("MessageList", "Result length is " + result.toString().length());
                if (result != null && result.getPropertyCount() > 0) {
                    SoapObject obj2 = (SoapObject) result.getProperty(0);
                    Constants.Logwrite("MsgLogCount:", "----------------");
                    if (obj2 != null) {
                        int count = obj2.getPropertyCount();
                        String[] myarray = new String[count];
                        for (int i = 0; i < count; i++) {
                            myarray[i] = obj2.getProperty(i).toString();
                            String[] msgitem = myarray[i].split("##@@");
                            String filename = msgitem[2];
                            File fileexist = new File(file + "/" + filename + "");
                            Constants.Logwrite("PhotoGalleryActivity", "Photo path 456 photoDetails:" + myarray[i]);
                            int albumid = Integer.parseInt(msgitem[0]);
                            fromServiceAlbumIds.add(albumid);
                            Contact contact=  new Contact();;
                            contact.setAlbumId(albumid);
                            contact.setAlbumName(msgitem[1]);
                            contact.setAlbumPhotofile(msgitem[5]);
                            contact.setStudnetId(Integer
                                    .parseInt(StudentId));
                            contact.setAlbumurl(msgitem[2]);
                            contact.setAlbumSchoolId(Integer.parseInt(SchoolId));
                            contact.setAlbumClassSecId(Integer.parseInt(msgitem[4]));
                            contact.setAlbumdateticks(Long.parseLong(msgitem[8]));
                            contact.setAlbumDatetime(msgitem[6]);
                            allAlbumsFromSerrvice.add(contact);
                            if(!uniqueServerAlbumIds.contains(albumid)){
                                uniqueServerAlbumIds.add(albumid);
                                newFromServiceAlbums.add(contact);
                            }
                          /*  if(!locallyPresentAlbumIds.contains(albumid)) {
                                contact.setAlbumId(albumid);
                                contact.setAlbumName(msgitem[1]);
                                contact.setAlbumPhotofile(msgitem[5]);
                                contact.setStudnetId(Integer
                                        .parseInt(StudentId));
                                contact.setAlbumurl(msgitem[2]);
                                contact.setAlbumSchoolId(Integer.parseInt(SchoolId));
                                contact.setAlbumClassSecId(Integer.parseInt(msgitem[4]));
                                contact.setAlbumdateticks(Long.parseLong(msgitem[8]));
                                contact.setAlbumDatetime(msgitem[6]);
                                newFromServiceAlbums.add(contact);
                            }*/
                        }
                        deleteAlbumIds="";
                        deleteAlbumIdsArray.clear();
                        for(int i=0;i<locallyPresentAlbumIds.size();i++){
                            if(!uniqueServerAlbumIds.contains(locallyPresentAlbumIds.get(i))){
                                deleteAlbumIdsArray.add(locallyAlbumIdsArray.get(i));
                                deleteAlbumIds+=locallyPresentAlbumIds.get(i)+",";
                            }
                        }
                        for(int i=0;i<uniqueServerAlbumIds.size();i++){
                            if(!locallyPresentAlbumIds.contains(uniqueServerAlbumIds.get(i))){
                                insertNewAlbumArray.add(newFromServiceAlbums.get(i));
                                int albumIdUnique=uniqueServerAlbumIds.get(i);
                                for (int j = 0; j < allAlbumsFromSerrvice.size(); j++){
                                    if(allAlbumsFromSerrvice.get(j).getAlbumId()==albumIdUnique){
                                        boolean isinserted = db.CheckAlbumDetailsInserted(
                                                Integer.parseInt(StudentId),
                                                Integer.parseInt(SchoolId),
                                                allAlbumsFromSerrvice.get(j).getAlbumClassSecId(),
                                                allAlbumsFromSerrvice.get(j).getAlbumId(),   allAlbumsFromSerrvice.get(j).getAlbumurl());
                                        if (!isinserted) {
                                            //int studid, int albumid, String albumname, String albumurl,
                                            //                   String photofilename, int schoolid, int classsecid, long Dateticks,
                                            //                   String DateTime) {
                                            db.AddAlbumDetails(new Contact(Integer
                                                    .parseInt(StudentId), allAlbumsFromSerrvice.get(j).getAlbumId(), allAlbumsFromSerrvice.get(j).getAlbumName(),allAlbumsFromSerrvice.get(j).getAlbumurl(), allAlbumsFromSerrvice.get(j).getAlbumPhotofile(),
                                                    Integer.parseInt(SchoolId), allAlbumsFromSerrvice.get(j).getAlbumClassSecId(),
                                                    allAlbumsFromSerrvice.get(j).getAlbumdateticks(), allAlbumsFromSerrvice.get(j).getAlbumDatetime()));
                                        }
                                    }
                                }
                            }
                        }
                        if (!deleteAlbumIds.isEmpty() && deleteAlbumIds.endsWith(",")) {
                            deleteAlbumIds = deleteAlbumIds.substring(0, deleteAlbumIds.length() - 1);
                        }
                        if(deleteAlbumIdsArray.size()>0){

                            //  String PhotoGallertFolderPath = Constants.GetPhotoGalleryFolderPath();
                              String[] splitAlbumIds=deleteAlbumIds.split(",");
                              for(int j=0;j<splitAlbumIds.length;j++){
                                  List<Contact> particulerAlbumDetail=db.getSpecificAlbumPhotos(Integer.parseInt(StudentId),Integer.parseInt(SchoolId),Integer.parseInt(splitAlbumIds[j]));

                                  for (int k = 0; k < particulerAlbumDetail.size(); k++){
                                          String PhotoFileName = particulerAlbumDetail.get(k).getAlbumPhotofile();
                                          File  fileexist = new File(file + "/" + PhotoFileName + "");
                                          if (fileexist.exists()) {
                                              fileexist.delete();
                                          }
                                  }
                              }
                              int isDeleted=  db.DeleteSpecificAlbum(deleteAlbumIds,Integer.parseInt(StudentId),
                                      Integer.parseInt(SchoolId));

                        }
                      /*  if(insertNewAlbumArray.size()>0) {
                            //int Studid, int SchoolID,
                            //                                             int ClassSecId, int AlbumId, String filename
                            for (int i = 0; i < insertNewAlbumArray.size(); i++){
                                boolean isinserted = db.CheckAlbumDetailsInserted(
                                        Integer.parseInt(StudentId),
                                        Integer.parseInt(SchoolId),
                                        insertNewAlbumArray.get(i).getAlbumClassSecId(),
                                        insertNewAlbumArray.get(i).getAlbumId(),   insertNewAlbumArray.get(i).getAlbumurl());
                            if (!isinserted) {
                                //int studid, int albumid, String albumname, String albumurl,
                                //                   String photofilename, int schoolid, int classsecid, long Dateticks,
                                //                   String DateTime) {
                                db.AddAlbumDetails(new Contact(Integer
                                        .parseInt(StudentId), insertNewAlbumArray.get(i).getAlbumId(), insertNewAlbumArray.get(i).getAlbumName(),insertNewAlbumArray.get(i).getAlbumurl(), insertNewAlbumArray.get(i).getAlbumPhotofile(),
                                        Integer.parseInt(SchoolId), insertNewAlbumArray.get(i).getAlbumClassSecId(),
                                        insertNewAlbumArray.get(i).getAlbumdateticks(), insertNewAlbumArray.get(i).getAlbumDatetime()));
                            }
                        }
                        }*/

                        //(int studid, int albumid, String albumname, String albumurl,
                        //                   String photofilename, int schoolid, int classsecid, long Dateticks,
                        //                   String DateTime)

                        //test
                      /*  boolean isinserted = db.CheckAlbumDetailsInserted(
                                Integer.parseInt(StudentId),
                                Integer.parseInt(SchoolId),
                                Integer.parseInt(msgitem[4]),
                                albumid, filename);
                        if (!isinserted) {
                            db.AddAlbumDetails(new Contact(Integer
                                    .parseInt(StudentId), albumid, msgitem[1], msgitem[2], msgitem[5],
                                    Integer.parseInt(SchoolId), Integer.parseInt(msgitem[4]),
                                    Long.parseLong(msgitem[8]), msgitem[6]));
                        }*/
                    } else {
                        albumlist = null;
                    }
                } else {
                    empty_folder_lyt_file_detail.setVisibility(View.VISIBLE);
                    mPhotographsRecycle.setVisibility(View.GONE);
                    mProgressbar.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                Constants.writelog("PhotoGalleryActivity", "AlbumDetails()505 Error::" + e.getMessage());
                return albumlist;
            }
        } else {
            Toast.makeText(this, SchoolDetails.MsgNoInternet, Toast.LENGTH_SHORT).show();
            empty_folder_lyt_file_detail.setVisibility(View.VISIBLE);
            mPhotographsRecycle.setVisibility(View.GONE);
            mProgressbar.setVisibility(View.GONE);
        }
        return albumlist;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.activity_photo_options, menu);
            menu.findItem(R.id.photo_view_type).setVisible(false);

            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView = (SearchView) menu.findItem(R.id.search)
                    .getActionView();
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            // listening to search query text change
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // filter recycler view when query submitted
                    mPhotoAdapter.getFilter().filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    // filter recycler view when text is changed
                    mPhotoAdapter.getFilter().filter(query);
                    return false;
                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                hideKeyboard(PhotoGalleryActivity.this);
                PhotoGalleryActivity.this.finish();
                onBackClickAnimation();
                break;
            case R.id.photo_view_type:
//                if (Datastorage.GetPhotoGalleryType(PhotoGalleryActivity.this) == 0) {
//                    menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_view_list_white_36dp));
//                    Datastorage.SetPhotoGalleryType(PhotoGalleryActivity.this, 1);
//                    startActivity(getIntent());
//                    finish();
//                } else {
//                    menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_photo_album_white_36dp));
//                    Datastorage.SetPhotoGalleryType(PhotoGalleryActivity.this, 0);
//                    startActivity(getIntent());
//                    finish();
//                }
                break;

            case R.id.action_refresh:
                Constants.googleAnalyticEvent(PhotoGalleryActivity.this, Constants.button_click, "Refresh");
                Is_Ref = 1;
                new GetAlbumList().execute();
                break;

            case R.id.action_add_account:
                addAccountClick(PhotoGalleryActivity.this);
                break;

            case R.id.action_remove_account:
                removeAccountClick(PhotoGalleryActivity.this);
                break;

            case R.id.action_set_default_account:
                setDefaultAccount(PhotoGalleryActivity.this);
                break;

            case R.id.action_clear_photo:
                Constants.googleAnalyticEvent(PhotoGalleryActivity.this, Constants.button_click, "SetDefaultYear");
                DeleteAllPhotoAlbum();
                break;

            case R.id.action_about:
                Constants.googleAnalyticEvent(PhotoGalleryActivity.this, Constants.button_click, "AboutActivity");
                intent = new Intent(PhotoGalleryActivity.this, AboutActivity.class);
                startActivity(intent);
                finish();
                onClickAnimation();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        try {
            if (mIsFromHome != null && !mIsFromHome.isEmpty()) {
                super.onBackPressed();
                onBackClickAnimation();
            } else {
                Intent intent = new Intent(PhotoGalleryActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        } catch (Exception err) {
            Constants.writelog("PhotoGalleryActivity", "onBackPressed()699 Error::" + err.getMessage());
        }
    }
}

