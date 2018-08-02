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
import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.Task;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class goals extends Fragment {

    ArrayList<Day> days;

    public goals() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HashMap<String, Integer> categoryCount= new HashMap<>();
        days= ((bottomNav)getContext()).getDays();


        for (Day day: days){
            for (Free free: day.getFreeBlocks()){
                for (Task task: free.getTasks()){

                    if(!categoryCount.containsKey(task.getCategory())){
                        categoryCount.put(task.getCategory(), 1);
                    }
                    else{
                        categoryCount.put(task.getCategory(), categoryCount.get(task.getCategory())+1);
                    }
                }
            }
        }


        //code for chart from https://github.com/PhilJay/MPAndroidChart/wiki/Setting-Colors !!!!
        PieChart chart = (PieChart) view.findViewById(R.id.chart);

        List<PieEntry> entries = new ArrayList<>();


        for (String key: categoryCount.keySet()){
            entries.add(new PieEntry(categoryCount.get(key),key));
        }



        PieDataSet set = new PieDataSet(entries, "Categories");
        PieData data = new PieData(set);

        set.setColors(new int[] { R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark, R.color.mdtp_red }, getContext());
        chart.setData(data);
       // chart.animate();
        chart.animateXY(2000,2000);
        //chart.invalidate(); // refresh

    }

}
