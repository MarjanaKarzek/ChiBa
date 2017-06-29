package de.emm.teama.chibaapp.Utils;

import android.support.v4.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.w3c.dom.Text;

import de.emm.teama.chibaapp.R;

import static de.emm.teama.chibaapp.Main.MainActivity.database;

/**
 * Created by Marjana Karzek on 28.06.2017.
 */

public class AppointmentDetailDialogFragment extends DialogFragment {
    private int eventId;
    private String eventTitle;
    private String eventFullDay;
    private String eventStartDate;
    private String eventEndDate;
    private String eventStartTime;
    private String eventEndTime;
    private String eventLocation;


    public AppointmentDetailDialogFragment() {
    }

    public static AppointmentDetailDialogFragment newInstance(int eventId) {
        AppointmentDetailDialogFragment dialog = new AppointmentDetailDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("eventId", eventId);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventId = getArguments().getInt("eventId");
        Cursor data = database.showEventByEventId(eventId);

        if (data.getCount() != 0) {
            while (data.moveToNext()) {
                eventTitle = data.getString(1);
                eventFullDay = data.getString(2);
                eventStartDate = data.getString(3);
                eventEndDate = data.getString(4);
                eventStartTime = data.getString(5);
                eventEndTime = data.getString(6);
                eventLocation= data.getString(7);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_appointment_detail_fragment, container);
        TextView modalTitle = (TextView) view.findViewById(R.id.appointmentDetailModalTitle);
        //modalTitle.setText(eventTitle);
        modalTitle.setText(eventFullDay);
        TextView eventLocationView = (TextView) view.findViewById(R.id.appointmentDetailEventLocation);
        eventLocationView.setText(eventLocation);
        TextView eventStartDateView = (TextView) view.findViewById(R.id.appointmentDetailTextViewStartDate);
        eventStartDateView.setText(eventStartDate);
        ViewFlipper viewFlipperFullDay = (ViewFlipper) view.findViewById( R.id.appointmentDetailViewFlipperFullDay);
        if(eventFullDay == "1"){
            viewFlipperFullDay.showNext();
            TextView eventEndDateView = (TextView) view.findViewById(R.id.appointmentDetailTextViewEndDate);
            eventEndDateView.setText(eventEndDate);
        }
        else{
            TextView eventStartTimeView = (TextView) view.findViewById(R.id.appointmentDetailTextViewStartTime);
            eventStartTimeView.setText(eventStartTime);
            TextView eventEndTimeView = (TextView) view.findViewById(R.id.appointmentDetailTextViewEndTime);
            eventEndTimeView.setText(eventEndTime);
        }
        return view;
    }
}
