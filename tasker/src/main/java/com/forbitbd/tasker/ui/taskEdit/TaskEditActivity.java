package com.forbitbd.tasker.ui.taskEdit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.forbitbd.androidutils.dialog.DatePickerListener;
import com.forbitbd.androidutils.dialog.MyDatePickerFragment;
import com.forbitbd.androidutils.models.Project;
import com.forbitbd.androidutils.utils.Constant;
import com.forbitbd.androidutils.utils.MyUtil;
import com.forbitbd.androidutils.utils.PrebaseActivity;
import com.forbitbd.tasker.R;
import com.forbitbd.tasker.models.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;


import java.util.Date;
import java.util.List;

public class TaskEditActivity extends PrebaseActivity implements TaskEditContract.View, View.OnClickListener {

    private EditText etName,etVolofWork,etUnitRate,etStartDate,etFinishedDate;
    private TextInputLayout tName,tVolofWork,tUnitRate,tStartDate,tFinishedDate,tUnit;
    private AutoCompleteTextView etUnit;
    private MaterialButton btnAdd;


    private ArrayAdapter<String> unitAdapter;

    private TaskEditPresenter mPresenter;

    private Project project;
    private List<String> unitSet;

    private Task task;


    private Date startDate,finishedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        mPresenter = new TaskEditPresenter(this);

        this.project = (Project) getIntent().getSerializableExtra(Constant.PROJECT);
        this.unitSet = getIntent().getStringArrayListExtra(Constant.UNITS);
        this.task = (Task) getIntent().getSerializableExtra(Constant.TASK);


        unitAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,unitSet);


        initView();
    }

    private void initView() {

        setupToolbar(R.id.toolbar);
        getSupportActionBar().setTitle(getString(R.string.edit_task));


        // Main View
        etName = findViewById(R.id.et_task_name);
        etVolofWork = findViewById(R.id.et_task_vol_of_work);
        etUnitRate = findViewById(R.id.et_unit_rate);
        etStartDate = findViewById(R.id.et_start_date);
        etFinishedDate = findViewById(R.id.et_finished_date);
        etUnit = findViewById(R.id.unit);
        etUnit.setThreshold(1);
        etUnit.setAdapter(unitAdapter);

        tName = findViewById(R.id.ti_task_name);
        tVolofWork = findViewById(R.id.ti_task_vol_of_work);
        tUnitRate = findViewById(R.id.ti_unit_rate);
        tUnit = findViewById(R.id.ti_unit);
        tStartDate = findViewById(R.id.ti_start_date);
        tFinishedDate = findViewById(R.id.ti_finished_date);


        btnAdd = findViewById(R.id.btn_add);

        etStartDate.setOnClickListener(this);
        etFinishedDate.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        mPresenter.bindTask(task);


    }

    @Override
    public void onClick(View view) {

        if(view==etStartDate){
            mPresenter.openStartDateCalender();
        }else if(view==etFinishedDate){
            mPresenter.openFinishedDateCalender();
        }else if(view==btnAdd){
            mPresenter.checkAndSave();
        }
    }

    @Override
    public void complete(Task task) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.TASK,task);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);

        finish();
    }

    @Override
    public void bindTask(Task task) {
        etName.setText(task.getName());
        etVolofWork.setText(String.valueOf(task.getVolume_of_works()));
        etUnitRate.setText(String.valueOf(task.getUnit_rate()));
        etUnit.setText(String.valueOf(task.getUnit()));
        etStartDate.setText(MyUtil.getStringDate(task.getStart_date()));
        etFinishedDate.setText(MyUtil.getStringDate(task.getFinished_date()));
    }

    @Override
    public void openStartDateCalender() {
        MyDatePickerFragment myDateDialog = new MyDatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TITLE,getString(R.string.select_start_date));
        myDateDialog.setArguments(bundle);
        myDateDialog.setCancelable(false);
        myDateDialog.setDatePickerListener(new DatePickerListener() {
            @Override
            public void onDatePick(long time) {
                startDate = new Date(time);
                etStartDate.setText(MyUtil.getStringDate(startDate));
            }
        });
        myDateDialog.show(getSupportFragmentManager(),"FFFF");
    }

    @Override
    public void openFinishedDateCalender() {
        MyDatePickerFragment myDateDialog = new MyDatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TITLE,getString(R.string.select_finished_date));
        myDateDialog.setArguments(bundle);
        myDateDialog.setCancelable(false);
        myDateDialog.setDatePickerListener(new DatePickerListener() {
            @Override
            public void onDatePick(long time) {
                finishedDate = new Date(time);
                etFinishedDate.setText(MyUtil.getStringDate(finishedDate));
            }
        });
        myDateDialog.show(getSupportFragmentManager(),"FFFF");
    }

    @Override
    public void checkAndSave() {
        String name = etName.getText().toString().trim();
        String volOfWork = etVolofWork.getText().toString().trim();
        String unitRate = etUnitRate.getText().toString().trim();
        final String unit = etUnit.getText().toString().trim();

        task.setProject(project.get_id());
        task.setName(name);
        task.setUnit(unit);

        if(startDate!=null){
            task.setStart_date(startDate);
        }

        if(finishedDate!=null){
            task.setFinished_date(finishedDate);
        }



        try {
            task.setVolume_of_works(Double.parseDouble(volOfWork));
            task.setUnit_rate(Double.parseDouble(unitRate));
        }catch (Exception e){

        }

        boolean valid = mPresenter.validate(task);

        if(!valid){
            return;
        }



        mPresenter.updateTask(task);
    }

    @Override
    public void clearPreError() {
        tName.setErrorEnabled(false);
        tVolofWork.setErrorEnabled(false);
        tUnitRate.setErrorEnabled(false);
        tUnit.setErrorEnabled(false);
        tStartDate.setErrorEnabled(false);
        tFinishedDate.setErrorEnabled(false);
    }

    @Override
    public void showErrorMessage(String message, int field) {
        switch (field){
            case 1:
                etName.requestFocus();
                tName.setError(message);
                break;

            case 2:
                etVolofWork.requestFocus();
                tVolofWork.setError(message);
                break;

            case 3:
                etUnitRate.requestFocus();
                tUnitRate.setError(message);
                break;

            case 4:
                etUnit.requestFocus();
                tUnit.setError(message);
                break;

            case 5:
                tStartDate.setError(message);
                break;

            case 6:
                tFinishedDate.setError(message);
                break;
        }
    }
}
