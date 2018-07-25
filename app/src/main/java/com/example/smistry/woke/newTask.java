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
    int iTaskDate=0;
    Time time;
    int duration;
    boolean isDateSet;
    ArrayList<Day> myDays = new ArrayList<>();

    Time start = new Time(10,30,0);
    Time end = new Time(12,0,0);
    ArrayList<Task> tasks = new ArrayList<Task>();
    Free example = new Free(tasks, start, end, 90);
    ArrayList<Free> freeBlocks = new ArrayList<Free>();
    Calendar c = Calendar.getInstance();

    String [] months = {"Jan","Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};

    private NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        ButterKnife.bind(this);
        myDays=Parcels.unwrap(getIntent().getParcelableExtra("dayArray"));
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

                    setTaskWithinFreeBlock(myDays.get(getIndexDay(iTaskDate)).getFreeBlocks()); //getting free blocks for task Date
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

    public void setTaskWithinFreeBlock (ArrayList<Free> freeBlocks){
        duration = (Integer.parseInt(etHours.getText().toString())*60) + Integer.parseInt(etMinutes.getText().toString());
        Task task = new Task(etTitle.getText().toString(), item.toString(), duration, taskDate);


        for(int i = 0; i < freeBlocks.size(); i++) {
            if (freeBlocks.get(i).getFreeBlockDuration() >= task.getDuration()) { //looping through all free blocks
               // tasks.add(task);
                freeBlocks.get(i).getTasks().add(task); // adding updated task list to free block
                task.setTime(freeBlocks.get(i).getStart()); //setting start time for task
                taskDate.setHours(task.getTime().getHours());
                taskDate.setMinutes(task.getTime().getMinutes());

                start.setHours(start.getHours() + (Integer.parseInt(etHours.getText().toString())));
                start.setMinutes(start.getMinutes() + (Integer.parseInt(etMinutes.getText().toString()))); //changing free block start time
                freeBlocks.get(i).setStart(start);
                Log.d("Testing", start.toString());
                Intent intent = new Intent(newTask.this, bottomNav.class);
                intent.putExtra("newFreeBlock", Parcels.wrap(freeBlocks));
                intent.putExtra("dayIndex",iTaskDate);
                intent.putExtra("freeIndex",i);
                setResult(RESULT_OK, intent); // set result code and bundle data for response


                Intent setAlarm = new Intent(AlarmClock.ACTION_SET_ALARM);
                setAlarm.putExtra(AlarmClock.EXTRA_HOUR,taskDate.getHours());
                setAlarm.putExtra(AlarmClock.EXTRA_MINUTES, taskDate.getMinutes());
                setAlarm.putExtra(AlarmClock.EXTRA_MESSAGE, etTitle.getText().toString());
                setAlarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);

                startActivity(setAlarm);

                Log.d("Testing", "Set the alarm");
                Log.d("Testing", task.getTime().toString());
                Log.d("Testing", taskDate.toString());

                finish(); // closes the activity, pass data to parent
            }
            else{
                Toast.makeText(newTask.this, "Please enter a different duration", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public int getIndexDay(int dayOfWeek){
        switch(dayOfWeek%7){
            case 0: //Sunday
                return compareDays(0,"Sunday");
            case 1: //Monday
                return compareDays(1,"Monday");
            case 2: //Tuesday
                return compareDays(2,"Tuesday");
            case 3: //Wednesday
                return compareDays(3,"Wednesday");
            case 4: //Thursday
                return compareDays(4,"Thursday");
            case 5: //Friday
                return compareDays(5,"Friday");
            case 6: //Saturday
                return compareDays(6,"Saturday");
        }
        return -1;
    }

    public int compareDays(int iCase, String sDay){
        for(int i=0;i<myDays.size();i++) {
            if(myDays.get(iCase).getDayOfWeek().equals(sDay));
            return i;
        }
        return -1;
    }
}
