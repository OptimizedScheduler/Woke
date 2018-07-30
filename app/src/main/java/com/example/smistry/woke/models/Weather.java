package com.example.smistry.woke.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

public @Parcel class Weather {
    String date;
    String minTemp;
    String maxTemp;

    public Weather(){ }

    public Weather(JSONObject object) throws JSONException
    {
        String [] Temp1 = object.getString("Temperature").split(",", 5);
        String [] Temp2 = Temp1[0].split(":",3);
        minTemp = Temp2[2];
        String [] Temp3 = Temp1[3].split(":",4);
        maxTemp = Temp3[2];
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

}
