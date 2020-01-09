package com.forbitbd.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.forbitbd.androidutils.models.Project;
import com.forbitbd.androidutils.utils.MyUtil;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;


public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectHolder> {

    private MainActivity activity;
    private List<Project> projectList;
    private LayoutInflater inflater;

    public ProjectAdapter(MainActivity activity) {
        this.activity = activity;
        this.projectList = new ArrayList<>();
        this.inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public ProjectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.project_item,viewGroup,false);
        return new ProjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectHolder projectHolder, int i) {
        Project project = projectList.get(i);
        projectHolder.bind(project);
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public void addProject(Project project){
        projectList.add(0,project);
        notifyItemInserted(0);
    }

    public void updateProject(Project project){
        int position = getPosition(project);
        projectList.set(position,project);
        notifyItemChanged(position);
    }

    public void deleteProject(Project project){
        int position = getPosition(project);
        projectList.remove(position);
        notifyItemRemoved(position);
    }

    public void clear(){
        projectList.clear();
        notifyDataSetChanged();
    }

    private int getPosition(Project project){
        for (Project x: projectList){
            if(x.get_id().equals(project.get_id())){
                return projectList.indexOf(x);
            }
        }
        return -1;
    }

    class ProjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvName,tvCreatedDate;


        public ProjectHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.name);
            tvCreatedDate = itemView.findViewById(R.id.created_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            activity.startTaskActivity(projectList.get(getAdapterPosition()));
            /*if(view==tvIndicatorOne || view==tvIndicatorTwo){
                mFoldingCell.toggle(false);
            }else if(view==ivDelete){
                fragment.showDeleteDialog(projectList.get(getAdapterPosition()));
            }else if(view==tvEmployee){
                fragment.startEmployeeActivity(projectList.get(getAdapterPosition()));
            }else if(view == ivEdit){
                fragment.editProject(projectList.get(getAdapterPosition()));
            }else if(view == tvActivity){
                fragment.startTaskActivity(projectList.get(getAdapterPosition()));
            }else if(view==tvFinance){
                fragment.startFinanceActivity(projectList.get(getAdapterPosition()));
            }else if(view == tvStore){
                fragment.startStoreActivity(projectList.get(getAdapterPosition()));
            }*/
        }

        public void bind(Project project){
            tvName.setText(project.getName());
            tvCreatedDate.setText("Created on Date "+ MyUtil.getStringDate(project.getCreated_at()));
        }
    }
}
