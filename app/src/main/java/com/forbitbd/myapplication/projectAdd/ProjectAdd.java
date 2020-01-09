package com.forbitbd.myapplication.projectAdd;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.forbitbd.androidutils.dialog.DatePickerListener;
import com.forbitbd.androidutils.dialog.MyDatePickerFragment;
import com.forbitbd.androidutils.models.Project;
import com.forbitbd.androidutils.utils.Constant;
import com.forbitbd.androidutils.utils.MyUtil;
import com.forbitbd.myapplication.MainActivity;
import com.forbitbd.myapplication.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;


public class ProjectAdd extends DialogFragment implements ProjectAddContract.View, View.OnClickListener {

    private EditText etProjectName,etProjectDesc,etProjectLocation,etStartDate,etCompletionDate;
    private TextInputLayout tProjectName,tProjectDesc,tProjectLocation,tStartDate,tCompletionDate;
    private TextView tvCancel,tvSave;


    private ProjectAddPresenter mPresenter;

    private Project project;
    private long startDate,completionDate;

    private MainActivity activity;


    public ProjectAdd() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() instanceof MainActivity){
            activity = (MainActivity) getActivity();
        }
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_project_add, null);
        initView(view);


        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.MyDialog).create();

        //AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.ThemeOverlay_AppCompat_Dialog);
        alertDialog.setView(view);
        return alertDialog;
    }

    private void initView(View view) {
        etProjectName = view.findViewById(R.id.et_project_name);
        etProjectDesc = view.findViewById(R.id.et_project_description);
        etProjectLocation = view.findViewById(R.id.et_project_location);
        etStartDate = view.findViewById(R.id.et_start_date);
        etCompletionDate = view.findViewById(R.id.et_completion_date);

        tProjectName = view.findViewById(R.id.ti_project_name);
        tProjectLocation = view.findViewById(R.id.ti_project_location);
        tProjectDesc= view.findViewById(R.id.ti_project_description);
        tStartDate = view.findViewById(R.id.ti_start_date);
        tCompletionDate = view.findViewById(R.id.ti_completion_date);

        tvCancel = view.findViewById(R.id.cancel);
        tvSave = view.findViewById(R.id.save);

        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);


        etStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view1, boolean b) {
                if (b) {
                    MyDatePickerFragment myDateDialog = new MyDatePickerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.TITLE, ProjectAdd.this.getString(R.string.select_start_date));
                    myDateDialog.setArguments(bundle);
                    myDateDialog.setCancelable(false);
                    myDateDialog.setDatePickerListener(new DatePickerListener() {
                        @Override
                        public void onDatePick(long time) {
                            startDate = time;
                            etStartDate.setText(MyUtil.getStringDate(new Date(time)));
                        }
                    });
                    myDateDialog.show(ProjectAdd.this.getFragmentManager(), "FFFF");
                }
            }
        });

        etCompletionDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view12, boolean b) {
                if (b) {
                    MyDatePickerFragment myDateDialog = new MyDatePickerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.TITLE, ProjectAdd.this.getString(R.string.select_completion_date));
                    myDateDialog.setArguments(bundle);
                    myDateDialog.setCancelable(false);
                    myDateDialog.setDatePickerListener(new DatePickerListener() {
                        @Override
                        public void onDatePick(long time) {
                            completionDate = time;
                            etCompletionDate.setText(MyUtil.getStringDate(new Date(time)));
                        }
                    });
                    myDateDialog.show(ProjectAdd.this.getFragmentManager(), "FFFF");
                }
            }
        });


        if(project!=null){
            mPresenter.bindProject(project);
        }

    }

    @Override
    public void showValidationError(String message, int fieldId) {
        switch (fieldId){
            case 1:
                etProjectName.requestFocus();
                tProjectName.setError(message);
                break;

            case 2:
                etProjectLocation.requestFocus();
                tProjectLocation.setError(message);
                break;

            case 3:
                etProjectDesc.requestFocus();
                tProjectDesc.setError(message);
                break;

            case 4:
                etStartDate.requestFocus();
                tStartDate.setError(message);
                break;

            case 5:
                etCompletionDate.requestFocus();
                tCompletionDate.setError(message);
                break;

        }
    }

    @Override
    public void showDialog() {
        if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            ma.showProgressDialog();
        }
    }

    @Override
    public void hideDialog() {
        if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            ma.hideProgressDialog();
        }
       dismiss();
    }

    @Override
    public void clearPreError() {
        tProjectName.setErrorEnabled(false);
        tProjectLocation.setErrorEnabled(false);
        tProjectDesc.setErrorEnabled(false);
        tStartDate.setErrorEnabled(false);
        tCompletionDate.setErrorEnabled(false);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void bindProject(Project project) {
        etProjectName.setText(project.getName());
        etProjectDesc.setText(project.getDescription());
        etProjectLocation.setText(project.getLocation());
        etStartDate.setText(MyUtil.getStringDate(project.getStart_date()));
        etCompletionDate.setText(MyUtil.getStringDate(project.getCompletion_date()));

        tvSave.setText(getString(R.string.update));

    }

    @Override
    public void addProject(Project project) {
        dismiss();
//        activity.addProject(project);
//
    }

    @Override
    public void updateProject(Project project) {
        dismiss();
//        activity.updateProject(project);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel:
                dismiss();
                break;

            case R.id.save:
                String name = etProjectName.getText().toString().trim();
                String location = etProjectLocation.getText().toString().trim();
                String description = etProjectDesc.getText().toString().trim();


                if(project==null){
                    project = new Project();
                }

                project.setName(name);
                project.setLocation(location);
                project.setDescription(description);
                if(startDate>0){
                    project.setStart_date(new Date(startDate));
                }
                if(completionDate>0){
                    project.setCompletion_date(new Date(completionDate));
                }

                boolean valid = mPresenter.validate(project);

                if(!valid){
                    return;
                }

                if(activity.isOnline()){
                    if(project.get_id()==null){
                        mPresenter.addProjectToDatabase(project);
                    }else {
                        mPresenter.updateProject(project);
                    }

                }else{
                    Toast.makeText(getContext(), "Internet Connection is not Available!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
