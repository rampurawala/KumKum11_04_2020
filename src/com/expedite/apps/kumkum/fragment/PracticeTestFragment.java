package com.expedite.apps.kumkum.fragment;



import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.MyApplication;
import com.expedite.apps.kumkum.R;

import com.expedite.apps.kumkum.activity.AccountListActivity;
import com.expedite.apps.kumkum.activity.BtmNavigationActivity;
import com.expedite.apps.kumkum.adapter.AccountListAdapter;
import com.expedite.apps.kumkum.adapter.AdapterPracticeTestFragment;
import com.expedite.apps.kumkum.common.Common;
import com.expedite.apps.kumkum.common.Datastorage;
import com.expedite.apps.kumkum.constants.Constants;
import com.expedite.apps.kumkum.constants.SchoolDetails;
import com.expedite.apps.kumkum.database.DatabaseHandler;
import com.expedite.apps.kumkum.model.AppService;

import com.expedite.apps.kumkum.model.Contact;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PracticeTestFragment extends Fragment {
    RecyclerView rc_fragPracticeTest;
    String flag, jsonstr;
    String tag="PracticeTestFragment";
    List<AppService.ListArray> listTest;
    LinearLayout empty_practiceTest;
    ProgressBar prg_test_frag;
    boolean canStart=false;

    public PracticeTestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_practice_test, container, false);
        try {
            rc_fragPracticeTest = v.findViewById(R.id.rc_fragPracticeTest);
            empty_practiceTest = v.findViewById(R.id.empty_practiceTest);
            prg_test_frag = v.findViewById(R.id.prg_test_frag);
           rc_fragPracticeTest.setHasFixedSize(true);
            rc_fragPracticeTest.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            if(getArguments()!=null){
                readBundle(getArguments());
            }else {
                empty_practiceTest.setVisibility(View.VISIBLE);
                rc_fragPracticeTest.setVisibility(View.GONE);
            }

            if (flag.equals("0"))
                getActivity().setTitle("Completed - Practice Test");
            if (flag.equals("1"))
                getActivity().setTitle("Pending - Practice Test");
            Gson gson = new Gson();
            Type type = new TypeToken<List<AppService.ListArray>>() {
            }.getType();
            listTest = gson.fromJson(jsonstr, type);
             if (listTest == null || listTest.isEmpty() || listTest.size() <= 0) {
                empty_practiceTest.setVisibility(View.VISIBLE);
                rc_fragPracticeTest.setVisibility(View.GONE);
            } else {
                empty_practiceTest.setVisibility(View.GONE);
                rc_fragPracticeTest.setVisibility(View.VISIBLE);
                AdapterPracticeTestFragment listAdapter = new AdapterPracticeTestFragment(getActivity(), listTest, Integer.parseInt(flag),prg_test_frag);
                rc_fragPracticeTest.setAdapter(listAdapter);
            }
        }catch (Exception e) {
                Constants.writelog("PracticeTestFragment", "onCreate 70:" + e.getMessage());
            }
            return v;
        }

        private void readBundle(Bundle bundle) {
            if (bundle != null) {
                flag = bundle.getString("flag");
                jsonstr = bundle.getString("testList");
            }
        }

}
