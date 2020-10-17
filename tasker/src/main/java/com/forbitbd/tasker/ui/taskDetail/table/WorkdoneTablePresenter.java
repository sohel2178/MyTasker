package com.forbitbd.tasker.ui.taskDetail.table;



import com.forbitbd.androidutils.utils.MyUtil;
import com.forbitbd.tasker.models.DailyWorkdone;
import com.forbitbd.tasker.models.WorkDone;

import java.util.ArrayList;
import java.util.List;

public class WorkdoneTablePresenter implements WorkDoneTableContract.Presenter {

    private WorkDoneTableContract.View mvView;

    private List<DailyWorkdone> dailyWorkdoneList;

    public WorkdoneTablePresenter(WorkDoneTableContract.View mvView) {
        this.mvView = mvView;
        this.dailyWorkdoneList = new ArrayList<>();
    }

    @Override
    public void processData(List<WorkDone> workDoneList) {

        mvView.clearAdpter();

        for (WorkDone x: workDoneList){
            mvView.addItem(x);
        }

    }

    private void processGroup(List<WorkDone> workDoneList){
        DailyWorkdone dailyWorkdone = new DailyWorkdone(MyUtil.getStringDate(workDoneList.get(0).getDate()));

        for (WorkDone x: workDoneList){
            dailyWorkdone.add(x.getAmount());
        }

        dailyWorkdoneList.add(dailyWorkdone);

    }
}
