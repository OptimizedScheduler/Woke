package com.example.smistry.woke;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smistry.woke.models.Day;
import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.Task;

import org.parceler.Parcels;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class SettingsActivity extends AppCompatPreferenceActivity  {

    public static ArrayList<ArrayList<Free>> enteredItems;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_sleep_time, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_free_times, false);
        if (DOW==null || enteredItems== null){
            DOW= new ArrayList<>();
            enteredItems= new ArrayList<>();

            DOW.add("Monday");
            DOW.add("Tuesday");
            DOW.add("Wednesday");
            DOW.add("Thursday");
            DOW.add("Friday");
            DOW.add("Saturday");
            DOW.add("Sunday");

            ArrayList<Free> monItems= new ArrayList<>();
            ArrayList<Free>tuesItems= new ArrayList<>();
            monItems.add(new Free(new ArrayList<Task>(), new Time(4,5,6), new Time(4,45,6), 40));
            monItems.add(new Free(new ArrayList<Task>(), new Time(5,5,6), new Time(5,45,6), 40));
            tuesItems.add(new Free(new ArrayList<Task>(), new Time(4,5,6), new Time(4,45,6), 40));
            enteredItems.add(0, new ArrayList<Free>());
            enteredItems.add(1, monItems);
            enteredItems.add(2,tuesItems );
            enteredItems.add(3,new ArrayList<Free>());
            enteredItems.add(4, new ArrayList<Free>());
            enteredItems.add(5,new ArrayList<Free>());
            enteredItems.add(6, new ArrayList<Free>());

        }
        

    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
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
                || SleepPreferenceFragment.class.getName().equals(fragmentName)
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

        private SwipeRefreshLayout swipeContainer;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
           // addPreferencesFromResource(R.xml.pref_free_times);
           // setHasOptionsMenu(true);
        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.add_free, container, false);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            RecyclerView options= view.findViewById(R.id.rvFree);
            super.onViewCreated(view, savedInstanceState);


            settingsFreeAdapter adapter= new settingsFreeAdapter( DOW, enteredItems);
            //RecyclerView setup (layout manager, use adapter)
            options.setLayoutManager(new LinearLayoutManager(getContext()));
            options.setAdapter(adapter);

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
    public static class SleepPreferenceFragment extends PreferenceFragment {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_sleep_time);
            setHasOptionsMenu(true);

            SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor=preferences.edit();


            final EditTextPreference SundaySleep= (EditTextPreference) findPreference("sleepTimeSunday");
            final EditTextPreference MondaySleep= (EditTextPreference) findPreference("sleepTimeMonday");
            final EditTextPreference TuesdaySleep= (EditTextPreference) findPreference("sleepTimeTuesday");
            final EditTextPreference WednesdaySleep= (EditTextPreference) findPreference("sleepTimeWednesday");
            final EditTextPreference ThursdaySleep= (EditTextPreference) findPreference("sleepTimeThursday");
            final EditTextPreference FridaySleep= (EditTextPreference) findPreference("sleepTimeFriday");
            final EditTextPreference SaturdaySleep= (EditTextPreference) findPreference("sleepTimeSaturday");


            editor.putString("sleepTimeSunday",SundaySleep.getText());
            editor.putString("sleepTimeMonday", MondaySleep.getText());
            editor.putString("sleepTimeTuesday", TuesdaySleep.getText());
            editor.putString("sleepTimeWednesday", WednesdaySleep.getText());
            editor.putString("sleepTimeThursday", ThursdaySleep.getText());
            editor.putString("sleepTimeFriday", FridaySleep.getText());
            editor.putString("sleepTimeSaturday", SaturdaySleep.getText());

            editor.commit();

            SundaySleep.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("sleepTimeSunday",SundaySleep.getText());
                    editor.commit();
                    return true;
                }
            });
            MondaySleep.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("sleepTimeMonday",MondaySleep.getText());
                    editor.commit();
                    return true;
                }
            });
            TuesdaySleep.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("sleepTimeTuesday",TuesdaySleep.getText());
                    editor.commit();
                    return false;
                }
            });
            WednesdaySleep.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("sleepTimeWednesday",WednesdaySleep.getText());
                    editor.commit();
                    return false;
                }
            });
            ThursdaySleep.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("sleepTimeThursday",ThursdaySleep.getText());
                    editor.commit();
                    return false;
                }
            });
            FridaySleep.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("sleepTimeFriday",FridaySleep.getText());
                    editor.commit();
                    return false;
                }
            });
            SaturdaySleep.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("sleepTimeSaturday",SaturdaySleep.getText());
                    editor.commit();
                    return false;
                }
            });


            final EditTextPreference SundayWake= (EditTextPreference) findPreference("wakeTimeSunday");
            final EditTextPreference MondayWake= (EditTextPreference) findPreference("wakeTimeMonday");
            final EditTextPreference TuesdayWake= (EditTextPreference) findPreference("wakeTimeTuesday");
            final EditTextPreference WednesdayWake= (EditTextPreference) findPreference("wakeTimeWednesday");
            final EditTextPreference ThursdayWake= (EditTextPreference) findPreference("wakeTimeThursday");
            final EditTextPreference FridayWake= (EditTextPreference) findPreference("wakeTimeFriday");
            final EditTextPreference SaturdayWake= (EditTextPreference) findPreference("wakeTimeSaturday");


            editor.putString("wakeTimeSunday",SundayWake.getText());
            editor.putString("wakeTimeMonday",MondayWake.getText());
            editor.putString("wakeTimeTuesday",TuesdayWake.getText());
            editor.putString("wakeTimeWednesday",WednesdayWake.getText());
            editor.putString("wakeTimeThursday",ThursdayWake.getText());
            editor.putString("wakeTimeFriday",FridayWake.getText());
            editor.putString("wakeTimeSaturday",SaturdayWake.getText());






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




    public static void addFree(int day, Free toAdd){
        enteredItems.get(day).add(toAdd);
    }

    @Override
    public void onHeaderClick(Header header, int position) {
        super.onHeaderClick(header, position);
        if (header.id == R.id.open_home) {






            SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);

            String SundaySleep=preferences.getString("sleepTimeSunday", "");
            String MondaySleep=preferences.getString("sleepTimeMonday", "");
            String TuesdaySleep=preferences.getString("sleepTimeTuesday", "");
            String WednesdaySleep=preferences.getString("sleepTimeWednesday", "");
            String ThursdaySleep=preferences.getString("sleepTimeThursday", "");
            String FridaySleep=preferences.getString("sleepTimeFriday", "");
            String SaturdaySleep=preferences.getString("sleepTimeSaturday", "");

            String SundayWake=preferences.getString("wakeTimeSunday","");
            String MondayWake=preferences.getString("wakeTimeMonday","");
            String TuesdayWake=preferences.getString("wakeTimeTuesday","");
            String WednesdayWake=preferences.getString("wakeTimeWednesday","");
            String ThursdayWake=preferences.getString("wakeTimeThursday","");
            String FridayWake=preferences.getString("wakeTimeFriday","");
            String SaturdayWake=preferences.getString("wakeTimeSaturday","");




            //TODO- check to ensure user inputted the data

            Intent i = new Intent(SettingsActivity.this, bottomNav.class);

            Day Sunday= new Day(enteredItems.get(0), "Sunday",stringTOTime(SundayWake), stringTOTime(SundaySleep));
            Day Monday = new Day(enteredItems.get(1), "Monday",stringTOTime(MondayWake), stringTOTime(MondaySleep));
            Day Tuesday= new Day(enteredItems.get(2), "Tuesday",stringTOTime(TuesdayWake), stringTOTime(TuesdaySleep));
            Day Wednesday= new Day(enteredItems.get(3), "Wednesday",stringTOTime(WednesdayWake), stringTOTime(WednesdaySleep));
            Day Thursday= new Day(enteredItems.get(4), "Thursday",stringTOTime(ThursdayWake), stringTOTime(ThursdaySleep));
            Day Friday= new Day(enteredItems.get(5), "Friday",stringTOTime(FridayWake), stringTOTime(FridaySleep));
            Day Saturday= new Day(enteredItems.get(6), "Saturday",stringTOTime(SaturdayWake), stringTOTime(SaturdaySleep));




            i.putExtra("Sunday", Parcels.wrap(Sunday));
            i.putExtra("Monday", Parcels.wrap(Monday));
            i.putExtra("Tuesday", Parcels.wrap(Tuesday));
            i.putExtra("Wednesday", Parcels.wrap(Wednesday));
            i.putExtra("Thursday", Parcels.wrap(Thursday));
            i.putExtra("Friday", Parcels.wrap(Friday));
            i.putExtra("Saturday", Parcels.wrap(Saturday));



            startActivity(i);
        }
    }


    public Time stringTOTime(String time){
        String[] split=time.split(":");
        return new Time(Integer.valueOf(split[0]),Integer.valueOf(split[1]),0);
    }


}