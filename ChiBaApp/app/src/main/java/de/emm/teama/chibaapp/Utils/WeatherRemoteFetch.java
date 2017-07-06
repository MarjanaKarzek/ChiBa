package de.emm.teama.chibaapp.Utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import de.emm.teama.chibaapp.R;

/**
 * Created by Marjana Karzek on 06.07.2017.
 *
 * Tutorial https://code.tutsplus.com/tutorials/create-a-weather-app-on-android--cms-21587
 * Assets from there
 */
public class WeatherRemoteFetch {

    private static final String TAG = "WeatherRemoteFetch";
    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

    public static JSONObject getJSON(Context context, String city) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.addRequestProperty("x-api-key", context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not successful
            if (data.getInt("cod") != 200) {
                return null;
            }
            Log.d(TAG, "getJSON: data returned");

            return data;
        } catch (Exception e) {
            Log.d(TAG, "getJSON: no data found");
            return null;
        }
    }
}
