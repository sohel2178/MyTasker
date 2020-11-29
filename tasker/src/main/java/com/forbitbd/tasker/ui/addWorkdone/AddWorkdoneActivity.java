package com.forbitbd.tasker.ui.addWorkdone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.forbitbd.androidutils.dialog.DatePickerListener;
import com.forbitbd.androidutils.dialog.MyDatePickerFragment;
import com.forbitbd.androidutils.models.Project;
import com.forbitbd.androidutils.models.Task;
import com.forbitbd.androidutils.utils.AppPreference;
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
    private AppCompatAutoCompleteTextView etTask;
    private TextInputLayout tDate,tVolOfWorkDone,tiTask;

    private ImageView ivImage;

    private MaterialButton btn_save,btn_browse;

    private Date date;

    private WorkDone workDone;
    private Project project;

    private List<Task> taskList;

    private byte[] bytes;

    private Task selectedTask;

    ArrayAdapter<Task> taskAdapter;



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
        setupBannerAd(R.id.adView);
        btn_save = findViewById(R.id.save);
        btn_browse = findViewById(R.id.browse);
        etDate = findViewById(R.id.et_date);
        etTask = findViewById(R.id.et_task);
        date = new Date();

        etTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTask =taskAdapter.getItem(i);
                etTask.setText(selectedTask.getName());
            }
        });

        etVolOfWorkDone = findViewById(R.id.vol_of_work_done);
        tVolOfWorkDone = findViewById(R.id.ti_vol_of_work_done);
        tiTask = findViewById(R.id.ti_task);
        ivImage = findViewById(R.id.image);

        taskAdapter = new ArrayAdapter<Task>(this,android.R.layout.simple_list_item_1,taskList);
        etTask.setAdapter(taskAdapter);

        etDate.setText(MyUtil.getStringDate(date));

        etDate.setOnClickListener(this);
        btn_browse.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void showError(String message,int field) {
        switch (field){
            case 1:
                tiTask.setError(message);
                etTask.requestFocus();
                break;

            case 2:
                tVolOfWorkDone.setError(message);
                etVolOfWorkDone.requestFocus();
                break;


        }
    }

    @Override
    public void clearPreError() {
        tVolOfWorkDone.setErrorEnabled(false);
        tiTask.setErrorEnabled(false);
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
        }catch (Exception e){
        }

        workDone.setAmount(amount);
        workDone.setProject(project.get_id());
        //workDone.setTask(etTask.getText().toString().trim());

        if(date !=null){
            workDone.setDate(date);
        }

        boolean valid = mPresenter.validate(selectedTask,workDone);

        if(!valid){
            return;
        }else{
            workDone.setTask(selectedTask.get_id());
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
    protected void onResume() {
        super.onResume();
        if(AppPreference.getInstance(this).getCounter()>Constant.COUNTER){
            showInterAd();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
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
