package com.forbitbd.tasker.models;

public class DailyWorkdone {

    private String date;
    private double amount;


    public DailyWorkdone() {
    }

    public DailyWorkdone(String date) {
        this.date = date;
    }

    public DailyWorkdone(String date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void add(double amount){
        this.amount = this.amount+amount;
    }
}
