package com.forbitbd.tasker.ui.addWorkdone;


import com.forbitbd.tasker.models.Task;
import com.forbitbd.tasker.models.WorkDone;

public interface AddWorkdoneContract {

    interface Presenter{
        boolean validate(Task task, WorkDone workdone);
        void saveWorkdone(WorkDone workdone, byte[] bytes);
        void browseClick();
        void openCalender();
        void checkAndSave();
    }

    interface View{
        void showToast(String message);
        void showError(String message);
        void clearPreError();
        void showProgressDialog();
        void hideProgressDialog();
        void complete(Task task);

        void openCamera();
        void checkAndSave();
        void openCalender();
    }
}
