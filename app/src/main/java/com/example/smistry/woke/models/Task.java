
package com.example.smistry.woke.models;


import org.parceler.Parcel;

import java.sql.Time;
import java.util.Date;

public @Parcel class Task {

    public String taskTitle;
    public String category;
    public int duration;
    public Date date;
    public Time time;


    public Task() {
        //Empty constructor
    }


    public Task(String task, String category, int duration,  Date date) {
        this.taskTitle = task;
        this.category = category;
        this.duration = duration;
        this.date = date;
    }


    public String toString() {
        return category + "," + String.valueOf(duration) + "," + date.toString();
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}