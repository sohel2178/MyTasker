package com.forbitbd.tasker.ui;


import com.forbitbd.androidutils.models.Task;
import com.forbitbd.tasker.api.ApiClient;
import com.forbitbd.tasker.api.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskPresenter implements TaskContract.Presenter {

    private TaskContract.View mView;

    public TaskPresenter(TaskContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getProjectTask(String projectId) {
        mView.showProgressDialog();
        ApiClient client = ServiceGenerator.createService(ApiClient.class);

        client.getProjectTasks(projectId)
                .enqueue(new Callback<List<Task>>() {
                    @Override
                    public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                        mView.hideProgressDialog();
                        if(response.isSuccessful()){
                            mView.renderList(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Task>> call, Throwable t) {
                        mView.hideProgressDialog();
                    }
                });
    }

    @Override
    public void initializeViewPager() {
        mView.initializePager();
    }

    @Override
    public void startAddTaskActivity() {
        mView.startAddTaskActivity();
    }

    @Override
    public void startAddWorkdoneActivity() {
        mView.startAddWorkdoneActivity();
    }

    @Override
    public void startGanttChartActivity() {
        mView.startGanttChartActivity();
    }

    @Override
    public void updateProgress(List<Task> taskList) {
        double totalWorkdone=0;
        double workTobeDone =0;
        double ph=0;

        for(Task task:taskList){
            workTobeDone = workTobeDone+task.getVolume_of_works()*task.getUnit_rate();
            totalWorkdone = totalWorkdone+task.getVolume_of_work_done()*task.getUnit_rate();
            ph = ph+(task.getVolume_of_work_done()/task.getVolume_of_works());
        }

        double financialProgress = totalWorkdone/workTobeDone*100;
        double physicalProgress = ph/taskList.size()*100;

        mView.updateProgress(financialProgress,physicalProgress,taskList.size());
    }

    @Override
    public void filterTask(List<Task> taskList, int viewPagerPosition) {

        List<Task> filteredList = new ArrayList<>();

        switch (viewPagerPosition){
            case 0:
                filteredList = taskList;
                break;

            case 1:
                filteredList = getTodaysList(taskList);
                break;

            case 2:
                filteredList = getRunningList(taskList);
                break;

            case 3:
                filteredList = getFinishedList(taskList);
                break;

            case 4:
                filteredList = getExpiredList(taskList);
                break;
        }

        mView.updateFragment(filteredList,viewPagerPosition);

    }

    @Override
    public void downloadTaskFile(String projectId) {
        mView.showProgressDialog();
        ApiClient client = ServiceGenerator.createService(ApiClient.class);
        client.downloadTaskFile(projectId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mView.hideProgressDialog();

                if(response.isSuccessful()){
                    String path = mView.saveFile(response.body());

                    if(path!=null){
                        mView.openFile(path);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mView.hideProgressDialog();

            }
        });

    }


    private List<Task> getTodaysList(List<Task> taskList){
        List<Task> tempList = new ArrayList<>();

        for (Task x: taskList){
            if(x.isIntersect() && !x.isFinished()){
                tempList.add(x);
            }
        }

        return tempList;
    }

    private List<Task> getRunningList(List<Task> taskList){
        List<Task> tempList = new ArrayList<>();

        for (Task x: taskList){
            if(!x.isFinished() && x.isActive()){
                tempList.add(x);
            }
        }

        return tempList;
    }

    private List<Task> getFinishedList(List<Task> taskList){
        List<Task> tempList = new ArrayList<>();

        for (Task x: taskList){
            if(x.isFinished()){
                tempList.add(x);
            }
        }

        return tempList;
    }

    private List<Task> getExpiredList(List<Task> taskList){
        List<Task> tempList = new ArrayList<>();

        for (Task x: taskList){
            if(!x.isFinished() && !x.isActive()){
                tempList.add(x);
            }
        }

        return tempList;
    }


}
