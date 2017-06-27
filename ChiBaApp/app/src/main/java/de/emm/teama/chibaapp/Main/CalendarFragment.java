package de.emm.teama.chibaapp.Main;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
    private TextView selectedDate;
    private String dateFormat = "d. MMMM yyyy";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.GERMANY);
    private CalendarView calendarView;
    private Calendar calendar = Calendar.getInstance();

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
        selectedDate = (TextView) view.findViewById(R.id.selectedDate);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        selectedDate.setText(simpleDateFormat.format(calendarView.getDate()));
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                selectedDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        });


        return view;
    }

    private void displayData() {
        Cursor data = database.showEvent();

        if (data.getCount() != 0) {
            String event = "";
            while (data.moveToNext()) {
                event += data.getString(5) + " ";
                event += data.getString(1);
                currentEvents.add(event);
                event = "";
            }
        }
    }
}