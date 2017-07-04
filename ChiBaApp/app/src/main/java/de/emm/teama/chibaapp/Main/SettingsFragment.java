package de.emm.teama.chibaapp.Main;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewFlipper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.emm.teama.chibaapp.Appointment.AddAppointmentActivity;
import de.emm.teama.chibaapp.R;

import static de.emm.teama.chibaapp.Application.ChiBaApplication.database;

/**
 * Created by Marjana Karzek on 18.06.2017.
 */

public class SettingsFragment extends Fragment {
    private static final String TAG = "SettingsFragment";

    private EditText name;
    private TextView birthdate;
    private EditText address;

    private Switch avatarUse;
    private Switch doNotDisturb;
    private TextView notDisturbStartTime;
    private TextView notDisturbEndTime;

    private ViewFlipper viewFlipper;
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener birthDatePicker;
    private TimePickerDialog.OnTimeSetListener notDisturbStartTimePicker;
    private TimePickerDialog.OnTimeSetListener notDisturbEndTimePicker;

    private String dateFormat = "d. MMMM yyyy";
    private String timeFormat = "kk:mm";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.GERMANY);
    private SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timeFormat, Locale.GERMANY);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Cursor data = database.showUser();

        name = (EditText) view.findViewById(R.id.settingsEditTextName);
        birthdate = (TextView) view.findViewById(R.id.settingsTextViewBirthdateSelection);
        address = (EditText) view.findViewById(R.id.settingsEditTextHomeaddress);
        avatarUse = (Switch) view.findViewById(R.id.settingsSwitchAvatarUse);
        doNotDisturb = (Switch) view.findViewById(R.id.settingsSwitchDoNotDisturb);
        notDisturbStartTime = (TextView) view.findViewById(R.id.settingsDoNotDisturbTextViewStartTime);
        notDisturbEndTime = (TextView) view.findViewById(R.id.settingsDoNotDisturbTextViewEndTime);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.settingsViewFlipperDoNotDisturbTime);

        //Log.d(TAG, "onCreateView: data count: " + data.getCount());
        if (data.getCount() != 0 && data.moveToNext()) {
            name.setText(data.getString(1));
            birthdate.setText(data.getString(2));
            address.setText(data.getString(3));
            if(data.getString(4).contains("1") || data.getString(4).contains("true"))
                avatarUse.setChecked(true);
            else
                avatarUse.setChecked(false);
            if(data.getString(5).contains("1") || data.getString(5).contains("true")) {
                doNotDisturb.setChecked(true);
                viewFlipper.showNext();
            }
            else
                doNotDisturb.setChecked(false);
            notDisturbStartTime.setText(data.getString(6));
            notDisturbEndTime.setText(data.getString(7));
        }

        birthDatePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                birthdate.setText(simpleDateFormat.format(calendar.getTime()));
                database.updateUserBirthdate(birthdate.getText().toString());
            }
        };

        notDisturbStartTimePicker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                notDisturbStartTime.setText(simpleTimeFormat.format(calendar.getTime()));
                database.updateUserDoNotDisturbStartTime(notDisturbStartTime.getText().toString());
            }
        };

        notDisturbEndTimePicker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                notDisturbEndTime.setText(simpleTimeFormat.format(calendar.getTime()));
                database.updateUserDoNotDisturbEndTime(notDisturbEndTime.getText().toString());
            }
        };

        setUpInPlaceEdit();
        setUpOnCheckedChangedListener();
        setUpDatePicker();
        setUpTimePickers();
        return view;
    }

    private void setUpTimePickers() {
        notDisturbStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), notDisturbStartTimePicker, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        notDisturbEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), notDisturbEndTimePicker, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

    }

    private void setUpDatePicker() {

        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), birthDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void setUpOnCheckedChangedListener() {
        doNotDisturb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    database.updateUserDoNotDisturb(true);
                    viewFlipper.showNext();
                }
                else{
                    database.updateUserDoNotDisturb(false);
                    viewFlipper.showPrevious();
                }
            }
        });

        avatarUse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    database.updateUserAvatarUse(true);
                }
                else{
                    database.updateUserAvatarUse(false);
                }
            }
        });
    }

    private void setUpInPlaceEdit() {
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                database.updateUserName(name.getText().toString());
            }
        });

       address.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {
               database.updateUserAddress(address.getText().toString());

           }
       });
    }
}
