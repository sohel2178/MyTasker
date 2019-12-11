package com.forbitbd.tasker.ui.gantt;

public class GanttPresenter implements GanttContract.Presenter {

    private GanttContract.View mView;

    public GanttPresenter(GanttContract.View mView) {
        this.mView = mView;
    }


    @Override
    public void initChart() {
        mView.initChart();
    }
}
