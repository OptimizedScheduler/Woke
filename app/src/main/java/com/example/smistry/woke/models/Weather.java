package com.example.smistry.woke.models;

import org.parceler.Parcel;

public @Parcel class Weather {
    String date;
    Double minTemp;
    Double maxTemp;

    public Weather(){ }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Double minTemp) {
        this.minTemp = minTemp;
    }

    public Double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        this.maxTemp = maxTemp;
    }

}
