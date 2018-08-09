package com.example.smistry.woke;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v14.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.smistry.woke.models.Day;
import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.MessageEvent;

import org.apache.commons.io.FileUtils;
import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends AppCompatPreferenceActivity {

    public static ArrayList<Day> enteredItems;

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);


            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);

            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);


        //checking for data that has beeen previously inputed for days
        if (getIntent() != null) {
            enteredItems = Parcels.unwrap(getIntent().getParcelableExtra("days"));
        }

        //inputting default sample data
        if (enteredItems == null || enteredItems.size() == 0) {
            enteredItems = new ArrayList<>();
            enteredItems.add(new Day(new ArrayList<Free>(), "Sunday", new Time(6, 0, 0), new Time(22, 00, 00)));
            enteredItems.add(new Day(new ArrayList<Free>(), "Monday", new Time(6, 0, 0), new Time(22, 00, 00)));
            enteredItems.add(new Day(new ArrayList<Free>(), "Tuesday", new Time(6, 0, 0), new Time(22, 00, 00)));
            enteredItems.add(new Day(new ArrayList<Free>(), "Wednesday", new Time(6, 0, 0), new Time(22, 00, 00)));
            enteredItems.add(new Day(new ArrayList<Free>(), "Thursday", new Time(6, 0, 0), new Time(22, 00, 00)));
            enteredItems.add(new Day(new ArrayList<Free>(), "Friday", new Time(6, 0, 0), new Time(22, 00, 00)));
            enteredItems.add(new Day(new ArrayList<Free>(), "Saturday", new Time(6, 0, 0), new Time(22, 00, 00)));

        }

        //sending over the day's array and saving the data inside a file for persistence
        MessageEvent event = new MessageEvent(enteredItems);
        EventBus.getDefault().postSticky(event);
        writeItems();


    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    public void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || FixedTimePreferenceFragment.class.getName().equals(fragmentName)
                || GoalsFragment.class.getName().equals(fragmentName);
    }



    //fragment in which general settings are stored
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        EditText age;
        EditText name;
        Switch rain;
        Switch jacket;
        SeekBar temp;
        TextView tvTemp;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.general_settings, container, false);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            age = (EditText) view.findViewById(R.id.etAge);
            name = (EditText) view.findViewById(R.id.etName);
            rain = (Switch) view.findViewById(R.id.sUmbrella);
            jacket = (Switch) view.findViewById(R.id.sJacket);
            temp = (SeekBar) view.findViewById(R.id.sbTemp);
            tvTemp = (TextView) view.findViewById(R.id.tvTemp);

            final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());

            //looking for previously inputted information to populate the editText's
            if (!pref.getString("age", "no age").equals("no age")) {
                age.setText(pref.getString("age", "19"));
            }
            if (!pref.getString("name", "no name").equals("no name")) {
                name.setText(pref.getString("name", "Arce"));
            }

            rain.setChecked(pref.getBoolean("umbrella", false));
            jacket.setChecked(pref.getBoolean("jacket", false));
            tvTemp.setText(pref.getString("temp", "0F"));

            final SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();


            int value = Integer.valueOf(tvTemp.getText().toString().replaceAll("[^0-9]", ""));
            temp.setProgress(value);


            //listeners set up to watch for input and store the respective data into a sharedPreference
            rain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    prefEditor.putBoolean("umbrella", rain.isChecked());
                    prefEditor.apply();
                }
            });


            age.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    prefEditor.putString("age", age.getText().toString());
                    prefEditor.apply();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }


                @Override
                public void afterTextChanged(Editable editable) {
                    prefEditor.putString("name", editable.toString());
                    prefEditor.apply();

                }
            });


            temp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    tvTemp.setText(String.valueOf(i) + "F");
                    prefEditor.putString("temp", tvTemp.getText().toString());
                    prefEditor.apply();

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            if (!jacket.isChecked()) {
                temp.setVisibility(View.INVISIBLE);
                tvTemp.setVisibility(View.INVISIBLE);

            } else {
                temp.setVisibility(View.VISIBLE);
                tvTemp.setVisibility(View.VISIBLE);
            }


            jacket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (!b) {
                        temp.setVisibility(View.INVISIBLE);
                        tvTemp.setVisibility(View.INVISIBLE);
                        prefEditor.putBoolean("jacket", b);
                        prefEditor.apply();
                    } else {
                        temp.setVisibility(View.VISIBLE);
                        tvTemp.setVisibility(View.VISIBLE);
                        prefEditor.putBoolean("jacket", b);
                        prefEditor.apply();
                    }

                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class FixedTimePreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dow_buttons, container, false);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

            super.onViewCreated(view, savedInstanceState);


            final TextView Sunday = view.findViewById(R.id.tvSunday);
            final TextView Monday = view.findViewById(R.id.tvMonday);
            final TextView Tuesday = view.findViewById(R.id.tvTuesday);
            final TextView Wednesday = view.findViewById(R.id.tvWednesday);
            final TextView Thursday = view.findViewById(R.id.tvThursday);
            final TextView Friday = view.findViewById(R.id.tvFriday);
            final TextView Saturday = view.findViewById(R.id.tvSaturday);

            //listenting for what day of the week is pressed to open editDay with the proper information

            Sunday.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View view) {
                    ((SettingsActivity) getContext()).openEditDay(0, Sunday.getText().toString());
                }
            });
            Monday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((SettingsActivity) getContext()).openEditDay(1, Monday.getText().toString());
                }
            });

            Tuesday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SettingsActivity) getContext()).openEditDay(2, Tuesday.getText().toString());

                }
            });
            Wednesday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((SettingsActivity) getContext()).openEditDay(3, Wednesday.getText().toString());
                }
            });
            Thursday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SettingsActivity) getContext()).openEditDay(4, Thursday.getText().toString());

                }
            });
            Friday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((SettingsActivity) getContext()).openEditDay(5, Friday.getText().toString());
                }
            });

            Saturday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SettingsActivity) getContext()).openEditDay(6, Saturday.getText().toString());

                }
            });


            //setting responses for when users press buttons

            Sunday.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            v.getBackground().setColorFilter(0xe0efc8b7, PorterDuff.Mode.SRC_ATOP);
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

            Monday.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            v.getBackground().setColorFilter(0xe0efc8b7, PorterDuff.Mode.SRC_ATOP);
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

            Tuesday.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            v.getBackground().setColorFilter(0xe0efc8b7, PorterDuff.Mode.SRC_ATOP);
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

            Wednesday.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            v.getBackground().setColorFilter(0xe0efc8b7, PorterDuff.Mode.SRC_ATOP);
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

            Thursday.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            v.getBackground().setColorFilter(0xe0efc8b7, PorterDuff.Mode.SRC_ATOP);
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

            Friday.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            v.getBackground().setColorFilter(0xe0efc8b7, PorterDuff.Mode.SRC_ATOP);
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

            Saturday.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            v.getBackground().setColorFilter(0xe0efc8b7, PorterDuff.Mode.SRC_ATOP);
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

        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    //fragment where goals can be set
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GoalsFragment extends PreferenceFragment {
        EditText fitness;
        EditText work;
        EditText entertainment;
        EditText social;
        EditText other;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.goals_settings, container, false);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
            final SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();

            fitness = view.findViewById(R.id.sFitness);
            work = view.findViewById(R.id.sWork);
            entertainment = view.findViewById(R.id.sEntertainment);
            social = view.findViewById(R.id.sSocial);
            other = view.findViewById(R.id.sOther);


            String fitnessInput = pref.getString("fitness", "0");
            String workInput = pref.getString("work", "0");
            String eInput = pref.getString("entertainment", "0");
            String socialInput = pref.getString("social", "0");
            String otherInput = pref.getString("other", "0");


            //checking for previous input to populate for data
            if (!fitnessInput.equals("0")) {
                fitness.setText(fitnessInput);
            }
            if (!workInput.equals("0")) {
                work.setText(workInput);
            }
            if (!eInput.equals("0")) {
                entertainment.setText(eInput);
            }
            if (!socialInput.equals("0")) {
                social.setText(socialInput);
            }
            if (!otherInput.equals("0")) {
                other.setText(otherInput);
            }


            //setting on change listners to watch for when text is changed to save into the shared preferences
            fitness.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    prefEditor.putString("fitness", editable.toString());
                    prefEditor.apply();
                }
            });
            work.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    prefEditor.putString("work", editable.toString());
                    prefEditor.apply();
                }
            });
            entertainment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    prefEditor.putString("entertainment", editable.toString());
                    prefEditor.apply();
                }
            });
            social.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    prefEditor.putString("social", editable.toString());
                    prefEditor.apply();
                }
            });
            other.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    prefEditor.putString("other", editable.toString());
                    prefEditor.apply();
                }
            });


        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {

        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    public Time stringTOTime(String time) {
        String[] split = time.split(":");
        return new Time(Integer.valueOf(split[0]), Integer.valueOf(split[1]), 0);
    }


    //sending over the proper data to editDay
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void openEditDay(int position, String day) {
        Intent i = new Intent(SettingsActivity.this, editDayActivity.class);
        i.putExtra("Position", position);
        i.putExtra("Day", day);
        i.putExtra("Days", Parcels.wrap(enteredItems));
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            enteredItems.clear();
            enteredItems.addAll((ArrayList<Day>) Parcels.unwrap(data.getParcelableExtra("days")));
            MessageEvent event = new MessageEvent(enteredItems);
            EventBus.getDefault().postSticky(event);
            writeItems();
        }

    }


    // write the items to the filesystem
    private void writeItems() {
        try {
            // save the item list as a line-delimited text file
            FileUtils.writeLines(getDataFile(), enteredItems);
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