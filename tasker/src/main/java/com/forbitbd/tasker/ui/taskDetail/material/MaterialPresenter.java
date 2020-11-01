package com.forbitbd.tasker.ui.taskDetail.material;

import android.util.Log;

import com.forbitbd.androidutils.api.ServiceGenerator;
import com.forbitbd.androidutils.models.Task;
import com.forbitbd.tasker.api.ApiClient;
import com.forbitbd.tasker.models.Material;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaterialPresenter implements MaterialContract.Presenter {

    private MaterialContract.View mView;

    public MaterialPresenter(MaterialContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getMaterialList(Task task) {
        ApiClient client = ServiceGenerator.createService(ApiClient.class);
        mView.showProgressDialog();

        client.getMaterialList(task.getProject(),task.get_id())
                .enqueue(new Callback<List<Material>>() {
                    @Override
                    public void onResponse(Call<List<Material>> call, Response<List<Material>> response) {
                        mView.hideProgressDialog();
                        if(response.isSuccessful()){
                            mView.renderAdapter(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Material>> call, Throwable t) {
                        mView.hideProgressDialog();
                    }
                });

    }
}
