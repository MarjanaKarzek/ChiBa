package de.emm.teama.chibaapp.AddAppointment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import java.util.Locale;

import de.emm.teama.chibaapp.Main.MainActivity;
import de.emm.teama.chibaapp.R;
import de.emm.teama.chibaapp.Utils.AddHashtagListAdapter;
import de.emm.teama.chibaapp.Utils.RemoveHashtagListAdapter;

/**
 * Created by Marjana Karzek on 18.06.2017.
 */

public class AddAppointmentActivity extends AppCompatActivity{
    private static final String TAG = "AddAppointmentActivity";
    private Context context = AddAppointmentActivity.this;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        Log.d(TAG, "onCreate: started");

        //Calendar Fields Initialization
        startDate = (TextView) findViewById(R.id.addAppointmentTextViewStartDate);
        endDate = (TextView) findViewById(R.id.addAppointmentTextViewEndDate);
        startTime = (TextView) findViewById(R.id.addAppointmentTextViewStartTime);
        endTime = (TextView) findViewById(R.id.addAppointmentTextViewEndTime);

        //Switch Fields Initialization
        viewFlipperFullDay = (ViewFlipper) findViewById( R.id.addAppointmentViewFlipperFullDay );
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
    }

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
    }

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
                new TimePickerDialog(AddAppointmentActivity.this, startTimePicker, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddAppointmentActivity.this, endTimePicker, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.addAppointmentToolbar);
        setSupportActionBar(toolbar);

        ImageView cancel = (ImageView) findViewById(R.id.addAppointmentCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: cancel add appointment");
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
        
        ImageView check = (ImageView) findViewById(R.id.addAppointmentOption);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: add appointment option selected");
                Intent intent = new Intent(context, MainActivity.class);
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
        hashtags.add("Fitness");
        hashtags.add("Kochen");
        hashtags.add("Einkaufen");
        hashtags.add("Arzt");
        hashtags.add("Haushalt");
        hashtags.add("Fitness");
        hashtags.add("Kochen");
        hashtags.add("Einkaufen");
        hashtags.add("Arzt");
        hashtags.add("Haushalt");
        hashtags.add("Fitness");
        hashtags.add("Kochen");
        hashtags.add("Einkaufen");
        hashtags.add("Arzt");
        hashtags.add("Haushalt");
        hashtags.add("Fitness");
        hashtags.add("Kochen");
        hashtags.add("Einkaufen");
        hashtags.add("Arzt");
        hashtags.add("Haushalt");
        hashtags.add("Fitness");
        hashtags.add("Kochen");
        hashtags.add("Einkaufen");
        hashtags.add("Arzt");
        hashtags.add("Haushalt");
        hashtags.add("Fitness");
        hashtags.add("Kochen");
        hashtags.add("Einkaufen");
        hashtags.add("Arzt");
        hashtags.add("Haushalt");
        hashtags.add("Fitness");
        hashtags.add("Kochen");
        hashtags.add("Einkaufen");
        hashtags.add("Arzt");
        hashtags.add("Haushalt");
        assignedHashtags.add("Assigend Hashtag");
    }
}
