package com.example.smistry.woke.models;


import org.parceler.Parcel;

import java.sql.Time;
import java.util.Date;

public @Parcel class Task {
    public String category;
    public int duration;
    public boolean automated;
    public int priority;
    public Date date;
    public Time time;
    public boolean day;


    public Task(){
        //Empty constructor
    }



    public Task(String category, int duration, boolean automated, int priority, Date date, boolean day) {
        this.category = category;
        this.duration = duration;
        this.automated = automated;
        this.priority = priority;
        this.date = date;
        this.time = time;
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

    public void setTime(Time time){
        this.time = time;
    }

    public boolean isDay() {
        return day;
    }

    public void setDay(boolean day) {
        this.day = day;
    }
}
