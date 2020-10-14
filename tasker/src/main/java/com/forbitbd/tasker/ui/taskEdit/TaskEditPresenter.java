package com.forbitbd.tasker.ui.taskEdit;



import com.forbitbd.androidutils.api.ServiceGenerator;
import com.forbitbd.androidutils.models.Task;
import com.forbitbd.tasker.api.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskEditPresenter implements TaskEditContract.Presenter {

    private TaskEditContract.View mView;

    public TaskEditPresenter(TaskEditContract.View mView) {
        this.mView = mView;
    }

    @Override
    public boolean validate(Task task) {
        mView.clearPreError();

        if(task.getName().equals("")){
            mView.showErrorMessage("Empty Field Not Allowed",1);
            return false;
        }

        if(task.getVolume_of_works()<=0){
            mView.showErrorMessage("Empty Field Not Allowed",2);
            return false;
        }

        if(task.getUnit_rate()<=0){
            mView.showErrorMessage("Empty Field Not Allowed",3);
            return false;
        }

        if(task.getUnit().equals("")){
            mView.showErrorMessage("Empty Field Not Allowed",4);
            return false;
        }

        if(task.getStart_date()==null){
            mView.showToast("Please Select Start Date");
            return false;
        }

        if(task.getFinished_date()==null){
            mView.showToast("Please Select Finished Date");
            return false;
        }

        if(task.getStart_date().compareTo(task.getFinished_date())>0){
            mView.showToast("Finished Date Should After Start Date");
            return false;
        }

        if(task.getVolume_of_works()<task.getVolume_of_work_done()){
            mView.showErrorMessage("Volume of works should Greater than or Equal Volume of works done",2);
            return false;
        }

        return true;
    }

    @Override
    public void bindTask(Task task) {
        mView.bindTask(task);
    }

    @Override
    public void updateTask(Task task) {
        mView.showProgressDialog();

        ApiClient client = ServiceGenerator.createService(ApiClient.class);
        client.updateTask(task.getProject(),task.get_id(),task)
                .enqueue(new Callback<Task>() {
                    @Override
                    public void onResponse(Call<Task> call, Response<Task> response) {
                        mView.hideProgressDialog();

                        if(response.isSuccessful()){
                            mView.complete(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Task> call, Throwable t) {
                        mView.hideProgressDialog();
                    }
                });
    }

    @Override
    public void openStartDateCalender() {
        mView.openStartDateCalender();
    }

    @Override
    public void openFinishedDateCalender() {
        mView.openFinishedDateCalender();
    }

    @Override
    public void checkAndSave() {
        mView.checkAndSave();
    }
}
