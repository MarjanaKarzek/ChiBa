package de.emm.teama.chibaapp.Appointment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import de.emm.teama.chibaapp.Application.ChiBaApplication;
import de.emm.teama.chibaapp.Main.MainActivity;
import de.emm.teama.chibaapp.R;
import de.emm.teama.chibaapp.Utils.AddHashtagListAdapter;
import de.emm.teama.chibaapp.Utils.RemoveHashtagListAdapter;

import static de.emm.teama.chibaapp.Application.ChiBaApplication.database;

/**
 * <h1>EditAppointmentActivity Class</h1>
 * This class provides the activity to edit an appointment.
 * <p>
 * In the comments find log entries to be used for debugging purposes.
 *
 * @author  Marjana Karzek
 * @version 4.0
 * @since   2017-06-18
 */

public class EditAppointmentActivity extends AppCompatActivity {
    private static final String TAG = "EditAppointmentActivity";
    private Context context = EditAppointmentActivity.this;
    private int eventId;

    //Current Event Information
    private String currentEventTitle;
    private String currentFullDayState;
    private String currentStartDate;
    private String currentEndDate;
    private String currentStartTime;
    private String currentEndTime;
    private String currentLocation;
    private ArrayList<String> currentAssignedHashtags = new ArrayList<String>();

    //Calendar Fields
    private Calendar calendar = Calendar.getInstance();
    private Calendar calendarStartDate = Calendar.getInstance();
    private Calendar calendarEndDate = Calendar.getInstance();
    private Calendar calendarStartTime = Calendar.getInstance();
    private Calendar calendarEndTime = Calendar.getInstance();
    private TextView startDate;
    private TextView endDate;
    private TextView startTime;
    private TextView endTime;
    private DatePickerDialog.OnDateSetListener startDatePicker;
    private DatePickerDialog.OnDateSetListener endDatePicker;
    private TimePickerDialog.OnTimeSetListener startTimePicker;
    private TimePickerDialog.OnTimeSetListener endTimePicker;
    private String dateFormat = "d. MMMM yyyy";
    private String timeFormat = "kk:mm";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.GERMANY);
    private SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timeFormat, Locale.GERMANY);

    //Switch Fields
    private ViewFlipper viewFlipperFullDay;
    private Switch fulldaySwitch;

    //Hashtag List Fields
    private ArrayList<String> hashtags = new ArrayList<String>();
    private ArrayList<String> assignedHashtags = new ArrayList<String>();
    private ListView hashtagListView;
    private ListView assignedHashtagListView;
    private EditText searchfield;
    private AddHashtagListAdapter adapter;
    private RemoveHashtagListAdapter adapter2;

    //Remaining Form Fields Initialization
    private EditText title;
    private EditText location;

    private Drawable errorIcon;

    /**
     * This method creates the view.
     * @param savedInstanceState This parameter is used to get the saved instance state.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment);
        //Log.d(TAG, "onCreate: started");

        errorIcon = getDrawable(R.drawable.ic_error_message);
        errorIcon.setBounds(0, 0,errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());

        eventId = getIntent().getIntExtra("EXTRA_EVENT_ID", -1);
        //Get Event Information
        Cursor data = database.showEventByEventId(eventId);
        if (data.getCount() != 0) {
            while (data.moveToNext()) {
                currentEventTitle = data.getString(1);
                currentFullDayState = data.getString(2);
                currentStartDate = data.getString(3);
                currentEndDate = data.getString(4);
                currentStartTime = data.getString(5);
                currentEndTime = data.getString(6);
                currentLocation = data.getString(7);
            }
        }
        data.close();
        currentAssignedHashtags = database.showHashtagsByEventId(eventId);

        //Calendar Fields Initialization
        startDate = (TextView) findViewById(R.id.addAppointmentTextViewStartDate);
        startDate.setText(currentStartDate);
        endDate = (TextView) findViewById(R.id.addAppointmentTextViewEndDate);
        endDate.setText(currentEndDate);
        startTime = (TextView) findViewById(R.id.addAppointmentTextViewStartTime);
        startTime.setText(currentStartTime);
        endTime = (TextView) findViewById(R.id.addAppointmentTextViewEndTime);
        endTime.setText(currentEndTime);

        //Switch Fields Initialization
        viewFlipperFullDay = (ViewFlipper) findViewById(R.id.addAppointmentViewFlipperFullDay);
        fulldaySwitch = (Switch) findViewById(R.id.addAppointmentSwitchFullday);
        if (currentFullDayState.contains("1") || currentFullDayState.contains("true")) {
            fulldaySwitch.setChecked(true);
            viewFlipperFullDay.showNext();
        } else
            fulldaySwitch.setChecked(false);

        //Hashtag List Fields Initialization
        hashtagListView = (ListView) findViewById(R.id.addAppointmentListViewHashtag);
        assignedHashtagListView = (ListView) findViewById(R.id.addAppointmentListViewAssignedHashtag);
        searchfield = (EditText) findViewById(R.id.addAppointmentEditTextSearchHashtag);
        assignedHashtags = currentAssignedHashtags;

        //Setup Components
        setupToolbar();
        setupDateTimePickers();
        setupPickersOnClickListener();
        setupFullDaySwitch();
        setupListViews();
        setupSearchField();

        //Remaining Form Fields Initialization
        title = (EditText) findViewById(R.id.addAppointmentEditTextTitle);
        title.setText(currentEventTitle);
        location = (EditText) findViewById(R.id.addAppointmentEditTextLocation);
        location.setText(currentLocation);

        title.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        searchfield.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        location.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        //Delete Button
        Button deleteButton = (Button) findViewById(R.id.editAppointmentDeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(context);
                deleteDialogBuilder.setCancelable(true);
                deleteDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                database.deleteEventByEventId(eventId);
                                ChiBaApplication.deleteApplicationTimer(eventId);
                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);
                            }
                        });
                deleteDialogBuilder.setNegativeButton("Abbrechen",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Do nothing, just close the Dialog
                            }
                        });
                deleteDialogBuilder.setTitle("Termin löschen");
                deleteDialogBuilder.setMessage("Möchten Sie wirklich den Termin " + title.getText() + " löschen?");
                AlertDialog cancleEditDialog = deleteDialogBuilder.create();
                cancleEditDialog.show();
            }
        });
    }

    /**
     * This method sets up the search field.
     */
    private void setupSearchField() {
        searchfield.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (EditAppointmentActivity.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                (EditAppointmentActivity.this).adapter.getFilter().filter(s);
            }
        });
    }

    /**
     * This method sets up the fullday switch
     */
    private void setupFullDaySwitch() {
        fulldaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    viewFlipperFullDay.showNext();
                } else {
                    viewFlipperFullDay.showPrevious();
                }
            }
        });
    }

    /**
     * This method sets up the list views.
     */
    private void setupListViews() {
        initializeHashtags();
        adapter = new AddHashtagListAdapter(this, R.layout.layout_add_hashtag_adapter_view, hashtags);
        hashtagListView.setAdapter(adapter);
        adapter2 = new RemoveHashtagListAdapter(this, R.layout.layout_remove_hashtag_adapter_view, assignedHashtags);
        assignedHashtagListView.setAdapter(adapter2);
        hashtagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d(TAG, "onItemClick: clicked " + hashtagListView.getAdapter().getItem(position).toString());
                String currentItem = hashtagListView.getAdapter().getItem(position).toString();
                if(!assignedHashtags.contains(currentItem)) {
                    assignedHashtags.add(currentItem);
                    Collections.sort(assignedHashtags);
                }
                else
                    Toast.makeText(EditAppointmentActivity.this,"Hashtag bereits hinzugefügt",Toast.LENGTH_LONG).show();
                adapter2.notifyDataSetChanged();
            }
        });
        hashtagListView.setEmptyView(findViewById(R.id.addAppointmentListViewHashtagEmpty));
        assignedHashtagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d(TAG, "onItemClick: clicked " + assignedHashtagListView.getAdapter().getItem(position).toString());
                String currentItem = assignedHashtagListView.getAdapter().getItem(position).toString();
                assignedHashtags.remove(assignedHashtags.indexOf(currentItem));
                adapter2.notifyDataSetChanged();
            }
        });
        assignedHashtagListView.setEmptyView(findViewById(R.id.addAppointmentListViewAssignedHashtagEmpty));
    }

    /**
     * This method sets up the OnClickListeners for the TimePickers.
     */
    private void setupPickersOnClickListener() {
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditAppointmentActivity.this, startDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditAppointmentActivity.this, endDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(EditAppointmentActivity.this, startTimePicker, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(EditAppointmentActivity.this, endTimePicker, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });
    }

    /**
     * This method sets up the toolbar.
     */
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.editAppointmentToolbar);
        setSupportActionBar(toolbar);

        ImageView cancel = (ImageView) findViewById(R.id.editAppointmentCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder cancleEditDialogBuilder = new AlertDialog.Builder(context);
                cancleEditDialogBuilder.setCancelable(true);
                cancleEditDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                cancleEditDialogBuilder.setNegativeButton("Abbrechen",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Do nothing, just close the Dialog
                            }
                        });
                cancleEditDialogBuilder.setTitle("Änderungen verwerfen");
                cancleEditDialogBuilder.setMessage("Möchten Sie wirklich die Änderungen verwerfen?");
                AlertDialog cancleEditDialog = cancleEditDialogBuilder.create();
                cancleEditDialog.show();
            }
        });

        ImageView check = (ImageView) findViewById(R.id.editAppointmentOption);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "onClick: save edits of appointment option selected");
                //Log.d(TAG, "onClick: add appointment option selected");
                if (title.getText().toString().length() == 0 || location.getText().toString().length() == 0) {
                    if (title.getText().toString().length() == 0) {
                        title.setError("Eingabe eines Titels ist erforderlich", errorIcon);
                    }
                    if (location.getText().toString().length() == 0) {
                        location.setError("Eingabe eines Orts ist erforderlich", errorIcon);
                    }
                } else {
                    String titleString = title.getText().toString();
                    boolean fulldayBoolean = fulldaySwitch.isChecked();
                    String locationString = location.getText().toString();
                    String startDateString = startDate.getText().toString();
                    String endDateString = endDate.getText().toString();
                    String startTimeString = startTime.getText().toString();
                    String endTimeString = endTime.getText().toString();
                    //Log.d(TAG, "onClick: current assigned hashtags of edited appointment " + assignedHashtags.toString());

                    boolean insertData = database.updateEvent(eventId, titleString, fulldayBoolean, startDateString, endDateString, startTimeString, endTimeString, locationString, assignedHashtags);
                    int successState = 0;
                    if (insertData)
                        successState = 1;
                    if(fulldayBoolean == true){
                        database.deleteFulldayMatchingByEventId(eventId);
                        //insert new date
                        String currentDateString;
                        do{
                            currentDateString = simpleDateFormat.format(calendarStartDate.getTime());
                            //Log.d(TAG, "onClick: current Date to be added to fullday matching: " + currentDateString);
                            database.insertFullDayEvent(currentDateString, eventId);
                            calendarStartDate.add(Calendar.DAY_OF_MONTH,1);
                        }while(!currentDateString.contentEquals(endDateString));
                    }else if(startDateString.contains(simpleDateFormat.format(Calendar.getInstance().getTime()))){
                        if(calendarStartTime.after(Calendar.getInstance()))
                            ChiBaApplication.editAppointmentTimer(database.getEventIdByLastEvent(),startTimeString,assignedHashtags);
                    }

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("EXTRA_SUCCESS_STATE_EDIT_APPOINTMENT", successState);
                    context.startActivity(intent);
                }
            }
        });
    }

    /**
     * This method sets up the date and time pickers.
     */
    private void setupDateTimePickers() {
        startDatePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarStartDate.set(Calendar.YEAR, year);
                calendarStartDate.set(Calendar.MONTH, monthOfYear);
                calendarStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDate.setText(simpleDateFormat.format(calendarStartDate.getTime()));
                if (calendarStartDate.after(calendarEndDate)) {
                    calendarEndDate.set(Calendar.YEAR, year);
                    calendarEndDate.set(Calendar.MONTH, monthOfYear);
                    calendarEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    endDate.setText(simpleDateFormat.format(calendarEndDate.getTime()));
                }
            }
        };

        endDatePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarEndDate.set(Calendar.YEAR, year);
                calendarEndDate.set(Calendar.MONTH, monthOfYear);
                calendarEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endDate.setText(simpleDateFormat.format(calendarEndDate.getTime()));
                Log.d(TAG, "onDateSet: date selected " + simpleDateFormat.format(calendarEndDate.getTime()));
                Log.d(TAG, "onDateSet: current startdate " + simpleDateFormat.format(calendarStartDate.getTime()));
                if (calendarEndDate.before(calendarStartDate)) {
                    calendarStartDate.set(Calendar.YEAR, year);
                    calendarStartDate.set(Calendar.MONTH, monthOfYear);
                    calendarStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    Log.d(TAG, "onDateSet: date to set " + simpleDateFormat.format(calendarStartDate.getTime()));
                    startDate.setText(simpleDateFormat.format(calendarStartDate.getTime()));
                }
            }
        };

        startTimePicker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendarStartTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendarStartTime.set(Calendar.MINUTE, minute);
                startTime.setText(simpleTimeFormat.format(calendarStartTime.getTime()));
                if (calendarStartTime.after(calendarEndTime)) {
                    calendarEndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendarEndTime.set(Calendar.MINUTE, minute);
                    endTime.setText(simpleTimeFormat.format(calendarEndTime.getTime()));
                }
            }
        };

        endTimePicker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendarEndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendarEndTime.set(Calendar.MINUTE, minute);
                endTime.setText(simpleTimeFormat.format(calendarEndTime.getTime()));
                if (calendarEndTime.before(calendarStartTime)) {
                    calendarStartTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendarStartTime.set(Calendar.MINUTE, minute);
                    startTime.setText(simpleTimeFormat.format(calendarStartTime.getTime()));
                }
            }
        };
    }

    /**
     * This method initializes the hashtag lists.
     */
    private void initializeHashtags() {
        Cursor data = database.showHashtags();
        if (data.getCount() != 0) {
            String hashtag = "";
            while (data.moveToNext()) {
                hashtag += data.getString(1);
                hashtags.add(hashtag);
                hashtag = "";
            }
        }
        Collections.sort(hashtags);
        //Log.d(TAG, "initializeHashtags: hashtags:" + hashtags.toString());
    }
}
