package com.forbitbd.tasker.ui.taskDetail.table;



import com.forbitbd.tasker.models.WorkDone;

import java.util.List;

public interface WorkDoneTableContract {

    interface Presenter{
        void processData(List<WorkDone> workDoneList);
    }

    interface View{
        void addItem(WorkDone dailyWorkdone);
        void clearAdpter();
        void showToast(String msg);

        void startZoomImageActivity(WorkDone workDone);
    }
}
