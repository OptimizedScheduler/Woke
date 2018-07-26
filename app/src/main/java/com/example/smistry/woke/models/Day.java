package com.example.smistry.woke.models;

import org.parceler.Parcel;

import java.sql.Time;
import java.util.ArrayList;

@Parcel

public class Day {

    //Show a list of the free blocks available on each day
    public ArrayList<Free> freeBlocks;
    public  String dayOfWeek;
    public Time sleep;
    public Time wakeUp;


    public Day() {
    //Empty constructor for Parceler
    }

    public Day(ArrayList<Free> freeBlocks, String dayOfWeek, Time wake, Time sleep) {
        this.freeBlocks = freeBlocks;
        this.dayOfWeek = dayOfWeek;
        this.sleep = sleep;
        this.wakeUp = wake;
    }

    public ArrayList<Free> getFreeBlocks() {
        return freeBlocks;
    }

    public void setFreeBlocks(ArrayList<Free> freeBlocks) {
        this.freeBlocks = freeBlocks;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }


    public void addFree( Free toAdd){
        freeBlocks.add(toAdd);
    }

    public void setSleep(Time sleep) {
        this.sleep = sleep;
    }

    public void setWakeUp(Time wakeUp) {
        this.wakeUp = wakeUp;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
