package com.forbitbd.tasker.ui.taskDetail.chart;






import com.forbitbd.androidutils.utils.MyUtil;
import com.forbitbd.tasker.models.WorkDone;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChartPresenter implements ChartContract.Presenter {
    private ChartContract.View mView;
    private Disposable wdDisposable;

    public ChartPresenter(ChartContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void processWorkDoneList(final List<WorkDone> workDoneList) {
        //getProcessList(workDoneList);
        Observable<List<WorkDone>> wdObs = Observable.fromCallable(new Callable<List<WorkDone>>() {
            @Override
            public List<WorkDone> call() throws Exception {
                return ChartPresenter.this.getProcessList(workDoneList);
            }
        });

        wdDisposable = wdObs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<WorkDone>>() {
                    @Override
                    public void accept(List<WorkDone> workDoneList1) throws Exception {
                        mView.showChart(workDoneList1);
                    }
                });
    }

    @Override
    public void destroy() {
        if (wdDisposable != null) {
            wdDisposable.dispose();
        }
    }


    private List<WorkDone> getProcessList(List<WorkDone> workDoneList){

        Map<String, Double> retMap = new HashMap<>();
        List<WorkDone> retList = new ArrayList<>();

        for (WorkDone x: workDoneList){
            String dateStr = MyUtil.getStringDate(x.getDate());
            Double amount = retMap.get(dateStr);

            if(amount==null){
                retMap.put(dateStr,x.getAmount());
            }else {
                retMap.put(dateStr,amount+x.getAmount());
            }


        }


        for (String key:retMap.keySet()){
            WorkDone workDone = new WorkDone();
            try {
                workDone.setDate(MyUtil.getDate(key));
                workDone.setAmount(retMap.get(key));
                retList.add(workDone);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(retList, new Comparator<WorkDone>() {
            @Override
            public int compare(WorkDone workDone, WorkDone t1) {
                return (int) (workDone.getDate().getTime() - t1.getDate().getTime());
            }
        });

        return retList;


    }




}
