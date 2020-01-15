package com.forbitbd.tasker.ui.addWorkdone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import com.forbitbd.androidutils.dialog.DatePickerListener;
import com.forbitbd.androidutils.dialog.MyDatePickerFragment;
import com.forbitbd.androidutils.models.Project;
import com.forbitbd.androidutils.models.Task;
import com.forbitbd.androidutils.utils.Constant;
import com.forbitbd.androidutils.utils.MyUtil;
import com.forbitbd.androidutils.utils.PrebaseActivity;
import com.forbitbd.tasker.R;
import com.forbitbd.tasker.models.WorkDone;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class AddWorkdoneActivity extends PrebaseActivity implements AddWorkdoneContract.View , View.OnClickListener {

    private AddWorkdonePresenter mPresenter;


    private EditText etDate,etVolOfWorkDone;
    private TextInputLayout tDate,tVolOfWorkDone;
    private AppCompatSpinner spTasks;

    private ImageView ivImage;

    private MaterialButton btn_save,btn_browse;

    private Date date;

    private WorkDone workDone;
    private Project project;

    private List<Task> taskList;

    private byte[] bytes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workdone);
        this.mPresenter = new AddWorkdonePresenter(this);

        this.project = (Project) getIntent().getSerializableExtra(Constant.PROJECT);
        this.taskList = (List<Task>) getIntent().getSerializableExtra(Constant.TASK_LIST);

        initView();
    }

    private void initView() {
        setupToolbar(R.id.toolbar);
        getSupportActionBar().setTitle("Add Workdone");
        btn_save = findViewById(R.id.save);
        btn_browse = findViewById(R.id.browse);
        etDate = findViewById(R.id.et_date);

        etVolOfWorkDone = findViewById(R.id.vol_of_work_done);
        tVolOfWorkDone = findViewById(R.id.ti_vol_of_work_done);
        ivImage = findViewById(R.id.image);

        spTasks = findViewById(R.id.task);
        ArrayAdapter<Task> taskAdapter = new ArrayAdapter<Task>(this,android.R.layout.simple_list_item_1,taskList);
        spTasks.setAdapter(taskAdapter);

        etDate.setOnClickListener(this);
        btn_browse.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void showError(String message) {
        tVolOfWorkDone.setError(message);
    }

    @Override
    public void clearPreError() {
        tVolOfWorkDone.setErrorEnabled(false);
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
    public void openCamera() {
        openCropImageActivity(9,16);
    }

    @Override
    public void checkAndSave() {
        if(workDone==null){
            workDone = new WorkDone();
        }

        String workDoneStr = etVolOfWorkDone.getText().toString().trim();
        double amount = 0;

        try {
            amount = Double.parseDouble(workDoneStr);
            Log.d("HHHHHHH","Called");
        }catch (Exception e){
            Log.d("HHHHHHH","Called in Errror");
        }

        workDone.setAmount(amount);
        workDone.setProject(project.get_id());
        workDone.setTask(((Task) spTasks.getSelectedItem()).get_id());

        if(date !=null){
            workDone.setDate(date);
        }

        boolean valid = mPresenter.validate((Task) spTasks.getSelectedItem(),workDone);

        if(!valid){
            return;
        }

        if(ivImage.getDrawable()==null){
            showToast("Browse to Select an Employee Image");
            return;
        }

        mPresenter.saveWorkdone(workDone,bytes);
    }

    @Override
    public void openCalender() {
        MyDatePickerFragment myDateDialog = new MyDatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TITLE,getString(R.string.select_work_done_date));
        myDateDialog.setArguments(bundle);
        myDateDialog.setCancelable(false);
        myDateDialog.setDatePickerListener(new DatePickerListener() {
            @Override
            public void onDatePick(long time) {
                date = new Date(time);
                etDate.setText(MyUtil.getStringDate(date));
            }
        });
        myDateDialog.show(getSupportFragmentManager(),"FFFF");
    }

    @Override
    public void onClick(View view) {

        if(view==etDate){
            mPresenter.openCalender();
        }else if(view==btn_browse){
            mPresenter.browseClick();
        }if(view==btn_save){
            mPresenter.checkAndSave();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());

                    if(bitmap.getWidth()<576){
                        showToast("Should Select Larger Image !");
                        return;
                    }
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80 /*ignored for PNG*/, bos);
                    bytes = bos.toByteArray();
                    ivImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
