package com.forbitbd.tasker.ui.taskDetail.table;

import android.util.Log;


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
        Log.d("SohelMAMA",workDoneList.size()+"");

        mvView.clearAdpter();

        for (WorkDone x: workDoneList){
            mvView.addItem(x);
        }

        /*dailyWorkdoneList.clear();

          Observable.fromIterable(workDoneList)
                    .groupBy(r-> MyUtil.getStringDate(r.getDate()))
                    .flatMapSingle(Observable::toList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(group-> processGroup(group)
                            ,err->err.printStackTrace()
                    ,()->sortData());*/
    }

    private void processGroup(List<WorkDone> workDoneList){
        DailyWorkdone dailyWorkdone = new DailyWorkdone(MyUtil.getStringDate(workDoneList.get(0).getDate()));

        for (WorkDone x: workDoneList){
            dailyWorkdone.add(x.getAmount());
        }

        dailyWorkdoneList.add(dailyWorkdone);

       //
    }

  /*  private void sortData(){
        Collections.sort(dailyWorkdoneList, new Comparator<DailyWorkdone>() {
            @Override
            public int compare(DailyWorkdone dailyWorkdone, DailyWorkdone t1) {
                try {
                    return MyUtil.getDate(dailyWorkdone.getDate()).compareTo(MyUtil.getDate(t1.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        for (DailyWorkdone dailyWorkdone :dailyWorkdoneList){
            mvView.addItem(dailyWorkdone);
        }
    }*/
}
