package com.expedite.apps.kumkum.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;

import com.expedite.apps.kumkum.adapter.DocumentAdapter;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.ActivityNames;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.model.Document;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentsActivity extends BaseActivity {
    RecyclerView mDocRecyclerview;
    ArrayList<Document.DocumentItemList> StudentsArray = new ArrayList<>();
    ArrayList<Document.DocumentItemList> filterStudentsArray = new ArrayList<>();
    DocumentAdapter mdocumentAdapter;
    String Studid = "", Schoolid = "", YearId = "", versionCode = "", platform = "";
    String S = "", A = "";
    String json;
    LinearLayout layout, empty_Document, llSpinner;
    RelativeLayout dataLyt;
    ProgressBar prgsDocument;
    private Spinner mYearSpinner;
    private int selectedIndex = 0;
    private String mSelectedYearId = "", mSelectedYearName = "";
    private List<String> mSpnYearNameArray = new ArrayList<>();
    private List<String> mSpnYearIdArray = new ArrayList<>();
    private ArrayAdapter<String> mYearAdapter;
    LinearLayout empty_folder_lyt_file_detail;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Documents");
        }
        mAdView = (AdView) findViewById(R.id.adView);
        showBannerAd(mAdView, ActivityNames.DocumentsActivity);
        showFullScreenAd(DocumentsActivity.this,ActivityNames.DocumentsActivity);
        mSpnYearIdArray.clear();
        mSpnYearNameArray.clear();
        mSpnYearIdArray.add("0");
        mSpnYearNameArray.add("All");

        layout = findViewById(R.id.empty_folder_lyt_file_detail);
        prgsDocument = findViewById(R.id.prgsDocument);
        mYearSpinner = findViewById(R.id.spnYear);
        empty_Document = findViewById(R.id.empty_Document);
        dataLyt = findViewById(R.id.dataLyt);
        llSpinner = findViewById(R.id.llSpinner);

        mDocRecyclerview = (RecyclerView) findViewById(R.id.rc_Doc);
        GridLayoutManager mGridlayout = new GridLayoutManager(DocumentsActivity.this, 1);
        mDocRecyclerview.setLayoutManager(mGridlayout);
        mdocumentAdapter = new DocumentAdapter(DocumentsActivity.this, filterStudentsArray);
        mDocRecyclerview.setAdapter(mdocumentAdapter);

        /*json ="{\"DocumentItemList\":[{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/scanned/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"TC\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/scanned/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"Fees_Pending_Letter\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/scanned/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"LC\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/scanned/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"I_Card_Secondary_Higher\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"Sci_Board_Registration_Form\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"Escortcard_Morning\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"LC_VERIFICATION\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/scanned/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"LC\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/scanned/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"H.S.C._CHARACTER_CERTIFICATE\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display\"}";*/
        // final Document.DocumentItemList tmp = mFilteredList.get(position);
        //List<Document.DocumentItemList> data = new Gson().fromJson(json, Document.class);
        //List<Document.DocumentItemList> data = new Document.DocumentItemList(json, Document.class);
      /*  List<Document.DocumentItemList> data = (List<Document.DocumentItemList>) new Gson().fromJson(json, Document.class);

        //data.get(1).getDocumentName();
        mdocumentAdapter = new DocumentAdapter(DocumentsActivity.this, StudentsArray);
        StudentsArray.addAll(data.);
        mdocumentAdapter.notifyDataSetChanged();

        Document data=*/


       /* JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(Data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonObject.toString();
        */


       /* if(true)
        {
            List<Document.DocumentItemList> jsonData =null;
            ArrayList<Document.DocumentItemList> StudentsArray = new ArrayList<>();
            StudentsArray.addAll(jsonData);
            mdocumentAdapter.notifyDataSetChanged();
        }else {
           // getDocuments();
        }*/

        getDocuments();
        mYearAdapter = new ArrayAdapter<String>(DocumentsActivity.this, R.layout
                .school_spinner_item, mSpnYearNameArray) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                try {
                    ((TextView) v).setTextColor(getResources().getColor(R.color.viewname));
                    ((TextView) v).setTextSize(15);
                } catch (Exception ex) {
                    Constants.Logwrite("Examlist", "Ex 102:" + ex.getMessage());
                }
                return v;
            }
        };
        mYearSpinner.setAdapter(mYearAdapter);
        mYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != selectedIndex) {
                    selectedIndex = position;
                    mSelectedYearId = mSpnYearIdArray.get(position);
                    mSelectedYearName = mSpnYearNameArray.get(position);
                    if (mSelectedYearId != null && !mSelectedYearId.isEmpty())
                        GetYearDocument(mSelectedYearName);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void GetYearDocument(String yearName) {
        if (yearName.equalsIgnoreCase("All")) {
            filterStudentsArray.clear();
            Document.DocumentItemList documentItemList = new Document.DocumentItemList();
            String year = "";
            for (int i = 0; i < StudentsArray.size(); i++) {
                documentItemList = new Document.DocumentItemList();
                if (StudentsArray.get(i).getYear() != null && StudentsArray.get(i).getYear() != "") {
                    if (!StudentsArray.get(i).getYear().equalsIgnoreCase(year)) {
                        year = StudentsArray.get(i).getYear();
                        documentItemList.setYearFlag("1");
                        documentItemList.setYear(StudentsArray.get(i).getYear());
                        filterStudentsArray.add(documentItemList);
                    }
                }
                documentItemList = new Document.DocumentItemList();
                documentItemList.setYearFlag("0");
                documentItemList.setYear(StudentsArray.get(i).getYear());
                documentItemList.setDocumentName(StudentsArray.get(i).getDocumentName());
                documentItemList.setDisplayUrl(StudentsArray.get(i).getDisplayUrl());
                documentItemList.setDocumentDate(StudentsArray.get(i).getDocumentDate());
                documentItemList.setDocumentId(StudentsArray.get(i).getDocumentId());
                documentItemList.setDocumentType(StudentsArray.get(i).getDocumentType());
                documentItemList.setShareUrl(StudentsArray.get(i).getShareUrl());
                filterStudentsArray.add(documentItemList);
            }
            mDocRecyclerview.setVisibility(View.VISIBLE);
            llSpinner.setVisibility(View.VISIBLE);
            empty_Document.setVisibility(View.GONE);

            mdocumentAdapter.notifyDataSetChanged();
        } else {
            filterStudentsArray.clear();
            int flag = 1;
            Document.DocumentItemList documentItemList = new Document.DocumentItemList();
            for (int i = 0; i < StudentsArray.size(); i++) {
                if (StudentsArray.get(i).getYear().equalsIgnoreCase(yearName)) {
                    if (flag == 1) {
                        documentItemList = new Document.DocumentItemList();
                        documentItemList.setYearFlag("1");
                        documentItemList.setYear(StudentsArray.get(i).getYear());
                        filterStudentsArray.add(documentItemList);
                        flag = 0;
                    }
                    filterStudentsArray.add(StudentsArray.get(i));
                }
            }
            if (filterStudentsArray.size() == 0) {
                mDocRecyclerview.setVisibility(View.GONE);
                llSpinner.setVisibility(View.VISIBLE);
                empty_Document.setVisibility(View.VISIBLE);
            } else {
                mDocRecyclerview.setVisibility(View.VISIBLE);
                llSpinner.setVisibility(View.VISIBLE);
                empty_Document.setVisibility(View.GONE);
            }

            mdocumentAdapter.notifyDataSetChanged();
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }*/

    //    private void search(SearchView searchView) {
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                mdocumentAdapter.getFilter().filter(newText);
//                return true;
//            }
//        });
//    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                DocumentsActivity.this.finish();
                onBackClickAnimation();
                return true;
            default:
                break;
        }
        return false;
    }

    public void onBackClickAnimation() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void getDocuments() {
        if (isOnline()) {
            prgsDocument.setVisibility(View.VISIBLE);
            String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
            String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
            Call<Document> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                    .GetStudentDocuments(mStudentId, mYearId, mSchoolId, Constants.PLATFORM, Constants.CODEVERSION);
            call.enqueue(new Callback<Document>() {
                @Override
                public void onResponse(Call<Document> call, Response<Document> response) {
                    try {
                        prgsDocument.setVisibility(View.GONE);
                        Document tmps = response.body();
                        if (tmps != null && tmps.getResultFlag() != null && !tmps.getResultFlag().isEmpty()
                                && tmps.getResultFlag().equalsIgnoreCase("1")) {
                            if (tmps.getDocumentItemLists() != null && tmps.getDocumentItemLists().size() > 0) {
                                dataLyt.setVisibility(View.VISIBLE);
                                empty_Document.setVisibility(View.GONE);

                               /* Document.DocumentItemList documentItemList = new Document.DocumentItemList();
                                documentItemList.setYear("2011-12");
                                documentItemList.setDocumentName("Voter Id");
                                tmps.getDocumentItemLists().add(documentItemList);

                                Document.DocumentItemList documentItemList2 = new Document.DocumentItemList();
                                documentItemList2.setYear("2011-12");
                                documentItemList2.setDocumentName("Voter Id 2");
                                tmps.getDocumentItemLists().add(documentItemList2);

                                Document.DocumentItemList documentItemList1 = new Document.DocumentItemList();
                                documentItemList1.setYear("2012-13");
                                documentItemList1.setDocumentName("Voter Id 1");
                                tmps.getDocumentItemLists().add(documentItemList1);*/

                                for (int i = 0; i < tmps.getDocumentItemLists().size(); i++) {
                                    if (tmps.getDocumentItemLists().get(i).getYear() != null && tmps.getDocumentItemLists().get(i).getYear() != "") {
                                        if (mSpnYearNameArray.size() > 0 && !mSpnYearNameArray.contains(tmps.getDocumentItemLists().get(i).getYear())) {
                                            mSpnYearNameArray.add(tmps.getDocumentItemLists().get(i).getYear());
                                            int j = i + 1;
                                            mSpnYearIdArray.add(String.valueOf(j));
                                        }
                                    }

                                }
                                StudentsArray.addAll(tmps.getDocumentItemLists());
                                GetYearDocument("ALL");
                            } else {
                                dataLyt.setVisibility(View.GONE);
                                empty_Document.setVisibility(View.VISIBLE);
                            }
                           /* filterStudentsArray.addAll(tmps.getDocumentItemLists());
                            mdocumentAdapter.notifyDataSetChanged();*/
                        } else {
                            Toast.makeText(DocumentsActivity.this, tmps.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            dataLyt.setVisibility(View.GONE);
                            empty_Document.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception ex) {
                        prgsDocument.setVisibility(View.GONE);
                        Constants.writelog("DocumentsActivity", "getDocuments 284:" + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Document> call, Throwable t) {
                    prgsDocument.setVisibility(View.GONE);
                    Constants.writelog("DocumentsActivity", "getDocuments 113:" + t.getMessage());
                }
            });
        }
        else {
            dataLyt.setVisibility(View.GONE);
            empty_Document.setVisibility(View.VISIBLE);
            prgsDocument.setVisibility(View.GONE);
            Common.showToast(DocumentsActivity.this, getString(R.string.msg_connection));
        }
    }
}
