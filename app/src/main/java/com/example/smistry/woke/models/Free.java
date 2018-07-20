package com.example.smistry.woke.models;


import java.util.ArrayList;

//This object represents free times throughout the day for scheduling tasks
public class Free {


    private ArrayList<Task> tasks;
    private int start;
    private int end;
    private int freeBlockDuration;


    public Free(ArrayList<Task> tasks, int start, int end, int freeBlockDuration) {
        this.tasks = tasks;
        this.start = start;
        this.end = end;
        this.freeBlockDuration = freeBlockDuration;
    }


    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getFreeBlockDuration() {
        return freeBlockDuration;
    }

    public void setFreeBlockDuration(int freeBlockDuration) {
        this.freeBlockDuration = freeBlockDuration;
    }
}

