package com.forbitbd.myapplication;

import com.forbitbd.androidutils.models.Project;
import com.forbitbd.tasker.api.ApiClient;
import com.forbitbd.tasker.api.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    public MainPresenter(MainContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getAllProjects(String uid) {
        mView.showProgressDialog();

        ApiClient client = ServiceGenerator.createService(ApiClient.class);
        client.getUserProjects(uid)
                .enqueue(new Callback<List<Project>>() {
                    @Override
                    public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                        mView.hideProgressDialog();

                        if(response.isSuccessful()){
                            mView.renderAdapter(response.body());
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Project>> call, Throwable t) {
                        mView.hideProgressDialog();
                    }
                });
    }
}
