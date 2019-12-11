package com.forbitbd.tasker.ui.taskDetail;



import com.forbitbd.androidutils.api.ServiceGenerator;
import com.forbitbd.tasker.api.ApiClient;
import com.forbitbd.tasker.models.Task;
import com.forbitbd.tasker.models.WorkDone;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskDetailPresenter implements TaskDetailContract.Presenter {

    private TaskDetailContract.View mView;

    public TaskDetailPresenter(TaskDetailContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getAllWorkdone(Task task) {
        mView.showProgressDialog();

        ApiClient client = ServiceGenerator.createService(ApiClient.class);

        client.getTaskWorkdones(task.getProject(),task.get_id())
                .enqueue(new Callback<List<WorkDone>>() {
                    @Override
                    public void onResponse(Call<List<WorkDone>> call, Response<List<WorkDone>> response) {
                        mView.hideProgressDialog();

                        if(response.isSuccessful()){
                            mView.initializeViewpager(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<WorkDone>> call, Throwable t) {
                        mView.hideProgressDialog();
                    }
                });
    }
}
