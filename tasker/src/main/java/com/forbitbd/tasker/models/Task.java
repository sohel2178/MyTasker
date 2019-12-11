package com.forbitbd.tasker.models;

import androidx.annotation.NonNull;

import com.forbitbd.androidutils.utils.MyUtil;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {

    private String _id;
    private String project;
    private String name;
    private String unit;
    private double volume_of_works;
    private double unit_rate;
    private double volume_of_work_done;
    private Date start_date;
    private Date finished_date;
    private Date insert_date;

    public Task() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getVolume_of_works() {
        return volume_of_works;
    }

    public void setVolume_of_works(double volume_of_works) {
        this.volume_of_works = volume_of_works;
    }

    public double getUnit_rate() {
        return unit_rate;
    }

    public void setUnit_rate(double unit_rate) {
        this.unit_rate = unit_rate;
    }

    public double getVolume_of_work_done() {
        return volume_of_work_done;
    }

    public void setVolume_of_work_done(double volume_of_work_done) {
        this.volume_of_work_done = volume_of_work_done;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getFinished_date() {
        return finished_date;
    }

    public void setFinished_date(Date finished_date) {
        this.finished_date = finished_date;
    }

    public Date getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(Date insert_date) {
        this.insert_date = insert_date;
    }

    public int getRemainingDays(){
        return MyUtil.getDuration(finished_date.getTime(),new Date().getTime());
    }

    public int getState(){
        Date date = new Date();
        if(MyUtil.getEndingTime(getFinished_date().getTime())>date.getTime()
                && getVolume_of_work_done()!= getVolume_of_works()){
            return 1;
        }else if(getVolume_of_work_done()== getVolume_of_works()){
            return 2;
        }else if(MyUtil.getEndingTime(getFinished_date().getTime())<date.getTime()
                && getVolume_of_work_done()!= getVolume_of_works()){
            return 3;
        }else{
            return 0;
        }
    }

    public String getStatus(){
        if(isFinished()){
            return "Completed";
        }else if(!isFinished() && isActive()){
            return "Running";
        }else if(!isFinished() && !isActive()){
            return "Expired";
        }
        return "Unknown";
    }

    public boolean isIntersect(){
        Date date = new Date();
        if(start_date.compareTo(date)<=0 && finished_date.compareTo(date)>=0){
            return true;
        }
        return false;
    }

    public boolean isFinished(){
        if(volume_of_work_done>=volume_of_works){
            return true;
        }
        return false;
    }

    public boolean isActive(){
        Date date = new Date();
        if(finished_date.compareTo(date)>=0){
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
