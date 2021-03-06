package com.forbitbd.tasker.ui;


import com.forbitbd.androidutils.models.Task;

import java.util.List;

import okhttp3.ResponseBody;


public interface TaskContract {

    interface Presenter{
        void getProjectTask(String projectId);
        void uploadBulk(String projectId,List<Task> taskList);
        boolean validate(Task task);
        void initializeViewPager();
        void showTapTargetView();
        void showInstructionDialog();
        void startAddTaskActivity();
        void startAddWorkdoneActivity();
        void startGanttChartActivity();
        void updateProgress(List<Task> taskList);
        void filterTask(List<Task> taskList, int viewPagerPosition);
        void downloadTaskFile(String projectId);
        void validateTaskList(List<Task> taskList);
    }

    interface View {
        void renderList(List<Task> taskList);
        void showInfoDialog(List<Task> taskList);
        void updateFragment(List<Task> taskList, int pagerPosition);
        void initializePager();
        void startAddTaskActivity();
        void updateProgress(double fp, double pp, int taskCount);

        void startEditTaskActivity(Task task);

        void deleteTaskFromAdapter(Task task);

        void startAddWorkdoneActivity();
        void startGanttChartActivity();

        void showProgressDialog();
        void hideProgressDialog();
        void showInstructionDialog();
        void showTapTargetView();

        String saveFile(ResponseBody responseBody);
        void openFile(String path);
    }
}
