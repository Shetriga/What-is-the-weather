package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.euicc.DownloadableSubscription;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cityName;

    TextView weatherState;

    public class WhatIsTheWeather extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection connection;
            String result = "";

            try {

                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = connection.getInputStream();

                InputStreamReader reader = new InputStreamReader(inputStream);

                int data = reader.read();

                while (data != -1){

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;

            } catch (Exception e) {

                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //Log.i("Result", result);

            try {

                JSONObject jsonObject = new JSONObject(result);

                String weather = jsonObject.getString("weather");

                //Log.i("weather", weather);

                JSONArray array = new JSONArray(weather);

                for (int i = 0;i < array.length(); i++){

                    JSONObject partJSONObject = array.getJSONObject(i);

                    String main = partJSONObject.getString("main");
                    String description = partJSONObject.getString("description");

                    weatherState.setText(main + ": " + description);


                }

            } catch (JSONException e) {

                e.printStackTrace();

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);

        weatherState = findViewById(R.id.weatherState);
    }

    public void checkWeather(View view){

        Toast.makeText(getApplicationContext(), "Button clicked!", Toast.LENGTH_SHORT).show();

        String cityNameString = cityName.getText().toString();

        if (cityNameString.isEmpty()){

            cityName.setError("Please enter a city name");
            cityName.requestFocus();
            return;
        } else {

            WhatIsTheWeather task = new WhatIsTheWeather();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + cityNameString + "&appid=e9411cd4a34b9bf5dc96d1f813d74f63");

        }
    }

}