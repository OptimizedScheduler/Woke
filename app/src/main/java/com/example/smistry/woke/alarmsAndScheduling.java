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


    }