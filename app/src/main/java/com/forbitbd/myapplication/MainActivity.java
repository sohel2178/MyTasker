package com.forbitbd.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.forbitbd.tasker.models.Project;
import com.forbitbd.tasker.ui.TaskActivity;

public class MainActivity extends AppCompatActivity {

    private Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        project = new Project();
        project.set_id("5dbe73b388262501774c4efa");
        project.setName("My Project");

        Button btnStart = findViewById(R.id.clickMe);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("PROJECT",project);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });


    }
}
