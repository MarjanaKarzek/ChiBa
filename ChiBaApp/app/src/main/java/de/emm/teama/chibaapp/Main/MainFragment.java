package de.emm.teama.chibaapp.Main;

import android.database.Cursor;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.emm.teama.chibaapp.Model3D.model.Object3DBuilder;
import de.emm.teama.chibaapp.Model3D.model.Object3DData;
import de.emm.teama.chibaapp.Model3D.sceneloader.SceneLoader;
import de.emm.teama.chibaapp.Model3D.view.ModelSurfaceView;
import de.emm.teama.chibaapp.R;
import de.emm.teama.chibaapp.Utils.AppointmentDetailDialogFragment;
import de.emm.teama.chibaapp.Utils.CityPreference;
import de.emm.teama.chibaapp.Utils.DisplayEventListAdapter;
import de.emm.teama.chibaapp.Utils.WeatherRemoteFetch;

import static de.emm.teama.chibaapp.Application.ChiBaApplication.database;


public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";

    /* The file to load. Passed as input parameter */
    private String paramFilename;
    private String paramAssetDir;
    private String paramAssetFilename;

    /* Enter into Android Immersive mode so the renderer is full screen or not */
    private boolean immersiveMode = true;

    /* Background GL clear color. Default is white */
    private float[] backgroundColor = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

    private GLSurfaceView gLView;

    private SceneLoader scene;

    private View fragmentHome;
    private String dateFormat = "d. MMMM yyyy";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.GERMANY);
    private String selectedDate;
    private Calendar calendar = Calendar.getInstance();
    private ArrayList<String> currentEvents = new ArrayList<String>();
    private ArrayList<Integer> displayedEvents = new ArrayList<Integer>();
    private ListView eventlist;
    private DisplayEventListAdapter adapter;

    private Typeface weatherFont;
    private TextView weathericon;
    private TextView textCity;
    private TextView textTemperature;

    private String lastKnownCity;
    private String lastKnownTemperature;
    private int lastKnownWeatherID;
    private long lastKnownSunrise;
    private long lastKnownSunset;
    private long lastCallTimeStamp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cursor data = database.getSystemInformation();
        if (data.getCount() != 0 && data.moveToNext()) {
            lastKnownCity = data.getString(1);
            lastKnownTemperature = data.getString(2);
            lastKnownWeatherID = Integer.valueOf(data.getString(3));
            lastKnownSunrise = Long.valueOf(data.getString(4));
            lastKnownSunset = Long.valueOf(data.getString(5));
            lastCallTimeStamp = Long.valueOf(data.getString(6));
        }

        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
        weathericon = new TextView(this.getContext());
        textCity = new TextView(this.getContext());
        textTemperature = new TextView(this.getContext());
        calendar.add(Calendar.MINUTE, 15);
        calendar = Calendar.getInstance();
        Log.d(TAG, "onCreate: current timestamp " + calendar.getTimeInMillis());
        Log.d(TAG, "onCreate: compare to timestamp " + lastCallTimeStamp);
        if (lastCallTimeStamp == 0 || calendar.getTimeInMillis() > lastCallTimeStamp) {
            Log.d(TAG, "onCreate: timestamp ok");
            updateWeatherData(new CityPreference(getActivity()).getCity());
            database.setSystemInfoTimeStamp(calendar.getTimeInMillis() + 900000);
        } else {
            Log.d(TAG, "onCreate: timestamp not ok");
            textCity.setText(lastKnownCity);
            textTemperature.setText(lastKnownTemperature);
            setWeatherIcon(lastKnownWeatherID, lastKnownSunrise, lastKnownSunset);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (fragmentHome == null) {
            LinearLayout linearLayoutRoot = new LinearLayout(this.getContext());
            linearLayoutRoot.setOrientation(LinearLayout.VERTICAL);
            //linearLayoutRoot.setWeightSum(2f);
            LinearLayout.LayoutParams rlpRoot = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            linearLayoutRoot.setLayoutParams(rlpRoot);

            RelativeLayout relativeLayoutAvatarArea = new RelativeLayout(this.getContext());
            RelativeLayout.LayoutParams rlpAvatarArea = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    1200);
            relativeLayoutAvatarArea.setLayoutParams(rlpAvatarArea);

            //Create Views for Realative Layout
            gLView = new ModelSurfaceView(this);
            // Create our 3D scenario
            scene = new SceneLoader(this);
            try {
                Object3DData chiba = Object3DBuilder.loadObj(this.getActivity().getAssets(), "models", "chiba.obj");
                chiba.centerAndScale(2.0f);
                chiba.setPosition(new float[]{-2.0f, -0.5f, 0f});
                chiba.setColor(new float[]{1.0f, 1.0f, 1f, 1.0f});
                scene.addObject(chiba);

                Object3DData ball = Object3DBuilder.loadObj(this.getActivity().getAssets(), "models", "BallAnimiert.obj");
                ball.centerAndScale(1.0f);
                ball.setPosition(new float[]{1.0f, 0.0f, 0f});
                scene.addObject(ball);

            } catch (Exception ex) {
            }

            gLView.setId(new Integer(100000));
            LinearLayout.LayoutParams rlpGLView = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    1200);
            gLView.setLayoutParams(rlpGLView);

            //Clock
            TextClock textClock = new TextClock(this.getContext());
            RelativeLayout.LayoutParams rlpTextClock = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlpTextClock.topMargin = 20;
            rlpTextClock.addRule(RelativeLayout.CENTER_HORIZONTAL);
            textClock.setId(new Integer(1000001));
            textClock.setLayoutParams(rlpTextClock);
            textClock.setTextSize(70);
            textClock.setTextColor(getContext().getColor(R.color.colorBlackish));

            //Date
            TextView textDate = new TextView(this.getContext());
            RelativeLayout.LayoutParams rlpTextDate = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlpTextDate.topMargin = 10;
            rlpTextDate.addRule(RelativeLayout.CENTER_HORIZONTAL);
            rlpTextDate.addRule(RelativeLayout.BELOW, 1000001);
            textDate.setLayoutParams(rlpTextDate);
            textDate.setTextSize(16);
            textDate.setTextColor(getContext().getColor(R.color.colorBlackish));
            textDate.setText(simpleDateFormat.format(calendar.getTime()));

            //Weather icon
            RelativeLayout.LayoutParams rlpWeathericon = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlpWeathericon.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            rlpWeathericon.topMargin = 50;
            rlpWeathericon.leftMargin = 50;
            weathericon.setLayoutParams(rlpWeathericon);
            weathericon.setTextSize(80);
            weathericon.setTypeface(weatherFont);
            weathericon.setTextColor(getContext().getColor(R.color.colorAccent));

            //City Label
            RelativeLayout.LayoutParams rlpTextCity = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlpTextCity.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            textCity.setLayoutParams(rlpTextCity);
            rlpTextCity.topMargin = 150;
            rlpTextCity.rightMargin = 50;

            //Temperature
            RelativeLayout.LayoutParams rlpTextTemperature = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlpTextTemperature.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            textTemperature.setLayoutParams(rlpTextTemperature);
            rlpTextTemperature.topMargin = 215;
            rlpTextTemperature.rightMargin = 50;
            textTemperature.setTextColor(getContext().getColor(R.color.colorBlackish));


            //List Area
            View eventListLayout = inflater.inflate(R.layout.fragment_home, container, false);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            p.addRule(RelativeLayout.BELOW, 1234567);
            eventListLayout.setLayoutParams(p);
            //Setup ListView
            eventlist = (ListView) eventListLayout.findViewById(R.id.eventlistHome);
            eventlist.setEmptyView(eventListLayout.findViewById(R.id.textViewEventsHomeEmpty));
            getCurrentEventData();
            adapter = new DisplayEventListAdapter(inflater.getContext(), R.layout.layout_list_events_adapter_view, currentEvents);
            eventlist.setAdapter(adapter);
            eventlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("appointment_detail_fragment");
                    if (prev != null) {
                        transaction.remove(prev);
                    }
                    transaction.addToBackStack(null);

                    // Create and show the dialog.
                    DialogFragment newDialog = AppointmentDetailDialogFragment.newInstance(displayedEvents.get(position));
                    newDialog.show(transaction, "appointment_detail_fragment");
                }
            });

            //Add Views to Layouts
            relativeLayoutAvatarArea.addView(gLView);
            relativeLayoutAvatarArea.addView(textClock);
            relativeLayoutAvatarArea.addView(textDate);
            relativeLayoutAvatarArea.removeView(weathericon);
            relativeLayoutAvatarArea.addView(weathericon);
            relativeLayoutAvatarArea.removeView(textCity);
            relativeLayoutAvatarArea.addView(textCity);
            relativeLayoutAvatarArea.removeView(textTemperature);
            relativeLayoutAvatarArea.addView(textTemperature);

            linearLayoutRoot.addView(relativeLayoutAvatarArea);
            linearLayoutRoot.addView(eventListLayout);
            fragmentHome = linearLayoutRoot;
        }
        return fragmentHome;
    }

    private void getCurrentEventData() {
        //set currentDate
        selectedDate = simpleDateFormat.format(calendar.getTime());
        Cursor data = database.showEventsByStartDateWithoutFullDay(selectedDate);
        currentEvents.clear();
        displayedEvents.clear();

        if (data.getCount() != 0) {
            String event = "";
            while (data.moveToNext()) {
                event += data.getString(5) + " ";
                event += data.getString(1);
                currentEvents.add(event);
                displayedEvents.add(Integer.valueOf(data.getString(0)));
                event = "";
            }
        }

        Cursor dataFullday = database.showEventIdsByStartDateThatAreFullDay(selectedDate);

        if (dataFullday.getCount() != 0) {
            String event;
            while (dataFullday.moveToNext()) {
                Cursor currentFulldayEvent = database.showEventByEventId(Integer.valueOf(dataFullday.getString(0)));
                if (currentFulldayEvent.getCount() != 0 && currentFulldayEvent.moveToNext()) {
                    event = currentFulldayEvent.getString(1);
                    currentEvents.add(event);
                }
            }
        }
    }

    public float[] getBackgroundColor() {
        return backgroundColor;
    }

    public SceneLoader getScene() {
        return scene;
    }

    public GLSurfaceView getgLView() {
        return gLView;
    }

    private void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final JSONObject json = WeatherRemoteFetch.getJSON(getActivity(), city);
                if (json != null) {
                    renderWeather(json);
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json) {
        try {
            textCity.setText(json.getString("name").toUpperCase(Locale.GERMAN) +
                    ", " +
                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");

            textTemperature.setText(
                    String.format("%.2f", main.getDouble("temp")) + " ℃");

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

            database.setSystemInfoData(textCity.getText().toString(),textTemperature.getText().toString());
        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
            textCity.setText(lastKnownCity);
            textTemperature.setText(lastKnownTemperature);
            setWeatherIcon(lastKnownWeatherID, lastKnownSunrise, lastKnownSunset);
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";
        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch (id) {
                case 2:
                    icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7:
                    icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = getActivity().getString(R.string.weather_cloudy);
                    break;
                case 6:
                    icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 5:
                    icon = getActivity().getString(R.string.weather_rainy);
                    break;
            }
        }
        database.setSystemInfoWeatherData(actualId, sunrise, sunset);
        weathericon.setText(icon);
    }
}
