package com.forbitbd.tasker.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;


import com.forbitbd.androidutils.models.Project;
import com.forbitbd.androidutils.models.Task;
import com.forbitbd.androidutils.utils.Constant;
import com.forbitbd.androidutils.utils.PrebaseActivity;
import com.forbitbd.androidutils.utils.ViewPagerAdapter;
import com.forbitbd.tasker.R;
import com.forbitbd.tasker.ui.addWorkdone.AddWorkdoneActivity;
import com.forbitbd.tasker.ui.gantt.GanttActivity;
import com.forbitbd.tasker.ui.pager.TaskPagerFragment;
import com.forbitbd.tasker.ui.taskAdd.TaskAddActivity;
import com.forbitbd.tasker.ui.taskEdit.TaskEditActivity;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.ramotion.foldingcell.FoldingCell;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class TaskActivity extends PrebaseActivity implements TaskContract.View , View.OnClickListener {
    private static final int ADD_TASK=7000;
    private static final int EDIT_TASK=8000;
    private static final int ADD_WORKDONE=9000;
    private static final int READ_WRITE_PERMISSION=12000;

    private TaskPresenter mPresenter;

    private List<Task> taskList;

    private Project project;

    private TreeSet<String> unitSet;

    private TextView tvProjectLocation,tvProjectDescription,tvPhysicalProgress,tvFinancialProgress,tvTaskCount;
    private FoldingCell mFoldingCell;

    private ViewPagerAdapter adapter;
    private ViewPager viewPager;

    private FloatingActionButton fabAdd,fabGantt,fabWorkdone,fabDownload;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        this.mPresenter = new TaskPresenter(this);

        this.project = (Project) getIntent().getSerializableExtra(Constant.PROJECT);

        initView();
    }

    private void initView() {
        setupToolbar(R.id.toolbar);
        setupBannerAd(R.id.adView);

        getSupportActionBar().setTitle(project.getName());


        fabAdd = findViewById(R.id.fab_add);
        fabGantt = findViewById(R.id.fab_gantt_chart);
        fabWorkdone = findViewById(R.id.fab_add_workdone);
        fabDownload = findViewById(R.id.fab_download);

        mFoldingCell = findViewById(R.id.folding_cell);

        tvProjectLocation = findViewById(R.id.project_location);
        tvProjectDescription = findViewById(R.id.project_desc);
        tvPhysicalProgress = findViewById(R.id.physical_progress);
        tvFinancialProgress = findViewById(R.id.financial_progress);
        tvTaskCount = findViewById(R.id.task_count);

        tvProjectLocation.setText(project.getLocation());
        tvProjectDescription.setText(project.getDescription());

        fabAdd.setOnClickListener(this);
        fabGantt.setOnClickListener(this);
        fabWorkdone.setOnClickListener(this);
        fabDownload.setOnClickListener(this);
        mFoldingCell.setOnClickListener(this);

        mPresenter.getProjectTask(project.get_id());


    }

    public Project getProject(){
        return this.project;
    }

    @Override
    public void onClick(View view) {

        if(view==fabAdd){
            mPresenter.startAddTaskActivity();
        }else if(view==mFoldingCell){
            mFoldingCell.toggle(false);
        }else if(view==fabWorkdone){
            mPresenter.startAddWorkdoneActivity();
        }else if(view==fabDownload){
            requestFileAfterPermission();
        }else if(view==fabGantt){
            mPresenter.startGanttChartActivity();
        }

    }

    @Override
    public void renderList(List<Task> taskList) {
        this.taskList = taskList;
        initialiazeUnitSet();
        mPresenter.initializeViewPager();
    }

    @Override
    public void updateFragment(List<Task> taskList, int pagerPosition) {
        TaskPagerFragment tf = (TaskPagerFragment) adapter.getItem(pagerPosition);
        tf.clearAdapter();

        for (Task task:taskList){
            tf.addTask(task);
        }

    }

    private void initialiazeUnitSet(){
        this.unitSet = new TreeSet<>();
        for (Task x:taskList){
            unitSet.add(x.getUnit());
        }
    }

    @Override
    public void initializePager() {

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPresenter.filterTask(taskList,position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mPresenter.updateProgress(taskList);
        mPresenter.filterTask(taskList,viewPager.getCurrentItem());
    }

    private void setupViewPager(ViewPager viewPager){
        if(adapter==null){
            adapter = new ViewPagerAdapter(getSupportFragmentManager());
        }


        adapter.addFragment(new TaskPagerFragment(), "ALL");
        adapter.addFragment(new TaskPagerFragment(), "TODAY");
        adapter.addFragment(new TaskPagerFragment(), "RUNNING");
        adapter.addFragment(new TaskPagerFragment(), "COMPLETED");
        adapter.addFragment(new TaskPagerFragment(), "EXPIRED");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void startAddTaskActivity() {
        Intent intent = new Intent(getApplicationContext(), TaskAddActivity.class);

        ArrayList<String> unitList = new ArrayList<>();
        unitList.addAll(unitSet);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PROJECT,project);
        bundle.putStringArrayList(Constant.UNITS,unitList);

        intent.putExtras(bundle);

        startActivityForResult(intent,ADD_TASK);
    }

    @Override
    public void startEditTaskActivity(Task task) {
        Intent intent = new Intent(getApplicationContext(), TaskEditActivity.class);

        ArrayList<String> unitList = new ArrayList<>();
        unitList.addAll(unitSet);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PROJECT,project);
        bundle.putSerializable(Constant.TASK,task);
        bundle.putStringArrayList(Constant.UNITS,unitList);

        intent.putExtras(bundle);

        startActivityForResult(intent,EDIT_TASK);
    }

    @Override
    public void deleteTaskFromAdapter(Task task) {
        int position = getTaskPosition(task);

        if(position!= -1){
            taskList.remove(position);
            mPresenter.updateProgress(taskList);
            mPresenter.filterTask(taskList,viewPager.getCurrentItem());
        }
    }


    @Override
    public void startAddWorkdoneActivity() {

        Intent intent = new Intent(getApplicationContext(), AddWorkdoneActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PROJECT,project);
        bundle.putSerializable(Constant.TASK_LIST, (Serializable) taskList);
        intent.putExtras(bundle);

        startActivityForResult(intent,ADD_WORKDONE);

    }

    @Override
    public void startGanttChartActivity() {
        Intent intent = new Intent(this, GanttActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PROJECT,project);
        bundle.putSerializable(Constant.TASK_LIST, (Serializable) taskList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public String saveFile(ResponseBody responseBody) {
        return saveTaskFile("Construction Manager",project.getName(),"Tasks","task.xlsx",responseBody);
    }

    @Override
    public void updateProgress(double fp, double pp, int taskCount) {

         if(tvFinancialProgress!=null){
            tvFinancialProgress.setText(String.format("%.2f",fp)+" %");
        }

        if(tvPhysicalProgress!=null){
            tvPhysicalProgress.setText(String.format("%.2f",pp)+" %");
        }

        if(tvTaskCount!=null){
            tvTaskCount.setText(String.valueOf(taskCount));
        }

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==ADD_TASK && resultCode == RESULT_OK){
            Task task = (Task) data.getSerializableExtra(Constant.TASK);
            unitSet.add(task.getUnit());

            //Todo Update Item
            taskList.add(task);

            mPresenter.updateProgress(taskList);
            mPresenter.filterTask(taskList,viewPager.getCurrentItem());
        }

        if(requestCode==EDIT_TASK && resultCode == RESULT_OK){
            Task task = (Task) data.getSerializableExtra(Constant.TASK);
            unitSet.add(task.getUnit());

            int position = getTaskPosition(task);

            if(position!= -1){
                taskList.set(position,task);
                mPresenter.updateProgress(taskList);
                mPresenter.filterTask(taskList,viewPager.getCurrentItem());
            }

        }

        if(requestCode==ADD_WORKDONE && resultCode == RESULT_OK){
            Task task = (Task) data.getSerializableExtra(Constant.TASK);
            unitSet.add(task.getUnit());

            int position = getTaskPosition(task);

            if(position!= -1){
                taskList.set(position,task);
                mPresenter.updateProgress(taskList);
                mPresenter.filterTask(taskList,viewPager.getCurrentItem());
            }
        }
    }


    @AfterPermissionGranted(READ_WRITE_PERMISSION)
    private void requestFileAfterPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getApplicationContext(), perms)) {
            //sendDownloadRequest();
            mPresenter.downloadTaskFile(project.get_id());
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "App need to Permission for Read and Write",
                    READ_WRITE_PERMISSION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }


    private int getTaskPosition(Task task){
        for (Task x: taskList){
            if(x.get_id().equals(task.get_id())){
                return taskList.indexOf(x);
            }
        }
        return -1;
    }

    public List<Task> getTaskList(){
        return this.taskList;
    }

}
