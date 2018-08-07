package com.example.smistry.woke;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.smistry.woke.fragments.TimePickerFragment;
import com.example.smistry.woke.models.Day;
import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.Task;

import org.apache.commons.io.FileUtils;
import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class editDayActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    Boolean sleepSet;
    Boolean wakeSet;
    Boolean startTimeFreeSet;
    Boolean endTimeFreeSet;

    int position;

    String image;
    ArrayList<Day>days;
    ArrayList<Free> frees;
    HashMap<String,Integer> dayOfWeek;
    Time sleepTime;
    Time wakeTime;
    Time startTimeFree;
    Time endTimeFree;
    Day currDay2;

    @BindView(R.id.ivDay) ImageView ivDay;
    @BindView(R.id.tvSleepTime)TextView tvSleepTime;
    @BindView(R.id.tvWakeTime) TextView tvWakeTime;
    @BindView(R.id.tvDayEditDay) TextView day;
    @BindView(R.id.btSleepTime) Button btsleepTime;
    @BindView(R.id.btWakeTime) Button btwakeTime;
    @BindView(R.id.btFreeTimeStart) Button btstartTimeFree;
    @BindView(R.id.btFreeTimeEnd)Button btendTimeFree;
    @BindView(R.id.btSetFree) Button makeFree;
    @BindView(R.id.tvEnteredFreeEditDay) TextView enteredFreeTimes;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_day_activity);
        ButterKnife.bind(this);
        dayOfWeek=new HashMap<>();
        dayOfWeek.put("Sunday", R.drawable.sunday);
        dayOfWeek.put("Monday", R.drawable.monday);
        dayOfWeek.put("Tuesday", R.drawable.tuesday);
        dayOfWeek.put("Wednesday", R.drawable.wednesday);
        dayOfWeek.put("Thursday", R.drawable.thursday);
        dayOfWeek.put("Friday", R.drawable.friday);
        dayOfWeek.put("Saturday", R.drawable.saturday);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        enteredFreeTimes.setMovementMethod(new ScrollingMovementMethod());

        sleepSet=false;
        wakeSet=false;
        startTimeFreeSet=false;
        endTimeFreeSet=false;

        position= getIntent().getIntExtra("Position",0);
        days= Parcels.unwrap(getIntent().getParcelableExtra("Days"));
        Day currDay=days.get(position);
        currDay2 = currDay;
        frees=currDay.getFreeBlocks();

        String sleepMinutes=currDay.getSleep().getMinutes()+"";
        String wakeMinutes=-currDay.getWakeUp().getMinutes()+"";

        if (sleepMinutes.length()!=2){
            sleepMinutes="0"+sleepMinutes;
        }
        if (wakeMinutes.length()!=2){
            wakeMinutes="0"+wakeMinutes;
        }


        tvSleepTime.setText(currDay.getSleep().getHours()+":"+sleepMinutes);
        tvWakeTime.setText(currDay.getWakeUp().getHours()+":"+wakeMinutes);
        wakeTime=currDay.getWakeUp();
        sleepTime=currDay.getSleep();

        day.setText(getIntent().getStringExtra("Day").toString());
        image=getIntent().getStringExtra("Day").toString();

        Glide.with(this)
                .load(dayOfWeek.get(image))
                .into(ivDay);

        int count=0;
        for (Free free:frees){
            if (count!=0){
                enteredFreeTimes.setText(enteredFreeTimes.getText().toString()+"\n"+free.stringInfo());
            }
            else{
                enteredFreeTimes.setText(free.stringInfo());
                count+=1;
            }
        }

        //Setting colors for presing
        btsleepTime.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0daf1f0, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        btwakeTime.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0daf1f0, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        btstartTimeFree.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f5ddb6, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        btendTimeFree.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f5ddb6, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        makeFree.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f5ddb6, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        //filing in data
        btwakeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wakeSet=true;
                sleepSet=false;
                startTimeFreeSet=false;
                endTimeFreeSet=false;

                DialogFragment TimePicker= TimePickerFragment.newInstance(currDay2.getWakeUp().getHours(),currDay2.getWakeUp().getMinutes());

                TimePicker.show(getSupportFragmentManager(), "TimePick");
            }
        });

        btsleepTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wakeSet=false;
                sleepSet=true;
                startTimeFreeSet=false;
                endTimeFreeSet=false;
                DialogFragment TimePicker= TimePickerFragment.newInstance(currDay2.getSleep().getHours(),currDay2.getSleep().getMinutes());
                    TimePicker.show(getSupportFragmentManager(), "TimePick");



            }
        });

        btstartTimeFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wakeSet=false;
                sleepSet=false;
                startTimeFreeSet=true;
                endTimeFreeSet=false;
                    DialogFragment TimePicker= TimePickerFragment.newInstance(0,0);
                    TimePicker.show(getSupportFragmentManager(), "TimePick");

            }
        });

        btendTimeFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wakeSet=false;
                sleepSet=false;
                startTimeFreeSet=false;
                endTimeFreeSet=true;
                DialogFragment TimePicker= TimePickerFragment.newInstance(0,0);
                    TimePicker.show(getSupportFragmentManager(), "TimePick");



            }
        });

        makeFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startTimeFree!=null&& endTimeFree!=null){

                    Free toAdd=new Free(new ArrayList<Task>(), startTimeFree, endTimeFree);
                    days.get(position).getFreeBlocks().add(toAdd);
                    String toEnter="";
                    if (enteredFreeTimes.getText().toString().equals("")){
                        toEnter=toAdd.stringInfo();
                    }
                    else{
                        toEnter=enteredFreeTimes.getText().toString()+"\n"+toAdd.stringInfo();
                    }

                    enteredFreeTimes.setText(toEnter);
                    endTimeFree=null;
                    startTimeFree=null;

                }
                else  if (startTimeFree==null){
                    Toast.makeText(getApplicationContext(), "Please set your Start Time first", Toast.LENGTH_SHORT).show();
                }
                else if (endTimeFree==null){
                    Toast.makeText(getApplicationContext(), "Please set your End Time first", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
                writeItems();
                Intent i= new Intent();
                i.putExtra("days", Parcels.wrap(days));
                setResult(RESULT_OK, i);
                finish();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Time time = new Time(hour, minute, 0);

        if (wakeSet){
            wakeTime=time;
            wakeSet=true;
            days.get(position).setWakeUp(wakeTime);

            String wakeMinutes=days.get(position).getWakeUp().getMinutes()+"";

            if (wakeMinutes.length()!=2){
                wakeMinutes="0"+wakeMinutes;
            }

            tvWakeTime.setText(days.get(position).getWakeUp().getHours()+":"+wakeMinutes);

        }
        else if (sleepSet){
            if (time.before(wakeTime)){
                Toast.makeText(getApplicationContext(), "Your sleep time can't be before your wake time", Toast.LENGTH_SHORT).show();

            }
            else{
                sleepTime=time;
                sleepSet=true;
                days.get(position).setSleep(sleepTime);

                String sleepMinutes=days.get(position).getSleep().getMinutes()+"";
                if (sleepMinutes.length()!=2){
                    sleepMinutes="0"+sleepMinutes;
                }
                tvSleepTime.setText( days.get(position).getSleep().getHours()+":"+sleepMinutes);
            }
        }

        else if (startTimeFreeSet){
            boolean between=false;
            for (Free free: days.get(position).getFreeBlocks()){
                if (time.after(free.start)&&time.before(free.end)){
                    between=true;
                }
            }


            if (time.before(wakeTime)){
                Toast.makeText(getApplicationContext(), "Your Free's start can't be before your wake time", Toast.LENGTH_SHORT).show();

            }
            else if (time.after(sleepTime)){
                Toast.makeText(getApplicationContext(), "Your Free's start can't be after your sleep time", Toast.LENGTH_SHORT).show();
            }
            else if (between){
                Toast.makeText(getApplicationContext(), "Your Free's start can't be between another Free block", Toast.LENGTH_SHORT).show();
            }
            else{
                startTimeFree=time;
                startTimeFreeSet=true;}
        }
        else if (endTimeFreeSet){
            boolean between=false;
            for (Free free: days.get(position).getFreeBlocks()){
                if (time.after(free.start)&&time.before(free.end)){
                    between=true;
                }
            }
            if (time.before(wakeTime)){
                Toast.makeText(getApplicationContext(), "Your Free's end can't be before your wake time", Toast.LENGTH_SHORT).show();

            }
            else if (time.after(sleepTime)){
                Toast.makeText(getApplicationContext(), "Your Free's end can't be after your sleep time", Toast.LENGTH_SHORT).show();
            }
            else if (time.before(startTimeFree)){
                Toast.makeText(getApplicationContext(), "Your Free's end can't be before your Free's start", Toast.LENGTH_SHORT).show();
            }
            else if (between){
                Toast.makeText(getApplicationContext(), "Your Free's end can't be between another Free block", Toast.LENGTH_SHORT).show();
            }
            else{
                endTimeFree=time;
                endTimeFreeSet=true;
            }
        }
        sleepSet=false;
        wakeSet=false;
        startTimeFreeSet=false;
        endTimeFreeSet=false;

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

    // returns the file in which the data is stored
    public File getDataFile() {
        return new File(this.getFilesDir(), "days.txt");
    }

}
