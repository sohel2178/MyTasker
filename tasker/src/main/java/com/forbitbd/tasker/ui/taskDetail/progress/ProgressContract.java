package com.forbitbd.tasker.ui.taskDetail.progress;




import com.forbitbd.tasker.models.ChartModel;
import com.forbitbd.tasker.models.WorkDone;

import java.util.List;

public interface ProgressContract {

    interface Presenter{
        void processList(List<WorkDone> workDoneList);
    }

    interface View{
        //updateChart(List<>)

       void updateChart(List<ChartModel> chartModelList);
    }
}
