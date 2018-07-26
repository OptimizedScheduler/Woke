package com.example.smistry.woke;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.smistry.woke.fragments.TimePickerFragment;
import com.example.smistry.woke.models.Day;
import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.Task;

import org.parceler.Parcels;

import java.sql.Time;
import java.util.ArrayList;

public class editDayActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    TextView day;
    TextView enteredFreeTimes;
    Button btsleepTime;
    Button btwakeTime;
    Button btstartTimeFree;
    Button btendTimeFree;
    Button makeFree;
    Day newDay;

    Boolean sleepSet;
    Boolean wakeSet;
    Boolean startTimeFreeSet;
    Boolean endTimeFreeSet;


    Time sleepTime;
    Time wakeTime;
    Time startTimeFree;
    Time endTimeFree;


    ArrayList<ArrayList<Free>> DOW;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_day);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        sleepSet=false;
        wakeSet=false;
        startTimeFreeSet=false;
        endTimeFreeSet=false;

        newDay= new Day();
        newDay.setDayOfWeek(getIntent().getStringExtra("Day").toString());
        newDay.setFreeBlocks(new ArrayList<Free>());

        //position=getIntent().getIntExtra("Position", 0);

        day= (TextView)findViewById(R.id.tvDayEditDay);
        day.setText(getIntent().getStringExtra("Day").toString());

        btsleepTime = (Button)findViewById(R.id.btSleepTime);
        btwakeTime = (Button)findViewById(R.id.btWakeTime);
        btstartTimeFree = (Button)findViewById(R.id.btFreeTimeStart);
        btendTimeFree = (Button)findViewById(R.id.btFreeTimeEnd);
        makeFree= (Button)findViewById(R.id.btSetFree) ;

        btwakeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment TimePicker= new TimePickerFragment();
                TimePicker.show(getSupportFragmentManager(), "TimePick");
            }
        });

        btsleepTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wakeSet){
                    DialogFragment TimePicker= new TimePickerFragment();
                    TimePicker.show(getSupportFragmentManager(), "TimePick");

                }
                else{
                    Toast.makeText(getApplicationContext(), "Please set your Sleep Time first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btstartTimeFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wakeSet && sleepSet){
                    DialogFragment TimePicker= new TimePickerFragment();
                    TimePicker.show(getSupportFragmentManager(), "TimePick");

                }
                else{
                    Toast.makeText(getApplicationContext(), "Please set your Wake Time first", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btendTimeFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startTimeFreeSet){
                    DialogFragment TimePicker= new TimePickerFragment();
                    TimePicker.show(getSupportFragmentManager(), "TimePick");

                }
                else{
                    Toast.makeText(getApplicationContext(), "Please set your Start Time first", Toast.LENGTH_SHORT).show();
                }

            }
        });

        makeFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (endTimeFreeSet && startTimeFreeSet){
                    endTimeFreeSet=false;
                    startTimeFreeSet=false;
                    newDay.getFreeBlocks().add(new Free(new ArrayList<Task>(), startTimeFree, endTimeFree, 60));

                }
                else  if (!startTimeFreeSet){
                    Toast.makeText(getApplicationContext(), "Please set your Start Time first", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please set your End Time first", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (sleepSet && wakeSet){
                Intent i= new Intent();
                i.putExtra("newDay", Parcels.wrap(newDay));
                i.putExtra("Position", position);
                setResult(RESULT_OK, i);
                finish();}
            else if (!wakeSet){
                Toast.makeText(getApplicationContext(), "Please set your Wake Time first", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(getApplicationContext(), "Please set your Sleep Time first", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Time time = new Time(hour, minute, 0);

        if (!wakeSet){
            wakeTime=time;
            wakeSet=true;
            newDay.setWakeUp(wakeTime);
        }
        else if (!sleepSet){
            sleepTime=time;
            sleepSet=true;
            newDay.setSleep(sleepTime);
        }

        else if (!startTimeFreeSet){
            startTimeFree=time;
            startTimeFreeSet=true;
        }
        else if (!endTimeFreeSet){
            endTimeFree=time;
            endTimeFreeSet=true;
        }

    }




}
