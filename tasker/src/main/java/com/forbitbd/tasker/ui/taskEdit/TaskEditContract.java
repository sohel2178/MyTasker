package com.forbitbd.tasker.ui.taskEdit;


import com.forbitbd.tasker.models.Task;

public interface TaskEditContract {

    interface Presenter{
        boolean validate(Task task);
        void bindTask(Task task);
        void updateTask(Task task);
        void openStartDateCalender();
        void openFinishedDateCalender();
        void checkAndSave();
    }

    interface View{

        void showProgressDialog();
        void hideProgressDialog();
        void complete(Task task);

        void bindTask(Task task);

        void openStartDateCalender();
        void openFinishedDateCalender();
        void checkAndSave();

        void clearPreError();
        void showErrorMessage(String message, int field);
        void showToast(String message);
    }
}
