package com.forbitbd.myapplication;

import com.forbitbd.androidutils.models.Project;

import java.util.List;

public interface MainContract {

    interface Presenter{
        void getAllProjects(String uid);
    }


    interface View{
        void showProgressDialog();
        void hideProgressDialog();
        void renderAdapter(List<Project> projectList);
        void startTaskActivity(Project project);
    }
}
