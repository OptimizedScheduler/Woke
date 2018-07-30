package com.example.smistry.woke;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.smistry.woke.models.Day;
import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.MessageEvent;
import com.example.smistry.woke.models.Task;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import java.io.File;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SettingsActivity extends AppCompatPreferenceActivity  {

    public static ArrayList<Day> enteredItems;
    // Fake testing data to ensure adding free blocks works
    static ArrayList<String>DOW;


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
//        PreferenceManager.setDefaultValues(this, R.xml.pref_sleep_time, false);
//        PreferenceManager.setDefaultValues(this, R.xml.pref_free_times, false);
        if (DOW==null || enteredItems== null){
            DOW= new ArrayList<>();
            enteredItems= new ArrayList<>();
            enteredItems.add(new Day(new ArrayList<Free>(),"Sunday", new Time(22,0,0),new Time(6,00,00)));
            enteredItems.add(new Day(new ArrayList<Free>(),"Monday", new Time(22,0,0),new Time(6,00,00)));
            enteredItems.add(new Day(new ArrayList<Free>(),"Tuesday", new Time(22,0,0),new Time(6,00,00)));
            enteredItems.add(new Day(new ArrayList<Free>(),"Wednesday", new Time(22,0,0),new Time(6,00,00)));
            enteredItems.add(new Day(new ArrayList<Free>(),"Thursday", new Time(22,0,0),new Time(6,00,00)));
            enteredItems.add(new Day(new ArrayList<Free>(),"Friday", new Time(22,0,0),new Time(6,00,00)));
            enteredItems.add(new Day(new ArrayList<Free>(),"Saturday", new Time(22,0,0),new Time(6,00,00)));

        }
        readItems();
        MessageEvent event = new MessageEvent(enteredItems);
        EventBus.getDefault().postSticky(event);




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
                || FixedTimePreferenceFragment.class.getName().equals(fragmentName);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);



            setHasOptionsMenu(true);

            if (!((SwitchPreference)getPreferenceManager().findPreference("weatherSwitch")).isChecked()) {
                getPreferenceManager().findPreference("rainSwitch").setEnabled(false);
                getPreferenceManager().findPreference("jacketSwitch").setEnabled(false);
                getPreferenceManager().findPreference("seekBar").setEnabled(false);
            }
            else{
                getPreferenceManager().findPreference("rainSwitch").setEnabled(true);
                getPreferenceManager().findPreference("jacketSwitch").setEnabled(true);
                getPreferenceManager().findPreference("seekBar").setEnabled(true);
            }

            ((SwitchPreference)getPreferenceManager().findPreference("weatherSwitch")).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (!((SwitchPreference)preference).isChecked()) {
                        getPreferenceManager().findPreference("rainSwitch").setEnabled(false);
                        getPreferenceManager().findPreference("jacketSwitch").setEnabled(false);
                        getPreferenceManager().findPreference("seekBar").setEnabled(false);
                    }

                    else{
                        getPreferenceManager().findPreference("rainSwitch").setEnabled(true);
                        getPreferenceManager().findPreference("jacketSwitch").setEnabled(true);
                        getPreferenceManager().findPreference("seekBar").setEnabled(true);
                    }

                    return false;
                }
            });

            ((SwitchPreference)getPreferenceManager().findPreference("jacketSwitch")).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (((SwitchPreference)preference).isChecked()){
                        getPreferenceManager().findPreference("seekBar").setVisible(true);
                    }
                    else{
                        getPreferenceManager().findPreference("seekBar").setVisible(false);

                    }
                    return false;
                }
            });
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
           // addPreferencesFromResource(R.xml.pref_free_times);
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


            final TextView Sunday=view.findViewById(R.id.tvSunday);
            final TextView Monday= view.findViewById(R.id.tvMonday);
            final TextView Tuesday= view.findViewById(R.id.tvTuesday);
            final TextView Wednesday=view.findViewById(R.id.tvWednesday);
            final TextView Thursday= view.findViewById(R.id.tvThursday);
            final TextView Friday=view.findViewById(R.id.tvFriday);
            final TextView Saturday=view.findViewById(R.id.tvSaturday);

            Sunday.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View view) {
                    ((SettingsActivity)getContext()).openEditDay(0,Sunday.getText().toString());
                }
            });
            Monday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((SettingsActivity)getContext()).openEditDay(1,Monday.getText().toString());
                }
            });

            Tuesday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SettingsActivity)getContext()).openEditDay(2,Tuesday.getText().toString());

                }
            });
            Wednesday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((SettingsActivity)getContext()).openEditDay(3,Wednesday.getText().toString());
                }
            });
            Thursday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SettingsActivity)getContext()).openEditDay(4,Thursday.getText().toString());

                }
            });
            Friday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((SettingsActivity)getContext()).openEditDay(5,Friday.getText().toString());
                }
            });

            Saturday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SettingsActivity)getContext()).openEditDay(6,Saturday.getText().toString());

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




    public Time stringTOTime(String time){
        String[] split=time.split(":");
        return new Time(Integer.valueOf(split[0]),Integer.valueOf(split[1]),0);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void openEditDay(int position, String day){
        Intent i= new Intent(SettingsActivity.this, editDayActivity.class);
        i.putExtra("Position", position);
        i.putExtra("Day", day);
        i.putExtra("Days", Parcels.wrap(enteredItems));
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==0){
            Day newDay= Parcels.unwrap(data.getParcelableExtra("newDay"));
            int position=data.getIntExtra("Position",0);
            enteredItems.remove(position);
            enteredItems.add(position, newDay);

            MessageEvent event = new MessageEvent(enteredItems);
            EventBus.getDefault().postSticky(event);

         //   EventBus.getDefault().postSticky(new MessageEvent(enteredItems));
        }

    }


    String testData="Sunday_02:28:00_04:28:00_[10:30:00;05:29:00;60;hey-Item 1-1-Mon Jul 30 10:29:00 PDT 2018-10:29:00/";
    String testData2="Monday_22:00:00_06:00:00_[]";
    String testData3= "Wednesday_21:00:00_06:00:00_[]";
    ArrayList<String> testArray=new ArrayList<>();


    ArrayList<Day>days= new ArrayList<>();
    // returns the file in which the data is stored
    public File getDataFile() {
        return new File(this.getFilesDir(), "days.txt");
    }

    // read the items from the file system
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void readItems() {
        try {
//            ArrayList<String> dayStrings;
//            // create the array using the content in the file
//            dayStrings = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
            days.clear();

            testArray.add(testData);
            testArray.add(testData2);
            testArray.add(testData3);

            for (String daysString : testArray) {
                Log.d("Day", daysString);

                String[] params = daysString.split("_");
                String dayOfWeek = params[0];
                String[] wakeTimeSplit = params[1].split(":");
                String[] sleepTimeSplit = params[2].split(":");
                Time wakeTime = new Time(Integer.valueOf(wakeTimeSplit[0]), Integer.valueOf(wakeTimeSplit[1]), 0);
                Time sleepTime = new Time(Integer.valueOf(sleepTimeSplit[0]), Integer.valueOf(sleepTimeSplit[1]), 0);


                String freeBlocks=params[3];
                //remove brackets from array toStirng
                freeBlocks=freeBlocks.substring(1,freeBlocks.length()-1);
                String [] freeBlocksSplit=freeBlocks.split(",");
                ArrayList<Free>frees=new ArrayList<>();

                if (!freeBlocks.equals("")) {
                    for (String free : freeBlocksSplit) {
                        String[] splitFree = free.split(";");
                        String[] freeStartSplit = splitFree[0].split(":");
                        String[] freeEndSplit = splitFree[1].split(":");
                        Time freeStart = new Time(Integer.valueOf(freeStartSplit[0].replaceAll("\\s+", "")), Integer.valueOf(freeStartSplit[1].replaceAll("\\s+", "")), 0);
                        Time freeEnd = new Time(Integer.valueOf(freeEndSplit[0].replaceAll("\\s+", "")), Integer.valueOf(freeEndSplit[1].replaceAll("\\s+", "")), 0);
                        int duration = Integer.valueOf(splitFree[2]);
                        ArrayList<Task> tasks = new ArrayList<>();

                        if (splitFree.length == 4) {

                            String[] tasksStrings = splitFree[3].split("/");
                            for (String taskString : tasksStrings) {

                                String[] taskStringSplit = taskString.split("-");
                                String[] tasktimeSplit = taskStringSplit[4].split(":");

                                String title = taskStringSplit[0];
                                String category = taskStringSplit[1];
                                int durationTask = Integer.parseInt(taskStringSplit[2].replaceAll("\\s+", ""));
                                DateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss ZZZ yyyy", Locale.ENGLISH);
                                Date date = format.parse(taskStringSplit[3]);
                                Time time = new Time(Integer.valueOf(tasktimeSplit[0].replaceAll("\\s+", "")), Integer.valueOf(tasktimeSplit[1].replaceAll("\\s+", "")), 0);
                                Task newTask = new Task(title, category, durationTask, date, time);
                                tasks.add(newTask);
                            }
                        }
                        frees.add(new Free(tasks, freeStart, freeEnd, duration));
                    }
                }
                Day newDay= new Day(frees,dayOfWeek,wakeTime, sleepTime);
                days.add(newDay);
                Log.d("Day", "added new day: " + newDay.toString());
            }

        }
//        catch (IOException e) {
//            // print the error to the console
//            e.printStackTrace();
//            // just load an empty list
//            days = new ArrayList<>();
//        }
        catch (ParseException e) {
            e.printStackTrace();
            Log.d("Home Activity", "Error with date to String");
        }

    }


}