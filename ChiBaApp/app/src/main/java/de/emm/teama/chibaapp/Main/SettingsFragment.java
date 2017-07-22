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
 * <h1>SettingsFragment Class</h1>
 * This class provides the calendar fragment.
 * <p>
 * In the comments find log entries to be used for debugging purposes.
 *
 * @author  Marjana Karzek
 * @version 1.0
 * @since   2017-06-18
 */
public class SettingsFragment extends Fragment {
    private static final String TAG = "SettingsFragment";

    private Calendar calendarOfToday = Calendar.getInstance();
    private Calendar birthdayCalendar = Calendar.getInstance();
    private Calendar startTimeCalendar = Calendar.getInstance();
    private Calendar endTimeCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener birthDatePicker;
    private TimePickerDialog.OnTimeSetListener notDisturbStartTimePicker;
    private TimePickerDialog.OnTimeSetListener notDisturbEndTimePicker;

    private String dateFormat = "d. MMMM yyyy";
    private String timeFormat = "kk:mm";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.GERMANY);
    private SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timeFormat, Locale.GERMANY);

    private View view;
    private final ViewHolderItem viewHolder = new ViewHolderItem();

    /**
     * This static class provides the items for the views to avoid reload.
     */
    static class ViewHolderItem {
        EditText name;
        TextView birthdate;
        EditText address;
        Switch avatarUse;
        Switch doNotDisturb;
        TextView notDisturbStartTime;
        TextView notDisturbEndTime;
        ViewFlipper viewFlipper;
    }

    /**
     * This method creates the view.
     *
     * @param inflater This parameter is used to get the inflater.
     * @param container This parameter is used to get the container.
     * @param savedInstanceState This parameter is used to get the save state from the dialog.
     * @return The method returns the view after it was modified.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_settings, container, false);

            Cursor data = database.showUser();
            viewHolder.name = (EditText) view.findViewById(R.id.settingsEditTextName);
            viewHolder.birthdate = (TextView) view.findViewById(R.id.settingsTextViewBirthdateSelection);
            viewHolder.address = (EditText) view.findViewById(R.id.settingsEditTextHomeaddress);
            viewHolder.avatarUse = (Switch) view.findViewById(R.id.settingsSwitchAvatarUse);
            viewHolder.doNotDisturb = (Switch) view.findViewById(R.id.settingsSwitchDoNotDisturb);
            viewHolder.notDisturbStartTime = (TextView) view.findViewById(R.id.settingsDoNotDisturbTextViewStartTime);
            viewHolder.notDisturbEndTime = (TextView) view.findViewById(R.id.settingsDoNotDisturbTextViewEndTime);
            viewHolder.viewFlipper = (ViewFlipper) view.findViewById(R.id.settingsViewFlipperDoNotDisturbTime);

            if (data.getCount() != 0 && data.moveToNext()) {
                viewHolder.name.setText(data.getString(1));
                viewHolder.birthdate.setText(data.getString(2));
                viewHolder.address.setText(data.getString(3));
                if (data.getString(4).contains("1") || data.getString(4).contains("true"))
                    viewHolder.avatarUse.setChecked(true);
                else
                    viewHolder.avatarUse.setChecked(false);
                if (data.getString(5).contains("1") || data.getString(5).contains("true")) {
                    viewHolder.doNotDisturb.setChecked(true);
                    viewHolder.viewFlipper.showNext();
                } else
                    viewHolder.doNotDisturb.setChecked(false);
                viewHolder.notDisturbStartTime.setText(data.getString(6));
                viewHolder.notDisturbEndTime.setText(data.getString(7));
            }

            birthdayCalendar.set(Calendar.YEAR, 1990);
            birthdayCalendar.set(Calendar.MONTH, 0);
            birthdayCalendar.set(Calendar.DAY_OF_MONTH, 1);

            birthDatePicker = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    birthdayCalendar.set(Calendar.YEAR, year);
                    birthdayCalendar.set(Calendar.MONTH, monthOfYear);
                    birthdayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    if (birthdayCalendar.after(calendarOfToday))
                        birthdayCalendar = Calendar.getInstance();
                    viewHolder.birthdate.setText(simpleDateFormat.format(birthdayCalendar.getTime()));
                    database.updateUserBirthdate(viewHolder.birthdate.getText().toString());
                }
            };

            startTimeCalendar.set(Calendar.MINUTE, 0);
            endTimeCalendar.set(Calendar.MINUTE, 0);

            notDisturbStartTimePicker = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    startTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    startTimeCalendar.set(Calendar.MINUTE, minute);
                    viewHolder.notDisturbStartTime.setText(simpleTimeFormat.format(startTimeCalendar.getTime()));
                    database.updateUserDoNotDisturbStartTime(viewHolder.notDisturbStartTime.getText().toString());
                }
            };

            notDisturbEndTimePicker = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    startTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    endTimeCalendar.set(Calendar.MINUTE, minute);
                    viewHolder.notDisturbEndTime.setText(simpleTimeFormat.format(endTimeCalendar.getTime()));
                    database.updateUserDoNotDisturbEndTime(viewHolder.notDisturbEndTime.getText().toString());
                }
            };

            setUpInPlaceEdit();
            setUpOnCheckedChangedListener();
            setUpDatePicker();
            setUpTimePickers();
        }
        return view;
    }

    /**
     * This method sets up the time picker.
     */
    private void setUpTimePickers() {
        viewHolder.notDisturbStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), notDisturbStartTimePicker, startTimeCalendar.get(Calendar.HOUR_OF_DAY), startTimeCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        viewHolder.notDisturbEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), notDisturbEndTimePicker, startTimeCalendar.get(Calendar.HOUR_OF_DAY), startTimeCalendar.get(Calendar.MINUTE), true).show();
            }
        });

    }

    /**
     * This method sets up the date picker.
     */
    private void setUpDatePicker() {
        viewHolder.birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), birthDatePicker, birthdayCalendar.get(Calendar.YEAR), birthdayCalendar.get(Calendar.MONTH), birthdayCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    /**
     * This method sets up the OnCheckedChangeListener for the switches.
     */
    private void setUpOnCheckedChangedListener() {
        viewHolder.doNotDisturb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    database.updateUserDoNotDisturb(true);
                    viewHolder.viewFlipper.showNext();
                } else {
                    database.updateUserDoNotDisturb(false);
                    viewHolder.viewFlipper.showPrevious();
                }
            }
        });

        viewHolder.avatarUse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    database.updateUserAvatarUse(true);
                } else {
                    database.updateUserAvatarUse(false);
                }
            }
        });
    }

    /**
     * This method makes in place edit available.
     */
    private void setUpInPlaceEdit() {
        viewHolder.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                database.updateUserName(viewHolder.name.getText().toString());
            }
        });

        viewHolder.address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                database.updateUserAddress(viewHolder.address.getText().toString());

            }
        });
    }
}
