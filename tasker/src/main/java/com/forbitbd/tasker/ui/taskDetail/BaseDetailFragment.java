package com.forbitbd.tasker.ui.taskDetail;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.forbitbd.tasker.models.Task;
import com.forbitbd.tasker.models.WorkDone;

import java.util.List;

public class BaseDetailFragment extends Fragment {




    private TaskDetailActivity activity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() instanceof TaskDetailActivity){
            activity = (TaskDetailActivity) getActivity();
        }
    }

    public List<WorkDone> getWorkdoneList(){
        return activity.getWorkDoneList();
    }

    public double getVolumeofWorks(){

        return activity.getVolumeOfWorks();
    }

    public Task getTask(){
        return activity.getTask();
    }
}
