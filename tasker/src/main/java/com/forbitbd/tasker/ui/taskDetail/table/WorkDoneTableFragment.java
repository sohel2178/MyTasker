package com.forbitbd.tasker.ui.taskDetail.table;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.forbitbd.androidutils.ui.zoomImage.ZoomImageActivity;
import com.forbitbd.androidutils.utils.Constant;
import com.forbitbd.tasker.R;
import com.forbitbd.tasker.models.WorkDone;
import com.forbitbd.tasker.ui.taskDetail.BaseDetailFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkDoneTableFragment extends BaseDetailFragment implements WorkDoneTableContract.View {

    private WorkDoneAdapter adapter;

    private WorkdoneTablePresenter mPresenter;



    public WorkDoneTableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new WorkdoneTablePresenter(this);
        adapter = new WorkDoneAdapter(this,getTask().getUnit());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_done_table, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        RecyclerView rvWorkdone = view.findViewById(R.id.rv_workdone);
        rvWorkdone.setAdapter(adapter);
        mPresenter.processData(getWorkdoneList());
    }


    @Override
    public void addItem(WorkDone workDone) {
        adapter.addItem(workDone);
    }

    @Override
    public void clearAdpter() {
        adapter.clear();
    }

    @Override
    public void startZoomImageActivity(WorkDone workDone) {
        Intent intent = new Intent(getContext(), ZoomImageActivity.class);
        intent.putExtra(Constant.PATH,workDone.getImage());
        startActivity(intent);
    }
}
