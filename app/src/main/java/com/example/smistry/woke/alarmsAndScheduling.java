package com.example.smistry.woke;

import android.support.v7.app.AppCompatActivity;

import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.Task;

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


}
