package com.example.smistry.woke;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.smistry.woke.models.Day;
import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.Task;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class alarmsAndScheduling extends AppCompatActivity {


    public void setAlarm(Time time, Date date) {


    }


    ArrayList<Task> morningTasks;
    Free morningBlock = new Free(morningTasks, new Time(11, 0, 0), new Time(12, 0, 0), 60);


    public void deletion(Free free, int position) {
        Task task = free.getTasks().get(position);
        int duration = task.getDuration();
        free.setFreeBlockDuration(free.getFreeBlockDuration() + duration);
        free.getTasks().remove(position);

        for (int i = position; i < free.getTasks().size(); i++) {
            Task toEdit = free.getTasks().get(i);
            toEdit.getTime().setMinutes(toEdit.getTime().getMinutes() - duration);
        }

    }

    // write the items to the filesystem
    private void writeItems() {
        try {
            // save the item list as a line-delimited text file
            FileUtils.writeLines(getDataFile(), days);
        } catch (IOException e) {
            // print the error to the console
            e.printStackTrace();
        }
    }


    ArrayList<Day>days= new ArrayList<>();

    // returns the file in which the data is stored
    public File getDataFile() {
        return new File(this.getFilesDir(), "days.txt");
    }

    // read the items from the file system
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void readItems() {
        try {
            ArrayList<String> dayStrings;
//           // create the array using the content in the file
            dayStrings = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
            days.clear();


            for (String daysString : dayStrings) {
                Log.d("Day", daysString);

                String[] params = daysString.split("_");
                String dayOfWeek = params[0];
                String[] wakeTimeSplit = params[1].split(":");
                String[] sleepTimeSplit = params[2].split(":");
                Time wakeTime = new Time(Integer.valueOf(wakeTimeSplit[0]), Integer.valueOf(wakeTimeSplit[1]), 0);
                Time sleepTime = new Time(Integer.valueOf(sleepTimeSplit[0]), Integer.valueOf(sleepTimeSplit[1]), 0);


                String freeBlocks=params[3];
                //remove brackets from array toStirng
                freeBlocks=freeBlocks.substring(1,freeBlocks.length()-1);
                String [] freeBlocksSplit=freeBlocks.split(",");
                ArrayList<Free>frees=new ArrayList<>();

                if (!freeBlocks.equals("")) {
                    for (String free : freeBlocksSplit) {
                        String[] splitFree = free.split(";");
                        String[] freeStartSplit = splitFree[0].split(":");
                        String[] freeEndSplit = splitFree[1].split(":");
                        Time freeStart = new Time(Integer.valueOf(freeStartSplit[0].replaceAll("\\s+", "")), Integer.valueOf(freeStartSplit[1].replaceAll("\\s+", "")), 0);
                        Time freeEnd = new Time(Integer.valueOf(freeEndSplit[0].replaceAll("\\s+", "")), Integer.valueOf(freeEndSplit[1].replaceAll("\\s+", "")), 0);
                        int duration = Integer.valueOf(splitFree[2]);
                        ArrayList<Task> tasks = new ArrayList<>();

                        if (splitFree.length == 4) {

                            String[] tasksStrings = splitFree[3].split("/");
                            for (String taskString : tasksStrings) {

                                String[] taskStringSplit = taskString.split("-");
                                String[] tasktimeSplit = taskStringSplit[4].split(":");

                                String title = taskStringSplit[0];
                                String category = taskStringSplit[1];
                                int durationTask = Integer.parseInt(taskStringSplit[2].replaceAll("\\s+", ""));
                                DateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss ZZZ yyyy", Locale.ENGLISH);
                                Date date = format.parse(taskStringSplit[3]);
                                Time time = new Time(Integer.valueOf(tasktimeSplit[0].replaceAll("\\s+", "")), Integer.valueOf(tasktimeSplit[1].replaceAll("\\s+", "")), 0);
                                Task newTask = new Task(title, category, durationTask, date, time);
                                tasks.add(newTask);
                            }
                        }
                        frees.add(new Free(tasks, freeStart, freeEnd, duration));
                    }
                }
                Day newDay= new Day(frees,dayOfWeek,wakeTime, sleepTime);
                days.add(newDay);
                Log.d("Day", "added new day: " + newDay.toString());
            }

        }
        catch (IOException e) {
            // print the error to the console
            e.printStackTrace();
            // just load an empty list
            days = new ArrayList<>();
        }
        catch (ParseException e) {
            e.printStackTrace();
            Log.d("Home Activity", "Error with date to String");
        }

    }


}