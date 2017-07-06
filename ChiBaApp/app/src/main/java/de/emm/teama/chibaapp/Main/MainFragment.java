package de.emm.teama.chibaapp.Main;

import android.database.Cursor;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.emm.teama.chibaapp.Model3D.model.Object3DBuilder;
import de.emm.teama.chibaapp.Model3D.model.Object3DData;
import de.emm.teama.chibaapp.Model3D.sceneloader.SceneLoader;
import de.emm.teama.chibaapp.Model3D.util.Utils;
import de.emm.teama.chibaapp.Model3D.view.ModelSurfaceView;
import de.emm.teama.chibaapp.R;
import de.emm.teama.chibaapp.Utils.DisplayEventListAdapter;

import static de.emm.teama.chibaapp.Application.ChiBaApplication.database;


public class MainFragment extends Fragment
{
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
    private ListView eventlist;
    private DisplayEventListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
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
            Object3DData android = Object3DBuilder.loadObj(this.getActivity().getAssets(), "models", "chiba.obj");
            android.setPosition(new float[] { 0f, 0f, 0f });
            android.setColor(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
            scene.addObject(android);
        } catch (Exception ex) {}
        // TODO: Alert user when there is no multitouch support (2 fingers). He won't be able to rotate or zoom for
        Utils.printTouchCapabilities(this.getActivity().getPackageManager());

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



        //Add Views to Layouts
        relativeLayoutAvatarArea.addView(gLView);
        relativeLayoutAvatarArea.addView(textClock);
        relativeLayoutAvatarArea.addView(textDate);
        linearLayoutRoot.addView(relativeLayoutAvatarArea);
        linearLayoutRoot.addView(eventListLayout);
        fragmentHome = linearLayoutRoot;
        return fragmentHome;
    }

    private void getCurrentEventData() {
        //set currentDate
        selectedDate = simpleDateFormat.format(calendar.getTime());
        Cursor data = database.showEventsByStartDateWithoutFullDay(selectedDate);
        currentEvents.clear();

        if (data.getCount() != 0) {
            String event = "";
            while (data.moveToNext()) {
                event += data.getString(5) + " ";
                event += data.getString(1);
                currentEvents.add(event);
                event = "";
            }
        }

        Cursor dataFullday = database.showEventIdsByStartDateThatAreFullDay(selectedDate);

        if (dataFullday.getCount() != 0) {
            String event;
            while (dataFullday.moveToNext()) {
                Cursor currentFulldayEvent = database.showEventByEventId(Integer.valueOf(dataFullday.getString(0)));
                if(currentFulldayEvent.getCount() != 0 && currentFulldayEvent.moveToNext()){
                    event = currentFulldayEvent.getString(1);
                    currentEvents.add(event);
                }
            }
        }
    }


    public File getParamFile() {
        return getParamFilename() != null ? new File(getParamFilename()) : null;
    }

    public String getParamAssetDir() {
        return paramAssetDir;
    }

    public String getParamAssetFilename() {
        return paramAssetFilename;
    }

    public String getParamFilename() {
        return paramFilename;
    }

    public float[] getBackgroundColor(){
        return backgroundColor;
    }

    public SceneLoader getScene() {
        return scene;
    }

    public GLSurfaceView getgLView() {
        return gLView;
    }
}
