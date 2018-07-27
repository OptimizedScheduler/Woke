package com.example.smistry.woke;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.Task;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class alarmsAndScheduling extends AppCompatActivity {


    public void setAlarm(Time time, Date date){


    }


    ArrayList<Task> morningTasks;
    Free morningBlock= new Free(morningTasks, new Time(11,0,0), new Time(12,0,0), 60);



    public void deletion(Free free, int position){
        Task task= free.getTasks().get(position);
        int duration=task.getDuration();
        free.setFreeBlockDuration(free.getFreeBlockDuration()+duration);
        free.getTasks().remove(position);

        for(int i=position; i<free.getTasks().size(); i++){
            Task toEdit=free.getTasks().get(i);
            toEdit.getTime().setMinutes(toEdit.getTime().getMinutes()-duration);
        }

    }



    // returns the file in which the data is stored
    public File getDataFile() {
        return new File(this.getFilesDir(), "tasks.txt");
    }

    // read the items from the file system
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void readItems() {
        try {

            ArrayList<String> dayStrings;
            // create the array using the content in the file
            dayStrings = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
            days.clear();

            for (String task : dayStrings) {
                Log.d("Day", task);

                String[] params = task.split("_");
                String dayOfWeek = params[0];
                String[] wakeTimeSplit = params[1].split(":");
                String[] sleepTimeSplit = params[2].split(":");
                Time wakeTime = new Time(Integer.valueOf(wakeTimeSplit[0]), Integer.valueOf(wakeTimeSplit[1]), 0);
                Time sleepTime = new Time(Integer.valueOf(sleepTimeSplit[0]), Integer.valueOf(sleepTimeSplit[1]), 0);


//                String [] freeBlocks=params[3].split("");
//
//
//                //task
//                String title=;
//                String category = params [0];
//                int duration = Integer.parseInt(params [1]);
//                DateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss ZZZ yyyy", Locale.ENGLISH);
//                Date date = format.parse(params[4]);
//                Time time=;
//                Task newTask = new Task ("Ending, something",category, duration,  date);
//
//
//               // days.add();
//                Log.d("Day", "added new day: " + newTask.toString());

            }

        } catch (IOException e) {
            // print the error to the console
            e.printStackTrace();
            // just load an empty list
            tasks = new ArrayList<>();
//        } catch (ParseException e) {
//            e.printStackTrace();
//            Log.d("Home Activity", "Error with date to String");
//        }
        }
    }

    // write the items to the filesystem
    private void writeItems () {
        try {
            // save the item list as a line-delimited text file
            FileUtils.writeLines(getDataFile(), days);
        } catch (IOException e) {
            // print the error to the console
            e.printStackTrace();
        }
    }


}
