package com.expedite.apps.kumkum.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.adapter.AppointmentListAdapter;
import com.expedite.apps.kumkum.constants.Constants;

import static com.expedite.apps.kumkum.activity.TakeAppointmentListActivity.mApprovedAppointmentArrayList;


public class AppointmentApprovedFragment extends Fragment {
    private View mParentView;
    private ProgressBar mProgressbar;
    private RecyclerView appointmentRecycleView;
    private AppointmentListAdapter mAppointmentAdapter;
    private String mUserId = "", mGroupId = "";


    public static final AppointmentApprovedFragment newInstance(String paramString) {
        AppointmentApprovedFragment f = new AppointmentApprovedFragment();
        Bundle localBundle = new Bundle(1);
        localBundle.putString("Flag", paramString);
        f.setArguments(localBundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.leave_pending_fragment_layout, container, false);
        init();
        return mParentView;
    }


    private void init() {
        try {
            mProgressbar = (ProgressBar) mParentView.findViewById(R.id.progressbar);
            appointmentRecycleView = (RecyclerView) mParentView.findViewById(R.id.leaveRecycleView);
            appointmentRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            mAppointmentAdapter = new AppointmentListAdapter(getActivity(), mApprovedAppointmentArrayList);
            appointmentRecycleView.setAdapter(mAppointmentAdapter);
            if (mApprovedAppointmentArrayList != null && mApprovedAppointmentArrayList.size() > 0) {
                ((TextView) mParentView.findViewById(R.id.txtNoRecordFound)).setVisibility(View.GONE);
                appointmentRecycleView.setVisibility(View.VISIBLE);
            } else {
                ((TextView) mParentView.findViewById(R.id.txtNoRecordFound)).setVisibility(View.VISIBLE);
                appointmentRecycleView.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            Constants.writelog("AppointmentApprovedFragment", "onCreate:69" + ex.getMessage());
        }
    }
}
