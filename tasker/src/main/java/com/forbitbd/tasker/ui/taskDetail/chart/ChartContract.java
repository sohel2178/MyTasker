package com.forbitbd.tasker.ui.taskDetail.chart;






import com.forbitbd.tasker.models.WorkDone;

import java.util.List;

public interface ChartContract {
    interface Presenter{
       void processWorkDoneList(List<WorkDone> workDoneList);
       void destroy();
    }

    interface View{
        void showChart(List<WorkDone> workDoneList);
    }
}
