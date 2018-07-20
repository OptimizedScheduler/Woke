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
import android.widget.TextView;

import com.example.smistry.woke.R;
import com.example.smistry.woke.TaskRecyclerAdapter;
import com.example.smistry.woke.models.Task;

import java.util.ArrayList;
import java.util.Date;


public class TaskFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    private ArrayList<Task> myTasks;
    private TaskRecyclerAdapter adapter;

    // newInstance constructor for creating fragment with arguments
    public static TaskFragment newInstance(int page, String title) {
        TaskFragment fragmentFirst = new TaskFragment();
        Bundle args = new Bundle();

        //TODO -- check to pass all the needed info (Can I pass the whole TASK object???)
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        myTasks=new ArrayList<Task>();
        Task task1 = new Task("work",7,new Date());
        //String category, int duration, boolean automated, int priority, Date date, boolean day
        myTasks.add(task1);
        adapter=new TaskRecyclerAdapter(myTasks);
        Log.d("TaskFrag", "NEW RECYCLER");

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        tvLabel.setText(page + " -- " + title);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvTasks= (RecyclerView) view.findViewById(R.id.rvTasks);
        rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTasks.setAdapter(adapter);
    }
}

