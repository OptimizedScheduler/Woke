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
    TextView tvSleepTime;
    TextView tvWakeTime;
    Button btsleepTime;
    Button btwakeTime;
    Button btstartTimeFree;
    Button btendTimeFree;
    Button makeFree;


    Boolean sleepSet;
    Boolean wakeSet;
    Boolean startTimeFreeSet;
    Boolean endTimeFreeSet;


    Time sleepTime;
    Time wakeTime;
    Time startTimeFree;
    Time endTimeFree;


    ArrayList<Day>days;
    ArrayList<Free> frees;
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

        position= getIntent().getIntExtra("Position",0);
        days= Parcels.unwrap(getIntent().getParcelableExtra("Days"));
        Day currDay=days.get(position);
        frees=currDay.getFreeBlocks();



        tvSleepTime= (TextView)findViewById(R.id.tvSleepTime);
        tvWakeTime= (TextView)findViewById(R.id.tvWakeTime);
        tvSleepTime.setText(currDay.getSleep().toString());
        tvWakeTime.setText(currDay.getWakeUp().toString());

        day= (TextView)findViewById(R.id.tvDayEditDay);
        day.setText(getIntent().getStringExtra("Day").toString());
        enteredFreeTimes= (TextView)findViewById(R.id.tvEnteredFreeEditDay);
        for (Free free:frees){
            enteredFreeTimes.setText(enteredFreeTimes.getText().toString()+" "+free.toString());
        }

        btsleepTime = (Button)findViewById(R.id.btSleepTime);
        btwakeTime = (Button)findViewById(R.id.btWakeTime);
        btstartTimeFree = (Button)findViewById(R.id.btFreeTimeStart);
        btendTimeFree = (Button)findViewById(R.id.btFreeTimeEnd);
        makeFree= (Button)findViewById(R.id.btSetFree) ;

        btwakeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wakeSet=true;
                sleepSet=false;
                startTimeFreeSet=false;
                endTimeFreeSet=false;

                DialogFragment TimePicker= new TimePickerFragment();
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
                    DialogFragment TimePicker= new TimePickerFragment();
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
                    DialogFragment TimePicker= new TimePickerFragment();
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
                    DialogFragment TimePicker= new TimePickerFragment();
                    TimePicker.show(getSupportFragmentManager(), "TimePick");



            }
        });

        makeFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startTimeFree!=null&& endTimeFree!=null){

                    Free toAdd=new Free(new ArrayList<Task>(), startTimeFree, endTimeFree, 60);
                    days.get(position).getFreeBlocks().add(toAdd);
                    enteredFreeTimes.setText(enteredFreeTimes.getText().toString()+" "+toAdd.toString());
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
            tvWakeTime.setText(wakeTime.toString());
        }
        else if (sleepSet){
            sleepTime=time;
            sleepSet=true;
            days.get(position).setSleep(sleepTime);
            tvSleepTime.setText(sleepTime.toString());
        }

        else if (startTimeFreeSet){
            startTimeFree=time;
            startTimeFreeSet=true;
        }
        else if (endTimeFreeSet){
            endTimeFree=time;
            endTimeFreeSet=true;
        }
        sleepSet=false;
        wakeSet=false;
        startTimeFreeSet=false;
        endTimeFreeSet=false;

    }




}
