package com.example.smistry.woke.fragments;

import android.content.SharedPreferences;
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

//fragment where the user's goals/ task duration are added up and displayed in graphs
public class goals extends Fragment {
    ArrayList<Day> days;
    PieChart chart;
    HashMap<String, Integer> categoryCount = new HashMap<>();
    TextView tvfitnessGoal;
    TextView tvWorkGoal;
    TextView tvEntertainGoal;
    TextView tvSocialGoal;
    TextView tvOtherGoal;

    //recieving the each categories number of goal minutes from settings
    int fitnessGoal = 0;
    int workGoal = 0;
    int entertainmentGoal = 0;
    int socialGoal = 0;
    int otherGoal = 0;
    String name;


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

        tvfitnessGoal = view.findViewById(R.id.tvGoals1);
        tvWorkGoal = view.findViewById(R.id.tvGoals2);
        tvEntertainGoal = view.findViewById(R.id.tvGoals3);
        tvSocialGoal = view.findViewById(R.id.tvGoals4);
        tvOtherGoal = view.findViewById(R.id.tvGoals5);


        getData();


        //Library for chart from https://github.com/PhilJay/MPAndroidChart/wiki/Setting-Colors !!!!
        chart = (PieChart) view.findViewById(R.id.chart);

        //for each category, enter it's minutes as a data point on the pie chart
        List<PieEntry> entries = new ArrayList<>();
        for (String key : categoryCount.keySet()) {
            entries.add(new PieEntry(categoryCount.get(key), key));
        }


        //setting the enteries and data
        PieDataSet set = new PieDataSet(entries, "Categories");
        PieData data = new PieData(set);


        //setting the text, colors, and data
        chart.setCenterText(name + " Activities");
        chart.setCenterTextSize(32);
        set.setColors(new int[]{R.color.orange0, R.color.orange1, R.color.orange2, R.color.orange3, R.color.orange4}, getContext());
        chart.setData(data);

        //adding a blank description to get rid of it
        Description descript = new Description();
        descript.setText("");
        chart.setDescription(descript);

        //animating the piechart
        chart.animateXY(2000, 2000);

    }

    //updating piechart with current infromation
    //using same method/code as done above
    @Override
    public void onResume() {
        super.onResume();
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        getData();


        Description descript = new Description();
        descript.setText("");
        chart.setDescription(descript);
        chart.setCenterText(name + " Activities");


        chart.animateXY(2000, 2000);
    }


    public void getData() {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        //for each day of the week, the duration of each categories are added up and placed inside of a hashmap
        days = ((bottomNav) getContext()).getDays();
        for (Day day : days) {
            for (Free free : day.getFreeBlocks()) {
                for (Task task : free.getTasks()) {
                    if (!categoryCount.containsKey(task.getCategory())) {
                        categoryCount.put(task.getCategory(), task.getDuration());
                    } else {
                        categoryCount.put(task.getCategory(), categoryCount.get(task.getCategory()) + task.getDuration());
                    }
                }
            }
        }

        //check for each category to make sure they exist
        if (categoryCount.containsKey("Fitness")) {
            fitnessGoal = categoryCount.get("Fitness");
        }
        if (categoryCount.containsKey("Work")) {
            workGoal = categoryCount.get("Work");
        }
        if (categoryCount.containsKey("Entertainment")) {
            entertainmentGoal = categoryCount.get("Entertainment");
        }
        if (categoryCount.containsKey("Social")) {
            socialGoal = categoryCount.get("Social");
        }
        if (categoryCount.containsKey("Other")) {
            otherGoal = categoryCount.get("Other");
        }


        tvfitnessGoal.setText(fitnessGoal + " minutes out of " + pref.getString("fitness", "0") + " minutes");
        tvWorkGoal.setText(workGoal + " minutes out of " + pref.getString("work", "0") + " minutes");
        tvEntertainGoal.setText(entertainmentGoal + " minutes out of " + pref.getString("entertainment", "0") + " minutes");
        tvSocialGoal.setText(socialGoal + " minutes out of " + pref.getString("social", "0") + " minutes");
        tvOtherGoal.setText(otherGoal + " minutes out of " + pref.getString("other", "0") + " minutes");


        //getting the user's name from settings to enter into title
        name = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("name", "Your");
        if (!name.equals("Your")) {
            if (name.equals("")) {
                name = "Your";
            } else {
                name += "'s";
            }
        }

    }
}
