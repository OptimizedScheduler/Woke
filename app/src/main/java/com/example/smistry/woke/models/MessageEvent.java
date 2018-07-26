package com.example.smistry.woke.models;

import java.util.ArrayList;

public class MessageEvent {
    public ArrayList<Day> mDaysList;
    // Add additional fields here if needed

    public MessageEvent(ArrayList<Day> list) {
        this.mDaysList = list;
        //https://stackoverflow.com/questions/45548223/how-to-get-contents-of-arraylist-from-first-activity-in-second-activity
        //https://medium.com/@taman.neupane/fragment-refresh-9ba6a6d7b897
    }

    public ArrayList<Day> getmDaysList() {
        return mDaysList;
    }

    public void setmDaysList(ArrayList<Day> mDaysList) {
        this.mDaysList = mDaysList;
    }

}
