package com.forbitbd.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.forbitbd.androidutils.models.Project;
import com.forbitbd.androidutils.utils.Constant;
import com.forbitbd.androidutils.utils.PrebaseActivity;
import com.forbitbd.tasker.ui.TaskActivity;

import java.util.List;

public class MainActivity extends PrebaseActivity implements MainContract.View {

    private String userId = "5ee221fdf127667cc70e13ed";

    private ProjectAdapter adapter;

    private MainPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter(this);

        adapter = new ProjectAdapter(this);

        initView();


    }

    private void initView() {

        setupToolbar(R.id.toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);


        mPresenter.getAllProjects(userId);


    }

    @Override
    public void renderAdapter(List<Project> projectList) {
        for (Project x: projectList){
            adapter.addProject(x);
        }
    }

    @Override
    public void startTaskActivity(Project project) {
        Intent intent = new Intent(getApplicationContext(),TaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PROJECT,project);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
