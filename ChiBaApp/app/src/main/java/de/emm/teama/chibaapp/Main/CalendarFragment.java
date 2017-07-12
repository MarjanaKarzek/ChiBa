package de.emm.teama.chibaapp.Main;

import android.support.v4.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.emm.teama.chibaapp.R;
import de.emm.teama.chibaapp.Utils.AppointmentDetailDialogFragment;
import de.emm.teama.chibaapp.Utils.DisplayEventListAdapter;

import static de.emm.teama.chibaapp.Application.ChiBaApplication.database;

/**
 * Created by Marjana Karzek on 18.06.2017.
 */

public class CalendarFragment extends Fragment {
    private static final String TAG = "CalendarFragment";

    private ArrayList<String> currentEvents = new ArrayList<String>();
    private ArrayList<Integer> displayedEvents = new ArrayList<Integer>();
    private DisplayEventListAdapter adapter;

    private String dateFormat = "d. MMMM yyyy";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.GERMANY);
    private Calendar calendar = Calendar.getInstance();

    private View view;
    private final ViewHolderItem viewHolder = new ViewHolderItem();

    static class ViewHolderItem {
        ListView eventlist;
        TextView selectedDate;
        CalendarView calendarView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_calendar, container, false);
            viewHolder.eventlist = (ListView) view.findViewById(R.id.eventlist);
            viewHolder.eventlist.setEmptyView(view.findViewById(R.id.textViewEventsEmpty));
            viewHolder.selectedDate = (TextView) view.findViewById(R.id.selectedDate);
            viewHolder.calendarView = (CalendarView) view.findViewById(R.id.calendarView);
            viewHolder.selectedDate.setText(simpleDateFormat.format(viewHolder.calendarView.getDate()));
            viewHolder.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    viewHolder.selectedDate.setText(simpleDateFormat.format(calendar.getTime()));
                    updateEventList();
                }
            });
            viewHolder.eventlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

            displayData();
            adapter = new DisplayEventListAdapter(inflater.getContext(), R.layout.layout_list_events_adapter_view, currentEvents);
            viewHolder.eventlist.setAdapter(adapter);
        }
        return view;
    }

    private void updateEventList() {
        displayedEvents.clear();
        currentEvents.clear();
        displayData();
        synchronized (adapter) {
            adapter.notifyDataSetChanged();
        }

    }

    private void displayData() {
        Cursor data = database.showEventsByStartDateWithoutFullDay(viewHolder.selectedDate.getText().toString());
        displayedEvents.clear();
        currentEvents.clear();

        if (data.getCount() != 0) {
            String event = "";
            while (data.moveToNext()) {
                event += data.getString(5) + " ";
                event += data.getString(1);
                displayedEvents.add(Integer.valueOf(data.getString(0)));
                currentEvents.add(event);
                event = "";
            }
        }

        Cursor dataFullday = database.showEventIdsByStartDateThatAreFullDay(viewHolder.selectedDate.getText().toString());

        if (dataFullday.getCount() != 0) {
            String event;
            while (dataFullday.moveToNext()) {
                Cursor currentFulldayEvent = database.showEventByEventId(Integer.valueOf(dataFullday.getString(0)));
                if (currentFulldayEvent.getCount() != 0 && currentFulldayEvent.moveToNext()) {
                    event = currentFulldayEvent.getString(1);
                    displayedEvents.add(Integer.valueOf(currentFulldayEvent.getString(0)));
                    currentEvents.add(event);
                }
            }
        }

    }
}