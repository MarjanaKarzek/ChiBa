package de.emm.teama.chibaapp.Main;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import de.emm.teama.chibaapp.R;
import de.emm.teama.chibaapp.Utils.AddHashtagListAdapter;
import de.emm.teama.chibaapp.Utils.DisplayEventListAdapter;

import static de.emm.teama.chibaapp.Main.MainActivity.database;

/**
 * Created by Marjana Karzek on 18.06.2017.
 */

public class CalendarFragment extends Fragment {
    private static final String TAG = "CalendarFragment";
    private ArrayList<String> currentEvents = new ArrayList<String>();
    private DisplayEventListAdapter adapter;


    private ListView eventlist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        eventlist = (ListView) view.findViewById(R.id.eventlist);
        eventlist.setEmptyView(view.findViewById(R.id.textViewEventsEmpty));
        displayData();
        adapter = new DisplayEventListAdapter(inflater.getContext(), R.layout.layout_list_events_adapter_view, currentEvents);
        eventlist.setAdapter(adapter);


        return view;
    }

    private void displayData() {
        Cursor data = database.showEvent();

        if(data.getCount() != 0){
            String event = "";
            while(data.moveToNext()){
                event += data.getString(5) + " ";
                event += data.getString(1);

                /*event += "ID: " + data.getString(0) + "\n";
                event += "TITLE: " + data.getString(1) + "\n";
                event += "FULLDAY: " + data.getString(2) + "\n";
                event += "STARTDATE: " + data.getString(3) + "\n";
                event += "ENDDATE: " + data.getString(4) + "\n";
                event += "STARTTIME: " + data.getString(5) + "\n";
                event += "ENDTIME: " + data.getString(6) + "\n";
                event += "LOCATION: " + data.getString(7) + "\n";*/

                currentEvents.add(event);
                event = "";
            }
        }
    }
}