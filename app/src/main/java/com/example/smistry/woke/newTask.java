package com.example.smistry.woke;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.smistry.woke.models.Task;

import org.parceler.Parcels;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class newTask extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{
    @BindView(R.id.spCategory) Spinner spCategory;
    @BindView(R.id.sbDuration) SeekBar sbDuration;
    @BindView(R.id.cbAutomated)CheckBox cbAutomated;
    @BindView(R.id.switchDN) Switch switchDN;
    @BindView(R.id.sbPriority) SeekBar sbPriority;

    @BindView(R.id.btTime) Button btTime;
    @BindView(R.id.btDate) Button btDate;
    @BindView(R.id.time) TextView tvTime;
    @BindView(R.id.tvDate) TextView tvDate;
    @BindView(R.id.ivExit) ImageView ivExit;
    @BindView(R.id.btFinish) Button btFinish;
    @BindView(R.id.etHours) EditText etHours;
    @BindView(R.id.etMinutes) EditText etMinutes;
    @BindView(R.id.tvHours) TextView tvHours;
    @BindView(R.id.tvMinutes) TextView tvMinutes;


    Object item;
    Date taskDate;
    Time time;
    int hourOfDay;
    int minute;
    int duration;
    boolean isDateSet;

    String [] months = {"Jan","Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        ButterKnife.bind(this);
        final DateFormat date = new SimpleDateFormat("MM dd, yyyy");

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                item = parent.getItemAtPosition(pos);

            }
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(newTask.this,"Please make a category selection", Toast.LENGTH_SHORT);
            }
        });


        btTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timepicker = new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(), "time pick");
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


        cbAutomated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cbAutomated.isChecked()){
                    btTime.setVisibility(View.INVISIBLE);
                }

                else {
                    btTime.setVisibility(View.VISIBLE);
                }
            }
        });

        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strHours = etHours.getText().toString();
                String strMinutes = etMinutes.getText().toString();

                if(TextUtils.isEmpty(strHours) && TextUtils.isEmpty(strMinutes) || etHours.equals("0") && etMinutes.equals("0")  ) {
                    Toast.makeText(newTask.this,"Duration is a required field. Please enter a value!", Toast.LENGTH_SHORT).show();
                }

                else if(sbPriority.getProgress() == 0){
                    Toast.makeText(newTask.this,"Priority is a required field. Please enter a value!", Toast.LENGTH_SHORT).show();
                }

                else if(taskDate == null){
                    Toast.makeText(newTask.this,"Date is a required field. Please enter a value!", Toast.LENGTH_SHORT).show();
                }

                else if(!cbAutomated.isChecked() && time == null){
                    Toast.makeText(newTask.this,"Time is a required field if you are not automating. Please enter a value!", Toast.LENGTH_SHORT).show();
                }

                else {
                    if(TextUtils.isEmpty(strHours)) {
                        etHours.setText("0");
                    }

                    if(TextUtils.isEmpty(strMinutes)) {
                        etMinutes.setText("0");
                    }
                    duration = (Integer.parseInt(etHours.getText().toString())*60) + Integer.parseInt(etMinutes.getText().toString());
                    Task task = new Task(item.toString(), duration , cbAutomated.isChecked(), sbPriority.getProgress(), taskDate, switchDN.isChecked());
                    Intent intent = new Intent(newTask.this, bottomNav.class);
                    intent.putExtra("task", Parcels.wrap(task));
                    startActivity(intent);
                    Log.d("Saved info", task.toString());
                }
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        time = new Time(hourOfDay, minute,0);
        this.hourOfDay = hourOfDay;
        this.minute = minute;

        if(!isDateSet){
            Toast.makeText(newTask.this, "Please set the date first", Toast.LENGTH_SHORT).show();
        }

        else {
            tvTime.setText("Hour: " + hourOfDay + " Minute: " + minute);
            taskDate.setHours(hourOfDay);
            taskDate.setMinutes(minute);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        taskDate = new Date(year, month, day);
        isDateSet = true;
        tvDate.setText(months[month]+ " " + day + ","+ " " + year);
    }
}