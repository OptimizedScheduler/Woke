package com.example.smistry.woke.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smistry.woke.R;
import com.example.smistry.woke.TaskRecyclerAdapter;
import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.Task;

import java.util.ArrayList;

public class TaskFragment extends Fragment {
    // Store instance variables
    private int dayOfW;
    private ArrayList<Free> freeBlocks;
    private ArrayList<Task> dailyTasks;
    private TaskRecyclerAdapter adapter;

    // newInstance constructor for creating fragment with arguments
    public static TaskFragment newInstance(int day, String title) { //add Day thisDay
        TaskFragment fragmentFirst = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt("day", day);
        args.putString("dayString", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO check for NullPointerExc
        dayOfW = getArguments().getInt("day"); //Returns the dayOfWeek this fragment is made for (Sunday,Monday...)
        freeBlocks = new ArrayList<>();
        dailyTasks = new ArrayList<>();

        //Obtain the array needed depending of which day is specified
        //Used for passing information from parent to child/mested fragment (Viewpager --> TaskFragment)
        if (getParentFragment() instanceof ViewPagerFragment) {
            try{
            freeBlocks=((ViewPagerFragment) getParentFragment()).getArray(dayOfW);}
            catch (Exception e){
                Log.d("TaskFragment", e.getMessage());
            }
        }

        //Pass the stored tasks (per day) in each Task adapter
        if(freeBlocks!=null) {
            for(int i=0; i<freeBlocks.size(); i++)
            try {
                if(dailyTasks!= null)
                    dailyTasks.addAll(freeBlocks.get(i).getTasks());
            }
            catch (Exception e){
                Log.d("TaskFragment",e.getMessage());
            }
            adapter = new TaskRecyclerAdapter(dailyTasks);
            adapter.notifyDataSetChanged();
        }
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvTasks= view.findViewById(R.id.rvTasks);
        rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTasks.setAdapter(adapter);
    }
}

