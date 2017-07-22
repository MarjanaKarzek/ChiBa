package de.emm.teama.chibaapp.Utils;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;

import de.emm.teama.chibaapp.Appointment.EditAppointmentActivity;
import de.emm.teama.chibaapp.R;

import static de.emm.teama.chibaapp.Application.ChiBaApplication.database;

/**
 * <h1>AppointmentDetailDialogFragment Class</h1>
 * This class sets up a DialogFragment to display the details of an appointment.
 * <p>
 * In the comments find log entries to be used for debugging purposes.
 *
 * @author  Marjana Karzek
 * @version 2.0
 * @since   2017-08-28
 */
public class AppointmentDetailDialogFragment extends DialogFragment {
    private static final String TAG = "AppointmentDetailDialog";
    private int eventId;
    private String eventTitle;
    private String eventFullDay;
    private String eventStartDate;
    private String eventEndDate;
    private String eventStartTime;
    private String eventEndTime;
    private String eventLocation;
    private ArrayList<String> eventHashtags = new ArrayList<String>();

    /**
     * This method creates a new dialog.
     *
     * @param eventId This parameter is used to figure out which event was selected.
     * @return This method returns the dialog.
     */
    public static AppointmentDetailDialogFragment newInstance(int eventId) {
        AppointmentDetailDialogFragment dialog = new AppointmentDetailDialogFragment();

        Bundle args = new Bundle();
        args.putInt("eventId", eventId);
        dialog.setArguments(args);

        return dialog;
    }

    /**
     * On creation this method retrieves the information about the selected event from the database.
     *
     * @param savedInstanceState This parameter is used to get the last saved state.
     */
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
        data.close();

        eventHashtags = database.showHashtagsByEventId(eventId);
    }

    /**
     * This method sets up the view and the actions on the navigation.
     *
     * @param inflater This parameter is used to get the inflater.
     * @param container This parameter is used to get the container.
     * @param savedInstanceState This parameter is used to get the save state from the dialog.
     * @return The method returns the view after it was modified.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_appointment_detail_fragment, container);

        ImageView cancel = (ImageView) view.findViewById(R.id.appointmentDetailCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "onClick: cancel appointment detail dialog");
                getDialog().dismiss();
            }
        });
        ImageView editOption = (ImageView) view.findViewById(R.id.appointmentDetailEditOption);
        editOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "onClick: edit appointment");
                Intent intent = new Intent(getActivity(), EditAppointmentActivity.class);
                intent.putExtra("EXTRA_EVENT_ID", eventId);
                startActivity(intent);
            }
        });

        TextView modalTitle = (TextView) view.findViewById(R.id.appointmentDetailModalTitle);
        modalTitle.setText(eventTitle);
        TextView eventLocationView = (TextView) view.findViewById(R.id.appointmentDetailEventLocation);
        eventLocationView.setText(eventLocation);
        TextView eventStartDateView = (TextView) view.findViewById(R.id.appointmentDetailTextViewStartDate);
        eventStartDateView.setText(eventStartDate);
        ViewFlipper viewFlipperFullDay = (ViewFlipper) view.findViewById( R.id.appointmentDetailViewFlipperFullDay);
        if(eventFullDay.contains("1") || eventFullDay.contains("true")){
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
        ListView hashtagList = (ListView) view.findViewById(R.id.appointmentDetailListViewAssignedHashtag);
        HashtagListAdapter adapter = new HashtagListAdapter(this.getContext(), R.layout.layout_hashtag_adapter_view, eventHashtags);
        hashtagList.setAdapter(adapter);
        hashtagList.setEmptyView( view.findViewById( R.id.appointmentDetailListViewAssignedHashtagEmpty) );

        return view;
    }
}
