package com.forbitbd.tasker.ui.taskAdd;


import com.forbitbd.androidutils.models.Task;

public interface TaskAddContract {

    interface Presenter{
        boolean validate(Task task);
        void saveTask(Task task);
        void openStartDateCalender();
        void openFinishedDateCalender();
        void checkAndSave();
    }

    interface View{

        void showProgressDialog();
        void hideProgressDialog();
        void complete(Task task);

        void openStartDateCalender();
        void openFinishedDateCalender();
        void checkAndSave();

        void clearPreError();
        void showErrorMessage(String message, int field);
        void showToast(String message);
    }
}
