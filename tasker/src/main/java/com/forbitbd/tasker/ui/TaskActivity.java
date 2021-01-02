package com.forbitbd.tasker.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;


import com.forbitbd.androidutils.dialog.delete.DeleteDialog;
import com.forbitbd.androidutils.dialog.delete.DialogClickListener;
import com.forbitbd.androidutils.models.SharedProject;
import com.forbitbd.androidutils.models.Task;
import com.forbitbd.androidutils.utils.AppPreference;
import com.forbitbd.androidutils.utils.Constant;
import com.forbitbd.androidutils.utils.PrebaseActivity;
import com.forbitbd.androidutils.utils.ViewPagerAdapter;
import com.forbitbd.tasker.R;
import com.forbitbd.tasker.ui.addWorkdone.AddWorkdoneActivity;
import com.forbitbd.tasker.ui.bulkEntryHelp.BulkEntryHelpDialog;
import com.forbitbd.tasker.ui.gantt.GanttActivity;
import com.forbitbd.tasker.ui.pager.TaskPagerFragment;
import com.forbitbd.tasker.ui.taskAdd.TaskAddActivity;
import com.forbitbd.tasker.ui.taskEdit.TaskEditActivity;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.obsez.android.lib.filechooser.ChooserDialog;
import com.opencsv.CSVReader;
import com.ramotion.foldingcell.FoldingCell;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
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
    private static final int READ_WRITE =7500;

    private TaskPresenter mPresenter;

    private List<Task> taskList;

    private SharedProject sharedProject;

    private TreeSet<String> unitSet;

    private TextView tvProjectLocation,tvProjectDescription,tvPhysicalProgress,tvFinancialProgress,tvTaskCount;
    private FoldingCell mFoldingCell;

    private ViewPagerAdapter adapter;
    private ViewPager viewPager;

    private FloatingActionButton fabAdd,fabGantt,fabWorkdone;

    private MenuItem menuUpload,menuInstruction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        this.mPresenter = new TaskPresenter(this);

        this.sharedProject = (SharedProject) getIntent().getSerializableExtra(Constant.PROJECT);

        initView();
    }

    private void initView() {
        setupToolbar(R.id.toolbar);
        setupBannerAd(R.id.adView);

        getSupportActionBar().setTitle(sharedProject.getProject().getName());



        fabAdd = findViewById(R.id.fab_add);
        fabGantt = findViewById(R.id.fab_gantt_chart);
        fabWorkdone = findViewById(R.id.fab_add_workdone);

        mFoldingCell = findViewById(R.id.folding_cell);

        tvProjectLocation = findViewById(R.id.project_location);
        tvProjectDescription = findViewById(R.id.project_desc);
        tvPhysicalProgress = findViewById(R.id.physical_progress);
        tvFinancialProgress = findViewById(R.id.financial_progress);
        tvTaskCount = findViewById(R.id.task_count);

        tvProjectLocation.setText(sharedProject.getProject().getLocation());
        tvProjectDescription.setText(sharedProject.getProject().getDescription());

        fabAdd.setOnClickListener(this);
        fabGantt.setOnClickListener(this);
        fabWorkdone.setOnClickListener(this);
        mFoldingCell.setOnClickListener(this);

        mPresenter.getProjectTask(sharedProject.getProject().get_id());

        // Permission Base Visibility
        if(!sharedProject.getActivity().isWrite()){
            fabAdd.setVisibility(View.GONE);
            fabWorkdone.setVisibility(View.GONE);
        }


    }

    public SharedProject getSharedProject(){
        return this.sharedProject;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(AppPreference.getInstance(this).getCounter()>Constant.COUNTER){
            showInterAd();
        }
    }

    @Override
    public void onClick(View view) {

        if(view==fabAdd){
            AppPreference.getInstance(this).increaseCounter();
            mPresenter.startAddTaskActivity();
        }else if(view==mFoldingCell){
            mFoldingCell.toggle(false);
        }else if(view==fabWorkdone){
            AppPreference.getInstance(this).increaseCounter();
            mPresenter.startAddWorkdoneActivity();
        }else if(view==fabGantt){
            AppPreference.getInstance(this).increaseCounter();
            mPresenter.startGanttChartActivity();
        }

    }

    private void controlVisibility(){
        if(sharedProject.getActivity().isWrite()){
            menuUpload.setVisible(true);
            menuInstruction.setVisible(true);
        }else {
            menuUpload.setVisible(false);
            menuInstruction.setVisible(false);
        }
    }

    @Override
    public void renderList(List<Task> taskList) {
        this.taskList = taskList;
        initialiazeUnitSet();
        mPresenter.initializeViewPager();

        controlVisibility();

        if(taskList.size()==0){
            mPresenter.showTapTargetView();
        }

    }

    @Override
    public void showInfoDialog(List<Task> taskList) {
        DeleteDialog deleteDialog = new DeleteDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.CONTENT,taskList.size()+" valid Task Found. Would you want to add them??");
        bundle.putString(Constant.TITLE,"Bulk Entry From CSV");
        deleteDialog.setCancelable(false);
        deleteDialog.setArguments(bundle);
        deleteDialog.setListener(new DialogClickListener() {
            @Override
            public void positiveButtonClick() {
                Log.d("Task",taskList.size()+"");
                deleteDialog.dismiss();
                mPresenter.uploadBulk(sharedProject.getProject().get_id(),taskList);
            }
        });

        deleteDialog.show(getSupportFragmentManager(),"DELETE");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_menu, menu);
        menuUpload = menu.findItem(R.id.action_upload);
        menuInstruction = menu.findItem(R.id.action_instruction);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);

       if(item.getItemId()==R.id.action_upload){
           getReadWritePermission();
           return true;
       }else if(item.getItemId()==R.id.action_download){
           requestFileAfterPermission();
           return true;
       }else if(item.getItemId()==R.id.action_instruction){
           mPresenter.showInstructionDialog();
           return true;
       }
       else{
           return super.onOptionsItemSelected(item);
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
        bundle.putSerializable(Constant.PROJECT,sharedProject.getProject());
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
        bundle.putSerializable(Constant.PROJECT,sharedProject.getProject());
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
        controlVisibility();
    }


    @Override
    public void startAddWorkdoneActivity() {

        Intent intent = new Intent(getApplicationContext(), AddWorkdoneActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PROJECT,sharedProject.getProject());
        bundle.putSerializable(Constant.TASK_LIST, (Serializable) taskList);
        intent.putExtras(bundle);

        startActivityForResult(intent,ADD_WORKDONE);

    }

    @Override
    public void startGanttChartActivity() {
        Intent intent = new Intent(this, GanttActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PROJECT,sharedProject.getProject());
        bundle.putSerializable(Constant.TASK_LIST, (Serializable) taskList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void showInstructionDialog() {
        BulkEntryHelpDialog bulkEntryHelpDialog = new BulkEntryHelpDialog();
        bulkEntryHelpDialog.setCancelable(false);
        bulkEntryHelpDialog.show(getSupportFragmentManager(),"SHOW");
    }

    @Override
    public void showTapTargetView() {

        TapTargetView.showFor(this,
                TapTarget.forView(findViewById(R.id.fab_add),"Create New Task","To Create a new Task Click the Blinking Button...")
                        .outerCircleColor(R.color.statusColor)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.colorAccent)
                        .tintTarget(false)
                        .textTypeface(Typeface.MONOSPACE)
                ,new TapTargetView.Listener(){
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        startAddTaskActivity();
                    }
                });

    }

    @Override
    public String saveFile(ResponseBody responseBody) {
        return saveTaskFile("Construction Manager",sharedProject.getProject().getName(),"Tasks","task.xlsx",responseBody);
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
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==ADD_TASK && resultCode == RESULT_OK){
            Task task = (Task) data.getSerializableExtra(Constant.TASK);
            unitSet.add(task.getUnit());

            //Todo Update Item
            taskList.add(task);

            mPresenter.updateProgress(taskList);
            mPresenter.filterTask(taskList,viewPager.getCurrentItem());

            controlVisibility();
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
            mPresenter.downloadTaskFile(sharedProject.getProject().get_id());
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


    @AfterPermissionGranted(READ_WRITE)
    private void getReadWritePermission() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            openFileDialog();

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Need READ and Write Permission",
                    READ_WRITE, perms);
        }
    }


    private void openFileDialog(){
        new ChooserDialog(this)
                .withFilter(false,false,"csv")
                .withStartFile(Environment.getExternalStorageDirectory().getAbsolutePath())
                .withDateFormat("HH:mm")
                .withChosenListener(new ChooserDialog.Result() {
                    @Override
                    public void onChoosePath(String dir, File dirFile) {
//                        Toast.makeText(FileChooserActivity.this, "FOLDER: " + dir, Toast.LENGTH_SHORT).show();

                        try {
                            CSVReader reader = new CSVReader(new FileReader(dir));
                            String [] nextLine;
                            List<Task> taskList = new ArrayList<>();
                            while ((nextLine = reader.readNext()) != null) {
                                // nextLine[] is an array of values from the line
                                System.out.println(nextLine[0] + nextLine[1] + "etc...");

                               Task task = new Task();
                               task.setName(nextLine[0].trim());
                               task.setVolume_of_works(Double.parseDouble(nextLine[1].trim()));
                               task.setUnit_rate(Double.parseDouble(nextLine[2].trim()));
                               task.setUnit(nextLine[3].trim());
                               task.setStart_date(getDate(nextLine[4]));
                               task.setFinished_date(getDate(nextLine[5]));
                               task.setProject(sharedProject.getProject().get_id());

                               taskList.add(task);

                            }

                            mPresenter.validateTaskList(taskList);

                            //Log.d("HHHHHH",taskList.size()+"");

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .build()
                .show();

    }

    private Date getDate(String date){
        String[] hhh = date.split("/");

        if(hhh.length!=3){
            hhh = date.split("-");
        }

        try {
            int year = Integer.parseInt(hhh[2].trim());
            int month = Integer.parseInt(hhh[1].trim())-1;
            int day = Integer.parseInt(hhh[0].trim());
            return new GregorianCalendar(year, month, day).getTime();
        }catch (Exception e){
            Log.d("YYYYYY",e.getMessage()+"");
            return null;
        }

    }

}
