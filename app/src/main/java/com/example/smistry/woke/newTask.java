package com.example.smistry.woke;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smistry.woke.fragments.DatePickerFragment;
import com.example.smistry.woke.models.Day;
import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.MessageEvent;
import com.example.smistry.woke.models.Task;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


public class newTask extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener{
    @BindView(R.id.spCategory) Spinner spCategory;
    @BindView(R.id.btDate) Button btDate;
    @BindView(R.id.tvDate) TextView tvDate;
    @BindView(R.id.ivExit) ImageView ivExit;
    @BindView(R.id.btFinish) Button btFinish;
    @BindView(R.id.etHours) EditText etHours;
    @BindView(R.id.etMinutes) EditText etMinutes;
    @BindView(R.id.tvHours) TextView tvHours;
    @BindView(R.id.tvMinutes) TextView tvMinutes;
    @BindView(R.id.etTitle) EditText etTitle;

    Object item;
    Date taskDate; //Date chosen for the activity from DatePickerFragment
    int iTaskDate=0; // Day of the week in int format
    int duration;
    boolean isDateSet;
    ArrayList<Day> myDays = new ArrayList<>();

    Time start = new Time(00,00,00);
    Time end = new Time(12,0,0);
    ArrayList<Free> freeBlocks = new ArrayList<Free>();

    String [] months = {"Jan","Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};

    private NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        ButterKnife.bind(this);
        myDays=Parcels.unwrap(getIntent().getParcelableExtra("dayArray"));
        final DateFormat date = new SimpleDateFormat("MM dd, yyyy");
        //freeBlocks.add(example);

        notificationManager = NotificationManagerCompat.from(this);

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
               item = parent.getItemAtPosition(pos);

            }
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(newTask.this,"Please make a category selection", Toast.LENGTH_SHORT);
            }
        });


        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datepicker = new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(), "date pick");
            }
        });

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(newTask.this,bottomNav.class);
                startActivity(intent);
            }
        });


        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strHours = etHours.getText().toString();
                String strMinutes = etMinutes.getText().toString();
                String strTitle = etTitle.getText().toString();

                if(TextUtils.isEmpty(strTitle)){
                    Toast.makeText(newTask.this, "Please enter a Task Title!", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(strHours) && TextUtils.isEmpty(strMinutes) || etHours.equals("0") && etMinutes.equals("0")  ) {
                    Toast.makeText(newTask.this,"Duration is a required field. Please enter a value!", Toast.LENGTH_SHORT).show();
                }


               else if(taskDate == null){
                    Toast.makeText(newTask.this,"Date is a required field. Please enter a value!", Toast.LENGTH_SHORT).show();
                }

                else {
                    if(TextUtils.isEmpty(strHours)) {
                        etHours.setText("0");
                    }

                    if(TextUtils.isEmpty(strMinutes)) {
                        etMinutes.setText("0");
                    }
                    duration = (Integer.parseInt(etHours.getText().toString())*60) + Integer.parseInt(etMinutes.getText().toString());
                    Task task = new Task(etTitle.getText().toString(), item.toString(), duration, taskDate);

                    setTaskWithinFreeBlock(myDays,task);

                    MessageEvent event = new MessageEvent(myDays);
                    EventBus.getDefault().postSticky(event);



                    finish();
                }

            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Log.d("Date Format", String.valueOf(year) + " " + String.valueOf(month)+ " " + String.valueOf(day));
        taskDate = new Date(year-1900, month, day);
        isDateSet = true;
        tvDate.setText(months[month]+ " " + day + ","+ " " + year);
        Log.d("Date Format", taskDate.toString());
        Log.d("Date Format", String.valueOf(taskDate.getYear()));
        iTaskDate=taskDate.getDay();
    }

    public void setTaskWithinFreeBlock (ArrayList<Day> dayArray, Task task){
        boolean reachEnd = false;
        int index = iTaskDate;
        int blockDuration;
        while(!reachEnd && index<7){
            for(int i = 0; i <dayArray.get(index).getFreeBlocks().size(); i++) {
                blockDuration = (dayArray.get(index).getFreeBlocks().get(i).getEnd().getHours()*60 + dayArray.get(index).getFreeBlocks().get(i).getEnd().getMinutes());
                blockDuration -= dayArray.get(index).getFreeBlocks().get(i).getStart().getHours()*60 + dayArray.get(index).getFreeBlocks().get(i).getStart().getMinutes();
                //blockDuration = Integer.parseInt(dayArray.get(index).getFreeBlocks().get(i).getStart().toString()) - Integer.parseInt(dayArray.get(index).getFreeBlocks().get(i).getEnd().toString());
                if (blockDuration >= task.getDuration()) { //looping through all free blocks
                    if(dayArray.get(index).getFreeBlocks().get(i).getTasks()==null)
                        dayArray.get(index).getFreeBlocks().get(i).setTasks(new ArrayList<Task>());
                    dayArray.get(index).getFreeBlocks().get(i).getTasks().add(task); // adding updated task list to free block
                    task.setTime(dayArray.get(index).getFreeBlocks().get(i).getStart()); //setting start time for task
                    taskDate.setHours(task.getTime().getHours());
                    taskDate.setMinutes(task.getTime().getMinutes());

                    start.setHours(task.getTime().getHours() + (Integer.parseInt(etHours.getText().toString())));
                    start.setMinutes(task.getTime().getMinutes() + (Integer.parseInt(etMinutes.getText().toString()))); //changing free block start time
                    dayArray.get(index).getFreeBlocks().get(i).setStart(start);
                    Log.d("Testing", start.toString());

                    setAlarm(new Time (taskDate.getHours(),taskDate.getHours(),00), task,etTitle.getText().toString(),i, iTaskDate);

                    MessageEvent event = new MessageEvent(myDays);
                    EventBus.getDefault().postSticky(event);
                    reachEnd = true;
                }
            }

            //TODO --- Show dialog fragment and return to nextMorning
            boolean nextMorning = true; //should be updated with dialog fragment

            if(nextMorning){
                Time t1= myDays.get(index+1).getWakeUp(); //wake up time of current day
                int newWake = t1.getHours()*60 + t1.getMinutes() - duration;  // new wake up time (Int format)
                Time t2 = new Time (newWake/60, (newWake%60)*60,00); //new Wake up time  && start of the task
                task.setTime(t2);
                Calendar cal = Calendar.getInstance();
                cal.setTime(taskDate);
                cal.add(Calendar.DATE, 1);
                Date movedDate = cal.getTime();
                movedDate.setHours(task.getTime().getHours());
                movedDate.setMinutes(task.getTime().getMinutes());
                task.setDate(movedDate);
                myDays.get(index+1).getFreeBlocks().add(0,new Free(new ArrayList<Task>(),t2,t1,0));
                myDays.get(index+1).getFreeBlocks().get(0).getTasks().add(0,task);

                setAlarm(t2,task,etTitle.getText().toString(),0,iTaskDate+1);
                reachEnd=true;
                MessageEvent event = new MessageEvent(myDays);
                EventBus.getDefault().postSticky(event);
            }

            else{
                index = (index++)%7;
                if(index==iTaskDate)
                    reachEnd=true;
            }
        }

        Toast.makeText(this, "No time during this week",Toast.LENGTH_LONG).show();
    }


    public void setAlarm(Time time, Task task,String title, int FreeIndex, int DayIndex){
        Intent setAlarm = new Intent(AlarmClock.ACTION_SET_ALARM);
        setAlarm.putExtra(AlarmClock.EXTRA_HOUR,time.getHours());
        setAlarm.putExtra(AlarmClock.EXTRA_MINUTES, time.getMinutes());
        setAlarm.putExtra(AlarmClock.EXTRA_MESSAGE, title);
        setAlarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        ArrayList<Integer> alarmDays = new ArrayList<>();
        alarmDays.add(task.getDate().getDay()+1);
        setAlarm.putExtra(AlarmClock.EXTRA_DAYS, alarmDays);

        Intent data = new Intent();
        //pass relevant data
        data.putExtra("newFreeBlock", Parcels.wrap(myDays.get(DayIndex).getFreeBlocks()));
        data.putExtra("dayIndex",DayIndex);
        data.putExtra("freeIndex",FreeIndex);
        setResult(RESULT_OK, data); // set result code and bundle data for response

        startActivity(setAlarm);

    }

}
