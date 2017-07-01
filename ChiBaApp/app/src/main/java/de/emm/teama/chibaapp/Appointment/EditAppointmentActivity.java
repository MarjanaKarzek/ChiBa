package de.emm.teama.chibaapp.Appointment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
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
import android.widget.ViewFlipper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import de.emm.teama.chibaapp.Main.MainActivity;
import de.emm.teama.chibaapp.R;
import de.emm.teama.chibaapp.Utils.AddHashtagListAdapter;
import de.emm.teama.chibaapp.Utils.RemoveHashtagListAdapter;

import static de.emm.teama.chibaapp.Main.MainActivity.database;

/**
 * Created by Marjana Karzek on 18.06.2017.
 */

public class EditAppointmentActivity extends AppCompatActivity{
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

    //Database Fields
    //private DatabaseHelper database;

    //Remaining Form Fields Initialization
    private EditText title;
    private EditText location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment);
        Log.d(TAG, "onCreate: started");
        eventId = getIntent().getIntExtra("EXTRA_EVENT_ID",-1);

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
                currentLocation= data.getString(7);
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
        viewFlipperFullDay = (ViewFlipper) findViewById( R.id.addAppointmentViewFlipperFullDay );
        fulldaySwitch = (Switch) findViewById(R.id.addAppointmentSwitchFullday);
        if(currentFullDayState != "0") {
            fulldaySwitch.setChecked(true);
            viewFlipperFullDay.showNext();
        }
        else
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
        for(String hashtag: currentAssignedHashtags){
            hashtags.remove(hashtag);
        }
    }

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

    private void setupFullDaySwitch() {
        fulldaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    viewFlipperFullDay.showNext();
                }
                else{
                    viewFlipperFullDay.showPrevious();
                }
            }
        });
    }

    private void setupListViews() {
        initializeHashtags();
        adapter = new AddHashtagListAdapter(this, R.layout.layout_add_hashtag_adapter_view, hashtags);
        hashtagListView.setAdapter(adapter);
        adapter2 = new RemoveHashtagListAdapter(this, R.layout.layout_remove_hashtag_adapter_view, assignedHashtags);
        assignedHashtagListView.setAdapter(adapter2);
        hashtagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: clicked " + hashtags.get(position));
                String currentItem = hashtags.get(position);
                hashtags.remove(position);
                assignedHashtags.add(currentItem);
                Collections.sort(assignedHashtags);
                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            }
        });
        hashtagListView.setEmptyView( findViewById( R.id.addAppointmentListViewHashtagEmpty) );
        assignedHashtagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: clicked " + assignedHashtags.get(position));
                String currentItem = assignedHashtags.get(position);
                assignedHashtags.remove(position);
                hashtags.add(currentItem);
                Collections.sort(hashtags);
                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            }
        });
        assignedHashtagListView.setEmptyView(findViewById(R.id.addAppointmentListViewAssignedHashtagEmpty));
    }

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

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.editAppointmentToolbar);
        setSupportActionBar(toolbar);

        ImageView cancel = (ImageView) findViewById(R.id.editAppointmentCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: cancel add appointment");
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
        
        ImageView check = (ImageView) findViewById(R.id.editAppointmentOption);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: add appointment option selected");

                String titleString = title.getText().toString();
                boolean fulldayBoolean = fulldaySwitch.isChecked();
                String locationString = location.getText().toString();
                String startDateString = startDate.getText().toString();
                String endDateString = endDate.getText().toString();
                String startTimeString = startTime.getText().toString();
                String endTimeString = endTime.getText().toString();

                boolean insertData = database.updateEvent(eventId, titleString,fulldayBoolean,startDateString,endDateString,startTimeString,endTimeString,locationString,assignedHashtags);
                int successState = 0;
                if(insertData)
                    successState = 1;

                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("EXTRA_SUCCESS_STATE", successState);
                context.startActivity(intent);

            }
        });
    }

    private void setupDateTimePickers(){
        startDatePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        endDatePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        startTimePicker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                startTime.setText(simpleTimeFormat.format(calendar.getTime()));
            }
        };

        endTimePicker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                endTime.setText(simpleTimeFormat.format(calendar.getTime()));
            }
        };
    }

    private void initializeHashtags(){
        Cursor data = database.showHashtags();
        if(data.getCount() != 0) {
            String hashtag = "";
            while (data.moveToNext()) {
                hashtag += data.getString(1);
                hashtags.add(hashtag);
                hashtag = "";
            }
        }
        Collections.sort(hashtags);
        Log.d(TAG, "initializeHashtags: hashtags:" + hashtags.toString());
    }
}
