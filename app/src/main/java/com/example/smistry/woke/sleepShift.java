package com.example.smistry.woke;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;

import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.Task;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class sleepShift extends AppCompatActivity {

    public AlarmManager alarmMgr= (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
    public PendingIntent alarmIntent;

    Intent intent = new Intent(this, AlarmReceiver.class);
    alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);


    ArrayList<Task> morningTasks;
    Free morningBlock= new Free(morningTasks, new Time(11,0,0), new Time(12,0,0), 60);

    public void morningScheduler(Free morning, Task task){
        if (morning.getFreeBlockDuration()>=task.getDuration()){
            //schedule in regularly Suchita

        }
        else{

            morning.getStart().setMinutes(morning.getStart().getMinutes()-task.getDuration());
            morning.getTasks().add(0,task);

            //set alarm

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
