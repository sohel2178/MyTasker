package com.forbitbd.tasker.ui.pager;



import com.forbitbd.androidutils.api.ServiceGenerator;
import com.forbitbd.tasker.api.ApiClient;
import com.forbitbd.tasker.models.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskPagerPresenter implements TaskPagerContract.Presenter {

    private TaskPagerContract.View mView;

    public TaskPagerPresenter(TaskPagerContract.View mView) {
        this.mView = mView;
    }


    @Override
    public void startTaskDetailActivity() {
        //mView.startTaskDetailActivity();
    }

    @Override
    public void editTask() {
        //mView.startEditTask();
    }

    @Override
    public void callViewShowDialog() {
        mView.showDeleteDialog("Do You Really want to Delete this Task");
    }

    @Override
    public void deleteTask(Task task) {
        mView.showProgressDialog();

        ApiClient client = ServiceGenerator.createService(ApiClient.class);

        client.deleteTask(task.getProject(),task.get_id())
                .enqueue(new Callback<Task>() {
                    @Override
                    public void onResponse(Call<Task> call, Response<Task> response) {
                        mView.hideProgressDialog();

                        if(response.isSuccessful()){
                            mView.sendTaskBackToTheActivity(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Task> call, Throwable t) {
                        mView.hideProgressDialog();
                    }
                });
    }

    @Override
    public void startFiltering(List<Task> taskList, int fragmentNo) {

    }


}
