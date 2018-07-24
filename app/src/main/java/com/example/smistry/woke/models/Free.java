package com.example.smistry.woke.models;


import org.parceler.Parcel;

import java.sql.Time;
import java.util.ArrayList;

//This object represents free times throughout the day for scheduling tasks
@Parcel
public class Free {
     public ArrayList<Task> tasks;
    public Time start;
    public Time end;
    public int freeBlockDuration;

    public Free() {
        //emtpy
    }

    public Free(ArrayList<Task> tasks, Time start, Time end, int freeBlockDuration) {
        this.tasks = tasks;
        this.start = start;
        this.end = end;
        this.freeBlockDuration = freeBlockDuration;
    }

    @Override
    public String toString() {
        return tasks.toString()+","+start.toString()+","+end.toString()+","+freeBlockDuration;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public Time getStart() {
        return start;
    }

    public void setStart(Time start) {
        this.start = start;
    }

    public Time getEnd() { return end; }

    public void setEnd(Time end) {
        this.end = end;
    }

    public int getFreeBlockDuration() {
        return freeBlockDuration;
    }

    public void setFreeBlockDuration(int freeBlockDuration) {
        this.freeBlockDuration = freeBlockDuration;
    }

}

