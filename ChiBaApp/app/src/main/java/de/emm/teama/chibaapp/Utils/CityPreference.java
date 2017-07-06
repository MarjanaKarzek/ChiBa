package de.emm.teama.chibaapp.Utils;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by marja on 06.07.2017.
 */

public class CityPreference {

    SharedPreferences prefs;

    public CityPreference(Activity activity) {
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity() {
        return prefs.getString("city", "Berlin, DE");
    }

    public void setCity(String city) {
        prefs.edit().putString("city", city).commit();
    }
}
