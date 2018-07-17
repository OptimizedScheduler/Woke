package com.example.smistry.woke;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity { //TODO make this into the home fragment!!! IMPORTANT :)
    ArrayList<Task> tasks;
    TaskAdapter tasksdapter;
    RecyclerView rvTasks;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    // define your fragments here
    final Fragment fragment1= new newTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                newTask newTask = newTask.newInstance("Some Title");
//                fragmentTransaction.show(fragmentManager, "fragment_new_task");

                Snackbar.make(view, "Creating a new task", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    // returns the file in which the data is stored
    private File getDataFile() {
        return new File(getFilesDir(), "tasks.txt");
    }

    // read the items from the file system
    private void readItems() {
        try {

            ArrayList<String> taskStrings;
            // create the array using the content in the file
           taskStrings  = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
           tasks.clear();

           for(String task : taskStrings){
               Log.d("TASK", task);

               String [] params = task.split(",");
               String category = params [0];
               int duration = Integer.parseInt(params [1]);
               boolean automated = Boolean.parseBoolean(params[2]);
               int priority = Integer.parseInt(params[3]);
               DateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss ZZZ yyyy", Locale.ENGLISH);
               Date date = format.parse(params[4]);
               boolean day = Boolean.parseBoolean(params[5]);
               Task newTask = new Task (category, duration, automated, priority, date, day);

               tasks.add(newTask);
               Log.d("TASK", "added new task: " + newTask.toString());

           }

        } catch (IOException e) {
            // print the error to the console
            e.printStackTrace();
            // just load an empty list
            tasks = new ArrayList<>();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Home Activity", "Error with date to String");
        }
    }

    // write the items to the filesystem
    private void writeItems() {
        try {
            // save the item list as a line-delimited text file
            FileUtils.writeLines(getDataFile(), tasks);
        } catch (IOException e) {
            // print the error to the console
            e.printStackTrace();
        }
    }

}
