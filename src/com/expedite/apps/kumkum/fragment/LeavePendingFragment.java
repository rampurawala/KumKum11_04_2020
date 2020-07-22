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
import com.expedite.apps.kumkum.adapter.LeaveListAdapter;
import com.expedite.apps.kumkum.constants.Constants;

import static com.expedite.apps.kumkum.activity.LeaveListActivity.mPendingLeaveArrayList;

public class LeavePendingFragment extends Fragment {
    private View mParentView;
    private ProgressBar mProgressbar;
    private RecyclerView mLeaveRecycleView;
    private LeaveListAdapter mLeaveAdapter;
    private String mUserId = "", mGroupId = "";


//    public static final LeavePendingFragment newInstance(ArrayList<LeaveListModel.LeaveHistory> mData) {
//        LeavePendingFragment f = new LeavePendingFragment();
//        Bundle localBundle = new Bundle(1);
//        localBundle.putSerializable("LeavePendingArray", mData);
//        f.setArguments(localBundle);
//        return f;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.leave_pending_fragment_layout, container, false);
        init();
        return mParentView;
    }


    private void init() {
        try {
            mProgressbar = (ProgressBar) mParentView.findViewById(R.id.progressbar);
            mLeaveRecycleView = (RecyclerView) mParentView.findViewById(R.id.leaveRecycleView);
            mLeaveRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            mLeaveAdapter = new LeaveListAdapter(getActivity(), mPendingLeaveArrayList);
            mLeaveRecycleView.setAdapter(mLeaveAdapter);
            if (mPendingLeaveArrayList != null && mPendingLeaveArrayList.size() > 0) {
                ((TextView) mParentView.findViewById(R.id.txtNoRecordFound)).setVisibility(View.GONE);
                mLeaveRecycleView.setVisibility(View.VISIBLE);
            } else {
                ((TextView) mParentView.findViewById(R.id.txtNoRecordFound)).setVisibility(View.VISIBLE);
                mLeaveRecycleView.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            Constants.writelog("LeavePendingFragment", "onCreate:85" + ex.getMessage());
        }
    }
}
