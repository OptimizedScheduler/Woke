package com.example.smistry.woke;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.Task;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class alarmsAndScheduling extends AppCompatActivity {


    public void setAlarm(Time time){
//        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
//        i.putExtra(AlarmClock.EXTRA_HOUR, time.getHours() );
//        i.putExtra(AlarmClock.EXTRA_MINUTES, time.getMinutes());
//        i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
//        startActivity(i);

    }


    ArrayList<Task> morningTasks;
    Free morningBlock= new Free(morningTasks, new Time(11,0,0), new Time(12,0,0), 60);

    public void morningScheduler(Free morning, Task task){
        if (morning.getFreeBlockDuration()>=task.getDuration()) {
            task.setTime(morning.getStart());
            morning.getTasks().add(task);
            morning.setFreeBlockDuration(morning.getFreeBlockDuration() - task.getDuration());
            morning.getStart().setMinutes(morning.getStart().getMinutes() + task.getDuration());

        }
        else{

            morning.getStart().setMinutes(morning.getStart().getMinutes()-task.getDuration());
            morning.getTasks().add(0,task);

            setAlarm(morning.getStart());

        }

    }

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
