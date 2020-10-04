package com.forbitbd.tasker.ui.taskDetail.table;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.forbitbd.androidutils.utils.MyUtil;
import com.forbitbd.tasker.R;
import com.forbitbd.tasker.models.WorkDone;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sohel on 20-04-18.
 */

public class WorkDoneAdapter extends RecyclerView.Adapter<WorkDoneAdapter.WorkDoneHolder>{

    private List<WorkDone> workDoneList;
    private LayoutInflater inflater;
    private String unit;

    private WorkDoneTableFragment fragment;

    public WorkDoneAdapter(WorkDoneTableFragment fragment, String unit) {
        this.workDoneList = new ArrayList<>();
        this.fragment = fragment;
        this.inflater = LayoutInflater.from(fragment.getContext());
        this.unit = unit;
    }

    @Override
    public WorkDoneHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_work_done,parent,false);

        WorkDoneHolder holder = new WorkDoneHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(WorkDoneHolder holder, int position) {
        WorkDone workDone = workDoneList.get(position);

        holder.bind(workDone);

       /* holder.tvDate.setText(MyUtil.getStringDate(new Date(workDone.getDate())));
        holder.tvWorkDone.setText(String.valueOf(workDone.getAmount()).concat(" ").concat(unit));*/

    }

    public void addItem(WorkDone workDone){
        workDoneList.add(workDone);

        int pos = workDoneList.indexOf(workDone);
        notifyItemInserted(pos);
    }

    public void clear(){
        this.workDoneList.clear();
        notifyDataSetChanged();
    }


   /* public void setData(List<WorkDone> workDoneList, String unit){
        this.workDoneList = workDoneList;
        this.unit = unit;
        notifyDataSetChanged();
    }*/

    @Override
    public int getItemCount() {
        return workDoneList.size();
    }

    public class WorkDoneHolder extends RecyclerView.ViewHolder{
        TextView tvWorkDone, tvday, tvmonth_year;

        FloatingActionButton imageView;

        public WorkDoneHolder(View itemView) {
            super(itemView);
            tvWorkDone = itemView.findViewById(R.id.work_done);
            tvday = itemView.findViewById(R.id.day);
            tvmonth_year = itemView.findViewById(R.id.month_year);
            imageView = itemView.findViewById(R.id.image);


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("JJJJJJJJJ",workDoneList.get(getAdapterPosition()).getImage()+"");
                    if(workDoneList.get(getAdapterPosition()).getImage() !=null){
                        fragment.startZoomImageActivity(workDoneList.get(getAdapterPosition()));
                    }else {
                        fragment.showToast("No Attachment Found !!!");
                    }

                }
            });
        }


        public void bind(WorkDone dailyWorkdone){
            Log.d("HHHHHHH",dailyWorkdone.getAmount()+"");
            tvWorkDone.setText(dailyWorkdone.getAmount()+" "+unit);
            String date =  MyUtil.getStringDate(dailyWorkdone.getDate());
            String[] dateArr = date.split("-");
            String day = dateArr[0];
            String monthyear = dateArr[1]+"-"+dateArr[2];

            tvday.setText(day);
            tvmonth_year.setText(monthyear);



           /* if(dailyWorkdone.getImage() != null){
                Picasso.with(fragment.getContext())
                        .load(dailyWorkdone.getImage())
                        .into(imageView);
            }*/
        }
    }
}
