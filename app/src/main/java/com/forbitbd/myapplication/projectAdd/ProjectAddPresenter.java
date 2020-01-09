package com.forbitbd.myapplication.projectAdd;

import android.util.Log;

import com.forbitbd.androidutils.models.Project;
import com.forbitbd.androidutils.models.User;
import com.forbitbd.tasker.api.ApiClient;
import com.forbitbd.tasker.api.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectAddPresenter implements ProjectAddContract.Presenter {

    private ProjectAddContract.View mView;
    private User user;

    public ProjectAddPresenter(ProjectAddContract.View mView, User user) {
        this.mView = mView;
        this.user = user;
    }

    @Override
    public boolean validate(Project project) {
        mView.clearPreError();

        if(project.getName().equals("") || project.getName()==null){
            mView.showValidationError("Project Name Empty",1);
            return false;
        }

        if(project.getLocation().equals("") || project.getLocation()==null){
            mView.showValidationError("Project Location Empty",2);
            return false;
        }

        if(project.getDescription().equals("") || project.getDescription()==null){
            mView.showValidationError("Project Description Empty",3);
            return false;
        }



        if(project.getStart_date()==null){
            mView.showValidationError("Select Project Start Date",4);
            return false;
        }

        if(project.getCompletion_date()==null){
            mView.showValidationError("Select Project Completion Date",5);
            return false;
        }

        if(project.getStart_date().getTime()>=project.getCompletion_date().getTime()){
            mView.showValidationError("Completion Date Must be Greater than Start Date",5);
        }



        return true;
    }

    @Override
    public void addProjectToDatabase(Project project) {
        project.setUser(user.get_id());

        mView.showDialog();
        ApiClient client = ServiceGenerator.createService(ApiClient.class);

        Call<Project> call = client.createProject(project);

        call.enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                mView.hideDialog();
                if(response.isSuccessful()){
                    if(response.code()==201){
                        Project project = response.body();
                        mView.addProject(project);
                    }else{
                        mView.showToast("Error in Network Oparation");
                    }
                }
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {
                mView.hideDialog();
                mView.showToast("Error Occure in Creating Project");
            }
        });
    }

    @Override
    public void bindProject(Project project) {
        mView.bindProject(project);
    }

    @Override
    public void updateProject(Project project) {
        mView.showDialog();

        ApiClient client = ServiceGenerator.createService(ApiClient.class);
        client.updateProject(project.get_id(),project)
                .enqueue(new Callback<Project>() {
                    @Override
                    public void onResponse(Call<Project> call, Response<Project> response) {
                        mView.hideDialog();
                        if(response.isSuccessful()){
                            Log.d("UUUUU","Update Successful");
                            mView.updateProject(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Project> call, Throwable t) {
                        mView.hideDialog();

                    }
                });

    }
}
