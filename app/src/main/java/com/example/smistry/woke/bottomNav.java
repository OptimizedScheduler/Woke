package com.example.smistry.woke;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.smistry.woke.fragments.ViewPagerFragment;
import com.example.smistry.woke.fragments.goals;
import com.example.smistry.woke.fragments.stats;
import com.example.smistry.woke.models.Day;
import com.example.smistry.woke.models.Free;
import com.example.smistry.woke.models.MessageEvent;
import com.example.smistry.woke.models.Task;
import com.example.smistry.woke.models.Weather;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class bottomNav extends AppCompatActivity {

    ArrayList<Task> tasks;
    ArrayList<Free> freeBlocks;
    ArrayList<Day> days;
    HashMap<String, ArrayList<Free>> settings;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private NotificationManagerCompat notificationManager;
    public final static String API_BASE_URL = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/337153";
    public final static String API_KEY_PARAM = "apikey";
    public final static String TAG = "TestActivity";
    public boolean bringJacket = false;
    int jacketTemp;

    AsyncHttpClient client;

    // define your fragments here
    final Fragment fragment2= new goals();
    final Fragment fragment3= new stats();

    ViewPagerFragment viewPager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flContainer, ViewPagerFragment.newInstance(days)).commit();
                    return true;
                case R.id.navigation_goals:
                    FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                    fragmentTransaction2.replace(R.id.flContainer, fragment2).commit();
                    return true;
                case R.id.navigation_statistics:
                    FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
                    fragmentTransaction3.replace(R.id.flContainer, fragment3).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String age= prefs.getString("age", "18");
        Boolean weatherTasks=prefs.getBoolean("weatherSwitch", false);
        Boolean rain=prefs.getBoolean("rainSwitch", false);
        Boolean jacket= prefs.getBoolean("jacketSwitch", false);
        jacketTemp=prefs.getInt("seekBar", 50);

        notificationManager = NotificationManagerCompat.from(this);
        client = new AsyncHttpClient();

        getWeather();

        //Shows Setting Activity if this is the FIRST time the app is running
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show sign up activity
            startActivityForResult(new Intent(this, SettingsActivity.class), 0);
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

     //   EventBus.getDefault().register(this);

       Intent data = getIntent();
       settings = (HashMap<String, ArrayList<Free>>) data.getSerializableExtra("FreeMap");

        //Fill the Day Array with information
        //TODO to be replaced with the information from the Files
        days=new ArrayList<>();

       viewPager= ViewPagerFragment.newInstance(days);
        Log.d("EventBus", days.toString());

        //begins fragment transaction_ViewPagerFragment is shown as the default view
       FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.flContainer, viewPager).commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

       // if(weatherTasks && jacket && bringJacket) {sendOnChannel1();};

        ArrayList<Task> tasks2;
        ArrayList<Free> freeBlocks2;
        ArrayList<Task> tasks3;
        ArrayList<Free> freeBlocks3;
        ArrayList<Task> tasks4;
        ArrayList<Free> freeBlocks4;
        ArrayList<Task> tasks5;
        ArrayList<Free> freeBlocks5;
        ArrayList<Task> tasks6;
        ArrayList<Free> freeBlocks6;
    }


    @Subscribe (sticky = true,threadMode = ThreadMode.BACKGROUND)
    public void onEvent(MessageEvent event){
        days= event.getmDaysList();
        viewPager.setDaysA(days);
        newTask nT = EventBus.getDefault().getStickyEvent(newTask.class);
        Log.d("EventBus", days.toString());

/*        if(event != null) {
            EventBus.getDefault().removeStickyEvent(nT);
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_toolbar, menu);
        return true;
    }

    public void openSettings(MenuItem item)
 {
     Intent intent = new Intent(bottomNav.this,SettingsActivity.class);
     startActivityForResult(intent,2);
 }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void sendOnChannel1(MenuItem menuItem)
    {
        Notification notification = new NotificationCompat.Builder(this, WokeApp.Channel_1_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Bring Jacket")
                .setContentText("Bring Jacket")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        notificationManager.notify(1,notification);

    }

    public void getWeather(){
        String url = API_BASE_URL;

        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.apikey)); //API key, always required
        //execute a GET Request expecting a JSON object response
        client.get(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONArray results = response.getJSONArray("DailyForecasts");
                    Weather weather = new Weather(results.getJSONObject(0));
                    if(Integer.parseInt(weather.getMinTemp()) <= jacketTemp) bringJacket = true;
                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now playing endpoint", throwable, true );
            }
        });
    }

    private void logError(String message, Throwable error, boolean alertUser){
        //always log the error
        Log.e(TAG, message,error);
        //alert the user to avoid silent errors
        if(alertUser){
            //show a toast with the error message
            Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
        }
    }

    public ArrayList<Day> getDays() {
        return days;
    }
}


