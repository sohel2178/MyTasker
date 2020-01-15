package com.forbitbd.tasker.ui.taskDetail;


import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.forbitbd.androidutils.models.Task;
import com.forbitbd.androidutils.utils.Constant;
import com.forbitbd.androidutils.utils.PrebaseActivity;
import com.forbitbd.androidutils.utils.ViewPagerAdapter;
import com.forbitbd.tasker.R;
import com.forbitbd.tasker.models.WorkDone;
import com.forbitbd.tasker.ui.taskDetail.chart.WorkDoneChartFragment;
import com.forbitbd.tasker.ui.taskDetail.progress.ProgressFragment;
import com.forbitbd.tasker.ui.taskDetail.table.WorkDoneTableFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class TaskDetailActivity extends PrebaseActivity implements TaskDetailContract.View {

    private TaskDetailPresenter mPresenter;
    
    
    private Task mTask;

    private ViewPagerAdapter adapter;

    private List<WorkDone> workDoneList;

    private ViewPager viewPager;

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        mTask = (Task) getIntent().getSerializableExtra(Constant.TASK);

        mPresenter = new TaskDetailPresenter(this);
        
        initView();
        
        
    }

    private void initView() {
        setupToolbar(R.id.toolbar);
        getSupportActionBar().setTitle(mTask.getName());

        mPresenter.getAllWorkdone(mTask);
    }

    private void setupViewPager(ViewPager viewPager) {
        if(adapter==null){
            adapter = new ViewPagerAdapter(getSupportFragmentManager());
        }
        adapter.addFragment(new WorkDoneTableFragment(), "TABLE");
        adapter.addFragment(new WorkDoneChartFragment(), "CHART");
        adapter.addFragment(new ProgressFragment(), "PROGRESS");
        //adapter.addFragment(new TaskGalleryFragment(), "GALLERY");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void initializeViewpager(List<WorkDone> workDoneList) {
        this.workDoneList = workDoneList;

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    public List<WorkDone> getWorkDoneList(){
        return this.workDoneList;
    }

    public double getVolumeOfWorks(){
        return mTask.getVolume_of_works();
    }

    public Task getTask(){
        return mTask;
    }
}
