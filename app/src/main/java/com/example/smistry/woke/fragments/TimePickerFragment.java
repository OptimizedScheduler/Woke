package com.example.smistry.woke.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;


// class used in free block settings to set the free time for the day
// dialog fragment generates a clock for users to select the free time
public class TimePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int hour = getArguments().getInt("hour");
        int min = getArguments().getInt("minutes");

        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), hour, min, false);
    }

    public static TimePickerFragment newInstance(int hour, int min) {
        TimePickerFragment f = new TimePickerFragment();

        Bundle args = new Bundle();
        args.putInt("hour", hour);
        args.putInt("minutes", min);
        f.setArguments(args);

        return f;
    }

}
