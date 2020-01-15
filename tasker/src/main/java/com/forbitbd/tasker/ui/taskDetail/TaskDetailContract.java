package com.forbitbd.tasker.ui.taskDetail;



import com.forbitbd.androidutils.models.Task;
import com.forbitbd.tasker.models.WorkDone;

import java.util.List;

public interface TaskDetailContract {

    interface Presenter{
        void getAllWorkdone(Task task);
    }


    interface View{
        void showProgressDialog();
        void hideProgressDialog();
        void initializeViewpager(List<WorkDone> workDoneList);
    }
}
