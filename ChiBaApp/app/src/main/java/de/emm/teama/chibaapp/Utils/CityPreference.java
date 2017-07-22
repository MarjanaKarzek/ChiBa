package de.emm.teama.chibaapp.Utils;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * <h1>CityPreference Class</h1>
 * This class hold the city preferences
 * <p>
 * In the comments find log entries to be used for debugging purposes.
 *
 * @author  Marjana Karzek
 * @version 1.0
 * @since   2017-07-06
 */

public class CityPreference {
    SharedPreferences prefs;

    /**
     * The constructor is used to retrieve the activity from its parent.
     *
     * @param activity The parameter gets the activity.
     */
    public CityPreference(Activity activity) {
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    /**
     * This method returns the preferred city.
     *
     * @return The method return the preferred city as string.
     */
    public String getCity() {
        return prefs.getString("city", "Berlin, DE");
    }
}
