package com.forbitbd.tasker.ui.pager;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forbitbd.androidutils.dialog.delete.DeleteDialog;
import com.forbitbd.androidutils.dialog.delete.DialogClickListener;
import com.forbitbd.androidutils.models.Project;
import com.forbitbd.androidutils.utils.Constant;
import com.forbitbd.tasker.R;
import com.forbitbd.tasker.models.Task;
import com.forbitbd.tasker.ui.TaskActivity;
import com.forbitbd.tasker.ui.taskDetail.TaskDetailActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskPagerFragment extends Fragment implements TaskPagerContract.View{


    private RecyclerView rvTask;
    private LinearLayoutManager manager;

    private TaskAdapter adapter;



    private TaskPagerPresenter mPresenter;

    private Project project;

    private TaskActivity activity;

    private int counter;




    private DeleteDialog deleteDialog;



    public TaskPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new TaskPagerPresenter(this);

        if(getActivity() instanceof TaskActivity){
            activity = (TaskActivity) getActivity();
            project = activity.getProject();
            adapter = new TaskAdapter(this);
        }


    }

    public void clearAdapter(){
        if(adapter!=null){
            adapter.clear();
        }
    }

    public void addTask(Task task){

        if(adapter!=null){
            adapter.addTask(task);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        initView(view);
        return view;
    }



    private void initView(View view) {

        rvTask = view.findViewById(R.id.rv_task);
        manager = new LinearLayoutManager(getContext());
        rvTask.setLayoutManager(manager);
        //rvTask.setNestedScrollingEnabled(false);
        rvTask.setAdapter(adapter);
    }



    public void updateUI(double fp, double pp,int taskCount){


    }







   /* @Override
    public void onEditClick(Task task) {
        this.action=EDIT_CLICK;
        this.selectedTask = task;

        mPresenter.editTask();

    }*/

    /*@Override
    public void onDeleteClick(Task task) {
        this.action=DELETE_CLICK;
        this.selectedTask = task;

        counter++;
        mPresenter.callViewShowDialog();

    }*/

    /*@Override
    public void setupAdCloseListener() {

        getmInterstitialAd().setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();

                getmInterstitialAd().loadAd(new AdRequest.Builder().build());

                switch (action){
                    case VIEW_CLICK:
                        mPresenter.callViewToStartTaskDetailActivity();
                        break;

                    case EDIT_CLICK:
                        mPresenter.callViewToEditTask();
                        break;

                    case DELETE_CLICK:
                        mPresenter.callViewShowDialog();
                        break;
                }
            }
        });
    }*/

    @Override
    public void startTaskDetailActivity(Task task) {
        //activity.startTaskDetailActivity(task);

        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.TASK,task);

        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void startEditTask(Task task) {
        activity.startEditTaskActivity(task);
    }


    @Override
    public void deleteTask(final Task task) {
        deleteDialog = new DeleteDialog();
        deleteDialog.setCancelable(false);

        Bundle bundle = new Bundle();
        bundle.putString(Constant.CONTENT,"Do you really want to delete this Task?");
        deleteDialog.setArguments(bundle);

        deleteDialog.setListener(new DialogClickListener() {
            @Override
            public void positiveButtonClick() {
                deleteDialog.dismiss();
                mPresenter.deleteTask(task);
            }
        });
        deleteDialog.show(getFragmentManager(),"HHHH");

    }

    @Override
    public void showDeleteDialog(String title) {

    }

    @Override
    public void showProgressDialog() {
        activity.showProgressDialog();
    }

    @Override
    public void hideProgressDialog() {
        activity.hideProgressDialog();
    }

    @Override
    public void sendTaskBackToTheActivity(Task task) {
        activity.deleteTaskFromAdapter(task);
    }


}
