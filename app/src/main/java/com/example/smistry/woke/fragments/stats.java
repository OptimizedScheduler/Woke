package com.example.smistry.woke.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smistry.woke.R;
import com.example.smistry.woke.bottomNav;
import com.example.smistry.woke.models.Day;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;

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
        float barWidth = 15f;
        float spaceForBar = 20f;
        ArrayList<Day> days=((bottomNav) getContext()).getDays();



        for(int i = days.size()-1; i >0; i--)
        {

           Day day = days.get(i-1);
           Day next = days.get(i);
           int nextHours = next.getWakeUp().getHours();
           int nextMin = next.getWakeUp().getMinutes();
           int dayHours = day.getSleep().getHours();
           int dayMins = day.getWakeUp().getMinutes();
           float sleeptime = 1440-Math.abs((nextHours-dayHours)*60 + (nextMin-dayMins));
           sleepVals.add(new BarEntry(i*spaceForBar, sleeptime));


        }

        Day day= days.get(days.size()-1);
        Day next= days.get(0);
        int nextHours = next.getWakeUp().getHours();
        int nextMin = next.getWakeUp().getMinutes();
        int dayHours = day.getSleep().getHours();
        int dayMins = day.getWakeUp().getMinutes();
        float sleeptime = 1440-Math.abs((nextHours-dayHours)*60 + (nextMin-dayMins));
        sleepVals.add(new BarEntry((0)*spaceForBar, sleeptime));







        BarDataSet set1;
        set1 = new BarDataSet(sleepVals, "Sleep Progress");
        BarData data = new BarData(set1);
        data.setBarWidth(barWidth);
        sleepChart.setFitBars(true);
        sleepChart.setData(data);



        YAxis yAxis= sleepChart.getAxisLeft();
        yAxis.setDrawGridLines(false);

        ArrayList<String>labels= new ArrayList<>();
        labels.add("h");
        labels.add("E");
        labels.add( "w");
        labels.add("wW");
        labels.add("o");
        labels.add("!");
        labels.add("!!");
        yAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        yAxis.setDrawLabels(true);
        //TODO- fix bar icons





        sleepChart.animateXY(1000,1000);



    }
}
