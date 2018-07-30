package com.example.smistry.woke.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smistry.woke.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;


public class stats extends Fragment {

    HorizontalBarChart sleepChart;

    public stats() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sleepChart = (HorizontalBarChart) view.findViewById(R.id.sleepChart);
        setData(7, 12);
    }

    private void setData (int count, int range) {
        ArrayList<BarEntry> sleepVals = new ArrayList<>();
        float barWidth = 9f;
        float spaceForBar = 10f;

        for(int i = 0; i <count; i++)
        {
            float val = (float) (Math.random()*range);
            sleepVals.add(new BarEntry(i*spaceForBar,val));
        }

        BarDataSet set1;
        set1 = new BarDataSet(sleepVals, "Sleep Progress");
        BarData data = new BarData(set1);
        data.setBarWidth(barWidth);
        sleepChart.setData(data);
    }
}
