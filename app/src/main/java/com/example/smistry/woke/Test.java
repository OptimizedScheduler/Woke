package com.example.smistry.woke;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.smistry.woke.models.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;

public class Test extends AppCompatActivity {

    @BindView(R.id.minTemp) TextView minTemp;
    @BindView(R.id.maxTemp) TextView maxTemp;
    private static final String TAG = Test.class.getSimpleName();
    private ArrayList<Weather> weatherArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        URL weatherUrl = NetworkUtils.buildUrlForWeather();
        new FetchWeatherDetails().execute(weatherUrl);
        Log.i(TAG, "onCreate: weatherUrl: " + weatherUrl);

    }


    private class FetchWeatherDetails extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL weatherUrl = urls[0];
            String weatherSearchResults = null;

            try {
                weatherSearchResults = NetworkUtils.getResponseFromHttpUrl(weatherUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "doInBackground: weatherSearchResults: " + weatherSearchResults);
            return weatherSearchResults;
        }

        @Override
        protected void onPostExecute(String weatherSearchResults) {
            if(weatherSearchResults != null && !weatherSearchResults.equals("")) {
                weatherArrayList = parseJSON(weatherSearchResults);
                //Just for testing
                Iterator itr = weatherArrayList.iterator();
                while(itr.hasNext()) {
                    Weather weatherInIterator = (Weather) itr.next();
                    Log.i(TAG, "onPostExecute: Date: " + weatherInIterator.getDate()+
                            " Min: " + weatherInIterator.getMinTemp() +
                            " Max: " + weatherInIterator.getMaxTemp());
                    Log.d("Yo", weatherInIterator.getMaxTemp().toString());
                }
            }
            super.onPostExecute(weatherSearchResults);
        }
    }

    private ArrayList<Weather> parseJSON(String weatherSearchResults) {

        if(weatherSearchResults != null) {
            try {
                JSONObject rootObject = new JSONObject(weatherSearchResults);
                JSONArray results = rootObject.getJSONArray("DailyForecasts");

                for (int i = 0; i < 1; i++) {
                    Weather weather = new Weather();

                    JSONObject resultsObj = results.getJSONObject(i);

                    String date = resultsObj.getString("Date");
                    weather.setDate(date);

                    JSONObject temperatureObj = resultsObj.getJSONObject("Temperature");
                    Double minTemperature = temperatureObj.getJSONObject("Minimum").getDouble("Value");
                    weather.setMinTemp(minTemperature);
                    Log.d("PRINT", String.valueOf(minTemperature));
                    minTemp.setText(String.valueOf(minTemperature));

                    Double maxTemperature = temperatureObj.getJSONObject("Maximum").getDouble("Value");
                    weather.setMaxTemp(maxTemperature);
                    Log.d("PRINT", String.valueOf(maxTemperature));
                    maxTemp.setText(String.valueOf(maxTemperature));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}