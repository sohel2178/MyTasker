package com.forbitbd.tasker.ui.addWorkdone;


import android.util.Log;

import com.forbitbd.androidutils.api.ServiceGenerator;
import com.forbitbd.androidutils.utils.MyUtil;
import com.forbitbd.tasker.api.ApiClient;
import com.forbitbd.tasker.models.Task;
import com.forbitbd.tasker.models.WorkDone;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddWorkdonePresenter implements AddWorkdoneContract.Presenter {

    private AddWorkdoneContract.View mView;

    public AddWorkdonePresenter(AddWorkdoneContract.View mView) {
        this.mView = mView;
    }

    @Override
    public boolean validate(Task task, WorkDone workdone) {
        mView.clearPreError();
        if(task==null){
            mView.showToast("Please Select a Task");
            return false;
        }

        if(workdone.getAmount()==0){
            mView.showError("Volume of Workdone should not Empty or 0 ");
            return false;
        }

        if(workdone.getDate()==null){
            mView.showToast("Please Select a Date");
            return false;
        }

        if(task.getVolume_of_work_done()+workdone.getAmount()>task.getVolume_of_works()){
            mView.showError("Volume of Workdone Exceed Volume of Works");
            return false;
        }


        return true;
    }

    @Override
    public void saveWorkdone(WorkDone workdone,byte[] bytes) {

        mView.showProgressDialog();

        MultipartBody.Part part=null;

        if(bytes!=null){
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
            //MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
            // RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), bytes);
            // Create MultipartBody.Part using file request-body,file name and part name
            part = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        }

        RequestBody task = RequestBody.create(MediaType.parse("text/plain"), workdone.getTask());
        RequestBody project = RequestBody.create(MediaType.parse("text/plain"), workdone.getProject());
        RequestBody date = RequestBody.create(MediaType.parse("text/plain"), MyUtil.getStringDate(workdone.getDate()));
        RequestBody amount = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(workdone.getAmount()));

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("task", task);
        map.put("project", project);
        map.put("date", date);
        map.put("amount", amount);


        ApiClient client = ServiceGenerator.createService(ApiClient.class);

        client.saveWorkdone(workdone.getProject(),workdone.getTask(),part,map)
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
                        Log.d("HHHHHHH","Error "+t.getMessage());
                    }
                });

    }

    @Override
    public void browseClick() {
        mView.openCamera();
    }

    @Override
    public void openCalender() {
        mView.openCalender();
    }

    @Override
    public void checkAndSave() {
        mView.checkAndSave();
    }
}
