package de.emm.teama.chibaapp.Appointment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
 * <h1>AddAppointmentActivity Class</h1>
 * This class provides the activity to add an appointment.
 * <p>
 * In the comments find log entries to be used for debugging purposes.
 *
 * @author  Marjana Karzek
 * @version 4.0
 * @since   2017-06-18
 */
public class AddAppointmentActivity extends AppCompatActivity {
    private static final String TAG = "AddAppointmentActivity";
    private Context context = AddAppointmentActivity.this;

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
    private String shortFormat = "kk";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.GERMANY);
    private SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timeFormat, Locale.GERMANY);
    private SimpleDateFormat shortTimeFormat = new SimpleDateFormat(shortFormat, Locale.GERMANY);

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

    private Drawable errorIcon;

    //Remaining Form Fields Initialization
    private EditText title;
    private EditText location;

    /**
     * This method creates the view.
     * @param savedInstanceState This parameter is used to get the saved instance state.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        Log.d(TAG, "onCreate: started");

        errorIcon = getDrawable(R.drawable.ic_error_message);
        errorIcon.setBounds(0, 0,errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());

        //Calendar Fields Initialization
        startDate = (TextView) findViewById(R.id.addAppointmentTextViewStartDate);
        endDate = (TextView) findViewById(R.id.addAppointmentTextViewEndDate);
        startDate.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));
        endDate.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));
        startTime = (TextView) findViewById(R.id.addAppointmentTextViewStartTime);
        endTime = (TextView) findViewById(R.id.addAppointmentTextViewEndTime);
        startTime.setText(shortTimeFormat.format(Calendar.getInstance().getTime()) + ":00");
        endTime.setText((Integer.valueOf(shortTimeFormat.format(Calendar.getInstance().getTime())) + 2) + ":00");

        //Switch Fields Initialization
        viewFlipperFullDay = (ViewFlipper) findViewById(R.id.addAppointmentViewFlipperFullDay);
        fulldaySwitch = (Switch) findViewById(R.id.addAppointmentSwitchFullday);

        //Hashtag List Fields Initialization
        hashtagListView = (ListView) findViewById(R.id.addAppointmentListViewHashtag);
        assignedHashtagListView = (ListView) findViewById(R.id.addAppointmentListViewAssignedHashtag);
        searchfield = (EditText) findViewById(R.id.addAppointmentEditTextSearchHashtag);

        //Setup Components
        setupToolbar();
        setupDateTimePickers();
        setupPickersOnClickListener();
        setupFullDaySwitch();
        setupListViews();
        setupSearchField();

        //Remaining Form Fields Initialization
        title = (EditText) findViewById(R.id.addAppointmentEditTextTitle);
        location = (EditText) findViewById(R.id.addAppointmentEditTextLocation);

        title.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        searchfield.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        location.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
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
                (AddAppointmentActivity.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                (AddAppointmentActivity.this).adapter.getFilter().filter(s);
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
                    Toast.makeText(AddAppointmentActivity.this,"Hashtag bereits hinzugefügt",Toast.LENGTH_LONG).show();
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
                new DatePickerDialog(AddAppointmentActivity.this, startDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddAppointmentActivity.this, endDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddAppointmentActivity.this, startTimePicker, calendar.get(Calendar.HOUR_OF_DAY), 0, true).show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddAppointmentActivity.this, endTimePicker, calendar.get(Calendar.HOUR_OF_DAY), 0, true).show();
            }
        });
    }

    /**
     * This method sets up the toolbar.
     */
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.addAppointmentToolbar);
        setSupportActionBar(toolbar);

        ImageView cancel = (ImageView) findViewById(R.id.addAppointmentCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "onClick: cancel add appointment");
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

        ImageView check = (ImageView) findViewById(R.id.addAppointmentOption);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "onClick: add appointment option selected");
                if (title.getText().toString().length() == 0 || location.getText().toString().length() == 0) {
                    if (title.getText().toString().length() == 0) {
                        title.setError("Eingabe eines Titels ist erforderlich",errorIcon);
                    }
                    if (location.getText().toString().length() == 0){
                        location.setError("Eingabe eines Orts ist erforderlich",errorIcon);
                    }
                } else {
                    String titleString = title.getText().toString();
                    boolean fulldayBoolean = fulldaySwitch.isChecked();
                    String locationString = location.getText().toString();
                    String startDateString = startDate.getText().toString();
                    String endDateString = endDate.getText().toString();
                    String startTimeString = startTime.getText().toString();
                    String endTimeString = endTime.getText().toString();
                    //Log.d(TAG, "onClick: assigned hashtag of new appointment " + assignedHashtags.toString());

                    boolean insertData = database.addEvent(titleString, fulldayBoolean, startDateString, endDateString, startTimeString, endTimeString, locationString, assignedHashtags);
                    int successState = 0;
                    if (insertData)
                        successState = 1;

                    if(fulldayBoolean == true){
                        int eventId = database.getEventIdByLastEvent();
                        String currentDateString;
                        do{
                            currentDateString = simpleDateFormat.format(calendarStartDate.getTime());
                            //Log.d(TAG, "onClick: current Date to be added to fullday matching: " + currentDateString);
                            database.insertFullDayEvent(currentDateString, eventId);
                            calendarStartDate.add(Calendar.DAY_OF_MONTH,1);
                        }while(!currentDateString.contentEquals(endDateString));
                    }
                    else if(startDateString.contains(simpleDateFormat.format(Calendar.getInstance().getTime()))){
                        //Log.d(TAG, "onClick: calling appointment schedule");
                        if(calendarStartTime.after(Calendar.getInstance()))
                            ChiBaApplication.addAppointmentTimer(database.getEventIdByLastEvent(),startTimeString,assignedHashtags);
                    }

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("EXTRA_SUCCESS_STATE_ADD_APPOINTMENT", successState);
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
                if (calendarEndDate.before(calendarStartDate)) {
                    calendarStartDate.set(Calendar.YEAR, year);
                    calendarStartDate.set(Calendar.MONTH, monthOfYear);
                    calendarStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
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
    }
}
