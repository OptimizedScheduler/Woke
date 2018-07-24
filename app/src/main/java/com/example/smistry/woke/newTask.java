package com.example.smistry.woke;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
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
import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.Task;

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
    Date taskDate;
    Time time;
    int duration;
    boolean isDateSet;

    Time start = new Time(10,30,0);
    Time end = new Time(12,0,0);
    ArrayList<Task> tasks = new ArrayList<Task>();
    Free example = new Free(tasks, start, end, 90);
    ArrayList<Free> freeBlocks = new ArrayList<Free>();
    final int REQUEST_CODE = 1;
    Calendar c = Calendar.getInstance();

    String [] months = {"Jan","Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};

    private NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        ButterKnife.bind(this);
        final DateFormat date = new SimpleDateFormat("MM dd, yyyy");
        freeBlocks.add(example);

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
                    setTaskWithinFreeBlock(freeBlocks);
                }
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        taskDate = new Date(year, month, day);
        isDateSet = true;
        tvDate.setText(months[month]+ " " + day + ","+ " " + year);
        Log.d("Date Format", taskDate.toString());
    }

    public void setTaskWithinFreeBlock (ArrayList<Free> freeBlocks){
        duration = (Integer.parseInt(etHours.getText().toString())*60) + Integer.parseInt(etMinutes.getText().toString());
        Task task = new Task(etTitle.toString(), item.toString(), duration, taskDate);
        for(int i = 0; i < freeBlocks.size(); i++) {
            if (freeBlocks.get(i).getFreeBlockDuration() >= task.getDuration()) { //looping through all free blocks
                tasks.add(task);
                freeBlocks.get(i).setTasks(tasks); // adding updated task list to free block
                task.setTime(freeBlocks.get(i).getStart()); //setting start time for task
                taskDate.setHours(task.getTime().getHours());
                taskDate.setMinutes(task.getTime().getMinutes());

                start.setHours(start.getHours() + (Integer.parseInt(etHours.getText().toString())));
                start.setMinutes(start.getMinutes() + (Integer.parseInt(etMinutes.getText().toString()))); //changing free block start time
                freeBlocks.get(i).setStart(start);
                Log.d("Testing", start.toString());
                Intent intent = new Intent(newTask.this, bottomNav.class);
                intent.putExtra("task", Parcels.wrap(task));
                startActivityForResult(intent, REQUEST_CODE);

                Intent setAlarm = new Intent(AlarmClock.ACTION_SET_ALARM);
                setAlarm.putExtra(AlarmClock.EXTRA_HOUR,taskDate.getHours());
                setAlarm.putExtra(AlarmClock.EXTRA_MINUTES, taskDate.getMinutes());
                setAlarm.putExtra(AlarmClock.EXTRA_MESSAGE, etTitle.getText().toString());
                setAlarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);

                startActivity(setAlarm);

                Log.d("Testing", "Set the alarm");
                Log.d("Testing", task.getTime().toString());
                Log.d("Testing", taskDate.toString());


            }
            else{
                Toast.makeText(newTask.this, "Please enter a different duration", Toast.LENGTH_SHORT).show();
            }
        }


    }

}
