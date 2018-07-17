package com.example.smistry.woke.models;


import java.util.Date;

public class Task {
    private String category;
    private int duration;
    private boolean automated;
    private int priority;
    private Date date;
    private boolean day;

    public Task(String category, int duration, boolean automated, int priority, Date date, boolean day) {
        this.category = category;
        this.duration = duration;
        this.automated = automated;
        this.priority = priority;
        this.date = date;
        this.day = day;
    }

    public String toString(){
       return category + "," + String.valueOf(duration) + "," + String.valueOf(automated) + "," + String.valueOf(priority) + "," + date.toString() + ","  + String.valueOf(day);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isAutomated() {
        return automated;
    }

    public void setAutomated(boolean automated) {
        this.automated = automated;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(long time){
        if(!automated){
            date.setTime(time);
        }
    }

    public boolean isDay() {
        return day;
    }

    public void setDay(boolean day) {
        this.day = day;
    }
}
