package com.forbitbd.myapplication.projectAdd;


import com.forbitbd.androidutils.models.Project;

public interface ProjectAddContract {

    interface Presenter{
        boolean validate(Project project);
        void addProjectToDatabase(Project project);
        void bindProject(Project project);
        void updateProject(Project project);
    }

    interface View{
        void showValidationError(String message, int fieldId);
        void showDialog();
        void hideDialog();
        void clearPreError();
        void showToast(String message);
        void bindProject(Project project);
        void addProject(Project project);
        void updateProject(Project project);
    }
}
