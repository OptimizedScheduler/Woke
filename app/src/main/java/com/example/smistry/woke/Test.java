package com.example.smistry.woke;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smistry.woke.models.Weather;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class Test extends AppCompatActivity {

    @BindView(R.id.minTemp) TextView minTemp;
    @BindView(R.id.maxTemp) TextView maxTemp;
    public final static String API_BASE_URL = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/337153";
    public final static String API_KEY_PARAM = "apikey";
    public final static String TAG = "TestActivity";

    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        client = new AsyncHttpClient();

        getWeather();
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
                    minTemp.setText(weather.getMinTemp());
                    maxTemp.setText(weather.getMaxTemp());
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

}