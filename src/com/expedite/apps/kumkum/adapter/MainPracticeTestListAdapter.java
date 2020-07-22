package com.expedite.apps.kumkum.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.kumkum.BaseActivity;
import com.expedite.apps.kumkum.R;
import com.expedite.apps.kumkum.activity.BtmNavigationActivity;
import com.expedite.apps.kumkum.model.AppService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainPracticeTestListAdapter extends RecyclerView.Adapter<MainPracticeTestListAdapter.MyViewHolder> {

    Context context;
    List<String> subjectName,count;
    List<String> subjectID;
    List<AppService.ListArray> mainLists;

    public MainPracticeTestListAdapter(Context context, List<String> subjectName, List<String> subjectID, List<AppService.ListArray> mainLists, List<String> count) {
        this.context = context;
        this.subjectID = subjectID;
        this.subjectName = subjectName;
        this.mainLists = mainLists;
        this.count = count;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.main_practice_test_raw_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder itemViewHolder, final int i) {
        try {
            String name = subjectName.get(i);
            itemViewHolder.txtTitle.setText(name);
            itemViewHolder.txtCount.setText(count.get(i));
            itemViewHolder.mainfolderll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selectedId=subjectID.get(i);
                    String selectedName=subjectName.get(i);
                    ArrayList<AppService.ListArray> filteredList=new ArrayList();
                    for(int j=0;j<mainLists.size();j++){
                        if(mainLists.get(j).getEighth().equalsIgnoreCase(selectedId)){
                            filteredList.add(mainLists.get(j));
                        }
                    }
                    if(filteredList!=null && filteredList.size()>0){
                        Gson gson = new Gson();
                        String  jsonList = gson.toJson(filteredList);
                        Intent i = new Intent(context, BtmNavigationActivity.class);
                        i.putExtra("jsonList", jsonList);
                        i.putExtra("selectedId", selectedId);
                        i.putExtra("selectedName", selectedName);
                        context.startActivity(i);
                        ((BaseActivity)context).onClickAnimation();
                    }else {
                        Toast.makeText(context, "No Test Available !!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Log.d("Exception msg", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return subjectName == null ? 0 : subjectName.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle,txtCount;
        LinearLayout mainfolderll;

        public MyViewHolder(View itemView) {
            super(itemView);
            mainfolderll = itemView.findViewById(R.id.mainfolderll);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtCount = itemView.findViewById(R.id.txtCount);
        }
    }
}
