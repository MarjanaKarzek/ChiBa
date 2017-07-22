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
 * <h1>WeatherRemoteFetch Class</h1>
 * This class fetches the weather data.
 * <p>
 * In the comments find log entries to be used for debugging purposes.
 *
 * @author  Marjana Karzek
 * @version 1.0
 * @since   2017-07-06
 *
 * <p>
 * Based on
 * Author: Ashraff Hathibelagal
 * Date: 2014-09-01
 * Title: Create a Weather App on Android
 * Version: 1
 * Availability: https://code.tutsplus.com/tutorials/create-a-weather-app-on-android--cms-21587
 */
public class WeatherRemoteFetch {

    private static final String TAG = "WeatherRemoteFetch";
    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

    /**
     * This method retrieves the weather data from the server.
     *
     * @param context This parameter is used to get the context form its parent.
     * @param city This parameter is used to get the city for which the weather data must be called.
     * @return The method returns the weather data or null.
     */
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

            if (data.getInt("cod") != 200) {
                return null;
            }
            //Log.d(TAG, "getJSON: data returned");

            return data;
        } catch (Exception e) {
            //Log.d(TAG, "getJSON: no data found");
            return null;
        }
    }
}
