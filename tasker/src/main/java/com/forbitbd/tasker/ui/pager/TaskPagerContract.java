package com.forbitbd.tasker.ui.pager;



import com.forbitbd.androidutils.models.Task;

import java.util.List;

public interface TaskPagerContract {

    interface Presenter {
        void startTaskDetailActivity();
        void editTask();
        void callViewShowDialog();
        void deleteTask(Task task);
        void startFiltering(List<Task> taskList, int fragmentNo);
    }

    interface View{
        void startTaskDetailActivity(Task task);
        void startEditTask(Task task);
        void deleteTask(Task task);
        void showDeleteDialog(String title);
        void showProgressDialog();
        void hideProgressDialog();
        void sendTaskBackToTheActivity(Task task);
    }
}
