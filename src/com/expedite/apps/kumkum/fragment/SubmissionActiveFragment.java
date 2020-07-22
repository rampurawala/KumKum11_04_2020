package com.expedite.apps.kumkum.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.adapter.SubmissionFileListAdapter;
import com.expedite.apps.kumkum.constants.Constants;

import static com.expedite.apps.kumkum.activity.LeaveListActivity.mApprovedLeaveArrayList;
import static com.expedite.apps.kumkum.activity.SubmissionFileListDiffActivity.mActiveArrayList;

public class SubmissionActiveFragment extends Fragment {
    private View mParentView;
    private ProgressBar mProgressbar;
    private RecyclerView mSubmissionFileRV;
    public static SubmissionFileListAdapter mLeaveAdapterActive;
    private String mUserId = "", mGroupId = "";
    private LinearLayout mLLShareDownloadAll;
    AppCompatButton mBtnDownload, mBtnShare;

    public static final SubmissionActiveFragment newInstance(String paramString) {
        SubmissionActiveFragment f = new SubmissionActiveFragment();
        Bundle localBundle = new Bundle(1);
        localBundle.putString("Flag", paramString);
        f.setArguments(localBundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mParentView = inflater.inflate(R.layout.fragment_submission_active, container, false);
        init();
        return mParentView;
    }

    private void init() {
        try {
            mProgressbar = (ProgressBar) mParentView.findViewById(R.id.progressbar);
            mSubmissionFileRV = (RecyclerView) mParentView.findViewById(R.id.submissionFileRV);
            mLLShareDownloadAll = mParentView.findViewById(R.id.LLShareAndDownload);
            mBtnDownload = (AppCompatButton) mParentView.findViewById(R.id.downloadbtn);
            mBtnShare = (AppCompatButton) mParentView.findViewById(R.id.shareAll);

            mSubmissionFileRV.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            //test
            //   mLeaveAdapter = new LeaveListAdapter(getActivity(), mApprovedLeaveArrayList);
            mLeaveAdapterActive = new SubmissionFileListAdapter(getActivity(), mActiveArrayList,mBtnDownload,mBtnShare,mLLShareDownloadAll);
            mSubmissionFileRV.setAdapter(mLeaveAdapterActive);
            if (mActiveArrayList != null && mActiveArrayList.size() > 0) {
                ((TextView) mParentView.findViewById(R.id.txtNoRecordFound)).setVisibility(View.GONE);
                mSubmissionFileRV.setVisibility(View.VISIBLE);
            } else {
                ((TextView) mParentView.findViewById(R.id.txtNoRecordFound)).setVisibility(View.VISIBLE);
                mSubmissionFileRV.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            Constants.writelog("LeavePendingFragment", "onCreate:69" + ex.getMessage());
        }
    }
}