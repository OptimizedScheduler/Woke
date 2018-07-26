package com.example.smistry.woke.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),(TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, false);

    }




//    @Override
//    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
//        Time time = new Time(hour, minute, 0);
//
//    }

//    @Override
//    public void onClick(View v){
//        DialogFragment timePicker = new TimePickerFragment();
//        timePicker.show(getSupportFragmentManager(), timePicker);
//    }
}
