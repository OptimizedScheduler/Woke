package com.example.smistry.woke.fragments;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.smistry.woke.R;
import com.example.smistry.woke.bottomNav;
import com.example.smistry.woke.models.Day;
import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.Task;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class goals extends Fragment {

    ArrayList<Day> days;
    PieChart chart;
    HashMap<String, Integer> categoryCount= new HashMap<>();
    TextView goals1;
    TextView goals2;
    TextView goals3;
    TextView goals4;
    TextView goals5;


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

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        goals1= view.findViewById(R.id.tvGoals1);
        goals2= view.findViewById(R.id.tvGoals2);
        goals3= view.findViewById(R.id.tvGoals3);
        goals4= view.findViewById(R.id.tvGoals4);
        goals5= view.findViewById(R.id.tvGoals5);


        days= ((bottomNav)getContext()).getDays();
        for (Day day: days){
            for (Free free: day.getFreeBlocks()){
                for (Task task: free.getTasks()){
                   if(!categoryCount.containsKey(task.getCategory())){
                        categoryCount.put(task.getCategory(), task.getDuration());
                    }
                    else{
                        categoryCount.put(task.getCategory(), categoryCount.get(task.getCategory())+task.getDuration());
                   }
                }
            }
        }

        int goal1=0;
        int goal2=0;
        int goal3=0;
        int goal4=0;
        int goal5=0;

        if (categoryCount.containsKey("Fitness")){
            goal1= categoryCount.get("Fitness");
        }
        if (categoryCount.containsKey("Work")){
            goal2= categoryCount.get("Work");
        }
        if (categoryCount.containsKey("Entertainment")){
            goal3= categoryCount.get("Entertainment");
        }
        if (categoryCount.containsKey("Social")){
            goal4= categoryCount.get("Social");
        }
        if (categoryCount.containsKey("Other")){
            goal5= categoryCount.get("Other");
        }



        goals1.setText(goal1+" minutes out of "+pref.getString("fitness","0")+" minutes");
        goals2.setText(goal2+" minutes out of "+pref.getString("work","0")+" minutes");
        goals3.setText(goal3+" minutes out of "+pref.getString("entertainment","0")+" minutes");
        goals4.setText(goal4+" minutes out of "+pref.getString("social","0")+" minutes");
        goals5.setText(goal5+" minutes out of "+pref.getString("other","0")+" minutes");






        //code for chart from https://github.com/PhilJay/MPAndroidChart/wiki/Setting-Colors !!!!
        chart = (PieChart) view.findViewById(R.id.chart);

        List<PieEntry> entries = new ArrayList<>();


        for (String key: categoryCount.keySet()){
            entries.add(new PieEntry(categoryCount.get(key),key));
        }


        PieDataSet set = new PieDataSet(entries, "Categories");
        PieData data = new PieData(set);

        Description desc= new Description();
       // desc.setPosition(0,0);
        desc.setTextSize(600);

        String name= PreferenceManager.getDefaultSharedPreferences(getContext()).getString("name", "Your");
        if (!name.equals("Your")){
            name+="'s";
        }

        chart.setCenterText(name+" Goals");
        chart.setCenterTextSize(32);
        set.setColors(new int[] { R.color.orange0, R.color.orange1, R.color.orange2, R.color.orange3, R.color.orange4}, getContext());
        chart.setData(data);
        Description descript= new Description();
        descript.setText("");
        chart.setDescription(descript);
       // chart.animate();
        chart.animateXY(2000,2000);
        //chart.invalidate(); // refresh

    }

    @Override
    public void onResume() {
        super.onResume();
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());


        int goal1=0;
        int goal2=0;
        int goal3=0;
        int goal4=0;
        int goal5=0;

        if (categoryCount.containsKey("Fitness")){
            goal1= categoryCount.get("Fitness");
        }
        if (categoryCount.containsKey("Work")){
            goal2= categoryCount.get("Work");
        }
        if (categoryCount.containsKey("Entertainment")){
            goal3= categoryCount.get("Entertainment");
        }
        if (categoryCount.containsKey("Social")){
            goal4= categoryCount.get("Social");
        }
        if (categoryCount.containsKey("Other")){
            goal5= categoryCount.get("Other");
        }



        goals1.setText(goal1+" minutes out of "+pref.getString("fitness","0")+" minutes");
        goals2.setText(goal2+" minutes out of "+pref.getString("work","0")+" minutes");
        goals3.setText(goal3+" minutes out of "+pref.getString("entertainment","0")+" minutes");
        goals4.setText(goal4+" minutes out of "+pref.getString("social","0")+" minutes");
        goals5.setText(goal5+" minutes out of "+pref.getString("other","0")+" minutes");



        String name= PreferenceManager.getDefaultSharedPreferences(getContext()).getString("name", "Your");
        if (!name.equals("Your")){
            name+="'s";
        }


        Description descript= new Description();
        descript.setText("");
        chart.setDescription(descript);
        chart.setCenterText(name+" Goals");


        chart.animateXY(2000,2000);
    }
}
