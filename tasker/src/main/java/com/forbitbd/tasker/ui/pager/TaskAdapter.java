package com.forbitbd.tasker.ui.pager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.forbitbd.androidutils.models.Task;
import com.forbitbd.androidutils.utils.MyUtil;
import com.forbitbd.tasker.R;
import com.google.android.material.button.MaterialButton;
import com.ramotion.foldingcell.FoldingCell;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private TaskPagerFragment fragment;
    private List<Task> taskList;
    private LayoutInflater inflater;


    public TaskAdapter(TaskPagerFragment fragment) {
        this.fragment = fragment;
        this.taskList = new ArrayList<>();
        this.inflater = LayoutInflater.from(fragment.getContext());
    }

    @Override
    public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_task,parent,false);
        return  new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskHolder holder, int position) {

        Task task = taskList.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void clear(){
        this.taskList.clear();
        notifyDataSetChanged();
    }


    public void addTask(Task task){
        this.taskList.add(task);
        int position = taskList.indexOf(task);
        notifyItemInserted(position);
    }

    public void setData(List<Task> taskList){
        clear();
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public void removeTask(Task task){
        int pos = taskList.indexOf(task);
        taskList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void updateItem(String id, double newWorkdone){
        for (Task x: taskList){
            if(x.get_id().equals(id)){
                x.setVolume_of_work_done(newWorkdone);
                break;
            }
        }
        notifyDataSetChanged();
    }

    class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        FoldingCell mFoldingCell;

        TextView tvName,tvNameTwo,tvRemaining,tvRemainingTwo,
                tvStartDate,tvFinishedDate,tvStatus,tvStatusTwo,tvProgress,
                tvDuration,tvVolumeofWorks,getTvVolumeofWorkDone;
        private ProgressBar mProgressBar;


        MaterialButton ivDelete,ivEdit,ivView;

        public TaskHolder(View itemView) {
            super(itemView);

            mFoldingCell = itemView.findViewById(R.id.folding_cell);

            tvName = itemView.findViewById(R.id.name);
            tvNameTwo = itemView.findViewById(R.id.name2);
            tvRemaining = itemView.findViewById(R.id.remaining);
            tvRemainingTwo = itemView.findViewById(R.id.remaining2);
            tvStatus = itemView.findViewById(R.id.status);
            tvStatusTwo = itemView.findViewById(R.id.status2);


            mProgressBar = itemView.findViewById(R.id.progressBar);
            tvProgress = itemView.findViewById(R.id.tv_progress);

           // tvProgress = itemView.findViewById(R.id.progress);
            tvStartDate = itemView.findViewById(R.id.start_date);
            tvFinishedDate = itemView.findViewById(R.id.finished_date);
            tvDuration = itemView.findViewById(R.id.duration);

            tvVolumeofWorks = itemView.findViewById(R.id.vol_of_works);
            getTvVolumeofWorkDone = itemView.findViewById(R.id.vol_of_work_done);

            mFoldingCell.setOnClickListener(this);

            //rlHide = itemView.findViewById(R.id.hide_container);

            ivDelete = itemView.findViewById(R.id.delete);
            ivEdit = itemView.findViewById(R.id.edit);
            ivView = itemView.findViewById(R.id.view_project);

            ivDelete.setOnClickListener(this);
            ivEdit.setOnClickListener(this);
            ivView.setOnClickListener(this);

        }

        public void bind(Task task){
            tvName.setText(task.getName());
            tvNameTwo.setText(task.getName());

            if(task.getRemainingDays()>0){
                tvRemaining.setText(String.valueOf(task.getRemainingDays()).concat(" Days Remaining"));
                tvRemainingTwo.setText(String.valueOf(task.getRemainingDays()).concat(" Days Remaining"));
            }else{
                tvRemaining.setText("Expired");
                tvRemainingTwo.setText("Expired");
            }


            tvStartDate.setText(MyUtil.getStringDate(task.getStart_date()));
            tvFinishedDate.setText(MyUtil.getStringDate(task.getFinished_date()));
            tvDuration.setText(String.valueOf(MyUtil.getDuration(task.getFinished_date().getTime(),task.getStart_date().getTime())).concat(" Days"));

            double pro = task.getVolume_of_work_done()/task.getVolume_of_works()*100;

            DecimalFormat df = new DecimalFormat("#.##");
            tvProgress.setText(df.format(pro).concat(" %"));

            tvVolumeofWorks.setText(df.format(task.getVolume_of_works())
                    .concat(" ").concat(task.getUnit()));
            getTvVolumeofWorkDone.setText(df.format(task.getVolume_of_work_done())
                    .concat(" ").concat(task.getUnit()));

            mProgressBar.setProgress((int)(task.getVolume_of_work_done()/task.getVolume_of_works()*100));

           tvStatus.setText(task.getStatus());
           tvStatusTwo.setText(task.getStatus());


        }


        @Override
        public void onClick(View view) {

            if(view==mFoldingCell){
                mFoldingCell.toggle(false);
            }else if(view==ivEdit){
                fragment.startEditTask(taskList.get(getAdapterPosition()));
            }else if(view==ivDelete){
                fragment.deleteTask(taskList.get(getAdapterPosition()));
            }else if(view ==ivView){
                fragment.startTaskDetailActivity(taskList.get(getAdapterPosition()));
            }

        }
    }
}
