package com.forbitbd.tasker.ui.gantt;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.forbitbd.androidutils.models.Project;
import com.forbitbd.androidutils.models.Task;
import com.forbitbd.androidutils.utils.Constant;
import com.forbitbd.tasker.R;
import com.google.gson.Gson;

import java.util.List;

public class GanttActivity extends AppCompatActivity implements GanttContract.View {

    private List<Task> taskList;

    private GanttPresenter mPresenter;

    WebView webHome;
    private TextView tvChartTitle;


    private Gson gson;

    private Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gantt);

        mPresenter = new GanttPresenter(this);

        gson = new Gson();

        taskList = (List<Task>) getIntent().getSerializableExtra(Constant.TASK_LIST);

        project = (Project) getIntent().getSerializableExtra(Constant.PROJECT);

        tvChartTitle = findViewById(R.id.chart_title);

        tvChartTitle.setText("Gantt Chart of "+project.getName());

        if(taskList!=null && taskList.size()>0){
            mPresenter.initChart();
        }
    }

    @Override
    public void initChart() {
        webHome = findViewById(R.id.home);
        WebSettings webSettings = webHome.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webHome.addJavascriptInterface(new JavaScriptInterface(this),"Android");
        webHome.setWebChromeClient(new MyChromeClient());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webHome.loadUrl("file:///android_asset/gantt/gantt.html");
    }


    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                0f, 1f, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        v.startAnimation(anim);
    }


    public class JavaScriptInterface{

        private Context mContext;

        public JavaScriptInterface(Context mContext) {
            this.mContext = mContext;
        }

        @JavascriptInterface
        public String getData() {
            return gson.toJson(taskList);
        }

        @JavascriptInterface
        public Project getProject(){
            return project;
        }

        @JavascriptInterface
        public void showToast(){


        }



    }
}
